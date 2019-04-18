"use strict";
jQuery(".picScroll-left").slide(
    {
        titCell:".hd ul",
        mainCell:".bd ul",
        autoPage:!0,
        effect:"left",
        autoPlay:!0,
        scroll:5,
        vis:5,
        easing:"easeOutCirc"
    }),
    $(document).on("click",".icon-delete",function(){
        layer.open(
            {
                type:1,
                content:$("#ly-delete"),
                area:"560px",
                title:["删除售后申请单","font-size:18px;color:#ba9963;font-weight:600"],
                btn:["确定","取消"],
                shadeClose:!0,
                btnAlign:"c",
                scrollbar:!1
            }
        )
    });
    var page = {};
    var id = "";
    var applyType= "";//售后类型
    var orderAfter = {
        init:function () {
            this.load(1);
        },
        load:function (pageNo) {
            var param={
                "pageNo": pageNo,
                "pageSize":10,
                "applyType": applyType
            }
            if(id != null && id != ""){
                param.id = id;
            }
            $.post("/member/orderAfter/data",param,function (data) {
                $("#content").html(data);
                page = {
                    pageCount : $("input[name='pageTotal']").val(),
                    current : pageNo,
                    backFn : function(pageNo){
                        orderAfter.load(pageNo,applyType);
                    }
                }
                $(".page").createPage(page);
            });
        }
    };
    $(document).ready(function () {
        orderAfter.init();
    });
    //查询方法
    function query() {
        applyType = $("#applyType").val();
        orderAfter.load(1);
    }
    
    function search() {
        id = $("input[name='id']").val();
        orderAfter.load(1);
    }
