package com.aebiz.app.web.modules.controllers.open.H5;

import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.goods.modules.models.Goods_image;
import com.aebiz.app.goods.modules.models.Goods_main;
import com.aebiz.app.goods.modules.models.Goods_product;
import com.aebiz.app.goods.modules.services.GoodsImageService;
import com.aebiz.app.goods.modules.services.GoodsProductService;
import com.aebiz.app.goods.modules.services.GoodsService;
import com.aebiz.app.member.modules.models.Member_cart;
import com.aebiz.app.member.modules.services.MemberCartService;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.view.annotation.SJson;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.nutz.dao.Cnd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author chengzhuming
 * @date 2019/4/10 14:00
 */
@Controller
@RequestMapping("/open/h5/cart")
public class CartManagerController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private MemberCartService memberCartService;

    @Autowired
    private GoodsProductService goodsProductService;

    @Autowired
    private GoodsImageService goodsImageService;

    /**
     * 进入购物车页
     */
    @RequestMapping("/shoppingCart.html")
    public String shoppingCart() {
        return "pages/front/h5/niantu/shoppingCart";
    }
    /**
     * 加入购物车操作
     */
    @RequestMapping("/addCart.html")
    @SJson
    public Result addCart(HttpServletRequest request){
        String productId = request.getParameter("productId");
        String sNum = request.getParameter("num");
        int num = Integer.parseInt(sNum);
        Subject subject = SecurityUtils.getSubject();
        Account_user accountUser = (Account_user) subject.getPrincipal();
        if(accountUser==null){
            return Result.error(-1,"未登录，请重新登录！");
        }
        Cnd cartCnd = Cnd.NEW();
        cartCnd.and("goodsId","=",productId);
        cartCnd.and("delFlag","=",false);
        List<Member_cart> carList = memberCartService.query(cartCnd);
        if(carList!=null&&carList.size()>0){
            Member_cart mc = carList.get(0);
            mc.setNum(mc.getNum()+1);
            memberCartService.update(mc);
            return Result.success();
        }
        Goods_main goods_main =  goodsService.fetch(productId);
        Cnd proCnd = Cnd.NEW();
        proCnd.and("goodsId", "=", goods_main.getId());
        List<Goods_product> gpList = goodsProductService.query(proCnd);
        if(gpList!=null&&gpList.size()>0){
            Goods_product goods_product = gpList.get(0);
            memberCartService.saveCart(accountUser.getAccountId(),goods_product,goods_product.getSalePrice(),num,goods_main.getImgList());
        }else {
            return Result.error(1,"商品不存在！");
        }
       return Result.success();
    }

    /**
     * 查询购物车操作
     */
    @RequestMapping("/getCart.html")
    @SJson
    public Result getCart(HttpServletRequest request){
        Subject subject = SecurityUtils.getSubject();
        Account_user accountUser = null;
        try{
            accountUser = (Account_user) subject.getPrincipal();

        }catch (Exception e){

        }
        if(accountUser==null){
            return Result.error(-1,"未登录，请重新登录！");
        }

        Cnd cnd = Cnd.NEW();
        cnd.and("accountId","=",accountUser.getAccountId());
        cnd.and("delFlag","=",false);
        List<Member_cart> member_carts =  memberCartService.query(cnd);
        for (Member_cart m:member_carts
             ) {
            Goods_main good = goodsService.fetch(m.getGoodsId());
            if(good!=null){
                Cnd imgCnd = Cnd.NEW();
                imgCnd.and("goodsId","=",good.getId());
                List<Goods_image> imgList = goodsImageService.query(imgCnd);
                if(imgList!=null&&imgList.size()>0){
                    m.setImgurl(imgList.get(0).getImgAlbum());
                }
                m.setProductName(good.getName());

            }

        }
        return Result.success("ok",member_carts);
    }

    @RequestMapping("/updateCart")
    @SJson
    public Result updateCart(HttpServletRequest request){
        Subject subject = SecurityUtils.getSubject();
        Account_user accountUser = (Account_user) subject.getPrincipal();
        if(accountUser==null){
            return Result.error(-1,"未登录，请重新登录！");
        }
        String productId = request.getParameter("productId");
        String num = request.getParameter("num");
        try{
            Cnd cnd = Cnd.NEW();
            cnd.and("accountId","=",accountUser.getAccountId());
            cnd.and("goodsId","=",productId);
            cnd.and("delFlag","=",false);
            List<Member_cart> member_carts =  memberCartService.query(cnd);
            if(member_carts!=null&&member_carts.size()>0){
                Member_cart member_cart = member_carts.get(0);
                member_cart.setNum(Integer.parseInt(num));
                memberCartService.update(member_cart);
            }
            return Result.success();
        }catch (Exception e){
            return Result.error();
        }

    }
    @RequestMapping("/deleteCart")
    @SJson
    public Result deleteCart(HttpServletRequest request){
        Subject subject = SecurityUtils.getSubject();
        Account_user accountUser = (Account_user) subject.getPrincipal();
        if(accountUser==null){
            return Result.error(-1,"未登录，请重新登录！");
        }
        String cartIds = request.getParameter("cartIds");
        try{
            String[] ids = StringUtils.split(cartIds, ";");
            for (int i = 0 ; i<ids.length;i++){
                Member_cart cart = memberCartService.fetch(ids[i]);
                cart.setDelFlag(true);
                memberCartService.update(cart);
            }
            return Result.success();
        }catch (Exception e){
            return Result.error();
        }

    }
}
