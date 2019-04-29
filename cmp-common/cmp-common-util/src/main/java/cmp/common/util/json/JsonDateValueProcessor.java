package cmp.common.util.json;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ltf 解决java转json 日期格式问题
 */
public class JsonDateValueProcessor implements JsonValueProcessor {

	private String datePattern = "yyyy-MM-dd HH:mm:ss";

	public JsonDateValueProcessor() {

	}

	public JsonDateValueProcessor(String datePattern) {
		this.datePattern = datePattern;
	}

	@Override
	public Object processArrayValue(Object arg0, JsonConfig arg1) {
		// TODO Auto-generated method stub
		return process(arg0);
	}

	@Override
	public Object processObjectValue(String arg0, Object arg1, JsonConfig arg2) {
		// TODO Auto-generated method stub
		return process(arg1);
	}

	public Object process(Object value) {
		String val = "";
		try {
			if (value instanceof Date) {
				SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
				val = dateFormat.format(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return val;
	}

}
