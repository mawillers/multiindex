package com.github.mawillers.multiindex;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("javadoc")
public final class MultiIndexContainerTest
{
    private MultiIndexContainer<Employee> m_multiIndexContainer;

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
}
