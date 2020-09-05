package example.vbc.validation.validator;


import example.vbc.validation.Person;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Spring 提供了 Validator 接口用于验证对象，Validator 接口和 Errors 对象一起动作，当验证失败时将错误存储在 Errors 对象
 */
public class PersonValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {

        return Person.class.isAssignableFrom(clazz);
    }

    /**
     * 校验失败时将信息注册到 Errors 对象
     */
    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "name", "name.empty");
        Person p = (Person) target;
        if (p.getAge() < 0) {
            errors.rejectValue("age", "negativevalue");
        } else if (p.getAge() > 110) {
            errors.rejectValue("age", "too.darn.old");
        }
    }
}
