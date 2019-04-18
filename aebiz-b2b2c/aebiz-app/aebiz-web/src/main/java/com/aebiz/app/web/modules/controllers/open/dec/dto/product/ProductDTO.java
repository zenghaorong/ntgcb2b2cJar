package com.aebiz.app.web.modules.controllers.open.dec.dto.product;

/**
 *  商品DTO
 *
 * Created by Aebiz_yjq on 2017/1/13.
 */
public class ProductDTO {


    /* 商品uuid */
    private String uuid;

    /* 商品名称 */
    private String name;

    /* 推荐语 */
    private String recommend;

    /* 品牌名称 */
    private String pname;

    /* 图片地址 */
    private String pic;

    /* 商品综合描述（商品名称+品牌名称+推荐语+商品描述+商品的属性值） */
    private String desc;

    /* 所属前台分类 */
    private String cats;

    /* 所属前台分类的名称 */
    private String catNames;

    /*商品的属性值（是多个值，这个是后台的属性数据，但是是前台需要的数据） */
    private String props;

    /* 所属商户 */
    private String storeUuid;

    /* 商户自定义分类uuid */
    private String storeCategoryUuids;

    /* 商户名称 */
    private String storeName;

    /* 商品当前销售价格 */
    private double price;

    /* 商品综合排序得分 */
    private double score;

    /* 总销量 */
    private float mount;

    /* 评论数 */
    private int comments;

    /*  缺省的skuNo */
    private String skuNo;

    /* 是否有库存 */
    private String existProduct;

    public ProductDTO(){}

    public ProductDTO(String uuid, String name, String recommend, String pname, String pic, String desc, String cats,
                      String catNames, String props, String storeUuid, String storeCategoryUuids, String storeName,
                      double price, double score, float mount, int comments, String skuNo, String existProduct) {
        this.uuid = uuid;
        this.name = name;
        this.recommend = recommend;
        this.pname = pname;
        this.pic = pic;
        this.desc = desc;
        this.cats = cats;
        this.catNames = catNames;
        this.props = props;
        this.storeUuid = storeUuid;
        this.storeCategoryUuids = storeCategoryUuids;
        this.storeName = storeName;
        this.price = price;
        this.score = score;
        this.mount = mount;
        this.comments = comments;
        this.skuNo = skuNo;
        this.existProduct = existProduct;
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

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCats() {
        return cats;
    }

    public void setCats(String cats) {
        this.cats = cats;
    }

    public String getCatNames() {
        return catNames;
    }

    public void setCatNames(String catNames) {
        this.catNames = catNames;
    }

    public String getProps() {
        return props;
    }

    public void setProps(String props) {
        this.props = props;
    }

    public String getStoreUuid() {
        return storeUuid;
    }

    public void setStoreUuid(String storeUuid) {
        this.storeUuid = storeUuid;
    }

    public String getStoreCategoryUuids() {
        return storeCategoryUuids;
    }

    public void setStoreCategoryUuids(String storeCategoryUuids) {
        this.storeCategoryUuids = storeCategoryUuids;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public float getMount() {
        return mount;
    }

    public void setMount(float mount) {
        this.mount = mount;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getSkuNo() {
        return skuNo;
    }

    public void setSkuNo(String skuNo) {
        this.skuNo = skuNo;
    }

    public String getExistProduct() {
        return existProduct;
    }

    public void setExistProduct(String existProduct) {
        this.existProduct = existProduct;
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", recommend='" + recommend + '\'' +
                ", pname='" + pname + '\'' +
                ", pic='" + pic + '\'' +
                ", desc='" + desc + '\'' +
                ", cats='" + cats + '\'' +
                ", catNames='" + catNames + '\'' +
                ", props='" + props + '\'' +
                ", storeUuid='" + storeUuid + '\'' +
                ", storeCategoryUuids='" + storeCategoryUuids + '\'' +
                ", storeName='" + storeName + '\'' +
                ", price=" + price +
                ", score=" + score +
                ", mount=" + mount +
                ", comments=" + comments +
                ", skuNo='" + skuNo + '\'' +
                ", existProduct='" + existProduct + '\'' +
                '}';
    }
}
