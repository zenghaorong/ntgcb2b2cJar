$(function(){
    $(".submit").attr("disabled",true);
    // $(".submit").css("background-color","#adadad")
    //更换验证码图片
    $("#codeImg").click(function () {
        $(this).attr("src","/member/register/captcha?"+new Date().getTime())
    });

    $(".col-md-1 span").click(function () {
        if(!$(this).hasClass("glyphicon-ok-circle")){
            $(this).parent().siblings(".col-md-8").children().val("");
            $(this).parent().siblings(".col-md-8").children().blur();
            $(this).parent().parent().parent().removeClass("import");
        }
    })

    $("#loginname").focus(function () {
        $(this).parent().parent().parent().removeClass("import");//边框颜色
        $(this).parent().parent().parent().addClass("importBorder");
        $(this).parent("div").siblings(".col-md-1").children("span").attr("class","");//去除右边图标
        $("#checkLoginNameInfo").siblings("span").attr("class","glyphicon glyphicon-exclamation-sign").css("color","#969696");
        $("#checkLoginNameInfo").html("仅支持字母、数字、“-”“_”,及其组合，4-20个字符").css("color","#969696");
    });

    $("#loginname").blur(function () {
        $(this).parent().parent().parent().removeClass("importBorder");
        var loginname = $(this).val();
        var re = /^[a-zA-Z0-9_u4e00-u9fa5-]+$/;
        var flag = re.exec(loginname);
        if(loginname.length==0) {
            $("#checkLoginNameInfo").html("");

            $(this).parent("div").siblings(".col-md-1").children("span").attr("class","");
            $("#checkLoginNameInfo").siblings("span").attr("class","");
        }else if(loginname.length<4 || loginname.length>20){
            $(this).parent().parent().parent().addClass("import")//边框颜色
            $(this).parent("div").siblings(".col-md-1").children("span").attr("class","glyphicon glyphicon-remove-circle").css({"color":"red"});//新增右边图标
            $("#checkLoginNameInfo").html("长度只能在4-20个字符之间").css("color","red");
            $("#checkLoginNameInfo").siblings("span").attr("class","glyphicon glyphicon-minus-sign").css("color","red");
        }else if(!flag){
            $(this).parent().parent().parent().addClass("import")//边框颜色
            $(this).parent("div").siblings(".col-md-1").children("span").attr("class","glyphicon glyphicon-remove-circle").css({"color":"red"});//新增右边图标
            $("#checkLoginNameInfo").html("格式错误，仅支持字母、数字、\"-\" \"_\",及其组合").css("color","red");
            $("#checkLoginNameInfo").siblings("span").attr("class","glyphicon glyphicon-minus-sign").css("color","red");
        }else{
            $.ajax({
                type:"POST",
                url:"/member/register/checkLoginname/"+$(this).val(),
                dataType:"JSON",
                success:function(data){
                    if(data.code==1){
                        $("#loginname").parent().parent().parent().addClass("import")//边框颜色
                        $("#loginname").parent("div").siblings(".col-md-1").children("span").attr("class","glyphicon glyphicon-remove-circle").css({"color":"red"});//新增右边图标
                        $("#checkLoginNameInfo").html("该用户名已被注册！").css("color","red");
                        $("#checkLoginNameInfo").siblings("span").attr("class","glyphicon glyphicon-minus-sign").css("color","red");
                    }else{
                        $("#checkLoginNameInfo").siblings("span").attr("class","");
                        $("#checkLoginNameInfo").html("");
                        $("#loginname").parent("div").siblings(".col-md-1").children("span").attr("class","glyphicon glyphicon-ok-circle").css({"color":"#1AEC35"});//新增右边图标
                    }
                }
            });
        }
    });

    $("#inputPassword1").focus(function () {
        $(this).parent().parent().parent().removeClass("import");//边框颜色
        $(this).parent().parent().parent().addClass("importBorder");
        $(this).parent("div").siblings(".col-md-1").children("span").attr("class","");//去除右边图标
        $("#checkPassword1").siblings("span").attr("class","glyphicon glyphicon-exclamation-sign").css("color","#969696");
        $("#checkPassword1").html("建议使用字母、数字和符号的组合，6-18个字符").css("color","#969696");
    });

    $("#inputPassword1").blur(function () {
        $(this).parent().parent().parent().removeClass("importBorder");
        var password1 = $(this).val();
        var password2 = $("#inputPassword2").val();
        if (password1.length == 0) {
            $("#checkPassword1").html("");
            $(this).parent("div").siblings(".col-md-1").children("span").attr("class","");
            $("#checkPassword1").siblings("span").attr("class","");
        }else if(password1.length<6 || password1.length>18){
            $(this).parent().parent().parent().addClass("import")//边框颜色
            $(this).parent("div").siblings(".col-md-1").children("span").attr("class","glyphicon glyphicon-remove-circle").css({"color":"red"});//新增右边图标
            $("#checkPassword1").html("长度只能在6-18个字符之间").css("color","red");
            $("#checkPassword1").siblings("span").attr("class","glyphicon glyphicon-minus-sign").css("color","red");
        }else{
            $("#checkPassword1").siblings("span").attr("class","");
            $("#checkPassword1").html("");
            $("#inputPassword1").parent("div").siblings(".col-md-1").children("span").attr("class","glyphicon glyphicon-ok-circle").css({"color":"#1AEC35"});//新增右边图标
        }
    });

    $("#inputPassword1").keyup(function () {
        var len1 = $(this).val().length;

        //密码为八位及以上并且字母数字特殊字符三项都包括
        var strongRegex = new RegExp("^(?=.{8,})(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*\\W).*$", "g");
        //密码为七位及以上并且字母、数字、特殊字符三项中有两项，强度是中等
        var mediumRegex = new RegExp("^(?=.{7,})(((?=.*[A-Z])(?=.*[a-z]))|((?=.*[A-Z])(?=.*[0-9]))|((?=.*[a-z])(?=.*[0-9]))).*$", "g");
        var enoughRegex = new RegExp("(?=.{6,}).*", "g");
        if (false == enoughRegex.test($(this).val())|len1>18) {
            $("#passwordStrength").val("");
            $("#pass1").addClass("pass").removeClass("low").removeClass("mid").removeClass("hig");
            $("#pass2").addClass("pass").removeClass("low").removeClass("mid").removeClass("hig");
            $("#pass3").addClass("pass").removeClass("low").removeClass("mid").removeClass("hig");
        } else if (strongRegex.test($(this).val())) {//强
            $("#passwordStrength").val(2);
            $("#pass1").addClass("hig").removeClass("pass").removeClass("mid").removeClass("low");
            $("#pass2").addClass("hig").removeClass("pass").removeClass("mid").removeClass("low");
            $("#pass3").addClass("hig").removeClass("pass").removeClass("mid").removeClass("low");
        } else if (mediumRegex.test($(this).val())) {//中
            $("#passwordStrength").val(1);
            $("#pass1").addClass("mid").removeClass("pass").removeClass("low").removeClass("hig");
            $("#pass2").addClass("mid").removeClass("pass").removeClass("low").removeClass("hig");
            $("#pass3").addClass("pass").removeClass("mid").removeClass("low").removeClass("hig");
        } else {//弱
            $("#passwordStrength").val(0);
            $("#pass1").addClass("low").removeClass("pass").removeClass("mid").removeClass("hig");
            $("#pass2").addClass("pass").removeClass("mid").removeClass("low").removeClass("hig");
            $("#pass3").addClass("pass").removeClass("mid").removeClass("low").removeClass("hig");
        }

    });

    $("#inputPassword2").focus(function () {
        $(this).parent().parent().parent().removeClass("import");//边框颜色
        $(this).parent().parent().parent().addClass("importBorder");
        $(this).parent("div").siblings(".col-md-1").children("span").attr("class","");//去除右边图标
        $("#checkPassword2").siblings("span").attr("class","glyphicon glyphicon-exclamation-sign").css("color","#969696");
        $("#checkPassword2").html("请再次输入密码").css("color","#969696");
    });

    $("#inputPassword2").blur(function () {
        $(this).parent().parent().parent().removeClass("importBorder");
        var password2 = $(this).val();
        var password1 = $("#inputPassword1").val();
        if (password2.length == 0) {
            $("#checkPassword2").html("");
            $(this).parent("div").siblings(".col-md-1").children("span").attr("class","");
            $("#checkPassword2").siblings("span").attr("class","");
        }else if(password1==password2){
            $("#checkPassword2").siblings("span").attr("class","");
            $("#checkPassword2").html("");
            $("#inputPassword2").parent("div").siblings(".col-md-1").children("span").attr("class","glyphicon glyphicon-ok-circle").css({"color":"#1AEC35"});//新增右边图标
        }else{
            $(this).parent().parent().parent().addClass("import")//边框颜色
            $(this).parent("div").siblings(".col-md-1").children("span").attr("class","glyphicon glyphicon-remove-circle").css({"color":"red"});//新增右边图标
            $("#checkPassword2").html("两次密码输入不一致").css("color","red");
            $("#checkPassword2").siblings("span").attr("class","glyphicon glyphicon-minus-sign").css("color","red");
        }
    });

    $("#phone").focus(function () {
        $(this).parent().parent().parent().removeClass("import");//边框颜色
        $(this).parent().parent().parent().addClass("importBorder");
        $(this).parent("div").siblings(".col-md-1").children("span").attr("class","");//去除右边图标
        $("#checkPhoneInfo").siblings("span").attr("class","glyphicon glyphicon-exclamation-sign").css("color","#969696");
        $("#checkPhoneInfo").html("请输入手机号").css("color","#969696");
    });

    $("#phone").blur(function () {
        $(this).parent().parent().parent().removeClass("importBorder");
        var mobile = $(this).val();
        if(mobile.length==0) {
            $("#checkPhoneInfo").html("");
            $(this).parent("div").siblings(".col-md-1").children("span").attr("class","");
            $("#checkPhoneInfo").siblings("span").attr("class","");
        }else if(mobile.length!=11){
            $(this).parent().parent().parent().addClass("import")//边框颜色
            $(this).parent("div").siblings(".col-md-1").children("span").attr("class","glyphicon glyphicon-remove-circle").css({"color":"red"});//新增右边图标
            $("#checkPhoneInfo").html("格式有误").css("color","red");
            $("#checkPhoneInfo").siblings("span").attr("class","glyphicon glyphicon-minus-sign").css("color","red");
        }else{
            $.ajax({
                type:"POST",
                url:"/member/register/checkMobile/"+$(this).val(),
                dataType:"JSON",
                success:function(data){
                    if(data.code==1){
                        $("#phone").parent().parent().parent().addClass("import")//边框颜色
                        $("#phone").parent("div").siblings(".col-md-1").children("span").attr("class","glyphicon glyphicon-remove-circle").css({"color":"red"});//新增右边图标
                        $("#checkPhoneInfo").html("该手机号已被注册！").css("color","red");
                        $("#checkPhoneInfo").siblings("span").attr("class","glyphicon glyphicon-minus-sign").css("color","red");
                    }else{
                        $("#checkPhoneInfo").siblings("span").attr("class","");
                        $("#checkPhoneInfo").html("");
                        $("#phone").parent("div").siblings(".col-md-1").children("span").attr("class","glyphicon glyphicon-ok-circle").css({"color":"#1AEC35"});//新增右边图标
                    }
                }
            });
        }
    });


    $("#code").focus(function () {
        $(this).parent().parent().parent().addClass("importBorder");
        $(this).parent().parent().parent().removeClass("import");//边框颜色
        $("#checkCodeInfo").siblings("span").attr("class","glyphicon glyphicon-exclamation-sign").css("color","#969696");
        $("#checkCodeInfo").html("点击图片切换验证码！").css("color","#969696");
    });

    $("#code").blur(function () {
        $(this).parent().parent().parent().removeClass("importBorder");
        var code = $(this).val();
        if(code.length!=5) {
            $(this).parent().parent().parent().addClass("import")//边框颜色
        }
        if(code.length==0) {
            $(this).parent().parent().parent().removeClass("import")//边框颜色
            $("#checkCodeInfo").html("");
            $("#checkCodeInfo").siblings("span").attr("class","");
        }
    });

    $("#mobileCode").focus(function () {
        $(this).parent().parent().parent().addClass("importBorder");
        $(this).parent().parent().parent().removeClass("import");//边框颜色
        $("#checkmobileCodeInfo").siblings("span").attr("class","");
        $("#checkmobileCodeInfo").html("");
    });

    $("#mobileCode").blur(function () {
        $(this).parent().parent().parent().removeClass("importBorder");
        var code = $(this).val();
        if(code.length==0) {
            $(this).parent().parent().parent().addClass("import")//边框颜色
        }
        if(code.length==0) {
            $(this).parent().parent().parent().removeClass("import")//边框颜色
        }
    });

    $(".agreement").change(function() {
        if ($(this).is(':checked')) {
            $(".submit").attr("disabled",false)
            $(".submit").css("background-color","#c52622")
            $(".submit").mouseover(function () {
                $(".submit").css("background-color","#f72723")
            });
            $(".submit").mouseout(function () {
                $(".submit").css("background-color","#c52622")
            })
        }else{
            $(".submit").attr("disabled",true)
            $(".submit").css("background-color","#adadad")
        }
    });

    $("#getMobileCaptcha").click(function () {
        var mobile = $("#phone").val();
        var i= 0;
        if(mobile.length==0) {
            $("#checkPhoneInfo").html("请输入手机号").css("color","red");
            $("#phone").parent("div").siblings(".col-md-1").children("span").attr("class","");
            $("#checkPhoneInfo").siblings("span").attr("class","glyphicon glyphicon-minus-sign").css("color","red");
            $("#phone").parent().parent().parent().addClass("import")//边框颜色
            i++;
        }else if(mobile.length!=11){
            $("#phone").parent().parent().parent().addClass("import")//边框颜色
            $("#phone").parent("div").siblings(".col-md-1").children("span").attr("class","glyphicon glyphicon-remove-circle").css({"color":"red"});//新增右边图标
            $("#checkPhoneInfo").html("格式有误").css("color","red");
            $("#checkPhoneInfo").siblings("span").attr("class","glyphicon glyphicon-minus-sign").css("color","red");
            i++;
        }else{
            $.ajax({
                type:"POST",
                url:"/member/register/checkMobile/"+$("#phone").val(),
                async:false,
                dataType:"JSON",
                success:function(data){
                    if(data.code==1){
                        $("#phone").parent().parent().parent().addClass("import")//边框颜色
                        $("#phone").parent("div").siblings(".col-md-1").children("span").attr("class","glyphicon glyphicon-remove-circle").css({"color":"red"});//新增右边图标
                        $("#checkPhoneInfo").html("该手机号已被注册！").css("color","red");
                        $("#checkPhoneInfo").siblings("span").attr("class","glyphicon glyphicon-minus-sign").css("color","red");
                        i++;
                        $("#mobileValue").val(1);
                    }else{
                        $("#checkPhoneInfo").siblings("span").attr("class","");
                        $("#checkPhoneInfo").html("");
                        $("#phone").parent("div").siblings(".col-md-1").children("span").attr("class","glyphicon glyphicon-ok-circle").css({"color":"#1AEC35"});//新增右边图标
                    }
                }
            });
        }
        if(i>0||$("#mobileValue").val()==1){
            $("#mobileValue").val(0);
            return ;
        }
        if($("#phone").css("border-color")=="rgb(255, 0, 0)"){
            return ;
        }else{
            $("#getMobileCaptcha").hide();
            var i=59
            function show(){
                $("#count").html((i--)+"s后重新获取");
            }
            $("#count").html(60+"s后重新获取");
            var iCount = setInterval(show,1000);// 注意函数名没有引号和括弧！
            //setInterval(count,1000);
            setTimeout(function () {
                $("#getMobileCaptcha").show();
                clearInterval(iCount);
                $("#count").html("");
            }, 60000);

            $("#checkmobileCodeInfo").siblings("span").attr("class","glyphicon glyphicon-exclamation-sign").css("color","#969696");
            $("#checkmobileCodeInfo").html("已发送手机验证码").css("color","#969696");

            $.ajax({
                type:"POST",
                url:"/member/register/getMobileCaptcha/"+$("#phone").val(),
                dataType:"JSON",
                success:function(data){
                    layer.msg(data);
                }
            });
        }

    });


    $(".submit").click(function () {
        submitForm()
   });

    //按回车键注册
    $(document).keypress(function(e) {
        // 回车键事件
        if(e.which == 13) {
            if($(".agreement").is(':checked')){
                jQuery(".submit").click();
            }
        }
    });

    function submitForm() {
        if(check()){
            $.ajax({
                type:"POST",
                url:"/member/register/doRegister",
                data:$("#registerForm").serialize(),
                dataType:"JSON",
                success:function(data){
                    if (data.code == 0) {
                        layer.msg(data.msg, {
                            icon: 6,
                            time: 1000 //2秒关闭（如果不配置，默认是3秒）
                        }, function(){
                            window.location.href="/member/login";
                        });
                    } else {
                        layer.msg(data.msg, {icon: 5});
                    }
                }
            });
        }
    }
});


//用户协议
$(document).on("click",".userAgreement",function(){
    layer.open({
        type: 1,
        content: $('#agreement'),
//	  offset: '200px',
        area: '840px',
        title: ['茗流汇用户注册协议', 'font-size:16px;color:#ba9963;background:#f3f3f3'],
        btn: ['同意并继续'],
        shadeClose: true,
        btnAlign: 'c',
        scrollbar: false,
    });

})

//隐私政策
$(document).on("click",".privacy",function(){
    layer.open({
        type: 1,
        content: $('#privacy'),
//	  offset: '200px',
        area: '840px',
        title: ['隐私政策', 'font-size:16px;color:#ba9963;background:#f3f3f3'],
        btn: ['同意并继续'],
        shadeClose: true,
        btnAlign: 'c',
        scrollbar: false,
    });
})

function check() {
    $("#code").blur();
    $("#mobileCode").blur();
    $("#loginname").blur();
    $("#inputPassword1").blur();
    $("#inputPassword2").blur();
    $("#phone").blur();
    if($("#loginname").val().length==0){
        $("#loginname").parent().parent().parent().addClass("import");//边框颜色
    }
    if($("#inputPassword1").val().length==0){
        $("#inputPassword1").parent().parent().parent().addClass("import");//边框颜色
    }
    if($("#inputPassword2").val().length==0){
        $("#inputPassword2").parent().parent().parent().addClass("import");//边框颜色
    }
    if($("#phone").val().length==0){
        $("#phone").parent().parent().parent().addClass("import");//边框颜色
    }
    if($("#code").val().length==0){
        $("#code").parent().parent().parent().addClass("import");//边框颜色
    }
    if($("#mobileCode").val().length==0){
        $("#mobileCode").parent().parent().parent().addClass("import");//边框颜色
    }
    var count = 0;
    $("#registerForm ul li").each(function () {
        if($(this).css("border-color")=="rgb(255, 0, 0)"){
            count++;
        }
    })
    if(count==0){
        return true;
    }else{
        return false;
    }
}
