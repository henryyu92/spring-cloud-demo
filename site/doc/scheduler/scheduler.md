## 定时任务

### Timer

`Timer` 底层使用最小堆实现任务的按顺序执行。

- `schedule`：在上一个任务执行完成之后再计算下次执行时间，当任务执行时间超过间隔时间会出现任务丢失(固定时间内任务执行次数少了)
- `scheduleAtFixRate`：按照设置的间隔计算下次执行时间，当任务执行时间超时时会导致任务连续执行

```java
public void runTask(){
    Timer t = new Timer();
    // 添加任务到最小堆
    t.schedule(new TimerTask(){
        public void run(){
            // do some thing...
        }
    }, new Date(), 2000);
}

public class FooTask extends TimerTask{
    
    public void run(){
        
    }
}
```

Timer 中创建的执行线程会不断的从最小堆中获取添加的任务，然后查看任务的下次执行是否小于当前时间，如果是则执行任务，任务执行完之后需要重新设置下次执行时间并重新加入最小堆。

Timer 使用单线程执行任务，任务执行时间超过间隔时间会有问题，可以在执行任务的逻辑中使用线程池方式。

Timer 中运行的任务异常会导致 Timer 终止

Timer 基于绝对时间，对系统的时间敏感

### ScheduledThreadPool

使用多线程执行任务，不会阻塞

如果任务执行异常则会被丢弃，创建新的线程执行其他任务

```java
public void runTask(){
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);
    scheduler.scheduleAtFixRate(() ->{
        // do something...
    }, 0, 2 ,TimeUnit.SECONDS)
}
```

