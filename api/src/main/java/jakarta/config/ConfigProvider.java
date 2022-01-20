/*
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jakarta.config;

import jakarta.config.spi.ConfigProviderResolver;

/**
 * This is the central class to access a {@link Config}.
 * <p>
 * A {@link Config} provides access to the application's configuration. It may have been automatically discovered, or
 * manually created and registered.
 * <p>
 * The default usage is to use {@link #getConfig()} to automatically pick up the <em>configuration</em> for the current
 * thread's {@linkplain Thread#getContextClassLoader() context class loader}.
 * <p>
 * TODO: We should add intro how config is created once API is clarified
 * <p>
 * For more advanced use cases (e.g. registering a manually created {@link Config} instance), please see
 * {@link ConfigProviderResolver#registerConfig(Config, ClassLoader)} and {@link ConfigProviderResolver#getBuilder()}.
 *
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 * @author <a href="mailto:rmannibucau@apache.org">Romain Manni-Bucau</a>
 * @author <a href="mailto:emijiang@uk.ibm.com">Emily Jiang</a>
 * @author <a href="mailto:viktor.klang@gmail.com">Viktor Klang</a>
 */
public final class ConfigProvider {
    private ConfigProvider() {
    }

    /**
     * Get the {@linkplain Config configuration} corresponding to the current application, as defined by the calling
     * thread's {@linkplain Thread#getContextClassLoader() context class loader}.
     * <p>
     * The {@link Config} instance will be created and registered to the context class loader if no such configuration
     * is already created and registered.
     * <p>
     * Each class loader corresponds to exactly one configuration.
     *
     * @return the configuration instance for the thread context class loader
     */
    public static Config getConfig() {
        return ConfigProviderResolver.instance().getConfig();
    }

    /**
     * Get the {@linkplain Config configuration} for the application corresponding to the given class loader instance.
     * <p>
     * The {@link Config} instance will be created and registered to the given class loader if no such configuration is
     * already created and registered.
     * <p>
     * Each class loader corresponds to exactly one configuration.
     *
     * @param loader
     *            the Classloader used to register the configuration instance
     * @return the configuration instance for the given class loader
     */
    public static Config getConfig(ClassLoader loader) {
        return ConfigProviderResolver.instance().getConfig(loader);
    }
}
