package example.vbc.validation.beanvalidation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 *  Spring 对 Bean Validation 提供了全面的支持，并且在启动的时候将 Bean Validation Provider 作为 Bean 加载到容器中，在需要使用的时候直接注入即可
 *
 */
@Configuration
public class BeanValidationProviderConfiguration {

    /**
     * 使用 LocalValidatorFactoryBean 将默认的 Validator 配置为 Spring Bean
     *
     *
     */
    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }
}
