package example.vbc.convert.converter;

import org.springframework.core.convert.support.ConversionServiceFactory;
import org.springframework.core.convert.support.GenericConversionService;

import java.util.Set;

/**
 *  GenericConversionService 实现了 ConversionService 和 ConverterRegistry，用于注册 Converter 并完成类型转换
 */
public class CustomConversionService extends GenericConversionService {


    public void addConverters(Set<?> converters){
        ConversionServiceFactory.registerConverters(converters, this);
    }
}
