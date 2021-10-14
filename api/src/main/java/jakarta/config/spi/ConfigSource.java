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

/**
 * A <em>configuration source</em> which provides configuration values from a specific place.
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
}
