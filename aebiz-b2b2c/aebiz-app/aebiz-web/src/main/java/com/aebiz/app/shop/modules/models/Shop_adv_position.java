package com.aebiz.app.shop.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 广告位置表
 * Created by wizzer on 2017/3/7.
 */
@Table("shop_adv_position")
@TableIndexes({@Index(name = "INDEX_ADV_POSITION_CODE", fields = {"code"}, unique = true)})
public class Shop_adv_position extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("位置标识")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String code;

    @Column
    @Comment("位置名称")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String name;

    @Column
    @Comment("位置介绍")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String note;

    @Column
    @Comment("广告类型")
    @ColDefine(type = ColType.INT)
    private int type;//0 图片 1 文字 2 幻灯片

    @Column
    @Comment("展现形式")
    @ColDefine(type = ColType.INT)
    private int display;//0 幻灯片 1 多广告展示 2单广告展示

    @Column
    @Comment("宽度")
    @ColDefine(type = ColType.INT)
    private int width;

    @Column
    @Comment("高度")
    @ColDefine(type = ColType.INT)
    private int height;

    @Column
    @Comment("是否禁用")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean disabled;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDisplay() {
        return display;
    }

    public void setDisplay(int display) {
        this.display = display;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
