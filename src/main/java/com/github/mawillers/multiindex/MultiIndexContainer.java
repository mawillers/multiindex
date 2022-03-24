package com.github.mawillers.multiindex;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.function.Function;

/**
 * A container class with dynamic indexes.
 * <p>
 * This container stores values of a given type, and allows retrieval of these values by an arbitrary number of indexes.
 * <p>
 * Currently, two index types have been implemented, these are:
 * <ul>
 * <li>{@link SequentialIndex} - an index that allows sequential access to its values in insertion order
 * <li>{@link UniqueIndex} - an index that allows access to its values by means of a key
 * </ul>
 * <p>
 * The container itself does not hold any data - all data is contained in the indexes. All indexes must be created before data can be put into the container
 * (this avoids potentially large overhead when a newly created index must first copy all data into itself).
 *
 * <p>
 * Note: as with standard Java Map or Set data structures, great care must be exercised if mutable objects are put into the container. The behavior of a keyed
 * index (such as UniqueIndex) is not specified if the value of an object is changed in a manner that affects equals comparisons while the object is a key in
 * any of the indexes. Since multiple indexes can be active, with each index using another field of the mutable object as key, it can be difficult to determine
 * which fields are safe to modify. It is best to only put immutable objects into the container, or at least not modify any object after it has been put into
 * the container.
 *
 * <p>
 * Typical use cases for this container are:
 * <ul>
 * <li>a HashMap with defined iteration order (a replacement for the JDK's {@link LinkedHashMap}). For instance:
 * <!-- @formatter:off -->
 * <pre>{@code
 *   MultiIndexContainer<Employee> container = MultiIndexContainer.create();
 *   SequentialIndex<Employee> bySequence = container.createSequentialIndex(); // contains elements in insertion order
 *   UniqueIndex<Integer, Employee> byId = container.createHashedUniqueIndex(e -> e.getId()); // allows fast lookup via hash map
 * }</pre>
 * <!-- @formatter:on -->
 *
 * <li>Fast lookup of values by both key and value (a replacement for Guava's {@link com.google.common.collect.BiMap}). For instance:
 * <!-- @formatter:off -->
 * <pre>{@code
 *   MultiIndexContainer<Employee> container = MultiIndexContainer.create();
 *   UniqueIndex<Integer, Employee> byKey = container.createHashedUniqueIndex(e -> e.getId()); // lookup via key
 *   UniqueIndex<Employee, Employee> byValue = container.createHashedUniqueIndex(e -> e); // lookup via value
 * }</pre>
 * <!-- @formatter:on -->
 *
 * <li>Fast lookup of values by different keys. For instance:
 * <!-- @formatter:off -->
 * <pre>{@code
 *   MultiIndexContainer<Employee> container = MultiIndexContainer.create();
 *   UniqueIndex<String, Employee> bySSN = container.createHashedUniqueIndex(e -> e.getSSN()); // lookup via Social Security Number
 *   UniqueIndex<Integer, Employee> byId = container.createHashedUniqueIndex(e -> e.getId()); // lookup via user ID
 * }</pre>
 * <!-- @formatter:on -->
 * </ul>
 * <p>
 * Putting values into the container can be done via any of the indexes. If any of the added values violates an index's constraints (for instance,
 * a value is being added whose one field has the same content as another value's one, and this field is defined by a UniqueIndex as a key), then
 * addition of this value fails. The {@link Index#add(Object)} method returns false in this case.
 *
 * <p>
 * <strong>Note that this implementation is not synchronized.</strong> If multiple threads access a MultiIndexContainer or any Index instance
 * concurrently, and at least one of the threads modifies the container or any index structurally, it <i>must</i> be synchronized externally.
 *
 * @param <V> the type that this Container contains
 */
public final class MultiIndexContainer<V>
{
    /**
     * This interface is to be implemented by all index implementations.
     *
     * @param <V> the type that this container contains
     */
    interface InternalIndex<V> extends Index<V>
    {
        boolean canAddInternal(V value);

        void addInternal(V value);

        boolean removeInternal(V value);

        void clearInternal();
    }

    private final ArrayList<InternalIndex<V>> m_indexes = new ArrayList<>();

    private MultiIndexContainer()
    {
        // Nothing to do, but make this constructor private so that the factory method is used instead.
    }

    // --------------------------------------------------------------------

    boolean addToAllIndexes(V value)
    {
        final boolean canAdd = m_indexes.stream().allMatch(index -> index.canAddInternal(value));
        if (canAdd)
            m_indexes.forEach(idx -> idx.addInternal(value));
        return canAdd;
    }

    boolean addAllToAllIndexes(Collection<? extends V> values)
    {
        final boolean atLeastOneValueWasAdded = values.stream() //
            .map(value -> addToAllIndexes(value)) //
            .reduce(false, (result, element) -> result | element);
        return atLeastOneValueWasAdded;
    }

    void removeFromAllIndexes(Index<V> except, V value)
    {
        checkNotNull(except);

        m_indexes.stream() //
            .filter(idx -> idx != except) // this index instance has already handled itself
            .forEach(idx -> idx.removeInternal(value));
    }

    void clearAllIndexes()
    {
        m_indexes.forEach(idx -> idx.clearInternal());
    }

    // --------------------------------------------------------------------

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
    public SequentialIndex<V> createSequentialIndex()
    {
        checkState(m_indexes.stream().findFirst().map(idx -> idx.size()).orElse(0) == 0, "must create all indexes before putting data into the container");

        final ArrayListIndex<V> index = new ArrayListIndex<>(this);
        m_indexes.add(index);
        return index;
    }

    /**
     * Creates a new UniqueIndex that is based on hashing keys.
     *
     * @param keyExtractor a function defining which key to use for the values
     * @return the new index, never null
     * @param <K> the type of key
     */
    public <K> UniqueIndex<K, V> createHashedUniqueIndex(Function<V, K> keyExtractor)
    {
        checkNotNull(keyExtractor, "Key extractor argument was null but expected non-null");
        checkState(m_indexes.stream().findFirst().map(idx -> idx.size()).orElse(0) == 0, "must create all indexes before putting data into the container");

        final HashMapIndex<K, V> index = new HashMapIndex<>(this, keyExtractor);
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
