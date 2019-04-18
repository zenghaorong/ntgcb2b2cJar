//B2B2C商品详情页js
$(function(){
	//商品详情页店铺促销信息展示、隐藏
	$(".y_morepromo").click(function(){
		$(".y_shoppromx").slideDown();
	});
	$(".y_probtn").click(function(){
		$(".y_shoppromx").slideUp();
	})
	//详情页促销信息的展示、隐藏
	$(".product_ms .y_prolist .y_slideDn").click(function(){
		$(this).hide().next(".y_slideUp").show().nextAll(".y_salebx").slideDown();
	});
	$(".product_ms .y_prolist .y_slideUp").click(function(){
		$(this).hide().prev(".y_slideDn").show().nextAll(".y_salebx").slideUp();
	});
	//详情页选择地区弹出框
	$(".y_sendarea .y_areasure").click(function(){
		$(this).parent().toggleClass("y_aretive");
	});
	$(".y_arelose").click(function(){
		$(this).parents(".y_sendarea").removeClass("y_aretive");
	});
	//详情页选择商品属性
	$(".y_cosdata .dd .y_item:not(.disabled)").click(function(e){
		$(this).addClass("active").siblings().removeClass("active");
		e.preventDefault();
	});
	
	// 悬浮的商品详情页tab切换
	if($('.y_pdtabbox').length){
		var navContainer = $('.y_pdtabbox'),	
				navContainerTop = navContainer.offset().top-3;
		$(".y_producttab .tab_style2 li a").click(function(){
  	$(this).parent().addClass("active").siblings().removeClass("active");
  	$('body,html').animate({ scrollTop: navContainerTop }, 200);
  		return false;
	  });
	  $(".to_primise").click(function(){
	  	$(".y_evaluation").trigger("click");
	  	return false;
	  });
	  $(".y_pddetails").click(function(){
	  	$("#y_pddetails").addClass("active").siblings().removeClass("active");
	  });
	   $(".y_aftservice").click(function(){
	  	$("#y_aftservice").addClass("active").siblings().removeClass("active");
	  });
	  $(".y_evaluation").click(function(){
	  	$("#y_pddetails").removeClass("active");
			$("#y_prodsingle").removeClass("active");
			$("#y_record").removeClass("active");
			$("#y_suyuan").removeClass("active");
	  });
	   $(".y_prodsingle").click(function(){
	  	$("#y_prodsingle").addClass("active").siblings().removeClass("active");
	  });
	 
	  $(".y_record").click(function(){
	  	$("#y_record").addClass("active").siblings().removeClass("active");
	  });
	   $(".y_examannounce").click(function(){
	  	$("#y_examannounce").addClass("active").siblings().removeClass("active");
	  });
	   $(".y_suyuan").click(function(){
	  	$("#y_suyuan").addClass("active").siblings().removeClass("active");
	  });
	  
		$(window).on('scroll', function () {
		  if ($(window).scrollTop() >= navContainerTop && $(window).scrollTop() <= $(".footer.inner-footer").offset().top-100 ) {
		  	if(!navContainer.closest('.y_producttabfix').length) {
		  		navContainer.attr("class","y_producttabfix");
		  	}
		  } else {
		  	if(navContainer.closest('.y_producttabfix').length) {
		  		navContainer.attr("class","y_pdtabbox");
		  	}
		  }
		});
	}

	//低屏幕分辨率下的商户信息显示
	$(".y_stoslbtn").click(function(){
		if($(this).find("i").is(".fa-angle-left")){
			$(this).parent(".y_store_rt").animate({width:"243px"},500,function(){$(".y_store_rt .store_ms").show();});
		}else{
			$(".y_store_rt .store_ms").hide();
			$(this).parent(".y_store_rt").animate({width:"31px"},500);
		};
		$(this).find("i").toggleClass("fa-angle-left").toggleClass("fa-angle-right");
	});
	
	//详情页底部推荐商品 guess-loves owl-carousel效果的调用js
	if($('.guess-loves.owl-carousel').length && $.fn.owlCarousel){
		$('.guess-loves.owl-carousel').owlCarousel({
		   items:7,
		   itemsDesktop:[1409,5],
		   itemsDesktopSmall:[1209,4],
		   itemsTablet:[1023,3],
		   navigation: true,
		   navigationText: false,
		   pagination: false,
		   slideSpeed: 500
	  });
	};
	
	//已下架商品详情页-商品信息右侧-猜你喜欢商品分类
	if($('.underfram_product.owl-carousel').length && $.fn.owlCarousel){
		$('.underfram_product.owl-carousel').owlCarousel({
		  items: 5,
      itemsDesktop : [1409,4],
      itemsDesktopSmall: [1209,3],
      itemsTablet: [1023,1],
      navigationText : false,
      scrollPerPage: true,
      autoPlay: 5000,
      slideSpeed: 800,
      navigation: true
	  });
	};
	
	//商品不存在详情页-底部推荐商品
	if($('.container_product.owl-carousel').length && $.fn.owlCarousel){
		$('.container_product.owl-carousel').owlCarousel({
		  items: 7,
      itemsDesktop : [1409,6],
      itemsDesktopSmall: [1209,5],
      itemsTablet: [1023,4],
      navigationText : false,
      scrollPerPage: true,
      autoPlay: 5000,
      slideSpeed: 800,
      navigation: true
	  });
	};
  
  //商品详情页放大镜及相应的滚动、小图切换的js
	var	carouselContainer = $('#product-carousel'),
      productImg =  $('#product-image');
  if($.fn.elastislide) {
		carouselContainer.elastislide({
			orientation : 'horizontal',
			minItems : 4
		});
	}
	// Product page zoom plugin settings
	if ($.fn.elevateZoom) {
		$('#product-image').elevateZoom({
			responsive: false, // simple solution for zoom plugin down here // it can cause bugs at resize
			zoomType: 'inner', // you can use 'inner' or 'window' // change inner and go to product.html page and see zoom plugin differances
			borderColour: '#d0d0d0',
			cursor: "crosshair",
			borderSize: 2,
			lensSize : 180,
			lensOpacity: 1,
			lensColour: 'rgba(255, 255, 255, 0.25)'
		});

		$('#product-carousel').find('a').on('mouseover', function (e) {
			var ez = $('#product-image').data('elevateZoom'),
				smallImg = $(this).attr("data-image"),
				bigImg = $(this).attr("data-zoom-image");
				//smallImg = $(this).data('image'),
				//bigImg = $(this).data('zoom-image');
				$(this).parent().addClass("active").siblings().removeClass("active");
				ez.swaptheimage(smallImg, bigImg);
			e.preventDefault();
		});
	};
	//商品详情页放大镜及相应的滚动、小图切换的js end
	
	//商品套装详情页放大镜及相应的滚动、小图切换的js
	var	carouselContainer2 = $('#commodi-carousel'),
      productImg2 =  $('#commodi-image');
  if($.fn.elastislide) {
		carouselContainer2.elastislide({
			orientation : 'vertical',
			minItems : 3
		});
	}
	// Product page zoom plugin settings
	if ($.fn.elevateZoom) {
		$('#commodi-image').elevateZoom({
			responsive: false, // simple solution for zoom plugin down here // it can cause bugs at resize
			zoomType: 'inner', // you can use 'inner' or 'window' // change inner and go to product.html page and see zoom plugin differances
			borderColour: '#d0d0d0',
			cursor: "crosshair",
			borderSize: 2,
			lensSize : 180,
			lensOpacity: 1,
			lensColour: 'rgba(255, 255, 255, 0.25)'
		});

		$('#commodi-carousel').find('a').on('mouseover', function (e) {
			var ez = $('#commodi-image').data('elevateZoom'),
				smallImg = $(this).data('image'),
				bigImg = $(this).data('zoom-image');
				$(this).parent().addClass("active").siblings().removeClass("active");
				ez.swaptheimage(smallImg, bigImg);
			e.preventDefault();
		});
	};
	//商品套装详情页放大镜及相应的滚动、小图切换的js end
})