package com.aebiz.app.web.modules.controllers.platform.member;

import com.aebiz.app.member.modules.models.Member_account;
import com.aebiz.app.member.modules.services.MemberAccountService;
import com.aebiz.app.member.modules.services.MemberCouponService;
import com.aebiz.app.sales.modules.models.Sales_coupon;
import com.aebiz.app.sales.modules.services.SalesCouponLogService;
import com.aebiz.app.sales.modules.services.SalesCouponService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
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

@Controller
@RequestMapping("/platform/member/account")
public class MemberAccountController {
    private static final Log log = Logs.get();
    @Autowired
	private MemberAccountService memberAccountService;
    @Autowired
    private MemberCouponService memberCouponService;
    @Autowired
    private SalesCouponService salesCouponService;
    @Autowired
    private SalesCouponLogService salesCouponLogService;

    @RequestMapping("")
    @RequiresPermissions("platform.member.account")
	public String index() {
		return "pages/platform/member/account/index";
	}

	@RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("platform.member.account")
    public Object data(DataTable dataTable) {
		Cnd cnd = Cnd.NEW();
    	return memberAccountService.data(dataTable.getLength(), dataTable.getStart(), dataTable.getDraw(), dataTable.getOrders(), dataTable.getColumns(), cnd, null);
    }

    @RequestMapping("/add")
    @RequiresPermissions("platform.member.account")
    public String add() {
    	return "pages/platform/member/account/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Member_account")
    @RequiresPermissions("platform.member.account.add")
    public Object addDo(Member_account memberAccount, HttpServletRequest req) {
		try {
			memberAccountService.insert(memberAccount);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("platform.member.account")
    public String edit(@PathVariable String id,HttpServletRequest req) {
		req.setAttribute("obj", memberAccountService.fetch(id));
		return "pages/platform/member/account/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Member_account")
    @RequiresPermissions("platform.member.account.edit")
    public Object editDo(Member_account memberAccount, HttpServletRequest req) {
		try {
            memberAccount.setOpBy(StringUtil.getUid());
			memberAccount.setOpAt((int) (System.currentTimeMillis() / 1000));
			memberAccountService.updateIgnoreNull(memberAccount);
			return Result.success("globals.result.success");
		} catch (Exception e) {
			return Result.error("globals.result.error");
		}
    }

    @RequestMapping(value = {"/delete/{id}", "/delete"})
    @SJson
    @SLog(description = "Member_account")
    @RequiresPermissions("platform.member.account.delete")
    public Object delete(@PathVariable(required = false) String id, @RequestParam(value = "ids",required = false)  String[] ids, HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
                String[] str=ids[0].split(",");
				memberAccountService.delete(str);
    			req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
			}else{
				memberAccountService.delete(id);
    			req.setAttribute("id", id);
			}
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("platform.member.account")
	public String detail(@PathVariable String id, HttpServletRequest req) {
		if (!Strings.isBlank(id)) {
            req.setAttribute("obj", memberAccountService.fetch(id));
		}else{
            req.setAttribute("obj", null);
        }
        return "pages/platform/member/account/detail";
    }

    @RequestMapping("/{accountId}/coupon")
    @RequiresPermissions(value = {"platform.member.account", "platform.member.user"}, logical = Logical.OR)
    public String coupon(@PathVariable String accountId, HttpServletRequest req) {
        Cnd cnd = Cnd.NEW();
        cnd.and("delFlag", "=", false);
        req.setAttribute("couponList", salesCouponService.query(cnd));
        req.setAttribute("accountId", accountId);
        return "pages/platform/member/account/coupon";
    }

    @RequestMapping("/couponDo")
    @SJson
    @SLog(description = "赠送优惠券")
    @RequiresPermissions(value = {"platform.member.account.edit", "platform.member.user"}, logical = Logical.OR)
    public Object couponDo(@RequestParam("storeId")String storeId,
                           @RequestParam("couponId")String couponId,
                           @RequestParam("accountId")String accountId,
                           @RequestParam("num")Integer num) {
        try {
            if (!salesCouponService.isReceieveable(couponId, num)) {
                return Result.error(102,"member.account.tip.coupon.notreceieveable");
            }
            memberCouponService.save(storeId, couponId, accountId, num);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

}
