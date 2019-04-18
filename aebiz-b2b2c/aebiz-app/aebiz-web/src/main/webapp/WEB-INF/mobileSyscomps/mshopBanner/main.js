/**
 * Created by wenyu on 2017/5/27.
 */
Comps.mshopBanner = function(){
    this.url = "";
}

Comps.mshopBanner.prototype.init = function(){
    var storeUuid = "";
    // var param = window.location.search;
    // if (param != null && param != "" && typeof param != 'undefined') {
    //     var paramList = param.split('=');
    //     if (paramList.length > 1) {
    //         storeUuid = paramList[1];
    //     } else {
    //         storeUuid = "";
    //     }
    // }
    if (storeUuid == "") {
        storeUuid = Utils.getQueryString("storeUuid");
    }
    var localPath = window.location.href + "?storeUuid=" + storeUuid;

    $("#storeUuid").val(storeUuid);
    var userId = localStorage.getItem("userId");


    if(userId != "" && userId != null){
        this.url =MobilePath+'jsonParam={"opeType":"getStoreIndex","map":{"storeUuid":"' + storeUuid + '","customerUuid":"' + userId + '"}}';
    }else{
        this.url = MobilePath+'jsonParam={"opeType":"getStoreIndex","map":{"storeUuid":"' + storeUuid + '"}}';
    }

    this.getBanner();
}

Comps.mshopBanner.prototype.getBanner = function(){
    var _self = this;

    console.log(_self.url)
    $.ajax({
        url: _self.url,
        type: "POST",
        dataType: "json",
        success: function(data) {
            if (data.return_code != "0") {
                mui.alert(data.message)
            } else {
                //店铺基本信息
                var storeInfo = data.storeInfo;
                //店招广告  第一子节点元素
                var dzgg = data.store_ad;
                var banner = data.store_banner;
                $('.shop-banner').children(":first").attr('src', banner[0].imageUrl);
                //头部广告
                $(".dianzhaogg").find('img').attr('src', dzgg[0].imageUrl);

                var action = linkdetail(dzgg[0].actionType);
                $('.dianzhaogg').find('a').attr("onclick", action);
                var action = linkdetail(banner[0].actionType);
                $('.shop-banner').children(":first").attr("onclick", action);

                //店铺名称
                $("#shopName").html(storeInfo.storeName);
                //店铺logo
                $(".logod").find('img').attr('src', storeInfo.logo);


                //销量
                $("#xiaoliang").html(storeInfo.saleCount);
                //店铺被收藏的次数
                $("#shoucang").html(storeInfo.favoriteCount);

                $('.dianzhaogg').find('a').attr('src', dzgg[0].imageUrl);
                //判断是否被收藏
                var favoriteState = storeInfo.favoriteState;
                if ("1" == favoriteState) { //已收藏
                    $("#scdp").val("取消收藏");
                    $("#scdp").addClass("on");
                }
                $("#storeFavoriteUuid").val(storeInfo.storeFavoriteUuid);

            }
        },
        error: function(data) {
            mui.alert(data.message,'提示');
        }
    })
};