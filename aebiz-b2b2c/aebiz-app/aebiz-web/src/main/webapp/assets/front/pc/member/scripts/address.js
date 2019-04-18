"use strict";
$(document).on("click", ".default", function () {
    $(this).parents(".item-list").addClass("active default").siblings().removeClass("active default")
}), $(document).on("click", "button.add", function () {
    var indexAdd = layer.open({
        type: 1,
        content: $("#add-address"),
        area: "840px",
        title: ["新增地址", "font-size:18px;color:#ba9963;font-weight:600"],
        btn: ["确定", "取消"]
        ,yes: function(index, layero){
        //按钮【按钮一】的回调
            if(checkAddForm()){
                layer.close(indexAdd);
            }else{
                return false
            }
        }
        ,btn2: function(index, layero){
            //按钮【按钮二】的回调
            addressData()
            //return false //开启该代码可禁止点击该按钮关闭
        }
        ,cancel: function(){
            //右上角关闭回调
            addressData()
            //return false //开启该代码可禁止点击该按钮关闭
        },
        shadeClose: !0,
        btnAlign: "c",
        scrollbar: !1
    })
}), $(document).on("click", "span.edit", function () {
    var indexEdit = layer.open({
        type: 1,
        content: $("#edit-address"),
        area: "840px",
        title: ["编辑地址", "font-size:18px;color:#ba9963;font-weight:600"],
        btn: ["确定", "取消"]
        ,yes: function(index, layero){
            //按钮【按钮一】的回调
            if(checkEditForm()){
                layer.close(indexEdit);
            }else{
                return false
            }
        }
        ,btn2: function(index, layero){
            //按钮【按钮二】的回调
            addressData()
            //return false //开启该代码可禁止点击该按钮关闭
        }
        ,cancel: function(){
            //右上角关闭回调
            addressData()
            //return false //开启该代码可禁止点击该按钮关闭
        },
        shadeClose: !0,
        btnAlign: "c",
        scrollbar: !1
    })
});
