//B2B2C团购-本地生活商品详情页js
$(function(){
	 //全部商品分类的展示和隐藏   
	 $('.nav-tree').hover(function(){ 
	  $(this).find('.nav-tree-con').slideDown(100);  
	 },function(){
	 	$(this).find('.nav-tree-con').slideUp(100);  
	 })
	
	//团购-本地生活商品详情页大小图的js
	$('#carousel').flexslider({
    animation: "slide",
    controlNav: false,
    directionNav: false,
    animationLoop: false,
    slideshow: false,
    itemWidth: 88,
    asNavFor: '#slider'
  });
  $('#slider').flexslider({
    animation: "slide",
    controlNav: false,
    animationLoop: false,
    slideshow: false,
    sync: "#carousel"
  });
	//商品加入收藏时调用收藏成功弹出框
	$(".y_collecpd").click(function(){
		$("#modal-collect-form").modal();
		return false;
	});
	//团购详情页浏览历史滚动到对应位置时悬浮
	var y_tghistory = $(".y_tgbrowhistory");
	y_tghistory.affix({
		offset: {
			top: function() {
				return this.top = y_tghistory.offset().top
			},
			bottom: function() {
				return this.bottom = $(".footer").outerHeight(true)+30
			}
		}
	});
	//加入购物车飞入购物车效果的调用
	$('.y_gobuybtn .btn-cart,.y_tgtbbtn .btn-cart').click(function(event){
		var this_x = $(this).offset().left + $(this).width()/2,
				this_y = $(this).offset().top- $(window).scrollTop() - 50;
		$(".y_lifebigpic .flex-active-slide img").shoping({
			star_x:this_x,//飞入元素的起点x轴位置
			star_y:this_y//飞入元素的起点y轴位置
		});
		event.preventDefault();;
	});
	// 悬浮的商品详情页tab切换 --- 团购本地生活商品详情页
	var navContainer = $('.y_tgmaintan'),
	navContainerTop = navContainer.offset().top-4;
	navContainer.affix({
		offset: {
			top: navContainerTop,
			bottom: function() {
				return this.bottom = $(".footer").outerHeight(true)+30
			}
		}
	});
	$(".y_tgmaintan .tab_style2 li a").click(function(){
		$(this).parent().addClass("active").siblings().removeClass("active");
		return false;
	});
	var dettop = $("#y_lifedet").offset().top-60;
	var storetop = $("#y_lifestore").offset().top-60;
	$(".y_lifedetbtn").click(function(){
		$('body,html').animate({ scrollTop: dettop }, 200);
	});
	$(".y_lifestorebtn").click(function(){
		$('body,html').animate({ scrollTop: storetop }, 200);
	});
	
});