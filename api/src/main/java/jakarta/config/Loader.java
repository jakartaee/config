/*
 * Copyright (c) 2022 Contributors to the Eclipse Foundation
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

import java.util.ServiceLoader;

/**
 * A loader of configuration-related objects.
 *
 * <p>Sample usage:</p>
 *
 * <blockquote><pre>{@linkplain Loader Loader} loader = {@linkplain Loader Loader}.{@linkplain Loader#bootstrap() bootstrap()};
 *MyConfigurationRelatedObject object = null;
 *try {
 *  object = loader.{@linkplain #load(Class) load(MyConfigurationRelatedObject.class)};
 *} catch ({@linkplain NoSuchObjectException} noSuchObjectException) {
 *  // object is <a href="doc-files/terminology.html#absent">absent</a>
 *} catch ({@linkplain ConfigException} configException) {
 *  // a {@linkplain #load(Class) loading}-related error occurred
 *}</pre></blockquote>
 *
 * @see #bootstrap()
 *
 * @see #bootstrap(ClassLoader)
 *
 * @see #load(Class)
 *
 * @see #load(TypeToken)
 *
 * @see <a href="doc-files/terminology.html">Terminology</a>
 */
public interface Loader {

    /**
     * Loads a configuration-related object of the supplied {@code
     * type} and returns it.
     *
     * <p><strong>Note:</strong> The rules governing how it is
     * determined whether any given configuration-related object is
     * "of the supplied {@code type}" are currently wholly
     * undefined.</p>
     *
     * <p>Implementations of this method must not return {@code
     * null}.</p>
     *
     * <p>Implementations of this method must be idempotent.</p>
     *
     * <p>Implementations of this method must be safe for concurrent
     * use by multiple threads.</p>
     *
     * <p>Implementations of this method may or may not return a <a
     * href="doc-files/terminology.html#determinate">determinate</a>
     * value.</p>
     *
     * @param <T> the type of object to load
     *
     * @param type the type of object to load; must not be {@code
     * null}
     *
     * @return the loaded object; never {@code null}
     *
     * @exception NoSuchObjectException if the invocation was sound
     * but the requested object was <a
     * href="doc-files/terminology.html#absent">absent</a>
     *
     * @exception ConfigException if the invocation was sound but the
     * object could not be loaded for any reason not related to <a
     * href="doc-files/terminology.html#absent">absence</a>
     *
     * @exception IllegalArgumentException if the suplied {@code type}
     * was invalid for any reason
     *
     * @exception NullPointerException if the supplied {@code type}
     * was {@code null}
     */
    public <T> T load(Class<T> type);

    /**
     * Loads a configuration-related object of the supplied {@code
     * type} and returns it.
     *
     * <p><strong>Note:</strong> The rules governing how it is
     * determined whether any given configuration-related object is
     * "of the supplied {@code type}" are currently wholly
     * undefined.</p>
     *
     * <p>Implementations of this method must not return {@code
     * null}.</p>
     *
     * <p>Implementations of this method must be idempotent.</p>
     *
     * <p>Implementations of this method must be safe for concurrent
     * use by multiple threads.</p>
     *
     * <p>Implementations of this method may or may not return a <a
     * href="doc-files/terminology.html#determinate">determinate</a>
     * value.</p>
     *
     * @param <T> the type of object to load
     *
     * @param type the type of object to load; must not be {@code null}
     *
     * @return the loaded object; never {@code null}
     *
     * @exception NoSuchObjectException if the invocation was sound
     * but the requested object was <a
     * href="doc-files/terminology.html#absent">absent</a>
     *
     * @exception ConfigException if the invocation was sound but the
     * object could not be loaded for any reason not related to <a
     * href="doc-files/terminology.html#absent">absence</a>
     *
     * @exception IllegalArgumentException if the suplied {@code type}
     * was invalid for any reason
     *
     * @exception NullPointerException if the supplied {@code type}
     * was {@code null}
     */
    public <T> T load(TypeToken<T> type);

    /**
     * <em>{@linkplain #bootstrap(ClassLoader) Bootstraps}</em> a
     * {@link Loader} instance for subsequent usage using the
     * {@linkplain Thread#getContextClassLoader() context
     * classloader}.
     *
     * <p>This method never returns {@code null}.</p>
     *
     * <p>This method is idempotent.</p>
     *
     * <p>This method is safe for concurrent use by multiple
     * threads.</p>
     *
     * <p>This method may or may not return a <a
     * href="doc-files/terminology.html#determinate">determinate</a>
     * value. See {@link #bootstrap(ClassLoader)} for details.</p>
     *
     * <p>Except as possibly noted above, the observable behavior of
     * this method is specified to be identical to that of the {@link
     * #bootstrap(ClassLoader)} method.</p>
     *
     * @return a {@link Loader}; never {@code null}
     *
     * @exception java.util.ServiceConfigurationError if bootstrapping
     * failed because of a {@link ServiceLoader#load(Class,
     * ClassLoader)} or {@link ServiceLoader#findFirst()} problem
     *
     * @exception ConfigException if bootstrapping failed because of a
     * {@link Loader#load(Class)} problem
     *
     * @see #bootstrap(ClassLoader)
     */
    public static Loader bootstrap() {
        return bootstrap(Thread.currentThread().getContextClassLoader());
    }

    /**
     * <em>Bootstraps</em> a {@link Loader} instance for subsequent
     * usage.
     *
     * <p>The bootstrap process proceeds as follows:</p>
     *
     * <ol>
     *
     * <li>A <em>primordial {@link Loader}</em> is located with
     * observable effects equal to those resulting from executing the
     * following code:
     *
     * <blockquote><pre>{@linkplain Loader} loader = {@linkplain ServiceLoader}.{@linkplain ServiceLoader#load(Class, ClassLoader) load(Loader.class, classLoader)}
     *  .{@linkplain java.util.ServiceLoader#findFirst() findFirst()}
     *  .{@linkplain java.util.Optional#orElseThrow() orElseThrow}({@linkplain NoSuchObjectException#NoSuchObjectException() NoSuchObjectException::new});</pre></blockquote></li>
     *
     * <li>The {@link #load(Class)} method is invoked on the resulting
     * {@link Loader} with {@link Loader Loader.class} as its sole
     * argument.
     *
     * <ul>
     *
     * <li>If the invocation throws a {@link NoSuchObjectException},
     * the primordial {@link Loader} is returned.</li>
     *
     * <li>If the invocation returns a {@link Loader}, that {@link
     * Loader} is returned.</li>
     *
     * </ul>
     *
     * </li>
     *
     * </ol>
     *
     * <p>This method never returns {@code null}.</p>
     *
     * <p>This method is idempotent.</p>
     *
     * <p>This method is safe for concurrent use by multiple
     * threads.</p>
     *
     * <p>This method may or may not return a <a
     * href="doc-files/terminology.html#determinate">determinate</a>
     * value depending on the implementation of the {@link Loader}
     * loaded in step 2 above.</p>
     *
     * <p><strong>Note:</strong> The implementation of this method may
     * change without notice between any two versions of this
     * specification.  The requirements described above, however, will
     * be honored in any minor version of this specification within a
     * given major version.</p>
     *
     * @param classLoader the {@link ClassLoader} used to {@linkplain
     * ServiceLoader#load(Class, ClassLoader) locate service provider
     * files}; may be {@code null} to indicate the system classloader
     * (or bootstrap class loader) in accordance with the contract of
     * the {@link ServiceLoader#load(Class, ClassLoader)} method;
     * often is the return value of an invocation of {@link
     * Thread#getContextClassLoader()
     * Thread.currentThread().getContextClassLoader()}
     *
     * @return a {@link Loader}; never {@code null}
     *
     * @exception java.util.ServiceConfigurationError if bootstrapping
     * failed because of a {@link ServiceLoader#load(Class,
     * ClassLoader)} or {@link ServiceLoader#findFirst()} problem
     *
     * @exception ConfigException if bootstrapping failed because of a
     * {@link Loader#load(Class)} problem
     */
    public static Loader bootstrap(ClassLoader classLoader) {
        Loader loader = ServiceLoader.load(Loader.class, classLoader)
            .findFirst()
            .orElseThrow(NoSuchObjectException::new);
        try {
            return loader.load(Loader.class);
        } catch (NoSuchObjectException absentValueException) {
            System.getLogger(Loader.class.getName())
                .log(System.Logger.Level.DEBUG, absentValueException::getMessage, absentValueException);
            return loader;
        }
    }

}
