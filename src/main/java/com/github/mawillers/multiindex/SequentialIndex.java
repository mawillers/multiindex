package com.github.mawillers.multiindex;

/**
 * An index that allows sequential access to its values in insertion order.
 *
 * @param <V> the type of elements in this index
 */
public interface SequentialIndex<V> extends Index<V>, Iterable<V>
{
    /**
     * Removes the first occurrence of the specified value from this index.
     *
     * @param value the value
     * @return true if the value has been removed
     */
    public boolean remove(Object value);

    /**
     * Returns true if this index contains the specified value.
     *
     * @param value the value
     * @return true if this index contains the specified value
     */
    public boolean contains(Object value);
}
