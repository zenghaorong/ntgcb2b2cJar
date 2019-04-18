
'use strict';

jQuery(".slideTxtBox").slide({effect:"fade",trigger:"click",easing:"easeOutCirc"});

$(document).ready(function(){
	$(".jqzoom").imagezoom();
	$("#thumblist li a").mouseover(function(){
		//增加点击的li的class:tb-selected，去掉其他的tb-selecte
		$(this).parents("li").addClass("tb-selected").siblings().removeClass("tb-selected");
		//赋值属性
		$(".jqzoom").attr('src',$(this).find("img").attr("mid"));
		$(".jqzoom").attr('rel',$(this).find("img").attr("big"));
	});
});

/*$(".collect").click(function(){
	$(this).find("i").toggleClass("icon-shoucang icon-active");
})*/



//查看大图
$(document).on("click",".m-comment-list .item-imgs .img",function(){
	var imgUrl = $(this).html();
	$(this).addClass("active").siblings().removeClass("active");
	$(this).parents("td").find(".big-img").html(imgUrl);
})
$(document).on("click",".m-comment-list .item-imgs .img.active",function(){
	$(this).removeClass("active");
	$(this).parents("td").find(".big-img").html("");
})


$(window).on('scroll', function() {
    var _self = this;
    var hdTop = $('.detail-evaluate').offset().top;
  	console.log(hdTop+"---"+$(this).scrollTop());
    if ($(this).scrollTop() >= hdTop) {
        $('.detail-evaluate .hd').addClass('on');
    } else {
        $('.detail-evaluate .hd').removeClass('on');
    }
})