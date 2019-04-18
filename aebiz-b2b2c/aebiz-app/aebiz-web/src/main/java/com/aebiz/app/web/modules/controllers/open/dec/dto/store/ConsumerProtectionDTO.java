package com.aebiz.app.web.modules.controllers.open.dec.dto.store;

/**
 * 用户权益DTO
 *
 * Created by Aebiz_yjq on 2017/1/20.
 */
public class ConsumerProtectionDTO {

    /* 权益uuid */
    private String uuid;

    /* 权益名称 */
    private String name;

    /* 权益图标 */
    private String iconUrl;

    /* 是否需要选择商品 */
    private String needChooseProduct;

    /* 是否需要同意协议 */
    private String needAgreeProtocol;

    /* 协议内容 */
    private String protocolContent;

    /* 备注 */
    private String note;

    /* 状态 */
    private String state;

    /* 商品的数量 */
    private int relProductNumbers;

    public ConsumerProtectionDTO() {
    }

    public ConsumerProtectionDTO(String uuid, String name, String iconUrl, String needChooseProduct,
                                 String needAgreeProtocol, String protocolContent, String note, String state, int relProductNumbers) {
        this.uuid = uuid;
        this.name = name;
        this.iconUrl = iconUrl;
        this.needChooseProduct = needChooseProduct;
        this.needAgreeProtocol = needAgreeProtocol;
        this.protocolContent = protocolContent;
        this.note = note;
        this.state = state;
        this.relProductNumbers = relProductNumbers;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getNeedChooseProduct() {
        return needChooseProduct;
    }

    public void setNeedChooseProduct(String needChooseProduct) {
        this.needChooseProduct = needChooseProduct;
    }

    public String getNeedAgreeProtocol() {
        return needAgreeProtocol;
    }

    public void setNeedAgreeProtocol(String needAgreeProtocol) {
        this.needAgreeProtocol = needAgreeProtocol;
    }

    public String getProtocolContent() {
        return protocolContent;
    }

    public void setProtocolContent(String protocolContent) {
        this.protocolContent = protocolContent;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getRelProductNumbers() {
        return relProductNumbers;
    }

    public void setRelProductNumbers(int relProductNumbers) {
        this.relProductNumbers = relProductNumbers;
    }

    @Override
    public String toString() {
        return "ConsumerProtectionDTO{" +
                "uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", needChooseProduct='" + needChooseProduct + '\'' +
                ", needAgreeProtocol='" + needAgreeProtocol + '\'' +
                ", protocolContent='" + protocolContent + '\'' +
                ", note='" + note + '\'' +
                ", state='" + state + '\'' +
                ", relProductNumbers=" + relProductNumbers +
                '}';
    }
}
