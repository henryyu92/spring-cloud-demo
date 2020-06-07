package example.actuator.monitor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkProxyCreator implements InvocationHandler {

    private Object target;

    public JdkProxyCreator(Object target){
        this.target = target;
    }

    public Object getProxy(){
        Class<?> clazz = target.getClass();
        // 生成代理对象
        return Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getAnnotation(Monitored.class) != null){
            QpsCollector.collect(method.getName());
        }



        return method.invoke(target, args);
    }
}
