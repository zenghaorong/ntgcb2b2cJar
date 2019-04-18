/**
 * Created by wenyu on 2017/5/16.
 */
Comps.gridCategory = function(options){
    this.ajaxUrl = MobilePath + 'jsonParam={"opeType":"getIndex"}';
    this.indexNav = options.mapProps.indexNav;
};

Comps.gridCategory.prototype.init = function(){
    var navList = {};
    navList.indexNav = this.indexNav;
    console.log(navList);
    var html = template('m-index-navlist', navList);
    $('.m-index-nav').html(html);
};