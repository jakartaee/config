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
import jakarta.config.spi.StringConverter;

/**
 * Metadata related to a config instance.
 *
 * @author <a href="mailto:tomas.langer@oracle.com">Tomáš Langer</a>
 */
public interface ConfigContext {
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
     * Return the {@link jakarta.config.spi.StringConverter} used by this instance to produce instances of the specified type from
     * string values.
     * This is a helper method to provide simple conversions (such as from a default value of an annotation).
     *
     * @param <T>
     *            the conversion type
     * @param forType
     *            the type to be produced by the converter
     * @return an {@link java.util.Optional} containing the converter, or empty if no converter is available for the specified
     *         type
     */
    <T> Optional<StringConverter<T>> getStringConverter(Class<T> forType);
}
