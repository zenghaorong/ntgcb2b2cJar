/**
 * Created by Xic on 2017/6/2.
 */
Comps.combinedProduct = function (options) {
    this.productUuid = Utils.getQueryString("productUuid");
    this.options = options;
    this.ajaxUrl = MobilePath + 'jsonParam={"opeType":"getProductDetailPageByNoValidate","map":{"productUuid":"'+this.productUuid+'","promotionUuid":"","customerUuid":""}}';
};
Comps.combinedProduct.prototype.init = function () {
    var _self = this;
    $.ajax({
        type: "POST",
        url: _self.ajaxUrl,
        dataType: "json",
        async: true,
        success: function (data) {
            console.log(data);
            _self.groupProduct(data);
        }

    })
};
Comps.combinedProduct.prototype.groupProduct = function (data) {
    var defineUrl = this.options.mapProps.defineUrl;
    groupShop(data);
    function groupShop(data){
        // 移除静态元素,后面动态创建
        $(".goods").remove();
        $("#freeCombinationHref span").remove();
        $("#freeCombinationHref i.arrow-more").remove();
        //组合信息
        var groupList = data.productDetailInfo.groupList;
        for (var i = 0; i < groupList.length; i++) {
            var sub = groupList[i].subProducts;//组合产品信息集合
            var groupName = groupList[i].groupName;	//组合套餐名称
            var groupSpan;//span标签

            if (i==0) {
                groupSpan = $('<span class="action">'+groupName+'</span>');
            }else{
                groupSpan = $('<span >'+groupName+'</span>');
            }

            $("#freeCombinationHref").append(groupSpan);
            var goodsDiv = $("<div class='goods'></div>");
            $("#freedom-team").append(goodsDiv);
            var length  = sub.length;
            for (var j = 0; j < sub.length; j++) {
                var sp = sub[j].shopPrice;
                var ci = sub[j].centerImage;
                var goods_span;
                sp = "￥ " + sp;
                if (j==0) {
                    goods_span = '<span><a href="free-combination.html"><img src="'+ci+'"><p>'+sp+'</p></a></span>';
                }else{
                    goods_span = '<span class="add"></span><span><a href=""><img src="'+ci+'"><p>'+sp+'</p></a></span>';
                }
                goodsDiv.append(goods_span);
            }
        }
        $("#freeCombinationHref").append('<i class="arrow-more"><img src="${contextPath}/static/mobileTempAssets/defaultTemplate/images/details/11.png" alt=""></i>');
        $(".goods").hide().eq(0).show();
        //点击自由组合哪一行跳转到自由组合详情页
        $("#freeCombinationHref").click(function(){
            var index = $(".wayss span.action").index();
            window.location.href = defineUrl+index;
        });
        $(".goods").click(function(){
            var index = $(".wayss span.action").index();
            window.location.href = defineUrl+index;

        });
        //点击自由组合套餐名的效果
        $(".wayss span").click(function(event) {
            event.stopPropagation();
            var index = $(this).index();
            $(this).addClass("action").siblings().removeClass("action");
            $(".goods").hide().eq(index).show();

        });
    }
}