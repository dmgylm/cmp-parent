package cmp.common.util.encry.bankencry;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @ClassName HttpClient3 
 * @Description  请求接口公共类
 * @author erle
 * @date 2014-4-17  
 * @version  1.0
 */
public class HttpClient3 {
	public static final Log as4j = LogFactory.getLog(HttpClient3.class);
    
    private static final String DEFAULT_CHARSET ="UTF-8";
    
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
        this.charset = (charset==null||charset.length()==0)? DEFAULT_CHARSET : charset;
        this.timeout = timeout < 0 ? DEFAULT_SOCKET_READ_TIMEOUT : timeout;
    }
    
  public String doPost(String url, Map<String, Object> params) throws Exception {
        return doPost(url, concatParameters(params));
    }
  
    /**
     * 发送POST请求
     * @param url 请求
     * @param params url编码的请求参数
     * @return
     * @throws Exception
     */
    public String doPost(String url, String params) throws Exception {
        as4j.info(" Http Post Url : " + url + "   param : "+ params);
        PostMethod post = null;
        try {
            HttpClient httpClient = new HttpClient();
            post = new PostMethod(url);
            RequestEntity requestEntity = new StringRequestEntity(params, "application/x-www-form-urlencoded",this.charset);
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
            post.releaseConnection();
        }
    }

    /**
     * 发送POST请求（包含请求头）
     * @param url 请求
     * @param params url编码的请求参数
     * @param contentType 请求头类型
     * @return
     * @throws Exception
     */
    public String doPost(String url, String params, String contentType) throws Exception {
        as4j.info(" Http Post Url : " + url + "   param : "+ params + "   contentType : "+ contentType);
        PostMethod post = null;
        try {
            HttpClient httpClient = new HttpClient();
            post = new PostMethod(url);
            RequestEntity requestEntity = new StringRequestEntity(params, contentType,this.charset);
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
            post.releaseConnection();
        }
    }
    
    public String doGet(String url, Map<String, String> params) throws Exception {
		return doGet(url + "?" + concatURLEncoderParameters(params));
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
        for(int n = -1; (n = in.read(bys)) != -1; ) {
            out.write(bys, 0, n);
        }
        return new String(out.toByteArray(), this.charset);
    }
    
    public String concatParameters(Map<String, Object> params) throws Exception {
    	
		//绑定加密因子结束
        StringBuilder sb = new StringBuilder();
        int p = 0;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (p++ > 0) {
                sb.append('&');
            }
            sb.append(entry.getKey()).append('=').append(entry.getValue());
        }
        return sb.toString();
    }
    
    public String encodeURI(String param) {
        if(param == null) {
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