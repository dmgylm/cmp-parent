package sd.cmp.common.resultdeal;



public class BaseExcetion extends RuntimeException{

	private String code;
	private String message;
	private BaseEnum resultEnum;

	public BaseExcetion(String message, String code) {
		this.message=message;
		this.code = code;
	}

	public BaseExcetion(BaseEnum resultEnum) {
		this.message=resultEnum.getMessage();
		this.code = resultEnum.getCode();
		this.resultEnum=resultEnum;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public BaseEnum getResultEnum() {
		return resultEnum;
	}

	public void setResultEnum(BaseEnum resultEnum) {
		this.resultEnum = resultEnum;
	}
}
