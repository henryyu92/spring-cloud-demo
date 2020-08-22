package example.container.bean;

import example.container.bean.StudentFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

@Component
public class DefaultBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String[] names = beanFactory.getBeanDefinitionNames();
        for (String name : names){
            BeanDefinition bd = beanFactory.getBeanDefinition(name);
            String className = bd.getBeanClassName();
            if (className != null && className.equalsIgnoreCase(StudentFactoryBean.class.getName())){
                bd.setAttribute("hello", "world");
            }
        }
    }
}
