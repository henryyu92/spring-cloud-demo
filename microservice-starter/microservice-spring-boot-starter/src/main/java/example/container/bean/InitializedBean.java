package example.container.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Component
public class InitializedBean implements BeanNameAware, BeanFactoryAware, ApplicationContextAware, BeanPostProcessor, InitializingBean {
    /**
     * BeanNameAware 接口方法在初始化完成之前调用
     * @param name
     */
    @Override
    public void setBeanName(String name) {
        System.out.println("step 1 is BeanNameAware 。。。。");
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("step 2 is BeanFactoryAware 。。。。");
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("step 3 is ApplicationContextAware 。。。。");
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        System.out.println("step 4 is BeanPostProcessor#postProcessBeforeInitiallization 。。。。。");

        return bean;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("step 5 is InitializingBean 。。。。");
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        System.out.println("step 6 is BeanPostProcessor#postProcessAfterInitialization 。。。。");

        Annotation[] annotations = bean.getClass().getAnnotations();
        if (annotations == null || annotations.length == 0){
            return bean;
        }

        for (Annotation an : annotations){

        }

        return bean;
    }







}
