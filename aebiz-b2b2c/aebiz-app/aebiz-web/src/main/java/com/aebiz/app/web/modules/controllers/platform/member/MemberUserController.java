package com.aebiz.app.web.modules.controllers.platform.member;

import com.aebiz.app.acc.modules.models.Account_info;
import com.aebiz.app.acc.modules.models.Account_login;
import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.acc.modules.services.AccountInfoService;
import com.aebiz.app.acc.modules.services.AccountLoginService;
import com.aebiz.app.acc.modules.services.AccountUserService;
import com.aebiz.app.member.modules.commons.vo.Member;
import com.aebiz.app.member.modules.models.Member_account;
import com.aebiz.app.member.modules.models.Member_user;
import com.aebiz.app.member.modules.services.*;
import com.aebiz.app.order.modules.models.Order_main;
import com.aebiz.app.order.modules.services.OrderMainService;
import com.aebiz.app.shop.modules.services.ShopAreaService;
import com.aebiz.app.web.commons.log.annotation.SLog;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.OffsetPager;
import com.aebiz.baseframework.page.datatable.DataTable;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.DateUtil;
import com.aebiz.commons.utils.StringUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.elasticsearch.common.Strings;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

import java.util.Calendar;
import java.util.List;

@Controller
@RequestMapping("/platform/member/user")
public class MemberUserController {
    private static final Log log = Logs.get();

    @Autowired
    private MemberUserService memberUserService;

    @Autowired
    private MemberTypeService memberTypeService;

    @Autowired
    private AccountUserService accountUserService;

    @Autowired
    private ShopAreaService shopAreaService;

    @Autowired
    private AccountInfoService accountInfoService;

    @Autowired
    private MemberAccountService memberAccountService;

    @Autowired
    private AccountLoginService accountLoginService;

    @Autowired
    private OrderMainService orderMainService;

    @RequestMapping("")
    @RequiresPermissions("platform.member.user")
    public String index(HttpServletRequest req) {
        //初始化会员类型
        req.setAttribute("typeList", memberTypeService.query(Cnd.NEW()));
        return "pages/platform/member/user/index";
    }

    @RequestMapping("/data")
    @SJson("full")
    @RequiresPermissions("platform.member.user")
    public Object data(@RequestParam(value = "typeId", required = false) String typeId,
                       @RequestParam(value = "levelId", required = false) String levelId,
                       @RequestParam(value = "queryStr", required = false) String queryStr,
                       DataTable dataTable) {
        /*查询条件*/
        Cnd cnd = Cnd.NEW();
        if (!Strings.isEmpty(typeId)) {
            cnd.and("mu.typeId", "=", typeId);
        }
        if (!Strings.isEmpty(levelId)) {
            cnd.and("mu.levelId", "=", levelId);
        }
        if (!Strings.isEmpty(queryStr)) {
            List<Account_user> accountUserList = accountUserService.query(Cnd.where("loginname", "like", queryStr+"%").or("email", "like", queryStr+"%").or("mobile", "like", queryStr+"%"));
            String[] accountIdArray = new String[]{};
            if (accountUserList != null) {
                for(Account_user accountUser:accountUserList){
                    accountIdArray = ArrayUtils.add(accountIdArray,accountUser.getAccountId());
                }
            }
            cnd.and("mu.accountId", "in", accountIdArray);
        }
        /*设置排序*/
        List<DataTableOrder> orders = dataTable.getOrders();
        List<DataTableColumn> columns = dataTable.getColumns();
        cnd.desc("mu.opAt");
        if (orders != null && orders.size() > 0) {
            for (int i = 0; i < orders.size(); i++) {
                DataTableOrder order = orders.get(i);
                int col = order.getColumn();
                String data = columns.get(col).getData();
                String dir = order.getDir();
                cnd.orderBy(data, dir);
                log.debug(cnd);
            }
        }
        log.debug(cnd);

        Pager pager = new OffsetPager(dataTable.getStart(), dataTable.getLength());  // 设置分页
        List<Member> list = queryMember(cnd.and("mu.delFlag","=",false), pager);                                  // 获取会员列表的数据

        /*返回到页面的对象,datatable接受的格式只能是这样,obj的key是固定死的,不能改变*/
        NutMap obj = new NutMap();
        obj.put("recordsFiltered", memberUserService.count());
        obj.put("data", list);
        obj.put("draw", dataTable.getDraw());
        obj.put("recordsTotal", dataTable.getLength());
        return obj;
    }

    @RequestMapping("/add")
    @RequiresPermissions("platform.member.user")
    public String add(HttpServletRequest req) {
        //初始化会员类型
        req.setAttribute("typeList", memberTypeService.query(Cnd.NEW()));
        return "pages/platform/member/user/add";
    }

    @RequestMapping("/addDo")
    @SJson
    @SLog(description = "Member_user")
    @RequiresPermissions("platform.member.user.add")
    public Object addDo(Member_user memberUser, Account_user accountUser, Account_info accountInfo) {
        try {
            memberUserService.addMemberUser(memberUser, accountUser, accountInfo);
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
        return Result.success("globals.result.success");
    }

    @RequestMapping("/edit/{id}")
    @RequiresPermissions("platform.member.user")
    public String edit(@PathVariable String id, HttpServletRequest req) {
        // 初始化数据
        Member_user memberUser = memberUserService.fetchLinks(memberUserService.fetch(id), "^(memberType|memberLevel|memberAccount|accountInfo|accountUser)$");
        req.setAttribute("obj", memberUser);
        //初始化会员类型
        req.setAttribute("typeList", memberTypeService.query(Cnd.NEW()));
        return "pages/platform/member/user/edit";
    }

    @RequestMapping("/editDo")
    @SJson
    @SLog(description = "Member_user")
    @RequiresPermissions("platform.member.user.edit")
    public Object editDo(Member_user memberUser, Account_user accountUser, Account_info accountInfo, @RequestParam("memberUserId") String memberUserId, @RequestParam("accountUserId") String accountUserId, @RequestParam("accountId") String accountId) {
        try {
            memberUser.setId(memberUserId);
            accountUser.setId(accountUserId);
            accountInfo.setId(accountId);
            memberUserService.updateMemberUser(memberUser, accountUser, accountInfo);
            return Result.success("globals.result.success");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/detail/{id}")
    @RequiresPermissions("platform.member.user")
    public String detail(@PathVariable String id, HttpServletRequest req) {
        Member_user user = memberUserService.fetchLinks(memberUserService.fetch(id), "^(memberType|memberLevel|memberAccount|accountUser|accountInfo)$");
        if (user != null && !Strings.isEmpty(user.getAccountId())) {
            req.setAttribute("info", infoHandle(user.getAccountInfo()));
            req.setAttribute("user", user.getAccountUser());
            req.setAttribute("type", user.getMemberType());
            req.setAttribute("level", user.getMemberLevel());
            req.setAttribute("account", user.getMemberAccount());
            req.setAttribute("lastRecord", getLastRecord(user.getAccountId()));
            req.setAttribute("area",shopAreaService);
        }
        return "pages/platform/member/user/detail";
    }

    @RequestMapping("/resetPwd/{id}")
    @SJson
    @RequiresPermissions("platform.member.user.resetPwd")
    @SLog(description = "重置店铺密码")
    public Object resetPwd(@PathVariable(required = false) String id, HttpServletRequest req) {
        try {
            Member_user memberUser = memberUserService.fetch(id);
            memberUserService.fetchLinks(memberUser,"accountInfo");
            RandomNumberGenerator rng = new SecureRandomNumberGenerator();
            String salt = rng.nextBytes().toBase64();
            String hashedPasswordBase64 = new Sha256Hash("ET922", salt, 1024).toBase64();

            accountUserService.update(Chain.make("salt", salt).add("password", hashedPasswordBase64), Cnd.where("accountId", "=", memberUser.getAccountId()));
            Account_user accountUser = accountUserService.fetch(Cnd.where("accountId","=",memberUser.getAccountId()));
            if(accountUser != null){
                req.setAttribute("loginname", accountUser.getLoginname());
            }
            return Result.success("globals.result.success", "ET922");
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    @RequestMapping("/check")
    @SJson
    @SLog(description = "Member_user")
    @RequiresPermissions("platform.member.user")
    public Object checkUnique(@RequestParam("fieldName") String fn, @RequestParam("fieldValue") String fv) {
        try {
            return Result.success("globals.result.success", memberUserService.checkUnique(fn, fv));
        } catch (Exception e) {
            return Result.error("globals.result.error");
        }
    }

    // 对会员信息进行预处理
    private Account_info infoHandle(Account_info accountInfo) {
        if (accountInfo != null) {
            String constellation = accountInfo.getAstro(); // 星座
            if (Strings.isEmpty(constellation)) {
                // 当没有取到星座值但生日有值时，根据生日自动判断星座
                constellation = StringUtil.getConstellationByBirthday(accountInfo.getB_month(), accountInfo.getB_day());
            }
            accountInfo.setAstro(constellation);
            //设置所在地
            String pid = accountInfo.getProvinceId(),cid = accountInfo.getCityId(),aid = accountInfo.getAreaId();
            if (!Strings.isEmpty(pid) && !Strings.isEmpty(cid) &&!Strings.isEmpty(aid)) {
                String pName = "", cName = "", aName = "";
                pName = shopAreaService.getNameByCode(pid);
                cName = shopAreaService.getNameByCode(cid);
                aName = shopAreaService.getNameByCode(aid);
                if (!Strings.isEmpty(pName) && !Strings.isEmpty(cName) &&!Strings.isEmpty(aName)){
                    accountInfo.setLocus(pName, cName, aName);
                }
            }
            //设置籍贯
            //accountInfo.setNativePlace(accountInfo.getNativePlace());
            // 设置生日
            Integer year = accountInfo.getB_year(), month = accountInfo.getB_month(),day = accountInfo.getB_day();
            if (year != null && month != null && day != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month - 1, day);// calendar中的月份是0-11，所以需要减1，否则算出来就多一个月
                String birthday = DateUtil.format(calendar.getTime(), "yyyy-MM-dd");
                accountInfo.setBirthday(birthday);
            }
        }
        return accountInfo;
    }

    // 获取会员近期记录
    private NutMap getLastRecord(String accountId) {
        Integer loginTime = null;
        String ip = null;
        Account_login accountLogin = accountLoginService.fetch(Cnd.where("accountId", "=", accountId));
        if (accountLogin != null) {
            loginTime = accountLogin.getLoginAt();
            ip = accountLogin.getIp();
        }

        Integer orderTime = null, payTime = null;
        Order_main orderMain = orderMainService.fetch(Cnd.where("accountId", "=", accountId).desc("orderAt"));
        if (orderMain != null) {
            orderTime = orderMain.getOrderAt();
            payTime = orderMain.getPayAt();
        }

        NutMap lastRecord = new NutMap();
        lastRecord.put("loginTime", loginTime);
        lastRecord.put("ip", ip);
        lastRecord.put("orderTime", orderTime);
        lastRecord.put("payTime", payTime);
        return lastRecord;
    }

    // 查询会员列表,可分页,可多重排序
    private List<Member> queryMember(Cnd cnd, Pager pager) {
        Sql sql = Sqls.queryRecord("SELECT\n" +
                "\tDISTINCT mu.id,\n" +
                "\tmu.opAt,\n" +
                "\tmu.accountId,\n" +
                "\tau.loginname AS username,\n" +
                "\tai.nickname,\n" +
                "\tmt.`name` AS type,\n" +
                "\tml.`name` AS level,\n" +
                "\tma.money,\n" +
                "\tma.score,\n" +
                "\tau.mobile,\n" +
                "\tau.email,\n" +
                "\tau.disabled AS state\n" +
                "FROM\n" +
                "\tmember_user AS mu\n" +
                "LEFT JOIN member_type AS mt ON mu.typeId = mt.id\n" +
                "LEFT JOIN member_level AS ml ON mu.levelId = ml.id\n" +
                "LEFT JOIN member_account AS ma ON mu.accountId = ma.accountId\n" +
                "LEFT JOIN account_info AS ai ON mu.accountId = ai.id\n" +
                "LEFT JOIN account_user AS au ON mu.accountId = au.accountId\n" +
                "$condition");
        if (cnd != null) {
            sql.setCondition(cnd);
        } else {
            sql.setCondition(Cnd.NEW());
        }

        if (pager != null) {
            sql.setPager(pager);
        }
        log.debug(sql);
        memberUserService.dao().execute(sql);
        return sql.getList(Member.class);
    }
}