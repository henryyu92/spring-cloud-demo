### 容器初始化
Spring 在实例化 AnnotationConfigApplicationContext 是开始容器的初始化工作，在实例化 AnnotationConfigApplicationContext 前先实例化父类 ConfigurableListableBeanFactory，在父类中初始化了 beanFactory：
```java
public GenericApplicationContext() {
    this.beanFactory = new DefaultListableBeanFactory();
}
```
AnnotationConfigApplicationContext 构造器中有三个方法调用：
```java
public AnnotationConfigApplicationContext() {
    // 实例化 reader 用于注册指定的类到 BeanDefinitionRegistry
    this.reader = new AnnotatedBeanDefinitionReader(this);
    // 实例化 scanner 用于扫描包下的类到 BeanDefinitionRegistry
    this.scanner = new ClassPathBeanDefinitionScanner(this);
}

public AnnotationConfigApplicationContext(Class<?>... annotatedClasses) {
    this();
    // 将指定的注解类注册到容器中
    register(annotatedClasses);
    // 初始化容器
    refresh();
}
```
#### Resource 资源定位
AnnotationConfigApplicationContext 构造函数中实例化了 reader 和 scanner 分别用于将指定类和包下的类解析为 BeanDefinition 并通过 BeanDefinitionRegistry 注册：
```java
public void register(Class<?>... annotatedClasses) {
    for (Class<?> annotatedClass : annotatedClasses) {
        // 真正实现在 doRegisterBean 中
        registerBean(annotatedClass);
    }
}


<T> void doRegisterBean(Class<T> annotatedClass, @Nullable Supplier<T> instanceSupplier, @Nullable String name,
        @Nullable Class<? extends Annotation>[] qualifiers, BeanDefinitionCustomizer... definitionCustomizers) {
    // 直接创建 BeanDefinition
    AnnotatedGenericBeanDefinition abd = new AnnotatedGenericBeanDefinition(annotatedClass);
    // @Conditional 下有些类不需要实例化
    if (this.conditionEvaluator.shouldSkip(abd.getMetadata())) {
        return;
    }

    abd.setInstanceSupplier(instanceSupplier);
    ScopeMetadata scopeMetadata = this.scopeMetadataResolver.resolveScopeMetadata(abd);
    abd.setScope(scopeMetadata.getScopeName());
    String beanName = (name != null ? name : this.beanNameGenerator.generateBeanName(abd, this.registry));

    // 处理 @Lazy  @Primary  @DependsOn  @Role  @Description 这些注解，将这些注解的属性设置到 BeanDefinition
    AnnotationConfigUtils.processCommonDefinitionAnnotations(abd);
    if (qualifiers != null) {
        for (Class<? extends Annotation> qualifier : qualifiers) {
            if (Primary.class == qualifier) {
                abd.setPrimary(true);
            }
            else if (Lazy.class == qualifier) {
                abd.setLazyInit(true);
            }
            else {
                abd.addQualifier(new AutowireCandidateQualifier(qualifier));
            }
        }
    }
    // 自定义的 BeanDefinitionCustomizer 用于自定义 BeanDefinition
    for (BeanDefinitionCustomizer customizer : definitionCustomizers) {
        customizer.customize(abd);
    }

    BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(abd, beanName);
    definitionHolder = AnnotationConfigUtils.applyScopedProxyMode(scopeMetadata, definitionHolder, this.registry);
    // 使用 BeanDefinitionRegistry 将 BeanDefinition 注册 beanFactory
    BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, this.registry);
}
```
##### BeanDefinition
##### BeanDefinitionRegistry
BeanDefinition 注册到 beanFactory 的 beanDefinitionMap 中，beanDefinitionMap 是一个 ConcurrentHashMap<String, BeanDefinition> 的数据结构，其中 key 是 beanName，value 是 BeanDefinition：
```java
// ...

if (hasBeanCreationStarted()) {
    // Cannot modify startup-time collection elements anymore (for stable iteration)
    synchronized (this.beanDefinitionMap) {
        this.beanDefinitionMap.put(beanName, beanDefinition);
        List<String> updatedDefinitions = new ArrayList<>(this.beanDefinitionNames.size() + 1);
        updatedDefinitions.addAll(this.beanDefinitionNames);
        updatedDefinitions.add(beanName);
        this.beanDefinitionNames = updatedDefinitions;
        removeManualSingletonName(beanName);
    }
}
else {
    // Still in startup registration phase
    this.beanDefinitionMap.put(beanName, beanDefinition);
    this.beanDefinitionNames.add(beanName);
    removeManualSingletonName(beanName);
}

// ...
```
#### 刷新容器
AnnotationConfigApplicationContext 中将指定的类注册到容器之后调用 refresh 方法刷新容器，该方法是整个 Spring 容器初始化以及 Spring bean 初始化的核心，该方法内调用了 12 个子方法：
- ```prepareRefresh```：主要是 properties 的处理
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

### 关闭容器