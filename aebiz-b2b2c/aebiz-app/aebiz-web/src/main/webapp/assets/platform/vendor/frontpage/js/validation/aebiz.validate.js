	// Validation 调用表单验证插件的js
  $(function(){
    if ($('.form-validate').length > 0) {
        $('.form-validate').each(function() {
            var id = $(this).attr('id');  //获取表单的id
            $("#" + id).validate({   //验证表单
                errorElement: 'span',  //输入错误时的提示标签
                errorClass: 'help-block has-error',  //输入错误时的提示标签类名
                errorPlacement: function(error, element) {  //输入错误时的提示标签显示的位置
                    if(element.parents(".input-group").length > 0){
                    		element.parents(".input-group").after(error);
                    }else if(element.parents(".y_validatainput").length > 0){
                    		element.parents(".y_validatainput").after(error);
                    }
                    else if(element.parents("label").length > 0) {
                        element.parents("label").after(error);
                    }else {
                        element.after(error);
                    }
                },
                highlight: function(label) {   //输入错误时执行的事件
                    $(label).closest('.form-group').removeClass('has-error has-success').addClass('has-error');
                },
                success: function(label) {   //输入正确时执行的事件
                    label.addClass('valid').closest('.form-group').removeClass('has-error has-success').addClass('has-success');
                },
                onkeyup: function(element) {   //验证元素输入值时按钮松开执行的事件
                    $(element).valid();
                },
                onfocusout: function(element) {   //验证元素失去焦点时进行验证
                    $(element).valid();
                }
            });
        });
    }	
  })