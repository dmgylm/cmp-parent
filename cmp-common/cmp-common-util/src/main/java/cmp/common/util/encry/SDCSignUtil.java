package cmp.common.util.encry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * c端请求参数加密工具类
 *
 * @author cmq
 */
public class SDCSignUtil {

    private static final Log LOGGER = LogFactory.getLog(SDCSignUtil.class);
    //安全秘钥
    public static final byte[] SECRET_KEY_CSP = "50F7A4D9".getBytes();
    //iv偏移量
    public static final byte[]  IV_KEY_CSP = "AB8BAD24".getBytes();
    //MD5加密串
    public static final String MD5_KEY_CSP = "ab610c3bfb3a85f35b4abb1b1ee17a93";

    public static final String SOURCE_DATA_KEY = "jsonData";

    public static final String DEFAULT_CHARSET = "UTF-8";


    public static String encryptDES(String encryptString)
            throws Exception {
        IvParameterSpec zeroIv = new IvParameterSpec(IV_KEY_CSP);
        SecretKeySpec key = new SecretKeySpec(SECRET_KEY_CSP, "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
        byte[] encryptedData = cipher.doFinal(encryptString.getBytes("UTF-8"));

        return cmp.common.util.encry.bankencry.Base64.encode(encryptedData);
    }


    //解密
    public static String decryptDES(String decryptString)
            throws Exception {
        byte[] byteMi =  cmp.common.util.encry.bankencry.Base64.decode(decryptString);
        IvParameterSpec zeroIv = new IvParameterSpec(IV_KEY_CSP);
        SecretKeySpec key = new SecretKeySpec(SECRET_KEY_CSP, "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
        byte decryptedData[] = cipher.doFinal(byteMi);

        return new String(decryptedData,"UTF-8");
    }


    private static String getKeyString(String data) {
        StringBuffer sb = new StringBuffer();
        sb.append(data.trim());
        sb.append(MD5_KEY_CSP);
        return sb.toString();
    }

    public static String getJsonDataBody(String originalBody) {
        // json转换
        JSONObject jsonObj = (JSONObject) JSON.parseObject(originalBody);

        return  getJsonData((String) jsonObj.get("body"));
    }

    public static String createOriginal(String jsonData){
        String sign = MD5.encodeString(getKeyString(jsonData), DEFAULT_CHARSET);
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("sign",sign);
        jsonObj.put(SOURCE_DATA_KEY,jsonData);
        try {
            return encryptDES(jsonObj.toJSONString());
        } catch (Exception e) {
            return null;
        }
    }

    public static String createOriginalBody(String jsonData){
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("body",createOriginal(jsonData));
        return jsonObj.toJSONString();
    }

    public static String getJsonData(String original){

        String data = null;
        try {
            // 解密参数
            data = decryptDES(original);
        } catch (Exception e) {
            LOGGER.error("解密异常",e);
            throw new UnsupportedOperationException("非法字符串");
        }

        // json转换
        JSONObject jsonObj = (JSONObject) JSON.parseObject(data);
        String jsonData = (String) jsonObj.get(SOURCE_DATA_KEY);
        //log.info("==========>DES解密后的请求参数：jsonData：" + jsonData);

        String sign = MD5.encodeString(getKeyString(jsonData), DEFAULT_CHARSET);
        if (!((String) jsonObj.get("sign")).equals(sign)) {
            LOGGER.error("sign验证失败,sign:"+sign);
            throw new UnsupportedOperationException("非法字符串");
        }
        return jsonData;
    }

    public static void main(String[] args) {

        try {
            String data = getJsonData("dKoFc0Me4Zvj65A0/UCN/bd342Fy96LpuTQMkt5Re/p1bcBTBpZgtqpxGYuz3OAWPCbEwQS8oh4B3knX6MqPg0KZgw1tbW+BVN6vh9j0PbENOl5GAYvi0Q==");
            System.out.println(data);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
