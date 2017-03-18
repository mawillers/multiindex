package com.github.mawillers.multiindex;

import java.util.ArrayList;
import java.util.Iterator;

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
        return m_index.iterator();
    }

    @Override
    public String toString()
    {
        return "ArrayListIndex: " + m_index.toString();
    }
}
