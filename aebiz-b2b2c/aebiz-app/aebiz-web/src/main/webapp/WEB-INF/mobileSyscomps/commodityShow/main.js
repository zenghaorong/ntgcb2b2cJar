/**
 * Created by Xic on 2017/5/27.
 */
Comps.commodityShow = function(){
    this.productUuid = "59df4f2cdff849c29970c15bbf9e4589";//Utils.getQueryString("productUuid");
    this.ajaxUrl = MobilePath + 'jsonParam={"opeType":"getProductDetailPageByNoValidate","map":{"productUuid":"'+this.productUuid+'","promotionUuid":"","customerUuid":""}}';
};

Comps.commodityShow.prototype.init = function(){
    var _self = this;
    $.ajax({
        type: "POST",
        url: _self.ajaxUrl,
        dataType: "json",
        async: true,
        success: function (data) {
            console.log(data);
            var html = '<h5 class="main3">商品详情</h5>'+data.productDetailInfo.desc;
            $('.xiangqing').html(html);
        }

    })
};