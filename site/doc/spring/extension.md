## 扩展点

### BeanDefinitionRegistryPostProcessor

```BeanDefinitionRegistryPostProcessor``` 接口继承自 ```BeanFactoryPostProcessor``` 接口，可以在其他 BeanPostProcessor 实现类执行之前注册更多的 BeanDefinition。
```java
public interface BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor {
    // 在 bean 的定义注册后执行，可以修改或者增加已经注册的 bean
    void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException;
}
```

```BeanDefinitionRegistryPostProcessor``` 接口的实现类在执行之前，所有常规的 ```BeanDefinition``` 都已经注册到 ```BeanDefinitionRegistry``` 中，因此可以修改在 ```BeanDefinitionRegistry``` 中注册的任意 ```BeanDefinition```，也可以增加或者删除 ```BeanDefinition```：
```java
public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
    String[] names = registry.getBeanDefinitionNames();
    for (String name : names){
        System.out.println("BeanDefinitionNames: " + name);
    }
}
```

Spring 中 ```org.springframework.context.annotation.ConfigurationClassPostProcessor``` 是用这种方式来将注解 @Configuration 中的生成 bean 的方法所对应的 BeanDefinition 进行注册。

### BeanFactoryPostProcessor




**[Back](../)**