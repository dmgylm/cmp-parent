package cmp.common.util.sms;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 发送短信工具类
 * 
 * @date 2017年9月4日
 */
public class SMSEmaySendUtil {
	public static final Logger log = LoggerFactory.getLogger(HttpClient3.class);
	private static String _CDKEY = SMSTools._SMS_EM_CDKEY;
	private static String _PASSWORD = SMSTools._SMS_EM_PASSWORD;
	private static String _SENDMS_URL = SMSTools._SMS_EM_SENDMS_URL;
	private static String _SIGN = SMSTools._EM_SIGN;
	private static String _TITLE = SMSTools._SMS_EM_TITLE;
	private static HttpClient3 http = new HttpClient3();
	private static final String POST_SUCC = "0";
	private static final String POST_FAIL = "1";

	/**
	 * 发送短信
	 * 
	 * @param mobile
	 *            手机号
	 * @param content
	 *            短信内容
	 * @return
	 */
	public static boolean send(String mobile, String content) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("cdkey", _CDKEY);
		params.put("password", _PASSWORD);
		params.put("phone", mobile);
		content = _SIGN.concat(_TITLE).concat(content);
		params.put("message", content);

		String message = null;
		try {
			log.info("do post send content:{}", content);
			message = http.doPost(_SENDMS_URL, params);
			log.info("do post send response:{}", message);
		} catch (Exception e) {
			log.error("do post send error:{}", e.getMessage());
		}
		return parseXMl(message);
	}

	/**
	 * 解析响应xml error为0时代表响应成功
	 * 
	 * @param message
	 * @return
	 */
	public static boolean parseXMl(String message) {
		message = message.trim();
		log.info("parse xml is", message);
		try {
			Document doc = DocumentHelper.parseText(message);
			Element root = doc.getRootElement();
			String error = root.elementText("error");
			String msg = root.elementText("message");
			log.info("error:{},msg:{}", error, msg);
			if (error.equals(POST_SUCC)) {
				log.info("post success");
				return true;
			} else {
				log.info("post fail! return error node is {}", POST_FAIL);
				return false;
			}
		} catch (DocumentException e) {
			e.printStackTrace();
			log.error("method:parseXMl params:【{}】", message);
			return false;
		}
	}

	public static void main(String[] args) {
		String message = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><response><error>0</error><message></message></response>";
		boolean flag = parseXMl(message);
		System.out.println(flag);
	}

	public void testSend() {
		String mobile = "";
		String content = "测试内容";
		boolean send = SMSEmaySendUtil.send(mobile, content);
		System.out.println("发送短信的结果为: " + send);
	}
}