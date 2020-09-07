## 参数校验

Spring 提供了 Validator 框架用于参数的校验，它可以使得参数的校验可以在应用的每一层，并且可以和任何的 validator 插件组合。

Spring 的参数校验主要由 `Validator` 和 `DataBinder` 构成，

## 数据绑定

## 类型转换

`core.convert` 包定义了一个通用的类型转换系统，提供了一个统一的 `ConversionService` API 用于实现从一个类型转换到另一个类型，Spring 容器使用这个系统来绑定 bean 的属性，Spring 的表达式语言(SpEL) 和 `DataBinder` 都是使用这个系统来绑定字段值。
