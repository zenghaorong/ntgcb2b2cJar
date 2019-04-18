package com.aebiz.app.dec.commons.comps.productdetail.vo;


import com.aebiz.app.web.modules.controllers.open.dec.dto.product.ProductAppraiseContentDTO;
import com.aebiz.baseframework.page.Pagination;

import java.util.List;

public class ProductDetailCommentModel {

	//评论列表
	private List<ProductAppraiseContentDTO> list;
	
	//分页信息
	private Pagination pg;
	
	//总评价数
	private int allCount;
	
	//总好评数
	private int goodCount;
	
	//总中评数
	private int middleCount;
	
	//总差评数
	private int badCount;
	
	//总晒单数
	private int picCount;
	
	//好评率
	private int goodAppraiseRate;
	
	//中评率
	private int middleAppraiseRate;
	
	//差评率
	private int badAppraiseRate;
	
	public void  computeRate(){
		int allCount = this.allCount;
		if(allCount == 0){
			return ; 
		}else{
			this.goodAppraiseRate = (int)Math.round(this.goodCount *100d / allCount);
			this.middleAppraiseRate = (int)Math.round(this.middleCount *100d / allCount);
			this.badAppraiseRate = 100 - goodAppraiseRate - middleAppraiseRate;
		}
	}
	
	
	public List<ProductAppraiseContentDTO> getList() {
		return list;
	}


	public void setList(List<ProductAppraiseContentDTO> list) {
		this.list = list;
	}


	public Pagination getPg() {
		return pg;
	}

	public void setPg(Pagination wm) {
		this.pg = wm;
	}

	public int getAllCount() {
		return allCount;
	}

	public void setAllCount(int allCount) {
		this.allCount = allCount;
	}


	public int getGoodCount() {
		return goodCount;
	}

	public void setGoodCount(int goodCount) {
		this.goodCount = goodCount;
	}

	public int getMiddleCount() {
		return middleCount;
	}

	public void setMiddleCount(int middleCount) {
		this.middleCount = middleCount;
	}

	public int getBadCount() {
		return badCount;
	}

	public void setBadCount(int badCount) {
		this.badCount = badCount;
	}

	public int getPicCount() {
		return picCount;
	}

	public void setPicCount(int picCount) {
		this.picCount = picCount;
	}


	public int getGoodAppraiseRate() {
		return goodAppraiseRate;
	}


	public void setGoodAppraiseRate(int goodAppraiseRate) {
		this.goodAppraiseRate = goodAppraiseRate;
	}


	public int getMiddleAppraiseRate() {
		return middleAppraiseRate;
	}


	public void setMiddleAppraiseRate(int middleAppraiseRate) {
		this.middleAppraiseRate = middleAppraiseRate;
	}


	public int getBadAppraiseRate() {
		return badAppraiseRate;
	}

	public void setBadAppraiseRate(int badAppraiseRate) {
		this.badAppraiseRate = badAppraiseRate;
	}
	
	
}
