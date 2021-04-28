### EventListener

事件监听机制包括事件、事件发布者和事件监听者

ApplicationContext 事件机制的观察者设计模式实现，当 ApplicationContext 发布 ApplicationEvent 事件时会触发 ApplicationListener 的 onApplicationEvent 方法

#### ApplicationEvent

- ContextRefreshEvent：ApplicationContext 初始化或者刷新时发布该事件
- ContextStartedEvent：使用 ConfigurableApplicaionContext 接口中的 start 方法启动 ApplicationContext 时发布该事件
- ContextClosedEvent：使用 ConfigurableApplicaionContext 接口中的 close 方法关闭 ApplicationContext 时发布该事件，此时容器已经关闭不能被刷新或重启
- ContextStoppedEvent：使用 ConfigurableApplicaionContext 接口中的 stop 方法停止 ApplicationContext 时发布该事件

#### ApplicationEventPublisher

#### ApplicationListener