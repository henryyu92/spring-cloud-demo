## 切面

切面通常定义为一个类，包含切入点和通知的定义。Spring 通过在 bean 上添加 `@Aspect` 注解就将类定义为切面，切面和普通的 bean 一样可以有方法和字段，另外切面中还可以包含切入点、通知和引入的声明。

```java
@Aspect
@Component
public class NotVeryUsefulAspect {
    
    // 定义切入点
    @Pointcut("")
    public void pointcut(){}
    
    // 定义通知
    @Before("pointcut()")
    public void begin(){
		// do something
    }
}
```

Spring 中切面本身不能作为代理的目标对象，也就是说切入点表达式不会匹配切面中的方法。

## 切入点

切入点声明通知可以执行的连接点，Spring AOP 只支持在 bean 的方法上的连接点，因此可以将切入点看做是与 bean 上的方法执行相匹配。切入点声明包含两部分：一个由名称和任意参数组成的签名和一个确定连接点方法的切入点表达式。Spring AOP 注解方式使用定义在返回值为 void 的普通方法上的 `@Pointcuut` 注解来声明切入点：

```java
// anyOldTransfer	定义的切入点声明
// transfer			切入点匹配的连接点
@Pointcut("execution(* transfer(..))")
private void anyOldTransfer(){}
```

Spring AOP 支持使用 `&&`,  `||`,  `!` 来组合不同的切入点表达式，使得切入点可以支持多个表达式：

```java
@Pointcut("execution(public * *(..))")
private void anyPublicOperation(){}

@Pointcut("within(com.xyz.myapp.trading..*)")
private void isTrading(){}

@Pointcut("anyPublicOperation() && isTrading()")
private void tradingOperation(){}
```

### Execution

切入点表达式常用 `execution` 表达式，其表达式语法为：

```
execution(modifiers-pattern? ret-type-pattern declaring-type-pattern?name-pattern(param-pattern) throws-pattern?)
```

除了返回类型 (ret-type-pattern) 表达式，方法名 (name-pattern) 表达式，方法参数(param-pattern) 表达式外，其他所有的表达式都是可选的。返回类型、方法名、方法参数需要全限定名匹配，通配符 `*` 可以用于匹配所有的类型，对于方法参数 `()` 表示方法不需要参数，`(*)` 表示方法只有一个参数(任意类型都行)，`(..)` 表示方法可以有任意参数：

```
// 任意类的 public 方法，方法需要任意参数，返回任意类型
execution("public * *(..)")

// service 包及其子包的任意方法
execution(* com.xyz.service..*.*(..))

// service 包的任意方法
execution(* com.xyz.service.*.*(..))

// service 包下 AccountService 类的任意方法
execution(* com.xyz.service.AccountService.*(..))
```

### Bean

使用 bean 来匹配指定 bean 的所有方法

```
// 匹配 id 或者 name 为 abc 的 bean 的所有方法
bean("abc")
bean("abc*")
```

### Annotation

使用 annotation 来匹配有指定注解的 bean 的所有方法

```
// 匹配有指定注解的 bean 的所有方法
@annotation(com.xyz.support.MyAnnotation)
```

