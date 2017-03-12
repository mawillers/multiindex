package com.github.mawillers.multiindex;

/**
 * An index that allows sequential access to its values in insertion order.
 *
 * @param <V> the type of elements in this index
 */
public interface SequentialIndex<V> extends Index<V>, Iterable<V>
{
}
