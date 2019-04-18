package com.aebiz.app.dec.commons.comps.bottomHelpCenter.vo;

import com.aebiz.app.dec.commons.vo.BaseCompModel;
import com.aebiz.app.dec.commons.comps.bottomHelpCenter.BottomHelpCenterCompController;

import java.io.Serializable;

/**
 * Created by yewei on 2017/5/9.
 */
public class BottomHelpCenterCompModel extends BaseCompModel implements Serializable {
    public BottomHelpCenterCompModel() {
        super(BottomHelpCenterCompController.class,"/bottomHelpCenterComp/toParamsDesign");
    }

    private static final long serialVersionUID = -2679137641785569098L;
    //组件名称
    private String compName = "底部帮助中心组件";
    //组件类型
    private String compType = "bottomHelpCenter";
    //文章内容详情
    private String contentUrl="";
    //栏目id
    private String channelId="";

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getCompType() {
        return compType;
    }

    public void setCompType(String compType) {
        this.compType = compType;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
}
