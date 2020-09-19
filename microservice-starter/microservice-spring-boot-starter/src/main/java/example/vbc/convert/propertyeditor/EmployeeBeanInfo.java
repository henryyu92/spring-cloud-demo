package example.vbc.convert.propertyeditor;

import example.vbc.bind.Employee;
import org.springframework.beans.propertyeditors.CustomNumberEditor;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.SimpleBeanInfo;

/**
 * JavaBean 规范通过 PropertyEditor 定义设置 JavaBean 属性的方法，通过 BeanInfo 描述 JavaBean 的属性描述
 *
 * BeanInfo 和 JavaBean 通过命名确定关系，命名规范为 <BeanName>BeanInfo，JavaBean 对应的 BeanInfo 可以为 JavaBean 指定 PropertyEditor
 *
 * 如果 BeanInfo 没有为对应的 JavaBean 指定 PropertyEditor，则使用 PropertyEditorManager 中注册的默认 PropertyEditor
 *
 * Spring BeanWrapperImpl 扩展了 PropertyEditorRegistrySupport 类，该类为常见的属性类型提供了默认的 PropertyEditor
 */
public class EmployeeBeanInfo extends SimpleBeanInfo {

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {

        try{
            final PropertyEditor numberPE = new CustomNumberEditor(Float.class, true);
            PropertyDescriptor ageDescriptor = new PropertyDescriptor("salary", Employee.class){
                @Override
                public PropertyEditor createPropertyEditor(Object bean) {
                    return numberPE;
                };
            };

            return new PropertyDescriptor[]{ageDescriptor};
        }catch (IntrospectionException ex){
            throw new Error(ex.toString());
        }
    }
}
