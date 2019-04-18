/**
 * Created by Xic on 2017/5/31.
 */
Comps.productTopNav = function (options) {
    this.options = options;
    this.compId = options.mapProps.compId;
    this.ajaxUrl = MobilePath + 'jsonParam={"opeType":"getIndex"}';
};
Comps.productTopNav.prototype.init = function () {
    var _self = this;
    var target = "#"+ this.compId;
    var indexUrl = this.options.mapProps.indexUrl;
    var searchUrl = this.options.mapProps.searchUrl;
    var cartUrl = this.options.mapProps.cartUrl;
    var optionTitle = this.options.mapProps.optionTitle;
    var optionUrl = this.options.mapProps.optionUrl;
    var optionImg = this.options.mapProps.imgSrc;
    var optionradio = '<li class="mui-table-view-cell" id="'+this.compId+'_cartRadio">' +
        '<a href="'+optionUrl+'" id="'+this.compId+'_cartUrl">' +
        '<img src="'+optionImg+'" data-bd-imgshare-binded="1" >'+optionTitle+'</a></li>';
    console.log(this.options);
    if(this.options.mapProps.indexradio === "false"){
        $(target+"_indexRadio").remove();
        selectHeight();
    }
    if(this.options.mapProps.searchradio === "false"){
        $(target+"_searchRadio").remove();
        selectHeight();
    }
    if(this.options.mapProps.cartradio === "false"){
        $(target+"_cartRadio").remove();
        selectHeight();
    }
    if(this.options.mapProps.optionradio === "true"){

       $(".mui-table-view").append(optionradio);
       selectHeight();
    }
    $(target+"_indexUrl").click(function () {
        window.location.href = indexUrl;
    });
    $(target+"_searchUrl").click(function () {
        window.location.href = searchUrl;
    });
    $(target+"_cartUrl").click(function () {
        window.location.href = cartUrl;
    });
    //选择列表宽度
    function selectHeight() {
        var num = $(".mui-table-view-cell").length;
        var height = 40 * num + "px";
        $('.mui-scroll-wrapper').css("height",height);
    }
    _self.change();
};
Comps.productTopNav.prototype.change = function () {
    $(".shopping").click(function(){
        $("html,body").animate({scrollTop:0},200);
        $('.main-wrapper').show();
        $('#details-evaluate').hide();
        $(this).addClass("action").siblings().removeClass("action");
    })

    $('.pingjia').click(function(){
        sessionStorage.setItem("pijianIndexForimgYulan",0);
        $("#zeroEvaluat").html("");
        Comps.productEvaluate.prototype.evaluate();
        $('.main-wrapper').hide();
        $('#details-evaluate').show();
        $(this).addClass("action").siblings().removeClass("action");
        $("#details-evaluate .pijianav li:eq(0)").addClass("action");
    })

    $('.productDetail').click(function(){
        $('.main-wrapper').show();
        $('#details-evaluate').hide();
        $(this).addClass("action").siblings().removeClass("action");
        setTimeout(function(){
            $(window).scrollTop($('#xiangq').offset().top);
        },100);
    })
};