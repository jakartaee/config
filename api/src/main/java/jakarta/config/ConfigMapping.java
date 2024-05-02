/*
 * Copyright (c) 2023 Contributors to the Eclipse Foundation
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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks an interface type as capable of mapping a configuration hierarchy.
 *
 * <p>Config Mapping allows mapping configuration entries to complex object types (usually user defined).
 *
 * <ul>
 *     <li>A configuration path uniquely identifies object member</li>
 *     <li>A configuration value maps to the object member value type</li>
 * </ul>
 *
 * <p>A complex object type uses the following rules to map configuration values to their member values:</p>
 *
 * <ul>
 *     <li>A configuration path is built by taking the object type path and the mapping member name</li>
 *     <li>The member name is converted to its kebab-case format</li>
 *     <li>If the member name is a getter, the member name is taken from its property name equivalent, and then converted to its kebab-case format</li>
 *     <li>The configuration value is automatically converted to the member type</li>
 *     <li>The configuration path is required to exist with a valid configuration value or the mapping will fail</li>
 * </ul>
 *
 * <h2>Example</h2>
 * <pre>
 * &#064;ConfigMapping("server")
 * interface Server {
 *     String host();
 *     int port();
 *     int ioThreads();
 *     List&lt;Endpoint&gt; endpoints();
 *     Optional&lt;Ssl&gt; ssl();
 *     Map&lt;String, String&gt; form();
 *
 *     interface Ssl {
 *         int port();
 *         String certificate();
 *         List&lt;String&gt; protocols();
 *     }
 *
 *     interface Endpoint {
 *         String path();
 *         List&lt;String&gt; methods();
 *     }
 * }
 * </pre>
 *
 * <p>The {@link ConfigMapping} members will be populated with the configuration found in the configuration paths:</p>
 *
 * <ul>
 *     <li><code>Server#host</code> in <code>server</code>, <code>host</code></li>
 *     <li><code>Server#port</code> in <code>server</code>, <code>port</code></li>
 *     <li><code>Server#ioThreads</code> in <code>server</code>, <code>io-threads</code></li>
 *     <li>
 *         <code>Server#endpoints()</code> in <code>server</code>, <code>endpoints</code>
 *         <ul>
 *             <li>Endpoint#path in <code>server</code>, <code>endpoints</code>, <code>path</code></li>
 *             <li>Endpoint#methods in <code>server</code>, <code>endpoints</code>, <code>methods</code></li>
 *         </ul>
 *     </li>
 *     <li>
 *         <code>Server#ssl</code> in <code>server</code>, <code>ssl</code>
 *         <ul>
 *             <li>Ssl#port in <code>server</code>, <code>ssl</code>, <code>port</code></li>
 *             <li>Ssl#certificate in <code>server</code>, <code>ssl</code>, <code>certificate</code></li>
 *             <li>Ssl#protocols in <code>server</code>, <code>ssl</code>, <code>protocols</code></li>
 *         </ul>
 *     </li>
 *     <li><code>Server#form</code> in <code>server</code>, <code>form</code></li>
 * </ul>
 *
 * <h2>Usage</h2>
 *
 * <p>A {@link ConfigMapping} annotated interface must be retrieved with {@link Config#load(Class)}:</p>
 *
 * <pre>
 *     Config config = Config.bootstrap();
 *     Server server = config.load(Server.class);
 * </pre>
 *
 * @see Config#load(Class)
 * @see ConfigName
 * @see ConfigDefault
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConfigMapping {
    /**
     * The <em>configuration</em> path may contain one or more elements. A <em>configuration path</em> element
     * only includes the single name of the {@link Config} child hierarchy.
     *
     * @return a <code>String</code> array of configuration paths
     */
    String[] value() default {};
}
