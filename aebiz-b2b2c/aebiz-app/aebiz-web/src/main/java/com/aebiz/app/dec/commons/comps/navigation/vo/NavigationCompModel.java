package com.aebiz.app.dec.commons.comps.navigation.vo;


import com.aebiz.app.dec.commons.comps.navigation.NavigationCompController;
import com.aebiz.app.dec.commons.vo.BaseCompModel;

import java.io.Serializable;

/**
 * Created by yewei on 2017/6/1.
 */
public class NavigationCompModel extends BaseCompModel implements Serializable{
    public static double serialVersionUID = -1;
    private String type="navigation";
    private String compName = "导航栏组件";
    /*最多显示菜单数*/
    private int showNum =10;
    /*自定义频道URL*/
    private String customUrl ="";
    public NavigationCompModel() {
        super(NavigationCompController.class,"/navigationComp/toParamsDesign");
    }

    public String getCustomUrl() {
        return customUrl;
    }

    public void setCustomUrl(String customUrl) {
        this.customUrl = customUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public int getShowNum() {
        return showNum;
    }

    public void setShowNum(int showNum) {
        this.showNum = showNum;
    }
}
