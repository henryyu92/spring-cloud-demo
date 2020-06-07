## IoC

IoC(Inversion of Control) 是一种“控制反转”的思想，即对象的控制权(生命周期)不是由上层对象控制，而是交由统一的容器来控制，控制对象生命周期的容器也称为 IoC 容器。

IoC 容器是也是实现依赖注入(DI, Dependency Injection)的载体，IoC 容器在创建对象时自动将其依赖对象注入并管理了对象的生命周期。IoC 容器解耦了对象的耦合，在使用对象时只需要从容器中获取对象即可，而不需要关心对象之间的依赖关系。

### IoC 容器

Spring IoC 容器有两种方式实现依赖注入：
- 构造器注入：在创建对象的构造器中传入依赖的对象实现依赖对象的注入
- Setter 方法注入：通过调用对象的 setter 方法实现依赖对象的注入

Spring 提供了 BeanFactory 和 ApplicationContext 两类容器，BeanFactory 作为基础的容器提供了 IoC 容器的基本功能，而 ApplicationContext 在提供了容器功能的基础上引入了 Resource 支持不同的资源以及 ApplicationEventPublisher 支持事件机制。

Spring 通过 BeanDefinition 来定义对象以及其之间的相互依赖关系，Spring IoC 容器创建管理对象前需要加载资源并转换成 BeanDefinition。

#### Resource
```Resource``` 接口继承 ```InputStreamSource``` 接口，是 Spring 框架中所有资源的抽象。该接口提供了对资源描述的方法并由抽象类 ```AbstractResource``` 提供默认实现，继承自 ```InputStreamSource``` 的 ```getInputStream``` 是获取资源数据流的方法，不同的资源通过重写该方法定义不同的方式获取资源数据流。

在 Resource 体系中，不同的资源提供了不同的实现类：
- ```FileSystemResource```：对 ```java.io.File``` 类型资源的封装，使用 NIO 进行读写交互
- ```ByteArrayResource```：对字节数组类型资源的封装，当调用 ```getInputStream``` 方法获取数据流时会根据字节数组构造 ```ByteArrayInputStream```
- ```UrlResource```：对 ```java.net.URL``` 类型资源的封装，内部使用 URL 获取数据流
- ```ClassPathResource```：classPath 类型资源的实现，使用给定的 ClassLoader 或者 Class 获取资源数据流
- ```InputStreamResource```：将给定的 InputStream 封装资源

#### ResourceLoader
Spring 将资源定义和资源加载分离开，```Resource``` 接口定义了资源，```ResourceLoader``` 接口定义了资源的加载。```ResourceLoader``` 是 Spring 中资源加载的抽象，具体资源的加载需要对应的资源加载器完成。```ResourceLoader``` 提供了两个接口方法：
- ```getResource```：根据指定资源路径返回 Resource 实例，返回的 Resource 实例不保证资源存在，需要调用 ```Resource#exists``` 方法确认资源存在
- ```getClassLoader```：返回 ```ResourceLoader``` 使用的类加载器

DefualResourceLoader 作为默认实现，实现的 ```getResource``` 接口根据指定的 location 返回对应的 Resource

#### BeanDefinitionReader

BeanDefinitionReader 将加载的 Resource 资源转换为对应的 BeanDefinition 并注册到容器中，容器通过注册 BeanDefinition 创建对象及其依赖对象。


#### 容器初始化

Spring 将指定位置的资源加载并解析成 BeanDefinition 然后注册到容器中，然后通过反射技术将 BeanDefinition 描述的对象实例化并注入依赖对象，完成整个容器的初始化。

#### Resource 定位
Resource 定位指的是 BeanDefinition 的资源定位，由 ResourceLoader 加载资源生成 Resource，这个 Resource 提供了获取 BeanDefinition 资源的接口。

Spring 在实例化 ```AnnotationConfigApplicationContext``` 时开始容器的初始化工作，在实例化 ```AnnotationConfigApplicationContext``` 前先实例化父类 ```ConfigurableListableBeanFactory```，在父类中初始化了 beanFactory：
```java
public GenericApplicationContext() {
    this.beanFactory = new DefaultListableBeanFactory();
}
```
```AnnotationConfigApplicationContext``` 无参构造函数中，初始化了 reader 和 scanner 分别用于将指定的类和包下的类解析为 BeanDefinition：
```java
public AnnotationConfigApplicationContext() {
    // 实例化 reader 用于注册指定的类到 BeanDefinitionRegistry
    this.reader = new AnnotatedBeanDefinitionReader(this);
    // 实例化 scanner 用于扫描包下的类到 BeanDefinitionRegistry
    this.scanner = new ClassPathBeanDefinitionScanner(this);
}
```
在 ```AnnotationConfigApplicationContext``` 的构造函数中包含三个方法的调用，分别用于初始化 ApplicaitonContext，将当前传入的配置类加载到 IoC 容器中，刷新 IoC 容器：
```java

public AnnotationConfigApplicationContext(Class<?>... annotatedClasses) {
    this();
    // 将指定的注解类注册到容器中
    register(annotatedClasses);
    // 初始化容器
    refresh();
}
```
在 ApplicationConfigApplication 调用 refresh 方法之前，Spring 实例化了 IoC 容器，此时部分内置的 BeanDefinition 注册到了容器中，通过注解方式定义的 BeanDefinition 还没有解析注册到容器中，注解方式定义的 Bean 的 BeanDefinition 是在 refresh 方法中注册到容器。

#### BeanDefinition 载入
通过 Resource 提供的接口获取 BeanDefinition 资源并将转换成 BeanDefinition 结构。

#### BeanDefinition 注册
把解析得到的 BeanDefinition 向 IoC 容器注册，其本质是将 BeanDefinition 存储到 IoC 容器中的一个 HashMap 中去。

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

容器初始化指定了 scnner 和 reader 并且实例化了 beanFactory 之后，就会调用 ```AbstractApplicationContext``` 的 refresh 方法刷新容器。IoC 容器刷新时会将定义的 Bean 从资源文件中加载解析成 BeanDefinition 并注册到容器中，是 Spring 容器初始化的核心。

refresh 方法是一个模板方法，整个方法内部调用了 12 个子方法，每个方法完成容器刷新过程中各自的工作：

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


### BeanFactory 和 FactoryBean

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