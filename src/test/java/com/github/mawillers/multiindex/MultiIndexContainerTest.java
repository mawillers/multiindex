package com.github.mawillers.multiindex;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
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
    }
}
