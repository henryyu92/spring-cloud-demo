package example.quartz.trigger;

import org.quartz.*;

import java.util.Date;

public class CustomTrigger implements Trigger {
    @Override
    public TriggerKey getKey() {
        return null;
    }

    @Override
    public JobKey getJobKey() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getCalendarName() {
        return null;
    }

    @Override
    public JobDataMap getJobDataMap() {
        return null;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public boolean mayFireAgain() {
        return false;
    }

    @Override
    public Date getStartTime() {
        return null;
    }

    @Override
    public Date getEndTime() {
        return null;
    }

    @Override
    public Date getNextFireTime() {
        return null;
    }

    @Override
    public Date getPreviousFireTime() {
        return null;
    }

    @Override
    public Date getFireTimeAfter(Date date) {
        return null;
    }

    @Override
    public Date getFinalFireTime() {
        return null;
    }

    @Override
    public int getMisfireInstruction() {
        return 0;
    }

    @Override
    public TriggerBuilder<? extends Trigger> getTriggerBuilder() {
        return null;
    }

    @Override
    public ScheduleBuilder<? extends Trigger> getScheduleBuilder() {
        return null;
    }

    @Override
    public int compareTo(Trigger trigger) {
        return 0;
    }
}
