### SpringBoot 启动
SpringBoot 应用通过 ```SpringApplication.run``` 方法启动，其需要传入主资源类和 main 方法参数，其中主资源类用于指定初始化 Context 的配置，方法参数作为运行时的参数。


### 自动化配置原理
Spring Boot 自动化配置相关的源码位于 ```org.springframework.boot.autoconfigure``` 包下，在对应的 ```spring-boot-autoconfigure.jar``` 包中有一个配置文件 ```/META-INF/spring.factories``` 是 Spring Boot 自动配置的关键，该文件中列出了可以被 Spring Boot 实施自动配置的模块清单：
```properties
# Initializers
org.springframework.context.ApplicationContextInitializer=\
org.springframework.boot.autoconfigure.SharedMetadataReaderFactoryContextInitializer,\
org.springframework.boot.autoconfigure.logging.ConditionEvaluationReportLoggingListener

# Auto Configure
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration,\
org.springframework.boot.autoconfigure.aop.AopAutoConfiguration,\
org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration,\
org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration,\
#.....
```

在 Spring Boot 的应用程序中，所有动作是从 ```@SpringBootApplication``` 注解开始的，其核心功能是由 ```@EnableAutoConfiguration``` 注解提供的：
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(excludeFilters = {
		@Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
		@Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
public @interface SpringBootApplication {
// ...
}


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@AutoConfigurationPackage
@Import(AutoConfigurationImportSelector.class)
public @interface EnableAutoConfiguration {
// ...
}
```
在 ```@EnableAutoConfiguration``` 注解中通过 ```@Import``` 注解引入了 ```AutoConfigurationImportSelector``` 这个类，这个类就是处理 Spring Boot 自动化配置的：
```java
public class AutoConfigurationImportSelector
		implements DeferredImportSelector, BeanClassLoaderAware, ResourceLoaderAware,
		BeanFactoryAware, EnvironmentAware, Ordered
```
通过类定义可以发现 AutoConfigurationImportSelector 实现了 DeferredImportSelector 接口，该接口的实现类会在所有的 @Configuration 注解的 bean 处理完之后运行 ```selectImports``` 方法：
```java
AutoConfigurationMetadata autoConfigurationMetadata = AutoConfigurationMetadataLoader
		.loadMetadata(this.beanClassLoader);
AutoConfigurationEntry autoConfigurationEntry = getAutoConfigurationEntry(
		autoConfigurationMetadata, annotationMetadata);
```
其中 loadMetadata 方法用于从 ```META-INF/spring-autoconfigure-metadata.properties``` 中加载 AutoConfigurationMetadata；getAutoConfigurationEntry 加载自动配置的类：
```java
// 获取
AnnotationAttributes attributes = getAttributes(annotationMetadata);
// 加载自动配置类
List<String> configurations = getCandidateConfigurations(annotationMetadata,
		attributes);
// 去除重复的配置类
configurations = removeDuplicates(configurations);
// 获取注解中排除加载的类
Set<String> exclusions = getExclusions(annotationMetadata, attributes);
// 去除排除加载的类
checkExcludedClasses(configurations, exclusions);
configurations.removeAll(exclusions);
configurations = filter(configurations, autoConfigurationMetadata);
fireAutoConfigurationImportEvents(configurations, exclusions);
```
getCandidateConfigurations 方法从 ```META-INF/spring.factories``` 中加载到配置类：
```java
MultiValueMap<String, String> result = cache.get(classLoader);
if (result != null) {
	return result;
}

try {
    Enumeration<URL> urls = (classLoader != null ? 
        classLoader.getResources(FACTORIES_RESOURCE_LOCATION) :
        ClassLoader.getSystemResources(FACTORIES_RESOURCE_LOCATION));
    result = new LinkedMultiValueMap<>();
    // 加载文件中所有的配置
    while (urls.hasMoreElements()) {
        URL url = urls.nextElement();
        UrlResource resource = new UrlResource(url);
        Properties properties = PropertiesLoaderUtils.loadProperties(resource);
        for (Map.Entry<?, ?> entry : properties.entrySet()) {
            String factoryClassName = ((String) entry.getKey()).trim();
            for (String factoryName : StringUtils.commaDelimitedListToStringArray((String) entry.getValue())) {
                result.add(factoryClassName, factoryName.trim());
            }
        }
    }
    cache.put(classLoader, result);
    return result;
} catch (IOException ex) {
    throw new IllegalArgumentException("Unable to load factories from location [" +
        FACTORIES_RESOURCE_LOCATION + "]", ex);
}
```
从整个加载逻辑可以看到 loadProperties 方法将 ```META-INF/spring.factories``` 中的所有 K-V 形式的配置加载到 Map<String, List<String>> 并返回，经过过滤处理之后由 @Import 注解将所有的配置类注册到容器中从而实现自动配置。
### 自定义 starter
自定义 Starter 利用 Spring Boot 自动配置原理实现，自动化配置需要满足两个条件：
- Bean 注册到容器中
- 自动配置通用的类

自动配置需要在 pom.xml 中引入 spring-boot-autoconfigure：
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-autoconfigure</artifactId>
</dependency>
```
自动配置原理中是通过加载 ```META-INF/spring.factories``` 文件夹在配置类，因此需要创建 ```META-INF/spring-factories``` 文件并将自动配置类加入其中：
```properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
configuration.DsfAutoConfiguration
```
Spring Boot 会加载 DsfAutoConfiguration 中的 Bean 到容器中：
```java
@Configuration
@EnableConfigurationProperties(DSFProperties.class)
public class DsfAutoConfiguration {
    private DSFProperties dsfProperties;

    public DsfAutoConfiguration(DSFProperties dsfProperties){
        this.dsfProperties = dsfProperties;
    }

    @Bean
    @ConditionalOnMissingBean(DSFClientFactory.class)
    public DSFClientFactory dsfClientFactory(DSFProperties dsfProperties){
        return new DSFClientFactory(dsfProperties);
    }

    @Bean
    @ConditionalOnMissingBean(DSFClient.class)
    public DSFClient dsfClient(DSFClientFactory clientFactory){
        return clientFactory.newClient();
    }
}
```

自动配置实际是使用了 @Import 加载指定的类那么可以使用自定义的注解 @EnableDSF 也可以自定义自动配置：
```java
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Import(DSFBootstrapConfigure.class)
public @interface EnableDSF {
}
```
自定义注解通过 @Import 加载外部的 DSFBootstrapConfigure 并将其中的 Bean 加载到容器中：
```java
@Configuration
@EnableConfigurationProperties(DSFProperties.class)
public class DSFBootstrapConfigure {

    private DSFProperties dsfProperties;

    public DSFBootstrapConfigure(DSFProperties dsfProperties){
        this.dsfProperties = dsfProperties;
    }

    @Bean
    @ConditionalOnMissingBean(DSFClientFactory.class)
    public DSFClientFactory dsfClientFactory(DSFProperties dsfProperties){
        return new DSFClientFactory(dsfProperties);
    }

    @Bean
    @ConditionalOnMissingBean(DSFClient.class)
    public DSFClient dsfClient(DSFClientFactory clientFactory){
        return clientFactory.newClient();
    }
}
```