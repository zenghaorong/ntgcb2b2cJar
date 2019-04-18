/**
 * Created by yewei on 2017/5/4.
 */


/**************************************工具 START***********************/
(function ($) {
    // 默认参数
    var defaults =
        {
            // 鼠标遮罩层样式
            shadecolor: "#FFD24D",
            shadeborder: "#FF8000",
            shadeopacity: 0.5,
            cursor: "move",

            // 大图外层样式
            layerwidth: 400,
            layerheight: 300,
            layerborder: "#DDD",
            fade: true,

            // 大图尺寸
            largewidth: 1280,
            largeheight: 960
        }

    // 插件入口
    var enlarge = function (option) {

        // 合并参数
        option = $.extend({}, defaults, option);

        var self = $(this).css("position", "relative").css("float","left");

        var img = self.children().first();

        // 计算大小图之间的比例
        var ratio =
            {
                x: img.width() / option.largewidth,
                y: img.height() / option.largeheight
            }

        // 定义一些尺寸
        var size =
            {
                // 计算鼠标遮罩层的大小
                shade:
                    {
                        width: option.layerwidth * ratio.x - 2,
                        height: option.layerheight * ratio.y - 2
                    }
            }


        // 创建鼠标遮罩层
        var shade = $("<div>").css(
            {
                "position": "absolute",
                "left": "0px",
                "top": "0px",
                "background-color": option.shadecolor,
                "border": "1px solid " + option.shadeborder,
                "width": size.shade.width,
                "height": size.shade.height,
                "opacity": option.shadeopacity,
                "cursor": option.cursor
            });
        shade.hide().appendTo(self);

        // 创建大图和放大图层
        var large = $("<img>").css(
            {
                "position": "absolute",
                "display": "block",
                "width": option.largewidth,
                "height": option.largeheight
            });
        var layer = $("<div>").css(
            {
                "position": "absolute",
                "left": self.width() + 5,
                "top": 0,
                "background-color": "#FFF",
                "overflow": "hidden",
                "width": option.layerwidth,
                "height": option.layerheight,
                "border": "1px solid " + option.layerborder,
                "z-index":999
            });
        large.attr("src", img.attr("data-big"));
        large.appendTo(layer);
        layer.hide().appendTo(self);

        // 不可移动的半径范围
        var half =
            {
                x: size.shade.width / 2,
                y: size.shade.height / 2
            }

        // 有效范围
        var area =
            {
                width: self.innerWidth() - shade.outerWidth(),
                height: self.innerHeight() - shade.outerHeight()
            }

        // 对象坐标
        var offset = self.offset();

        // 显示效果
        var show = function () {
            offset = self.offset();
            shade.show();
            if (option.fade) layer.stop().fadeIn(300);
            else layer.show();
        }

        // 隐藏效果
        var hide = function () {
            shade.hide();
            layer.hide();
        }

        // 绑定鼠标事件
        self.mousemove(function (e) {

            // 鼠标位置
            var x = e.pageX - offset.left;
            var y = e.pageY - offset.top;

            // 不在图像中则直接隐藏
            if (x < 0 || x > self.innerWidth()) return hide();
            if (y < 0 || y > self.innerHeight()) return hide();

            // 判断是否在有效范围内
            x = x - half.x;
            y = y - half.y;

            if (x < 0) x = 0;
            if (y < 0) y = 0;
            if (x > area.width) x = area.width;
            if (y > area.height) y = area.height;

            // 遮罩层跟随鼠标
            shade.css(
                {
                    left: x,
                    top: y
                });

            // 大图移动到相应位置
            large.css(
                {
                    left: (0 - x / ratio.x),
                    top: (0 - y / ratio.y)
                });
        })
            .mouseenter(show)
            .mouseleave(hide);

        // 提供接口 修改图片的url
        var plugin = {
            "_img":img,
            "_large":large,
            "changeUrl":function(middleUrl,bigUrl){
                //先缓存图片
                this._cacheImg(middleUrl);
                this._cacheImg(bigUrl);
                this._img[0].src = middleUrl;
                //设置大图url
                this._img.attr('data-big',bigUrl);
                //设置大图
                this._large[0].src = bigUrl;
            },
            "_cacheImg":function(url){
                var isExist = false;
                $(".j_caImg").each(function(index,ele){
                    if($(ele).attr("src")==url)isExist = true;
                });
                if(!isExist){
                    $("<img>").css({"display": "none","width":0,"height": 0}).attr("src",url).addClass("j_caImg").appendTo($('body'));
                }
            }
        };

        return plugin;
    }

    // 扩展插件
    $.fn.extend(
        {
            enlarge: enlarge
        });


})(jQuery);


//图片列表滚动组件
(function ($){
    var defaultOption = {
        count:5,
        mouseover:function(){return true;}
    }
    //封装 小图列表滚动事件
    var imgslider = function(option){
        option = $.extend({}, defaultOption, option);
        var $that = this;
        var displayCount = option.count;
        var stopNextIndex = $that.find('.j_img-items li').length - displayCount;/** 显示 5张图片**/
        //初始化 上一个下一个 是否能点
        $that.find('.j_img-prev').addClass('j_disabled');
        if(stopNextIndex < 1){ //图片数目小于要展示的5张图片时，设置不能下一张
            $that.find('.j_img-next').addClass('j_disabled');
        }

        var firstLi = $that.find(".j_img-items li:first");
        //动态计算每次移动的距离
        var interval = firstLi.width()+parseInt(firstLi.css("margin-left")) + parseInt(firstLi.css("margin-right"));
        //console.log(parseInt($(".j_img-items li").css("border-left-width"))) 边框宽度
        var curIndex = 0;
        var silder = $that.find('.j_scrollbutton');

        silder.click(function(){
            if( $(this).hasClass('j_disabled') ) return false;
            if ($(this).hasClass('j_img-prev')) --curIndex;
            else ++curIndex;
            silder.removeClass('j_disabled');
            if (curIndex == 0)  $that.find('.j_img-prev').addClass('j_disabled');
            if (curIndex == stopNextIndex)  $that.find('.j_img-next').addClass('j_disabled');
            $(".j_img-items ul").stop(false, true).animate({"marginLeft" : -curIndex*interval + "px"}, 200);
        });

        $that.find('.j_img-items li img').bind("click", function(){}).bind("mouseover", function(){
            if (!$(this).parent().hasClass('img-hover')) {
                var that = this;
                if(option.mouseover){
                    if(option.mouseover(that)){
                        //先清除所有的样式
                        $(that).parent().siblings().removeClass('img-hover');
                        $(that).parent().addClass('img-hover');
                    };
                }
            }

        }).bind("mouseout", function(){});

    };

    // 扩展插件
    $.fn.extend({imgslider: imgslider});

})(jQuery);

/**************************************工具END****************************************************/

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
        var xx=decodeURI(that.content);
        $("#"+that.compId+"_content").html(decodeURI(that.content));
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
                                    if(contentUuid==data2.articlUuid){

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
    firstCatEle.prop("href","   /front/list?categoryUuid="+m.categoryUuid);
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
        secCat.prop("href", "/front/list?categoryUuid="+secModel.categoryUuid);
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
        tmpProduct.find(".j_product-price").html((product.price/100).toFixed(2));

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
            sku : uuid,
            ranNum : Math.random()
        },
        dataType : "json",
        async : false,
        success : function(data) {
            data=eval("("+data+")");
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
                $returnMessage.html("收藏成功");
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
/**
 * Created by 金辉 on 2017/1/11.
 */
Comps.ProductList = function(config){
    this.compId = config.compId;
    this.config = config;
}
//异步情况下渲染数据
Comps.ProductList.prototype.init=function(){
    var ts = this;
    var needAsyncInit = ts.config.needAsyncInit;
    var totalNum = $("#"+ts.config.compId+"_productTotalNum").html();
    if(needAsyncInit){
        ts.ajaxLoadData();
    }else{
        ts._initPages(totalNum,ts.config.pageShow);
    }
    this.bindEvent();
}
//ajax请求数据
Comps.ProductList.prototype.ajaxLoadData =function(queryData){
    var ts = this;
    var defaultData = {
        "compId":ts.config.compId,
        "keyword":ts._getUrlParam("keyword"),
        "categoryUuid":ts._getUrlParam("categoryUuid"),
        "sortType":ts._getUrlParam("sortType"),
        "sortBy":ts._getUrlParam("sortBy"),
        "attrValue":ts._getUrlParam("attrValue"),
        "nowPage":ts._getUrlParam("nowPage"),
        "pageShow":ts.config.pageShow

    }
    $.extend(defaultData,queryData);
    $.ajax({
        "url":ContextPath+"/productListComp/ajaxLoadData",
        data: defaultData,
        "success":function(data){
            ts._renderProdcutList(data);
            ts._initPages(data.totalNum,data.pageShow);
            Utils.ajaxLoadComplate(ts);
        }
    })
}
//渲染商品列表
Comps.ProductList.prototype._renderProdcutList = function(data){
    var compId = this.config.compId;
    var productsEle = $("#"+compId+"_products");
    var noResultEle = $("#"+compId + "_noResult");
    var productLoopEle = $("#"+compId+"_productLoop");
    var productTotalNumEle = $("#"+compId+"_productTotalNum");
    var productEle = productsEle.find(".j_product").first();
    productsEle.find(".j_product").remove();
    if(data ==null || data ==""){
        productEle.remove();
    }else{
        var productList = data.productList;
        //循环渲染商品信息
        if(productList.length>0){
            productTotalNumEle.html(data.totalNum);
            noResultEle.remove();
            for(var i=0;i<productList.length;++i){
                var model = productList[i];
                var newEle = productEle.clone();
                //渲染商品Element
                this._renderProductInfo(newEle,model);
                productLoopEle.append(newEle);
            }
        }else{//若无商品，提示无商品
            productEle.remove();
        }
    }
}
//渲染商品信息
Comps.ProductList.prototype._renderProductInfo=function(newEle,model){
    var productIdEle = newEle.find(".j_productId");
    //价格
    var picEle = newEle.find(".j_pic");
    //图片
    var picHoverEle = newEle.find(".j_picHover");
    //评分
    var scoreEle = newEle.find(".j_score");
    //商品名称
    var productNameEle = newEle.find(".j_productName");
    /*商品详情路径*/
    var productUrlEle = newEle.find(".j_productUrl");
    //商品推荐语
    var recommendEle = newEle.find(".j_recommend");
    //商品价格
    var priceEle = newEle.find(".j_price");
    //所属商户名称
    var storeName = newEle.find(".j_storeName");
    //所属商户logo
    var storeLogo = newEle.find(".j_storeLogo");
    productIdEle.html(model.sku);
    if($.isEmptyObject(model.pic)){
        picEle.prop("src","");
        picHoverEle.prop("src","");
    }else{
        picEle.prop("src",model.pic);
        picHoverEle.prop("src",model.pic);
    }
    picEle.prop("alt",model.productName);
    picHoverEle.prop("alt",model.productName);
    scoreEle.prop("style","width:"+model.score*20+"%");
    productNameEle.prop("href",Utils.getProductDetailUrl(ContextPath,this.config.productInfoUrl,model.sku));
    productNameEle.html(model.productName);
    productUrlEle.prop("href",Utils.getProductDetailUrl(ContextPath,this.config.productInfoUrl,model.sku));
    recommendEle.html(model.recommend);
    priceEle.html((model.price/100).toFixed(2));
   // storeName.prop("title",model.storeName);
    storeName.html(model.storeName);
   // storeLogo.prop("src",model.storeLogo);
}
//初始化分页栏
Comps.ProductList.prototype._initPages = function(totalNum,pageShow){
    var ts = this;
    var compId = ts.config.compId;
    layui.use(['laypage', 'layer'], function() {
        var laypage = layui.laypage
            , layer = layui.layer;
        var pages = Math.ceil(totalNum/pageShow);
        if(pages<1){
            pages = 1;
        }
        var uri = window.location.href;
        var reg = new RegExp("(^|&)nowPage=([^&]*)");
        var r = uri.match(reg);
        if(!$.isEmptyObject(r)){
            var curr = r[2];
        }
        laypage({
            cont: compId+'_paging'
            , pages: pages //总页数
            , groups: 5 //连续显示分页数
            ,curr: curr
            ,jump: function(obj, first){
                if(!first) {
                    var uriName=window.location;
                    uriName=uriName.origin+uriName.pathname;
                    var index=uri.indexOf("?");
                    console.log(r);
                    if(index>0){
                        var words = $.isEmptyObject(r)?"":r[0];
                        ts.changeUrl(uri,words,"&nowPage="+obj.curr);
                        ts.ajaxLoadData();
                        //跳转锚点
                        $("html,body").animate({scrollTop:$("#"+ts.config.compId+"_productLoop").offset().top}, 0);
                    }else{
                        var words = $.isEmptyObject(r)?"":r[0];
                        ts.changeUrl(uri,words,"?nowPage="+obj.curr);
                        ts.ajaxLoadData();
                        //跳转锚点
                        $("html,body").animate({scrollTop:$("#"+ts.config.compId+"_productLoop").offset().top}, 0);
                    }

                }
            }
        });
    })
}
//无刷新修改URL
Comps.ProductList.prototype.changeUrl=function(uri,words,replaceWords){
    var title = document.title;
    var state=({
        url: uri, title: title
    });
    if(!$.isEmptyObject(words)&&""!=words){
        uri=uri.replace(words,replaceWords);
    }else{
        uri=uri+replaceWords;
    }
    window.history.pushState(state, title, uri);
}
//获取URL上参数
Comps.ProductList.prototype._getUrlParam=function(name){
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg); //匹配目标参数
    if (r != null) return decodeURIComponent(r[2]); return null; //返回参数值
}
//商品收藏
Comps.ProductList.prototype.collectProduct=function(element){
    var ts = this;
    var compId = this.config.compId;
    var productEle = $(element).parents(".j_product");
    var productIdEle = productEle.find(".j_productId");
    var productId = productIdEle.html().trim();
    var returnMessage = $("#"+compId+"_returnMessage");
    var collectNum = $("#"+compId+"_collectNum");
    var collectUrl = $("#"+compId+"_collectUrl");
    var url = ContextPath+"/productListComp/collectProduct.json";
    $.ajax({
        "url":url,
        "data":{"productUuid":productId},
        "success":function(data){
           // data=eval("("+data+")");
            var code = data.code;
            var totalNum = data.totalNum;
            if(code==1){
                collectNum.html(totalNum);
                collectUrl.prop("href", ContextPath+ts.config.favoriteProUrl);
                returnMessage.html("收藏成功！");
            }else if(code==2){
                collectNum.html(totalNum);
                collectUrl.prop("href", ContextPath+ts.config.favoriteProUrl);
                returnMessage.html("收藏失败！");
            }else if(code==3){
                collectNum.html(totalNum);
                collectUrl.prop("href", ContextPath+ts.config.favoriteProUrl);
                returnMessage.html("收藏成功！");
            }else{
                returnMessage.html(data);
            }
            $("#"+compId+"_modal-collect-form").modal('show');
        },
        "error":function(data){
            alert(data.responseText);
        }
    })
}

Comps.ProductList.prototype.bindEvent = function(){
    var ts = this;
    var compId = ts.config.compId;
    $("#"+compId).delegate(".j_collectProduct","click",function(){
        var that = this;
        CompMessage.publish(ts.config.topic,'',function(data){
            ts.collectProduct(that);
        });
    })
    $("a[id^='"+compId+"_sort_']").click(function(){
        var nameValue = $(this).prop("name");
        var sortType = "1";
        var type = $(this).find("i").attr("class");
        if(type=="fa fa-long-arrow-up"){
            sortType="1";
        }else if(type=="fa fa-long-arrow-down"){
            sortType="2";
        }
        var queryData = {
            "sortBy":nameValue,
            "sortType":sortType,
            "nowPage":"1"
        }
        $(this).find('.fa').toggleClass('fa-long-arrow-down').toggleClass('fa-long-arrow-up');
        ts.ajaxLoadData(queryData);
    })
    $("#"+compId+"_productPicSmall").click(function(){
        $("#"+compId).removeClass("m-pdblock");
        $("#"+compId).addClass("m-pdlist");
        $("#"+compId+"_productPicBig").removeClass("active");
        $(this).removeClass("active");
    })
    $("#"+compId+"_productPicBig").click(function(){
        $("#"+compId).removeClass("m-pdlist");
        $("#"+compId).addClass("m-pdblock");
        $("#"+compId+"_productPicSmall").removeClass("active");
        $(this).removeClass("active");
    })
}

/************************************** productTab START**************************************/

Comps.ProductTab = function(option){
    this.compId = option.config.compId;
    this.needAsyncInit = option.config.needAsyncInit;
    this.showStyle = option.config.showStyle;
    this.tabsJson = JSON.stringify(option.tabsJson);
    this.productDetailUrl = option.config.productDetailUrl;
    this.loginTopic = option.config.loginTopic;
    this.collectUrl = option.config.collectUrl;
}

//初始化保存模板
Comps.ProductTab.prototype.initTemplate= function(){
    //保存 详情模板，防止在嵌套循环中模板被删除掉
    var root = $("#"+this.compId+"-products");
    var productDeail = root.find(".j_product-detail");
    var productTmplate = productDeail;
    productDeail.remove();

    //商品组 模板，8个商品一组，产生轮播效果（8个商品在一个LI内）
    var productLi = root.find(".j_tab-product-page-group");
    var productLiTmplate = productLi;
    productLi.remove();

    //标签页内容模板，每个标签页对应一个标签页内容
    var tabContent = root.find(".j_tab-content-wrap");
    var tabContentTemplate = tabContent;
    tabContent.remove();

    return {"productTmplate":productTmplate,"productLiTmplate":productLiTmplate,"tabContentTemplate":tabContentTemplate};
}

Comps.ProductTab.prototype.init= function(){
    var that = this;
    if(this.needAsyncInit){
        $.ajax({
            type : "GET",
            url : ContextPath+"/producttab/getInitData",// "/producttab/getInitData" "/syscomps/pages/producttab/initData.json",
            data : {tabJson:this.tabsJson},
            dataType : "json",
            success : function(data) {
                if(data ){
                    //先初始化模板，生成页面需要这些模板
                    var template = that.initTemplate();
                    //生成页面HTML
                    that.genHtml(data,template);
                    that.initEvents();
                    Utils.ajaxLoadComplate(that);
                }
            }
        });
    }else{
        $(document).ready(function(){
            that.initEvents();
            Utils.ajaxLoadComplate(that);
        });
    }
}

Comps.ProductTab.prototype.genHtml= function(data,template){
    var compId = this.compId;
    var that = this;
    //标签页tab
    var tabs = $("#"+compId+"-tabs");
    var liEl = tabs.find(".j_product-tab");
    liEl.remove();

    var tabContents = $("#"+compId+"-products");
    var tabContentTemplate = template.tabContentTemplate;

    for(var i =0, len = data.length; i < len ; i++){
        var tabData = data[i];
        var tabContentId = compId+"-tab-content-"+i;
        //生成一个标签页
        var tmpLi = liEl.clone();
        tmpLi.attr("href","#"+tabContentId);//标签页锚向标签页内容容器
        tmpLi.find(".j_tab-name").html(tabData.tabName);
        if(i==0)tmpLi.addClass("on");
        tabs.append(tmpLi);
        //生成一个标签页对应的内容容器
        var tmpTabContent  = tabContentTemplate.clone();
        tmpTabContent.attr("id",tabContentId);
        //常规展示 不需添加slide
        if(that.showStyle == "1")tmpTabContent.addClass("m-noslide");
        //向容器里面添加数据
        that.genTabProducts(tabData.products,tmpTabContent,template);
        tabContents.append(tmpTabContent);
    }
}

//根据 商品 生成一个标签页中的 商品
Comps.ProductTab.prototype.genTabProducts = function(products,container,template){
    var pagingContainerUl = container.find("ul.j_tab-product-page-wrap");
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

}

Comps.ProductTab.prototype.genOneGroupHtml = function(products,tmpGroupLi,template){
    //商品模板
    var productDetail = template.productTmplate;
    var that = this;
    for(var i = 0 ,len = products.length ; i < len ; i++){
        var product = products[i];
        var tmpProduct = productDetail.clone();
        tmpProduct.find(".j_product-name").html(product.productName);
        tmpProduct.find(".j_product-price").html((product.price/100).toFixed(2));
        var link = Utils.getProductDetailUrl(ContextPath,that.productDetailUrl,product.productUuid);
        tmpProduct.find("a.j_product-link").attr("href",link);
        tmpProduct.find("a.j_product-collect").attr("data-id",product.productUuid);
        tmpProduct.find(".j_product-photo").attr("src",product.pic);
        tmpGroupLi.append(tmpProduct);
    }
}


//将 商品分组，8个商品为一组，获得一个二位数组
Comps.ProductTab.prototype.groupProducts = function(products){
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

// 商品收藏事件
Comps.ProductTab.prototype.collectProduct=function(uuid){
    var that=this;
    var compId = this.compId;
    var url = ContextPath+"/collectionProductComp/collectProduct";
    var collectCenterUrl = ContextPath + this.collectUrl;
    var $returnMessage = $("#"+compId+"_returnMessage");
    var $collectNum = $("#"+compId+"_collectNum");
    //设置收藏中心的URL
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
            data=eval("("+data+")");
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
                $returnMessage.html("收藏成功");
            }

            $("#"+compId+"_modal-collect-form").modal('show');

        }
    })
}

Comps.ProductTab.prototype.initEvents= function(){
    var that = this;
    //轮播slider
    if(this.showStyle=="2"){
        $("#"+this.compId+" .j_tab-content-wrap").each(function(){
            $(this).slide({mainCell:".itembd ul",effect:"leftLoop",prevCell:".sprev",nextCell:".snext"});
        });
    }
    //外层slider
    $("#"+this.compId+".m-productmc-slider").slide({});

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
/************************************** productTab END**************************************/

//###########精选商品##########2
Comps.ChooseWellProduct = function(option){
    this.compId = option.compId;
    this.productUuid = option.param_inProductUuids;
    this.needAsyncInit = option.needAsyncInit;
    this.lableName = option.lableName;
    this.productUrl=option.productUrl;
    this.topic=option.topic;
}
$(function () { $("[data-toggle='tooltip']").tooltip(); });

Comps.ChooseWellProduct.prototype.init=function(){
    // tip
    var that=this;

    if(this.needAsyncInit){


        $.ajax({
            type: "GET",
            url: ContextPath+"/chooseWellProductComp/ajaxLoadData",
            data: {productUuid:that.productUuid, lableName:that.lableName},
            dataType: "json",
            async:true,
            success: function(data){
                data=eval("("+data+")");
                var listdata=  data.rows;


                $(".j_choosewellproduct_lable").html(data.lableName);

                var oneProductHtml=	$(".j_choosewellproduct_list").eq(0).clone();


                $(".j_choosewellproduct_all").html('');

                if(listdata.length>0){


                    for(var i=0;i<listdata.length;i++){


                        oneProductHtml.find("img").attr("src",listdata[i].imgsrc);
                        oneProductHtml.find(".j_choosewellproduct_title").text(listdata[i].name);

                        oneProductHtml.find(".j_choosewellproduct_title").attr("uuid",listdata[i].productUuid);
                        oneProductHtml.find(".j_choosewellproduct_collection").attr("uuid",listdata[i].productUuid);
                        oneProductHtml.find(".j_choosewellproduct_price").text("￥"+(listdata[i].price/100).toFixed(2));
                        $(".j_choosewellproduct_all").append(oneProductHtml[0].outerHTML);


                    }

                }

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

Comps.ChooseWellProduct.prototype.collectProduct=function(uuid){



    var that=this;


    var url = ContextPath+"/collectionProductComp/collectProduct";


    var returnMessage = $("#"+this.compId+"_returnMessage");
    var collectNum = $("#"+this.compId+"_collectNum");
    var collectUrl = $("#"+this.compId+"_collectUrl");

    $.ajax({
        type : "GET",
        url : url,
        data : {
            productUuid : uuid,
            ranNum : Math.random()
        },
        dataType : "json",
        async : true,
        success : function(data) {
            data=eval("("+data+")");

            var msg="";

            if(data.state=="1"){

                msg="收藏成功!";

            }
            else if (data.state=="2"){

                msg="收藏失败!";

            }

            else if (data.state=="3"){

                msg="收藏成功!";

            }
            returnMessage.html(msg);

            collectNum.html(data.count);

            collectUrl.attr("href",ContextPath+that.collectProductUrl);

            $("#"+that.compId+"_modal-collect-form").modal('show');

            that.bindSomeEvent();

            Utils.ajaxLoadComplate(that);
        }

    })



}

Comps.ChooseWellProduct.prototype.bindSomeEvent=function(){

    var that=this;






    $(".j_choosewellproduct_list").each(function(){

        var productlist=$(this);


        $(this).find("a[data-toggle!='tooltip']").bind("click",function(){

            var uuid=$(productlist).find(".j_choosewellproduct_title").attr("uuid");



            var url=Utils.getProductDetailUrl(ContextPath,that.productUrl,uuid);
            window.open(url);

        })

    })


    $(".j_choosewellproduct_collection").click(function() {

        var uuid = $(this).attr("uuid");

        CompMessage.publish(that.topic,'',function(data){

            that.collectProduct(uuid);
        });

    })




}







//#####################
////***********************顶部组件*****************
Comps.TopComp = function(option){
    this.compId = option.compId;
    this.needAsyncInit = option.needAsyncInit;
    this.option = option;
}

Comps.TopComp.prototype.init = function(){
    var that = this;
    var compId = this.compId;
    //1.是否懒加载，如果是懒加载，则下面数据通过ajax请求，渲染html页面
    if(this.needAsyncInit){
        $.ajax({
            type: "GET",
            url: ContextPath+"/topComp/ajaxLoadData",
            data: {},
            dataType: "json",
            success: function(data){
                that.genViewHtml(data);
                that.bindSomeEvent();
                Utils.ajaxLoadComplate(that);
            }
        });
    }else{
        that.bindSomeEvent();
        Utils.ajaxLoadComplate(that);
    }

    var subTopic = this.option.subTopic;
    if(subTopic != null && subTopic != ""){
        var topicList = subTopic.split(",");
        $.each(topicList,function(){
            CompMessage.subscribe(this,function(option,callback){
                //订阅到登录成功刷新顶部
                $.ajax({
                    type: "GET",
                    url: ContextPath+"/topComp/ajaxLoadData",
                    data: {},
                    dataType: "json",
                    success: function(data){
                        that.genViewHtml(data);
                    }
                });
            });
        });
    }
}

Comps.TopComp.prototype.genViewHtml = function(data){
    var compId = this.compId;
    var platName = data.platName;
    $("#"+compId+"_em_platName").html(platName);
    var nickName = data.nickName;
    if(nickName != null && nickName != ""){
        $("#"+compId+"_a_nickname").show();
        $("#"+compId+"_a_nickname").attr("href",ContextPath+this.option.txt_customerIndexUrl).html(nickName);
        $("#"+compId+"_a_login").hide();
        $("#"+compId+"_a_register").hide();
        $("#"+compId+"_a_myorder").show();
        $("#"+compId+"_a_myorder").attr("href", ContextPath+this.option.txt_myorderUrl).html(this.option.lbl_myorderName);
        $("#"+compId+"_a_logout").show();
        $("#"+compId+"_a_logout").html(this.option.lbl_logoutName);
    }else{
        $("#"+compId+"_a_nickname").hide();
        $("#"+compId+"_a_logout").hide();
        $("#"+compId+"_a_myorder").hide();
        $("#"+compId+"_a_login").show();
        $("#"+compId+"_a_login").attr("href", ContextPath+this.option.txt_loginUrl).html(this.option.lbl_loginName);
        $("#"+compId+"_a_register").show();
        $("#"+compId+"_a_register").attr("href", ContextPath+this.option.txt_regUrl).html(this.option.lbl_regName);
    }
    $("#"+compId+"_a_index").attr("href", ContextPath+this.option.txt_indexUrl).html(this.option.lbl_indexName);
    $("#"+compId+"_a_favproduct").attr("href", ContextPath+this.option.txt_favProductUrl).html(this.option.lbl_favProductName);
    $("#"+compId+"_a_favstore").attr("href", ContextPath+this.option.txt_favStoreUrl).html(this.option.lbl_favStoreName);
    $("#"+compId+"_a_store").attr("href", ContextPath+this.option.txt_storeUrl).html(this.option.lbl_storeName);
    $("#"+compId+"_a_toapply").attr("href", ContextPath+this.option.txt_toApplyUrl).html(this.option.lbl_toApplyName);
    $("#"+compId+"_a_helpcenter").attr("href", ContextPath+this.option.txt_helpCenterUrl).html(this.option.lbl_helpCenterName);
}



//绑定事件
Comps.TopComp.prototype.bindSomeEvent=function(){
    var compId = this.compId;
    var that = this;

    $("#"+compId+"_a_logout").click(function(){
        $.getJSON(ContextPath+"/topComp/logout",{},function(data){
            if(data == "true"){
                window.location.href = ContextPath + "/" +that.option.txt_logoutUrl;
            }
        });
    });
}
//###########热销商品##########2
Comps.HotProduct = function(option){
    this.compId = option.compId;
    this.productUuid = option.param_inProductUuids;
    this.needAsyncInit = option.needAsyncInit;
    this.lableName = option.lableName;
    this.productUrl=option.productUrl;
}


Comps.HotProduct.prototype.init=function(){

    var that=this;

    if(this.needAsyncInit){


        $.ajax({
            type: "GET",
            url: ContextPath+"/hotProductComp/ajaxLoadData",
            data: {productUuid:that.productUuid, lableName:that.lableName},
            dataType: "json",
            async:true,
            success: function(data){
                data=eval("("+data+")");
                var listdata=  data.rows;


                $(".j_hotproduct_lable").html(data.lableName);

                var oneProductHtml=	$(".j_hotproduct_list").eq(0).clone();


                $(".j_hotproduct_all").html('');

                for(var i=0;i<listdata.length;i++){
                    oneProductHtml.find(".j_hotproduct_num").text(i+1);
                    oneProductHtml.find("img").attr("src",listdata[i].imgsrc);
                    oneProductHtml.find("div.j_hotproduct_title").text(listdata[i].name);


                    oneProductHtml.find("div.j_hotproduct_title").attr("uuid",listdata[i].productUuid);
                    oneProductHtml.find("div.j_hotproduct_price").text("￥"+(listdata[i].price/100).toFixed(2));
                    $(".j_hotproduct_all").append(oneProductHtml[0].outerHTML);


                }

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

Comps.HotProduct.prototype.bindSomeEvent=function(){

    var that=this;


    $(".j_hotproduct_list").each(function(){


        $(this).bind("click",function(){

            var uuid=$(this).find("div.j_hotproduct_title").attr("uuid");


            var url=Utils.getProductDetailUrl(ContextPath,that.productUrl,uuid);


            window.open(url);

        })

    })
}

///////////////////////购物车组件
/**
 * CartComp
 * Created by 金辉 on 2017/1/11.
 */
Comps.CartComp = function (config) {
    this.compId = config.compId;
    this.config = config;
}
Comps.CartComp.prototype.init = function () {
    var ts = this;
    var needAsyncInit = this.config.needAsyncInit;
    var topic = this.config.topic;
    var topicList = topic.split(",");
    //订阅交互组件
   /* $.each(topicList, function () {
        CompMessage.subscribe(this, function () {
            ts.loadData();
        });
    })*/
    if (needAsyncInit) {
        this.loadData();
    }
    //绑定事件
    this._bindEvent();
}
Comps.CartComp.prototype.loadData = function () {
    var ts = this;
    var compId = this.config.compId;
    //循环html清空
    $("#"+compId+"_product").html("");
    $.ajax({
        "url": ContextPath+"/cartComp/ajaxLoadData",
        data: {"compId": compId},
        "success": function (data) {
            data=eval("("+data+")");
            ts._render(data);
            Utils.ajaxLoadComplate(ts);
        }
    })
}
Comps.CartComp.prototype._render = function (data) {
    var compId = this.config.compId;
    //上方总价
    var totalPriceEle = $("#" + compId + "_totalPrice");
    //悬浮框上的总价
    var priceEle = $("#" + compId + "_price");
    var price =0;
    if (!$.isEmptyObject(data)) {
        //商品总价
        var cart=data.cartProduct;
        for(var x=0;x<cart.length;x++){
            price +=parseInt(cart[x].price)*parseInt(cart[x].num);
        }
    } else {
        //商品总价
        price = 0;
    }
    var totalprice = (price/100).toFixed(2);
    totalPriceEle.html(totalprice);
    priceEle.html(totalprice);
    //
    this._renderUpFrame(data.cartProduct);
    if (this.config.isEasyMode == 0) {
        $("#" + this.config.compId + "_drop").remove();
    } else {
        this._renderDropFrame(data);
    }
}
Comps.CartComp.prototype._renderUpFrame = function () {
    var compId = this.config.compId;
    var imgUrlEle = $("#" + compId + "_img");
    imgUrlEle.prop("src", this.config.imgUrl);
}
Comps.CartComp.prototype._renderDropFrame = function (data) {
    var ts = this;
    var compId = this.config.compId;
    var productEle = $("#" + compId + "_product");
    productEle.html("");
    var limodel = $("#"+compId+"_addModule li");
    if ($.isEmptyObject(data)) {
        return;
    }
    var carts = data.cartProduct;
    //悬浮框上的总数
    var numEle = $("#" + compId + "_num");
    //商品总数
    var number = 0;
    if(carts !=null && carts.length>0){
        for (var i = 0; i < carts.length; ++i) {
            var cart = carts[i];
            var newli = limodel.clone();
            var productId = newli.find(".j_productId");
            var attrId = newli.find(".j_attrId");
            var productName = newli.find(".j_productName");
            var productPrice = newli.find(".j_productPrice");
            var productNum = newli.find(".j_productNum");
            var productUrl = newli.find(".j_productUrl");
            var productImg = newli.find(".j_productImg");
            var goodsProduct=cart.goodsProduct;
            number +=cart.num;
            productId.html(cart.productId);
            productName.html(goodsProduct.name);
            productName.prop("href", Utils.getProductDetailUrl(ContextPath,ts.config.productUrl,goodsProduct.sku));
            attrId.html(goodsProduct.sku);
            productPrice.html((cart.price/100).toFixed(2));
            productNum.html(cart.num);
            productUrl.prop("href", Utils.getProductDetailUrl(ContextPath,ts.config.productUrl,goodsProduct.sku));
            productImg.prop("src", cart.imgurl);
            productImg.prop("data_sku",goodsProduct.sku);
            productEle.append(newli);
        }
    }
    numEle.html(number);
}
Comps.CartComp.prototype._deletePro = function (proEle) {
    var compId = this.config.compId;
    var productId = proEle.find(".j_productId").html().trim();
    var attrId = proEle.find(".j_attrId").html().trim();
    $.ajax({
        "url": ContextPath+"/cartComp/deletePro.json",
        "data": {"productId": productId, "attrId": attrId},
        success: function (data) {
            data=eval("("+data+")");
            //如果删除成功
            var totalPriceEle = $("#" + compId + "_totalPrice");
            var priceEle = $("#" + compId + "_price");
            var numEle = $("#" + compId + "_num");
            var totalNum = 0;
            if (!$.isEmptyObject(data)) {
                totalPriceEle.html((data.totPrice/100).toFixed(2));
                priceEle.html((data.totPrice/100).toFixed(2));
                totalNum=data.totNum;
            } else {
                totalPriceEle.html(0.00);
                priceEle.html(0.00);
            }
            numEle.html(totalNum);
            if($(".y_rtcart").length > 0){
                $(".y_rtcart .y_rcatsize").text(totalNum);
            }
            proEle.remove();
        }
    })
}
Comps.CartComp.prototype._bindEvent = function () {
    var ts = this;
    var compId = this.config.compId;
    $("#" + compId+"_cartUrl").click(function(){
        window.location.href = ContextPath+ts.config.cartShopUrl;
    })
    $("#" + compId).delegate(".j_deletePro", "click", function () {
        var proEle = $(this).parents(".j_productModel");
        ts._deletePro(proEle);
    });


    $("#"+compId).delegate(".j_cartUrl","click",function(){

        CompMessage.publish(ts.config.loginTopic,'',function(data){
            var cartList = [];
            $("#" + compId+"_product").find("li").each(function(index,obj){
                var cart ={};
                cart.sku =  $(obj).find(".j_attrId").html().trim();
                cartList.push(cart);
            });
            if(cartList !=null){
                var info = JSON.stringify(cartList);
                window.location.href =ContextPath+"/goods/placeOrder?"+$.param({cartInfo:info}, true);
            }
        });
    })
}

//###########推荐商品##########2
Comps.RecommendProduct = function(option){
    this.compId = option.compId;
    this.productUuid = option.param_inProductUuids;
    this.needAsyncInit = option.needAsyncInit;
    this.lableName = option.lableName;
    this.productUrl=option.productUrl;
}


Comps.RecommendProduct.prototype.init=function(){

    var that=this;

    if(this.needAsyncInit){


        $.ajax({
            type: "GET",
            url: ContextPath+"/recommendProductComp/ajaxLoadData",
            data: {productUuid:that.productUuid, lableName:that.lableName},
            dataType: "json",
            async:true,
            success: function(data){
                data=eval("("+data+")");
                var listdata=  data.rows;


                $(".j_recommendproduct_lable").html(data.lableName);

                var oneProductHtml=	$(".j_recommendproduct_list").eq(0).clone();
                $(".j_recommendproduct_all").html('');

                for(var i=0;i<listdata.length;i++){


                    oneProductHtml.find("img").attr("src",listdata[i].imgsrc);
                    oneProductHtml.find("div.j_recommendproduct_title").text(listdata[i].name);

                    oneProductHtml.find("div.j_recommendproduct_title").attr("uuid",listdata[i].sku);
                    oneProductHtml.find("div.j_recommendproduct_price").text("￥"+(listdata[i].price/100).toFixed(2));
                    $(".j_recommendproduct_all").append(oneProductHtml[0].outerHTML);


                }

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

Comps.RecommendProduct.prototype.bindSomeEvent=function(){

    var that=this;


    $(".j_recommendproduct_list").each(function(){


        $(this).bind("click",function(){

            var uuid=$(this).find("div.j_recommendproduct_title").attr("uuid");


            var url =Utils.getProductDetailUrl(ContextPath,that.productUrl,uuid);


            window.open(url);

        })

    })


}

//###########推荐商品##########

//////*************************快速入口组件****************
Comps.SideEnteranceComp = function(option){
    this.compId = option.compId;
    this.needAsyncInit = option.needAsyncInit;
    this.option = option;
    //监听页面宽度，右侧边栏的帐号、收藏、调查问卷的显示、隐藏
    this.rtfixshow = false,
        this.rtboxshow = false;
}

Comps.SideEnteranceComp.prototype.init = function(){
    var that = this;
    var compId = this.compId;
    //1.是否懒加载，如果是懒加载，则下面数据通过ajax请求，渲染html页面
    if(this.needAsyncInit){
        $.ajax({
            type: "GET",
            url: ContextPath+"/sideEnteranceComp/ajaxLoadData",
            data: {},
            dataType: "json",
            success: function(data){
                $("#loginName1").val(data.loginName);
                $("#pwd1").val(data.pwd);
                var hasVilidateCode = data.hasVilidateCode;
                $("#hasVilidateCode1").val(hasVilidateCode);
                var remember = data.remember;
                if(remember == "on"){
                    $("#remember").attr("checked","checked");
                }else{
                    $("#remember").removeAttr("checked");
                }
            }
        });
        if(this.option.loginIsShow == "0"){
            $("#"+compId+"_pr_userlogin").hide();
        }
        if(this.option.cartIsShow == "0"){
            $("#"+compId+"_w_cart").hide();
        }
        if(this.option.collectionIsShow == "0"){
            $("#"+compId+"_pr_mycollection").hide();
        }
        if(this.option.quesNaireIsShow == "0"){
            $("#"+compId+"_pr_quesnaire").hide();
        }else{
            $("#"+compId+"_pr_quesnaire").attr("href",this.option.quesNaireUrl);
        }
        if(this.option.qqIsShow == "0"){
            $("#"+compId+"_pr_qq").hide();
        }
        if(onlineIsShow == "0"){
            $("#baidusq").hide();
        }

        $("#"+compId+"_a_reg").attr("href",ContextPath+this.option.a_forgetPwdUrl).html(this.option.a_forgetPwdName);
        $("#"+compId+"_a_forgetpwd").attr("href",ContextPath+this.option.a_regUrl).html(this.option.a_regName);
        Utils.ajaxLoadComplate(that);
    }else{
        var loginIsShow = $("#"+compId+"_loginIsShow").val();
        var cartIsShow = $("#"+compId+"_cartIsShow").val();
        var collectionIsShow = $("#"+compId+"_collectionIsShow").val();
        var quesNaireIsShow = $("#"+compId+"_quesNaireIsShow").val();
        var qqIsShow = $("#"+compId+"_qqIsShow").val();
        var onlineIsShow = $("#"+compId+"_onlineIsShow").val();
        if(loginIsShow == "0"){
            $("#"+compId+"_pr_userlogin").hide();
        }
        if(cartIsShow == "0"){
            $("#"+compId+"_w_cart").hide();
        }
        if(collectionIsShow == "0"){
            $("#"+compId+"_pr_mycollection").hide();
        }
        if(quesNaireIsShow == "0"){
            $("#"+compId+"_pr_quesnaire").hide();
        }
        if(qqIsShow == "0"){
            $("#"+compId+"_pr_qq").hide();
        }
        if(onlineIsShow == "0"){
            $("#baidusq").hide();
        }

        Utils.ajaxLoadComplate(that);
    }

    var customerIndexUrl = this.option.customerIndexUrl;
    $.getJSON(ContextPath+"/sideEnteranceComp/isLogin",{},function(data){
        data=eval("("+data+")");
        if(data.flag == true){
            $("#"+compId+"_userlogin").show();
            $("#"+compId+"_nologin").remove();
            $(".y_ifixed").addClass("y_haslogoin").removeClass("y_nologin");
            $("#"+compId+"_userphoto").attr("src",data.profilePic);
            $("#"+compId+"_customer_index").attr("href",ContextPath + "/" +customerIndexUrl);
        }else{
            $("#"+compId+"_nologin").show();
            $("#"+compId+"_userlogin").remove();
        }
    });
    //绑定事件
    that.bindSomeEvent();
    //加载收藏商品
   that.loadFavoteProduct();

    that.rtcartshow();
    that.loadAjaxCart();
}


//绑定事件
Comps.SideEnteranceComp.prototype.bindSomeEvent=function(){
    var compId = this.compId;
    var that = this;

    $("#pwd1").blur(function(){
        //$("#pwd1").val(hex_md5($("#pwd1").val()));
    });

//	$(".y_userbtn").click(function() {
//		//$(".y_rtdlbox").removeClass("y_botshow").css({"display":"block","top":"34px","bottom":"auto"})
//	});

    $('#domainForm222').each(function() {
        var id = $(this).attr('id');  //获取表单的id
        $("#" + id).validate({   //验证表单
            errorElement: 'span',  //输入错误时的提示标签
            errorClass: 'help-block has-error',  //输入错误时的提示标签类名
            errorPlacement: function(error, element) {  //输入错误时的提示标签显示的位置
                if(element.parents(".input-group").length > 0){
                    element.parents(".input-group").after(error);
                }else if(element.parents(".y_validatainput").length > 0){
                    element.parents(".y_validatainput").after(error);
                }
                else if(element.parents("label").length > 0) {
                    element.parents("label").after(error);
                }else {
                    element.after(error);
                }
            },
            highlight: function(label) {   //输入错误时执行的事件
                $(label).closest('.form-group').removeClass('has-error has-success').addClass('has-error');
            },
            success: function(label) {   //输入正确时执行的事件
                label.addClass('valid').closest('.form-group').removeClass('has-error has-success').addClass('has-success');
            },
            onkeyup: function(element) {   //验证元素输入值时按钮松开执行的事件
                $(element).valid();
            },
            onfocusout: function(element) {   //验证元素失去焦点时进行验证
                $(element).valid();
            },
            submitHandler: function(form){
                that.login();
            }
        });
    });

    $("#btn_customer_login").click(function(){
        that.login();
    });

    //返回顶部js
    $(".y_ifixed .y_totop").click(function() {
        $("html,body").animate({
            scrollTop: '0px'
        }, 300);
    });


    $(window).resize(function() {
        if (!$(".y_rtfixbx").hasClass("y_showrt")) {
            that.rtcartshow();
        }
    });

    $(".y_rtfixbx").click(function(event) {
        event.stopPropagation();
    });

    //右侧导航登录后 点击购物车弹出、关闭购物车信息
    $(".y_ifixed ").on("click", ".y_rtcart", function() {
        var customerIndexUrl = that.option.customerIndexUrl;
        $.getJSON(ContextPath+"/sideEnteranceComp/isLogin",{},function(data){
            if(data.flag == true){
                $("#"+compId+"_userlogin").show();
                $("#"+compId+"_nologin").remove();
                $(".y_ifixed").addClass("y_haslogoin").removeClass("y_nologin");
                $("#"+compId+"_userphoto").attr("src",data.profilePic);
                $("#"+compId+"_customer_index").attr("href",ContextPath +"/"+ customerIndexUrl);
            }else{
                $("#"+compId+"_nologin").show();
                $("#"+compId+"_userlogin").remove();
            }
        });
        if (!$(".y_rtctbx").is(".y_rtmsshow")) {
            that.y_rtctlopen();
            $(this).addClass("y_xzcur");
            $(".y_rtlove").removeClass("y_xzcur");
            $(".y_rtctbx").addClass("y_rtmsshow");
            $(".y_rtlovebx").removeClass("y_rtmsshow");
            $(".y_rtfixbx").animate({
                width: "310px"
            }, 300);
            $('.add-popover,[rel=popover]').popover('hide');

        } else {
            that.y_rtctloclose();
            $('.add-popover,[rel=popover]').popover('hide');
        };
        that.loadAjaxCart();
        //加载收藏商品
        that.loadFavoteProduct();
    });
    //右侧导航登录后 点击我的收藏弹出、关闭收藏信息
    $(".y_rtfixbx").on("click", ".y_haslogoin .y_rtlove", function() {
        if (!$(".y_rtlovebx").is(".y_rtmsshow")) {
            that.y_rtctlopen();
            $(this).addClass("y_xzcur");
            $(".y_rtcart").removeClass("y_xzcur");
            $(".y_rtlovebx").addClass("y_rtmsshow");
            $(".y_rtctbx").removeClass("y_rtmsshow");
            $(".y_rtfixbx").animate({
                width: "270px"
            }, 300);
        } else {
            that.y_rtctloclose();
        };
    });
    //右侧购物车或收藏显示时，关闭按钮的事件
    $(".y_closertms").click(function() {
        that.y_rtctloclose();
    });

    //右侧导航点击显示登录弹出框
    $(".y_rtfixbx").on("click", ".y_nologin .y_userbtn", function() {
        $(".y_rtdlbox").removeClass("y_botshow").css({
            "display": "block",
            "top": "34px",
            "bottom": "auto"
        })
    });
    $(".y_nologin .y_userbtn,.y_nologin .y_rtcart,.y_nologin .y_rtlove").mouseleave(function() {
        $(".y_rtdlbox").hide();
        $(".y_rtdlbox").hover(function() {
            $(this).show();
        }, function() {
            $(this).hide();
        });
    });

    $(".y_rtfixbx").on("click", ".y_nologin .y_rtlove", function() {
        $(".y_rtdlbox").addClass("y_botshow").css({
            "display": "block",
            "top": "auto",
            "bottom": "70px"
        })
    });
    //点击关闭右侧登陆弹出框的事件
    $(".y_closedlbx").click(function() {
        $(this).parents(".y_rtdlbox").hide();
    });
    //右侧悬浮侧边栏鼠标移上时显示、隐藏部分模块
    $(".y_ifixed").hover(function() {
        if (that.rtfixshow == false) {
            $(".y_ifixed").removeClass("y_rtcthide");
        };
    }, function() {
        if (that.rtfixshow == false && that.rtboxshow == false) {
            $(".y_ifixed").addClass("y_rtcthide");
        };
    });

    //除首页外其他页面鼠标移上全部商品分类时展示分类树
    $('.y_produall').hover(function() {
        $(this).find('.y_prudaltc').show();
    }, function() {
        $(this).find('.y_prudaltc').hide();
    });

    $(".pop-qq").on('click', function() {
        that.rtfixshow = true;
    })

    $('.add-popover,[rel=popover]').on('hidden.bs.popover', function() {
        that.rtfixshow = false;
    })

    // Bootstrap tooltip
    if ($.fn.tooltip) {
        $('.add-tooltip,[rel=tooltip]').tooltip();
    };

    // Bootstrap popover
    if ($.fn.popover) {
        $('.add-popover,[rel=popover]').popover({
            trigger: "click",
            html:true,
            content:this.option.qqListStr
        });
    };


    //根据.progress-animate的data-width属性动态设置元素的宽度，如果页面引入了jquery.appear.js则滚动到元素所在位置才设置否则默认设置
    if ($.fn.appear) {
        $('.progress-animate').appear();
        $('.progress-animate').on('appear', function() {
            var $this = $(this),
                progressVal = $(this).data('width'),
                progressText = $this.find('.progress-text');
            $this.css({
                'width': progressVal + '%'
            }, 400);
            progressText.fadeIn(500);
        });

    } else {
        $('.progress-animate').each(function() {
            var $this = $(this),
                progressVal = $(this).data('width'),
                progressText = $this.find('.progress-text');
            $this.css({
                'width': progressVal + '%'
            }, 400);
            progressText.fadeIn(500);
        });
    }

    //收藏商品成功弹出框内的商品滚动
    if ($(".y_collpdowl").length > 0) {
        $('.y_collpdowl.owl-carousel').owlCarousel({
            items: 3,
            itemsDesktop: false,
            itemsDesktopSmall: false,
            itemsTablet: false,
            pagination: false,
            navigationText: false,
            scrollPerPage: true,
            autoPlay: 5000,
            slideSpeed: 800,
            navigation: true
        });
    };


    $("#shoppingCart").on("click", "[id^='addstock_']", function(){
        var operId = $(this).attr("id") ;

        var stockStr = operId.replace("addstock_","stock_") ;
        var changeNum = $("#"+stockStr).val() ;
        $("#"+stockStr).val(parseInt(changeNum)+1) ;
        //调用异步更新数量，计算价格
        that.ajaxChangeNum(operId ,parseInt(changeNum)+1) ;
        that.ajaxChangeTotPrice();
    })

    $("#shoppingCart").on("click", "[id^='reducestock_']", function(){
        var operId = $(this).attr("id") ;
        var stockStr = operId.replace("reducestock_","stock_") ;
        var changeNum = $("#"+stockStr).val() ;
        if(parseInt(changeNum)==1) {
            return ;
        }
        $("#"+stockStr).val(changeNum-1);
        var priceStr=$(this).parent().next().html();
        var price=priceStr.substring(2);
        //调用异步更新数量，计算价格
        that.ajaxChangeNum(operId ,changeNum-1) ;
        that.ajaxChangeTotPrice();
    })

    //弹出确认是否删除框
    $("#shoppingCart").on("click", "[id^='remove_']", function(){
        var operId = $(this).attr("id") ;

        //调用异步更新数量，计算价格
        that.ajaxRemove(operId,this) ;
    })

    //购物车商品全选
    $("#shoppingCart").on("click", "[id='CheckAll']", function(){
        var flag = $(this).prop("checked");
        if(flag) {
            $(':checkbox').prop('checked',true);
        }else{
            $(':checkbox').prop('checked',false);
        }
        that.ajaxChangeChoose("allRecords" ,flag) ;
    })

    $("#shoppingCart").on("blur", "[id^='stock_']", function(){
        var operId = $(this).attr("id") ;
        var buyNum = $(this).val().replace(/[^\d]/g, '');
        if(buyNum == "") {
            buyNum = 1 ;
        }
        $(this).val(buyNum);
        that.ajaxChangeNum(operId ,buyNum) ;
    })

    //购物车商户复选框选中全选
    $("#shoppingCart").on("click", "[id^='store_']", function(){
        var flag = $(this).prop("checked");
        var operId = $(this).attr("id") ;
        var operationId = operId.replace("store_" ,"product_") ;
        if(flag) {
            $("[id^='"+operationId+"']").prop('checked',true);
        }	else{
            $("[id^='"+operationId+"']").prop('checked',false);
        }

        var productIds = "" ;
        $("[id^='"+operationId+"']").each(function(index){
            var productCheckId = $(this).attr("id") ;
            productIds = productCheckId+";"+productIds ;
        })

        that.ajaxChangeChoose(productIds ,flag) ;
    })

    //购物车商品选中框操作
    $("#shoppingCart").on("click", "[id^='product_']", function(){

        var operId = $(this).attr("id") ;
        var flag = $(this).prop("checked");

        that.ajaxChangeChoose(operId ,flag) ;
    })

    //购物车去结算
    //1.判断是否登录，如果未登录，则弹出登录框
    //2.如果已登录，则进入下一步
    $("#shoppingCart").on("click", "[id^='goBlance']", function(){
        var loginUrl = ContextPath+"/popLoginComp/isLogin"
        $.get(loginUrl,{ranNum:Math.random()} ,function(data){
            if("0"==data) {
                //弹出登录框
                is_right_cart_buy="true";
                $("#modal-login-form").modal();
            }else{
                var size= $(".y_rtpbox").find("li").size();
                var cartList = [];
                $(".y_rtpbox").find("li").each(function(){
                    var $isChecked=$(this).find(".y_rtliipt").find("input");
                    if($isChecked.is(":checked")){
                        var sku=$isChecked.val().split("_");
                        var cart ={};
                        cart.sku =sku[2] ;
                        cartList.push(cart);
                    }
                });
                var info = JSON.stringify(cartList);
                window.location.href =ContextPath+"/goods/placeOrder?"+$.param({cartInfo:info}, true);
            }
        })
    })

    $(document).on("click", "[id^='goBlance']", function(){
        var loginUrl = ContextPath+"/popLoginComp/isLogin"
        $.get(loginUrl,{ranNum:Math.random()} ,function(data){
            if("0"==data) {
                //弹出登录框
                $("#modal-login-form").modal();
            }else{
                var size= $(".y_rtpbox").find("li").size();
                var cartList = [];
                $(".y_rtpbox").find("li").each(function(){
                    var $isChecked=$(this).find(".y_rtliipt").find("input");
                    if($isChecked.is(":checked")){
                        var sku=$isChecked.val().split("_");
                        var cart ={};
                        cart.sku =sku[2] ;
                        cartList.push(cart);
                    }
                });
                var info = JSON.stringify(cartList);
                window.location.href =ContextPath+"/goods/placeOrder?"+$.param({cartInfo:info}, true);
            }
        })
    })

    //加载 购物车
    $("#w_cart").click(function(){
        that.loadAjaxCart();
    });

}


//加载会员收藏的商品
Comps.SideEnteranceComp.prototype.loadFavoteProduct = function(){
    var url = ContextPath+"/sideEnteranceComp/platRightFavoriteProduct";
    $.post(url,{ranNum:Math.random()},
        function(data){
            $(".y_rtlovebx").html(data);
        }
    );
}

Comps.SideEnteranceComp.prototype.login = function(){
    var loginName = $("#loginName1").val();
    if(loginName == null || loginName == ""){
        $("#error").css("display","block");
        $(".alert").show();
        $(".alert").html("请输入手机号/用户名!");
        return false;
    }
    var pwd = $("#pwd1").val();
    if(pwd == null || pwd == ""){
        $("#error").css("display","block");
        $(".alert").show();
        $(".alert").html("请输入密码!");
        return false;
    }
    var hasVilidateCode =  $("#hasVilidateCode1").val()+"";
    var validateCode =  $("#validateCode").val();
    if(hasVilidateCode == "true"){
        if(validateCode == null || validateCode == ""){
            $("#error").css("display","block");
            $(".alert").show();
            $(".alert").html("请输入验证码!");
            return false;
        }
    }
    var remember = "";
    $("input[name='remember']:checked").each(function(){
        remember = $(this).val();
    });
    $.get(ContextPath+"/sideEnteranceComp/quickRightLogin",
        {
            "loginName":loginName,
            "pwd":pwd,
            "hasVilidateCode":hasVilidateCode,
            "validateCode":validateCode,
            "remember" : remember,
            "type":"customer",
            ranNum : Math.random()
        },
        function(data) {
            if(data.code == "2"){
                $("#error").css("display","block");
                $(".alert").show();
                $(".alert").html("验证码为空!");
            }else if(data.code == "1"){
                $("#error").css("display","block");
                $(".alert").show();
                $(".alert").html("验证码错误!");
            }else if(data.code == "4"){
                $("#error").css("display","block");
                $(".alert").show();
                $(".alert").html("用户名或者密码错误!");
            }else if(data.code == "5"){
                $("#error").css("display","block");
                $(".alert").show();
                $(".alert").html("用户名或者密码错误!");
            }else if(data.code == "3"){
                $("#error").css("display","block");
                $(".alert").show();
                $(".alert").html("该用户已被锁住!");
            }else if(data.code == "0"){
                $(".alert").hide();
                $(".alert").html(" ");
                $("#modal-login-form").modal("hide");
                window.location.reload();
            }
        });
}


Comps.SideEnteranceComp.prototype.login1 = function(){
    var loginName = $("#loginName1").val();
    if(loginName == null || loginName == ""){
        $("#error").css("display","block");
        $(".alert").show();
        $(".alert").html("请输入手机号/用户名!");
        return false;
    }
    var pwd = $("#pwd1").val();
    if(pwd == null || pwd == ""){
        $("#error").css("display","block");
        $(".alert").show();
        $(".alert").html("请输入密码!");
        return false;
    }
    var hasVilidateCode =  $("#hasVilidateCode1").val()+"";
    var validateCode =  $("#validateCode1").val();
    if(hasVilidateCode == "true"){
        if(validateCode == null || validateCode == ""){
            $("#error").css("display","block");
            $(".alert").show();
            $(".alert").html("请输入验证码!");
            return false;
        }
    }
    var remember = "";
    $("input[name='remember']:checked").each(function(){
        remember = $(this).val();
    });
    $.get(ContextPath+"/sideEnteranceComp/quickRightLogin",
        {
            "loginName":loginName,
            "pwd":pwd,
            "hasVilidateCode":hasVilidateCode,
            "validateCode":validateCode,
            "remember" : remember,
            "type":"customer",
            ranNum : Math.random()
        },
        function(data) {
            if(data == "codeNull"){
                $("#error").css("display","block");
                $(".alert").show();
                $(".alert").html("验证码为空!");
            }else if(data == "codeError"){
                $("#error").css("display","block");
                $(".alert").show();
                $(".alert").html("验证码错误!");
            }else if(data == "loginError"){
                $("#error").css("display","block");
                $(".alert").show();
                $(".alert").html("用户名或者密码错误!");
            }else if(data == "hasCode"){
                $("#error").css("display","block");
                $(".alert").show();
                $(".alert").html("用户名或者密码错误!");
                $("#hasVilidateCode").val("true");
            }else if(data.code == "0"){
                $(".alert").hide();
                $(".alert").html(" ");
                $("#modal-login-form").modal("hide");
                window.location.reload();
            }
        });
}


/* 刷新验证码 */
Comps.SideEnteranceComp.prototype.flushValidateCode = function(){
    var validateImgObject = document.getElementById("validateImg");
    validateImgObject.src = ContextPath+"/getCustomerLoginCode?time=" + new Date();
}


Comps.SideEnteranceComp.prototype.rtcartshow = function() {
    var boxwidth = $(".container").width(),
        winwidth = $(window).width();
    if ((winwidth / 2 - boxwidth / 2) < 1000) {
        if (!$(".y_ifixed").hasClass("y_rtcthide")) {
            $(".y_ifixed").addClass("y_rtcthide");
        };
        this.rtfixshow = false;
    } else {
        $(".y_ifixed").removeClass("y_rtcthide");
        this.rtfixshow = true;
    };
};

//右侧购物车、收藏信息的显示事件
Comps.SideEnteranceComp.prototype.y_rtctlopen = function() {
    var that = this;
    $(".y_rtfixbx").addClass("y_showrt");
    $(".y_ifixed").removeClass("y_rtcthide");
    //右侧购物车或收藏显示时，点击右侧区域以外的地方可以关闭
    $(document).one("click", function() {
        that.y_rtctloclose();
    });
    this.rtboxshow = true;
}


//右侧购物车、收藏信息的关闭事件
Comps.SideEnteranceComp.prototype.y_rtctloclose = function() {
    var that = this;
    $(".y_rtcart,.y_rtlove").removeClass("y_xzcur");
    $(".y_rtfixbx").removeClass("y_showrt");
    $(".y_rtctbx,.y_rtlovebx").removeClass("y_rtmsshow");
    $(".y_rtfixbx").animate({
        width: "35px"
    }, 300);
    that.rtcartshow();
    this.rtboxshow = false;
};


//页面修改购买数量
Comps.SideEnteranceComp.prototype.ajaxChangeNum = function(operId ,changeNum) {
    var arr=operId.split("_");
    var sku=arr[2];
    var that = this;
    var changeNumUrl = ContextPath+"/goods/updateCartInfoNum" ;
    $.post(changeNumUrl,{"sku":sku,"num":changeNum,ranNum:Math.random()} ,function(data){
        if(data.code=="0"){
            $("#shoppingCart").addClass("y_rtmsshow");
            if($(".yw_totalPrice").length>0){
                var productEle = $(".dropdown-cart-product-list");
                var limodel = $(".yw_addModule li");
                productEle.html("");
                var key="memberCartList";
                var carts=data.data;
                var price=0;
                var totalNum=0;
                if(carts !=null && carts.length>0){
                    for (var i = 0; i < carts.length; i++) {
                        var cart = carts[i];
                        var newli = limodel.clone();
                        var productId = newli.find(".j_productId");
                        var attrId = newli.find(".j_attrId");
                        var productName = newli.find(".j_productName");
                        var productPrice = newli.find(".j_productPrice");
                        var productNum = newli.find(".j_productNum");
                        var productUrl = newli.find(".j_productUrl");
                        var productImg = newli.find(".j_productImg");
                        var goodsProduct=cart.goodsProduct;
                        price +=parseInt(carts[i].price)*parseInt(carts[i].num);
                        totalNum +=parseInt(cart.num);
                        productId.html(cart.productId);
                        productName.html(goodsProduct.name);
                        productName.prop("href","/front/detail&sku="+goodsProduct.sku);
                        attrId.html(goodsProduct.sku);
                        productPrice.html((cart.price/100).toFixed(2));
                        productNum.html(cart.num);
                        productUrl.prop("href","/front/detail&sku="+goodsProduct.sku);
                        productImg.prop("src", cart.imgurl);
                        productImg.prop("data_sku",goodsProduct.sku);
                        productEle.append(newli);
                    }
                }
                $(".yw_totalPrice").html((price/100).toFixed(2));
                $(".yw_bottomTotalPrice").html((price/100).toFixed(2));
                $(".yw_totalNum").html(totalNum);
                $("#cartsTotal").html(totalNum);
            }
        }else{
            $("#cartsTotal").html("0");
        }

    })
}

Comps.SideEnteranceComp.prototype.ajaxChangeTotPrice=function(){
    var size= $(".y_rtpbox").find("li").size();
    var totprice=0.00;
    var checkSize=0;
    $(".y_rtpbox").find("li").each(function(){
        if($(this).find(".y_rtliipt").find("input").is(":checked")){
            checkSize +=1;
            var num=$(this).find(".y_rtpsize").find("input").val();
            var price=$(this).find(".y_rtpprcetop").html().substring(2);
            totprice+=num*price;
        }
    });
    $(".y_rtjsbx").find("strong").html("已选"+size+"件")
    $(".y_jsprce").html("￥"+totprice);
}

//删除购物车中的商品
Comps.SideEnteranceComp.prototype.ajaxRemove = function(operId,obj) {
    var that = this;
    var changeNumUrl=ContextPath+"/cartComp/deletePro.json";
    var arr=operId.split("_");
    var sku=arr[2];
    $.post(changeNumUrl,{"attrId": sku,ranNum:Math.random()} ,function(data){
        data=eval("("+data+")");
        var size=$(obj).parents("ul").find("li").size();
        var allSize=$(obj).parents(".y_rtbigbox").find(".y_rtptit").size();
        if(allSize ==1){
            $("#shoppingCart").html('<div class="y_slidecartno">'+
                '<span class="y_carticon"><i class="fa fa-shopping-cart"></i></span>'+
                '<span class="y_cartnoms">'+
                '购物车内暂时没有商品，快去挑选自己喜欢的商品吧~'+
                '</span>'+
                '</div>');
        }else{
            if(size ==1){
                $(obj).parents("ul").prev().remove();
                $(obj).parents("ul").remove();
            }else{
                $(obj).parents("li").remove();
            }
            var totprice=0.00;
            var checkSize=0;
            $(".y_rtpbox").find("li").each(function(){
                if($(this).find(".y_rtliipt").find("input").is(":checked")){
                    checkSize +=1;
                    var num=$(this).find(".y_rtpsize").find("input").val();
                    var price=$(this).find(".y_rtpprcetop").html().substring(2);
                    totprice+=num*price;
                }
            });
            $(".y_rtjsbx").find("strong").html("已选"+checkSize+"件")
            $(".y_jsprce").html("￥"+totprice);
        }
        if($(".yw_totalPrice").length>0){
            var productEle = $(".dropdown-cart-product-list");
            var limodel = $(".yw_addModule li");
            productEle.html("");
            var carts=data.cartProduct;
            var price=0;
            var totalNum=0;
            if(carts !=null && carts.length>0){
                for (var i = 0; i < carts.length; i++) {
                    var cart = carts[i];
                    var newli = limodel.clone();
                    var productId = newli.find(".j_productId");
                    var attrId = newli.find(".j_attrId");
                    var productName = newli.find(".j_productName");
                    var productPrice = newli.find(".j_productPrice");
                    var productNum = newli.find(".j_productNum");
                    var productUrl = newli.find(".j_productUrl");
                    var productImg = newli.find(".j_productImg");
                    var goodsProduct=cart.goodsProduct;
                    price +=parseInt(carts[i].price)*parseInt(carts[i].num);
                    totalNum +=parseInt(cart.num);
                    productId.html(cart.productId);
                    productName.html(goodsProduct.name);
                    productName.prop("href","/front/detail&sku="+goodsProduct.sku);
                    attrId.html(goodsProduct.sku);
                    productPrice.html((cart.price/100).toFixed(2));
                    productNum.html(cart.num);
                    productUrl.prop("href","/front/detail&sku="+goodsProduct.sku);
                    productImg.prop("src", cart.imgurl);
                    productImg.prop("data_sku",goodsProduct.sku);
                    productEle.append(newli);
                }
            }
            $(".yw_totalPrice").html((price/100).toFixed(2));
            $(".yw_bottomTotalPrice").html((price/100).toFixed(2));
            $(".yw_totalNum").html(totalNum);
            $("#cartsTotal").html(totalNum);
        }
    })
}

//复选框是否选中
Comps.SideEnteranceComp.prototype.ajaxChangeChoose = function(operId ,flag) {
    var size= $(".y_rtpbox").find("li").size();
    var totprice=0.00;
    var checkSize=0;
    $(".y_rtpbox").find("li").each(function(){
        if($(this).find(".y_rtliipt").find("input").is(":checked")){
            checkSize +=1;
            var num=$(this).find(".y_rtpsize").find("input").val();
            var price=$(this).find(".y_rtpprcetop").html().substring(2);
            totprice+=num*price;
        }
    });
    $(".y_rtjsbx").find("strong").html("已选"+checkSize+"件")
    $(".y_jsprce").html("￥"+totprice);
}

//加载右侧购物车
Comps.SideEnteranceComp.prototype.loadAjaxCart = function(){
    var url = ContextPath+"/goods/cartForComp" ;
    $.post(url,function(data){
        data=eval("("+data+")");
        if(!$.isEmptyObject(data)){
            console.log(data);
            var bigHtml='<div class="y_rtcttit"><label><input name="CheckAll" id="CheckAll" type="checkbox" checked="checked">全选</label><a href="/goods/cart">去购物车查看</a></div>';
            var productHtml='<div class="y_ctlistbox"><input name="isAnonymous1" id="isAnonymous1" type="hidden" value="0">'+
                '<div class="y_h28">&nbsp;</div><div class="y_rtpbox">';
            var cartsTotal=0;
            var cartPrice=0.00;
            var productSize=0;
            var xx='';
            for(var key in data){
                var list=data[key];
                var $ul='';
                if(list !=null && list.length>0){
                    var storeModel=list[0].storeMain;
                    if(storeModel!=null) {
                        var store = '<p class="y_rtptit">' +
                            '<input id="store_' + key + '" type="checkbox" checked="checked" value="' + key + '">' +
                            '<span>' + storeModel.storeName + '</span>' +
                            '</p>';
                    }
                    var goodsHtml='';
                    for(var i=0;i<list.length;i++){
                        productSize=productSize+1;
                        cartsTotal +=list[i].num;
                        cartPrice+=list[i].num*(list[i].price/100).toFixed(2);
                        //循环商品列表
                        var goodsProduct=list[i].goodsProduct;
                        //加入购物车时所选的规格
                        var spec=goodsProduct.spec;
                        /* spec=JSON.parse(spec);*/
                        goodsHtml +='<li style="overflow: hidden;width: 100%;padding-bottom:5px;position: relative">'+
                            '<label class="y_rtliipt"  style="float: left;margin: 20px 8px 0 0;"><input id="product_'+key+'_'+goodsProduct.id+'_'+goodsProduct.sku+'" type="checkbox" checked="checked" value="product_'+goodsProduct.id+'_'+goodsProduct.sku+'"></label>'+
                            '<div class="y_rtlipic"  style="width:50px;height: 50px;float: left;margin: 5px 0;"><a href="javascript:void(0);"><img style="width:50px;height: 50px;" src="'+list[i].imgurl+'"></a></div>'+
                            '<p class="y_rtlsxb">'+
                            '<br>'+
                            '</p>'+
                            ' <span class="y_rtpsize" style="float: left;width: 60px;text-align: center;color: #999;">'+
                            ' <a id="reducestock_'+goodsProduct.id+'_'+goodsProduct.sku+'" href="javascript:void(0);"><i class="fa fa-minus-circle"></i></a>'+
                            '  <input disabled="" style="width:20px;border:none;text-align:center" id="stock_'+goodsProduct.id+'_'+goodsProduct.sku+'" type="text" value="'+list[i].num+'">'+
                            '<a id="addstock_'+goodsProduct.id+'_'+goodsProduct.sku+'"  href="javascript:void(0);"><i class="fa fa-plus-circle"></i></a>'+
                            ' </span>'+
                            '<span class="y_rtpprcetop" style="float: left;width: 58px;text-align: right;">'+
                            ' ￥'+(list[i].price/100).toFixed(2)+
                            '</span>'+
                            '<a class="y_delct" style="margin-left:15px " id="remove_'+goodsProduct.id+'_'+goodsProduct.sku+'" href="javascript:loadTopCart();"><i class="fa fa-times-circle"></i></a>'+
                            '</li>';
                    }
                    var $ul='<ul class="y_rtulbox">'+goodsHtml+'</ul>';
                    console.log($ul);
                    xx +='<div class="y_rtbigbox">'+store+$ul+'</div>';
                    console.log(xx);
                }
            }
            console.log(xx);
            var end='<div class="y_rtjsbx">'+
                '<p class="clearfix"><strong>已选'+productSize+'件</strong><span class="y_jsprce">￥'+cartPrice+'</span></p>'+
                '<a class="btn btn-custom" id="goBlance" href="javascript:void(0);">结 算</a>'+
                '</div>';
            $("#shoppingCart").html(bigHtml+productHtml+xx+"</div></div>"+end);
            $("#cartsTotal").html(cartsTotal);
        }else {
            $("#shoppingCart").html('<div class="y_slidecartno">'+
                '<span class="y_carticon"><i class="fa fa-shopping-cart"></i></span>'+
                '<span class="y_cartnoms">'+
                '购物车内暂时没有商品，快去挑选自己喜欢的商品吧~'+
                '</span>'+
                '</div>');
        }
    });
}




//screen
/**
 * Created by 金辉 on 2017/1/11.
 */
Comps.Screen = function (config) {
    this.compId = config.compId;
    this.config = config;
}
Comps.Screen.prototype.init = function () {
    var ts = this;
    var needAsyncInit = this.config.needAsyncInit;
    if(needAsyncInit){
        var attrValue = ts._getUrlParam("attrValue");
        var defaultData = {
            "compId":ts.config.compId,
            "keyword":ts._getUrlParam("keyword"),
            "categoryUuid":ts._getUrlParam("categoryUuid"),
            "sortType":ts._getUrlParam("sortType"),
            "sortBy":ts._getUrlParam("sortBy"),
            "attrValue":ts._getUrlParam("attrValue"),
            "nowPage":ts._getUrlParam("nowPage"),
            "attrValue":attrValue,
            "pageShow":this.config.pageShow
        }
        this.ajaxLoadData(defaultData);
    }else{
        Utils.ajaxLoadComplate(this);
    }
    this._bindEvent();
}
Comps.Screen.prototype.ajaxLoadData = function (defaultData) {
    var ts = this;
    var compId = ts.config.compId;
    var compEle = $("#" + compId);
    var attrValue = defaultData.attrValue;
    var categoryUuid = ts._getUrlParam("categoryUuid");
    if(!$.isEmptyObject(categoryUuid)){
        $.ajax({
            "url":ContextPath+"/screenComp/getCategoryName.json",
            "data":{"categoryUuid":categoryUuid},
            "dataType":"text",
            "async":false,
            "success": function (data) {
                if($.isEmptyObject(attrValue)){
                    attrValue="分类_"+data;
                }else{
                    attrValue="分类_"+data+"@"+attrValue;
                }
            }
        })
    }
    $.ajax({
        "url": ContextPath+"/screenComp/ajaxLoadData.json",
        "data": defaultData,
        "success": function (data) {
            if (!$.isEmptyObject(attrValue)) {
                ts._renderChooseAttr(compEle, attrValue, compId);
            }
            //渲染筛选模块
            ts._renderScreenMoudle(compEle, compId, data, attrValue);
            Utils.ajaxLoadComplate(ts);
        }
    })
}
Comps.Screen.prototype._renderScreenMoudle = function (compEle, compId, screenModel, attrValue) {
    var component = $("#" + compId + "");
    var brandList = screenModel.brands;
    var priceList = screenModel.prices;
    var attributeList = screenModel.attributeList;
    var categoryList = screenModel.cateList;
    if (!this._isHaveQuery("attributeBrand", attrValue) && brandList != null && brandList.length > 0) {
        //渲染品牌
        this._renderBrandLoop(compEle, compId, brandList);
    } else {//如果已经查询了该属性 就不显示该删选条件
        var brandModule = $("#" + compId + "_brandModule");
        brandModule.remove();
    }
    if (!this._isHaveQuery("attributePrice", attrValue) && priceList != null && priceList.length > 0) {
        //渲染价格
        this._renderPrice(compEle, compId, priceList);
    } else {//如果已经查询了该属性 就不显示该删选条件
        var priceModule = $("#" + compId + "_priceModule");
        priceModule.remove();
    }
    if (!this._isHaveQuery("categoryUuid", attrValue) && categoryList != null && categoryList.length > 1) {
        //渲染分类
        this._renderCategory(compEle, categoryList, compId);
    } else {//如果已经查询了该属性 就不显示该删选条件
        var categoryModule = $("#" + compId + "_categoryModule");
        categoryModule.remove();
    }
    //渲染其他属性
    this._loopAddOtherEle(compEle, attributeList, attrValue);
    if($.isEmptyObject(screenModel)){
        component.find("box").remove();
    }
}
Comps.Screen.prototype._isHaveQuery = function (param, attrValue) {
    if (attrValue != null) {
        if(param=="categoryUuid"){
            var uri = window.location.href;
            var reg = new RegExp("(^|&)categoryUuid=([^&]*)");
            var r = uri.match(reg);
            if(!$.isEmptyObject(r)){
                return true;
            }
        }else {
            var attrs = attrValue.split("@");
            if (attrs != null) {
                for (var i = 0; i < attrs.length; ++i) {
                    var attr = attrs[i];
                    var keyAndValue = attr.split("_");
                    if (keyAndValue != null) {
                        if (keyAndValue[0] == param) {
                            return true;
                        }
                    }
                }
            }
        }
    }
    return false;
}
Comps.Screen.prototype._renderBrandLoop = function (compEle, compId, brandList) {
    var brandLoopEle = $("#" + compId + "_brandLoop");
    var brandEle = brandLoopEle.find(".j_brand").first();
    if(typeof(brandList)!=brandList)
    for (var i = 0; i < brandList.length; ++i) {//渲染默认全部品牌Element
        var brandModel = brandList[i];
        var pinyin = brandModel.brandEnName;
        var firstChar = pinyin.charAt(0);
        firstChar = firstChar.toLowerCase();
        var newEle = brandEle.clone();
        //渲染品牌
        this._renderBrandInfo(newEle, brandModel, firstChar);
        brandLoopEle.append(newEle);
    }
    //渲染查看更多时全部的品牌
    brandEle.remove();
}
Comps.Screen.prototype._renderBrandInfo = function (newEle, brandModel, firstChar) {
    var compId = this.config.compId;
    var brandUrlEle = newEle.find(".j_brandUrl").first();
    var brandImgEle = newEle.find(".j_brandImgUrl").first();
    var brandNameEle = newEle.find(".j_brandName").first();
//            brandUrlEle.attr("href",ContextPath+"/webpage/run?pageUuid=123&attrValue=attributeBrand_"+brandModel.getBrandName());
//            brandUrlEle.attr("");
    brandUrlEle.attr("onclick", "doSearch_" + compId + "(this,'attributeBrand','" + brandModel.brandName + "')");
    brandUrlEle.attr("title", brandModel.brandName);
    newEle.attr("data-initial", firstChar);
    brandImgEle.attr("src", brandModel.imageUrl1);
    brandNameEle.html(brandModel.brandName);
}
Comps.Screen.prototype._renderCategory = function (compEle, categoryList, compId) {
    var loopEle = compEle.find(".j_cateLoop").first();
    var moduleEle = loopEle.find(".j_cateModule").first();
    if(typeof(categoryList)!=undefined && !$.isEmptyObject(categoryList)){
        for (var i = 0; i < categoryList.length; ++i) {
            var categoryModel = categoryList[i];
            var newEle = moduleEle.clone();
            var valueEle = newEle.find(".j_value").first();
            var categoryUuidEle = newEle.find(".j_categoryUuid").first();
            categoryUuidEle.val(categoryModel.uuid);
            valueEle.html(categoryModel.categoryName);
            valueEle.attr("onclick", "doSearch_" + compId + "(this,'categoryUuid','" + categoryModel.uuid + "')");
            loopEle.append(newEle);
        }
    }
    moduleEle.remove();
}
Comps.Screen.prototype._renderPrice = function (compEle, compId, priceList) {
    var loopEle = $("#" + compId + "_priceLoop");
    var moduleEle = loopEle.find(".j_priceModule").first();
    for (var i = 0; i < priceList.length; ++i) {
        var str = priceList[i];
        var newEle = moduleEle.clone();
        var valueEle = newEle.find(".j_value").first();
        if (i == 0) {
            str = "0-" + str;
        } else if (i == priceList.length - 1) {
            str += "以上";
        }
        valueEle.attr("onclick", "doSearch_" + compId + "(this,'attributePrice','" + str + "')");
        valueEle.html(str);
        loopEle.append(newEle);
    }
    //删除模板
    moduleEle.remove();
}
Comps.Screen.prototype._loopAddOtherEle = function (compEle, attributeList, attrValue) {
    var otherEle = compEle.find(".j_other").first();
    if(typeof(attributeList)!=undefined && !$.isEmptyObject(attributeList)) {
        for (var i = 0; i < attributeList.length; ++i) {
            var attributeModel = attributeList[i];
            if (this._isHaveQuery(attributeModel.attributeName, attrValue)) {
                continue;
            }
            var newEle = otherEle.clone();
            var attrNameEle = newEle.find(".j_attrName").first();
            attrNameEle.html(attributeModel.attributeName + "：");
            this._renderOtherAttr(newEle, attributeModel.attributeName, attributeModel.selectValue, attributeModel.unit);
            compEle.append(newEle);
        }
    }
    otherEle.remove();
}
Comps.Screen.prototype._renderOtherAttr = function (otherEle, attrName, attributeList, unit) {
    var compId = this.config.compId;
    var loopEle = otherEle.find(".j_attrLoop").first();
    var moduleEle = otherEle.find(".j_attrModule").first();
    for (var i = 0; i < attributeList.length; ++i) {
        var str = attributeList[i];
        var newEle = moduleEle.clone();
        var valueEle = newEle.find(".j_value").first();
        valueEle.attr("onclick", "doSearch_" + compId + "(this,'" + attrName + "','" + str + "')");
        valueEle.html(str + unit);
        loopEle.append(newEle);
    }
    //删除模板
    moduleEle.remove();
}
Comps.Screen.prototype._renderChooseAttr = function (compEle, attrValue, compId) {
    var chooseAttrEle = $("#" + compId + "_chooseAttributesDIV");
    var loopEle = $("#" + compId + "_chooseAttributes");
    var attrEle = loopEle.find(".j_attribute").first();
    var attrArray = attrValue.split("@");
    for (var i = 0; i < attrArray.length; ++i) {
        var attr = attrArray[i];
        var newEle = attrEle.clone();
        var nameEle = newEle.find(".j_attrName").first();
        var valueEle = newEle.find(".j_attrValue").first();
        var nameAndValue = attr.split("_");
        newEle.attr("data-name", nameAndValue[0]);
        if ("attributeBrand" == nameAndValue[0]) {
            nameEle.html("品牌");
        } else if ("attributePrice" == nameAndValue[0]) {
            nameEle.html("价格");
        } else {
            nameEle.html(nameAndValue[0]);
        }
        valueEle.html(nameAndValue[1]);
        loopEle.append(newEle);
    }
    attrEle.remove();
    chooseAttrEle.attr("style", "display:block");
}
Comps.Screen.prototype._getUrlParam = function (name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg); //匹配目标参数
    if (r != null) return decodeURIComponent(r[2]);
    return null; //返回参数值
}
Comps.Screen.prototype._bindEvent = function () {
    var compId = this.config.compId;
    //更多事件
    $(".j_extMore").click(function () {
        $(this).parents(".sl-wrap").toggleClass("extend");
        $(".j_brand").prop("style", "display:block");
    })
    //拼音首字母覆盖事件
    $(".j_brandLetter li").hover(function () {
        $(".j_brand").prop("style", "display:none");
        var firstWord = $(this).data("initial");
        $(".j_brand").each(function () {
            var initial = $(this).data("initial");
            if (initial == firstWord || firstWord == 0) {
                $(this).prop("style", "display:block");
            }
        })
    })
    //多选事件
    $("#" + compId).delegate(".j_extMultiple", "click", function () {
        $(this).parents(".sl-wrap").addClass("multiple");
        $(".j_brand").prop("style", "display:block");
        $(".sl-ext").prop("style", "display:none");
    })
    //多选内的取消按钮事件
    $("#" + compId).delegate(".j_btnsCancel", "click", function () {
        $(this).parents(".sl-wrap").removeClass("multiple");
        $(".sl-ext").prop("style", "display:block");
        var selectedDiv = $(this).parents(".sl-wrap").find(".j_selectedDiv");
        selectedDiv.data("attrName", "");
        selectedDiv.data("attrValue", "");
        selectedDiv.find("ul").html("");
        selectedDiv.hide();
        $(".j_btnsConfirm").addClass("disabled");
    })
    // 条件删除效果
    $("#" + compId).delegate('.fa', 'click', function () {
        var attrName = $(this).parents('.j_attribute').data("name");
        $(this).parents('.j_attribute').remove();
        var uri = window.location.href;
        var uriName=window.location;
        uriName=uriName.origin+uriName.pathname;
        var index=uri.indexOf("?");
        if(index>0){
            var uriparam=uri.substring(index+1);
            var obj = getQueryObj(uriparam);
            console.log(obj);
            for(key in obj){
                if("分类"==attrName){
                    if(key=="categoryUuid"){
                        delete obj[key];
                    }
                }
                if("attributeBrand"==attrName){
                    if(key=="attrValue")
                    delete obj[key];
                }
            }
            var parmStr=getQueryString(obj);
            if(parmStr !="" && parmStr !=null){
                window.location.href=uriName+"?"+parmStr;
            }else{
                window.location.href=uriName;
            }
        }
    })
    //删除所有条件
    $("#" + compId).delegate("#" + compId + "_clearAll", "click", function () {
        var uri=window.location.href;
        var uriName=window.location;
        uriName=uriName.origin+uriName.pathname;
        var index=uri.indexOf("?");
        if(index>0){
            uri=uri.substring(index+1);
            var obj = getQueryObj(uri);
            console.log(obj);
            for(key in obj){
               if(key=="categoryUuid"){
                   delete obj[key];
               }
               if(key=="attrValue"){
                   delete obj[key];
                }
            }
            var parmStr=getQueryString(obj);
            if(parmStr !="" && parmStr !=null){
                parmStr=decodeURI(parmStr);
                window.location.href=uriName+"?"+parmStr;
            }else{
                window.location.href=uriName;
            }
        }
    })
    $("#" + compId).delegate(".j_selectedAttr", "click", function () {
        var attrValue = $(this).parents(".j_selectedDiv").data("attrValue");
        var thisAttr = $(this).find("a").prop("title")
        var reg = new RegExp("(^|、)" + thisAttr + "([^、]*)");
        var r = attrValue.match(reg);
        $(this).parents(".j_selectedDiv").data("attrValue", attrValue.replace(r[0], ""));
        $(this).remove();
    });
    function loadTopCart(){

    }
    function getQueryObj(queryString) {
        var reg = /([^=&\s]+)[=\s]*([^=&\s]*)/g;
        var obj = {};
        while(reg.exec(queryString)){
            obj[RegExp.$1] = RegExp.$2;
        }
        return obj;
    }

    function getQueryString(queryObj) {
        return $.param(queryObj, true);
    }
}
///************************弹出登录框组件*******************
Comps.PopLoginComp = function(option){
    this.compId = option.compId;
    this.needAsyncInit = option.needAsyncInit;
    this.option = option;
}

//异步加载数据
Comps.PopLoginComp.prototype.init = function(){
    var that = this;
    var compId = this.compId;
    var subTopic = this.option.subTopic;
    if(subTopic != null && subTopic != ""){
        var topicList = subTopic.split(",");
        $.each(topicList,function(){
            CompMessage.subscribe(this,function(option,callback){
                //弹出登录框
                $.getJSON(ContextPath+"/popLoginComp/isLogin",{},function(data){
                    data=eval("("+data+")");
                    if(data.flag == false){
                        $("#modal-login-form").modal();
                        $('#y_loadlogin_id').unbind("click");
                        $("#y_loadlogin_id").click(function(){
                            that.customerlogin(callback);
                        });
                    }else{
                        if(callback){
                            callback();
                        }
                    }
                });
            });
        });
    }

    //1.是否懒加载，如果是懒加载，则下面数据通过ajax请求，渲染html页面
    if(this.needAsyncInit){
        $.ajax({
            type: "GET",
            url: ContextPath+"/popLoginComp/ajaxLoadData",
            data: {},
            dataType: "json",
            success: function(data){
                console.log(data);
                that.genViewHtml(data);
                that.bindSomeEvent();
                Utils.ajaxLoadComplate(that);
            },
        });
    }else{
        that.bindSomeEvent();
        Utils.ajaxLoadComplate(that);
    }
}


Comps.PopLoginComp.prototype.genViewHtml = function(data){
    var compId = this.compId;

    $("#"+compId+"_loginName").val(data.loginName);
    $("#"+compId+"_pwd").val(data.pwd);

    var remember = data.remember;
    if(remember == "on"){
        $("#"+compId+"_remember").attr("checked","checked");
    }else{
        $("#"+compId+"_remember").removeAttr("checked");
    }

    $("#"+compId+"_a_reg").attr("href",ContextPath+this.option.a_forgetPwdUrl).html(this.option.a_forgetPwdName);
    $("#"+compId+"_a_forgetpwd").attr("href",ContextPath+this.option.a_regUrl).html(this.option.a_regName);
}


Comps.PopLoginComp.prototype.bindSomeEvent = function(){
    var compId = this.compId;
    var that = this;
    $("#y_loadlogin_id").click(function(){
        that.customerlogin();
    });

    $("#"+compId+"_loginName").blur(function(){
        var loginName = $("#"+compId+"_loginName").val();
        if(loginName == null || loginName == ""){
            $("#error_msg").html("请输入用户名/手机");
            $("#errordiv").show();
            return false;
        }else{
            $("#error_msg").html("");
            $("#errordiv").hide();
        }
    });

    $("#"+compId+"_pwd").blur(function(){
        var password = $("#"+compId+"_pwd").val();
        if(password == null || password == ""){
            $("#error_msg").html("请输入密码");
            $("#errordiv").show();
            return false;
        }else{
            $("#error_msg").html("");
            $("#errordiv").hide();
           // $("#"+compId+"_pwd").val(hex_md5($("#"+compId+"_pwd").val()));
        }
    });

}


//验证用户名、密码，提交
Comps.PopLoginComp.prototype.customerlogin = function(cal){
    var that = this;
    var compId = this.compId;
    var loginName = $("#"+compId+"_loginName").val();
    if(loginName == null || loginName == ""){
        $("#error_msg").html("请输入用户名/手机");
        $("#errordiv").show();
        return false;
    }else{
        $("#error_msg").html("");
        $("#errordiv").hide();
    }

    var pwd = $("#"+compId+"_pwd").val();
    if(pwd == null || pwd == ""){
        $("#error_msg").html("请输入密码");
        $("#errordiv").show();
        return false;
    }else{
        $("#error_msg").html("");
        $("#errordiv").hide();
    }
    var hasVilidateCode =  $("#"+compId+"_hasVilidateCode").val();
    if(hasVilidateCode == "true"){
        var validateCode = $("#"+compId+"_validateCode").val();
        if(validateCode == null || validateCode == ""){
            $("#error_msg").html("请输入验证码");
            $("#errordiv").show();
            return false;
        }else{
            $("#error_msg").html("");
            $("#errordiv").hide();
        }
    }
    var validateCode =  $("#validateCode").val();
    var remember = "";
    $("input[name='remember']:checked").each(function(){
        remember = $(this).val();
    });

    $.ajax({
        type: "POST",
        url: ContextPath+"/popLoginComp/quickLogin",
        data: {
            "loginName":loginName,
            "pwd":pwd,
            "hasVilidateCode":hasVilidateCode,
            "validateCode":validateCode,
            "remember":remember,
            "type":"customer",
            ranNum : Math.random()
        },
        dataType: "json",
        success: function(data){
            if(data.code == "0"){
                $("#modal-login-form").modal('hide');//关闭模态框
                //window.location.reload(); //登录成功后，刷新当前页面
                if(cal){
                    cal();
                }
                var pubTopic = that.option.pubTopic;
                if(pubTopic != null && pubTopic != ""){
                    //发布登录成功主题
                    CompMessage.publish(pubTopic,{},function(){

                    });
                }

            }else{
                $("#error_msg").html(data.msg);
                $("#errordiv").show();
                return false;
            }
        }
    });
}


/************************************** productMain  START**************************************/

Comps.Productmain = function(option){
    this.compId = option.compId;
    this.showMarketPrice = option.showMarketPrice;
    this.showAppraise = option.showAppraise;
    this.showBuy = option.showBuy;
    this.showStock = option.showStock;
    this.needAsyncInit = option.needAsyncInit;
    //加入购物车发布的主题名称
    this.cartEventTopic = option.cartEventTopic;
    //购买时 要求登陆的主题名称
    this.loginTopic = option.loginTopic;
    //运行期商品ID从URL中获取
    this.sku = this.getQueryString("sku");
    if(this.sku == null){
        //预览时  从属性配置中获取
        this.sku  = option.productUuid;
    }
}


Comps.Productmain.prototype.getQueryString = function(name){
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}

Comps.Productmain.prototype.init = function(){
    var that = this;

    //检查必选参数
    if(!this.checkProductUuid()){
        Utils.ajaxLoadComplate(that);//去掉遮罩
        return;
    }

    //1.是否懒加载，如果是懒加载，则下面数据通过ajax请求，渲染html页面
    if(this.needAsyncInit){
        // ajax先生成html页面，然后在初始化效果组件
        $.ajax({
            url:ContextPath+"/productmain/getInitData",
            data:{sku: this.sku},
            method:"GET",
            dataType: "json",
            success:function(data){
                if(data.productUuid!= null && data.productUuid!= undefined && data.poductUuid!=""){
                    //生成页面
                    that.genCompHtml(data);
                    //初始化事件
                    that.initEvents();

                    //进入页面，添加浏览足迹
                    that.viewRecords(data);
                }else{
                    $("#"+that.compId).html("<p style='text-align: center;color: red'>请配置 正确的预览参数[商品UUID]</p>");
                }
                Utils.ajaxLoadComplate(that);


            },error:function(e){
                console.log("懒加载数据格式出现错误");
            }
        });

    }else{
        //同步加载，后台生成页面
        //初始化事件
        $(document).ready(function(){
            var viewRecordStr = $("#"+that.compId).attr("data-viewRecord");
            if(viewRecordStr && viewRecordStr!=""){
                var viewRecord  = JSON.parse(viewRecordStr);
                that.viewRecords(viewRecord);
            }
            that.initEvents();
            Utils.ajaxLoadComplate(that);
        });

    }

}


Comps.Productmain.prototype.checkProductUuid = function(){
    var compId = this.compId;
    if(this.sku!= null && this.sku!= undefined && this.sku!=""){
        return true;
    }else{
        $("#"+compId).html("<p style='text-align: center;color: red'>请配置 预览参数[商品sku]</p>");
        return false;
    }
}

Comps.Productmain.prototype.viewRecords = function(data){
    var that = this;
    var product = {};
    product.productUuid = data.productUuid;
    product.productName = data.productName;
    product.bigImageUrl = data.bigImageUrl;
    product.shopPrice = data.shopPrice;
    product.saleCount = data.saleCount;
    product.privilegeTypes = ["1"];////????
    that.saveViewRecords(product);

}

//保存浏览足迹
Comps.Productmain.prototype.saveViewRecords = function(productDetailInfo){
    var viewRecords = localStorage.getItem('_viewRecords2');
    if(viewRecords != null && viewRecords != ""){
        var viewRecordsArray = JSON.parse(viewRecords);
        var productUuid = productDetailInfo.productUuid;
        localStorage.setItem('productUuid',productUuid);
        if(viewRecordsArray[productUuid] == null){
            viewRecordsArray[productUuid] = productDetailInfo;
            localStorage.setItem('_viewRecords2',JSON.stringify(viewRecordsArray));
        }

    }else{
        var viewRecordsArray = {};
        var productUuid = productDetailInfo.productUuid
        if(viewRecordsArray[productUuid] == null){
            viewRecordsArray[productUuid] =  productDetailInfo;
            localStorage.setItem('_viewRecords2',JSON.stringify(viewRecordsArray));
        }

    }
}



Comps.Productmain.prototype.genCompHtml = function (data){
    var compId = this.compId;
    //生成左侧图片列表
    var ulELe = $("#"+compId+"_img-items ul");
    var liEle  = ulELe.find("li:first");
    liEle.remove();//移除模板

    //第一张主图
    $("#"+compId+"_mid").attr("src",data.centerImageUrl).attr("data-big",data.bigImageUrl);
    var firstLi = liEle.clone();
   // firstLi.find("img").attr("src",data.smallImageUrl).attr("data-mid-url", data.centerImageUrl).attr("data-big-url", data.bigImageUrl);
   // ulELe.append(firstLi);
    //其他多视角图片
    if(data.imgList && data.imgList != null){
        $.each(data.imgList,function(index,one){
            var tmpLi = liEle.clone();//克隆模板，填充数据，然后追加到ul下
            tmpLi.find("img").attr("src",one.smallImageUrl).attr("data-mid-url", one.centerImageUrl).attr("data-big-url", one.bigImageUrl);
            ulELe.append(tmpLi);
        });
    }


    //生成右侧主信息页面
    if(!data.isSelfStore)$("#"+compId+"_selfStore").remove();
    $("#"+compId+"_productName").html(data.productName);
    $("#"+compId+"_note").html(data.adviceNote);
    //是否显示市场价
    if(this.showMarketPrice){
        $("#"+compId+"_marketPriceValue").html(((data.marketPrice)/100).toFixed(2));
    }else{
        $("#"+compId+"_marketPrice").remove();
    }
    //商城价格
    $("#"+compId+"_shopPrice").html((data.shopPrice/100).toFixed(2));

   /* //设置促销名称
    if(data.promotionName){
        $("#"+compId+"_promoname").html(data.promotionName);
    }else{
        $("#"+compId+"_promonameContainer").remove();
    }
*/

    //购买积分
    if(data.leastIntegral && data.leastIntegral>0){
        $("#"+compId+"_buyPoints").html(data.leastIntegral);
    }else{
        $("#"+compId+"_buyPointsEl").remove();
    }

    //单品促销
    this.genProductPromotions(data);


    //商品评价
    if(this.showAppraise){
        $("#"+compId+"_appraise").css("width",data.productScore*20 + "%");
        $("#"+compId+"_appCount").html(data.appCount);
    }else{
        $("#"+compId+"_productAppCount").remove();
    }

    //生成库存
    this.genStock(data);
    //显示立即购买按钮
    this.genFastBuy(data);
    //生成 商品规格属性
    this.genAttriubte(data);

}
$(document).on("click",".open",function(){
    $(".j_tmp_promotion").find("p").each(function(index,obj){
        if(index !=0){
            if($(obj).hasClass("hideClass")){
                $(obj).removeClass("hideClass");
            }else{
                $(obj).addClass("hideClass");
            }
        }
    });
})
Comps.Productmain.prototype.genStorePromotions=function(data){


}

//生成单品促销
Comps.Productmain.prototype.genProductPromotions=function(data){
    var that = this;
    var compId = this.compId;
    if(data.promotionName && data.promotionName.length>0){
        var parentContainer = $("#"+compId+"_promotionTmp");
        var promotionTmp = $("#"+compId+"_promotionTmp").find(".j_tmp_promotion");
        promotionTmp.remove();
        var proHtml="";
        for(var x=0;x<data.promotionName.length;x++){
            var onePromotionTmp  = promotionTmp.clone();
            var name=data.promotionName[x];
            var addClass=x==0?"":"hideClass"
            proHtml +='<p class="promotionName  '+addClass+'"><span>'+name+'</span></p>';
        }
        parentContainer.append('<div class="dd  j_tmp_promotion">'+
            proHtml+
            '<a class="open">展开促销</a>'+
            '</div>');
    }else{
        $("#"+compId+"_promotionTmp").remove();
    }
}



//生成库存
Comps.Productmain.prototype.genStock= function(data){
    var compId = this.compId;
    //显示库存
    $("#"+compId+"_number").attr("data-stock",data.stock);
    if(this.showStock){
        $("#"+compId+"_stock").html(data.stock);
    }else{
        $("#"+compId+"_stockNode").remove();
    }
    if(data && data.stock < 1){
        //无库存，置灰购买按钮和加入购物车
        $("#"+compId+"_fastBuy").prop("disabled",true);
        $("#"+compId+"_addtocar").prop("disabled",true);
    }
}
//购买按钮和加入购物车
Comps.Productmain.prototype.genFastBuy= function(data){
    var compId = this.compId;
    if(this.showBuy){

    }else{
        $("#"+compId+"_fastBuy").remove();
    }
}

//生成选择规格属性
Comps.Productmain.prototype.genAttriubte= function(data){
    var compId = this.compId;
    //初始化表单数据
    this.genInitFormData(data);
    var ulELe = $("#"+compId+"_productAttributes");
    var normalLiEle  = ulELe.find("li.j_normalAttr");
    var colorLiEle  = ulELe.find("li.j_colorAttr");
    console.log(colorLiEle.html());
    normalLiEle.remove();//移除模板
    colorLiEle.remove();//移除模板
    if(data.productAttributes && data.productAttributes != null){
        $.each(data.productAttributes,function(index,obj){
            if(obj.canColor =="0"){
                var tmpLi = normalLiEle.clone();//克隆模板，填充数据，然后追加到ul下
                var attrValsDiv = tmpLi.find(".j_attributesValues");
                var spanEle = tmpLi.find(".j_valuespan");
                spanEle.remove();
                tmpLi.find(".j_attributeName").html("选择"+obj.attributeName);
                $.each(obj.values,function(ind,val){
                    var tmpSpan = spanEle.clone();
                    if($.inArray(val.valueUuid,data.initSelectSpecUuids) != -1){
                        tmpSpan.addClass('active');
                        //父类加上已选择的uuid,以便事件处理
                        tmpLi.attr("data-selectValueUuid",val.valueUuid);
                    }
                    tmpSpan.find(".j_event_attributeValue").attr("data-valueUuid",val.valueUuid).html(val.value);
                    attrValsDiv.append(tmpSpan);
                });
                ulELe.append(tmpLi);
            }else{
                var attrVlaueId=data.attrValueId;
                if(attrVlaueId !==null){
                    var z=attrVlaueId.split("-");
                }
                //颜色属性
                var tmpLi = colorLiEle.clone();//克隆模板，填充数据，然后追加到ul下
                var attrValsDiv = tmpLi.find(".j_attributesValues");
                var spanEle = tmpLi.find(".j_valuespan");
                spanEle.remove();
                tmpLi.find(".j_attributeName").html("选择"+obj.attributeName);
                $.each(obj.values,function(ind,val){
                    var tmpSpan = spanEle.clone();
                    if($.inArray(val.valueUuid,data.initSelectSpecUuids) != -1){
                        tmpSpan.addClass('active');
                        //父类加上已选择的uuid,以便事件处理
                        tmpLi.attr("data-selectValueUuid",val.valueUuid);
                    }
                    if(false){
                        tmpSpan.find(".j_color_img").attr("src","");
                    }else{
                        tmpSpan.find(".j_color_img").remove();
                    }
                    tmpSpan.find(".j_event_attributeValue").attr("data-valueUuid",val.valueUuid);
                    tmpSpan.find(".j_attr_color").html(val.value);
                    attrValsDiv.append(tmpSpan);
                });
                ulELe.append(tmpLi);

            }
        });
    }

}

//初始化商品购买表单
Comps.Productmain.prototype.genInitFormData= function(data){

    var compId = this.compId;
    var form = $("#"+compId+"_pForm");
    var z=data.initSelectSpecUuids;
    if(z !=null && z!=""){
        var specSize = data.initSelectSpecUuids.length;
    }else{
        var specSize =0;
    }

    form.find("#"+compId+"_skuNo").val(data.skuNo);
    form.find("#"+compId+"_specSize").val(specSize);
    if(specSize > 0){
        for(var i = 0 ;i < specSize ; i++){
            form.find("#"+compId+"_specUuid"+i).val( data.initSelectSpecUuids[i]);
        }
    }
    form.find("#"+compId+"_productUuid").val(data.productUuid);
    form.find("#"+compId+"_productType").val(data.productType);
}



//组件事件
Comps.Productmain.prototype.events = {}

Comps.Productmain.prototype.events.fastBuy = function(comp){
    var compId = comp.compId;
    if(comp.loginTopic){
        CompMessage.publish(comp.loginTopic,{},function(){
            var num=$("#"+compId+"_number").val();
            var cartList = [];
            var cart ={};
            cart.sku = $("#"+compId+"_skuNo").val();
            cartList.push(cart);
            window.location.href =ContextPath+"/goods/placeOrder?"+$.param({cartInfo:JSON.stringify(cartList),num:num}, true);
        });
    }
}
//减少按钮事件
Comps.Productmain.prototype.events.reduceNumber = function(comp){
    var compId = comp.compId;
    $number = 	$("#"+compId+"_number");
    var number = parseInt($number.val());
    if(number == 1 || number < 2){
        return;
    }
    $number.val(--number);
}
//增加按钮事件
Comps.Productmain.prototype.events.addNumber = function(comp){
    var compId = comp.compId;
    $number = 	$("#"+compId+"_number");
    var number = $number.val();
    if(comp.showStock){
        var stock = parseInt($number.attr("data-stock"));
        if(number >= stock){
            $number.val(stock);
        }else{
            $number.val(++number);
        }
    }else{
        $number.val(++number);
    }
}

//数量输入框 填写事件
Comps.Productmain.prototype.events.inputNumber = function(comp,e){
    var input = $(e.target);
    var number = input.val();
    var stock = parseInt(input.attr('data-stock'));
    try{
        number = parseInt(number);
        if(isNaN(number)){
            number = 1;
        }
    }catch(e){
        number = 1;
    }
    if((number > stock) & comp.showStock)number = stock;
    input.val(number);
}

//加入购物车
Comps.Productmain.prototype.events.addToCart = function(comp){
    var compId = comp.compId;
    var skuNo = $("#"+compId+"_skuNo").val();
    var buyNum = $("#"+compId+"_number").val();
    data = {
        "sku":skuNo,
        "productUuid":comp.productUuid,
        "buyNum":buyNum,
        "time":new Date().getTime()
    };
    $.ajax({
        url:ContextPath+"/goods/addCart",
        data:data,
        method:"POST",
        success:function(data){
            if(comp.cartEventTopic){
                //加入购物车成功后，发布购物车消息
                CompMessage.publish(comp.cartEventTopic,{});
            }
            //有侧边栏购物车的时候 才有飞入效果
            if($(".y_rtcart").length > 0){
                //购物车飞入效果
                var this_x = $("#"+compId+"_addtocar").offset().left + $("#"+compId+"_addtocar").width()/2,
                    this_y = $("#"+compId+"_addtocar").offset().top- $(window).scrollTop() - 50;

                $("#"+compId+"_mid").shoping({
                    buyNum:buyNum,
                    star_x:this_x,//飞入元素的起点x轴位置
                    star_y:this_y//飞入元素的起点y轴位置
                });
            }
            if($(".yw_totalPrice").length>0){
                var productEle = $(".dropdown-cart-product-list");
                var limodel = $(".yw_addModule li");
                productEle.html("");
                var key="memberCartList";
                var carts=data.data[key];
                var price=0;
                var totalNum=0;
                if(carts !=null && carts.length>0){
                    for (var i = 0; i < carts.length; i++) {
                        var cart = carts[i];
                        var newli = limodel.clone();
                        var productId = newli.find(".j_productId");
                        var attrId = newli.find(".j_attrId");
                        var productName = newli.find(".j_productName");
                        var productPrice = newli.find(".j_productPrice");
                        var productNum = newli.find(".j_productNum");
                        var productUrl = newli.find(".j_productUrl");
                        var productImg = newli.find(".j_productImg");
                        var goodsProduct=cart.goodsProduct;
                        price +=parseInt(carts[i].price)*parseInt(carts[i].num);
                        totalNum +=parseInt(cart.num);
                        productId.html(cart.productId);
                        productName.html(goodsProduct.name);
                        productName.prop("href","/front/detail&sku="+goodsProduct.sku);
                        attrId.html(goodsProduct.sku);
                        productPrice.html((cart.price/100).toFixed(2));
                        productNum.html(cart.num);
                        productUrl.prop("href","/front/detail&sku="+goodsProduct.sku);
                        productImg.prop("src", cart.imgurl);
                        productImg.prop("data_sku",goodsProduct.sku);
                        productEle.append(newli);
                    }
                }
                $(".yw_totalPrice").html((price/100).toFixed(2));
                $(".yw_bottomTotalPrice").html((price/100).toFixed(2));
                $(".yw_totalNum").html(totalNum);
            }
        },error:function(e){
            console.log("加入购物车出现错误");
        }
    });
}


//2.初始化插件,功能
Comps.Productmain.prototype.initEvents=function(){
    var compId = this.compId;
    var that = this;
    var sku = this.sku;
    //初始化放大器事件
    var height =$("#"+compId).find(".j_preview").height();
    var plugin = $("#"+compId+"_bigImgWrap").enlarge(
        {
            // 鼠标遮罩层样式
            shadecolor: "#FFD24D",
            shadeborder: "#FF8000",
            shadeopacity: 0.4,
            cursor: "move",

            // 大图外层样式
            layerwidth: height,
            layerheight: height,
            layerborder: "#DDD",
            fade: true,

            // 大图尺寸
            largewidth: 740,
            largeheight: 740
        });


    //初始化小图列表鼠标移动事件
    $("#"+compId+"_imgSlider").imgslider({
        mouseover:function(obj){
            plugin.changeUrl($(obj).attr("data-mid-url"),$(obj).attr("data-big-url"));
            return true;
        }
    });


    $(".product_ms .y_prolist .y_slideDn").click(function(){
        $(this).hide().next(".y_slideUp").show().nextAll(".y_salebx").slideDown();
    });
    $(".product_ms .y_prolist .y_slideUp").click(function(){
        $(this).hide().prev(".y_slideDn").show().nextAll(".y_salebx").slideUp();
    });

    // 减少按钮事件
    $("#"+compId+"_reduce").click(function(){
        that.events.reduceNumber(that);
    });

    //增加按钮事件
    $("#"+compId+"_add").click(function(){
        that.events.addNumber(that);
    });

    //输入框事件
    $("#"+compId+"_number").bind('input propertychange', function(e) {
        that.events.inputNumber(that,e);
    });

    //选择规格属性事件,选择规格属性后动态改变价格和库存
    $("#"+compId+"_productAttributes").find(".j_event_attributeValue").click(function(){
        var selectUuid = $(this).attr("data-valueUuid");
        //设置该属性已选择的UUid为 selectUuid
        var parentLi = $(this).parents("li.j_data_chooseAttribute");
        parentLi.attr("data-selectValueUuid",selectUuid);
        parentLi.find(".j_valuespan").removeClass('active');
        $(this).parent().addClass('active');

        //获取所有已选择的规格属性
        var noSelectMsg = [];
        var  form = $("#"+compId+"_pForm");
        var selectedUuids = parentLi.parent().find("li.j_data_chooseAttribute").map(function(index,liEle){
            var selectedUuid = $(liEle).attr("data-selectValueUuid");
            if(selectedUuid && selectedUuid!= ""){
                //已经选择的 给表单赋值,购买时提交表单需要此数据
                form.find("#"+compId+"_specUuid"+index).val(selectedUuid);
                return selectedUuid;
            }else{
                noSelectMsg.push($(liEle).find('.j_attributeName').html());
                return false ;
            }
        }).get();
        //所有规格已经选择，提交后台获取库存价格等信息
        if($.inArray(false,selectedUuids) == -1){
            $.getJSON(ContextPath+"/productmain/selectSpec",
                {sku:sku, selectUuids : selectedUuids.join("-")},
                function(data){
                    // 动态修改价格和库存
                    $("#"+compId+"_productName").html(data.productName);
                    //设置价格
                    $("#"+compId+"_shopPrice").html((data.price/100).toFixed(2));
                    //设置SKU
                    $("#"+compId+"_skuNo").val(data.skuNo);
                    //设置库存
                    $("#"+compId+"_number").attr("data-stock",data.stock);
                    if(that.showStock){
                        var stock = parseInt(data.stock);
                        $("#"+compId+"_stock").html(stock);
                        if(stock <= 0 ){
                            //没有库存，不可购买
                            $("#"+compId+"_addtocar").prop("disabled",true);
                            if($("#"+compId+"_fastBuy")){
                                $("#"+compId+"_fastBuy").prop("disabled",true);
                            }
                        }else{
                            $("#"+compId+"_addtocar").prop("disabled",false);
                            if($("#"+compId+"_fastBuy")){
                                $("#"+compId+"_fastBuy").prop("disabled",false);
                            }
                        }
                    }

                    //修改图片显示
                    plugin.changeUrl(data.centerPictureUrl,data.bigPictureUrl);
                });
        }else{
            console.log("未选择："+noSelectMsg);
        }

    });

    //立即购买事件
    $("#"+compId+"_fastBuy").on('click',function(){
        that.events.fastBuy(that);
    });
    $("#"+compId+"_addtocar").on('click',function(){
        that.events.addToCart(that);
    });

}



/************************************** productMain END **************************************/
//********************搜索页面，显示分类***********
Comps.SearchShowCategoryComp = function(option){
    this.compId = option.compId;
    this.needAsyncInit = option.needAsyncInit;
    this.option = option;
}

Comps.SearchShowCategoryComp.prototype.init = function(){
    var that = this;
    //1.是否懒加载，如果是懒加载，则下面数据通过ajax请求，渲染html页面
    if(this.needAsyncInit){
        var categoryUuid = this.getQueryString("categoryUuid");
        var keyword = this.getQueryString("keyword");
        var compId = this.compId;
        $.ajax({
            type: "GET",
            url: ContextPath+"/searchShowCategoryComp/ajaxLoadData",
            data: {
                categoryUuid:categoryUuid,
                keyword:keyword
            },
            dataType: "json",
            success: function(data){
                data=eval("("+data+")");
                var searchName = data.searchName;
                if(searchName != null && searchName != ""){
                    $("#"+compId+"_searchname").html(searchName);
                }else{
                    $("#"+compId+"_searchname").html("");
                }
                var subCate = data.subCate;
                that.genViewHtml(subCate);
                Utils.ajaxLoadComplate(that);
            }
        });
    }else{
        Utils.ajaxLoadComplate(that);
    }
}
//初始化页面数据
Comps.SearchShowCategoryComp.prototype.genViewHtml = function(subCate){
    var compId = this.compId;
    if(subCate != null && subCate.length > 0){
        var panel = $("#"+compId).find("#"+compId+"_category-filter");
        var subDiv = panel.clone();
        $("#"+compId+"_panel").remove();
        for(var i=0;i<subCate.length;i++){
            if(i==0){
                subDiv.find(".cat-list-cla").addClass("in");
            }else{
                subDiv.find(".cat-list-cla").removeClass("in");
            }
            subDiv.find(".cat-list-cla").attr("id","category-list-"+i);
            subDiv.find("#"+compId+"_panel_h3").attr("data-target", "#category-list-"+i);
            var plUrl = Utils.getProductListUrl(ContextPath,this.option.productListUrl,subCate[i].categoryUuid);
            subDiv.find("#"+compId+"_h3_a").attr("href",plUrl).html(subCate[i].categoryName);
            var scList = subCate[i].subCategoryList;
            subDiv.find("#"+compId+"_list_ul").empty();
            if(scList != null && scList.length > 0){
                for(var j=0;j<scList.length;j++){
                    var productListUrl = Utils.getProductListUrl(ContextPath,this.option.productListUrl,scList[j].categoryUuid);
                    subDiv.find("#"+compId+"_list_ul").append("<li><a href='"+productListUrl+"'>"+scList[j].categoryName+"</a></li>");
                }
            }else{
                $("#"+compId+"_list_ul").remove();
            }
            $("#"+compId+"_category-filter").append(subDiv.html());
        }
    }else{
        $("#"+compId+"_category-filter").remove();;
    }
}

//获取参数
Comps.SearchShowCategoryComp.prototype.getQueryString = function(name){
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}


//********************搜索页面，分类提示***********
Comps.SearchCategoryTipComp = function(option){
    this.compId = option.compId;
    this.needAsyncInit = option.needAsyncInit;
    this.option = option;
}

Comps.SearchCategoryTipComp.prototype.init = function(){
    var that = this;
    //1.是否懒加载，如果是懒加载，则下面数据通过ajax请求，渲染html页面
    if(this.needAsyncInit){
        var categoryUuid = this.getQueryString("categoryUuid");
        var keyword = this.getQueryString("keyword");
        var compId = this.compId;
        $.ajax({
            type: "GET",
            url: ContextPath+"/searchCategoryTipComp/ajaxLoadData",
            data: {
                categoryUuid:categoryUuid,
                keyword:keyword
            },
            dataType: "json",
            success: function(data){
                $("#"+compId+"_breadcrumb").empty();
                if(keyword != null && keyword != ""){
                    $("#"+compId+"_breadcrumb").append("<li>全部结果</li>").append("<li>"+data.keyword+"</li>");
                }
                var catess = data.catess;
                if(catess != null && catess.length > 0){
                    $("#"+compId+"_breadcrumb").append("<li><a href='"+ContextPath+that.option.indexUrl+"' title='首页'>首页</a></li>");
                    for(var i=0;i<catess.length;i++){
                        var productListUrl = Utils.getProductListUrl(ContextPath,that.option.productListUrl,catess[i].categoryUuid);
                        $("#"+compId+"_breadcrumb").append("<li><a href='"+productListUrl+"'  title='"+catess[i].categoryName+"'>"+catess[i].categoryName+"</a></li>");
                    }
                }
                Utils.ajaxLoadComplate(that);
            }
        });
    }else{
        Utils.ajaxLoadComplate(that);
    }
}

Comps.SearchCategoryTipComp.prototype.getQueryString = function(name){
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}


//###########商品收藏##########5
Comps.CollectionProduct = function(option) {
    this.compId = option.compId;
    this.needAsyncInit = option.needAsyncInit;
    this.lableName = option.lableName;
    this.imgSrc=option.imgSrc;
    this.topic=option.topic;
    this.collectProductUrl=option.collectProductUrl
}
Comps.CollectionProduct.prototype.GetQueryString = function(name) {
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}
// 初始化方法
Comps.CollectionProduct.prototype.init = function() {
    var that=this;
    var sku=that.GetQueryString("sku");
    if (this.needAsyncInit) {
        $.ajax({
            type : "GET",
            url : ContextPath+"/collectionProductComp/ajaxLoadData",
            data : {
                lableName : that.lableName,
                imgSrc:that.imgSrc
            },
            dataType : "json",
            async : true,
            success : function(data) {
                if(data.imgSrc!=""){
                    $("#"+that.compId+"_collectionproduct_img").attr("src", data.imgSrc);
                }
                $("#"+that.compId+"_collectionproduct_span").text(data.lableName);
                Utils.ajaxLoadComplate(that);
                that.bindSomeEvent();
            }
        })
    }
    else {
        that.bindSomeEvent();
        Utils.ajaxLoadComplate(that);
    }
}
Comps.CollectionProduct.prototype.collectProduct=function(){
    var that=this;
    var sku= that.GetQueryString("sku");
    var url = ContextPath+"/collectionProductComp/collectProduct";
    var returnMessage = $("#"+this.compId+"_returnMessage");
    var collectNum = $("#"+this.compId+"_collectNum");
    var collectUrl = $("#"+this.compId+"_collectUrl");
    $.ajax({
        type : "GET",
        url : url,
        data : {
            sku : sku,
            ranNum : Math.random()
        },
        dataType : "json",
        async : true,
        success : function(data) {
            data=eval("("+data+")");
            var msg="";
            if(data.state=="1"){
                msg="收藏成功!";
            }
            else if (data.state=="2"){
                msg="收藏失败!";
            }
            else if (data.state=="3"){
                msg="收藏成功!";
            }
            returnMessage.html(msg);
            collectNum.html(data.count);
            collectUrl.attr("href",ContextPath+that.collectProductUrl);
            $("#"+that.compId+"_modal-collect-form").modal('show');
            that.bindSomeEvent();
            Utils.ajaxLoadComplate(that);
        }
    })
}
Comps.CollectionProduct.prototype.bindSomeEvent=function(){
    var that=this;
    var sku=that.GetQueryString("sku");
    $("#"+that.compId+"_collectionproduct_a").click(function() {
        var loginUrl = ContextPath+"/popLoginComp/isLogin";
        $.get(loginUrl,{ranNum:Math.random()} ,function(data) {
            data=eval("("+data+")");
            if (false == data.flag) {
                //弹出登录框
                $("#modal-login-form").modal();
            }else{
                that.collectProduct(sku);
            }
        })

    })
}
//###########商品收藏##########



/************************************** productDetail StART**************************************/


//组件定义
Comps.Productdetail = function(option){
    this.compId = option.compId;
    this.needAsyncInit = option.needAsyncInit;
    this.pageShow = option.pageShow;
    this.showAttribute = option.showAttribute;
    //运行期商品ID从URL中获取
    this.sku = this.getQueryString("sku");
    if(this.sku == null){
        //预览时  从属性配置中获取
        this.sku  = option.productUuid;
    }
}

Comps.Productdetail.prototype.getQueryString = function(name){
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}

//组件渲染方法
Comps.Productdetail.prototype.init=function(){
    var that = this;
    if(this.needAsyncInit){
        $.getJSON( ContextPath+"/productdetail/getInitData",
            {sku:this.sku,pageShow:this.pageShow},
            function(data){
                if(data ){
                    //生成页面HTML
                    that.genHtml(data);
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

//生成页面
Comps.Productdetail.prototype.genHtml = function(data){
    var compId = this.compId;
    var root = $("#"+compId);

    //表头内购物车
    //root.find(".j_addtocart").find(".j_product-detail-img").attr("src",data.smallImg);
    //root.find(".j_addtocart").find(".j_smallPrice").html(data.price);


    //商品介绍
    this.genDetailHtml(data);

    //售后服务
    root.find("#"+compId+"_detailname").html(data.afterService);

    //规格与包装
    this.genProdAttrs(data);
    //商品评价
    this.genAppraise(data);
}

//生成详情页签html
Comps.Productdetail.prototype.genDetailHtml = function(data){
    var compId = this.compId;
    var root = $("#"+compId);

    //描述
    root.find("#"+compId+"_detailimg").html(data.desc);
    //商品规格属性
    //this.genSpecAttribute(data);
}

//商品规格属性
Comps.Productdetail.prototype.genSpecAttribute = function(data){
    var compId = this.compId;
    var root = $("#"+compId);
    var ulEle = root.find("#"+compId+"_attribute");
    var liEle = ulEle.find(".j_attr-item");
    liEle.remove();
    if(data.productExtends){
        $.each(data.productExtends,function(index,attr){
            var temLi = liEle.clone();
            temLi.find(".j_attrName").html(attr.originalName);
            temLi.find(".j_attrValue").html(attr.attrValue);
            ulEle.append(temLi);
        });
    }else{
        //没有规格参数
        $("#"+compId+"_attrWrap").remove();
    }
}

//生成规格与包装
Comps.Productdetail.prototype.genProdAttrs = function(data){
    var compId = this.compId;
    if(this.showAttribute){
        if(data.prodAttr !=null){
            $("#"+compId+"_prodAttrs-content").html(data.prodAttr);
        }else{
            $("#"+compId+"_prodAttrs-content").find(".j_content").html("");
        }

    }else{
        //删除标签页
        $("#"+compId+"_prodAttrs").remove();
        //删除标签页对应的内容
        $("#"+compId+"_prodAttrs-content").remove();
    }

}


//生成评价页签内容
Comps.Productdetail.prototype.genAppraise = function(data){
    var compId = this.compId;
    var comments = data.comments;
    //商品评价页签评价数
    $("#"+compId+"_appCount").html("("+comments.allCount+")");

    //好评率
    $("#"+compId+"_goodAppraiseRate").html(comments.goodAppraiseRate);
    //总评价人数
    $("#"+compId+"_allAppCount").html(comments.allCount);
    //好评率
    $("#"+compId+"_goodAppraiseRate2").html(comments.goodAppraiseRate+"%");
    $("#"+compId+"_goodProgress").css("width",comments.goodAppraiseRate+"%");

    //中评率
    $("#"+compId+"_middleAppraiseRate").html(comments.middleAppraiseRate+"%");
    $("#"+compId+"_middleProgress").css("width",comments.middleAppraiseRate+"%");
    //差评率
    $("#"+compId+"_badAppraiseRate").html(comments.badAppraiseRate+"%");
    $("#"+compId+"_badProgress").css("width",comments.badAppraiseRate+"%");

    //标签页各种评价数
    var tab = $("#"+compId+"_apptab");
    tab.find(".j_allAppCount").html(comments.allCount);
    //设置类型，用于点击tab事件，重新渲染评价
    tab.find("#"+compId+"_allAppraise").attr("data-type","0");
    //设置总页数，用于分页组件初始化
    tab.find("#"+compId+"_allAppraise").attr("data-totalPage",comments.pg.totalPage);


    tab.find(".j_goodAppCount").html(comments.goodCount);
    tab.find("#"+compId+"_goodAppraise").attr("data-type","1");

    tab.find(".j_middleAppCount").html(comments.middleCount);
    tab.find("#"+compId+"_normalAppraise").attr("data-type","2");

    tab.find(".j_badAppCount").html(comments.badCount);
    tab.find("#"+compId+"_lowAppraise").attr("data-type","3");

    tab.find(".j_picCount").html(comments.picCount);
    tab.find("#"+compId+"_showpicAppraise").attr("data-type","4");

    //评价内容
    this.genAppraiseContent(data.comments);

}
//评价内容
Comps.Productdetail.prototype.genAppraiseContent = function(comments){
    var compId = this.compId;
    var that = this;
    var divEle = $("#"+compId+"_evaluall");
    //重新渲染此部分需要移除之前的节点（点击好评，中评，差评，需要重新渲染内容）
    divEle.find(".j_remove").remove();
    if(comments.list && comments.list.length > 0){
        var itemEle = divEle.find(".j_item.j_template");
        $.each(comments.list,function(index,comment){
            var tempItem = itemEle.clone();
            //模板本身隐藏，克隆后 展示出来
            tempItem.css("display","block");
            //添加样式j_remove 以便于重新渲染时 删除这些节点
            tempItem.removeClass('j_template').addClass("j_remove");

            // 生成评论人信息
            //评论人头衔
            var defaultImg = ContextPath + "/static/usercenter/img/default.png";

            if(comment.customerImg){
                if(comment.customerImg.substr(0,4)!="http"){
                    tempItem.find(".j_customerImg").attr("src",defaultImg);
                }else{
                    tempItem.find(".j_customerImg").attr("src",comment.customerImg);
                }
                //tempItem.find(".j_customerImg").attr("src","/static/usercenter/img/default.png");
            }else{
                tempItem.find(".j_customerImg").attr("src",defaultImg);
            }
            //会员名称
            tempItem.find(".j_customerName").html(comment.customerName);
            tempItem.find(".j_appTime").html(comment.appraiseTime);
            //会员评分
            tempItem.find(".j_appScore").css("width",(comment.score/5)*100+"%");
            //评论内容
            tempItem.find(".j_appContent").html(comment.appraiseContent);
            //会员购买的规格
            var parent = tempItem.find(".j_attr");
            var attrName = parent.find(".j_attrName");
            var attrValue = parent.find(".j_attrValue");
            attrName.remove();
            attrValue.remove();

            /*	$.each(comment.spec,function(index,attr){
             that.genAppContentBuyAttr(attr,attrName,attrValue,parent);
             });*/

            //会员的晒单信息
            that.genCommentPic(comment,tempItem);

            divEle.append(tempItem);
        });
    }else{
        divEle.find(".j_nothing").clone().css("display","block").removeClass("j_template").addClass("j_remove").appendTo(divEle);
        //$("#"+$_compId_$+"_page").css();
    }


}

//评价人的购买信息
Comps.Productdetail.prototype.genAppContentBuyAttr = function(attr,attrName,attrValue,parent){
    var tmpAttrName = attrName.clone();
    var tmpAttrValue = attrValue.clone();
    tmpAttrName.html(attr.name+":");
    tmpAttrValue.html(attr.value);
    parent.append(tmpAttrName);
    parent.append(tmpAttrValue);
}

//评价人的晒图信息
Comps.Productdetail.prototype.genCommentPic = function(comment,tempItem){
    var wrap = tempItem.find(".j_pics");
    if(comment && comment.picList && comment.picList.length > 0){
        var ulEle = wrap.find(".j_photos-thumb");
        var liEle = ulEle.find(".j_photos-thumb-item");
        liEle.remove();
        $.each(comment.picList,function(index,pic){
            var tmpLi = liEle.clone();
            tmpLi.attr("data-src",pic).find(".j_img").attr("src",pic);
            ulEle.append(tmpLi);
        });
    }else{
        wrap.remove();
    }
}

//初始化 分页
Comps.Productdetail.prototype.initPaging=function(type,totalPage){
    var that = this;
    var page = $("#"+this.compId+"_page");
    var pageShow = this.pageShow;
    layui.use(['laypage', 'layer'], function(){
        var laypage = layui.laypage,layer = layui.layer;
        laypage({
            cont: page,
            pages: totalPage,
            skip: true,
            jump: function(obj, first){
                if(!first){
                    //初始化的时候也会执行次方法，如果不是第一次渲染分页，则只需要重新渲染评价内容
                    that.getCommentPage(type,obj.curr,pageShow,false);
                }else{
                    //首次渲染，什么也不做
                }
            }
        });
    });

}
//分页获取评论信息
Comps.Productdetail.prototype.getCommentPage=function(type,nowPage,pageShow,isInitPage){
    var that = this;
    $.getJSON( ContextPath+"/productdetail/getComment",
        {"sku":this.sku,"type":type,"nowPage":nowPage,"pageShow":pageShow},
        function(data){
            that.genAppraiseContent(data);
            if(isInitPage){
                //如果是点击了页签 好评，中评，则分页组件需要重新渲染，否则只点击上一页，下一页不需要重新渲染
                that.initPaging(type,data.pg.totalPage);
            }
        });
}
//固定表头
Comps.Productdetail.prototype.fixTop = function(){
    var compId = this.compId;
    var compTop = $("#"+compId).offset().top;
    var header =  $("#"+compId+"_header") ;
    window.onscroll = function(){
        var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
        if(  scrollTop >=  compTop) {
            header.removeClass("y_pdtabbox").addClass("y_producttabfix");
        } else {
            header.removeClass("y_producttabfix").addClass("y_pdtabbox");
        }
    }
}

//点击小图，展示大图
Comps.Productdetail.prototype.showPhoto = function(liEle){
    $(liEle).siblings().removeClass("j_current");
    $(liEle).toggleClass("j_current");
    var viewer = $(liEle).parents(".j_photos").find(".j_photo-view");
    viewer.css("display",$(liEle).hasClass("j_current")?"block":"none");
    var src = $(liEle).attr("data-src");
    viewer.find("img").attr("src",src);

    //点开，如果是 第一张或者最后一张 隐藏上一页或者下一页
    /**
     var photos = $(el).parents(".j_photos");
     var photoSize = photos.find(".j_photos-thumb-item").length;
     var currentPhoto = photos.find(".j_photos-thumb-item.j_current");
     var currentIndex = currentPhoto.index();
     if(currentIndex == 0){
		//第一张图 隐藏
	}
     **/
}

//点击大图片收缩起来
Comps.Productdetail.prototype.hidePhoto = function(el){
    $(el).parents(".j_photos").find(".j_photos-thumb-item").removeClass("j_current");
    $(el).css("display","none");
}

Comps.Productdetail.prototype.nextPhoto = function(el,next,e){
    var photos = $(el).parents(".j_photos");
    var photoSize = photos.find(".j_photos-thumb-item").length;
    var currentPhoto = photos.find(".j_photos-thumb-item.j_current");
    var currentIndex = currentPhoto.index();

    var $next = photos.find(".j_photos-thumb-item").eq(currentIndex+next);
    if(currentIndex+next == photoSize ){
        $next = photos.find(".j_photos-thumb-item").eq(0);
    }
    //移动到下一个
    photos.find(".j_big-photo").attr("src",$next.attr("data-src"));
    //先移除j_current,在给下一个加上j_current
    photos.find(".j_photos-thumb-item").removeClass("j_current");
    $next.addClass("j_current");

    e.stopPropagation();
}


//初始化事件
Comps.Productdetail.prototype.initEvents = function(){
    var compId = this.compId;
    var that = this;
    var tab = $("#"+compId+"_apptab");
    //页签事件
    tab.find("a").click(function(){
        var type = $(this).attr("data-type");
        that.getCommentPage(type,"1",that.pageShow,true);
    });

    //第一次加载页面,展示全部评价，分页首次渲染
    var totalPage = parseInt($("#"+compId+"_allAppraise").attr("data-totalPage"));
    this.initPaging("0", totalPage);
    this.fixTop();

    //点击晒图，展示大图
    $(document.body).on("click", ".j_photos-thumb-item", function(){
        that.showPhoto(this);
    });
    //点击大图，收起事件
    $(document.body).on("click", ".j_photo-view", function(){
        that.hidePhoto(this);
    });
    //点击上一张图，下一张图事件
    $(document.body).on("click", ".j_photo-view-nav-prev", function(e){
        that.nextPhoto(this,-1,e);
    });
    $(document.body).on("click", ".j_photo-view-nav-next", function(e){
        that.nextPhoto(this,1,e);
    });

    $("#"+compId+" .main_wrap").find("li a").on("click",function(e){
        var compTop = $("#"+compId).offset().top;
        var activeHeight = $("#"+compId+" .main_wrap").find("li.active").height();
        window.scrollTo(0,compTop-7);
        // 7是 active的高度和 正常的高度差
        //var height =  $("#"+compId+" .main_wrap").find("li").not(".active").height();
        //var pad = activeHeight -height;
        //window.scrollTo(0,compTop-pad);
    });

}


/************************************** productDetail END**************************************/
/**
 * StoreSearchComp
 * Created by 金辉 on 2017/1/11.
 */
/**
 * Created by 金辉 on 2017/1/11.
 */
Comps.StoreSearchComp = function (config) {
    this.compId = config.compId;
    this.config = config;
}
Comps.StoreSearchComp.prototype.init = function () {
    var compId = this.compId;
    var keyword = this._getUrlParam("storeKeyword");
    $("#" + compId + "_keyword").val(keyword);
    this._bindEvent();
    Utils.ajaxLoadComplate(this);
}
Comps.StoreSearchComp.prototype.search = function () {
    var compId = this.config.compId;
    var searchUrl = ContextPath+this.config.searchUrl;
    var keyword = $("#" + compId + "_keyword").val();
    var startPrice = $("#" + compId + "_startPrice").val();
    var endPrice = $("#" + compId + "_endPrice").val();
    //获取商铺UUID
    var storeId = this._getUrlParam("storeUuid");
    if($.isEmptyObject(storeId)){
        storeId=this._getStoreId();
    }
    if ($.isEmptyObject(keyword)) {
        keyword = "";
    }
    var url = searchUrl + "&storeUuid=" + storeId + "&storeKeyword=" + keyword;
    if (!$.isEmptyObject(startPrice)) {
        url += "&startPrice=" + startPrice;
    }
    if (!$.isEmptyObject(endPrice)) {
        url += "&endPrice=" + endPrice;
    }
    window.location.href = url;
}
Comps.StoreSearchComp.prototype._getStoreId = function () {
    var sku = this._getUrlParam("sku");
    var storeId="";
    $.ajax({
        "url":ContextPath+"/storeSearchComp/getProductStoreId",
        "data":{"sku": sku},
        "async":false,
        "success":function(data){
            storeId =  data;
        }
    })
    return storeId;
}
Comps.StoreSearchComp.prototype._bindEvent = function () {
    var ts = this;
    var compId = this.config.compId;
    $("#" + compId + "_searchProduct").click(function () {
        ts.search();
    })
}
//获取URL上参数
Comps.StoreSearchComp.prototype._getUrlParam = function (name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg); //匹配目标参数
    if (r != null) return decodeURIComponent(r[2]);
    return null; //返回参数值
}


//###########信誉##########11


//组件定义
Comps.Reputation = function(option) {
    this.compId = option.compId;
    this.needAsyncInit = option.needAsyncInit;
    this.lableName=option.lableName;
    this.collectShopUrl=option.collectShopUrl;
    this.topic=option.topic;
}


Comps.Reputation.prototype.GetQueryString = function(name) {

    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;

}



Comps.Reputation.prototype.init = function() {

    var that = this;

    var sku=this.GetQueryString("sku");

    if (this.needAsyncInit) {
        $.ajax({
            type : "GET",
            url : ContextPath+"/reputationComp/ajaxLoadData",
            data : {
                sku : sku,
                lableName : that.lableName
            },
            dataType : "json",
            async : true,
            success : function(data) {
                data=eval("("+data+")");
                $("#" + that.compId + "_reputation_img").attr("src",
                    data.shopUrl);
                $("#" + that.compId + "_reputation_img_a").attr("href",
                    ContextPath+"/dfFront/toShopPage/" + data.shopUuid + "/pc");
                $("#" + that.compId + "_reputation_name").attr("href",
                    ContextPath+"/dfFront/toShopPage/" + data.shopUuid + "/pc");

                $("#" + that.compId + "_reputation_name_span").first()
                    .text(data.shopName);

                $("#" + that.compId + "_lablename").first()
                    .text(data.lableName);

                $("#" + that.compId + "_reputation_i").attr("uuid",
                    data.shopUuid);

                $("#" + that.compId + "_reputation_i").attr("uuid",
                    data.shopUuid);

                $("#" + that.compId + "_reputation_descritionScore").attr("style",
                    "width:" + (data.prodDescScore) * 20 + "%");
                $("#" + that.compId + "_reputation_serviceScore").attr("style",
                    "width:" + (data.serviceAttitudeScore) * 20 + "%");
                $("#" + that.compId + "_reputation_speedScore").attr("style",
                    "width:" + (data.logisticSpeedScore) * 20 + "%");
                that.bindSomeEvent();

                Utils.ajaxLoadComplate(that);

            },
            error:function (e) {
                console.log(e);
            }

        })


    }
    else {

        that.bindSomeEvent();
        Utils.ajaxLoadComplate(that);


    }




}

Comps.Reputation.prototype.bindSomeEvent=function(){

    var that=this;

    $(".y_collect_store").click(function() {
        var uuid = $(".collect_btn").attr("uuid");
        var loginUrl = ContextPath+"/popLoginComp/isLogin";
        $.get(loginUrl,{ranNum:Math.random()} ,function(data) {
            data=eval("("+data+")");
            if (false == data.flag) {
                //弹出登录框
                $("#modal-login-form").modal();
            }else{
                that.collectShop(uuid);
            }
        })
        CompMessage.publish(that.topic,'',function(data){
        });
    })


}

Comps.Reputation.prototype.collectShop=function(uuid){

    var that=this;
    var url = ContextPath+"/reputationComp/collectStore";


    var returnMessage = $("#"+this.compId+"_returnMessage");
    var collectNum = $("#"+this.compId+"_collectNum");
    var collectUrl = $("#"+this.compId+"_collectUrl");

    $.ajax({
        type : "GET",
        url : url,
        data : {

            "type":"reputation",
            "storeId" : uuid,
            ranNum : Math.random()
        },
        dataType : "json",
        async : true,
        success : function(data) {
            var msg="";

            if(data.state=="0"){

                msg="收藏成功!";

            }
            else if (data.state=="2"){

                msg="收藏失败!";

            }

            else if (data.state=="1"){

                msg="收藏成功!";

            }
            returnMessage.html(msg);

            collectNum.html(data.count);

            collectUrl.attr("href",ContextPath+that.collectShopUrl);

            $("#"+that.compId+"_modal-collect-form").modal('show');
            Utils.ajaxLoadComplate(that);
        }

    })
}

//###########信誉##########


Comps.StoreRelCategoryComp = function(option) {
    this.compId = option.compId;
    this.needAsyncInit = option.needAsyncInit;
    this.relcategory = option.relcategory;
    this.relUrl=option.relUrl;

    //运行期商品ID从URL中获取
    this.sku = this.getQueryString("sku");
    if(this.sku == null){
        //预览时  从属性配置中获取
        this.sku  = option.productUuid;
    }
}

Comps.StoreRelCategoryComp.prototype.getQueryString = function(name){
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}

Comps.StoreRelCategoryComp.prototype.init = function(){
    var that = this;

    if (this.needAsyncInit) {
        $.ajax({
            type: "GET",
            url: ContextPath + "/storeRelCategoryComp/ajaxLoadData",
            data: {
                "sku": that.sku,
                "compId": that.compId,
                "relcategory": that.relcategory,
                "relUrl": that.relUrl,
                "contextPath": ContextPath

            },
            dataType: "text",
            async: true,
            success: function(data) {
                $("#" + that.compId).html(data);

                Utils.ajaxLoadComplate(that);
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                console.log("Comps.StoreRelCategoryComp.prototype.init " + XMLHttpRequest.status);
                console.log("Comps.StoreRelCategoryComp.prototype.init " + XMLHttpRequest.readyState);
                console.log("Comps.StoreRelCategoryComp.prototype.init " + textStatus);
            },
        })
    }
    else {
        Utils.ajaxLoadComplate(that);

    }
    $(document).on("click",".first_cate_y",function(){
        if($(this).next().is(":hidden")){
            $(this).next().show();
        }else{
            $(this).next().hide();
        }

    })
}


