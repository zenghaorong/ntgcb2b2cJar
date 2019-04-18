package com.aebiz.app.dec.commons.comps.screen.vo;

import com.aebiz.app.dec.commons.comps.screen.ScreenCompController;
import com.aebiz.app.dec.commons.vo.BaseCompModel;

import java.io.Serializable;

/**
 * Created by 金辉 on 2016/12/21.
 */
public class ScreenCompModel extends BaseCompModel implements Serializable{
    private static final long serialVersionUID = -1;
    private String type="screen";
    private String compName="条件筛选组件";
    private String shareDataName ="";
    private String pageShow = "16";
    public ScreenCompModel() {
        super(ScreenCompController.class, "/screenComp/toParamsDesign");
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShareDataName() {
        return shareDataName;
    }

    public void setShareDataName(String shareDataName) {
        this.shareDataName = shareDataName;
    }

    public String getPageShow() {
        return pageShow;
    }

    public void setPageShow(String pageShow) {
        this.pageShow = pageShow;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }
}
