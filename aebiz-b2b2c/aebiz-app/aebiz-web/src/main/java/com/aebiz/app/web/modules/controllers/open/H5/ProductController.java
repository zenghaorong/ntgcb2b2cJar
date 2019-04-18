package com.aebiz.app.web.modules.controllers.open.H5;

import com.aebiz.app.goods.modules.models.Goods_image;
import com.aebiz.app.goods.modules.models.Goods_main;
import com.aebiz.app.goods.modules.models.Goods_product;
import com.aebiz.app.goods.modules.services.GoodsImageService;
import com.aebiz.app.goods.modules.services.GoodsProductService;
import com.aebiz.app.goods.modules.services.GoodsService;
import com.aebiz.baseframework.base.Result;
import com.aebiz.baseframework.page.Pagination;
import com.aebiz.baseframework.view.annotation.SJson;
import com.alibaba.fastjson.JSON;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Auther: zenghaorong
 * @Date: 2019/3/23  21:39
 * @Description:
 */
@Controller
@RequestMapping("/open/h5/product")
public class ProductController {

    private static final Log log = Logs.get();

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsImageService goodsImageService;

    @Autowired
    private GoodsProductService goodsProductService;

    /**
     * 进入商品列表页  或 进入宝妈专区
     */
    @RequestMapping("/list.html")
    public String index() {
        return "pages/front/h5/niantu/productList";
    }

    /**
     * 进入商品详情页
     */
    @RequestMapping("/productDetail.html")
    public String productDetail(HttpServletRequest request) {
        String id = request.getParameter("id");
        request.setAttribute("id",id);
        return "pages/front/h5/niantu/productDetails";
    }




    /**
     * 获得商品列表
     * @param pageNumber 页码
     * @return
     */
    @RequestMapping("ProductList.html")
    @SJson
    public Result getProductList(Integer pageNumber){
        try {
            if(pageNumber == null){
                pageNumber = 0;
            }
            Cnd cnd = Cnd.NEW();
            cnd.and("delFlag", "=", 0 );
            Pagination res = goodsService.listPage(pageNumber, 20, cnd);
            List<?> resList = res.getList();
            List<Goods_main> productList = new ArrayList<>();
            if(resList!=null&&resList.size()>0){
                for (int i=0;i<resList.size();i++){
                    Goods_main o = (Goods_main) resList.get(i);
                    Cnd imgCnd = Cnd.NEW();
                    imgCnd.and("goodsId","=",o.getId());
                    List<Goods_image> imgList = goodsImageService.query(imgCnd);
                    if(imgList!=null&&imgList.size()>0){
                        o.setImgList(imgList.get(0).getImgAlbum());
                    }
                    Cnd proCnd = Cnd.NEW();
                    proCnd.and("goodsId","=",o.getId());
                    List<Goods_product> gpList = goodsProductService.query(proCnd);
                    if(gpList!=null&&gpList.size()>0){
                        Integer salePrice = gpList.get(0).getSalePrice();
                        int price = salePrice / 100;
                        o.setPrice(price+"");
                        o.setSaleNumMonth(gpList.get(0).getSaleNumMonth()+"");
                    }
                    productList.add(o);
                }
            }
            return Result.success("ok",productList);
        } catch (Exception e) {
            log.error("获取商品列表异常",e);
            return Result.error("fail");
        }
    }

    /**
     * 获得商品列表
     * @param id
     * @return
     */
    @RequestMapping("getProductDetail.html")
    @SJson
    public Result getProductDetail(String id){
        try {
            Cnd cnd = Cnd.NEW();
            cnd.and("delFlag", "=", 0 );
            cnd.and("id","=",id);
            Goods_main o = goodsService.fetch(id);
                    Cnd imgCnd = Cnd.NEW();
                    imgCnd.and("goodsId","=",o.getId());
                    List<Goods_image> imgList = goodsImageService.query(imgCnd);
                    o.setImageList(imgList);
                    if(imgList!=null&&imgList.size()>0){
                        o.setImgList(imgList.get(0).getImgAlbum());
                    }
                    Cnd proCnd = Cnd.NEW();
                    proCnd.and("goodsId","=",o.getId());
                    List<Goods_product> gpList = goodsProductService.query(proCnd);
                    if(gpList!=null&&gpList.size()>0){
                        Integer salePrice = gpList.get(0).getSalePrice();
                        int marketPrice = gpList.get(0).getCostPrice();
                        int price = salePrice / 100;
                        o.setPrice(price+"");
                        o.setMarketPrice(marketPrice/100+"");
                        o.setSaleNumMonth(gpList.get(0).getSaleNumMonth()+"");
                    }
            return Result.success("ok",o);
        } catch (Exception e) {
            log.error("获取商品列表异常",e);
            return Result.error("fail");
        }
    }

    /**
     * 订单确认页获得商品列表
     * @param productList
     * @return
     */
    @RequestMapping("getOrderProductList.html")
    @SJson
    public Result getOrderProductList(String productList){
        try {
            List<Map<String,Object>> list = (List<Map<String, Object>>) JSON.parse(productList);
            List<Goods_main> goods_mainList = new ArrayList<>();
            for(Map<String,Object> map:list) {
                String id = (String) map.get("productId");
                Cnd cnd = Cnd.NEW();
                cnd.and("delFlag", "=", 0);
                cnd.and("id", "=", id);
                Goods_main o = goodsService.fetch(id);
                Cnd imgCnd = Cnd.NEW();
                imgCnd.and("goodsId", "=", o.getId());
                List<Goods_image> imgList = goodsImageService.query(imgCnd);
                o.setImageList(imgList);
                if (imgList != null && imgList.size() > 0) {
                    o.setImgList(imgList.get(0).getImgAlbum());
                }
                Cnd proCnd = Cnd.NEW();
                proCnd.and("goodsId", "=", o.getId());
                List<Goods_product> gpList = goodsProductService.query(proCnd);
                if (gpList != null && gpList.size() > 0) {
                    Integer salePrice = gpList.get(0).getSalePrice();
                    int marketPrice = gpList.get(0).getCostPrice();
                    int price = salePrice / 100;
                    o.setPrice(price + "");
                    o.setMarketPrice(marketPrice / 100 + "");
                    o.setSaleNumMonth(gpList.get(0).getSaleNumMonth() + "");
                }
                String num = (String) map.get("num");
                o.setNum(num);
                goods_mainList.add(o);
            }
            return Result.success("ok",goods_mainList);
        } catch (Exception e) {
            log.error("获取商品列表异常",e);
            return Result.error("fail");
        }
    }





}
