package com.aebiz.app.shop.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 商城配置信息表
 * Created by wizzer on 2017/3/27.
 */
@Table("shop_config")
public class Shop_config extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column
    @Name
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Default("system")
    private String id;

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 255)
    @Comment("店铺名称")
    private String shopName;

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 255)
    @Comment("店铺Logo")
    private String shopLogo;

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 255)
    @Comment("登陆页Logo")
    private String shopLoginLogo;

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 255)
    @Comment("商城所有人")
    private String shopLinkCompany;

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 255)
    @Comment("商城联系人")
    private String shopLinkName;

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 255)
    @Comment("联系电话")
    private String shopLinkPhone;

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 255)
    @Comment("手机号码")
    private String shopLinkMobile;

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 255)
    @Comment("电子邮箱")
    private String shopLinkEmail;

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 255)
    @Comment("QQ")
    private String shopLinkQQ;

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 255)
    @Comment("微信二维码")
    private String shopLinkQrcode;

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 255)
    @Comment("官方微博地址")
    private String shopLinkWeibo;

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 255)
    @Comment("联系地址")
    private String shopLinkAddress;

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 255)
    @Comment("邮政编码")
    private String shopLinkPostcode;

    @Column
    @ColDefine(type = ColType.VARCHAR, width = 10)
    @Comment("会员登录方式")
    private String memberLoginType;//self--页面跳转  open--弹出框

    @Column
    @ColDefine(type = ColType.BOOLEAN)
    @Comment("会员登录需要验证码")
    private boolean memberLoginCaptcha;

    @Column
    @ColDefine(type = ColType.BOOLEAN)
    @Comment("会员注册需要验证码")
    private boolean memberRegisterCaptcha;

    @Column
    @ColDefine(type = ColType.BOOLEAN)
    @Comment("会员发送短信需要验证码")
    private boolean memberSmsCaptcha;

    @Column
    @ColDefine(type = ColType.BOOLEAN)
    @Comment("会员只允许通过手机号注册")
    private boolean memberRegisterOnlyMobile;

    @Column
    @ColDefine(type = ColType.INT)
    @Comment("积分计算方式")
    @Default("0")
    private Integer scoreMethod;//0--不使用积分 1--按订单商品总价格

    @Column
    @ColDefine(type = ColType.INT)
    @Comment("积分换算比率")
    @Default("100")
    private Integer scoreRate;//默认 100%

    @Column
    @ColDefine(type = ColType.BOOLEAN)
    @Comment("是否启用签到送积分")
    private boolean scoreSignin;

    @Column
    @ColDefine(type = ColType.INT)
    @Comment("每次签到送的积分值")
    @Default("5")
    private Integer scoreSigninValue;

    @Column
    @ColDefine(type = ColType.BOOLEAN)
    @Comment("积分过期时间")
    private boolean scoreExpire;

    @Column
    @ColDefine(type = ColType.INT)
    @Comment("积分过期时间")
    @Default("0")
    private Integer scoreExpireType;// 0--设置结束时间  1--设置过期天数

    @Column
    @ColDefine(type = ColType.VARCHAR,width = 10)
    @Comment("积分过期时间")
    @Default("0")
    private String scoreExpireTime;//scoreExpireType=0 填年月日  scoreExpireType=1 填写天数

    @Column
    @ColDefine(type = ColType.INT)
    @Comment("积分用途")
    @Default("0")
    private Integer scoreUsage;//0--只用于兑换  1--只用于抵扣

    @Column
    @ColDefine(type = ColType.INT)
    @Comment("商品价格进位方式")
    @Default("0")
    private Integer saleMoneyType;//0--四舍五入  1--向下取整  2--向上取整

    @Column
    @ColDefine(type = ColType.BOOLEAN)
    @Comment("邮编是否必填")
    private boolean salePostcode;

    @Column
    @ColDefine(type = ColType.BOOLEAN)
    @Comment("是否启用发票")
    private boolean saleInvoice;

    @Column
    @ColDefine(type = ColType.BOOLEAN)
    @Comment("是否开启配送时间")
    private boolean saleReceiveMore;

    @Column
    @ColDefine(type = ColType.BOOLEAN)
    @Comment("是否启用指定日期")
    private boolean saleReceiveDate;

    @Column
    @ColDefine(type = ColType.BOOLEAN)
    @Comment("是否评论需审核")
    private boolean saleCommentModeration;

    @Column
    @ColDefine(type = ColType.BOOLEAN)
    @Comment("是否开启夜间配送")
    private boolean saleReceiveNght;//设置为否时，订单将不会在夜晚配送（18:00~22:00）

    @Column
    @ColDefine(type = ColType.INT)
    @Comment("接单后最短配送所需时间(单位:小时)")
    @Default("5")
    private Integer saleReceiveTime;

    @Column
    @ColDefine(type = ColType.BOOLEAN)
    @Comment("是否开启自动取消订单")
    private boolean saleAutoCancelOrder;

    @Column
    @ColDefine(type = ColType.BOOLEAN)
    @Comment("重新下单是否恢复原价")
    private boolean resumePrice;

    @Column
    @ColDefine(type = ColType.INT)
    @Comment("自动取消订单支付时间(单位:分钟)")
    @Default("30")
    private Integer saleAutoCancelTime;//订单创建后多少分钟不支付，则自动取消

    @Column
    @ColDefine(type = ColType.BOOLEAN)
    @Comment("商品发布是否需平台审核")
    private boolean goodsNeedCheck;

    @Column
    @ColDefine(type = ColType.VARCHAR,width = 255)
    @Comment("底部信息配置")
    private String bottomInfoConfig;

    @Column
    @ColDefine(type = ColType.BOOLEAN)
    @Comment("送货人")
    private boolean deliveryName;

    @Column
    @ColDefine(type = ColType.BOOLEAN)
    @Comment("送货省")
    private boolean deliveryProvince;

    @Column
    @ColDefine(type = ColType.BOOLEAN)
    @Comment("送货市")
    private boolean deliveryCity;

    @Column
    @ColDefine(type = ColType.BOOLEAN)
    @Comment("送货县区")
    private boolean deliveryCounty;

    @Column
    @ColDefine(type = ColType.BOOLEAN)
    @Comment("送货街道")
    private boolean deliveryTown;

    @Column
    @ColDefine(type = ColType.BOOLEAN)
    @Comment("送货地址")
    private boolean deliveryAddress;

    @Column
    @ColDefine(type = ColType.BOOLEAN)
    @Comment("送货手机号")
    private boolean deliveryMobile;

    @Column
    @ColDefine(type = ColType.BOOLEAN)
    @Comment("送货电话")
    private boolean deliveryPhone;

    @Column
    @ColDefine(type = ColType.BOOLEAN)
    @Comment("邮政编码")
    private boolean deliveryPostcode;

    @Column
    @ColDefine(type = ColType.TEXT)
    @Comment("注册协议配置")
    private String agreement;

    @Column
    @ColDefine(type = ColType.TEXT)
    @Comment("隐私政策配置")
    private String secret;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopLogo() {
        return shopLogo;
    }

    public void setShopLogo(String shopLogo) {
        this.shopLogo = shopLogo;
    }

    public String getShopLoginLogo() {
        return shopLoginLogo;
    }

    public void setShopLoginLogo(String shopLoginLogo) {
        this.shopLoginLogo = shopLoginLogo;
    }

    public String getShopLinkCompany() {
        return shopLinkCompany;
    }

    public void setShopLinkCompany(String shopLinkCompany) {
        this.shopLinkCompany = shopLinkCompany;
    }

    public String getShopLinkName() {
        return shopLinkName;
    }

    public void setShopLinkName(String shopLinkName) {
        this.shopLinkName = shopLinkName;
    }

    public String getShopLinkPhone() {
        return shopLinkPhone;
    }

    public void setShopLinkPhone(String shopLinkPhone) {
        this.shopLinkPhone = shopLinkPhone;
    }

    public String getShopLinkMobile() {
        return shopLinkMobile;
    }

    public void setShopLinkMobile(String shopLinkMobile) {
        this.shopLinkMobile = shopLinkMobile;
    }

    public String getShopLinkEmail() {
        return shopLinkEmail;
    }

    public void setShopLinkEmail(String shopLinkEmail) {
        this.shopLinkEmail = shopLinkEmail;
    }

    public String getShopLinkQQ() {
        return shopLinkQQ;
    }

    public void setShopLinkQQ(String shopLinkQQ) {
        this.shopLinkQQ = shopLinkQQ;
    }

    public String getShopLinkQrcode() {
        return shopLinkQrcode;
    }

    public void setShopLinkQrcode(String shopLinkQrcode) {
        this.shopLinkQrcode = shopLinkQrcode;
    }

    public String getShopLinkWeibo() {
        return shopLinkWeibo;
    }

    public void setShopLinkWeibo(String shopLinkWeibo) {
        this.shopLinkWeibo = shopLinkWeibo;
    }

    public String getShopLinkAddress() {
        return shopLinkAddress;
    }

    public void setShopLinkAddress(String shopLinkAddress) {
        this.shopLinkAddress = shopLinkAddress;
    }

    public String getShopLinkPostcode() {
        return shopLinkPostcode;
    }

    public void setShopLinkPostcode(String shopLinkPostcode) {
        this.shopLinkPostcode = shopLinkPostcode;
    }

    public String getMemberLoginType() {
        return memberLoginType;
    }

    public void setMemberLoginType(String memberLoginType) {
        this.memberLoginType = memberLoginType;
    }

    public boolean isMemberLoginCaptcha() {
        return memberLoginCaptcha;
    }

    public void setMemberLoginCaptcha(boolean memberLoginCaptcha) {
        this.memberLoginCaptcha = memberLoginCaptcha;
    }

    public boolean isMemberRegisterCaptcha() {
        return memberRegisterCaptcha;
    }

    public void setMemberRegisterCaptcha(boolean memberRegisterCaptcha) {
        this.memberRegisterCaptcha = memberRegisterCaptcha;
    }

    public boolean isMemberSmsCaptcha() {
        return memberSmsCaptcha;
    }

    public void setMemberSmsCaptcha(boolean memberSmsCaptcha) {
        this.memberSmsCaptcha = memberSmsCaptcha;
    }

    public boolean isMemberRegisterOnlyMobile() {
        return memberRegisterOnlyMobile;
    }

    public void setMemberRegisterOnlyMobile(boolean memberRegisterOnlyMobile) {
        this.memberRegisterOnlyMobile = memberRegisterOnlyMobile;
    }

    public Integer getScoreMethod() {
        return scoreMethod;
    }

    public void setScoreMethod(Integer scoreMethod) {
        this.scoreMethod = scoreMethod;
    }

    public Integer getScoreRate() {
        return scoreRate;
    }

    public void setScoreRate(Integer scoreRate) {
        this.scoreRate = scoreRate;
    }

    public boolean isScoreSignin() {
        return scoreSignin;
    }

    public void setScoreSignin(boolean scoreSignin) {
        this.scoreSignin = scoreSignin;
    }

    public Integer getScoreSigninValue() {
        return scoreSigninValue;
    }

    public void setScoreSigninValue(Integer scoreSigninValue) {
        this.scoreSigninValue = scoreSigninValue;
    }

    public boolean isScoreExpire() {
        return scoreExpire;
    }

    public void setScoreExpire(boolean scoreExpire) {
        this.scoreExpire = scoreExpire;
    }

    public Integer getScoreExpireType() {
        return scoreExpireType;
    }

    public void setScoreExpireType(Integer scoreExpireType) {
        this.scoreExpireType = scoreExpireType;
    }

    public String getScoreExpireTime() {
        return scoreExpireTime;
    }

    public void setScoreExpireTime(String scoreExpireTime) {
        this.scoreExpireTime = scoreExpireTime;
    }

    public Integer getScoreUsage() {
        return scoreUsage;
    }

    public void setScoreUsage(Integer scoreUsage) {
        this.scoreUsage = scoreUsage;
    }

    public Integer getSaleMoneyType() {
        return saleMoneyType;
    }

    public void setSaleMoneyType(Integer saleMoneyType) {
        this.saleMoneyType = saleMoneyType;
    }

    public boolean isSalePostcode() {
        return salePostcode;
    }

    public void setSalePostcode(boolean salePostcode) {
        this.salePostcode = salePostcode;
    }

    public boolean isSaleInvoice() {
        return saleInvoice;
    }

    public void setSaleInvoice(boolean saleInvoice) {
        this.saleInvoice = saleInvoice;
    }

    public boolean isSaleReceiveMore() {
        return saleReceiveMore;
    }

    public void setSaleReceiveMore(boolean saleReceiveMore) {
        this.saleReceiveMore = saleReceiveMore;
    }

    public boolean isSaleReceiveDate() {
        return saleReceiveDate;
    }

    public void setSaleReceiveDate(boolean saleReceiveDate) {
        this.saleReceiveDate = saleReceiveDate;
    }

    public boolean isSaleCommentModeration() {
        return saleCommentModeration;
    }

    public void setSaleCommentModeration(boolean saleCommentModeration) {
        this.saleCommentModeration = saleCommentModeration;
    }

    public boolean isSaleReceiveNght() {
        return saleReceiveNght;
    }

    public void setSaleReceiveNght(boolean saleReceiveNght) {
        this.saleReceiveNght = saleReceiveNght;
    }

    public Integer getSaleReceiveTime() {
        return saleReceiveTime;
    }

    public void setSaleReceiveTime(Integer saleReceiveTime) {
        this.saleReceiveTime = saleReceiveTime;
    }

    public boolean isSaleAutoCancelOrder() {
        return saleAutoCancelOrder;
    }

    public void setSaleAutoCancelOrder(boolean saleAutoCancelOrder) {
        this.saleAutoCancelOrder = saleAutoCancelOrder;
    }

    public boolean isResumePrice() {
        return resumePrice;
    }

    public void setResumePrice(boolean resumePrice) {
        this.resumePrice = resumePrice;
    }

    public Integer getSaleAutoCancelTime() {
        return saleAutoCancelTime;
    }

    public void setSaleAutoCancelTime(Integer saleAutoCancelTime) {
        this.saleAutoCancelTime = saleAutoCancelTime;
    }

    public boolean isGoodsNeedCheck() {
        return goodsNeedCheck;
    }

    public void setGoodsNeedCheck(boolean goodsNeedCheck) {
        this.goodsNeedCheck = goodsNeedCheck;
    }

    public String getBottomInfoConfig() {
        return bottomInfoConfig;
    }

    public void setBottomInfoConfig(String bottomInfoConfig) {
        this.bottomInfoConfig = bottomInfoConfig;
    }

    public boolean isDeliveryName() {
        return deliveryName;
    }

    public void setDeliveryName(boolean deliveryName) {
        this.deliveryName = deliveryName;
    }

    public boolean isDeliveryProvince() {
        return deliveryProvince;
    }

    public void setDeliveryProvince(boolean deliveryProvince) {
        this.deliveryProvince = deliveryProvince;
    }

    public boolean isDeliveryCity() {
        return deliveryCity;
    }

    public void setDeliveryCity(boolean deliveryCity) {
        this.deliveryCity = deliveryCity;
    }

    public boolean isDeliveryCounty() {
        return deliveryCounty;
    }

    public void setDeliveryCounty(boolean deliveryCounty) {
        this.deliveryCounty = deliveryCounty;
    }

    public boolean isDeliveryTown() {
        return deliveryTown;
    }

    public void setDeliveryTown(boolean deliveryTown) {
        this.deliveryTown = deliveryTown;
    }

    public boolean isDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(boolean deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public boolean isDeliveryMobile() {
        return deliveryMobile;
    }

    public void setDeliveryMobile(boolean deliveryMobile) {
        this.deliveryMobile = deliveryMobile;
    }

    public boolean isDeliveryPhone() {
        return deliveryPhone;
    }

    public void setDeliveryPhone(boolean deliveryPhone) {
        this.deliveryPhone = deliveryPhone;
    }

    public boolean isDeliveryPostcode() {
        return deliveryPostcode;
    }

    public void setDeliveryPostcode(boolean deliveryPostcode) {
        this.deliveryPostcode = deliveryPostcode;
    }

    public String getAgreement() {
        return agreement;
    }

    public void setAgreement(String agreement) {
        this.agreement = agreement;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
