package example.vbc.validation.beanvalidation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 *  Spring 对 Bean Validation 提供了全面的支持，并且在启动的时候将 Bean Validation Provider 作为 Bean 加载到容器中，在需要使用的时候直接注入即可
 *
 */
@Configuration
public class BeanValidationConfiguration {

    /**
     * LocalValidatorFactoryBean 在初始化时(afterPropertiesSet)会将默认的 Validator 配置为 Spring Bean
     *
     * LocalValidatorFactoryBean 实现了 javax.validation.ValidatorFactory 和 javax.validation.Validator，同时也实现了 org.springframework.validation.Validator 接口
     */
    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

    /**
     * 通过 MethodValidationPostProcessor 可以将方法验证功能集成到 Spring
     *
     * 使用方法验证时需要在所有的目标类上使用 @Validated 注解
     *
     */
    @Bean
    public MethodValidationPostProcessor validationPostProcessor(){
        return new MethodValidationPostProcessor();
    }
}
