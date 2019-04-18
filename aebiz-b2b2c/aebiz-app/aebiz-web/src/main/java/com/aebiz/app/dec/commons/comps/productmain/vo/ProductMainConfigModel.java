package com.aebiz.app.dec.commons.comps.productmain.vo;

import java.io.Serializable;

public class ProductMainConfigModel implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private boolean showMarketPrice;
	
	private boolean showAppraise;
	
	private boolean showBuy;
	
	private boolean showStock;
	
	private boolean needAsyncInit;

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


}
