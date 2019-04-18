/**
 * Created by Xic on 2017/6/15.
 */
Comps.share = function(option){
    this.compId = option.compId;
    this.imgSize = option.imgSize;
    this.needAsyncInit = option.needAsyncInit;
    this.lableName = option.lableName;
}


//初始化方法
Comps.share.prototype.init=function(){

    var that =this;


    if(this.needAsyncInit){

        $.ajax({
            type : "GET",
            url : ContextPath+"/shareComp/ajaxLoadData",
            data : {
                imgSize : that.imgSize,
                lableName : that.lableName
            },
            dataType : "json",
            async : true,
            success : function(data) {

                $("#" + that.compId + "_share_name").first().text(data.lableName);
                $("#" + that.compId + "_share_detail").attr("imgsize",
                    data.imgSize);

                that.bindSomeEvent();
                Utils.ajaxLoadComplate(that);

            }

        })


    }

    else {
        that.bindSomeEvent();
        Utils.ajaxLoadComplate(that);


    }

}

//绑定事件
Comps.share.prototype.bindSomeEvent=function(){


    var that =this;

    window._bd_share_config = {
        "common" : {
            "bdSnsKey" : {},
            "bdText" : "",
            "bdMini" : "2",
            "bdMiniList" : false,
            "bdPic" : "",
            "bdStyle" : "0",
            "bdSize" : that.imgSize
        },
        "share" : {},
        "image2" : {
            "viewList" : [ "qzone", "tsina", "weixin" ],
            "viewText" : "分享到：",
            "viewSize" : "16"
        },
        "selectShare" : {
            "bdContainerClass" : null,
            "bdSelectMiniList" : [ "qzone", "tsina", "weixin" ]
        }
    };
    with (document)
        0[(getElementsByTagName('head')[0] || body)
            .appendChild(createElement('script')).src = 'http://bdimg.share.baidu.com/static/api/js/share.js?v=89860593.js?cdnversion='
            + ~(-new Date() / 36e5)];


}