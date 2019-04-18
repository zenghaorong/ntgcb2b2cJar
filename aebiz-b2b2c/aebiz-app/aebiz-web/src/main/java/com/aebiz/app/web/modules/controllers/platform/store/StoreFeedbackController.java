package com.aebiz.app.web.modules.controllers.platform.store;

import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.commons.utils.StringUtil;
import com.aebiz.app.store.modules.models.Store_feedback;
import com.aebiz.app.store.modules.services.StoreFeedbackService;
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

@Controller
@RequestMapping("/platform/store/score")
public class StoreFeedbackController {
    private static final Log log = Logs.get();
    @Autowired
	private StoreFeedbackService storeScoreService;

    @RequestMapping("")
    @RequiresPermissions("platform.store.score")
	public String index() {
		return "pages/platform/store/score/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("platform.store.score")
    public Object data(DataTable dataTable) {
		Cnd cnd = Cnd.NEW();
    	return storeScoreService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions("platform.store.score")
    public String add() {
    	return "pages/platform/store/score/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Store_feedback")
    @RequiresPermissions("platform.store.score.add")
    public Object addDo(Store_feedback storeScore, HttpServletRequest req) {
		try {
			storeScoreService.insert(storeScore);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("platform.store.score")
    public String edit(@PathVariable String id,HttpServletRequest req) {
		req.setAttribute("obj", storeScoreService.fetch(id));
		return "pages/platform/store/score/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Store_feedback")
    @RequiresPermissions("platform.store.score.edit")
    public Object editDo(Store_feedback storeScore, HttpServletRequest req) {
		try {
            storeScore.setOpBy(StringUtil.getUid());
			storeScore.setOpAt((int) (System.currentTimeMillis() / 1000));
			storeScoreService.updateIgnoreNull(storeScore);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Store_feedback")
    @RequiresPermissions("platform.store.score.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
				storeScoreService.delete(ids);
    			req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
			}else{
				storeScoreService.delete(id);
    			req.setAttribute("id", id);
			}
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("platform.store.score")
	public String detail(@PathVariable String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
            req.setAttribute("obj", storeScoreService.fetch(id));
		}else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/store/score/detail";
    }

}
