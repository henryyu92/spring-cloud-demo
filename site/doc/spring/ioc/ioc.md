## IoC

IoC(Inversion of Control) 是一种“控制反转”的思想，即对象的控制权(生命周期)不是由上层对象控制，而是交由统一的容器来控制，控制对象生命周期的容器也称为 IoC 容器。

IoC 容器是也是实现依赖注入(DI, Dependency Injection)的载体，IoC 容器在创建对象时自动将其依赖对象注入并管理了对象的生命周期。IoC 容器解耦了对象的耦合，在使用对象时只需要从容器中获取对象即可，而不需要关心对象之间的依赖关系。


### 容器初始化

使用 Spring IoC 容器之前需要初始化，容器初始化的过程实在 `ConfigurableApplicationContext#refresh` 方法中实现。容器初始化包括 BeanDefinition 的载入注册以及 Bean 的实例化。

```refresh``` 方法采用模板方法模式定义了容器初始化的过程，子类通过重写这些方法实现自定义的容器初始化过程：


- ```obtainFreshBeanFactory```：获取 beanFactory，在 AnnotationConfigApplicationContext 实例化时 beanFactory 已经创建，因此该方法直接返回创建的 DefaultListableBeanFactory 实例
- ```prepareBeanFactory```：对 beanFactory 中设置一些属性，其中向 beanFactory 加入了 ApplicationContextAwareProcessor
- ```postProcessBeanFactory```：对 beanFactory 的其他属性设置
- ```invokeBeanFactoryPostProcessors```：完成将 BeanDefinition 注册到 beanFactory 中并调用 beanFactoryPostProcessor 的方法 
- ```registerBeanPostProcessors```：向 beanFactory 中设置 BeanPostProcessors
- ```initMessageSource```：初始化 Message 相关的资源
- ```initApplicationEventMulticaster```：初始化容器中的 event multicaster
- ```onRefresh```：初始化容器中其他的 bean
- ```registerListeners```：注册 ApplicationListener
- ```finishBeanFactoryInitialization```：初始化 BeanDefinition 定义的非 lazy 的 Bean。在这个方法中调用 ```beanFactory.preInstantiateSingletons()```，beanFactory 遍历 beanName 得到 BeanDefinition 并进行校验后调用 ```getBean(beanName)``` 方法实例化 Bean

### 扩展点


#### FactoryBean

BeanFactory 是 Spring 的 IoC 容器，管理着 Spring 中的 bean 的生命周期；FactoryBean 是一个接口，当 IoC 容器中的 Bean 实现了 FactoryBean 后通过 getBean 方法获取的对象并不是容器中管理的实现类对象，而是这个实现类中的 getObject 方法返回的对象，要想获取 FactoryBean 的实现类需要在 bean 的名称前加上 &

```java
public class A implements FactoryBean<Student> {
    @Override
    public Student getObject() throws Exception {
        return new Student();
    }

    @Override
    public Class<?> getObjectType() {
        return Student.class;
    }
}


Object bean1 = context.getBean("studentFactoryBean");
// example.container.bean.Student@2c604965
System.out.println(bean1);

Object bean2 = context.getBean("&studentFactoryBean");
// example.container.bean.StudentFactoryBean@57f8951a
System.out.println(bean2);
```
FacotryBean 接口可以用于 AOP 代理对象的创建，在运行时创建代理对象并在代理对象的目标方法中根据业务要求织入相应的方法，这个对象在 Spring 中就是 ProxyFactoryBean

#### BeanFactoryPostProcessor

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


[Back](../../)