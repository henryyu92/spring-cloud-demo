package example.vbc.bind;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;

/**
 * DataBinder 可以为目标对象设置属性值，并支持字段校验、格式化和绑定结果分析，绑定过程也可以自定义实现
 */
public class DataBinderMain {

    public static void main(String[] args) {

        dataBind();
        dataBindAndResult();
    }

    public static void dataBind(){

        final MutablePropertyValues mpv = new MutablePropertyValues();
        mpv.add("name", "Joe");
        mpv.add("salary", "200");

        Employee employee = new Employee();
        final DataBinder binder = new DataBinder(employee);

        binder.bind(mpv);

        System.out.println(employee);
    }


    public static void dataBindAndResult(){

        final MutablePropertyValues mpv = new MutablePropertyValues();
        mpv.add("name", "Joe");
        mpv.add("salary", "200x");

        Employee employee = new Employee();
        final DataBinder binder = new DataBinder(employee);

        binder.bind(mpv);
        final BindingResult result = binder.getBindingResult();

        result.getAllErrors().forEach(System.out::println);

        System.out.println(employee);
    }
}
