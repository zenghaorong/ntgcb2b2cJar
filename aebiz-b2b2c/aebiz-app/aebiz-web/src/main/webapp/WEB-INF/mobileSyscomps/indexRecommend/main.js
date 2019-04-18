/**
 * Created by wenyu on 2017/5/17.
 */
Comps.indexRecommend = function(options){
    this.ajaxUrl = MobilePath + 'jsonParam={"opeType":"getIndex"}';
};

Comps.indexRecommend.prototype.init = function(){
    var _self = this;
    $.ajax({
        type: "POST",
        url: _self.ajaxUrl,
        dataType: "json",
        async: true,
        success: function (data) {
            console.log(data);
            var html = template('m-index-act2-cnt', data);
            $('.m-index-act-2').html(html);
        }

    })
};