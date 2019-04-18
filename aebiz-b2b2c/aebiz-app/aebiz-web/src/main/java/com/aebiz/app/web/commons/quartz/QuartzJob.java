package com.aebiz.app.web.commons.quartz;

import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.quartz.*;

/**
 * Created by wizzer on 2016/12/30.
 */
public class QuartzJob {

    protected String jobName;
    protected String jobGroup;
    protected String cron;
    protected String scheduled;
    protected String className;
    protected String dataMap;
    protected String comment;

    public QuartzJob() {
    }

    public QuartzJob(JobKey jobKey, Trigger trigger, JobDetail jobDetail) {
        setJobKey(jobKey);
        setTrigger(trigger);
        className = jobDetail.getJobClass().getName();
        dataMap = Json.toJson(jobDetail.getJobDataMap(), JsonFormat.compact());
    }

    public void setJobKey(JobKey jobKey) {
        setJobName(jobKey.getName());
        setJobGroup(jobKey.getGroup());
    }

    public JobKey getJobKey() {
        return JobKey.jobKey(jobName, jobGroup);
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getScheduled() {
        return scheduled;
    }

    public void setScheduled(String schedule) {
        this.scheduled = schedule;
    }

    public Trigger getTrigger() {
        if (Strings.isBlank(cron)) {
            NutMap map = Json.fromJson(NutMap.class, scheduled);
            return Quartzs.makeSimpleTrigger(jobName, jobGroup, map.getInt("rate"), map.getInt("count"), map.getLong("delay"));
        } else {
            return Quartzs.makeCronTrigger(jobName, jobGroup, cron);
        }
    }

    public void setTrigger(Trigger trigger) {
        if (trigger instanceof CronTrigger) {
            cron = ((CronTrigger) trigger).getCronExpression();
        } else if (trigger instanceof SimpleTrigger) {
            // TODO 怎么玩
            NutMap tmp = new NutMap();
            SimpleTrigger st = (SimpleTrigger) trigger;
            tmp.put("rate", st.getRepeatInterval());
            tmp.put("count", st.getRepeatCount());
            scheduled = Json.toJson(tmp, JsonFormat.compact());
        }
    }

    public TriggerKey getTriggerKey() {
        return new TriggerKey(jobName, jobGroup);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDataMap() {
        return dataMap;
    }

    public void setDataMap(String dataMap) {
        this.dataMap = dataMap;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}