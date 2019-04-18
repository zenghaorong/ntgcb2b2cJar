package com.aebiz.app.store.modules.services.impl;

import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.store.modules.models.Store_goodsclass;
import com.aebiz.app.store.modules.services.StoreGoodsclassService;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
@CacheConfig(cacheNames = "storeGoodsClassCache")
public class StoreGoodsclassServiceImpl extends BaseServiceImpl<Store_goodsclass> implements StoreGoodsclassService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
    /**
     * 清除货币缓存
     */
    @CacheEvict(key = "#root.targetClass.getName()+'*'")
    @Async
    public void clearCache() {

    }

    public void save(Store_goodsclass goodsClass, String parentId, String storeId) {
        String path = "";
        if (Strings.isNotBlank(parentId)) {
            Store_goodsclass pclass = this.fetch(parentId);
            if (!Lang.isEmpty(pclass)) {
                path = pclass.getPath();
            }
        }
        goodsClass.setPath(getSubPath("store_goodsclass", "path", path));
        goodsClass.setParentId(parentId);
        goodsClass.setStoreId(storeId);
        dao().insert(goodsClass);
        if (!Strings.isEmpty(parentId)) {
            this.update(Chain.make("hasChildren", true), Cnd.where("id", "=", parentId));
        }

    }

    //获取所有的商品分类列表
    @Override
    @Cacheable
    public List<Store_goodsclass> getAllClassGoodsName(){
        List<Store_goodsclass> goodsClssList=this.query(Cnd.where("parentId","=",""));
        if(goodsClssList!=null && goodsClssList.size() > 0){
            for(Store_goodsclass first : goodsClssList){
                //获取一级分类的前缀
                String prefix =first.getPath();
                //根据前缀获取该分类下所有的分类
                List<Store_goodsclass>  productCatFrontModels =this.query(Cnd.where("path","like",prefix+"%"));
                //分类一级分类下的分类，并且组装成完整的一级分类
                first = sortProductCatFront(first,productCatFrontModels);
            }
        }
        return goodsClssList;
        //  Map<String,List<ProductCategoryFrontModel>> sortedFrontCatFrontMap = sortProductCatFrontModeToMap(productCatFrontModels);
    }


    /**
     * 对一级分类下的所有分类进行分类排序，总共3级分类
     * @return 组装好的一级分类
     */
    private Store_goodsclass sortProductCatFront(Store_goodsclass productCatFrontModel, List<Store_goodsclass>  productCatFrontModels){
        Map<String,List<Store_goodsclass>> sortedFrontCatFrontMap = sortProductCatFrontModeToMap(productCatFrontModels);
        String firstUuid = productCatFrontModel.getId();
        //二级分类
        List<Store_goodsclass> secondLevelProductCatFront = sortedFrontCatFrontMap.get(firstUuid);
        if(secondLevelProductCatFront!=null && secondLevelProductCatFront.size()>0){
            for(Store_goodsclass secondLevel : secondLevelProductCatFront){
                //通过二级分类UUID获取三级分类
                List<Store_goodsclass> thirdLevelList = sortedFrontCatFrontMap.get(secondLevel.getId());
                //设置三级
                secondLevel.setClassList(thirdLevelList);
            }
        }
        //将二级model设置给一级model
        productCatFrontModel.setClassList(secondLevelProductCatFront);
        return productCatFrontModel;
    }
    /**
     * 将 productCatFrontModels  按照parentUUid 分类
     * @param productCatFrontModels
     * @return 按照parentUuid分类好的Map集合
     */
    private Map<String,List<Store_goodsclass>> sortProductCatFrontModeToMap(List<Store_goodsclass>  productCatFrontModels){
        //结果集Map
        Map<String,List<Store_goodsclass>> res = new HashMap<String,List<Store_goodsclass>>();
        //key列表
        List<String> parentUuids = new ArrayList<String>();
        if(productCatFrontModels!=null && productCatFrontModels.size() > 0){
            for(Store_goodsclass model : productCatFrontModels){
                String parentUuid = model.getParentId();
                if(parentUuids.contains(parentUuid)){
                    res.get(parentUuid).add(model);
                }else{
                    List<Store_goodsclass> initList = new ArrayList<Store_goodsclass>();
                    initList.add(model);
                    res.put(parentUuid, initList);
                    //key列表保存key
                    parentUuids.add(parentUuid);
                }
            }
        }
        //对map中每个数组排序
        for(String key:res.keySet()){
            List<Store_goodsclass> list = res.get(key);
            Collections.sort(list, new Comparator<Store_goodsclass>() {

                public int compare(Store_goodsclass o1,
                                   Store_goodsclass o2) {
                    return o1.getLocation()>o2.getLocation()?1:-1;
                }
            });
        }

        return res;
    }




    /**
     * 根据分类uuid查询分类,三级分类
     * @return
     */
    public Store_goodsclass getByCategoryUuid(String categoryUuid){
        Store_goodsclass storeClass=null;
        if(!Strings.isEmpty(categoryUuid)){
            storeClass=this.fetch(categoryUuid);
            if(storeClass!=null){
                List<Store_goodsclass> subList=this.query(Cnd.where("parentId","=",categoryUuid).desc("location"));
                if(subList !=null && subList.size()>0){
                    for (Store_goodsclass roorCategory : subList) {
                        List<Store_goodsclass> list=this.query(Cnd.where("parentId","=",roorCategory.getId()).desc("location"));
                        roorCategory.setClassList(list);
                    }
                }
                storeClass.setClassList(subList);
            }
        }
        return storeClass;
    }


    /**
     * 对外接口：获取所有店铺分类
     *
     * @param parentUuid
     * 			父类uuid
     * @return
     */
    public List<Store_goodsclass> getAllProductCategoryFront(String parentUuid){
        List<Store_goodsclass> tempList=this.query(Cnd.where("parentId","=",parentUuid).desc("location"));
        if(tempList==null || tempList.size()==0){
            return null;
        }
       /* for (Store_goodsclass roorCategory : tempList) {
            List<Store_goodsclass> list=this.getAllProductCategoryFront(roorCategory.getId());
            roorCategory.setClassList(list);
        }*/
        return tempList;
    }

    @Transactional
    public void sort(String[] menuIds, String storeId) {
        int i = 0;
        this.update(Chain.make("location", 0), Cnd.where("storeId", "=", storeId));
        for (String s : menuIds) {
            if (!Strings.isBlank(s)) {
                this.update(Chain.make("location", i), Cnd.where("id", "=", s));
                i++;
            }
        }
    }
    /**
     * 根据商户查询商户某一分类对应的同级分类
     * @param storeUuid
     * @return
     */
    @Override
    public List<Store_goodsclass> getSameLevelProductCategorysByStoreUuid(String storeUuid) {
        return this.query(Cnd.where("storeId","=",storeUuid).and("parentId","!=","").groupBy("parentId"));
    }



}
