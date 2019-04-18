'use strict';
$("[data-content]").eq(0).show();
$(document).on("click",".center-tab li",function(){
    $("[data-content]").hide().eq(num).fadeIn();
})


//取消收藏弹层
$(document).on("click",".icon-delete,.cancel",function(){
    layer.open({
        type: 1,
        content: $('#collect-delete'),
//	  offset: '200px',
        area: '560px',
        title: ['取消收藏', 'font-size:18px;color:#ba9963;font-weight:600'],
        btn: ['确定', '取消'],
        shadeClose: true,
        btnAlign: 'c',
        scrollbar: false,
    });
})