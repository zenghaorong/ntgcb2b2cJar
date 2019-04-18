/**
 * Created by wenyu on 2017/5/17.
 */
Comps.recommandStore = function(options){
    this.shopList = options.mapProps.shopList;
    this.defineUrl = options.mapProps.defineUrl;
    this.storeUrl = options.mapProps.storeUrl;
    this.ajaxUrl =  ContextPath + '/comps/integration/getStoresByStoreUuids4Mobile';
};

Comps.recommandStore.prototype.init = function(){
    var _self = this;
    $.ajax({
        type: "POST",
        url: _self.ajaxUrl,
        dataType: "json",
        data:{
            uuids:this.shopList
        },
        async: true,
        success: function (data) {
            console.log(data);
            var shopList = {};
            shopList.stores = [];
            for(var i = 0;i<data.length;i+=1){
                shopList.stores.push(data[i]);
            }
            console.log(shopList);
            var html = template('m-indexStore', shopList);
            $('.recommendstore').html(html);
        }

    });
    $(".read-more").click(function () {
        window.location.href = _self.defineUrl;
    });
    $(document).on("click",'.item',function () {
        var uuids = $(this).attr('id');
        window.location.href = _self.storeUrl+"&storeUuid="+uuids;
        console.log(_self.storeUrl+"&storeUuid="+uuids);
    });
};