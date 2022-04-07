package jakarta.config.tck.programmatic.mutability;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

import jakarta.config.Config;
import jakarta.config.spi.ConfigProviderResolver;
import jakarta.config.spi.ConfigSource;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.fail;

/**
 * Test configuration mutability.
 */
public class MutabilityTest {
    public static final String SYSTEM_PROPERTY = "jakarta.config.tck.MutabilityTest.systemProperty";
    private static ConfigProviderResolver configProviderResolver;
    private static Config mutableConfig;
    private static MutableConfigSource mutableSource;

    @BeforeClass
    public static void init() {
        configProviderResolver = ConfigProviderResolver.instance();

        mutableSource = new MutableConfigSource(Map.of("group.key-1", "value",
                                                       "group.key-2", "value2",
                                                       "other.key", "other"));
        mutableConfig = configProviderResolver.getBuilder()
                .withSources(mutableSource)
                .build();
        Config groupConfig = mutableConfig.get("group");
        // first check that config source provides the values

        assertEquals(groupConfig.get("key-1").asString(),
                     Optional.of("value"),
                     "Value from custom source should be available for key group.key-1");

        assertEquals(groupConfig.get("key-2").asString(),
                     Optional.of("value2"),
                     "Value from custom source should be available for key group.key-2");

        assertEquals(mutableConfig.get("other.key").asString(),
                     Optional.of("other"),
                     "Value from custom source should be available for key other.key");
    }

    @Test
    public void testSystemPropertiesDoNotMutateConfig() {
        String originalValue = "someValue";
        String changedValue = "newValue";

        try {
            System.setProperty(SYSTEM_PROPERTY, "someValue");
            Config config = configProviderResolver.getBuilder()
                    .addDefaultSources()
                    .build();
            assertEquals(config.get(SYSTEM_PROPERTY).asString(),
                         Optional.of(originalValue),
                         "System property " + SYSTEM_PROPERTY + " should be available in config with default sources");

            System.setProperty(SYSTEM_PROPERTY, changedValue);
            assertEquals(config.get(SYSTEM_PROPERTY).asString(),
                         Optional.of(originalValue),
                         "System property " + SYSTEM_PROPERTY
                                 + " should be available in config with default sources and should not change even if the "
                                 + "property changes");
        } finally {
            System.getProperties().remove(SYSTEM_PROPERTY);
        }
    }

    @Test
    public void testMutableConfigSourceRemoveKey() throws ExecutionException, InterruptedException {
        try {
            Config groupConfig = mutableConfig.get("group");
            Config otherConfig = mutableConfig.get("other");
            /*
             Register listeners
             */
            CompletableFuture<ChangeEvent> rootConfigChange = new CompletableFuture<>();
            CompletableFuture<ChangeEvent> groupConfigChange = new CompletableFuture<>();
            CompletableFuture<ChangeEvent> otherConfigChange = new CompletableFuture<>();

            // now start mutability testing
            otherConfig.onChange((newConfig, changedKeys) -> {
                otherConfigChange.complete(new ChangeEvent(newConfig, changedKeys));
                // this should never happen
                return false;
            });
            groupConfig.onChange((newConfig, changedKeys) -> {
                groupConfigChange.complete(new ChangeEvent(newConfig, changedKeys));
                // only interested in the first event
                return false;
            });
            mutableConfig.onChange((newConfig, changedKeys) -> {
                rootConfigChange.complete(new ChangeEvent(newConfig, changedKeys));
                // only interested in the first event
                return false;
            });

            // remove the key
            mutableSource.mutate("group.key-2", null);

            try {
                ChangeEvent rootChange = rootConfigChange.get(10, TimeUnit.SECONDS);
                assertEquals(rootChange.changedKeys, Set.of("group.key-2"), "Change should only contain the changed key");
                assertEquals(rootChange.newConfig.get("group.key-2").asString(),
                             Optional.empty(),
                             "Removed key should not be available in new config");
                assertEquals(mutableConfig.get("group.key-2").asString(),
                             Optional.of("value2"),
                             "Original config should still contain unchanged mutated value");
            } catch (TimeoutException e) {
                fail("We should have received a config change on root configuration for a custom mutable config source."
                             + " Timed out after 10 seconds");
            }

            try {
                ChangeEvent groupChange = groupConfigChange.get(10, TimeUnit.SECONDS);
                assertEquals(groupChange.changedKeys, Set.of("key-2"), "Change should only contain the changed key");
                assertEquals(groupChange.newConfig.get("key-2").asString(),
                             Optional.empty(),
                             "Removed key should not be available in new config");
            } catch (TimeoutException e) {
                fail("We should have received a config change on 'group' configuration node for a custom mutable config source."
                             + " Timed out after 10 seconds");
            }
            ChangeEvent otherChange = otherConfigChange.getNow(null);
            assertNull(otherChange, "We should have NOT received a config change on 'other' node as it was not modified "
                    + "for a custom mutable config source.");
        } finally {
            mutableSource.mutate("group.key-2", "value2");
        }
    }

    private static final class ChangeEvent {
        private final Config newConfig;
        private final Collection<String> changedKeys;

        ChangeEvent(Config newConfig, Collection<String> changedKeys) {
            this.newConfig = newConfig;
            this.changedKeys = changedKeys;
        }
    }

    private static final class MutableConfigSource implements ConfigSource {
        private final Map<String, String> originalKeys = new HashMap<>();
        private volatile Map<String, String> lastKeys;
        private volatile Function<Map<String, String>, Boolean> listener;
        private volatile boolean sendEvents = true;

        MutableConfigSource(Map<String, String> initialKeys) {
            originalKeys.putAll(initialKeys);
        }

        @Override
        public String getName() {
            return "tck-mutable-source";
        }

        @Override
        public String getValue(String key) {
            return originalKeys.get(key);
        }

        @Override
        public Set<String> getKeys() {
            return originalKeys.keySet();
        }

        @Override
        public boolean onChange(Function<Map<String, String>, Boolean> changedNodesFunction) {
            this.listener = changedNodesFunction;
            // changes are supported
            return true;
        }

        void mutate(String key, String value) {
            Map<String, String> newValues = new HashMap<>(lastKeys == null ? originalKeys : lastKeys);
            if (value == null) {
                newValues.remove(key);
            } else {
                newValues.put(key, value);
            }
            lastKeys = newValues;
            if (sendEvents) {
                // we must use a map that supports null values, to be able to remove a binding
                Map<String, String> changed = new HashMap<>();
                changed.put(key, value);
                sendEvents = listener.apply(changed);
            }
        }
    }
}
