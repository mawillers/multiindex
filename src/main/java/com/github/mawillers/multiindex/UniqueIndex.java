package com.github.mawillers.multiindex;

/**
 * An index that allows access to its values by means of a key.
 *
 * @param <K> the type of key
 * @param <V> the type of elements in this index
 */
public interface UniqueIndex<K, V> extends Index<V>
{
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
}
