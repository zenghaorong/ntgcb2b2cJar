	 /*
      * Jquery—shopping   0.1
      * Copyright (c) 2013  Nicky Yan   个人网：站http://www.chinacoder.cn  QQ：525690001
      * Date: 2013-04-02
      * 使用Jquery—shopping可以很方便的实现加入购物车效果
      
      **根据原来的js改用为自己的js 2014年10月27日 16:00:56 -- by yb
	 */

;(function($){
	$.extend($.fn,{
		shoping:function(o){
			o= $.extend({
				cartbox:'.y_rtcart',//目标飞入对象
				cartnumber:'.y_rtcart .y_rcatsize',//显示购物车数量的元素
				imgflyclass:"y_flyimg",//飞入到购物车元素的类名
				position:'fixed',//飞入元素、+1元素的position值
				star_x:0,//飞入元素的起点x轴位置
				star_y:0,//飞入元素的起点y轴位置
				imgFlyTime:1000,//飞入对象飞到购物车时间
				imgDowTime:500,//飞入对象在购物车下沉时间
				numUpTime:1000//+1字符上移时间
			}, o || {});
				
			var self=this;
			var sch=$(window).scrollTop(),
					X = "97.5%",
					Y = $(o.cartbox).offset().top-sch;
					//添加飞入的商品
					var $obj = $(self).clone().addClass(o.imgflyclass);
					$obj.css({'left': o.star_x,'top': o.star_y,'position': o.position}).appendTo($('body')).animate({'left': X,'top': Y,'width':20,'height':20},o.imgFlyTime,function() {  //移动到购物车上方
						$obj.stop(false, false).animate({'top': Y+15,'opacity':0},o.imgDowTime,function(){  //移进到购物车里面并淡出
							$obj.remove();	  //移除飞入的元素
							var num=Number($(o.cartnumber).text());//把对象的值转化为数字
							$(o.cartnumber).text(num+1);  //购物车的数量+1
							var $flynum = $("<span class='y_flynum'>+1</span>");
							$flynum.css({'left': X,'top': Y+10,'position': o.position}).appendTo($('body')).animate({'top': Y-5,'opacity':0},o.numUpTime);
						});
					});
		}
	});	
})(jQuery);
