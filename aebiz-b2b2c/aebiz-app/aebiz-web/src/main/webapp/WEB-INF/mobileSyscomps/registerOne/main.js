/**
 * Created by xic on 2017/5/17.
 */
Comps.registerOne = function(options){
    this.ajaxUrl = MobilePath + 'jsonParam={"opeType":"getIndex"}';
    this.defineUrl = options.mapProps.defineUrl;
};

Comps.registerOne.prototype.init = function(){
    var _self = this;
    $.ajax({
        type: "POST",
        url: _self.ajaxUrl,
        dataType: "json",
        async: true,
        success: function (data) {
            console.log(data);
            _self.toRegisterOne(_self.defineUrl);
        }

    })
};
Comps.registerOne.prototype.toRegisterOne = function (defineUrl) {
        //获取验证码
    $("#phone").blur(function(){
        var phone = $("#phone").val();
        var reg = /^(0|86|17951)?(13[0-9]|15[012356789]|17[15678]|18[0-9]|14[579])[0-9]{8}$/;
        if (!reg.test(phone)) {
            mui.alert('手机号码输入有误', '提示');
        };
    })

    $("#hqyzm").click(function(){
        var mobliePhone = $("#phone").val();
        var url = MobilePath+'jsonParam={"opeType":"getMobileCode","map":{"mobile":"'+mobliePhone+'"}}'
        $.ajax({
            url:url,
            type:'POST',
            dataType:'JSON',
            success:function(data){
                console.log(data);
                if(data.return_code != "0"){
                    mui.alert(data.message,'提示');
                }else{
                    localStorage.setItem("captcha",data.captcha);
                    localStorage.setItem("sessionId",data.sessionId);
                    $('#yzm').val(data.captcha);
                }
            },
            error:function(data){
                console.log(data);
                mui.alert('系统错误','提示');
            }
        })
    });


    $('#lijizhuce').click(function(){
        var mobliePhone = $("#phone").val();
        var captcha = $("#yzm").val();
        var cap=localStorage.getItem("captcha");
        if(typeof mobliePhone=='undefined' || mobliePhone == null || mobliePhone == ''){
            mui.alert('账号不能为空', '提示');
            return;
        }
        if(typeof captcha=='undefined' || captcha == null || captcha == ''){
            mui.alert('验证码不为空', '提示');
            return;
        }
        if(cap==captcha){
            localStorage.setItem("mobliePhone",mobliePhone);
            localStorage.setItem("captcha",captcha);
            window.location.href= defineUrl;//注册第一步
        }else{
            mui.alert("验证码输入错误","提示");
        }

    })
}