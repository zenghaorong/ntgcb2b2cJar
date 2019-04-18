
//
// var params = [
//           	{
//        		"tabName":"电视",
//        		"productUuids":["59df4f2cdff849c29970c15bbf9e4589","6e04d3560c8049dda7ee2a9bf8d6e61d","e092c4c5b26048eab135127efcceea8e"
//        		                ,"e092c4c5b26048eab135127efcceea8e","e092c4c5b26048eab135127efcceea8e","e092c4c5b26048eab135127efcceea8e"
//        		                ,"e092c4c5b26048eab135127efcceea8e","e092c4c5b26048eab135127efcceea8e","e092c4c5b26048eab135127efcceea8e"
//        		                ,"e092c4c5b26048eab135127efcceea8e","e092c4c5b26048eab135127efcceea8e","e092c4c5b26048eab135127efcceea8e"
//        		                ,"e092c4c5b26048eab135127efcceea8e","e092c4c5b26048eab135127efcceea8e","e092c4c5b26048eab135127efcceea8e"
//        		                ,"e092c4c5b26048eab135127efcceea8e","e092c4c5b26048eab135127efcceea8e","e092c4c5b26048eab135127efcceea8e"]
//        	},
//        	{
//        		"tabName":"冰箱",
//        		"productUuids":["2db5a61fa3b64644adb17eaa01055044","30f7403d455b4a4d841f9967cfa5a220","30f7403d455b4a4d841f9967cfa5a220"
//        		                ,"30f7403d455b4a4d841f9967cfa5a220","30f7403d455b4a4d841f9967cfa5a220","30f7403d455b4a4d841f9967cfa5a220"
//        		                ,"30f7403d455b4a4d841f9967cfa5a220","30f7403d455b4a4d841f9967cfa5a220","30f7403d455b4a4d841f9967cfa5a220"]
//        	}
//        ];
//
//var config = {
//	compId:"producttab",
//	needAsyncInit:true,
//	showStyle:"1"
//}


CompsLoader.loadComponent_$_compId = function(){
	var option2 = {
			config:$_compConfig_$,
			tabsJson:$_tabsJson_$,
	};
	var option = {
			config:option2.config,
			tabsJson:option2.tabsJson,
			compId:option2.config.compId
	}
	Utils.loader('ProductTab',option);
}



