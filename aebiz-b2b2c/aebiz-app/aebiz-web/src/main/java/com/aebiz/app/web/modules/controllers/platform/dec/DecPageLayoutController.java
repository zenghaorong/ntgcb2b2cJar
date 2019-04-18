package com.aebiz.app.web.modules.controllers.platform.dec;

import com.aebiz.app.dec.commons.utils.DecorateCacheConstant;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.commons.utils.StringUtil;
import com.aebiz.app.dec.modules.models.Dec_page__layout;
import com.aebiz.app.dec.modules.services.DecPageLayoutService;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/platform/dec/page/layout")
public class DecPageLayoutController {
    private static final Log log = Logs.get();
    @Autowired
	private DecPageLayoutService decPageLayoutService;

    @RequestMapping("")
    @RequiresPermissions("dec.page.layout")
	public String index() {
		return "pages/platform/dec/page/layout/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("dec.page.layout")
    public Object data(DataTable dataTable) {
		Cnd cnd = Cnd.NEW();
    	return decPageLayoutService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions("dec.page.layout")
    public String add() {
    	return "pages/platform/dec/page/layout/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Dec_page__layout")
    @RequiresPermissions("dec.page.layout.add")
    public Object addDo(Dec_page__layout decPageLayout, HttpServletRequest req) {
		try {
            String layoutHtml = decPageLayout.getLayoutContent();
            String layoutId = decPageLayout.getLayoutId();
            if(layoutHtml != null && layoutHtml.length() > 0 && layoutId != null && layoutId.length() > 0){
                //保存页面布局的内容到redis
                decPageLayoutService.saveLayoutHtmlResource(layoutId, layoutHtml.getBytes());
            }
            String resourceKey = DecorateCacheConstant.LAYOUT_HTML_REDIS_KEY + layoutId;
            decPageLayout.setResourceKey(resourceKey);
			decPageLayoutService.insert(decPageLayout);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("dec.page.layout")
    public String edit(@PathVariable String id,HttpServletRequest req) {
        Dec_page__layout layout=decPageLayoutService.fetch(id);
        String redisLayoutHtml=decPageLayoutService.getRedisResourceByKey(layout.getResourceKey());
        layout.setLayoutContent(redisLayoutHtml);
		req.setAttribute("obj",layout);
		return "pages/platform/dec/page/layout/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Dec_page__layout")
    @RequiresPermissions("dec.page.layout.edit")
    public Object editDo(Dec_page__layout decPageLayout, HttpServletRequest req) {
		try {
            decPageLayout.setOpBy(StringUtil.getUid());
			decPageLayout.setOpAt((int) (System.currentTimeMillis() / 1000));
			decPageLayoutService.updateIgnoreNull(decPageLayout);
            String layoutHtml = decPageLayout.getLayoutContent();
            String layoutId = decPageLayout.getLayoutId();
            if(layoutHtml != null && layoutHtml.length() > 0 && layoutId != null && layoutId.length() > 0){
                //保存页面布局的内容更新到redis
                decPageLayoutService.saveLayoutHtmlResource(layoutId, layoutHtml.getBytes());
            }
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Dec_page__layout")
    @RequiresPermissions("dec.page.layout.del")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
				decPageLayoutService.delete(ids);
    			req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
			}else{
				decPageLayoutService.delete(id);
    			req.setAttribute("id", id);
			}
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("dec.page.layout")
	public String detail(@PathVariable String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
            req.setAttribute("obj", decPageLayoutService.fetch(id));
		}else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/dec/page/layout/detail";
    }
    /**
     * 获取所有页面布局
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = { "/getPageLayouts" }, method = { RequestMethod.GET })
    public String getPageLayouts(@RequestParam String versionType, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        String contextPath = request.getContextPath();
        List<Dec_page__layout> pageLayoutList = decPageLayoutService.getPageLayouts(contextPath, versionType);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        try {
            response.getWriter().print(Json.toJson(pageLayoutList));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
