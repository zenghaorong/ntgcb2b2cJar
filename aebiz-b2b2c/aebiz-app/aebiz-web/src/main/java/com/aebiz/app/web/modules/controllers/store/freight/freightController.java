package com.aebiz.app.web.modules.controllers.store.freight;

import com.aebiz.app.goods.modules.services.GoodsClassService;
import com.aebiz.app.shop.modules.services.ShopAreaManagementService;
import com.aebiz.app.shop.modules.services.ShopExpressService;
import com.aebiz.app.store.modules.commons.vo.StoreFreightProduct;
import com.aebiz.app.store.modules.models.Store_freight;
import com.aebiz.app.store.modules.models.Store_main;
import com.aebiz.app.store.modules.services.StoreFreightRulesService;
import com.aebiz.app.store.modules.services.StoreFreightService;
import com.aebiz.app.store.modules.services.StoreMainService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by ThinkPad on 2017/8/16.
 */
@Controller
@RequestMapping("/store/freight/template")
public class freightController {
    private static final Log log = Logs.get();
    @Autowired
    private StoreFreightService storeFreightService;
    @Autowired
    private ShopAreaManagementService shopAreaManagementService;
    @Autowired
    private ShopExpressService shopExpressService;
    @Autowired
    private GoodsClassService goodsClassService;
    @Autowired
    private StoreFreightRulesService storeFreightRulesService;
    @Autowired
    private StoreMainService storeMainService;

    @RequestMapping("")
    @RequiresPermissions("store.setting.freight.template")
    public String index(HttpServletRequest req) {
        String storeId=StringUtil.getStoreId();
        List template = storeFreightService.query(Cnd.where("storeId", "=", storeId).and("enabled","=",true));
        //初始化一条数据 保证模板不为空
        if (0 == template.size()) {
            Store_freight storeFreight = new Store_freight();
            storeFreight.setTemplateName("默认模板");
            storeFreight.setStoreId(storeId);
            storeFreight.setAddUnit(1);
            storeFreight.setAddCost(500);
            storeFreight.setDefaultUnit(1);
            storeFreight.setDefautAffix(1000);
            storeFreight.setEnabled(true);
            storeFreight.setBillingType("1");
            storeFreight.setOpAt((int) (System.currentTimeMillis() / 1000));
            storeFreight.setOpBy(StringUtil.getUid());
            template.add(storeFreightService.insert(storeFreight));
        }
        req.setAttribute("template", storeFreightService.query(Cnd.where("storeId", "=", storeId)));
        req.setAttribute("templateRules", storeFreightRulesService.query(Cnd.NEW()));
        return "pages/store/freight/index";
    }

    @RequestMapping("/add")
    @RequiresPermissions("store.setting.freight.template")
    public String add(HttpServletRequest req) {
        req.setAttribute("areaList", shopAreaManagementService.query(Cnd.NEW()));
        req.setAttribute("goodsList", goodsClassService.query(Cnd.NEW()));
        req.setAttribute("logisticsList", shopExpressService.query(Cnd.NEW()));
        return "pages/store/freight/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Store_freight")
    @RequiresPermissions("store.setting.freight.template.add")
    public Object addDo(Store_freight storeFreight, @RequestParam(required = false) String temps,
                        HttpServletRequest req) {
        try {
            storeFreightService.addStoreTemplateFreight(storeFreight, temps);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("store.setting.freight.template")
    public String edit(@PathVariable String id, HttpServletRequest req) {
        req.setAttribute("areaList", shopAreaManagementService.query(Cnd.NEW()));
        req.setAttribute("goodsList", goodsClassService.query(Cnd.NEW()));
        req.setAttribute("logisticsList", shopExpressService.query(Cnd.NEW()));
        req.setAttribute("obj", storeFreightService.fetch(id));
        req.setAttribute("objRules", storeFreightRulesService.query(Cnd.where("templateId", "=", id)));
        return "pages/store/freight/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Store_freight")
    @RequiresPermissions("store.setting.freight.template.edit")
    public Object editDo(Store_freight storeFreight, @RequestParam(required = false) String temps, HttpServletRequest req) {
        try {
            String storeId=StringUtil.getStoreId();
            if(!storeFreight.isEnabled()){
                storeFreightService.fetch(Cnd.where("storeId","=",storeId).and("enabled","=",true));
            }
            storeFreight.setOpBy(StringUtil.getUid());
            storeFreight.setOpAt((int) (System.currentTimeMillis() / 1000));
            storeFreight.setStoreId(storeId);
            if (storeFreight.isEnabled()) {
                storeFreightService.update(Chain.make("enabled", false), Cnd.where("id", "<>", storeFreight.getId()));
            }else{
                if (storeFreightService.fetch(storeFreight.getId()).isEnabled()==true){
                    return Result.error("默认模板不能操作禁用 ");
                }
            }
            storeFreightService.editStoreFreight(storeFreight, temps);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/delete/{id}")
    @SJson
    @SLog(description = "Store_freight")
    @RequiresPermissions("store.setting.freight.template.delete")
    public Object delete(@PathVariable(required = false) String id, HttpServletRequest req) {
        try {
            Store_freight storeFreight = storeFreightService.fetch(id);
            if (storeFreight.isEnabled()) {
                return Result.error("默认模板不能删除");
            }
            storeFreightService.del(id);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/deleteRules/{id}")
    @SJson
    @SLog(description = "Store_freight")
    @RequiresPermissions("store.setting.freight.template.delete")
    public Object deleteRules(@PathVariable(required = false) String id, HttpServletRequest req) {
        try {
            storeFreightService.delRules(id);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/enabled/{id}")
    @SLog(description = "Store_freight")
    @RequiresPermissions("store.setting.freight.template.edit")
    public Object enabled(@PathVariable(required = false) String id, HttpServletRequest req) {
        storeFreightService.enabled(id);
        return "redirect:/store/freight ";
    }

    @RequestMapping("/class/tree")
    @SJson("{locked:'path|typeId|disabled|seoTitle|seoKeywords|seoDescription|goodsType|opBy|opAt|delFlag',ignoreNull:false}")
    @RequiresAuthentication
    public Object classTree() {
        //取授权分类树
        String storeId=StringUtil.getStoreId();
        Store_main store = storeMainService.fetchLinks(storeMainService.fetch(storeId), "goodsClasses", Cnd.where("disabled", "=", false).and("delFlag", "=", false));
        if (!Lang.isEmpty(store)) {
            return store.getGoodsClasses();
        }
        return null;
    }

    @RequestMapping("/freight")
    @SLog(description = "Store_freight")
    @ResponseBody
//    @RequiresPermissions("platform.store.freight")
    public Integer freight(@RequestParam(required = false) String productsLists, @RequestParam("provinceCode") String provinceCode, @RequestParam(required = false) String memberId, @RequestParam(required = false) String storeId, @RequestParam(required = false) String logisticsCode) {
        List<StoreFreightProduct> productsList = Json.fromJsonAsList(StoreFreightProduct.class, productsLists);
        return storeFreightService.storeCountFreight(productsList, provinceCode, logisticsCode, memberId, storeId);
    }
}
