package jakarta.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Override the configuration member name.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD })
public @interface ConfigName {
    /**
     * The name of the configuration member name. Must not be empty.
     *
     * @return the configuration member name
     */
    String value();
}
