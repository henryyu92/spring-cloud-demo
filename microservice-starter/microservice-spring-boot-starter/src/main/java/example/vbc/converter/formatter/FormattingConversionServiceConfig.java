package example.vbc.converter.formatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.DateFormatterRegistrar;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;

import java.time.format.DateTimeFormatter;

@Configuration
public class FormattingConversionServiceConfig {

    @Bean
    public FormattingConversionService formattingConversionService(){

        final DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();

        // 添加 @NumberFormat 支持
        conversionService.addFormatterForFieldAnnotation(new NumberFormatAnnotationFormatterFactory());

        // 注册 Formatter
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setDateFormatter(DateTimeFormatter.ofPattern("yyyyMMdd"));
        registrar.registerFormatters(conversionService);

        DateFormatterRegistrar registrar1 = new DateFormatterRegistrar();
        registrar1.setFormatter(new org.springframework.format.datetime.DateFormatter("yyyyMMdd"));
        registrar1.registerFormatters(conversionService);

        return conversionService;
    }
}
