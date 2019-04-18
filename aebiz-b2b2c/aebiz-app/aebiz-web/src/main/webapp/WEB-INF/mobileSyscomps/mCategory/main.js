/**
 * Created by wenyu on 2017/5/16.
 */
Comps.mCategory = function(options){
    this.options = options.mapProps;
};

Comps.mCategory.prototype.init = function(){
    // if(!isWeiXin()){
    //     $("body").prepend('<header class="mui-bar mui-bar-nav m-category-topbar"><a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a><h1 class="mui-title">全部分类</h1></header>');
    // }

    $('.defaultPng').remove();

    //品牌分类
    this.categoryPpName("","");
    //获取商品分类
    this.categoryProduct();
    //链接跳转
    this.categoryToList();

    $(".j-category .nav li").click(function(){
        var num = $(this).index();
        $(this).addClass("on").siblings().removeClass("on");
        $(".j-category .content ul").siblings().fadeOut(10).eq(num).fadeIn(800);
    });

    //得到上次数据
    var categoryHtmlActive = localStorage.getItem('categoryHtmlActive');
    if(categoryHtmlActive != null && categoryHtmlActive != ""){
        $(".ca .on").removeClass("on");
        $(".categoryHtmlActive0").attr("style","display:none")
        var domlist = $('.ca').find('li');
        domlist.each(function(index,obj){
            var temp = $(this).html();
            if(temp == categoryHtmlActive){
                $(this).addClass("on");
                $(".categoryHtmlActive"+index).attr('style','display:block');
            }
        })
        //清楚记录
        localStorage.removeItem("categoryHtmlActive");
    }
};

Comps.mCategory.prototype.categoryPpName = function(nowPage,pageShow){
    if(nowPage == "" || nowPage != null){
        nowPage =1;
    }
    if(pageShow == "" || pageShow != null ){
        pageShow =10;
    }
    var url = MobilePath+'jsonParam={"opeType":"getPNames","map":{"nowPage":"'+nowPage+'","pageShow":"'+pageShow+'"}}';
    $.ajax({
        url:url,
        type: "POST",
        dataType: "json",
        async : false,
        success:function(data){
            //debugger;
            //console.log(data);
            if(data.return_code !=0){
                mui.alert(data.message,'提示');
            }else{
                //添加主分类  segmentedControls
                var categoryPNames = data.pNames;
                $(".ca").append(
                    '<li>品牌列表</li>'
                );
                var htmlStr = '';
                htmlStr 　=　htmlStr + '<ul class="brandList"  style="display:block;">';
                //添加子分类
                for(var j=0;j<categoryPNames.length;j++){
                    htmlStr = htmlStr + ' <li><a href="javascript:void(0)" data-brandName="'+categoryPNames[j].brandName+'"><div class="pics"><img src="'+categoryPNames[j].imgUrl1 +'"></div><p>'+categoryPNames[j].brandName+'</p></a></li>'
                }
                htmlStr =htmlStr + '</ul>';
                $(".content").append(htmlStr);
            }
        }
    })
};

Comps.mCategory.prototype.categoryProduct = function(){
    var url = MobilePath+'jsonParam={"opeType":"getFrontCategorys"}';
    $.ajax({
        url:url,
        type: "POST",
        dataType: "json",
        async:false,
        success:function(data){
            //debugger;
            //console.log(data);
            if(data.return_code !=0){
                mui.alert(data.message,'提示');
            }else{
                //添加主分类  segmentedControls
                var categoryList = data.categoryList;
                for(var i=0;i<categoryList.length;i++){
                    //添加主分类
                    $(".ca").append(
                        '<li>'+categoryList[i].categoryName+'</li>'
                    );

                    //添加子分类
                    var subCategoryList = categoryList[i].subCategoryList;

                    var htmlStr = '';
                    var tempi = i+1;
                    htmlStr 　=　htmlStr + '<ul class="cateList">';
                    console.log(categoryList[i])
                    if(subCategoryList != null){
                        for(var j=0;j<subCategoryList.length;j++){
                            htmlStr = htmlStr + '<li class="nopic"><a href="javascript:void(0)" data-catename="'+subCategoryList[j].categoryName+'" data-cateuuid="'+subCategoryList[j].uuid+'"><p class="good_name">'+subCategoryList[j].categoryName+'</p></a></li>'
                        }
                    }else{
                        htmlStr = htmlStr + '<li><a href="javascript:void(0)">暂无分类！</a></li>'
                    }

                    htmlStr =htmlStr + '</ul>';
                    $(".content").append(htmlStr);


                }

                $(".ca").find('li').eq(0).addClass('on');
            }
        }
    })
}

Comps.mCategory.prototype.categoryToList = function(){
    var _self = this;
    $(document).on('click','.brandList li',function(){
        var brandName  = $(this).find('a').attr('data-brandName');

        var jumpUrl = Utils.getProductListUrlByBrand(ContextPath,_self.options.mcate_url,brandName)

        window.location.href=jumpUrl;
    })

    $(document).on('click','.cateList li',function(){
        var cateuuid  = $(this).find('a').attr('data-cateuuid');

        var jumpUrl = Utils.getProductListUrl(ContextPath,_self.options.mcate_url,cateuuid)
        console.log(jumpUrl)

        window.location.href=jumpUrl;
    })
}