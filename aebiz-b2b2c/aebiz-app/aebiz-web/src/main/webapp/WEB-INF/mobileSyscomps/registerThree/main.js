/**
 * Created by xic on 2017/5/17.
 */
Comps.registerThree = function(options){
    this.defineUrl  = options.mapProps.defineUrl;
    this.ajaxUrl = MobilePath + 'jsonParam={"opeType":"getIndex"}';
};

Comps.registerThree.prototype.init = function(){
    var _self = this;
    $.ajax({
        type: "POST",
        url: _self.ajaxUrl,
        dataType: "json",
        async: true,
        success: function (data) {
            console.log(data);
            _self.toRegisterThree(_self.defineUrl);
        }
    })
};
Comps.registerThree.prototype.toRegisterThree = function (defineUrl) {
    var password=localStorage.getItem("password");
    var mobliePhone = localStorage.getItem("mobliePhone");
    var sessionId = localStorage.getItem("sessionId");
    $("#lijidenglu").click(function(){
        //立即登录
        localStorage.setItem("password",password);
        localStorage.setItem("mobilePhone",mobliePhone);
        window.location.href= defineUrl;
    })
}