/**
 * Created by wenyu on 2017/5/17.
 */
Comps.topBar = function(options){
    this.options = options;
    this.compId = options.mapProps.compId;
};

Comps.topBar.prototype.init = function(){
    $(this.compId+'_title').text(this.options.mapProps.title);
};