
'use strict';
$(".coupon-list").eq(0).show();
$(document).on("click",".coupon-tab li",function(){
//	alert(num);
	$(".coupon-list").hide().eq(num).fadeIn();
})


//删除优惠劵弹层
$(document).on("click",".icon-delete1",function(){
	layer.open({
	  type: 1,
	  content: $('#coupon-delete'),
//	  offset: '200px',
	  area: '560px',
	  title: ['删除优惠券', 'font-size:18px;color:#ba9963;font-weight:600'],
	  btn: ['确定', '取消'],
	  shadeClose: true,
	  btnAlign: 'c',
	  scrollbar: false,
	});     

})