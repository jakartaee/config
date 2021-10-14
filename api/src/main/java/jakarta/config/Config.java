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

import java.util.Optional;

import jakarta.config.spi.ConfigSource;
import jakarta.config.spi.Converter;

/**
 * Access to configuration values.
 */
public interface Config {
    /**
     * Return all the currently registered {@link jakarta.config.spi.ConfigSource sources} for this configuration.
     * <p>
     * The returned sources will be sorted by priority and name, which can be iterated in a thread-safe
     * manner. The {@link java.lang.Iterable Iterable} contains a fixed number of {@link jakarta.config.spi.ConfigSource
     * configuration
     * sources}, determined at application start time, and the config sources themselves may be static or dynamic.
     *
     * @return the configuration sources
     */
    Iterable<ConfigSource> getConfigSources();

    /**
     * Return the {@link jakarta.config.spi.Converter} used by this instance to produce instances of the specified type from
     * string values.
     *
     * @param <T>
     *            the conversion type
     * @param forType
     *            the type to be produced by the converter
     * @return an {@link java.util.Optional} containing the converter, or empty if no converter is available for the specified
     *         type
     */
    <T> Optional<Converter<T>> getConverter(Class<T> forType);

    /**
     * Returns an instance of the specific class, to allow access to the provider specific API.
     * <p>
     * If the MP Config provider implementation does not support the specified class, a {@link IllegalArgumentException}
     * is thrown.
     * <p>
     * Unwrapping to the provider specific API may lead to non-portable behaviour.
     *
     * @param type
     *            Class representing the type to unwrap to
     * @param <T>
     *            The type to unwrap to
     * @return An instance of the given type
     * @throws IllegalArgumentException
     *             If the current provider does not support unwrapping to the given type
     */
    <T> T unwrap(Class<T> type);
}
