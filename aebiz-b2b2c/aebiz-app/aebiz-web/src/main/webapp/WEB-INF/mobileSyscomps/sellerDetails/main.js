/**
 * Created by Xic on 2017/5/27.
 */
Comps.sellerDetails = function (options) {
    this.productUuid = Utils.getQueryString("productUuid");
    this.ajaxUrl = MobilePath + 'jsonParam={"opeType":"getProductDetailPageByNoValidate","map":{"productUuid":"'+this.productUuid+'","promotionUuid":"","customerUuid":""}}';
    this.loginUrl = options.mapProps.loginUrl;
    this.storeUrl = options.mapProps.storeUrl;
    this.productUrl = options.mapProps.productUrl;
};
Comps.sellerDetails.prototype.init = function () {
    var _self = this;
    $.ajax({
        type: "POST",
        url: _self.ajaxUrl,
        dataType: "json",
        async: true,
        success: function (data) {
            var html = template("m-index-sellerinfo",data);
            $(".sellerInfo").html(html);
            var haoping = (data.productDetailInfo.storeScore/5)*100.0;
            haoping = haoping.toFixed(2);
            $("#praise").html("&nbsp;好评率:"+haoping+"%");
            _self.toShow(_self.loginUrl,_self.productUrl,_self.storeUrl);
        }
    });

};
Comps.sellerDetails.prototype.toShow = function (loginUrl,productUrl,storeUrl) {
    $('#productCount').click(function () {
        var storeUuid = sessionStorage.getItem("storeUuid");
        localStorage.setItem("storeUuid",storeUuid);
        localStorage.setItem("storeSearchKey","");
        window.location.href = productUrl;
    });
    $("#intoStore").click(function () {
        console.log(this.storeUrl);
        var storeUuid = sessionStorage.getItem("storeUuid");
        localStorage.setItem('storeUuid',storeUuid);
        window.location.href = storeUrl;
    });
    $("#shoucangStore").click(function() {


        var sessionId = localStorage.getItem("sessionId");
        var customerUuid = localStorage.getItem("userId");
        var token = localStorage.getItem("token");
        var productUuid = $("#productUuidTmp").val();


        if (typeof customerUuid == 'undefind' || customerUuid == null || customerUuid == '') {
            localStorage.setItem("comeAddress", location.href);

            var storeUuid = $("#storeUuidTmp").val();
            sessionStorage.setItem("preStoreCollectData", storeUuid);
            mui.alert('当前用户未登录', '提示', function() {
                localStorage.setItem("comeAddress", location.href);
                window.location.href = loginUrl;})
            return;
        }

        var isCollected = $("#storeCollected").val();
        if (isCollected == 'collected') {

            var storeFavorUuid = $("#storeFavorUuid").val();
            deleteStoreFavorite(sessionId, customerUuid, token, storeFavorUuid,loginUrl);

        } else {

            var storeUuid = $("#storeUuidTmp").val();

            addStoreFavorite(sessionId, customerUuid, token, storeUuid,loginUrl);

        }

    });
    function deleteStoreFavorite(sessionId, customerUuid, token, storeFavorUuid,loginUrl) {
        var url = MobilePath + 'jsonParam={"opeType":"deleteStoreFavorite","map":{"storeFavoriteUuid":"' + storeFavorUuid + '","token":"' + token + '","sessionId":"' + sessionId + '"}}';

        $.ajax({
            url: url,
            type: "POST",
            dataType: "json",
            async: true,
            success: function(data) {
                if (data.return_code == 0) {

                    mui.alert("取消收藏商铺成功", "提示");
                    $("#storeCollected").val("no_collected");
                    $("#shoucangStore").val('收藏店铺');
                    storeFavoriteCountHandle("del");

                } else if (isLogin(data.return_code)) {
                    mui.alert('当前用户未登录', '提示', function() {
                        localStorage.setItem("comeAddress", location.href);
                        window.location.href = loginUrl;
                    })

                } else {
                    mui.alert("取消收藏商铺失败", "提示");
                }
            }
        });


    }
    function isLogin(return_code){
        if (return_code == "49" ||
            return_code == "50" ||
            return_code == "51") {
            return true;
        }
    }
    function storeFavoriteCountHandle(action) {

        action = action.toLowerCase();
        var favoriteCount = $(".storeFavoriteCount").html();
        var favoriteCount = favoriteCount.replace(/[^0-9]/ig, "");
        favoriteCount = parseInt(favoriteCount);
        if (action == "add") {
            favoriteCount++;
        } else if (action == "del") {
            favoriteCount--;
        }

        if (favoriteCount < 0) {
            favoriteCount = 0;
        }

        $(".storeFavoriteCount").html("收藏 " + favoriteCount); //店铺收藏数

    }
    function addStoreFavorite(sessionId, customerUuid, token, storeUuid,loginUrl) {
        var url = MobilePath + 'jsonParam={"opeType":"addStoreFavorite","map":{"customerUuid":"' + customerUuid + '","storeUuid":"' + storeUuid + '","token":"' + token + '","sessionId":"' + sessionId + '"}}';

        console.log(url);
        $.ajax({
            url: url,
            type: "POST",
            dataType: "json",
            async: true,
            success: function(data) {

                if (data.return_code == "0") {
                    if (data.state == "1" || data.state == "3") {
                        $("#storeCollected").val("collected");
                        $("#storeFavorUuid").val(data.uuid);
                        mui.alert("收藏商铺成功", "提示");
                        $("#shoucangStore").val('已收藏');
                        storeFavoriteCountHandle("add");
                    } else {
                        mui.alert("收藏商铺失败", "提示");
                    }
                } else if (isLogin(data.return_code)) {
                    mui.alert('当前用户未登录', '提示', function() {
                        localStorage.setItem("comeAddress", location.href);
                        window.location.href = loginUrl;
                    })

                } else {
                    mui.alert("收藏商铺失败", "提示");
                }
            }
        });

    }
};



