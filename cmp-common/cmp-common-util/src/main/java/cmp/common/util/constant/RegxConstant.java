package cmp.common.util.constant;

public interface RegxConstant {

	/**
	 * 手机号校验
	 */
	public static final String PHONE_RGX="^1[0-9]{10}$";

	/**
	 * 不超过50个字符
	 */
	public static final String NO_MORE_THAN_50="\\S{0,50}";

	/**
	 * 不超过200个字符
	 */
	public static final String NO_MORE_THAN_200="\\S{0,200}";

	/**
	 * 单编号
	 */
	public static final String ORDER_NO="^DO[0-9]{4}(0[1-9]{1}|1[1-2]{1})(0[1-9]{1}|[1-3]{1}[0-9]{1})$";


	/**
	 * 机油编号 Motor oil
	 */

	public static final String OIL_NO="^EO[a-zA-Z0-9]{4}$";


}
