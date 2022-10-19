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

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import java.util.Objects;

/**
 * A holder of a modeled {@link Type} that embodies <a
 * href="http://gafter.blogspot.com/2006/12/super-type-tokens.html"
 * target="_parent">Gafter's gadget</a>.
 *
 * <p>To use this class, create a new instance of an anonymous
 * subclass of it, and then call {@link #type() type()} on it.  For
 * example:</p>
 *
 * <blockquote><pre>
 * // type will be a {@linkplain ParameterizedType} whose {@linkplain ParameterizedType#getRawType() rawType} is {@linkplain java.util.List List.class} and
 * // whose {@linkplain ParameterizedType#getActualTypeArguments() sole type argument} is {@linkplain String String.class}
 * {@linkplain Type} type = new {@linkplain TypeToken TypeToken}&lt;{@linkplain java.util.List List}&lt;{@linkplain String}&gt;&gt;() {}.{@linkplain #type() type()};
 * assert type instanceof {@linkplain ParameterizedType};
 * assert (({@linkplain ParameterizedType})type).{@linkplain ParameterizedType#getRawType() getRawType()} == {@linkplain java.util.List List.class};
 * assert (({@linkplain ParameterizedType})type).{@linkplain ParameterizedType#getActualTypeArguments() getActualTypeArguments()}[0] == {@linkplain String String.class};</pre></blockquote>
 *
 * @param <T> the modeled type; often {@linkplain ParameterizedType
 * parameterized}
 *
 * @see #type()
 */
public abstract class TypeToken<T> {


    /*
     * Instance fields.
     */


    private final Type type;


    /*
     * Constructors.
     */


    /**
     * Creates a new {@link TypeToken}.
     */
    protected TypeToken() {
        super();
        this.type = mostSpecializedParameterizedSuperclass(this.getClass()).getActualTypeArguments()[0];
    }

    private TypeToken(final Type t) {
        super();
        this.type = Objects.requireNonNull(t, "t");
    }


    /*
     * Instance methods.
     */


    /**
     * Returns the {@link Type} modeled by this {@link TypeToken}.
     *
     * <p>This method never returns {@code null}.</p>
     *
     * <p>This method produces a <a
     * href="doc-files/terminology.html#determinate">determinate</a>
     * value.</p>
     *
     * <p>This method is idempotent.</p>
     *
     * <p>This method is safe for concurrent use by multiple
     * threads.</p>
     *
     * @return the {@link Type} modeled by this {@link TypeToken};
     * never {@code null}
     */
    public final Type type() {
        return this.type;
    }

    /**
     * Returns the {@linkplain #erase(Type) type erasure} of this {@link
     * TypeToken}'s {@linkplain #type() modeled <code>Type</code>}, or
     * {@code null} if erasing the {@link Type} would result in a
     * non-{@link Class} erasure (in which case the erasure is simply
     * the {@link Type} itself), or if an erasure cannot be determined.
     *
     * <p>This method never returns {@code null}.</p>
     *
     * <p>This method produces a <a
     * href="doc-files/terminology.html#determinate">determinate</a>
     * value.</p>
     *
     * <p>This method is idempotent.</p>
     *
     * <p>This method is safe for concurrent use by multiple
     * threads.</p>
     *
     * @return the {@linkplain #erase(Type) type erasure} of this {@link
     * TypeToken}'s {@linkplain #type() modeled <code>Type</code>}, or
     * {@code null} if erasing the {@link Type} would result in a
     * non-{@link Class} erasure, or if an erasure cannot be determined
     */
    public final Class<?> erase() {
        return erase(this.type());
    }

    /**
     * Returns a hashcode for this {@link TypeToken} computed from the
     * {@link Type} it {@linkplain #type() models}.
     *
     * <p>This method produces a <a
     * href="doc-files/terminology.html#determinate">determinate</a>
     * value.</p>
     *
     * <p>This method is idempotent.</p>
     *
     * <p>This method is safe for concurrent use by multiple
     * threads.</p>
     *
     * @return a hashcode for this {@link TypeToken}
     *
     * @see #equals(Object)
     */
    @Override // Object
    public int hashCode() {
        Type type = this.type();
        return type == null ? 0 : type.hashCode();
    }

    /**
     * Returns {@code true} if the supplied {@link Object} is equal to
     * this {@link TypeToken}.
     *
     * <p>This method returns {@code true} if the supplied {@link
     * Object}'s {@linkplain Object#getClass() class} is this {@link
     * TypeToken}'s class and if its {@linkplain #type() modeled
     * <code>Type</code>} is equal to this {@link TypeToken}'s
     * {@linkplain #type() modeled <code>Type</code>}.</p>
     *
     * <p>This method produces a <a
     * href="doc-files/terminology.html#determinate">determinate</a>
     * value.</p>
     *
     * <p>This method is idempotent.</p>
     *
     * <p>This method is safe for concurrent use by multiple
     * threads.</p>
     *
     * @param other the {@link Object} to test; may be {@code null} in
     * which case {@code false} will be returned
     *
     * @return {@code true} if the supplied {@link Object} is equal to
     * this {@link TypeToken}; {@code false} otherwise
     *
     * @see #hashCode()
     */
    @Override // Object
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof TypeToken<?>) {
            return Objects.equals(this.type(), ((TypeToken<?>)other).type());
        } else {
            return false;
        }
    }

    /**
     * Returns a {@link String} representation of this {@link
     * TypeToken}.
     *
     * <p>This method returns a value equal to that returned by {@link
     * Type#getTypeName() this.type().getTypeName()}.</p>
     *
     * <p>This method never returns {@code null}.</p>
     *
     * <p>This method produces a <a
     * href="doc-files/terminology.html#determinate">determinate</a>
     * value.</p>
     *
     * <p>This method is idempotent.</p>
     *
     * <p>This method is safe for concurrent use by multiple
     * threads.</p>
     *
     * @return a {@link String} representation of this {@link
     * TypeToken}; never {@code null}
     */
    @Override // Object
    public String toString() {
        Type type = this.type();
        return type == null ? "null" : type.getTypeName();
    }

    /**
     * Returns the type erasure for the supplied {@link Type} according
     * to <a
     * href="https://docs.oracle.com/javase/specs/jls/se11/html/jls-4.html#jls-4.6"
     * target="_parent">the rules of the Java Language
     * Specification, section 4.6</a>.
     *
     * <ul>
     *
     * <li>If {@code null} is supplied, {@code null} is returned.</li>
     *
     * <li>If a {@link Class} is supplied, the {@link Class} is
     * returned.</li>
     *
     * <li>If a {@link ParameterizedType} is supplied, the result of
     * invoking {@link #erase(Type)} on its {@linkplain
     * ParameterizedType#getRawType() raw type} is returned.</li>
     *
     * <li>If a {@link GenericArrayType} is supplied, the result of
     * invoking {@link Object#getClass()} on an invocation of {@link
     * java.lang.reflect.Array#newInstance(Class, int)} with the return
     * value of an invocation of {@link #erase(Type)} on its {@linkplain
     * GenericArrayType#getGenericComponentType() generic component
     * type} and {@code 0} as its arguments is returned.</li>
     *
     * <li>If a {@link TypeVariable} is supplied, the result of invoking
     * {@link #erase(Type)} <strong>on its {@linkplain
     * TypeVariable#getBounds() first (leftmost) bound}</strong> is
     * returned (if it has one) or {@link Object Object.class} if it
     * does not.  <strong>Any other bounds are ignored.</strong></li>
     *
     * <li>If a {@link WildcardType} is supplied, the result of invoking
     * {@link #erase(Type)} <strong>on its {@linkplain
     * WildcardType#getUpperBounds() first upper bound}</strong> is
     * returned.  <strong>Any other bounds are ignored.</strong></li>
     *
     * <li>If any other {@link Type} implementation is supplied, {@code
     * null} is returned.</li>
     *
     * </ul>
     *
     * <p>This method may return {@code null}.</p>
     *
     * <p>This method produces a <a
     * href="doc-files/terminology.html#determinate">determinate</a>
     * value.</p>
     *
     * <p>This method is idempotent.</p>
     *
     * <p>This method is safe for concurrent use by multiple
     * threads.</p>
     *
     * @param type the {@link Type} for which the corresponding type
     * erasure is to be returned; may be {@code null} in which case
     * {@code null} will be returned
     *
     * @return a {@link Class}, or {@code null} if a suitable {@link
     * Class}-typed type erasure could not be determined, indicating
     * that the type erasure is the supplied {@link Type} itself
     */
    private static final Class<?> erase(Type type) {
        // https://docs.oracle.com/javase/specs/jls/se11/html/jls-4.html#jls-4.6
        // 4.6. Type Erasure
        //
        // Type erasure is a mapping from types (possibly including
        // parameterized types and type variables) to types (that are
        // never parameterized types or type variables). We write |T|
        // for the erasure of type T. The erasure mapping is defined
        // as follows:
        //
        // The erasure of a parameterized type (§4.5) G<T1,…,Tn> is
        // |G|.
        //
        // The erasure of a nested type T.C is |T|.C.
        //
        // The erasure of an array type T[] is |T|[].
        //
        // The erasure of a type variable (§4.4) is the erasure of its
        // leftmost bound.
        //
        // The erasure of every other type is the type itself.
        if (type == null) {
            return null;
        } else if (type instanceof Class<?>) {
            return erase((Class<?>)type);
        } else if (type instanceof ParameterizedType) {
            return erase((ParameterizedType)type);
        } else if (type instanceof GenericArrayType) {
            return erase((GenericArrayType)type);
        } else if (type instanceof TypeVariable<?>) {
            return erase((TypeVariable<?>)type);
        } else if (type instanceof WildcardType) {
            return erase((WildcardType)type);
        } else {
            return null;
        }
    }

    private static final Class<?> erase(Class<?> type) {
        // https://docs.oracle.com/javase/specs/jls/se11/html/jls-4.html#jls-4.6
        // …
        // The erasure of a nested type T.C is
        // |T|.C. [Class.getDeclaringClass() returns an already erased
        // type.]
        //
        // The erasure of an array type T[] is |T|[]. [A Class that is
        // an array has a Class as its component type, and that is
        // already erased.]
        // …
        // The erasure of every other type is the type itself. [So in all
        // cases we can just return the supplied Class<?>.]
        return type;
    }

    private static final Class<?> erase(ParameterizedType type) {
        // https://docs.oracle.com/javase/specs/jls/se11/html/jls-4.html#jls-4.6
        // …
        // The erasure of a parameterized type (§4.5) G<T1,…,Tₙ> is
        // |G| [|G| means the erasure of G, i.e. the erasure of
        // type.getRawType()].
        return erase(type.getRawType());
    }

    private static final Class<?> erase(GenericArrayType type) {
        // https://docs.oracle.com/javase/specs/jls/se11/html/jls-4.html#jls-4.6
        //
        // The erasure of an array type T[] is |T|[]. [|T| means the
        // erasure of T. We erase the genericComponentType() and use
        // Class#arrayType() to find the "normal" array class for the
        // erasure.]
        Class<?> componentType = erase(type.getGenericComponentType());
        if (componentType == null) {
            return null;
        } else {
            // Needs Java 17
            // return componentType.arrayType();

            // (Java 17's Class#arrayType() does exactly this behind the scenes.)
            return Array.newInstance(componentType, 0).getClass();
        }
    }

    private static final Class<?> erase(TypeVariable<?> type) {
        // https://docs.oracle.com/javase/specs/jls/se11/html/jls-4.html#jls-4.6
        //
        // The erasure of a type variable (§4.4) is the erasure of its
        // leftmost bound. [In the case of a TypeVariable<?> that
        // returns multiple bounds, we know they will start with a
        // class, not an interface and not a type variable.]
        Type[] bounds = type.getBounds();
        return bounds.length > 0 ? erase(bounds[0]) : Object.class;
    }

    private static final Class<?> erase(WildcardType type) {
        // https://docs.oracle.com/javase/specs/jls/se11/html/jls-4.html#jls-4.6
        //
        // The erasure of a type variable (§4.4) is the erasure of its
        // leftmost bound.  [WildcardTypes aren't really in the JLS
        // per se but they behave like type variables. Only upper
        // bounds will matter here.]
        Type[] bounds = type.getUpperBounds();
        return bounds != null && bounds.length > 0 ? erase(bounds[0]) : Object.class;
    }


    /*
     * Static methods.
     */


    /**
     * Returns a new {@link TypeToken} representing the supplied
     * <strong>non-generic</strong> {@link Class}.
     *
     * <p>This method never returns {@code null}.</p>
     *
     * <p>This method is safe for concurrent use by multiple
     * threads.</p>
     *
     * @param <T> the modeled type
     *
     * @param c the <strong>non-generic</strong> class in question;
     * must not be {@code null}; must return a zero-length array from
     * any invocation of its {@link Class#getTypeParameters()} method
     *
     * @return a new {@link TypeToken}; never {@code null}
     *
     * @exception NullPointerException if {@code c} is {@code null}
     *
     * @exception IllegalArgumentException if {@code c} is a generic
     * {@link Class}
     */
    public static final <T> TypeToken<T> of(final Class<T> c) {
        if (c.getTypeParameters().length > 0) {
            throw new IllegalArgumentException();
        }
        return new TypeToken<>(c) {};
    }

    private static final ParameterizedType mostSpecializedParameterizedSuperclass(Type type) {
        if (type == null || type == Object.class || type == TypeToken.class) {
            return null;
        } else {
            Class<?> erasure = erase(type);
            if (erasure == null || erasure == Object.class || !(TypeToken.class.isAssignableFrom(erasure))) {
                return null;
            } else if (type instanceof ParameterizedType) {
                return (ParameterizedType)type;
            } else {
                return mostSpecializedParameterizedSuperclass(erasure.getGenericSuperclass());
            }
        }
    }

}
