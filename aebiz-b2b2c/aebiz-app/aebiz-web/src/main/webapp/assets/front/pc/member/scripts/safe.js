
'use strict';

//订单中心轮播
jQuery(".picScroll-left").slide({titCell:".hd ul",mainCell:".bd ul",autoPage:true,effect:"left",autoPlay:true,vis:5,easing:"easeOutCirc"});

$(function(){
    //设置个人中心左侧 对应的菜单为选中状态
    $("#member_left li a[href|='/member/security/index']").parent("li").addClass("active").siblings("li").removeClass("active");

});