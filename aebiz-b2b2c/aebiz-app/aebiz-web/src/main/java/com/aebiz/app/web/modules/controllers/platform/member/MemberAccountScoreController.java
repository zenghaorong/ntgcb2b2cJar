package com.aebiz.app.web.modules.controllers.platform.member;

import com.aebiz.app.member.modules.models.Member_account_score;
import com.aebiz.app.member.modules.services.MemberAccountScoreService;
import com.aebiz.app.member.modules.services.MemberAccountService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
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
@RequestMapping("/platform/member/account/score")
public class MemberAccountScoreController {
    private static final Log log = Logs.get();

    @Autowired
	private MemberAccountScoreService memberAccountScoreService;
    @Autowired
    private MemberAccountService memberAccountService;

    @RequestMapping("")
    @RequiresPermissions("platform.member.account.score")
	public String index() {
		return "pages/platform/member/account/score/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("platform.member.account.score")
    public Object data(@RequestParam(value = "accountId",required = false) String accountId,DataTable dataTable) {
		Cnd cnd = Cnd.NEW();
		if (!Strings.isEmpty(accountId)){
            cnd = Cnd.where("accountId", "=", accountId);
            cnd.desc("creatAt");
            NutMap map = memberAccountScoreService.viewScoreInfo(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
		    return map;
        }
    	return memberAccountScoreService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping("/add/{accountId}")
    @RequiresPermissions("platform.member.account.score")
    public String add(@PathVariable String accountId, HttpServletRequest req) {
        req.setAttribute("obj",memberAccountService.fetch(Cnd.where("accountId","=",accountId)));
    	return "pages/platform/member/account/score/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Member_account_score")
    @RequiresPermissions("platform.member.account.score.add")
    public Object addDo(Member_account_score memberAccountScore, @RequestParam("accountId") String accountId) {
		try {
			memberAccountScoreService.updateScore(memberAccountScore,accountId);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("platform.member.account.score")
	public String detail(@PathVariable String id, HttpServletRequest req) {
        req.setAttribute("accountId",id);
        return "pages/platform/member/account/score/detail";
    }

}
