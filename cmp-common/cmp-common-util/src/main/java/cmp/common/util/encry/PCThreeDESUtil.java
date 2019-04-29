package cmp.common.util.encry;

import org.bouncycastle.util.encoders.UrlBase64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;

/**
 * @author sd
 * @date 2018/01/15
 */
public class PCThreeDESUtil {

    private static final Logger log = LoggerFactory.getLogger(PCThreeDESUtil.class);

    private static final String CRYPT_ALGORITHM = "DESede";

    private static String result = "sktFA8BSLm9j0xcQq6ri8pBDkg2lT0Nlw4EzYGJMbor8afFahZ1t2MCH5Z2P-1VBeQBncGBn9MJ1jdKWe5bnQZWNWfx2diq0GvXqtO2CiBlowoLPQKX9ktI4UB3UkeRofP-ZW4-xf3hRUOVkUxTjcs0aUlbstF9ZuazRmcIPJjlDIHVE49pPUIt51fyWWDa8NSl9rt318htJYI93uMXZVA..";

    /**
     * 解密
     *
     * @param value
     * @param key
     * @return
     * @throws Exception
     */
    public static String decrypt(String value, String key) throws Exception {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), CRYPT_ALGORITHM);
            Cipher cipher = Cipher.getInstance(CRYPT_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decodedByte = UrlBase64.decode(value.getBytes("UTF-8"));
            byte[] decryptedByte = cipher.doFinal(decodedByte);
            return new String(decryptedByte, "UTF-8");
        } catch (Exception e) {
            log.error("PCThreeDESUtil.decrypt : decrypt was failed! The error message is: {}!", e);
            throw e;
        }
    }

    /**
     * 加密
     *
     * @param value
     * @param key
     * @return
     * @throws Exception
     */
    public static String encrypt(String value, String key) throws Exception {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), CRYPT_ALGORITHM);
            Cipher cipher = Cipher.getInstance(CRYPT_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

            byte[] encryptedByte = cipher.doFinal(value.getBytes("UTF-8"));
            byte[] encodedByte = UrlBase64.encode(encryptedByte);
            return new String(encodedByte, "UTF-8");
        } catch (Exception e) {
            log.error("PCThreeDESUtil.encrypt : encrypt was failed! The error message is: {}!", e);
            throw e;
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        String key = "C405BC5839533270jUN1d77Y";
        String szSrc = "abc|123";
        System.out.println("加密前的字符�?" + szSrc);
        String encoded = encrypt(szSrc, key);
        System.out.println("加密后的字符�?" + encoded);
        String xmString = new String(result.getBytes("GBK"));
        String strUTF8 = URLEncoder.encode(xmString, "GBK");
        System.out.println("转编码后的字符?" + strUTF8);
        String srcBytes = decrypt(result, key);
        System.out.println("解密后的字符�?" + srcBytes);
    }
}
