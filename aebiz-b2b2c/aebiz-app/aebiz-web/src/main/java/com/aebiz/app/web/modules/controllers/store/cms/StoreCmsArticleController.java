package com.aebiz.app.web.modules.controllers.store.cms;

import com.aebiz.app.cms.modules.models.Cms_article;
import com.aebiz.app.cms.modules.models.Cms_channel;
import com.aebiz.app.cms.modules.services.CmsArticleService;
import com.aebiz.app.cms.modules.services.CmsChannelService;
import com.aebiz.app.store.modules.models.Store_user;
import com.aebiz.app.sys.modules.models.Sys_user;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/store/cms/article")
public class StoreCmsArticleController {
	@Autowired
	private CmsArticleService cmsArticleService;

	@Autowired
	private CmsChannelService cmsChannelService;

	@RequestMapping("")
	@RequiresPermissions("store.cms.article")
	public String index() {
		return "pages/store/cms/article/index";
	}

	@RequestMapping("/data")
	@SJson("full")
	@RequiresPermissions("store.cms.article")
	public Object data(@RequestParam String channelId, @RequestParam String title,
			@RequestParam("length") int length, @RequestParam("start") int start, @RequestParam("draw") int draw,
			ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns) {
		Store_user user = (Store_user) SecurityUtils.getSubject().getPrincipal();
		Cnd cnd = Cnd.NEW();
		if (!Strings.isBlank(channelId) && !"0".equals(channelId)) {
			cnd.and("channelId", "like", "%" + channelId + "%");
		}
		if (!Strings.isBlank(title)) {
			cnd.and("title", "like", "%" + title + "%");
		}
		cnd.and("storeId", "=", user.getStoreId());
		return cmsArticleService.data(length, start, draw, order, columns, cnd, null);
	}

	@RequestMapping(value = { "/tree", "/tree/{pid}" })
	@SJson
	@RequiresPermissions("store.cms.article")
	public Object tree(@PathVariable(required = false) String pid, HttpServletRequest req) {
		Store_user user = (Store_user) SecurityUtils.getSubject().getPrincipal();
		List<Cms_channel> list = cmsChannelService
				.query(Cnd.where("storeId", "=", user.getStoreId()).and("parentId", "=", Strings.sBlank(pid)).asc("location").asc("path"));
		List<Map<String, Object>> tree = new ArrayList<>();
		if (Strings.isBlank(pid)) {
			Map<String, Object> obj = new HashMap<>();
			obj.put("id", "0");
			obj.put("text", "所有栏目");
			obj.put("children", false);
			tree.add(obj);
		}
		for (Cms_channel channel : list) {
			Map<String, Object> obj = new HashMap<>();
			obj.put("id", channel.getId());
			obj.put("text", channel.getName());
			obj.put("children", channel.isHasChildren());
			tree.add(obj);
		}
		return tree;
	}

	@RequestMapping({ "/add", "/add/{channelId}" })
	@RequiresPermissions("store.cms.article")
	public String add(@PathVariable(required = false) String channelId, HttpServletRequest req) {
		req.setAttribute("channel",
				channelId != null && !"0".equals(channelId) ? cmsChannelService.fetch(channelId) : null);
		Store_user user = (Store_user) SecurityUtils.getSubject().getPrincipal();
		req.setAttribute("nickname", user == null ? "" : user.getAccountInfo().getNickname());
		return "pages/store/cms/article/add";
	}

	@RequestMapping("/addDo")
	@SJson
	@SLog(description = "Cms_article")
	@RequiresPermissions("store.cms.article.add")
	public Object addDo(@RequestParam String at, Cms_article cmsArticle, HttpServletRequest req) {
		try {
			Store_user user = (Store_user) SecurityUtils.getSubject().getPrincipal();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int publishAt = (int) (sdf.parse(at).getTime() / 1000);
			cmsArticle.setPublishAt(publishAt);
			cmsArticle.setStoreId(user.getStoreId());
			cmsArticleService.insert(cmsArticle);
			cmsArticleService.clearCache();
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
	}

	@RequestMapping("/edit/{id}")
	@RequiresPermissions("store.cms.article")
	public String edit(@PathVariable String id, HttpServletRequest req) {
		 Cms_article article = cmsArticleService.fetch(id);
	        req.setAttribute("obj", article != null ? article : null);
		return "pages/store/cms/article/edit";
	}

	@RequestMapping("/editDo")
	@SJson
	@SLog(description = "Cms_article")
	@RequiresPermissions("store.cms.article.edit")
	public Object editDo(Cms_article cmsArticle, HttpServletRequest req) {
		try {
			cmsArticle.setOpBy(StringUtil.getUid());
			cmsArticle.setOpAt((int) (System.currentTimeMillis() / 1000));
			cmsArticleService.updateIgnoreNull(cmsArticle);
			cmsArticleService.clearCache();
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
					cmsArticleService.delete(ssids[i]);
				}
			} else {
				cmsArticleService.delete(id);
			}
			cmsArticleService.clearCache();
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
	}

    @RequestMapping("/enable/{id}")
    @SJson
    @RequiresPermissions("store.cms.article.edit")
    @SLog(description = "发布项目")
    public Object enable(@PathVariable String id, HttpServletRequest req) {
        try {
            req.setAttribute("title", cmsArticleService.fetch(id).getTitle());
            cmsArticleService.update(org.nutz.dao.Chain.make("disabled", false), Cnd.where("id", "=", id));
			cmsArticleService.clearCache();
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    }
    @RequestMapping("/disable/{id}")
    @SJson
    @RequiresPermissions("store.cms.article.edit")
    @SLog(description = "取消发布")
    public Object disable(@PathVariable String id, HttpServletRequest req) {
        try {
            req.setAttribute("title", cmsArticleService.fetch(id).getTitle());
            cmsArticleService.update(org.nutz.dao.Chain.make("disabled", true), Cnd.where("id", "=", id));
			cmsArticleService.clearCache();
            return Result.success("system.success");
        } catch (Exception e) {
            return Result.error("system.error");
        }
    } 	
}
