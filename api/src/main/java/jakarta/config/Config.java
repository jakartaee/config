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
import java.util.function.Function;

/**
 * Access to configuration values.
 *
 * TODO: This javadoc must be expanded once the API is clarified
 */
public interface Config {
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
    String getName();

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
    <T> Optional<T> as(Function<Config, T> converter);

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

    /*
     * Shortcut helper methods
     */
    default Optional<Integer> asInt() {
        return as(Integer.class);
    }
}
