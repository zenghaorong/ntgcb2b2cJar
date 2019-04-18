package com.aebiz.app.store.modules.services.impl;

import com.aebiz.app.store.modules.models.Store_favorite;
import com.aebiz.app.store.modules.models.Store_main;
import com.aebiz.app.store.modules.services.StoreFavoriteService;
import com.aebiz.app.store.modules.services.StoreMainService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StoreFavoriteServiceImpl extends BaseServiceImpl<Store_favorite> implements StoreFavoriteService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }

    @Autowired
    private StoreMainService storeMainService;

    /**
     * 保存店铺收藏信息(调用方法前要先验证accountId、store_main对应的记录是否存在)
     * @param accountId 会员id
     * @param store_main 店铺
     * @return
     */
    @Override
    public Store_favorite saveData(String accountId, Store_main store_main) {
        Store_favorite store_favorite = new Store_favorite();
        store_favorite.setAccountId(accountId);
        store_favorite.setStoreId(store_main.getId());
        store_favorite.setStoreName(store_main.getStoreName());
        store_favorite.setFavoriteTime((int)(System.currentTimeMillis()/1000));
        return dao().insert(store_favorite);
    }

    @Override
    @Transactional
    public Map<String, Object> selectDataAll(String accountId, Integer page, Integer rows, Integer status) {
        String sq="SELECT * FROM store_favorite sf\n" +
                "WHERE sf.accountId='"+accountId+"' and sf.delFlag=0";
        Sql sql1 = Sqls.queryRecord(sq+" limit "+(page-1)*rows+","+rows);//
        dao().execute(sql1);
        int totle = dao().count("store_favorite sf \n" +
                "WHERE sf.accountId='"+accountId+"' and sf.delFlag=0");
        List<Store_favorite> list = sql1.getList(Store_favorite.class);
        Map<String,Object> map = new HashMap<String,Object>();
        int total = (totle+rows-1)/rows;
        map.put("totalPage", total);
        map.put("page", page);
        map.put("status", status);
        map.put("records", totle);
        map.put("count", list.size());
        map.put("rowList", list);
        return map;
    }

    @Override
    @Transactional
    public Map<String, Object> selectData(String content,String accountId, Integer page, Integer rows, Integer status) {
        String sq="SELECT * FROM store_favorite sf\n" +
                "WHERE sf.accountId='"+accountId+"'and sf.storeName like '%"+content+"%' and sf.delFlag=0";
        Sql sql1 = Sqls.queryRecord(sq+" limit "+(page-1)*rows+","+rows);
        dao().execute(sql1);
        int totle = dao().count("store_favorite sf \n" +
                "WHERE sf.accountId='"+accountId+"' and sf.storeName like '%"+content+"%' and sf.delFlag=0");
        List<Store_favorite> list = sql1.getList(Store_favorite.class);
        Map<String,Object> map = new HashMap<String,Object>();
        int total = (totle+rows-1)/rows;
        map.put("totalPage", total);
        map.put("page", page);
        map.put("status", status);
        map.put("records", totle);
        map.put("count", list.size());
        map.put("rowList", list);
        return map;
    }
}
