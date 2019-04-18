/**
 * Created by yewei on 2017/5/4.
 */
//底部组件
Comps.BottomComp = function(option){
    this.compId = option.compId;
    this.needAsyncInit = option.needAsyncInit;
}

Comps.BottomComp.prototype.init = function(){
    var that = this;
    //1.是否懒加载，如果是懒加载，则下面数据通过ajax请求，渲染html页面
    if(this.needAsyncInit){
        var compId = this.compId;
        //异步获取数据
        $.ajax({
            type: "GET",
            url: ContextPath+"/bottomComp/ajaxLoadData",
            data: {},
            success: function(data) {
                console.log(data);
                $("#"+compId+"_content").html(data);
                Utils.ajaxLoadComplate(that);
            }
        });
    }else{
        Utils.ajaxLoadComplate(that);
        Utils.loader();
    }
}


//############底部帮助中心
// 组件入口
Comps.BottomHelpCenterComp = function(option){
    this.compId = option.compId;
    this.needAsyncInit = option.needAsyncInit;
    this.contentUrl = option.contentUrl;
    this.channelId=option.channelId;
}

Comps.BottomHelpCenterComp.prototype.init = function(){
    var that = this;
    //1.是否懒加载，如果是懒加载，则下面数据通过ajax请求，渲染html页面
    if(this.needAsyncInit){
        var compId = this.compId;
        $.ajax({
            type: "GET",
            url:ContextPath+"/bottomHelpCenterComp/ajaxLoadData",
            data: {channelId:this.channelId},
            dataType: "json",
            success: function(data){
                console.log(data);
                that.genCompHtml(data);
                Utils.ajaxLoadComplate(that);
            },
            error:function(e){
                console.log(e);
            }
        });
    }else{
        Utils.ajaxLoadComplate(that);
    }
}

Comps.BottomHelpCenterComp.prototype.genCompHtml = function (data){
    data = eval('(' + data + ')');
    var compId = this.compId;
    var htmlStr = "";
    for(var key in data){
        var cat =eval('(' + key + ')');
        var conList=data[key];
        console.log(cat);
        htmlStr += "<dl>";
        htmlStr += "<dt></dt>";
        htmlStr += "<dd class='w_footerh'>"+cat.categoryName+"</dd>";
        for(var j=0;j<conList.length;j++){
            var con = conList[j];
            if(con.contentType == "article"){
                var contentUrl = this.contentUrl;
                contentUrl = Utils.getContentUrl(ContextPath,contentUrl,con.articlUuid);
                htmlStr +="<dd><a target='_blank' href='"+contentUrl+"'>"+con.articlTitle+"</a></dd>";
            }
            if(con.contentType == 2){
                htmlStr += "<dd><a target='_blank' href='"+con.articlValue+"' >"+con.articlTitle+"</a></dd>";
            }
        }
        htmlStr += "</dl>";
    }
    $("#"+compId+"_cotcont").html(htmlStr);
}
/////***************************友情链接******************
Comps.UsefulLinksComp = function(option){
    this.compId = option.compId;
    this.needAsyncInit = option.needAsyncInit;
}

//加载友情链接数据
Comps.UsefulLinksComp.prototype.init = function(){
    var that = this;
    var compId = this.compId;
    //1.是否懒加载，如果是懒加载，则下面数据通过ajax请求，渲染html页面
    if(this.needAsyncInit){
        $.ajax({
            type: "GET",
            url: ContextPath+"/usefulLinksComp/ajaxLoadData",
            data: {},
            dataType: "json",
            success: function(data){
                $("#"+compId+"_usefulLinks").empty();
                for(var i=0;i<data.length;i++){
                    $("#"+compId+"_usefulLinks").append("<a href='"+data[i].siteLinks+"' target='"+data[i].siteTarget+"'>"+data[i].siteName+"</a>&nbsp;&nbsp;");
                }

                Utils.ajaxLoadComplate(that);
            }
        });
    }else{
        Utils.ajaxLoadComplate(that);
    }
}


//############楼层标签1


Comps.FloorLable = function(option) {
    this.compId = option.compId;
    this.needAsyncInit = option.needAsyncInit;
    this.lableName = option.lableName;
    this.floorName=option.floorName;
}

Comps.FloorLable.prototype.init = function(){
    var that = this;

    if (this.needAsyncInit) {
        $.ajax({
            type: "GET",
            url: ContextPath+"/floorLableComp/ajaxLoadData",
            data: {
                "lableName": that.lableName,
                "floorName": that.floorName
            },
            dataType: "json",
            async: true,
            success: function(data) {
                $("#" + that.compId + "_lablename").text(data.lableName);
                $("#" + that.compId + "_span").text(data.floorName);

                Utils.ajaxLoadComplate(that);
            }
        })
    }
    else {
        Utils.ajaxLoadComplate(that);

    }

}


//############楼层标签
// 组件定义
Comps.FloorStyle = function(option) {
    this.compId = option.compId;
    this.showtype = option.showtype;
}


Comps.FloorStyle.prototype.init = function() {

    var that =this;

    if (!this.showtype) {
        $(".m-fixfloor").hide();
    }
    Utils.ajaxLoadComplate(that);
}

//############楼层
//组件定义
Comps.ShareModel = function(option){
    this.compId = option.compId;
    this.imgSize = option.imgSize;
    this.needAsyncInit = option.needAsyncInit;
    this.lableName = option.lableName;
}

//############楼层

//初始化方法
Comps.ShareModel.prototype.init=function(){
    var that =this;
    if(this.needAsyncInit){
        $.ajax({
            type : "GET",
            url : ContextPath+"/shareComp/ajaxLoadData",
            data : {
                imgSize : that.imgSize,
                lableName : that.lableName
            },
            dataType : "json",
            async : true,
            success : function(data) {

                $("#" + that.compId + "_share_name").first().text(data.lableName);
                $("#" + that.compId + "_share_detail").attr("imgsize",
                    data.imgSize);

                that.bindSomeEvent();
                Utils.ajaxLoadComplate(that);
            }
        })
    }

    else {
        that.bindSomeEvent();
        Utils.ajaxLoadComplate(that);
    }
}

//绑定事件
Comps.ShareModel.prototype.bindSomeEvent=function(){
    var that =this;
    window._bd_share_config = {
        "common" : {
            "bdSnsKey" : {},
            "bdText" : "",
            "bdMini" : "2",
            "bdMiniList" : false,
            "bdPic" : "",
            "bdStyle" : "0",
            "bdSize" : that.imgSize
        },
        "share" : {},
        "image2" : {
            "viewList" : [ "qzone", "tsina", "weixin" ],
            "viewText" : "分享到：",
            "viewSize" : "16"
        },
        "selectShare" : {
            "bdContainerClass" : null,
            "bdSelectMiniList" : [ "qzone", "tsina", "weixin" ]
        }
    };
    with (document)
        0[(getElementsByTagName('head')[0] || body)
            .appendChild(createElement('script')).src = 'http://bdimg.share.baidu.com/static/api/js/share.js?v=89860593.js?cdnversion='
            + ~(-new Date() / 36e5)];
}


//###########分享组件##########
//###########图文编辑器##########
Comps.ImageTextEditor = function(option) {
    this.compId = option.compId;
    this.needAsyncInit = option.needAsyncInit;
    this.defineWidth = option.defineWidth;
    this.defineHeight = option.defineHeight;
    this.content=option.content;
}
Comps.ImageTextEditor.prototype.init = function() {
    var that =this;
    if (this.needAsyncInit) {
        $("#"+that.compId+"_content").height(that.defineHeight);
        $("#"+that.compId+"_content").width(that.defineWidth);
        $("#"+that.compId+"_content").html(that.content);
    }
    Utils.ajaxLoadComplate(that);
}
//###########图文编辑器##########
/*************************************** slideAds START *************************************/

Comps.SlideAds = function(option) {
    this.needAsyncInit = option.needAsyncInit;
    this.compId = option.compId;
    this.slideSpeed = option.slideSpeed;
    this.interTime = option.interTime;
    this.adUuid = option.adUuid;
}
//轮播效果触发方法
Comps.SlideAds.prototype.slide = function() {
    var speed = this.slideSpeed;//轮播速度
    var interTime = this.interTime;//自动轮播时 轮播间隔
    jQuery(".m-bannerslider").slide({mainCell:".bd ul",effect:"leftLoop",autoPlay:true,prevCell:".bnprev",nextCell:".bnnext",  delayTime:speed, interTime:interTime });
}
//页面初始化方法
Comps.SlideAds.prototype.init = function() {
    var that = this;
    if(this.adUuid==""||this.adUuid==undefined||this.adUuid==null){
        Utils.ajaxLoadComplate(that);
        return;
    }
    if(this.needAsyncInit){
        $.ajax({
            type: "GET",
            url: ContextPath+"/slideAds/ajaxLoadData",
            data: {adUuid :this.adUuid},
            dataType: "json",
            success: function(data) {
                //1.获取返回的广告、图片url
                var listData= eval(data);
                var urlList;

                //2.获取锚点所在ul，并克隆节点
                var fgUl = $("#slideBox_fgUl");
                var fgUlClone = fgUl.clone();
                var fgLiClone = fgUl.children(0).clone();
                fgUlClone.empty();

                //3、获取广告图片所在ul，并克隆节点
                var bgUl = $("#slideBox_bgUl");
                var bgUlClone = bgUl.clone();
                bgUlClone.empty();
                var bgLiClone = bgUl.children(0);

                //4、生成锚点及广告图片节点
                var adUrl = "";
                var picUrl = "";
                for(var i=0;i<listData.length;i++){
                    var bgLi = bgLiClone.clone();
                    var fgLi = fgLiClone.clone();
                    if(i!=0){
                        fgLi.removeClass();
                    }
                    fgUlClone.append(fgLi[0].outerHTML);
                    adUrl = listData[i];
                    urlList = adUrl.split(";");
                    if(urlList.length==2){
                        adUrl = urlList[1];
                        picUrl = urlList[0];
                    }else if(urlList.length==1){
                        picUrl = urlList[0];
                    }
                    bgLi.children(0).attr("href", adUrl);
                    bgLi.children(0).children(0).attr("src", picUrl);
                    bgUlClone.append(bgLi[0].outerHTML);
                }

                //5、替换原有节点
                fgUl.replaceWith(fgUlClone);
                bgUl.replaceWith(bgUlClone);
                that.slide();
                Utils.ajaxLoadComplate(that);
            }
        });
    }else{
        //同步加载，初始化事件
        that.slide();
        Utils.ajaxLoadComplate(that);
    }

}
/*************************************** slideAds END *************************************/

//*************公告栏组件
Comps.NoticeComp = function(config) {
    this.compId = config.compId;
    this.config = config;
}
Comps.NoticeComp.prototype.init = function() {
    var needAsyncInit = this.config.needAsyncInit;
    var rollType = this.config.rollType;
    if (rollType == 1) {
        jQuery(".noticeWrap").slide({
            mainCell: ".bd ul",
            autoPlay: true,
            effect: "topMarquee",//向左滚动用leftMarquee
            vis: 5,
            interTime: 50,
            trigger: "click"
        });

    }else if(rollType == 2){
        jQuery(".noticeWrap").slide({
            mainCell: ".bd ul",
            autoPlay: true,
            effect: "leftMarquee",//向左滚动用leftMarquee
            vis: 5,
            interTime: 50,
            trigger: "click"
        });
    }
    if (needAsyncInit) {
        this.ajaxLoad();
    }else {
        Utils.ajaxLoadComplate(this);
    }
}
Comps.NoticeComp.prototype.ajaxLoad=function(){
    var ts = this;
    $.ajax({
        "url": ContextPath+"/noticeComp/ajaxLoadData",
        "data": { "contentCateUuid": ts.config.categoryId },
        "success": function(result) {
            var data = result.data;
            ts._loadData(data);
            Utils.ajaxLoadComplate(ts);
        },
        "error":function(e){
            console.log(e);
        }
    })
}
Comps.NoticeComp.prototype._loadData = function(data) {
    var compId = this.config.compId;
    var rollType = this.config.rollType;
    var categoryUrl = ContextPath+this.config.categoryUrl;
    var categoryId = this.config.categoryId;
    var categoryNameEle = $("#" + compId + "_categoryName");
    var categoryUrlEle = $("#" + compId + "_categoryUrl");
    categoryNameEle.html(this.config.categoryName);
    categoryUrlEle.prop("href", categoryUrl+"&categoryUuid="+ categoryId);
    var loopEle = $("#" + compId + "_loop");
    var liEle = loopEle.find("li");
    for (var i = 0; i < data.length; ++i) {
        var content = data[i];
        var newEle = liEle.clone();
        //渲染被循环的li
        this._renderLi(newEle, content);
        loopEle.append(newEle);
    }
    liEle.remove();
    if (rollType == 1) {
        jQuery(".noticeWrap").slide({
            mainCell: ".bd ul",
            autoPlay: true,
            effect: "topMarquee",//向左滚动用leftMarquee
            vis: 5,
            interTime: 50,
            trigger: "click"
        });
    }else if(rollType == 2){
        jQuery(".noticeWrap").slide({
            mainCell: ".bd ul",
            autoPlay: true,
            effect: "leftMarquee",//向左滚动用leftMarquee
            vis: 5,
            interTime: 50,
            trigger: "click"
        });
    }
}
//渲染被循环的li
Comps.NoticeComp.prototype._renderLi = function(newEle, content) {
    var contentUrl = ContextPath+this.config.contentUrl;
    var contentUrlEle = newEle.find(".j_contentUrl");
    //var contentEle = newEle.find(".j_note");
    var titleEle = newEle.find(".j_title");
    contentUrlEle.prop("href", contentUrl + content.contentId);
    //contentEle.html(content.note);
    titleEle.html(content.title);
}
//*************公告栏组件end
//###########二维码##########10




Comps.Qrcode = function(option) {
    this.compId = option.compId;
    this.needAsyncInit = option.needAsyncInit;
    this.imgSrc = option.imgSrc;
    this.defineUrl = option.defineUrl;
    this.defineWidth = option.defineWidth;
    this.defineHeight = option.defineHeight;
    this.qrtype = option.qrtype;
    this.productUrl=option.productUrl;
    this.shopUrl=option.shopUrl;

}

Comps.Qrcode.prototype.GetQueryString = function(name) {

    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;

}


Comps.Qrcode.prototype.init = function() {
    var that =this;

    var url="";

    if(that.qrtype=="define"){
        url=that.defineUrl;


    }
    if(that.qrtype=="product"){

        var productUuid=	that.GetQueryString("productUuid");

        url=that.productUrl+"&productUuid="+productUuid


    }
    if(that.qrtype=="shop"){

        var shopUuid=	that.GetQueryString("shopUuid");


        url=that.shopUrl+"&shopUuid="+shopUuid


    }
    if (this.needAsyncInit) {
        var img=that.imgSrc;
        $.ajax({
            type : "GET",
            url : ContextPath+"/qrCodeComp/ajaxLoadData",
            data : {
                imgSrc : that.imgSrc,
                defineWidth:that.defineWidth,
                defineHeight:that.defineHeight,
                defineUrl:url,
                qrtype:that.qrtype
            },
            dataType : "json",
            async : true,
            success : function(data) {
                data=eval("("+data+")");
                console.log(data);
                url=escape(url);
                $("#"+that.compId+"_qrcode_img").qrcode({
                    render:"canvas",
                    width: data.defineWidth, //宽度
                    height:data.defineHeight, //高度
                    text:utf16to8(url),
                    src:that.imgSrc
                })
                if(that.defineWidth=="" || that.defineWidth==0){

                    $("#"+that.compId+"_qrcode_img").find("canvas").attr("width",100);
                    $("#"+that.compId+"_qrcode_codeimg").attr("height",100);
                }
                Utils.ajaxLoadComplate(that);
            }

        })

    }
}
//防止出现中文汉字，转二维码会出现问题。所以转一下
function utf16to8(str) {
    var out, i, len, c;
    out = "";
    len = str.length;
    for(i = 0; i < len; i++) {
        c = str.charCodeAt(i);
        if ((c >= 0x0001) && (c <= 0x007F)) {
            out += str.charAt(i);
        } else if (c > 0x07FF) {
            out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));
            out += String.fromCharCode(0x80 | ((c >>  6) & 0x3F));
            out += String.fromCharCode(0x80 | ((c >>  0) & 0x3F));
        } else {
            out += String.fromCharCode(0xC0 | ((c >>  6) & 0x1F));
            out += String.fromCharCode(0x80 | ((c >>  0) & 0x3F));
        }
    }
    return out;
}

Comps.Qrcode.prototype.bindSomeEvent=function(){

    $(".j_qrcode_codeimg").css("display","none");

    $(".j_qrcode_img").bind("mouseover",function(){



        $(".j_qrcode_codeimg").css("display","block");


    }).bind("mouseleave",function(){


        $(".j_qrcode_codeimg").css("display","none");


    })



}
//###########二维码##########
/**
 * 导航栏组件
 */
Comps.NavigationComp = function (config) {
    this.compId = config.compId;
    this.config = config;
}
Comps.NavigationComp.prototype.init = function () {
    var needAsyncInit = this.config.needAsyncInit;
    if (needAsyncInit) {
        this.ajaxLoad();
    }
}
Comps.NavigationComp.prototype.ajaxLoad = function () {
    var ts = this;
    var compId = this.config.compId;
    $.ajax({
        "url": ContextPath+"/navigationComp/ajaxLoadData",
        data: {"compId": compId},
        "success": function (result) {
            var data = result.data;
            ts._loadData(data);
            Utils.ajaxLoadComplate(ts);
        }
    })
}
Comps.NavigationComp.prototype._loadData = function (data) {
    var showNum = this.config.showNum;
    var compId = this.config.compId;
    var ulEle = $("#" + compId + "_channelUl");
    var modelEle = ulEle.find(".j_channel");
    if($.isEmptyObject(data)){
        modelEle.remove();
        return ;
    }
    var size = data.length;
    if(size>showNum){
        size = showNum;
    }
    for (var i = 0; i <size; ++i) {
        var channel = data[i];
        var newEle = modelEle.clone();
        var aEle = newEle.find("a");
        if ($.isEmptyObject(channel.channelUrl)) {
            aEle.prop("href",this.config.customUrl + channel.pageUuid);
        } else {
            aEle.prop("href", channel.channelUrl);
        }
        aEle.html(channel.channelName);
        ulEle.append(newEle);
    }
    modelEle.remove();
}
//###########栏目导航##########7


// 组件定义
Comps.ColumnNavigation = function(option) {
    this.compId = option.compId;
    this.needAsyncInit = option.needAsyncInit;
    this.categoryUuid = option.categoryUuid;
}

Comps.ColumnNavigation.prototype.bindSomeEvent = function() {
    var contentUuid= this.GetQueryString("contentUuid");





    for (var x = 0; x < 15; x++) {

        for (var y = 0; y < 15; y++) {

            var uuid=	$("#" + this.compId + "columnnavigation_thirdtitle" + x + "" + y).attr("uuid");
            var contentSelect=	$("#" + this.compId + "columnnavigation_thirdtitle" + x + "" + y).attr("content");


            if(uuid==contentUuid){

                $("#help-breadcrumb").html(contentSelect);

            }

            $("#" + this.compId + "columnnavigation_thirdtitle" + x + "" + y).click(function() {

                var content = $(this).attr("content");



                if($.trim(content).startWith("http:")){

                    window.open(content);
                }
                else {
                    $("#help-breadcrumb").html(content);


                }






            })

        }
    }

}

String.prototype.startWith=function(str){
    var reg=new RegExp("^"+str);
    return reg.test(this);
}

Comps.ColumnNavigation.prototype.GetQueryString = function(name) {

    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;

}

Comps.ColumnNavigation.prototype.init = function() {

    var that = this;

    var contentUuid= that.GetQueryString("contentUuid");


    if (this.needAsyncInit) {

        $.ajax({
            type: "GET",
            url: ContextPath+"/columnNavigationComp/ajaxLoadData",
            data: {
                categoryUuid: that.categoryUuid
            },
            dataType: "json",
            async: true,
            success: function(data) {
                $("#help-breadcrumb").html("");

                var parentHtml = $("#" + that.compId + "_columnnavigation_panelgroup");

                // 克隆节点
                var oneHtml = $("#" + that.compId + "_columnnavigation_panel").clone();

                // 移除模板数据
                $("#" + that.compId + "_columnnavigation_panel").remove();

                console.log(data)

                $.each(data,
                    function(index, data3) {
                        console.log(data3.key.categoryName)


                        oneHtml.find(".columnnavigation_sencondtitle").text(data3.key.categoryName);

                        oneHtml.attr("id", that.compId + "_columnnavigation_panel" + (index + 1));

                        parentHtml.append(oneHtml[0].outerHTML);

                        var newHtml = parentHtml.find("#" + that.compId + "_columnnavigation_panel" + (index + 1));

                        newHtml.find(".columnnavigation_span_toggle").attr("data-target", "#panel" + index);
                        newHtml.find(".columnnavigation_toggle").attr("id", "panel" + index);


                        var secondParentEle = newHtml.find(".columnnavigation_ul");

                        var secondClonetEle = newHtml.find(".columnnavigation_li").clone();

                        secondParentEle.find(".columnnavigation_li").remove();

                        //  newHtml.remove();

                        $.each(data3.value,
                            function(index2, data2) {
                                secondClonetEle.find(".columnnavigation_thirdtitle").attr("id", that.compId + "columnnavigation_thirdtitle" + index + index2)

                                secondClonetEle.find(".columnnavigation_thirdtitle").text(data2.articlTitle);
                                secondClonetEle.find(".columnnavigation_thirdtitle").attr("content", data2.articlValue);

                                secondParentEle.append(secondClonetEle[0].outerHTML);





                                if(contentUuid){

                                    if(contentUuid==data2.contentUuid){

                                        $("#help-breadcrumb").html(data2.articlValue);



                                    }

                                }
                                else {

                                    if(index2==0&&index==0){
                                        $("#help-breadcrumb").html(data2.articlValue);
                                    }


                                }




                                $("#" + that.compId + "columnnavigation_thirdtitle" + index + index2).bind("click",
                                    function() {

                                        var content = $(this).attr("content");

                                        if (content && content != "") {


                                            if($.trim(content).startWith("http:")){

                                                window.open(content);
                                            }
                                            else {
                                                $("#help-breadcrumb").html(content);


                                            }



                                        }

                                    });

                            })

                    })

                Utils.ajaxLoadComplate(that);

            }

        })

    }

    else {
        that.bindSomeEvent();

        Utils.ajaxLoadComplate(that);

    }

}


//###########栏目导航##########
//###########全文搜索框########
/**
 * Created by 金辉 on 2017/1/11.
 */
Comps.FullSearchComp = function (config) {
    this.compId = config.compId;
    this.config = config;
}
Comps.FullSearchComp.prototype.init = function () {
    var needAsyncInit = this.config.needAsyncInit;
    var keyword = this._getUrlParam("keyword");
    if(needAsyncInit){
        this._loadData();
        Utils.ajaxLoadComplate(this);
    }
    if(!$.isEmptyObject(keyword)&&keyword!=""){
        $("#"+this.compId+"_keyword").val(keyword);
    }
    this._bindEvent();
}
Comps.FullSearchComp.prototype._loadData = function () {
    var isHaveHot = this.config.isHaveHot;
    var hotwords = this.config.hotWords;
    var compId = this.config.compId;
    var hotwordList = "";
    var defaultSelect = this.config.defaultSelect;
    var typeProEle = $("#" + compId + "_typeProduct");
    var typeStoreEle = $("#" + compId + "_typeStore");
    var typeValueEle = $("#" + compId + "_typeValue");
    if (defaultSelect == 1) {
        typeStoreEle.addClass("current");
        typeProEle.removeClass("current");
        typeValueEle.val("store");
    } else {
        typeProEle.addClass("current");
        typeStoreEle.removeClass("current");
        typeValueEle.val("product");
    }
    if (!$.isEmptyObject(hotwords)) {
        hotwordList = hotwords.split(",");
    }
    //var productSearchUrl = ContextPath + this.config.productSearchUrl;
    var compId = this.config.compId;

    var loopEle = $("#" + compId + "_hotwords");
    var hotwordEle = loopEle.find(".j_hotword");
    if (isHaveHot == "1") {
        for (var i = 0; i < hotwordList.length; ++i) {
            var hotword = hotwordList[i];
            var newEle = hotwordEle.clone();
            newEle.prop("href", Utils.getProductSearchUrl(ContextPath,this.config.productSearchUrl,hotword));
            newEle.html(hotword);
            loopEle.append(newEle);
        }
    }
    hotwordEle.remove();
}
Comps.FullSearchComp.prototype.search = function (searchType) {
    var compId = this.config.compId;
    //var searchUrl;
    //if (searchType == "product") {
    //    searchUrl = ContextPath + this.config.productSearchUrl;
    //} else {
    //    searchUrl = ContextPath + this.config.storeSearchUrl;
    //}
    var keyword = $("#" + compId + "_keyword").val();
    if ($.isEmptyObject(keyword)) {
        keyword = "";
    }
    window.location.href = Utils.getProductSearchUrl(ContextPath,this.config.productSearchUrl,keyword);
}
Comps.FullSearchComp.prototype._bindEvent = function () {
    var ts = this;
    var compId = this.config.compId;
    $("#" + compId + "_search").click(function () {
        var searchType = $("#" + compId + "_typeValue").val();
        ts.search(searchType);
    })
    $("#" + compId + "_searchType li").click(function () {
        $("#" + compId + "_typeValue").val($(this).data("searchtype"));
        $("#" + compId + "_searchType li").removeClass("current");
        $(this).addClass("current");
    })
}
Comps.FullSearchComp = function(config){
    this.compId = config.compId;
    this.config = config;
}
Comps.FullSearchComp.prototype.init=function(){
    var needAsyncInit = this.config.needAsyncInit;
    var keyword = this._getUrlParam("keyword");
    if(needAsyncInit){
        this._loadData();
        Utils.ajaxLoadComplate(this);
    }
    if(!$.isEmptyObject(keyword)&&keyword!=""){
        $("#"+this.compId+"_keyword").val(keyword);
    }
    this._bindEvent();
}
Comps.FullSearchComp.prototype._loadData=function(){
    var isHaveHot = this.config.isHaveHot;
    var hotwords = this.config.hotWords;
    var compId = this.config.compId;
    var hotwordList="";
    var defaultSelect = this.config.defaultSelect;
    var typeProEle = $("#"+compId + "_typeProduct");
    var typeStoreEle = $("#"+compId + "_typeStore");
    var typeValueEle = $("#"+compId+"_typeValue");
    if(defaultSelect == 1){
        typeStoreEle.addClass("current");
        typeProEle.removeClass("current");
        typeValueEle.val("store");
    }else{
        typeProEle.addClass("current");
        typeStoreEle.removeClass("current");
        typeValueEle.val("product");
    }
    if(!$.isEmptyObject(hotwords)){
        hotwordList = hotwords.split(",");
    }
    //var productSearchUrl=ContextPath+this.config.productSearchUrl;
    var compId = this.config.compId;

    var loopEle = $("#"+compId+"_hotwords");
    var hotwordEle = loopEle.find(".j_hotword");
    if(isHaveHot=="1"){
        for(var i=0;i<hotwordList.length;++i){
            var hotword = hotwordList[i];
            var newEle = hotwordEle.clone();
            newEle.prop("href",Utils.getFullPath(ContextPath,this.config.productSearchUrl,"keyword",hotword));
            newEle.html(hotword);
            loopEle.append(newEle);
        }
    }
    hotwordEle.remove();
}
Comps.FullSearchComp.prototype.search=function(searchType){
    var compId = this.config.compId;
    var searchUrl;
    if(searchType=="product"){
        searchUrl = this.config.productSearchUrl;
    }else{
        searchUrl = this.config.storeSearchUrl;
    }
    var keyword = $("#"+compId+"_keyword").val();
    if($.isEmptyObject(keyword)){
        keyword = "";
    }
    window.location.href=Utils.getFullPath(ContextPath,searchUrl,"keyword",keyword);
}
Comps.FullSearchComp.prototype._bindEvent=function(){
    var ts = this;
    var compId = this.config.compId;
    $("#"+compId+"_search").click(function(){
        var searchType = $("#"+compId+"_typeValue").val();
        ts.search(searchType);
    })
    $("#"+compId+"_searchType li").click(function(){
        $("#"+compId+"_typeValue").val($(this).data("searchtype"));
        $("#"+compId+"_searchType li").removeClass("current");
        $(this).addClass("current");
    })
}
Comps.FullSearchComp.prototype._getUrlParam=function(name){
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg); //匹配目标参数
    if (r != null) return decodeURIComponent(r[2]); return null; //返回参数值
}

//###########全文搜索框########


//###########图片广告##########9



Comps.PictureAd = function(option) {
    this.compId = option.compId;
    this.needAsyncInit = option.needAsyncInit;
    this.imgSrc = option.imgSrc;
    this.defineUrl = option.defineUrl;
    this.defineWidth = option.defineWidth;
    this.defineHeight = option.defineHeight;

}

Comps.PictureAd.prototype.GetQueryString = function(name) {

    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;

}


Comps.PictureAd.prototype.init = function() {

    var that =this;


    if (this.needAsyncInit) {



        $("#"+that.compId+"_img").attr("src",that.imgSrc);
        $("#"+that.compId+"_img").attr("width",that.defineWidth);
        $("#"+that.compId+"_img").attr("height",that.defineHeight);


    }

    that.bindSomeEvent();
    Utils.ajaxLoadComplate(that);



}


Comps.PictureAd.prototype.bindSomeEvent=function(){

    var that=this;



    $("#"+that.compId+"_a").click(function(){

        window.open(ContextPath+that.defineUrl);


    })
}



//###########图片广告##########


/**
 * Created by 金辉 on 2017/1/11.
 */

//前台分类组件
Comps.CategoryComp = function (compConfig) {
    this.compId = compConfig.compId;
    this.config = compConfig;
}
Comps.CategoryComp.prototype.init = function () {
    var needAsyncInit = this.config.needAsyncInit;
    if (needAsyncInit) {
        this.ajaxLoadData();
    }
}
Comps.CategoryComp.prototype.ajaxLoadData = function () {
    var ts = this;
    var compId = this.config.compId
    $.ajax({
        "url": ContextPath+"/categoryComp/ajaxLoadData",
        data: {"compId": compId},
        "success": function (data) {
            ts.render(data.data);
            Utils.ajaxLoadComplate(ts);
        }
    })
}
Comps.CategoryComp.prototype.render = function (data) {
    var compId = this.config.compId;
    var showType = this.config.showType;
    var firstLevelNum = this.config.firstLevelNum;
    var dropDiv = $("#" + compId + "_dropDiv");
    var loopEle = $("#" + compId + "_loop");
    var liEle = loopEle.find("li");
    if (showType == 0) {
        dropDiv.prop("style", "display:block");
    }
   if(data=="无数据"){
       liEle.remove();
   }else{
       console.log(data);
       var length = data.length;
       if (length > firstLevelNum) {
           length = firstLevelNum;
       }
       for (var i = 0; i < length; ++i) {
           var catModel = data[i];
           var newEle = liEle.clone();
           //渲染一级分类，根据配置判断是否带二级分类
           newEle = this._renderFirstCat(newEle, catModel);
           //渲染右侧分类
           newEle = this._renderRitghtCat(newEle, catModel);
           loopEle.append(newEle);
       }
       liEle.remove();
   }
}

/**
 * 渲染一级分类，根据配置判断是否带二级分类
 * @param m
 */
Comps.CategoryComp.prototype._renderFirstCat = function (newEle, m) {
    var showLevel = this.config.showLevel;
    var isShowImage = this.config.isShowImage;
    var secondLevelNum = this.config.secondLevelNum;
    var catImgEle = newEle.find(".j_catImg");
    var firstCatEle = newEle.find(".j_firstCat");
    var secLoopEle = newEle.find(".j_secCatLoop");
    var secAEle = secLoopEle.find(".j_secCat");
    if (isShowImage == 0) {
        catImgEle.prop("src", m.img);
    }
    firstCatEle.html(m.categoryName);
    firstCatEle.prop("href", m.url);
    if (showLevel == 1) {
        var secList = m.subCategory;
        var length = secList.length;
        if (length > secondLevelNum) {
            length = secondLevelNum;
        }
        for (var i = 0; i < length; ++i) {
            var secModel = secList[i];
            var newSecA = secAEle.clone();
            newSecA.prop("href", secModel.url);
            newSecA.html(secModel.categoryName);
            secLoopEle.append(newSecA);
        }
    }
    secAEle.remove();
    return newEle;
}

/**
 * 渲染右侧分类
 * @param m
 */
Comps.CategoryComp.prototype._renderRitghtCat = function (newEle, m) {
    var rightLoop = newEle.find(".j_rightLoop");
    var rightDl = rightLoop.find(".j_rightDl");

    var secList = m.subCategory;
    for (var i = 0; i < secList.length; ++i) {
        var secModel = secList[i];
        var newSecCat = rightDl.clone();
        var secCat = newSecCat.find(".j_rightSecCat");
        secCat.prop("href", secModel.url);
        secCat.html(secModel.categoryName);
        var thdCatLoop = newSecCat.find(".j_rightThdCatLoop");
        var thdCat = newSecCat.find(".j_rightThdCat");
        var thdCatList = secModel.subCategory;
        if (!$.isEmptyObject(thdCatList)) {
            for (var j = 0; j < thdCatList.length; ++j) {
                var thdModel = thdCatList[j];
                var newThdCat = thdCat.clone();
                newThdCat.prop("href", thdModel.url);
                newThdCat.html(thdModel.categoryName);
                thdCatLoop.append(newThdCat);
            }
        }
        thdCat.remove();
        rightLoop.append(newSecCat);
    }
    rightDl.remove();
    return newEle;
}


/************************************** slideProduct START**************************************/

Comps.SlideProducts = function(option){
    this.compId = option.compId;
    this.needAsyncInit = option.needAsyncInit;
    this.productUuids = option.productUuids;
    this.productDetailUrl = option.productDetailUrl;
    this.slideSpeed = option.slideSpeed;
    this.slideAuto = option.slideAuto;
    this.interTime = option.interTime;
    this.loginTopic = option.loginTopic;
    this.dataCount = 0;
    this.collectUrl = option.collectUrl;
}

Comps.SlideProducts.prototype.init=function(){
    var that = this;
    if(this.needAsyncInit){
        $.getJSON( ContextPath+"/slideproducts/ajaxLoadData",// "/producttab/getInitData" "/syscomps/pages/producttab/initData.json"
            {productUuids:this.productUuids},
            function(data){
                if(data ){
                    //生成页面HTML
                    var template = that.initTemplate();
                    that.genHtml(data,template);
                    that.initEvents();
                    Utils.ajaxLoadComplate(that);
                }
            });
    }else{
        $(document).ready(function(){
            that.initEvents();
            Utils.ajaxLoadComplate(that);
        });
    }
}

Comps.SlideProducts.prototype.initTemplate=function(){
    var compId = this.compId;
    //保存 详情模板，防止在嵌套循环中模板被删除掉
    var root = $("#"+this.compId+"-products");
    var productDeail = root.find(".j_product-detail");
    var productTmplate = productDeail;
    productDeail.remove();

    //商品组 模板，8个商品一组，产生轮播效果（8个商品在一个LI内）,轮播LI
    var productLi = root.find(".j_tab-product-page-group");
    var productLiTmplate = productLi;
    productLi.remove();

    return {"productTmplate":productTmplate,"productLiTmplate":productLiTmplate};
}

Comps.SlideProducts.prototype.genHtml= function(products,template){
    if(products&& products.length>0)
    {
        this.dataCount = products.length;
        var compId = this.compId;
        var that = this;
        var pagingContainerUl = $("#"+compId+"-paging-container");

        var productGroupLi = template.productLiTmplate;
        var groups = this.groupProducts(products);
        for(var j = 0 ; j < groups.length ; j++){
            //循环每个分组
            var groupProducts = groups[j];//每个分组内的商品
            //创建分组
            var tmpGroupLi = productGroupLi.clone();
            //往分组内添加 商品数据
            this.genOneGroupHtml(groupProducts,tmpGroupLi,template);
            //将新生成的li追加到Ul下
            pagingContainerUl.append(tmpGroupLi);
        }
    }else{
        //没有配置数据
        console.log("没有配置数据");
    }

}

Comps.SlideProducts.prototype.genOneGroupHtml = function(products,tmpGroupLi,template){
    //商品模板
    var productDetail = template.productTmplate;
    var that = this;
    for(var i = 0 ,len = products.length ; i < len ; i++){
        var product = products[i];
        var tmpProduct = productDetail.clone();
        tmpProduct.find(".j_product-name").html(product.productName);
        tmpProduct.find(".j_product-price").html(product.price);

        var link = Utils.getProductDetailUrl(ContextPath,that.productDetailUrl,product.productUuid);
        tmpProduct.find("a.j_product-link").attr("href",link);
        tmpProduct.find("a.j_product-collect").attr("data-id",product.productUuid);
        tmpProduct.find(".j_product-photo").attr("src",product.pic);
        tmpGroupLi.append(tmpProduct);
    }
}

//将 商品分组，8个商品为一组，获得一个二位数组
Comps.SlideProducts.prototype.groupProducts = function(products){
    var res = new Array();
    for(var i = 0 ,len = products.length ; i < len ; i++){
        var arr = null;
        if((i+1) % 8 == 1){
            arr  = new Array();
            res.push(arr);
        }else{
            arr = res[res.length-1];
        }
        arr.push(products[i]);
    }
    return res;
}

//商品收藏事件
Comps.SlideProducts.prototype.collectProduct=function(uuid){
    var that=this;
    var compId = this.compId;
    var url = ContextPath+"/collectionProductComp/collectProduct";
    var collectCenterUrl = ContextPath + this.collectUrl;
    var $returnMessage = $("#"+compId+"_returnMessage");
    var $collectNum = $("#"+compId+"_collectNum");
    //设置收藏中心的URL...
    $("#"+compId+"_collectUrl").attr("href",collectCenterUrl);
    $.ajax({
        type : "GET",
        url : url,
        data : {
            productUuid : uuid,
            ranNum : Math.random()
        },
        dataType : "json",
        async : false,
        success : function(data) {
            if(data.state=="1"){
                $collectNum.html(data.count);
                $returnMessage.html("收藏成功");
            }
            else if (data.state=="2"){
                $collectNum.html(data.count);
                $returnMessage.html("收藏失败");
            }
            else if (data.state=="3"){
                $collectNum.html(data.count);
                $returnMessage.html("您已经收藏过改商品了");
            }

            $("#"+compId+"_modal-collect-form").modal('show');

        }
    })
}

Comps.SlideProducts.prototype.initEvents = function(){
    var that = this;
    var speed = this.slideSpeed;//轮播速度
    var auto = this.slideAuto;//是否自动轮播
    var interTime = this.interTime;//自动轮播时 轮播间隔
    $("#"+this.compId+" .j_tab-content-wrap").each(function(){
        $(this).slide({mainCell:".itembd ul",effect:"leftLoop",prevCell:".sprev",nextCell:".snext",
            delayTime:speed,
            autoPlay:auto ,
            interTime:interTime
        });
    });
    //商品收藏
    $("#"+this.compId).find(".j_product-collect").on("click",function(){
        var productUuid = $(this).attr("data-id");
        if(that.loginTopic){
            CompMessage.publish(that.loginTopic,{},function(){
                that.collectProduct(productUuid);
            });
        }

    });
}

/************************************** slideProduct END**************************************/
