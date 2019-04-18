package com.aebiz.app.web.modules.controllers.platform.member;


import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;

import com.aebiz.commons.utils.StringUtil;
import com.aebiz.app.member.modules.models.Member_type;
import com.aebiz.app.member.modules.services.MemberTypeService;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/platform/member/type")
public class MemberTypeController {

    private static final Log log = Logs.get();

    @Autowired
    private MemberTypeService memberTypeService;

    @RequestMapping("")
    @RequiresPermissions("platform.member.type")
    public String index() {
        return "pages/platform/member/type/index";
    }

    @RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("platform.member.type")
    public Object data(@RequestParam(value = "name", required = false) String name,
                       @RequestParam(value = "aliasName", required = false) String aliasName,
                       DataTable dataTable) {
        Cnd cnd = Cnd.NEW();
        if (Strings.isNotBlank(name)) {
            cnd.and("name", "like", Sqls.escapeSqlFieldValue("%" + Strings.trim(name) + "%"));
        }
        if (Strings.isNotBlank(aliasName)) {
            cnd.and("aliasName", "like", Sqls.escapeSqlFieldValue("%" + Strings.trim(aliasName) + "%"));
        }
        return memberTypeService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions("platform.member.type")
    public String add() {
        return "pages/platform/member/type/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Member_type")
    @RequiresPermissions("platform.member.type.add")
    public Object addDo(Member_type memberType, HttpServletRequest req) {
        try {
            memberTypeService.insert(memberType);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("platform.member.type")
    public String edit(@PathVariable String id, HttpServletRequest req) {
        req.setAttribute("obj", memberTypeService.fetch(Integer.parseInt(id)));
        return "pages/platform/member/type/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Member_type")
    @RequiresPermissions("platform.member.type.edit")
    public Object editDo(Member_type memberType, HttpServletRequest req) {
        try {
            memberType.setOpBy(StringUtil.getUid());
            memberType.setOpAt((int) (System.currentTimeMillis() / 1000));
            memberTypeService.updateIgnoreNull(memberType);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Member_type")
    @RequiresPermissions("platform.member.type.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids", required = false) String[] ids, HttpServletRequest req) {
        try {
            if (ids != null && ids.length > 0) {
                String[] str = ids[0].split(",");
                //转换为int数组
                for (int i = 0; i < str.length; i++) {
                    memberTypeService.delete(Integer.parseInt(str[i]));
                }
                req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
            } else {
                memberTypeService.delete(Integer.parseInt(id));
                req.setAttribute("id", id);
            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("platform.member.type")
    public String detail(@PathVariable String id, HttpServletRequest req) {
        if (!Strings.isBlank(id)) {
            req.setAttribute("obj", memberTypeService.fetch(Integer.parseInt(id)));
        } else {
            req.setAttribute("obj", null);
        }
        return "pages/platform/member/type/detail";
    }

}
