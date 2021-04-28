### AOP

AOP(Aspect Oriented Programming) 是面向切面的编程，主要思想是在程序正常执行的某一个点切入并加入特定的逻辑。

AOP 基本上都是通过代理模式为目标对象生成代理对象并将横切逻辑织入到目标方法执行的前后。Spring AOP 只能对自身容器中的 bean 对象进行代理，并且Spring AOP 只支持方法执行的连接点，也就是通知只会在 bean 的方法上应用。

实现 AOP 的动态代理通常有 jdk 和 cglib 两种方式，如果 bean 对象实现了接口则 Spring 会默认使用 jdk 动态代理，反之则使用 cglib 动态代理。Spring 在初始化 bean 对象时会判断 bean 是否需要进行代理，如果需要则会将其初始化为一个代理对象。

#### AOP 术语

- **切面(Aspect)**：定义切入点和通知的对应关系，Spring 中 Aspect 的实现是 `@Aspect` 注解的普通类
- **连接点(JoinPoint)**：程序执行的某个点，例如方法的执行或者异常的处理。Spring 中连接点总是表示一个方法的执行
- **通知(Advice)**：切面在特定连接点执行的操作，Spring 将通知实现为一个拦截器，并在连接点周围维护一个拦截器链
  - 前置通知(Before)：在匹配的连接点之前执行，前置通知不能阻止连接点的执行，除非抛出异常
  - 后置通知(AfterReturning)：在匹配的连接点正常执行完成后执行，如果连接点执行失败(抛出异常)则不会执行
  - 异常通知(AfterThrowing)：在匹配的连接点执行跑出异常后执行
  - 最终通知(After)：在匹配的连接点退出执行后执行，无论连接点是执行完成还是抛出异常导致退出执行
  - 环绕通知(Around)：在匹配的连接点执行之前和之后执行，可以通过返回值或者抛出异常控制是否继续执行连接点逻辑，是最通用的通知
- **切入点(PointCut)**：匹配连接点的表达式，切入点 (PointCut) 可以确定需要织入通知的连接点 (JoinPoint) ，切入点通常会绑定通知 (Advice) 用于织入切入点确定的连接点。使用切入点表达式使得通知可以独立出来同时服务多个不同的切入点，Spring 默认使用 AspectJ 切入点表达式
- **引入(Introduction)**：为类型声明额外的方法和字段，Spring 允许在任意目标对象(Target Object)引入新的接口以及相应的实现，例如可以使用 Introduction  使 bean 实现 IsModified 接口从而简化缓存逻辑
- **目标对象(Target Object)**：被一个或者多个切面切入的对象，也是通知作用的对象。Spring 使用运行时代理来实现 AOP，因此目标对象是被代理的对象，即从容器获取的是代理对象
- **代理(Proxy)**：Spring 使用代理实现 AOP，支持 JDK 动态代理和 CGLIB 代理
- **织入(Weaving)**：把切面应用到目标对象来创建新的代理对象的过程，这个过程可以发生在编译(AspectJ)、加载和运行时，Spring 使用运行时执行织入

#### 开启 AOP

Spring AOP 默认使用标准的 JDK 动态代理实现，因此实现了接口的类就可以使用 AOP。如果没有实现接口，Spring 则使用 CGLIB 来完成代理。

Spring 支持 `@AspectJ` 和 XML 配置文件两种形式，`@AspectJ` 形式可以采用在普通的 bean 上添加注解声明切面，XML 配置文件则需要在配置文件中定义。

```java
// 使用注解开启 AOP 支持
@EnableAspectJAutoProxy
@Configuration
public class AopConfig{
    
}
```





https://blog.csdn.net/elim168/category_9270255.html