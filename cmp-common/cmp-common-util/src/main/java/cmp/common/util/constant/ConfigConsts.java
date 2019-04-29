package cmp.common.util.constant;

/**
 * 系统配置相关常量类
 * 
 * @author sd
 * @date 2017年7月13日
 */
public class ConfigConsts {

	/**
	 * 默认时间格式：yyyy-MM-dd HH:mm:ss
	 */
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 默认时间格式：yyyy-MM-dd
	 */
	public static final String DATE_TIME_FORMAT_NEW = "yyyy-MM-dd";
	/**
	 * 默认时间戳格式：yyyyMMddHHmmss
	 */
	public static final String TIMESTAMP_FORMAT = "yyyyMMddHHmmss";

	/**
	 * 订单使用时间格式：yyyy-MM-dd
	 */
	public static final String ORDER_USE_TIME_FORMAT = "yyyy-MM-dd";

	/**
	 * Date默认时区
	 */
	public static final String DATE_TIMEZONE = "GMT+8";

	/**
	 * UTF-8字符集
	 */
	public static final String CHARSET_UTF8 = "UTF-8";

	/**
	 * 请求秘钥
	 */
	public static final String KEY = ConfigHelp.getString("common.key");

	/**
	 * 百度地图相关
	 */
	public static final String BAIDUMAP_GET_CITY_URL = ConfigHelp.getString("baiduMap.cityUrl");

	/**
	 * 百度地图callback
	 */
	public static final String BAIDUMAP_CALLBACK_CODE = ConfigHelp.getString("baiduMap.callback");

	/**
	 * 百度地图AK
	 */
	public static final String BAIDUMAP_AK = ConfigHelp.getString("baiduMap.ak");

	/**
	 * 查询地区
	 */
	public static final String BAIDUMAP_SEARCHLOCATION_URL = ConfigHelp.getString("searchlocation");

	/**
	 * 文件上传成功返回的状态码
	 */
	public static final String FILE_UPLOAD_CODE_SUCCESS = "SUCCESS";

	/**
	 * 文件上传成功返回的状态码
	 */
	public static final String FILE_UPLOAD_CODE_FAILED = "FAILED";

	/**
	 * 文件上传成功返回的信息
	 */
	public static final String FILE_UPLOAD_MSG_SUCCESS = "文件上传成功!";

	/**
	 * 文件上传成功返回的信息
	 */
	public static final String FILE_UPLOAD_MSG_FAILED = "文件上传失败!";

	/**
	 * 响应成功返回的状态码：SUCCESS
	 */
	public static final String RESPONSE_CODE_SUCCESS = "SUCCESS";

}
