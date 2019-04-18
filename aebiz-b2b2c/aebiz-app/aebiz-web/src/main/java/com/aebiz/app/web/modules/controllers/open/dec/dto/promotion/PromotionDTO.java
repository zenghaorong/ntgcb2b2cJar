package com.aebiz.app.web.modules.controllers.open.dec.dto.promotion;

import java.util.List;
import java.util.Map;

/**促销DTO
 *
 * Created by Aebiz_yjq on 2017/1/20.
 */
public class PromotionDTO {

    /* 促销活动的编号 */
    private String promotionUuid;

    /* 促销类型 */
    private String promotionType;

    /* 包含的促销组件优惠类型 */
    private List<String> promotionTypes;

    /* 促销名称 */
    private String promotionName;

    /* 最终购买金额 */
    private double reduceMoney;

    /* 如果是赠品促销，则将赠品的 id封装到list中 */
    private List<String> productGiftIds;

    /* 如果赠品是优惠券，则将优惠券封装好发送 ,key=优惠券类型的ID，value=优惠券的名称 */
    private Map<String, String> couponGifts;

    public PromotionDTO() {
    }

    public PromotionDTO(String promotionUuid, String promotionType, List<String> promotionTypes, String promotionName,
                        double reduceMoney, List<String> productGiftIds, Map<String, String> couponGifts) {
        this.promotionUuid = promotionUuid;
        this.promotionType = promotionType;
        this.promotionTypes = promotionTypes;
        this.promotionName = promotionName;
        this.reduceMoney = reduceMoney;
        this.productGiftIds = productGiftIds;
        this.couponGifts = couponGifts;
    }

    public String getPromotionUuid() {
        return promotionUuid;
    }

    public void setPromotionUuid(String promotionUuid) {
        this.promotionUuid = promotionUuid;
    }

    public String getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(String promotionType) {
        this.promotionType = promotionType;
    }

    public List<String> getPromotionTypes() {
        return promotionTypes;
    }

    public void setPromotionTypes(List<String> promotionTypes) {
        this.promotionTypes = promotionTypes;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public double getReduceMoney() {
        return reduceMoney;
    }

    public void setReduceMoney(double reduceMoney) {
        this.reduceMoney = reduceMoney;
    }

    public List<String> getProductGiftIds() {
        return productGiftIds;
    }

    public void setProductGiftIds(List<String> productGiftIds) {
        this.productGiftIds = productGiftIds;
    }

    public Map<String, String> getCouponGifts() {
        return couponGifts;
    }

    public void setCouponGifts(Map<String, String> couponGifts) {
        this.couponGifts = couponGifts;
    }

    @Override
    public String toString() {
        return "PromotionDTO{" +
                "promotionUuid='" + promotionUuid + '\'' +
                ", promotionType='" + promotionType + '\'' +
                ", promotionTypes=" + promotionTypes +
                ", promotionName='" + promotionName + '\'' +
                ", reduceMoney=" + reduceMoney +
                ", productGiftIds=" + productGiftIds +
                ", couponGifts=" + couponGifts +
                '}';
    }
}
