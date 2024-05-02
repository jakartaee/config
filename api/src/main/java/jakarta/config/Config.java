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

import java.util.NoSuchElementException;
import java.util.ServiceLoader;

/**
 * A loader of configuration-related objects.
 *
 * <p>The loader <em>resolves</em> configuration data of the provided <em>configuration interface</em> with a portion of
 * application's <em>persistent configuration</em> identified by <em>configuration path</em>. The portion of the
 * <em>persistent configuration</em> is identified by <em>configuration path</em>.</p>
 *
 * <p>The loader must support <em>persistent configuration</em> stored in <em>META-INF/jakarta-config.properties</em>
 * file found on the classpath that follows a format that is recognized by the class {@link java.util.Properties}.</p>
 *
 * <p>In the following example the {@code MyConfigurationRelatedObject} is the <em>configuration interface</em> to be
 * <em>resolved</em>. An instance of the <em>configuration interface</em> is created by the {@link Config}:</p>
 *
 * <blockquote><pre> {@linkplain Config Loader} loader = {@linkplain Config Loader}.{@linkplain Config#bootstrap() bootstrap()};
 *MyConfigurationRelatedObject object = null;
 *try {
 *  object = loader
 *             .{@linkplain #path(String) path("my.configuration")}
 *             .{@linkplain #load(Class) load(MyConfigurationRelatedObject.class)};
 *} catch ({@linkplain NoSuchElementException} noSuchElementException) {
 *  // object is <a href="doc-files/terminology.html#absent">absent</a>
 *} catch ({@linkplain ConfigException} configException) {
 *  // a {@linkplain #load(Class) loading}-related error occurred
 *}</pre></blockquote>
 *
 * <p>Implementations of the methods in this class must be:</p>
 * <ul>
 *     <li>idempotent</li>
 *     <li>safe for concurrent use by multiple threads</li>
 *     <li>must not return {@code null}.</li>
 * </ul>
 */
public interface Config {
    /**
     * Loads an object of the supplied {@code type} from the current {@link Config} <em>configuration path</em>.
     *
     * @param <T> the type of object to load
     * @param type the type of object to load; must not be {@code null}
     * @return the loaded object; never {@code null}
     * @exception NoSuchElementException if the requested object is not found.
     * @exception IllegalArgumentException if the supplied {@code type} was invalid for any reason
     * @exception NullPointerException if the supplied {@code type} was {@code null}
     */
    <T> T load(Class<T> type);

    /**
     * Return a new instance of a {@link Config} with the <em>configuration path</em> set.
     *
     * <p>The <em>configuration</em> path may contain one or more elements. A <em>configuration path</em> element
     * only includes the single name of the {@link Config} child hierarchy.</p>
     *
     * <br>
     * For instance, if the configuration contains a <code>properties</code> file with:
     * <pre>my.configuration.user=tester</pre>
     * the <em>configuration path</em> for the configuration name <code>user</code> is <code>my</code> and
     * <code>configuration</code>.
     *
     * @param paths a <code>String</code> array of configuration paths
     * @return a new instance of the {@link Config} class with the new <em>configuration path</em>
     */
    Config path(String... paths);

    /**
     * Return a new instance of a {@link Config} with the <em>configuration path</em> set.
     *
     * <p>The <em>configuration path</em> is the single name of the {@link Config} child hierarchy.</p>
     *
     * @param path a <code>String</code> of a configuration path
     * @return a new instance of the {@link Config} class with the new <em>configuration path</em>
     * @see Config#path(String...)
     */
    default Config path(String path) {
        return path(new String[] {path});
    }

    /**
     * {@linkplain #bootstrap(ClassLoader) Bootstraps} a {@link Config} instance for subsequent usage using
     * the {@linkplain Thread#getContextClassLoader() context classloader}.
     *
     * <p>This method is idempotent.</p>
     *
     * <p>This method is safe for concurrent use by multiple threads.</p>
     *
     * <p>Except as possibly noted above, the observable behavior of this method is specified to be identical to that
     * of the {@link #bootstrap(ClassLoader)} method.</p>
     *
     * @return a {@link Config}; never {@code null}
     *
     * @exception java.util.ServiceConfigurationError if bootstrapping failed because of a
     * {@link ServiceLoader#load(Class, ClassLoader)} or {@link ServiceLoader#findFirst()} problem.
     * @exception ConfigException if bootstrapping failed because of a {@link Config#load(Class)} problem.
     *
     * @see #bootstrap(ClassLoader)
     */
    static Config bootstrap() {
        return bootstrap(Thread.currentThread().getContextClassLoader());
    }

    /**
     * Bootstraps a {@link Config} instance for subsequent usage.
     * <br>
     * A <em>primordial {@link Config}</em> is located with observable effects equal to those resulting from
     * executing the following code:
     *
     * <blockquote>
     * <pre>
     *  {@linkplain Config} loader = {@linkplain ServiceLoader}.{@linkplain ServiceLoader#load(Class, ClassLoader) load(Loader.class, classLoader)}
     *  .{@linkplain java.util.ServiceLoader#findFirst() findFirst()}
     *  .{@linkplain java.util.Optional#orElseThrow() orElseThrow}({@linkplain NoSuchElementException#NoSuchElementException() NoSuchElementException::new});
     * </pre>
     * </blockquote>
     *
     * @param classLoader the {@link ClassLoader} used
     *                    to {@linkplain ServiceLoader#load(Class, ClassLoader) locate service provider files};
     *                    may be {@code null} to indicate the system classloader (or bootstrap class loader) in
     *                    accordance with the contract of the {@link ServiceLoader#load(Class, ClassLoader)} method;
     *                    often is the return value of an invocation of
     *                    {@link Thread#getContextClassLoader() Thread.currentThread().getContextClassLoader()}
     *
     * @return a {@link Config}; never {@code null}
     *
     * @exception java.util.ServiceConfigurationError if bootstrapping failed because of a {@link ServiceLoader#load(Class, ClassLoader)} problem.
     * @exception NoSuchElementException if a {@linkplain Config} is not found.
     */
    static Config bootstrap(ClassLoader classLoader) {
        return ServiceLoader.load(Config.class, classLoader)
                            .findFirst()
                            .orElseThrow(NoSuchElementException::new);
    }
}
