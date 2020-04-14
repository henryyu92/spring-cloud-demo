
### ImportSelector
ImportSelector 接口只定义了一个 selectImports 方法用于指定需要注册为 bean 的 Class 名称，其实现类通常和 @Import 注解一起使用。当在 @Import 中指定了 ImportSelector 实现类后会将实现类方法中返回的所有 Class 名称都定义为 bean。

ImportSelector 的实现类可以实现多个 Aware 接口，这些接口相应的方法会在 ImportSelector 对应的方法之前执行：
- EnvironmentAware
- BeanFactoryAware
- BeanClassLoaderAware
- ResourceLoaderAware

以自动配置中的 ```AutoConfigurationImportSelector``` 为例：
```java
public class AutoConfigurationImportSelector
		implements DeferredImportSelector, BeanClassLoaderAware, ResourceLoaderAware,
		BeanFactoryAware, EnvironmentAware, Ordered {
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        if (!isEnabled(annotationMetadata)) {
            return NO_IMPORTS;
        }
        AutoConfigurationMetadata autoConfigurationMetadata = AutoConfigurationMetadataLoader.loadMetadata(this.beanClassLoader);
        AutoConfigurationEntry autoConfigurationEntry = getAutoConfigurationEntry(autoConfigurationMetadata, annotationMetadata);
        return StringUtils.toStringArray(autoConfigurationEntry.getConfigurations());
    }
	
    // ...
}
```
可以基于 ImportSelector 和 @Import 注解自定义注解用于删选需要注册为 bean 的 Class：
```java
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(CustomImportSelector.class)
public @interface CustomAnnotation{
  // ...
}
```
定义注解需要包含几个内置注解：
- @Documented：表示注解可以使用 javadoc 生成文档
- @Retention：表示注解生命周期，可选值为 RetentionPolicy.SOURCE(只在源码中有效)、RetentionPolicy.CLASS(编译后依然有效，但是运行时无效)、RetentionPolicy.RUNTIME(运行时依然有效)
- @Target：表示注解可以使用的范围，可选值为：
  - ElementType.TYPE：类、接口、enum
  - ElementType.FIELD：类成员变量
  - ElementType.METHOD：类方法
  - ElementType.PARAMETER：参数
  - ElementType.CONSTRUCTOR：构造器
  - ElementType.LOCAL_VARIABLE：本地变量
  - ElementType.ANNOTATION_TYPE：注解
  - ElementType.PACKAGE：包
  - ElementType.TYPE_PARAMETER：Type 参数
  - ElementType.TYPE_USE
#### DeferredImportSelector
DeferredImportSelector 实现了 ImportSelector，将在所有 @Configuration 注解的类的 bean 处理完成之后执行，通常用于处理 @Import 注解和 @Conditional 注解同时存在的情况。
### ImportBeanDefinitionRegistrar
这个接口用于向容器中注册额外的 BeanDefinition，从而实现动态向容器中注入 bean。@ImportBeanDefinitionRegistrar 通常可以和 @Configuration 注解和 @Import 注解一起使用。

ImportBeanDefinitionRegistrar 的实现类可以实现多个 Aware 接口且对应的实现方法在 ImportBeanDefinitionRegistrar 实现方法之前执行：
- EnvironmentAware
- BeanFactoryAware
- BeanClassLoaderAware
- ResourceLoaderAware

可以从 ```@EnableAutoConfiguration``` 中的 ```EnableConfigurationPropertiesImportSelector``` 看到 ImportBeanDefinitionRegistrar 将 @ConfigurationProperties 对应的类注册进容器：
```java
class EnableConfigurationPropertiesImportSelector implements ImportSelector {

    private static final String[] IMPORTS = {
		ConfigurationPropertiesBeanRegistrar.class.getName(),
		ConfigurationPropertiesBindingPostProcessorRegistrar.class.getName() };

    @Override
    public String[] selectImports(AnnotationMetadata metadata) {
        return IMPORTS;
    }
	
    // ...
}

public static class ConfigurationPropertiesBeanRegistrar implements ImportBeanDefinitionRegistrar {
			
    // ...
}
```

### @Configuration
表示类中使用 @Bean 注解的一个或多个方法的返回类会被容器注册为 bean，@Configuration 注解的类如果在包扫描范围内会在容器启动时自动将 @Bean 注解生成的类注册到容器中。@Configuration 是 Spring 提供的基础的注解：
```java
@Configuration
public class KafkaBootstrapConfiguration {

    @SuppressWarnings("rawtypes")
    @Bean(name = KafkaListenerConfigUtils.KAFKA_LISTENER_ANNOTATION_PROCESSOR_BEAN_NAME)
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public KafkaListenerAnnotationBeanPostProcessor kafkaListenerAnnotationProcessor() {
        return new KafkaListenerAnnotationBeanPostProcessor();
    }

    @Bean(name = KafkaListenerConfigUtils.KAFKA_LISTENER_ENDPOINT_REGISTRY_BEAN_NAME)
    public KafkaListenerEndpointRegistry defaultKafkaListenerEndpointRegistry() {
        return new KafkaListenerEndpointRegistry();
    }

}
```
### @ConfigurationProperties
注解用于外部配置，在类上或者在 @Configuration 注解类中的 @Bean 方法上添加这个注解就可以将类和外部的 Properties 类绑定。
```java
@ConfigurationProperties(prefix="spring.redis")
public class RedisProperties{
    private int database = 0;
    private String url;
    private String host = "localhost";
    private String password;
    private int port = 6379;
    private boolean ssl;
    private Duration timeout;
    private Sentinel sentinel;
    private Cluster cluster;
    private final Jedis jedis = new Jedis();
    private final Lettuce lettuce = new Lettuce();
	
    // getter & setter
	
    public static class Pool{
        private int maxIdle = 8;
        private int minIdel = 0;
        private int maxActive = 8;
        private Duration maxWait = Duration.ofMillis(-1);
		
        // setter & getter
    }
	
	public static class Cluster{
		private List<String> nodes;
		private Integer maxRedirects;
		
		// setter & getter
	}
	
	public static class Sentinel{
		private String master;
		private List<String> nodes;
		
		// getter & setter
	}
	
	public static class Jedis{
		private Pool pool;
		
		// setter & getter
	}
	
	public static class Lettuce{
		private Duration shutdownTimeout = Duration.ofMillis(100);
		private Pool pool;
		
		// setter & getter
	}
	
}
```
### @EnableConfigurationProperties
该注解可以将 @ConfigurationProperties 注解的类注册到容器中：
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(EnableConfigurationPropertiesImportSelector.class)
public @interface EnableConfigurationProperties {
    
	Class<?>[] value() default {};
}
```
该注解通过 @Import 的 ```EnableConfigurationPropertiesImportSelector``` 类将所有 value() 方法返回的类注册到容器中。

可以从 spring-boot-starter-data-redis 中的自动配置看到 ```@EnableAutoConfiguration``` 注解的用法：
```java
@Configuration
@ConfitionalOnClass(RedisOperations.class)
@EnableConfigurationProperties(RedisProperties.class)
@Import({LettuceConnectionConfiguration.class, JedisConnectionConfiguration.class})
public class RedisAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(name = "redisTemplate")
	public RedisTemplate<Object, Object> redisTemplate(
			RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
		RedisTemplate<Object, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		return template;
	}

	@Bean
	@ConditionalOnMissingBean
	public StringRedisTemplate stringRedisTemplate(
			RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
		StringRedisTemplate template = new StringRedisTemplate();
		template.setConnectionFactory(redisConnectionFactory);
		return template;
	}

}
```
### @Bean
用于类的方法上，常和 @Configuration 一起使用，用于生成 Sparing 容器管理的 bean 实例：
```java
@Configuration
@EnableConfigurationProperties(RedisConfigProperties.class)
public class RedisConfig {

	@Autowired
	private RedisConfigProperties properties;

	@Bean(destroyMethod = "shutdown")
	public DefaultClientResources clientResources(){
		DefaultClientResources resources = DefaultClientResources.create();
		return resources;
	}

	@Bean(destroyMethod = "shutdown")
	public RedisClient redisClient(){
		RedisURI redisURI = RedisURI.builder().withHost(properties.getHost()).withPort(properties.getPort()).build();
		RedisClient redisClient = RedisClient.create(redisURI);
		return redisClient;
	}

	@Bean(destroyMethod = "close")
	public StatefulRedisConnection redisConnection(RedisClient redisClient){
		StatefulRedisConnection<String, String> connect = redisClient.connect();
		return connect;
	}
}
```

### @Import
使用 @Configuration 注解类并将类放到可以被扫描到的 package 中可以使 Spring Boot 自动注册类中的 @Bean 注解生成的类到容器中；如果 @Configuration 注解的类不在可以被扫描到的 package 中时可以使用 @Import 引入。

@Import 注解有三种方式指定需要注册为 bean 的类：
- 指定有 @Configuration 的类
- 指定 ImportBeanDefinitionRegistrar 接口的实现类
- 指定 ImportSelector 接口的实现类

Spring Boot 自动配置中的 EnableXXX 注解就是通过 @Import 注解实现：
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(KafkaBootstrapConfiguration.class)
public @interface EnableKafka {
}
```