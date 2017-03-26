package com.github.mawillers.multiindex;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

@SuppressWarnings("javadoc")
public final class ArrayListIndexTest
{
    private MultiIndexContainer<Employee> m_multiIndexContainer;
    private SequentialIndex<Employee> m_sequentialIndex;

    @Rule
    public ExpectedException m_exception = ExpectedException.none();

    @Before
    public void setup()
    {
        m_multiIndexContainer = MultiIndexContainer.create();
        m_sequentialIndex = m_multiIndexContainer.createSequentialIndex();
    }

    @Test
    public void creationShouldAlwaysSucceed()
    {
        assertThat(m_sequentialIndex, is(notNullValue()));
    }

    @Test
    public void newIndexShouldBeEmpty()
    {
        assertThat(m_sequentialIndex.isEmpty(), is(true));
        assertThat(m_sequentialIndex.size(), is(0));
    }

    @Test
    public void clearedIndexShouldBeEmpty()
    {
        m_sequentialIndex.clear();
        assertThat(m_sequentialIndex.isEmpty(), is(true));
        assertThat(m_sequentialIndex.size(), is(0));

        m_sequentialIndex.add(TD.m_data1);
        m_sequentialIndex.clear();
        assertThat(m_sequentialIndex.isEmpty(), is(true));
        assertThat(m_sequentialIndex.size(), is(0));
    }

    @Test
    public void addShouldAlwaysSucceed()
    {
        final boolean b = m_sequentialIndex.add(TD.m_data1);
        assertThat(b, is(true));
        assertThat(m_sequentialIndex.isEmpty(), is(false));
        assertThat(m_sequentialIndex.size(), is(1));
    }

    @Test
    public void addNullShouldWork()
    {
        boolean b = m_sequentialIndex.add(TD.m_data1);

        b = m_sequentialIndex.add(null);
        assertThat(b, is(true));
        assertThat(m_sequentialIndex.size(), is(2));
        b = m_sequentialIndex.add(null);
        assertThat(b, is(true));
        assertThat(m_sequentialIndex.size(), is(3));
    }

    @Test
    public void addAllShouldAlwaysSucceed()
    {
        boolean b = m_sequentialIndex.addAll(Arrays.asList(TD.m_data1, TD.m_data2));
        assertThat(b, is(true));
        assertThat(m_sequentialIndex, contains(TD.m_data1, TD.m_data2));

        b = m_sequentialIndex.addAll(Arrays.asList(TD.m_data1, TD.m_data2));
        assertThat(b, is(true));
        assertThat(m_sequentialIndex, contains(TD.m_data1, TD.m_data2, TD.m_data1, TD.m_data2));

        b = m_sequentialIndex.addAll(Arrays.asList(TD.m_data1, TD.m_data2, TD.m_data3));
        assertThat(b, is(true));
        assertThat(m_sequentialIndex, contains(TD.m_data1, TD.m_data2, TD.m_data1, TD.m_data2, TD.m_data1, TD.m_data2, TD.m_data3));
    }

    @Test
    public void removeExisting()
    {
        m_sequentialIndex.add(TD.m_data1);
        m_sequentialIndex.add(TD.m_data2);

        boolean isRemoved = m_sequentialIndex.remove(TD.m_data1);
        assertThat(isRemoved, is(true));
        assertThat(m_sequentialIndex.contains(TD.m_data2), is(true));
    }

    @Test
    public void removeExistingNull()
    {
        m_sequentialIndex.add(null);
        m_sequentialIndex.add(TD.m_data2);

        boolean isRemoved = m_sequentialIndex.remove(null);
        assertThat(isRemoved, is(true));
        assertThat(m_sequentialIndex.contains(TD.m_data2), is(true));
    }

    @Test
    public void removeNonExisting()
    {
        m_sequentialIndex.add(TD.m_data1);
        m_sequentialIndex.add(TD.m_data2);

        boolean isRemoved = m_sequentialIndex.remove(TD.m_data3);
        assertThat(isRemoved, is(false));
        assertThat(m_sequentialIndex, contains(TD.m_data1, TD.m_data2));
    }

    @Test
    public void removeNonExistingNull()
    {
        m_sequentialIndex.add(TD.m_data1);
        m_sequentialIndex.add(TD.m_data2);

        boolean isRemoved = m_sequentialIndex.remove(null);
        assertThat(isRemoved, is(false));
        assertThat(m_sequentialIndex.contains(TD.m_data2), is(true));
    }

    @Test
    public void testContains()
    {
        m_sequentialIndex.add(TD.m_data1);
        m_sequentialIndex.add(TD.m_data2);

        assertThat(m_sequentialIndex.contains(TD.m_data1), is(true));
        assertThat(m_sequentialIndex.contains(TD.m_data2), is(true));
        assertThat(m_sequentialIndex.contains(TD.m_data3), is(false));
        assertThat(m_sequentialIndex.contains(null), is(false));
    }

    @Test
    public void testIteration()
    {
        assertThat(m_sequentialIndex, is(emptyIterable()));

        m_sequentialIndex.add(TD.m_data1);
        m_sequentialIndex.add(TD.m_data2);
        m_sequentialIndex.add(TD.m_data3);

        assertThat(m_sequentialIndex, is(iterableWithSize(3)));
        assertThat(m_sequentialIndex, contains(TD.m_data1, TD.m_data2, TD.m_data3));
    }

    @Test
    public void removeViaIteratorShouldThrow()
    {
        m_sequentialIndex.add(TD.m_data1);
        m_sequentialIndex.add(TD.m_data2);

        final Iterator<Employee> iter = m_sequentialIndex.iterator();
        iter.next();

        m_exception.expect(UnsupportedOperationException.class);
        iter.remove();
    }

    @Test
    public void sameInstanceAlwaysEquals()
    {
        assertThat(m_sequentialIndex.equals(m_sequentialIndex), is(true));
    }

    @Test
    public void unrelatedTypeIsDifferent()
    {
        final Object obj = new Object();
        assertThat(m_sequentialIndex.equals(obj), is(false));
    }

    @Test
    public void allSequentialIndexesFromSameContainerAreEqual()
    {
        final SequentialIndex<Employee> otherSeq = m_multiIndexContainer.createSequentialIndex();
        assertThat(m_sequentialIndex.hashCode(), is(otherSeq.hashCode()));
        assertThat(m_sequentialIndex.equals(otherSeq), is(true));
    }

    @Test
    public void anotherContainersIndexIsDifferent()
    {
        final MultiIndexContainer<Employee> container2 = MultiIndexContainer.create();
        final SequentialIndex<Employee> foreignSeq = container2.createSequentialIndex();
        assertThat(m_sequentialIndex.equals(foreignSeq), is(false));
    }

    @Test
    public void toStringShouldPrependIndexName()
    {
        final String text = m_sequentialIndex.toString();
        assertThat(text, startsWith("ArrayListIndex"));
    }
}
