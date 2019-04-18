
'use strict';
$("[data-content]").eq(0).show();
$(document).on("click",".center-tab li",function(){
	$("[data-content]").hide().eq(num).fadeIn();
})
