package example.vbc.convert.formatter;

import org.springframework.format.support.FormattingConversionService;

/**
 * FormattingRegistry 接口继承了 ConverterRegistry 接口，可以用于 Formatter 和 Converter 的注册
 *
 * FormattingConversionService 实现了 FormatterRegistry 和 ConversionService 接口
 *
 * FormattingConversionFactoryBean 可以将 FormattingConversionService 作为 Bean 注入到容器
 *
 */
public class CustomFormatterService extends FormattingConversionService {
}
