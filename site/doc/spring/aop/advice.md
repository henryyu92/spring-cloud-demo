## 通知(Advisor)

通知和切入点表达式相关联并且会在连接点执行之前、之后或者前后运行。通知关联的切入点表达式可以是已经命名的切入点表达式的引用，也可以直接声明新的表达式。

通知是在通过切点选择出的连接点(目标方法)上执行的横切逻辑，Spring 定义了几种通知用于在目标方法不同的执行时机执行：

- 前置通知(Before advice)：通知在连接点执行前执行，除非在运行时抛出异常，否则 Before 通知不会阻止连接点的运行。Spring 中通过 `@Before` 注解定义前置通知
- 后置通知(After advice)：通知在连接点执行完成后执行，无论连接点执行是否正常都会执行。Spring 中通过`@AfterReturning` 注解定义后置通知
- 返回通知(After returning advice)：通知在连接点正常执行后运行，如果连接点在执行过程中抛出异常则返回通知不会执行。Spring 中通过 `@AfterReturning` 定义返回通知
- 异常通知(After throwing advice)：通知在连接点执行抛出对外的异常后执行。Spring 中通过 `@AfterThrowing` 定义异常通知
- 环绕通知(Around advice)：环绕通知在连接点方法执行的前后都会执行。Spring 中通过 `@Around` 定义环绕通知

```java
@Aspect
@Component
public class AspectClass{
    // 前置通知
    @Before("com.xyz.myapp.CommonPointcuts.dataAccessOperation()")
    public void doAccessCheck(){
        // ...
    }
    
    // 返回通知，使用 returning 绑定连接点方法的返回值
    @AfterReturning(
        pointcut="com.xyz.myapp.CommonPointcuts.dataAccessOperation()",
        returning="retVal")
    public void doAccessCheck(Object retVal) {
        // ...
    }
    
    // 异常通知，连接点在抛出 Exception 的时候才触发异常通知
    @AfterThrowing(
        pointcut="com.xyz.myapp.CommonPointcuts.dataAccessOperation()",
    	throwing="ex")
    public void doRecoveryActions(Exception ex){
        // ...
    }
    
    // 后置通知
    @After("com.xyz.myapp.CommonPointcuts.dataAccessOperation()")
    public void doReleaseLock() {
        // ...
    }
    
    // 环绕通知，jpj 为连接点，返回方法调用结果
    @Around("com.xyz.myapp.CommonPointcuts.businessService()")
    public Object doBasicProfiling(ProceedingJoinPoint pjp) throws Throwable {
        // start stopwatch
        // 执行连接点
        Object retVal = pjp.proceed();
        // stop stopwatch
        return retVal;
    }
}
```

### 通知参数

Spring 提供了全类型的通知，也就是说可以在通知签名中声明任意的参数，可以在任意通知的签名中声明第一个参数为 `org.aspectj.lang.JoinPoint`(环绕通知需要是其子类 `ProceedingJoinPoint`)，通过接口提供的方法可以获取连接点方法和代理信息：

- `getArgs()`：返回连接点方法的参数
- `getThis()`：返回连接点方法的代理对象
- `getTarget()`：返回被代理的对象
- `getSignature()`：返回连接点方法的描述

如果需要向通知传入参数，可以使用 `args` 表达式，如果在表达式中指定参数的名字，则在通知调用的时候就会将值传入参数：

```java
@Aspect
public class ParameterExample {
    
    @Before("com.xyz.myapp.CommonPointcuts.dataAccessOperation() && args(account,..)")
    public void validateAccount(Account account){
        // ...
    }
    
    // 等效写法
    @Pointcut("com.xyz.myapp.CommonPointcuts.dataAccessOperation() && args(account,..)")
	private void accountDataAccessOperation(Account account) {}

	@Before("accountDataAccessOperation(account)")
	public void validateAccount(Account account) {
    	// ...
	}
}
```

`args` 表达是有两个用处，首先它仅匹配那些至少需要一个参数且参数为指定的类型，其次它将参数和通知参数进行绑定使得在通知中能够使用。

### Advisor