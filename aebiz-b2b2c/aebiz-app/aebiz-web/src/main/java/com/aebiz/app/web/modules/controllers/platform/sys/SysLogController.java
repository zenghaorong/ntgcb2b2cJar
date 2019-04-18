package com.aebiz.app.web.modules.controllers.platform.sys;

import com.aebiz.app.sys.modules.models.Sys_log;
import com.aebiz.app.sys.modules.services.SysLogService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.OffsetPager;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.DateUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.util.Daos;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by gaoen on 2017-2-3.
 */
@Controller
@RequestMapping("/platform/sys/log")
public class SysLogController {

    @Autowired
    private SysLogService sysLogService;

    @RequestMapping("")
    @RequiresPermissions("sys.manager.log")
    public String index(HttpServletRequest req) {
        req.setAttribute("today", DateUtil.getDate());
        req.setAttribute("month", DateUtil.format(new Date(), "yyyy-MM"));
        return "pages/platform/sys/log/index";
    }

    @RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("sys.manager.log")
    public Object data(@RequestParam(required = false, value = "beginDate") String beginDate, @RequestParam(required = false, value = "endDate") String endDate, @RequestParam(required = false, value = "username") String username, HttpServletRequest req, DataTable dataTable) {
        Cnd cnd = Cnd.NEW();
        if (Strings.isNotBlank(beginDate)) {
            cnd.and("opAt", ">=", DateUtil.getTime(beginDate + " 00:00:00"));
        }
        if (Strings.isNotBlank(endDate)) {
            cnd.and("opAt", "<=", DateUtil.getTime(endDate + " 23:59:59"));
        }
        if (Strings.isNotBlank(username)) {
            cnd.and("username", "like", "%" + username + "%");
        }
        String tabelName = DateUtil.format(new Date(), "yyyyMM");
        Dao logDao = Daos.ext(sysLogService.dao(), tabelName);
        Pager pager = new OffsetPager(dataTable.getStart(), dataTable.getLength());
        NutMap result = new NutMap();
        if (dataTable.getOrders() != null && dataTable.getOrders().size() > 0) {
            for (DataTableOrder order : dataTable.getOrders()) {
                DataTableColumn col = dataTable.getColumns().get(order.getColumn());
                cnd.orderBy(Sqls.escapeSqlFieldValue(col.getData()).toString(), order.getDir());
            }
        }
        result.put("recordsFiltered", logDao.count(Sys_log.class, cnd));
        result.put("data", logDao.query(Sys_log.class, cnd, pager));
        result.put("draw", dataTable.getDraw());
        result.put("recordsTotal", dataTable.getLength());
        return result;
    }

    @RequestMapping("/delete")
    @SJson
    @RequiresPermissions("sys.manager.log.delete")
    @SLog(description = "清空日志")
    public Object delete(HttpServletRequest req) {
        try {
            sysLogService.dropLogTable();
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }

}
