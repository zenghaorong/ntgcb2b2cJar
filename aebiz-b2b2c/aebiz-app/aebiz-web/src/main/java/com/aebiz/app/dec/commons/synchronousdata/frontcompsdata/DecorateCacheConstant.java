package com.aebiz.app.dec.commons.synchronousdata.frontcompsdata;

/**
 * 
 * 装修系统的memcached的参数设置 <br />
 * 
 * 装修系统的key设置
 * 
 * @author linqiang
 * 
 */
public interface DecorateCacheConstant {

	/* 装修系统， 名称 */
	public static final String DECORATE_SYS_NAME = "decorate";

	/* 装修系统， 缓存客户端名称 */
	public static final String DECORATE_MEM_CLIENT_NAME = "memCachedClient";

	/* 模板的缓存key */
	public static final String DECORATEPLATFORM_TEMPLATES = DECORATE_SYS_NAME
			+ "/templates";
	
	/* 组件显示的html缓存前缀 */
	public static String COMPONENTS_HTML_REDIS_KEY = "component_html_";
	/* 组件参数页面缓存前缀 */
	public static String COMPONENTS_JSP_REDIS_KEY = "component_jsp_";
	/* 组件引用的js缓存前缀 */
	public static String COMPONENTS_JS_REDIS_KEY = "component_js_";
	
	/**
	 *  设计器页面需要保存的文件的key前缀
	 */
	public static final String DESIGNER_PAGEMODEL = "pagemodel_";
	public static final String DESIGNER_PAGEMODELJSON = "pagemodeljson_";
	public static final String DESIGNER_PAGEVEIWHTML = "pageviewhtml_";
	public static final String DESIGNER_PAGEJS = "pagejs_";
	
	//模板启用的CSS名称
	public static final String DECORATEPLATFORM_TEMPLATE_CSS = "usecssname_";
	
	
	//===================================added by ly start================================//
	/** 正在使用的页面 **/
	public static final String DECORATEPLATFORM_SUBPAGE_USING = "page_using_";
	
	/** 当前模板资源 版本 **/
	public static final String TEMPLATE_RESOURCES_VERSION = "template_resouces_version_";
	
	/* 正在使用的模板的缓存key */
	public static final String DECORATEPLATFORM_TEMPLATES_USING = DECORATE_SYS_NAME+ "/templates/using";
	//===================================added by ly end================================//
	
}
