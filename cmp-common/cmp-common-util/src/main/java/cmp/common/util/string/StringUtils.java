package cmp.common.util.string;


import cmp.common.util.constant.BankParamConsts;
import cmp.common.util.encry.bankencry.Base64;

import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 字符串工具类
 * 
 * @author sd
 * @date 2017年7月15日
 */
public class StringUtils {

    private StringUtils() {
    }

    // Empty checks
    // -----------------------------------------------------------------------
    /**
     * <p>
     * Checks if a CharSequence is empty ("") or null.
     * </p>
     *
     * <pre>
     * StringUtils.isEmpty(null)      = true
     * StringUtils.isEmpty("")        = true
     * StringUtils.isEmpty(" ")       = false
     * StringUtils.isEmpty("bob")     = false
     * StringUtils.isEmpty("  bob  ") = false
     * </pre>
     *
     * <p>
     * NOTE: This method changed in Lang version 2.0. It no longer trims the CharSequence. That
     * functionality is available in isBlank().
     * </p>
     *
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is empty or null
     * @since 3.0 Changed signature from isEmpty(String) to isEmpty(CharSequence)
     */
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * <p>
     * Checks if a CharSequence is not empty ("") and not null.
     * </p>
     *
     * <pre>
     * StringUtils.isNotEmpty(null)      = false
     * StringUtils.isNotEmpty("")        = false
     * StringUtils.isNotEmpty(" ")       = true
     * StringUtils.isNotEmpty("bob")     = true
     * StringUtils.isNotEmpty("  bob  ") = true
     * </pre>
     *
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is not empty and not null
     * @since 3.0 Changed signature from isNotEmpty(String) to isNotEmpty(CharSequence)
     */
    public static boolean isNotEmpty(CharSequence cs) {
        return !StringUtils.isEmpty(cs);
    }

    /**
     * <p>
     * Checks if a CharSequence is whitespace, empty ("") or null.
     * </p>
     *
     * <pre>
     * StringUtils.isBlank(null)      = true
     * StringUtils.isBlank("")        = true
     * StringUtils.isBlank(" ")       = true
     * StringUtils.isBlank("bob")     = false
     * StringUtils.isBlank("  bob  ") = false
     * </pre>
     *
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is null, empty or whitespace
     * @since 2.0
     * @since 3.0 Changed signature from isBlank(String) to isBlank(CharSequence)
     */
    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>
     * Checks if a CharSequence is not empty (""), not null and not whitespace only.
     * </p>
     *
     * <pre>
     * StringUtils.isNotBlank(null)      = false
     * StringUtils.isNotBlank("")        = false
     * StringUtils.isNotBlank(" ")       = false
     * StringUtils.isNotBlank("bob")     = true
     * StringUtils.isNotBlank("  bob  ") = true
     * </pre>
     *
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is not empty and not null and not whitespace
     * @since 2.0
     * @since 3.0 Changed signature from isNotBlank(String) to isNotBlank(CharSequence)
     */
    public static boolean isNotBlank(CharSequence cs) {
        return !StringUtils.isBlank(cs);
    }

    /**
     * 检查指定的字符串是否为空。
     * <ul>
     * <li>SysUtils.isEmpty(null) = true</li>
     * <li>SysUtils.isEmpty("") = true</li>
     * <li>SysUtils.isEmpty("   ") = true</li>
     * <li>SysUtils.isEmpty("abc") = false</li>
     * </ul>
     * 
     * @param value 待检查的字符串
     * @return true/false
     */
    public static boolean isEmpty(String value) {

        org.apache.commons.lang3.StringUtils.isNotBlank(value);

        int strLen;
        if (value == null || (strLen = value.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(value.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查对象是否为数字型字符串,包含负数开头的。
     */
    public static boolean isNumeric(Object obj) {
        if (obj == null) {
            return false;
        }
        char[] chars = obj.toString().toCharArray();
        int length = chars.length;
        if (length < 1) {
            return false;
        }

        int i = 0;
        if (length > 1 && chars[0] == '-') {
            i = 1;
        }

        for (; i < length; i++) {
            if (!Character.isDigit(chars[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查指定的字符串列表是否不为空。
     */
    public static boolean areNotEmpty(String... values) {
        boolean result = true;
        if (values == null || values.length == 0) {
            result = false;
        } else {
            for (String value : values) {
                result &= !isEmpty(value);
            }
        }
        return result;
    }

    /**
     * 把通用字符编码的字符串转化为汉字编码。
     */
    public static String unicodeToChinese(String unicode) {
        StringBuilder out = new StringBuilder();
        if (!isEmpty(unicode)) {
            for (int i = 0; i < unicode.length(); i++) {
                out.append(unicode.charAt(i));
            }
        }
        return out.toString();
    }

    /**
     * 过滤不可见字符
     */
    public static String stripNonValidXMLCharacters(String input) {
        if (input == null || ("".equals(input)))
            return "";
        StringBuilder out = new StringBuilder();
        char current;
        for (int i = 0; i < input.length(); i++) {
            current = input.charAt(i);
            if ((current == 0x9) || (current == 0xA) || (current == 0xD) || ((current >= 0x20) && (current <= 0xD7FF)) || ((current >= 0xE000) && (current <= 0xFFFD)) || ((current >= 0x10000) && (current <= 0x10FFFF)))
                out.append(current);
        }
        return out.toString();
    }

    /**
     * 将时间转换成指定格式的字符串
     * 
     * @param date
     * @param fmt
     * @return
     */
    public static String dateFmtStr(Date date, String fmt) {
        if (null != date && !StringUtils.isEmpty(fmt)) {
            SimpleDateFormat sdf = new SimpleDateFormat(fmt); // 设置时间格式
            String dateStr = sdf.format(date); // 格式化时间
            return dateStr;
        } else {
            return null;
        }
    }

    /**
     * 
     * @param img 图片字符串
     * @param fileName 图片名称
     * @param folder 保存图片的文件夹
     * @param servletContext servlet上下文
     * @return imgUrl 返回图片访问地址
     */
    public static String saveImgToServer(String img, String fileName, String folder, ServletContext servletContext, String httpUrl) {
        // 将字符串转换成二进制，用于显示图片
        byte[] imgByte = Base64.decode(img);
        String imgUrl = null;
        

        String realPath = servletContext.getRealPath(folder + fileName + BankParamConsts.IMG_SUFFIX);// 获取项目文件夹路径
        // String realPathBack = BankParamConsts.APP_IMG_SAVE_FOLDER_BACK + fileName +
        // BankParamConsts.IMG_SUFFIX;// 获取服务器路径
        String realPathBack = "D:\\home\\imgback\\" + fileName + BankParamConsts.IMG_SUFFIX;// 获取服务器路径

        // log.info("linux图片保存路径："+realPathBack);
        // log.info("linux图片保存路径："+realPathBack);
        FileImageOutputStream imageOutput;
        try {

            imageOutput = new FileImageOutputStream(new File(realPathBack));
            // 打开输入流
            imageOutput.write(imgByte, 0, imgByte.length);// 将byte写入硬盘
            imageOutput.close();

            imageOutput = new FileImageOutputStream(new File(realPath));
            // 打开输入流
            imageOutput.write(imgByte, 0, imgByte.length);// 将byte写入硬盘
            imageOutput.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("头像保存成功，路径： " + realPath);
        imgUrl = httpUrl + fileName + BankParamConsts.IMG_SUFFIX;
        return imgUrl;
    }
    
    /**
     * 
     * @Description:  日期字符串比较  返回间隔日期
     * @author: xuhaoming
     * @date: 2017年8月28日
     */
    public static int daysBetween(String smdate, String bdate) throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(sdf.parse(bdate));    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*3600*24);  
       return Integer.parseInt(String.valueOf(between_days))+1;
    }  
    
    
    //判断是否超过24小时  
    public static boolean jisuan(String date1, String date2) throws Exception {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date start = sdf.parse(date1);
        java.util.Date end = sdf.parse(date2);
        long cha = end.getTime() - start.getTime(); 
        double result = cha * 1.0 / (1000 * 60 * 60); 
        if(result<=24){ 
             //System.out.println("可用");   
             return true; 
        }else{ 
             //System.out.println("已过期");  
             return false; 
        } 
    } 
    /** 
     *  获取两个日期相差的月数 
     * @param d1    较大的日期 
     * @param d2    较小的日期 
     * @return  如果d1>d2返回 月数差 否则返回0 
     */  
    public static int getMonthSpace(Date date1, Date date2)
            throws ParseException {
    	 Calendar c1 = Calendar.getInstance();
         Calendar c2 = Calendar.getInstance();
         c1.setTime(date1);  
         c2.setTime(date2);  
         if(c1.getTimeInMillis() < c2.getTimeInMillis()) return 0;  
         int year1 = c1.get(Calendar.YEAR);
         int year2 = c2.get(Calendar.YEAR);
         int month1 = c1.get(Calendar.MONTH);
         int month2 = c2.get(Calendar.MONTH);
         int day1 = c1.get(Calendar.DAY_OF_MONTH);
         int day2 = c2.get(Calendar.DAY_OF_MONTH);
         // 获取年的差值 假设 d1 = 2015-8-16  d2 = 2011-9-30  
         int yearInterval = year1 - year2;  
         // 如果 d1的 月-日 小于 d2的 月-日 那么 yearInterval-- 这样就得到了相差的年数  
         if(month1 < month2 || month1 == month2 && day1 < day2) yearInterval --;  
         // 获取月数差值  
         int monthInterval =  (month1 + 12) - month2  ;  
         //if(day1 < day2) monthInterval --;  
         monthInterval %= 12;  
         return yearInterval * 12 + monthInterval;  
    }

    /**
     *@Description:  * 转换区域列表为sql中的in查询格式
     *  @param list
     *@Date: 下午2:35 2018/11/5
     *@Return:  * @return : java.lang.String
     **/
    public static String changeListToString(List<String> list){

        if(list == null || list.size() < 1){
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("(");
        for(int i = 0; i < list.size(); i++){
            String each = list.get(i);
            if(i == 0){
                stringBuffer.append("'" + each + "'");
            }else {
                stringBuffer.append(",'" + each + "'");
            }
        }
        stringBuffer.append(")");
        return stringBuffer.toString();
    }
    public static String changeListIntToString(List<Integer> list){

        if(list == null || list.size() < 1){
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("(");
        for(int i = 0; i < list.size(); i++){
            int each = list.get(i);
            if(i == 0){
                stringBuffer.append(each);
            }else {
                stringBuffer.append("," + each);
            }
        }
        stringBuffer.append(")");
        return stringBuffer.toString();
    }
}
