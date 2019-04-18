package com.aebiz.app.dec.commons.comps.productlist.vo;

import java.util.List;

/**
 * Created by 金辉 on 2016/12/22.
 */
public class ProductPageModel {
    /*商品list*/
    private List productList;
    /*每页显示数量*/
    private String pageShow;
    /*当前页*/
    private String nowPage;
    /*商品总数*/
    private String totalNum;

    public List getProductList() {
        return productList;
    }

    public void setProductList(List productList) {
        this.productList = productList;
    }

    public String getPageShow() {
        return pageShow;
    }

    public void setPageShow(String pageShow) {
        this.pageShow = pageShow;
    }

    public String getNowPage() {
        return nowPage;
    }

    public void setNowPage(String nowPage) {
        this.nowPage = nowPage;
    }

    public String getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }
}
