package com.aebiz.app.web.modules.controllers.store.setting;

import com.aebiz.app.store.modules.models.*;
import com.aebiz.app.store.modules.services.*;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * Created by wlp on 2017/4/28.
 */
@Controller
@RequestMapping("/store/setting/shop")
public class StoreSettingController {

    @Autowired
    private StoreApplyClassService storeApplyClassService;

    @Autowired
    private StoreApplyLevelService storeApplyLevelService;

    @Autowired
    private StoreLevelService storeLevelService;

    @Autowired
    private StoreTypeService storeTypeService;

    @RequestMapping("")
    @RequiresPermissions("store.setting.config.shop")
    public String index(HttpServletRequest req) {
        req.setAttribute("levelList", storeLevelService.query(Cnd.NEW()));
        req.setAttribute("typeList", storeTypeService.query(Cnd.NEW()));
        return "pages/store/setting/index";
    }

    @RequestMapping("/typeData")
    @SJson("full")
    @RequiresPermissions("store.setting.config.shop")
    public Object typeData(@RequestParam("length") int length, @RequestParam("start") int start, @RequestParam("draw") int draw, ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
        Store_user user = (Store_user) SecurityUtils.getSubject().getPrincipal();
        Cnd cnd = Cnd.NEW();
        cnd.and("storeId","=",user.getStoreId());
        return storeApplyClassService.data(length, start, draw, order, columns, cnd, "");
    }

    @RequestMapping("/levelData")
    @SJson("full")
    @RequiresPermissions("store.setting.config.shop")
    public Object levelData(@RequestParam("length") int length, @RequestParam("start") int start, @RequestParam("draw") int draw, ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
        Store_user user = (Store_user) SecurityUtils.getSubject().getPrincipal();
        Cnd cnd = Cnd.NEW();
        cnd.and("storeId","=",user.getStoreId());
        return storeApplyLevelService.data(length, start, draw, order, columns, cnd, "");
    }

    @RequestMapping("/editLevel")
    @SJson
    @SLog(description = "店铺等级申请")
    @RequiresPermissions("store.setting.config.shop")
    public Object editLevel(@RequestParam("levelId") String levelId) {
        try {
            if (Strings.isNotBlank(levelId)) {
                Store_user user = (Store_user) SecurityUtils.getSubject().getPrincipal();
                String storeId = user.getStoreId();
                Store_apply_level level = storeApplyLevelService.fetch(Cnd.where("storeId","=",storeId).and("status","=",0));

                if (level != null) {
                    return Result.error("店铺已经发起申请，请稍后操作。");

                }else if (user.getStoreMain().getLevelId().equals(levelId)) {
                    return Result.error("该等级与原有等级相同，请重新选择。");

                } else {
                    Store_apply_level storeApplyLevel =  new Store_apply_level();
                    storeApplyLevel.setStatus(0);
                    storeApplyLevel.setStoreId(storeId);
                    Store_level storeLevel = storeLevelService.fetch(levelId);
                    storeApplyLevel.setApplyNote("商户发起 “"+storeLevel.getName()+"” 店铺等级申请！");
                    storeApplyLevel.setCheckNote("发起申请！");
                    storeApplyLevel.setApplyAt((int) (System.currentTimeMillis() / 1000));
                    storeApplyLevel.setOpBy(StringUtil.getUid());
                    storeApplyLevelService.insert(storeApplyLevel);
                }
            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/editType")
    @SJson
    @SLog(description = "店铺类型申请")
    @RequiresPermissions("store.setting.config.shop")
    public Object editType(@RequestParam("typeId") String typeId) {
        try {
            if (Strings.isNotBlank(typeId)) {
                Store_user user = (Store_user) SecurityUtils.getSubject().getPrincipal();
                String storeId = user.getStoreId();
                Store_apply_class apply_class = storeApplyClassService.fetch(Cnd.where("storeId","=",storeId).and("status","=",0));

                if (apply_class != null) {
                    return Result.error("店铺已经发起申请，请稍后操作。");

                }else if (user.getStoreMain().getTypeId().equals(typeId)) {
                    return Result.error("该类型与原有类型相同，请重新选择。");

                } else {
                    Store_apply_class storeApplyClass =  new Store_apply_class();
                    storeApplyClass.setStatus(0);
                    storeApplyClass.setStoreId(storeId);
                    Store_type storeType = storeTypeService.fetch(typeId);
                    storeApplyClass.setApplyNote("商户发起 “"+storeType.getName()+"” 店铺类型申请！");
                    storeApplyClass.setCheckNote("发起申请！");
                    storeApplyClass.setApplyAt((int) (System.currentTimeMillis() / 1000));
                    storeApplyClass.setOpBy(StringUtil.getUid());
                    storeApplyClassService.insert(storeApplyClass);
                }
            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }
}
