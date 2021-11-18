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
package jakarta.config.spi;

import java.util.Set;

/**
 * A <em>configuration source</em> which provides configuration values from a specific place.
 * <p>
 * The underlying source is abstracted by this SPI to provide unified access for any source type.
 * The following rules must be honored by all config sources:
 * <ul>
 *     <li>The configuration key points to a specific {@code leaf} value, if a key is resolved into a tree node that is
 *     not a leaf value, it should be considered as not found</li>
 *     <li>Keys use a dot ({@code .}) separator notation - each dot MAY be a tree node separator</li>
 *     <li>Values are always returned as {@link java.lang.String}, see {@link jakarta.config.spi.Converter} specification
 *     on how to handle conversion to String (the result must be convertible to appropriate types using built-in converters)</li>
 * </ul>
 *
 * <b>Examples of dot notation</b>
 * <p>
 * {@code server.host}:
 * <ul>
 *    <li>In flat config sources (flat config source is a map of keys to values), the key is {@code server.host}</li>
 *    <li>In tree config sources, this would be resolved to node {@code host} first, and then to child node {@code port}
 *    of the {@code host} node, or to an exact match {@code server.host}.</li>
 * </ul>
 */
public interface ConfigSource {
    /**
     * The name of the configuration source. The name might be used for logging or for analysis of configured values,
     * and also may be used in ordering decisions.
     * <p>
     * An example of a configuration source name is "{@code property-file mylocation/myprops.properties}".
     *
     * @return the name of the configuration source
     */
    String getName();

    /**
     * Return the value for the specified key in this configuration source.
     *
     * @param key
     *            the property key
     * @return the property value, or {@code null} if the property is not present
     */
    String getValue(String key);

    /**
     * Gets all property keys known to this configuration source, potentially without evaluating the values. The
     * returned property keys may be a subset of the keys of the total set of retrievable properties in this config
     * source.
     * <p>
     * The returned set is not required to allow concurrent or multi-threaded iteration; however, if the same set is
     * returned by multiple calls to this method, then the implementation must support concurrent and multi-threaded
     * iteration of that set.
     *
     * @return a set of property keys that are known to this configuration source
     */
    Set<String> getKeys();
}
