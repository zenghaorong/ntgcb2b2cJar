/**
 * Created by wenyu on 2017/5/17.
 */
Comps.mcart = function(options){
    this.options = options;
    this.compId = options.mapProps.compId;
    this.ajaxUrl = MobilePath + 'jsonParam={"opeType":"getIndex"}';
};

Comps.mcart.prototype.init = function(){
    console.log(this.options)
    var height = this.options.mapProps.defineHeight,
        width = this.options.mapProps.defineWidth;

    var urlTarget = this.compId+'_url';

    $('#'+urlTarget).attr('href',this.options.mapProps.defineUrl);
    $('#'+this.compId+'_txt').html(this.options.mapProps.defineTitle);
    $('#'+urlTarget).find('img').css({
        height:height+'px',
        width:width+'px'
    })
};