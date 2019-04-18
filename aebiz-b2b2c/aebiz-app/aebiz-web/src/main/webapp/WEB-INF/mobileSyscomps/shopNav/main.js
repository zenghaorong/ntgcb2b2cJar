/**
 * Created by Xic on 2017/6/9.
 */
Comps.shopNav = function (options) {
    this.defineUrl = options.mapProps.defineUrl;
};
Comps.shopNav.prototype.init = function () {
    var _self = this;
    localStorage.setItem('defineUrl', _self.defineUrl);
    _self.loading();
};
Comps.shopNav.prototype.loading = function () {
        setAgoPageUrl();
        var storeUuid = Utils.getQueryString("storeUuid");
        var userId = localStorage.getItem("userId") || Utils.getQueryString("userId");
        var url = '';
        if(userId != "" && userId != null){
            url = MobilePath+'jsonParam={"opeType":"getStoreIndex","map":{"storeUuid":"' + storeUuid + '","customerUuid":"' + userId + '"}}';
        }else{
            url = MobilePath+'jsonParam={"opeType":"getStoreIndex","map":{"storeUuid":"' + storeUuid + '"}}';
        }
        $.ajax({
            url: url,
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

                    //商品橱窗
                    var spccList = data.store_productWindow;
                    for (var spc = 0; spc < spccList.length; spc++) {
                        var privilegeTypes = spccList[spc].privilegeTypes;
                        var zhuxiaowayStr = zhuxiaoway(privilegeTypes);
                        $("#dianpuss").append(
                            '<li class="pl-item spcc' + spc + '">' +
                            '  <a href="javascript:void(0)" class="pl-pic" onclick="productInfo(\'' + spccList[spc].uuid + '\')"><img src="../../../images/index/22.png"></a>' +
                            ' <div class="pl-info"><p class="baoyou pl-title">' + zhuxiaowayStr + '<font>韩版时尚女宽松短牛仔上衣阔腿短裤两件套</font></p>' +
                            '    <strong class="pl-price">￥198</strong><span class="pl-buycount">3387人付款</span>' +
                            '  </div>' +
                            ' </li>'
                        )
                        $('.spcc' + spc).find('a').find('img').attr('src', spccList[spc].imageUrl);
                        $('.spcc' + spc).find('font').html(spccList[spc].name);
                        $('.spcc' + spc).find('strong').html('￥' + spccList[spc].price);
                        $('.spcc' + spc).find('span').html(spccList[spc].saleCount + '人付款');

                        if(spc == spccList.length-1){
                            setListTyle('#dianpuss',1.42);
                        }
                    }
                    //热卖新品
                    var rmxpList = data.store_hotProductWindow;
                    for (var rm = 0; rm < rmxpList.length; rm++) {
                        var privilegeTypes = spccList[rm].privilegeTypes;
                        var zhuxiaowayStr = zhuxiaoway(privilegeTypes);
                        if (rm < 4) { //只展示4个
                            $("#dianpurmxp").append(
                                '<li class="pl-item rmxp' + rm + '">' +
                                '  <a href="javascript:void(0)" class="pl-pic" onclick="productInfo(\'' + rmxpList[rm].uuid + '\')"><img src="../../../images/index/22.png"></a>' +
                                ' <div class="pl-info"><p class="baoyou pl-title">' + zhuxiaowayStr + '<font>韩版时尚女宽松短牛仔上衣阔腿短裤两件套</font></p>' +
                                '    <strong class="pl-price">￥198</strong><span class="pl-buycount">3387人付款</span>' +
                                '  </div>' +
                                ' </li>'
                            )
                            $('.rmxp' + rm).find('a').find('img').attr('src', rmxpList[rm].imageUrl);
                            $('.rmxp' + rm).find('font').html(rmxpList[rm].name);
                            $('.rmxp' + rm).find('strong').html('￥' + rmxpList[rm].price);
                            $('.rmxp' + rm).find('span').html(rmxpList[rm].saleCount + '人付款');
                        }
                        if(rm == rmxpList.length-1){
                            setListTyle('#dianpurmxp',1.42);
                        }
                    }
                    //记录刷新前的效果
                    var shopIndexBigC = localStorage.getItem("shopIndexBigC");
                    if(shopIndexBigC != null && shopIndexBigC != ""){
                        //进入优惠券页面
                        //debugger;
                        $('.shouye').removeClass('action');
                        $('.shopindex').attr("style","display:none");

                        $('.youhusp').parent().addClass('action');
                        $('#youhuilist').attr("style",'display:block');
                        var userId = Utils.getQueryString("userId");
                        var storeUuid = Utils.getQueryString("storeUuid");
                        $("#youhuinowpage").val("1");
                        $("#youhuitotalpage").val("");
                        youhuiquanajax(storeUuid, userId, "1", "20");
                        localStorage.removeItem("shopIndexBigC");
                    }
                }
            },
            error: function(data) {
                mui.alert(data.message,'提示');
            }
        })

        /**全部商品内接口开始*/
        //全部商品
        $(".quanbusp").click(function() {
            $("#quanbushangpinType").val("综合");
            $("#quanbushangpinnowpage").val("1");
            $("#quanbushangpintotalpage").val("");
            var storeUuid = Utils.getQueryString("storeUuid");
            //商户ID searchKey pageNow pageShow orderBy orderType
            quanbushangpin(storeUuid, "", "", "", "", "", "", "", "");
        })
        $(".zongheshangpun").click(function() {
            $("#quanbushangpinType").val("综合");
            $("#quanbushangpinnowpage").val("1");
            $("#quanbushangpintotalpage").val("");
            var storeUuid = Utils.getQueryString("storeUuid");
            quanbushangpin(storeUuid, "", "", "", "", "", "", "", "");
        })
        $(".qbspxiaoliang").click(function() {
            $("#quanbushangpinType").val("销量");
            $("#quanbushangpinnowpage").val("1");
            $("#quanbushangpintotalpage").val("");
            var storeUuid = Utils.getQueryString("storeUuid");
            quanbushangpin(storeUuid, "", "", "", "", "mount", "", "", "");
        })
        $(".qbspzuixin").click(function() {
            $("#quanbushangpinType").val("最新");
            $("#quanbushangpinnowpage").val("1");
            $("#quanbushangpintotalpage").val("");
            var storeUuid = Utils.getQueryString("storeUuid");
            quanbushangpin(storeUuid, "", "", "", "", "shelveTime", 1, "", "");
        })
        //价格顺序
        $(".pricexx").click(function() {
            $("#quanbushangpinType").val("价格");
            $("#quanbushangpinnowpage").val("1");
            $("#quanbushangpintotalpage").val("");
            var sortType = $("#priceIndex").val();
            if (sortType == "1") {
                quanbushangpin(storeUuid, "", "", "", "", "price", sortType, "", "");
                $("#priceIndex").val("0");
            } else {
                quanbushangpin(storeUuid, "", "", "", "", "price", sortType, "", "");
                $("#priceIndex").val("1");
            }

        })
        /**全部商品内接口结束*/


        /**优惠券接口开始*/
        //优惠券
        $(".youhusp").click(function() {
            var userId = Utils.getQueryString("userId");
            var storeUuid = Utils.getQueryString("storeUuid");
            $("#youhuinowpage").val("1");
            $("#youhuitotalpage").val("");
            youhuiquanajax(storeUuid, userId, "1", "20")
        })
        /**优惠券接口结束*/

        /**商品分类的接口**/
        $(".categorysp").click(function() {
            $("#categorynowpage").val("1");
            $("#categorytotalpage").val("0");
            var storeUuid = Utils.getQueryString("storeUuid");
            var url = MobilePath + 'jsonParam={"opeType":"getStoreCategory","map":{"storeUuid":"' + storeUuid + '"}}';
            $('.loader').show();
            $.ajax({
                url: url,
                type: "POST",
                dataType: "json",
                success: function(data) {
                    //console.log(url);
                    if (data.return_code != 0) {
                        mui.alert(data.message, '提示');
                    } else {
                        //分类
                        var categoryList = data.categorys;
                        $('.loader').hide();
                        $('#subcateg').html('');
                        //初始化分类数据
                        for (var c = 0; c < categoryList.length; c++) {
                            $("#pageCategoryId").val(categoryList[c].uuid);
                            $('#subcateg').append('<span class="sub subcategor" data-id="' + categoryList[c].uuid + '" id="' + categoryList[c].uuid + '_' + c + '"><em >' + categoryList[c].categoryName + '</em></span>');
                        }

                        //默认加载第一块
                        renderCateList(categoryList[0].uuid);
                        $('.subcategor').eq(0).find('em').addClass("action");
                    }
                }

            })

        })

        //商品分类列表加载
        function renderCateList(uuid_c){
            var productList = categorysahngpin(storeUuid, "", uuid_c, "", "", "", "", "", "", "");
            var data = productList.products;

            if (typeof data != 'undefined' && data != null && data != "" && data.length > 0) {
                $(".categoryfl").html('');
                for (var spfl = 0; spfl < data.length; spfl++) {
                    var privilegeTypes = data[spfl].privilegeTypes;
                    var zhuxiaowayStr = zhuxiaoway(privilegeTypes);
                    $(".categoryfl").append(
                        '<li class="pl-item categoryflli' + spfl + '">' +
                        '  <a href="javascript:void(0)" onclick="productInfo(\''+data[spfl].uuid+'\')" class="pl-pic"><img src="' + data[spfl].pic + '"></a>' +
                        ' <div class="pl-info"><p class="baoyou pl-title">' + zhuxiaowayStr + '<font>' + data[spfl].name + '</font></p>' +
                        '    <strong class="pl-price">￥' + data[spfl].price + '</strong><span class="pl-buycount">' + data[spfl].mount + '人付款</span>' +
                        '  </div>' +
                        ' </li>'
                    )

                    if (spfl == data.length - 1) {
                        setListTyle('.categoryfl',1.42);
                    }
                }
            } else {
                $(".categoryfl").html('');
                $(".categoryfl").append('<div class="pic"><img src="../../../images/index/44.png"></div>');
            }
        }

        $(document).on('click', '.subcategor', function() {
            $(this).find('em').addClass("action");
            $(this).siblings().find('em').removeClass("action");
            var uuid_c = $(this).attr('data-id');
            var uuid_d = $(this).attr('id');
            $('#pageCategoryId').val(uuid_d);
        })



        /**商品分类的接口结束*/
        $(".shop-nav li").click(function() {
            num = $(this).index();
            //alert(num);
            $(".shop-nav li span").removeClass("action");
            $(this).find("span").toggleClass("action");
            $(".shop-subnav li").eq(num).css("display", "block").siblings().css("display", "none");
            //alert($(".Modular").size());
            $(".Modular").css("display", "none").eq(num).css("display", "block");
            // setListTyle(1.42);
        });
        $(".shop-subnav li .sub em").click(function() {
            $(".shop-subnav li .sub em").removeClass("action");
            $(this).toggleClass("action");
        });
        $(".accept button").click(function() {
            $(this).parent().parent().css("display", "none");
        });

        /********分页molde***************/
        $(window).scroll(function() {
            if ($(window).scrollTop() == $(document).height() - $(window).height()) {
                //判断是哪个大分类下面
                var parentcate = $('.shop-nav .action').text();
                if (parentcate == "首页") {
                    return;
                } else if (parentcate == "全部") { //全部商品
                    //二级分类
                    var cate = $('#quanbushangpinType').val();
                    if (cate == "综合") {
                        var nowPage = Number($('#quanbushangpinnowpage').val());
                        nowPage = nowPage + 1;
                        var totalPage = $('#quanbushangpintotalpage').val();
                        if (totalPage != null && totalPage != '' && typeof totalPage != 'undfinded') {
                            if (nowPage > totalPage) {
                                mui.alert('没有更多商品');
                            }
                        } else {
                            var storeUuid = Utils.getQueryString("storeUuid");
                            //商户ID searchKey pageNow pageShow orderBy orderType
                            pagequanbushangpin(storeUuid, "", "", nowPage, "20", "", "", "", "");
                        }
                    } else if (cate == '销量') {
                        var nowPage = Number($('#quanbushangpinnowpage').val());
                        nowPage = nowPage + 1;
                        var totalPage = $('#quanbushangpintotalpage').val();
                        if (totalPage != null && totalPage != '' && typeof totalPage != 'undfinded') {
                            if (nowPage > totalPage) {
                                mui.alert('没有更多商品');
                            }
                        } else {
                            var storeUuid = Utils.getQueryString("storeUuid");
                            //商户ID searchKey pageNow pageShow orderBy orderType
                            pagequanbushangpin(storeUuid, "", "", nowPage, "20", "mount", "", "", "");
                        }
                    } else if (cate == '最新') {
                        var nowPage = Number($('#quanbushangpinnowpage').val());
                        nowPage = nowPage + 1;
                        var totalPage = $('#quanbushangpintotalpage').val();
                        if (totalPage != null && totalPage != '' && typeof totalPage != 'undfinded') {
                            if (nowPage > totalPage) {
                                mui.alert('没有更多商品');
                            }
                        } else {
                            var storeUuid = Utils.getQueryString("storeUuid");
                            //商户ID searchKey pageNow pageShow orderBy orderType
                            pagequanbushangpin(storeUuid, "", "", nowPage, "20", "shelveTime", 0, "", "");
                        }
                    } else { //价格
                        var nowPage = Number($('#quanbushangpinnowpage').val());
                        nowPage = nowPage + 1;
                        var totalPage = $('#quanbushangpintotalpage').val();
                        if (totalPage != null && totalPage != '' && typeof totalPage != 'undfinded') {
                            if (nowPage > totalPage) {
                                mui.alert('没有更多商品');
                            }
                        } else {
                            var storeUuid = Utils.getQueryString("storeUuid");
                            // pagequanbushangpin(storeUuid,"","",nowPage,"20","shelveTime",0,"","");
                            var sortType = $("#priceIndex").val();
                            if (sortType == "0") {
                                pagequanbushangpin(storeUuid, "", "", nowPage, "20", "price", sortType, "", "");
                            } else {
                                pagequanbushangpin(storeUuid, "", "", nowPage, "20", "price", sortType, "", "");
                            }
                        }
                    }
                } else if (parentcate == "分类") {
                    return;
                } else if (parentcate == "优惠") {
                    var nowPage = Number($('#youhuinowpage').val());
                    nowPage = nowPage + 1;
                    var totalPage = $('#youhuitotalpage').val();
                    if (nowPage > totalPage) {
                        mui.alert('亲,没有了哦');
                    } else {
                        var userId = Utils.getQueryString("userId");
                        var storeUuid = Utils.getQueryString("storeUuid");
                        pageyouhuiquanajax(storeUuid, userId, nowPage, "20");
                    }


                }
            }
        });
        /*********分页molde结束**************/
    /**************分页方法**************/
    //全部商品ajax方法分页
    function pagequanbushangpin(storeUuid, searchKey, categoryUuid, nowPage, pageShow, sortBy, sortType, needStoreInfo, wm) {
        var wm = wmModelStr(nowPage, pageShow, "", "");
        if (needStoreInfo == "") {
            needStoreInfo = 1;
        }
        var url = MobilePath + 'jsonParam={"opeType":"storeSearch","map":{"storeUuid":"' + storeUuid + '","searchKey":"' + searchKey + '","categoryUuid":"' + categoryUuid + '","nowPage":"' + nowPage + '","pageShow":"' + pageShow + '","sortBy":"' + sortBy + '","sortType":"' + sortType + '","needStoreInfo":"' + needStoreInfo + '","wm":' + wm + '}}';
        $.ajax({
            url: url,
            type: "POST",
            dataType: "json",
            async: false,
            success: function(data) {
                if (data.return_code != "0") {
                    mui.alert(data.message, '提示');
                } else {
                    //模拟数据展示
                    var productList = data.products;
                    if (productList == null || productList.length == 0) {
                        mui.alert('没有更多商品');
                    } else {
                        var pageModel = data.wm;
                        for (var qbsp = 0; qbsp < productList.length; qbsp++) {
                            var privilegeTypes = productList[qbsp].privilegeTypes;
                            var zhuxiaowayStr = zhuxiaoway(privilegeTypes);
                            var pageqbsp = nowPage * 20 + qbsp;
                            $("#quanbusp").append(
                                '<li class="pl-item' + pageqbsp + '">' +
                                '  <a href="javascript:void(0)" class="pl-pic" onclick="productInfo(\'' + productList[qbsp].uuid + '\')"><img src="' + productList[qbsp].pic + '"></a>' +
                                ' <div class="pl-info"><p class="baoyou pl-title">' + zhuxiaowayStr + '<font>' + productList[qbsp].name + '</font></p>' +
                                '    <strong class="pl-price">￥' + productList[qbsp].price + '</strong><span class="pl-buycount">' + productList[qbsp].mount + '人付款</span>' +
                                '  </div>' +
                                ' </li>'
                            )

                            if (qbsp == productList.length - 1) {
                                setListTyle('#quanbusp',1.42);
                            }
                        }
                        $('#quanbushangpinnowpage').val(nowPage);
                        $('#quanbushangpintotalpage').val(pageModel.totalPage);
                    }
                }
            }

        })
    }

//优惠券ajax请求方法分页
    function pageyouhuiquanajax11(storeUuid, customerUuid, nowPage, pageShow) {
        var url = window.basePath + 'jsonParam={"opeType":"getStoreCoupons","map":{"storeUuid":"' + storeUuid + '","customerUuid":"' + customerUuid + '","nowPage":"' + nowPage + '","pageShow":"' + pageShow + '"}}';
        $.ajax({
            url: url,
            type: "POST",
            dataType: "json",
            success: function(data) {
                if (data.return_code != "0") {
                    mui.alert(data.message, '提示');
                } else {
                    //优惠券
                    var promotionList = data.coupons;
                    if (promotionList != null && promotionList != "" && promotionList.length > 0) {
                        for (var ptl = 0; ptl < promotionList.length; ptl++) {
                            var pageptl = nowPage * 20 + ptl;
                            var temparea = promotionList[ptl].startTime.substring(0,10).replace("-",".")+"-"+promotionList[ptl].endTime.substring(0,10).replace("-",".");
                            $("#Coupon").append(
                                '<tr>' +
                                '<td class="td1"><div style=" width:5rem; overflow:hidden;"><b id="youhuiname' + pageptl + '">'+promotionList[ptl].couponTypeName+'</b>'+
                                '<p id="youhuiyuan' + pageptl + '">￥'+promotionList[ptl].denomination+'</p></div></td>' +
                                '<td class="td2"><p id="youhuitime' + pageptl + '">'+temparea+'</p>' +
                                '<input type="button" onclick="huoquyouhuiquan(\'youhuitime'+ptl+'\',\''+promotionList[ptl].uuid +'\')" value="立即领取">' +
                                '</td></tr>'
                            );
                            //判断优惠券是否有效
                            var endTime = promotionList[ptl].endTime;
                            endTime = (new Date(endTime)).getTime(); //得到毫秒数
                            var nowTime = new Date().getTime();
                            if(nowTime>endTime){
                                $("#youhuitime"+ptl).parent().find('input').val("已结束");
                                $("#youhuitime"+ptl).parent().find('input').css("background","#999");
                                $("#youhuitime"+ptl).parent().find('input').removeAttr('onclick');
                            }
                            if (ptl == promotionList.length - 1) {
                                setListTyle('#Coupon',1.42);
                            }
                        }
                        var pageModel = data.wm;
                        $("#youhuinowpage").val(nowPage);
                        $('#youhuitotalpage').val(pageModel.totalPage);
                    } else {
                        mui.alert('没有更多优惠券了');
                    }


                }
            }

        })
    }




    /************分页方法结束**************/

//全部商品ajax方法
    function quanbushangpin(storeUuid, searchKey, categoryUuid, nowPage, pageShow, sortBy, sortType, needStoreInfo, wm) {
        if(nowPage == ""){
            nowPage ="1";
        }
        if(pageShow == ""){
            pageShow ="20";
        }
        var wm = wmModelStr(nowPage, pageShow, "", "");
        if (needStoreInfo == "") {
            needStoreInfo = 1;
        }
        var url = window.basePath + 'jsonParam={"opeType":"storeSearch","map":{"storeUuid":"' + storeUuid + '","searchKey":"' + searchKey + '","categoryUuid":"' + categoryUuid + '","nowPage":"' +nowPage + '","pageShow":"20","sortBy":"' + sortBy + '","sortType":"' + sortType + '","needStoreInfo":"' + needStoreInfo + '","wm":' + wm + '}}';

        $('.loader').show();
        $(".product-list").hide();
        $.ajax({
            url: url,
            type: "POST",
            dataType: "json",
            success: function(data) {
                console.log(url);
                console.log(data);
                if (data.return_code != "0") {
                    mui.alert(data.message, '提示');
                } else {
                    //模拟数据展示
                    $('.loader').hide();
                    $(".product-list").show();

                    var productList = data.products;
                    var pageModel = data.wm;
                    $("#quanbusp").html("");
                    for (var qbsp = 0; qbsp < productList.length; qbsp++) {
                        var privilegeTypes = productList[qbsp].privilegeTypes;
                        var zhuxiaowayStr = zhuxiaoway(privilegeTypes);
                        $("#quanbusp").append(
                            '<li class="pl-item' + qbsp + '">' +
                            '  <a href="javascript:void(0)" class="pl-pic" onclick="productInfo(\'' + productList[qbsp].uuid + '\')"><img src="' + productList[qbsp].pic + '"></a>' +
                            ' <div class="pl-info"><p class="baoyou pl-title">' + zhuxiaowayStr + '<font>' + productList[qbsp].name + '</font></p>' +
                            '    <strong class="pl-price">￥' + productList[qbsp].price + '</strong><span class="pl-buycount">' + productList[qbsp].mount + '人付款</span>' +
                            '  </div>' +
                            ' </li>'
                        )

                        if (qbsp == productList.length - 1) {
                            setListTyle('#quanbusp',1.42);
                        }
                    };


                }
            }

        })
    }


//优惠券ajax请求方法
    function youhuiquanajax(storeUuid, customerUuid, nowPage, pageShow) {
        if (nowPage == "") {
            nowPage = "1";
        }
        if (pageShow == "") {
            pageShow = "20";
        }
        var url = MobilePath + 'jsonParam={"opeType":"getStoreCoupons","map":{"storeUuid":"' + storeUuid + '","customerUuid":"' + customerUuid + '","nowPage":"' + nowPage + '","pageShow":"' + pageShow + '"}}';
        $.ajax({
            url: url,
            type: "POST",
            dataType: "json",
            success: function(data) {
                if (data.return_code != "0") {
                    mui.alert(data.message, '提示');
                } else {
                    //优惠券
                    var promotionList = data.coupons;
                    $("#Coupon").html("");
                    if(promotionList != null && promotionList!="" && promotionList.length>0){
                        for (var ptl = 0; ptl < promotionList.length; ptl++) {
                            console.log(promotionList[ptl].startTime.substring(0,10))
                            var temparea = promotionList[ptl].startTime.substring(0,10).replace(/-/g,".")+"-"+promotionList[ptl].endTime.substring(0,10).replace(/-/g,".");
                            $("#Coupon").append(
                                '<tr>' +
                                '<td class="td1"><div style=" width:5rem; overflow:hidden;"><b id="youhuiname' + ptl + '">'+promotionList[ptl].couponTypeName+'</b>'+
                                '<p id="youhuiyuan' + ptl + '">￥'+promotionList[ptl].denomination+'</p></div></td>' +
                                '<td class="td2"><p id="youhuitime' + ptl + '">'+temparea+'</p>' +
                                '<input type="button" onclick="huoquyouhuiquan(\'youhuitime'+ptl+'\',\''+promotionList[ptl].uuid +'\')"  name="" value="立即领取">' +
                                '</td></tr>'
                            );

                            //判断优惠券是否有效
                            //var endTime = "2016-09-22 18:00:00";
                            var endTime = promotionList[ptl].endTime;
                            endTime = (new Date(endTime)).getTime(); //得到毫秒数
                            var nowTime = new Date().getTime();
                            if(nowTime>endTime){
                                $("#youhuitime"+ptl).parent().find('input').val("已结束");
                                $("#youhuitime"+ptl).parent().find('input').css("background","#999");
                                $("#youhuitime"+ptl).parent().find('input').removeAttr('onclick');
                            }


                            if (ptl == promotionList.length - 1) {
                                setListTyle(1.42);
                            }

                        }
                    }else{
                        $("#Coupon").append('<div class="pic"><img src="../../../images/index/44.png"></div>')
                    }

                    var pageModel = data.wm;
                    $("#youhuinowpage").val(nowPage);
                    $('#youhuitotalpage').val(pageModel.totalPage);

                }
            }

        })
    }


//获取优惠券
    function huoquyouhuiquan(yhqid,couponDetailUuid) {
        var userId = Utils.getQueryString("userId");
        var flag = false;
        if (userId == null || userId == "") {
            flag = true;
            mui.alert('当前用户未登录', '提示', function() {
                localStorage.setItem("comeAddress", window.location.href);
                window.location.href = localStorage.getItem('defineUrl');
            });
        }
        if (flag) {
            return;
        }
        var token = localStorage.getItem("token");
        var sessionId = localStorage.getItem("sessionId");
        var url = MobilePath + 'jsonParam={"opeType":"receiveCoupon","map":{"customerUuid":"' + userId + '","couponDetailUuid":"' + couponDetailUuid + '","token":"' + token + '","sessionId":"' + sessionId + '"}}'
        $.ajax({
            url: url,
            type: "POST",
            dataType: "json",
            async: false,
            success: function(data) {
                if (data.return_code == "0") {
                    mui.alert(data.message, '提示',function(){

                        localStorage.setItem("shopIndexBigC","优惠");
                        //刷新当前页面
                        location.reload();
                    })

                } else {
                    mui.alert(data.message,'提示');
                }
            }
        })
    }
    /**分类ajax获取店铺商品*/
    function categorysahngpin(storeUuid, searchKey, categoryUuid, nowPage, pageShow, sortBy, sortType, needStoreInfo, wm) {
        var wm = wmModelStr(nowPage, pageShow, "", "");
        if (needStoreInfo == "") {
            needStoreInfo = 1;
        }
        var url = MobilePath + 'jsonParam={"opeType":"storeSearch","map":{"storeUuid":"' + storeUuid + '","searchKey":"' + searchKey + '","categoryUuid":"' + categoryUuid + '","nowPage":"' + nowPage + '","pageShow":"' + pageShow + '","sortBy":"' + sortBy + '","sortType":"' + sortType + '","needStoreInfo":"' + needStoreInfo + '","wm":' + wm + '}}';
        //console.log(url);
        var dataObj;
        $.ajax({
            url: url,
            type: "POST",
            dataType: "json",
            async: false,
            success: function(data) {
                if (data.return_code != "0") {
                    mui.alert(data.message, '提示');
                } else {
                    //模拟数据展示
                    dataObj = data;
                }
            }

        })
        return dataObj;

    }


}