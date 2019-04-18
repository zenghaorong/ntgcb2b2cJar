/**
 * Created by wenyu on 2017/5/16.
 */
Comps.slider = function(options){
    this.slideTime = options.mapProps.slideTime;
    this.ajaxUrl = ContextPath + '/comps/integration/getAdByUuid';
    this.options = options.mapProps;
};

Comps.slider.prototype.init = function(){
    var _self = this;
    if(_self.options.adUuid != undefined || _self.options.adUuid != ""){
        $.ajax({
            type: "POST",
            url: _self.ajaxUrl,
            dataType: "json",
            async: true,
            data:{
                adUuid:_self.options.adUuid
            },
            success: function (data) {
                data.banner = [];

                data.adUrlAndPicList.forEach(function(e){
                    var dataSingle = e.split(';');
                    var dataList = {
                        imageUrl : dataSingle[0],
                        linkUrl : dataSingle[1]
                    }

                    data.banner.push(dataList);
                })

                console.log(data.banner)
                var html = template('index-banner-list', data);
                $('.mui-slider').html(html);

                var mySwiper = new Swiper('.swiper-container',{
                    direction: 'horizontal',
                    autoplay: 5000,
                    loop: true,
                    pagination : '.swiper-pagination',
                })
            }

        })
    }

};