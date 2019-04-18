/**
 * Created by wenyu on 2017/5/17.
 */
Comps.mact1 = function(options){
    this.ajaxUrl = MobilePath + 'jsonParam={"opeType":"getIndex"}';
};

Comps.mact1.prototype.init = function(){
    var _self = this;
    $.ajax({
        type: "POST",
        url: _self.ajaxUrl,
        dataType: "json",
        async: true,
        success: function (data) {
            console.log(data);
            var html = template('m-index-adact1', data);
            $('.m-index-act-1').html(html);
        }

    })
};