package com.aebiz.app.sys.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * Created by wizzer on 2016/6/21.
 */
@Table("sys_log_${id}")
@TableIndexes({@Index(name = "INDEX_SYS_LOG_TYPE", fields = {"type"}, unique = false)})
public class Sys_log extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Id
    private long id;

    @Column
    @Comment("用户名")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String username;

    @Column
    @Comment("IP地址")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String ip;

    @Column
    @Comment("请求模块")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String module;

    @Column
    @Comment("请求方法")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String action;

    @Column
    @Comment("日志描述")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String description;

    @Column
    @Comment("日志类型(默认:system)")
    @ColDefine(type = ColType.VARCHAR, width = 10)
    private TypeEnum type;

    @Column
    @Comment("方法")
    @ColDefine(type = ColType.TEXT)
    private String methodMeta;

    @Column
    @Comment("参数")
    @ColDefine(type = ColType.TEXT)
    private String parameters;

    @Column
    @Comment("返回值")
    @ColDefine(type = ColType.TEXT)
    private String methodReturn;

    @Column
    @Comment("请求时间")
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String actionTime;

    @Column
    @Comment("执行时间")
    @ColDefine(type = ColType.INT)
    private long operationTime;

    @Column
    @Comment("操作状态(0:失败 1:成功)")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean success = true;

    public static enum TypeEnum {
        SYSTEM, LOGIN, STORE
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TypeEnum getType() {
        return type;
    }

    public void setType(TypeEnum type) {
        this.type = type;
    }

    public String getMethodMeta() {
        return methodMeta;
    }

    public void setMethodMeta(String methodMeta) {
        this.methodMeta = methodMeta;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getMethodReturn() {
        return methodReturn;
    }

    public void setMethodReturn(String methodReturn) {
        this.methodReturn = methodReturn;
    }

    public String getActionTime() {
        return actionTime;
    }

    public void setActionTime(String actionTime) {
        this.actionTime = actionTime;
    }

    public long getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(long operationTime) {
        this.operationTime = operationTime;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
