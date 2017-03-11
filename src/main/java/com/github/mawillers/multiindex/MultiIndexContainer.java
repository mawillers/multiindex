package com.github.mawillers.multiindex;

/**
 * A container class with dynamic indexes.
 * <p>
 * This container stores values of a given type, and allows retrieval of these values by an arbitrary number of indexes.
 *
 * @param <V> the type that this Container contains
 */
public final class MultiIndexContainer<V>
{
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
     * Creates a new sequential index.
     *
     * @return the new index, never null
     */
    public Index<V> createSequentialIndex()
    {
        final ArrayListIndex<V> index = new ArrayListIndex<>();
        return index;
    }
}
