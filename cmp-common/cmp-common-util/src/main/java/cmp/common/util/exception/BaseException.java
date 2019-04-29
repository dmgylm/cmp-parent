package cmp.common.util.exception;

/**
 * 业务异常基类，所有业务异常都必须继承于此异常
 * 
 * 定义异常时，需要先确定异常所属模块。 例如：添加机构模块异常，可以定义为 [10050001] 前四位数为系统模块编号，后4位为错误代码 ,唯一 <br>
 * 
 * 基本模块异常 1001 <br>
 * 
 * 账号模块异常 1002 <br>
 * 
 * 权限模块异常 1003 <br>
 * 
 * 中行模块异常 1004 <br>
 * 
 * 机构模块异常 1005 <br>
 * 
 * 结算模块异常 1006 <br>
 * 
 * 交易模块异常 1007 <br>
 * 
 * 网点模块异常 1008 <br>
 * 
 * 商户端模块异常 1009 <br>
 * 
 * 拓展App模块异常 1010 <br>
 * 
 * 合同模块异常 1011 <br>
 * 
 * 审批模块异常1012 <br>
 * 
 * @author sd
 * @date 2017年7月15日
 */
public class BaseException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1966826440086378165L;

	/**
	 * 请求参数：时间戳字段非法
	 */
	public static final BaseException PARAM_TIMESTAMP_IS_ILLEGAL = new BaseException(
			"10010001", "请求参数中的时间戳字段非法");
	
	/**
	 * 请求参数：渠道类型字段非法
	 */
	public static final BaseException PARAM_STYPE_ILLEGAL = new BaseException(
			"10010002", "请求参数中的渠道类型字段非法");
	
	/**
	 * 请求参数：appId字段非法
	 */
	public static final BaseException PARAM_APPID_ILLEGAL = new BaseException(
			"10010003", "请求参数中的appId字段非法");
	

	/**
	 * 请求参数：数据签名字段非法
	 */
	public static final BaseException PARAM_SIGN_IS_ILLEGAL = new BaseException(
			"10010004", "请求参数中的数据签名字段非法");


	/**
	 * 具体异常码
	 */
	private String code;

	/**
	 * 异常信息
	 */
	private String msg;

	public BaseException() {
		super();
	}

	/**
	 * 格式化异常
	 * 
	 * @param code
	 * @param msg
	 * @param args
	 */
	public BaseException(String code, String msg, Object... args) {
		super(String.format(msg, args));
		this.code = code;
		this.msg = String.format(msg, args);
	}

	/**
	 * 实例化异常
	 * 
	 * @param msgFormat
	 * @param args
	 * @return
	 */
	public BaseException newInstance(String msgFormat, Object... args) {
		return new BaseException(this.code, msgFormat, args);
	}

	public BaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public BaseException(Throwable cause) {
		super(cause);
	}

	public BaseException(String message) {
		super(message);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
