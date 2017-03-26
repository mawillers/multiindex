package com.github.mawillers.multiindex;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;

/**
 * An implementation of {@link UniqueIndex} that uses a HashMap for storage.
 *
 * @param <K> the type of key
 * @param <V> the type of elements in this index
 */
final class HashMapIndex<K, V> implements UniqueIndex<K, V>, MultiIndexContainer.InternalIndex<V>
{
    private final HashMap<K, V> m_index = new HashMap<>();
    private final MultiIndexContainer<V> m_container;
    private final Function<V, K> m_keyExtractor;

    HashMapIndex(MultiIndexContainer<V> container, Function<V, K> keyExtractor)
    {
        m_container = container;
        m_keyExtractor = keyExtractor;
    }

    // --------------------------------------------------------------------

    @Override
    public boolean canAddInternal(V value)
    {
        if (value == null) {
            // Cannot extract a key from a null value.
            return false;
        }

        final K key = m_keyExtractor.apply(value);
        final boolean isContained = m_index.containsKey(key);
        // When a value is already associated with this key, we cannot add this new value.
        return !isContained;
    }

    @Override
    public void addInternal(V value)
    {
        // null-check of value has already been done in canAddInternal(), which always has been called before.
        checkNotNull(value, "Value argument was null but expected non-null");

        final K key = m_keyExtractor.apply(value);
        m_index.put(key, value);
    }

    @Override
    public boolean removeInternal(V value)
    {
        if (value == null) {
            // Cannot extract a key from a null value.
            return false;
        }

        final K key = m_keyExtractor.apply(value);
        return m_index.remove(key, value);
    }

    @Override
    public void clearInternal()
    {
        m_index.clear();
    }

    // --------------------------------------------------------------------

    @Override
    public boolean add(V value)
    {
        return m_container.addToAllIndexes(value);
    }

    @Override
    public boolean addAll(Collection<? extends V> values)
    {
        return m_container.addAllToAllIndexes(values);
    }

    @Override
    public V remove(Object key)
    {
        final V valueToRemove = m_index.remove(key);

        m_container.removeFromAllIndexes(this, valueToRemove);

        return valueToRemove;
    }

    @Override
    public void clear()
    {
        m_container.clearAllIndexes();
    }

    // --------------------------------------------------------------------

    @Override
    public boolean isEmpty()
    {
        return m_index.isEmpty();
    }

    @Override
    public int size()
    {
        return m_index.size();
    }

    @Override
    public boolean containsKey(Object key)
    {
        return m_index.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value)
    {
        return m_index.containsValue(value);
    }

    @Override
    public Optional<V> getOptional(K key)
    {
        final V value = m_index.get(key);
        // Since null values are not supported, a null result from the Map lookup can safely be taken as non-existence.
        return Optional.ofNullable(value);
    }

    @Override
    public String toString()
    {
        return "HashMapIndex: " + m_index;
    }
}
