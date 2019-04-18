/*
* @Author: wenyu
* @Date:   2017-01-11 17:14:13
* @Last Modified by:   wenyu
* @Last Modified time: 2017-01-11 17:14:17
*/





/*
 * lsl 楼层事件
 */
$(function() {
	
	$(".m-floorlable").each(function(index){
		
		$(this).removeClass("y_inflor1").addClass("y_inflor"+(index+1));
		
		$(this).attr("data-flor",index+1);
		
	});
	
	
	
		$('.y_florfix').html("");

	$('.y_catatit').each(function(i){
		
		var num = Number(i) + 1;
		var floorname = $(this).children().children('strong').text();
		
		var showname=$(this).find("span").text();
		
		if(showname==''){
			showname=floorname;
		}	
		
		$('.y_florfix').append('<span class="y_fixfl'+num+'" data-fix="'+num+'">'+showname+'</span>');
	});	



ltflorshow();
$(window).resize(function(){
	ltflorshow();
});
function ltflorshow(){
	var boxwidth=$(".container").width(),
	winwidth=$(window).width();
	if((winwidth/2-boxwidth/2)<77){
		$(".y_florfix").show();
	}else{
		$(".y_florfix").show();
	};
};
$(window).scroll(function() {
	$("[data-flor]").each(function(){
		var whei=$(window).height()/2;
	var list1=$(this).attr("data-flor");
	var opt1=$(".y_inflor"+list1).offset().top-whei;

	if ($(window).scrollTop() >= opt1) {
  	$(".y_florfix span").eq(list1-1).addClass("y_active").siblings().removeClass("y_active");
  }
});
if($(window).scrollTop() >= 200){
		if(!$('.y_florfix').hasClass("y_fixshow")){
   	$('.y_florfix').addClass("y_fixshow");
  }
 }else{
		if($('.y_florfix').hasClass("y_fixshow")){
   	$('.y_florfix').removeClass("y_fixshow");
  }
 };
});
$(".y_florfix span").click(function(){
	
	$(this).addClass("y_active");
	var list=$(this).attr("data-fix");
	var ltop=$(".y_inflor"+list).offset().top-55;
	$("html,body").animate({scrollTop:ltop},300);
});
});

/*
 * lsl 楼层事件
 */
//全部
//全局变量
//window.basePath='http://139.196.186.16:8080/api/app/appCall?';
//window.basePath='http://117.78.36.35:8887/api/app/appCall?';
window.basePath='http://shop.qwang.com.cn/api/app/appCall?';

//首页地址
homePageUrl = "http://shop.qwang.com.cn/bbc-h5/h5/templet/shop/index.html";






//判断是微信进入 还是浏览器进入
function isWeiXin(){
    var ua = window.navigator.userAgent.toLowerCase();
    if(ua.match(/MicroMessenger/i) == 'micromessenger'){
        return true;
    }else{
        return false;
    }
}










mui.init({
    swipeBack:true //启用右滑关闭功能
});



var slider = mui(".sub-banner");
slider.slider({ interval: 5000 });



$(function(){
    //此处为首页导航栏购物车显示效果
    var buynum = localStorage.getItem("buyNum");
    var temp = "<span  class=\"mui-badge\">"+buynum+"</span>";
    if(buynum != '0' && buynum != null){
        $(".cartNum").append(temp);
    }




    // //搜索页面的得到焦点事件
    // $('.searchproductall').focus(function(){
    //     window.location.href = '../../../h5/templet/search/initSearch.html';
    // })
});


/*点亮星星*/
$("#star i").click(function(){
    $(this).siblings().removeClass("on");
    $(this).addClass("on").prevAll().addClass("on");
});



//json字符串  解析
function strToJson(str){
    var json = eval('(' + str + ')');
    return json;
}

//今日活动活动商品详情
function activeProductInfo(productUuid,promotionUuid){
    localStorage.setItem("productIndex","2");//限时特卖
    localStorage.setItem("productUuid",productUuid);
    localStorage.setItem("promotionUuid",promotionUuid);
    window.location.href="../../../h5/templet/detail/b2b-details.html";
}

//进入 商品详情页
function productInfo(productUuid, backurl){

    if (backurl)
    {
        sessionStorage.setItem("setCurrentPageForPreviousPage", backurl);
    }


    var comeAddress =window.location.href;
    localStorage.setItem("comeAddress",comeAddress);
    localStorage.setItem("productIndex","1");//1普通商品详情
    localStorage.setItem("productUuid",productUuid);
    window.location.href="../../../h5/templet/detail/b2b-details.html";
}

//进入限时特卖活动页面    & A_1
function limitActiveInfo(activityId){
    localStorage.setItem("activityId",activityId);
    window.location.href="../../../h5/templet/shop/limit-sale-detail.html";
}

//进入立即购买页面
function payMoneyNow(productId){
    window.location.href="../../../h5/templet/detail/b2b-details.html";
}

//进入店铺
function instore(b){
    //跳转到店铺首页
    localStorage.setItem('storeUuid',b);
    window.location.href = '../../../h5/templet/shop/shop-index2.html';
}

//首页分类里面的方法
function subCategory(subCategoryId){
    localStorage.setItem("subCategoryId",subCategoryId);
    window.location.href='../../../h5/templet/shop/stroeclass.html';
}

//P_L_C_0  品牌列表  分类
function categoryStrUuid(Str){
    localStorage.setItem("searchType","2");
    localStorage.setItem("searchTypeParam",Str);
    localStorage.setItem("searchKeyWord","商品列表");
    window.location.href = '../../../h5/templet/search/list-interaction.html';
}

//A_0效果页
function localherf(src){
    window.location.href=src;
}
//a_1  limitActiveInfo 同进入限时特卖详情相同方法

// P_L_O_0       进入品牌list效果页  brandList
function brandList(brandName){
    localStorage.setItem("searchType","3");
    localStorage.setItem("searchTypeParam",brandName);
    localStorage.setItem("searchKeyWord",brandName);
    window.location.href = '../../../h5/templet/search/list-interaction.html';
}


//展示公告
function showhtml(body,name){
    localStorage.setItem("body",body);
    localStorage.setItem("name",name);
    window.open("../../../h5/templet/show.html");
}

//关键词搜索
function searchTypeWord(searchTypeWord,type){
    localStorage.setItem("searchType",type);
    localStorage.setItem("searchTypeParam",searchTypeWord);
    localStorage.setItem("searchKeyWord",searchKeyWord);
    window.location.href = '../../../h5/templet/search/list-interaction.html';
}

//获取优惠券效果
function getCoupon(couponId){
    var userId = localStorage.getItem('userId');
    var token = localStorage.getItem('token');
    var sessionId = localStorage.getItem('sessionId');
    var url = window.basePath+'jsonParam={"opeType":"receiveCoupon","map":{"customerUuid":"'+userId+'","couponDetailUuid":"'+couponId+'","token":"'+token+'","sessionId":"'+sessionId+'"}}';
    $.ajax({
        url:url,
        type: "POST",
        dataType:'json',
        success:function(data){
            mui.alert(data.message);
        }
    })
}
//解析进入我的积分页面
function personalPoints(){
    window.location.href ="../../../h5/templet/personal/integral.html";
}
//店铺推荐
function recomendStore(){
    window.location.href ="../../../h5/templet/shop/recommended-shop.html";
}


$(".shop-subnav span i").click(function(){
    $(this).addClass("action").toggleClass("on");
});

//actionType 解析方法                                    当前所需的dom对象
function linkdetail(actionType){
    var strList = strToJson(actionType);
    var actionCode = strList.actionCode;
    var actionId = strList.actionId;
    var actionTitle = strList.actionTitle;

    if(actionCode == "S_0"){//店铺首页
        if(typeof actionId != "undefined" && actionId !=null && actionId!=""){
            action = "instore(\'"+actionId+"\')";
            return action;
        }
        return action
    }
    if(actionCode == "P_0"){//商品详情
        if(typeof actionId != "undefined" && actionId !=null && actionId!=""){
            action = "productInfo(\'"+actionId+"\')";
            return action;
        }
        return action
    }
    if(actionCode == "A_3"){//店铺活动列表    暂无
        if(typeof actionId != "undefined" && actionId !=null && actionId!=""){
//			action = "productInfo(\'"+actionId+"\')";
//			localStorage.setItem("S_0",action);
            return action;
        }
        return action
    }
    if(actionCode == "A_0"){//活动内容页面暂无
        if(typeof actionId != "undefined" && actionId !=null && actionId!=""){
            action = "localherf(\'"+actionId+"\')";
            return action;
        }
        return action
    }
    if(actionCode == "A_1"){ //平台活动商品列表
        if(typeof actionId == "undefined" || actionId ==null || actionId!=""){
            action = "limitActiveInfo(\'"+actionId+"\')";
            return action;
        }
        return ""
    }
    if(actionCode == "P_L_O_0"){ //商品列表-品牌
        if(typeof actionId != "undefined" && actionId !=null && actionId!=""){
            action = "brandList(\'"+actionId+"\')";
            return action;
        }
        return action
    }
    if(actionCode == "P_L_C_0"){  //商品列表-分类
        if(typeof actionId != "undefined" && actionId !=null && actionId!=""){
            action = "categoryStrUuid(\'"+actionId+"\')";
            return action;
        }
        return action
    }
    if(actionCode == "M_I_0"){
        action = "personalPoints()";
        return action;
    }
    if(actionCode == "Q_P_0"){ //关键字搜索商品列表
        if(typeof actionId != "undefined" && actionId !=null && actionId!=""){
            action = "searchTypeWord(\'"+actionId+"\','4')";
            return action;
        }
        return action
    }
    if(actionCode == "Q_S_0"){ //关键字店铺商品列表
        if(typeof actionId != "undefined" && actionId !=null && actionId!=""){
            action = "searchTypeWord(\'"+actionId+"\','1')";
            return action;
        }
        return action
    }
    if(actionCode == "S_RM_0"){ //店铺推荐
        if(typeof actionId != "undefined" && actionId !=null && actionId!=""){
            action = "recomendStore()";
            return action;
        }
        return action
    }
    if(actionCode == "A_0_1"){ //展示一段html
        if(typeof actionId != "undefined" && actionId !=null && actionId!=""){
            action = "showhtml(\'"+actionId+"\',\'"+actionTitle+"\')";
            return action;
        }
        return action
    }
    return "";

}
//登录跳转
function toLogin(url){
    var userId= localStorage.getItem('userId');
    var token = localStorage.getItem('token');
    var sessionId = localStorage.getItem('sessionId');
    if(userId != null && userId != "" ){
        window.location.href = url;
    }else{
        mui.alert('当前用户未登录','提示',function(){
            window.location.href = '../../../h5/templet/login/login1.html';
        })
    }
}



//获取当前页面url，存入缓存
function setAgoPageUrl(){
    // var urlNow = window.location.href;
    // sessionStorage.setItem("urlNow",urlNow);//为保证session的key不重复，不宜设置过短。
    sessionStorage.setItem("setCurrentPageForGetPreviousPage",location.href);
}

//获取当前页面的来源，获取不到就默认首页。
//备注：在微信公众号上，当某个页面选择从浏览器打开时，是获取不到session的值的，会显示404错误，故设置默认值
function getAgoPageUrl(){
    var urlNowPage = sessionStorage.getItem("setCurrentPageForPreviousPage") || homePageUrl;
    console.log("需要返回页是: " + urlNowPage);
    var backurl = urlNowPage == '' ? homePageUrl : urlNowPage;
    sessionStorage.setItem("setCurrentPageForPreviousPage", ''); // 重置
    window.location.href = backurl;
}

//跳转到之前的页面
function jumpTOBeforPage(){
    var ref = "";
    if (document.referrer.length > 0) {
        ref = document.referrer;
    }

    try {
        if (ref.length == 0 && opener.location.href.length > 0) {
            ref = opener.location.href;
        }
    } catch (e) {
        location.href = homePageUrl;
    }
    if (ref != "") {
        location.href = ref;
    }else{
        location.href = homePageUrl;
    }

}




//如果token或者session失效登录
function toLoginAjax(returnCode){
    if(returnCode == '49'||returnCode=='50'||returnCode=='51'){
        mui.alert('当前用户未登录','提示',function(){
            window.location.href = '../../../h5/templet/login/login1.html';
        })
    }
}
//从哪里来到哪里去，记录下当前页面，登录后取到这个url再跳回去
function toLoginAjax(returnCode,url){
    if(returnCode == '49'||returnCode=='50'||returnCode=='51'){
        mui.alert('当前用户未登录','提示',function(){
            localStorage.setItem("comeAddress",url);
            window.location.href = '../../../h5/templet/login/login1.html';
        })
    }
}
//删除购物车
function delCart(productAttrForDel){
    var productAttrForDel = localStorage.getItem("productAttrForDel");
    window.location.href = '../../../h5/templet/cart/cart.html';
}

//购物车商品数量、
function cartproductNum(){
    var userId = localStorage.getItem("userId");
    var token = localStorage.getItem("token");
    var sessionId = localStorage.getItem("sessionId");
    var url = window.basePath+'jsonParam={"opeType":"getCart","map":{"customerUuid":"'+userId+'","token":"'+token+'","sessionId":"'+sessionId+'"}}';
    var cartNum = 0;
    $.ajax({
        url:url,
        type: "POST",
        dataType:'json',
        async : false,
        success:function(data){
            if(data.return_code == "0"){
                var object = data.carts;
                for (var obj in object){
                    var reg = /^(cart_).*/;
                    if (reg.test(obj)){
                        var mingxiInfo = object[obj];
                        cartNum = cartNum + mingxiInfo.length;
                    }
                }

            }else{
                cartNum = 0;
            }
        }
    })
    return cartNum;
}


//分页model
function wmModelStr(nowPage,pageShow,totalNum,totalPage){
    if(pageShow == "" || pageShow == null){
        pageShow="20";
    }else{
        pageShow=pageShow;
    }
    if(nowPage == "" || nowPage == null){
        nowPage= "1";
    }else{
        nowPage= nowPage;
    }
    if(totalNum == "" ){
        totalNum =" ";
    }
    if(totalPage == "" ){
        totalPage =" ";
    }
	/* wm.totalNum=totalNum;
	 wm.totalPage=totalPage*/
    var wmModelStr = '{"nowPage":"'+nowPage+'","pageShow":"'+pageShow+'","totalNum":"'+totalNum+'","totalPage":"'+totalPage+'"}';
    return wmModelStr;
};

// 判断字符串是否为空   为空则返回true;否则返回false; add by jt 20160909
function isEmptyStr(str)
{
    var reg = /\S/;

    var v = true;

    (!reg.test(str)) ? v : v = false;

    return v;


}

//解析参与活动str   //返回一段html
function  zhuxiaoway(obj){ //<i class="by">包邮</i>
    var zhuxiaowayStr = '';
    if(typeof obj != 'undefined' && obj != null && obj.length>0){
        for(var i = 0 ; i<obj.length;i++){
            if(obj[i]=="1"){
                zhuxiaowayStr = zhuxiaowayStr + '<i class="mj">满减</i>';
            }else if(obj[i]=="2"){
                zhuxiaowayStr = zhuxiaowayStr + '<i class="by">包邮</i>';
            }else if(obj[i]=="3"){
                zhuxiaowayStr = zhuxiaowayStr + '<i class="dpz">单品赠</i>';
            }else if(obj[i]=="4"){
                zhuxiaowayStr = zhuxiaowayStr + '<i class="ddz">订单赠</i>';
            }else if(obj[i]=="5"){
                zhuxiaowayStr = zhuxiaowayStr + '<i class="dpdz">单品打折</i>';
            }else if(obj[i]=="6"){
                zhuxiaowayStr = zhuxiaowayStr + '<i class="dddz">订单打折</i>';
            }
        }
    }
    return zhuxiaowayStr;
}




function UrlRegEx(url) {
    //如果加上/g参数，那么只返回$0匹配。也就是说arr.length = 0   
    var re = /(\w+):\/\/([^\:|\/]+)(\:\d*)?(.*\/)([^#|\?|\n]+)?(#.*)?(\?.*)?/i;
    //re.exec(url);   
    var arr = url.match(re);
    return arr;

}


//获取url参数的值，传的是参数名称(类型：String)，此方法在多个参数时比较好用
// 例如获取商品的uuid--var productUuid = getQueryString(productUuid);
function getQueryString(name){
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null){
        return  unescape(r[2]);
    }
    return null;
}


// 判断微信环境
function isWeiXin()
{
    var ua = window.navigator.userAgent.toLowerCase();
    if(ua.match(/MicroMessenger/i) == 'micromessenger')
    {
        return true;
    }
    else
    {
        return false;
    }
}


/**html转义*/
var entityMap = {
    "&" : "&amp;",
    "<" : "&lt;",
    ">" : "&gt;",
    '"' : '&quot;',
    "'" : '&#39;',
    "/" : '&#x2F;'
};

function encodeHtml(str) {
    return String(str).replace(/[&<>"'\/]/g, function(s) {
        return entityMap[s];
    });
}

function setListTyle(target,multiple) {
    $.each($(target+' li'), function() {
        var $width = $(target+' li').eq(0).width();
        var $height = $width * multiple; //alert(_width3);
        $(this).height($height+'px');
    })

}

