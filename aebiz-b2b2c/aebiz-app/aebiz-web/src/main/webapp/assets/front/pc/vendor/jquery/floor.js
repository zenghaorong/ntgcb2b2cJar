

//楼层
$(window).on('scroll', function() {
    var _self = this;
    var floorTop = $('.u-caption').eq(0).offset().top - 20;
  	console.log(floorTop);
    if ($(this).scrollTop() >= floorTop) {
        $('.u-fixedfloor').addClass('on');
        $.each($('.u-caption.floor'), function(index) {
            if ($(_self).scrollTop() >= $(this).offset().top) {
                $('.u-fixedfloor').find('li').eq(index).siblings().removeClass('active')
                $('.u-fixedfloor').find('li').eq(index).addClass('active')
            }
        })
    } else {
        $('.u-fixedfloor').removeClass('on');
    }
})

$('.J_floorjump').on('click',function(){
    var index = parseInt($(this).attr('data-fix'));
    if($('.u-caption').eq(index).length>0){
        var getTop = $('.u-caption').eq(index).offset().top;
        $("html,body").animate({
            'scrollTop':getTop
        },500);
    }

})