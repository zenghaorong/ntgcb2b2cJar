package com.aebiz.app.dec.commons.comps.fullsearch.vo;



import com.aebiz.app.dec.commons.comps.fullsearch.FullSearchCompController;
import com.aebiz.app.dec.commons.vo.BaseCompModel;

import java.io.Serializable;

/**
 * Created by 金辉 on 2016/12/19.
 */
public class FullSearchCompModel extends BaseCompModel implements Serializable{
    private static final long serialVersionUID = -1;
    private String type="fullSearch";
    private String compName = "全文检索组件";
    /*是否有店铺搜索*/
    private int isShopSearch = 0;
    /*是否有热词*/
    private int isHaveHot=0;
    /*默认选中：0、商品 1、店铺*/
    private int defaultSelect =0;
    /*热词list*/
    private String hotWords="";
    /*商品搜索路径*/
    private String productSearchUrl="";
    /*店铺搜索路径*/
    private String storeSearchUrl="";
    /*搜索每页显示条数*/
    private String pageShow = "10";

    public static int IS_SHOP_SEARCH = 1;
    public static int NOT_SHOP_SEARCH = 0;
    public static int HAVE_HOT = 1;
    public static int NOT_HAVE_HOT = 0;
    public static int PRODUCT_SELECT=0;
    public static int SHOP_SELECT=1;
    public FullSearchCompModel() {
        super(FullSearchCompController.class,"/fullSearchComp/toParamsDesign");
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIsShopSearch() {
        return isShopSearch;
    }

    public void setIsShopSearch(int isShopSearch) {
        this.isShopSearch = isShopSearch;
    }

    public int getIsHaveHot() {
        return isHaveHot;
    }

    public void setIsHaveHot(int isHaveHot) {
        this.isHaveHot = isHaveHot;
    }

    public String getHotWords() {
        return hotWords;
    }

    public void setHotWords(String hotWords) {
        this.hotWords = hotWords;
    }

    public String getProductSearchUrl() {
        return productSearchUrl;
    }

    public void setProductSearchUrl(String productSearchUrl) {
        this.productSearchUrl = productSearchUrl;
    }

    public String getPageShow() {
        return pageShow;
    }

    public void setPageShow(String pageShow) {
        this.pageShow = pageShow;
    }

    public String getStoreSearchUrl() {
        return storeSearchUrl;
    }

    public void setStoreSearchUrl(String storeSearchUrl) {
        this.storeSearchUrl = storeSearchUrl;
    }

    public int getDefaultSelect() {
        return defaultSelect;
    }

    public void setDefaultSelect(int defaultSelect) {
        this.defaultSelect = defaultSelect;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }
}



