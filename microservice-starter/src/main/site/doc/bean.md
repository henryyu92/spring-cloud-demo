### Spring Bean 生命周期
Spring Bean 整个生命周期交由 Spring 容器管理，Spring Bean 在容器中从创建到销毁总共经历了 4 个阶段：
- 实例化(Instantiation)
- 属性赋值(Populate)
- 初始化(Initialization)
- 销毁(Destruction)

Spring 容器的 getBean 方法是实例化 bean 的入口，真正的实例化逻辑在 ``` AbstractAutowireCapableBeanFactory#doCreateBean```方法中：
```java
protected Object doCreateBean(final String beanName, final RootBeanDefinition mbd, final @Nullable Object[] args)
        throws BeanCreationException {

    // Instantiate the bean.
    BeanWrapper instanceWrapper = null;
    if (mbd.isSingleton()) {
        instanceWrapper = this.factoryBeanInstanceCache.remove(beanName);
    }
    if (instanceWrapper == null) {
        instanceWrapper = createBeanInstance(beanName, mbd, args);
    }
    final Object bean = instanceWrapper.getWrappedInstance();
    Class<?> beanType = instanceWrapper.getWrappedClass();
    if (beanType != NullBean.class) {
        mbd.resolvedTargetType = beanType;
    }
    
    // 省略部分代码 ...

    // Initialize the bean instance.
    Object exposedObject = bean;
    try {
        // bean 属性赋值
        populateBean(beanName, mbd, instanceWrapper);
        // 初始化 bean，初始完之后的 bean 即可暴露到容器外
        exposedObject = initializeBean(beanName, exposedObject, mbd);
    }
    catch (Throwable ex) {
        if (ex instanceof BeanCreationException && beanName.equals(((BeanCreationException) ex).getBeanName())) {
            throw (BeanCreationException) ex;
        }
        else {
            throw new BeanCreationException(
                    mbd.getResourceDescription(), beanName, "Initialization of bean failed", ex);
        }
    }

    // 省略部分代码 ...

    return exposedObject;
}
```
#### Spring 生命周期扩展点
Spring 生命周期的扩展点主要分为两类：
- ```BeanPostProcessor``` 接口：BeanPostProcessor 的实现类会切入到多个 Bean 的生命周期中，自动注入以及 AOP 的实现都和 BeanPostProcessor 有关
- Aware 接口和生命周期接口：这些接口的实现类只有在当前 Bean 初始化的时候才会被调用，常用于自定义扩展

Spring Bean 的整个生命周期过程中有大量的扩展点作用，因此 Spring Bean 的整个生命周期如图：
```
+----------------+
| BeanDefinition |
+----------------+
         |
         |<---- InstantiationAwareBeanPostProcessor#postProcessBeforeInstantiation
         |                         
+----------------------------+             
| 实例化(createBeanInstance) |            
+----------------------------+           
         |
         |<---- InstantiationAwareBeanPostProcessor#postProcessAfterInstantiation
         |
+-----------------------+
| 属性赋值(populateBean) |
+-----------------------+
         |<---- BeanNameAware, BeanClassLoaderAware, BeanFactoryAware, ApplicationContextAware
         |<---- BeanPostProcessor#postProcessBeforeInitialization
         |
+-----------------------+
| 初始化(initializeBean) |
+-----------------------+
         |
         |<---- InitializingBean#afterProperties
         |<---- BeanPostProcessor#postProcessAfterInitialization
         |
+--------------------+
| 销毁(destroyBeans) |
+--------------------+
```

#### BeanPostProcessor 接口
BeanPostProcessor 接口可以自定义修改 bean 的实例，ApplicationContext 会自动检测到 beanDefinition 中的 BeanPostProcessor 并将其应用到后续所有的 bean 的创建过程。BeanPostProcessor 接口定义两个方法：
- ```postProcessBeforeInitialization```：在 bean 初始化之前调用，
- ```postProcessAfterInitialization```：在 bean 初始化之后调用

InstantiationAwareBeanPostProcessor 接口是 BeanPostProcessor 接口的子接口，该接口的实现类的方法会在 bean 的实例化前后调用：
- ```postProcessBeforeInstantiation```：在 bean 实例化之前调用，可以返回一个代理对象。如果返回的对象不为 null 则会打断 bean 的实例化及后续的过程，之后对该返回对象的唯一处理是 ```BeanPostProcessors#postProcessAfterInitialization``` 方法，默认返回 null
- ```postProcessAfterInstantiation```：在 bean 实例化之后且属性赋值之前调用，默认返回 true

AbstractAutowireCapableBeanFactory 的 doCreateBean 方法是在 createBean 方法中调用的，在调用 doCreateBean 方法创建 bean 之前会调用 ```InstantiationAwareBeanPostProcessor#postProcessBeforeInstantiation``` 方法创建 bean 的代理类：
```java
protected Object createBean(String beanName, RootBeanDefinition mbd, @Nullable Object[] args)
        throws BeanCreationException {
    
    // 省略部分代码 ...

    try {
        // 在调用 doCreateBean 方法前调用 InstantiationAwareBeanPostProcessor 类的 
        // postProcessBeforeInstantiation 方法
        // Give BeanPostProcessors a chance to return a proxy instead of the target bean instance.
        Object bean = resolveBeforeInstantiation(beanName, mbdToUse);
        // 如果 postProcessBeforeInstantiation 方法返回代理类则直接返回
        if (bean != null) {
            return bean;
        }
    }
    catch (Throwable ex) {
        throw new BeanCreationException(mbdToUse.getResourceDescription(), beanName,
                "BeanPostProcessor before instantiation of bean failed", ex);
    }

    try {
        // 调用 doCreateBean 创建 Bean 的实例
        Object beanInstance = doCreateBean(beanName, mbdToUse, args);
        if (logger.isTraceEnabled()) {
            logger.trace("Finished creating instance of bean '" + beanName + "'");
        }
        return beanInstance;
    }
    catch (BeanCreationException | ImplicitlyAppearedSingletonException ex) {
        // A previously detected exception with proper bean creation context already,
        // or illegal singleton state to be communicated up to DefaultSingletonBeanRegistry.
        throw ex;
    }
    catch (Throwable ex) {
        throw new BeanCreationException(
                mbdToUse.getResourceDescription(), beanName, "Unexpected exception during bean creation", ex);
    }
}


protected Object resolveBeforeInstantiation(String beanName, RootBeanDefinition mbd) {
    Object bean = null;
    if (!Boolean.FALSE.equals(mbd.beforeInstantiationResolved)) {
        // Make sure bean class is actually resolved at this point.
        if (!mbd.isSynthetic() && hasInstantiationAwareBeanPostProcessors()) {
            Class<?> targetType = determineTargetType(beanName, mbd);
            if (targetType != null) {
                // 在实例化 bean 前调用 InstantiationAwareBeanPostProcessor 接口的 
                // postProcessorsBeforeInstantiation 方法获取代理类
                bean = applyBeanPostProcessorsBeforeInstantiation(targetType, beanName);
                if (bean != null) {
                    bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
                }
            }
        }
        mbd.beforeInstantiationResolved = (bean != null);
    }
    return bean;
}
```
resolveBeforeInstantiation 方法中调用了 ```InstantiationAwareBeanPostProcessor#postProcessorsBeforeInstantiation``` 方法，该方法可以返回一个代理类替代原始的 bean，这是实现 AOP 的关键。

InstantiationAwareBeanPostProcessor 的 postProcessAfterInstantiation 方法则是在实例化 bean 之后且属性赋值之前调用。在 doCreateBean 方法中首先调用 createBeanInstance 实例化 bean，然后调用 populateBean 方法为实例化 bean 的属性赋值：
```java
protected void populateBean(String beanName, RootBeanDefinition mbd, @Nullable BeanWrapper bw) {

    // 省略部分代码 ...

    // Give any InstantiationAwareBeanPostProcessors the opportunity to modify the
    // state of the bean before properties are set. This can be used, for example,
    // to support styles of field injection.
    boolean continueWithPropertyPopulation = true;

    if (!mbd.isSynthetic() && hasInstantiationAwareBeanPostProcessors()) {
        for (BeanPostProcessor bp : getBeanPostProcessors()) {
            if (bp instanceof InstantiationAwareBeanPostProcessor) {
                InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor) bp;
                // 如果 InstantiationAwareBeanPostProcessor 的 
                // postProcessAfterInstantiation 方法返回 false 则会阻断属性赋值
                if (!ibp.postProcessAfterInstantiation(bw.getWrappedInstance(), beanName)) {
                    continueWithPropertyPopulation = false;
                    break;
                }
            }
        }
    }

    if (!continueWithPropertyPopulation) {
        return;
    }


    // 省略属性赋值代码 ...
    
```
属性赋值的 populateBean 方法中首先调用 ```InstantiationAwareBeanPostProcessor#postProcessAfterInstantiation``` 方法控制是否需要进行属性赋值，当 postProcessAfterInstantiation 方法返回 false 时，continueWithPropertyPopulation 设置为 false，populateBean 方法直接返回。


BeanPostProcessor 的其他实现类会在 bean 初始化前后调用，在 bean 的初始化方法 initializeBean 中：
```java
protected Object initializeBean(final String beanName, final Object bean, @Nullable RootBeanDefinition mbd) {
    
    // 省略部分代码 ...
    else {
        // 调用部分 Aware 接口的方法
        invokeAwareMethods(beanName, bean);
    }

    Object wrappedBean = bean;
    if (mbd == null || !mbd.isSynthetic()) {
        // 调用 BeanPostProcessor 的 postProcessBeforeInitialization 方法
        wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
    }

    try {
        // 调用 InitializingBean 的 afterPropertiesSet 方法或者 bean 指定的 init-method 方法
        invokeInitMethods(beanName, wrappedBean, mbd);
    }
    catch (Throwable ex) {
        throw new BeanCreationException(
                (mbd != null ? mbd.getResourceDescription() : null),
                beanName, "Invocation of init method failed", ex);
    }
    if (mbd == null || !mbd.isSynthetic()) {
        // 调用 BeanPostProcessor 的 postProcessAfterInitialization 方法
        wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
    }

    return wrappedBean;
}
```
初始化 bean 方法 initializeBean 中首先调用 invokeInitMethods 方法调用了部分 Aware 接口的方法：
- ```BeanNameAware#setBeanName```：当前初始化的 bean 如果实现了 BeanNameAware 接口则向其传入 beanName 
- ```BeanClassLoaderAware#setBeanClassLoader```：当前初始化的 bean 如果实现 BeanClassLoaderAware 接口则向其传入 classLoader
- ```BeanFactoryAware```：当前初始化的 bean 如果实现 BeanFactoryAware 接口则向其传入 beanFactory

invokeAwareMethods 方法执行完之后会调用 BeanPostProcessor 的 postProcessBeforeInitialization 方法，Spring 容器在创建 bean 之前已经在容器中注册了 ```ApplicationContextAwareProcessor``` 向实现了 EnvironmentAware, EmbeddedValueResolverAware, ResourceLoaderAware, ApplicationEventPublisherAware, MessageSourceAware, ApplicationContextAware 这些接口的 bean 中传入了 ApplicationContext：
```java
public Object postProcessBeforeInitialization(final Object bean, String beanName) throws BeansException {
    
    // 省略部分代码 ...
    
    invokeAwareInterfaces(bean);

    return bean;
}

// 向实现了接口的 bean 中传入 ApplicationContext
private void invokeAwareInterfaces(Object bean) {
    if (bean instanceof Aware) {
        if (bean instanceof EnvironmentAware) {
            ((EnvironmentAware) bean).setEnvironment(this.applicationContext.getEnvironment());
        }
        if (bean instanceof EmbeddedValueResolverAware) {
            ((EmbeddedValueResolverAware) bean).setEmbeddedValueResolver(this.embeddedValueResolver);
        }
        if (bean instanceof ResourceLoaderAware) {
            ((ResourceLoaderAware) bean).setResourceLoader(this.applicationContext);
        }
        if (bean instanceof ApplicationEventPublisherAware) {
            ((ApplicationEventPublisherAware) bean).setApplicationEventPublisher(this.applicationContext);
        }
        if (bean instanceof MessageSourceAware) {
            ((MessageSourceAware) bean).setMessageSource(this.applicationContext);
        }
        if (bean instanceof ApplicationContextAware) {
            ((ApplicationContextAware) bean).setApplicationContext(this.applicationContext);
        }
    }
}
```
BeanPostProcessor 的 postProcessBeforeInitialization 方法执行完后调用 invokeInitMethods 方法，如果当前 bean 实现了 InitializingBean 接口则会调用 ```InitializingBean#afterProperties``` 方法，如果当前 bean 指定了 init-method 方法则会调用相应的方法进行初始化。

invokeInitMethods 方法执行完之后需要执行 BeanPostProcessor 的 postProcessAfterInitialization 方法。

容器在销毁 bean 之前会获取所有实现了 DisposableBean 接口的方法，然后遍历调用 destroy 方法执行销毁前的一些逻辑。

### EventListener 和 EventObject
EventListener 所有事件监听器的基础接口，EventObject 是所有事件的基础类。一类事件监听器监听者一类事件：
#### ApplicationListener
ApplicationContext 事件机制的观察者设计模式实现，当 ApplicationContext 发布 ApplicationEvent 事件时会触发 ApplicationListener 的 onApplicationEvent 方法
#### ApplicationEvent
- ContextRefreshEvent：ApplicationContext 初始化或者刷新时发布该事件
- ContextStartedEvent：使用 ConfigurableApplicaionContext 接口中的 start 方法启动 ApplicationContext 时发布该事件
- ContextClosedEvent：使用 ConfigurableApplicaionContext 接口中的 close 方法关闭 ApplicationContext 时发布该事件，此时容器已经关闭不能被刷新或重启
- ContextStoppedEvent：使用 ConfigurableApplicaionContext 接口中的 stop 方法停止 ApplicationContext 时发布该事件
```java
public class CustomApplicationListener implements ApplicationListener<ApplicationEvent>{
    @Override
    public void onApplicationEvent(event ApplicationEvent){
        if(event instanceof ContextStartedEvent){
            System.out.println("ContextStartedEvent 事件发生");
        }
        if(event instanceof ContextStoppedEvent){
            System.out.println("ContextStoppedEvent 事件发生");
        }
        if(event instanceof ContextRefreshEvent){
            System.out.println("ContextRefreshEvent 事件发生");
        }
        if(event instanceof ContextClosedEvent){
            System.out.println("ContextClosedEvent 事件发生");
        }
    }
}
```
#### Spring Bean 循环依赖
#### 创建单例 bean