package example.vbc.convert.converter;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Set;

/**
 * ConditionalConverter 用于在特定条件下才运行的转换器
 */
public class IdToEntityConverter implements ConditionalGenericConverter {

    private final ConversionService conversionService;


    public IdToEntityConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(Object.class, Object.class));
    }

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        Method finder = getFinder(targetType.getType());
        return (finder != null &&
                this.conversionService.canConvert(sourceType, TypeDescriptor.valueOf(finder.getParameterTypes()[0])));
    }

    @Override
    @Nullable
    public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (source == null) {
            return null;
        }
        Method finder = getFinder(targetType.getType());
        Assert.state(finder != null, "No finder method");
        Object id = this.conversionService.convert(
                source, sourceType, TypeDescriptor.valueOf(finder.getParameterTypes()[0]));
        return ReflectionUtils.invokeMethod(finder, source, id);
    }

    @Nullable
    private Method getFinder(Class<?> entityClass) {
        String finderMethod = "find" + getEntityName(entityClass);
        Method[] methods;
        boolean localOnlyFiltered;
        try {
            methods = entityClass.getDeclaredMethods();
            localOnlyFiltered = true;
        }
        catch (SecurityException ex) {
            // Not allowed to access non-public methods...
            // Fallback: check locally declared public methods only.
            methods = entityClass.getMethods();
            localOnlyFiltered = false;
        }
        for (Method method : methods) {
            if (Modifier.isStatic(method.getModifiers()) && method.getName().equals(finderMethod) &&
                    method.getParameterCount() == 1 && method.getReturnType().equals(entityClass) &&
                    (localOnlyFiltered || method.getDeclaringClass().equals(entityClass))) {
                return method;
            }
        }
        return null;
    }

    private String getEntityName(Class<?> entityClass) {
        String shortName = ClassUtils.getShortName(entityClass);
        int lastDot = shortName.lastIndexOf('.');
        if (lastDot != -1) {
            return shortName.substring(lastDot + 1);
        }
        else {
            return shortName;
        }
    }

}
