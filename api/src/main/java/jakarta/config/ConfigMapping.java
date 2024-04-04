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
 * <p>This annotation is to mark an interface to contain configuration data.</p>
 *
 * <p>The <em>configuration interface</em> is <em>resolved</em> with a portion of application's
 * <em>persistent configuration</em> identified by <em>configuration path</em>.
 *
 * <p>This configuration annotation is ignored on all nested objects.</p>
 *
 * <p>The terms <em>configuration interface</em>, <em>configuration key</em>, <em>configuration path</em>,
 * <em>persistent configuration</em>, <em>resolve</em>, and others are used here as defined in
 * the Jakarta Config specification.</p>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConfigMapping {

    /**
     * The <em>configuration path</em> identifies where the configuration relevant for the annotated configuration class is found
     * in a given application's <em>persistent configuration</em>.
     *
     * <p>The configuration path uses the dot symbol as a separator.</p>
     *
     * <br>
     * For instance, if the <em>persistent configuration</em> contains
     * <pre>  my.configuration.user=tester</pre>
     * the <em>configuration path</em> for the configuration portion {@code user=tester} would be {@code my.configuration}.
     *
     * @return a {@link String} representation of a configuration path.
     */
    String path() default "";
}
