## Quartz

Quartz 是 Java 语言开发的一个任务调度框架,用于执行定时任务.

### Job

Job 表示需要执行的任务,是任务真正的执行逻辑,定义任务需要实现 Job 接口.

### Scheduler

Scheduler 是调度器的实现,所有的任务调度由 Scheduler 来实现,任务由 Trigger 触发后需要由 Scheduler 来调度执行.

### Trigger

Trigger 定义任务触发的规则


https://segmentfault.com/a/1190000015885177


https://segmentfault.com/a/1190000015294464


### SpringBoot Quartz

Quartz 的 Job 中注入 Spring IoC 的 bean
```java
public final class SpringJobFactory extends SpringBeanJobFactory implements
        ApplicationContextAware {

    private transient AutowireCapableBeanFactory beanFactory;

    @Override
    public void setApplicationContext(final ApplicationContext context) {
        beanFactory = context.getAutowireCapableBeanFactory();
    }

    @Override
    protected Object createJobInstance(final TriggerFiredBundle bundle) throws Exception {
        final Object job = super.createJobInstance(bundle);
        beanFactory.autowireBean(job);
        return job;
    }
}

@Configuration
public class SchedulerConfig {
    @Autowired
    private SpringJobFactory springJobFactory;
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setJobFactory(springJobFactory);
        return schedulerFactoryBean;
    }
}
```

https://jverson.com/spring-boot-demo/schedule/quartz-springboot.html
