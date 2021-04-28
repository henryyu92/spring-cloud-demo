package example.quartz.scheduler;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Properties;

public class SchedulerManagger {

    // the 'default' scheduler is defined in "quartz.properties" found
    // in the current working directory, in the classpath, or
    // resorts to a fall-back default that is in the quartz.jar

    public static Scheduler defaultScheduler() throws SchedulerException {

        SchedulerFactory factory = new StdSchedulerFactory();
        Scheduler scheduler = factory.getScheduler();

        // Scheduler will not execute jobs until it has been started (though they can be scheduled before start())
        scheduler.start();

        return scheduler;
    }

    // Instantiating A Specific Scheduler From Specific Properties
    public static Scheduler getScheduler(Properties properties) throws SchedulerException {
        SchedulerFactory factory = new StdSchedulerFactory(properties);

        Scheduler scheduler = factory.getScheduler();

        scheduler.start();

        return scheduler;
    }

    // Instantiating A Specific Scheduler From A Specific Property File
    public static Scheduler getScheduler(String fileName) throws SchedulerException {
        SchedulerFactory factory = new StdSchedulerFactory(fileName);

        Scheduler scheduler = factory.getScheduler();

        scheduler.start();

        return scheduler;
    }


    //  now the scheduler will not fire triggers / execute jobs
    private void standby(Scheduler scheduler) throws SchedulerException {
        scheduler.standby();
    }

    /**
     *
     * @param scheduler
     * @param waitForJobsToComplete true 表示直到正在运行的 job 执行完之后才会关闭, false 表示立即关闭但是正在执行的 job 依然执行
     * @throws SchedulerException
     */
    private void shutdown(Scheduler scheduler, boolean waitForJobsToComplete) throws SchedulerException {
        scheduler.shutdown(waitForJobsToComplete);
    }
}
