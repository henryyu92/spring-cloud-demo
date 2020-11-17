package example.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzTest {

    public static void main(String[] args) {
        try{
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            scheduler.start();

            // Define a job
            JobDetail job = JobBuilder.newJob(HelloJob.class)
                    .withIdentity("jobName", "jobGroup")
                    .withDescription("hello job description")
                    .build();

            // Define a trigger
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("triggerName", "triggerGroup")
                    .startNow()
                    .withSchedule(
                            SimpleScheduleBuilder.simpleSchedule()
                                    .withIntervalInSeconds(40)
                                    .repeatForever())
                    .build();

            // schedule job using trigger
            scheduler.scheduleJob(job, trigger);

            // you will also need to allow some time for the job to be triggered and executed before calling shutdown() - for a simple example such as this, you might just want to add a Thread.sleep(60000) call

            // Once you obtain a scheduler using StdSchedulerFactory.getDefaultScheduler(), your application will not terminate until you call scheduler.shutdown(), because there will be active threads.
            scheduler.shutdown();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    static class HelloJob implements Job{

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            String data = context.getMergedJobDataMap().getString("hello");
            System.out.println("hello " + data);
        }
    }
}
