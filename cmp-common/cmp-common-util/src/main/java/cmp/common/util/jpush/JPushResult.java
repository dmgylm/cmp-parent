package cmp.common.util.jpush;

import cn.jpush.api.push.PushResult;

/**
 * 简单的封装 调用极光推送消息返回的结果集
 * 
 */
public class JPushResult {

	private int errorCode;// 错误code

	private String errorMessage;// 错误消息

	private PushResult pushResult;// 推送结果

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public PushResult getPushResult() {
		return pushResult;
	}

	public void setPushResult(PushResult pushResult) {
		this.pushResult = pushResult;
	}

	public JPushResult() {
		super();
	}

	public JPushResult(int errorCode, String errorMessage, PushResult pushResult) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.pushResult = pushResult;
	}

	@Override
	public String toString() {
		return "JPushResult [errorCode=" + errorCode + ", errorMessage=" + errorMessage + ", pushResult=" + pushResult
				+ "]";
	}

}
