//单选
$(document).on("click","[data-label]:not('.disable')",function(){
	$(this).addClass("active").siblings().removeClass("active");
	num = $(this).index();
})

/*$(document).on("click","[data-labelt]:not('.disable')",function(){
    $(this).toggleClass("active").siblings().removeClass("active");
})*/


$(document).on("click","[data-check]",function(){
	$(this).addClass("active");
})
//数量加减
$(function(){ 
  	/*$(".next").click(function(){
	var t=$(this).parent().find('input[type=text]'); 
	    t.val(parseInt(t.val())+1) 
	    //setTotal();
	}) 
    $(".prev").click(function(){ 
        var t=$(this).parent().find('input[type=text]'); 
        t.val(parseInt(t.val())-1) 
            if(parseInt(t.val())<0){ 
            	t.val(0); 
            } 
        	//setTotal();
    }) */
}) 

//点亮星星
$(document).on("mouseover",".u-stars i",function(){
//	$(this).css("color","#ff7a68").prevAll().css("color","#ff7a68");
}).on("mouseout",".u-stars i",function(){
//	$(".u-stars i").css("color","#ccc");
}).on("click",".u-stars i",function(){
	$(".u-stars i").css("color","#ccc");
	$(this).css("color","#ff7a68").prevAll().css("color","#ff7a68");
})

//限制字数
jQuery.fn.limit=function(){
    var self = $("[limit]");
    self.each(function(){
        var objString = $(this).text();
        var objLength = $(this).text().length;
        var num = $(this).attr("limit");
        if(objLength > num){
            $(this).attr("title",objString);
            objString = $(this).text(objString.substring(0,num)+"...");
        }
        $(this).attr("title","");
    })
}

$(function(){
    ajaxTop();
    getCart();
    $(".good-info .info").attr("limit",30);
    $("[limit]").limit();
});

//nav
$(document).on("click",".unm-hd",function(){
	$(".unm-bd").toggle();
})

$(document).on("click",".icon-check",function(){
	$(this).toggleClass("checked");
})
/**
 * 头部信息
 */
function ajaxTop(){
    $.ajax({
        type: "GET",
        url: "/topComp/ajaxLoadData",
        data: {},
        dataType: "json",
        success: function(data){
            var platName = data.platName;
            $("#top_comm_em_platName").html(platName);
            var nickName = data.nickName;
            if(nickName != null && nickName != ""){
                $("#top_comm_a_nickname").show();
                $("#top_comm_a_nickname").attr("href","/member").html(nickName);
                $("#top_comm_a_login").hide();
                $("#top_comm_a_register").hide();
                $("#top_comm_a_myorder").show();
                $("#top_comm_a_myorder").attr("href","/member/order").html("我的订单");
                $("#top_comm_a_logout").show();
                $("#top_comm_a_logout").attr("href","/member/login/logout").html("退出");
            }else{
                $("#top_comm_a_nickname").hide();
                $("#top_comm_a_logout").hide();
                $("#top_comm_a_myorder").hide();
                $("#top_comm_a_login").show();
                $("#top_comm_a_login").attr("href","/member/login").html("登录");
                $("#top_comm_a_register").show();
                $("#top_comm_a_register").attr("href", "/member/regist").html("注册");
            }
            $("#top_comm_a_index").attr("href","/").html("首页");
            $("#top_comm_a_favproduct").attr("href", "/member/collection").html("收藏的商品");
            $("#top_comm_a_favstore").attr("href",  "/member/collection").html("收藏的商品");
            $("#top_comm_a_store").attr("href", "/store/login").html("商家中心");
            $("#top_comm_a_toapply").attr("href", "/store/join/join").html("商家入驻");
            $("#top_comm_a_helpcenter").attr("href", "/").html("帮助中心");
        }
    });
}

/**
 * 搜索和购物和数据
 */

$(document).on("click",".m-search-btn",function(){
    var keyword = $(".m-search-input").val();
    if($.isEmptyObject(keyword)){
        keyword = "";
        return;
    }
    window.location.href="/front/list?keyword="+keyword;
})
function getCart(){
    $.ajax({
        "url":"/cartComp/ajaxLoadData",
        "success": function (data) {
            data = eval("(" + data + ")");
            //上方总价
            var totalPriceEle = $(".yw_bottomTotalPrice");
            //悬浮框上的总价
            var priceEle = $(".yw_totalPrice");
            var price = 0;
            if (!$.isEmptyObject(data)) {
                //商品总价
                var cart = data.cartProduct;
                for (var x = 0; x < cart.length; x++) {
                    price += parseInt(cart[x].price) * parseInt(cart[x].num);
                }
            } else {
                //商品总价
                price = 0;
            }
            var totalprice = (price / 100).toFixed(2);
            totalPriceEle.html(totalprice);
            priceEle.html(totalprice);
            var productEle = $(".dropdown-cart-product-list");
            productEle.html("");
            var limodel = $(".yw_addModule li");
            if ($.isEmptyObject(data)) {
                return;
            }
            var carts = data.cartProduct;
            //悬浮框上的总数
            var numEle = $(".yw_totalNum");
            //商品总数
            var number = 0;
            if (carts != null && carts.length > 0) {
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
                    var goodsProduct = cart.goodsProduct;
                    number += cart.num;
                    productId.html(cart.productId);
                    productName.html(goodsProduct.name);
                    productName.prop("href", "/front/detail?sku="+goodsProduct.sku);
                    attrId.html(goodsProduct.sku);
                    productPrice.html((cart.price / 100).toFixed(2));
                    productNum.html(cart.num);
                    productUrl.prop("href","/front/detail?sku="+goodsProduct.sku);
                    productImg.prop("src", cart.imgurl);
                    productImg.prop("data_sku", goodsProduct.sku);
                    productEle.append(newli);
                }
            }
            numEle.html(number);
          }
    })
}

$(".m-topcart-dropdown").click(function(){
    window.location.href ="/goods/cart";
})

$(document).on("click",".j_deletePro",function () {
    var proEle = $(this).parents(".j_productModel");
    var productId = proEle.find(".j_productId").html().trim();
    var attrId = proEle.find(".j_attrId").html().trim();
    $.ajax({
        "url": "/cartComp/deletePro.json",
        "data": {"productId": productId, "attrId": attrId},
        success: function (data) {
            data=eval("("+data+")");
            //如果删除成功
            var totalPriceEle = $(".yw_bottomTotalPrice");
            var priceEle = $(".yw_totalPrice");
            var numEle = $(".yw_totalNum");
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
})
$(document).on("click",".j_cartUrl",function () {
    var cartList = [];
    $(".dropdown-cart-product-list").find("li").each(function(index,obj){
        var cart ={};
        cart.sku =  $(obj).find(".j_attrId").html().trim();
        cartList.push(cart);
    });
    var info = JSON.stringify(cartList);
    window.location.href ="/goods/placeOrder?"+$.param({cartInfo:info}, true);
})