/**
 * Created by wenyu on 2017/5/10.
 */
Comps.topSearch = function(options){
    this.options = options.mapProps;
};

Comps.topSearch.prototype.init = function(){
    var _self = this;
    $('.searchproductall').focus(function(){
            window.location.href = _self.options.mdetail_url;
    });

    $('.j-gocategory').on('click',function(){
        window.location.href = _self.options.mcategory_url;
    })

    $('.j-gocart').on('click',function(){
        window.location.href = _self.options.mcart_url;
    })
};