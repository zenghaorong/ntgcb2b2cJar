package com.aebiz.app.web.modules.controllers.platform.goods;

import com.aebiz.app.goods.modules.models.*;
import com.aebiz.app.goods.modules.services.*;
import com.aebiz.app.member.modules.services.MemberTypeService;
import com.aebiz.app.shop.modules.models.Shop_area;
import com.aebiz.app.shop.modules.models.Shop_area_management;
import com.aebiz.app.shop.modules.services.ShopAreaManagementService;
import com.aebiz.app.shop.modules.services.ShopAreaService;
import com.aebiz.app.shop.modules.services.ShopConfigService;
import com.aebiz.app.store.modules.services.StoreMainService;
import com.aebiz.app.sys.modules.services.SysDictService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 商品管理列表
 */
@Controller
@RequestMapping("/platform/goods/manager/goods")
public class GoodsManagerController {

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
    private ShopAreaManagementService shopAreaManagementService;
    @Autowired
    private ShopConfigService shopConfigService;

    @RequestMapping("")
    @RequiresPermissions("goods.manager.goods")
	public String index(Goods_main goods, @RequestParam(value = "tagId", required = false) String tagId, @RequestParam(value = "tmp_hasSpec", required = false) String hasSpec, HttpServletRequest req) {
        req.setAttribute("typeList", goodsTypeService.query(Cnd.NEW()));
        req.setAttribute("brandList", goodsBrandService.query(Cnd.NEW()));
        req.setAttribute("tagList", goodsTagService.query(Cnd.NEW()));
        req.setAttribute("tagId", tagId);
        req.setAttribute("hasSpec", hasSpec);
        req.setAttribute("obj", goods);
        return "pages/platform/goods/manager/index";
	}

    /**
     * 商品列表页
     *
     * @return
     */
    @RequestMapping("/data")
    @SJson("{locked:'pcNote|wapNote|tvNote|prop|param|spec',ignoreNull:false}")
    @RequiresPermissions("goods.manager.goods")
    public Object data(Goods_main goods, @RequestParam(value = "tagId", required = false) String tagId, @RequestParam(value = "tmp_hasSpec", required = false) String hasSpec, DataTable dataTable) {
        NutMap fieldMap = NutMap.NEW();
        if (Strings.isNotBlank(tagId)) {
            fieldMap.put("tagId", tagId);
        }
        if (Strings.isNotBlank(hasSpec)) {
            fieldMap.put("hasSpec", hasSpec);
        }
        return goodsService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), "^(tagList|goodsClass|goodsType|goodsBrand)$", goods, fieldMap);
    }

    /**
     *  审核页
     * @param id
     * @param req
     * @return
     */
    @RequestMapping("/check/{id}")
    @RequiresPermissions("goods.manager.goods")
    public String check(@PathVariable String id, HttpServletRequest req) {
        Goods_main goods = goodsService.fetch(id);

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
        return "pages/platform/goods/manager/check";
    }

    /**
     * 审核
     *
     * @return
     */
    @RequestMapping(value = {"/checkDo", "/checkDo/{id}"})
    @SJson
    @RequiresPermissions("goods.manager.goods.edit")
    @SLog(description = "商品审核")
    public Object checkDo(@PathVariable(required = false) String id, @RequestParam("ids") String[] ids, @RequestParam("checkStatus") Integer checkStatus, @RequestParam("reason") String reason) {
        try {
            if (Strings.isNotBlank(id)) {
                goodsService.check(id, checkStatus, reason);
            }
            if (!Lang.isEmpty(ids)) {
                goodsService.check(ids, checkStatus, reason);
            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    /**
     * 商品批量违规下架
     *
     * @param ids 商品IDS
     * @return
     */
    @RequestMapping("/markbreakrule")
    @SJson
    @RequiresPermissions("goods.manager.goods.edit")
    @SLog(description = "商品批量违规下架")
    public Object markBreakrule(@RequestParam("ids") String[] ids, @RequestParam("reason") String reason){
        try {
            goodsService.markBreakrule(ids, reason);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("goods.manager.goods")
    public String detail(@PathVariable String id, HttpServletRequest req) {

        Goods_main goods = goodsService.fetch(Cnd.where("id", "=", id));

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

        return "pages/platform/goods/manager/detail";
    }

    /**
     * 审核记录页面
     *
     * @param id
     * @param req
     */
    @RequestMapping("/{id}/checklog")
    @RequiresPermissions("goods.manager.goods")
    public String getChecklog(@PathVariable("id")String id, HttpServletRequest req) {
        req.setAttribute("checklogList", goodsMainChecklogService.query(Cnd.where("goodsId", "=", id).desc("opAt"), "^(storeMain|applicant|checker)$"));
        return "pages/platform/goods/manager/checklog";
    }

    /**
     * 取授权分类树数据
     *
     * @param pid
     * @return
     */
    @RequestMapping(value = {"/classes", "/classes/{pid}"})
    @SJson("{locked:'disabled|seoTitle|seoKeywords|seoDescription|goodsType|opBy|opAt|delFlag',ignoreNull:false}")
    @RequiresPermissions("goods.manager.goods")
    public Object classes(@PathVariable(value = "pid", required = false) String pid) {
        return goodsClassService.query(Cnd.where("parentId", "=", Strings.sBlank(pid)).asc("location").asc("path"));
    }

    /**
     * 获取商品分类及分类的商品类型
     *
     * @param id 商品分类ID
     * @return 商品分类JSON
     */
    @RequestMapping("/class/{id}")
    @SJson("full")
    @RequiresPermissions("goods.manager.goods")
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
    @RequiresPermissions("goods.manager.goods")
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
    @RequiresPermissions("goods.manager.goods")
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
    @RequiresPermissions("goods.manager.goods")
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
    @SJson("{locked:'path|typeId|disabled|seoTitle|seoKeywords|seoDescription|goodsType|opBy|opAt|delFlag',ignoreNull:false}")
    @RequiresPermissions("goods.manager.goods")
    public Object classTree() {
        return goodsClassService.query(Cnd.where("disabled", "=", false));
    }

    /**
     * 获取销售区域
     *
     * @param pcode 父级编码
     * @return
     */
    @RequestMapping(value = {"/areas", "/areas/{pcode}"})
    @SJson
    @RequiresPermissions("goods.manager.goods")
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
    @RequiresPermissions("goods.manager.goods")
    public Object getStatistic() {
        return Result.success("", goodsService.getStatisticMap(""));
    }

}
