package com.github.mawillers.multiindex;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("javadoc")
public final class ArrayListIndexTest
{
    private final Employee m_data1 = new Employee(1, "Harris", 26, "Sunnydale");

    private MultiIndexContainer<Employee> m_multiIndexContainer;
    private Index<Employee> m_sequentialIndex;

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

        m_sequentialIndex.add(m_data1);
        m_sequentialIndex.clear();
        assertThat(m_sequentialIndex.isEmpty(), is(true));
        assertThat(m_sequentialIndex.size(), is(0));
    }

    @Test
    public void addShouldAlwaysSucceed()
    {
        final boolean b = m_sequentialIndex.add(m_data1);
        assertThat(b, is(true));
        assertThat(m_sequentialIndex.isEmpty(), is(false));
        assertThat(m_sequentialIndex.size(), is(1));
    }

    @Test
    public void toStringShouldPrependIndexName()
    {
        final String text = m_sequentialIndex.toString();
        assertThat(text, startsWith("ArrayListIndex"));
    }
}
