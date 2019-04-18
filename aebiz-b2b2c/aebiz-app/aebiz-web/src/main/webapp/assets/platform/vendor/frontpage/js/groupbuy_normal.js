//B2B2C团购-普通商品详情页js
$(function(){
	//全部商品分类的展示和隐藏   
	 $('.nav-tree').hover(function(){ 
	  $(this).find('.nav-tree-con').slideDown(100);  
	 },function(){
	 	$(this).find('.nav-tree-con').slideUp(100);  
	 })
	 
	//团购-普通商品详情页大小图的js
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
	//详情页选择商品属性
	$(".y_cosdata .dd .y_item:not(.disabled)").click(function(e){
		$(this).addClass("active").siblings().removeClass("active");
		e.preventDefault();
	});
	//商品加入收藏时调用收藏成功弹出框
	$(".y_collecpd").click(function(){
		$("#modal-collect-form").modal();
		return false;
	});
	//店铺加入收藏时调用收藏成功弹出框
	$(".collect_store").click(function(){
		$("#modal-store-form").modal();
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
		$(".y_tgbigpic img").shoping({
			star_x:this_x,//飞入元素的起点x轴位置
			star_y:this_y//飞入元素的起点y轴位置
		});
		event.preventDefault();;
	});
	
	//详情页评价回复
	$(".i_replybx .btn,.i_replibx .i_repbtn").click(function(){
		$(this).toggleClass("i_show").parent().next(".i_replyms").slideToggle();
		return false;
	});
	//详情页晒单列表查看更多晒单
	$(".i_single").each(function(){
		var singmore = $(this).find(".i_singst:gt(2)");
		singmore.hide();
		$(this).find(".i_sigmore .btn-more").click(function(){
			$(this).hide().next(".btn").show();
			singmore.show();
		});
		$(this).find(".i_sigmore .btn-less").click(function(){
			$(this).hide().prev(".btn").show();
			singmore.hide();
		});
	});
	// 悬浮的商品详情页tab切换 --- 团购普通商品详情页
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
		$('body,html').animate({ scrollTop: navContainerTop }, 200);
		return false;
	});
	 $(".to_primise").click(function(){
		$(".y_evaluation").trigger("click");
		return false;
	});
	$(".y_pddetails").click(function(){
		$("#y_pddetails").addClass("active").siblings().removeClass("active").siblings("#y_evaluation").addClass("active");
	});
	 $(".y_aftservice").click(function(){
		$("#y_aftservice").addClass("active").siblings().removeClass("active").siblings("#y_evaluation").addClass("active");
	});
	$(".y_evaluation").click(function(){
		$("#y_evaluation").addClass("active").siblings().removeClass("active");
	});
	 $(".y_prodsingle").click(function(){
		$("#y_prodsingle").addClass("active").siblings().removeClass("active");
	});

});