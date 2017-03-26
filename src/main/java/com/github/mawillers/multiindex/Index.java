package com.github.mawillers.multiindex;

import java.util.Collection;

/**
 * An index into the MultiIndexContainer.
 *
 * @param <V> the type of elements in this index
 */
public interface Index<V>
{
    /**
     * Adds the specified value to the container.
     * <p>
     * An index might place restrictions on what values can be added to it. In particular, certain indexes have uniqueness constrains that prevent certain
     * values from being added. If any of the indexes rejects this value, this method returns false.
     *
     * @param value the value to add
     * @return true if value has been added to the container
     */
    public boolean add(V value);

    /**
     * Appends all of the elements in the specified collection to this index.
     *
     * @param values collection containing elements to be added to this index
     * @return true if value has been added to the container
     */
    public boolean addAll(Collection<? extends V> values);

    /**
     * Removes all of the elements from the container.
     */
    public void clear();

    /**
     * Returns true if the container contains no elements.
     *
     * @return true if the container contains no elements
     */
    public boolean isEmpty();

    /**
     * Returns the number of elements in the container.
     *
     * @return the number of elements in the container
     */
    public int size();
}
