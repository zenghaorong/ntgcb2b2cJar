package com.aebiz.app.web.modules.controllers.platform.store;

import com.aebiz.app.store.modules.models.Store_apply_main;
import com.aebiz.app.store.modules.models.Store_config;
import com.aebiz.app.store.modules.models.Store_main;
import com.aebiz.app.store.modules.services.*;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Controller
@RequestMapping("/platform/store/apply/main")
public class StoreApplyMainController {
    private static final Log log = Logs.get();
    @Autowired
	private StoreApplyMainService storeApplyMainService;

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
    private StoreMainService storeMainService;

    @RequestMapping("")
    @RequiresPermissions("store.apply.main")
	public String index() {
		return "pages/platform/store/apply/main/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("store.apply.main")
    public Object data(@RequestParam("length") int length, @RequestParam("start") int start, @RequestParam("draw") int draw, ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
		Cnd cnd = Cnd.NEW();
    	return storeApplyMainService.data(length, start, draw, order, columns, cnd, "storeMain");
    }

    @RequestMapping("/class")
    @RequiresAuthentication
    @SJson
    public Object getClassName(@RequestParam("id") String id) {
        try {
            return Result.success("globals.result.success", storeClassService.fetch(id).getName());
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/add")
    @RequiresPermissions("store.apply.main")
    public String add() {
    	return "pages/platform/store/apply/main/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Store_apply_main")
    @RequiresPermissions("store.apply.main.add")
    public Object addDo(Store_apply_main storeApplyMain, HttpServletRequest req) {
		try {
			storeApplyMainService.insert(storeApplyMain);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    /**
     * 开店审核【修改】
     * @param id
     * @param req
     * @return
     */
    @RequestMapping("/edit/{id}")
    @RequiresPermissions("store.apply.main")
    public String edit(@PathVariable String id,HttpServletRequest req) {
        Store_apply_main storeApplyMain = storeApplyMainService.fetch(id);
        req.setAttribute("storeApplyMain", storeApplyMainService.fetch(id));

        Store_main storeMain = storeMainService.fetch(storeApplyMain.getStoreId());
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
        req.setAttribute("classOption", storeClassService.getClassOption());
        req.setAttribute("levelList", storeLevelService.query(Cnd.NEW()));
        req.setAttribute("typeList", storeTypeService.query(Cnd.NEW()));
		return "pages/platform/store/apply/main/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Store_apply_main")
    @RequiresPermissions("store.apply.main.edit")
    public Object editDo(Store_apply_main storeApplyMain, HttpServletRequest req) {
		try {
            storeApplyMain.setOpBy(StringUtil.getUid());
			storeApplyMain.setOpAt((int) (System.currentTimeMillis() / 1000));
			storeApplyMainService.updateIgnoreNull(storeApplyMain);
			//表示审核通过
			if(storeApplyMain.getStatus() == 1){
                Store_main storeMain = storeMainService.fetch(storeApplyMain.getId());
                //开启店铺
                storeMainService.update(Chain.make("disabled",false),Cnd.where("id","=",storeMain.getId()));
                //给店铺用户赋予店铺角色

			}
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Store_apply_main")
    @RequiresPermissions("store.apply.main.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
				storeApplyMainService.delete(ids);
    			req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
			}else{
				storeApplyMainService.delete(id);
    			req.setAttribute("id", id);
			}
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("store.apply.main")
	public String detail(@PathVariable String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
            req.setAttribute("obj", storeApplyMainService.fetch(id));
		}else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/store/apply/main/detail";
    }

}
