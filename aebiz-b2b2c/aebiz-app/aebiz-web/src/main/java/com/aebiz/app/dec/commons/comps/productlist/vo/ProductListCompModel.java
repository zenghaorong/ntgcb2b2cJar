package com.aebiz.app.dec.commons.comps.productlist.vo;



import com.aebiz.app.dec.commons.comps.productlist.ProductListCompController;
import com.aebiz.app.dec.commons.vo.BaseCompModel;

import java.io.Serializable;

/**
 * Created by 金辉 on 2016/12/21.
 */
public class ProductListCompModel extends BaseCompModel implements Serializable{

    private static final long serialVersionUID = -1;
    private String type="productList";
    private String compName = "商品列表组件";
    /*每页显示商品数*/
    private String pageShow = "16";
    /*商品详情url,例如：http://localhost/front/product/toProduct/*/
    private String productInfoUrl="";
    private String favoriteProUrl = "/usercenter/productfavorite/toPage";
    private String shareDataName ="";
    /*组件交互订阅*/
    private String topic="";

    public ProductListCompModel() {
        super(ProductListCompController.class, "/productListComp/toParamsDesign");
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPageShow() {
        return pageShow;
    }

    public void setPageShow(String pageShow) {
        this.pageShow = pageShow;
    }

    public String getProductInfoUrl() {
        return productInfoUrl;
    }

    public void setProductInfoUrl(String productInfoUrl) {
        this.productInfoUrl = productInfoUrl;
    }

    public String getShareDataName() {
        return shareDataName;
    }

    public void setShareDataName(String shareDataName) {
        this.shareDataName = shareDataName;
    }

    public String getFavoriteProUrl() {
        return favoriteProUrl;
    }

    public void setFavoriteProUrl(String favoriteProUrl) {
        this.favoriteProUrl = favoriteProUrl;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }
}
