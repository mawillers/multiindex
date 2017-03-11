package com.github.mawillers.multiindex;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A container class with dynamic indexes.
 * <p>
 * This container stores values of a given type, and allows retrieval of these values by an arbitrary number of indexes.
 *
 * @param <V> the type that this Container contains
 */
public final class MultiIndexContainer<V>
{
    private final ArrayList<Index<V>> m_indexes = new ArrayList<>();

    private MultiIndexContainer()
    {
        // Nothing to do, but make this constructor private so that the factory method is used instead.
    }

    /**
     * Creates a new instance.
     *
     * @return the new instance, never null
     * @param <V> the type that the new container is to contain
     */
    public static <V> MultiIndexContainer<V> create()
    {
        return new MultiIndexContainer<>();
    }

    /**
     * Returns an Iterable with all indexes known by this container instance.
     *
     * @return an Iterable
     */
    public Iterable<Index<V>> indexes()
    {
        return Collections.unmodifiableList(m_indexes);
    }

    /**
     * Creates a new sequential index.
     *
     * @return the new index, never null
     */
    public Index<V> createSequentialIndex()
    {
        final ArrayListIndex<V> index = new ArrayListIndex<>();
        m_indexes.add(index);
        return index;
    }

    /**
     * Removes the specified index from this container.
     *
     * @param index the index, must not be null
     */
    public void removeIndex(Index<V> index)
    {
        checkNotNull(index, "Index argument was null but expected non-null");

        m_indexes.remove(index);
    }
}
