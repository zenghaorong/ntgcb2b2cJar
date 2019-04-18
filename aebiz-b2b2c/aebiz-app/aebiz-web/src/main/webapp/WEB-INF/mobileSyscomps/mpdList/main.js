/**
 * Created by wenyu on 2017/5/25.
 */
Comps.mpdList = function (options) {
    this.options = options.mapProps;
};

Comps.mpdList.prototype.searchCondition = function (searchType) {
    //返回搜索的信息
    var data = "";
    var searchType = Utils.getQueryString("searchType");
    var searchTypeParam = Utils.getQueryString("searchTypeParam");
    $(".screenways").children().eq(0).find('span').addClass("action");

    if(searchTypeParam == ''){
        searchTypeParam = "1"
    }

    if (searchType == "1") {//店铺搜索
        data = this.searchProduct("1", "20", searchTypeParam, "", "", "", "", "", "", "");
    } else if (searchType == "2") {//分类搜索
        data = this.searchProduct("1", "20", "", searchTypeParam, "", "", "", "", "", "");
    } else if (searchType == "3") {//品牌
        var attrValue = "attributeBrand=" + searchTypeParam;
        data = this.searchProduct("1", "20", "", "", "", "", "", "", "", attrValue);
    } else if (searchType == "4") {//关键词
        data = this.searchProduct("1", "20", searchTypeParam, "", "", "", "", "", "", "");
    } else { //商品
        data = this.searchProduct("1", "20", searchTypeParam, "", "", "", "", "", "", "");
    }

    return data;
}

Comps.mpdList.prototype.init = function () {
    $('.defaultPng').hide();
    $('.m-pdlist-wrap').show();
    //判断展现形式
    if(this.options.showType == '0'){
        $('#indexss').addClass('showLine')
    }else{
        $('#indexss').removeClass('showLine')
    }
    setAgoPageUrl();

    var _self = this;
    _self.toDetailsPage();


    //刷选页面
    //初始化筛选属性
    //头上参数
    var searchKey = Utils.getQueryString("searchKeyWord");
    $(".searchPro").html(searchKey);

    //返回搜索结果
    var data = this.searchCondition();

    //根据搜索数据展示搜索结果
    this.dataView(data, "0");

    //根据属性刷新当前产品列表页
    $(".selectValue").click(function () {
        var selectPorperityValue = $("#selectPorperityValue").val();
        var value = $(this).html();
        var key = $('.porperity_name .on').find('span').html();
        var k = key.indexOf('<');
        key = key.substring(0, k);
        if (selectPorperityValue != null && selectPorperityValue != "") {
            if (selectPorperityValue.indexOf(key) < 0) {//没找到
                selectPorperityValue = selectPorperityValue + '&' + key + '=' + value;
            }
        } else {
            selectPorperityValue = key + '=' + value;
        }

        var searchKey = Utils.getQueryString("searchKeyWord");
        $(".searchPro").html(searchKey);
        //返回搜索的信息
        var data;
        var searchType = Utils.getQueryString("searchType");
        var searchTypeParam = Utils.getQueryString("searchTypeParam");

        if (searchType == "1") {//店铺搜索
            data = _self.searchProduct("1", "20", searchTypeParam, "", "", "", "", "", "", selectPorperityValue);
        } else if (searchType == "2") {//分类搜索
            data = _self.searchProduct("1", "20", "", searchTypeParam, "", "", "", "", "", selectPorperityValue);
        } else if (searchType == "3") {//品牌
            var attrValue = "attributeBrand=" + searchTypeParam + '&' + selectPorperityValue;
            data = _self.searchProduct("1", "20", "", "", "", "", "", "", "", attrValue);
        } else if (searchType == "4") {//关键词
            data = _self.searchProduct("1", "20", searchTypeParam, "", "", "", "", "", "", selectPorperityValue);
        } else { //商品
            data = _self.searchProduct("1", "20", searchTypeParam, "", "", "", "", "", "", selectPorperityValue);
        }
        //替换当前html
        var productList = data.products;
        var htmlPro = '';
        $('#indexss').html(htmlPro);
        if (productList != null && productList.length > 0) {
            for (var p = 0; p < productList.length; p++) {
                var privilegeTypes = productList[p].privilegeTypes;
                var zhuxiaowayStr = zhuxiaoway(privilegeTypes);
                htmlPro = htmlPro +
                    ' <li class="pl-item spcc' + p + ' shangping">' +
                    '<a href="javascript:void(0)" class="pl-pic" data-uuid="'+productList[p].uuid+'"><img src="' + productList[p].pic + '"></a>' +
                    '<div class="pl-info">' + zhuxiaowayStr + '<p class="baoyou pl-title">' + productList[p].name + '</p>' +
                    '<strong class="pl-price">￥' + productList[p].price + '</strong><span class="pl-buycount">' + productList[p].mount + '人付款</span>' +
                    '</div></li>'
            }
        } else {
            //空白页面
            htmlPro = htmlPro + ' <div class="blank"><img src="../../../images/index/97.png"></div>';
        }
        $('#indexss').append(htmlPro);

        _self.toDetailsPage();

    })

    //刷选确认选择条件确认选择条件
    $(document).on('click', ".sureSelect", function () {
        var searchTypeParam = Utils.getQueryString("searchTypeParam");
        //品牌
        var attrValue = "";
        var pingpaiList = $('.pingpai').find('span');
        var pingpai = "";
        pingpaiList.each(function (index, obj) {
            if ($(obj).hasClass("action")) {
                pingpai = $(obj).html()
            }
        })
        if (pingpai != "") {
            attrValue = attrValue + "attributeBrand=" + pingpai;
        }

        //商品分类
        var productCategoryList = $('.productCategory').find('span');
        var productCategory = "";
        //productCategoryx
        var uuid = "";
        productCategoryList.each(function (index, obj) {
            if ($(obj).hasClass("action")) {
                uuid = $("#productCategory" + index).val();
            }
        })

        //价格
        var tdprice = $(".priceArea");
        var startPrice = "";
        var endPrice = "";
        tdprice.each(function (index, obj) {
            if ($(obj).hasClass("action")) {
                if ("0" == index) {
                    endPrice = $(obj).html();
                    startPrice = 0;
                } else if (index == tdprice.length - 1) {
                    startPrice = $(obj).html();
                    endPrice = 100000000;
                } else {
                    price = $(obj).html();
                    var pl = price.split("-")
                    startPrice = pl[0];
                    endPrice = pl[1];
                }

            }
        })

        var searchKey = Utils.getQueryString("searchKeyWord");
        $(".searchPro").html(searchKey);
        var data;
        var searchType = Utils.getQueryString("searchType");

        var param = ""
        if (uuid != null && uuid != "") {
            param = uuid;
        } else {
            param = searchTypeParam
        }
        if (searchType == "1") {//店铺搜索
            data = _self.searchProduct("1", "20", "", param, "", startPrice, endPrice, "", "", attrValue);
        } else if (searchType == "2") {//分类搜索
            data = _self.searchProduct("1", "20", "", param, "", startPrice, endPrice, "", "", attrValue);
        } else if (searchType == "3") {//品牌
            data = _self.searchProduct("1", "20", "", param, "", startPrice, endPrice, "", "", attrValue);
        } else if (searchType == "4") {//关键词
            data = _self.searchProduct("1", "20", searchTypeParam, "", "", startPrice, endPrice, "", "", attrValue);
        } else { //商品
            data = _self.searchProduct("1", "20", searchTypeParam, "", "", startPrice, endPrice, "", "", attrValue);
        }

        $("#shaixuan").css('display', 'none');
        $(".m-product-list,#condition,#object").css('display', 'block');
        $(".shaixuan").removeClass("action");

        //根据搜索数据展示搜索结果
        _self.dataViewBrush("0", data, function () {
            //空白页面
            $('#indexss').append('<div class="blank"><img src="'+ContextPath+'/static/mobileTempAssets/defaultTemplate/images/index/97.png"></div>');
        })

    })

    //重置
    $(".reset").click(function () {
        localStorage.removeItem('keysPorpertityValue');
        localStorage.removeItem('priceArea');
        localStorage.removeItem('selectCategory');

        $('.catePrices .action').removeClass("action");
        //$('.catePrices ').find('td').eq(0).addClass("action");

        var pp = $('.pingpai .tab').find('span');
        pp.each(function (index, obj) {
            $(obj).removeClass("action");
        });
        // $('.pingpai .tab').find('span').eq(0).addClass("action");

        var pc = $('.productCategory .tab').find('span');
        pc.each(function (indexp, objp) {
            $(objp).removeClass("action");
        });
        //$('.productCategory .tab').find('span').eq(0).addClass("action");

    })

    //选择价格区间
    $(document).on('click', '.catePrices .priceArea', function () {
        var otherdom = $('.catePrices .priceArea .action');
        otherdom.removeClass("action");
        $(this).addClass("action");
        $(this).siblings().removeClass('action');
    })

    //选择品牌
    $(document).on('click', '.pingpai .tab .sub', function () {
        var otherdom = $('.pingpai .tab .action');
        otherdom.removeClass("action");
        $(this).addClass("action");
    });

    //选择商品分类
    $(document).on('click', '.productCategory .tab .sub', function () {
        var otherdom = $('.productCategory .tab .action');
        otherdom.removeClass("action");
        $(this).addClass("action");
    });


    $(".screenways .shaixuan").click(function () {
        $(".m-product-list,#condition,#object").css('display', 'none');
        $("#shaixuan").css('display', 'block');
        $("#screentype").val("筛选");
    });

    $(".screenways .activ").click(function () {
        $("#shaixuan").css('display', 'none');
        $(".m-product-list,#condition,#object").css('display', 'block');
        var type = $(this).html().substring(0, 2);
        $("#screentype").val(type);
    });

    $("#screening .screenways li span").click(function () {
        var num = $(this).index();
        $("#screening .screenways li span").removeClass("action");
        $(this).addClass("action");
        // $("#object ul").removeClass("action");
        // $("#condition div li").removeClass("on");
    });

    $(document).on('click', '#condition div li', function () {
        var num = $(this).index();
        $(this).toggleClass("on").siblings().removeClass("on");
        //$("#object ul").removeClass("action");
        $("#object ul").eq(num).toggleClass("action").siblings().removeClass("action");
    })
    $(document).on('click', '#object ul li', function () {
        $("#object ul").removeClass("action");
        $("#condition div li").removeClass("on");
    })
    $(document).on('click', '#screening .screenways li span', function () {
        $("#screening .screenways li span").removeClass("action")
        $(this).addClass("action");
    })
    $(document).on("click", "#screening .screenways li span.pricexx", function () {
        $("#screening .screenways li span").toggleClass("on");
    })


    //三大条件切换
    $('.activ').click(function () {
        var sortby = $(this).html();
        var sortBy = "";
        var sortType = "";
        if (sortby == "销量") {
            sortBy = "mount";
        } else if (sortby == "综合") {
            sortBy = "";
        } else {
            //价格
            sortBy = "price"
            var descType = $("#sortType").val();
            if (descType == "0") {
                sortType = "0";
                $("#sortType").val("1");
            } else {
                sortType = "1";
                $("#sortType").val("0")
            }
        }
        var data;
        var searchKey = Utils.getQueryString("searchKeyWord");
        $(".searchPro").html(searchKey);

        //返回搜索的信息
        var searchType = Utils.getQueryString("searchType");
        var searchTypeParam = Utils.getQueryString("searchTypeParam");
        if (searchType == "1") {//店铺搜索
            data = _self.searchProduct("1", "20", searchTypeParam, "", "", "", "", sortBy, sortType, "");
        } else if (searchType == "2") {//分类搜索
            data = _self.searchProduct("1", "20", "", searchTypeParam, "", "", "", sortBy, sortType, "");
        } else if (searchType == "3") {//品牌
            var attrValue = "attributeBrand=" + searchTypeParam;
            data = _self.searchProduct("1", "20", "", "", "", "", "", sortBy, sortType, attrValue);
        } else if (searchType == "4") {//关键词
            data = _self.searchProduct("1", "20", searchTypeParam, "", "", "", "", sortBy, sortType, "");
        } else { //商品
            data = _self.searchProduct("1", "20", searchTypeParam, "", "", "", "", sortBy, sortType, "");
        }
        _self.dataView(data, "0");
    })


    /*****分页model开始*******/
    $(window).scroll(function () {
        if ($(window).scrollTop() == $(document).height() - $(window).height()) {

            var screentype = $("#screentype").val();
            if (screentype == "筛选") {
                return false;
            }
            var nowPage = Number($("#searchNowPage").val());
            nowPage = nowPage + 1;
            var totalPage = $('#searchTotalPage').val();

            if (nowPage > totalPage) {
                mui.alert('没有更多商品');
            } else {
                //1.查看当前是那种排序
                //var sortby = $(this).html();
                var sortBy = "";
                var sortType = "";
                if (screentype == "销量") {
                    sortBy = "mount";
                } else if (screentype == "综合") {
                    sortBy = "";
                } else {
                    //价格
                    sortBy = "price"
                    var descType = $("#sortType").val();
                    if (descType == "0") {
                        sortType = "0";
                        $("#sortType").val("1");
                    } else {
                        sortType = "1";
                        $("#sortType").val("0")
                    }
                }

                var data;
                var searchKey = Utils.getQueryString("searchKeyWord");
                //返回搜索的信息
                var searchType = Utils.getQueryString("searchType");
                var searchTypeParam = Utils.getQueryString("searchTypeParam");
                if (searchType == "1") {//店铺搜索
                    data = _self.searchProduct(nowPage, "20", searchTypeParam, "", "", "", "", sortBy, sortType, "");
                } else if (searchType == "2") {//分类搜索
                    data = _self.searchProduct(nowPage, "20", "", searchTypeParam, "", "", "", sortBy, sortType, "");
                } else if (searchType == "3") {//品牌
                    var attrValue = "attributeBrand=" + searchTypeParam;
                    data = _self.searchProduct(nowPage, "20", "", "", "", "", "", sortBy, sortType, attrValue);
                } else if (searchType == "4") {//关键词
                    data = _self.searchProduct(nowPage, "20", searchTypeParam, "", "", "", "", sortBy, sortType, "");
                } else { //商品
                    data = _self.searchProduct(nowPage, "20", searchTypeParam, "", "", "", "", sortBy, sortType, "");
                }
                _self.dataViewPage(data, "1");
            }

        }
    });

    /*****分页model结束*******/
}

//获取热门关键词
Comps.mpdList.prototype.getHotkey = function () {
    var url = window.basePath + 'jsonParam={"opeType":"getHotKey"}';
    var hotKeyWords;
    $.ajax({
        url: url,
        type: "POST",
        dataType: "json",
        async: false,
        success: function (data) {
            //debugger;
            console.log('获取热搜词');
            console.log(data);
            console.log(url);
            if (data.return_code != "0") {
                mui.alert(data.message, '提示');
            } else {
                hotKeyWords = data.hotkey;
            }
        }

    })
    return hotKeyWords;
}

Comps.mpdList.prototype.getLikeKey = function (key) {
    //debugger;
    var url = window.basePath + 'jsonParam={"opeType":"getLikeKey","map":{"key":"' + key + '"}}';
    var LikeKey;
    $.ajax({
        url: url,
        type: "POST",
        dataType: "json",
        async: false,
        success: function (data) {
            //debugger;
            if (data.return_code != "0") {
                mui.alert(data.message, '提示');
            } else {
                LikeKey = data.productNameList;
            }
        }

    })
    return LikeKey;
}

Comps.mpdList.prototype.searchSearchKey = function (searchKey) {
    //保存历史记录
    var hiskey = localStorage.getItem("hiskey")
    var historyWords = localStorage.getItem("historyWords");
    if (typeof historyWords != "undefined" && historyWords != null && historyWords != "") {
        if (historyWords.indexOf(hiskey) < 0) {
            historyWords = historyWords + "," + hiskey;
            localStorage.setItem("historyWords", historyWords);
        }
    } else {
        localStorage.setItem("historyWords", searchKey);
    }

    localStorage.setItem("searchType", "4")
    localStorage.setItem("searchTypeParam", searchKey)
    localStorage.setItem("searchKeyWord", searchKey)
    //跳转到刷选页面
    window.location.href = '../../../h5/templet/search/list-interaction.html';
}

Comps.mpdList.prototype.searchStore = function (storeKey, nowPage, pageShow) {
    if (nowPage == "" || nowPage == null) {
        nowPage = "1";
    }
    if (pageShow == "" || pageShow == null) {
        pageShow = "20";
    }
    var url = window.basePath + 'jsonParam={"opeType":"searchStore","map":{"key":"' + storeKey + '","nowPage":"' + nowPage + '","pageShow":"' + pageShow + '"}}';
    var storesObj;
    $.ajax({
        url: url,
        type: "POST",
        dataType: "json",
        async: false,
        success: function (data) {
            //debugger;
            console.log(data);
            console.log(url);
            if (data.return_code != "0") {
                mui.alert(data.message, '提示');
            } else {
                storesObj = data.storeList;
            }
        }

    })
    return storesObj;
}

Comps.mpdList.prototype.cateToProduct = function (searchKey, cateUuid) {
    var hiskey = localStorage.setItem("hiskey", searchKey);
    localStorage.setItem("searchType", "2")//0代表商品  1代表品牌 2.代表分类
    localStorage.setItem("searchTypeParam", cateUuid);
    localStorage.setItem("searchKeyWord", searchKey);
    //跳转到刷选页面

    //记录跳转之前的动作
    var categoryHtmlActive = $(".ca .on").html();
    localStorage.setItem("categoryHtmlActive", categoryHtmlActive);
    window.location.href = '../../../h5/templet/search/list-interaction.html';
}

Comps.mpdList.prototype.cateToPbName = function (pbname) {
    var hiskey = localStorage.setItem("hiskey", pbname);
    localStorage.setItem("searchType", "3")//
    localStorage.setItem("searchTypeParam", pbname);
    localStorage.setItem("searchKeyWord", pbname);
    //跳转到刷选页面
    window.location.href = '../../../h5/templet/search/list-interaction.html';
}

Comps.mpdList.prototype.searchSto = function (searkey) {
    localStorage.setItem("searchType", "2");//
    localStorage.setItem("searchTypeParam", searkey);
    window.location.href = '../../../h5/templet/search/search.html';
}

Comps.mpdList.prototype.searchProductInStore = function (storeUuid, searchKey, categoryUuid, nowPage, pageShow, sortBy, sortType, needStoreInfo, wm) {
    var wm = wmModelStr(nowPage, pageShow, "", "");
    if (needStoreInfo == "") {
        needStoreInfo = 1;
    }
    var url = window.basePath + 'jsonParam={"opeType":"storeSearch","map":{"storeUuid":"' + storeUuid + '","searchKey":"' + searchKey + '","categoryUuid":"' + categoryUuid + '","nowPage":"' + nowPage + '","pageShow":"' + pageShow + '","sortBy":"' + sortBy + '","sortType":"' + sortType + '","needStoreInfo":"' + needStoreInfo + '","wm":' + wm + '}}';
    $.ajax({
        url: url,
        type: "POST",
        dataType: "json",
        async: false,
        success: function (data) {
            if (data.return_code != "0") {
                mui.alert(data.message, '提示');
            } else {
                //模拟数据展示
                var productList = data.products;
            }
        }
    })
}

//搜索公用方法
Comps.mpdList.prototype.searchProduct = function (nowPage, pageShow, searchKey, categoryUuid, brand, startPrice, endPrice, sortBy, sortType, attrValue) {
    //排序字段（mount-销量，shelveTime-上架时间，price-价格）  sortBy
    //排序方式（1 desc 2 asc）  sortType
    //{尺寸=5.5&keywords2=&existProduct=1}    attrValue
    //debugger;
    if (nowPage == "" || nowPage == null) {
        nowPage = "1";
    }
    if (pageShow == "" || pageShow == null) {
        pageShow = "20";
    }
    var dataObj;
    var url = window.basePath + 'jsonParam={"opeType":"search","map":' +
        '{"nowPage":"' + nowPage + '","pageShow":"' + pageShow + '","searchKey":"' + searchKey + '","categoryUuid":"' + categoryUuid + '","brand":"' + brand + '",' +
        '"startPrice":"' + startPrice + '","endPrice":"' + endPrice + '","sortBy":"' + sortBy + '","sortType":"' + sortType + '","attrValue":"' + attrValue + '"}}';
    $.ajax({
        url: url,
        type: "POST",
        dataType: "json",
        async: false,
        success: function (data) {
            //debugger;
            console.log(data);
            console.log(url);
            if (data.return_code != "0") {
                mui.alert(data.message, '提示');
            } else {
                dataObj = data;
            }
        }
    })
    return dataObj;
}

//搜索分页接口
Comps.mpdList.prototype.dataViewPage = function (data, index) {
    var _self = this;
    //商品列表信息
    var productList = data.products;
    var pageModel = data.wm;
    var nowPage = 1;
    var totalPage;
    if (typeof pageModel != 'undefined' && pageModel != '' && pageModel != null) {
        nowPage = pageModel.nowPage
        totalPage = pageModel.totalPage;
    }

    _self.dataViewBrush(index, data, function () {
        //空白页面
        mui.alert('没有更多商品了');
    })
}

//根据搜索出来的结果 解析
Comps.mpdList.prototype.dataView = function (data,index) {
    var _self = this;
    //商品列表信息
    _self.dataViewBrush(index, data, function () {
        //空白页面
        $('#indexss').append('<div class="blank"><img src="'+ContextPath+'/static/mobileTempAssets/defaultTemplate/images/index/97.png"></div>');
    })

    //debugger;
    //商品品牌集合（List）
    var brands = data.brands;
    var pphtml = ''
    $('.pingpai').html(pphtml);
    pphtml = pphtml + '<p class="p">品牌分类:</p>';
    if (typeof brands != 'undefined' && brands != null && brands.length > 0) {
        pphtml = pphtml + '<p class="tab">'
        for (var b = 0; b < brands.length; b++) {
            pphtml = pphtml + '<span class="sub">' + brands[b].brandName + '</span>';
        }
        pphtml = pphtml + '</p>';
        $('.pingpai').append(pphtml);
    } else {
        $('.pingpai').append(pphtml);
    }

    //子分类集合 productCategory
    var subCateList = data.subCate;
    var sbhtml = '';
    $('.productCategory').html(sbhtml);
    sbhtml = sbhtml + ' <p class="p">商品分类</p>';
    if (subCateList != null && subCateList != "" && subCateList.length > 0) {
        sbhtml = sbhtml + '<p class="tab">';
        for (var s = 0; s < subCateList.length; s++) {
            sbhtml = sbhtml + '<span class="sub" >' + subCateList[s].categoryName + '</span>' +
                '<input type="hidden" value="' + subCateList[s].uuid + '" id="productCategory' + s + '">';
        }
        sbhtml = sbhtml + '</p>';
        $('.productCategory').append(sbhtml);
    } else {
        $('.productCategory').append(sbhtml);
    }

    //价格区间
    var catePrices = data.catePrices;
    var pricehtml = '';
    $('.catePrices').html(pricehtml);
    pricehtml = pricehtml + ' <p>价格区间</p>';
    if (catePrices != null && catePrices.length > 0) {
        for (var cp = 0; cp < catePrices.length; cp++) {
            pricehtml = pricehtml + '<td class="priceArea">' + catePrices[cp] + '</td>';
        }
        pricehtml = '<table><tr>' + pricehtml + '</tr></table>';
        $('.catePrices').append(pricehtml);
    } else {
        $('.catePrices').append(pricehtml);
    }


    //筛选属性
    var selectPorperityList = data.jsonList;
    $(".porperity_value").html("");
    $('.porperity_name').html("");
    var porperit = '';
    if (selectPorperityList != null && selectPorperityList.length > 0) {
        for (var sporty = 0; sporty < selectPorperityList.length; sporty++) {
            $('.porperity_name').show();
            var porperit = '<ul class="">';
            var name = selectPorperityList[sporty].attributeName;
            $('.porperity_name').append('<li class="" ><span>' + name + '<i></i></span></li>');

            var selectValue = selectPorperityList[sporty].selectValue;
            var valueStr = '';
            for (var v = 0; v < selectValue.length; v++) {
                valueStr = valueStr + '<li class="selectValue">' + selectValue[v] + '</li>';
            }
            porperit = porperit + valueStr;
            porperit = porperit + '<div class="clear"></div></ul>';
            $(".porperity_value").append(porperit);
        }
    }
}

//根据搜索出来的结果 解析
Comps.mpdList.prototype.dataViewBrush = function (index, data, callback) {
    //商品列表信息
    var productList = data.products;
    var pageModel = data.wm;
    var nowPage = 1;
    var totalPage;
    if (typeof pageModel != 'undefined' && pageModel != '' && pageModel != null) {
        nowPage = pageModel.nowPage
        totalPage = pageModel.totalPage;
    }

    this.listView(productList, pageModel, nowPage, totalPage, callback, index);
}

//渲染listview
Comps.mpdList.prototype.listView = function (productList, pageModel, nowPage, totalPage, callback, index) {
    if (index == "0") {
        $('#indexss').html('');
    }
    if (productList != null && productList.length > 0) {
        for (var p = 0; p < productList.length; p++) {
            var privilegeTypes = productList[p].privilegeTypes;
            var zhuxiaowayStr = zhuxiaoway(privilegeTypes);
            var ppage = nowPage * 20 + p;

            $('#indexss').append(' <li class="pl-item spcc' + ppage + ' shangping">' +
                '<a href="javascript:void(0)" class="pl-pic" data-uuid="'+productList[p].uuid+'"><img src="' + productList[p].pic + '"></a>' +
                '<div class="pl-info">' + zhuxiaowayStr + '<p class="baoyou pl-title">' + productList[p].name + '</p>' +
                '<strong class="pl-price">￥' + productList[p].price + '</strong><span class="pl-buycount">' + productList[p].mount + '人付款</span>' +
                '</div></li>');

            // if (p == productList.length - 1) {
            //     setListTyle('#indexss', 1.42);
            // }
        }
        $("#searchTotalPage").val(totalPage);
        $("#searchNowPage").val(nowPage);
    } else {
        callback()
    }
}

Comps.mpdList.prototype.selectCategory = function (uuid) {
    localStorage.removeItem('selectCategory');
    localStorage.setItem('selectCategory', uuid);
}

Comps.mpdList.prototype.toDetailsPage = function(){
    var _self = this;
    $(document).on('click','.pl-item',function(){
        var uuids = $(this).find('a').attr('data-uuid');

        var url = Utils.getProductDetailUrl(ContextPath,_self.options.mdetail_url,uuids)
        window.location.href = url;
    })
};