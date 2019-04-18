package com.aebiz.app.web.modules.controllers.open.dec.dto.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品组合DTO
 *
 * Created by Aebiz_yjq on 2017/1/21.
 */
public class ProductGroupApiDTO {

    /** 组合uuid 	*/
    private String groupUuid="";

    /** 组合名称*/
    private String groupName="";

    /** 组合里面的商品信息*/
    private List<Map<String, Object>> subProducts=new ArrayList<Map<String,Object>>();

    public ProductGroupApiDTO() {
    }

    public ProductGroupApiDTO(String groupUuid, String groupName, List<Map<String, Object>> subProducts) {
        this.groupUuid = groupUuid;
        this.groupName = groupName;
        this.subProducts = subProducts;
    }

    public String getGroupUuid() {
        return groupUuid;
    }

    public void setGroupUuid(String groupUuid) {
        this.groupUuid = groupUuid;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<Map<String, Object>> getSubProducts() {
        return subProducts;
    }

    public void setSubProducts(List<Map<String, Object>> subProducts) {
        this.subProducts = subProducts;
    }

    @Override
    public String toString() {
        return "ProductGroupApiDTO{" +
                "groupUuid='" + groupUuid + '\'' +
                ", groupName='" + groupName + '\'' +
                ", subProducts=" + subProducts +
                '}';
    }
}
