
'use strict';

//选择发票类型
$(document).on("click",".hd [data-label]",function(){
	var num = $(this).index();
	$(this).addClass("active").siblings().removeClass("active");
	$("[data-con]").hide().eq(num).show();
})

$(document).on("click",".icon-shuxie",function(){
	$(this).parent().find("span").attr("contenteditable","true").focus();
})

$(document).on("click",".default",function(){
	$(this).parents(".item-list").addClass("active default").siblings().removeClass("active default");
})

//创建地址
$(document).on("click","button.add",function(){
	layer.open({
	  type: 1,
	  content: $('#add-address'),
//	  offset: '200px',
	  area: '840px',
	  title: ['新增地址', 'font-size:18px;color:#ba9963;font-weight:600'],
	  btn: ['确定', '取消'],
	  shadeClose: true,
	  btnAlign: 'c',
	  scrollbar: false,
	});     

})
//创建地址
$(document).on("click","span.edit",function(){
	layer.open({
	  type: 1,
	  content: $('#edit-address'),
//	  offset: '200px',
	  area: '840px',
	  title: ['编辑地址', 'font-size:18px;color:#ba9963;font-weight:600'],
	  btn: ['确定', '取消'],
	  shadeClose: true,
	  btnAlign: 'c',
	  scrollbar: false,
	});     

})

//创建地址
$(document).on("click",".active-btn .mark",function(){
	layer.open({
	  type: 1,
	  content: $('#check-add'),
//	  offset: '200px',
	  area: '655px',
	  title: ['选择地址', 'font-size:18px;color:#ba9963;font-weight:600'],
	  btn: ['确定', '取消'],
	  shadeClose: true,
	  btnAlign: 'c',
	  scrollbar: false,
		yes:function (index, layero) {
            var $choose = $(".f-cb.active");
            var addressId = $choose.children("input[name='addressId']").val();
            var fullName = $choose.find("dd.name").text();
            var mobile = $choose.find("dd.mobile").text();
            var detailAddress = $choose.find("dd.detailAddress").text();
            //绑定地址信息
            $(".receipt-info .info").find("input").val(addressId);
            $(".receipt-info .info").find("span.fullname").text(fullName);
            $(".receipt-info .info").find("span.mobile").text(mobile);
            $(".receipt-info .info").find("span.addressDetail").text(detailAddress);
			//绑定订单的收货信息
			$("#address").text(detailAddress);
			$("#userIno").text(fullName + "  "+mobile);
            layer.close(index);
        }
	});     

})

//修改发票信息
$(document).on("click",".changeInfo",function(){
	layer.open({
	  type: 1,
	  content: $('#invoiceInfo'),
//	  offset: '200px',
	  area: '655px',
	  title: ['发票信息', 'font-size:18px;color:#ba9963;font-weight:600'],
	  btn: ['保存', '取消'],
	  shadeClose: true,
	  btnAlign: 'c',
	  scrollbar: false,
    });

})


$(document).on("click",".add-btn,.confirm,.cancel",function(){
	$(".receipt-info .bd").toggle();
})
