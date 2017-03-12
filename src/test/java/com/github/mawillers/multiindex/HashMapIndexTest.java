package com.github.mawillers.multiindex;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("javadoc")
public final class HashMapIndexTest
{
    private MultiIndexContainer<Employee> m_multiIndexContainer;
    private UniqueIndex<Integer, Employee> m_byId;

    @Before
    public void setup()
    {
        m_multiIndexContainer = MultiIndexContainer.create();
        m_byId = m_multiIndexContainer.createHashedUniqueIndex(e -> e.m_id);
    }

    @Test
    public void creationShouldAlwaysSucceed()
    {
        assertThat(m_byId, is(notNullValue()));
    }

    @Test
    public void newIndexShouldBeEmpty()
    {
        assertThat(m_byId.isEmpty(), is(true));
        assertThat(m_byId.size(), is(0));
    }

    @Test
    public void clearedIndexShouldBeEmpty()
    {
        m_byId.clear();
        assertThat(m_byId.isEmpty(), is(true));
        assertThat(m_byId.size(), is(0));

        m_byId.add(TD.m_data1);
        m_byId.clear();
        assertThat(m_byId.isEmpty(), is(true));
        assertThat(m_byId.size(), is(0));
    }

    @Test
    public void addUniquesShouldAlwaysSucceed()
    {
        boolean b = m_byId.add(TD.m_data1);
        assertThat(b, is(true));
        assertThat(m_byId.isEmpty(), is(false));
        assertThat(m_byId.size(), is(1));

        b = m_byId.add(TD.m_data2);
        assertThat(b, is(true));
        assertThat(m_byId.isEmpty(), is(false));
        assertThat(m_byId.size(), is(2));

        b = m_byId.add(TD.m_data3);
        assertThat(b, is(true));
        assertThat(m_byId.isEmpty(), is(false));
        assertThat(m_byId.size(), is(3));
    }

    @Test
    public void testContainsKey()
    {
        m_byId.add(TD.m_data1);
        m_byId.add(TD.m_data2);
        m_byId.add(TD.m_data3);

        assertThat(m_byId.containsKey(1), is(true));
        assertThat(m_byId.containsKey(2), is(true));
        assertThat(m_byId.containsKey(3), is(true));
        assertThat(m_byId.containsKey(4), is(false));
        assertThat(m_byId.containsKey(0), is(false));
    }

    @Test
    public void testContainsValue()
    {
        m_byId.add(TD.m_data1);
        m_byId.add(TD.m_data2);

        assertThat(m_byId.containsValue(TD.m_data1), is(true));
        assertThat(m_byId.containsValue(TD.m_data2), is(true));
        assertThat(m_byId.containsValue(TD.m_data3), is(false));
    }

    @Test
    public void toStringShouldPrependIndexName()
    {
        final String text = m_byId.toString();
        assertThat(text, startsWith("HashMapIndex"));
    }
}