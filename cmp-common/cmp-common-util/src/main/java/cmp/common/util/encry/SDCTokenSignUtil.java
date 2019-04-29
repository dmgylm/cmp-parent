package cmp.common.util.encry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import cmp.common.util.constant.ConfigConsts;
import cmp.common.util.encry.bankencry.HttpClient3;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * c端请求参数加密工具类
 *
 * @author lyh
 */
public class SDCTokenSignUtil {

	private static final Log LOGGER = LogFactory.getLog(SDCTokenSignUtil.class);
	// 安全秘钥
	public static final String DES_KEY = "D5ED1E14";
	// iv偏移量
	public static final String DES_IV = "9ACBE716";
	// 签名默认值
	public static final String DEFAULT_VAULE = "SHENGDACPROJECT";
	// 系统唯一标识
	public static final String SYSTEM_CODE = "SDCSP";

	public static final String ACCESS_TOKEN_URL = "https://test.chengniu.com:9102/api/common/getAccessToken";

//	public static final String ACCESS_TOKEN_URL = "http://192.168.101.140:8082/api/common/getAccessToken";

	public static final String SOURCE_DATA_KEY = "data";

	public static final String DEFAULT_CHARSET = "UTF-8";

	public static final String CONTENT_TYPE = "application/json";

	public static String accessToken = "94c4029437ab467289f4907b38dbe64d";

	public static String createOriginalBody(String jsonData) throws Exception {
		JSONObject jsonObj = new JSONObject();
		String accessToken = getAccessToken().getAccessToken();
		jsonObj.put("body", createOriginal(jsonData, accessToken));
		return jsonObj.toJSONString();
	}

	public static String createOriginalBody(String jsonData, String accessToken) throws Exception {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("body", createOriginal(jsonData, accessToken));
		return jsonObj.toJSONString();
	}

	public static String getJsonDataBody(String originalBody) {
		return getJsonData(originalBody);
	}

	@SuppressWarnings("unchecked")
	public static String createOriginal(String jsonData, String accessToken) throws Exception {
		Map<String, Object> param = (Map<String, Object>) JSON.parse(jsonData);
		// 系统标识
		param.put("systemCode", SYSTEM_CODE);
		// 访问秘钥
		param.put("accessToken", accessToken);
		// 时间戳
		param.put("timestamp", System.currentTimeMillis());
		// 签名
		param.put("signature", getSign(param));
		param.remove("accessToken");
		try {
			return encryptDES(JSON.toJSONString(param));
		} catch (Exception e) {
			return null;
		}
	}

	public static String getJsonData(String original) {

		String data = null;
		try {
			// 解密参数
			data = decryptDES(original);
		} catch (Exception e) {
			LOGGER.error("解密异常", e);
			throw new UnsupportedOperationException("非法字符串");
		}

		// json转换
		JSONObject jsonObj = (JSONObject) JSON.parseObject(data);

		return jsonObj.toJSONString();
	}

	// 加密
	public static String encryptDES(String encryptString) throws Exception {
		IvParameterSpec zeroIv = new IvParameterSpec(DES_IV.getBytes());
		SecretKeySpec key = new SecretKeySpec(DES_KEY.getBytes(), "DES");
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
		byte[] encryptedData = cipher.doFinal(encryptString.getBytes("UTF-8"));

		return cmp.common.util.encry.bankencry.Base64.encode(encryptedData);
	}

	// 解密
	public static String decryptDES(String decryptString) throws Exception {
		byte[] byteMi = cmp.common.util.encry.bankencry.Base64.decode(decryptString);
		IvParameterSpec zeroIv = new IvParameterSpec(DES_IV.getBytes());
		SecretKeySpec key = new SecretKeySpec(DES_KEY.getBytes(), "DES");
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
		byte decryptedData[] = cipher.doFinal(byteMi);

		return new String(decryptedData, "UTF-8");
	}

	// 生成签名
	public static String getSign(Map<String, Object> param) {
		List<String> list = new ArrayList<String>();
		for (Map.Entry<String, Object> entry : param.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (null != value && "" != value && !"".equals(value)) {
				if (value instanceof String || value instanceof Integer || value instanceof Float
						|| value instanceof BigDecimal || value instanceof Double || value instanceof Long
						|| value instanceof Boolean) {
					list.add(key + "=" + value + "&");
				} else {
					// 若value是复杂类型，则将value用固定值进行替换
					list.add(key + "=" + DEFAULT_VAULE + "&");
				}
			}
		}
		int size = list.size();
		String[] arrayToSort = list.toArray(new String[size]);
		Arrays.sort(arrayToSort, java.lang.String.CASE_INSENSITIVE_ORDER);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < size; i++) {
			sb.append(arrayToSort[i]);
		}
		String result = sb.toString();
		result = result.substring(0, result.length() - 1).toUpperCase();
		return MD5.encodeString(result, ConfigConsts.CHARSET_UTF8);
	}

	// 获取访问秘钥(k:token,v:tokenDeadline)
	public static SDCTokenVO getAccessToken() throws Exception {
		SDCTokenVO sdcTokenVO = new SDCTokenVO();
		JSONObject jsonObj = new JSONObject();
		try {
			JSONObject requestObj = new JSONObject();
			requestObj.put("systemCode", SYSTEM_CODE);
			HttpClient3 httpClient3 = new HttpClient3();
			String encryptDES = encryptDES(requestObj.toJSONString());
			JSONObject paramObj = new JSONObject();
			paramObj.put("body", encryptDES);
			// 请求
			String result = httpClient3.doPost(ACCESS_TOKEN_URL, paramObj.toJSONString(), CONTENT_TYPE);
			// 转换为json
			String jsonData = SDCTokenSignUtil.getJsonDataBody(result);
			jsonObj = JSONObject.parseObject(jsonData);
		} catch (Exception e) {
			LOGGER.error("get SDC token failed,{}", e);
			throw e;
		}
		// 成功
		if ("0000".equals((String) jsonObj.get("resultCode"))) {
			JSONObject resultJson = (JSONObject) jsonObj.get(SOURCE_DATA_KEY);
			sdcTokenVO.setAccessToken((String) resultJson.get("accessToken"));
			sdcTokenVO.setTokenDeadLine((String) resultJson.get("endTimeStr"));
		} else {
			String resultDesc = (String) jsonObj.get("resultDesc");
			LOGGER.error("get SDC token failed,exception is :" + resultDesc);
			throw new Exception(resultDesc);
		}
		return sdcTokenVO;
	}
}
