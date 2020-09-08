package example.vbc.validation.beanvalidation;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * javax.validation.Validator  的实现类实现对注解的字段的校验
 *
 * javax.validation.ValidatorFactory 的实现类作为 Validator 的工厂类
 */
public class PersonForm {

    @NotNull
    @Size(max = 64)
    private String name;
    @Min(0)
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}
