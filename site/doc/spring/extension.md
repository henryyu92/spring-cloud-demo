## 扩展点

Spring 在初始化容器以及在管理 Bean 的生命周期的过程中设置了多个扩展点，通过扩展点可以介入整个过程从而可以自定义的操作整个流程。

### BeanDefinitionRegistryPostProcessor

`BeanDefinitionRegistryPostProcessor` 接口继承自 `BeanFactoryPostProcessor` 接口，可以在其他 BeanPostProcessor 实现类执行之前注册更多的 BeanDefinition。
```
public interface BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor {
    // 在 bean 的定义注册后执行，可以修改或者增加已经注册的 bean
    void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException;
}
```

BeanDefinitionRegistryPostProcessor 接口的实现类在执行之前，所有常规的 `BeanDefinition` 都已经注册到 `BeanDefinitionRegistry` 中，因此可以修改在 `BeanDefinitionRegistry` 中注册的任意 `BeanDefinition`，也可以增加或者删除 `BeanDefinition`：
```
public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
    String[] names = registry.getBeanDefinitionNames();
    for (String name : names){
        System.out.println("BeanDefinitionNames: " + name);
    }
}
```

Spring 中 `org.springframework.context.annotation.ConfigurationClassPostProcessor` 是用这种方式来将注解 @Configuration 中的生成 bean 的方法所对应的 `BeanDefinition` 进行注册。

### BeanFactoryPostProcessor

`BeanFactoryPostProcessor` 可以在 BeanFactory 初始化完毕，即资源定位、加载、解析并注册 BeanDefinition 之后但在 Bean 初始化前对 BeanFactory 进行修改。ApplicationContext 会在创建其他 Bean 之前自动应用这些 BeanFactoryPostProcessor。
```
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
```

### InstantiationAwareBeanPostProcessor

```InstantiationAwareBeanPostProcessor``` 接口继承自 ```BeanPostProcessor```,该接口的实现类的方法会在 bean 的实例化前后调用，也就是说在调用该接口的方法时 Bean 对象还并未完成属性注入等初始化，可以通过复写方法实现代理、自定义实例化等，一般是通过继承 ```InstantiationAwareBeanPostProcessorAdapter``` 实现。

- ```postProcessBeforeInstantiation```：在 bean 实例化之前调用，可以返回一个代理对象。如果返回的对象不为 null 则会打断 bean 的实例化及后续的过程，之后对该返回对象的唯一处理是 ```BeanPostProcessors#postProcessAfterInitialization``` 方法，默认返回 null
- ```postProcessAfterInstantiation```：在 bean 实例化之后且属性赋值之前调用，默认返回 true

```java
```

### BeanPostProcessor

```BeanPostProcessor``` 接口允许修改 bean 的实例，其中 ```postProcessBeforeInitialization``` 在 Bean 实例化并且属性赋值完成之后但是在自定义的初始化方法和属性注入方法之前调用，```postProcessAfterInitialization``` 在 Bean 实例化完成注册到容器暴露给外部之前调用。

```java
```


### Aware

- BeanNameAware
- ApplicationContextAware
- BeanFactoryAware
- ResourceLoaderAware


**[Back](../)**