
'use strict';
//登录提示
$(document).on("click",".btn-evaluate",function(){
	layer.open({
	  type: 1,
	  content: $('#evaluate'),
//	  offset: '200px',
	  area: '560px',
	  title: ['提示', 'font-size:18px;color:#ba9963;font-weight:600'],
	  btn: ['确定'],
	  shadeClose: true,
	  btnAlign: 'c',
	  scrollbar: false,
	});     

})