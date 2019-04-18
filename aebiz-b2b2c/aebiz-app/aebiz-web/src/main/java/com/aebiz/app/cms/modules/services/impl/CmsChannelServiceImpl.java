package com.aebiz.app.cms.modules.services.impl;

import com.aebiz.app.cms.modules.models.Cms_channel;
import com.aebiz.app.cms.modules.services.CmsChannelService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.baseframework.page.Pagination;
import com.aebiz.commons.utils.StringUtil;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.util.cri.SqlExpression;
import org.nutz.dao.util.cri.SqlExpressionGroup;
import org.nutz.lang.Strings;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@CacheConfig(cacheNames = "cmsCache")
public class CmsChannelServiceImpl extends BaseServiceImpl<Cms_channel> implements CmsChannelService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
    
    /**
     * 新增单位
     *
     * @param channel
     * @param pid
     */
    @Transactional
    public void save(Cms_channel channel, String pid) {
        String path = "";
        if (!Strings.isEmpty(pid)) {
        	Cms_channel pp = this.fetch(pid);
            path = pp.getPath();
        }
        channel.setPath(getSubPath("Cms_channel", "path", path));
        channel.setParentId(pid);
        dao().insert(channel);
        if (!Strings.isEmpty(pid)) {
            this.update(Chain.make("hasChildren", true), Cnd.where("id", "=", pid));
        }
    }
    /**
     * 级联删除单位
     *
     * @param channel
     */
    @Transactional
    public void deleteAndChild(Cms_channel channel) {
        this.clear(Cnd.where("path","like",channel.getPath() + "%"));
        if (!Strings.isEmpty(channel.getParentId())) {
            int count = count(Cnd.where("parentId", "=", channel.getParentId()));
            if (count < 1) {
                this.update(Chain.make("hasChildren",false),Cnd.where("pid","=",channel.getParentId()));
            }
        }
    }

    @CacheEvict(key = "#root.targetClass.getName()+'*'")
    @Async
    public void clearCache() {

    }
    /**
     * 根据parentId查询
     */
    @Cacheable
    public List<Cms_channel> list(String parentId){
        Cnd cnd=Cnd.NEW();
        cnd.and("disabled","=",false);
        if(Strings.isEmpty(parentId)){
            SqlExpressionGroup group=new SqlExpressionGroup();
            SqlExpression exp1=Cnd.exp("parentId","=","");
            SqlExpression exp2=Cnd.exp("parentId","is",null);
            group.or(exp1).or(exp2);
            cnd.and(group);
        }else {
            cnd.and("parentId","=",parentId);
        }
        cnd.asc("location");
        cnd.asc("path");
        return this.query(cnd);

    }

    /**
     * 根据id查询
     */
    @Cacheable
    public Cms_channel get(String id) {
        return this.fetch(id);
    }

    /**
     * 分页查询栏目信息
     */
    @Cacheable
    public Pagination listPage(String pageNumber,String pageSize,String parentId){

        Cnd cnd=Cnd.NEW();
        cnd.and("disabled","=",false);
        if(Strings.isEmpty(parentId)){
            SqlExpressionGroup group=new SqlExpressionGroup();
            SqlExpression exp1=Cnd.exp("parentId","=","");
            SqlExpression exp2=Cnd.exp("parentId","is",null);
            group.or(exp1).or(exp2);
            cnd.and(group);
        }else {
            cnd.and("parentId","=",parentId);
        }
        cnd.asc("location");
        cnd.asc("path");
        return  this.listPage(Integer.parseInt(pageNumber),Integer.parseInt(pageSize),cnd);
    }

    @Override
    @Transactional
    public void editDo(Cms_channel cmsChannel) {
        cmsChannel.setOpBy(StringUtil.getUid());
        cmsChannel.setOpAt((int) (System.currentTimeMillis() / 1000));
        String path, parentpath;
        // 前台没有传path值 所以在此查取
        if (null != cmsChannel.getParentId() && "" != cmsChannel.getParentId()) {
            parentpath = this.fetch(Cnd.where("id", "=", cmsChannel.getParentId())).getPath();
            path = this.fetch(Cnd.where("id", "=", cmsChannel.getId())).getPath();
            cmsChannel.setPath(parentpath + path.substring(path.length() - 4, path.length()));
        } else {
            path = this.fetch(Cnd.where("id", "=", cmsChannel.getId())).getPath();
            cmsChannel.setPath(path.substring(path.length() - 4, path.length()));
        }
        this.updateIgnoreNull(cmsChannel);
        // 若上级目录以前无子集 则更改hasChildren
        this.update(Chain.make("hasChildren", true), Cnd.where("id", "=", cmsChannel.getParentId()));
        List<Cms_channel> cmsChannels = this.query(Cnd.where("hasChildren", "=", true));
        // 更改后父类无Children则更改其hasChildren为false
        for (Cms_channel s_cmsChannel : cmsChannels) {
            if (null == this.fetch(Cnd.where("parentId", "=", s_cmsChannel.getId()))) {
                this.update(Chain.make("hasChildren", false),
                        Cnd.where("id", "=", s_cmsChannel.getId()));
            }
        }
    }

}
