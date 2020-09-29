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

#### `BeanWrapper`

`BeanWrapper` 包装了目标 `bean` 并且提供了设置和读取属性值、获取属性的描述符以及获取属性是否可读写的功能，`BeanWrapper`支持目标对象无限深度的嵌套属性的设置。

`BeanWrapper` 还支持添加 JavaBeans 标准的 `PropertyChangeListener` 和 `VetoableChangeListener` 而不需要目标类支持。

`BeanWrapper` 接口以及其默认实现类 `BeanWrapperImpl`是 Spring 的基础设施，一般不会直接在应用程序中使用，而是通过 `BeanFactory` 或者 `DataBinder` 来隐式的调用。

```java
// BeanWrapper 包装 bean
BeanWrapper company = new BeanWrapperImpl(new Company());
// 设置属性值
company.setPropertyValue("name", "Some Company Inc.");
// 设置嵌套属性值
company.setPropertyValue("managingDirector.name", "Jim Stravinsky");
// 设置索引属性值
company.setPropertyValue("employees[1].name", "Someone");

// BeanWrapper 设置 Listener 监听绑定属性数据的变化
BeanWrapper wrapper = new BeanWrapperImpl(target);
        
PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(wrapper);
propertyChangeSupport.addPropertyChangeListener("name", 
        new BeansListener.BeanWrapperPropertyChangeListener());

VetoableChangeSupport vetoableChangeSupport = new VetoableChangeSupport(wrapper);
vetoableChangeSupport.addVetoableChangeListener(
        new BeansListener.BeanWrapperVetoableChangeListener());
```

#### `PropertyEditor`

``PropertyEditors` 是 JavaBeans 规范的一部分，可以实现对象和字符串之间的转换，`DataBinder` 和 `BeanWrapper` 使用 `PropertyEditorSupport` 实现解析和格式化属性值。 

Spring 通过使用 `PropertyEditor` 实现 bean 的属性设置，使用 XML 文件声明 bean 的属性时，通过属性的类型获取对应的 `propertyEditor` 实现类将设置的属性字符串解析为对应的数据类型值

实现自定义的 `PropertyEditor` 并注册到 `BeanWrapper` 或者注册到 IoC 容器中就可以实现自定义的数据类型转换。Spring 内置了大量的 `PropertyEditor` 的实现类，其中大多数都注册到了 `BeanWrapperImpl`

- `CustomBooleanEditor`：将字符串转换为 Boolean 对象
- `CustomDateEditor`：按照指定的 `DataFormat` 将字符串转换为 `Date` 对象
- `CustomNumberEditor`
- `CustomCollectionEditor`

`PropertyEditorSupport` 是 `PropertyEditor` 的实现类，通过继承 `PropertyEditorSupport` 可以构建自定义的属性编辑器：

```java

```

JavaBean 规范使用 `PropertyEditorManager` 为指定类型设置 `PropertyEditor` 的搜索路径，`PropertyEditorManager` 的默认搜索路径为 `sun.beans.editors`

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



## 类型转换

Spring 的 `core.convert` 包提供了一套通用类型的转换，并且提供了 `format` 包用于格式化数据，使用这些包提供的工具类可以替代 `PropertyEditor` 的实现。

### Converter

### Formatter
`core.convert` 包定义了一个通用的类型转换系统，提供了一个统一的 `ConversionService` API 用于实现从一个类型转换到另一个类型，Spring 容器使用这个系统来绑定 bean 的属性，Spring 的表达式语言(SpEL) 和 `DataBinder` 都是使用这个系统来绑定字段值。

## 数据校验

Spring 提供了 Validator 框架用于参数的校验，它可以使得参数的校验可以在应用的每一层，并且可以和任何的 validator 插件组合。

Spring 的参数校验主要由 `Validator` 和 `DataBinder` 构成，

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