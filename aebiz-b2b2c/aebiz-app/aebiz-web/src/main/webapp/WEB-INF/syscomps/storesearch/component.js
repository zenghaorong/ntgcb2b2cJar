/**
 * Created by 金辉 on 2017/1/11.
 */
Comps.StoreSearchComp = function (config) {
    this.compId = config.compId;
    this.config = config;
}
Comps.StoreSearchComp.prototype.init = function () {
    var keyword = this._getUrlParam("storeKeyword");
    $("#" + compId + "_keyword").val(keyword);
    this._bindEvent();
    Utils.ajaxLoadComplate(this);
}
Comps.StoreSearchComp.prototype.search = function () {
    var compId = this.config.compId;
    var searchUrl = ContextPath+this.config.searchUrl;
    var keyword = $("#" + compId + "_keyword").val();
    var startPrice = $("#" + compId + "_startPrice").val();
    var endPrice = $("#" + compId + "_endPrice").val();
    var storeId = this._getUrlParam("storeUuid");
    if($.isEmptyObject(storeId)){
        storeId=this._getStoreId();
    }
    if ($.isEmptyObject(keyword)) {
        keyword = "";
    }
    var url = searchUrl + "&storeUuid=" + storeId + "&storeKeyword=" + keyword;
    if (!$.isEmptyObject(startPrice)) {
        url += "&startPrice=" + startPrice;
    }
    if (!$.isEmptyObject(endPrice)) {
        url += "&endPrice=" + endPrice;
    }
    window.location.href = url;
}
Comps.StoreSearchComp.prototype._getStoreId = function () {
    var productId = this._getUrlParam("productUuid");
    var storeId = "";
    $.ajax({
        "url": ContextPath+"/storeSearchComp/getProductStoreId",
        "data": {"productId": productId},
        "async": false,
        "success": function (data) {
            storeId = data;
        }
    })
    return storeId;
}
Comps.StoreSearchComp.prototype._bindEvent = function () {
    var ts = this;
    var compId = this.config.compId;
    $("#" + compId + "_searchProduct").click(function () {
        ts.search();
    })
}
//获取URL上参数
Comps.StoreSearchComp.prototype._getUrlParam = function (name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg); //匹配目标参数
    if (r != null) return decodeURIComponent(r[2]);
    return null; //返回参数值
}