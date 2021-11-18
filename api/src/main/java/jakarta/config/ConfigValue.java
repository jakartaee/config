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

/**
 * The ConfigValue holds additional information after the lookup of a configuration property and is itself immutable.
 * <p>
 * Holds information about the configuration property name, configuration value, the
 * {@link jakarta.config.spi.ConfigSource} name from where the configuration property was loaded and
 * the ordinal of the {@link jakarta.config.spi.ConfigSource}.
 * <p>
 * This is used together with {@link Config} to expose the configuration property lookup metadata.
 *
 * @author <a href="mailto:radcortez@yahoo.com">Roberto Cortez</a>
 */
public interface ConfigValue {
    /**
     * The key of the property.
     *
     * @return the name of the property.
     */
    String getKey();

    /**
     * The value of the property lookup with transformations (expanded, etc).
     *
     * @return the value of the property lookup
     */
    String getValue();

    /**
     * The value of the property lookup without any transformation (expanded , etc).
     *
     * @return the raw value of the property lookup
     */
    String getRawValue();

    /**
     * The {@link jakarta.config.spi.ConfigSource} name that loaded the property lookup.
     *
     * @return the ConfigSource name that loaded the property lookup
     */
    String getSourceName();
}
