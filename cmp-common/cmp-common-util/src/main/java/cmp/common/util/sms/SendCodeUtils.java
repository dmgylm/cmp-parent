package cmp.common.util.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cmp.common.util.Tools;
import cmp.common.util.radom.RandomNumberUtil;
import cmp.common.util.string.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 发送短信验证码工具类
 * 
 * @date 2017年9月6日
 */
public class SendCodeUtils {

	private static final Logger log = LoggerFactory.getLogger(SendCodeUtils.class);

	public static Map<String, SMSCode> smsMap = new HashMap<String, SMSCode>();

	/** 1、 短信验证码在120s有效期内，不重复发送 **/
	private static final int RESP_IN_EXPIRY_DATE = 1;

	/** 2、同一手机号连续发送5次短信验证码，发送频率过高 **/
	private static final int RESP_OFTEN_SEND = 2;

	/** 3、当天同一ip地址发送短信验证码次数超过最大限度50条 **/
	private static final int RESP_OVER_TOP_SEND = 3;

	/**
	 * 根据手机号获取短信验证码
	 * 
	 * @param mobile
	 * @return
	 */
	public static SMSCodeResult getSMScode(String mobile) {
		return getSMScode(mobile, null, null);
	}

	/**
	 * 根据手机号和ip地址获取短信验证码
	 * 
	 * @param mobile
	 *            手机号
	 * @param ipAddress
	 *            ip地址
	 * @param vCode
	 *            图形验证码
	 * @return 短信验证码发送结果
	 */
	public static SMSCodeResult getSMScode(String mobile, String ipAddress, String vCode) {
		SMSCodeResult smsCodeResult = new SMSCodeResult();
		try {
			// 处理发短信的规则
			int sendResp = checkSend(mobile, ipAddress, vCode);

			String mesgCode = "";
			switch (sendResp) {
			// 正常情况
			case 0:
				mesgCode = getMessageCode(mobile, ipAddress);
				smsCodeResult.setMessageCode(mesgCode);
				smsCodeResult.setSendStatus(0);
				break;
			// 有效期范围内
			case 1:
				mesgCode = smsMap.get(mobile).getCode();
				smsCodeResult.setMessageCode(mesgCode);
				smsCodeResult.setSendStatus(1);
				break;
			// 同一个手机号连续获取验证码5次后，页面弹出图形验证码校验
			case 2:
				smsCodeResult.setSendStatus(2);
				break;
			// 超过当天最大值，终止发送
			case 3:
				smsCodeResult.setSendStatus(3);
				break;
			default:
				break;
			}
			log.info("messageCode:{}", mesgCode);
		} catch (Exception e) {
			log.error("The function getSMScode() call error! The error message is:{}", e.getMessage());
		}
		return smsCodeResult;
	}

	/**
	 * 校验短信验证码的发送规则
	 * 
	 * @param mobile
	 * @param ipAddress
	 * @return
	 */
	private static int checkSend(String mobile, String ipAddress, String vCode) {
		// 1.同一手机号120秒内只能获取一次验证码，120秒内重复点击获取验证码按钮，系统提示：验证码已发送至您的手机
		// 2.同一个手机号连续获取验证码5次后，页面弹出图形验证码校验。
		// 3.同一ip地址，同一天内不能连续发送短信验证码超50条。

		int resp = 0;

		if (smsMap.containsKey(mobile)) {

			// vCode（图形验证码）不为空，并且手机号码存在于map中，将SMScode的对象的count减1,保证客户端下次请求的时候还是会出现验证码校验
			SMSCode smsCode = smsMap.get(mobile);

			// 1.校验验证码是否在有效期内，返回结果1,表示验证码在有效期范围内，告知请求源验证码已经发送，请使用上次发送的验证码
			if ((new Date().getTime() - smsCode.getTime()) / 1000 <= SMSTools._SMS_LOST_TIME) {
				// 告诉客户端用上次生成的验证码
				resp = RESP_IN_EXPIRY_DATE;
			} else {
				// 2.校验同一个ip地址发送短信验证码的次数
				int totalCount = getCountBySameAddress(mobile, ipAddress);

				if (totalCount >= SMSTools._SMS_SAME_ADDRESS_SEND_COUNT) {
					resp = RESP_OVER_TOP_SEND;
				} else {
					// 3.校验同一手机号短信验证码的发送次数
					if (!Tools.isBlank(vCode)) {
						int count = smsCode.getCount();
						--count;
						smsCode.setCount(count);
					}
					if (smsCode.getCount() >= SMSTools._SMS_SAME_MOBILENUMBER_SEND_COUNT) {
						// 发送图形验证码给客户端
						resp = RESP_OFTEN_SEND;
					}
				}
			}
		}
		return resp;
	}

	/**
	 * 获取同一个ip地址发送短信验证码的次数
	 * 
	 * @param mobile
	 * @param ipAddress
	 * @return
	 */
	private static int getCountBySameAddress(String mobile, String ipAddress) {

		int totalCount = 0;

		for (Map.Entry<String, SMSCode> entry : smsMap.entrySet()) {

			SMSCode smsCode = entry.getValue();

			if (StringUtils.isNotBlank(ipAddress) && ipAddress.equals(smsCode.getIpAddress())) {
				totalCount += smsCode.getCount();
			}
		}
		return totalCount;
	}

	/**
	 * 获取同一手机号发送短信验证码的次数
	 */
	public Integer getCountByTel(String tel) {

		if (smsMap.containsKey(tel)) {
			return smsMap.get(tel).getCount();
		}
		return 0;
	}

	/**
	 * 发送短信验证码
	 * 
	 * @param mobile
	 * @param ipAddress
	 * @return
	 */
	private static String getMessageCode(String mobile, String ipAddress) {
		String messageCode;
		try {
			/**
			 * 生成随机码,发送短信
			 */
			messageCode = String.valueOf(RandomNumberUtil.randomLong(6, 6));// 生成六位的随机数字
			/**
			 * 短信服务
			 */
			String content = SMSTools._EM_CHECKCODE_MSG;
			content = content.replaceAll("\\$", messageCode);
			for (int i = 0; i < Integer.valueOf(SMSTools._EM_MSG_RESEND_TIMES); i++) {
				boolean isReSend = SMSEmaySendUtil.send(mobile, content);
				if (isReSend) {
					if (smsMap.containsKey(mobile)) {
						SMSCode smsCode = smsMap.get(mobile);
						int count = smsCode.getCount();
						if ((new Date().getTime() - smsCode.getTime()) / 1000 > SMSTools._SMS_LOST_TIME) {
							count++;
						}
						smsCode.setCount(count);
						smsCode.setIpAddress(ipAddress);
						smsCode.setMobile(mobile);
						smsCode.setCode(messageCode);
						smsCode.setTime(new Date().getTime());
					} else {
						SMSCode smsCode = new SMSCode(messageCode, new Date().getTime(), mobile, 1, ipAddress);
						smsMap.put(mobile, smsCode);
					}
					break;
				}
				log.warn("send message error,then resend message again...");
				try {
					Thread.sleep(5 * 1000);
				} catch (InterruptedException e) {

				}
			}
		} catch (Exception e) {
			messageCode = null;
			log.error("{}", e.getMessage());
		}
		return messageCode;
	}

	/**
	 * 校验手机短信验证码是否正确
	 */
	public boolean checkCode(String mobile, String code) {
		boolean bool = false;
		if (smsMap.containsKey(mobile)) {
			SMSCode smsCode = smsMap.get(mobile);
			// 判断验证码是否超过有效期
			if (code.equals(smsCode.getCode())
					&& (new Date().getTime() - smsCode.getTime()) / 1000 <= SMSTools._SMS_LOST_TIME) {
				smsMap.remove(mobile);
				bool = true;
			}
		}
		return bool;
	}

	/**
	 * 定时清除smsMap中的数据
	 */
	public void clearSmsMap() {

		String mobile;
		SMSCode smsCode;
		boolean inValid = false;// 标记无效的信息

		if (null != smsMap) {
			log.info("smsMap size : " + smsMap.size());
			// 遍历smsMap,找出其中过期的信息
			for (Map.Entry<String, SMSCode> entry : smsMap.entrySet()) {
				mobile = entry.getKey();
				smsCode = entry.getValue();
				inValid = (new Date().getTime() - smsCode.getTime()) / 1000 > SMSTools._SMS_LOST_TIME;
				if (inValid) {
					smsMap.remove(mobile);
				}
			}
		}
	}
}
