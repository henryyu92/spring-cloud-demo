package example.vbc.bind.bean.propertyeditor.registrar;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;

/**
 * Spring 提供了几种注册自定义的 PropertyEditor 的方式：
 * - 使用 ConfigurableBeanFactory 接口的 registerCustomEditor 方法注册
 * - 使用 CustomEditorConfigurer，建议将 CustomEditorConfigurer 与 ApplicationContext 一起使用
 *
 * beanFactory 和 ApplicationContext 会自动使用多个内置的 PropertyEditor 并通过 BeanWrapper 来处理属性转换
 *
 *  向 Spring 容器注入 PropertyEditor 的另一种机制时创建和使用 PropertyEditorRegistrar，通过 CustomEditorConfigurer 接口的
 *  setPropertyEditorRegistrars 方法加入到其中
 *
 *  PropertyEditorRegistrySupport 是 PropertyEditorRegistry 的实现类，createDefaultEditors 方法中
 *
 */
public final class CustomPropertyEditorRegistrar implements PropertyEditorRegistrar {
    @Override
    public void registerCustomEditors(PropertyEditorRegistry registry) {

        // 注册 JavaBean 对应的 PropertyEditor
        registry.registerCustomEditor(ExoticType.class, new ExoticTypeEditor());

        // 可以注册多个 PropertyEditor
        // ...
    }
}
