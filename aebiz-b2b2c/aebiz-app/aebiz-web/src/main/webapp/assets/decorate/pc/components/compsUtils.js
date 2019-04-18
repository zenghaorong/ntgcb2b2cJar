/*
 * @Author: wenyu
 * @Date:   2017-01-09 17:26:22
 * @Last Modified by:   wenyu
 * @Last Modified time: 2017-01-13 08:46:38
 */

'use strict';
var Comps = {},
    CompsLoader = {},
    CompMessage = {},
    Utils = {},
    isLoadedData = [];

CompMessage = {
    topics:{},
    _genName:function(name){
        return "t."+ escape(name);
    },
    subscribe:function(topic,func){
        var name = this._genName(topic);
        if(this.topics[name]){
            this.topics[name].push(func);
        }else{
            this.topics[name] = [];
            this.topics[name].push(func);
        }
    },
    publish:function(topic,option,callback){
        console.log("发布消息:"+topic);
        if(option==null || option == undefined){
            option = {};
        }
        var name = this._genName(topic);
        for(var i in this.topics[name]){
            this.topics[name][i](option,callback);
        }
    }
};

Utils.loader = function(funcName, option) {
    // if (!option.hasOwnProperty('compsType')) {
    //     var moduleFuncName = funcName.toLowerCase();
    //     require([moduleFuncName], function(moduleFuncName) {
    //         var obj = new Comps[funcName](option);
    //         obj.init();
    //     });
    // }
    //
    var obj = new Comps[funcName](option);
    obj.init();

    isLoadedData.push(option.compId);
    console.info('%c' + funcName + "组件已经被加载，组件id为" + option.compId, 'color:' + Utils.getRandomColor());

}

Utils.ajaxLoadComplate = function(target) {
    $("#" + target.compId).find('.m-comsloading').hide();
}

Utils.loadComplate = function(target) {
    $("#" + target.compId).find('.m-comsloading').hide();
}

Utils.throttle = function(fn, delay) {
    var timer = null;
    return function() {
        var context = this,
            args = arguments;
        clearTimeout(timer);
        timer = setTimeout(function() {
            fn.apply(context, args);
        }, delay);
    };
}

Utils.getRandomColor = function() {
    return '#' + (Math.random() * 0xffffff << 0).toString(16);
}


Utils.getProductDetailUrl = function(contextPath,configedUrl,sku){
    if(configedUrl){
        return configedUrl.indexOf("?")==-1 ?((contextPath+configedUrl+"?sku=")+sku):((contextPath+configedUrl+"&sku=")+sku)
    }else{
        return "";
    }
}

Utils.getStoreDetailUrl = function(contextPath,configedUrl,storeUuid){
    if(configedUrl){
        return configedUrl.indexOf("?")==-1 ?((contextPath+configedUrl)+"?storeUuid="+storeUuid):((contextPath+configedUrl)+"&storeUuid="+storeUuid)
    }else{
        return "";
    }
}
Utils.getProductSearchUrl = function(contextPath,configedUrl,keyword){
    if(configedUrl){
        return configedUrl.indexOf("?")==-1 ?((contextPath+configedUrl)+"?keyword="+keyword):((contextPath+configedUrl)+"&keyword="+keyword)
    }else{
        return "";
    }
}

Utils.getProductListUrl = function(contextPath,configedUrl,categoryUuid){
    if(configedUrl){
        return configedUrl.indexOf("?")==-1 ?((contextPath+configedUrl)+"?categoryUuid="+categoryUuid):((contextPath+configedUrl)+"&searchType=searchTypeParam="+categoryUuid)
    }else{
        return "";
    }
}

Utils.getProductListUrlByBrand = function(contextPath,configedUrl,brandName){
    if(configedUrl){
        return configedUrl.indexOf("?")==-1 ?((contextPath+configedUrl)+"?searchType=3&searchTypeParam="+brandName):((contextPath+configedUrl)+"&searchType=3&searchTypeParam="+brandName)
    }else{
        return "";
    }
}

Utils.getContentUrl = function(contextPath,configedUrl,contentUuid){
    if(configedUrl){
        return configedUrl.indexOf("?")==-1 ?((contextPath+configedUrl)+"?contentUuid="+contentUuid):((contextPath+configedUrl)+"&contentUuid="+contentUuid)
    }else{
        return "";
    }
}
Utils.getShopSearch = function(contextPath,configedUrl,storeUuid,searchKey){
    if(configedUrl){
        return configedUrl.indexOf("?")==-1 ?((contextPath+configedUrl)+"?storeUuid="+storeUuid+"?searchKey="+searchKey):((contextPath+configedUrl)+"?storeUuid="+storeUuid+"?searchKey="+searchKey)
    }else{
        return "";
    }
}
Utils.getFullPath = function(contextPath,configedUrl,key,value){
    if(configedUrl){
        return configedUrl.indexOf("?")==-1 ?((contextPath+configedUrl)+"?"+key+"="+value):((contextPath+configedUrl)+"&"+key+"="+value)
    }else{
        return "";
    }
}

Utils.getNoticeDetailUrl = function(contextPath,configedUrl,noticeUuid){
    if(configedUrl){
        return configedUrl.indexOf("?")==-1 ?((contextPath+configedUrl)+"?noticeUuid="+noticeUuid):((contextPath+configedUrl)+"&noticeUuid="+noticeUuid)
    }else{
        return "";
    }
}

/**
 * [getQueryString 获取地址栏的地址参数]
 * @param  {[String]} name [需要获取的参数名称]
 * @return {[String]}      [返回获取的参数值]
 * @return {[Null]}        [没有参数则返回null]
 */
Utils.getQueryString = function(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if(r != null) return decodeURIComponent(r[2]);
    return null;
}




