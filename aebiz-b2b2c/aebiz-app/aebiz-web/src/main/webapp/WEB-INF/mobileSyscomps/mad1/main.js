/**
 * Created by wenyu on 2017/5/17.
 */
Comps.mad1 = function(options){
    this.options = options;
    this.compId = options.mapProps.compId;
    this.ajaxUrl = MobilePath + 'jsonParam={"opeType":"getIndex"}';
};

Comps.mad1.prototype.init = function(){
    console.log(this.options)
    var height = this.options.mapProps.defineHeight,
        width = this.options.mapProps.defineWidth;

    var urlTarget = this.compId+'_url';

    $('#'+urlTarget).attr('href',this.options.mapProps.defineUrl);
    $('#'+urlTarget).find('img').css({
        height:height+'px',
        width:width+'px'
    })
};