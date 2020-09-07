package example.vbc.converter;

import org.springframework.core.CollectionFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * GenericConverter 用于转换比较复杂但是类型更加通用的类型
 */
public class ArrayToCollectionConverter implements GenericConverter {

    private final ConversionService conversionService;


    public ArrayToCollectionConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(Object[].class, Collection.class));
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {

        if (source == null) {
            return null;
        }

        int length = Array.getLength(source);
        TypeDescriptor elementDesc = targetType.getElementTypeDescriptor();
        Collection<Object> target = CollectionFactory.createCollection(targetType.getType(),
                (elementDesc != null ? elementDesc.getType() : null), length);

        if (elementDesc == null) {
            for (int i = 0; i < length; i++) {
                Object sourceElement = Array.get(source, i);
                target.add(sourceElement);
            }
        }
        else {
            for (int i = 0; i < length; i++) {
                Object sourceElement = Array.get(source, i);
                Object targetElement = this.conversionService.convert(sourceElement,
                        sourceType.elementTypeDescriptor(sourceElement), elementDesc);
                target.add(targetElement);
            }
        }
        return target;
    }
}
