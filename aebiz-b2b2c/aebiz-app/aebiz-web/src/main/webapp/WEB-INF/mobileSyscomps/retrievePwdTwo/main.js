/**
 * Created by Xic on 2017/5/17.
 */
Comps.retrievePwdTwo = function(options){
    this.defineUrl = options.mapProps.defineUrl;
    this.ajaxUrl = MobilePath + 'jsonParam={"opeType":"getIndex"}';
};

Comps.retrievePwdTwo.prototype.init = function(){
    var _self = this;
    $.ajax({
        type: "POST",
        url: _self.ajaxUrl,
        dataType: "json",
        async: true,
        success: function (data) {
            console.log(data);
            _self.retrieveTwo(_self.defineUrl);
        }

    })
};
Comps.retrievePwdTwo.prototype.retrieveTwo = function (defineUrl) {
        //获取参数
        var captcha=localStorage.getItem("captcha");
        $("#yzm").val(captcha);
        var mobliePhone = localStorage.getItem("mobilePhone");
        var mphone = mobliePhone.substr(0, 3) + '****' + mobliePhone.substr(7);
        $('#phone').html(mphone);
        var sessionId = localStorage.getItem("sessionId");
        $("#yzm").blur(function(){
            var yzm = $("#yzm").val();
            if(typeof yzm=='undefined' || yzm == null || yzm == ''){
                mui.alert('验证码不能为空', '提示');
            }
        });
        //下一步
        $("#nextStep").click(function(){
            var captcha1=$("#yzm").val();
            var cap=localStorage.getItem("captcha");
            if(cap==captcha1){
                window.location.href = defineUrl;
            }else{
                mui.alert('验证码输入错误', '提示');
            }

        })
        $("#hqyzm").click(function(){
            var captcha=localStorage.getItem("captcha");
            localStorage.setItem("captcha",captcha);
            var jsonParam = '{"opeType":"getMobileCodeForFindPwd","map":{"mobile":'+phone+'}}';
            var url =MobilePath+'jsonParam={"opeType":"getMobileCodeForFindPwd","map":{"mobile":"'+mobliePhone+'"}}';
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
                        $("#yzm").val(data.captcha);
                        //window.location.href=defineUrl+'?captcha='+data.captcha+'&mobilePhone='+phone;
                    }
                },
                error:function(data){
                    console.log(data);
                    mui.alert('系统错误','提示');
                }
            })
        })
};
