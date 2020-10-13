## 数据绑定

数据绑定用于将用户的输入动态的绑定到程序的领域模型(domain model)，也就是允许在目标对象中设置属性值。Spring 提供了 `DataBinder` 实现数据绑，并且提供了字段校验、格式化和绑定结果分析。

```java
public static void dataBind(){

    final MutablePropertyValues mpv = new MutablePropertyValues();
    mpv.add("name", "Joe");
    mpv.add("salary", "200");

    Employee employee = new Employee();
    final DataBinder binder = new DataBinder(employee);

    binder.bind(mpv);

    System.out.println(employee);
}
```

`DataBinder` 提供了绑定结果分析的功能，使用 `DataBinder#getBindingResult` 可以获取绑定的结果对象 `BindingResult`，其中包含绑定失败的错误信息。`DataBinder` 在绑定失败时并不会抛出异常，而是指定为目标对象的默认值，因此使用 `DataBinder` 时需要手动检测 `BindingResult` 中是否存在 `Error` 并处理：

```java
public static void dataBindAndResult(){

    final MutablePropertyValues mpv = new MutablePropertyValues();
    mpv.add("name", "Joe");
    mpv.add("salary", "200x");	// 转换错误

    Employee employee = new Employee();
    final DataBinder binder = new DataBinder(employee);

    binder.bind(mpv);
    final BindingResult result = binder.getBindingResult();
    
    // print bind error
    result.getAllErrors().forEach(System.out::println);
    
    // Employee{name='Joe', salary=0.0}
    System.out.println(employee);
}
```

https://www.cnblogs.com/yourbatman/p/11212092.html

### `DataBinder`

### `PropertyEditor`

``PropertyEditors` 是 JavaBeans 规范的一部分，可以实现对象和字符串之间的转换。`DataBinder` 和 `BeanWrapper` 使用 `PropertyEditorSupport` 实现解析和格式化属性值。 

实现自定义的 `PropertyEditor` 并注册到 `BeanWrapper` 或者注册到 IoC 容器中就可以实现自定义的数据类型转换。Spring 内置了大量的 `PropertyEditor` 的实现类，其中大多数都注册到了 `BeanWrapperImpl`

- `CustomBooleanEditor`：将字符串转换为 Boolean 对象
- `CustomDateEditor`：按照指定的 `DataFormat` 将字符串转换为 `Date` 对象
- `CustomNumberEditor`
- `CustomCollectionEditor`
- ...

`PropertyEditorSupport` 是 `PropertyEditor` 的实现类，通过继承 `PropertyEditorSupport` 可以构建自定义的属性编辑器：

```java

```

JavaBean 规范使用 `PropertyEditorManager` 为指定类型设置 `PropertyEditor` 的搜索路径，`PropertyEditorManager` 的默认搜索路径为 `sun.beans.editors`。如果 `PropertyEditor` 类与所处理的类在同一个包中并且名字相同，则 JavaBeans 规范会自动发现 `PropertyEditor`类。

```java
// 获取默认的搜索路径
String[] searchPath = PropertyEditorManager.getEditorSearchPath();

// 获取类型对应的 PropertyEditor
PropertyEditor editor = PropertyEditorManager.findEditor(Boolean.class);

// 注册自定义类型的 PropertyEditor
PropertyEditorManager.registerEditor(LocalDate.class, CustomLocalDateEditor.class);
PropertyEditor editor = PropertyEditorManager.findEditor(LocalDate.class);

// 自定义搜索路径
PropertyEditorManager.setEditorSearchPath(new String[]{"example.vbc.bind.propertyeditor"});
// 如果类定义和 PropertyEditor 名称相同且在同一个包下，则可以直接搜索而不需要中注册
PropertyEditor editor = PropertyEditorManager.findEditor(Hello.class);

```

`JavaBeans` 规范还提供了标准的 `BeanInfo` 机制

## 类型转换

Spring 在 `core.convert` 包中定义了通用的类型转换系统，在 Spring 容器中可以使用该系统代替 `PropertyEditor` 实现将字符串转换为所需的数据类型。

### `Converter`

`Converter`  接口是 Spring 提供的实现简单且强类型转换逻辑的 `SPI` ，实现类型转换需要实现该接口并重写类型转换逻辑，如果需要转换数组或者集合则需要注册代理的转换器(`DefaultConversion` 默认会注册)。

```java

```

`Converter` 调用的过程中有可能抛出三种异常：

- `IllegalArgumentException`：需要转换的实例为 null 或者实例类型不能转换成目标类型
- `ConversionFailedException`：定义的 `Converter` 实现类在转换的过程中异常
- `ConverterNotFoundException`：没有定义实例类型和目标类型之间的 `Converter`

`Converter` 在调用的过程中没有并发控制，因此需要保证定义的 `Converter` 实现类是无状态的。Spring 在 `core.convert.support` 包中定义了大量的基础类型转换实现类，并由 `DefaultConversionService` 默认注册：

```java
public static void addDefaultConverters(ConverterRegistry converterRegistry) {
    addScalarConverters(converterRegistry);
    addCollectionConverters(converterRegistry);

    converterRegistry.addConverter(new ByteBufferConverter((ConversionService) converterRegistry));
    converterRegistry.addConverter(new StringToTimeZoneConverter());
    converterRegistry.addConverter(new ZoneIdToTimeZoneConverter());
    converterRegistry.addConverter(new ZonedDateTimeToCalendarConverter());

    converterRegistry.addConverter(new ObjectToObjectConverter());
    converterRegistry.addConverter(new IdToEntityConverter((ConversionService) converterRegistry));
    converterRegistry.addConverter(new FallbackObjectToStringConverter());
    converterRegistry.addConverter(new ObjectToOptionalConverter((ConversionService) converterRegistry));
}
```



### `ConverterFactory`

`Converter` 接口只能在特定类型之间进行转换，在对具有相同父类或者接口进行转换时，使用 Spring 提供的 `ConverterFactory` 接口能够更加方便的管理类型的转换。

```java
public interface ConverterFactory<S, R> {
    <T extends R> Converter<S, T> getConverter(Class<T> targetType);
}
```

Spring 提供了默认的 `ConverterFactory` 用于数字、字符串、枚举类型之间的转换，然后包装成 `ConverterFactoryAdapter` 由 `DefaultConversionService` 注册：

```java
addConverter(new ConverterFactoryAdapter(factory,
		new ConvertiblePair(typeInfo[0].toClass(), typeInfo[1].toClass())));
```



### `GenericConverter`

`GenericConverter` 可以实现更加复杂的转换，支持多个类型间的转换并提供类型上下文用于类型转换逻辑，是 Spring 提供的最复杂的类型转换 `SPI`。`GenericConverter` 接口定义了两个方法：

```java
// 返回当前 converter 支持转换的源类型和目标类型
Set<ConvertiblePair> getConvertibleTypes();

// 类型转换逻辑，TypeDescriptor 描述了源类型和目标类型
Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType);
```

`DefaultConversionService` 注册的 Spring 默认提供的集合间的转换器，实现了 `GenericConverter` 接口：

```java
public static void addCollectionConverters(ConverterRegistry converterRegistry) {
    ConversionService conversionService = (ConversionService) converterRegistry;

    converterRegistry.addConverter(new ArrayToCollectionConverter(conversionService));
    converterRegistry.addConverter(new CollectionToArrayConverter(conversionService));

    converterRegistry.addConverter(new ArrayToArrayConverter(conversionService));
    converterRegistry.addConverter(new CollectionToCollectionConverter(conversionService));
    converterRegistry.addConverter(new MapToMapConverter(conversionService));

    converterRegistry.addConverter(new ArrayToStringConverter(conversionService));
    converterRegistry.addConverter(new StringToArrayConverter(conversionService));

    converterRegistry.addConverter(new ArrayToObjectConverter(conversionService));
    converterRegistry.addConverter(new ObjectToArrayConverter(conversionService));

    converterRegistry.addConverter(new CollectionToStringConverter(conversionService));
    converterRegistry.addConverter(new StringToCollectionConverter(conversionService));

    converterRegistry.addConverter(new CollectionToObjectConverter(conversionService));
    converterRegistry.addConverter(new ObjectToCollectionConverter(conversionService));

    converterRegistry.addConverter(new StreamConverter(conversionService));
}
```



### `ConditionalGenericConverter`

`ConditionalGenericConverter` 接口提供了

### `ConversionService`



## 格式化

### `Formatter`
`core.convert` 包定义了一个通用的类型转换系统，提供了一个统一的 `ConversionService` API 用于实现从一个类型转换到另一个类型，Spring 容器使用这个系统来绑定 bean 的属性，Spring 的表达式语言(SpEL) 和 `DataBinder` 都是使用这个系统来绑定字段值。

## 数据校验

Spring 提供了 `Validator` 框架用于参数的校验，它可以使得参数的校验可以在应用的每一层，并且可以和任何的 `validator` 插件组合。

### `Validator`



## 自动配置

SpringBoot 自动配置文件 `spring.factories` 中配置 Spring Boot 应用启动时加载的 Web 自动配置类 `WebMvcAutoConfiguration`，类上的注解 `@AutoConfigureAfter` 表明在配置该类之前需要配置 `ValidationAutoConfiguration`。





```flow
st=>start: Start
op=>operation: Your OPeration
cond=>condition: Yes or No?
e=>end
st->op->cond
cond(yes)->e
cond(no)->op
```

```sequence
TiTle:时序图示例
客户端->服务端:我想访问你 SYN
服务端->客户端:我收到请求，开始通信吧 ACK+SYN
客户端->服务端:我收到你的确认啦 ACK
客户端-->服务端:虚线实心箭头
服务端->>客户端:实线小箭头
客户端-->>服务端:虚线小箭头
Note right of 服务端:我是一个服务端
Note left of 客户端:我是一个客户端
Note over 服务端,客户端: TCP 三次握手
participant 观察者
```

https://blog.csdn.net/kl28978113/article/details/93617103



https://segmentfault.com/a/1190000006247465