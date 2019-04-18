
  // 列表页 预商品分类 小图标变化
	 $('.category-title .icon-style').click(function(){
			$(this).find('.fa').toggleClass('fa-minus-square').toggleClass('fa-plus-square');
			$(this).toggleClass('active');
			$(this).parent('.category-title').toggleClass('active');
		})
	 $('.category-title').click(function(){	
	 	
		  var collpanseIn = $(this).next('.panel-body')
			if(collpanseIn.hasClass('in')){
			 $(this).addClass('active');
			}else{
			 $(this).removeClass('active');
			 $(this).find('.icon-style').removeClass('active');
			 $(this).find('.fa').removeClass('active');
			}
			
		  $(this).toggleClass('active');
			$(this).find('.fa').toggleClass('fa-minus-square').toggleClass('fa-plus-square');
			$(this).find('.fa').toggleClass('active');
		})
		
	//商品预分类调用美化滚动条的js预商品的列表超过一定高度的时候出现美化的滚动条 
		var catFilter = $('.category-filter-list.jscrollpane'),  // 预设调用类名
			checkForScrollbar = function(a) {     //检查核对美化的滚动条的方法
				var catHeight = a.height();
				if ( catHeight > 300 ) {
					a.css('height', 300);
					a.jScrollPane({
						showArrows: false     //jScrollPane方法的参数
					});
				}
			};
		
		// 超过一定高度的文档调用插件
		$.each(catFilter, function () {  //为每个匹配的 category-filter-list.jscrollpane 运行一个函数
			var $this = $(this);
			checkForScrollbar($this);  // 运行美化的滚动的小条的方法
		});
			
		// 在collapse效果呈现展开的状态的时候，调用美化的滚动条
		$('#category-filter').find('.collapse').on('shown.bs.collapse', function() {     // 绑定collapse效果打开的事件
			var cFilter = $(this).find('.category-filter-list.jscrollpane');  // 定义变量参数
			checkForScrollbar(cFilter); // 运行此方法。
		});
		
	 //	搜索为空浏览记录中的 美化滚动条效果调用	
	  var catFilter = $('.record-content.jscrollpane'),  // 预设调用类名
			checkForScrollbar = function(a) {     //检查核对美化的滚动条的方法
				var catHeight = a.height();
				if ( catHeight > 360 ) {
					a.css('height', 360 );
					a.jScrollPane({
						showArrows: false     //jScrollPane方法的参数
					});
				}
			};
	 // 超过一定高度的文档调用插件
		$.each(catFilter, function () {  //为每个匹配的 category-filter-list.jscrollpane 运行一个函数
			var $this = $(this);
			checkForScrollbar($this);  // 运行美化的滚动的小条的方法
		});
		
   //多选条件的选择效果
		$('.condition-more > .more-select').click( function(){
			$(this).parent('.condition-more').slideUp(300);
			$(this).parent('.condition-more').prev('.condition-cont').slideUp(300);
			$(this).parent('.condition-more').next('.more-select-container').slideDown(300);
			$(this).parents('.condition').find('.more-container').slideUp(300);
			$(this).parent('.condition-more').prev('.more-container2').find('.current-cont').slideUp(300);		
		})
		$('.condition-submit > .cancel').click(function(){
			$(this).parents('.more-select-container').slideUp(300);
			$(this).parents('.condition').find('.condition-cont').slideDown(300);
			$(this).parents('.condition').find('.condition-more').slideDown(300);
			$(this).parents('.condition').find('.condition-more').find('.down').show();
			$(this).parents('.condition').find('.condition-more').find('.up').hide();
			$(this).parents('.condition').find('.current-cont').slideDown(300);
		})
		
	 //选择条件后相应的相应的字体颜色的变化和选中后确定按钮颜色的修改
		 $('.condition-name-in input').focus(function(){
			 $(this).parent('.condition-name-in').toggleClass('active'); 
			 $(this).parents('.show-more').next('.condition-submit').find('.btn-confirm').removeClass('disabled'); 
			})
		
	//更多条件的选择效果
	$('.condition-more .down').click( function(){
		$(this).hide();
		$(this).next('.up').show();
		$(this).next('.up').addClass('active');
		$(this).parents('.condition').find('.condition-cont-brand').slideUp(300);
		$(this).parents('.condition').find('.more-container').slideDown(300);
		$(this).parents('.condition').find('.more-container2').find('.more-cont').slideDown(300);
	})
	$('.condition-more .up').click( function(){
		$(this).hide();
		$(this).prev('.down').show();
		$(this).parents('.condition-more').prev('.condition-cont-brand').slideDown(300);
		$(this).parents('.condition').find('.more-container').slideUp(300);
		$(this).parents('.condition').find('.more-container2').find('.more-cont').slideUp(300);
	})
	
	// 触碰(hover)字母时对应盒子的出现！
	$('.more-select-title .letter').hover(function(){
	 var letterValue = $(this).attr('data-target');
	 $(letterValue).addClass('active').siblings().removeClass('active');
	},function(){});
		
	// 底部查看更多的展开和收起效果
	$(".filterDown").click(function(){
		$(this).hide();
		$(this).next('.filterUp').show();
		$('.count-last').slideDown(300);
	})
	$(".filterUp").click(function(){
		$(this).hide();
		$(this).prev('.filterDown').show();
		$('.count-last').slideUp(300);
	})	
			   
	// 列表页 价格筛选滑动器方法
	function priceSlider(){
	  var startPrice=$("#startPrice").val();  //获取开始价格的值
		var endPrice=$("#endPrice").val();   //获取结束价格的值
		if ($.fn.noUiSlider) {
			if($('#price-range').length > 0){
				$('#price-range').noUiSlider({
					range: [startPrice, endPrice],  // 插件参数 范围和幅度
					start: [startPrice, endPrice],  // 插件参数 开始值和结束值
					handles: 2,          // 插件参数 
					connect: true,       // 插件参数 已经滑动范围的颜色
					step: 1,             // 插件参数 滑动时每步变化的幅度 
					serialization: {     // 插件参数 从高到低还是从低到高
						to: [ $('#price-range-low'), $('#price-range-high') ],
						resolution: 1   //分辨率
					}
				});
			}	
		}
	}
	// 调用此方方法
	priceSlider();			 
 
  // 排序栏箭头上下的切换
	 $('.sort-left a').click(function(){	     	
	 	 $(this).find('.fa').toggleClass('fa-long-arrow-down').toggleClass('fa-long-arrow-up');	     		
	 })
 
  //列表页商品大图展示效果  
  itemAnimationIn = function(){  // 定义触碰商品时的方法    
   var $this = $(this),   //当前项目的指向对象
			itemText = $this.find('.icon-cart-text'),  // 定义 加入购物车的文本
			itemWidth = $this.width(),   //定义当前项目的宽度
			ratingAmount = $this.find('.ratings-amount'), // 定义隐藏的商品评价
			moreActionBtns = $this.find('.item-action-inner'); //定义隐藏的按钮

		if (itemWidth < 220) { // 当项目的宽度小于220px的情况下
			itemText.animate({width: 'hide'},100, function() {   //放入购物车的文字动画宽度的在100秒内 属性目标值是隐藏
				$(this).closest('.item-add-btn').addClass('icon-cart');  // 动画执行完成后执行的一个函数加入购物车的图标形式出现
			});
		}

		ratingAmount.animate({width : 'show'}, 300); // 商品宽度展示出来
		moreActionBtns.css({'visibility': 'visible', 'overflow': 'hidden'}).animate({width:80}, 300); // 隐藏按钮展现 （css()多个属性及属性值展现的方法）
  }
  itemAnimationOut = function(){    // 定义离开商品时的方法  
   var $this = $(this),  // 自定义变量
			itemText = $this.find('.icon-cart-text'),
			itemWidth = $this.width(),
			ratingAmount = $this.find('.ratings-amount'),
			moreActionBtns = $this.find('.item-action-inner');


		if (itemWidth < 220) {
			// be careful about this duration //请注意时间的变化
			// make sure that it is the same as below's
			itemText.animate({width: 'show'},300).closest('.item-add-btn').removeClass('icon-cart');
		}

		ratingAmount.animate({width : 'hide'}, 300);
		moreActionBtns.animate({width:0}, 300, function() {
			/* For better animation delete padding after animation's done*/
			$(this).css('padding-left', 0); //动画隐藏之后执行函数 
		}).css({'visibility': 'hidden', 'overflow': 'hidden'});  //.item-action-inner 按钮隐藏
  }  
  // 使用使用点击延时插件调用以上两种方法（使用hoverIntent插件是为了更好的展示动画效果）
  var self = this;	
 	if ($.fn.hoverIntent) {
		$('.item-hover').hoverIntent(self.itemAnimationIn, self.itemAnimationOut); // hoverIntent插件的调用
	 } else {
		$('.item-hover').on('mouseover', self.itemAnimationIn).on('mouseleave', self.itemAnimationOut); // on()绑定事件 
	 }; 

	//列表页 点击对比时的弹出框
	$('.item-action-inner .icon-button').click(function(){
	 $('.compare-glanceover').show();
	})
	//列表页 点击眼睛查看大图的弹出框
	$('.eyes-btn').click( function(){
		$("#eyes-modal").modal();
		return false;
	} )
	
	// 列表页 排序 选择复选框效果
	$('.sifting-sort .check-box').click(function(){
	  $(this).find('.fa').toggleClass('fa-square-o').toggleClass('fa-check-square-o');
		$(this).toggleClass('active');
	})
	$('.main-sort-container .check-box').click(function(){
	    $(this).find('.fa').toggleClass('fa-square-o').toggleClass('fa-check-square-o');
		$(this).toggleClass('active');
	})
	// 列表页 顶部出现排序悬浮框
	 $(window).on('scroll', function(){
	   topSort();
	 });
	 function topSort() {
		var windowTop = $(window).scrollTop();
		    topSortCon = $('.main-sort-container > .container');
				appendHtml = topSortCon.find('sort-box');
				appendSort = $('<div class="sort"></div>');
				appendSiftingSort = $('<div class="sifting-sort"></div>')
				topSortConHeight = topSortCon.height();
				if(windowTop >= 840){
					$('.sort-fixed-wrapper').css("top","0px");
					}else{
					$('.sort-fixed-wrapper').css("top","-73px");
				}
       			
	 }
	 
	// 列表页大图列表商品飞入购物车的效果
	$('#products').on("click",".recommend-items-slider .item-add-btn",function(){
		var this_x = $(this).offset().left + $(this).width()/2,
				this_y = $(this).offset().top- $(window).scrollTop() - 50;
		$(this).parents(".item-meta-container").prev(".item-image-wrapper").find(".item-image").shoping({
			star_x:this_x,//飞入元素的起点x轴位置
			star_y:this_y//飞入元素的起点y轴位置
		});
	});

	// 列表页小图列表商品飞入购物车的效果
	$('#products').on("click",".recommend-items-list .item-add-btn",function(){
		var this_x = $(this).offset().left + $(this).width()/2,
				this_y = $(this).offset().top- $(window).scrollTop() - 50;
		$(this).closest(".item").find(".item-image-box .item-image").shoping({
			star_x:this_x,//飞入元素的起点x轴位置
			star_y:this_y//飞入元素的起点y轴位置
		});
	});

	//列表页底部商品精选加入购物车按钮调用飞入购物车事件
	$('.featured .cart-coin').click(function(){
		var this_x = $(this).offset().left + $(this).width()/2,
				this_y = $(this).offset().top- $(window).scrollTop() - 50;
		$(this).parents(".featured-image-container").find(".item-image").shoping({
			star_x:this_x,//飞入元素的起点x轴位置
			star_y:this_y//飞入元素的起点y轴位置
		});
	});
	
	//列表页点击眼睛快速购买加入购物车按钮调用飞入购物车事件
	$('.eyes-modal .btn-custom2').click(function(){
		var this_x = $(this).offset().left + $(this).width()/2,
				this_y = $(this).offset().top- $(window).scrollTop() - 50;
		$(".product_bigpic img").shoping({
			star_x:this_x,//飞入元素的起点x轴位置
			star_y:this_y//飞入元素的起点y轴位置
		});
	});
	
  // 列表页店铺搜索  store-goods.owl-carouse 效果的调用
   $(function(){
    $('.store-goods.owl-carousel').owlCarousel({
     items:5,
     itemsDesktop:[1409,4],
     itemsDesktopSmall:[1209,4],
     itemsTablet:[1023,3],
     navigation: true,
     navigationText: false,
     pagination: false,
     slideSpeed: 500
    })
   }); 
   
   //根据浏览记录推荐的商品owl carousel效果调用
   $(function(){
    $('.recommend-goods.owl-carousel').owlCarousel({
     items:5,
     itemsDesktop:[1409,4],
     itemsDesktopSmall:[1209,3],
     itemsTablet:[1023,3],
     navigation: true,
     navigationText: false,
     pagination: false,
     slideSpeed: 500
    })
   }); 
   
 //搜索时相关搜索商品.relate-seacher.owl-carousel效果调用
   $(function(){
    $('.relate-seacher.owl-carousel').owlCarousel({
     items:5,
     itemsDesktop:[1409,4],
     itemsDesktopSmall:[1209,3],
     itemsTablet:[1023,3],
     navigation: true,
     navigationText: false,
     pagination: false,
     slideSpeed: 500
    })
   }); 
   
  // 图片懒加载效果	
  $(function(){
		$(".lazyimg").lazyload({effect:"fadeIn"});
  });
	
	// 列表页小图展示 对比图标选择效果
	$('.item-action-box .icon-button').click(function(){
  	$(this).toggleClass('active');
    $(this).find('.icon-style').find('.fa').toggleClass('fa-square-o').toggleClass('fa-check-square-o');
  })
	
	 //点击主题标题 相应盒子关闭效果
	  $('.tr-click').click(function(){
	   $(this).parents('.tab-1').find('tbody').slideToggle(300);
	   $(this).find('.fa').toggleClass('fa-minus').toggleClass('fa-plus');
	  })
	  
	 //滚动条到达一定高度，浮出对比的商品信息
		$(window).on('scroll',function(){
			 toptabCon()
			});
		function toptabCon(){
		 var windowTop = $(window).scrollTop();
		     if(windowTop > 620){
		     	$('.fix-box').css('top','0px');
		     	}else{
		     	$('.fix-box').css('top','-120px');	
		     	}
		}
	
	//精选商品owl carousel 效果调用
   $(function(){
    $('.goods-featured.owl-carousel').owlCarousel({
     items:5,
     itemsDesktop:[1409,5],
     itemsDesktopSmall:[1209,4],
     itemsTablet:[1023,4],
     navigation: true,
     navigationText: false,
     pagination: false,
     slideSpeed: 500
    })
   });  
     
  //列表页左侧效果的添加
	  $(function(){
	    $('.featured-slider.flexslider').flexslider({
        animation: "slide",
				selector: ".featured-list > li",
				controlNav: false,// false
				prevText: '',
				nextText: '',
				smoothHeight: true,
				slideshowSpeed: 7000
	    })
	   }); 
    	