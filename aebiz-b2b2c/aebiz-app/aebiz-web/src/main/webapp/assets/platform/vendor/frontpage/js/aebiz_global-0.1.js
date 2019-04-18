//B2B2C前台页面公用js

$(function(){
	//头部搜索切换、点击事件
	$(".y_serlist").hover(function(){
		$(this).addClass("y_hover");
	},function(){
		$(this).removeClass("y_hover");
	});
	$(".y_serlist ul li").click(function(){
		$(".y_serlist .y_serspan").text($(this).text());
		$(this).parents(".y_serlist").removeClass("y_hover");	
	});
	
	//头部购物车移上时显示购物车信息
	$('.y_topcart').hover(function(){
		$(this).addClass('open');
	},function(){
		$(this).removeClass('open');
	})

	
	//头部导航鼠标移上调用hoverIntent插件显示二级三级导航
	if ($.fn.hoverIntent) {
		$('ul.menu').hoverIntent({
			over: function() {
				$(this).addClass('active').children('ul, .mega-menu').fadeIn(200);
			},
			out: function() {
				$(this).removeClass('active').children('ul, .mega-menu').fadeOut(75);
			},
			selector: 'li',
			timeout: 145,
			interval: 55
		});
	}else{
		$('ul.menu li').hover(function(){
			$(this).addClass('active').children('ul, .mega-menu').fadeIn(200);
		},function(){
			$(this).removeClass('active').children('ul, .mega-menu').fadeOut(75);
		})
	};
	
	//登录弹出框用户名、密码验证成功，点击登录按钮改变按钮文字的事件
	$('#y_loadlogin').on('click', function () {
	  var $btn = $(this).button('loading'); //loading
	  //$btn.button('reset')  重置
	});
	
	//返回顶部js
	$(".y_ifixed .y_totop").click(function(){
		$("html,body").animate({scrollTop:'0px'},300);
	});
	
	//监听页面宽度，右侧边栏的帐号、收藏、调查问卷的显示、隐藏
	var rtfixshow = false,
			rtboxshow = false;
	rtcartshow();
	$(window).resize(function(){
		if(!$(".y_rtfixbx").hasClass("y_showrt")){
			rtcartshow();
		}
	});
	function rtcartshow(){
		var boxwidth=$(".container").width(),
		winwidth=$(window).width();
		if((winwidth/2-boxwidth/2)<1000){
			if(!$(".y_ifixed").hasClass("y_rtcthide")){
				$(".y_ifixed").addClass("y_rtcthide");
			};
			rtfixshow = false;
			/*
			if(!$(".y_rtfixbx").hasClass("y_showrt")){
				$(".y_ifixed").bind("mouseenter mouseleave", function(e) { 
					var w = $(this).width(); 
					var h = $(this).height(); 
					var x = (e.pageX - this.offsetLeft - (w / 2)) * (w > h ? (h / w) : 1); 
					var y = (e.pageY - this.offsetTop - (h / 2)) * (h > w ? (w / h) : 1); 
					var direction = Math.round((((Math.atan2(y, x) * (180 / Math.PI)) + 180) / 90) + 3) % 4; 
					var eventType = e.type; 
					if(e.type == 'mouseleave'/ && direction==2/){  
						if(!$(this).hasClass("y_rtcthide")){
							$(this).addClass("y_rtcthide");
						}
					}else{ 
						if($(this).hasClass("y_rtcthide")){
							$(".y_ifixed").removeClass("y_rtcthide");
						}
					} 
				});
			}
			*/
		}else{
			$(".y_ifixed").removeClass("y_rtcthide");
			rtfixshow = true;
		};
	};
	
	//右侧购物车、收藏信息的显示事件
	function y_rtctlopen(){
		$(".y_rtfixbx").addClass("y_showrt");
		$(".y_ifixed").removeClass("y_rtcthide");
		//右侧购物车或收藏显示时，点击右侧区域以外的地方可以关闭
		$(document).one("click", function () {
	  	y_rtctloclose();
	  });
		rtboxshow = true;
	};
	$(".y_rtfixbx").click(function (event) {
		event.stopPropagation();
	});
	//右侧购物车、收藏信息的关闭事件
	function y_rtctloclose(){
		$(".y_rtcart,.y_rtlove").removeClass("y_xzcur");
		$(".y_rtfixbx").removeClass("y_showrt");
		$(".y_rtctbx,.y_rtlovebx").removeClass("y_rtmsshow");
		$(".y_rtfixbx").animate({width:"35px"},300);
		rtcartshow();
		rtboxshow = false;
	};
	//右侧导航登录后 点击购物车弹出、关闭购物车信息
	$(".y_rtfixbx").on("click",".y_rtcart",function(){
		if(!$(".y_rtctbx").is(".y_rtmsshow")){
			y_rtctlopen();
			$(this).addClass("y_xzcur");
			$(".y_rtlove").removeClass("y_xzcur");
			$(".y_rtctbx").addClass("y_rtmsshow");
			$(".y_rtlovebx").removeClass("y_rtmsshow");
			$(".y_rtfixbx").animate({width:"310px"},300);
			
		}else{
			y_rtctloclose();
		};
	});
	//右侧导航登录后 点击我的收藏弹出、关闭收藏信息
	$(".y_rtfixbx").on("click",".y_haslogoin .y_rtlove",function(){
		if(!$(".y_rtlovebx").is(".y_rtmsshow")){
			y_rtctlopen();
			$(this).addClass("y_xzcur");
			$(".y_rtcart").removeClass("y_xzcur");
			$(".y_rtlovebx").addClass("y_rtmsshow");
			$(".y_rtctbx").removeClass("y_rtmsshow");
			$(".y_rtfixbx").animate({width:"270px"},300);
		}else{
			y_rtctloclose();
		};
	});
	//右侧购物车或收藏显示时，关闭按钮的事件
	$(".y_closertms").click(function(){
		y_rtctloclose();
	});
	
	//右侧导航点击显示登录弹出框
	$(".y_rtfixbx").on("click",".y_nologin .y_userbtn",function(){
		$(".y_rtdlbox").removeClass("y_botshow").css({"display":"block","top":"34px","bottom":"auto"})
	});
	$(".y_nologin .y_userbtn,.y_nologin .y_rtcart,.y_nologin .y_rtlove").mouseleave(function(){
		$(".y_rtdlbox").hide();
		$(".y_rtdlbox").hover(function(){
			$(this).show();
		},function(){
			$(this).hide();
		});	
	});
	//右侧导航未登录时 点击购物车和我的收藏弹出登录框
	/*
	$(".y_rtfixbx").on("click",".y_nologin .y_rtcart",function(){
		$(".y_rtdlbox").removeClass("y_botshow").css({"display":"block","top":"114px","bottom":"auto"})
	});
	*/
	$(".y_rtfixbx").on("click",".y_nologin .y_rtlove",function(){
		$(".y_rtdlbox").addClass("y_botshow").css({"display":"block","top":"auto","bottom":"70px"})
	});
	//点击关闭右侧登陆弹出框的事件
	$(".y_closedlbx").click(function(){
		$(this).parents(".y_rtdlbox").hide();	
	});
	//右侧悬浮侧边栏鼠标移上时显示、隐藏部分模块
	$(".y_ifixed").hover(function(){
		if(rtfixshow == false){
			$(".y_ifixed").removeClass("y_rtcthide");
		};
	},function(){
		if(rtfixshow == false && rtboxshow == false){
			$(".y_ifixed").addClass("y_rtcthide");
		};
	});
	
	//除首页外其他页面鼠标移上全部商品分类时展示分类树
   $('.y_produall').hover(function(){
   	 $(this).find('.y_prudaltc').show();
 	 },function(){
 		$(this).find('.y_prudaltc').hide(); 
 	 });
 	 
 	 // Bootstrap tooltip
 	 if($.fn.tooltip) {
	 	$('.add-tooltip,[rel=tooltip]').tooltip();
	 };
	 
	 // Bootstrap popover
	 if($.fn.popover) {
		 $('.add-popover,[rel=popover]').popover();
	 };
	 
	//根据.progress-animate的data-width属性动态设置元素的宽度，如果页面引入了jquery.appear.js则滚动到元素所在位置才设置否则默认设置
	if ($.fn.appear) {
		$('.progress-animate').appear();
		$('.progress-animate').on('appear', function () {
			var $this = $(this),
				progressVal = $(this).data('width'),
				progressText = $this.find('.progress-text');
			$this.css({ 'width' : progressVal + '%'}, 400);
			progressText.fadeIn(500);
		});
	
	} else {
		$('.progress-animate').each(function () {
			var $this = $(this),
				progressVal = $(this).data('width'),
				progressText = $this.find('.progress-text');
			$this.css({ 'width' : progressVal + '%'}, 400);
			progressText.fadeIn(500);
		});
	}
	 
	 //收藏商品成功弹出框内的商品滚动
	 if($(".y_collpdowl").length > 0){
		 $('.y_collpdowl.owl-carousel').owlCarousel({
		    items: 3,
		    itemsDesktop : false,
		    itemsDesktopSmall: false,
		    itemsTablet: false,
		    pagination: false,
		    navigationText : false,
		    scrollPerPage: true,
		    autoPlay: 5000,
		    slideSpeed: 800,
		    navigation: true
		 });
	 };
	 
	 //调用css3 animation动画的js wow.js的事件（页面滚动到元素所在位置时执行css3动画）
	 if (typeof WOW === 'function') {
			new WOW({
				boxClass:     'wow',      // default
				animateClass: 'animated', // default
				offset:       0          // default
			}).init();
		};
		
		//调用放大图prettyPhoto插件的js,详情页放大图的小图和晒单用到了
		if ($.fn.prettyPhoto) {
			$("a[data-rel^='prettyPhoto']").prettyPhoto({
				hook: 'data-rel',
        animation_speed: 'fast',
        slideshow: 6000,
        autoplay_slideshow: true,
        show_title: false,
        deeplinking: false,
        social_tools: '',
        overlay_gallery: true,
				theme: 'light_square'
			});
		};
		
});
