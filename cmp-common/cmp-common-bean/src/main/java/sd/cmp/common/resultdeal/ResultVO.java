package sd.cmp.common.resultdeal;



import java.io.Serializable;

public class ResultVO<T> implements Serializable {

	private static final long serialVersionUID = 8960474786737581150L;

	/**
	 * 错误码
	 */
	private String code;
	/**
	 *提示信息
	 */
	private String msg;
	/**
	 * 具体内容
	 */
	private T data;

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

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
