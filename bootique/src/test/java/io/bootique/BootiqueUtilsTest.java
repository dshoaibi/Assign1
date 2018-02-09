package io.bootique;

import org.junit.Test;

import java.util.Collection;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class BootiqueUtilsTest {

    @Test
    public void testToArray() {
        assertArrayEquals(new String[]{}, BootiqueUtils.toArray(asList()));
        assertArrayEquals(new String[]{"a", "b", "c"}, BootiqueUtils.toArray(asList("a", "b", "c")));
    }

    @Test
    public void testMergeArrays() {
        assertArrayEquals(new String[]{}, BootiqueUtils.mergeArrays(new String[0], new String[0]));
        assertArrayEquals(new String[]{"a"}, BootiqueUtils.mergeArrays(new String[]{"a"}, new String[0]));
        assertArrayEquals(new String[]{"b"}, BootiqueUtils.mergeArrays(new String[0], new String[]{"b"}));
        assertArrayEquals(new String[]{"b", "c", "d"}, BootiqueUtils.mergeArrays(new String[]{"b", "c"}, new String[]{"d"}));
    }

    @Test
    public void moduleProviderDependencies() {
        final BQModuleProvider testModuleProvider1 = mock(BQModuleProvider.class);
        final BQModuleProvider testModuleProvider2 = mock(BQModuleProvider.class);
        final BQModuleProvider testModuleProvider3 = mock(BQModuleProvider.class);

        when(testModuleProvider1.dependencies()).thenReturn(asList(testModuleProvider2, testModuleProvider3));
        when(testModuleProvider1.module()).thenReturn(binder -> {
        });
        when(testModuleProvider2.module()).thenReturn(binder -> {
        });
        when(testModuleProvider3.module()).thenReturn(binder -> {
        });

        final Collection<BQModuleProvider> bqModuleProviders =
                BootiqueUtils.moduleProviderDependencies(singletonList(testModuleProvider1));

        assertThat(bqModuleProviders, hasItems(testModuleProvider1, testModuleProvider2, testModuleProvider3));
        assertEquals(3, bqModuleProviders.size());

        verify(testModuleProvider1, atLeastOnce()).dependencies();
        verify(testModuleProvider2, atLeastOnce()).dependencies();
        verify(testModuleProvider3, atLeastOnce()).dependencies();

        verify(testModuleProvider1, times(1)).module();
        verify(testModuleProvider2, times(1)).module();
        verify(testModuleProvider3, times(1)).module();

        verifyNoMoreInteractions(testModuleProvider1, testModuleProvider2, testModuleProvider3);
    }

    @Test
    public void moduleProviderDependenciesTwoLevels() {
        final BQModuleProvider testModuleProvider1 = mock(BQModuleProvider.class);
        final BQModuleProvider testModuleProvider2 = mock(BQModuleProvider.class);
        final BQModuleProvider testModuleProvider3 = mock(BQModuleProvider.class);

        when(testModuleProvider1.dependencies()).thenReturn(singletonList(testModuleProvider2));
        when(testModuleProvider2.dependencies()).thenReturn(singletonList(testModuleProvider3));

        when(testModuleProvider1.module()).thenReturn(binder -> {
        });
        when(testModuleProvider2.module()).thenReturn(binder -> {
        });
        when(testModuleProvider3.module()).thenReturn(binder -> {
        });

        final Collection<BQModuleProvider> bqModuleProviders =
                BootiqueUtils.moduleProviderDependencies(singletonList(testModuleProvider1));

        assertThat(bqModuleProviders, hasItems(testModuleProvider1, testModuleProvider2, testModuleProvider3));
        assertEquals(3, bqModuleProviders.size());

        verify(testModuleProvider1, atLeastOnce()).dependencies();
        verify(testModuleProvider2, atLeastOnce()).dependencies();
        verify(testModuleProvider3, atLeastOnce()).dependencies();

        verify(testModuleProvider1, times(1)).module();
        verify(testModuleProvider2, times(1)).module();
        verify(testModuleProvider3, times(1)).module();

        verifyNoMoreInteractions(testModuleProvider1, testModuleProvider2, testModuleProvider3);
    }

    @Test
    public void moduleProviderDependenciesCircular() {
        final BQModuleProvider testModuleProvider1 = mock(BQModuleProvider.class);
        final BQModuleProvider testModuleProvider2 = mock(BQModuleProvider.class);
        final BQModuleProvider testModuleProvider3 = mock(BQModuleProvider.class);

        when(testModuleProvider1.dependencies()).thenReturn(singletonList(testModuleProvider2));
        when(testModuleProvider2.dependencies()).thenReturn(singletonList(testModuleProvider3));
        when(testModuleProvider3.dependencies()).thenReturn(singletonList(testModuleProvider1));

        when(testModuleProvider1.module()).thenReturn(binder -> {
        });
        when(testModuleProvider2.module()).thenReturn(binder -> {
        });
        when(testModuleProvider3.module()).thenReturn(binder -> {
        });

        final Collection<BQModuleProvider> bqModuleProviders =
                BootiqueUtils.moduleProviderDependencies(singletonList(testModuleProvider1));

        assertThat(bqModuleProviders, hasItems(testModuleProvider1, testModuleProvider2, testModuleProvider3));
        assertEquals(3, bqModuleProviders.size());

        verify(testModuleProvider1, atLeastOnce()).dependencies();
        verify(testModuleProvider2, atLeastOnce()).dependencies();
        verify(testModuleProvider3, atLeastOnce()).dependencies();

        verify(testModuleProvider1, times(1)).module();
        verify(testModuleProvider2, times(1)).module();
        verify(testModuleProvider3, times(1)).module();

        verifyNoMoreInteractions(testModuleProvider1, testModuleProvider2, testModuleProvider3);
    }
}
