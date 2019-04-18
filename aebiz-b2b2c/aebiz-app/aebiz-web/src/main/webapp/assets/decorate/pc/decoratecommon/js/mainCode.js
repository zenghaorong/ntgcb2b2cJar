/*
 * @Author: wenyu
 * @Date:   2016-12-13 10:19:22
 * @Last Modified by:   wenyu
 * @Last Modified time: 2017-01-19 21:40:57
 */

'use strict';

var decoration = {},
    layouthistory,//保存ls中的操作步骤
    layoutArea = '#layoutContent',//设计器目标元素
    currentDocument = null,//当前文档对象
    timerSave = 60 * 1000,//自动保存时间
    stopsave = 0,//
    startdrag = 0,//
    layoutAreaHtml = $(layoutArea).html(),//编辑区域的html
    currenteditor = null,
    temporaryId = '',//临时组件id
    temporaryParams = {},//临时组件配置参数
    rowParams = {},//行属性,
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
 * [initContainer 初始化容器]
 * @return {[type]} [description]
 */
decoration.layout.initContainer = function() {

    decoration.layout.removeLoading(); //移除loading加载层

    decoration.layout.containerAddAni(); //加载动画

    //decoration.layout.initLayout(); //初始化页面布局

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
 * [init 初始化数据]
 * @return {[type]} [description]
 */
decoration.data.init = function() {
    decoration.data.getPageModel();
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

        this.pageSave();
        this.downHtml();
        this.preview();
        this.selectPages();
        this.preview();
        this.saveNewPage();
        this.osavePage();
        this.transversionPage();
    },
    /**
     * [selectPages 选择页面进行切换]
     * @return {[type]} [description]
     */
    selectPages: function() {
        $('#select_new_page').select2({
            minimumResultsForSearch: -1
        });

        $('#select_new_page').on('change', function() {
            var val = $("#select_new_page").val();

            if (val != "") {
                window.location.href = ContextPath + "platform/dec/templates/manager/toPageDefineByCode?templateUuid=" + decoration.base.getQueryString("templateUuid") + "&pageUuid=" + val + ""
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
            saveHtml: editor.getValue(),
            pageJson: decoration.base.stringJson(dataJson),
            pageUuid: decoration.base.getQueryString('pageUuid')
        }

        $.ajax({
            type: "POST",
            url: ContextPath + "/platform/dec/templates/manager/save",
            data: data,
            success: callback,
            error: function() {
                layer.msg('错误', { icon: 2, time: 1000 })
            }
        });
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
                saveHtml: editor.getValue(),
                pageJson: decoration.base.stringJson(dataJson),
                pageUuid: decoration.base.getQueryString('pageUuid')
            }

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
                    dataList.list = decoration.base.parseJson(data);

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
                url: ContextPath + "/platform/dec/templates/manager/getPageVersions",
                data: data,
                success: function(data) {
                    var dataList = {};
                    dataList.transversionlist = decoration.base.parseJson(data);

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
                url: ContextPath + "/platform/dec/templates/manager/switchPageVersion",
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
                    url: ContextPath + "/platform/dec/templates/manager/deletePageVersion",
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
     * [downHtml 格式化html]
     * @return {[type]} [description]
     */
    downHtml: function() { //格式化html
        $('#button-download-modal').on('click', function() {
            var html = decoration.layout.getEditHtml();

            $('.download-layout').find('.content').html("");
            $('.download-layout').find('.content').append(html);

            editor.getValue();

            pageHtml = $('.download-layout').find('.content').html();
        })
    },
    /**
     * [preview 预览页面]
     * @return {[type]} [description]
     */

}

$(function() {
    decoration.do.init();
})
