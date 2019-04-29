package cmp.common.util.sms;

public class SMSTools {
	public static final String _SMS_EM_CDKEY = SMSPropertiesHelp.getValueByKey("cdkey");
	public static final String _SMS_EM_PASSWORD = SMSPropertiesHelp.getValueByKey("password");
	public static final String _SMS_EM_REGIST_URL = SMSPropertiesHelp.getValueByKey("registUrl");
	public static final String _SMS_EM_LOGOUT_URL = SMSPropertiesHelp.getValueByKey("logoutUrl");
	public static final String _SMS_EM_SENDMS_URL = SMSPropertiesHelp.getValueByKey("sendMsUrl");
	public static final String _SMS_EM_TIMING_URL = SMSPropertiesHelp.getValueByKey("timingUrl");
	public static final String _SMS_EM_BALANCE_URL = SMSPropertiesHelp.getValueByKey("balanceUrl");
	public static final String _SMS_EM_RECHARGE_URL = SMSPropertiesHelp.getValueByKey("rechargeUrl");
	public static final String _SMS_EM_TITLE = SMSPropertiesHelp.getValueByKey("companyTitle");
	public static final String _EM_CHECKCODE_MSG = SMSPropertiesHelp.getValueByKey("checkMsg");
	public static final String _EM_MSG_RESEND_TIMES = SMSPropertiesHelp.getValueByKey("reSendTimes");
	public static final String _EM_MODIFY_PASSWORD_MSG = SMSPropertiesHelp.getValueByKey("passwordMsg");
	public static final String _EM_SIGN = SMSPropertiesHelp.getValueByKey("sign");
	public static final long _SMS_LOST_TIME = Long.valueOf(SMSPropertiesHelp.getValueByKey("lostTime"));
	/** 同一个ip地址当天能连续发送短信验证码的最大次数 **/
	public static final int _SMS_SAME_ADDRESS_SEND_COUNT = Integer
			.valueOf(SMSPropertiesHelp.getValueByKey("sameAddressSendCount"));
	/** 同一个手机号当天能连续发送短信验证码的最大次数 **/
	public static final int _SMS_SAME_MOBILENUMBER_SEND_COUNT = Integer
			.valueOf(SMSPropertiesHelp.getValueByKey("sameMobileNumberSendCount"));

}