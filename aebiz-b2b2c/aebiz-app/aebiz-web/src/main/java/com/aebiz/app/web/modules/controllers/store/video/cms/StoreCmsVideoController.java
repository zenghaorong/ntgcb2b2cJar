package com.aebiz.app.web.modules.controllers.store.video.cms;


import com.aebiz.app.cms.modules.models.Cms_video;
import com.aebiz.app.cms.modules.services.CmsVideoService;
import com.aebiz.app.store.modules.models.Store_user;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;


@Controller
@RequestMapping("/store/video/cms")
public class StoreCmsVideoController {


	@Autowired
	private CmsVideoService cmsVideoService;

	@RequestMapping("")
	@RequiresPermissions("store.cms.video")
	public String index() {
		return "pages/store/video/cms/index";
	}

	@RequestMapping("/data")
	@SJson("full")
	@RequiresPermissions("store.cms.video")
	public Object data(@RequestParam String delFlag, @RequestParam String videoTitle,
			@RequestParam("length") int length, @RequestParam("start") int start, @RequestParam("draw") int draw,
			ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
		Store_user user = (Store_user) SecurityUtils.getSubject().getPrincipal();
		Cnd cnd = Cnd.NEW();
		if (!Strings.isBlank(delFlag) && !"0".equals(delFlag)) {
			cnd.and("delFlag", "=", delFlag );
		}
		if (!Strings.isBlank(videoTitle)) {
			cnd.and("videoTitle", "like", "%" + videoTitle + "%");
		}
		cnd.and("storeId", "=", user.getStoreId());
		return cmsVideoService.data(length, start, draw, order, columns, cnd, null);
	}


	@RequestMapping({ "/add"})
	@RequiresPermissions("store.cms.video.add")
	public String add(HttpServletRequest req) {
		return "pages/store/video/cms/add";
	}

	@RequestMapping("/addDo")
	@SJson
	@RequiresPermissions("store.cms.video.add")
	public Object addDo(Cms_video cms_video) {
		try {
			Store_user user = (Store_user) SecurityUtils.getSubject().getPrincipal();
			cms_video.setStoreId(user.getStoreId());
			cmsVideoService.insert(cms_video);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
	}

	@RequestMapping("/edit/{id}")
	@RequiresPermissions("store.cms.video")
	public String edit(@PathVariable String id, HttpServletRequest req) {
		    Cms_video cms_video = cmsVideoService.fetch(id);
	        req.setAttribute("obj", cms_video != null ? cms_video : null);
		return "pages/store/video/cms/edit";
	}

	@RequestMapping("/editDo")
	@SJson
	@SLog(description = "Cms_article")
	@RequiresPermissions("store.cms.video")
	public Object editDo(Cms_video cms_video) {
		try {
			if(cms_video.getDelFlag() == null){
				cms_video.setDelFlag(true);
			}
			cms_video.setOpBy(StringUtil.getUid());
			cms_video.setOpAt((int) (System.currentTimeMillis() / 1000));
			cmsVideoService.update(cms_video);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
	}

	@RequestMapping(value = { "/delete/{id}", "/delete" })
	@SJson
	@SLog(description = "Cms_article")
	@RequiresPermissions("store.cms.article.delete")
	public Object delete(@PathVariable(required = false) String id,
			@RequestParam(value = "ids", required = false) String[] ids, HttpServletRequest req) {
		try {
			if (ids != null && ids.length > 0) {
				// ids转化为字符串存进数组 作用：区分单删还是多选
				String[] ssids = ids[0].split(",");
				for (int i = 0; i < ssids.length; i++) {
					cmsVideoService.delete(ssids[i]);
				}
			} else {
				cmsVideoService.delete(id);
			}
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
	}

}
