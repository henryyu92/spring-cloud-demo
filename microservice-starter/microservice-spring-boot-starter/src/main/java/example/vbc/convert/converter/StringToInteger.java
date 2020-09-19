package example.vbc.convert.converter;

import org.springframework.core.convert.converter.Converter;

/**
 *  Converter 可以作为 PropertyEditor 的替代方案，可以将字符串转换成所需的类型
 *
 */
public final class StringToInteger implements Converter<String, Integer> {
    @Override
    public Integer convert(String source) {
        return Integer.valueOf(source);
    }
}
