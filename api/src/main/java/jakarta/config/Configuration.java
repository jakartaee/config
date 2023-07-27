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
 * <p> This annotation is to mark an interface to contain configurations. The configurations will be injected
 * by the implementation of Jakarta Config.</p>
 *
 * <p>The <em>configuration interface</em> is resolved with portion of application's
 * <em>persistent configuration</em> identified by <em>configuration path</em>.
 *
 * <p>The terms <em>configuration interface</em>, <em>configuration key</em>, <em>configuration path</em>, <em>canonical
 * representation</em>, <em>persistent configuration</em>, <em>resolve</em>, and others are used here as defined in
 * the Jakarta Config specification.</p>

 * <p><strong>\u26A0 Caution:</strong> you are reading an incomplete draft specification that is subject to change.</p>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Configuration {

    /**
     * The <em>configuration path</em> may identify where, in a given application's <em>persistent configuration</em>,
     * the configuration relevant for the annotated configuration class may be found.
     *
     * <p>The configuration path is a <em>canonical representation</em> of a <em>configuration key</em>,
     * as defined in the Jakarta Config specification.</p>
     *
     * <p>For instance, should the <em>persistent configuration</em> contain
     * <pre>  my.configuration.user=tester</pre>
     * the <em>configuration key</em> for configuration portion {@code user=tester} would be the prefix {@code my.configuration}.
     * The <em>canonical representation</em> (configuration path) of this configuration key using
     * the default configuration path separator of {@code .} (dot) would be {@code my.configuration} as well,
     * while the <em>canonical representation</em> (path) of this configuration key using
     * the {@code /} (slash) separator would be {@code my/configuration}.
     * </p>
     *
     * @return a {@link String} representation of a configuration path.
     */
    String path() default "";


    /**
     * The <em>separator</em> used in the <em>configuration path</em> to identify the relevant <em>configuration key</em>
     * in application's <em>persistent configuration</em>.
     *
     * <p>The default separator is <em>.</em> (dot) symbol.</p>
     *
     * @return a {@link String} representation of a configuration path separator.
     */
    String separator() default ".";

}
