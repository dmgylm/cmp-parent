package cmp.common.util.encry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cmp.common.util.constant.ConfigConsts;
import cmp.common.util.string.StringUtils;

import java.util.*;

/**
 * 签名工具类
 * 
 * @author gqb
 * @date 2017年8月8日
 */
public class SignUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(SignUtil.class);
	/**
	 * 使用MD5对请求参数进行签名
	 * 
	 * @param map
	 *            请求参数
	 * @return 签名后的字符串
	 */
	public static String getSign(Map<String, Object> map) {
		List<String> list = new ArrayList<String>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (null != value && "" != value && !"".equals(value)) {
				if (value instanceof String || value instanceof Integer || value instanceof Float
						|| value instanceof Double || value instanceof Long || value instanceof Boolean) {
					list.add(key + "=" + value + "&");
				} else {// 若value是复杂类型，则将value用固定的String类型值进行替换
					list.add(key + "=" + "AUTOSHENGDA" + "&");
				}
			}
		}
		int size = list.size();
		String[] arrayToSort = list.toArray(new String[size]);
		Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < size; i++) {
			sb.append(arrayToSort[i]);
		}
		String result = sb.toString();
		result += "key=" + ConfigConsts.KEY;
		LOGGER.info("result:" + result);
		return MD5.encodeString(result, "UTF-8");
	}

	/**
	 * 将Map中的key根据ASCII字典顺序进行排序
	 * 
	 * @param paramMap
	 * @return
	 */
	public static Map<String, String> getSortedMap(Map<String, Object> paramMap) {
		Map<String, String> sortedMap = new TreeMap<String, String>();
		if (null != paramMap && paramMap.size() > 0) {
			SDHashMap params = new SDHashMap();
			for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
				params.put(entry.getKey(), entry.getValue());
			}
			sortedMap.putAll(params);
		}
		return sortedMap;
	}

	/**
	 * 将有序的Map转换为key=value的拼接形式字符串
	 * 
	 * @param sortedMap
	 * @return
	 */
	public static String getSignContent(Map<String, String> sortedMap) {
		StringBuffer content = new StringBuffer();
		List<String> keys = new ArrayList<String>(sortedMap.keySet());
		Collections.sort(keys);
		int index = 0;
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = sortedMap.get(key);
			if (StringUtils.areNotEmpty(key, value)) {
				content.append((index == 0 ? "" : "&") + key + "=" + value);
				index++;
			}
		}
		return content.toString();
	}

}
