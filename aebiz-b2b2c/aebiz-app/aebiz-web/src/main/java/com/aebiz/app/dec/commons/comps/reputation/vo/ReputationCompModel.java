package com.aebiz.app.dec.commons.comps.reputation.vo;


import com.aebiz.app.dec.commons.comps.reputation.ReputationController;
import com.aebiz.app.dec.commons.vo.BaseCompModel;

public class ReputationCompModel extends BaseCompModel {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReputationCompModel() {
		super(ReputationController.class,"/reputationComp/toParamsDesign");

	}

	private String compName = "店铺信誉";
	private String compType = "reputation";
	
	
	
	
	/**
	 * 标签名称
	 */
	private String lableName="信誉：";
	
	
	
	

	/**
	 * 店铺名称
	 */
	private String shopName="";
	
	
	/**
	 * 总得分
	 */
	private String scoreTotal="";
	
	
	/**
	 * 店铺uuid
	 */
	private String shopUuid="";
	
	
	/**
	 * 店铺logo
	 */
	private String shopUrl="";
	
	/**
	 * 店铺QQ
	 */
	private String shopQQ="";
	
	
	/**
	 * 我收藏的店铺链接地址
	 */
	private String collectShopUrl="";
	
	/**
	 * 发布主题 和 登录关联
	 */
	private String topic;

	/*物流速度评分*/
	private double logisticSpeedScore;

	/*服务态度评分*/
	private double serviceAttitudeScore;

	/*商品描述评分*/
	private double prodDescScore;

	public double getLogisticSpeedScore() {
		return logisticSpeedScore;
	}

	public void setLogisticSpeedScore(double logisticSpeedScore) {
		this.logisticSpeedScore = logisticSpeedScore;
	}

	public double getServiceAttitudeScore() {
		return serviceAttitudeScore;
	}

	public void setServiceAttitudeScore(double serviceAttitudeScore) {
		this.serviceAttitudeScore = serviceAttitudeScore;
	}

	public double getProdDescScore() {
		return prodDescScore;
	}

	public void setProdDescScore(double prodDescScore) {
		this.prodDescScore = prodDescScore;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getCollectShopUrl() {
		return collectShopUrl;
	}

	public void setCollectShopUrl(String collectShopUrl) {
		this.collectShopUrl = collectShopUrl;
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

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}



	public String getShopUuid() {
		return shopUuid;
	}

	public void setShopUuid(String shopUuid) {
		this.shopUuid = shopUuid;
	}

	public String getShopUrl() {
		return shopUrl;
	}

	public void setShopUrl(String shopUrl) {
		this.shopUrl = shopUrl;
	}

	public String getShopQQ() {
		return shopQQ;
	}

	public void setShopQQ(String shopQQ) {
		this.shopQQ = shopQQ;
	}


	public String getLableName() {
		return lableName;
	}

	public void setLableName(String lableName) {
		this.lableName = lableName;
	}

	public String getScoreTotal() {
		return scoreTotal;
	}

	public void setScoreTotal(String scoreTotal) {
		this.scoreTotal = scoreTotal;
	}
	
	

	
	


	


}
