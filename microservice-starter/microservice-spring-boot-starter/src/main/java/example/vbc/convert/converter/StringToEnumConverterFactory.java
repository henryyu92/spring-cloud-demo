package example.vbc.convert.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

public final class StringToEnumConverterFactory implements ConverterFactory<String, Enum> {
    @Override
    public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToEnumConverter<>(targetType);
    }

    private final class StringToEnumConverter<T extends Enum> implements Converter<String, T> {

        private Class<T> enumType;

        public StringToEnumConverter(Class<T> enumType) {
            this.enumType = enumType;
        }

        @Override
        public T convert(String source) {
            return (T) Enum.valueOf(this.enumType, source.trim());
        }
    }


    private static final class StringToEnumConverterFactoryTest{

        private enum  TestEnum{
            ONE, TOW;

        }

        public static void main(String[] args) {

            final StringToEnumConverterFactory factory = new StringToEnumConverterFactory();
            final Converter<String, TestEnum> converter = factory.getConverter(TestEnum.class);
            final TestEnum hello = converter.convert("ONE");
            System.out.println(hello);
        }
    }

}
