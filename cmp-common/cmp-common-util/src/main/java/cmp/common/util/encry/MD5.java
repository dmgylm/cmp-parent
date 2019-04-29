package cmp.common.util.encry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import cmp.common.util.string.StringConverter;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class MD5 {

    private static final Log log = LogFactory.getLog(MD5.class);

    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b",
            "c", "d", "e", "f"};

    /**
     * The jce MD5 message digest generator.
     */
    private static MessageDigest md5;

    /**
     * * Retrieves a hexidecimal character sequence representing the MD5 * digest of the specified character sequence, using the specified * encoding
     * to first convert the character sequence into a byte sequence. * If the specified encoding is null, then ISO-8859-1 is assumed * *
     *
     * @param srcStr      the string to encode. *
     * @param charsetName the encoding used to convert the string into the * byte sequence to submit for MD5 digest *
     * @return a hexidecimal character sequence representing the MD5 * digest of the specified string *
     * @throws HsqlUnsupportedOperationException if an MD5 digest * algorithm is not available through the * java.security.MessageDigest spi or the requested * encoding is not
     *                                           available
     */
    public static final String encodeString(String srcStr, String charsetName) throws RuntimeException {
        return StringConverter.byteToHex(digestString(srcStr, charsetName));
    }

    /**
     * * Retrieves a hexidecimal character sequence representing the
     * MD5 * digest of the specified character sequence, using the
     * specified * encoding to first convert the character sequence
     * into a byte sequence. * If the specified encoding is null, then
     * ISO-8859-1 is assumed * *
     *
     * @param string   the string to encode. *
     * @param encoding the encoding used to convert the string into
     *                 the * byte sequence to submit for MD5 digest *
     * @return a hexidecimal character sequence representing the MD5 *
     * digest of the specified string *
     * @throws HsqlUnsupportedOperationException if an MD5 digest *
     *                                           algorithm is not available through the *
     *                                           java.security.MessageDigest spi or the requested *
     *                                           encoding is not available
     */
    public static final String encodeString(String string)
            throws RuntimeException {
        return StringConverter.byteToHex(digestString(string));
    }

    /**
     * * Retrieves a byte sequence representing the MD5 digest of the * specified character sequence, using the specified encoding to * first convert
     * the character sequence into a byte sequence. * If the specified encoding is null, then ISO-8859-1 is * assumed. * *
     *
     * @param string   the string to digest. *
     * @param encoding the character encoding. *
     * @return the digest as an array of 16 bytes. *
     * @throws HsqlUnsupportedOperationException if an MD5 digest * algorithm is not available through the * java.security.MessageDigest spi or the requested * encoding is not
     *                                           available
     */
    public static byte[] digestString(String string, String encoding) throws RuntimeException {
        byte[] data;
        if (encoding == null) {
            encoding = "ISO-8859-1";
        }
        try {
            data = string.getBytes(encoding);
        } catch (UnsupportedEncodingException x) {
            throw new RuntimeException(x.toString());
        }
        return digestBytes(data);
    }

    /**
     * * Retrieves a byte sequence representing the MD5 digest of the *
     * specified character sequence, using the specified encoding to *
     * first convert the character sequence into a byte sequence. * If
     * the specified encoding is null, then ISO-8859-1 is * assumed. * *
     *
     * @param string   the string to digest. *
     * @param encoding the character encoding. *
     * @return the digest as an array of 16 bytes. *
     * @throws HsqlUnsupportedOperationException if an MD5 digest *
     *                                           algorithm is not available through the *
     *                                           java.security.MessageDigest spi or the requested *
     *                                           encoding is not available
     */
    public static byte[] digestString(String string)
            throws RuntimeException {
        byte[] data;
        String encoding = "UTF-8";
        try {
            data = string.getBytes(encoding);
        } catch (UnsupportedEncodingException x) {
            throw new RuntimeException(x.toString());
        }
        return digestBytes(data);
    }

    /**
     * * Retrieves a byte sequence representing the MD5 digest of the * specified byte sequence. * *
     *
     * @param data the data to digest. *
     * @return the MD5 digest as an array of 16 bytes. *
     * @throws HsqlUnsupportedOperationException if an MD5 digest * algorithm is not available through the * java.security.MessageDigest spi
     */
    public static final byte[] digestBytes(byte[] data) throws RuntimeException {
        synchronized (MD5.class) {
            if (md5 == null) {
                try {
                    md5 = MessageDigest.getInstance("MD5");
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e.toString());
                }
            }
            return md5.digest(data);
        }
    }

    /**
     * 针对迷彩的MD5加密
     */
    public static String encryptStringWithMD5(String strData) {
        strData = strData.trim();
        String digest = "";
        try {
            MessageDigest currentAlgorithm = MessageDigest.getInstance("md5");
            currentAlgorithm.reset();
            byte[] mess = strData.getBytes();
            byte[] hash = currentAlgorithm.digest(mess);
            for (int i = 0; i < hash.length; i++) {
                int v = hash[i];
                if (v < 0) {
                    v = 256 + v;
                }
                if (v < 16) {
                    digest += "0";
                }
                digest += Integer.toString(v, 16).toUpperCase() + "";
            }
        } catch (NoSuchAlgorithmException e) {
            log.error("MD5 Arithmetic Can Not Be Used Error!");
        }
        return digest;
    }

    public static String string2MD5(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();

    }

    /**
     * 加密解密算法 执行一次加密，两次解密
     */
    public static String convertMD5(String inStr) {

        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        String s = new String(a);
        return s;

    }

    /**
     * 加密算法
     *
     * @param b
     * @return
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuilder resultSb = new StringBuilder();
        for (byte aB : b) {
            resultSb.append(byteToHexString(aB));
        }
        return resultSb.toString();
    }

    /**
     * 加密算法
     *
     * @param b
     * @return
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * MD5加密
     *
     * @param ctx         待加密字符串
     * @param charactName
     * @return 加密后串码
     */
    public static String MD5Encode(String ctx, String charactName) {
        if (charactName == null || charactName.equals("")) {
            charactName = "UTF-8";
        }
        String resultString = null;
        try {
            resultString = ctx;
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString.getBytes(charactName)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }

    public static void main(String[] args) {
        /*
         * String ss = encodeString("234","GB2312"); System.out.println(ss);
		 * 
		 * byte[] digestString = digestString("289dff07669d7a23de0ef88d2f7129e7","GB2312"); for(int i=0;i<digestString.length;i++){
		 * System.out.println(digestString[i]); }
		 */
        encodeString("234", "GB2312");

        System.out.println(encodeString("234", "GB2312"));

        // String s = new String("tangfuqiang");
        // System.out.println("原始：" + s);
        // System.out.println("MD5后：" + string2MD5(s));
        // System.out.println("加密的：" + convertMD5(s));
        System.out.println("解密的：" + convertMD5(convertMD5("234")));

    }

}