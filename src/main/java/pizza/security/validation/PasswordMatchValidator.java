package pizza.security.validation;

import lombok.SneakyThrows;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, String> {

    private String value;
    @Override
    public void initialize(PasswordMatch p) {
        value = p.value();
    }

    @SneakyThrows
    public boolean isValid(String password, ConstraintValidatorContext c) {
        return password.equals(value);
    }

}
