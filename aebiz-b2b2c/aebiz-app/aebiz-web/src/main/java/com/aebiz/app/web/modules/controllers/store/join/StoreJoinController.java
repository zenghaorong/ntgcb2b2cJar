package com.aebiz.app.web.modules.controllers.store.join;

import com.aebiz.app.goods.modules.models.Goods_class;
import com.aebiz.app.goods.modules.services.GoodsClassService;
import com.aebiz.app.store.modules.models.Store_company;
import com.aebiz.app.store.modules.models.Store_main;
import com.aebiz.app.store.modules.models.Store_user;
import com.aebiz.app.store.modules.services.*;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wizzer on 2017/1/18.
 */
@Controller
@RequestMapping("/store/join")
public class StoreJoinController {

    @Autowired
    private StoreMainService storeMainService;

    @Autowired
    private StoreClassService storeClassService;

    @Autowired
    private StoreTypeService storeTypeService;

    @Autowired
    private StoreUserService storeUserService;

    @Autowired
    private StoreLevelService storeLevelService;

    @Autowired
    private GoodsClassService goodsClassService;

    @RequestMapping("/join")
    public String join(HttpServletRequest req) {
        String accountId = StringUtil.getMemberUid();
        if(Strings.isBlank(accountId)){
            return  "redirect:/member/login";
        }
        //判断该会员是否有店铺
        Store_user storeUser = storeUserService.fetch(Cnd.where("delFlag","=",false).and("accountId","=",accountId));
        if(storeUser != null){
            Store_main storeMain = storeMainService.fetch(storeUser.getStoreId());
            if(!storeMain.getDelFlag() && !storeMain.isDisabled()){
                //存在开启的店铺
                return  "redirect:/store/login";
            }
        }
        req.setAttribute("classOption", storeClassService.getClassOption());
        req.setAttribute("levelList", storeLevelService.query(Cnd.NEW()));
        req.setAttribute("typeList", storeTypeService.query(Cnd.NEW()));
        return "pages/store/join/join";
    }

    @RequestMapping("/tree")
    @SJson
    @RequiresAuthentication
    public Object tree(@RequestParam(value = "pid", required = false) String pid) {
        List<Goods_class> list = goodsClassService.query(Cnd.where("parentId", "=", Strings.sBlank(pid)).asc("location").asc("path"));
        List<Map<String, Object>> tree = new ArrayList<>();
        for (Goods_class menu : list) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("id", menu.getId());
            obj.put("text", menu.getName());
            obj.put("children", menu.isHasChildren());
            tree.add(obj);
        }
        return tree;
    }

    @InitBinder("storeMain")
    public void initBinderStore_main(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("storeMain.");
    }

    @InitBinder("storeCompany")
    public void initBinderStore_company(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("storeCompany.");
    }

    @RequestMapping("/addDo")
    @SJson
    public Object addDo(@ModelAttribute("storeMain") Store_main storeMain, @ModelAttribute("storeCompany")Store_company storeCompany,
                        @RequestParam("ids") String ids, HttpServletRequest req) {
        try {
            storeMainService.join(storeMain,storeCompany,ids);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }
}
