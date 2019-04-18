package com.aebiz.app.web.modules.controllers.front.pc.member;


import com.aebiz.app.acc.modules.models.Account_info;
import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.acc.modules.services.AccountInfoService;
import com.aebiz.app.acc.modules.services.AccountUserService;
import com.aebiz.app.member.modules.models.Member_account_score;
import com.aebiz.app.member.modules.models.Member_cart;
import com.aebiz.app.member.modules.models.Member_level;
import com.aebiz.app.member.modules.models.Member_user;
import com.aebiz.app.member.modules.services.MemberAccountScoreService;
import com.aebiz.app.member.modules.services.MemberCartService;
import com.aebiz.app.member.modules.services.MemberLevelService;
import com.aebiz.app.member.modules.services.MemberUserService;
import com.aebiz.app.shop.modules.services.ShopAreaService;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

import static com.aebiz.baseframework.base.Result.error;

/**
 * PC会员中心常用功能控制器：
 * 会员中心首页、根据当前登录会员，加载header、left页面
 * 商品、商户收藏
 * 优惠券
 * 积分记录
 *
 * Created by Thinkpad on 2017-05-31.
 */
@Controller
@RequestMapping("/member/home")
public class PcMemberHomeController {

    private static final Log log = Logs.get();

    @Autowired
    private AccountInfoService accountInfoService;

    @Autowired
    private AccountUserService accountUserService;

    @Autowired
    private MemberAccountScoreService memberAccountScoreService;

    @Autowired
    private MemberLevelService memberLevelService;

    @Autowired
    private ShopAreaService shopAreaService;
    /**
     * 跳转到会员中心首页
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(HttpServletRequest req,HttpSession session) {
        Subject subject = SecurityUtils.getSubject();
        Member_user user = (Member_user) subject.getPrincipal();
        if(Strings.isNotBlank(user.getAccountInfo().getImageUrl())){
            session.setAttribute("personal_photo",user.getAccountInfo().getImageUrl());
        }
        if(Strings.isNotBlank(user.getLevelId())){
            Member_level member_level = memberLevelService.fetch(Cnd.where("id","=",user.getLevelId()));
            session.setAttribute("level",member_level.getName());
        }else{
            session.setAttribute("level","vip0");
        }
        return "pages/front/pc/member/order";
    }

    /**
     * 跳转到会员中心
     * @return
     */

    @RequestMapping(value = "center", method = RequestMethod.GET)
    public String center(HttpServletRequest req,HttpSession session) {
        String accountId = StringUtil.getMemberUid();
        Subject subject = SecurityUtils.getSubject();
        Member_user user = (Member_user) subject.getPrincipal();
        Account_user accountUser = accountUserService.getAccount(user.getAccountId());
        int length = accountUser.getLoginname().length();
        String username = " ";
        username = accountUser.getLoginname().charAt(0) + "******" + accountUser.getLoginname().charAt(length-1);
        req.setAttribute("username",username);
        Account_info info =  accountInfoService.fetch(Cnd.where("id","=",accountId));
        String pid = info.getProvinceId(),cid = info.getCityId(),aid = info.getAreaId();
        String pjid = info.getJx_province(),cjid = info.getJx_city(), ajid = info.getJx_county();
        if (!Strings.isEmpty(pid)){
            info.setProvinceId(shopAreaService.getNameByCode(pid));
            if (!Strings.isEmpty(cid)) {
                info.setCityId(shopAreaService.getNameByCode(cid));
                if (!Strings.isEmpty(aid)) {
                    info.setAreaId(shopAreaService.getNameByCode(aid));
                }
            }
        }
        if (!Strings.isEmpty(pjid)){
            info.setJx_province(shopAreaService.getNameByCode(pjid));
            if (!Strings.isEmpty(cjid)) {
                info.setJx_city(shopAreaService.getNameByCode(cjid));
                if (!Strings.isEmpty(ajid)) {
                    info.setJx_county(shopAreaService.getNameByCode(ajid));
                }
            }
        }
        if(Strings.isNotBlank(info.getImageUrl())){
            session.setAttribute("personal_photo",info.getImageUrl());
        }
        req.setAttribute("accountInfo",info);
        List<Member_account_score> list = memberAccountScoreService.query(Cnd.where("accountId","=",accountId));
        req.setAttribute("obj", list);
        return "pages/front/pc/member/center";
    }

    /**
     *会员积分
     * @param req
     * @return
     */
    @RequestMapping(value= "/data",method = RequestMethod.POST)
    @SJson
    public Object integral(@RequestParam("page")Integer page, @RequestParam("rows")Integer rows, @RequestParam("status")Integer status, HttpServletRequest req) {
        //获取当前会员的Id
        String accountId = StringUtil.getMemberUid();
        Map<String,Object> map  = memberAccountScoreService.selectDataAll(accountId,page,rows,status);
        int records = (int)map.get("records");
        if(records != 0){
            List<Member_account_score> listAll = memberAccountScoreService.query(Cnd.where("accountId","=",accountId).asc("creatAt"));
            map.put("scoreAll", listAll.get(listAll.size()-1).getNewScore());
        }
        return map;
    }
    /**
     *保存修改的个人数据
     * @return
     */
    @RequestMapping(value = "/edit/{id}")
    @SJson
    public Object updateData(Account_info account_info, @PathVariable String id, HttpServletRequest req) {
        account_info.setOpBy(StringUtil.getUid());
        account_info.setOpAt((int) (System.currentTimeMillis() / 1000));
        accountInfoService.updateIgnoreNull(account_info);
        return Result.success("Save success");
    }
}
