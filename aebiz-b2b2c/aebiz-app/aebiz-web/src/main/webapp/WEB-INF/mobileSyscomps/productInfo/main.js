/**
 * Created by Xic on 2017/5/25.
 */
Comps.productInfo = function (options) {
    this.slideTime = options.mapProps.slideTime;
    this.productUuid = "c7e4746e9dae4b0e8a96e054327a16d7";//Utils.getQueryString("productUuid");
    this.ajaxUrl = MobilePath + 'jsonParam={"opeType":"getProductDetailPageByNoValidate","map":{"productUuid":"'+this.productUuid+'","promotionUuid":"","customerUuid":""}}';

};

Comps.productInfo.prototype.init = function () {
    var _self = this;
    $.ajax({
        type: "POST",
        url: _self.ajaxUrl,
        dataType: "json",
        async: true,
        success: function (data) {
            console.log(data);
            var html = template("m-index-product",data);
            $(".main-wrapper").html(html);
            var mySwiper = new Swiper('.swiper-container',{
                direction: 'horizontal',
                autoplay: 5000,
                loop: true,
                pagination : '.swiper-pagination',
                width:414,
                height:414,

            });
            var protections = data.productDetailInfo.protections;//商户的服务集合：正品保证、七天换货等
            if (protections != null) {
                $("#protections").html("");
                var li_str = '';
                for (var i = 0; i < protections.length; i++) {
                    var name = protections[i].name;
                    var iconUrl = protections[i].iconUrl;
                    li_str = '<li><i><img src="' + iconUrl + '"/></i>' + name + '</li>'
                    $("#protections").append(li_str);

                }
            }
            _self.productDetails();
        }

    })

};
Comps.productInfo.prototype.productDetails = function () {
        //如果是浏览器进入，则显示分享按钮
        $(document).on("click", "#isShareYesNo", function() {
            $(".fxiang").slideDown(200);
            $(".opacitys").fadeIn(200);
            $(".opacitys,.fxiang button").click(function() {
                $(".fxiang").slideUp(1);
                $(".opacitys").fadeOut(100);
            })
        });
        $(document).on("click", ".opacity,#giveaway h5 img,#giveaway .off input", function() {
            $(".opacity").fadeOut(300);
            $("#giveaway").slideUp(300);
        });

        $(document).on("click", "#maizen", function() {
            $(".opacity").fadeIn(300);
            $("#promotion").slideDown(300);
        });
        $(document).on("click", ".opacity,#promotion h5 img,#promotion .off input", function() {
            $(".opacity").fadeOut(300);
            $("#promotion").slideUp(300);
        });
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

};

