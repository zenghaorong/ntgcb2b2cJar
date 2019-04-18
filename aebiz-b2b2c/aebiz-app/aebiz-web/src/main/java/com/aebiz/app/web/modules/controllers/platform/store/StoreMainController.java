package com.aebiz.app.web.modules.controllers.platform.store;

import com.aebiz.app.acc.modules.models.Account_info;
import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.acc.modules.services.AccountInfoService;
import com.aebiz.app.acc.modules.services.AccountUserService;
import com.aebiz.app.goods.modules.models.Goods_class;
import com.aebiz.app.goods.modules.services.GoodsClassService;
import com.aebiz.app.store.modules.commons.vo.Store;
import com.aebiz.app.store.modules.models.*;
import com.aebiz.app.store.modules.services.*;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.OffsetPager;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/platform/store/manager/main")
public class StoreMainController {

    @Autowired
    private StoreMainService storeMainService;

    @Autowired
    private StoreConfigService storeConfigService;

    @Autowired
    private StoreClassService storeClassService;

    @Autowired
    private StoreTypeService storeTypeService;

    @Autowired
    private StoreUserService storeUserService;

    @Autowired
    private StoreLevelService storeLevelService;

    @Autowired
    private StoreCompanyService storeCompanyService;

    @Autowired
    private AccountUserService accountUserService;

    @Autowired
    private GoodsClassService goodsClassService;

    @RequestMapping("")
    @RequiresAuthentication
    public String index(HttpServletRequest request) {
        request.setAttribute("levelList", storeLevelService.query(Cnd.NEW()));
        request.setAttribute("typeList", storeTypeService.query(Cnd.where("disabled","=",false)));
        return "pages/platform/store/manager/main/index";
    }

    @RequestMapping("/data")
    @SJson("full")
    public Object data(@RequestParam(value = "storeName",required = false) String storeName,
                       @RequestParam(value = "typeId",required = false) String typeId,
                       @RequestParam(value = "levelId",required = false) String levelId,
                       @RequestParam(value = "disabled",required = false) String disabled,
                       DataTable dataTable) {
        Cnd cnd = Cnd.NEW();
        if (Strings.isNotBlank(storeName)) {
            cnd.and("main.storeName", "like", Sqls.escapeSqlFieldValue("%" + Strings.trim(storeName) + "%"))
                    .or("user.loginname", "like", Sqls.escapeSqlFieldValue("%" + Strings.trim(storeName) + "%"));
        }
        if (Strings.isNotBlank(typeId)) {
            cnd.and("main.typeId", "=", typeId);
        }
        if (Strings.isNotBlank(levelId)) {
            cnd.and("main.levelId", "=", levelId);
        }
        if (Strings.isNotBlank(disabled)) {
            cnd.and("main.disabled","=","true".equals(disabled) ? true : false);
        }

        /*设置排序*/
        List<DataTableOrder> orders = dataTable.getOrders();
        List<DataTableColumn> columns = dataTable.getColumns();
        if (orders != null && orders.size() > 0) {
            for (int i = 0; i < orders.size(); i++) {
                DataTableOrder order = orders.get(i);
                int col = order.getColumn();
                String data = columns.get(col).getData();
                String dir = order.getDir();
                cnd.orderBy(data, dir);
            }
        }
        Pager pager = new OffsetPager(dataTable.getStart(), dataTable.getLength());  // 设置分页
        StringBuilder sb = new StringBuilder("select main.id,main.storeName storename,user.loginname,main.levelId,main.typeId,class.name classname,level.name levelname,type.name typename,main.logo,main.storeProvince storeprovince,main.storeCity storecity,main.disabled " +
                "from store_main main left join store_class as class on main.classId = class.id left join store_level as level on main.levelId = level.id " +
                "left join store_type as type on main.typeId = type.id left join account_user as user on main.id = user.accountId $condition ");
        //列表分页数据
        Sql sql = Sqls.queryRecord(sb.toString());
        sql.setCondition(cnd);
        sql.setPager(pager);
        storeMainService.dao().execute(sql);
        List<Store> storeList = sql.getList(Store.class);

        //列表分页显示数据
        Sql sql2 = Sqls.queryRecord(sb.toString());
        sql2.setCondition(cnd);
        storeMainService.dao().execute(sql2);

        NutMap obj = new NutMap();
        obj.put("recordsFiltered", sql2.getList(Store.class).size());
        obj.put("data", storeList);
        obj.put("draw", dataTable.getDraw());
        obj.put("recordsTotal", dataTable.getLength());
        return obj;
    }

    @RequestMapping("/check")
    @RequiresPermissions("store.manager.main")
    public String check() {
        return "pages/platform/store/manager/main/check";
    }

    @RequestMapping("/checkUser")
    @SJson
    @SLog(description = "Store_main")
    public Object checkUser(@RequestParam("account") String account, HttpServletRequest req) {
        try {
            String accountId = "";
            Account_user accountUser = accountUserService.getAccountByLoginname(account);
            if (accountUser != null) {
                accountId =  accountUser.getAccountId();
                Store_user user = storeUserService.getUser(accountId);
                if (user != null) {
                    return Result.error(1,"该帐号存在相应的店铺！");
                }
            }
            return Result.success("globals.result.success",accountId);
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/add")
    public String add(HttpServletRequest req) {
        Store_config config = storeConfigService.fetch("system");
        if (config == null) {
            Store_config storeConfig = new Store_config();
            storeConfig.setId("system");
            config = storeConfigService.insert(storeConfig);
        }
        req.setAttribute("obj", config);
        req.setAttribute("classOption", storeClassService.getClassOption());
        req.setAttribute("levelList", storeLevelService.query(Cnd.NEW()));
        req.setAttribute("typeList", storeTypeService.query(Cnd.where("disabled","=",false)));
        return "pages/platform/store/manager/main/add";
    }

    @RequestMapping("/tree")
    @SJson
    @RequiresAuthentication
    public Object tree(@RequestParam(value = "pid", required = false) String pid, @RequestParam(value = "storeId", required = false) String storeId) {
        List<Goods_class> list = goodsClassService.query(Cnd.where("parentId", "=", Strings.sBlank(pid)).asc("location").asc("path"));
        String str = "select classId from store_goods_class where storeId='"+storeId+"'";
        Sql sql = Sqls.create(str);
        List<Record> records = storeMainService.list(sql);
        List<String> ids = new ArrayList<String>();
        if (!records.isEmpty()){
            for (Record record : records) {
                ids.add(record.getString("classid"));
            }
        }
        List<Map<String, Object>> tree = new ArrayList<>();
        Map<String, Object> state = new HashMap<>();
        state.put("selected", true);
        for (Goods_class menu : list) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("id", menu.getId());
            obj.put("text", menu.getName());
            if (ids.contains(menu.getId())) {
                obj.put("state",state);
            }
            obj.put("children", menu.isHasChildren());
            tree.add(obj);
        }
        return tree;
    }

    @RequestMapping("/storeClassTree")
    @SJson
    @RequiresAuthentication
    public Object storeClassTree() {
        return storeClassService.query(Cnd.where("delFlag","=",false));
    }

    @InitBinder("storeMain")
    public void initBinderStore_main(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("storeMain.");
    }

    @InitBinder("accountInfo")
    public void initBinderAccount_info(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("accountInfo.");
    }

    @InitBinder("accountUser")
    public void initBinderAccount_user(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("accountUser.");
    }

    @InitBinder("storeCompany")
    public void initBinderStore_company(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("storeCompany.");
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Store_main")
    @RequiresPermissions("store.manager.main.add")
    public Object addDo(@ModelAttribute("storeMain") Store_main storeMain,
                        @ModelAttribute("accountInfo")Account_info accountInfo,
                        @ModelAttribute("accountUser")Account_user accountUser,
                        @ModelAttribute("storeCompany")Store_company storeCompany,
                        @RequestParam("ids") String ids, HttpServletRequest req) {
        try {
            storeMainService.addDo(storeMain,accountInfo,accountUser,storeCompany,ids);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/edit/{id}")
    @RequiresAuthentication
    public String edit(@PathVariable String id,HttpServletRequest req) {
        Store_main storeMain = storeMainService.fetch(id);
        storeMainService.fetchLinks(storeMain,"goodsClasses");

        req.setAttribute("storeMain",storeMain );
        req.setAttribute("storeUser", storeUserService.fetch(Cnd.where("storeId", "=", storeMain.getId())));
        req.setAttribute("storeCompany", storeCompanyService.fetch(Cnd.where("storeId", "=", storeMain.getId())));
        Store_config config = storeConfigService.fetch("system");
        if (config == null) {
            Store_config storeConfig = new Store_config();
            storeConfig.setId("system");
            config = storeConfigService.insert(storeConfig);
        }
        req.setAttribute("obj", config);
        req.setAttribute("storeClass", storeClassService.fetch(Cnd.where("delFlag","=",false).and("id","=",storeMain.getClassId())));
        req.setAttribute("levelList", storeLevelService.query(Cnd.NEW()));
        req.setAttribute("typeList", storeTypeService.query(Cnd.where("disabled","=",false)));
        return "pages/platform/store/manager/main/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Store_main")
    @RequiresPermissions("store.manager.main.edit")
    public Object editDo(@ModelAttribute("storeMain") Store_main storeMain,@ModelAttribute("storeCompany")Store_company storeCompany,
                         @RequestParam("ids") String ids, HttpServletRequest req) {
        try {
            storeMainService.editDo(storeMain,storeCompany,ids);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Store_main")
    @RequiresPermissions("store.manager.main.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
        try {
            if(ids!=null&&ids.length>0){
                storeMainService.delete(ids);
                req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
            }else{
                storeMainService.delete(id);
                req.setAttribute("id", id);
            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    public String detail(@PathVariable String id, HttpServletRequest req) {
        if (!Strings.isBlank(id)) {
            req.setAttribute("obj", storeMainService.fetch(id));
        }else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/store/manager/main/detail";
    }

    @RequestMapping("/resetPwd/{id}")
    @SJson
    @RequiresPermissions("store.manager.main.edit")
    @SLog(description = "重置店铺密码")
    public Object resetPwd(@PathVariable(required = false) String id, HttpServletRequest req) {
        try {
            Store_user storeUser = storeUserService.fetch(Cnd.where("storeId", "=", id));
            storeUserService.fetchLinks(storeUser,"accountInfo");
            RandomNumberGenerator rng = new SecureRandomNumberGenerator();
            String salt = rng.nextBytes().toBase64();
            String hashedPasswordBase64 = new Sha256Hash("ET922", salt, 1024).toBase64();
            accountUserService.update(Chain.make("salt", salt).add("password", hashedPasswordBase64), Cnd.where("accountId", "=", storeUser.getAccountId()));
            req.setAttribute("loginname", storeUser.getAccountInfo().getNickname());
            return Result.success("globals.result.success", "ET922");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/enable/{id}")
    @SJson
    @RequiresPermissions("store.manager.main.edit")
    @SLog(description = "启用店铺")
    public Object enable(@PathVariable(required = false) String id, HttpServletRequest req) {
        try {
            storeMainService.update(Chain.make("disabled", false), Cnd.where("id", "=", id));
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/disable/{id}")
    @SJson
    @RequiresPermissions("store.manager.main.edit")
    @SLog(description = "禁用店铺")
    public Object disable(@PathVariable(required = false) String id, HttpServletRequest req) {
        try {
            storeMainService.update(Chain.make("disabled", true), Cnd.where("id", "=", id));
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

}
