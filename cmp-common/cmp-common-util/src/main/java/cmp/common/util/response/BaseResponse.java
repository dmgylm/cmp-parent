package cmp.common.util.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 响应对象基类
 * 
 * @author sd
 * @date 2017年7月16日
 */
public class BaseResponse {

	private static final Logger LOGGER = LoggerFactory.getLogger(BaseResponse.class);

	public static final String SUCCESS = "SUCCESS";

	public static final String MSG = "成功!系统正常!";
	
	/**
	 * 返回的状态码
	 */
	private String resultCode;

	/**
	 * 返回的状态信息
	 */
	private String resultDesc;

	/**
	 * 创建返回的结果对象
	 * 
	 * @param clazz
	 * @param resultCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends BaseResponse> T create(Class<T> clazz, String resultCode) {
		BaseResponse result = null;
		try {
			result = getInstance(clazz);
			result.setResultCode(resultCode);
			// result.setResultDesc(baseException.getMsg());
		} catch (Exception e) {
			LOGGER.error("create BaseResult error! The error message is：{}，e:{}", e.getMessage(), e);
		}
		return (T) result;

	}

	/**
	 * 业务处理成功时创建返回的结果对象
	 * 
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends BaseResponse> T createSuccess(Class<T> clazz) {

		BaseResponse result = null;
		try {
			result = getInstance(clazz);
			result.setResultCode(SUCCESS);
			result.setResultDesc(MSG);
		} catch (Exception e) {
			LOGGER.error("create BaseResult error! The error message is：{}，e:{}", e.getMessage(), e);
		}
		return (T) result;
	}

	private static <T extends BaseResponse> BaseResponse getInstance(Class<T> clazz) throws Exception {
		return clazz.newInstance();
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}

}
