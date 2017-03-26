package com.github.mawillers.multiindex;

import java.util.Optional;

/**
 * An index that allows access to its values by means of a key.
 *
 * @param <K> the type of key
 * @param <V> the type of elements in this index
 */
public interface UniqueIndex<K, V> extends Index<V>
{
    /**
     * Removes the value that has the specified key in this index.
     *
     * @param key the key
     * @return the previous value associated with key, or null if there was no mapping for key
     */
    public V remove(Object key);

    /**
     * Returns true if the index contains a mapping for the specified key.
     *
     * @param key the key
     * @return true if the index contains a mapping for the specified key
     */
    public boolean containsKey(Object key);

    /**
     * Returns true if this index maps one or more keys to the specified value.
     *
     * @param value the value
     * @return true if this index maps one or more keys to the specified value
     */
    public boolean containsValue(Object value);

    /**
     * Returns the value that is associated with the specified key, if present.
     *
     * @param key the key
     * @return the value that is associated with the specified key
     */
    public Optional<V> getOptional(K key);
}
