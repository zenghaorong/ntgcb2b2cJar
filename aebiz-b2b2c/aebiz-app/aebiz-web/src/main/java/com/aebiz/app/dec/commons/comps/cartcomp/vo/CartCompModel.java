package com.aebiz.app.dec.commons.comps.cartcomp.vo;



import com.aebiz.app.dec.commons.comps.cartcomp.CartCompController;
import com.aebiz.app.dec.commons.vo.BaseCompModel;

import java.io.Serializable;

/**
 * 购物车组件配置model
 * Created by 金辉 on 2016/12/12.
 */
public class CartCompModel extends BaseCompModel implements Serializable{

    private static final long serialVersionUID = -1;
    private String type = "cartComp";
    private String compName = "购物车组件";
    /*是否使用简易模式，默认为true*/
    private int isEasyMode=0;
    /*图标路径*/
    private String imgUrl="";
    /*图片uuid*/
    private String imgUuid="";
    /*跳往购物车路径*/
    private String cartShopUrl ="";
    private String subOrderUrl="";

    /*商品跳转路径*/
    private String productUrl="";
    //订阅主题，组件交互使用
    private String topic="";
    //发布登录主题
    private String loginTopic="";

    //简易模式
    public static int IS_EASY_MODE=0;
    //非简易模式
    public static int NOT_EASY_MODE=1;

    public CartCompModel() {
        super(CartCompController.class,"/cartComp/toParamsDesign");
    }

    public int getIsEasyMode() {
        return isEasyMode;
    }

    public void setIsEasyMode(int isEasyMode) {
        this.isEasyMode = isEasyMode;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    public String getCartShopUrl() {
        return cartShopUrl;
    }

    public void setCartShopUrl(String cartShopUrl) {
        this.cartShopUrl = cartShopUrl;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }


    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getImgUuid() {
        return imgUuid;
    }

    public void setImgUuid(String imgUuid) {
        this.imgUuid = imgUuid;
    }

    public String getLoginTopic() {
        return loginTopic;
    }

    public void setLoginTopic(String loginTopic) {
        this.loginTopic = loginTopic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getSubOrderUrl() {
        return subOrderUrl;
    }

    public void setSubOrderUrl(String subOrderUrl) {
        this.subOrderUrl = subOrderUrl;
    }
}
