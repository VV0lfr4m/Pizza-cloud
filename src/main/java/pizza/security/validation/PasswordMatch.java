package pizza.security.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy=PasswordMatchValidator.class)
@Documented
public @interface PasswordMatch {
    String message() default "Password doesn't match";

    String value();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
