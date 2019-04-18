
'use strict';

jQuery(".picScroll-left").slide({titCell:".hd ul",mainCell:".bd ul",autoPage:true,effect:"left",autoPlay:true,vis:4,trigger:"click"});

//删除订单弹层
/*
$(document).on("click",".icon-delete1,.icon-shanchu",function(){
    layer.open({
        type: 1,
        content: $('#ly-delete'),
//	  offset: '200px',
        area: '560px',
        title: ['删除订单', 'font-size:18px;color:#ba9963;font-weight:600'],
        btn: ['确定', '取消'],
        shadeClose: true,
        btnAlign: 'c',
        scrollbar: false,
    });

})*/

/*
$(document).on("click",".getCoupon .coupons .button",function(){
    $(this).addClass("active");
})*/
