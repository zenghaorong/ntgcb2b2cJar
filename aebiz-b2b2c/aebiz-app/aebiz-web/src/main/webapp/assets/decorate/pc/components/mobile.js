/**
 * Created by wenyu on 2017/6/2.
 */
Comps.mbottombar = function(options){
    this.options  = options.mapProps;
}

Comps.mbottombar.prototype.init = function(){
    var _self = this;
    $.each($('.mui-bar-tab a'),function(index){
        $(this).attr('data-href',_self.options['icon'+(index+1)+'url']);
        $(this).find('.mui-tab-label').text(_self.options['icon'+(index+1)+'txt']);
        console.log(_self.options['icon'+(index+1)+'txt'])
        $(this).on('click',function(){
            window.location.href=$(this).attr('data-href')
        })
    })
};
