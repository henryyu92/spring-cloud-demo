## AOP
AOP(Aspect Oriented Programming) 是面向切面的编程，通过代理模式为目标对象生成代理对象并将横切逻辑插入到目标方法执行的前后。

#### 连接点(Joinpoint)
连接点是指程序执行过程中的一些点，比如方法调用，异常处理。Spring 连接点仅支持方法级别，即每个方法调用是一个连接点。
```
public interface Joinpoint{
    // 用于执行拦截器链中的下一个拦截器逻辑
    Object proceed() throws Throwable;

    Object getThis();

    AccessibleObject getStaticPart();
} 
```
#### 切点(Pointcut)
切点是用于选择连接点的，Spring 中切点通过 Pointcut 接口定义：
```
public interface Pointcut{
    // 返回一个类型过滤器
    ClassFilter getClassFilter();

    // 返回一个方法匹配器
    MethodMatcher getMethodMatcher();

    Pointcut TRUE = TruePointcut.INSTANCE;
}
```
Pointcut 接口中定义了两个接口分别用于返回类型过滤器和方法匹配器，通过实现这些接口可以自定义切点，即自定义连接点的选择。
#### 通知(Advice)
通知是在通过切点选择出的连接点(目标方法)上执行的横切逻辑，Spring 定义了几种通知用于在目标方法不同的执行时机执行：
- 前置通知(Before advice)：在目标方法调用前执行通知
- 后置通知(After advice)：在目标方法调用完成后执行通知
- 返回通知(After returning advice)：在目标方法执行返回后执行通知
- 异常通知(After throwing advice)：在目标方法抛出异常后执行通知
- 环绕通知(Around advice)：在目标方法调用前后均可执行自定义逻辑

#### 切面(Aspect)
切面是一个概念，用于将切入点和通知组合起来可以确定在指定的目标对象执行指定的通知：
```
@Component
@Aspect
public class AopDemo{

    // 指定切点
    @Pointcut("")
    public void pointCut(){}

    // 指定通知
    @Before("pointCut()")
    public void begin(){

    }
}
```
#### 织入(Weaving)
织入是在切点的引导下将切面指定的通知(逻辑处理)插入到指定的连接点(目标方法)上，使得通知逻辑在目标方法调用时得以执行。Spring 通过 AbstractAutoProxyCreator 实现织入，AbstractAutoProxyCreator 实现了 BeanPostProcessor 接口，在 bean 初始化完毕之后会调用 postProcessAfterInitialization 方法：
```
public Object postProcessAfterInitialization(@Nullable Object bean, String beanName) {
    if (bean != null) {
        Object cacheKey = getCacheKey(bean.getClass(), beanName);
        if (this.earlyProxyReferences.remove(cacheKey) != bean) {
            // 为 bean 生成代理对象
            return wrapIfNecessary(bean, beanName, cacheKey);
        }
    }
    return bean;
}


protected Object wrapIfNecessary(Object bean, String beanName, Object cacheKey) {
    if (StringUtils.hasLength(beanName) && this.targetSourcedBeans.contains(beanName)) {
        return bean;
    }
    if (Boolean.FALSE.equals(this.advisedBeans.get(cacheKey))) {
        return bean;
    }

    // 如果是基础的 bean(Pointcut, Advice Advisor 接口实现类)则直接返回不需要创建代理类
    if (isInfrastructureClass(bean.getClass()) || shouldSkip(bean.getClass(), beanName)) {
        this.advisedBeans.put(cacheKey, Boolean.FALSE);
        return bean;
    }

    // Create proxy if we have advice.
    Object[] specificInterceptors = getAdvicesAndAdvisorsForBean(bean.getClass(), beanName, null);
    if (specificInterceptors != DO_NOT_PROXY) {
        this.advisedBeans.put(cacheKey, Boolean.TRUE);
        // 创建代理对象
        Object proxy = createProxy(
                bean.getClass(), beanName, specificInterceptors, new SingletonTargetSource(bean));
        this.proxyTypes.put(cacheKey, proxy.getClass());
        return proxy;
    }

    this.advisedBeans.put(cacheKey, Boolean.FALSE);
    return bean;
}
```
wrapIfNecessary 方法中调用 getAdvicesAndAdvisorsForBean 方法判断是否需要为目标对象创建代理并且为该目标对象筛选合适的通知，如果需要创建代理则调用 createProxy 方法创建代理并将通知织入到目标对象的方法。Spring 提供了两种创建代理的方法：
- 基于 JDK 的动态代理
- 基于 CGLIB 的动态代理

基于 JKD 的动态代理创建代理对象需要目标对象实现了接口，JDK 动态代理通过 Proxy 类为目标对象创建代理对象：
```
public static Object newProxyInstance(ClassLoader loader, Class<?>[] interfaces, InvocationHandler h)
```
- loader 表示类加载器
- interfaces 表示目标类实现的接口
- h 用于封装代理逻辑
```
public class JdkProxyCreator implements InvocationHandler {
    // 目标对象
    private Ojbect target;

    public JdkProxyCreator(Object target){
        Class<?> interfaces = target.getClass().getInterfaces();
        if(interfaces.length == 0){
            throw new IllegalArgumentException("");
        }
        this.target = target;
    }

    // 获取代理对象
    public Object getProxy(){
        Class<?> clazz = target.getClass();
        // 生成代理对象
        return Proxy.newProxyInstance(clazz.getClassLoader, clazz.getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable{
        // 目标方法执行前执行的代理逻辑
        doProxyBeforeMethodInvoke();
        // 执行目标方法
        Object retVal = method.invoke(target, args);
        // 目标方法执行后的代理逻辑
        doProxyAfterMethodInvoke();

        return retVal;
    }
}
```
如果目标对象没有实现接口则需要使用 CGLIB 实现动态代理的创建，CGLIB 的代理逻辑封装在 MethodInterceptor 实现类中，代理对象通过 Enhancer 类的 create 方法创建：
```
public class CglibProxyCreator implements MethodInterceptor{
    private Object target;

    private MethodInterceptor methodInterceptor;

    public CglibProxyCreator(Object target, MethodInterceptor methodInterceptor){
        this.target = target;
        this.methodInterceptor = methodInterceptor;
    }

    public Object getProxy(){
        Enhancer enhancer = new Enhancer();
        // 设置代理类的父类
        enhancer.setSuperclass(target.getClass);
        // 设置代理逻辑
        enhancer.setCallback(methodInterceptor);
        // 创建代理对象
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable{
        // 代理逻辑
    }
}
```

### PointCut

### Advice

### Advisor









[Back](../../)