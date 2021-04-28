## 声明式事务

Spring 声明式事务将事务管理代码从业务方法中分离出来，以声明式的方式来实现事务管理。Spring 通过 AOP 将事务管理作为切面管理切点。

在 Spring Boot 中由于在启动是自动加载的 ```TransactionAutoConfiguration``` 中注入的 TransactionManager，因此在使用时只需要在关注的事务方法上加上 @Transactional 注解同时指定事务传播级别以及异常回滚策略即可。

```java
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private DataSourceTransactionManager transactionManager;

    @Override
    @Transactional
    public void save(User user){
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("SomeTxName");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        TransactionStatus status = transactionManager.getTransaction(def);
        try{

        }catch(Exception ex){
            transactionManager.rollback(status);
            throw ex;
        }
    }
}
```

### 事务传播级别

事务方法被另一个事务方法调用时，需要指定事务的传播级别，Spring 指定了 7 种事务传播级别，默认是 REQUIRED：
- ```REQUIRED```：如果存在事务，则当前事务方法加入该事务；如果没有事务，则创建一个新的事务
- ```REQUIRES_NEW```：为当前事务方法创建一个新的事务，如果已经存在事务则挂起
- ```SUPPORTS```：如果存在事务，则当前事务方法加入该事务；否则以非事务的方式继续运行
- ```NOT_SUPPORTED```：方法不应该在事务中运行，如果当前存在事务，则把事务挂起
- ```NEVER```：以非事务方式运行，如果当前存在事务，则抛出异常
- ```MANDATORY```：当前方法必须在事务中运行，如果存在事务，则当前方法加入该事务；如果没有事务则抛出异常
- ```NESTED```：如果当前存在事务，则创建一个事务作为当前事务的嵌套事务来运行，可独立的提交或回滚；如果当前没有事务，则该取值等价于 REQUIRED