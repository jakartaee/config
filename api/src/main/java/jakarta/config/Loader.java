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

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.ServiceLoader;

/**
 * A loader of configuration-related objects.
 *
 * <p>Sample usage:</p>
 *
 * <blockquote><pre>{@linkplain Loader Loader} loader = {@linkplain Loader Loader}.{@linkplain Loader#bootstrap() bootstrap()};
 *MyConfigurationRelatedObject object = null;
 *try {
 *  object = loader.{@linkplain #load(Request) load}({@linkplain Request Request}.{@linkplain Request#builder(Class) builder}(MyConfigurationRelatedObject.class){@linkplain Request.Builder#build() build}()).{@linkplain Response#get() get}();
 *} catch ({@linkplain NoSuchObjectException} noSuchObjectException) {
 *  // object is <a href="doc-files/terminology.html#absent">absent</a>
 *} catch ({@linkplain ConfigException} configException) {
 *  // a {@linkplain #load(Request) loading}-related error occurred
 *}</pre></blockquote>
 *
 * @see #bootstrap()
 *
 * @see #bootstrap(ClassLoader)
 *
 * @see #load(Request)
 *
 * @see <a href="doc-files/terminology.html">Terminology</a>
 */
public interface Loader {

    /**
     * Loads a configuration-related object <a
     * href="doc-files/terminology.html#suitability"><em>suitable</em></a>
     * for the supplied {@code request} and returns a {@link Response}
     * {@linkplain Response#get() containing} it.
     *
     * <p><strong>Note:</strong> The rules governing how it is
     * determined whether any given configuration-related object is
     * "suitable for the supplied {@code request}" are currently
     * wholly undefined.</p>
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
     * @param request the {@link Request} designating the kind of
     * object to load; must not be {@code null}
     *
     * @return a {@link Response} {@linkplain Response#get()
     * containing} the loaded object; never {@code null}
     *
     * @exception NoSuchObjectException if the invocation was sound
     * but the requested object was <a
     * href="doc-files/terminology.html#absent">absent</a>
     *
     * @exception ConfigException if the invocation was sound but the
     * object could not be loaded for any reason not related to <a
     * href="doc-files/terminology.html#absent">absence</a>
     *
     * @exception IllegalArgumentException if the suplied {@code request}
     * was invalid for any reason
     *
     * @exception NullPointerException if the supplied {@code request}
     * was {@code null}
     *
     * @see Response
     */
    public <T> Response<T> load(Request<T> request);

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
     * {@link Loader#load(Request)} problem
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
     * <li>The {@link #load(Request)} method is invoked on the
     * resulting {@link Loader} with a {@link Request
     * Request&lt;Loader&gt;} as its sole argument.
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
     * {@link Loader#load(Request)} problem
     */
    public static Loader bootstrap(ClassLoader classLoader) {
        Loader loader = ServiceLoader.load(Loader.class, classLoader)
            .findFirst()
            .orElseThrow(NoSuchObjectException::new);
        try {
            return loader.load(Request.builder(Loader.class).build()).get();
        } catch (NoSuchObjectException absentValueException) {
            System.getLogger(Loader.class.getName())
                .log(System.Logger.Level.DEBUG, absentValueException::getMessage, absentValueException);
            return loader;
        }
    }

    /**
     * A representation of a <a
     * href="doc-files/terminology.html#loadrequest">load request</a>
     * for use by the {@link Loader#load(Request)} method.
     *
     * @see #builder(Class)
     *
     * @see #builder(TypeToken)
     *
     * @see Loader#load(Request)
     *
     * @see <a href="doc-files/terminology.html">Terminology</a>
     */
    public static interface Request<T> {

        // This is an example of a request qualifier: additional
        // information that might help to select a *particular* type
        // of configuration-related object.
        //
        // As our use cases come up with more scenarios for
        // qualifiers, we can evolve them here, rather than adding
        // overloaded methods to the Loader class itself.
        /**
         * Returns a (possibly {@linkplain Optional#isEmpty() empty})
         * {@link Optional} holding a {@link Locale} used to <a
         * href="doc-files/terminology.html#qualifier">qualify</a>
         * this {@link Request}.
         *
         * <p>Implementations of this method must not return {@code
         * null}.</p>
         *
         * <p>Implementations of this method must be idempotent.</p>
         *
         * <p>Implementations of this method must return a <a
         * href="doc-files/terminology.html#determinate">determinate</a>
         * value.</p>
         *
         * @return a (possibly {@linkplain Optional#isEmpty() empty})
         * {@link Optional} holding a {@link Locale} used to <a
         * href="doc-files/terminology.html#qualifier">qualify</a>
         * this {@link Request}; never {@code null}
         */
        public Optional<Locale> locale();

        // This is an example of a request qualifier: additional
        // information that might help to select a *particular* type
        // of configuration-related object.
        //
        // As our use cases come up with more scenarios for
        // qualifiers, we can evolve them here, rather than adding
        // overloaded methods to the Loader class itself.
        /**
         * Returns a (possibly {@linkplain Optional#isEmpty() empty})
         * {@link Optional} holding a {@link String} representing some
         * kind of name used to <a
         * href="doc-files/terminology.html#qualifier">qualify</a>
         * this {@link Request}.
         *
         * <p>Implementations of this method must not return {@code
         * null}.</p>
         *
         * <p>Implementations of this method must be idempotent.</p>
         *
         * <p>Implementations of this method must return a <a
         * href="doc-files/terminology.html#determinate">determinate</a>
         * value.</p>
         *
         * @return a (possibly {@linkplain Optional#isEmpty() empty})
         * {@link Optional} holding a {@link String} representing some
         * kind of name used to <a
         * href="doc-files/terminology.html#qualifier">qualify</a>
         * this {@link Request}; never {@code null}
         */
        public Optional<String> name();

        /**
         * Returns a {@link TypeToken} modeling the type of object
         * this {@link Request} is requesting.
         *
         * <p>Implementations of this method must not return {@code null}.</p>
         *
         * <p>Implementations of this method must be idempotent.</p>
         *
         * <p>Implementations of this method must return a <a
         * href="doc-files/terminology.html#determinate">determinate</a>
         * value.</p>
         *
         * @return a {@link TypeToken}; never {@code null}
         */
        public TypeToken<T> type();

        /**
         * Returns a new {@link Builder}.
         *
         * <p>This method never returns {@code null}.</p>
         *
         * @param <T> the modeled type
         *
         * @param type the type of the {@link Request} that the
         * returned {@link Builder} will {@linkplain Builder#build()
         * build}; must not be {@code null}
         *
         * @return a new {@link Builder}; never {@code null}
         *
         * @exception NullPointerException if {@code type} is {@code
         * null}
         *
         * @exception IllegalArgumentException if the supplied {@link
         * Class} has type parameters
         *
         * @see #builder(TypeToken)
         *
         * @see TypeToken#of(Class)
         */
        public static <T> Builder<T> builder(Class<T> type) {
            return builder(TypeToken.of(type));
        }

        /**
         * Returns a new {@link Builder}.
         *
         * <p>This method never returns {@code null}.</p>
         *
         * @param <T> the modeled type
         *
         * @param type the type of the {@link Request} that the
         * returned {@link Builder} will {@linkplain Builder#build()
         * build}; must not be {@code null}
         *
         * @return a new {@link Builder}; never {@code null}
         *
         * @exception NullPointerException if {@code type} is {@code
         * null}
         */
        public static <T> Builder<T> builder(TypeToken<T> type) {
            return new Builder<>(type);
        }

        /**
         * A builder of {@link Request} objects for use by the {@link
         * Loader#load(Request)} method.
         *
         * @see Request#builder(Class)
         *
         * @see Loader#load(Request)
         *
         * @see #build()
         *
         * @see <a href="doc-files/terminology.html">Terminology</a>
         */
        public static final class Builder<T> {

            private Locale locale;

            private String name;

            private final TypeToken<T> type;

            private Builder(TypeToken<T> type) {
                super();
                this.type = Objects.requireNonNull(type, "type");
            }

            /**
             * Returns a new {@link Request} implementation.
             *
             * <p>This method never returns {@code null}.</p>
             *
             * @return a new {@link Request}; never {@code null}
             *
             * @see Request
             */
            public Request<T> build() {
                Optional<Locale> locale = Optional.ofNullable(this.locale);
                Optional<String> name = Optional.ofNullable(this.name);
                return new Request<>() {
                    @Override
                    public final Optional<Locale> locale() {
                        return locale;
                    }
                    @Override
                    public final Optional<String> name() {
                        return name;
                    }
                    @Override
                    public final TypeToken<T> type() {
                        return type;
                    }
                };
            }

            /**
             * Sets this {@link Builder}'s associated {@link Locale}
             * that will further <a
             * href="doc-files/terminology.html#qualifier">qualify</a>
             * any {@link Request}s this {@link Builder} {@linkplain
             * #build() builds}.
             *
             * <p>This method never returns {@code null}.</p>
             *
             * <p>This method mutates this {@link Builder}.</p>
             *
             * @param locale the {@link Locale}; must not be {@code
             * null}
             *
             * @return this {@link Builder} itself; never {@code null}
             */
            public Builder<T> locale(Locale locale) {
                this.locale = Objects.requireNonNull(locale, "locale");
                return this;
            }

            /**
             * Sets this {@link Builder}'s associated name that will
             * further <a
             * href="doc-files/terminology.html#qualifier">qualify</a>
             * any {@link Request}s this {@link Builder} {@linkplain
             * #build() builds}.
             *
             * <p>This method never returns {@code null}.</p>
             *
             * <p>This method mutates this {@link Builder}.</p>
             *
             * @param name the name; must not be {@code null}
             *
             * @return this {@link Builder} itself; never {@code null}
             */
            public Builder<T> name(String name) {
                this.name = Objects.requireNonNull(name, "name");
                return this;
            }

        }

    }

    /**
     * An object representing the result of {@linkplain
     * Loader#load(Request) loading} a configuration-related object.
     *
     * @param <T> the modeled type of the configuration-related object
     *
     * @see #get()
     *
     * @see #determinate()
     *
     * @see Loader#load(Request)
     */
    public interface Response<T> {

        /**
         * Returns the payload of this {@link Response}.
         *
         * <p>If an invocation of the {@link #determinate()} method
         * returns {@code true}, then the implementation of this
         * method must return a <a
         * href="doc-files/terminology.html#determinate">determinate</a>
         * value.</p>
         *
         * <p>If an invocation of the {@link #determinate()} method
         * returns {@code false}, then the implementation of this
         * method may or may not return a <a
         * href="doc-files/terminology.html#determinate">determinate</a>
         * value.</p>
         *
         * <p>Implementations of this method must not return {@code
         * null}.</p>
         *
         * @return the payload; never {@code null}
         *
         * @exception NoSuchObjectException if and only if {@link
         * #determinate()} returns {@code false} and at invocation
         * time the object is found to be <a
         * href="doc-files/terminology.html#absent">absent</a>
         *
         * @exception ConfigException if an error occurs
         */
        public T get();

        /**
         * Returns {@code true} if and only if this {@link Response}
         * represents a <a
         * href="doc-files/terminology.html#determinate">determinate</a>
         * value.
         *
         * <p>If an invocation of this method returns {@code true},
         * then the implementation of the {@link #get()} method must
         * return a <a
         * href="doc-files/terminology.html#determinate">determinate</a>
         * value.</p>
         *
         * <p>If an invocation of this method returns {@code false},
         * then the implementation of the {@link #get()} method may or
         * may not return a <a
         * href="doc-files/terminology.html#determinate">determinate</a>
         * value.</p>
         *
         * <p>Implementations of this method must be idempotent.</p>
         *
         * <p>Implementations of this method must return a <a
         * href="doc-files/terminology.html#determinate">determinate</a>
         * value.  More specifically, an implementation of this method
         * must return the same value, whether {@code true} or {@code
         * false}, from all invocations of it.</p>
         *
         * @return {@code true} if and only if this {@link Response}
         * represents a <a
         * href="doc-files/terminology.html#determinate">determinate</a>
         * value
         *
         * @exception ConfigException if an error occurs
         */
        public boolean determinate();

        /**
         * A convenience method that returns a new, {@linkplain
         * #determinate() determinate} {@link Response} representing
         * the supplied object.
         *
         * <p>This method never returns {@code null}.</p>
         *
         * <p>This method is safe for concurrent use by multiple
         * threads.</p>
         *
         * @param <T> the modeled type
         *
         * @param t the object in question; must not be {@code null}
         *
         * @return a new, {@linkplain #determinate() determinate}
         * {@link Response} representing the supplied object; never
         * {@code null}
         */
        public static <T> Response<T> of(final T t) {
            Objects.requireNonNull(t, "t");
            return new Response<>() {
                @Override
                public T get() {
                    return t;
                }

                @Override
                public boolean determinate() {
                    return true;
                }
            };
        }
        
    }

}
