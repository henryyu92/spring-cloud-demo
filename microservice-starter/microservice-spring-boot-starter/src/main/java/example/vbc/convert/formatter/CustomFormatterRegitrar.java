package example.vbc.convert.formatter;

import org.springframework.format.FormatterRegistrar;
import org.springframework.format.FormatterRegistry;

/**
 * FormatterRegistrar 用于通过 FormatterRegistry 注册 Formatter 和 Converter
 *
 * 当为给定的类别(如日期格式) 注册多个 Formatter 和 Converter 时可以使用 FormatterRegistrar
 */
public class CustomFormatterRegitrar implements FormatterRegistrar {
    @Override
    public void registerFormatters(FormatterRegistry registry) {

    }
}
