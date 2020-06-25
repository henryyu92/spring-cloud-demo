## 资源

Spring 容器在初始化时需要将 Bean 的描述资源加载到上下文中并解析成 BeanDefinition 对象注册到容器中，Bean 资源以 Resource 为抽象，资源的加载使用 ResourceLoader，而将资源解析为 BeanDefinition 并注册到容器中使用的是 BeanDefinitionReader。

#### Resource
`Resource` 接口继承 `InputStreamSource` 接口，是 Spring 框架中所有资源的抽象。该接口提供了对资源描述的方法并由抽象类 `AbstractResource` 提供默认实现，继承自 `InputStreamSource` 的 `getInputStream` 是获取资源数据流的方法，不同的资源通过重写该方法定义不同的方式获取资源数据流。

在 `Resource` 体系中，不同的资源提供了不同的实现类：
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

##### BeanDefinitionRegistry
BeanDefinition 注册到 beanFactory 的 beanDefinitionMap 中，beanDefinitionMap 是一个 ConcurrentHashMap<String, BeanDefinition> 的数据结构，其中 key 是 beanName，value 是 BeanDefinition：
```
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
