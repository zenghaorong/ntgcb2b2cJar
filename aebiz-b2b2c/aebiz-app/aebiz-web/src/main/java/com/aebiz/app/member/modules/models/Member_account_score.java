package com.aebiz.app.member.modules.models;

import com.aebiz.baseframework.base.model.BaseModel;
import org.nutz.dao.entity.annotation.*;

import java.io.Serializable;

/**
 * 会员积分变动历史表
 * Created by wizzer on 2017/3/5.
 */
@Table("member_account_score")
@TableIndexes({@Index(name = "INDEX_MEMBER_ACC_SCORE", fields = {"accountId"}, unique = false)})
public class Member_account_score extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column
    @Name
    @Comment("ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    @Prev(els = {@EL("ig(view.tableName,'')")})
    private String id;

    @Column
    @Comment("帐号ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String accountId;

    @Column
    @Comment("订单ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String orderId;

    @Column
    @Comment("商品ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String goodsId;

    @Column
    @Comment("货品ID")
    @ColDefine(type = ColType.VARCHAR, width = 32)
    private String productId;

    @Column
    @Comment("交易说明")
    @ColDefine(type = ColType.VARCHAR, width = 255)
    private String note;

    @Column
    @Comment("原积分")
    @ColDefine(type = ColType.INT)
    @Default("0")
    private Integer oldScore = 0;

    @Column
    @Comment("现积分")
    @Default("0")
    @ColDefine(type = ColType.INT)
    private Integer newScore = 0;

    @Column
    @Comment("差额")
    @Default("0")
    @ColDefine(type = ColType.INT)
    private Integer diffScore = 0;

    @Column
    @Comment("产生时间")
    @ColDefine(type = ColType.INT)
    private Integer creatAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getOldScore() {
        return oldScore;
    }

    public void setOldScore(Integer oldScore) {
        this.oldScore = oldScore;
    }

    public Integer getNewScore() {
        return newScore;
    }

    public void setNewScore(Integer newScore) {
        this.newScore = newScore;
    }

    public Integer getDiffScore() {
        return diffScore;
    }

    public void setDiffScore(Integer diffScore) {
        this.diffScore = diffScore;
    }

    public Integer getCreatAt() {
        return creatAt;
    }

    public void setCreatAt(Integer creatAt) {
        this.creatAt = creatAt;
    }
}
