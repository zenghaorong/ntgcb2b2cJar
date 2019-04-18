package com.aebiz.app.dec.commons.utils;

import java.text.MessageFormat;

public class CommonUtil {

	/**
	 * 获取商品详情页的 URL
	 * @param contextPath
	 * @param url
	 * @param productUuid
	 * @return
	 */
	public static String getProductDetailUrl(String contextPath,String url,String productUuid){
		if(url == null){
			return "";
		}
		if(url.indexOf("?")!=-1){
			return MessageFormat.format("{0}{1}&productUuid={2}", new Object[]{contextPath,url,productUuid});
		}else{
			return MessageFormat.format("{0}{1}?productUuid={2}", new Object[]{contextPath,url,productUuid});
		}
	}
	
	/**
	 * 获取商品列表页Url
	 * @param contextPath
	 * @param url
	 * @param categoryUuid
	 * @return
	 * String
	 */
	public static String getProductListUrl(String contextPath,String url,String categoryUuid){
		if(url == null){
			return "";
		}
		if(url.indexOf("?")!=-1){
			return MessageFormat.format("{0}{1}&categoryUuid={2}", new Object[]{contextPath,url,categoryUuid});
		}else{
			return MessageFormat.format("{0}{1}?categoryUuid={2}", new Object[]{contextPath,url,categoryUuid});
		}
	}

	public static String getFullPath(String contextPath,String url,String key,String value){
		if(url == null){
			return "";
		}
		if(url.indexOf("?")!=-1){
			return MessageFormat.format("{0}{1}&"+key+"={2}", new Object[]{contextPath,url,value});
		}else{
			return MessageFormat.format("{0}{1}?"+key+"={2}", new Object[]{contextPath,url,value});
		}
	}
	
	/**
	 * 获取文章详情Url
	 * @param contextPath
	 * @param url
	 * @param categoryUuid
	 * @return
	 * String
	 */
	public static String getContentUrl(String contextPath,String url,String contentUuid){
		if(url == null){
			return "";
		}
		if(url.indexOf("?")!=-1){
			return MessageFormat.format("{0}{1}&contentUuid={2}", new Object[]{contextPath,url,contentUuid});
		}else{
			return MessageFormat.format("{0}{1}?contentUuid={2}", new Object[]{contextPath,url,contentUuid});
		}
	}
}
