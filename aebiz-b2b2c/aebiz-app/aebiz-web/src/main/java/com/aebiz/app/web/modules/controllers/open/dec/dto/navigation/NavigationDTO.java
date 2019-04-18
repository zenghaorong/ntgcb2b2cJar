package com.aebiz.app.web.modules.controllers.open.dec.dto.navigation;

/**
 * Created by yewei on 2017/6/1.
 */
public class NavigationDTO {

    /* 频道名称 */
    private String channelName;

    /* 链接地址 */
    private String channelUrl;

    /* 所在位置 */
    private Integer position;

    /* 所属页面 */
    private String pageUuid;

    public NavigationDTO() {
    }

    public NavigationDTO(String channelName, String channelUrl, Integer position, String pageUuid) {
        this.channelName = channelName;
        this.channelUrl = channelUrl;
        this.position = position;
        this.pageUuid = pageUuid;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelUrl() {
        return channelUrl;
    }

    public void setChannelUrl(String channelUrl) {
        this.channelUrl = channelUrl;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getPageUuid() {
        return pageUuid;
    }

    public void setPageUuid(String pageUuid) {
        this.pageUuid = pageUuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NavigationDTO that = (NavigationDTO) o;

        if (channelName != null ? !channelName.equals(that.channelName) : that.channelName != null) return false;
        if (channelUrl != null ? !channelUrl.equals(that.channelUrl) : that.channelUrl != null) return false;
        if (position != null ? !position.equals(that.position) : that.position != null) return false;
        return pageUuid != null ? pageUuid.equals(that.pageUuid) : that.pageUuid == null;

    }

    @Override
    public int hashCode() {
        int result = channelName != null ? channelName.hashCode() : 0;
        result = 31 * result + (channelUrl != null ? channelUrl.hashCode() : 0);
        result = 31 * result + (position != null ? position.hashCode() : 0);
        result = 31 * result + (pageUuid != null ? pageUuid.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ChannelDTO{" +
                "channelName='" + channelName + '\'' +
                ", channelUrl='" + channelUrl + '\'' +
                ", position=" + position +
                ", pageUuid='" + pageUuid + '\'' +
                '}';
    }
}
