package com.aebiz.app.dec.commons.utils;

/**
 * 
 * 装修系统公用常量
 * 
 */
public interface DecorateCommonConstant {
	/* 装修系统使用类型 1：系统 ，2：店铺 */
	public static final int DECORATE_USETYPE_SYSTEM = 1;
	public static final int DECORATE_USETYPE_SHOP = 2;

	/* 是否启用 true：是 ，false：否 */
	public static final int DECORATE_USING_YES = 1;
	public static final  int DECORATE_USING_NO= 0;

	/* 组件使用类型 1：系统组件 ，2：自定义组件 */
	public static final int COMPONENTS_USETYPE_SYSTEM = 1;
	public static final int COMPONENTS_USETYPE_CUSTOM = 2;
	
	/* 模板文件类型 1：文件夹，2：页面文件 */
	public static final int TEMPLAE_FILE_TYPE_FOLDER = 1;
	public static final int TEMPLAE_FILE_TYPE_HTML = 2;
	
	/* 是否系统默认文件夹 1：是 ，0：不是 */
	public static final int TEMPLATE_FOLDER_DEFAULT_YES = 1;
	public static final int TEMPLATE_FOLDER_DEFAULT_NO = 0;
	
	/* 组件保存的资源文件类型： html、jsp、js*/
	public static final String COMPONENT_RESOURCETYE_HTML = "html";
	public static final String COMPONENT_RESOURCETYE_JSP = "jsp";
	public static final String COMPONENT_RESOURCETYE_JS = "js";
	
	/* 需要传递到组件方法的参数名称*/
	public static final String COMPONENT_TOPARAMSJSP_URL = "toParamsJspURL";
	public static final String COMPONENT_WEBPAGEMODEL_JSONSTR = "webPageModelJsonStr";
	public static final String COMPONENT_REQUEST_COOKIES = "cookies";
	public static final String COMPONENT_REQUEST_CONTEXTPATH = "base";
	
	/* 资源文件默认保存的版本号和版本描述*/ 
	public static final String FILE_DEFAULT_VERSIONNO = "1.0";
	public static final String FILE_DEFAULT_VERSIONDESCRIBE = "v1.0版本";
}
