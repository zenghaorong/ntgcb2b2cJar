 // dynatree  文件树结构调用插件的js 
	$(function(){
    if ($(".filetree").length > 0) {
        $(".filetree").each(function() {
            var $el = $(this),
                opt = {};
            opt.debugLevel = 0;
            if ($el.hasClass("filetree-callbacks")) {
                opt.onActivate = function(node) {
                    $(".activeFolder").text(node.data.title);
                    $(".additionalInformation").html("<ul style='margin-bottom:0;'><li>Key: " + node.data.key + "</li><li>is folder: " + node.data.isFolder + "</li></ul>");
                };
            }
            if ($el.hasClass("filetree-checkboxes")) {
                opt.checkbox = true;

                opt.onSelect = function(select, node) {
                    var selNodes = node.tree.getSelectedNodes();
                    var selKeys = $.map(selNodes, function(node) {
                        return "[" + node.data.key + "]: '" + node.data.title + "'";
                    });
                    $(".checkboxSelect").text(selKeys.join(", "));
                };
            }

            $el.dynatree(opt);
        });
    }
	
	});