/**
 * Created by xic on 2017/5/17.
 */
Comps.registerTwo = function(options){
    this.defineUrl = options.mapProps.defineUrl;
    this.ajaxUrl = MobilePath + 'jsonParam={"opeType":"getIndex"}';
};

Comps.registerTwo.prototype.init = function(){
    var _self = this;
    $.ajax({
        type: "POST",
        url: _self.ajaxUrl,
        dataType: "json",
        async: true,
        success: function (data) {
            console.log(data);
            _self.toregisterTwo(_self.defineUrl);
        }

    })
};
Comps.registerTwo.prototype.toregisterTwo = function (defineUrl) {
    //获取参数信息
    var mobliePhone=localStorage.getItem("mobliePhone");

    var captcha = localStorage.getItem("captcha");
    var sessionId = localStorage.getItem("sessionId");

    $("#password").blur(function(){
        var password = $("#password").val();
        if(typeof password=='undefined' || password == null || password == ''){
            mui.alert('密码不能为空', '提示');
        }
    })

    $('#lijizhuces').click(function(){
        var password = $("#password").val();
        if(typeof password=='undefined' || password == null || password == ''){
            mui.alert('密码不能为空', '提示');
            return;
        }
        if(!/^[a-zA-Z0-9_-]{6,20}$/.test(password)){
            mui.alert('密码格式不对', '提示');
            return;
        }
        var url = MobilePath+'jsonParam={"opeType":"customerRegister","map":{"mobile":"'+mobliePhone+'","password":"'+password+'","captcha":"'+captcha+'","pwdStrength":"2","sessionId":"'+sessionId+'"}}';
        $.ajax({
            url:url,
            type:'POST',
            dataType:'JSON',
            success:function(data){
                console.log(data);
                if(data.return_code != "0"){
                    mui.alert(data.message, '提示');
                }else{
                    localStorage.setItem("mobliePhone",mobliePhone);
                    localStorage.setItem("captcha",captcha);
                    localStorage.setItem("password",password);
                    window.location.href= defineUrl;

                }
            },
            error:function(data){
                console.log(data);
                mui.alert('系统错误','提示');
            }
        })
    })
}