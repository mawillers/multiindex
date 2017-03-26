package com.github.mawillers.multiindex;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

@SuppressWarnings("javadoc")
public final class MultiIndexContainerTest
{
    private MultiIndexContainer<Employee> m_multiIndexContainer;

    @Rule
    public ExpectedException m_exception = ExpectedException.none();

    @Before
    public void setup()
    {
        m_multiIndexContainer = MultiIndexContainer.create();
    }

    @Test
    public void testCreation()
    {
        assertThat(m_multiIndexContainer, is(not(nullValue())));
        final Index<Employee> index = m_multiIndexContainer.createSequentialIndex();
        assertThat(index, is(notNullValue()));
    }

    @Test
    public void twoEmptyContainersAreDifferent()
    {
        final MultiIndexContainer<Employee> container2 = MultiIndexContainer.create();
        // This class does not override equals() and hashCode(), so different instances are always non-equal.
        assertThat(m_multiIndexContainer.equals(container2), is(false));
    }

    @Test
    public void testIteration()
    {
        assertThat(m_multiIndexContainer.indexes(), is(emptyIterable()));

        m_multiIndexContainer.createSequentialIndex();
        assertThat(m_multiIndexContainer.indexes(), is(iterableWithSize(1)));
    }

    @Test
    public void testIndexRemoval()
    {
        final Index<Employee> index = m_multiIndexContainer.createSequentialIndex();

        m_multiIndexContainer.removeIndex(index);
        assertThat(m_multiIndexContainer.indexes(), is(emptyIterable()));
    }

    @Test
    public void testTwoSequentialIndexesShouldHaveTheSameContent()
    {
        final SequentialIndex<Employee> seq1 = m_multiIndexContainer.createSequentialIndex();
        final SequentialIndex<Employee> seq2 = m_multiIndexContainer.createSequentialIndex();

        seq1.add(TD.m_data1);
        assertThat(seq2, contains(TD.m_data1));
    }

    @Test
    public void testAddTwiceViolatesUniquenessConstraint()
    {
        final SequentialIndex<Employee> seq = m_multiIndexContainer.createSequentialIndex();
        final UniqueIndex<Integer, Employee> byId = m_multiIndexContainer.createHashedUniqueIndex(e -> e.m_id);

        seq.add(TD.m_data1);
        boolean isAdded = seq.add(TD.m_data1);
        assertThat(isAdded, is(false));
        assertThat(seq, contains(TD.m_data1));
        assertThat(byId.size(), is(1));
    }

    @Test
    public void testAddNullValueFailsWithUniqueIndex()
    {
        final SequentialIndex<Employee> seq = m_multiIndexContainer.createSequentialIndex();
        final UniqueIndex<Integer, Employee> byId = m_multiIndexContainer.createHashedUniqueIndex(e -> e.m_id);

        seq.add(TD.m_data1);
        seq.add(TD.m_data1);

        final boolean isAdded = seq.add(null);
        assertThat(isAdded, is(false));
        assertThat(seq, contains(TD.m_data1));
        assertThat(byId.size(), is(1));
    }

    @Test
    public void testRemovalFromUniqueIndex()
    {
        final SequentialIndex<Employee> seq = m_multiIndexContainer.createSequentialIndex();
        final UniqueIndex<Integer, Employee> byId = m_multiIndexContainer.createHashedUniqueIndex(e -> e.m_id);

        seq.add(TD.m_data1);
        seq.add(TD.m_data2);
        seq.add(TD.m_data3);

        Employee removed = byId.remove(42);
        assertThat(removed, is(nullValue()));
        removed = byId.remove(2);
        assertThat(removed, is(sameInstance(TD.m_data2)));
        assertThat(seq, contains(TD.m_data1, TD.m_data3));
        removed = byId.remove(1);
        assertThat(removed, is(sameInstance(TD.m_data1)));
        assertThat(seq, contains(TD.m_data3));
    }

    @Test
    public void testRemoveWrongType()
    {
        final SequentialIndex<Employee> seq = m_multiIndexContainer.createSequentialIndex();
        final UniqueIndex<Integer, Employee> byId = m_multiIndexContainer.createHashedUniqueIndex(e -> e.m_id);
        seq.add(TD.m_data1);

        final Object obj = new Object();
        final boolean isRemoved = seq.remove(obj);
        assertThat(isRemoved, is(false));
        assertThat(seq, contains(TD.m_data1));
        assertThat(byId.size(), is(1));

        final Employee removed = byId.remove(obj);
        assertThat(removed, is(nullValue()));
        assertThat(seq, contains(TD.m_data1));
        assertThat(byId.size(), is(1));
    }

    @Test
    public void testRemoveExisting()
    {
        final SequentialIndex<Employee> seq = m_multiIndexContainer.createSequentialIndex();
        final UniqueIndex<Integer, Employee> byId = m_multiIndexContainer.createHashedUniqueIndex(e -> e.m_id);
        seq.add(TD.m_data1);
        seq.add(TD.m_data2);
        seq.add(TD.m_data3);

        final boolean isRemoved = seq.remove(TD.m_data2);
        assertThat(isRemoved, is(true));
        assertThat(seq, contains(TD.m_data1, TD.m_data3));
        assertThat(byId.size(), is(2));
    }

    @Test
    public void testRemoveNull()
    {
        final SequentialIndex<Employee> seq = m_multiIndexContainer.createSequentialIndex();
        final UniqueIndex<Integer, Employee> byId = m_multiIndexContainer.createHashedUniqueIndex(e -> e.m_id);
        seq.add(TD.m_data1);

        final boolean isRemoved = seq.remove(null);
        assertThat(isRemoved, is(false));
        assertThat(seq, contains(TD.m_data1));
        assertThat(byId.size(), is(1));
    }

    @Test
    public void testCreateSequentialIndexAfterwards()
    {
        final SequentialIndex<Employee> seq1 = m_multiIndexContainer.createSequentialIndex();
        seq1.add(TD.m_data1);

        m_exception.expect(IllegalStateException.class);
        m_multiIndexContainer.createSequentialIndex();
    }

    @Test
    public void testCreateHashedUniqueIndexAfterwards()
    {
        final SequentialIndex<Employee> seq1 = m_multiIndexContainer.createSequentialIndex();
        seq1.add(TD.m_data1);

        m_exception.expect(IllegalStateException.class);
        m_multiIndexContainer.createHashedUniqueIndex(e -> e.m_id);
    }
}
