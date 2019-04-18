package com.aebiz.app.web.commons.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import sun.misc.BASE64Encoder;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Desc: 
 * @author rongmin
 * create at 2014-10-22
 */
public class HttpClientUtil {
	
/*	public static void main(String ar[]) throws Exception {
		System.err.println(HttpClientUtil.getMethod("http://baidu.com"));
	}*/

	private static CloseableHttpClient HTTP_CLIENT = null;
	
	private static final int SOCKET_TIMEOUT = 60000;//读取超时
	
	private static final int CONNECT_TIMEOUT = 60000;//请求超时
	
	private static final int REQ_CONNECT_TIMEOUT = 60000;//从连接池去请求获取连接超时
	
	private static RequestConfig requestConfig = null;
	
	static {
		
		
		RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();
		ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();
		registryBuilder.register("http", plainSF);
		//指定信任密钥存储对象和连接套接字工厂
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			SSLContext sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()).build();
			LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			registryBuilder.register("https", sslSF);
		} catch (KeyStoreException e) {
			throw new RuntimeException(e);
		} catch (KeyManagementException e) {
			throw new RuntimeException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		Registry<ConnectionSocketFactory> registry = registryBuilder.build();
		
		
		
		
		
		requestConfig = RequestConfig.custom()
				.setSocketTimeout(SOCKET_TIMEOUT)
				.setConnectTimeout(CONNECT_TIMEOUT)
				.setConnectionRequestTimeout(REQ_CONNECT_TIMEOUT)
				.build();
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
		// 将最大连接数增加到200
		connManager.setMaxTotal(200);
		// 将每个路由基础的连接增加到20
		/**maxConnectionsPerHost ：最大连接数，默认是 2 。
		maxTotalConnections ：最大活动连接数，默认是 20 。
		增加配置后，不会报 等等timeout异常了*/
		connManager.setDefaultMaxPerRoute(20);
		
		/* http authentication setting */
		/*HttpHost target = new HttpHost("localhost", 80, "http");
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(
                new AuthScope(target.getHostName(), target.getPort()),
                new UsernamePasswordCredentials("username", "password"));*/
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(
		        new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), 
		        new UsernamePasswordCredentials("tcl14o2o", "tcl14o2o"));
		
		
		HTTP_CLIENT = HttpClients.custom()
				.setConnectionManager(connManager)
				.setDefaultCredentialsProvider(credsProvider)//http authentication setting
				.build();
		////HttpParams httpParams = HTTP_CLIENT.getParams();
		///HTTP_CLIENT.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		///HTTP_CLIENT.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");
				
		//to destory
		//////HTTP_CLIENT.close();
	}
	
	public static String getMethod(String url) throws Exception  {
		HttpGet httpget = new HttpGet(url);
		httpget.setConfig(requestConfig);
		HttpContext httpContext = new BasicHttpContext();
		CloseableHttpResponse response = null;
    	try {
    		response = HTTP_CLIENT.execute(httpget, httpContext);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity, "UTF-8");
            }
            return null;
        } finally {
            if (response != null) {
            	response.close();
			}
        }
	}
	
	public static String postMethod(String url, List<NameValuePair> data) throws Exception {
		HttpPost httpost = new HttpPost(url);
		httpost.setConfig(requestConfig);
		httpost.setEntity(new UrlEncodedFormEntity(data, "gbk"));
		HttpContext httpContext = new BasicHttpContext();
		CloseableHttpResponse response = null;
		try {
			response = HTTP_CLIENT.execute(httpost, httpContext);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				return EntityUtils.toString(entity, "gbk");
				
				
			}
			return null;
		} finally {
			if (response != null) {
            	response.close();
			}
		}
	}
	
	public static String postMethod(String url, String id, String data) throws Exception {
		List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
		NameValuePair nameValuePairId = new BasicNameValuePair("Id", data);
		nameValuePairList.add(nameValuePairId);
		NameValuePair nameValuePair = new BasicNameValuePair("Data", data);
		nameValuePairList.add(nameValuePair);
		return postMethod(url, nameValuePairList);
	}
	
	public static String postMethod(String url, Map<String,Object> map) throws Exception {
		List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
		NameValuePair nameValuePairId;
		
		for(String key : map.keySet()){
			nameValuePairId = new BasicNameValuePair(key, map.get(key).toString());
			nameValuePairList.add(nameValuePairId);
		}
		
		return postMethod(url, nameValuePairList);
	}
	
	
	
	
	

	
	public static Object getMethod3(String url) throws Exception  {
		HttpGet httpget = new HttpGet(url);
		httpget.setConfig(requestConfig);
		HttpContext httpContext = new BasicHttpContext();
		CloseableHttpResponse response = null;
    	try {
    		response = HTTP_CLIENT.execute(httpget, httpContext);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
            	byte[] objBytes=EntityUtils.toByteArray(entity);
				ByteArrayInputStream bi = new ByteArrayInputStream(objBytes);   
		        ObjectInputStream oi = new ObjectInputStream(bi);   
		        return oi.readObject(); 
            }
            return null;
        } finally {
            if (response != null) {
            	response.close();
			}
        }
	}
	
	public static Object postMethod3(String url, Map<String,Object> map) throws Exception {
		List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
		NameValuePair nameValuePairId;
		
		for(String key : map.keySet()){
			nameValuePairId = new BasicNameValuePair(key, map.get(key).toString());
			nameValuePairList.add(nameValuePairId);
		}
		
		return postMethod3(url, nameValuePairList);
	}
	
	public static Object postMethod3(String url, List<NameValuePair> data) throws Exception {
		HttpPost httpost = new HttpPost(url);
		httpost.setConfig(requestConfig);
		httpost.setEntity(new UrlEncodedFormEntity(data, "UTF-8"));
		HttpContext httpContext = new BasicHttpContext();
		CloseableHttpResponse response = null;
		try {
			response = HTTP_CLIENT.execute(httpost, httpContext);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				//return EntityUtils.toString(entity, "UTF-8");   
				byte[] objBytes=EntityUtils.toByteArray(entity);
				ByteArrayInputStream bi = new ByteArrayInputStream(objBytes);   
		        ObjectInputStream oi = new ObjectInputStream(bi);   
		        return oi.readObject();  
				
			}
			return null;
		} finally {
			if (response != null) {
            	response.close();
			}
		}
	}

	public static Object postMethod4(String url, String jsonStr) throws Exception {
		HttpPost httpost = new HttpPost(url);
		httpost.setConfig(requestConfig);
		httpost.setHeader("Content-type", "application/json");
		StringEntity requestEntity = new StringEntity(jsonStr,"utf-8");
		requestEntity.setContentEncoding("UTF-8");
		httpost.setEntity(requestEntity);
		HttpContext httpContext = new BasicHttpContext();
		CloseableHttpResponse response = null;
		try {
			response = HTTP_CLIENT.execute(httpost, httpContext);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream is = response.getEntity().getContent();
				String resultStr = getStreamAsString(is, "UTF-8");
				JSONObject object = JSON.parseObject(resultStr);
				return object;
			}
			return null;
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}finally {
			if (response != null) {
				response.close();
			}
		}
	}

	private static String getStreamAsString(InputStream stream, String charset) throws IOException {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset), 8192);
			StringWriter writer = new StringWriter();
			char[] chars = new char[8192];
			int count = 0;
			while ((count = reader.read(chars)) > 0) {
				writer.write(chars, 0, count);
			}
			return writer.toString();
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
	}


	//请求服务器，返回JSON对象
	public static org.json.JSONObject AjaxPostObject(String url,JSONObject pm) {
		org.json.JSONObject rs = new org.json.JSONObject();
		DefaultHttpClient client = new DefaultHttpClient();

		HttpPost post = new HttpPost(url);
		StringBuffer sb = new StringBuffer();
		try {
			StringEntity reqEntity= null;
			if(pm==null)
			{
				reqEntity = new StringEntity("{}",HTTP.UTF_8);
			}
			else
			{
				reqEntity = new StringEntity(pm.toString(),HTTP.UTF_8);
			}
			reqEntity.setContentType("application/x-www-form-urlencoded");
			post.setEntity(reqEntity);
			HttpResponse resp = client.execute(post);

			HttpEntity entity = resp.getEntity();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					entity.getContent(), HTTP.UTF_8));

			String result = br.readLine();
			while (result != null) {
				sb.append(result);
				result = br.readLine();
			}

			String str = "";
			if(StringUtils.isNotBlank(sb.toString())) {
				str = sb.toString();
			}
			str = str.trim();

			String jsonstr = str;
			rs = new org.json.JSONObject(jsonstr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}

	/**
	 * base64位编码
	 */
	public static String base64Utils(String s){
		// BASE64编码
		String str = "";
		BASE64Encoder encoder = new BASE64Encoder();
		try {
			str = encoder.encode(s.getBytes("UTF-8"));
			return str;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return str;
		}
	}

	
}