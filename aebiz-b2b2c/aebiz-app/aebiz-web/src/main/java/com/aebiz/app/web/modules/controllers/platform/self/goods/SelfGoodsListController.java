package com.aebiz.app.web.modules.controllers.platform.self.goods;

import com.aebiz.app.goods.modules.models.*;
import com.aebiz.app.goods.modules.services.*;
import com.aebiz.app.member.modules.services.MemberTypeService;
import com.aebiz.app.shop.modules.models.Shop_area;
import com.aebiz.app.shop.modules.models.Shop_area_management;
import com.aebiz.app.shop.modules.services.ShopAreaManagementService;
import com.aebiz.app.shop.modules.services.ShopAreaService;
import com.aebiz.app.shop.modules.services.ShopConfigService;
import com.aebiz.app.store.modules.models.Store_goodsclass;
import com.aebiz.app.store.modules.models.Store_main;
import com.aebiz.app.store.modules.services.StoreMainService;
import com.aebiz.app.sys.modules.services.SysDictService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.util.cri.SqlExpression;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 自营商品列表
 */
@Controller
@RequestMapping("/platform/self/goods/list")
public class SelfGoodsListController {

    @Autowired
	private GoodsService goodsService;
    @Autowired
    private GoodsClassService goodsClassService;
    @Autowired
    private GoodsTypeService goodsTypeService;
    @Autowired
    private GoodsSpecService goodsSpecService;
    @Autowired
    private GoodsTypeSpecService goodsTypeSpecService;
    @Autowired
    private GoodsTypeParamgService goodsTypeParamgService;
    @Autowired
    private GoodsTypePropsService goodsTypePropsService;
    @Autowired
    private GoodsBrandService goodsBrandService;
    @Autowired
    private GoodsTypeBrandService goodsTypeBrandService;
    @Autowired
    private GoodsTagService goodsTagService;
    @Autowired
    private GoodsProductService goodsProductService;
    @Autowired
    private MemberTypeService memberTypeService;
    @Autowired
    private GoodsImageService goodsImageService;
    @Autowired
    private SysDictService sysDictService;
    @Autowired
    private ShopAreaService shopAreaService;
    @Autowired
    private GoodsProductAreaService goodsProductAreaService;
    @Autowired
    private GoodsMainChecklogService goodsMainChecklogService;
    @Autowired
    private StoreMainService storeMainService;
    @Autowired
    private ShopConfigService shopConfigService;
    @Autowired
    private ShopAreaManagementService shopAreaManagementService;

    @RequestMapping("")
    @RequiresPermissions("self.goods.list")
    public String index(HttpServletRequest req) {
        req.setAttribute("typeList", goodsTypeService.query(Cnd.NEW()));
        req.setAttribute("brandList", goodsBrandService.query(Cnd.NEW()));
        req.setAttribute("tagList", goodsTagService.query(Cnd.NEW()));
        return "pages/platform/self/goods/list/index";
    }

    /**
     *  草稿箱列表
     * @param req
     * @return
     */
    @RequestMapping("/draft")
    @RequiresPermissions("self.goods.list")
    public String draftIndex(HttpServletRequest req) {
        req.setAttribute("typeList", goodsTypeService.query(Cnd.NEW()));
        req.setAttribute("brandList", goodsBrandService.query(Cnd.NEW()));
        req.setAttribute("tagList", goodsTagService.query(Cnd.NEW()));
        return "pages/platform/self/goods/list/draft";
    }

    /**
     * 垃圾箱列表
     * @param req
     * @return
     */
    @RequestMapping("/trash")
    @RequiresPermissions("self.goods.list")
    public String trashIndex(HttpServletRequest req) {
        req.setAttribute("typeList", goodsTypeService.query(Cnd.NEW()));
        req.setAttribute("brandList", goodsBrandService.query(Cnd.NEW()));
        req.setAttribute("tagList", goodsTagService.query(Cnd.NEW()));
        return "pages/platform/self/goods/list/trash";
    }

    /**
     * 商品列表页
     *
     * @return
     */
    @RequestMapping("/data")
    @SJson("{locked:'pcNote|wapNote|tvNote|prop|param|spec',ignoreNull:false}")
    @RequiresPermissions("self.goods.list")
    public Object data(Goods_main goods, @RequestParam(value = "tagId", required = false) String tagId, @RequestParam(value = "tmp_hasSpec", required = false) String hasSpec, DataTable dataTable) {
        NutMap fieldMap = NutMap.NEW();
        if (Strings.isNotBlank(tagId)) {
            fieldMap.put("tagId", tagId);
        }
        if (Strings.isNotBlank(hasSpec)) {
            fieldMap.put("hasSpec", hasSpec);
        }
        fieldMap.put("storeId", "2017060000000001");
        return goodsService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), "^(tagList|goodsClass|goodsType|goodsBrand)$", goods, fieldMap);
    }

    /**
     * 商品编辑页
     * @param id 商品ID
     * @param req
     * @return
     */
    @RequestMapping(value = {"/edit/{id}", "/{type}/edit/{id}"})
    @RequiresPermissions("self.goods.list")
    public String edit(@PathVariable(value = "type", required = false)String type, @PathVariable("id")String id, HttpServletRequest req) {

        Goods_main goods = goodsService.fetch(Cnd.where("id", "=", id).and("storeId", "=", "2017060000000001"));

        //获取商品类型对应的属性信息
        List<Goods_type_props> typePropList = goodsTypePropsService.query(Cnd.where("typeId", "=", goods.getTypeId()).asc("location"));
        goodsTypePropsService.fetchLinks(typePropList, "propsValues", Cnd.orderBy().asc("location"));

        //获取商品类型对应的参数信息
        List<Goods_type_paramg> typeParamgList = goodsTypeParamgService.query(Cnd.where("typeId", "=", goods.getTypeId()).asc("location"));
        goodsTypeParamgService.fetchLinks(typeParamgList, "params", Cnd.orderBy().asc("location"));

        //获取商品类型对应的规格信息
        List<String> ids = new ArrayList<>();
        List<Goods_type_spec> typeSpecList = goodsTypeSpecService.query("specId", Cnd.where("typeId", "=", goods.getTypeId()).asc("location"));
        for (Goods_type_spec spec : typeSpecList) {
            ids.add(spec.getSpecId());
        }
        //组装规格的值
        List<Goods_spec> specList = goodsSpecService.query(Cnd.where("id", "in", ids).asc("location"));
        goodsSpecService.fetchLinks(specList, "specValues", Cnd.orderBy().asc("location"));

        //组装商品各类关联表数据
        goodsService.fetchLinks(goods, "^(goodsClass|goodsBrand|imageList)$", Cnd.orderBy().asc("location"));
        goodsService.fetchLinks(goods, "^(goodsType|storeGoodsClassList|tagList)$");

        //获取货品列表
        List<Goods_product> productsList = goodsProductService.query(Cnd.where("goodsId", "=", goods.getId()));
        //组装货品各类关联表数据
        goodsProductService.fetchLinks(productsList, "^(areaList|memberTypeList)$");

        //获取省市列表
        List<Shop_area> provinceCityList = new ArrayList<>();
        Map<String, String> provinceCityMap = new HashMap<>();
        List<Shop_area> shopAreaList = shopAreaService.query("^(code|name|path|hasChildren|location)$",Cnd.where("disabled", "=", false).asc("location"));
        if (!Lang.isEmpty(shopAreaList)) {
            provinceCityList.addAll(shopAreaList.parallelStream().filter(p->p.getPath().length() <= 12).collect(Collectors.toList()));
            //组装省市Map
            provinceCityMap.putAll(shopAreaList.parallelStream().collect(Collectors.toMap(Shop_area::getCode, area->area.getName())));
        }

        //获取片区列表
        List<Shop_area_management> areaList = shopAreaManagementService.query(Cnd.NEW());
        Map<String, String> areaMap = new HashMap<>();
        if (!Lang.isEmpty(areaList)) {
            areaMap.putAll(areaList.parallelStream().collect(Collectors.toMap(Shop_area_management::getCode, area->area.getName())));
        }

        req.setAttribute("type", type);
        req.setAttribute("needCheck", shopConfigService.checkOnGoodsPublish());
        req.setAttribute("provinceCityList", provinceCityList);
        req.setAttribute("provinceCityMap", provinceCityMap);
        req.setAttribute("areaList", areaList);
        req.setAttribute("areaMap", areaMap);
        req.setAttribute("specList", specList);
        req.setAttribute("typePropList", typePropList);
        req.setAttribute("typeParamgList", typeParamgList);
        req.setAttribute("typeList", goodsTypeService.query(Cnd.NEW()));
        req.setAttribute("brandList", goodsBrandService.listByGoodsTypeId(goods.getTypeId()));
        req.setAttribute("tagList", goodsTagService.query(Cnd.NEW()));
        req.setAttribute("unitDictList", sysDictService.getSubListByCode("goods_unit"));
        req.setAttribute("memberTypeList", memberTypeService.query(Cnd.NEW()));
        req.setAttribute("productsList", productsList);
        req.setAttribute("obj", goods);

        return "pages/platform/self/goods/list/edit";
    }

    /**
     * 保存商品
     * @param goods 商品
     * @param saleAt 上架时间
     * @param offAt 下架时间
     * @param storeGoodsclass 前台分类
     * @param tags 标签
     * @param images 相册图
     * @param products 货品
     * @return
     */
    @RequestMapping("/editDo")
    @SJson("full")
    @RequiresPermissions("self.goods.list.edit")
    @SLog(description = "保存商品")
    public Object editDo(Goods_main goods,
                         @RequestParam(value = "tmp_saleAt", required = false) String saleAt,
                         @RequestParam(value = "tmp_offAt", required = false) String offAt,
                         @RequestParam("front_classes") String storeGoodsclass,
                         @RequestParam("tags")String tags, @RequestParam("images") String images,
                         @RequestParam("products") String products) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (Strings.isNotBlank(saleAt)) {
                goods.setSaleAt((int) (sdf.parse(saleAt).getTime() / 1000));
            }
            if (Strings.isNotBlank(offAt)) {
                goods.setOffAt((int) (sdf.parse(offAt).getTime() / 1000));
            }
            goods.setOpBy(StringUtil.getUid());
            goods.setOpAt((int) (System.currentTimeMillis() / 1000));
            goods.setTagList(Json.fromJsonAsList(Goods_tag.class, tags));
            goods.setImageList(Json.fromJsonAsList(Goods_image.class, images));
            goods.setStoreGoodsClassList(Json.fromJsonAsList(Store_goodsclass.class, storeGoodsclass));
            goods.setGoodsProducts(Json.fromJsonAsList(Goods_product.class, products));
            //校验SKU
            if (goodsService.isDuplicatedSku(goods)) {
                return Result.error("goods.main.tip.skuhasbeenused");
            }
            goodsService.save(goods);
            return Result.success("globals.result.success", goods.getId());
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }
    /**
     * 商品上架
     *
     * @param id 商品ID
     * @return
     */
    @RequestMapping("/{id}/upDo")
    @SJson
    @RequiresPermissions("self.goods.list.edit")
    @SLog(description = "商品下架")
    public Object upDo(@PathVariable("id") String id, HttpServletRequest req){
        try {
            goodsService.updown(id, true, "2017060000000001");
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    /**
     * 商品下架
     *
     * @param id 商品ID
     * @return
     */
    @RequestMapping("/{id}/downDo")
    @SJson
    @RequiresPermissions("self.goods.list.edit")
    @SLog(description = "商品下架")
    public Object downDo(@PathVariable("id") String id, HttpServletRequest req){
        try {
            goodsService.updown(id, false, "2017060000000001");
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    /**
     * 商品批量上架
     *
     * @param ids 商品IDS
     * @return
     */
    @RequestMapping("/upsDo")
    @SJson
    @RequiresPermissions("self.goods.list.edit")
    @SLog(description = "商品批量上架")
    public Object upsDo(@RequestParam("ids") String[] ids){
        try {
            goodsService.updowns(ids, false, "2017060000000001");//StringUtil.getStoreUid()
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    /**
     * 商品批量下架
     *
     * @param ids 商品IDS
     * @return
     */
    @RequestMapping("/downsDo")
    @SJson
    @RequiresPermissions("self.goods.list.edit")
    @SLog(description = "商品批量下架")
    public Object downsDo(@RequestParam("ids") String[] ids){
        try {
            goodsService.updowns(ids, false, "2017060000000001");
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    /**
     * 保存商品标签
     *
     * @param ids 商品IDS
     * @param tagIds 标签IDS
     * @return
     */
    @RequestMapping("/saveTag")
    @SJson
    @RequiresPermissions("self.goods.list.edit")
    @SLog(description = "保存商品标签")
    public Object saveTag(@RequestParam("ids") String[] ids, @RequestParam("tagIds") String[] tagIds) {
        try {
            goodsService.saveTagLink(ids, tagIds);
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

    @RequestMapping("/delete/{id}")
    @SJson
    @SLog(description = "删除商品")
    @RequiresPermissions("self.goods.list.delete")
    public Object delete(@PathVariable String id, HttpServletRequest req) {
        try {
            goodsService.vDeleteGoods(id, "2017060000000001");
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/delete")
    @SJson
    @SLog(description = "批量删除商品")
    @RequiresPermissions("self.goods.list.delete")
    public Object delete(@RequestParam("ids") String[] ids, HttpServletRequest req) {
        try {
            goodsService.vDeleteGoods(ids, "2017060000000001");
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping(value = {"/detail/{id}", "/{type}/detail/{id}"})
    @RequiresPermissions("self.goods.list")
    public String detail(@PathVariable String id, @PathVariable(value = "type", required = false) String type, HttpServletRequest req) {
        Goods_main goods = goodsService.fetch(Cnd.where("id", "=", id).and("storeId", "=", "2017060000000001"));

        //获取商品类型对应的属性信息
        List<Goods_type_props> typePropList = goodsTypePropsService.query(Cnd.where("typeId", "=", goods.getTypeId()).asc("location"));
        goodsTypePropsService.fetchLinks(typePropList, "propsValues", Cnd.orderBy().asc("location"));

        //获取商品类型对应的参数信息
        List<Goods_type_paramg> typeParamgList = goodsTypeParamgService.query(Cnd.where("typeId", "=", goods.getTypeId()).asc("location"));
        goodsTypeParamgService.fetchLinks(typeParamgList, "params", Cnd.orderBy().asc("location"));

        //获取商品类型对应的规格信息
        List<String> ids = new ArrayList<>();
        List<Goods_type_spec> typeSpecList = goodsTypeSpecService.query("specId", Cnd.where("typeId", "=", goods.getTypeId()).asc("location"));
        for (Goods_type_spec spec : typeSpecList) {
            ids.add(spec.getSpecId());
        }
        //组装规格的值
        List<Goods_spec> specList = goodsSpecService.query(Cnd.where("id", "in", ids).asc("location"));
        goodsSpecService.fetchLinks(specList, "specValues", Cnd.orderBy().asc("location"));

        //组装商品各类关联表数据
        goodsService.fetchLinks(goods, "^(goodsClass|goodsBrand|imageList)$", Cnd.orderBy().asc("location"));
        goodsService.fetchLinks(goods, "^(goodsType|storeGoodsClassList|tagList)$");

        //获取货品列表
        List<Goods_product> productsList = goodsProductService.query(Cnd.where("goodsId", "=", goods.getId()));
        //组装货品各类关联表数据
        goodsProductService.fetchLinks(productsList, "^(areaList|memberTypeList)$");

        //获取省市列表
        List<Shop_area> provinceCityList = new ArrayList<>();
        Map<String, String> provinceCityMap = new HashMap<>();
        List<Shop_area> shopAreaList = shopAreaService.query("^(code|name|path|hasChildren|location)$",Cnd.where("disabled", "=", false).asc("location"));
        if (!Lang.isEmpty(shopAreaList)) {
            provinceCityList.addAll(shopAreaList.parallelStream().filter(p->p.getPath().length() <= 12).collect(Collectors.toList()));
            //组装省市Map
            provinceCityMap.putAll(shopAreaList.parallelStream().collect(Collectors.toMap(Shop_area::getCode, area->area.getName())));
        }

        req.setAttribute("type", type);
        req.setAttribute("needCheck", shopConfigService.checkOnGoodsPublish());
        req.setAttribute("provinceCityList", provinceCityList);
        req.setAttribute("provinceCityMap", provinceCityMap);
        //片区列表
        req.setAttribute("areaList", shopAreaManagementService.getShopAreaManagementList());
        //片区map<code,name>
        req.setAttribute("areaMap", shopAreaManagementService.getShopAreaManagementMap());
        req.setAttribute("specList", specList);
        req.setAttribute("typePropList", typePropList);
        req.setAttribute("typeParamgList", typeParamgList);
        req.setAttribute("typeList", goodsTypeService.query(Cnd.NEW()));
        req.setAttribute("brandList", goodsBrandService.listByGoodsTypeId(goods.getTypeId()));
        req.setAttribute("tagList", goodsTagService.query(Cnd.NEW()));
        req.setAttribute("unitDictList", sysDictService.getSubListByCode("goods_unit"));
        req.setAttribute("memberTypeList", memberTypeService.query(Cnd.NEW()));
        req.setAttribute("productsList", productsList);
        req.setAttribute("obj", goods);
        return "pages/platform/self/goods/list/detail";
    }

    /**
     * 审核记录页面
     *
     * @param id
     * @param req
     */
    @RequestMapping("/{id}/checklog")
    @RequiresPermissions("self.goods.list")
    public String getChecklog(@PathVariable("id")String id, HttpServletRequest req) {
        req.setAttribute("checklogList", goodsMainChecklogService.query(Cnd.where("goodsId", "=", id).desc("opAt"), "^(storeMain|applicant|checker)$"));
        return "pages/platform/self/goods/list/checklog";
    }

    /**
     * 取授权分类树数据
     *
     * @param pid
     * @return
     */
    @RequestMapping(value = {"/classes", "/classes/{pid}"})
    @SJson("{locked:'disabled|seoTitle|seoKeywords|seoDescription|goodsType|opBy|opAt|delFlag',ignoreNull:false}")
    @RequiresPermissions("self.goods.list")
    public Object classes(@PathVariable(value = "pid", required = false) String pid) {
        return storeMainService.getSubGoodsClasses("2017060000000001", pid);//StringUtil.getStoreUid()
    }

    /**
     * 获取商品分类及分类的商品类型
     *
     * @param id 商品分类ID
     * @return 商品分类JSON
     */
    @RequestMapping("/class/{id}")
    @SJson("full")
    @RequiresAuthentication
    public Object getClass(@PathVariable("id") String id) {
        return Result.success("", goodsClassService.fetchLinks(goodsClassService.fetch(id), "goodsType"));
    }

    /**
     * 获取商品类型信息
     *
     * @param id 类型ID
     * @return 类型JSON
     */
    @RequestMapping("/type/{id}")
    @SJson("full")
    @RequiresPermissions("self.goods.list")
    public Object getType(@PathVariable("id") String id) {
        return Result.success("", goodsTypeService.fetch(id));
    }

    /**
     * 获取商品类型的扩展属性
     *
     * @param id 类型ID
     * @return 扩展属性列表JSON
     */
    @RequestMapping("/type/{id}/props")
    @SJson("full")
    @RequiresAuthentication
    public Object getProps(@PathVariable("id")String id) {
        List<Goods_type_props> list = goodsTypePropsService.query(Cnd.where("typeId", "=", id).asc("location"));
        goodsTypePropsService.fetchLinks(list, "propsValues", Cnd.orderBy().asc("location"));
        return Result.success("", list);
    }

    /**
     * 获取商品类型的详细参数
     *
     * @param id 类型ID
     * @return 详细参数列表
     */
    @RequestMapping("/type/{id}/params")
    @SJson("full")
    @RequiresAuthentication
    public Object getParam(@PathVariable("id")String id) {
        List<Goods_type_paramg> list = goodsTypeParamgService.query(Cnd.where("typeId", "=", id).asc("location"));
        goodsTypeParamgService.fetchLinks(list, "params", Cnd.orderBy().asc("location"));
        return Result.success("", list);
    }

    /**
     * 通过商品类型获取品牌
     *
     * @param id 类型ID
     * @return 品牌列表JSON
     */
    @RequestMapping("/type/{id}/brands")
    @SJson("full")
    @RequiresAuthentication
    public Object getBrand(@PathVariable("id")String id) {
        List<Goods_type_brand> list = goodsTypeBrandService.query(Cnd.where("typeId", "=", id).asc("location"));
        goodsTypeBrandService.fetchLinks(list, "brand", Cnd.orderBy().asc("location"));
        return Result.success("", list);
    }

    @RequestMapping("/class/tree")
    @SJson("{locked:'typeId|seoDescription|goodsType|opBy|opAt|delFlag',ignoreNull:false}")
    @RequiresAuthentication
    public Object classTree() {
        //取授权分类树
        Store_main store = storeMainService.fetchLinks(storeMainService.fetch("2017060000000001"), "goodsClasses", Cnd.where("delFlag", "=", false));
        if (!Lang.isEmpty(store)) {
            return store.getGoodsClasses();
        }
        return null;
    }

    /**
     * 获取销售区域
     *
     * @param pcode 父级编码
     * @return
     */
    @RequestMapping(value = {"/areas", "/areas/{pcode}"})
    @SJson
    @RequiresAuthentication
    public Object getAreas(@PathVariable(required = false) String pcode) {

        return shopAreaService.getAreaNodeList(pcode);
    }

    /**
     * 获取商品统计数据
     *
     * @return
     */
    @RequestMapping("statistic")
    @SJson
    @RequiresPermissions("self.goods.list")
    public Object getStatistic() {
        return Result.success("", goodsService.getStatisticMap(StringUtil.getStoreUid()));
    }

}
