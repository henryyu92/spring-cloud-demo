### WebApplicationInitializer

Servlet 3.0 为了支持可以不使用 `web.xml` 来配置，提供了 `ServletContainerInitializer` 接口，通过 SPI 机制从 `META-INF/services/javax.servlet.ServletContainerInitializer` 中加载并实例化所有实现该接口的类。

容器启动时会调用 `ServletContainerInitializer` 实现类的 `onStartup` 方法并传入该实现类的 `@HandlesTypes` 注解中的类。Spring 提供了 `SpringServletContainerInitializer` 类实现了 `ServletContainerInitializer` ，因此在 Servlet 容器启动时会实例化并调用 `onStartup`方法：

```java
// 遍历所有传入的 Class，只保留类上通过 @HandlesTypes 注解的 WebApplicationInitializer 类
for (Class<?> waiClass : webAppInitializerClasses) {
	// Be defensive: Some servlet containers provide us with invalid classes,
	// no matter what @HandlesTypes says...
	if (!waiClass.isInterface() && !Modifier.isAbstract(waiClass.getModifiers()) &&
		WebApplicationInitializer.class.isAssignableFrom(waiClass)) {
	try {
		initializers.add((WebApplicationInitializer)											// 实例化保留下来的类
        ReflectionUtils.accessibleConstructor(waiClass).newInstance());
	}
	catch (Throwable ex) {
		throw new ServletException("Failed to instantiate WebApplicationInitializer class", ex);
	}
}


// 实现类进行排序并依次调用 onStarup 方法
AnnotationAwareOrderComparator.sort(initializers);
for (WebApplicationInitializer initializer : initializers) {
	initializer.onStartup(servletContext);
}
```

分析 `SpringServletContainerInitializer#onStartup` 方法可以确定该方法实例化所有实现 `WebApplicationInitializer` 接口的类并在排序后依次调用 `onStartup` 方法。



通过实现 `WebApplicationInitializer` 就可以自定义 Web 应用：

```java
public void MyWebApplicationInitializer implements WebApplicationInitializer {
    
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
    	// ...
    }
}
```

### WebApplicationContext

Servlet 容器在启动应用时需要创建 `ServletContext` 用于保存上下文信息。