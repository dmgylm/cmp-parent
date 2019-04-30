package cmp.common.util.constant;

public enum SMSVariable {
	/**
	 * 保养客户类型
	 */
	BAOYANG_CUSTOMER_TYPE("BAOYANG","customer_type","保养预约客户类型");

	private String smsType;

	private String smsVal;

	private String smsValLiteral;


	SMSVariable(String smsType, String smsVal, String smsValLiteral) {
		this.smsType = smsType;
		this.smsVal = smsVal;
		this.smsValLiteral = smsValLiteral;
	}
}
