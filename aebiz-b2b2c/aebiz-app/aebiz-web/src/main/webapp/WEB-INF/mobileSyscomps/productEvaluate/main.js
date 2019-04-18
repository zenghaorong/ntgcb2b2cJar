/**
 * Created by Xic on 2017/5/27.
 */
Comps.productEvaluate = function () {
    this.ajaxUrl = MobilePath + 'jsonParam={"opeType":"getComments","map":{"productUuid":"59df4f2cdff849c29970c15bbf9e4589","customerUuid":"null","type":"","nowPage":"1","pageShow":"10"}}';
};
Comps.productEvaluate.prototype.init = function () {
    var _self = this;
    $.ajax({
        type: "POST",
        url: _self.ajaxUrl,
        dataType: "json",
        async: true,
        success: function (data) {
            console.log(data);
            _self.evaluate();
        }

    })
};
Comps.productEvaluate.prototype.evaluate = function () {
    allAppShow();
    mui.init({
        gestureConfig: {
            tap: true, //默认为true
            doubletap: true, //默认为false
            longtap: true, //默认为false
            swipe: true, //默认为true
            drag: true, //默认为true
            hold: true, //默认为false，不监听
            release: true //默认为false，不监听
        }
    });

    mui('.mui-scroll-wrapper').scroll()
    mui.previewImage();
    $("#details-evaluate .pijianav li").click(function() {
        var num = $(this).index();
        sessionStorage.setItem("pijianIndexForimgYulan", num);
        $("#zeroEvaluat").html("");
        //alert(num);
        $(this).addClass("action").siblings().removeClass("action");
        $("#details-evaluate .mui-content").css('display', 'none').eq(num).css('display', 'block');

        var typeMap = {
            "0": "",
            "1": "1",
            "2": "2",
            "3": "3",
            "4": "4"
        }
        //1好评；2中评；3差评；4晒图
        getCommentsByType("1", typeMap[num]);

    });
    var pageShow = 10;
    function allAppShow(){
        var nowPage = 1;
        sessionStorage.setItem("data-preview-group",1);
        sessionStorage.setItem("evaluateType","");
        $("#details-evaluate .mui-content").html("");
        getCommentsAjax(1,nowPage,pageShow,"");
    }

    function getCommentsAjax(first,nowPage,pageShow,type){

        var productUuid = localStorage.getItem('productUuid') || "59df4f2cdff849c29970c15bbf9e4589";
        var customerUuid = localStorage.getItem("userId") || " ";

        var url = MobilePath + 'jsonParam={"opeType":"getComments","map":{"productUuid":"'+productUuid+'","customerUuid":"'+customerUuid+'","type":"'+type+'","nowPage":"'+nowPage+'","pageShow":"'+pageShow+'"}}';
        $.ajax({
            type: 'post',
            url: url ,
            dataType: 'json',
            crossDomain: true,
            success: function(data){
                if (data.return_code == "0") {
                    sessionStorage.setItem("evaluateTotalPage",data.wm.totalPage);
                    sessionStorage.setItem("evaluateNextPage",parseInt(data.wm.nowPage)+1);
                    getComments(first,data);
                }

            }

        });
    }

    function getComments(first,data){

        if (first == 1) {
            $("#details-evaluate .pijianav li").eq(0).html("全部评价("+data.allCount+")");
            $("#details-evaluate .pijianav li").eq(1).html("好评("+data.goodCount+")");
            $("#details-evaluate .pijianav li").eq(2).html("中评("+data.middleCount+")");
            $("#details-evaluate .pijianav li").eq(3).html("差评("+data.badCount+")");
            $("#details-evaluate .pijianav li").eq(4).html("晒图("+data.picCount+")");
        }

        if (data.list != null && data.list.length > 0) {

            commentsShow(data.list);
        }else{
            $("#details-evaluate .mui-content").hide();
            var index = $(".pijianav li.action").index();
            switch(index){
                case 1 :
                    index = "好评";
                    break;
                case 2 :
                    index = "中评";
                    break;
                case 3 :
                    index = "差评";
                    break;
                case 4 :
                    index = "晒图";
                    break;
                default:
                    index = "评论";

            }
            $(".pijianav").after("<div id='zeroEvaluat' align='center'><img src='../../../images/order/11.png'><p>该商品暂无"+index+"信息</p></div>");
        }


    }
    function getCommentsByType(nowPage,type){
        $("#details-evaluate .mui-content").html("");
        sessionStorage.setItem("evaluateType",type);
        getCommentsAjax(0,nowPage,pageShow,type);

    }

    function commentsShow(comments){
        var index = sessionStorage.getItem("pijianIndexForimgYulan");
        var result;
        var len = comments.length;
        if(len > 0){
            for (var i = 0; i < len; i++) {
                if (comments[i] != null) {
                    result = appShow(comments[i]);
                    $("#details-evaluate .mui-content").eq(index).append(result[0]);
                    $("#details-evaluate .mui-content").eq(index).append(result[1]);
                }
            }
        }
    }

    function appShow(comment){
        var data_preview_group = sessionStorage.getItem("data-preview-group");
        sessionStorage.setItem("data-preview-group",parseInt(data_preview_group)+1);
        var productAppraise = comment.productAppraise;
        var picList = comment.picList;
        var customerImage = comment.customerLevelImage;
        var customerName = comment.customerName;
        var appTime = productAppraise.appTime;
        var appContent = productAppraise.appContent;

        var appScore = productAppraise.appScore;
        var $table = $('<table id="tbshmess">'
            +'<tr>'
            +'<td class="portrait"><img src="'+customerImage+'"></td>'
            +'<td class="name">'+customerName+'</td><td class="more">'+appTime+'</td></tr>'
            +'<tr>'
            +'<td colspan="3"><p id="" style="font-size:110%;"><span><i class="on"></i><i class="on"></i><i class="on"></i><i class="on"></i><i></i></span></p></td>'
            +'</tr>'
            +'</table>');
        var appContentP = $('<p>'+appContent+'</p>');
        var clear = $('<p class="clear"></p>');
        var picP;
        var pic;

        var $div = $('<div class="mui-content-padded"></div>');
        $div.append(appContentP);

        if(picList.length > 0){
            for (var i = 0; i < picList.length; i++) {
                pic = picList[i].picUrl;
                picP = $('<p class="pic"><img src="'+pic+'" data-preview-src="" data-preview-group="'+data_preview_group+'" /></p>');
                $div.append(picP);
            }
        }

        $div.append(clear);

        var $Is = $table.find("i").removeClass("on");
        $Is.eq(appScore-1).addClass("on").prevAll().addClass("on");


        return new Array($table,$div);
    }

    $(window).scroll(function(){
        var scrollTop = $(this).scrollTop();
        var scrollHeight = $(document).height();
        var windowHeight = $(this).height();
        if(scrollTop + windowHeight >= scrollHeight){

            var evaluateTotalPage = sessionStorage.getItem("evaluateTotalPage");
            var evaluateNextPage = sessionStorage.getItem("evaluateNextPage");
            evaluateTotalPage = parseInt(evaluateTotalPage);
            evaluateNextPage = parseInt(evaluateNextPage);
            if (evaluateNextPage <= evaluateTotalPage) {
                var nowPage = evaluateNextPage;
                var type = sessionStorage.getItem("evaluateType");
                getCommentsAjax(0,evaluateNextPage,pageShow,type);
            }
        }
    });

}