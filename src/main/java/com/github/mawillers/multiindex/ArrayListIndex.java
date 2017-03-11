package com.github.mawillers.multiindex;

import java.util.ArrayList;

/**
 * An implementation of {@link Index} that uses an ArrayList for storage.
 *
 * @param <V> the type of elements in this index
 */
final class ArrayListIndex<V> implements Index<V>
{
    private final ArrayList<V> m_index = new ArrayList<>();

    ArrayListIndex()
    {
    }

    @Override
    public boolean add(V value)
    {
        return m_index.add(value);
    }

    @Override
    public void clear()
    {
        m_index.clear();
    }

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
    public String toString()
    {
        return "ArrayListIndex: " + m_index.toString();
    }
}
