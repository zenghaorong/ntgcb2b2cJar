package com.aebiz.app.web.modules.controllers.open.dec.dto.promotion;

import java.util.List;

/**
 * 促销详细信息DTO
 *
 * Created by Aebiz_yjq on 2017/1/21.
 */
public class PromotionDetailDTO {

    /* 促销类型*/
    private String type;

    /* 促销名称*/
    private String name;

    /* 赠送类型：增优惠劵,商品*/
    private String giveType;

    /* 购买赠品*/
    private List<ProductGiftDTO> productGift;

    /* 优惠券*/
    private List<String> couponGifts;

    public PromotionDetailDTO() {
    }

    public PromotionDetailDTO(String type, String name, String giveType, List<ProductGiftDTO> productGift, List<String> couponGifts) {
        this.type = type;
        this.name = name;
        this.giveType = giveType;
        this.productGift = productGift;
        this.couponGifts = couponGifts;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGiveType() {
        return giveType;
    }

    public void setGiveType(String giveType) {
        this.giveType = giveType;
    }

    public List<ProductGiftDTO> getProductGift() {
        return productGift;
    }

    public void setProductGift(List<ProductGiftDTO> productGift) {
        this.productGift = productGift;
    }

    public List<String> getCouponGifts() {
        return couponGifts;
    }

    public void setCouponGifts(List<String> couponGifts) {
        this.couponGifts = couponGifts;
    }

    @Override
    public String toString() {
        return "PromotionDetailDTO{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", giveType='" + giveType + '\'' +
                ", productGift=" + productGift +
                ", couponGifts=" + couponGifts +
                '}';
    }
}
