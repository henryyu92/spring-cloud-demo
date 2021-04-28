## BeanDefinition

## Bean 生命周期

Spring Bean 整个生命周期交由 Spring 容器管理，Spring Bean 在容器中从创建到销毁总共经历了 4 个阶段：

- 实例化(Instantiation)
- 属性赋值(Populate)
- 初始化(Initialization)
- 销毁(Destruction)

Spring 容器的 getBean 方法是实例化 bean 的入口，真正的实例化逻辑在 ``` AbstractAutowireCapableBeanFactory#doCreateBean```方法中：

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

### 扩展点

Spring 在初始化容器以及在管理 Bean 的生命周期的过程中设置了多个扩展点，通过扩展点可以介入整个过程从而可以自定义的操作整个流程。

``

#### `BeanPostProcessor`

```BeanPostProcessor``` 接口允许修改 bean 的实例，其中 ```postProcessBeforeInitialization``` 在 Bean 实例化并且属性赋值完成之后但是在自定义的初始化方法和属性注入方法之前调用，```postProcessAfterInitialization``` 在 Bean 实例化完成注册到容器暴露给外部之前调用。

- `InstantiationAwareBeanPostProcessor`
  - ```postProcessBeforeInstantiation```：在 bean 实例化之前调用，可以返回一个代理对象。如果返回的对象不为 null 则会打断 bean 的实例化及后续的过程，之后对该返回对象的唯一处理是 ```BeanPostProcessors#postProcessAfterInitialization``` 方法，默认返回 null
  - ```postProcessAfterInstantiation```：在 bean 实例化之后且属性赋值之前调用，默认返回 true
- `BeanPostProcessor`
  - ```postProcessBeforeInitialization```：在 Bean 实例化并且属性赋值完成之后但是在自定义的初始化方法和属性注入方法之前调用
  - ```postProcessAfterInitialization```：在 Bean 实例化完成注册到容器暴露给外部之前调用

```InstantiationAwareBeanPostProcessor``` 接口继承自 ```BeanPostProcessor```,该接口的实现类的方法会在 bean 的实例化前后调用，也就是说在调用该接口的方法时 Bean 对象还并未完成属性注入等初始化，可以通过复写方法实现代理、自定义实例化等，一般是通过继承 ```InstantiationAwareBeanPostProcessorAdapter``` 实现。

- ```postProcessBeforeInstantiation```：在 bean 实例化之前调用，可以返回一个代理对象。如果返回的对象不为 null 则会打断 bean 的实例化及后续的过程，之后对该返回对象的唯一处理是 ```BeanPostProcessors#postProcessAfterInitialization``` 方法，默认返回 null
- ```postProcessAfterInstantiation```：在 bean 实例化之后且属性赋值之前调用，默认返回 true

#### `InstantiationAwareBeanPostProcessor`

`InstantiationAwareBeanPostProcess` 扩展了 `BeanPostProcessor`。

#### `Aware`

- `BeanNameAware`：当前初始化的 bean 如果实现了 BeanNameAware 接口则调用 `setBeanName`向其传入 beanName 
- `BeanClassLoaderAware`：当前初始化的 bean 如果实现 BeanClassLoaderAware 接口则调用 `setClassLoader` 方法向其传入 classLoader
- `BeanFactoryAware`：当前初始化的 bean 如果实现 BeanFactoryAware 接口则调用 setBeanFactory 方法向其传入 beanFactory
- `EnvironmentAware`
- `ResourceLoaderAware`
- `ApplicationEventPublisherAware`
- `ApplicationContextAware`

#### `InitializeBean`

`InitializingBean` 在 bean 属性赋值完成之后调用 `afterProperties` 方法

#### `DisposableBean`

### 循环依赖

循环依赖是指 A 依赖 B 并且 B 依赖 A，Spring 通过定义三级缓存解决了循环依赖问题，Spring 只能解决 setter 方法注入的循环依赖，不能解决构造器注入的循环依赖问题。

```
// 一级缓存，存储实例化完成 bean 的实例
private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

// 二级缓存，存放未初始化的 bean
private final Map<String, Object> earlySingletonObjects = new HashMap<>(16);

// 三级缓存，存放 bean 工厂
private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16);

```

Spring 三级缓存解决循环依赖的流程：

- 对象 A 在初始化时先实例化了对象，并将自己提前曝光在 singletonFactories 中，然后在调用 setter 方法注入依赖对象 B 时需要初始化对象 B
- 对象 B 在初始化时先实例化了对象，并将自己提前曝光在 singletonFactories 中，然后在调用 setter 方法注入依赖对象 A 时需要初始化对象 A
- 对象 A 在初始化时可以从 singletonFactories 中获取到，并将获取到的 bean 放入 earlySingletonObjects 中然后返回，此时对象 B 的 setter 方法注入依赖对象 A 可以完成，对象 B 的整个初始化过程可以完成，将对象 B 放入 singletonObjects 集合中并从 earlySingletonObjects 中移除
- 对象 B 的初始化完成后，对象 A 的 setter 方法注入就可以完成，对象 A 的整个初始化就可以完成了，此时需要将对象 A 从 earlySingletonObjects 集合移入 singletonObjects 集合中