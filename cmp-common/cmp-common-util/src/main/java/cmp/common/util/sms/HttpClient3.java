package cmp.common.util.sms;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cmp.common.util.Tools;

import java.io.*;
import java.net.URLEncoder;
import java.util.Map;

public class HttpClient3 {

	public static final Logger log = LoggerFactory.getLogger(HttpClient3.class);

	private static final String DEFAULT_CHARSET = "UTF-8";

	private static final int DEFAULT_CONNECTION_TIMEOUT = 120 * 1000;

	private static final int DEFAULT_SOCKET_READ_TIMEOUT = 120 * 1000;

	private String charset;

	private int timeout;

	public HttpClient3() {
		this(DEFAULT_CHARSET, DEFAULT_SOCKET_READ_TIMEOUT);
	}

	public HttpClient3(int timeout) {
		this(DEFAULT_CHARSET, timeout);
	}

	public HttpClient3(String charset, int timeout) {
		this.charset = Tools.isBlank(charset) ? DEFAULT_CHARSET : charset;
		this.timeout = timeout < 0 ? DEFAULT_SOCKET_READ_TIMEOUT : timeout;
	}

	public String doPost(String url, Map<String, String> params) throws Exception {
		return doPost(url, concatParameters(params));
	}

	/**
	 * 发送POST请求
	 *
	 * @param url
	 *            请求
	 * @param params
	 *            url编码的请求参数
	 * @return
	 * @throws Exception
	 */
	public String doPost(String url, String params) throws Exception {
		if (url.contains("insurance")) {
			log.info("pa-chong:url:{}", url);
			log.info("pa-chong:req:{}", params);
		}

		log.info("pa-chong:url:{}", url);
		log.info("pa-chong:req:{}", params);

		PostMethod post = null;
		HttpClient httpClient = new HttpClient(new HttpClientParams(), new SimpleHttpConnectionManager(true));
		try {
			post = new PostMethod(url);
			RequestEntity requestEntity = new StringRequestEntity(params, "application/x-www-form-urlencoded",
					this.charset);
			post.setRequestEntity(requestEntity);
			post.setRequestHeader("Connection", "close");
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(DEFAULT_CONNECTION_TIMEOUT);
			httpClient.getHttpConnectionManager().getParams().setSoTimeout(this.timeout);
			post.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
			httpClient.executeMethod(post);
			String resp = getResonseString(post);
			return resp;
		} catch (Exception e) {
			throw e;
		} finally {
			if (null != post) {
				post.releaseConnection();
				((SimpleHttpConnectionManager) httpClient.getHttpConnectionManager()).shutdown();
			}
		}
	}

	public String doGet(String url, Map<String, String> params) throws Exception {
		return doGet(url += concatURLEncoderParameters(params));
	}

	public String doGet(String url) throws Exception {
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = null;
		try {
			getMethod = new GetMethod(url);
			// 设置http连接超时
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(DEFAULT_CONNECTION_TIMEOUT);
			// 设置get请求超时
			getMethod.getParams().setSoTimeout(this.timeout);
			// 设置请求重试处理，默认三�?
			getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
			getMethod.getParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
			getMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=" + DEFAULT_CHARSET);
			int status = httpClient.executeMethod(getMethod);
			if (status != HttpStatus.SC_OK) {
				throw new HttpException();
			}
			String response = getResponseString(getMethod);
			return response;
		} catch (Exception e) {
			throw e;
		} finally {
			// 释放连接
			getMethod.releaseConnection();
		}
	}

	private String getResponseString(GetMethod get) throws IOException {
		BufferedReader buff = new BufferedReader(new InputStreamReader(get.getResponseBodyAsStream(), DEFAULT_CHARSET));
		StringBuffer response = new StringBuffer();
		String line;
		while ((line = buff.readLine()) != null) {
			response.append(line);
			response.append("\r\n");
		}
		return response.toString();
	}

	public String concatURLEncoderParameters(Map<String, String> params) throws Exception {
		StringBuilder sb = new StringBuilder();
		int p = 0;
		sb.append('?');
		for (Map.Entry<String, String> entry : params.entrySet()) {
			if (p++ > 0) {
				sb.append('&');
			}
			sb.append(entry.getKey()).append('=').append(encodeURI(entry.getValue()));
		}
		return sb.toString();
	}

	private String getResonseString(PostMethod post) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		InputStream in = post.getResponseBodyAsStream();
		byte[] bys = new byte[1024];
		for (int n = -1; (n = in.read(bys)) != -1;) {
			out.write(bys, 0, n);
		}
		return new String(out.toByteArray(), this.charset);
	}

	/**
	 * 将map参数转换成string类型
	 *
	 * @param params
	 * @return ps:map('a','b') ==>a=b
	 * @throws Exception
	 */
	public String concatParameters(Map<String, String> params) throws Exception {
		StringBuilder sb = new StringBuilder();
		int p = 0;
		for (Map.Entry<String, String> entry : params.entrySet()) {
			if (p++ > 0) {
				sb.append('&');
			}
			sb.append(entry.getKey()).append('=').append(entry.getValue());
		}
		return sb.toString();
	}

	public String encodeURI(String param) {
		if (param == null) {
			return "";
		}
		try {
			return URLEncoder.encode(param, this.charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
}