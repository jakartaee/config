/*
 * Copyright (c) 2022, 2023 Contributors to the Eclipse Foundation
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
 * A {@link RuntimeException} thrown when a problem is encountered in
 * an implementation of the Jakarta Config specfication.
 *
 * <p><strong>\u26A0 Caution:</strong> you are reading an incomplete
 * draft specification that is subject to change.</p>
 */
public class ConfigException extends RuntimeException {

    /**
     * Creates a new {@link ConfigException}.
     */
    public ConfigException() {
        super();
    }

    /**
     * Creates a new {@link ConfigException}.
     *
     * @param message a detail message; may be {@code null}
     */
    public ConfigException(String message) {
        super(message);
    }

    /**
     * Creates a new {@link ConfigException}.
     *
     * @param cause the {@link Throwable} responsible for this {@link
     * ConfigException}'s existence; may be {@code null}
     */
    public ConfigException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new {@link ConfigException}.
     *
     * @param message a detail message; may be {@code null}
     *
     * @param cause the {@link Throwable} responsible for this {@link
     * ConfigException}'s existence; may be {@code null}
     */
    public ConfigException(String message, Throwable cause) {
        super(message, cause);
    }

}
