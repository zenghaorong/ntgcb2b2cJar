
//======团购系统公共js======

	//头部可以隐藏的焦点图
	 $(function(){
			$('.top-banner-flex.flexslider').flexslider({
		    animation: "slide",
				animationLoop: true,
				pauseOnAction: false,
				pauseOnHover: true,
				slideshowSpeed: 4000,
				directionNav: false
		  });	
		  $('.wrapper .close').click(function(){
		   $(this).parents('.wrapper').slideUp(300);
		  })
	  });

//======团购系统-生活团首页====

  //本周精选图片切换
  $(function(){
		$('.choice-flex.flexslider').flexslider({
	    animation: "slide",
			animationLoop: true,
			pauseOnAction: false,
			pauseOnHover: true,
			slideshowSpeed: 4000,
			directionNav: false
	  });	  
  });
  
  //右侧特别推出的团购
  $(function(){
		$('.recom-flex.flexslider').flexslider({
	    animation: "slide",
			animationLoop: true,
			pauseOnAction: false,
			pauseOnHover: true,
			slideshowSpeed: 4000,
			directionNav: false
	  });	  
  });
  
  //右侧专题推荐
  $(function(){
		$('.subject-flex.flexslider').flexslider({
	    animation: "slide",
			animationLoop: true,
			pauseOnAction: false,
			pauseOnHover: true,
			slideshowSpeed: 4000,
			directionNav: false
	  });	  
  });
  
  //二级菜单选择
   $('.tgnav-goods .item').hover(function(){
   	  $(this).find('.item-categ').show();
   	},function(){
   	  $(this).find('.item-categ').hide();
   	});
      
  //选择城市弹出框效果
  $(function(){
  	$('.select-city .input-group').click(function(){
  	  $(this).parents('.select-city').find('.select-city-box').slideDown(300);	  
  	})
    $('.select-city-box .btn').click(function(){
     $(this).parents('.select-city-box').slideUp(300);	
    })
  }) 
	  
	//抢团购owl-carousel效果
	 $(function(){
	    $('.list-group.owl-carousel').owlCarousel({
	     items:5,
	     itemsDesktop:[1409,4],
	     itemsDesktopSmall:[1209,4],
	     itemsTablet:[1023,4],
	     navigation: true,
	     navigationText: false,
	     pagination: false,
	     slideSpeed: 500
	    })
	 });
	 
	 //生活团logo轮播
	 $(function(){
	    $('.logo-list.owl-carousel').owlCarousel({
	     items:6,
	     itemsDesktop:[1409,5],
	     itemsDesktopSmall:[1209,5],
	     itemsTablet:[1023,4],
	     navigation: true,
	     navigationText: false,
	     pagination: false,
	     slideSpeed: 500
	    })
	 });
	 	
	 //左侧楼层悬浮特效
		$('body').scrollspy({ target: '.fixedLeft.life-fixed' })
		$('[data-spy="scroll"]').each(function () {
		  var $spy = $(this).scrollspy('refresh')	  
		})  
		
		$(window).scroll(function(){
		  if($(window).scrollTop() >= 1000){
	   		$('.fixedLeft.life-fixed').addClass('fixedLeft-cur');
		  }else{
				$('.fixedLeft.life-fixed').removeClass('fixedLeft-cur');
		  };	
		})
		
	 //左侧楼层悬浮特效工具提示
	 $('.fixedLeft li').tooltip();		
	
	 //左侧楼层悬浮特效平滑移动的效果
	 $(".fixedLeft.life-fixed .nav li").click(function(){
	 	$(this).addClass('active').siblings().removeClass('active');
	 	var list=$(this).find('a').attr("href");
	  var ltop=$(list).offset().top;
		  $("html,body").animate({scrollTop:ltop},300);   
	 });

//======团购系统-商品类首页====

   //焦点图banner切换
	 $(function(){
			$('.foucs-banner-flex.flexslider').flexslider({
		    animation: "slide",
				animationLoop: true,
				pauseOnAction: false,
				pauseOnHover: true,
				slideshowSpeed: 4000,
				directionNav: false
		  });	
		  $('.wrapper .close').click(function(){
		   $(this).parents('.wrapper').slideUp(300);
		  })
	  });
  
  //推荐商品owl carousel 效果调用
   $(function(){
    $('.list-group.owl-carousel').owlCarousel({
     navigation: true,
     navigationText: false,
     pagination: false,
     slideSpeed: 500
    })
   });
  
  //左侧楼层悬浮特效
	$('body').scrollspy({ target: '.fixedLeft' })
	$('[data-spy="scroll"]').each(function () {
	  var $spy = $(this).scrollspy('refresh')	  
	})  
	
	$(window).scroll(function(){
		
	  if($(window).scrollTop() >= 500){
   		$('.fixedLeft').addClass('fixedLeft-cur');
	  }else{
			$('.fixedLeft').removeClass('fixedLeft-cur');
	  };	
	  
	})
	
	//左侧楼层悬浮特效工具提示
	$('.fixedLeft li').tooltip();
 
  //左侧楼层悬浮特效平滑移动的效果
	 $(".fixedLeft .nav li").click(function(){
	 	$(this).addClass('active').siblings().removeClass('active'); 
	 	var list=$(this).find('a').attr("href");
	  var ltop=$(list).offset().top;
		  $("html,body").animate({scrollTop:ltop},300); 
	 });
	 
//======团购系统-列表页====
  
	// 筛选条件 点击出现更多条件
  $('.filter-con .more-box').click(function(){
    $(this).find('.fa').toggleClass('fa-caret-down').toggleClass('fa-caret-up');
    $(this).parents('.filter-con').find('.filter-list').toggleClass('over-hidden');
  })

	// 列表页 排序 选择复选框效果
	$('.sort-wrapper .check-box').click(function(){
	    $(this).find('.fa').toggleClass('fa-square-o').toggleClass('fa-check-square-o');
		  $(this).toggleClass('active');
	})
	$('.main-sort-container .check-box').click(function(){
	    $(this).find('.fa').toggleClass('fa-square-o').toggleClass('fa-check-square-o');
		$(this).toggleClass('active');
	})
  
  // 列表页分类切换效果
	 $('.nav > .categ-tit').click(function(){
	  $(this).addClass('active').siblings().removeClass('active');
	 });
   
  $('.filter-con .categ-tit').click(function(){
   $(this).addClass('active').siblings().removeClass('active');
  });
  
  $('.filter-con .categ-tit').click(function(){
   $(this).addClass('active').siblings().removeClass('active');
   $(this).parent('.filter-list').prev('.categ-all').removeClass('active');  
  })
   
  $('.filter-con .categ-all').click(function(){
    $(this).next('.filter-list').find('.categ-tit').removeClass('active');
  }) 
   
  // 列表页排序的切换效果
  $('.sort-left a').click(function(){
   $(this).addClass('active').siblings().removeClass('active');
  })
  
  //全部商品分类的展示和隐藏   
   $('.nav-tree.nav-tree-hide').hover(function(){ 
    $(this).find('.nav-tree-con').slideDown(100);  
   },function(){
   	$(this).find('.nav-tree-con').slideUp(100);  
   })


	