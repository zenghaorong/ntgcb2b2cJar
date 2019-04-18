package com.aebiz.app.web.modules.controllers.front.pc.member;

import com.aebiz.app.member.modules.models.Member_address;
import com.aebiz.app.member.modules.services.MemberAddressService;
import com.aebiz.app.shop.modules.services.ShopAreaService;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 会员收货地址管理
 * 新增、编辑、删除、设为默认
 * Created by Thinkpad on 2017/6/8.
 */
@Controller
@RequestMapping("/member/address")
public class PcMemberAddressController {
    private static final Log log = Logs.get();

    @Autowired
    private MemberAddressService memberAddressService;
    @Autowired
    private ShopAreaService shopAreaService;
    /**
     * 到个人中心收货地址管理页面
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    //@RequiresRoles(value={"member"})
    public String index(HttpServletRequest request) {
        List<Member_address> list = memberAddressService.query(Cnd.where("accountId","=",StringUtil.getMemberUid()).desc("defaultValue"));
        for(int i=0;i<list.size();i++){
            String provinceId = list.get(i).getProvince();
            String cityId = list.get(i).getCity();
            String countyId = list.get(i).getCounty();
            if (!Strings.isEmpty(provinceId)) {
                list.get(i).setProvince(shopAreaService.getNameByCode(provinceId));
                if(!Strings.isEmpty(cityId)){
                    list.get(i).setCity(shopAreaService.getNameByCode(cityId));
                    if(!Strings.isEmpty(countyId)){
                        list.get(i).setCounty(shopAreaService.getNameByCode(countyId));
                    }
                }
            }
        }
        request.setAttribute("listAddressSize",list.size());
        request.setAttribute("listAddress", list);
        return "pages/front/pc/member/addressIndex";
    }

    @RequestMapping(value = "/data", method = RequestMethod.POST)
    @SJson
    public Object data(HttpServletRequest request) {
        try{
            List<Member_address> list = memberAddressService.query(Cnd.where("accountId","=",StringUtil.getMemberUid()).desc("defaultValue"));
            for(int i=0;i<list.size();i++){
                String provinceId = list.get(i).getProvince();
                String cityId = list.get(i).getCity();
                String countyId = list.get(i).getCounty();
                if (!Strings.isEmpty(provinceId)) {
                    list.get(i).setProvince(shopAreaService.getNameByCode(provinceId));
                    if(!Strings.isEmpty(cityId)){
                        list.get(i).setCity(shopAreaService.getNameByCode(cityId));
                        if(!Strings.isEmpty(countyId)){
                            list.get(i).setCounty(shopAreaService.getNameByCode(countyId));
                        }
                    }
                }
            }
            return list;
        }catch (Exception e){
            return Result.error("member.address.select.fail");
        }
    }

    /**
     * 新增收货地址
     * @return
     */
    @RequestMapping(value = "/addAddressDo")
    @SJson
    public Object addAddressDo(Member_address member_address, HttpServletRequest req) {
        try{
            String result = memberAddressService.add(member_address);
            if("1".equals(result)){
                return Result.error("member.address.count");
            }
            return Result.success("member.address.add.success");
        }catch (Exception e){
            return Result.error("member.address.add.fail");
        }
    }

    /**
     * 查询单个收货地址
     * @return
     */
    @SJson
    @RequestMapping(value = "/selectAddressDo/{id}")
    public Object selectAddressDo(@PathVariable("id") String id, HttpServletRequest req) {
        try {
            Member_address member_address = memberAddressService.fetch(id);
            String provinceId = member_address.getProvince();
            String cityId = member_address.getCity();
            String countyId = member_address.getCounty();
            if (!Strings.isEmpty(provinceId)) {
                member_address.setProvince(shopAreaService.getNameByCode(provinceId));
                if(!Strings.isEmpty(cityId)){
                    member_address.setCity(shopAreaService.getNameByCode(cityId));
                    if(!Strings.isEmpty(countyId)){
                        member_address.setCounty(shopAreaService.getNameByCode(countyId));
                    }
                }
            }
            return member_address;
        }catch (Exception e){
            return Result.error("member.address.select.fail");
        }
    }

    /**
     * 编辑收货地址
     * @return
     */
    @SJson
    @RequestMapping(value = "/editAddressDo")
    public Object editAddressDo(Member_address member_address, HttpServletRequest req) {
        try {
            member_address.setAccountId(StringUtil.getMemberUid());
            member_address.setOpBy(StringUtil.getUid());
            member_address.setOpAt((int) (System.currentTimeMillis() / 1000));
            memberAddressService.editAddress(member_address);
            //memberAddressService.updateIgnoreNull(member_address);
            return Result.success("member.address.update.success");
        }catch (Exception e){
            return Result.error("member.address.update.fail");
        }

    }

    /**
     * 删除收货地址
     * @return
     */
    @SJson
    @RequestMapping(value = "/delAddressDo/{id}")
    public Object delAddressDo(@PathVariable("id")String id) {
        try{
            memberAddressService.delete(id);
            return Result.success("member.address.delete.success");
        }catch (Exception e){
            return Result.error("member.address.delete.fail");
        }
    }

    /**
     * 收货地址设为默认
     * @return
     */
    @SJson
    @RequestMapping(value = "/setDefaultAddressDo/{id}")
    public Object setDefaultAddressDo(@PathVariable("id")String id) {
        try{
            memberAddressService.updateDefault(id,StringUtil.getMemberUid());
            return Result.success("member.address.setDefault.success");
        }catch (Exception e){
            return Result.error("member.address.setDefault.fail");
        }
    }
}
