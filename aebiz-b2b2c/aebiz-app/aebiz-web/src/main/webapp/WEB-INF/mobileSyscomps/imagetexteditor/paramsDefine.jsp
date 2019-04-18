<!doctype html>
<html>
<head>
	<script type="text/javascript">
        window.UEDITOR_CONFIG.toolbars=[[
            'fullscreen', 'source', '|', 'undo', 'redo', '|',
            'bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'superscript', 'subscript', 'removeformat', 'formatmatch', 'autotypeset', 'blockquote', 'pasteplain', '|', 'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist', 'selectall', 'cleardoc', '|',
            'rowspacingtop', 'rowspacingbottom', 'lineheight', '|',
            'customstyle', 'paragraph', 'fontfamily', 'fontsize', '|',
            'directionalityltr', 'directionalityrtl', 'indent', '|',
            'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', '|', 'touppercase', 'tolowercase', '|',
            'link', 'unlink', 'anchor', '|', 'imagenone', 'imageleft', 'imageright', 'imagecenter', '|',
            'simpleupload', 'emotion', 'scrawl', 'map', 'gmap', 'insertframe', 'insertcode', 'webapp', 'pagebreak', 'template', 'background', '|',
            'horizontal', 'date', 'time', 'spechars', 'snapscreen', 'wordimage', '|',
            'inserttable', 'deletetable', 'insertparagraphbeforetable', 'insertrow', 'deleterow', 'insertcol', 'deletecol', 'mergecells', 'mergeright', 'mergedown', 'splittocells', 'splittorows', 'splittocols', 'charts', '|',
            'print', 'preview', 'searchreplace', 'drafts'
        ]]
        var ue = UE.getEditor('editor');
	</script>
</head>
<body>
<form class="form-horizontal" id="imagetexteditorform">
	<div class="form-group">
		<label for="loginname" class="col-sm-3 control-label">显示宽度</label>
		<div class="col-sm-3">
			<input type="text" name="defineWidth" id="defineWidth" placeholder="" class="form-control">
		</div>
		<div class="col-sm-2 control-label">
			<span style="display: inline-block;color: red;float: left;">只能是数字</span>
		</div>
	</div>
	<div class="form-group">
		<label for="loginname" class="col-sm-3 control-label">显示高度</label>
		<div class="col-sm-3">
			<input type="text" name="defineHeight" id="defineHeight" placeholder="" class="form-control">
		</div>
		<div class="col-sm-2 control-label">
			<span style="display: inline-block;color: red;float: left;">只能是数字</span>
		</div>
	</div>
	<div class="form-group">
		<label for="moduleName" class="col-sm-2 control-label">内容</label>
		<div class="col-sm-6">
			<script id="editor" type="text/plain" style="width:500px;height:400px;">
									请输入内容
							</script>
			<input name="content" id="content" type="hidden"/>
		</div>
	</div>
</form>
</body>
<script type="text/javascript">
    $("#imagetexteditorform").validate({
        onfocusout: function(element) {
            var vali=$(element).valid();
            if(vali==0){
                $(element).val(0);
            }
        },
        rules:{
            defineWidth:{
                digits:true
            },
            defineHeight:{
                digits:true
            }
        }  ,
        messages: {
            defineWidth:{
                digits:"请输入数字"
            },
            defineHeight:{
                digits:"请输入数字"
            }
        }
    });
</script>
<script type="text/javascript">
    var pageJson = ${webPageModelJsonStr};
    var componentsParam = {
        initParamData : function(pageJson) {
            if (typeof (pageJson) == "string") {
                pageJson = JSON.parse(pageJson);
            }

            if (pageJson.defineWidth) {
                $("#defineWidth").val(pageJson.defineWidth);
            }

            if (pageJson.defineHeight) {
                $("#defineHeight").val(pageJson.defineHeight);
            }
            if (pageJson.content) {
                //var html=unescape(pageJson.content);
                $("#content").val(pageJson.content);
                // ue.setContent(html);
            }
        },
        saveParamData : function() {

            var defineWidth = $("#defineWidth").val();
            if (!defineWidth) {
                defineWidth = "0";
            }
            var defineHeight = $("#defineHeight").val();
            if (!defineHeight) {
                defineHeight = "0";
            }
            var content=ue.getContent();
            content=	encodeURI(content);
            var pageJson = {
                compId : 'picturead',
                "defineWidth" : defineWidth,
                "defineHeight" : defineHeight,
                "content":content,
            };
            return pageJson;
        }
        ,
        showParamData:function(){
            var paramData = [];
            var content = {
                type:'html',
                target:'content',
                value:ue.getContent()
            }
            paramData.push(content);
            return paramData;
        }
    }
    componentsParam.initParamData(pageJson);
</script>
</html>