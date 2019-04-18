package com.aebiz.app.store.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 商家角色表
 * Created by wizzer on 2017/3/1.
 */
@Table("store_role")
@TableIndexes({@Index(name = "INDEX_STORE_ROLE_CODE", fields = {"code"}, unique = true)})
public class Store_role extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("角色名称")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String name;

    @Column
    @Comment("唯一标识")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String code;

    @Column
    @Comment("是否禁用")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean disabled;

    @Column
    @Comment("是否默认")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean defaultValue;

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String note;

    @ManyMany(from = "roleId", relation = "store_role_menu", to = "menuId")
    protected List<Store_menu> menus;

    @ManyMany(from = "roleId", relation = "store_user_role", to = "userId")
    private List<Store_user> users;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<Store_menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Store_menu> menus) {
        this.menus = menus;
    }

    public List<Store_user> getUsers() {
        return users;
    }

    public void setUsers(List<Store_user> users) {
        this.users = users;
    }

    public boolean isDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(boolean defaultValue) {
        this.defaultValue = defaultValue;
    }
}
