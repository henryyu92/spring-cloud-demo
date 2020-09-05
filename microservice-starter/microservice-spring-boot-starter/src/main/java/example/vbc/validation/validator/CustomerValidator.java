package example.vbc.validation.validator;


import example.vbc.validation.Address;
import example.vbc.validation.Customer;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * 复合对象可以注入 Validator 复用校验逻辑
 */
public class CustomerValidator implements Validator {

    private final Validator addressValidator;

    public CustomerValidator(Validator addressValidator){

        Assert.notNull(addressValidator, "The supplied [Validator] is required and must not be null.");
        Assert.isTrue(addressValidator.supports(Address.class), "The supplied [Validator] must support the validation of [Address] instances.");

        this.addressValidator = addressValidator;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Customer.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "field.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "surname", "field.required");
        Customer customer = (Customer) target;
        try{
            errors.pushNestedPath("address");
            ValidationUtils.invokeValidator(this.addressValidator, customer.getAddress(), errors);
        }finally {
            errors.popNestedPath();
        }

    }
}
