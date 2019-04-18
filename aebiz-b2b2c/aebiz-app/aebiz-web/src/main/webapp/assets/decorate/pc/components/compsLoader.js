/*
 * @Author: wenyu
 * @Date:   2017-01-09 17:51:29
 * @Last Modified by:   wenyu
 * @Last Modified time: 2017-01-13 10:04:34
 */

'use strict';
var compsPublicLoad = {
    init: function() {
        this.load();
    },
    load: function() {
        var _self = this;
        $(window).scrollTop(1)
        if ($(window).scrollTop() <= 0) {
            _self.renderData()
        } else {
            $(window).scrollTop(1)
            document.onscroll = Utils.throttle(_self.renderData, 200);
        }
    },
    renderData: function() {
        $.each($(".m-components"), function() {
            var id = $(this).attr('id');

            var scrollH = $(window).scrollTop(),
                winH = $(window).height(),
                top = $(this).offset().top;

            if (top < (winH + scrollH + 200)) {
                if ($.inArray(id, isLoadedData) == -1) {
                    try{
                        CompsLoader['loadComponent_' + id]();
                    }catch(err){
                        console.log('%c'+id,'font-size:18px;color:#f00')
                    }
                }
            }
        })
    }
}

compsPublicLoad.init();
