package com.aebiz.app.web.modules.controllers.open.dec.dto.platinfo;

/**
 * 友情链接DTO
 *
 * Created by Aebiz_yjq on 2017/1/11.
 */
public class PartnerDTO {

    /*网站名称*/
    private String siteName;

    /*网站链接*/
    private String siteLinks;

    /*打开方式*/
    private String siteTarget;

    public PartnerDTO(){}

    public PartnerDTO(String siteName, String siteLinks, String siteTarget) {
        this.siteName = siteName;
        this.siteLinks = siteLinks;
        this.siteTarget = siteTarget;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteLinks() {
        return siteLinks;
    }

    public void setSiteLinks(String siteLinks) {
        this.siteLinks = siteLinks;
    }

    public String getSiteTarget() {
        return siteTarget;
    }

    public void setSiteTarget(String siteTarget) {
        this.siteTarget = siteTarget;
    }

    @Override
    public String toString() {
        return "PartnerDTO{" +
                "siteName='" + siteName + '\'' +
                ", siteLinks='" + siteLinks + '\'' +
                ", siteDesc='" + siteTarget + '\'' +
                '}';
    }
}
