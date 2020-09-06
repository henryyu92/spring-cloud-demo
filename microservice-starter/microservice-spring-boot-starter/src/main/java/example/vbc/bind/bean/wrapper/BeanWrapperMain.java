package example.vbc.bind.bean.wrapper;

import example.vbc.bind.bean.Company;
import example.vbc.bind.bean.Employee;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.PropertyValue;

public class BeanWrapperMain {

    public static void main(String[] args) {

        BeanWrapper company = new BeanWrapperImpl(new Company());

        // 设置属性值
        company.setPropertyValue("name", "Some Company Inc.");

        PropertyValue value = new PropertyValue("name", "Some Company Inc.");
        company.setPropertyValue(value);

        // 设置嵌入属性
        BeanWrapper employee = new BeanWrapperImpl(new Employee());
        employee.setPropertyValue("name", "Jim Stravinsky");
        company.setPropertyValue("managingDirector", employee.getWrappedInstance());

        // 获取嵌入属性值
        Float salary = (Float) company.getPropertyValue("managingDirector.salary");

        System.out.println(company.getPropertyValue("name"));
        System.out.println(company.getPropertyValue("managingDirector.name"));

    }
}
