package com.aebiz.app.dec.commons.comps.productmain.vo;


import com.aebiz.app.dec.commons.comps.productmain.ProductMainController;
import com.aebiz.app.dec.commons.vo.BaseCompModel;

public class ProductMainCompModel extends BaseCompModel {
	private static final long serialVersionUID = 1L;
	
	private String compName = "商品主信息";
	
	private String compType = "productmain";
	
	private String productUuid = "";
	
	private boolean showMarketPrice;
	
	private boolean showAppraise;
	
	private boolean showBuy;
	
	private boolean showStock;
	
	private boolean needAsyncInit;
	
	/**加入购物车发布主题**/
	private String cartEventTopic = "";
	
	/**登陆发布主题**/
	private String loginTopic = "";
	
	/**商品详情页连接，促销活动赠品 需要链接到商品详情页面**/
	private String productDetailUrl = "";
	
	public ProductMainCompModel() {
		super(ProductMainController.class,"/productmain/toParamsDesign");
	}

	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	public String getCompType() {
		return compType;
	}

	public void setCompType(String compType) {
		this.compType = compType;
	}


	public String getProductUuid() {
		return productUuid;
	}

	public void setProductUuid(String productUuid) {
		this.productUuid = productUuid;
	}

	public boolean isShowMarketPrice() {
		return showMarketPrice;
	}

	public void setShowMarketPrice(boolean showMarketPrice) {
		this.showMarketPrice = showMarketPrice;
	}

	public boolean isShowAppraise() {
		return showAppraise;
	}

	public void setShowAppraise(boolean showAppraise) {
		this.showAppraise = showAppraise;
	}

	public boolean isShowBuy() {
		return showBuy;
	}

	public void setShowBuy(boolean showBuy) {
		this.showBuy = showBuy;
	}

	public boolean isShowStock() {
		return showStock;
	}

	public void setShowStock(boolean showStock) {
		this.showStock = showStock;
	}

	public boolean isNeedAsyncInit() {
		return needAsyncInit;
	}

	public void setNeedAsyncInit(boolean needAsyncInit) {
		this.needAsyncInit = needAsyncInit;
	}

	public String getCartEventTopic() {
		return cartEventTopic;
	}

	public void setCartEventTopic(String cartEventTopic) {
		this.cartEventTopic = cartEventTopic;
	}

	public String getLoginTopic() {
		return loginTopic;
	}

	public void setLoginTopic(String loginTopic) {
		this.loginTopic = loginTopic;
	}

	public String getProductDetailUrl() {
		return productDetailUrl;
	}

	public void setProductDetailUrl(String productDetailUrl) {
		this.productDetailUrl = productDetailUrl;
	}
	
}
