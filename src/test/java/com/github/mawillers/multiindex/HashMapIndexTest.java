package com.github.mawillers.multiindex;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresent;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

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
    public void testAddAll()
    {
        boolean b = m_byId.addAll(Arrays.asList(TD.m_data1, TD.m_data2));
        assertThat(b, is(true));
        assertThat(m_byId.size(), is(2));

        b = m_byId.addAll(Arrays.asList(TD.m_data1, TD.m_data2));
        assertThat(b, is(false));
        assertThat(m_byId.size(), is(2));

        b = m_byId.addAll(Arrays.asList(TD.m_data1, TD.m_data2, TD.m_data3));
        assertThat(b, is(true));
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
    public void testRemoveKey()
    {
        m_byId.add(TD.m_data1);
        m_byId.add(TD.m_data2);
        m_byId.add(TD.m_data3);

        m_byId.remove(2);
        assertThat(m_byId.containsKey(1), is(true));
        assertThat(m_byId.containsKey(2), is(false));
        assertThat(m_byId.containsKey(3), is(true));
    }

    @Test
    public void testGetOptional()
    {
        m_byId.add(TD.m_data1);
        m_byId.add(TD.m_data2);

        assertThat(m_byId.getOptional(1), isPresent());
        assertThat(m_byId.getOptional(2), isPresent());
        assertThat(m_byId.getOptional(3), not(isPresent()));
        assertThat(m_byId.getOptional(4), not(isPresent()));
        assertThat(m_byId.getOptional(0), not(isPresent()));
    }

    @Test
    public void toStringShouldPrependIndexName()
    {
        final String text = m_byId.toString();
        assertThat(text, startsWith("HashMapIndex"));
    }
}
