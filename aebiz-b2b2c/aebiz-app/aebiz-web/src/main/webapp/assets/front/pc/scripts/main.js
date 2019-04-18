/*
* @Author: jacobwang
* @Date:   2016-11-09 09:19:27
* @Last Modified by:   jacobwang
* @Last Modified time: 2016-11-15 16:01:51
*/

'use strict';
//价格分转元的公共方法
function setPrice(str){
    return (str/100).toFixed(2);
}

//浮点数加法运算
function FloatAdd(arg1,arg2){
    var r1,r2,m;
    try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
    try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
    m=Math.pow(10,Math.max(r1,r2));
    return (arg1*m+arg2*m)/m;
}

//浮点数减法运算
function FloatSub(arg1,arg2){
    var r1,r2,m,n;
    try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
    try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
    m=Math.pow(10,Math.max(r1,r2));
    //动态控制精度长度
    n=(r1=r2)?r1:r2;
    return ((arg1*m-arg2*m)/m).toFixed(2);
}

//浮点数乘法运算
function FloatMul(arg1,arg2)
{
    var m=0,s1=arg1.toString(),s2=arg2.toString();
    try{m+=s1.split(".")[1].length}catch(e){}
    try{m+=s2.split(".")[1].length}catch(e){}
    return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m);
}

$(document).on("click","#y_loadlogin_id",function () {
    var loginName = $("#yenew_loginName").val();
    if(loginName == null || loginName == ""){
        $("#error_msg").html("请输入用户名/手机");
        $("#errordiv").show();
        return false;
    }else{
        $("#error_msg").html("");
        $("#errordiv").hide();
    }

    var pwd = $("#yenew_pwd").val();
    if(pwd == null || pwd == ""){
        $("#error_msg").html("请输入密码");
        $("#errordiv").show();
        return false;
    }else{
        $("#error_msg").html("");
        $("#errordiv").hide();
    }
    var hasVilidateCode =  $("#yenew_hasVilidateCode").val();
    if(hasVilidateCode == "true"){
        var validateCode = $("#yenew_validateCode").val();
        if(validateCode == null || validateCode == ""){
            $("#error_msg").html("请输入验证码");
            $("#errordiv").show();
            return false;
        }else{
            $("#error_msg").html("");
            $("#errordiv").hide();
        }
    }
    var validateCode =  $("#validateCode").val();
    var remember = "";
    $("input[name='remember']:checked").each(function(){
        remember = $(this).val();
    });

    $.ajax({
        type: "POST",
        url: "/popLoginComp/quickLogin",
        data: {
            "loginName":loginName,
            "pwd":pwd,
            "hasVilidateCode":hasVilidateCode,
            "validateCode":validateCode,
            "remember":remember,
            "type":"customer",
            ranNum : Math.random()
        },
        dataType: "json",
        success: function(data){
            if(data.code == "0"){
                $("#modal-login-form").modal('hide');//关闭模态框
                var info=localStorage.getItem("href");
                localStorage.removeItem("href");
                window.location.href="/goods/placeOrder?"+$.param({cartInfo:info}, true);;
            }else{
                $("#error_msg").html(data.msg);
                $("#errordiv").show();
                return false;
            }
        }
    });
});