/**
 * Created by Xic on 2017/5/17.
 */
Comps.login = function(options){
    this.options = options;
    this.ajaxUrl = MobilePath + 'jsonParam={"opeType":"getIndex"}';
};

Comps.login.prototype.init = function(){
    var _self = this;
    var defineUrl = this.options.mapProps.defineUrl;
    var registerUrl = this.options.mapProps.registerUrl;
    var forgetUrl = this.options.mapProps.forgetUrl;
    console.log(registerUrl+"|"+forgetUrl);
    _self.toLogin(defineUrl,registerUrl,forgetUrl);

};

Comps.login.prototype.toLogin = function (defineUrl,registerUrl,forgetUrl) {
    var isAppend = true;
    $('.mui-input-group input').bind('focus', function() {
        $('.oauth-area').css('display', 'none');
    }).bind('blur', function() {
        $('.oauth-area').css('display', 'block');
    });

    $('.mui-input-group input').bind('input propertychange', function() {
        if (isAppend == true) {
            $('.login-btn').remove();
            $('.register-btn').before("<button id='login' class='mui-btn mui-btn-block mui-btn-primary login-btn'>登录</button>");
            isAppend = false;
        }

        $(".mui-btn").css({
            "position": 'relative',
            'top': 0,
            "background": "red",

        });
    });
    //注册
    $(".register-btn").click(function () {
       window.location.href = registerUrl;
    });
    //忘记密码
    $("#forgetPassword").click(function () {
        window.location.href = forgetUrl;
    });
    //获取comeAddress  获取来的位置,如果取不到值，或者来源未设置，默认是首页。
    var comeAddress = localStorage.getItem("comeAddress");
    if (comeAddress == null || comeAddress == "" || comeAddress == "undefined") {
        comeAddress = defineUrl;
    }

    var mobliePhone = localStorage.getItem("mobliePhone");
    $("#account").val(mobliePhone);
    $("#account").blur(function(){
        var acount = $("#account").val();
        if(typeof acount=='undefined' || acount == null || acount == ''){
            mui.alert('账号不能为空', '提示');
        }
    });
    $("#password").blur(function(){
        var password = $("#password").val();
        if(typeof password=='undefined' || password == null || password == ''){
            mui.alert('密码不能为空', '提示');
        }
    });
    // 这里初始化数据和绑定页面按钮的js方法
    $(document).on('click','#login',function(){
        console.log(comeAddress);
        var acount = $("#account").val();
        if(typeof acount=='undefined' || acount == null || acount == ''){
            mui.alert('账号不能为空', '提示');
            return;
        }
        var password = $("#password").val();
        localStorage.setItem("loginPWD",password);
        var pwd= $.md5(password);
        if(typeof password=='undefined' || password == null || password == ''){
            mui.alert('密码不能为空', '提示');
            return;
        }
        var url =MobilePath+'jsonParam={"opeType":"customerToLogin","map":{"mobile":"'+acount+'","password":"'+pwd+'"}}';
        $.ajax({
            url: url,
            type: "POST",
            dataType: "json",
            success: function(data){
                console.log(data);
                if(data.return_code != "0"){
                    mui.alert(data.message,'提示');
                }else{
                    var customer = data.customer;
                    var userId = customer.uuid;
                    localStorage.setItem("userId",userId);
                    localStorage.setItem("token",data.token);
                    localStorage.setItem("sessionId",data.sessionId);
                    localStorage.setItem("mobliePhone",acount);
                    localStorage.setItem("password",pwd);
                    localStorage.removeItem("comeAddress");
                    location.href=comeAddress;
                }
            },
            error:function(data){
                console.log("error");
            }
        });

    });


};
