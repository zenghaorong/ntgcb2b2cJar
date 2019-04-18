package com.aebiz.app.web.modules.controllers.platform.member;

import com.aebiz.app.member.modules.models.Member_level;
import com.aebiz.app.member.modules.services.MemberLevelService;
import com.aebiz.app.member.modules.services.MemberTypeService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.json.Json;
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
import java.util.List;

@Controller
@RequestMapping("/platform/member/level")
public class MemberLevelController {

    private static final Log log = Logs.get();

    @Autowired
    private MemberLevelService memberLevelService;

    @Autowired
    private MemberTypeService memberTypeService;

    @RequestMapping("")
    @RequiresPermissions("platform.member.level")
    public String index(HttpServletRequest req) {
        req.setAttribute("typeList", memberTypeService.query(Cnd.NEW()));
        return "pages/platform/member/level/index";
    }

    @RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("platform.member.level")
    public Object data(@RequestParam(value = "name", required = false) String name,
                       @RequestParam(value = "typeId", required = false) String typeId,
                       DataTable dataTable) {
        Cnd cnd = Cnd.NEW();
        if (Strings.isNotBlank(name)) {
            cnd.and("name", "like", Sqls.escapeSqlFieldValue("%" + Strings.trim(name) + "%"));
        }
        if (!Strings.isBlank(typeId)) {
            cnd = Cnd.where("typeId", "=", typeId);
        }
        return memberLevelService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, "memberType");
    }

    @RequestMapping("/add")
    @RequiresPermissions("platform.member.level")
    public String add(HttpServletRequest req) {
        req.setAttribute("typeList", memberTypeService.query(Cnd.NEW()));
        return "pages/platform/member/level/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Member_level")
    @RequiresPermissions("platform.member.level.add")
    public Object addDo(Member_level memberLevel, @RequestParam("uploadInfo")String uploadInfo) {
        try {
            if(Strings.isNotBlank(uploadInfo)) {
                List<String> uploadImageList = Json.fromJsonAsList(String.class, uploadInfo);
                memberLevel.setImgurl(uploadImageList.get(0));
            }
            memberLevelService.insert(memberLevel);
            memberLevelService.updateLevelDefaultByTypeId(memberLevel);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("platform.member.level")
    public String edit(@PathVariable String id, HttpServletRequest req) {
        req.setAttribute("level", memberLevelService.fetch(id));
        req.setAttribute("typeList", memberTypeService.query(Cnd.NEW()));
        return "pages/platform/member/level/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Member_level")
    @RequiresPermissions("platform.member.level.edit")
    public Object editDo(Member_level memberLevel, @RequestParam("uploadInfo")String uploadInfo) {
        try {
            if(Strings.isNotBlank(uploadInfo)) {
                List<String> uploadImageList = Json.fromJsonAsList(String.class, uploadInfo);
                memberLevel.setImgurl(uploadImageList.get(0));
            }
            memberLevel.setOpBy(StringUtil.getUid());
            memberLevel.setOpAt((int) (System.currentTimeMillis() / 1000));
            memberLevelService.updateLevelDefaultByTypeId(memberLevel);
            memberLevelService.updateIgnoreNull(memberLevel);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Member_level")
    @RequiresPermissions("platform.member.level.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids", required = false) String ids, HttpServletRequest req) {
        try {
            if (!Strings.isBlank(ids)) {
                String[] idList = ids.split(",");
                memberLevelService.delete(idList);
                req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(idList));
            } else {
                memberLevelService.delete(id);
                req.setAttribute("id", id);
            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("platform.member.level")
    public String detail(@PathVariable String id, HttpServletRequest req) {
        if (!Strings.isBlank(id)) {
            req.setAttribute("obj", memberLevelService.fetchLinks(memberLevelService.fetch(id), "memberType"));
        } else {
            req.setAttribute("obj", null);
        }
        return "pages/platform/member/level/detail";
    }

    @RequestMapping("/getLevelListByTypeId")
    @SJson
    @SLog(description = "Member_level")
    @RequiresPermissions("platform.member.level")
    public Object getLevelListByTypeId(@RequestParam(value = "typeId",required = false) int typeId) {
        try {
            if (typeId <= 0){
                typeId = memberTypeService.query(Cnd.NEW()).get(0).getId();
            }
                return Result.success("globals.result.success",memberLevelService.query(Cnd.where("typeId","=",typeId)));

        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }
}
