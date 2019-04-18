package com.aebiz.app.web.modules.controllers.platform.member;

import com.aebiz.app.member.modules.models.Member_account;
import com.aebiz.app.member.modules.models.Member_account_money;
import com.aebiz.app.member.modules.services.MemberAccountMoneyService;
import com.aebiz.app.member.modules.services.MemberAccountService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

@Controller
@RequestMapping("/platform/member/account/money")
public class MemberAccountMoneyController {
    private static final Log log = Logs.get();

    @Autowired
    private MemberAccountMoneyService memberAccountMoneyService;
    @Autowired
    private MemberAccountService memberAccountService;

    @RequestMapping("")
    @RequiresPermissions("platform.member.account.money")
    public String index() {
        return "pages/platform/member/account/money/index";
    }

    @RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("platform.member.account.money")
    public Object data(String accountId,DataTable dataTable) {
        Cnd cnd = Cnd.NEW();
        if (!Strings.isEmpty(accountId)){
            cnd = Cnd.where("accountId", "=", accountId);
            cnd.desc("creatAt");
            NutMap map =  memberAccountMoneyService.moneyInfo(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
            return map;
        }else {
            return memberAccountMoneyService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
        }
    }

    @RequestMapping("/add/{accountId}")
    @RequiresPermissions("platform.member.account.money")
    public String add(@PathVariable String accountId, HttpServletRequest req) {
        Member_account memberAccount = memberAccountService.fetch(Cnd.where("accountId","=",accountId));
        double money = 0;
        if (memberAccount != null && memberAccount.getMoney() != null){
            // 将分转换为元
            money = memberAccount.getMoney();

        }
        DecimalFormat df = new DecimalFormat("0.00");
        req.setAttribute("money",df.format(money/100));
        req.setAttribute("obj",memberAccount);
        return "pages/platform/member/account/money/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Member_account_money")
    @RequiresPermissions("platform.member.account.money.add")
    public Object addDo(String accountId,String note, double oldMoney,double diffMoney) {
        try {
            Member_account_money memberAccountMoney = new Member_account_money();
            memberAccountMoney.setOldMoney((int) (oldMoney*100));
            memberAccountMoney.setDiffMoney((int) (diffMoney*100));
            memberAccountMoney.setNote(note);
            memberAccountMoney.setAccountId(accountId);
            memberAccountMoneyService.updateMoney(memberAccountMoney,accountId);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("platform.member.account.money")
    public String edit(@PathVariable String id, HttpServletRequest req) {
        req.setAttribute("obj", memberAccountMoneyService.fetch(id));
        return "pages/platform/member/account/money/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Member_account_money")
    @RequiresPermissions("platform.member.account.money.edit")
    public Object editDo(Member_account_money memberAccountMoney, @RequestParam String at) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int createTime = (int) (sdf.parse(at).getTime() / 1000);
            memberAccountMoney.setCreatAt(createTime);
            memberAccountMoney.setOpBy(StringUtil.getUid());
            memberAccountMoney.setOpAt((int) (System.currentTimeMillis() / 1000));
            memberAccountMoneyService.updateIgnoreNull(memberAccountMoney);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Member_account_money")
    @RequiresPermissions("platform.member.account.money.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids", required = false) String ids, HttpServletRequest req) {
        try {
            if (ids != null && ids.length() > 0) {
                String[] idList = ids.split(",");
                memberAccountMoneyService.delete(idList);
                req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(idList));
            } else {
                memberAccountMoneyService.delete(id);
                req.setAttribute("id", id);
            }
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{accountId}")
    @RequiresPermissions("platform.member.account.money")
    public String detail(@PathVariable String accountId, HttpServletRequest req) {
        req.setAttribute("accountId", accountId);
        return "pages/platform/member/account/money/detail";
    }

}
