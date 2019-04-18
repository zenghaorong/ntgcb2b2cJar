package com.aebiz.app.web.commons.quartz;

import org.quartz.*;

import java.util.Date;

/**
 * Created by wizzer on 2017/1/3.
 */
public class Quartzs {
    public static void cron(Scheduler scheduler, String cron, Class<?> klass) {
        try {
            String name = klass.getName();
            String group = Scheduler.DEFAULT_GROUP;
            JobKey jobKey = new JobKey(name, group);
            if (scheduler.checkExists(jobKey))
                scheduler.deleteJob(jobKey);
            scheduler.scheduleJob(makeJob(jobKey, klass), makeCronTrigger(name, group, cron));
        }
        catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    public static void simple(Scheduler scheduler, Class<?> klass, int fixedRate, int count, long initialDelay) {
        try {
            String name = klass.getName();
            String group = Scheduler.DEFAULT_GROUP;
            JobKey jobKey = new JobKey(name, group);
            if (scheduler.checkExists(jobKey))
                scheduler.deleteJob(jobKey);
            scheduler.scheduleJob(makeJob(jobKey, klass), makeSimpleTrigger(name, group, fixedRate, count, initialDelay));
        }
        catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    public static CronTrigger makeCronTrigger(String jobName, String jobGroup, String cron) {
        return TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup)
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                .build();
    }

    public static SimpleTrigger makeSimpleTrigger(String jobName, String jobGroup, int fixedRate, int count, long initialDelay) {
        SimpleScheduleBuilder schedule = SimpleScheduleBuilder.simpleSchedule();
        if (fixedRate > 0)
            schedule.withIntervalInSeconds(fixedRate);
        if (count > 0) {
            schedule.withRepeatCount(count);
        } else {
            schedule.repeatForever();
        }
        TriggerBuilder<SimpleTrigger> trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup).withSchedule(schedule);
        if (initialDelay > 0)
            trigger.startAt(new Date(System.currentTimeMillis() + initialDelay*1000));
        return trigger.build();
    }

    public static JobDetail makeJob(String jobName, String jobGroup, Class<?> klass) {
        return makeJob(new JobKey(jobName, jobGroup), klass);
    }

    public static JobDetail makeJob(String jobName, String jobGroup, Class<?> klass, JobDataMap data) {
        return makeJob(new JobKey(jobName, jobGroup), klass, data);
    }

    public static JobDetail makeJob(JobKey jobKey, Class<?> klass) {
        return makeJob(jobKey, klass, null);
    }

    @SuppressWarnings("unchecked")
    public static JobDetail makeJob(JobKey jobKey, Class<?> klass, JobDataMap data) {
        if (data == null)
            data = new JobDataMap();
        return JobBuilder.newJob((Class<? extends Job>) klass).withIdentity(jobKey).setJobData(data).build();
    }
}
