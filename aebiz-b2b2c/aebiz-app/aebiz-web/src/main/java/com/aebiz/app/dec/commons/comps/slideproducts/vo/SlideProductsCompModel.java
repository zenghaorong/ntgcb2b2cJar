package com.aebiz.app.dec.commons.comps.slideproducts.vo;


import com.aebiz.app.dec.commons.comps.slideproducts.SlideProductsCompController;
import com.aebiz.app.dec.commons.vo.BaseCompModel;

/**
 * 首页轮播商品组件
 *
 * Created by Aebiz_yjq on 2016/12/28.
 */
public class SlideProductsCompModel extends BaseCompModel {

    private static final long serialVersionUID = -7930744746642845402L;
    /*返回关联的商品的uuid*/
    private String productUuids="";
    
    //商品详情页URL
    private String productDetailUrl = "";
    
    //轮播 缓动效果速度
    private int slideSpeed = 300;
    
    //是否自动轮播
    private boolean slideAuto = false;
    
    //每行商品数目
    private int slideCount = 4;
    
    //自动轮播间隔时间
    private int interTime = 5000;
    
    //登陆验证发布的topic
    private String loginTopic = "";
    
    //收藏夹路径
    private String collectUrl = "";


    public SlideProductsCompModel() {
        super(SlideProductsCompController.class,"/slideproducts/toParamsDesign");
    }

    public SlideProductsCompModel(Class compControllerClass, String urlToParamsJsp) {
        super(compControllerClass, urlToParamsJsp);
    }

	public String getProductUuids() {
		return productUuids;
	}

	public void setProductUuids(String productUuids) {
		this.productUuids = productUuids;
	}

	public String getProductDetailUrl() {
		return productDetailUrl;
	}

	public void setProductDetailUrl(String productDetailUrl) {
		this.productDetailUrl = productDetailUrl;
	}

	public int getSlideSpeed() {
		return slideSpeed;
	}

	public void setSlideSpeed(int slideSpeed) {
		this.slideSpeed = slideSpeed;
	}

	public boolean isSlideAuto() {
		return slideAuto;
	}

	public void setSlideAuto(boolean slideAuto) {
		this.slideAuto = slideAuto;
	}

	public int getSlideCount() {
		return slideCount;
	}

	public void setSlideCount(int slideCount) {
		this.slideCount = slideCount;
	}

	public int getInterTime() {
		return interTime;
	}

	public void setInterTime(int interTime) {
		this.interTime = interTime;
	}

	public String getLoginTopic() {
		return loginTopic;
	}

	public void setLoginTopic(String loginTopic) {
		this.loginTopic = loginTopic;
	}

	public String getCollectUrl() {
		return collectUrl;
	}

	public void setCollectUrl(String collectUrl) {
		this.collectUrl = collectUrl;
	}
	
	
	

}
