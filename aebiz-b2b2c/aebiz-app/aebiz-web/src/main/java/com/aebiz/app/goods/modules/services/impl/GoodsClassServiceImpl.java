package com.aebiz.app.goods.modules.services.impl;

import com.aebiz.app.goods.modules.models.Goods_type;
import com.aebiz.app.goods.modules.services.GoodsTypeService;
import com.aebiz.baseframework.base.service.BaseServiceImpl;
import com.aebiz.app.goods.modules.models.Goods_class;
import com.aebiz.app.goods.modules.services.GoodsClassService;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class GoodsClassServiceImpl extends BaseServiceImpl<Goods_class> implements GoodsClassService {
    @Resource(name = "nutDao", type = Dao.class)
    public void init(Dao dao) {
        super.setDao(dao);
    }
    @Autowired
    private GoodsTypeService goodsTypeService;

    @Override
    public void deleteAndChild(Goods_class goodsClass) {
        dao().execute(Sqls.create("delete from goods_class where path like @path").setParam("path", goodsClass.getPath() + "%"));
        if (!Strings.isEmpty(goodsClass.getParentId())) {
            int count = count(Cnd.where("parentId", "=", goodsClass.getParentId()));
            if (count < 1) {
                dao().execute(Sqls.create("update goods_class set hasChildren=0 where id=@pid").setParam("pid", goodsClass.getParentId()));
            }
        }
    }

    @Override
    public void save(Goods_class goodsClass, String parentId) {
        String path = "";
        if (!Strings.isEmpty(parentId)) {
            Goods_class pp = this.fetch(parentId);
            path = pp.getPath();
        } else parentId = "";
        goodsClass.setPath(getSubPath("goods_class", "path", path));
        goodsClass.setParentId(parentId);
        dao().insert(goodsClass);
        if (!Strings.isEmpty(parentId)) {
            this.update(Chain.make("hasChildren", true), Cnd.where("id", "=", parentId));
        }
    }

   //获取所有的商品分类列表
    @Override
    public List<Goods_class> getAllClassGoodsName(){
       List<Goods_class> goodsClssList=this.query(Cnd.where("parentId","=",""));
        if(goodsClssList!=null && goodsClssList.size() > 0){
            for(Goods_class first : goodsClssList){
                //获取一级分类的前缀
                String prefix =first.getPath();
                //根据前缀获取该分类下所有的分类
                List<Goods_class>  productCatFrontModels =this.query(Cnd.where("path","like",prefix+"%"));
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
    private Goods_class sortProductCatFront(Goods_class productCatFrontModel,List<Goods_class>  productCatFrontModels){
        Map<String,List<Goods_class>> sortedFrontCatFrontMap = sortProductCatFrontModeToMap(productCatFrontModels);
        String firstUuid = productCatFrontModel.getId();
        //二级分类
        List<Goods_class> secondLevelProductCatFront = sortedFrontCatFrontMap.get(firstUuid);
        if(secondLevelProductCatFront!=null && secondLevelProductCatFront.size()>0){
            for(Goods_class secondLevel : secondLevelProductCatFront){
                //通过二级分类UUID获取三级分类
                List<Goods_class> thirdLevelList = sortedFrontCatFrontMap.get(secondLevel.getId());
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
    private Map<String,List<Goods_class>> sortProductCatFrontModeToMap(List<Goods_class>  productCatFrontModels){
        //结果集Map
        Map<String,List<Goods_class>> res = new HashMap<String,List<Goods_class>>();
        //key列表
        List<String> parentUuids = new ArrayList<String>();
        if(productCatFrontModels!=null && productCatFrontModels.size() > 0){
            for(Goods_class model : productCatFrontModels){
                String parentUuid = model.getParentId();
                if(parentUuids.contains(parentUuid)){
                    res.get(parentUuid).add(model);
                }else{
                    List<Goods_class> initList = new ArrayList<Goods_class>();
                    initList.add(model);
                    res.put(parentUuid, initList);
                    //key列表保存key
                    parentUuids.add(parentUuid);
                }
            }
        }
        //对map中每个数组排序
        for(String key:res.keySet()){
            List<Goods_class> list = res.get(key);
            Collections.sort(list, new Comparator<Goods_class>() {

                public int compare(Goods_class o1,
                                   Goods_class o2) {
                    return o1.getLocation()>o2.getLocation()?1:-1;
                }
            });
        }

        return res;
    }

    //获取一级分类
    public List<Goods_class> getSubProductCategoryByParentUuid(){
       return this.query(Cnd.where("parentId","=","").and("disabled","=",false).orderBy("location","asc"));
    }
}
