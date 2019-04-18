/**
 * Created by Xic on 2017/6/5.
 */
Comps.rStoreList = function (options) {
    this.storeUrl = options.mapProps.storeUrl;
    this.productUrl = options.mapProps.productUrl;
};
Comps.rStoreList.prototype.init = function () {
    var _self = this;
    _self.loading(_self.storeUrl,_self.productUrl);
};
Comps.rStoreList.prototype.loading = function (storeUrl,productUrl) {
    var storeUrl = storeUrl;
    var productUrl = productUrl;
    moreStore('1',"20");
//**推荐更多店铺分页***/
    //Utils.getQueryString('uuids');
    $(window).scroll(function(){
        if($(window).scrollTop() == $(document).height() - $(window).height()){
            var nowPage =Number($("#nowPage").val());
            nowPage = nowPage + 1;
            var totalPage=Number($("#totalPage").val());
            if(nowPage >totalPage){
                mui.alert('沒有更多店铺!')
                return false;
            }else{
                var ajaxUrl =  ContextPath + '/comps/integration/getStoreGroomByUuids4Mobile';
                $.ajax({
                    url: ajaxUrl,
                    type: "POST",
                    dataType: "json",
                    data:{
                        nowPage:nowPage,
                        pageShow:"20"
                    },
                    async:false,
                    success: function(data){
                        console.log(data);
                        var storeList = data.storeList;
                        if(storeList.length == 0){
                            mui.alert('沒有更多店铺!');
                        }else{

                            storeInfo(data);
                        }

                    }
                });
            }
        }
    });

//******分页结束*******/

//请求店铺的ajax
    function moreStore(nowPage,pageShow){

        var ajaxUrl =  ContextPath + '/comps/integration/getStoreGroomByUuids4Mobile';
        console.log(ajaxUrl);
        $.ajax({
            type: "POST",
            url: ajaxUrl,
            dataType: "json",
            data:{
                nowPage:nowPage,
                pageShow:pageShow
            },
            async: true,
            success:function(data){
                console.log(data);
                storeInfo(data);
            }

        })
    }
    function storeInfo(data) {
        var storeList = data;
        console.log(storeUrl);
        console.log(productUrl);
        for(var i=0;i<storeList.length;i++){
            var sadf = haopinglv(storeList[i].storeScore);
            var hrmlStr ="";
            hrmlStr = hrmlStr +
                '<dl><dt><table><tr>'+
                '<td class="td1"><a href="'+storeUrl+'&storeUuid='+storeList[i].accountUuid+'" ><img src="'+storeList[i].logo+'"></a></td>'+
                '<td><p>'+storeList[i].storeName+'</p><span>'+sadf+(storeList[i].storeScore/5*100).toFixed(2)+'%</span></td>'+
                '</tr></table></dt><dd><ul>';
            var productList = storeList[i].productList;
            var subHrmlStr = "";
            for(var p =0;p<productList.length;p++){
                subHrmlStr = subHrmlStr +
                    '<li> <a href="'+productUrl+'&productUuid='+productList[p].uuid+'"><img src="'+productList[p].pic+'"></a><span>￥'+productList[p].price+'</span></li>';
            }
            hrmlStr =hrmlStr + subHrmlStr +  '</ul></dd></dl>';
            $("#recommended-shop").append(hrmlStr);
        }
        $("#nowPage").val("1");
        $("#totalPage").val("1")
    }
    function haopinglv(b){
        b = (b/5*100).toFixed(2);
        var str = "";
        if(b>0){
            if(10>=b && b >0){
                str = str + '<i class="ban"></i>';
            }else{
                str = str + '<i class="man"></i>';
            }
        }else{
            str = str + '<i></i>';
        }
        if(b>20){
            if(30>=b && b >20){
                str = str + '<i class="ban"></i>';
            }else{
                str = str + '<i class="man"></i>';
            }
        }else{
            str = str + '<i><em></em></i>';
        }
        if(b>40){
            if(50>=b && b >40){
                str = str + '<i class="ban"></i>';
            }else{
                str = str + '<i class="man"></i>';
            }
        }else{
            str = str + '<i><em></em></i>';
        }
        if(b>60){
            if(70>=b && b >60){
                str = str + '<i class="ban"></i>';
            }else{
                str = str + '<i class="man"></i>';
            }
        }else{
            str = str + '<i><em></em></i>';
        }
        if(b>80){
            if(90>=b && b >80){
                str = str + '<i class="ban"></i>';
            }else{
                str = str + '<i class="man"></i>';
            }
        }else{
            str = str + '<i><em></em></i>';
        }
        return str;
    }
};