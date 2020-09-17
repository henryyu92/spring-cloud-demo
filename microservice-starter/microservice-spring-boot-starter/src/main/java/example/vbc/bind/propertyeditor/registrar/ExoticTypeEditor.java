package example.vbc.bind.propertyeditor.registrar;

import java.beans.PropertyEditorSupport;

/**
 *  当将 bean 属性设置为字符串时，Spring IoC 容器最终使用 JavaBean PropertyEditor 的实现类将字符串转换成复杂的属性类型
 *
 *  Spring 预先注册了许多自定义的 PropertyEditor 实现，此外 JavaBean 规范的查找机制允许在同一个包中找到 JavaBean 对应的 PropertyEditor
 *
 */
public class ExoticTypeEditor extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(new ExoticType(text.toUpperCase()));
    }
}
