package cmp.common.util.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClientUtil {

	private static PoolingHttpClientConnectionManager cm;// httpclient4.5 连接池对象
	private static String EMPTY_STR = "";// 空字符串
	private static String UTF_8 = "UTF-8";// 编码

	private static void init() {
		if (cm == null) {
			cm = new PoolingHttpClientConnectionManager();
			cm.setMaxTotal(150);// 整个连接池最大连接数
			cm.setDefaultMaxPerRoute(50);// 每路由最大连接数，默认值是2
		}
	}

	/**
	 * 通过连接池获取HttpClient
	 * 
	 * @return
	 */
	private static CloseableHttpClient getHttpClient() {
		init();
		return HttpClients.custom().setConnectionManager(cm).build();
	}

	/**
	 * 不带参数的GET请求
	 * 
	 * @param url
	 *            请求地址
	 * @return
	 */
	public static String httpGetRequest(String url) {
		HttpGet httpGet = new HttpGet(url);
		return getResult(httpGet);
	}

	/**
	 * 带参数的GET请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws URISyntaxException
	 */
	public static String httpGetRequest(String url, Map<String, Object> params) throws URISyntaxException {
		URIBuilder ub = new URIBuilder();
		ub.setPath(url);

		ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
		ub.setParameters(pairs);

		HttpGet httpGet = new HttpGet(ub.build());
		return getResult(httpGet);
	}

	/**
	 * 带请求头的HTTP GET请求
	 * 
	 * @param url
	 * @param headers
	 * @param params
	 * @return
	 * @throws URISyntaxException
	 */
	public static String httpGetRequest(String url, Map<String, Object> headers, Map<String, Object> params)
			throws URISyntaxException {
		URIBuilder ub = new URIBuilder();
		ub.setPath(url);

		ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
		ub.setParameters(pairs);

		HttpGet httpGet = new HttpGet(ub.build());
		for (Map.Entry<String, Object> param : headers.entrySet()) {
			httpGet.addHeader(param.getKey(), String.valueOf(param.getValue()));
		}
		return getResult(httpGet);
	}

	/**
	 * HTTP POST 请求
	 * 
	 * @param url
	 * @return
	 */
	public static String httpPostRequest(String url) {
		HttpPost httpPost = new HttpPost(url);
		return getResult(httpPost);
	}

	/**
	 * 带请求参数的HTTP POST请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String httpPostRequest(String url, Map<String, Object> params) throws UnsupportedEncodingException {
		HttpPost httpPost = new HttpPost(url);
		ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
		httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));
		return getResult(httpPost);
	}

	/**
	 * 带请求头的HTTP POST请求
	 * 
	 * @param url
	 * @param headers
	 * @param params
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String httpPostRequest(String url, Map<String, Object> headers, Map<String, Object> params)
			throws UnsupportedEncodingException {
		HttpPost httpPost = new HttpPost(url);

		for (Map.Entry<String, Object> param : headers.entrySet()) {
			httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
		}

		ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
		httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));

		return getResult(httpPost);
	}

	/**
	 * 参数转换MAP--->ArrayList
	 * 
	 * @param params
	 * @return
	 */
	private static ArrayList<NameValuePair> covertParams2NVPS(Map<String, Object> params) {
		ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
		for (Map.Entry<String, Object> param : params.entrySet()) {
			if (param.getValue() instanceof ArrayList<?>) {
				List<Object> list = (List<Object>) param.getValue();
				for (Object object : list) {
					pairs.add(new BasicNameValuePair(param.getKey(), String.valueOf(object)));
				}
			} else {
				pairs.add(new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue())));
			}
		}

		return pairs;
	}

	/**
	 * 处理Http请求
	 * 
	 * @param request
	 * @return
	 */
	private static String getResult(HttpRequestBase request) {
		// CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpClient httpClient = getHttpClient();
		try {
			CloseableHttpResponse response = httpClient.execute(request);
			// response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// long len = entity.getContentLength();// -1 表示长度未知
				String result = EntityUtils.toString(entity);
				response.close();
				// httpClient.close();
				return result;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}

		return EMPTY_STR;
	}

	/**
	 * HTTP GET 请求
	 * 
	 * @param url
	 * @return 返回状态码
	 */
	public static Integer getStatusByGet(String url) {
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpClient httpClient = getHttpClient();
		Integer statusCode = null;
		try {
			CloseableHttpResponse response = httpClient.execute(httpGet);
			statusCode = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// long len = entity.getContentLength();// -1 表示长度未知
				String result = EntityUtils.toString(entity);
				response.close();
				// httpClient.close();
				return statusCode;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}

		return 0;
	}

	
	
	/**
	 * HTTP Post 请求
	 * 
	 * @param url
	 * @return 返回状态码
	 */
	public static Integer getStatusByPost(String url) {
		HttpPost httpPost = new HttpPost(url);
		CloseableHttpClient httpClient = getHttpClient();
		Integer statusCode = null;
		try {
			CloseableHttpResponse response = httpClient.execute(httpPost);
			statusCode = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// long len = entity.getContentLength();// -1 表示长度未知
				String result = EntityUtils.toString(entity);
				response.close();
				// httpClient.close();
				return statusCode;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}

		return 0;
	}
	
	/*
	 * private static String getResult2(HttpRequestBase request) {
	 * CloseableHttpClient httpclient = getHttpClient(); try { // Create a local
	 * instance of cookie store CookieStore cookieStore = new
	 * BasicCookieStore();
	 * 
	 * // Create local HTTP context HttpClientContext localContext =
	 * HttpClientContext.create(); // Bind custom cookie store to the local
	 * context localContext.setCookieStore(cookieStore);
	 * 
	 * // Pass local context as a parameter CloseableHttpResponse response =
	 * httpclient.execute(request, localContext); try { List<Cookie> cookies =
	 * cookieStore.getCookies(); for (int i = 0; i < cookies.size(); i++) {
	 * System.out.println("Local cookie: " + cookies.get(i)); }
	 * EntityUtils.consume(response.getEntity()); } finally { response.close();
	 * } } finally { httpclient.close(); }
	 * 
	 * return EMPTY_STR; }
	 */
    
	public static byte[] download(String url) {
		List<Byte> list = new ArrayList<Byte>();
		HttpClient httpclient = null;
		HttpGet httpget = null;
		try {
			httpclient = HttpClientBuilder.create().build();
			httpget = new HttpGet(url);
			httpget.setHeader("User-Agent", "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)");
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				try {
					byte temp[] = new byte[1024];
					int i = 0;
					while ((i = instream.read(temp)) != -1) {
						for(int j=0;j<i;j++) {
							list.add(temp[j]);
						}
					}
				} catch (IOException ex) {
					throw ex;
				} catch (RuntimeException ex) {
					httpget.abort();
					throw ex;
				} finally {
					try {
						instream.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(httpget!=null) {
				httpget.releaseConnection();
			}
		}
		Byte[] tempArray = list.toArray(new Byte[list.size()]);
		byte[] result = new byte[tempArray.length];
		for(int i=0;i<tempArray.length;i++){
			result[i] = tempArray[i];
		}
		return result;
	}
	
}
