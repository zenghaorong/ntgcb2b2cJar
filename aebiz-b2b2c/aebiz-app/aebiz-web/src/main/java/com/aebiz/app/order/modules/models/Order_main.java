package com.aebiz.app.order.modules.models;

import com.aebiz.app.acc.modules.models.Account_info;
import com.aebiz.app.acc.modules.models.Account_user;
import com.aebiz.app.goods.modules.models.Goods_main;
import com.aebiz.app.member.modules.models.Member_address;
import com.aebiz.app.store.modules.models.Store_main;
import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 订单表
 * 开发注意事项：
 * 1、前台查询订单，必须传递会员ID，防止订单信息越权提取；
 * 2、订单号使用Redis生成，确保集群环境下的唯一性；
 * 3、下订单的操作，使用队列实现，提升并发性能；
 * 4、每个店铺生成一个订单；
 * 5、订单主表记录了主要的时间方便排序，比如下单时间、支付时间、发货时间、收货时间，其他时间则记录在订单日志表，如取消订单时间、审核订单时间、确认订单时间等
 * Created by wizzer on 2017/3/29.
 */
@Table("order_main")
@TableIndexes({@Index(name = "INDEX_ORDER_MAIN_ACC", fields = {"accountId"}, unique = false)})
public class Order_main extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("订单号")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("order()")})
    private String id;

    @Column
    @Comment("订单组ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String groupId;

    @Column
    @Comment("帐号ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String accountId;   //account_info 表中的id

    @Column
    @Comment("商城ID")
        @ColDefine(type = ColType.VARCHAR, width = 32)
    private String storeId;    //store_main 表中的id

    @Column
    @Comment("商品总金额")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer goodsMoney;

    @Comment("商品总优惠")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer goodsFreeMoney;

    @Column
    @Comment("商品应付金额")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer goodsPayMoney;//商品详情表 payMoney 应付金额之和

    @Column
    @Comment("商品总重量")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer goodsWeight;

    @Column
    @Comment("订单运费")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer freightMoney;

    @Column
    @Comment("订单优惠")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer freeMoney;//订单优惠，如满减、优惠券抵扣等

    @Column
    @Comment("应付金额")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer payMoney;//payMoney = goodsPayMoney + freightMoney - freeMoney

    @Column
    @Comment("支付方式")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer payType;//OrderPayTypeEnum

    @Column
    @Comment("网上支付code")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String payCode;//shop_payment 表的code值

    @Column
    @Comment("网上支付名称")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String payName;//shop_payment 表的name值

    @Column
    @Comment("支付货币")
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String currency;

    @Column
    @Comment("货币汇率")
    @ColDefine(type = ColType.FLOAT)
    private Double currencyRate;

    @Column
    @Comment("支付状态")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer payStatus;//OrderPayStatusEnum

    @Column
    @Comment("支付时间")
    @ColDefine(type = ColType.INT)
    private Integer payAt;

    @Column
    @Comment("订单来源")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private Integer orderSrc;//OrderSourceEnum

    @Column
    @Comment("订单状态")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer orderStatus;//OrderStatusEnum

    @Column
    @Comment("订单类型")
    @ColDefine(type = ColType.VARCHAR, width = 10)
    private String orderType; //1.商品订单 2.视频订单

    @Column
    @Comment("下单时间")
    @ColDefine(type = ColType.INT)
    private Integer orderAt;

    @Column
    @Comment("IP地址")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String orderIp;

    @Column
    @Comment("是否开发票")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean taxNeed;

    @Column
    @Comment("发票类型")
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String taxType;//personal 个人 company 公司

    @Column
    @Comment("发票抬头")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String taxHead;

    @Column
    @Comment("发票内容")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String taxNote;

    @Column
    @Comment("发票税号")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String taxNo;

    @Column
    @Comment("送货人")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String deliveryName;

    @Column
    @Comment("送货省")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String deliveryProvince;

    @Column
    @Comment("送货市")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String deliveryCity;

    @Column
    @Comment("送货县区")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String deliveryCounty;

    @Column
    @Comment("送货街道")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String deliveryTown;

    @Column
    @Comment("送货地址")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String deliveryAddress;

    @Column
    @Comment("送货手机号")
    @ColDefine(type = ColType.VARCHAR, width = 20)
    private String deliveryMobile;

    @Column
    @Comment("送货电话")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String deliveryPhone;

    @Column
    @Comment("邮政编码")
    @ColDefine(type = ColType.VARCHAR, width = 10)
    private String deliveryPostcode;

    @Column
    @Comment("配送时间")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String deliveryTime;

    @Column
    @Comment("是否需要发货")
    @ColDefine(type = ColType.BOOLEAN)
    private boolean deliveryNeed;

    @Column
    @Comment("发货状态")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer deliveryStatus;//OrderDeliveryStatusEnum

    @Column
    @Comment("发货时间")
    @ColDefine(type = ColType.INT)
    private Integer deliveryAt;

    @Column
    @Comment("收货状态")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer getStatus;//OrderGetStatusEnum

    @Column
    @Comment("收货时间")
    @ColDefine(type = ColType.INT)
    private Integer getAt;

    @Column
    @Comment("物流公司ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String expressId;

    @Column
    @Comment("物流公司名称")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String expressName;

    @Column
    @Comment("物流单号")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String expressNo;

    @Column
    @Comment("评价状态")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer feedStatus;//OrderFeedStatusEnum

    @Column
    @Comment("评价时间")
    @ColDefine(type = ColType.INT)
    private Integer feedAt;

    @Column
    @Comment("订单附言")
    @ColDefine(type = ColType.VARCHAR, width = 500)
    private String note;//会员购买时填写

    @Column
    @Comment("订单备注")
    @ColDefine(type = ColType.VARCHAR, width = 500)
    private String mark;//商家管理时填写

    @Many(field = "orderId")
    private List<Order_goods>  goodsList;

    @Many(field = "orderId")
    private List<Order_pay_payment> paymentList;

    @Many(field = "orderId")
    private List<Order_pay_transfer> transferList;

    @Many(field = "orderId")
    private List<Order_delivery_detail> deliveryDetailList;

    @One(field = "accountId")
    private Account_info accountInfo;

    private Account_user accountUser;

    private List<Goods_main> productList;

    private Member_address addresses;



    private String endTime;

    @One(field = "storeId")
    private Store_main storeMain;

    private String expressInfo;

    public List<Goods_main> getProductList() {
        return productList;
    }

    public void setProductList(List<Goods_main> productList) {
        this.productList = productList;
    }

    public Member_address getAddresses() {
        return addresses;
    }

    public void setAddresses(Member_address addresses) {
        this.addresses = addresses;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public Integer getGoodsMoney() {
        return goodsMoney;
    }

    public void setGoodsMoney(Integer goodsMoney) {
        this.goodsMoney = goodsMoney;
    }

    public Integer getGoodsFreeMoney() {
        return goodsFreeMoney;
    }

    public void setGoodsFreeMoney(Integer goodsFreeMoney) {
        this.goodsFreeMoney = goodsFreeMoney;
    }

    public Integer getGoodsPayMoney() {
        return goodsPayMoney;
    }

    public void setGoodsPayMoney(Integer goodsPayMoney) {
        this.goodsPayMoney = goodsPayMoney;
    }

    public Integer getGoodsWeight() {
        return goodsWeight;
    }

    public void setGoodsWeight(Integer goodsWeight) {
        this.goodsWeight = goodsWeight;
    }

    public Integer getFreightMoney() {
        return freightMoney;
    }

    public void setFreightMoney(Integer freightMoney) {
        this.freightMoney = freightMoney;
    }

    public Integer getFreeMoney() {
        return freeMoney;
    }

    public void setFreeMoney(Integer freeMoney) {
        this.freeMoney = freeMoney;
    }

    public Integer getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(Integer payMoney) {
        this.payMoney = payMoney;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getCurrencyRate() {
        return currencyRate;
    }

    public void setCurrencyRate(Double currencyRate) {
        this.currencyRate = currencyRate;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getPayAt() {
        return payAt;
    }

    public void setPayAt(Integer payAt) {
        this.payAt = payAt;
    }

    public Integer getOrderSrc() {
        return orderSrc;
    }

    public void setOrderSrc(Integer orderSrc) {
        this.orderSrc = orderSrc;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getOrderAt() {
        return orderAt;
    }

    public void setOrderAt(Integer orderAt) {
        this.orderAt = orderAt;
    }

    public String getOrderIp() {
        return orderIp;
    }

    public void setOrderIp(String orderIp) {
        this.orderIp = orderIp;
    }

    public boolean isTaxNeed() {
        return taxNeed;
    }

    public void setTaxNeed(boolean taxNeed) {
        this.taxNeed = taxNeed;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public String getTaxHead() {
        return taxHead;
    }

    public void setTaxHead(String taxHead) {
        this.taxHead = taxHead;
    }

    public String getTaxNote() {
        return taxNote;
    }

    public void setTaxNote(String taxNote) {
        this.taxNote = taxNote;
    }

    public String getTaxNo() {
        return taxNo;
    }

    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo;
    }

    public String getDeliveryName() {
        return deliveryName;
    }

    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
    }

    public String getDeliveryProvince() {
        return deliveryProvince;
    }

    public void setDeliveryProvince(String deliveryProvince) {
        this.deliveryProvince = deliveryProvince;
    }

    public String getDeliveryCity() {
        return deliveryCity;
    }

    public void setDeliveryCity(String deliveryCity) {
        this.deliveryCity = deliveryCity;
    }

    public String getDeliveryCounty() {
        return deliveryCounty;
    }

    public void setDeliveryCounty(String deliveryCounty) {
        this.deliveryCounty = deliveryCounty;
    }

    public String getDeliveryTown() {
        return deliveryTown;
    }

    public void setDeliveryTown(String deliveryTown) {
        this.deliveryTown = deliveryTown;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getDeliveryMobile() {
        return deliveryMobile;
    }

    public void setDeliveryMobile(String deliveryMobile) {
        this.deliveryMobile = deliveryMobile;
    }

    public String getDeliveryPhone() {
        return deliveryPhone;
    }

    public void setDeliveryPhone(String deliveryPhone) {
        this.deliveryPhone = deliveryPhone;
    }

    public String getDeliveryPostcode() {
        return deliveryPostcode;
    }

    public void setDeliveryPostcode(String deliveryPostcode) {
        this.deliveryPostcode = deliveryPostcode;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public boolean isDeliveryNeed() {
        return deliveryNeed;
    }

    public void setDeliveryNeed(boolean deliveryNeed) {
        this.deliveryNeed = deliveryNeed;
    }

    public Integer getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(Integer deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public Integer getDeliveryAt() {
        return deliveryAt;
    }

    public void setDeliveryAt(Integer deliveryAt) {
        this.deliveryAt = deliveryAt;
    }

    public Integer getGetStatus() {
        return getStatus;
    }

    public void setGetStatus(Integer getStatus) {
        this.getStatus = getStatus;
    }

    public Integer getGetAt() {
        return getAt;
    }

    public void setGetAt(Integer getAt) {
        this.getAt = getAt;
    }

    public String getExpressId() {
        return expressId;
    }

    public void setExpressId(String expressId) {
        this.expressId = expressId;
    }

    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public Integer getFeedStatus() {
        return feedStatus;
    }

    public void setFeedStatus(Integer feedStatus) {
        this.feedStatus = feedStatus;
    }

    public Integer getFeedAt() {
        return feedAt;
    }

    public void setFeedAt(Integer feedAt) {
        this.feedAt = feedAt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public List<Order_goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Order_goods> goodsList) {
        this.goodsList = goodsList;
    }

    public List<Order_pay_payment> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<Order_pay_payment> paymentList) {
        this.paymentList = paymentList;
    }

    public List<Order_pay_transfer> getTransferList() {
        return transferList;
    }

    public void setTransferList(List<Order_pay_transfer> transferList) {
        this.transferList = transferList;
    }

    public Account_info getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(Account_info accountInfo) {
        this.accountInfo = accountInfo;
    }

    public Store_main getStoreMain() {
        return storeMain;
    }

    public void setStoreMain(Store_main storeMain) {
        this.storeMain = storeMain;
    }

    public Account_user getAccountUser() {
        return accountUser;
    }

    public void setAccountUser(Account_user accountUser) {
        this.accountUser = accountUser;
    }

    public List<Order_delivery_detail> getDeliveryDetailList() {
        return deliveryDetailList;
    }

    public void setDeliveryDetailList(List<Order_delivery_detail> deliveryDetailList) {
        this.deliveryDetailList = deliveryDetailList;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getExpressInfo() {
        return expressInfo;
    }

    public void setExpressInfo(String expressInfo) {
        this.expressInfo = expressInfo;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
}
