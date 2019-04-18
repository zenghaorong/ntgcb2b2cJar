"use strict";

//订单中心轮播
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
                title:["删除订单","font-size:18px;color:#ba9963;font-weight:600"],
                btn:["确定","取消"],
                shadeClose:!0,
                btnAlign:"c",
                scrollbar:!1
            }
        )
    });
    var page = {};
    var id = "";
    var status= 0;//当前要查询的订单状态
    var order = {
        init:function () {
            this.load(1);
        },
        load:function (pageNo) {
            var param={
                "pageNo": pageNo,
                "status": status
            }
            if(id != null && id != ""){
                param.id = id;
            }
            $.post("/member/order/data",param,function (data) {
                $("#content").html(data);
                page = {
                    pageCount : $("input[name='pageTotal']").val(),
                    current : pageNo,
                    backFn : function(pageNo){
                        order.load(pageNo,status);
                    }
                }
                $(".page").createPage(page);
            });
        }
    };
    $(document).ready(function () {
        order.init();
    });
    //查询方法
    function query(val) {
        status = val;
        order.load(1);
    }
    
    function search() {
        id = $("input[name='id']").val();
        order.load(1);
    }

//确认收货
$(document).on("click",".receipt",function(){
    var orderId = $(this).data("val");
    layer.open({
        type: 1,
        content: $('#ly-receipt'),
    	offset: '200px',
        area: '560px',
        title: ['提示', 'font-size:18px;color:#ba9963;font-weight:600'],
        btn: ['确认收到', '暂时没有'],
        yes: function(index, layero){
            $.post("/member/order/takeDelivery",{"orderId":orderId},function (result) {
                layer.close(index);
                if(result.code == 0){
                    query(status);
                }
            })
        },
        btn2:function (index, layero) {
            layer.close(index);
        },
        shadeClose: true,
        btnAlign: 'c',
        scrollbar: false,
    });

})

//取消订单
$(document).on("click",".cancelOrder",function(){
    var orderId = $(this).data("val");
    layer.open({
        type: 1,
        content: $('#cancelOrder'),
    	offset: '200px',
        area: '560px',
        title: ['订单取消申请', 'font-size:18px;color:#ba9963;'],
        btn: ['确定取消', '暂时不取消'],
        yes: function(index, layero){
            $.post("/member/order/update",{"id":orderId, "dictId":$("#dictId").val()},function (result) {
                layer.close(index);
                if(result.code == 0){
                    query(status);
                }
            })
        },
        btn2:function (index, layero) {
            layer.close(index);
        },
        shadeClose: true,
        btnAlign: 'c',
        scrollbar: false,
    });

})