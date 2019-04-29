package cmp.common.util.sms;

import java.io.Serializable;

/**
 * 发送短信验证码返回结果对象
 * 
 * @date 2017年9月6日
 */
public class SMSCodeResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -438977312544170210L;

	/**
	 * 短信验证码
	 */
	private String messageCode;

	/**
	 * 发送结果状态
	 */
	private int sendStatus;

	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	public int getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(int sendStatus) {
		this.sendStatus = sendStatus;
	}

	public SMSCodeResult() {
	}

	@Override
	public String toString() {
		return "SMSCodeResult [messageCode=" + messageCode + ", sendStatus=" + sendStatus + "]";
	}

}
