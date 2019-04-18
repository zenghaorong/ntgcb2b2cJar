/**
 * Created by wenyu on 2017/5/26.
 */
Comps.mpromotionList = function(){

}

Comps.mpromotionList.prototype.init = function(){
    //限时特卖
    var url =window.basePath+'jsonParam={"opeType":"getTMIndex"}';
    $.ajax({
        url: url,
        type: "POST",
        dataType: "json",
        success:function(data){
            if(data.return_code != 0){
                mui.alert(data.message,'提示');
            }else{

                //特卖首页特卖活动橱窗
                var hdccList = data.flashSalePromotionWindow;
                for(var hd =0;hd<hdccList.length;hd++){
                    $(".dianpuhuodong").append(
                        '<li class="pic hdcc'+hd+'">'+
                        '<a href="javascript:void(0)" onclick="limitActiveInfo(\''+hdccList[hd].uuid+'\')"><img src="'+hdccList[hd].iconUrl+'"></a>'+
                        '<p><span id="hdname'+hd+'">'+hdccList[hd].promotionName+'</span><span class="fr">剩余:<i>01:32:37</i></span></p>'+
                        '<input type="hidden" class="huodongdaojishi" id="hongdong'+hd+'" value=""/>'+
                        '</li>'
                    )

                    //$('.hdcc'+hd).find('a').find('img').attr('src',hdccList[hd].iconUrl);
                    $('#hdname'+hd).html(hdccList[hd].promotionName);
                    //$('.hdcc'+hd).find('i').html(hdccList[hd].currenSysTime);
                    var endTime = hdccList[hd].endTime;
                    var regEx = new RegExp("\\-","gi");
                    endTime=endTime.replace(regEx,"/");
                    endTime = Date.parse(endTime);
                }
            }
        }
    })
};