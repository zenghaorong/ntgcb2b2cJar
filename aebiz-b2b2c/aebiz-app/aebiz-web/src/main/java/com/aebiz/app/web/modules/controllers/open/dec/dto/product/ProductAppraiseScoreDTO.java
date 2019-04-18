package com.aebiz.app.web.modules.controllers.open.dec.dto.product;

/**
 * 商品评价分数DTO
 *
 * Created by Aebiz_yjq on 2017/1/17.
 */
public class ProductAppraiseScoreDTO {


    /* 总评价数量 */
    private int totalCount = 0;

    /* 好评数*/
    private int gAppraiseCount = 0;

    /* 中评数 */
    private int mAppraiseCount = 0;

    /* 差评数 */
    private int bAppraiseCount = 0;

    /* 晒单个数 */
    private int orderShowCount = 0;

    public ProductAppraiseScoreDTO() {
    }

    public ProductAppraiseScoreDTO(int totalCount, int gAppraiseCount, int mAppraiseCount, int bAppraiseCount, int orderShowCount) {
        this.totalCount = totalCount;
        this.gAppraiseCount = gAppraiseCount;
        this.mAppraiseCount = mAppraiseCount;
        this.bAppraiseCount = bAppraiseCount;
        this.orderShowCount = orderShowCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getgAppraiseCount() {
        return gAppraiseCount;
    }

    public void setgAppraiseCount(int gAppraiseCount) {
        this.gAppraiseCount = gAppraiseCount;
    }

    public int getmAppraiseCount() {
        return mAppraiseCount;
    }

    public void setmAppraiseCount(int mAppraiseCount) {
        this.mAppraiseCount = mAppraiseCount;
    }

    public int getbAppraiseCount() {
        return bAppraiseCount;
    }

    public void setbAppraiseCount(int bAppraiseCount) {
        this.bAppraiseCount = bAppraiseCount;
    }

    public int getOrderShowCount() {
        return orderShowCount;
    }

    public void setOrderShowCount(int orderShowCount) {
        this.orderShowCount = orderShowCount;
    }

    @Override
    public String toString() {
        return "ProductAppraiseScoreDTO{" +
                "totalCount=" + totalCount +
                ", gAppraiseCount=" + gAppraiseCount +
                ", mAppraiseCount=" + mAppraiseCount +
                ", bAppraiseCount=" + bAppraiseCount +
                ", orderShowCount=" + orderShowCount +
                '}';
    }
}
