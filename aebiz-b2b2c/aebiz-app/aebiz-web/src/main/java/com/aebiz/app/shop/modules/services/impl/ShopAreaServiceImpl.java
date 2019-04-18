package com.aebiz.app.shop.modules.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aebiz.app.shop.modules.models.Shop_area;
import com.aebiz.app.shop.modules.services.ShopAreaService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;

@Service
@CacheConfig(cacheNames = "shopCache")
public class ShopAreaServiceImpl extends BaseServiceImpl<Shop_area> implements ShopAreaService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }


    @CacheEvict(key = "#root.targetClass.getName()+'*'")
    @Async
    public void clearCache() {

    }

    /**
     * 通过code获取name
     *
     * @param code
     * @return
     */
    @Cacheable
    public String getNameByCode(String code) {
        Shop_area shoparea = this.fetch(Cnd.where("code", "=", code));
        return shoparea == null ? "" : shoparea.getName();
    }

    /**
     * 通过id获取name
     *
     * @param id
     * @return
     */
    @Cacheable
    public String getNameById(String id) {
        Shop_area shoparea = this.fetch(id);
        return shoparea == null ? "" : shoparea.getName();
    }

    /**
     *  通过下级code获取父级code
     * @param childCode
     * @return
     */
    @Cacheable
    public String getParentCode(String childCode) {
        Shop_area area = this.getByCode(childCode);
        if (!Lang.isEmpty(area) && Strings.isNotBlank(area.getParentId())) {
            Shop_area parent = this.fetch(area.getParentId());
            if (!Lang.isEmpty(parent)) {
                return parent.getCode();
            }
            return null;
        }
        return null;
    }

     /**
     * 通过code获取下级map
     *
     * @param code
     * @return
     */
    @Cacheable
    public Map getArea(String code) {
        if(Strings.isEmpty(code)){
            return this.getMap(Sqls.create("select code,name from shop_area where parentId = '' or parentId is null order by location asc"));
        }
        Shop_area shoparea = this.fetch(Cnd.where("code", "=", code));
        return shoparea == null ? new HashMap() : this.getMap(Sqls.create("select code,name from shop_area where parentId = @id order by location asc").setParam("id", shoparea.getId()));
    }

    @Cacheable
    public Shop_area getByCode(String code) {
        return this.fetch(Cnd.where("code", "=", code));
    }

    /**
     * 通过code获取下级节点List
     *
     * @param code
     * @return
     */
    @Cacheable
    public List<Shop_area> getAreaNodeList(String code) {
        if (Strings.isNotBlank(code)) {
            Shop_area area = this.fetch(Cnd.where("code", "=", code).and("disabled", "=", false));
            if (!Lang.isEmpty(area)) {
                return this.query("^(code|name|path|hasChildren|location)$",Cnd.where("parentId", "=", Strings.sNull(area.getId())).and("disabled", "=", false).asc("location"));
            }
        }
        return null;
    }


    /**
     * 新增单位
     *
     * @param shoparea
     * @param pid
     */
    @Transactional
    public void save(Shop_area shoparea, String pid) {
        String path = "";
        if (!Strings.isEmpty(pid)) {
            Shop_area pp = this.fetch(pid);
            path = pp.getPath();
        }
        shoparea.setPath(getSubPath("shop_area", "path", path));
        shoparea.setParentId(pid);
        dao().insert(shoparea);
        if (!Strings.isEmpty(pid)) {
            this.update(Chain.make("hasChildren", true), Cnd.where("id", "=", pid));
        }
    }

    /**
     * 级联删除单位
     *
     * @param shoparea
     */
    @Transactional
    public void deleteAndChild(Shop_area shoparea) {
        this.clear(Cnd.where("path","like",shoparea.getPath()+"%"));
        if (!Strings.isEmpty(shoparea.getParentId())) {
            int count = count(Cnd.where("parentId", "=", shoparea.getParentId()));
            if (count < 1) {
                this.update(Chain.make("hasChildren",false),Cnd.where("id","=",shoparea.getParentId()));
            }
        }
    }

    @Cacheable
    public List<Shop_area> getShopAreaList() {
        return this.query(Cnd.where("delFlag", "=", false).asc("location"));
    }
}
