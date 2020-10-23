package example.vbc.convert.formatter;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

import java.time.LocalDate;

@Configuration
public class FormatterMain {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = getContext(FormatterMain.class);

        testDateTimeFormatterAnnotation(context);

        context.close();
    }

    private static void testDateTimeFormatterAnnotation(AnnotationConfigApplicationContext context){

        // 注册默认实现的 Formatter
        context.register(FormattingConversionServiceFactoryBean.class);

        FormattingConversionService conversionService = context.getBean(FormattingConversionService.class);

        LocalDate convert = conversionService.convert("2020-10-11", LocalDate.class);
        System.out.println(convert);
    }


    private static AnnotationConfigApplicationContext getContext(Class<?>... configClass){
        return new AnnotationConfigApplicationContext(configClass);
    }
}
