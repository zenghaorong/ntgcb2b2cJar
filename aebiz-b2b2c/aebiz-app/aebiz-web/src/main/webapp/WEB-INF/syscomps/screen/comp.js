/**
 * Created by 金辉 on 2016/12/21.
 */
CompsLoader.loadComponent_$_compId = function(){
    Utils.loader('Screen',$_compConfig_$);
}
$(function(){
    $("#$_compId").delegate(".j_btnsConfirm","click",function(){
        var attrName = $(this).parents(".sl-wrap").find(".j_selectedDiv").data("attrName");
        var attrValue = $(this).parents(".sl-wrap").find(".j_selectedDiv").data("attrValue");
        search_$_compId(attrName,attrValue);
    })
})
function search_$_compId(attrName,attrValue){
    var uri = window.location.href;
    var reg = new RegExp("(^|&)attrValue=([^&]*)");
    var r = uri.match(reg);
    if(!$.isEmptyObject(r)){
        var attrRep = r[2].match(new RegExp("(^|%40)"+attrName+"_(((?!%40).)*)"));
        if(!$.isEmptyObject(attrRep)){
            uri=uri.replace(attrRep[0],attrName+"_"+attrValue);
        }else{
            var newAttr = r[0]+"%40"+attrName+"_"+attrValue;
            uri=uri.replace(r[0],newAttr);
        }
    }else{
        if(uri.indexOf("?")>0){
            uri+="&attrValue="+attrName+"_"+attrValue;
        }else{
            uri+="?attrValue="+attrName+"_"+attrValue;
        }
    }
    window.location.href = uri;
}

function multipleSelect_$_compId(attrDiv,attrName,attrValue){
    var selectedDiv = attrDiv.find(".j_selectedDiv");
    var selectedAttr = selectedDiv.find(".j_selected");
    selectedDiv.data("attrName",attrName);
    selectedDiv.show();
    //添加选中条件
    selectedAttr.append("<li style='display:block;' class='j_selectedAttr selected'> " +
        "<a href='javascript:void(0)' title='"+attrValue+"'> " +
        "<i></i>"+attrValue+"</a> " +
        "</li>");
    $(".j_btnsConfirm").removeClass("disabled");
    var selectedAttrValue = selectedDiv.data("attrValue");
    if(!$.isEmptyObject(selectedAttrValue)){
        selectedAttrValue+="、"+attrValue;
    }else{
        selectedAttrValue="";
        selectedAttrValue+=attrValue;
    }
    selectedDiv.data("attrValue",selectedAttrValue);
}

//条件选中刷新页面
function doSearch_$_compId(ts,attrName,attrValue){
    var attrDiv = $(ts).parents(".sl-wrap");
    if(attrDiv.hasClass("multiple")){
        multipleSelect_$_compId(attrDiv,attrName,attrValue);
        return false;
    }
    var uri=window.location.href;
    var uriName=window.location;
    uriName=uriName.origin+uriName.pathname;
    var index=uri.indexOf("?");
    if(index>0){
        uri = uri.substring(index + 1);
        var obj = getQueryObj(uri);
        if("categoryUuid"==attrName) {
            obj[attrName] = attrValue;
            var parmStr=getQueryString(obj);
            if(parmStr !="" && parmStr !=null){
                parmStr=decodeURI(parmStr);
                window.location.href=uriName+"?"+parmStr;
            }else{
                window.location.href=uriName;
            }
        }else{
            obj["attrValue"] =attrName+"_"+attrValue;
            var parmStr=getQueryString(obj);
            if(parmStr !="" && parmStr !=null){
                parmStr=decodeURI(parmStr);
                window.location.href=uriName+"?"+parmStr;
            }else{
                window.location.href=uriName;
            }
        }
    }else{
        if("categoryUuid"==attrName) {
            window.location.href = uri + "?"+attrName+"=" + attrValue;
        }else{
            window.location.href = uri + "?attrValue="+attrName+"_"+attrValue;
        }

    }
}
function getQueryObj(queryString) {
    var reg = /([^=&\s]+)[=\s]*([^=&\s]*)/g;
    var obj = {};
    while(reg.exec(queryString)){
        obj[RegExp.$1] = RegExp.$2;
    }
    return obj;
}

function getQueryString(queryObj) {
    return $.param(queryObj, true);
}