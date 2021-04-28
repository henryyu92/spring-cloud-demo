package example.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

import java.util.Objects;

/**
 * Generic Events, 使用 ResolvableTypeProvider 解决范型擦除问题
 */
public class EntityCreatedEvent<T> extends ApplicationEvent implements ResolvableTypeProvider {

    private final T entity;

    public EntityCreatedEvent(Object source, T entity) {
        super(source);
        this.entity = entity;
    }

    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(getClass(), ResolvableType.forInstance(entity));
    }


    public static void main(String[] args) {
        EntityCreatedEvent<Object> event = new EntityCreatedEvent<>(new Object(), new Object());
        System.out.println(event.getClass() + ", " + event.getSource());
        System.out.println(Objects.requireNonNull(event.getResolvableType()).getType().getTypeName());
        System.out.println(event.getResolvableType().getGeneric(0).getType());
        System.out.println(event.getResolvableType().getRawClass());
    }
}
