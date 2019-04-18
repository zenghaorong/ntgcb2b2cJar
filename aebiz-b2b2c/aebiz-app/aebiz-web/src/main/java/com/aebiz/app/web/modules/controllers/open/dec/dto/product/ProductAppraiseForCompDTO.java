package com.aebiz.app.web.modules.controllers.open.dec.dto.product;


import com.aebiz.baseframework.page.datatable.DataTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * product appraise for components
 *
 * Created by Aebiz_yjq on 2017/1/21.
 */
public class ProductAppraiseForCompDTO {

    /*商品评论对象*/
    private ProductAppraiseDTO productAppraise;

    /* list of order show picture  */
    private List<OrderShowPicDTO> picList;

    /*买家所买商品属性，属性值uuid*/
    private String specUuid="";

    /*买家所买商品属性和属性值集合*/
    private List<DataTable> spec=new ArrayList<DataTable>();

    /*评价标签集合*/
    private String[] appTags=new String[]{};

    /*会员名*/
    private String customerName="";

    /*会员图像*/
    private String customerImage="";

    /*会员等级图像*/
    private String customerLevelImage="";

    private String customerLevelName="";

    public ProductAppraiseForCompDTO() {
    }

    public ProductAppraiseDTO getProductAppraise() {
        return productAppraise;
    }

    public void setProductAppraise(ProductAppraiseDTO productAppraise) {
        this.productAppraise = productAppraise;
    }

    public List<OrderShowPicDTO> getPicList() {
        return picList;
    }

    public void setPicList(List<OrderShowPicDTO> picList) {
        this.picList = picList;
    }

    public String getSpecUuid() {
        return specUuid;
    }

    public void setSpecUuid(String specUuid) {
        this.specUuid = specUuid;
    }

    public List<DataTable> getSpec() {
        return spec;
    }

    public void setSpec(List<DataTable> spec) {
        this.spec = spec;
    }

    public String[] getAppTags() {
        return appTags;
    }

    public void setAppTags(String[] appTags) {
        this.appTags = appTags;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerImage() {
        return customerImage;
    }

    public void setCustomerImage(String customerImage) {
        this.customerImage = customerImage;
    }

    public String getCustomerLevelImage() {
        return customerLevelImage;
    }

    public void setCustomerLevelImage(String customerLevelImage) {
        this.customerLevelImage = customerLevelImage;
    }

    public String getCustomerLevelName() {
        return customerLevelName;
    }

    public void setCustomerLevelName(String customerLevelName) {
        this.customerLevelName = customerLevelName;
    }

    @Override
    public String toString() {
        return "ProductAppraiseForCompDTO{" +
                "productAppraise=" + productAppraise +
                ", picList=" + picList +
                ", specUuid='" + specUuid + '\'' +
                ", spec=" + spec +
                ", appTags=" + Arrays.toString(appTags) +
                ", customerName='" + customerName + '\'' +
                ", customerImage='" + customerImage + '\'' +
                ", customerLevelImage='" + customerLevelImage + '\'' +
                ", customerLevelName='" + customerLevelName + '\'' +
                '}';
    }
}
