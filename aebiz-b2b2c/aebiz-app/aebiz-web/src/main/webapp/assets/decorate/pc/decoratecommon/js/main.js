/*
 * @Author: wenyu
 * @Date:   2016-12-13 10:19:22
 * @Last Modified by:   wenyu
 * @Last Modified time: 2017-01-19 21:40:57
 */

'use strict';

var decoration = {},
    layouthistory,//保存ls中的操作步骤
    layoutArea = '.design-container',//设计器目标元素
    currentDocument = null,//当前文档对象
    timerSave = 60 * 1000,//自动保存时间
    stopsave = 0,//
    startdrag = 0,//
    layoutAreaHtml = $(layoutArea).html(),//编辑区域的html
    currenteditor = null,
    temporaryId = '',//临时组件id
    temporaryParams = {},//临时组件配置参数
    rowParams = {},//行属性
    columnParams = {},//列属性
    getParamData = null,//临时组件配置
    isSave = false,//是否保存

    pageModel = [],//设计器数据结构保存空间
    markedLang = { //操作信息提示
        saveNewPage: "描述不可为空",
        osavePage: "页面名称和者描述不能为空！",
        noComponents: "当前页面没有组件，请添加组件！",
        noSave: "当前页面还未保存，请先保存！",
        saveSuccess: "保存成功！",
        doSuccess: "操作成功！",
        isComponentsDelete: "当前容器下有组件，确认删除？",
        isDeleteComps: "确认删除该组件？",
        isDeleteRows: "确认删除布局?",
        isVersionDelete: "当前版本正在使用，不允许删除！"
    },
    pageHtml = '';//设计器页面html

decoration.base = {};
decoration.layout = {};
decoration.ids = {};
decoration.data = {};

/**
 * [supportStorage 检测是否支持localstorage]
 * @return {[Boolean]} [是否支持的布尔类型结果]
 */
decoration.base.supportStorage = function() {
    if (typeof window.localStorage == 'object') {
        return true;
    } else {
        return false;
    }
}

/**
 * [randomNumber 生成随机数]
 * @return {[Int]} [调用randomFromInterval方法，返回该方法的返回值]
 */
decoration.base.randomNumber = function() {
    return randomFromInterval(1, 1e6)
}

/**
 * [randomFromInterval 生成随机数]
 * @return {[Int]} [返回一个随机数字]
 */
decoration.base.randomFromInterval = function() {
    return Math.floor(Math.random() * (t - e + 1) + e)
}

/**
 * [parseJson 解析json]
 * @param  {[String]} data [JSON字符串]
 * @return {[Object]}      [返回序列化之后的JSON对象]
 */
decoration.base.parseJson = function(data) {
    if (data && data.length > 0 && typeof(data) == 'string') {
        return JSON.parse(data);
    }
}

/**
 * [stringJson json字符串化]
 * @param  {[Object]} data [JSON对象]
 * @return {[String]}      [返回字符串后的JSON字符串]
 */
decoration.base.stringJson = function(data) {
    if (data && typeof(data) == 'object') {
        return JSON.stringify(data);
    }
}

/**
 * [getQueryString 获取地址栏的地址参数]
 * @param  {[String]} name [需要获取的参数名称]
 * @return {[String]}      [返回获取的参数值]
 * @return {[Null]}        [没有参数则返回null]
 */
decoration.base.getQueryString = function(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
}

/**
 * [cloneObj 对象深拷贝]
 * @param  {[Object]} obj [需要被深拷贝的对象]
 * @return {[Object]}     [返回深拷贝之后的对象]
 */
decoration.base.cloneObj = function(obj) {
    var str, newobj = obj.constructor === Array ? [] : {};

    if (typeof obj !== 'object') {
        return;
    } else if (window.JSON) {
        str = decoration.base.stringJson(obj),
            newobj = decoration.base.parseJson(str);
    } else {
        for (var i in obj) {
            newobj[i] = typeof obj[i] === 'object' ? cloneObj(obj[i]) : obj[i];
        }
    }

    return newobj;
}

/**
 * [generateComponentsNumber 生成组件ID]
 * @param  {[Object]} target [需要生成ID的元素DOM对象]
 * @return {[String]}        [随机生成新的ID]
 */
decoration.base.generateComponentsNumber = function(target) {
    var id = target.find('.m-components').attr('id'),
        pageNum = decoration.base.getQueryString('pageUuid'),
        type = target.find('.m-components').parents('.g-view').attr("data-compId"),
        genID = pageNum + '_' + type + '_' + Date.now();

    $.each(target.find('[id^="' + id + '"]'), function() {
        var getid = $(this).attr('id').replace(id, genID)
        $(this).attr('id', getid);
    })

    $.each(target.find('[href^="#' + id + '"]'), function() {
        if ($(this).attr('href') != undefined) {
            var getHref = $(this).attr('href').replace(id, genID);
            $(this).attr('href', getHref);
        } else {
            console.log("=========== no href ============")
        }
    })

    return genID;
}

/**
 * [generateComponentsParams 生成组件配置参数]
 * @param  {[type]} target [生成参数的目标元素]
 * @return {[type]}        [返回序列化后的配置参数]
 */
decoration.base.generateComponentsParams = function(target) {
    var params = target.find('.m-components').parents('.g-view').attr("data-params")

    return decoration.base.parseJson(params);
}


/**
 * [stringToDom 字符串Html转换成Dom]
 * @param  {[type]} html [服务端返回的字符串]
 * @return {[type]}      [返回解析成html类型]
 */
decoration.base.stringToDom = function(html) {
    if (html != "") {
        return $.parseHTML(html);
    }

}

/**
 * [validateSpecial 校验特殊字符]
 * @param  {[type]} msg     [错误信息数组]
 * @param  {[type]} value   [校验值]
 * @param  {[type]} message [校验信息]
 * @return {[type]}         [错误信息数组]
 */
decoration.base.validateSpecial = function(msg, value, message) {
    if (/[~^!*<>]/gi.test(value)) {
        msg.push(message);
    }
    return msg;
}

/**
 * [initLayout 初始化数据]
 * @return {[type]} [description]
 */
decoration.layout.initLayout = function() {
    var data = layouthistory;
    if (!data) {
        data = {};
        data.count = 0;
        data.list = [];
        data.pageModel = [];
    }

    //初始化data数据模型
    if (data.list.length > data.count) {
        for (var i = data.count; i < data.list.length; i++)
            data.list[i] = null;
        data.pageModel[i] = null;
    }

    layoutAreaHtml = $(layoutArea).html();

    data.list[data.count] = layoutAreaHtml;
    data.pageModel[data.count] = decoration.base.stringJson(pageModel);

    console.info(pageModel)

    data.count++;

    if (decoration.base.supportStorage()) {
        try {
            localStorage.setItem("layoutdata", JSON.stringify(data));
        } catch (e) {
            if (e.name === 'QUOTA_EXCEEDED_ERR' || e.name === 'NS_ERROR_DOM_QUOTA_REACHED') {
                data.list.shift();
                data.pageModel.shift();
            } else {
                data.list.shift();
                data.pageModel.shift();
            }
        }
    }

    layouthistory = data;

    decoration.layout.setDoStatus(data);

}

/**
 * [initComponentsName 初始化组件名称]
 */

decoration.layout.initComponentsName = function(){
    $.each($('.g-view'),function(){
        if($(this).attr('data-name')){
            var name = $(this).attr('data-name');
            $(this).siblings('.edit-content').find('.preview').find("span").text(name)
        }

    })
}
/**
 * [handleSaveLayout 更新布局]
 */
decoration.layout.handleSaveLayout = function() {
    var e = $(layoutArea).html();

    if (!stopsave && e != layoutAreaHtml) {
        stopsave++;
        layoutAreaHtml = e;
        decoration.layout.saveLayout();
        stopsave--;
    }
}

/**
 * [saveLayout 保存页面布局]
 * @return {[type]} [description]
 */
decoration.layout.saveLayout = function() {
    var data = layouthistory;

    if (!data) {
        data = {};
        data.count = 0;
        data.list = [];
        data.pageModel = [];
    }
    if (data.list.length > data.count) {
        for (var i = data.count; i < data.list.length; i++)
            data.list[i] = null;
        data.pageModel[i] = null;
    }

    layoutAreaHtml = $(layoutArea).html();

    data.list[data.count] = layoutAreaHtml;
    data.pageModel[data.count] = decoration.base.stringJson(pageModel);

    console.info(pageModel)

    data.count++;

    layer.msg(markedLang.doSuccess, {
        offset: '60px',
        time: 500
    });

    if (decoration.base.supportStorage()) {
        try {
            localStorage.setItem("layoutdata", JSON.stringify(data));
        } catch (e) {
            if (e.name === 'QUOTA_EXCEEDED_ERR' || e.name === 'NS_ERROR_DOM_QUOTA_REACHED') {
                data.list.shift();
                data.pageModel.shift();
            } else {
                data.list.shift();
                data.pageModel.shift();
            }
        }
    }

    layouthistory = data;

    decoration.layout.setDoStatus(data);

}

/**
 * [setDoStatus 撤销、重做按钮的状态修改]
 * @param {[Int]} data [全局计数器传入进去]
 */
decoration.layout.setDoStatus = function(data) {
    if (data.count > 1) {
        $('#undo').removeAttr('disabled');
        $('#redo').removeAttr('disabled');
    } else {
        $('#undo').attr('disabled', true);
        $('#redo').attr('disabled', true);
    }
}

/**
 * [undoLayout 撤销上一步]
 * @return {[type]} [返回当前是否为可撤销状态]
 */
decoration.layout.undoLayout = function() {
    var data = layouthistory;

    if (data) {

        if (data.count < 2) {
            return false;
        }

        layoutAreaHtml = data.list[data.count - 2];
        pageModel = decoration.base.parseJson(data.pageModel[data.count - 2]);

        data.count--;
        console.info(pageModel)

        layer.msg(markedLang.doSuccess, {
            offset: '60px',
            time: 500
        });

        $(layoutArea).html(layoutAreaHtml);
        if (decoration.base.supportStorage()) {
            try {
                localStorage.setItem("layoutdata", JSON.stringify(data));
            } catch (e) {
                if (e.name === 'QUOTA_EXCEEDED_ERR' || e.name === 'NS_ERROR_DOM_QUOTA_REACHED') {
                    data.list.shift();
                    data.pageModel.shift();
                } else {
                    data.list.shift();
                    data.pageModel.shift();
                }
            }
        }

        decoration.layout.setDoStatus(data);
        $('#redo').removeAttr('disabled');
        return true;
    }
    return false;
}

/**
 * [redoLayout 重做上一步]
 * @return {[type]} [返回当前是否为可重做状态]
 */
decoration.layout.redoLayout = function() {
    var data = layouthistory;

    if (data) {
        if (data.list[data.count] != undefined) {
            layoutAreaHtml = data.list[data.count];
            pageModel = decoration.base.parseJson(data.pageModel[data.count]);

            data.count++;

            console.info(pageModel)

            layer.msg(markedLang.doSuccess, {
                offset: '60px',
                time: 500
            });
            $(layoutArea).html(layoutAreaHtml);
            if (decoration.base.supportStorage()) {
                try {
                    localStorage.setItem("layoutdata", JSON.stringify(data));
                } catch (e) {
                    if (e.name === 'QUOTA_EXCEEDED_ERR' || e.name === 'NS_ERROR_DOM_QUOTA_REACHED') {
                        data.list.shift();
                        data.pageModel.shift();
                    } else {
                        data.list.shift();
                        data.pageModel.shift();
                    }
                }
            }

            decoration.layout.setDoStatus(data);
            return true;
        }
    }
    return false;
}

/**
 * [cleanData 清除页面布局]
 * @return {[type]} [description]
 */
decoration.layout.cleanData = function() {
    $(layoutArea).empty();
    // layouthistory = null;
    pageModel = [];

    // if (decoration.base.supportStorage()) {
    //     localStorage.removeItem("layoutdata");
    // }

    decoration.layout.saveLayout()
}

/**
 * [restoreData 重载数据]
 * @return {[type]} [description]
 */
decoration.layout.restoreData = function() {
    if (decoration.base.supportStorage()) {
        layouthistory = JSON.parse(localStorage.getItem("layoutdata"));
        if (!layouthistory) return false;
        layoutAreaHtml = layouthistory.list[layouthistory.count - 1];
        if (layoutAreaHtml) {
            layoutArea.html(layoutAreaHtml);
        }
    }
}

/**
 * [removeLoading 移除加载层]
 * @return {[type]} [description]
 */
decoration.layout.removeLoading = function() {
    $('.design-loading').remove();
}

/**
 * [containerAddAni 添加动画]
 * @return {[type]} [description]
 */
decoration.layout.containerAddAni = function() {
    $('.m-design-container').addClass('a-fadein');
}

/**
 * [initSidebar 初始化sideBar]
 * @return {[type]} [description]
 */
decoration.layout.initSidebar = function() {
    $('.m-compslist').on('click', '.compslist-item-hd', function() {
        $(this).parents('.compslist-litem').find('.compslist-item-bd').slideToggle(200);
    })
}

/**
 * [initContainer 初始化容器]
 * @return {[type]} [description]
 */
decoration.layout.initContainer = function() {
    decoration.do.rowDraggable(); //初始化行拖拽
    decoration.do.boxDraggable(); //初始化组件拖拽

    decoration.layout.renderEditBarHtml(); //渲染编辑条
    decoration.layout.sortable(); //初始化排序组件

    decoration.layout.removeLoading(); //移除loading加载层

    decoration.layout.containerAddAni(); //加载动画

    decoration.layout.initLayout(); //初始化页面布局
    decoration.layout.initComponentsName(); //初始化名称
    decoration.layout.initSidebar(); //初始化侧边栏

    //decoration.layout.autoSave();
}

/**
 * [getEditHtml 获取页面html]
 * @return {[Object]} [获取编辑器编辑容器区域内的html]
 */
decoration.layout.getEditHtml = function() {
    return $(layoutArea).html();
}

/**
 * [formatHtml 生成页面html]
 * @return {[type]} [返回格式化之后的html]
 */
decoration.layout.formatHtml = function() {
    var html = decoration.layout.getEditHtml();

    $('.download-layout').find('.content').html(" ");
    $('.download-layout').find('.content').append(html);

    $.each($('.download-layout').find('.content .lyrow'), function() {
        $(this).find('.column').removeClass('ui-sortable');
        $('.ui-draggable,.column').removeAttr('style');
        $(this).find('.edit-content').remove();
        $(this).find('.columnconfiguration').remove();
        $(this).find('.m-components-wrap').find('.g-view').removeAttr('data-params')

        if ($(this).find(".g-container").attr('data-style') != undefined) {
            var getStyle = $(this).find(".g-container").attr('data-style').split(',');
            rowParams[getStyle[0]] = getStyle[1];
            $(this).find(".g-container").css(rowParams);
        }
    })

    $.each($('.download-layout').find('.column'),function(){
        if ($(this).attr('data-style') != undefined) {
            var getStyle = $(this).attr('data-style').split(',');
            columnParams[getStyle[0]] = getStyle[1];
            $(this).css(columnParams);

        }
    })
    pageHtml = $('.download-layout').find('.content').html();

    return pageHtml;
}

/**
 * [autoSave 自动保存页面内容]
 * @return {[type]} [description]
 */
decoration.layout.autoSave = function() {
    setInterval(function() {
        decoration.do.pageSaveAjax(function(data) {
            return;
            // if (data == "true") {
            //     isSave = true;
            //     var autosave = layer.msg(timerSave/1000+'s自动保存', {
            //         offset: 'b',
            //         time: '1000'
            //     })
            // }
        });
    }, timerSave)
}

/**
 * [editBarHtml 编辑工具条的HTML]
 * @return {[String]} [返回编辑工具条的html]
 */
decoration.layout.editBarHtml = function() {
    var html = '<div class="edit-content f-cb">' +
        '   <div class="btn-group btn-group-xs f-fr">' +
        '       <a href="javascript:void(0)" class="drag btn btn-info" title="拖拽">' +
        '       <i class="iconfont icon-tuozhuai"></i>' +
        '       </a>' +
        '       <a href="javascript:void(0)" class="remove btn btn-danger" title="删除">' +
        '       <i class="iconfont icon-shanchu"></i>' +
        '       </a>' +
        '   </div>' +
        '   <div class="preview f-fl"><i class="iconfont icon-ai216"></i>  行' +
        '   </div>' +
        '</div>';

    return html;
}

/**
 * [editBarParamHtml 属性配置的HTML]
 * @return {[String]} [返回属性配置的html]
 */
decoration.layout.editBarParamHtml = function() {
    var html = '<a href="javascript:void(0)" class="configuration btn btn-primary" title="设置">' +
        '<i class="iconfont icon-shezhi"></i>' +
        '</a>';

    return html;
}

/**
 * [editRowBarParamHtml 行属性配置的HTML]
 * @return {[String]} [返回行属性配置的html]
 */
decoration.layout.editRowBarParamHtml = function() {
    var html = '<a href="javascript:void(0)" class="rowconfiguration btn btn-primary" title="设置">' +
        '<i class="iconfont icon-shezhi"></i>' +
        '</a>';

    return html;
}

/**
 * [editRowBarParamHtml 列属性配置的HTML]
 * @return {[String]} [返回行属性配置的html]
 */
decoration.layout.editColumnBarParamHtml = function() {
    var html = '<a href="javascript:void(0)" class="columnconfiguration btn btn-primary" title="设置">' +
        '<i class="iconfont icon-shezhi"></i>' +
        '</a>';

    return html;
}

/**
 * [renderEditBarHtml 初始化渲染组件工具条]
 * @return {[type]} [description]
 */
decoration.layout.renderEditBarHtml = function() {
    var renderHtmlTemplate = decoration.layout.editBarHtml(),
        renderHtmlParamTemplate = decoration.layout.editBarParamHtml(),
        renderHtmlRowParamTemplate = decoration.layout.editRowBarParamHtml(),
        renderHtmlColumnParamTemplate = decoration.layout.editColumnBarParamHtml();

    if ($('.g-view').length > 0) {
        $('.design-container .g-view').before(renderHtmlTemplate);
        $('.design-container .column').addClass('ui-sortable');
        $('.design-container .g-container,.column').removeAttr('style');
        $('.design-container .g-container').siblings('.edit-content').find('.btn-group').find('.remove').before(renderHtmlRowParamTemplate);
        $('.design-container .m-components-wrap').find('.edit-content').find('.btn-group').find('.remove').before(renderHtmlParamTemplate);
        $('.design-container .g-container').find('.column').append(renderHtmlColumnParamTemplate);

        for (var i in pageModel) {
            if (pageModel[i].hasOwnProperty('compName')) {
                $('#' + pageModel[i].compId).parents('.m-components-wrap').find('.edit-content').find('.preview').html('<i class="iconfont icon-zujian"></i> ' + pageModel[i].compName)
            } else {
                $('#' + pageModel[i].compId).parents('.m-components-wrap').find('.edit-content').find('.preview').html('<i class="iconfont icon-zujian"></i> 组件')
            }
        }
    }
}

/**
 * [sortableContainer 布局排序]
 * @return {[type]} [description]
 */
decoration.layout.sortableContainer = function() {
    $(".design-container").sortable({
        connectWith: ".column",
        handle: ".drag",
        opacity: .35,
        start: function(e, t) {
            if (!startdrag) stopsave++;
            startdrag = 1;
        },
        stop: function(e, t) {
            if (stopsave > 0) stopsave--;
            startdrag = 0;
            decoration.layout.saveLayout();
        }
    });
}

/**
 * [sortableColum 组件列排序]
 * @return {[type]} [description]
 */
decoration.layout.sortableColum = function() {
    $(".design-container .column").sortable({
        opacity: .35,
        connectWith: ".column",
        handle: ".drag",
        start: function(e, t) {
            if (!startdrag) stopsave++;
            startdrag = 1;
        },
        stop: function(e, t) {
            if (stopsave > 0) stopsave--;
            startdrag = 0;

            decoration.data.setPageModel(temporaryId, temporaryParams);
            decoration.layout.saveLayout()
        }
    });
}

/**
 * [sortable 初始化触发排序接受功能]
 * @return {[type]} [description]
 */
decoration.layout.sortable = function() {
    decoration.layout.sortableContainer();
    decoration.layout.sortableColum();
}

/**
 * [showCompsData 属性配置数据回显]
 * @param  {[String]} id [组件id]
 * @return {[type]}    [description]
 */
decoration.layout.showCompsData = function(id) {
    var getData = decoration.data.getCompsParamsData();

    for (var i in getData) {
        switch (getData[i].type) {
            case 'text':
                $(".design-container").find('#' + id + '_' + getData[i].target).text(getData[i].value)
                break;
            case 'src':
                $(".design-container").find('#' + id + '_' + getData[i].target).prop('src', getData[i].value)
                break;
            case 'val':
                $(".design-container").find('#' + id + '_' + getData[i].target).val(getData[i].value)
                break;
            case 'href':
                $(".design-container").find('#' + id + '_' + getData[i].target).prop('href', getData[i].value)
                break;
            case 'html':
                $(".design-container").find('#' + id + '_' + getData[i].target).html(getData[i].value)
                break;
            default:
        }
    }
}

/**
 * [downloadLayout 下载布局]
 * @return {[type]} [description]
 */
decoration.layout.downloadLayout = function() {}


/**
 * [init 初始化数据]
 * @return {[type]} [description]
 */
decoration.data.init = function() {
    decoration.data.getLayoutsList();
    decoration.data.getComponentsList();
    decoration.data.getPageModel();
}

/**
 * [getLayoutsList 获取布局列表]
 * @return {[type]} [description]
 */
decoration.data.getLayoutsList = function() {

    $.ajax({
        type: "GET",
        url: ContextPath + "/platform/dec/page/layout/getPageLayouts",
        data:{
            versionType:$('#versionType').val()
        },
        success: function(data) {
            var dataList = {};
            dataList.list = decoration.base.parseJson(data);
            template.config('escape', false);
            var html = template('pageLayouts', dataList);
            $('#pageLayoutsList').html(html);
            decoration.do.rowDraggable();
        }
    });
}

/**
 * [getComponentsList 获取组件列表]
 * @return {[type]} [description]
 */
decoration.data.getComponentsList = function() {
    var url=ContextPath+"/platform/dec/component/getComps";
    console.log(url);
    $.ajax({
        type: "GET",
        url:url,
        data:{
            versionType:$('#versionType').val()
        },
        success: function(data) {
            var dataList = {};
            dataList.list = decoration.base.parseJson(data);
            console.log( dataList.list);
            template.config('escape', false);
            var html = template('test', dataList);
            $('#estRows').html(html);

            decoration.do.boxDraggable();
        }
    });
}

/**
 * [getComponentsInfo 获取单个组件信息]
 * @param  {[type]} compId [单个组件id]
 * @return {[type]}        [description]
 */
decoration.data.getComponentsInfo = function(compId) {
    $.ajax({
        type: "GET",
        url: ContextPath + "/webpage/getCompInfo",
        data: {
            compId: compId
        },
        success: function(data) {
            console.info(data)
        }
    });
}

/**
 * [getPageModel 获取pageModel]
 * @return {[type]} [description]
 */
decoration.data.getPageModel = function() {
    var getPageModelJson = $('#pageModelJson').val();

    if (getPageModelJson != '') {
        getPageModelJson = decoration.base.parseJson(getPageModelJson);

        pageModel = getPageModelJson.comps;
    }
}

/**
 * [setPageModel 设置页面Model]
 * @param {[type]} id   [组件id]
 * @param {[type]} data [对应组件传入的属性配置json]
 */
decoration.data.setPageModel = function(id, data) {

    var datalist = {
        compId: id,
        needAsyncInit: true
    }

    if (typeof(data) == 'undefined') {
        data = datalist;
    }

    for (var i in pageModel) {

        if (pageModel[i]['compId'] == data['compId']) {
            $.extend(true, pageModel[i], data);
            return;
        }
    }

    if (data['compId'] != id) {
        data['compId'] = id;
    }

    if (data['compId'] != '') {
        pageModel.push(data)
    }
}

/**
 * [deletePageModel 删除组件设置Model]
 * @param  {[type]} id [组件id]
 * @return {[type]}    [description]
 */
decoration.data.deletePageModel = function(id) {
    for (var i in pageModel) {
        if (pageModel[i]['compId'] == id) {
            pageModel.splice(i, 1);
            break;
        }
    }
}


/**
 * [getCompsParams 获取组件的属性参数]
 * @return {[type]} [description]
 */
decoration.data.getCompsParams = function() {
    if (typeof(componentsParam) == 'object') {
        return componentsParam.saveParamData()
    }

}

/**
 * [setCompsParams 设置组件属性参数]
 * @param {[type]} paramJson [组件属性参数json]
 */
decoration.data.setCompsParams = function(paramJson) {
    if (typeof(componentsParam) == 'object') {
        return componentsParam.initParamData(paramJson)
    }
}

/**
 * [getCompsParamsData 生成组件回显数据]
 * @return {[Object]} [返回组件配置json]
 */
decoration.data.getCompsParamsData = function() {
    if (typeof(componentsParam) == 'object') {
        if (componentsParam.hasOwnProperty("showParamData")) {
            return componentsParam.showParamData()
        } else {
            return [];
        }
    }
}

//业务逻辑处理部分
decoration.do = {
    /**
     * [init 业务逻辑初始化]
     * @return {[type]} [description]
     */
    init: function() {
        $("body").css("min-height", $(window).height() - 90);
        // $(".design-container").css("min-height", $(window).height() - 160);

        decoration.data.init();
        decoration.layout.initContainer();

        this.removeElm();
        this.doHtml();
        this.showConfigModal();
        this.pageSave();
        this.downHtml();
        this.preview();
        this.saveNewPage();
        this.osavePage();
        this.transversionPage();
        this.addNewPage();
        this.selectPages();
        this.cleanHtml();
        this.rowConfigModal();
        this.columnConfigModal();
    },
    /**
     * [selectPages 选择页面进行切换]
     * @return {[type]} [description]
     */
    selectPages: function() {
        $('#select_new_page').select2({
            minimumResultsForSearch: -1
        });

        $('.select_new_page').on('change', function() {
            var val = $(".select_new_page").val();

            if (val != "") {
                window.location.href = ContextPath + "/platform/dec/templates/manager/toPageDefine?templateUuid=" + decoration.base.getQueryString("templateUuid") + "&pageUuid=" + val + ""
            }
        })
    },
    /**
     * [rowDraggable 行拖拽]
     * @return {[type]} [description]
     */
    rowDraggable: function() { //行拖拽
        $(".m-design-sidebar .g-dgrow").draggable({
            connectToSortable: ".design-container",
            helper: "clone",
            handle: ".drag",
            opacity: '.35',
            zIndex: 100,
            scroll: false,
            start: function(e, t) {
                if (!startdrag) stopsave++;
                startdrag = 1;
            },
            drag: function(e, t) {
                t.helper.width("100%")
            },
            stop: function(e, t) {
                if (stopsave > 0) stopsave--;
                startdrag = 0;

                decoration.layout.sortableColum();
                decoration.do.boxDraggable();
            }
        });
    },
    /**
     * [boxDraggable 组件拖拽]
     * @return {[type]} [description]
     */
    boxDraggable: function() { //组件拖拽
        $(".m-design-sidebar .m-components-wrap").draggable({
            connectToSortable: ".column",
            helper: "clone",
            handle: ".drag",
            opacity: '.35',
            zIndex: 100,
            scroll: false,
            start: function(e, t) {
                temporaryId = decoration.base.generateComponentsNumber($(this))
                temporaryParams = decoration.base.generateComponentsParams($(this))

                if (!startdrag) stopsave++;
                startdrag = 1;
            },
            drag: function(e, t) {
                t.helper.width("100%")
            }
        });
    },
    /**
     * [columnSort 组件行排序]
     * @return {[type]} [description]
     */
    columnSort: function() { //组件行排序
        $(".design-container, .design-container .column").sortable({
            connectWith: ".column",
            opacity: .35,
            handle: ".drag"
        });
    },
    /**
     * [doHtml 撤销和重做功能]
     * @return {[type]} [description]
     */
    doHtml: function() { //撤销和重做功能
        $('#undo').click(function() {
            stopsave++;
            decoration.layout.undoLayout();
            decoration.layout.sortableColum();
            stopsave--;
        });

        $('#redo').click(function() {
            stopsave++;
            decoration.layout.redoLayout();
            decoration.layout.sortableColum();
            stopsave--;
        });
    },
    /**
     * [removeElm 移除拖拽的组件]
     * @return {[type]} [description]
     */
    removeElm: function() { //移除拖拽的组件
        var _self = this;
        $(layoutArea).on("click", ".remove", function(e) {
            e.preventDefault();
            var __self = this;

            if ($(this).parent().parent().parent().hasClass('lyrow')) {
                if ($(this).parent().parent().parent().find('.m-components-wrap').length == 0) {
                    layer.confirm(markedLang.isDeleteRows, function(index) {
                        $(__self).parent().parent().parent().remove();
                        decoration.layout.saveLayout();
                    })
                } else {
                    layer.confirm(markedLang.isComponentsDelete, function(index) {
                        $.each($(__self).parent().parent().parent().find('.m-components-wrap'), function() {
                            var id = $(this).find('.g-view').find('.m-components').attr('id');
                            decoration.data.deletePageModel(id);
                            $(__self).parent().parent().parent().remove();
                        })

                        decoration.layout.saveLayout();

                        layer.close(index);
                    })
                }

            } else {
                layer.confirm(markedLang.isDeleteComps, function(index) {
                    var compId = $(__self).parents('.m-components-wrap').find('.g-view').find('.m-components').attr('id');

                    if (compId != "") {
                        decoration.data.deletePageModel(compId);
                    }

                    $(__self).parent().parent().parent().remove();
                    decoration.layout.saveLayout();
                })
            }
        })
    },
    /**
     * [rowConfigModal 开启行属性配置弹层]
     * @return {[type]} [description]
     */
    rowConfigModal: function() { //开启行属性配置弹层
        var __self = this;
        $(document).on('click', '.rowconfiguration', function() {
            var _self = this;
            var parentTag = $(_self).parent().parent().siblings('.g-container');
            $('#rowSetModal').modal();
            if (parentTag.attr('data-style') != undefined) {
                $('#rowSetModal').attr('data-style', parentTag.attr('data-style'))
            }


            $(".j_rowconfig_save").off().on('click', function() {
                var setStyle = ["backgroundColor", $('#rowbgcolor').val()]

                parentTag.attr('data-style', setStyle.join());

                decoration.layout.saveLayout();

                __self.pageSaveAjax(function(data) {
                    // if (data == "true") {
                    //     layer.msg("保存成功", {
                    //         icon:'1',
                    //         time: 1000,
                    //         offset:"rb"
                    //     });
                    // }
                });

                $('#rowSetModal').modal('hide')
            })
        })

        $('#rowSetModal').on('shown.bs.modal', function() {
            if ($('#rowSetModal').attr('data-style') != undefined) {
                var getStyle = $('#rowSetModal').attr('data-style').split(',');
                $('#chooseColor').val(getStyle[1])
                $("#rowbgcolor").val(getStyle[1])
            }

            $("#rowbgcolor").on('blur', function() {
                $('#chooseColor').val($(this).val())
            })

            $('#chooseColor').on('change', function() {
                $("#rowbgcolor").val($(this).val())
            })
        })


    },
    /**
     * [columnConfigModal 开启列属性配置弹层]
     * @return {[type]} [description]
     */
    columnConfigModal: function() { //开启列属性配置弹层
        var __self = this;
        $(document).on('click', '.columnconfiguration', function() {
            var _self = this;
            var parentTag = $(_self).parent();
            $('#columnSetModal').modal();

            if (parentTag.attr('data-style') != undefined) {
                $('#columnSetModal').attr('data-style', parentTag.attr('data-style'))
            }


            $(".j_columnconfig_save").off().on('click', function() {
                var setStyle = ["width", $('#columnwidth').val()]

                parentTag.attr('data-style', setStyle.join());

                decoration.layout.saveLayout();

                __self.pageSaveAjax(function(data) {
                    // if (data == "true") {
                    //     layer.msg("保存成功", {
                    //         icon:'1',
                    //         time: 1000,
                    //         offset:"rb"
                    //     });
                    // }
                });

                $('#columnSetModal').modal('hide')
            })

        })

        $('#columnSetModal').on('shown.bs.modal', function() {
            $('#columnwidth').val('')
            if ($('#columnSetModal').attr('data-style') != undefined) {
                var getStyle = $('#columnSetModal').attr('data-style').split(',');
                $('#columnwidth').val(getStyle[1])
            }
        })


    },
    /**
     * [showConfigModal 开启属性配置弹层]
     * @return {[type]} [description]
     */
    showConfigModal: function() { //开启属性配置弹层
        var __self = this;
        $(document).on('click', '.configuration', function() {
            var id = $(this).parents('.m-components-wrap').find('.g-view').attr('data-compId'),
                newId = $(this).parents('.m-components-wrap').find('.g-view').find('.m-components').attr('id');

            $("#editorModal").attr('data-newcompId', newId);
            $("#editorModal").attr('data-compId', id);
            $("#editorModal").modal();
        })

        $("#editorModal").on('show.bs.modal', function() {
            $("#editorModal").find('.modal-body').html('<div class="loadings f-tac"><img src="/assets/decorate/pc/decoratecommon/images/loading.gif" alt=""></div>');


            var _self = this,
                data = {
                    compId: $(_self).attr('data-compid'),
                    pageJson: {}
                },
                compId = $("#editorModal").attr('data-newcompId');

            for (var i in pageModel) {
                if (pageModel[i]['compId'] == compId) {
                    data.pageJson = decoration.base.stringJson(pageModel[i]);
                    break;
                }
            }

            $.ajax({
                type: "POST",
                cache:false,
                url: ContextPath + "/platform/dec/component/getCompParamsDefine",
                data: data,
                success: function(data) {
                    $(_self).find('.modal-body').html('<div class="loadings f-tac"><img src="/assets/decorate/pc/decoratecommon/images/loading.gif" alt=""></div>');
                    $(_self).find('.modal-body').html(data);
                }
            });
        })

        $(document).on('click', '.j_save', function() {
            var compNewId = $("#editorModal").attr('data-newcompId');
            var _self = this;
            getParamData = decoration.data.getCompsParams();
            if (getParamData.hasOwnProperty('success') && !getParamData.success) {
                layer.msg(getParamData.msg, {
                    time: 5000
                });
            } else {
                getParamData['compId'] = compNewId;
                decoration.data.setPageModel(compNewId, getParamData);
                decoration.layout.showCompsData(compNewId);
                decoration.layout.saveLayout();

                $("#editorModal").find('.modal-body').html('<div class="loadings f-tac"><img src="/assets/decorate/pc/decoratecommon/images/loading.gif" alt=""></div>');
                $('#editorModal').modal('hide')


                __self.pageSaveAjax(function(data) {
                    // if (data == "true") {
                    //     layer.msg("保存成功", {
                    //         icon:'1',
                    //         time: 1000,
                    //         offset:"rb"
                    //     });
                    // }
                });
            }
        })
    },
    /**
     * [pageSave 设计器页面保存]
     * @return {[type]} [description]
     */
    pageSave: function() { //设计器页面保存
        var _self = this;
        $('#save').on('click', function() {
            _self.pageSaveAjax(function(data) {
                if (data == "true") {
                    isSave = true;
                    layer.msg(markedLang.saveSuccess, { icon: 1, time: 1000 })
                }
            });
        })
    },
    /**
     * [pageSaveAjax 设计器页面保存ajax方法]
     * @param  {Function} callback [回调函数]
     * @return {[type]}            [description]
     */
    pageSaveAjax: function(callback) {
        var dataJson = {
            pageUuid: decoration.base.getQueryString('pageUuid'),
            pageType: 1,
            comps: pageModel
        }
        var data = {
            saveHtml: decoration.layout.formatHtml(),
            pageJson: decoration.base.stringJson(dataJson),
            pageUuid: decoration.base.getQueryString('pageUuid')
        }

        if ($(".design-container .m-components-wrap").length <= 0) {
            layer.msg(markedLang.noComponents, {
                offset: '60px',
                time: 1000
            });
        } else {
            $.ajax({
                type: "POST",
                url: ContextPath + "/platform/dec/templates/manager/save",
                data: data,
                success: callback,
                error: function() {
                    layer.msg('错误', { icon: 2, time: 1000 })
                }
            });
        }
    },
    /**
     * [downHtml 格式化html]
     * @return {[type]} [description]
     */
    downHtml: function() { //格式化html
        $('#button-download-modal').on('click', function() {
            var html = decoration.layout.getEditHtml();

            $('.download-layout').find('.content').html("");
            $('.download-layout').find('.content').append(html);

            decoration.layout.formatHtml();

            pageHtml = $('.download-layout').find('.content').html();
        })
    },
    /**
     * [preview 预览页面]
     * @return {[type]} [description]
     */
    preview: function() { //预览页面
        $('#preview').on('click', function() {
            var _self = this;
            var dataJson = {
                pageUuid: decoration.base.getQueryString('pageUuid'),
                pageType: 1,
                comps: pageModel
            }

            var data = {
                saveHtml: decoration.layout.formatHtml(),
                pageJson: decoration.base.stringJson(dataJson),
                pageUuid: decoration.base.getQueryString('pageUuid')
            }

            if ($(".design-container .m-components-wrap").length <= 0) {
                layer.msg(markedLang.noComponents, {
                    offset: '60px',
                    time: 1000
                });
            } else {
                var newWindow = window.open();
                $.ajax({
                    type: "POST",
                    url: ContextPath + "/platform/dec/templates/manager/save",
                    data: data,
                    success: function(data) {
                        if (data == "true") {
                            newWindow.location.href = $(_self).attr('data-href')
                        }
                    }
                });
            }
        })
    },
    /**
     * [saveNewPage 另存为新版本]
     * @return {[type]} [description]
     */
    saveNewPage: function() { //另存为新版本
        var _self = this;
        $("#savenew").on('click', function() {
            _self.pageSaveAjax(function(data) {
                if (data == "true") {
                    $('#saveNewModal').modal();
                }
            });
        })

        $('.j_newpage_save').on('click', function() {
            var data = {
                templateUuid: decoration.base.getQueryString('templateUuid'),
                pageUuid: decoration.base.getQueryString('pageUuid'),
                versionDescribe: $('#versionDescribe').val()
            }

            if ($('#versionDescribe').val() != "") {
                $.ajax({
                    type: "GET",
                    url: ContextPath + "/platform/dec/templates/manager/saveAsNewVersion",
                    data: data,
                    success: function(data) {
                        $('#saveNewModal').modal('hide');
                        layer.msg(markedLang.saveSuccess, { icon: 1, time: 1000 }, function() {
                            window.location.reload();
                        })
                    }
                });
            } else {
                layer.msg(markedLang.saveNewPage, { icon: 2, time: 1000 })
            }
        })
    },
    /**
     * [osavePage 另存为]
     * @return {[type]} [description]
     */
    osavePage: function() { //另存为
        var _self = this;
        $("#osave").on('click', function() {
            _self.pageSaveAjax(function(data) {
                if (data == "true") {
                    $('#osaveModal').modal();
                }
            });
        })

        $('#osaveModal').on('show.bs.modal', function() {

            $.ajax({
                type: "GET",
                url: ContextPath + "/platform/dec/templates/manager/getPageTypes",
                success: function(data) {
                    var dataList = {};
                    dataList.list = data;
                    var html = template('newPageoption', dataList);
                    $('#osavenewPagetype').html(html);
                }
            });

        })

        $('.j_osave').on('click', function() {
            var data = {
                templateUuid: decoration.base.getQueryString('templateUuid'),
                pageUuid: decoration.base.getQueryString('pageUuid'),
                pageName: $('#pageName').val(),
                description: $('#description').val(),
                pageFileName: $('#PageFileName').val(),
                pageType: $('#osavenewPagetype').val()
            }

            if ($('#pageName').val() != "" && $('#description').val() != "") {
                $.ajax({
                    type: "GET",
                    url: ContextPath + "/platform/dec/templates/manager/saveAsNewPage",
                    data: data,
                    success: function(data) {
                        layer.msg(markedLang.saveSuccess, { icon: 1, time: 1000 }, function() {
                            window.location.href = ContextPath + data;
                        })
                        $('#saveNewModal').modal('hide');
                    },
                });
            } else {
                layer.msg(markedLang.osavePage, { icon: 2, time: 1000 })
            }
        })
    },
    /**
     * [transversionPage 切换版本]
     * @return {[type]} [description]
     */
    transversionPage: function() { //切换版本
        $("#transversion").on('click', function() {
            $('#transversionModal').modal();
        })

        $('#transversionModal').on('show.bs.modal', function() {
            var data = {
                pageUuid: decoration.base.getQueryString('pageUuid')
            }
            $.ajax({
                type: "GET",
                url:ContextPath +"/platform/dec/templates/manager/getPageVersions",
                data: data,
                success: function(data) {
                    console.log(data);
                    var dataList = {};
                    dataList.transversionlist = data;

                    var html = template('transversionTable', dataList);

                    $('#transversionTableList').html(html);
                }
            });

        })

        $(document).on('click', '.trans_t', function() {
            var id = $(this).attr('data-id'),
                data = {
                    templateUuid: decoration.base.getQueryString('templateUuid'),
                    pageUuid: decoration.base.getQueryString('pageUuid'),
                    subPageUuid: id
                }
            $.ajax({
                type: "GET",
                url: ContextPath +"/platform/dec/templates/manager/switchPageVersion",
                data: data,
                success: function(data) {
                    layer.msg(markedLang.saveSuccess, { icon: 1, time: 1000 }, function() {
                        window.location.reload();
                    })
                }
            });
        })

        $(document).on('click', '.trans_d', function() {
            var id = $(this).attr('data-id'),
                isUsing = $(this).parents('tr').attr('data-using'),
                data = {
                    subPageUuid: id
                }

            if (isUsing == "1") {
                layer.msg(markedLang.isVersionDelete, { icon: 2, time: 1000 })
            } else {
                $.ajax({
                    type: "POST",
                    url:  ContextPath +"/platform/dec/templates/manager/deletePageVersion",
                    data: data,
                    success: function(data) {
                        if (data == "false") {
                            layer.msg(markedLang.isVersionDelete, { icon: 2, time: 1000 })
                        } else {
                            layer.msg(markedLang.doSuccess, { icon: 1, time: 1000 }, function() {
                                window.location.reload();
                            })
                        }

                    }
                });
            }
        })
    },
    /**
     * [addNewPage 添加新页面]
     */
    addNewPage: function() {
        $("#newpage").on('click', function() {
            $('#pageNewModal').modal();
        })

        $('#pageNewModal').on('show.bs.modal', function() {

            $.ajax({
                type: "GET",
                url: ContextPath + "/platform/dec/templates/manager/getPageTypes",
                success: function(data) {
                    var dataList = {};
                    dataList.list = data;
                    var html = template('newPageoption', dataList);
                    $('#newPagetype').html(html);
                }
            });

        })

        $('.j_addpage_save').on('click', function() {
            var data = {
                templateUuid: decoration.base.getQueryString('templateUuid'),
                pageName: $('#newPageName').val(),
                pageType: $('#newPagetype').val(),
                description: $('#newPagedescription').val(),
                pageFileName: $("#newPageFileName").val()
            }

            if ($('#newPageName').val() != "" && $('#newPagedescription').val() != "") {
                $.ajax({
                    type: "POST",
                    url: ContextPath + "/platform/dec/templates/manager/addNewPage",
                    data: data,
                    success: function(data) {
                        layer.msg(markedLang.saveSuccess, { icon: 1, time: 1000 }, function() {
                            window.location.href = ContextPath + data;
                        })
                        $('#saveNewModal').modal('hide');
                    }
                });
            } else {
                layer.msg(markedLang.osavePage, { icon: 2, time: 1000 })
            }
        })
    },
    /**
     * [cleanHtml 清空页面]
     * @return {[type]} [description]
     */
    cleanHtml: function() {
        $("#cleanhtml").on('click', function() {
            var clean = layer.confirm("确定清空所有内容?且不可撤销", function() {
                decoration.layout.cleanData();
                layer.close(clean);
            })
        })
    }

}
$(function() {
    var ue = UE.getEditor('content');
    decoration.do.init();
})
