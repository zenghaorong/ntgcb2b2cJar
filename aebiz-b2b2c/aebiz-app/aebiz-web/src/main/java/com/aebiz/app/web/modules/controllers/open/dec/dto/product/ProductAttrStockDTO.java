package com.aebiz.app.web.modules.controllers.open.dec.dto.product;

import java.util.List;

/**
 *  商品库存DTO
 *
 * Created by Aebiz_yjq on 2017/1/21.
 */
public class ProductAttrStockDTO {


    /* 库存uuid */
    private String stockUuid;

    /* 商品uuid */
    private String productUuid;

    /* 业务Id*/
    private String skuNo;

    /* 组合值 */
    private String attrGroupValue;

    /* 库存 */
    private int stock;

    /* 价格 */
    private double price;

    /* SKU编号 */
    private String skuUuid;

    /* 锁定库存 */
    private int lockStock;

    /*  是否默认规格：1-是，0-不是*/
    private String specDefault;

    /*库存预警数量*/
    private int stockWarning = 0;

    /* 商品名称 */
    private String productName;

    /* 商品编号 */
    private String productNo;

    /* 属性集合 */
    private List<SpecJsonDTO> specModel;

    /* 品牌名称 */
    private String brandName;

    /* 分类名称 */
    private String categoryName;

    /* 一级分类名称 */
    private String categoryOneName;

    /* 二级分类名称 */
    private String categoryTwoName;

    /* 三级分类名称 */
    private String categoryThrowName;

    /* 属性说明 */
    private String specNote="";

    public ProductAttrStockDTO() {
    }

    public String getStockUuid() {
        return stockUuid;
    }

    public void setStockUuid(String stockUuid) {
        this.stockUuid = stockUuid;
    }

    public String getProductUuid() {
        return productUuid;
    }

    public void setProductUuid(String productUuid) {
        this.productUuid = productUuid;
    }

    public String getSkuNo() {
        return skuNo;
    }

    public void setSkuNo(String skuNo) {
        this.skuNo = skuNo;
    }

    public String getAttrGroupValue() {
        return attrGroupValue;
    }

    public void setAttrGroupValue(String attrGroupValue) {
        this.attrGroupValue = attrGroupValue;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSkuUuid() {
        return skuUuid;
    }

    public void setSkuUuid(String skuUuid) {
        this.skuUuid = skuUuid;
    }

    public int getLockStock() {
        return lockStock;
    }

    public void setLockStock(int lockStock) {
        this.lockStock = lockStock;
    }

    public String getSpecDefault() {
        return specDefault;
    }

    public void setSpecDefault(String specDefault) {
        this.specDefault = specDefault;
    }

    public int getStockWarning() {
        return stockWarning;
    }

    public void setStockWarning(int stockWarning) {
        this.stockWarning = stockWarning;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public List<SpecJsonDTO> getSpecModel() {
        return specModel;
    }

    public void setSpecModel(List<SpecJsonDTO> specModel) {
        this.specModel = specModel;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryOneName() {
        return categoryOneName;
    }

    public void setCategoryOneName(String categoryOneName) {
        this.categoryOneName = categoryOneName;
    }

    public String getCategoryTwoName() {
        return categoryTwoName;
    }

    public void setCategoryTwoName(String categoryTwoName) {
        this.categoryTwoName = categoryTwoName;
    }

    public String getCategoryThrowName() {
        return categoryThrowName;
    }

    public void setCategoryThrowName(String categoryThrowName) {
        this.categoryThrowName = categoryThrowName;
    }

    public String getSpecNote() {
        return specNote;
    }

    public void setSpecNote(String specNote) {
        this.specNote = specNote;
    }

    @Override
    public String toString() {
        return "ProductAttrStockDTO{" +
                "stockUuid='" + stockUuid + '\'' +
                ", productUuid='" + productUuid + '\'' +
                ", skuNo='" + skuNo + '\'' +
                ", attrGroupValue='" + attrGroupValue + '\'' +
                ", stock=" + stock +
                ", price=" + price +
                ", skuUuid='" + skuUuid + '\'' +
                ", lockStock=" + lockStock +
                ", specDefault='" + specDefault + '\'' +
                ", stockWarning=" + stockWarning +
                ", productName='" + productName + '\'' +
                ", productNo='" + productNo + '\'' +
                ", specModel=" + specModel +
                ", brandName='" + brandName + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", categoryOneName='" + categoryOneName + '\'' +
                ", categoryTwoName='" + categoryTwoName + '\'' +
                ", categoryThrowName='" + categoryThrowName + '\'' +
                ", specNote='" + specNote + '\'' +
                '}';
    }
}
