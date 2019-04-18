package com.aebiz.app.web.modules.controllers.store.goods;

import com.aebiz.app.goods.modules.models.*;
import com.aebiz.app.goods.modules.services.*;
import com.aebiz.app.member.modules.services.MemberTypeService;
import com.aebiz.app.shop.modules.models.Shop_area;
import com.aebiz.app.shop.modules.services.ShopAreaManagementService;
import com.aebiz.app.shop.modules.services.ShopAreaService;
import com.aebiz.app.shop.modules.services.ShopConfigService;
import com.aebiz.app.store.modules.models.Store_goodsclass;
import com.aebiz.app.store.modules.services.StoreGoodsclassService;
import com.aebiz.app.store.modules.services.StoreMainService;
import com.aebiz.app.sys.modules.services.SysDictService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 商品发布
 */
@Controller
@RequestMapping("/store/goods/publish")
public class StoreGoodsPublishController {

    @Autowired
	private GoodsService goodsService;
    @Autowired
    private GoodsClassService goodsClassService;
    @Autowired
    private StoreGoodsclassService storeGoodsclassService;
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
    private StoreMainService storeMainService;
    @Autowired
    private ShopConfigService shopConfigService;
    @Autowired
    private ShopAreaManagementService shopAreaManagementService;

    /**
     * 商品添加页面
     *
     * @param req
     */
    @RequestMapping("/add")
    @RequiresPermissions("store.goods.publish")
    public String add(HttpServletRequest req) {

        req.setAttribute("needCheck", shopConfigService.checkOnGoodsPublish());
        req.setAttribute("typeList", goodsTypeService.query(Cnd.NEW()));
        req.setAttribute("tagList", goodsTagService.query(Cnd.NEW()));
        req.setAttribute("unitDictList", sysDictService.getSubListByCode("goods_unit"));
        req.setAttribute("memberTypeList", memberTypeService.query(Cnd.NEW()));
        req.setAttribute("areaList", shopAreaManagementService.query(Cnd.NEW()));

        return "pages/store/goods/publish/add";
    }

    /**
     * 新增商品
     * @param goods 商品
     * @param saleAt 上架时间
     * @param offAt 下架时间
     * @param storeGoodsclass 前台分类
     * @param tags 标签
     * @param images 相册图
     * @param products 货品
     * @return
     */
    @RequestMapping("addDo")
    @SJson("full")
    @RequiresPermissions("store.goods.publish.add")
    @SLog(description = "新增商品")
    public Object addDo(Goods_main goods,
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
            goods.setStoreId(StringUtil.getStoreId());
            //校验SKU
            if (goodsService.isDuplicatedSku(goods)) {
                return Result.error("goods.main.tip.skuhasbeenused");
            }
            goodsService.add(goods);
            return Result.success("globals.result.success", goods.getId());
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    /**
     * 开启规格页面
     *
     * @param id
     * @param sku
     * @param req
     */
    @RequestMapping({"type/{id}/spec", "type/{id}/spec/{sku}"})
    @RequiresPermissions("store.goods.publish")
    public String spec(@PathVariable("id")String id, @PathVariable(value="sku", required = false)String sku, HttpServletRequest req) {
        List<Goods_type_spec> typeSpecList = goodsTypeSpecService.query(Cnd.where("typeId", "=", id).asc("location"), "^(spec|specValList)$");
        goodsSpecService.fetchLinks(typeSpecList, "specValues", Cnd.where("typeId", "=", id).orderBy().asc("location"));
        List<Goods_spec> specList = new ArrayList<>();
        for (Goods_type_spec ts : typeSpecList) {
            Goods_spec spec = ts.getSpec();
            List<String> valueIds = new ArrayList<>();
            for (Goods_type_spec_values tsv: ts.getSpecValList()) {
                valueIds.add(tsv.getSpecValueId());
            }
            goodsSpecService.fetchLinks(spec, "specValues", Cnd.where("id", "in", valueIds).asc("location"));
            specList.add(spec);
        }
        specList.sort(Comparator.comparing(Goods_spec::getLocation));
        req.setAttribute("specList", specList);
        req.setAttribute("areaList", shopAreaManagementService.query(Cnd.NEW()));
        req.setAttribute("memberTypeList", memberTypeService.query(Cnd.NEW()));
        return "pages/store/goods/publish/spec";
    }

    @RequestMapping(value = {"/classes", "/classes/{pid}"})
    @SJson("{locked:'disabled|seoTitle|seoKeywords|seoDescription|goodsType|opBy|opAt|delFlag',ignoreNull:false}")
    @RequiresPermissions("store.goods.publish")
    public Object classes(@PathVariable(value = "pid", required = false) String pid) {
        return storeMainService.getSubGoodsClasses(StringUtil.getStoreId(), pid);
    }

    /**
     * 获取商品分类及分类的商品类型
     *
     * @param id 商品分类ID
     * @return 商品分类JSON
     */
    @RequestMapping("/class/{id}")
    @SJson("full")
    @RequiresPermissions("store.goods.publish")
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
    @RequiresPermissions("store.goods.publish")
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
    @RequiresPermissions("store.goods.publish")
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
    @RequiresPermissions("store.goods.publish")
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

    /**
     * 获取前台分类
     *
     * @return
     */
    @RequestMapping("/frontclass/tree")
    @SJson("{locked:'path|opBy|opAt|delFlag',ignoreNull:false}")
    @RequiresPermissions("store.goods.publish")
    public Object frontClassTree() {
        return storeGoodsclassService.query(Cnd.where("storeId", "=", StringUtil.getStoreId()));
    }

    /**
     * 获取销售区域树
     *
     * @return
     */
    @RequestMapping("/area/tree")
    @SJson
    @RequiresPermissions("store.goods.publish")
    public Object getAreas() {
        List<Shop_area> shopAreaList = shopAreaService.query(Cnd.where("disabled", "=", false));
        if (!Lang.isEmpty(shopAreaList)) {
            //只需取到市级（长度为12）
            return shopAreaList.parallelStream().filter(x->x.getPath().length() <= 12).map(x->{
                Map<String, Object> map = NutMap.NEW();
                map.put("id", x.getCode());
                map.put("text", x.getName());
                map.put("parent", Strings.isBlank(x.getParentId()) ? "#" : shopAreaList.parallelStream().filter(p->p.getId().equals(x.getParentId())).findFirst().get().getCode());
                map.put("hasChildren", x.isHasChildren() && x.getPath().length() < 12);
                return map;
            }).collect(Collectors.toList());
        }
        return null;
    }
}
