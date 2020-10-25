package example.vbc.validation.beanvalidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MyConstraintValidator implements ConstraintValidator<MyConstraint, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        return value != null && !value.isEmpty();
    }
}
