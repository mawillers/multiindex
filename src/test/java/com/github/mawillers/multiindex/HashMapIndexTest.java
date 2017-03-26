package com.github.mawillers.multiindex;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresent;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("javadoc")
public final class HashMapIndexTest
{
    private MultiIndexContainer<Employee> m_multiIndexContainer;
    private UniqueIndex<Integer, Employee> m_byId;

    private static final Function<Employee, Integer> s_idExtractor = e -> e.m_id;

    @Before
    public void setup()
    {
        m_multiIndexContainer = MultiIndexContainer.create();
        m_byId = m_multiIndexContainer.createHashedUniqueIndex(s_idExtractor);
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
    public void sameInstanceAlwaysEquals()
    {
        assertThat(m_byId.equals(m_byId), is(true));
    }

    @Test
    public void testSameIndexWithSameKeyExtractorIsEqual()
    {
        final UniqueIndex<Integer, Employee> otherSeq = m_multiIndexContainer.createHashedUniqueIndex(s_idExtractor);
        assertThat(m_byId.hashCode(), is(otherSeq.hashCode()));
        assertThat(m_byId.equals(otherSeq), is(true));
    }

    @Test
    public void testSameIndexWithAnonymousKeyExtractorIsDifferent()
    {
        final UniqueIndex<Integer, Employee> otherSeq = m_multiIndexContainer.createHashedUniqueIndex(e -> e.m_id);
        // This is rather unfortunate, but unavoidable with current language rules, because functional interfaces can only be compared for identity.
        assertThat(m_byId.equals(otherSeq), is(false));
    }

    @Test
    public void unrelatedTypeIsDifferent()
    {
        final Object obj = new Object();
        assertThat(m_byId.equals(obj), is(false));
    }

    @Test
    public void instanceWithAnotherKeyExtractorIsDifferent()
    {
        final UniqueIndex<Integer, Employee> otherSeq = m_multiIndexContainer.createHashedUniqueIndex(e -> e.m_age);
        assertThat(m_byId.equals(otherSeq), is(false));
    }

    @Test
    public void anotherContainersIndexIsDifferent()
    {
        final MultiIndexContainer<Employee> container2 = MultiIndexContainer.create();
        final UniqueIndex<Integer, Employee> foreignSeq = container2.createHashedUniqueIndex(e -> e.m_id);
        assertThat(m_byId.equals(foreignSeq), is(false));
    }

    @Test
    public void toStringShouldPrependIndexName()
    {
        final String text = m_byId.toString();
        assertThat(text, startsWith("HashMapIndex"));
    }
}
