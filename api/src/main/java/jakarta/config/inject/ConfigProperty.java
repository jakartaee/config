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
package jakarta.config.inject;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.inject.Qualifier;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;

/**
 * Binds an injection point to a configured value.
 *
 * Depending on a target, the behavior is as follows:
 * <ul>
 *     <li>Field - injects the field, if a converter exists for {@code String -> field type}, default value can be defined</li>
 *     <li>Parameter - same as injection to a field</li>
 * </ul>
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD, FIELD, PARAMETER, TYPE})
@Documented
public @interface ConfigProperty {
    String UNCONFIGURED_VALUE = "jakarta.config.configproperty.unconfiguredvalue";

    /**
     * The key of the config property used to look up the configuration value.
     * <p>
     * If it is not specified, it will be derived automatically as {@code <class_name>.<injection_point_name>}, where
     * {@code injection_point_name} is the field name or parameter name, {@code class_name} is the fully qualified name
     * of the class being injected to.
     * <p>
     * If one of the {@code class_name} or {@code injection_point_name} cannot be determined, the value has to be
     * provided.
     *
     * @return Name (key) of the config property to inject
     */
    String name() default "";

    /**
     * The default value if the configured property does not exist. This value acts as a config source with the
     * lowest ordinal.
     * <p>
     * If the target Type is not String, a proper {@link jakarta.config.spi.Converter} will get
     * applied.
     *
     * @return the default value as a string
     */
    String defaultValue() default UNCONFIGURED_VALUE;
}
