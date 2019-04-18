/**
 * Created by Xic on 2017/5/17.
 */
Comps.retrievePwdOne = function(options){
    this.defineUrl = options.mapProps.defineUrl;
    this.ajaxUrl = MobilePath + 'jsonParam={"opeType":"getIndex"}';
};

Comps.retrievePwdOne.prototype.init = function(){
    var _self = this;
    $.ajax({
        type: "POST",
        url: _self.ajaxUrl,
        dataType: "json",
        async: true,
        success: function (data) {
            console.log(data);
            _self.retrieveOne(_self.defineUrl);
        }

    })
};
Comps.retrievePwdOne.prototype.retrieveOne = function (defineUrl) {
        //判null
        $("#mobilePhone").blur(function(){
            var acount = $("#mobilePhone").val();
            if(typeof acount=='undefined' || acount == null || acount == ''){
                mui.alert('账号不能为空', '提示');
            }
        });
        //获取验证码
        $("#getcaptcha").click(function(){
            //debugger;
            var phone = $("#mobilePhone").val();
            if(typeof phone=='undefined' || phone == null || phone == ''){
                mui.alert('账号不能为空', '提示');
            }else{
                var url = MobilePath + 'jsonParam={"opeType":"getMobileCodeForFindPwd","map":{"mobile":"'+phone+'"}}';
                $.ajax({
                    url:url,
                    type:'POST',
                    dataType:'JSON',
                    success:function(data){
                        //debugger;
                        if(data.return_code != "0"){
                            mui.alert(data.message,'提示');
                        }else{
                            localStorage.setItem("captcha",data.captcha);
                            localStorage.setItem("mobilePhone",phone);
                            localStorage.setItem("sessionId",data.sessionId);
                            window.location.href= defineUrl;
                        }
                    },
                    error:function(data){
                        console.log(data);
                        mui.alert('系统错误','提示');
                    }
                })
            }
        })
};
