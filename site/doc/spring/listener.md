### EventListener

事件监听机制包括事件、事件发布者和事件监听者。`ApplicationContext` 中的事件由 `ApplicationEvent` 表示，而监听者则由 `ApplicationListener` 接口表示。

Spring 提供的事件监听机制是标准的观察者设计模式，如果实现 `ApplicationListener`  接口的类注册到容器中，则每次将 `ApplicationEvent` 发布到 `ApplicationContext` 时都会通过该 bean。


#### ApplicationEvent

#### ApplicationEventPublisher

#### ApplicationListener