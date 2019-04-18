package com.aebiz.app.sys.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * Created by wizzer on 2016/7/30.
 */
@Table("sys_task")
public class Sys_task extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("uuid()")})
    private String id;

    @Column
    @Comment("任务名")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String name;

    @Column
    @Comment("任务类别 SYSTEM:不可禁用或删除 CUSTOM:")
    @ColDefine(type = ColType.VARCHAR, width = 10)
    private TypeEnum type = TypeEnum.CUSTOM;

    @Column
    @Comment("执行类")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String jobClass;

    @Column
    @Comment("任务说明")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String note;

    @Column
    @Comment("定时规则")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String cron;

    @Column
    @Comment("执行参数")
    @ColDefine(type = ColType.TEXT)
    private String data;

    @Column
    @Comment("执行时间")
    @ColDefine(type = ColType.INT)
    private Integer exeAt;

    @Column
    @Comment("下次执行时间")
    @ColDefine(type = ColType.INT)
    private Integer nextAt;

    @Column
    @Comment("执行结果")
    @ColDefine(type = ColType.TEXT)
    private String exeResult;

    @Column
    @Comment("任务状态")
    @ColDefine(type = ColType.INT, width = 1)
    private Integer status;//0 启用 1 禁用 2 暂停

    public static enum TypeEnum {
        SYSTEM, CUSTOM
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeEnum getType() {
        return type;
    }

    public void setType(TypeEnum type) {
        this.type = type;
    }

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getExeAt() {
        return exeAt;
    }

    public void setExeAt(Integer exeAt) {
        this.exeAt = exeAt;
    }

    public Integer getNextAt() {
        return nextAt;
    }

    public void setNextAt(Integer nextAt) {
        this.nextAt = nextAt;
    }

    public String getExeResult() {
        return exeResult;
    }

    public void setExeResult(String exeResult) {
        this.exeResult = exeResult;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
