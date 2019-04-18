/**
 * Created by wenyu on 2017/5/16.
 */
Comps.mnotice = function(options){
    this.ajaxUrl = ContextPath + '/comps/integration/getHotBulletinList4Mobile';
    this.options = options.mapProps;
};

Comps.mnotice.prototype.init = function(){
    var _self = this;
    $.ajax({
        type: "POST",
        url: _self.ajaxUrl,
        dataType: "json",
        async: true,
        success: function (data) {
            console.log(data);

            var html = template('m-index-notice-list', data);
            $('.m-index-notice #scrollnews').html(html);

            _self.slider();

            _self.toList();
        }

    })
};


Comps.mnotice.prototype.slider = function(){
    var scrollnews = document.getElementById('scrollnews');
    var lis = scrollnews.getElementsByTagName('li');
    var ml = 0;
    var timer1 = setInterval(function() {
        var liHeight = lis[0].offsetHeight;
        var timer2 = setInterval(function() {
            scrollnews.scrollTop = (++ml);
            if (ml == liHeight) {
                clearInterval(timer2);
                scrollnews.scrollTop = 0;
                ml = 0;
                lis[0].parentNode.appendChild(lis[0]);
                lis[0].parentNode.appendChild(lis[0]);
            }
        }, 20);
    }, 2000);
}

Comps.mnotice.prototype.toList = function(){
    var _self = this;
    $(document).on('click','.m-index-notice li',function(){
        var uuid  = $(this).attr('data-uuid');

        var jumpUrl = Utils.getNoticeDetailUrl(ContextPath,_self.options.mdetail_url,uuid);

        window.location.href=jumpUrl;
    })

}