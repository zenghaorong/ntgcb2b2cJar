/**
 * Created by Xic on 2017/5/17.
 */
Comps.retrievePwdThree = function(options){
    this.defineUrl  = options.mapProps.defineUrl;
    this.ajaxUrl = MobilePath + 'jsonParam={"opeType":"getIndex"}';
};

Comps.retrievePwdThree.prototype.init = function(){
    var _self = this;
    $.ajax({
        type: "POST",
        url: _self.ajaxUrl,
        dataType: "json",
        async: true,
        success: function (data) {
            console.log(data);
            _self.retrieveThree(_self.defineUrl);
        }

    })
};
Comps.retrievePwdThree.prototype.retrieveThree = function (defineUrl) {
    var captcha=localStorage.getItem("captcha");
    var mobliePhone = localStorage.getItem("mobilePhone");
    var sessionId = localStorage.getItem("sessionId");
    //密码1
    $("#pwd1").blur(function(){
        var pwd1 = $("#pwd1").val();
        if(typeof pwd1=='undefined' || pwd1 == null || pwd1 == ''){
            mui.alert('密码不能为空', '提示');
            return;
        }
        if(!/^[a-z0-9_-]{6,20}$/.test(pwd1)){
            mui.alert('密码格式不对', '提示');
            return;
        }
    })
    $("#pwd2").blur(function(){
        var pwd1 = $("#pwd1").val();
        var pwd2 = $("#pwd2").val();
        if(typeof pwd2=='undefined' || pwd2 == null || pwd2 == ''){
            mui.alert('密码不能为空', '提示');
            return;
        }
        if(!/^[a-z0-9_-]{6,20}$/.test(pwd2)){
            mui.alert('密码格式不对', '提示');
            return;
        }
        if(pwd1 != pwd2){
            mui.alert('密码不一致', '提示');
            return;
        }
    })
    $("#toastBtn").click(function(){
        var pwd1 = $("#pwd1").val();
        var pwd2 = $("#pwd2").val();
        if(typeof pwd1=='undefined' || pwd1 == null || pwd1 == ''){
            mui.alert('密码不能为空', '提示');
            return;
        }
        if(!/^[a-z0-9_-]{6,20}$/.test(pwd1)){
            mui.alert('密码格式不对', '提示');
            return;
        }
        if(typeof pwd2=='undefined' || pwd2 == null || pwd2 == ''){
            mui.alert('密码不能为空', '提示');
            return;
        }
        if(!/^[a-z0-9_-]{6,20}$/.test(pwd2)){
            mui.alert('密码格式不对', '提示');
            return;
        }
        if(pwd1 != pwd2){
            mui.alert('密码不一致', '提示');
            return;
        }
        var url = MobilePath+'jsonParam={"opeType":"resetCustomerPwd","map":{"mobile":"'+mobliePhone+'","captcha":"'+captcha+'","password":"'+pwd1+'","pwdStrength":"2","sessionId":"'+sessionId+'"}}'
        $.ajax({
            url:url,
            type:'POST',
            dataType:'JSON',
            success:function(data){
                console.log(data);
                if(data.return_code != "0"){
                    mui.alert(data.message,'提示');
                }else{
                    window.location.href= defineUrl;
                }
            },
            error:function(data){
                console.log(data);
                mui.alert('系统错误','提示');
            }
        })
    })
};
