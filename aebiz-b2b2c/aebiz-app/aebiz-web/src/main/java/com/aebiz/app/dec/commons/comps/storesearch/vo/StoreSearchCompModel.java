package com.aebiz.app.dec.commons.comps.storesearch.vo;


import com.aebiz.app.dec.commons.comps.storesearch.StoreSearchCompController;
import com.aebiz.app.dec.commons.vo.BaseCompModel;

import java.io.Serializable;

/**
 * Created by 金辉 on 2016/12/21.
 */
public class StoreSearchCompModel extends BaseCompModel implements Serializable{

    private static final long serialVersionUID = -1;
    private String type="storeSearch";
    private String compName = "店铺内搜索组件";
    /*店内搜索结果url,例如：http://shop.qwang.com.cn/dfFront/toStoreProductList?storeUuid=*/
    private String searchUrl="";

    public StoreSearchCompModel() {
        super(StoreSearchCompController.class, "/storeSearchComp/toParamsDesign");
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSearchUrl() {
        return searchUrl;
    }

    public void setSearchUrl(String searchUrl) {
        this.searchUrl = searchUrl;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }
}
