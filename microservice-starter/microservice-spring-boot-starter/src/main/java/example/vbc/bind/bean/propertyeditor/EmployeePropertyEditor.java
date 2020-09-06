package example.vbc.bind.bean.propertyeditor;

import example.vbc.bind.bean.Employee;

import java.beans.PropertyEditorSupport;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * Spring 使用 PropertyEditor 来实现对象和字符串之间的转换
 *
 * 通过自定实现 PropertyEditor 并注册到 BeanWrapper 或者 IoC 容器，可以实现字符串到特定类型的转换
 *
 * Spring 内置了大量的 PropertyEditor，都位于 org.springframework.beans.propertieditors 包，默认情况下大多数由 beanWrapperImpl 注册
 *
 * Java bean 规范使用 PropertyEditorManager 为 PropertyEditor 设置 bean 的搜索路径，如果 PropertyEditor 和 bean 在同一个包中且具有相同的名称则无需显示注册而会自动发现
 */
public class EmployeePropertyEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {

        try {
            Employee employee = parse(text);
            setValue(employee);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getAsText() {
        return super.getAsText();
    }

    // {name:"hello", salary: 12.0}
    private Employee parse(String text) throws NoSuchMethodException {
        Employee employee = new Employee();
        String[] split = text.split(",");
        for (String field : split){
            String[] kv = field.split(":");
            String methodName = "Set" + kv[0].trim().substring(0,1).toUpperCase() + kv[0].trim().substring(1);
            Arrays.stream(Employee.class.getMethods()).forEach(method -> {
                if (methodName.equals(method.getName())){
                    try {
                        method.invoke(employee, kv[1]);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return employee;
    }
}
