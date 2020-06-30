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