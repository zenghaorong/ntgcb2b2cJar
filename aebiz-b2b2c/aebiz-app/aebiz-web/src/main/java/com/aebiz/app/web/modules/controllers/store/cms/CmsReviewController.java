package com.aebiz.app.web.modules.controllers.store.cms;

import com.aebiz.app.store.modules.models.Store_user;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.commons.utils.StringUtil;
import com.aebiz.app.cms.modules.models.Cms_review;
import com.aebiz.app.cms.modules.services.CmsReviewService;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.SecurityUtils;
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

@Controller
@RequestMapping("/store/cms/review")
public class CmsReviewController {
    private static final Log log = Logs.get();
    @Autowired
	private CmsReviewService cmsReviewService;

    @RequestMapping("")
    @RequiresPermissions("store.cms.review")
	public String index(String cmsId, HttpServletRequest req) {
        req.setAttribute("cmsId",cmsId);
		return "pages/store/cms/review/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("store.cms.review")
    public Object data(DataTable dataTable,String cmsId,String reviewId,String type,String cmsType,String content) {
		Cnd cnd = Cnd.NEW();
        Store_user user = (Store_user) SecurityUtils.getSubject().getPrincipal();
        cnd.and("storeId", "=", user.getStoreId());
        cnd.and("delFlag", "=", 0);
        if(!Strings.isBlank(cmsId)){
            cnd.and("cmsId", "=",cmsId);
        }
        if(!Strings.isBlank(reviewId)){//根据评论编号查询回复列表
            cnd.and("reviewId", "=",reviewId);
        }
        if(!Strings.isBlank(type)){//1评论  2回复
            cnd.and("type", "=",type);
        }
        if(!Strings.isBlank(cmsType)){
            cnd.and("cmsType", "=",cmsType);
        }
        if(!Strings.isBlank(content)){
            cnd.and("content", "like","%"+content+"%");
        }
    	return cmsReviewService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions("store.cms.review")
    public String add() {
    	return "pages/store/cms/review/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Cms_review")
    @RequiresPermissions("store.cms.review.add")
    public Object addDo(Cms_review cmsReview, HttpServletRequest req) {
		try {
			cmsReviewService.insert(cmsReview);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("store.cms.review")
    public String edit(@PathVariable String id,HttpServletRequest req) {
		req.setAttribute("obj", cmsReviewService.fetch(id));
		return "pages/store/cms/review/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Cms_review")
    @RequiresPermissions("store.cms.review.edit")
    public Object editDo(Cms_review cmsReview, HttpServletRequest req) {
		try {
            cmsReview.setOpBy(StringUtil.getUid());
			cmsReview.setOpAt((int) (System.currentTimeMillis() / 1000));
			cmsReviewService.updateIgnoreNull(cmsReview);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Cms_review")
    @RequiresPermissions("store.cms.review.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
				cmsReviewService.delete(ids);
    			req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
			}else{
//				cmsReviewService.delete(id);
                Cms_review cms_review = new Cms_review();
                cms_review.setDelFlag(true);
                cms_review.setId(id);
                cmsReviewService.updateIgnoreNull(cms_review);
    			req.setAttribute("id", id);
			}
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("store.cms.review")
	public String detail(@PathVariable String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
            req.setAttribute("obj", cmsReviewService.fetch(id));
		}else{
            req.setAttribute("obj", null);
        }
        return "pages/store/cms/review/detail";
    }

    /**
     * 进入回复列表页面
     * @param reviewId
     * @param req
     * @return
     */
    @RequestMapping("/reply")
    public String reply(String reviewId, HttpServletRequest req) {
        req.setAttribute("reviewId", reviewId);
        return "pages/store/cms/review/replyIndex";
    }

}
