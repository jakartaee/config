package jakarta.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// TODO - Should we leave this as is, or add ways to accept other defaults types (ints, booleans, etc)
/**
 * Specify the default value of a configuration member.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface ConfigDefault {
    /**
     * The default value of the member.
     *
     * @return the default value as a <code>String</code>
     */
    String value();
}
