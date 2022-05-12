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

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import jakarta.config.spi.ConfigSource;
import jakarta.config.spi.Converter;

/**
 * Access to configuration values.
 *
 * TODO: This javadoc must be expanded once the API is clarified
 */
public interface Config {
    /**
     * A convenience method to get any typed (sub) key of this configuration node.
     *
     * @param key key relative to current node
     * @param type class of the configuration option
     * @param <T> type of the configuration option
     * @return typed configuration option if defined, empty {@link java.util.Optional} otherwise
     *
     * @see #get(String)
     * @see #as(Class)
     */
    default <T> Optional<T> getValue(String key, Class<T> type) {
        return get(key).as(type);
    }
    /**
     * Context related to the root configuration instance.
     *
     * @return configuration context
     */
    ConfigContext context();

    /**
     * Fully qualified key of this config node (such as {@code server.port}).
     * Returns an empty String for root config.
     *
     * @return key of this config
     */
    String getKey();

    /**
     * Name of this node - the last element of a fully qualified key.
     * <p>
     * For example for key {@code server.port} this method would return {@code port}.
     *
     * @return name of this node
     */
    String getNodeName();

    /**
     * Single sub-node for the specified sub-key.
     * For example if requested for key {@code server}, this method would return a config
     * representing the {@code server} node, which would have for example a child {@code port}.
     * The sub-key can return more than one level of nesting (e.g. using {@code server.tls} would
     * return a node that contains the TLS configuration under {@code server} node).
     *
     * @param key sub-key to retrieve nested node.
     * @return sub node, never null
     */
    Config get(String key);

    /**
     * Typed value created using a converter function.
     * The converter is called only if this config node exists.
     *
     * @param converter to create an instance from config node
     * @param <T> type of the object
     * @return converted value of this node, or an empty optional if this node does not exist
     * @throws java.lang.IllegalArgumentException if this config node cannot be converted to the desired type
     */
    <T> Optional<T> as(Converter<T> converter);

    /**
     * Typed value created using a discovered/built-in converter.
     *
     * @param type class to convert to
     * @param <T> type of the object
     * @return converted value of this node, or an empty optional if this node does not exist
     * @throws java.lang.IllegalArgumentException if this config node cannot be converted to the desired type
     */
    <T> Optional<T> as(Class<T> type);

    /**
     * Direct value of this node used for string converters.
     *
     * @return value of this node
     */
    Optional<String> asString();

    /**
     * Get a list of sub-nodes of this configuration node correctly typed.
     *
     * @param type type to convert nodes to, {@link jakarta.config.Config} is a possible type to use
     * @param <T> type of the list elements
     * @return typed list of configuration options, or empty {@link java.util.Optional} if there are no children
     *          or if this node does not have value
     */
    <T> Optional<List<T>> asList(Class<T> type);

    /**
     * Register a change consumer for this config node. The consumer would only be called if a change in configuration impacts
     * the current node or its sub-nodes.
     * <p>
     * Example 1: If this node is {@code server.host} and {@code server.host} is modified, the consumer would be triggered,
     * in case {@code server.port} is modified, the consumer would not be triggered.
     * <p>
     * Example 2: If this node is {@code server} and either {@code server.host} or {@code server.port} is modified, the
     * consumer would be triggered.
     * <p>
     * Change consumer is retained by configuration regardless of changes in the underlying sources. Invoking this method
     * on a different instance of config (including a new version of this config instance) will result in duplicate change
     * events delivered to the change consumer.
     *
     * @param changeConsumer processor of changes, the config provided to the function is the same node as the current node,
     *                       but with values from the changed config source, the collection provided is the set of node
     *                       names that were modified (if this node is {@code server} and {@code server.host} was modified,
     *                       the set would contain the String {@code host}; the function returns {@code true} if further
     *                       events should be delivered, {@code false} to stop receiving change events
     */
    void onChange(BiFunction<Config, Collection<String>, Boolean> changeConsumer);

    /**
     * Metadata related to a config instance.
     *
     * @author <a href="mailto:tomas.langer@oracle.com">Tomáš Langer</a>
     */
    interface ConfigContext {
        /**
         * Return all the currently registered {@link jakarta.config.spi.ConfigSource sources} for this configuration.
         * <p>
         * The returned sources will be sorted by priority and name, which can be iterated in a thread-safe
         * manner. The {@link Iterable Iterable} contains a fixed number of {@link jakarta.config.spi.ConfigSource
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
    }
}
