package com.aebiz.app.web.modules.controllers.open.dec.dto.product;

import com.aebiz.app.dec.commons.vo.Datatable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品评价信息DTO
 *
 * Created by Aebiz_yjq on 2017/1/17.
 */
@Component
public class ProductAppraiseContentDTO {

    /* 评分 */
    private int score = 0;

    /* 用户头像 */
    private String customerImg = "";

    /* 用户名称 */
    private String customerName = "";

    /* 评价时间 */
    private String appraiseTime = "";

    /*  评价内容 */
    private String appraiseContent = "";

    /* 商品skuuuid */
    private String productSpecUuid = "";

    /* 晒单图片链接 */
    private List<String> picList=new ArrayList<>();

    /*买家所买商品属性和属性值集合*/
    private List<Datatable> spec=new ArrayList<>();

    public ProductAppraiseContentDTO() {
    }

    public ProductAppraiseContentDTO(int score, String customerImg, String customerName, String appraiseTime,
                                     String appraiseContent, String productSpecUuid, List<String> picList) {
        this.score = score;
        this.customerImg = customerImg;
        this.customerName = customerName;
        this.appraiseTime = appraiseTime;
        this.appraiseContent = appraiseContent;
        this.productSpecUuid = productSpecUuid;
        this.picList = picList;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getCustomerImg() {
        return customerImg;
    }

    public void setCustomerImg(String customerImg) {
        this.customerImg = customerImg;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAppraiseTime() {
        return appraiseTime;
    }

    public void setAppraiseTime(String appraiseTime) {
        this.appraiseTime = appraiseTime;
    }

    public String getAppraiseContent() {
        return appraiseContent;
    }

    public void setAppraiseContent(String appraiseContent) {
        this.appraiseContent = appraiseContent;
    }

    public String getProductSpecUuid() {
        return productSpecUuid;
    }

    public void setProductSpecUuid(String productSpecUuid) {
        this.productSpecUuid = productSpecUuid;
    }

    public List<String> getPicList() {
        return picList;
    }

    public void setPicList(List<String> picList) {
        this.picList = picList;
    }

    public void setSpec(List<Datatable> spec) {
        this.spec = spec;
    }
    
    

    public List<Datatable> getSpec() {
		return spec;
	}

	@Override
    public String toString() {
        return "ProductAppraiseContentDTO{" +
                "score=" + score +
                ", customerImg='" + customerImg + '\'' +
                ", customerName='" + customerName + '\'' +
                ", appraiseTime='" + appraiseTime + '\'' +
                ", appraiseContent='" + appraiseContent + '\'' +
                ", productSpecUuid='" + productSpecUuid + '\'' +
                ", picList=" + picList +
                ", spec=" + spec +
                '}';
    }
}
