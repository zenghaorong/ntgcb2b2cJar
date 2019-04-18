/**
 * Created by wenyu on 2017/5/22.
 */
Comps.mCategorySearch = function(options){
    this.options = options
};

Comps.mCategorySearch.prototype.init = function(options){
    var _self = this;
    $('#'+_self.options.mapProps.compId+'_defineUrl').focus(function(){
        window.location.href = _self.options.mapProps.defineUrl;
    })
};
