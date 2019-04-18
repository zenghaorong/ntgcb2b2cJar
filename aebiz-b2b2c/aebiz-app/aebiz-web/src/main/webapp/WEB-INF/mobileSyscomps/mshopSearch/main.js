/**
 * Created by wenyu on 2017/6/3.
 */
Comps.mshopSearch = function(options){
    this.options = options.mapProps;
}

Comps.mshopSearch.prototype.init = function(){
    var _self = this;
    $(".storeKeyWord").click(function() {
        //debugger;
        var keyword = $('.storeSearchKey').val();
        if (typeof keyword == 'undefinde' || keyword == "" || keyword == null) {
            mui.alert('请输入搜索内容');
            return;
        }

        var url = _self.toUrl(_self.options);

        console.log(url)
        //window.location.href = "../../../h5/templet/shop/shop-search.html";
    })
}

Comps.mshopSearch.prototype.toUrl = function(options){
    var uuid = Utils.getQueryString('storeUuid'),
        searchKey = $('.storeSearchKey').val(),
        url = Utils.getShopSearch(ContextPath,options.msearch_url,uuid,searchKey);

    return url;
}