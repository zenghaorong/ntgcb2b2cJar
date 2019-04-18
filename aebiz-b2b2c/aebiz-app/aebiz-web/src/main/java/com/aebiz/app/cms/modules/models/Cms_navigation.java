package com.aebiz.app.cms.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * Created by yewei on 2017/6/1.
 */
@Table("cms_navigation")
public class Cms_navigation extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = { @EL("ig(view.tableName,'')") })
    private String id;

    @Column
    @Comment("频道名称")
    @ColDefine(type = ColType.VARCHAR, width = 120)
    private String navigationName;

    @Column
    @Comment("链接地址")
    @ColDefine(type = ColType.VARCHAR, width = 120)
    private String navigationUrl;


    @Column
    @Comment("所在位置")
    @ColDefine(type = ColType.INT)
    @Default("1")
    private Integer location;


    @Column
    @Comment("创建时间")
    @ColDefine(type = ColType.INT)
    private Integer createAt;


    @Column
    @Comment("是否发布")
    @ColDefine(type = ColType.BOOLEAN, width = 120)
    private boolean disabled;


    @Column
    @Comment("频道类型")
    @ColDefine(type = ColType.INT)
    private Integer type;//1：自定义 2：链接类型频道

    @Column
    @Comment("频道所属客户端")
    @ColDefine(type = ColType.INT)
    private Integer belongType ;// 1：pc端 2：app端 3：pad端 4：微信端

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNavigationName() {
        return navigationName;
    }

    public void setNavigationName(String navigationName) {
        this.navigationName = navigationName;
    }

    public String getNavigationUrl() {
        return navigationUrl;
    }

    public void setNavigationUrl(String navigationUrl) {
        this.navigationUrl = navigationUrl;
    }

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    public Integer getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Integer createAt) {
        this.createAt = createAt;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getBelongType() {
        return belongType;
    }

    public void setBelongType(Integer belongType) {
        this.belongType = belongType;
    }
}
