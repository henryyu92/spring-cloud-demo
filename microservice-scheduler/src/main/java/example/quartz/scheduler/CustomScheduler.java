package example.quartz.scheduler;

import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.spi.JobFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CustomScheduler implements Scheduler {
    @Override
    public String getSchedulerName() throws SchedulerException {
        return null;
    }

    @Override
    public String getSchedulerInstanceId() throws SchedulerException {
        return null;
    }

    @Override
    public SchedulerContext getContext() throws SchedulerException {
        return null;
    }

    @Override
    public void start() throws SchedulerException {

    }

    @Override
    public void startDelayed(int i) throws SchedulerException {

    }

    @Override
    public boolean isStarted() throws SchedulerException {
        return false;
    }

    @Override
    public void standby() throws SchedulerException {

    }

    @Override
    public boolean isInStandbyMode() throws SchedulerException {
        return false;
    }

    @Override
    public void shutdown() throws SchedulerException {

    }

    @Override
    public void shutdown(boolean b) throws SchedulerException {

    }

    @Override
    public boolean isShutdown() throws SchedulerException {
        return false;
    }

    @Override
    public SchedulerMetaData getMetaData() throws SchedulerException {
        return null;
    }

    @Override
    public List<JobExecutionContext> getCurrentlyExecutingJobs() throws SchedulerException {
        return null;
    }

    @Override
    public void setJobFactory(JobFactory jobFactory) throws SchedulerException {

    }

    @Override
    public ListenerManager getListenerManager() throws SchedulerException {
        return null;
    }

    @Override
    public Date scheduleJob(JobDetail jobDetail, Trigger trigger) throws SchedulerException {
        return null;
    }

    @Override
    public Date scheduleJob(Trigger trigger) throws SchedulerException {
        return null;
    }

    @Override
    public void scheduleJobs(Map<JobDetail, Set<? extends Trigger>> map, boolean b) throws SchedulerException {

    }

    @Override
    public void scheduleJob(JobDetail jobDetail, Set<? extends Trigger> set, boolean b) throws SchedulerException {

    }

    @Override
    public boolean unscheduleJob(TriggerKey triggerKey) throws SchedulerException {
        return false;
    }

    @Override
    public boolean unscheduleJobs(List<TriggerKey> list) throws SchedulerException {
        return false;
    }

    @Override
    public Date rescheduleJob(TriggerKey triggerKey, Trigger trigger) throws SchedulerException {
        return null;
    }

    @Override
    public void addJob(JobDetail jobDetail, boolean b) throws SchedulerException {

    }

    @Override
    public void addJob(JobDetail jobDetail, boolean b, boolean b1) throws SchedulerException {

    }

    @Override
    public boolean deleteJob(JobKey jobKey) throws SchedulerException {
        return false;
    }

    @Override
    public boolean deleteJobs(List<JobKey> list) throws SchedulerException {
        return false;
    }

    @Override
    public void triggerJob(JobKey jobKey) throws SchedulerException {

    }

    @Override
    public void triggerJob(JobKey jobKey, JobDataMap jobDataMap) throws SchedulerException {

    }

    @Override
    public void pauseJob(JobKey jobKey) throws SchedulerException {

    }

    @Override
    public void pauseJobs(GroupMatcher<JobKey> groupMatcher) throws SchedulerException {

    }

    @Override
    public void pauseTrigger(TriggerKey triggerKey) throws SchedulerException {

    }

    @Override
    public void pauseTriggers(GroupMatcher<TriggerKey> groupMatcher) throws SchedulerException {

    }

    @Override
    public void resumeJob(JobKey jobKey) throws SchedulerException {

    }

    @Override
    public void resumeJobs(GroupMatcher<JobKey> groupMatcher) throws SchedulerException {

    }

    @Override
    public void resumeTrigger(TriggerKey triggerKey) throws SchedulerException {

    }

    @Override
    public void resumeTriggers(GroupMatcher<TriggerKey> groupMatcher) throws SchedulerException {

    }

    @Override
    public void pauseAll() throws SchedulerException {

    }

    @Override
    public void resumeAll() throws SchedulerException {

    }

    @Override
    public List<String> getJobGroupNames() throws SchedulerException {
        return null;
    }

    @Override
    public Set<JobKey> getJobKeys(GroupMatcher<JobKey> groupMatcher) throws SchedulerException {
        return null;
    }

    @Override
    public List<? extends Trigger> getTriggersOfJob(JobKey jobKey) throws SchedulerException {
        return null;
    }

    @Override
    public List<String> getTriggerGroupNames() throws SchedulerException {
        return null;
    }

    @Override
    public Set<TriggerKey> getTriggerKeys(GroupMatcher<TriggerKey> groupMatcher) throws SchedulerException {
        return null;
    }

    @Override
    public Set<String> getPausedTriggerGroups() throws SchedulerException {
        return null;
    }

    @Override
    public JobDetail getJobDetail(JobKey jobKey) throws SchedulerException {
        return null;
    }

    @Override
    public Trigger getTrigger(TriggerKey triggerKey) throws SchedulerException {
        return null;
    }

    @Override
    public Trigger.TriggerState getTriggerState(TriggerKey triggerKey) throws SchedulerException {
        return null;
    }

    @Override
    public void resetTriggerFromErrorState(TriggerKey triggerKey) throws SchedulerException {

    }

    @Override
    public void addCalendar(String s, Calendar calendar, boolean b, boolean b1) throws SchedulerException {

    }

    @Override
    public boolean deleteCalendar(String s) throws SchedulerException {
        return false;
    }

    @Override
    public Calendar getCalendar(String s) throws SchedulerException {
        return null;
    }

    @Override
    public List<String> getCalendarNames() throws SchedulerException {
        return null;
    }

    @Override
    public boolean interrupt(JobKey jobKey) throws UnableToInterruptJobException {
        return false;
    }

    @Override
    public boolean interrupt(String s) throws UnableToInterruptJobException {
        return false;
    }

    @Override
    public boolean checkExists(JobKey jobKey) throws SchedulerException {
        return false;
    }

    @Override
    public boolean checkExists(TriggerKey triggerKey) throws SchedulerException {
        return false;
    }

    @Override
    public void clear() throws SchedulerException {

    }
}
