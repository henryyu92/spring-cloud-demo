### 数据绑定

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

#### `DataBinder`

#### `PropertyEditor`

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

### 类型转换

Spring 在 `core.convert` 包中定义了通用的类型转换系统，在 Spring 容器中可以使用该系统代替 `PropertyEditor` 实现将字符串转换为所需的数据类型。

#### `Converter`

`Converter`  接口是 Spring 提供的实现简单的强类型转换逻辑的 `SPI` ，实现类型转换需要实现该接口并重写类型转换逻辑。Spring 在 `core.convert.support` 包中定义了多种类型转换实现，并通过 `DefaultConversionService` 注册。

```java
public interface Converter<S, T> {
    T convert(S source);
}
```

`Converter` 调用时不能保证参数对象为非空，也不能保证对象是否可以转换，因此在调用的过程中有可能抛出三种异常：

- `IllegalArgumentException`：需要转换的实例为 null 或者实例类型不能转换成目标类型
- `ConversionFailedException`：定义的 `Converter` 实现类在转换的过程中异常
- `ConverterNotFoundException`：没有定义实例类型和目标类型之间的 `Converter`



#### `ConverterFactory`

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



#### `GenericConverter`

`GenericConverter` 可以实现更加复杂的转换，支持多个类型间的转换并提供类型上下文用于类型转换逻辑，是 Spring 提供的最复杂的类型转换 `SPI`。`GenericConverter` 接口定义了两个方法：

```java
// 返回当前 converter 支持转换的源类型和目标类型
Set<ConvertiblePair> getConvertibleTypes();

// 类型转换逻辑，TypeDescriptor 描述了源类型和目标类型
Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType);
```



#### `ConditionalGenericConverter`

`ConditionalGenericConverter` 接口继承了 `GenericConvertion` 和 `ConditionalConverter`，可以在满足特定情况下才进行类型转换：

```java
public interface ConditionalConverter{
    boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType);
}

public interface ConditionalGenericConverter extends GenericConverter, ConditionalConverter {
}
```



#### `ConversionService`

`ConversionService` 接口是整个类型转换系统的入口，通过统一的 `API`实现运行时的类型转换逻辑。 

```java
public interface ConversionService {
    // 判断类型转换是否可以进行，将判断逻辑从转换逻辑中抽离
    boolean canConvert(@Nullable Class<?> sourceType, Class<?> targetType);
    
    boolean canConvert(@Nullable TypeDescriptor sourceType, TypeDescriptor targetType);
    
    // 实现类型转换逻辑
    <T> T convert(@Nullable Object source, Class<T> targetType);
    
    Object convert(@Nullable Object source, @Nullable TypeDescriptor sourceType, 				TypeDescriptor targetType);
}
```



`GenericConversionService` 是最常用的实现类，并且实现了 `ConverterRegistry` 接口用于将类型转换器注册，在进行类型转换时代理给注册到注册器中的类型转换器实现真正的类型转换逻辑。

```java
public Object convert(@Nullable Object source, @Nullable TypeDescriptor sourceType, 			TypeDescriptor targetType) {
    Assert.notNull(targetType, "Target type to convert to cannot be null");
    // 转换对象为 null 时，目标对象为 optional 或者 null
    if (sourceType == null) {
        Assert.isTrue(source == null, "Source must be [null] if source type == [null]");
        return handleResult(null, targetType, convertNullSource(null, targetType));
    }
    if (source != null && !sourceType.getObjectType().isInstance(source)) {
        throw new IllegalArgumentException("Source to convert from must be an instance of 			[" + sourceType + "]; instead it was a [" + source.getClass().getName() + "]");
    }
    // 获取代理的 Converter 执行实际类型转换逻辑
    GenericConverter converter = getConverter(sourceType, targetType);
    if (converter != null) {
        Object result = ConversionUtils.invokeConverter(converter, source, sourceType, 				targetType);
        return handleResult(sourceType, targetType, result);
    }
    // 没有找到 converter 时如果目标对象和源对象是同一类型，则直接返回源对象，否则抛出异常
    return handleConverterNotFound(source, sourceType, targetType);
}
```

`ConversionServiceFactory` 提供了一个方便的工厂类用于将 `Converter` 注册到  `ConverterRegistry`。

```java
public static void registerConverters(@Nullable Set<?> converters, ConverterRegistry 			registry) {
    if (converters != null) {
        for (Object converter : converters) {
            if (converter instanceof GenericConverter) {
                registry.addConverter((GenericConverter) converter);
            }
            else if (converter instanceof Converter<?, ?>) {
                registry.addConverter((Converter<?, ?>) converter);
            }
            else if (converter instanceof ConverterFactory<?, ?>) {
                registry.addConverterFactory((ConverterFactory<?, ?>) converter);
            }
            else {
                throw new IllegalArgumentException("Each converter object must implement 					one of the " + "Converter, ConverterFactory, or GenericConverter 						interfaces");
            }
        }
    }
}
```



`ConversionService` 是无状态的，在应用启动的时候实例化并且在多个线程之间共享。Spring 应用中，只需要为容器或者 `ApplicationContext` 配置 `ConversionService` 实例，就可以在需要类型转换的任何 bean 中注入并直接调用。

Spring 使用 `ConversionServiceFactoryBean` 来注册提供的默认 `ConversionService`，将其注册到容器中就可以使用 Spring 提供的类型转换器：

```java
// ConversionServiceFactoryBean 的 name 需要为 conversionService
@Bean
public ConversionServiceFactoryBean conversionService(){
    return new ConversionServiceFactoryBean();
}
```

`ConversionServiceFactoryBean` 实例化了 `DefaultConversionService`，在实例化时将内建的转换器注册：

```java
public void afterPropertiesSet() {
    this.conversionService = createConversionService();
    ConversionServiceFactory.registerConverters(this.converters, this.conversionService);
}


protected GenericConversionService createConversionService() {
    return new DefaultConversionService();
}
```



### 格式化

`Converter` API 提供了通用的类型转换体系，Spring 在大部分情况下使用这套体系完成类型的转换以及数据的绑定。但是在一些情况下，如本地化字符串值，`Converter` 体系就不能直接处理这类问题，Spring 提供了 `Formatter` 作为 `PropertyEditor` 的替换。

#### `Formatter`

`Formatter` 接口继承了 `Printer` 和 `Parser` 接口，定义了强类型的字段类型格式化：

```java
public interface Formatter<T> extends Printer<T>, Parser<T> {
}
```

创建自定义的字段格式化需要实现 `Formatter` 接口并重写 `print` 方法完成对象的本地化，重写 `parse` 方法完成从本地化值解析对象。在本地化以及解析的过程中，`Formatter` 可能因为解析失败而抛出 `ParseException` 或者 `IllegalArgumentException`，`Formatter` 的实现也需要保证时线程安全的。

```java
public final class DateFormatter implements Formatter<Date>{
    private String pattern;
    
    public DateFormatter(String pattern){
        this.pattern = pattern;
    }
    
    public String print(Date date, Locale locale){
        if(date == null){
            return "";
        }
        return getDateFormat(locale).format(date);
    }
    
    public Date parse(String formatted, Locale locale){
        if(formatted.length() == 0){
            return null;
        }
        return getDateFormat(locale).parse(formatted);
    }
    
    protected DateFormat getDateFormat(Locale locale){
		DateFormat dateFormat = new SimpleDateFormat(this.pattern, locale);
        dateFormat.setLenient(false);
        return dateFormat;
    }
}
```

#### `AnnotationFormatterFactory`

通过实现 `AnnotationFormatterFactory` 将注解和 `Formatter` 实现绑定就可以直接使用注解来配置字段的格式化。`AnnotationFormatterFactory` 接口定义了三个方法：

```java
public interface AnnotationFomatterFactory<A extends Annotation> {
    
    // 定义注解适用的字段类型
    Set<Class<?>> getFieldTypes();
    
    // 返回作用在字段上的注解对应的 Fomartter
    Printer<?> getPrinter(A annotation, Class<?> fieldType);
    
    Parser<?> getParser(A annotation, Class<?> fieldType);
}
```

Spring 内置的 `@NumberFormat` 注解用于格式化数字类型的字段，其原理就是通过实现 `AnnotationFormatterFactory` 接口完成字段格式化：

```java
public final class NumberFormatAnnotationFomatterFactory 
    implements AnnotationFormatterFactory<NumberFormat> {
    
    public Set<Class<?>> getFieldTypes(){
        return new HashSet<Class<?>>(Arrays.asList(new Class<?>[]{
            Short.class, Integer.class, Long.class, Float.class,
            Double.class, BigDecimal.class, BigInteger.class
        }))
    }
    
    public Printer<Number> getPrinter(NumberFormat annotation, Class<?> fieldType){
        return configureFormatterFrom(annotation, fieldType);
    }
    
    public Parser<Number> getParser(NumberFormat annotation, Class<?> fieldType){
        return configureFormatterFrom(annotation, fieldType);
    }
    
    private Formatter<Number> configureFormatterFrom(
        NumberFormat annotation, Class<?> fieldType){
        
        if(!annotation.pattern().isEmpty()){
            return new NumberStyleFormatter(annotation,pattern());
        }else{
            Style style = annotation.style();
            if(style == Style.PERCENT){
                return new PercentStyleFormatter();
            }else if(stype == Style.CURRENCY){
                return new CurrencyStyleFormatter();
            }else{
                return new NumberFormatter();
            }
        }
    }
}
```

Spring 在 `org.springframework.format.annotation` 包定义了 `@NumberFormat` 格式化数字类型，`@DateTimeFormat` 格式化时间类型。

#### `FormatterRegistry`

`FormatterRegistry` 接口继承自 `ConverterRegistry`，因此可以注册 `Formatter` 和 `Converter` 。`FormattingConversionRegistry` 是 `FormatterRegistry` 的一种实现，通过 `FormattingConversionServiceFactoryBean` 可以显式的将其注册到容器中：

```java
public void afterPropertiesSet() {
    this.conversionService = new DefaultFormattingConversionService(
        this.embeddedValueResolver, this.registerDefaultFormatters);
    ConversionServiceFactory.registerConverters(this.converters, this.conversionService);
    registerFormatters(this.conversionService);
}
```



`FormatterRegistry` 集中了格式化规则的配置，只需要一次定义就可以在所有地方使用这些规则。可以按照字段类型或者注解来注册格式化规则：

```java
public interface FormatterRegistry extends ConverterRegistry {
    
    // 按照字段类型添加 Formatter
    void addFormatterForFieldType(Class<?> fieldType, Formatter<?> formatter);

	// 按照注解添加 Formatter
    void addFormatterForAnnotation(AnnotationFormatterFactory<?> factory);
}
```



#### `FormatterRegistrar`

`FormatterRegistrar` 通过 `FormatterRegistry` 注册 `Formatter` 和 `Converter`，在为给定的类别注册多种格式化注册器的时候会非常有用。

```java
public interface FormatterRegistrar {
    
    void registerFormatters(FormatterRegistry registry);
}
```

- `DateTimeFormatterRegistrar`
- `DateFormatterRegistrar`

### 数据校验

Spring 提供 `Validator` 框架可以通过约束声明和元数据进行参数的校验，`Validator` 可以作用于任何的对象并且通过与 `Hibernate Validator` 结合使用实现更加方便的参数校验。

#### `Validator`

`Validator` 接口声明应用程序中对象的校验器，接口完全独立于任何基础设施或者上下文，因此可以在任意地方验证任何对象。

```java
public interface Validator {
    
    boolean supports(Class<?> clazz);
    
    // 验证失败则将错误记录在 Errors 对象中
    void validate(Object target, Errors errors);
}
```



#### `LocalValidatorFactoryBean`

`LocalValidatorFactoryBean` 是 Spring 提供的验证功能的核心类，在初始化完成时(`afterPropertiesSet`)会将默认提供的所有 `Validator` 注册到容器中，包括 `javax.validator` 和 `Hibernate Validator` 都会注册到容器中，之后就可以在需要的地方直接注入：

```java
@Configuration
public class AppConfig{
    
    @Bean
    public LocalValidatorFactoryBean validator(){
        return new LocalValidatorFactoryBean();
    }
    
}
```



#### `MethodValidationPostProcessor`

Spring 通过 `MethodValidationPostProcessor` 将 Bean Validation 标准以及自定义的实现(如 `Hibernate Validator`) 支持的方法校验集成到 Spring 上下文中。要使用方法校验，则需要在校验类添加 `@Validated` 注解。

```java
@Configuration
public class AppConfig {

    @Bean
    public MethodValidationPostProcessor validationPostProcessor() {
        return new MethodValidationPostProcessor();
    }
}

@Validated
public class Person{
    
    @Max(120)
    privte int age;
}
```



#### 自定义校验器

每个校验器由两部分组成：

- 声明约束以及可以配置属性的 `@Constraint` 注解
- 实现 `javax.validation.ConstraintValidator` 接口的类

为了将注解声明与接口实现相关联，每个 `@Constraint` 注解都引用对应的 `ConstraintValidator` 实现类，在运行时如果遇到定义的注解，`ConstraintValidatorFactory` 就会实例化引用的实现类。

默认情况下，`LocalValidatorFactoryBean` 配置了一个 `SpringConstraintValidatorFactory` 使得 `ConstraintValidator` 的创建交由 Spring 管理，这使得自定义的 `ConstraintVlidator` 可以注入到其他类中。

```java
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=MyConstraintValidator.class)
public @interface MyConstraint {
}

// Spring 会自动将关联的实现类注入容器
public class MyConstraintValidator implements ConstraintValidator {

    @Autowired;
    private Foo aDependency;

    // ...
}
```



### 自动配置

Spring Boot 在启动时自动配置了数据转换和数据校验的默认实现类，这使得不需要额外的配置就可以使用 Spring 提供的大量默认的类型转换器和校验器。

#### 类型转换

在 `WebMvcAutoConfiguration` 配置类中定义了

```java
@Bean
public FormattingConversionService mvcConversionService() {
    WebConversionService conversionService = new WebConversionService(this.mvcProperties.getDateFormat());
    addFormatters(conversionService);
    return conversionService;
}
```



#### 数据校验

Spring Boot 自动配置文件 `spring.factories` 中配置了应用启动时加载的自动配置类 `WebMvcAutoConfiguration`，类上的注解 `@AutoConfigureAfter` 表明在配置该类之前需要配置 `ValidationAutoConfiguration`。

在 `ValidationAutoConfiguration` 中向 Spring 容器注册了 `LocalValidatorFactoryBean` 和 `MethodValidationPostProcessor`。

