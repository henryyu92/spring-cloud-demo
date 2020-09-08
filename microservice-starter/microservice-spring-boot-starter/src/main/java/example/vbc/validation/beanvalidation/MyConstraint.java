package example.vbc.validation.beanvalidation;

import javax.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义的 bean validation 注解包含两个部分：
 * - 包含 @Constraint 注解的自定义注解
 * - javax.validation.ConstraintValidator 的实现类完成具体的校验逻辑
 *
 * 每个注解都需要指定 ConstraintValidator 的实现类，在运行的时候，当遇到注解的字段时， ConstraintValidatorFactory 会实例化 ConstraintValidator 实现类
 *
 * 默认情况下，
 */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {MyConstraintValidator.class})
public @interface MyConstraint {
}
