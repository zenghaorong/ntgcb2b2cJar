package com.aebiz.app.web.modules.controllers.open.dec.dto.product;

/**
 * 商品多角度视图DTO
 *
 * Created by Aebiz_yjq on 2017/1/21.
 */
public class ProductMainMultiImageDTO {

    /* 图片UUID  */
    private String imgUuid;

    /* 商品uuid */
    private String productUuid;

    /* 多角度原图 */
    private String basicImageUrl;

    /* 多角度视图大图 */
    private String bigImageUrl;

    /* 多角度视图中图 */
    private String centerImageUrl;

    /* 多角度视图小图 */
    private String smallImageUrl;

    /* 位置*/
    private int position;

    public ProductMainMultiImageDTO() {
    }

    public ProductMainMultiImageDTO(String imgUuid, String productUuid, String basicImageUrl, String bigImageUrl, String centerImageUrl, String smallImageUrl, int position) {
        this.imgUuid = imgUuid;
        this.productUuid = productUuid;
        this.basicImageUrl = basicImageUrl;
        this.bigImageUrl = bigImageUrl;
        this.centerImageUrl = centerImageUrl;
        this.smallImageUrl = smallImageUrl;
        this.position = position;
    }

    public String getImgUuid() {
        return imgUuid;
    }

    public void setImgUuid(String imgUuid) {
        this.imgUuid = imgUuid;
    }

    public String getProductUuid() {
        return productUuid;
    }

    public void setProductUuid(String productUuid) {
        this.productUuid = productUuid;
    }

    public String getBasicImageUrl() {
        return basicImageUrl;
    }

    public void setBasicImageUrl(String basicImageUrl) {
        this.basicImageUrl = basicImageUrl;
    }

    public String getBigImageUrl() {
        return bigImageUrl;
    }

    public void setBigImageUrl(String bigImageUrl) {
        this.bigImageUrl = bigImageUrl;
    }

    public String getCenterImageUrl() {
        return centerImageUrl;
    }

    public void setCenterImageUrl(String centerImageUrl) {
        this.centerImageUrl = centerImageUrl;
    }

    public String getSmallImageUrl() {
        return smallImageUrl;
    }

    public void setSmallImageUrl(String smallImageUrl) {
        this.smallImageUrl = smallImageUrl;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "ProductMainMultiImageDTO{" +
                "imgUuid='" + imgUuid + '\'' +
                ", productUuid='" + productUuid + '\'' +
                ", basicImageUrl='" + basicImageUrl + '\'' +
                ", bigImageUrl='" + bigImageUrl + '\'' +
                ", centerImageUrl='" + centerImageUrl + '\'' +
                ", smallImageUrl='" + smallImageUrl + '\'' +
                ", position=" + position +
                '}';
    }
}
