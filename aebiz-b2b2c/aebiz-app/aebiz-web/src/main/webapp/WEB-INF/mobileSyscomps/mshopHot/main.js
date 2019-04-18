/**
 * Created by wenyu on 2017/6/3.
 */
Comps.mshopHot = function(options){
    this.url = "";
    this.options = options.mapProps;
}

Comps.mshopHot.prototype.init = function(){
    var storeUuid = "";

    if (storeUuid == "") {
        storeUuid = Utils.getQueryString("storeUuid");
    }
    var localPath = window.location.href + "?storeUuid=" + storeUuid;

    $("#storeUuid").val(storeUuid);
    var userId = localStorage.getItem("userId");


    if(userId != "" && userId != null){
            this.url =MobilePath+'jsonParam={"opeType":"getStoreIndex","map":{"storeUuid":"' + storeUuid + '","customerUuid":"' + userId + '"}}';
    }else{
        this.url = MobilePath+'jsonParam={"opeType":"getStoreIndex","map":{"storeUuid":"' + storeUuid + '"}}';
    }

    this.getHotProduct();
}

Comps.mshopHot.prototype.getHotProduct = function(){
    var _self = this;
    _self.toDetailsPage();

    $.ajax({
        url: _self.url,
        type: "POST",
        dataType: "json",
        success: function(data) {
            if (data.return_code != "0") {
                mui.alert(data.message)
            } else {
                var spccList = data.store_productWindow;
                var rmxpList = data.store_hotProductWindow;
                for (var rm = 0; rm < rmxpList.length; rm++) {
                    var privilegeTypes = spccList[rm].privilegeTypes;
                    var zhuxiaowayStr = zhuxiaoway(privilegeTypes);
                    if (rm < 4) { //只展示4个
                        $("#dianpurmxp").append(
                            '<li class="pl-item rmxp' + rm + '">' +
                            '  <a href="javascript:void(0)" class="pl-pic" data-uuid="'+rmxpList[rm].uuid+'"><img src="../../../images/index/22.png"></a>' +
                            ' <div class="pl-info"><p class="baoyou pl-title">' + zhuxiaowayStr + '<font>韩版时尚女宽松短牛仔上衣阔腿短裤两件套</font></p>' +
                            '    <strong class="pl-price">￥198</strong><span class="pl-buycount">3387人付款</span>' +
                            '  </div>' +
                            ' </li>'
                        )
                        $('.rmxp' + rm).find('a').find('img').attr('src', rmxpList[rm].imageUrl);
                        $('.rmxp' + rm).find('font').html(rmxpList[rm].name);
                        $('.rmxp' + rm).find('strong').html('￥' + rmxpList[rm].price);
                        $('.rmxp' + rm).find('span').html(rmxpList[rm].saleCount + '人付款');
                    }
                    if (rm == rmxpList.length - 1) {
                        setListTyle('#dianpurmxp', 1.42);
                    }
                }

            }
        },
        error: function(data) {
            mui.alert(data.message,'提示');
        }
    })
};

Comps.mshopHot.prototype.toDetailsPage = function(){
    var _self = this;
    $(document).on('click','.pl-item',function(){
        var uuids = $(this).find('a').attr('data-uuid');

        var url = Utils.getProductDetailUrl(ContextPath,_self.options.mdetail_url,uuids);
        console.log(_self.options)
        window.location.href = url;
    })
};