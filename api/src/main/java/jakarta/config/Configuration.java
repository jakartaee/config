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
 * Indicates that the annotated class is a <em>configuration class</em>, as defined in the Jakarta Config specification.
 *
 * <p><strong>\u26A0 Caution:</strong> you are reading an incomplete draft specification that is subject to
 * change.</p>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Configuration {

    /**
     * An array of <em>configuration keys</em>, forming a <em>configuration path</em>, that may identify where, in a
     * given application's <em>persistent configuration</em>, the configuration relevant for the annotated configuration
     * class may be found.
     *
     * <p>Each configuration key in the array forming this element is a <em>canonical representation</em> of a
     * configuration key, as defined in the Jakarta Config specification.</p>
     *
     * <p>The configuration path represented by this element is effectively a hint to a Jakarta Config implementation
     * about where, in an application's persistent configuration, a portion of that configuration relevant to the
     * annotated configuration class may be found.  Because a Java class may be used in many different applications, and
     * because those applications may have very different persistent configurations, the configuration path represented
     * by this element cannot be regarded as authoritative for all possible applications. Rather, it is a useful
     * default, and Jakarta Config implementations should permit it to be overridden <strong>in ways that are currently
     * undefined</strong>.</p>
     *
     * <p>There is no requirement that the configuration path represented by this element must resolve in any given
     * application, since it may be ignored in favor of an application-specific override. On the other hand, in simple
     * applications where it is known that a configuration class will not be reused elsewhere, the configuration path
     * represented by this element may indeed resolve.</p>
     *
     * <p>Jakarta Config implementations must consider namespace concerns <strong>in ways that are yet to be
     * defined</strong> when reading the value of this element.</p>
     *
     * <p>The terms <em>configuration class</em>, <em>configuration key</em>, <em>configuration path</em>, <em>canonical
     * representation</em>, <em>persistent configuration</em>, <em>resolve</em>, and others are used here as defined in
     * the Jakarta Config specification.</p>
     *
     * @return an array of {@link String}s, each of which is a canonical representation of a configuration key, forming
     * a representation of a configuration path
     */
    String[] path() default {};

}
