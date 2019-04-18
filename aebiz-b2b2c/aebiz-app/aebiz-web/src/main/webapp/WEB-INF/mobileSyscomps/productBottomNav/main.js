/**
 * Created by Xic on 2017/6/1.
 */
Comps.productBottomNav = function (options) {
    this.options = options;
    this.compId = options.mapProps.compId;
    this.loginUrl = options.mapProps.loginUrl;
};
Comps.productBottomNav.prototype.init = function () {
    var productUuid = Utils.getQueryString("productUuid");
    var customerUuid = localStorage.getItem("userId");
    var ajaxUrl = MobilePath + 'jsonParam={"opeType":"getProductDetailPageByNoValidate","map":{"productUuid":"'+productUuid+'","promotionUuid":"","customerUuid":"'+customerUuid+'"}}'
    var _self = this;
    $.ajax({
        type: "POST",
        url: ajaxUrl,
        dataType: "json",
        async: true,
        success: function (data) {
            console.log(data);
            sessionStorage.setItem("storeUuid",data.productDetailInfo.storeUuid);
            _self.change(data);
        }

    })
};
Comps.productBottomNav.prototype.change = function (data) {
    var _self = this;
    dataHandle(data);
    preActionHandle();
    var urlTarget = this.options.mapProps.defineUrl;
    var loginUrl = this.loginUrl;
    var clickAddtoCartAndFastBuyKey = "notifymsg";
    function preActionHandle(){

        var preProductCollectData = sessionStorage.getItem("preProductCollectData");
        var sessionId = localStorage.getItem("sessionId");
        var customerUuid = localStorage.getItem("userId");
        var token = localStorage.getItem("token");

        if (customerUuid == null || customerUuid == "") {
            return;
        }

        if(preProductCollectData != null && preProductCollectData != ""){


            var productUuid = preProductCollectData;
            sessionStorage.setItem("preProductCollectData","");
            addProductFavorite(sessionId, customerUuid, token, productUuid);
        }


        var preStoreCollectData = sessionStorage.getItem("preStoreCollectData");
        if(preStoreCollectData != null && preStoreCollectData != ""){

            var storeUuid = preStoreCollectData;
            sessionStorage.setItem("preStoreCollectData","");
            addStoreFavorite(sessionId, customerUuid, token, storeUuid);
        }


        var addToCartJsonDataStr = sessionStorage.getItem("addToCartJsonData");
        var addToCartJsonData = JSON.parse(addToCartJsonDataStr);
        if (addToCartJsonData != null && addToCartJsonData != "" && addToCartJsonData["productUuid"] != null &&
            addToCartJsonData["productUuid"] != "" &&
            addToCartJsonData["productUuid"] != "undefind") {

            var tmp = {};
            sessionStorage.setItem("addToCartJsonData",JSON.stringify(tmp));
            var productUuid = addToCartJsonData["productUuid"];
            var skuNo = addToCartJsonData["skuNo"];
            var buyNum = addToCartJsonData["buyNum"];

            addtoCart(productUuid,customerUuid,skuNo,buyNum,token,sessionId);
        }

    }

    function preIsLogin(){
            var userId= localStorage.getItem('userId');
            if(userId == null || userId == "" ){

                return false;
            }

            return true;

        }

        $(".spkf").click(function() {
            var userId = localStorage.getItem("userId") || '';
            var moblie = localStorage.getItem("mobliePhone") || '';
            // 客服需要先登录
            if (moblie == null || moblie == '' || userId == null || userId == '') {
                localStorage.setItem("comeAddress", location.href);
                console.log(loginUrl);
                window.location.href = loginUrl;
                return
            }
            else
            {
                window.location.href = urlTarget;
            }

        });
        $("#goumai button").click(function() {
            $(this).addClass("action").siblings().removeClass("action");

        });
    //弹出层
        $("#goumai .lijigoumai").click(function() {

            var msg = localStorage.getItem(clickAddtoCartAndFastBuyKey);
            if (msg != "" && msg != null) {
                mui.alert(msg);
                return;
            }

            sessionStorage.setItem("productDetailPageAction", "fastBuyAction");
            $(".opacity").fadeIn(300);
            $("#Specifications").slideDown(300);
        });
        function dataHandle(data) {
            productStock = parseInt(data.productDetailInfo.stock);
            if (data.productDetailInfo.state == 0) {//1上架  0 下架
                window.location.href = "";
            }

            if (productStock <= 0) {
                localStorage.setItem(clickAddtoCartAndFastBuyKey, "库存不够");
            }
            var len = data.productDetailInfo.groupList.length;
            if (len > 0) {
                //groupShop(data);
            } else {
                var productDetailInfo = data.productDetailInfo;
                $("#productNumber").val();
                $("#productName").html(productDetailInfo.productName);
                $("#Specifications .td2 p").html(productDetailInfo.productName);
                $("#Specifications .td1 img").attr("src", productDetailInfo.smallImageUrl);
                $("#Specifications .td2 span.red").html("￥" + productDetailInfo.shopPrice);
                $("#defaultSkuNoTmp").val(productDetailInfo.skuNo);
                if (productDetailInfo.selectAttrValues != null &&
                    productDetailInfo.selectAttrValues.length > 0 &&
                    productDetailInfo.attrStock != null && productDetailInfo.attrStock.specModel != null) {
                    var spec = productDetailInfo.attrStock.specModel;
                    defaultSpecificationShow(productDetailInfo.selectAttrValues, spec, productDetailInfo.productUuid);
                }

                var hasProductFavorite = productDetailInfo.hasFavorite;

                if (hasProductFavorite == "1") {
                    $("#productCollected").val("collected");
                    $("#shoucangProduct span").eq(1).html('已收藏');
                    $("#shoucangProduct").addClass("on");
                    $("#productFavorUuid").val(productDetailInfo.productFavoriteUuid);
                } else {
                    $("#productCollected").val("no_collected");
                    $("#shoucangProduct span").eq(1).html('收藏');
                }

                var hasStoreFavorite = productDetailInfo.hasStoreFavorite;
                if (hasStoreFavorite == "1") {
                    $("#shoucangStore").val('已收藏');
                    $("#storeCollected").val("collected");
                    $("#storeFavorUuid").val(productDetailInfo.storeFavoriteUuid);

                } else {
                    $("#storeCollected").val("no_collected");
                    $("#shoucangStore").val('收藏店铺');
                }
            }

            function defaultSpecificationShow(selectAttrValues, productAttrValue, productUuid) {

                var specificationDescr = "";
                for (var i = 0; i < selectAttrValues.length; i++) {

                    var attributeUuid = selectAttrValues[i].attributeUuid;
                    var attributeName = selectAttrValues[i].attributeName;
                    var $h5 = $('<h5>' + attributeName + '</h5>');
                    var $p = $('<p></p>');
                    var values = selectAttrValues[i].values;

                    for (var j = 0; j < values.length; j++) {
                        var value = values[j].value;
                        var valueUuid = values[j].valueUuid;

                        var $span = $('<span id="' + valueUuid + '">' + value + '</span>');
                        if (isDefaultAttribute(valueUuid, productAttrValue)) {
                            specificationDescr += " " + value;
                            $span.addClass("on");
                        }

                        $p.append($span);
                    }

                    var $li = $('<li></li>');
                    $li.append($h5);
                    $li.append($p);
                    $("#productNum").before($li);
                }
                specificationDescrSet(specificationDescr);

                $("#Specifications li span").click(function () {
                    $(this).addClass("on").siblings().removeClass("on");
                    selectAttrHandle(productUuid);
                    var specificationDescr = getSpecificationDescr();
                    $("#specificationDescr").html(specificationDescr);
                });
            }
            function isDefaultAttribute(valueUuid,productAttrValue){
                if (productAttrValue != null && productAttrValue.length > 0) {

                    for (var i = 0; i < productAttrValue.length; i++) {
                        if (valueUuid == productAttrValue[i].valueUuid) {
                            return true;
                        }
                    }
                }

                return false;
            }
            function getSpecificationDescr(){

                var selectValues = "";
                $("#Specifications ul span.on").each(function(){

                    selectValues += ' '+$(this).html();

                });
                return selectValues;
            }
            function specificationDescrSet(specificationDescr) {
                $("#specificationDescr").html(specificationDescr);
            }

            function selectAttrHandle(productUuid) {
                var selectUuids = getgetSelectAttrUuid(productUuid);
                getSelectSpec(productUuid, selectUuids);
            }

            function getSelectSpec(productUuid, selectUuids) {
                console.log(productUuid);
                var skuNo = "";
                var url = MobilePath + 'jsonParam={"opeType":"selectSpec","map":{"productUuid":"' + productUuid + '","promotionUuid":"","selectUuids":"' + selectUuids + '"}}';
                console.log(url);
                $.ajax({
                    url: url,
                    type: "POST",
                    dataType: "json",
                    success: function (data) {
                        console.log(JSON.stringify(data));
                        if (data != null && data.return_code == 0) {
                            resetPriceAndStock(productUuid, data.selecSpceData);
                        }
                    }
                })
            }

            function resetPriceAndStock(productUuid, selecSpceData) {
                if (selecSpceData != null) {
                    var price = selecSpceData.price;
                    var stock = selecSpceData.stock;
                    var skuNo = selecSpceData.skuNo;
                    var selectUuids = selecSpceData.selectUuids;

                    $("#Specifications table td.td2 span").html('￥' + price);

                    var productNumber = $("#productNumber").val();
                    //can not buy when stock is not enough
                    productNumber = parseInt(productNumber);
                    stock = parseInt(stock);
                    if (productNumber <= 0 || stock <= productNumber) {
                        $("." + productUuid + " ul li.submit input").attr("disabled", "true");
                    }

                }
            }

            function getgetSelectAttrUuid(productUuid) {
                var selectUuids = "";
                $("#Specifications ul span.on").each(function () {

                    if (selectUuids != "") {
                        selectUuids += '-' + $(this).attr("id");
                    } else {
                        selectUuids += $(this).attr("id");
                    }
                });
                return selectUuids;
            }
        }
    //加入购物车按钮
        $("#goumai .gwcss").click(function() {
            var msg = localStorage.getItem(clickAddtoCartAndFastBuyKey);
            if (msg != "" && msg != null) {
                mui.alert(msg);
                return;
            }
            sessionStorage.setItem("productDetailPageAction", "AddToCartAction");
            $("#Specifications").slideDown(300);
            $(".opacity").fadeIn(300);
        });

        $("#Specifications .submit input,#Specifications .td3 img,.opacity").click(function() {
            $("#Specifications").slideUp(300);
            $(".opacity").fadeOut(300);
        });
        $("#Specifications li span").click(function() {
            $(this).addClass("on").siblings().removeClass("on");

        });


        //添加购物车 or 立即购买
        $("#productConfirm").click(function(){
            var hh = window.location.href;
            sessionStorage.setItem("lastPage1",hh);
            localStorage.removeItem("fplx");
            localStorage.removeItem("fptt");
            localStorage.removeItem("jtxx");
            localStorage.removeItem("conpanyName");
            localStorage.removeItem("name1");
            localStorage.removeItem("telphone1");
            localStorage.removeItem("address1");
            localStorage.removeItem("uuid1");
            localStorage.removeItem("fplx");
            localStorage.removeItem("fptt");
            localStorage.removeItem("jtxx");
            sessionStorage.removeItem("yhquanId");
            sessionStorage.removeItem("dpinzId");
            sessionStorage.removeItem("typeName");
            sessionStorage.removeItem("dpzPromotionIdForFastBuy");
            sessionStorage.removeItem("couponIdForFastBuy");
            sessionStorage.removeItem("storePromotionIdForFastBuy");
            sessionStorage.removeItem("fpttForFastBuy");
            sessionStorage.removeItem("jtxxForFastBuy");
            var productDetailPageAction = sessionStorage.getItem("productDetailPageAction");
            var selectUuids = "";
            var customerUuid = localStorage.getItem('userId');
            var token = localStorage.getItem('token');
            var sessionId = localStorage.getItem('sessionId');
            var productUuid = Utils.getQueryString("productUuid");;
            var skuNo = "";
            var buyNum = 0;

            buyNum = parseInt($("#productNumber").val());
            selectUuids = getSelectUuids();
            skuNo = getSkuNobyUuids(selectUuids);
            if (skuNo == null || skuNo == "") {
                skuNo = $("#defaultSkuNoTmp").val();
            }

            if (productDetailPageAction == "fastBuyAction") {
                sessionStorage.setItem("fastBuy_productUuid",productUuid);
                sessionStorage.setItem("fastBuy_skuNo",skuNo);
                sessionStorage.setItem("fastBuy_buyNum",buyNum);
                window.location.href = '../../../h5/templet/order/confirm-orderforfastbuy.html';

            }else if(productDetailPageAction == "AddToCartAction"){

                if(preIsLogin() == false){

                    var addToCartJsonData = {};
                    addToCartJsonData["productUuid"] = productUuid;
                    addToCartJsonData["skuNo"] = skuNo;
                    addToCartJsonData["buyNum"] = buyNum;

                    sessionStorage.setItem("addToCartJsonData",JSON.stringify(addToCartJsonData));
                    mui.alert('当前用户未登录','提示',function(){
                        localStorage.setItem("comeAddress",location.href);
                        console.log(loginUrl);
                        window.location.href = loginUrl;
                    });

                    return;
                }

                if (buyNum > productStock) {
                    mui.alert("库存不够，本商品最多可购买数量为："+productStock);
                    return;
                }

                addtoCart(productUuid,customerUuid,skuNo,buyNum,token,sessionId);
            }


        });
        function getSkuNobyUuids(selectUuids){

            return 0;
            var skuNo = "";
            var url = MobilePath + 'jsonParam={"opeType":"selectSpec","map":{"productUuid":"'+productUuid+'","promotionUuid":"","selectUuids":"'+selectUuids+'"}}';
            $.ajax({
                url: url,
                type: "POST",
                dataType: "json",
                success:function(data){
                    var returnCode = data.return_code;
                    if (returnCode == 0) {
                        skuNo = data.selecSpceData.skuNo;
                    }
                }
            })

            return skuNo;

        }

        function getSelectUuids(){

            var selectUuids = "";
            $("#Specifications ul span.on").each(function(){

                if (selectUuids != "") {
                    selectUuids += '-'+$(this).attr("id");
                }else{
                    selectUuids += $(this).attr("id");
                }
            })

            return selectUuids;
        }
        function addtoCart(productUuid,customerUuid,skuNo,buyNum,token,sessionId){

            var url = MobilePath + 'jsonParam={"opeType":"addCart","map":{"productUuid":"'+productUuid+'","customerUuid":"'+customerUuid+'","attrIds":"'+skuNo+'","buyNum":"'+buyNum+'","token":"'+token+'","sessionId":"'+sessionId+'"}}';
            $.ajax({
                url: url,
                type: "POST",
                dataType: "json",
                success:function(data){
                    console.log(data);
                    var returnCode = data.return_code;

                    if (returnCode != "0") {
                        toLoginAjax(returnCode,location.href);
                    }else{
                        //debugger;
                        var cartNum = $("#productnumVal").val();
                        if(cartNum == "" || cartNum == null){
                            cartNum = 0;
                        }
                        cartNum = parseInt(cartNum) + parseInt(buyNum);
                        $("#productnumVal").val(cartNum);

                        if (cartNum == null || cartNum <= 0) {
                            $("#productnum").hide();
                            $("#productnum").html(0);
                        }else{
                            $("#productnum").show();
                            var cartVal = cartNum > 99 ? "99+" : cartNum;
                            $("#productnum").html(cartVal);
                        }
                    }
                }
            });
        }


    };
