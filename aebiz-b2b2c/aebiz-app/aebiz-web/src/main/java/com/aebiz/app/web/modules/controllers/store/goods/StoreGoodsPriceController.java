package com.aebiz.app.web.modules.controllers.store.goods;

import com.aebiz.app.goods.modules.models.*;
import com.aebiz.app.goods.modules.services.*;
import com.aebiz.app.member.modules.models.Member_level;
import com.aebiz.app.member.modules.models.Member_type;
import com.aebiz.app.member.modules.services.MemberLevelService;
import com.aebiz.app.member.modules.services.MemberTypeService;
import com.aebiz.app.shop.modules.models.Shop_area;
import com.aebiz.app.shop.modules.models.Shop_area_management;
import com.aebiz.app.shop.modules.services.ShopAreaManagementService;
import com.aebiz.app.shop.modules.services.ShopAreaService;
import com.aebiz.app.store.modules.services.StoreMainService;
import com.aebiz.app.sys.modules.services.SysDictService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 价格中心
 */
@Controller
@RequestMapping("/store/goods/price")
public class StoreGoodsPriceController {

    private static final Log log = Logs.get();

    @Autowired
    private GoodsPriceService goodsPriceService;
    @Autowired
    private GoodsPriceLevelService goodsPriceLevelService;
    @Autowired
    private GoodsProductService goodsProductService;
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
    private MemberTypeService memberTypeService;
    @Autowired
    private MemberLevelService memberLevelService;
    @Autowired
    private SysDictService sysDictService;
    @Autowired
    private ShopAreaService shopAreaService;
    @Autowired
    private GoodsProductAreaService goodsProductAreaService;
    @Autowired
    private StoreMainService storeMainService;
    @Autowired
    private ShopAreaManagementService shopAreaManagementService;

    @RequestMapping("")
    @RequiresPermissions("store.goods.price")
    public String index(HttpServletRequest req) {
        req.setAttribute("typeList", goodsTypeService.query(Cnd.NEW()));
        req.setAttribute("tagList", goodsTagService.query(Cnd.NEW()));
        req.setAttribute("unitDictList", sysDictService.getSubListByCode("goods_unit"));
        req.setAttribute("memberTypeList", memberTypeService.query(Cnd.NEW()));
        return "pages/store/goods/price/index";
    }

    /**
     * 货品列表数据
     *
     * @return
     */
    @RequestMapping("/products/data")
    @SJson("full")
    @RequiresPermissions("store.goods.price")
    public Object productsData(@ModelAttribute("goods") Goods_main goods, @ModelAttribute("product") Goods_product product, DataTable dataTable) {
        return goodsProductService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), goods, product, StringUtil.getStoreId());
    }

    @RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("store.goods.price")
    public Object data(@ModelAttribute("goods") Goods_main goods, @ModelAttribute("product") Goods_product product, DataTable dataTable) {
        Cnd cnd = Cnd.NEW();
        return goodsPriceService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping("/{productId}/data")
    @SJson("full")
    @RequiresPermissions("store.goods.price")
    public Object data(@PathVariable("productId")String productId, DataTable dataTable) {
        Cnd cnd = Cnd.where("productId", "=", Strings.sNull(productId));
        return goodsPriceService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping("/{productId}/strategyDo")
    @SJson
    @SLog(description = "按策略生成商品价格")
    @RequiresPermissions("store.goods.price.edit")
    public Object strategyDo(@PathVariable("productId")String productId, @RequestParam("strategy") String strategy, HttpServletRequest req) {
        try {
            List<Goods_price> priceList = Json.fromJsonAsList(Goods_price.class, strategy);
            goodsPriceService.updatePriceOnStrategy(priceList, productId, StringUtil.getStoreId());
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/add")
    @RequiresPermissions("store.goods.price")
    public String add() {
        return "pages/store/goods/price/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "保存价格")
    @RequiresPermissions("store.goods.price")
    public Object addDo(Goods_price goodsPrice, HttpServletRequest req) {
        try {
            goodsPriceService.insert(goodsPrice);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("store.goods.price")
    public String edit(@PathVariable String id, HttpServletRequest req) {
        req.setAttribute("obj", goodsProductService.fetchLinks(goodsProductService.fetch(id),"goodsMain"));
        return "pages/store/goods/price/edit";
    }

    /**
     * 设置价格页
     *
     * @param productId 货品ID
     * @param req
     * @return
     */
    @RequestMapping("/setting/{productId}")
    @RequiresPermissions("store.goods.price")
    public String setting(@PathVariable("productId") String productId, HttpServletRequest req) {
        Goods_product product = goodsProductService.fetch(productId);

        //会员等级
        List<Member_type> mtl = memberTypeService.query(Cnd.NEW());
        List<Member_level> mll = memberLevelService.query(Cnd.NEW());
        Map<String, List<Member_level>> memberMap = new HashMap<>();
        for (Member_type mt : mtl) {
            memberMap.put(String.valueOf(mt.getId()), mll.parallelStream().filter(ml->ml.getTypeId()==mt.getId()).collect(Collectors.toList()));
        }

        //价格列表
        List<Goods_price> priceList = goodsPriceService.query(Cnd.where("productId", "=", productId).and("delFlag", "=", false));
        for (Goods_price price : priceList) {
            goodsPriceService.fetchLinks(price, "priceLevelList", Cnd.where("delFlag", "=", false));
        }

        //组装省市Map
        Map<String, String> provinceCityMap = new HashMap<>();
        List<Shop_area> shopAreaList = shopAreaService.query(Cnd.where("disabled", "=", false));
        if (!Lang.isEmpty(shopAreaList)) {
            provinceCityMap.putAll(shopAreaList.parallelStream().collect(Collectors.toMap(Shop_area::getCode, area->area.getName())));
        }

        //获取片区列表
        List<Shop_area_management> areaList = shopAreaManagementService.query(Cnd.NEW());
        Map<String, String> areaMap = new HashMap<>();
        if (!Lang.isEmpty(areaList)) {
            areaMap.putAll(areaList.parallelStream().collect(Collectors.toMap(Shop_area_management::getCode, area->area.getName())));
        }

        //片区
        req.setAttribute("provinceCityMap", provinceCityMap);
        req.setAttribute("areaList", areaList);
        req.setAttribute("areaMap", areaMap);
        //销售对象
        req.setAttribute("saleToMemberType", memberTypeService.fetch(product.getSaleToMemberType()));
        req.setAttribute("memberTypeList", memberTypeService.query(Cnd.NEW()));
        req.setAttribute("saleToMemberTypeMap",mtl.parallelStream().collect(Collectors.toMap(Member_type::getId, mt->mt)));
        req.setAttribute("memberLevelMap",mll.parallelStream().collect(Collectors.toMap(Member_level::getId, ml->ml)));

        req.setAttribute("priceList", priceList);
        req.setAttribute("memberMap", memberMap);
        req.setAttribute("obj", goodsProductService.fetchLinks(product,"goodsMain"));
        return "pages/store/goods/price/setting";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "保存货品价格")
    @RequiresPermissions("store.goods.price.edit")
    public Object editDo(@RequestParam("prices") String prices, HttpServletRequest req) {
        try {
            List<Goods_price> priceList = Json.fromJsonAsList(Goods_price.class, prices);
            int date = (int) (System.currentTimeMillis() / 1000);
            for (Goods_price price : priceList) {
                price.setOpBy(StringUtil.getUid());
                price.setOpAt(date);
            }
            goodsPriceService.updates(priceList);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/enable/{id}")
    @SJson
    @RequiresPermissions("store.goods.price.edit")
    @SLog(description = "启用价格")
    public Object enable(@PathVariable String id, HttpServletRequest req) {
        try {
            goodsPriceService.update(Chain.make("disabled", false), Cnd.where("id", "=", Strings.sNull(id)));
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/disable/{id}")
    @SJson
    @RequiresPermissions("store.goods.price.edit")
    @SLog(description = "禁用价格")
    public Object disable(@PathVariable String id, HttpServletRequest req) {
        try {
            goodsPriceService.update(Chain.make("disabled", true), Cnd.where("id", "=", Strings.sNull(id)));
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/editLevelPriceDo")
    @SJson
    @SLog(description = "保存会员等级价格")
    @RequiresPermissions("store.goods.price.edit")
    public Object editLevelPriceDo(@RequestParam("memberLevelPrices") String memberLevelPrices, HttpServletRequest req) {
        try {
            List<Goods_price_level> list = Json.fromJsonAsList(Goods_price_level.class, memberLevelPrices);
            goodsPriceService.saveLevelPrice(list);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping(value = {"/{id}/delete", "/{id}/delete/{priceLevelId}"})
    @SJson
    @SLog(description = "删除商品价格")
    @RequiresPermissions("store.goods.price.edit")
    public Object delete(@PathVariable("id")String id, @PathVariable(value = "priceLevelId", required = false)String priceLevelId, HttpServletRequest req) {
        try {
            goodsPriceService.vDelete(id, priceLevelId, StringUtil.getStoreId());
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    /**
     * 取货品的销售区域树
     * @param productId 商品ID
     * @param req
     * @return 区域树JSON
     */
    @RequestMapping("/{productId}/area/tree")
    @SJson
    @RequiresPermissions("store.goods.price")
    public Object areas(@PathVariable("productId") String productId, HttpServletRequest req) {
        List<Map<String, Object>> nodeList = new ArrayList<>();
        List<Goods_product_area> maList = goodsProductAreaService.query(Cnd.where("productId", "=", productId));
        if (!Lang.isEmpty(maList)) {

            //将省、市放入map中去重
            Map<String, Shop_area> areaMap = new LinkedHashMap<>();
            for(Goods_product_area ma : maList){
                //从缓存中取区域
                Shop_area province = shopAreaService.getByCode(ma.getProvince());
                Shop_area city = shopAreaService.getByCode(ma.getCity());
                if (!Lang.isEmpty(province)) {
                    areaMap.put(province.getCode(), province);
                }
                if (!Lang.isEmpty(city)) {
                    areaMap.put(city.getCode(), city);
                }
            }

            //组装树节点
            List<Shop_area> shopAreaList = new ArrayList<>(areaMap.values());
            if (!Lang.isEmpty(shopAreaList)) {
                //只需取到市级（长度为12）
                nodeList = shopAreaList.parallelStream().map(x->{
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", x.getCode());
                    map.put("text", x.getName());
                    map.put("parent", Strings.isBlank(x.getParentId()) || x.getPath().length() == 8 ? "#" : shopAreaList.parallelStream().filter(p->p.getId().equals(x.getParentId())).findFirst().get().getCode());
                    map.put("hasChildren", x.isHasChildren() && x.getPath().length() == 8);
                    return map;
                }).collect(Collectors.toList());
            }
        }
        return nodeList;
    }


    @RequestMapping("/{sku}/price")
    @SJson
    @RequiresPermissions("store.goods.price")
    public Object price(@PathVariable("sku")String sku,
                        @RequestParam("areaCode")String areaCode,
                        @RequestParam("provinceCode")String provinceCode,
                        @RequestParam("cityCode")String cityCode,
                        @RequestParam("saleToClient")int saleToClient,
                        @RequestParam("saleToMember")int saleToMember,
                        @RequestParam("saleToMemberLevel")String saleToMemberLevel, HttpServletRequest req) {
        try {
            return Result.success("globals.result.success", goodsPriceService.getSalePrice(sku, areaCode, provinceCode, cityCode, saleToClient, saleToMember, saleToMemberLevel));
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }


}
