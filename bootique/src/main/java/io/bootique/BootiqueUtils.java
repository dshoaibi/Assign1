package io.bootique;

import com.google.inject.Module;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * Utils methods which used inside {@link Bootique} class, but can be moved outside.
 *
 * @since 0.24
 */
final class BootiqueUtils {

    private BootiqueUtils() {
        throw new AssertionError("Should not be called.");
    }

    static Collection<BQModuleProvider> moduleProviderDependencies(Collection<BQModuleProvider> rootSet) {
        return moduleProviderDependencies(rootSet, new HashMap<>())
                .entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .collect(toList());
    }

    private static Map<Class<? extends Module>, BQModuleProvider> moduleProviderDependencies(
            Collection<BQModuleProvider> rootSet,
            Map<Class<? extends Module>, BQModuleProvider> processed) {

        for (BQModuleProvider moduleProvider : rootSet) {
            // check if current module provider already processed
            if (processed.containsValue(moduleProvider)) continue;

            // Because RuntimeModuleMerger Bootique uses Module Class as unique key.
            final Class<? extends Module> moduleClass = moduleProvider.module().getClass();

            if (!processed.containsKey(moduleClass)) {
                processed.put(moduleClass, moduleProvider);

                final Collection<BQModuleProvider> dependencies = moduleProvider.dependencies();
                if (!dependencies.isEmpty()) {
                    processed.putAll(moduleProviderDependencies(dependencies, processed));
                }
            }
        }

        return processed;
    }

    static String[] mergeArrays(String[] a1, String[] a2) {
        if (a1.length == 0) {
            return a2;
        }

        if (a2.length == 0) {
            return a1;
        }

        String[] merged = new String[a1.length + a2.length];
        System.arraycopy(a1, 0, merged, 0, a1.length);
        System.arraycopy(a2, 0, merged, a1.length, a2.length);

        return merged;
    }

    static String[] toArray(Collection<String> collection) {
        return collection.toArray(new String[collection.size()]);
    }
}

