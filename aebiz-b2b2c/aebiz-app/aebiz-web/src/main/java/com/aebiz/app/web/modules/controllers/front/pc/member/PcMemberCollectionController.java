package com.aebiz.app.web.modules.controllers.front.pc.member;

import com.aebiz.app.goods.modules.models.Goods_favorite;
import com.aebiz.app.goods.modules.models.Goods_image;
import com.aebiz.app.goods.modules.services.GoodsFavoriteService;
import com.aebiz.app.goods.modules.services.GoodsImageService;
import com.aebiz.app.member.modules.models.Member_coupon;
import com.aebiz.app.store.modules.models.Store_favorite;
import com.aebiz.app.store.modules.services.StoreFavoriteService;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.datatable.DataTableColumn;
import com.aebiz.baseframework.page.datatable.DataTableOrder;
import com.aebiz.baseframework.view.annotation.SJson;
import com.aebiz.commons.utils.StringUtil;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.lang.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.nutz.log.Log;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.nutz.dao.util.Pojos.log;


/**
 * Created by Administrator on 2017/6/14.
 */
@Controller
@RequestMapping("/member/collection")
public class PcMemberCollectionController {

    @Autowired
    private GoodsFavoriteService goodsFavoriteService;

    @Autowired
    private GoodsImageService goodsImageService;

    @Autowired
    private StoreFavoriteService storeFavoriteService;

    /**
     * 跳转到个人中心的收藏夹
     * @return
     */

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(HttpServletRequest req, HttpSession session,@RequestParam(value = "status",required = false)String status) {
        String accountId = StringUtil.getMemberUid();
        req.setAttribute("status",Strings.isNotBlank(status)?"1" : "0");
        return "pages/front/pc/member/collection";
    }

    /**
     * 跳转到个人中心的收藏夹(全部)
     * @return
     */

    @RequestMapping(value= "/data",method = RequestMethod.POST)
    public String data(@RequestParam("pageNo")Integer page, @RequestParam("status")Integer status, @RequestParam("rows")Integer rows, ArrayList<DataTableOrder> order, ArrayList<DataTableColumn> columns, HttpServletRequest req) {
        //获取当前会员的Id
        String accountId = StringUtil.getMemberUid();
        Cnd cnd = Cnd.NEW();
        if (status == 0) {
            Map<String, Object> map = goodsFavoriteService.selectDataAll(accountId, page, rows, status);
            map.put("record",1);
            req.setAttribute("obj",map);
            return  "pages/front/pc/member/conllection_merchandise_data";
        } else if (status == 1){
            Map<String, Object> map = storeFavoriteService.selectDataAll(accountId, page, rows, status);
            map.put("record",1);
            req.setAttribute("obj",map);
            return  "pages/front/pc/member/conllection_store_data";
        }
        return "pages/front/pc/member/collection";
    }

    @RequestMapping(value= "/update",method = RequestMethod.POST)
    public String update(@RequestParam("status")Integer status,@RequestParam("id")String id,HttpServletRequest req) {
        //获取当前会员的Id
        String accountId = StringUtil.getMemberUid();
        Cnd cnd = Cnd.NEW();
        if (status == 0) {
            Goods_favorite goodsFavorite = goodsFavoriteService.fetch(id);
            goodsFavorite.setDelFlag(true);
            goodsFavorite.setOpBy(StringUtil.getUid());
            goodsFavorite.setOpAt((int) (System.currentTimeMillis() / 1000));
            goodsFavorite.setUnFavoriteTime((int) (System.currentTimeMillis() / 1000));
            goodsFavoriteService.update(goodsFavorite);
        } else if (status == 1){
            Store_favorite storeFavorite = storeFavoriteService.fetch(id);
            storeFavorite.setDelFlag(true);
            storeFavorite.setOpBy(StringUtil.getUid());
            storeFavorite.setOpAt((int) (System.currentTimeMillis() / 1000));
            storeFavorite.setUnFavoriteTime((int) (System.currentTimeMillis() / 1000));
            storeFavoriteService.update(storeFavorite);
        }
        return "pages/front/pc/member/collection";
    }

    @RequestMapping(value= "/updateAll",method = RequestMethod.POST)
    public String updateAll(@RequestParam("status")Integer status,@RequestParam("id")String id,HttpServletRequest req) {
        //获取当前会员的Id
        String accountId = StringUtil.getMemberUid();
        Cnd cnd = Cnd.NEW();
        if (status == 0) {
            int time = (int) (System.currentTimeMillis() / 1000);
            String opBy = StringUtil.getUid();
            goodsFavoriteService.update("goods_favorite", Chain.make("opBy",opBy),Cnd.where("id", "in", id));
            goodsFavoriteService.update("goods_favorite",Chain.make("opAt",time),Cnd.where("id", "in", id));
            goodsFavoriteService.update("goods_favorite",Chain.make("unFavoriteTime",time),Cnd.where("id", "in", id));
            goodsFavoriteService.update("goods_favorite",Chain.make("delFlag",true),Cnd.where("id", "in", id));
        } else if (status == 1){
            int time = (int) (System.currentTimeMillis() / 1000);
            String opBy = StringUtil.getUid();
            storeFavoriteService.update("store_favorite", Chain.make("opBy", opBy), Cnd.where("id", "in", id));
            storeFavoriteService.update("store_favorite", Chain.make("opAt", time), Cnd.where("id", "in", id));
            storeFavoriteService.update("store_favorite", Chain.make("unFavoriteTime", time), Cnd.where("id", "in", id));
            storeFavoriteService.update("store_favorite", Chain.make("delFlag", true), Cnd.where("id", "in", id));
        }
        return "pages/front/pc/member/collection";
    }

    @RequestMapping(value= "/search",method = RequestMethod.POST)
    public String search(@RequestParam("pageNo")Integer page,@RequestParam("status")Integer status,@RequestParam("rows")Integer rows,@RequestParam("content")String content, HttpServletRequest req) {
        //获取当前会员的Id
        String accountId = StringUtil.getMemberUid();
        Cnd cnd = Cnd.NEW();
        if (status == 0) {
            Map<String, Object> map = goodsFavoriteService.selectData(content,accountId, page, rows, status);
            int records = (int)map.get("records");
            if( records == 0){
                map.put("record",0);
            }else{
                map.put("record",1);
            }
            req.setAttribute("obj",map);
            return  "pages/front/pc/member/conllection_merchandise_data";
        } else if (status == 1){
            Map<String, Object> map = storeFavoriteService.selectData(content,accountId, page, rows, status);
            int records = (int)map.get("records");
            if( records == 0){
                map.put("record",0);
            }else{
                map.put("record",1);
            }
            req.setAttribute("obj",map);
            return  "pages/front/pc/member/conllection_store_data";
        }
        return "pages/front/pc/member/collection";
    }
}