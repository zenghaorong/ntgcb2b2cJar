package com.aebiz.app.web.modules.controllers.platform.store;

import com.aebiz.app.store.modules.commons.utils.StoreUtil;
import com.aebiz.app.store.modules.models.Store_apply_class;
import com.aebiz.app.store.modules.services.StoreApplyClassService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.DateUtil;
import com.aebiz.commons.utils.SpringUtil;
import com.aebiz.commons.utils.StringUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
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
@RequestMapping("/platform/store/apply/class")
public class StoreApplyClassController {
    private static final Log log = Logs.get();
    @Autowired
	private StoreApplyClassService storeApplyClassService;

    @RequestMapping("")
    @RequiresPermissions("store.apply.class")
	public String index(HttpServletRequest req) {
        //待审核数量
        String sql = "SELECT count(1) FROM store_apply_class a ,store_main b" +
        " WHERE NOT EXISTS(SELECT 1 FROM store_apply_class WHERE storeId = a.storeId AND applyAt > a.applyAt)" +
                " and a.storeId = b.id and a.delFlag = 0 and  a.status = @status ";
        Sql countSql = Sqls.create(sql);
        int waitVerifyNum = storeApplyClassService.count(countSql.setParam("status",0));
        //审核通过的数量
        int verifyOkNum = storeApplyClassService.count(countSql.setParam("status",1));
        //审核不通过的数量
        int verifyNoNum = storeApplyClassService.count(countSql.setParam("status",2));
        req.setAttribute("waitVerifyNum",waitVerifyNum);
        req.setAttribute("verifyOkNum",verifyOkNum);
        req.setAttribute("verifyNoNum",verifyNoNum);
        return "pages/platform/store/apply/class/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("store.apply.class")
    public Object data(@RequestParam(value = "startAt",required = false)String startAt,@RequestParam(value = "endAt",required = false)String endAt, @RequestParam(value = "storeName",required = false)String storeName, @RequestParam("status") int status,@RequestParam("length") int length, @RequestParam("start") int start, @RequestParam("draw") int draw, ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
        StringBuilder sqlBuild  = new StringBuilder("SELECT a.*,b.storeName FROM store_apply_class a ,store_main b" +
                " WHERE NOT EXISTS(SELECT 1 FROM store_apply_class WHERE storeId = a.storeId AND applyAt > a.applyAt)" +
                " and a.storeId = b.id and a.delFlag = 0 and a.status = @status ");
        if(Strings.isNotBlank(storeName)){
            sqlBuild.append(" and b.storeName=@storeName ");
        }
        if(Strings.isNotBlank(startAt)){
            sqlBuild.append(" and a.applyAt >= @startAt ");
        }
        if(Strings.isNotBlank(endAt)){
            sqlBuild.append(" and a.applyAt < @endAt ");
        }

        String s = sqlBuild.toString();
        if (order != null && order.size() > 0) {
            for (DataTableOrder o : order) {
                DataTableColumn col = columns.get(o.getColumn());
                s += " order by a." + Sqls.escapeSqlFieldValue(col.getData()).toString() + " " + o.getDir();
            }
        }
        Sql countSql =  Sqls.create(sqlBuild.toString());
        Sql querySql = Sqls.create(s);
        countSql.setParam("status",status);
        querySql.setParam("status",status);
        if(Strings.isNotBlank(storeName)){
            countSql.setParam("storeName",storeName);
            querySql.setParam("storeName",storeName);
        }
        if(Strings.isNotBlank(startAt)){
            int startTime = DateUtil.getTime(startAt);
            countSql.setParam("startAt",startTime);
            querySql.setParam("startAt",startTime);
        }
        if(Strings.isNotBlank(endAt)){
            int endTime = DateUtil.getTime(endAt);
            countSql.setParam("endAt",endTime);
            querySql.setParam("endAt",endTime);
        }
    	return storeApplyClassService.data(length, start, draw, countSql, querySql);
    }

    @RequestMapping("/add")
    @RequiresPermissions("store.apply.class")
    public String add() {
    	return "pages/platform/store/apply/class/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Store_apply_class")
    @RequiresPermissions("store.apply.class.add")
    public Object addDo(Store_apply_class storeApplyClass, HttpServletRequest req) {
		try {
			storeApplyClassService.insert(storeApplyClass);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("store.apply.class")
    public String edit(@PathVariable String id,HttpServletRequest req) {
        Store_apply_class storeApplyClass =  storeApplyClassService.fetch(Cnd.where("delFlag","=",false).and("id","=",id));
        if(storeApplyClass == null){
            return "redirect:404";
        }
        storeApplyClassService.fetchLinks(storeApplyClass,"storeMain");
        req.setAttribute("obj", storeApplyClass);
		return "pages/platform/store/apply/class/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Store_apply_class")
    @RequiresPermissions("store.apply.class.edit")
    public Object editDo(Store_apply_class storeApplyClass, HttpServletRequest req) {
		try {
            storeApplyClass.setOpBy(StringUtil.getUid());
			storeApplyClass.setOpAt((int) (System.currentTimeMillis() / 1000));
			storeApplyClassService.updateIgnoreNull(storeApplyClass);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Store_apply_class")
    @RequiresPermissions("store.apply.class.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
				storeApplyClassService.delete(ids);
    			req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
			}else{
				storeApplyClassService.delete(id);
    			req.setAttribute("id", id);
			}
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("store.apply.class")
	public String detail(@PathVariable String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
		    Store_apply_class storeApplyClass =  storeApplyClassService.fetch(id);
            storeApplyClassService.fetchLinks(storeApplyClass,"storeMain");
            req.setAttribute("obj", storeApplyClass);
            req.setAttribute("storeStatus", SpringUtil.getBean(StoreUtil.class));
		}else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/store/apply/class/detail";
    }

}
