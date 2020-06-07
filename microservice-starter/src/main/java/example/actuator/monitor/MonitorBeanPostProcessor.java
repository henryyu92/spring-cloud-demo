package example.actuator.monitor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class MonitorBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        Class<?> clazz = bean.getClass();

        for (Method method : clazz.getMethods()){
            Monitored monitored = method.getAnnotation(Monitored.class);
            if (monitored != null){
                if (clazz.getInterfaces().length == 0){
                    throw new BeanCreationException("@Monitor 修饰的对象需要实现接口");
                }
                return getProxy(bean);
            }
        }

        return bean;
    }

    private Object getProxy(Object bean){
        return new JdkProxyCreator(bean).getProxy();
    }
}
