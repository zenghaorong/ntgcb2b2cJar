/**
 * Created by wenyu on 2017/5/17.
 */
Comps.indexShowcase = function(options){
    this.ajaxUrl = ContextPath+"/comps/integration/getProductByProductUuids4Mobile";
    this.options = options.mapProps;
};

Comps.indexShowcase.prototype.init = function(){
    var _self = this;
    var reqdata = {
        "productUuids":this.options.pduuids,
    };

    $.ajax({
        type: "POST",
        url: _self.ajaxUrl,
        dataType: "json",
        async: true,
        data:reqdata,
        success: function (data) {
            var datalist = {};
            datalist.list = data;
            var html = template('m-showcase-list-tpl', datalist);
            $('.m-showcase-list').html(html);

            _self.toDetailsPage();
        }

    })
};

Comps.indexShowcase.prototype.toDetailsPage = function(){
    var _self = this;
    $(document).on('click','.pl-item',function(){
        var uuids = $(this).find('a').attr('data-uuid');

        var url = Utils.getProductDetailUrl(ContextPath,_self.options.mdetail_url,uuids)
        window.location.href = url;
    })
};