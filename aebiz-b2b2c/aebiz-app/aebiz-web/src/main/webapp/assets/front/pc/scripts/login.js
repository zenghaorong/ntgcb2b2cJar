
'use strict';

$(function(){
	$(".icon-cuo1").click(function() {
		//console.log($('input.string').size());
		$(this).css("display","none")
    	$(this).parents("li").find('input.string').val("").focus(); // 清空并获得焦点
    });
})
$('input.string').bind('input propertychange', function() {
	$(this).parents("li").find(".icon-cuo1").css("display","inline-block");
});