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
 * <p>
 *     Annotation of configuration-related objects. Defines a {@code value} that represents the property prefix.
 * </p>
 * <p>
 *     Should the {@link ConfigMapping} be used on an interface passed to {@link Loader}, the Loader must use the {@code value}
 *     to load the respective configuration sub-part.
 * </p>
 * <p>
 *     The {@link ConfigMapping} must be used to mark the interface to be handled by the injection support during the injection
 *     requested by the {@code @Inject} annotation.
 * </p>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConfigMapping {
    /**
     * @return the base configuration path (prefix or namespace) of the mapping.
     */
    String value() default "";
}
