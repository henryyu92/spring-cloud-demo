### EventListener

事件监听机制包括事件、事件发布者和事件监听者。`ApplicationContext` 中的事件由 `ApplicationEvent` 表示，而监听者则由 `ApplicationListener` 接口表示。

Spring 提供的事件监听机制是标准的观察者设计模式，如果实现 `ApplicationListener`  接口的类注册到容器中，则每次将 `ApplicationEvent` 发布到 `ApplicationContext` 时都会通过该 bean。

#### ApplicationEvent

`ApplicationEvent` 是 Spring 提供的事件抽象类，通过继承抽象类可以自定义事件。Spring 内置了标准的上下文事件，通过监听容器上下文事件可以获取容器状态：
- `ContextRefreshedEvent`：容器初始化完成或者容器刷新后触发。初始化完成表示容器中的所有工作已经处理完并且可以对外服务，容器初始化完成后可以多次触发刷新(`ConfigurableApplicationContext#refresh`)
-  `ContextStartedEvent`：调用 `ConfigurableApplication#start` 方法显式启动容器时触发，所有的 `Lifecycle` 都会收到一个显式的开始信号
- `ContextStoppedEvent`：调用 `ConfigurableApplication#stop` 方法显式停止容器时触发，所有的 `Lifecycle` 都会收到一个显式的停止信号
- `ContextClosedEvent`：调用 `ConfigurableApplication#close` 方法或者通过 JVM 的 `shutdown hook` 来关闭容器，容器一旦关闭则其管理的 bean 都会被销毁并且容器无法刷新或者重启
- `RequestHandledEvent`：在 HTTP 请求已处理完成后触发，仅仅适用于使用 `DispatcherServlet` 的 Web 应用
- `ServletRequestHandledEvent`：`RequestHandledEvent` 的子类，添加了 `Servlet` 相关信息

在实现自定义 `ApplicationEvent` 的时候，如果事件之间关联性较强，则可以适用范型来定义事件，同时为了避免范型擦除问题可以实现 `ResolvableTypeProvider` 接口：
```java
public class EntityCreatedEvent<T> extends ApplicationEvent implements ResolvableTypeProvider {
   
    public EntityCreatedEvent(T entity) {
        super(entity);
    }    

    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(getClass(), ResolvableType.forInstance(getSource()));
    }

}
```
#### ApplicationEventPublisher


#### ApplicationListener