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

import jakarta.config.Config;

/**
 * A mechanism for converting configured values from {@link String} to any Java type.
 *
 * This is a helper interface to support direct {@link String} conversion, for a generic
 * conversion from nodes, please see {@link jakarta.config.spi.Converter}.
 *
 * @author <a href="mailto:rsmeral@apache.org">Ron Smeral</a>
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 * @author <a href="mailto:emijiang@uk.ibm.com">Emily Jiang</a>
 * @author <a href="mailto:john.d.ament@gmail.com">John D. Ament</a>
 */
public interface StringConverter<T> extends Converter<T> {
    /**
     * Convert the given string value to a specified type. Callers <em>must not</em> pass in {@code null} for
     * {@code value}; doing so may result in a {@code NullPointerException} being thrown.
     *
     * @param value
     *            the string representation of a property value (must not be {@code null})
     * @return the converted value, or {@code null} if the value is empty
     * @throws IllegalArgumentException
     *             if the value cannot be converted to the specified type
     * @throws NullPointerException
     *             if the given value was {@code null}
     */
    T convert(String value) throws IllegalArgumentException, NullPointerException;

    @Override
    default T convert(Config node) throws IllegalArgumentException, NullPointerException {
        return node.asString()
            .map(this::convert)
            .orElse(null);
    }
}
