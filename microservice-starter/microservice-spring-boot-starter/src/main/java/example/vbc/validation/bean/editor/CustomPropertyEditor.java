package example.vbc.validation.bean.editor;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;

/**
 * Spring 使用 PropertyEditor 来实现对象和字符串之间的转换
 *
 * 通过自定实现 PropertyEditor 并注册到 BeanWrapper 或者 IoC 容器，可以实现字符串到特定类型的转换
 *
 * Spring 内置了大量的 PropertyEditor，都位于 org.springframework.beans.PropertyEditor 包，默认情况下大多数由 beanWrapperImpl 注册
 *
 * Spring 使用 PropertyEditorManager 为 PropertyEditor 设置 bean 的搜索路径，如果 PropertyEditor 和 bean 在同一个包中且具有相同的名称则无需显示注册而会自动发现
 */
public class CustomPropertyEditor implements PropertyEditor {
    @Override
    public void setValue(Object value) {

    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public boolean isPaintable() {
        return false;
    }

    @Override
    public void paintValue(Graphics gfx, Rectangle box) {

    }

    @Override
    public String getJavaInitializationString() {
        return null;
    }

    @Override
    public String getAsText() {
        return null;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {

    }

    @Override
    public String[] getTags() {
        return new String[0];
    }

    @Override
    public Component getCustomEditor() {
        return null;
    }

    @Override
    public boolean supportsCustomEditor() {
        return false;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {

    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {

    }
}
