package com.github.mawillers.multiindex;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertThat;

import org.junit.Test;

@SuppressWarnings("javadoc")
public final class MultiIndexContainerTest
{
    @Test
    public void testCreation()
    {
        final MultiIndexContainer<Integer> mi = MultiIndexContainer.create();
        assertThat(mi, is(not(nullValue())));
        final Index<Integer> index = mi.createSequentialIndex();
        assertThat(index, is(notNullValue()));
    }

    @Test
    public void testIteration()
    {
        final MultiIndexContainer<Integer> mi = MultiIndexContainer.create();
        assertThat(mi.indexes(), is(emptyIterable()));

        mi.createSequentialIndex();
        assertThat(mi.indexes(), is(iterableWithSize(1)));
    }
}
