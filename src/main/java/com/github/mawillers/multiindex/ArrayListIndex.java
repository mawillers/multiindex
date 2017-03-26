package com.github.mawillers.multiindex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

import com.google.common.collect.ForwardingIterator;

/**
 * An implementation of {@link SequentialIndex} that uses an ArrayList for storage.
 *
 * @param <V> the type of elements in this index
 */
final class ArrayListIndex<V> implements SequentialIndex<V>, MultiIndexContainer.InternalIndex<V>
{
    private final ArrayList<V> m_index = new ArrayList<>();
    private final MultiIndexContainer<V> m_container;

    ArrayListIndex(MultiIndexContainer<V> container)
    {
        m_container = container;
    }

    @SuppressWarnings("unchecked")
    private static <T> T cast(Object object)
    {
        return (T) object;
    }

    // --------------------------------------------------------------------

    @Override
    public boolean canAddInternal(V value)
    {
        return true;
    }

    // When these methods are called, all necessary checks have already been done, and we really only need to modify our local data.

    @Override
    public void addInternal(V value)
    {
        m_index.add(value);
    }

    @Override
    public boolean removeInternal(V value)
    {
        return m_index.remove(value);
    }

    @Override
    public void clearInternal()
    {
        m_index.clear();
    }

    // --------------------------------------------------------------------
    // In the following implementations, must make sure that the call is propagated to all other existing indexes as well.

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
    public boolean remove(Object object)
    {
        try {
            // This cast is unchecked here, but checks will be done inside those indexes' removeInternal() methods where the value is actually used as a 'V'.
            final V value = cast(object);
            final boolean wasRemoved = removeInternal(value);

            m_container.removeFromAllIndexes(this, value);

            return wasRemoved;
        } catch (ClassCastException ex) {
            return false;
        }
    }

    @Override
    public void clear()
    {
        m_container.clearAllIndexes();
    }

    // --------------------------------------------------------------------
    // Query methods. These can easily be satisfied from our local data alone.

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
    public boolean contains(Object value)
    {
        return m_index.contains(value);
    }

    @Override
    public Iterator<V> iterator()
    {
        return new ForwardingIterator<V>() {
            private final Iterator<V> m_delegate = m_index.iterator();

            @Override
            protected Iterator<V> delegate()
            {
                return m_delegate;
            }

            @Override
            public void remove()
            {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(m_container, m_index);
    }

    /**
     * Compares the specified object with this Index for equality.
     * <p>
     * Returns true if and only if the specified object is also an ArrayListIndex, both Indexes originate from the same MultiIndexContainer instance, and all
     * corresponding pairs of elements in the two Indexes are equal.
     *
     * @param o the object to be compared for equality with this Index
     * @return true if the specified object is equal to this Index
     */
    @Override
    public boolean equals(Object o)
    {
        if (o == this)
            return true;
        if (!(o instanceof ArrayListIndex))
            return false;

        final ArrayListIndex<?> other = (ArrayListIndex<?>) o;
        return Objects.equals(m_container, other.m_container) //
            && Objects.equals(m_index, other.m_index);
    }

    @Override
    public String toString()
    {
        return "ArrayListIndex: " + m_index.toString();
    }
}
