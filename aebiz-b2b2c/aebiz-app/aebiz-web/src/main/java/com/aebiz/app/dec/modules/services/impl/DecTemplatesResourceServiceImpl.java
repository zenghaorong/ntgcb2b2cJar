package com.aebiz.app.dec.modules.services.impl;

import com.aebiz.app.dec.commons.utils.DecorateCacheConstant;
import com.aebiz.app.dec.modules.models.Dec_templates_files;
import com.aebiz.app.dec.modules.services.DecTemplatesFilesService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.dec.modules.models.Dec_templates_resource;
import com.aebiz.app.dec.modules.services.DecTemplatesResourceService;
import com.aebiz.baseframework.page.OffsetPager;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.redis.RedisService;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.beans.Transient;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class DecTemplatesResourceServiceImpl extends BaseServiceImpl<Dec_templates_resource> implements DecTemplatesResourceService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
    @Autowired
    private  RedisService redisService;
    @Autowired
    private DecTemplatesFilesService decTemplatesFilesService;
    private static final Log log = Logs.get();

    /**
     * DataTable Page
     *@param folderUuid
     * @param length   页大小
     * @param start    start
     * @param draw     draw
     * @param orders   排序
     * @param columns  字段
     * @param cnd      查询条件
     * @param linkName 关联查询
     * @return
     */
    public NutMap getData(String folderUuid, int length, int start, int draw, List<DataTableOrder> orders, List<DataTableColumn> columns, Cnd cnd, String linkName){
        NutMap re = new NutMap();
        if (orders != null && orders.size() > 0) {
            for (DataTableOrder order : orders) {
                DataTableColumn col = columns.get(order.getColumn());
                cnd.orderBy(Sqls.escapeSqlFieldValue(col.getData()).toString(), order.getDir());
            }
        }
        Pager pager = new OffsetPager(start, length);
        re.put("recordsFiltered", this.dao().count(this.getEntityClass(), cnd));
        List<Dec_templates_resource>  list=this.query(cnd.where("folderUuid","=",folderUuid));
        if (!Strings.isBlank(linkName)) {
            this.dao().fetchLinks(list, linkName);
        }
        re.put("data", list);
        re.put("draw", draw);
        re.put("recordsTotal", length);
        return re;
    }



    /**
     * 获取文件夹下所有资源文件
     */
    @Override
    public List<Dec_templates_resource> getFileResourcesByFolderUuid(
            String folderUuid) {
        List<Dec_templates_resource> resourceslist=this.query(Cnd.where("folderUuid","=",folderUuid));
        return  resourceslist;
    }

    /**
     * 根据资源key集合删除redis里保存的资源信息
     */
    @Override
    public void deleteResourcesOfRedis(byte[]... bytesArr) {
        try (Jedis jedis = redisService.jedis()) {
            jedis.del(bytesArr);
        }
    }
    /**
     * 删除文件夹下所有资源文件
     */
    @Override
    public void deleteFileResources(String folderUuid) {
        if (Strings.isEmpty(folderUuid)) {
            return;
        }
        this.clear(Cnd.where("folderUuid","=",folderUuid));
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("folderUuid", folderUuid);
    }

    @Override
    public byte[] getRedisResoureBytesByKey(String resourceKey) {
            byte[] resourceBytes = null;
        try (Jedis jedis = redisService.jedis()) {
            resourceBytes=jedis.get(resourceKey.getBytes());
        }
        return resourceBytes;
    }

    /**
     * 上传模板资源文件
     */
    @Override
    public void uploadFileResources(String folderUuid,
                                    MultipartFile[] resourceFiles) {
        Dec_templates_files filesModel = decTemplatesFilesService.fetch(folderUuid);
        String templateUuid = filesModel.getTemplateUuid();
        String folderName = filesModel.getShowName();

        for (int i = 0; i < resourceFiles.length; i++) {
            try {
                MultipartFile resourceFile = resourceFiles[i];
                String fileName = resourceFiles[i].getOriginalFilename();
                boolean fileNameExisted = this.checkFolderFileNameExisted(fileName, folderUuid);

                if (fileNameExisted) {
                    continue;
                }

                String fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1,
                        fileName.length());
                String resourceKey = templateUuid + "_" + folderName + "_" + fileName;

                Dec_templates_resource fileResourceModel = new Dec_templates_resource();
                fileResourceModel.setFileName(fileName);
                fileResourceModel.setSuffix(fileSuffix);
                fileResourceModel.setFolderUuid(folderUuid);
                fileResourceModel.setResourceKey(resourceKey);

                this.insert(fileResourceModel);

                // 将上传的资源文件存入redis
                this.saveResourceBytesToRedis(resourceKey.getBytes(), resourceFile.getBytes());
            } catch (Exception ex) {
               log.error(ex);
            }
        }
    }
    /**
     * 检查文件夹下文件名是否已存在
     */
    @Override
    public boolean checkFolderFileNameExisted(String fileName, String folderUuid) {
        List<Dec_templates_resource> list=this.query(Cnd.where("fileName","=",fileName).and("folderUuid","=",folderUuid));
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 将资源信息存入redis
     */
    @Override
    public void saveResourceBytesToRedis(byte[] resourceKey,
                                         byte[] resourceBytes) {
        try (Jedis jedis = redisService.jedis()) {
           jedis.set(resourceKey,resourceBytes);
        }
    }

    /**
     * 获取模板资源文件版本号
     * @param templateUuid
     */
    public String getTemplateResourceVersion(String templateUuid){
        String version = null;

        try (Jedis jedis = redisService.jedis()) {
            version= jedis.get(DecorateCacheConstant.TEMPLATE_RESOURCES_VERSION+templateUuid);
            if(version == null || "".equals(version)){
                version = System.currentTimeMillis()+"";
            }
        }
        return version;
    }


    /**
     * 获取模板下所有的资源key集合
     */
    @Override
    public List<String> getTemplateResourceKeysByTemplateUuid(String templateUuid) {
        String str="select resourceKey from Dec_templates_resource  where resourceKey like @resourceKey";
        Sql sql = Sqls.create(str).setParam("resourceKey",templateUuid + "%");
        sql.setCallback(new SqlCallback() {
            public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
                List<String> list = new LinkedList<String>();
                while (rs.next())
                    list.add(rs.getString("resourceKey"));
                return list;
            }
        });
        dao().execute(sql);
        List<String> list=sql.getList(String.class);
        return list;
    }

    /**
     * 删除模板下的所有资源文件
     */
    @Override
    public void deleteTemplateFileResourcesByTemplateUuid(String templateUuid) {
        if (Strings.isEmpty(templateUuid)) {
            return;
        }
        String str= " delete from Dec_templates_resource  where resourceKey like @resourceKey ";
        Sql sql=Sqls.create(str).setParam("resourceKey",templateUuid+"%");
        dao().execute(sql);
    }


    /**
     * 删除该id对应对的model的redis里的文件
     */
    @Override
    @Transient
    public void deleteByResouceId(String id){
        Dec_templates_resource resource=this.fetch(id);
        String resourceKey=resource .getResourceKey();
        this.delete(id);
        try (Jedis jedis = redisService.jedis()) {
            jedis.del(resourceKey);
        }
    }
}
