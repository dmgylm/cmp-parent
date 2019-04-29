package cmp.common.util.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cmp.common.util.constant.ConfigConsts;
import cmp.common.util.constant.RequestConsts;
import cmp.common.util.encry.SignUtil;
import cmp.common.util.exception.BaseException;
import cmp.common.util.string.StringUtils;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 请求对象基类
 * 
 * @ClassName BaseRequest
 * @Description 验证参数
 * @author gqb
 * @date 2015-6-03
 */
public abstract class BaseRequest {

	private static final Logger LOGGER = LoggerFactory.getLogger(BaseRequest.class);

	/**
	 * 请求调用的接口名称
	 */
	protected String method;

	/**
	 * 发送请求的时间，格式"yyyyMMddHHmmss"
	 */
	protected String timestamp;

	/**
	 * 请求渠道类型（0：IOS，1：Android，2：H5）
	 */
	protected String sourceType;

	/**
	 * app的唯一标签（如：101：拓展APP，102:商户端APP，103：中行APP...）
	 */
	protected String appId;

	/**
	 * 请求参数的签名串
	 */
	protected String sign;

	public BaseRequest() {
		super();
	}

	/**
	 * 验证参数
	 * 
	 * @param o
	 *            参数请求对象
	 * @throws ParseException
	 * @throws ServiceCodeException
	 */
	public static void checkParams(Object obj) throws BaseException {
		// 1、校验时间戳是否在有效期范围内
		if (!checkTime(obj)) {
			LOGGER.error("时间戳字段非法!!");
			throw BaseException.PARAM_TIMESTAMP_IS_ILLEGAL;
		}
		// 2、渠道类型校验
		if (!checkSourceType(obj)) {
			LOGGER.error("渠道类型字段非法!!");
			throw BaseException.PARAM_STYPE_ILLEGAL;
		}
		// 3、appid校验
		if (!checkAppId(obj)) {
			LOGGER.error("appid校验字段非法!!");
			throw BaseException.PARAM_STYPE_ILLEGAL;
		}
		// 4、校验请求参数签名是否被篡改过
		if (!checkSign(obj)) {
			LOGGER.error("签名错误!!");
			throw BaseException.PARAM_SIGN_IS_ILLEGAL;
		}
	}

	/**
	 * 校验时间戳字段是否合法
	 * 
	 * @param obj
	 * @return
	 * @throws ParseException
	 */
	public static boolean checkTime(Object obj) {
		Map<String, Object> map = getMapFromObject(obj);
		String timestamp = map.get("timestamp") + "";
		if (StringUtils.isBlank(timestamp)) {
			LOGGER.error("请求参数中没有时间戳字段timestamp，该请求非法！");
			return false;
		} else {
			LOGGER.info("请求参数中的时间戳字段timestamp值为：{}", timestamp);
			try {
				// 1、接口的请求时间
				Date sendTime = new SimpleDateFormat(ConfigConsts.TIMESTAMP_FORMAT).parse(timestamp);
				// 2、当前时间
				Date nowDate = new Date();
				long minute = (nowDate.getTime() - sendTime.getTime()) / 60000;
				if (minute > 20) {// 发送请求的20分钟之内为有效期，超出该时间段则为非法请求
					LOGGER.error("请求校验失败！该请求的有效期已超过规定的时间范围：20分钟！");
					return false;
				}
			} catch (ParseException e) {
				e.printStackTrace();
				LOGGER.error("解析时间格式出现异常！异常信息为：{}，e：{}", e.getMessage(), e);
				throw BaseException.PARAM_TIMESTAMP_IS_ILLEGAL;
			}
			return true;
		}
	}

	/**
	 * 渠道类型校验
	 * 
	 * @param obj
	 * @return
	 */
	private static boolean checkSourceType(Object obj) {
		Map<String, Object> map = getMapFromObject(obj);
		String sType = map.get("sourceType") + "";
		if (StringUtils.isBlank(sType)) {
			LOGGER.error("请求参数中没有渠道类型字段sType，该请求非法！");
			return false;
		} else {
			LOGGER.info("请求参数中的渠道类型字段sType值为：{}", sType);
			if (!RequestConsts.STYPE_IOS.equals(sType) && !RequestConsts.STYPE_ANDROID.equals(sType)
					&& !RequestConsts.STYPE_H5.equals(sType)
					&& !RequestConsts.STYPE_EXPAND.equals(sType)
					&& !RequestConsts.STYPE_THIRDPARTY.equals(sType) ) {
				LOGGER.error("请求参数中渠道类型字段sType不在指定的范围内，该请求非法！");
				return false;
			}
			return true;
		}
	}

	/**
	 * app唯一标识appId字段校验
	 * 
	 * @param obj
	 * @return
	 */
	private static boolean checkAppId(Object obj) {
		Map<String, Object> map = getMapFromObject(obj);
		String appId = map.get("appId") + "";
		if (StringUtils.isBlank(appId)) {
			LOGGER.error("请求参数中没有appId字段，该请求非法！");
			return false;
		} else {
			LOGGER.info("请求参数中的字段appId值为：{}", appId);
			if (!RequestConsts.APPID_EXPAND.equals(appId) && !RequestConsts.APPID_MERCHANT.equals(appId)
					&& !RequestConsts.APPID_BANK.equals(appId)) {
				LOGGER.error("请求参数中app唯一标识字段appId不在指定的范围内，该请求非法！");
				return false;
			}
			return true;
		}
	}

	/**
	 * 检验请求的数据里面的签名是否合法，避免数据在传输的过程中被第三方篡改
	 * 
	 * @param o
	 *            请求数据对象
	 * @return 签名是否合法
	 */
	public static boolean checkSign(Object obj) {
		Map<String, Object> map = getMapFromObject(obj);
		LOGGER.info("checkSign>>>map===" + map.toString());
		String requestSign = map.get("sign") + "";// 获取请求参数中的数据签名
		if (requestSign == "" || requestSign == null) {
			LOGGER.error("请求签名为空或不存在!!!");
			return false;
		}
		LOGGER.info("请求的签名是:" + requestSign);
		// 清掉请求数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
		map.put("sign", "");
		// 将返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
		String responseSign = SignUtil.getSign(map);

		if (!responseSign.equals(requestSign)) {
			// 签名验不过，表示这个请求的数据有可能已经被篡改了
			LOGGER.error("请求的数据签名验证不通过，数据有可能被第三方篡改!!!");
			return false;
		}
		LOGGER.info("恭喜，数据签名验证通过!!!");
		return true;
	}

	/**
	 * 将对象属性转Map
	 * 
	 * @param object
	 *            待转换的实体对象
	 * @return 转换后的Map
	 */
	public static Map<String, Object> getMapFromObject(Object object) {
		Map<String, Object> map = new HashMap<String, Object>();
		Field[] fields = object.getClass().getDeclaredFields();// 获取本类声明的所有字段
		Field[] parentFields = object.getClass().getSuperclass().getDeclaredFields();// 获取父类中声明的所有字段，包括method，timestamp，sign字段
		// 1、获取父类中声明的所有字段
		for (Field parentField : parentFields) {
			parentField.setAccessible(true);
			Object value;
			try {
				value = parentField.get(object);
				String parentFieldName = parentField.getName();
				// 过滤掉父类中的日志字段LOGGER、LOG、serialVersionUID字段，这些字段不需要签名校验
				if (value != null && !parentFieldName.equals("LOGGER") && !parentFieldName.equals("LOG")
						&& !parentFieldName.equals("serialVersionUID")) {
					map.put(parentFieldName, value);
				}
			} catch (IllegalArgumentException e) {
				LOGGER.error("将对象属性转Map的方法getMap()执行出现异常！异常信息为：{}，e：{}", e.getMessage(), e);
			} catch (IllegalAccessException e) {
				LOGGER.error("将对象属性转Map的方法getMap()执行出现异常！异常信息为：{}，e：{}", e.getMessage(), e);
			}
		}
		// 2、获取本类中声明的所有字段
		for (Field field : fields) {
			field.setAccessible(true);
			Object value;
			try {
				value = field.get(object);
				String fieldName = field.getName();
				// 过滤掉本类中的日志字段LOGGER、LOG、serialVersionUID字段，这些字段不需要签名校验
				if (value != null && !fieldName.equals("LOGGER") && !fieldName.equals("LOG")
						&& !fieldName.equals("serialVersionUID")) {
					map.put(fieldName, value);
				}
			} catch (IllegalArgumentException e) {
				LOGGER.error("将对象属性转Map的方法getMap()执行出现异常！异常信息为：{}，e：{}", e.getMessage(), e);
			} catch (IllegalAccessException e) {
				LOGGER.error("将对象属性转Map的方法getMap()执行出现异常！异常信息为：{}，e：{}", e.getMessage(), e);
			}
		}
		LOGGER.info("转换成的map为：" + map.toString());
		return map;
	}

	/**
	 * 对参数按照key=value的格式，并按照参数名ASCII字典序排序
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public static String concatParameters(Map<String, String> map) throws Exception {
		List<Map.Entry<String, String>> mapList = sort(map);
		StringBuilder sb = new StringBuilder();
		int p = 0;
		for (Map.Entry<String, String> entry : mapList) {
			if (p++ > 0) {
				sb.append('&');
			}
			sb.append(entry.getKey()).append('=').append(entry.getValue());
		}
		return sb.toString();
	}

	/**
	 * 排序
	 * 
	 * @param map
	 * @return
	 */
	public static List<Map.Entry<String, String>> sort(Map<String, String> map) {
		List<Map.Entry<String, String>> mapList = new ArrayList<Map.Entry<String, String>>(map.entrySet());
		Collections.sort(mapList, new Comparator<Map.Entry<String, String>>() {
			public int compare(Map.Entry<String, String> mapping1, Map.Entry<String, String> mapping2) {
				return mapping1.getKey().compareTo(mapping2.getKey());
			}
		});
		return mapList;
	}

	/**
	 * 根据请求参数对象获取请求参数签名
	 * 
	 * @param obj
	 *            请求参数对象
	 * @return
	 */
	public static String getSign(Object obj) {
		// 1、将传入的实体类转换为Map，Map中的key为实体类的属性名，value为实体类属性对应的值
		Map<String, Object> map = getMapFromObject(obj);
		// 2、清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
		map.put("sign", "");
		// 将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
		return SignUtil.getSign(map);
	}

	/**
	 * 请求调用的接口名称
	 * 
	 * @return
	 */
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * 发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
	 * 
	 * @return
	 */
	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	/**
	 * 获取请求参数的签名串
	 * 
	 * @return
	 */
	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

}