package cmp.common.util.encry;

import cmp.common.util.constant.ConfigConsts;
import cmp.common.util.string.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * 纯字符串字典结构
 * 
 * @author sd
 * @date 2017年7月15日
 */
public class SDHashMap extends HashMap<String, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7718366216820000534L;

	public SDHashMap() {
		super();
	}

	public SDHashMap(Map<? extends String, ? extends String> m) {
		super(m);
	}

	public String put(String key, String value) {
		if (StringUtils.areNotEmpty(key, value)) {
			return super.put(key, value);
		} else {
			return null;
		}
	}

	public String put(String key, Object value) {
		String strValue;
		if (null == value) {
			strValue = null;
		} else if (value instanceof String) {
			strValue = (String) value;
		} else if (value instanceof Integer) {
			strValue = ((Integer) value).toString();
		} else if (value instanceof Long) {
			strValue = ((Long) value).toString();
		} else if (value instanceof Float) {
			strValue = ((Float) value).toString();
		} else if (value instanceof Double) {
			strValue = ((Double) value).toString();
		} else if (value instanceof Boolean) {
			strValue = ((Boolean) value).toString();
		} else if (value instanceof Date) {
			DateFormat format = new SimpleDateFormat(ConfigConsts.DATE_TIME_FORMAT);
			format.setTimeZone(TimeZone.getTimeZone(ConfigConsts.DATE_TIMEZONE));
			strValue = format.format((Date) value);
		} else {
			strValue = value.toString();
		}
		return strValue;
	}
}
