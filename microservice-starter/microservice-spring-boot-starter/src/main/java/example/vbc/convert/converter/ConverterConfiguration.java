package example.vbc.convert.converter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;

import java.util.Collections;

@Configuration
public class ConverterConfiguration {

    /**
     * 如果没有注册 ConversionService，Spring 会使用基于 PropertyEditor 的类型转换系统
     *
     * ConversionServiceFactoryBean 创建时注入 DefaultConversionService，默认会注册多个 Converter
     *
     */
    @Bean
    public ConversionServiceFactoryBean conversionService(){

        final ConversionServiceFactoryBean conversionService = new ConversionServiceFactoryBean();

        return conversionService;
    }

}
