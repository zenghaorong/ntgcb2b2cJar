//B2B2C前台首页js
$(function() {
  // 悬浮的搜索框
	$(window).on('scroll', function () {
		stickyMenu();
	});
	function stickyMenu() {
		var windowTop = $(window).scrollTop(),
	  navContainer = $('.main-serch-container'),
	  navContainerHeight = navContainer.height();
	     	
	  if (windowTop >= 180) {
	  	if(!navContainer.closest('.fixed-wrapper').length) {
	  		navContainer.wrap(function() {
	  			return '<div class="fixed-wrapper" style="height:'+ navContainerHeight +'px"></div>';
	  		});
	  	}
	  	if(!navContainer.hasClass("fixed")){
	      navContainer.addClass('fixed');
	    }
	  } else {
	  	navContainer.removeClass('fixed');
	  }
	};
	
	//首页点加入收藏时调用登录弹出框
	$(".y_inflor1 .y_lorbtn").click(function(){//测试时1F调用登录弹出框，正式开发不需要.y_inflor1
		$("#modal-login-form").modal();
		return false;
	});
	//首页点加入收藏时调用收藏成功弹出框
	$(".y_inflor2 .y_lorbtn").click(function(){//测试时2F调用加入收藏弹出框，正式开发不需要.y_inflor2
		$("#modal-collect-form").modal();
		return false;
	});
	
	//监听页面宽度，设置左侧边栏显示、隐藏
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
	//首页左侧悬浮楼层特效
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
  	var list=$(this).attr("data-fix");
  	var ltop=$(".y_inflor"+list).offset().top-55;
  	$("html,body").animate({scrollTop:ltop},300);
  });
});