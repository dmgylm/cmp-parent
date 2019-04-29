package sd.cmp.common.resultdeal;



public enum BaseEnum {

	SUCCESS("0","成功"),

	PARAM_ERROR("1","参数不正确"),


	//-------------------------
	CREATE_BATCH_NO_FAIL ("1106000", "生成批次号失败！"),
	INCOME_SETTLE_NOT_EXSIT("1106001","结算记录不存在"),
	SAVE_SETTLE_DETAIL_FAIL("11060002", "结算记录详情保存失败"),


	SAVE_SHOP_SETTLE_FAIL("11060003", "商户结算记录保存失败"),
	SAVE_SHOP_SETTLE_DETAIL_FAIL("11060004", "商户结算记录详情保存失败"),
	SAVE_HISTORY_SETTLE_FAIL("11060005", "历史结算记录保存失败"),
	SAVE_HISTORY_SETTLE_DETAIL_FAIL("11060006", "历史结算记录详情保存失败"),
	//-----------------------

	SYSTEM_ERROR("2","系统异常"),

	START_TIME_IS_BLANK ("1006029", "请选择起始时间！"),

	END_TIME_IS_BLANK ("1006031", "请选择结束时间！"),


	MONEY_FORMAT_ERROR("10060041", "浮动金额格式不正确！"),

	SAVE_SETTLE_FAIL("10060001", "结算记录保存失败"),


	INCOME_SETTLE_UPDATE_FAIL("10060008","更新收入结算记录信息失败"),

	UPDATE_SETTLE_RECORD_STATE_FAIL("10060015", "确认收款后更新结算记录状态为已结算失败"),

	ID_IS_NULL("10060007", "收入结算记录ID为空"),

	DELETE_SETTLE_DETAIL_FAILED("10060013", "撤销后删除结算记录明细失败"),

	DELETE_SETTLE_RECORD_FAILED("10060014", "撤销后删除结算记录失败"),

	ORDER_ID_IS_BLANK ("1006024", "订单编号为空！"),

	ORDER_IS_NULL("10060006", "订单不存在"),

	BATCH_ID_IS_BLANK("10060032", "批次号为空！"),

	SETTLE_BATCH_IS_NULL("10060033", "批次号不存在！"),

	SETTLE_IS_NULL ("10060005", "结算记录不存在"),

	SHOPCODE_IS_BLANK("10060019", "商户编号为空"),

	NOT_RELATE_THE_SAME_SHOP("10060020", "不是属于同一个商户"),

	NOT_RELATE_THE_SAME_SHOP_SOURCE ("10060040", "所选订单不属于同一个商户同一个机构！"),

	ORDER_ONE_KEY_SETTLE("10060021", "单项订单一键结算失败！"),

	SAVE_SETTLE_DETAIL_ERROR ("10060036", "保存支出待结算记录明细失败！"),

	SETTLE_ID_IS_BLANK("10060004", "结算记录主键id为空"),

	CANCEL_ONE_KEY_SHOP_SETTLE_ERROR ("10060048", "取消订单的商户结算-一键结算异常！"),

	BATCH_SETTLE_OUTCOME_ORDER_ERROR("10060046", "非预约类订单支出一键结算失败！"),

	BATCH_ID_IS_OPERATING("10060049", "该批次号正在结算，请勿重新操作！"),

	RECORD_HAVE_ALREADY_SETTLED("10060028", "该结算记录已完成结算！"),
	ORDER_TYPE_IS_BLANK("10060023", "订单类型字段为空！"),

	SETTLE_NO_IS_BLANK ("1006033", "结算编号为空！"),

	CANCEL_SHOP_SETTLE_ERROR("1006049", "撤销商户结算记录失败");

	private String code;
	private String message;

	BaseEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
