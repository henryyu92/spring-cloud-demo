package example.vbc.validation.beanvalidation;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.BindException;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class BeanValidatorMain {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = initContext();

        testValidator(context);

    }

    private static void testValidator(ApplicationContext context){
        Validator validator = context.getBean(Validator.class);

        PersonForm personForm = new PersonForm();
        personForm.setAge(20);
        personForm.setName("");

        validator.validate(personForm, new BindException(personForm, "person"));
    }


    private static AnnotationConfigApplicationContext initContext(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BeanValidatorMain.class);

        context.register(LocalValidatorFactoryBean.class);

        closeContextBeforeExit(context);

        return context;
    }

    private static void closeContextBeforeExit(final AnnotationConfigApplicationContext context){
        Runtime.getRuntime().addShutdownHook(new Thread(context::close));
    }

}
