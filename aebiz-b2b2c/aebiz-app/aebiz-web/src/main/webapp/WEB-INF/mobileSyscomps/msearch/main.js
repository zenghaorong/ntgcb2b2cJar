/**
 * Created by wenyu on 2017/5/10.
 */
Comps.msearch = function(options){
    this.options = options.mapProps;
};

Comps.msearch.prototype.init = function(){
    this.render();
    this.searchModel();
};

Comps.msearch.prototype.render = function(){
    $("#store-search div.tab span").click(function() {
        $(this).addClass("on").siblings().removeClass("on");
    })

    $('input[type=password]').bind('input propertychange', function() {
        $("input[type=button]").addClass("action");
        $("#authentication .pass img").css("display", "block");
    });

    $("#authentication .pass img").click(function() {
        //alert($("#clearbutton").size());
        $(this).css("display", "none")
        $("input[type='password']").val("").focus(); // 清空并获得焦点

    });
}

Comps.msearch.prototype.getHotkey = function(){
    var url = MobilePath+'jsonParam={"opeType":"getHotKey"}';
    var hotKeyWords;
    $.ajax({
        url:url,
        type: "POST",
        dataType: "json",
        async:false,
        success:function(data){
            //debugger;
            console.log('获取热搜词');console.log(data);console.log(url);
            if(data.return_code != "0"){
                mui.alert(data.message,'提示');
            }else{
                hotKeyWords = data.hotkey;
            }
        }

    })
    return hotKeyWords;
}

Comps.msearch.prototype.getLikeKey = function(key){
    //debugger;
    var url = MobilePath+'jsonParam={"opeType":"getLikeKey","map":{"key":"'+key+'"}}';
    var LikeKey;
    $.ajax({
        url:url,
        type: "POST",
        dataType: "json",
        async:false,
        success:function(data){
            //debugger;
            if(data.return_code != "0"){
                mui.alert(data.message,'提示');
            }else{
                LikeKey = data.productNameList;
            }
        }

    });

    return LikeKey;
}

Comps.msearch.prototype.searchSearchKey = function (searchKey) {
    //debugger;
    //保存历史记录
    var hiskey = localStorage.getItem("hiskey")
    var historyWords = localStorage.getItem("historyWords");
    if(typeof historyWords != "undefined" && historyWords!= null && historyWords!=""){
        if(historyWords.indexOf(hiskey)<0){
            historyWords = historyWords + "," + hiskey;
            localStorage.setItem("historyWords",historyWords);
        }
    }else{
        localStorage.setItem("historyWords",searchKey);
    }

    localStorage.setItem("searchType","4")
    localStorage.setItem("searchTypeParam",searchKey)
    localStorage.setItem("searchKeyWord",searchKey)
    //跳转到刷选页面
    this.toUrl(searchKey)
}

Comps.msearch.prototype.searchStore = function (storeKey,nowPage,pageShow) {
    if(nowPage == ""  || nowPage == null){
        nowPage = "1";
    }
    if(pageShow == ""  || pageShow == null){
        pageShow = "20";
    }
    var url = MobilePath+'jsonParam={"opeType":"searchStore","map":{"key":"'+storeKey+'","nowPage":"'+nowPage+'","pageShow":"'+pageShow+'"}}';
    var storesObj;
    $.ajax({
        url:url,
        type: "POST",
        dataType: "json",
        async:false,
        success:function(data){
            //debugger;
            console.log(data);console.log(url);
            if(data.return_code != "0"){
                mui.alert(data.message,'提示');
            }else{
                storesObj = data.storeList;
            }
        }

    })
    return storesObj;
}

Comps.msearch.prototype.searchStore = function (searchKey,cateUuid) {
    var hiskey = localStorage.setItem("hiskey",searchKey);
    localStorage.setItem("searchType","2")//0代表商品  1代表品牌 2.代表分类
    localStorage.setItem("searchTypeParam",cateUuid);
    localStorage.setItem("searchKeyWord",searchKey);
    //跳转到刷选页面

    //记录跳转之前的动作
    var categoryHtmlActive = $(".ca .on").html();
    localStorage.setItem("categoryHtmlActive",categoryHtmlActive);
    this.toUrl()
}

//从商品分类跳转商品筛选页
Comps.msearch.prototype.cateToProduct = function(searchKey,cateUuid){
    var hiskey = localStorage.setItem("hiskey",searchKey);
    localStorage.setItem("searchType","2")//0代表商品  1代表品牌 2.代表分类
    localStorage.setItem("searchTypeParam",cateUuid);
    localStorage.setItem("searchKeyWord",searchKey);
    //跳转到刷选页面

    //记录跳转之前的动作
    var categoryHtmlActive = $(".ca .on").html();
    localStorage.setItem("categoryHtmlActive",categoryHtmlActive);
    this.toUrl()
}

//店铺内搜索产品
Comps.msearch.prototype.storeSearchKey = function(searchkey){
    var hiskey = localStorage.setItem("hiskey",searchKey);
    localStorage.setItem("searchType","5")//5代表店铺内商品
    localStorage.setItem("searchTypeParam",cateUuid);
    localStorage.setItem("searchKeyWord",searchKey);
    //跳转到刷选页面
    this.toUrl()
}


//商品品牌搜索
Comps.msearch.prototype.cateToPbName = function(pbname){
    var hiskey = localStorage.setItem("hiskey",pbname);
    localStorage.setItem("searchType","3")//
    localStorage.setItem("searchTypeParam",pbname);
    localStorage.setItem("searchKeyWord",pbname);
    //跳转到刷选页面
    this.toUrl()
}

//店铺搜索
Comps.msearch.prototype.searchSto = function(searkey){
    localStorage.setItem("searchType","2");//
    localStorage.setItem("searchTypeParam",searkey);
    window.location.href = '../../../h5/templet/search/search.html';
}

//店铺内搜索商品方法
Comps.msearch.prototype.searchProductInStore = function(storeUuid,searchKey,categoryUuid,nowPage,pageShow,sortBy,sortType,needStoreInfo,wm){
    var wm = wmModelStr(nowPage,pageShow,"","");
    if(needStoreInfo == ""){
        needStoreInfo = 1;
    }
    var url =MobilePath+'jsonParam={"opeType":"storeSearch","map":{"storeUuid":"'+storeUuid+'","searchKey":"'+searchKey+'","categoryUuid":"'+categoryUuid+'","nowPage":"'+nowPage+'","pageShow":"'+pageShow+'","sortBy":"'+sortBy+'","sortType":"'+sortType+'","needStoreInfo":"'+needStoreInfo+'","wm":'+wm+'}}';
    $.ajax({
        url:url,
        type: "POST",
        dataType: "json",
        async: false,
        success:function(data){
            if(data.return_code != "0"){
                mui.alert(data.message,'提示');
            }else{
                //模拟数据展示
                var productList = data.products;
            }
        }
    })
}


//搜索公用方法
Comps.msearch.prototype.searchProduct = function(nowPage,pageShow,searchKey,categoryUuid,brand,startPrice,endPrice,sortBy,sortType,attrValue){
    //排序字段（mount-销量，shelveTime-上架时间，price-价格）  sortBy
    //排序方式（1 desc 2 asc）  sortType
    //{尺寸=5.5&keywords2=&existProduct=1}    attrValue
    //debugger;
    if(nowPage == ""  || nowPage == null){
        nowPage = "1";
    }
    if(pageShow == ""  || pageShow == null){
        pageShow = "20";
    }
    var dataObj;
    var url = MobilePath+'jsonParam={"opeType":"search","map":'+
        '{"nowPage":"'+nowPage+'","pageShow":"'+pageShow+'","searchKey":"'+searchKey+'","categoryUuid":"'+categoryUuid+'","brand":"'+brand+'",'+
        '"startPrice":"'+startPrice+'","endPrice":"'+endPrice+'","sortBy":"'+sortBy+'","sortType":"'+sortType+'","attrValue":"'+attrValue+'"}}';
    $.ajax({
        url:url,
        type: "POST",
        dataType: "json",
        async : false,
        success:function(data){
            //debugger;
            console.log(data); console.log(url);
            if(data.return_code != "0"){
                mui.alert(data.message,'提示');
            }else{
                dataObj = data;
            }
        }
    })
    return dataObj;
}


Comps.msearch.prototype.searchModel = function () {
    var hiskey;
    var searchType = localStorage.getItem("searchType");
    var searchKeyWord = localStorage.getItem("searchKeyWord");
    var _self = this;
    this.initRender(searchType,searchKeyWord);

    //商品
    $('.span1').click(function(){
        $('.list-store').removeAttr("style");
        $('.list-good').removeAttr("style");
        $('.list-likegood').removeAttr("style");
        $('.list-store').attr("style","display:none");
        $('.list-likegood').attr("style","display:none");
        $('#indexSearchType').val("1");
        $(".blank").attr("style","display:none");

        //切换效果
        /* $(".mess").find('input[type=button]').attr("id","searchForm");
         $(".mess").find('input[type=button]').attr("value","搜索");*/

    });
    //店铺
    $('.span2').click(function(){
        $('.list-store').removeAttr("style");
        $('.list-good').removeAttr("style");
        $('.list-likegood').removeAttr("style");
        $('.list-good').attr("style","display:none");
        $('.list-likegood').attr("style","display:none");
        $('.list-store').attr("style","display:none");
        $('#indexSearchType').val("2");

        //切换效果
        /* $(".mess").find('input[type=button]').attr("onclick","history.go(-1)");
         $(".mess").find('input[type=button]').attr("value","搜索");*/
    });

    $('#searchForm').click(function(){
        var indexSearchType =   $("#indexSearchType").val();
        var searchKey = $("#searchChange").val();
        if(searchKey == null || searchKey == ""){
            mui.alert('请输入搜索词');
            return;
        }
        if(indexSearchType == "1"){ //商品
            //历史记录
            var historyWords = localStorage.getItem("historyWords");
            if(typeof historyWords != "undefined" && historyWords != null && historyWords !=""){
                if(historyWords.indexOf(searchKey)<0){
                    var temphistoryWords = historyWords+","+searchKey;
                    localStorage.setItem("historyWords",temphistoryWords);
                }
            }else{
                localStorage.setItem("historyWords",searchKey);
            }

            var hiskey = localStorage.setItem("hiskey",searchKey);
            localStorage.setItem("searchType","4")//4代表商品
            localStorage.setItem("searchTypeParam",searchKey);
            localStorage.setItem("searchKeyWord",searchKey);
            //跳转到刷选页面
            _self.toUrl()
        }else{ //店铺
            //当前为店铺搜索
            var storeList =  searchStore(searchKey);
            $('#storelist').html("");
            if(typeof storeList != "undefined" && storeList != null && storeList !="" && storeList.length>0){
                var storeListStr = '';
                for(var s=0;s<storeList.length;s++){
                    storeListStr = storeListStr +
                        '<li><a href="javascript:void(0)" onclick="instore(\''+storeList[s].uuid+'\')">'+
                        '<div class="photo"><img src="'+storeList[s].storeLogo+'"></div>'+
                        '<div><p class="name">'+storeList[s].storeName+'</p>'+
                        '<span>收藏<font>'+storeList[s].favoriteCount+'</font></span><span>销量<font>'+storeList[s].saleCount+'</font></span>'+
                        '</div></a>'+
                        '</li>'
                }
                $('#storelist').html(storeListStr);
                $('.list-store').attr("style","display:block");
                $('.blank').attr('style','display:none');
            }else{
                $('.blank').removeAttr('style');
            }
            $('.list-store').removeAttr("style");

        }
    });

    //删除历史记录
    $(document).on('click','.delhistory',function(){
        localStorage.setItem("historyWords","");
        window.location.reload();
    })


}

Comps.msearch.prototype.initRender = function(searchType,searchKeyWord){
    //初始化方法
    //获取搜索历史记录
    //localStorage.clear();
    var _self = this;
    var historyWords = localStorage.getItem("historyWords");
    if(typeof historyWords != "undefined" && historyWords != null && historyWords !=""){
        var hisStr = '<p class="histo searchhis"><i><img src="'+ContextPath+'/static/mobileTempAssets/defaultTemplate/images/index/102.png"></i>历史搜索<i class="delete fr delhistory"><img src="'+ContextPath+'/static/mobileTempAssets/defaultTemplate/images/index/102.png"></i></p><p class="words">';
        if(historyWords.indexOf(",")>0){
            var wordList = historyWords.split(",");
            for(var w=0;w<wordList.length;w++){
                hisStr = hisStr + '<span data-sid="'+wordList[w]+'" class="hissearch">'+wordList[w]+'</span>';
            }
        }else{
            hisStr = hisStr + '<span data-sid="'+wordList[w]+'" class="hissearch">'+historyWords+'</span>';
        }
        hisStr = hisStr + '</p>';
        $('.history').append(hisStr);
    }


    //得到热门搜索
    //debugger;
    var hotwords = this.getHotkey();
    if(typeof hotwords != "undefined" && hotwords != null && hotwords !=""){
        var hotStr = '<p class="histo searchhis"><i><img src="'+ContextPath+'/static/mobileTempAssets/defaultTemplate/images/index/100.png"></i>历史搜索<i class="delete fr"><img src="'+ContextPath+'/static/mobileTempAssets/defaultTemplate/images/index/102.png"></i></p><p class="words">';
        if(hotwords.indexOf(",")>0){
            var hotgood = hotwords.split(",");
            for(var w=0;w<hotgood.length;w++){
                hotStr = hotStr + '<span onclick="searchSearchKey(\''+hotgood[w]+'\')>'+hotgood[w]+'</span>';
            }
        }else{
            hotStr = hotStr + hotwords;
        }
        hotStr = hotStr + '</p>';
        $('.hot').append(hotStr);
    }

    //店铺搜索
    //debugger;
    $('#storelist').html("");


    //当得到当期店铺的关键字   MIO解析方法
    if(searchType == "1"){
        $('.list-store').removeAttr("style");
        $('.list-good').removeAttr("style");
        $('.list-likegood').removeAttr("style");
        $('.list-good').attr("style","display:none");
        $('.list-likegood').attr("style","display:none");
        $('.list-store').attr("style","display:none");
        $('#indexSearchType').val("2");

        $('.list-store').removeAttr("style");
        $('.list-good').removeAttr("style");
        $('.list-good').attr("style","display:none");
        $('.list-store').attr("style","display:none");
        //判断是店铺还是商品
        var indexSearchType = $('#indexSearchType').val();
        //获取当前搜索框的值
        $('.searchKey').val(searchKeyWord)
        var searchKey = searchKeyWord;
        var storeList =  searchStore(searchKey);
        if(typeof storeList != "undefined" && storeList != null && storeList !="" && storeList.length>0){
            var storeListStr = '';
            for(var s=0;s<storeList.length;s++){
                storeListStr = storeListStr +
                    '<li><a href="javascript:void(0)" onclick="instore(\''+storeList[s].uuid+'\')">'+
                    '<div class="photo"><img src="'+storeList[s].storeLogo+'"></div>'+
                    '<div><p class="name">'+storeList[s].storeName+'</p>'+
                    '<span>收藏<font>'+storeList[s].favoriteCount+'</font></span><span>销量<font>'+storeList[s].saleCount+'</font></span>'+
                    '</div></a>'+
                    '</li>'
            }
            $('#storelist').html(storeListStr);
        }else{
            $('.blank').removeAttr('style');
        }
        $('.list-store').removeAttr("style");
    }

    $(document).on('click','.hissearch',function(){
        _self.searchSearchKey($(this).attr('data-sid'));
    })
};

Comps.msearch.prototype.toUrl = function(skey){
    if(skey == undefined){
        skey = $('#searchChange').val()
    }else{
        skey = skey
    }

    var url;
    if(this.options.mdetail_url){
        url = this.options.mdetail_url.indexOf("?")==-1 ?((ContextPath+this.options.mdetail_url)+"?searchType=4&searchTypeParam="+skey):((ContextPath+this.options.mdetail_url)+"&searchType=4&searchTypeParam="+skey)
    }else{
        url = "";
    }

    window.location.href = url;
}



