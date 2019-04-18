package com.aebiz.app.web.modules.controllers.platform.dec;

import com.aebiz.app.dec.modules.models.Dec_api_class;
import com.aebiz.app.dec.modules.models.Dec_api_config_params;
import com.aebiz.app.dec.modules.services.DecApiClassService;
import com.aebiz.app.dec.modules.services.DecApiConfigParamsService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.commons.utils.StringUtil;
import com.aebiz.app.dec.modules.models.Dec_api;
import com.aebiz.app.dec.modules.services.DecApiService;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/platform/dec/api")
public class DecApiController {
    private static final Log log = Logs.get();
    @Autowired
	private DecApiService decApiService;

    @Autowired
    private DecApiClassService decApiClassService;

    @Autowired
    private DecApiConfigParamsService decApiConfigParamsService;

    @RequestMapping("")
    @RequiresPermissions("dec.api")
	public String index() {
		return "pages/platform/dec/api/manager/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("dec.api")
    public Object data(@RequestParam("length") int length, @RequestParam("start") int start, @RequestParam("draw") int draw, ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
		Cnd cnd = Cnd.NEW();
    	return decApiService.data(length, start, draw, order, columns, cnd, null);
    }
    @RequestMapping("/add")
    @RequiresPermissions("dec.api")
    public String add(HttpServletRequest req) {
        req.setAttribute("list",decApiClassService.query(Cnd.NEW()));
    	return "pages/platform/dec/api/manager/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Dec_api")
    @RequiresPermissions("dec.api.add")
    public Object addDo(Dec_api decApi, HttpServletRequest req) {
		try {
            decApiService.insert(decApi);
            Dec_api_config_params decApiConfigParams=new Dec_api_config_params();
            // 获取接口参数总数
            String tNumber = req.getParameter("tNumber");
            if(tNumber !=null && tNumber !=""){
                int number = Integer.parseInt(tNumber);
                for (int i = 1; i <= number; i++) {
                    String paramName =req.getParameter("paramName"+i);
                    String whetherMust=req.getParameter("whetherMust"+i);
                    String paramDescribe=req.getParameter("paramDescribe"+i);
                    // 参数描述
                    if (paramName ==null || paramName =="") {
                        continue;
                    }
                    if (whetherMust ==null || whetherMust =="") {
                        continue;
                    }
                    if (paramDescribe ==null || paramDescribe =="") {
                        continue;
                    }
                    decApiConfigParams.setInterfaceUuid(decApi.getId());
                    decApiConfigParams.setParamDescribe(paramDescribe);
                    decApiConfigParams.setParamName(paramName);
                    decApiConfigParams.setWhetherMust(whetherMust);
                    decApiConfigParams.setOpAt((int)(System.currentTimeMillis() / 1000));
                    try {
                        decApiConfigParamsService.insert(decApiConfigParams);
                    }catch (Exception e){
                        return Result.error("globals.result.error");
                    }

                }

            }
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("dec.api")
    public String edit(@PathVariable String id,HttpServletRequest req,Cnd cnd) {
		req.setAttribute("obj", decApiService.fetch(id));
		List<Dec_api_config_params> list=(List<Dec_api_config_params>)decApiConfigParamsService.query(cnd.where("interfaceUuid","=",id));
        // 获取当前接口的参数总数
        int count=decApiConfigParamsService.count();
        req.setAttribute("count",count);
		req.setAttribute("list",list);
		return "pages/platform/dec/api/manager/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Dec_api")
    @RequiresPermissions("dec.api.edit")
    public Object editDo(Dec_api decApi, HttpServletRequest req) {
		try {
            decApiConfigParamsService.deleteModel(decApi.getId());
            Dec_api_config_params decApiConfigParams=new Dec_api_config_params();
            // 获取接口参数总数
            String tNumber = req.getParameter("tNumber");
            if(tNumber !=null && tNumber !=""){
                int number = Integer.parseInt(tNumber);
                for (int i = 1; i <= number; i++) {
                    String paramName =req.getParameter("paramName"+i);
                    String whetherMust=req.getParameter("whetherMust"+i);
                    String paramDescribe=req.getParameter("paramDescribe"+i);
                    // 参数描述
                    if (paramName ==null || paramName =="") {
                        continue;
                    }
                    if (whetherMust ==null || whetherMust =="") {
                        continue;
                    }
                    if (paramDescribe ==null || paramDescribe =="") {
                        continue;
                    }
                    decApiConfigParams.setInterfaceUuid(decApi.getId());
                    decApiConfigParams.setParamDescribe(paramDescribe);
                    decApiConfigParams.setParamName(paramName);
                    decApiConfigParams.setWhetherMust(whetherMust);
                    decApiConfigParamsService.insert(decApiConfigParams);
                }
            }
            decApi.setOpBy(StringUtil.getUid());
			decApi.setOpAt((int) (System.currentTimeMillis() / 1000));
			decApiService.updateIgnoreNull(decApi);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Dec_api")
    @RequiresPermissions("dec.api.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
				decApiService.delete(ids);
    			req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
			}else{
				decApiService.delete(id);
    			req.setAttribute("id", id);
			}
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("dec.api")
	public String detail(@PathVariable String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
            req.setAttribute("obj", decApiService.fetch(id));
		}else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/dec/api/manager/detail";
    }

    @RequestMapping("/goApiClass")
    @RequiresPermissions("dec.apiClass")
    public String goApiClass() {
        return "pages/platform/dec/api/class/apiClassIndex";
    }

    @RequestMapping("/dataApiClass")
    @SJson("full")
    @RequiresPermissions("dec.apiClass")
    public Object dataApiClass(@RequestParam("length") int length, @RequestParam("start") int start, @RequestParam("draw") int draw, ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
        Cnd cnd = Cnd.NEW();
        return decApiClassService.data(length, start, draw, order, columns, cnd, null);
    }

    @RequestMapping("/addApiClass")
    @RequiresPermissions("dec.apiClass")
    public String addComponentClass() {
        return "pages/platform/dec/api/class/apiClassAdd";
    }

    @RequestMapping("/addApiClassDo")
    @SJson
    @SLog(description = "Dec_api_class")
    @RequiresPermissions("dec.apiClass")
    public Object addComponentClassDo(Dec_api_class decApiClass, HttpServletRequest req) {
        try {
            decApiClassService.insert(decApiClass);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/editApiClass/{id}")
    @RequiresPermissions("dec.apiClass")
    public String editComponentClass(@PathVariable String id,HttpServletRequest req) {
        req.setAttribute("obj", decApiClassService.fetch(id));
        return "pages/platform/dec/api/class/apiClassEdit";
    }

    @RequestMapping("/editApiClassDo")
    @SJson
    @SLog(description = "Dec_Api_class")
    @RequiresPermissions("dec.apiClass")
    public Object editComponentClassDo(Dec_api_class decApiClass, HttpServletRequest req) {
        try {
            decApiClass.setOpBy(StringUtil.getUid());
            decApiClass.setOpAt((int) (System.currentTimeMillis() / 1000));
            decApiClassService.updateIgnoreNull(decApiClass);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping(value = {"/deleteApiClass/{id}", "/deleteApiClass"})
    @SJson
    @SLog(description = "Dec_api_class")
    @RequiresPermissions("dec.apiClass")
    public Object deleteComponentClass(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
        try {
            if(ids!=null&&ids.length>0){
                decApiClassService.delete(ids);
                req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
            }else{
                decApiClassService.delete(id);
                req.setAttribute("id", id);
            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }
}
