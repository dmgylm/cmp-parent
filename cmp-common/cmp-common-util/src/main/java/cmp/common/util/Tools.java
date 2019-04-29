package cmp.common.util;

import cmp.common.util.encry.MD5;
import cmp.common.util.radom.RandomCharUtil;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 开发中常用的公用方法
 */
public class Tools {

    private static final Logger LOGGER = LoggerFactory.getLogger(Tools.class);

    private static final String MOBILE_REGEX = "1[3-9][0-9]{9}";

    public static <T extends Object> void increment(T key, int increment, Map<T, Integer> map) {
        Integer orignal = map.get(key);
        if (orignal == null) {
            orignal = 0;
        }
        orignal += increment;
        map.put(key, orignal);
    }

    public static double doubleFormat(double d, int digit) {
        BigDecimal b = new BigDecimal(d);
        double value = b.setScale(digit, BigDecimal.ROUND_HALF_UP).doubleValue();
        return value;
    }

    public static int parseInt(String str, int defaultValue) {
        if (Tools.isBlank(str)) {
            return defaultValue;
        }
        int result = defaultValue;
        try {
            result = Integer.parseInt(str);
        } catch (Exception e) {
            result = defaultValue;
        }
        return result;
    }

    public static long parseLong(String str, long defaultValue) {
        if (Tools.isBlank(str)) {
            return defaultValue;
        }
        long result = defaultValue;
        try {
            result = Long.parseLong(str);
        } catch (Exception e) {
            result = defaultValue;
        }
        return result;
    }

    /**
     * 去除字符串首尾的空白字符
     *
     * @param str
     * @return
     * @author Gao Baowen
     * @since 2009-6-1 下午02:53:39
     */
    public static String trim(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        return str.trim();
    }

    /**
     * 去除文字中的空白字符
     *
     * @param str
     * @return
     */
    public static String removeSpace(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        return str.trim().replaceAll("\\s", "");
    }

    /**
     * 将 byte[] 写入文件
     *
     * @param bys
     * @param file
     * @throws IOException
     */
    public static void writeBytes2File(byte[] bys, File path, String filename) throws IOException {
        if (Tools.isBlank(bys) || path == null || isBlank(filename)) {
            return;
        }
        BufferedOutputStream bos = null;
        try {
            File file = new File(path, filename);
            bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write(bys);
        } finally {
            bos.close();
        }
    }

    /**
     * 获得文件的后缀名
     *
     * @param filename
     * @return
     */
    public static String getLowerCaseFileSuffixName(String filename) {
        int idx = filename.lastIndexOf(".");
        if (idx < 0) {
            return "";
        }
        return filename.substring(idx).toLowerCase();
    }

    /**
     * 以 yyyy-MM-dd HH:mm:ss 的格式进行日期的格式化
     */
    public static String defaultFormat(Date date) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static String shortFormat(Date date) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    /**
     * yyyy-MM-dd HH:mm:ss格式的时间
     *
     * @param date
     * @return
     */
    public static Date getCurrentFormatDate() {
        String dateStr = getFormatDateString(new Date(), "yyyy-MM-dd HH:mm:ss");
        return parseDate(dateStr);
    }

    /**
     * YYYYMMDD格式
     *
     * @param date
     * @return
     */
    public static String shortFormatDate(Date date) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat("yyyyMMdd").format(date);
    }

    /**
     * YYYYMMDDhhmmss格式
     *
     * @param date
     * @return
     */
    public static String LongFormatDate(Date date) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat("yyyyMMddhhmmss").format(date);
    }

    /**
     * hhmmss格式
     *
     * @param date
     * @return
     */
    public static String shortFormatTime(Date date) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat("HHmmss").format(date);
    }

    /**
     * 判断集合是否为空或者是空白字符
     */
    public static boolean isBlank(Collection<?> c) {
        if (c == null || c.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断集合是否为空或者是空白字符
     */
    public static boolean isBlank(String[] str) {
        if (str == null || str.length == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断数字是否为空
     */
    public static boolean isBlank(Number num) {
        return num == null;
    }

    /**
     * 检查 byte[] 是否为空
     */
    public static boolean isBlank(byte[] bys) {
        return (bys == null || bys.length == 0);
    }

    /**
     * 检查参数中是否有不为空的值，只要有一个不为空时就返回 true
     */
    public static boolean hasAnyNotBlank(Object... args) {
        for (int i = 0; i < args.length; i++) {
            if (!isBlank(args[i])) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBlank(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String) {
            return ((String) obj).trim().length() == 0;
        }
        if (obj instanceof Integer) {
            return ((Integer) obj) == null;
        }
        if (obj instanceof Collection<?>) {
            return ((Collection<?>) obj).size() == 0;
        }
        if (obj instanceof byte[]) {
            return ((byte[]) obj).length == 0;
        }
        if (obj instanceof String[]) {
            return ((String[]) obj).length == 0;
        }
        throw new IllegalArgumentException("undefined type: " + obj.getClass().getName());
    }

    /**
     * 将 Map 的键值互转
     */
    public static <K, V> Map<V, K> convertKey2Value(Map<K, V> map) {
        if (map == null) {
            return null;
        }
        Map<V, K> result = new LinkedHashMap<V, K>();
        for (Iterator<K> i = map.keySet().iterator(); i.hasNext(); ) {
            K key = i.next();
            result.put(map.get(key), key);
        }
        return result;
    }

    /**
     * 获得传入日期的前一天，截断至日（时、分、秒、毫秒全部清零）
     */
    public static Date getPreDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.add(Calendar.DATE, -1);
        return c.getTime();
    }

    /**
     * 检查字符长度
     */
    public static boolean checkLength(String str, int maxChar, String infoPrefix) {
        if (str == null || str.length() == 0) {
            return true;
        }
        int len = getStringCharLength(str);
        if (len > maxChar) {
            return false;
        }
        return true;
    }

    /**
     * 检查字符串字节长度，汉字算为 2 个长度，字母数字算为 1 个长度
     */
    public static boolean checkByteLength(String str, int maxByte, String infoPrefix) {
        if (str == null || str.length() == 0) {
            return true;
        }
        int len = getStringByteLength(str);
        if (len > maxByte) {
            return false;
        }
        return true;
    }

    public static boolean isNotEmpty(String str) {
        if (str == null || str.trim().length() == 0) {
            return false;
        }
        return true;
    }

    public static boolean isBlank(String str) {
        if (str == null || str.trim().length() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 获得 yyyyMM 格式下一个月值
     */
    public static int nextMonth(int ym) {
        int m = ym % 100;
        return (ym / 100 + m / 12) * 100 + (m % 12) + 1;
    }

    public static int decrementOneMonth(int ym) {
        int m = ym % 100 - 1;
        int y = ym / 100;
        if (m == 0) {
            m = 12;
            y--;
        }
        return y * 100 + m;
    }

    /**
     * 将 Map 的键值互转
     */
    public static Map<Integer, String> convertValue2Key(Map<String, Integer> map) {
        Map<Integer, String> result = new LinkedHashMap<Integer, String>();
        if (map == null || map.size() == 0) {
            return result;
        }
        for (Iterator<String> i = map.keySet().iterator(); i.hasNext(); ) {
            String key = i.next();
            result.put(map.get(key), key);
        }
        return result;
    }

    /**
     * 计算字节某个字符串的长度，字母等 ASCII 字符计为一个长度，汉字等全角字符计为两个长度。
     */
    public static int getStringByteLength(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        int count = 0;
        char[] chs = str.toCharArray();
        for (int i = 0; i < chs.length; i++) {
            count += (chs[i] > 0xff) ? 2 : 1;
        }
        return count;
    }

    /**
     * 根据字符计算某个字符串的长度。
     */
    public static int getStringCharLength(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        return str.length();
    }

    /**
     * 判断字符串是否为空，为空条件 null or "";
     */
    public static boolean isEmpty(String string) {

        if (string == null || string.trim().equals(""))
            return true;
        else
            return false;

    }

    public static Date parseShortDate(String str) {
        if (isBlank(str)) {
            return null;
        }
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 根据指定的日期和时间格式取得相应的字符串 <br>
     * 出错返回null
     */
    public static String getFormatDateString(Date date, String pattern) {
        String formatDateString = null;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            formatDateString = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sdf = null;
        }
        return formatDateString;
    }

    /**
     * 根据指定的日期字符串和日期格式取得Date实例 <br>
     * 出错返回 null
     *
     * @param dateString
     * @param pattern
     * @return
     */
    public static Date getDateFromString(String dateString, String pattern) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            date = sdf.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sdf = null;
        }
        return date;
    }

    public static Date parseDate(String date) {
        if (date == null || date.length() == 0) {
            return null;
        }
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date.trim());
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean containsDate(Date current, Date start, Date end) {
        long currentMillis = current.getTime();
        return (currentMillis >= start.getTime()) && (currentMillis < end.getTime());
    }

    /**
     * 得到这个月的第一天，日以下字段清零
     */
    public static Date getMonthFirstDate() {
        Calendar c = getCurrentCalendar();
        c.set(Calendar.DATE, 1);
        return c.getTime();
    }

    /**
     * 得到当天，日以下字段清零
     */
    public static Date getCurrentDate() {
        return getCurrentCalendar().getTime();
    }

    /**
     * 得到当天的后一天，日以下字段清零
     */
    public static Date getNextDate() {
        Calendar c = getCurrentCalendar();
        c.add(Calendar.DATE, 1);
        return c.getTime();
    }

    /**
     * 得到当天的后N天，日以下字段清零
     */
    public static Date getDateForDayNum(int dayNum) {
        Calendar c = getCurrentCalendar();
        c.add(Calendar.DATE, dayNum);
        return c.getTime();
    }

    /**
     * 得到下个月的第一天，日以下字段清零
     */
    public static Date getNextMonthFirstDate() {
        Calendar c = getCurrentCalendar();
        c.set(Calendar.DATE, 1);
        c.add(Calendar.MONTH, 1);
        return c.getTime();
    }

    public static Date getCurrentMonthLastSecond() {
        Date date = getNextMonthFirstDate();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.SECOND, -1);
        return c.getTime();
    }

    public static Calendar getCurrentCalendar() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c;
    }

    public static Date getBeforeCalendar(Date date, int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, day * (-1));
        return c.getTime();
    }

    public static Date incrementMinute(Date date, int minute) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MINUTE, minute);
        return c.getTime();
    }

    public static Date incrementHour(Date date, int hour) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR_OF_DAY, hour);
        return c.getTime();
    }

    /**
     * set转换字符换
     *
     * @param set
     * @return
     */
    public static String set2String(Set<String> set) {
        if (Tools.isBlank(set)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int k = 0;
        for (Iterator<String> i = set.iterator(); i.hasNext(); ) {
            if (k++ > 0) {
                sb.append(",");
            }
            sb.append("'").append(i.next()).append("'");
        }
        return sb.toString();
    }

    /**
     * set转换字符换
     *
     * @param set
     * @return
     */
    public static String set2Long(Set<Long> set) {
        if (Tools.isBlank(set)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int k = 0;
        for (Iterator<Long> i = set.iterator(); i.hasNext(); ) {
            if (k++ > 0) {
                sb.append(",");
            }
            sb.append("'").append(i.next()).append("'");
        }
        return sb.toString();
    }

    /**
     * 用于导出文件时文件名编码
     */
    public static String encodeFilename(String filename) {
        if (Tools.isBlank(filename)) {
            return "undefined";
        }
        String str = null;
        try {
            str = new String(filename.getBytes("GB2312"), "ISO8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            str = "undefined";
            int index = filename.lastIndexOf(".");
            if (index > 0) {
                str += filename.substring(index);
            }
        }
        return str;
    }

    /**
     * 截取字符串，并在截取的字符串后添加指定后缀，如果字符串长度小于指定长度时不添加后缀 原样返回。
     */
    public static String truncate(String str, int length, String suffix) {
        if ((str == null) || (str.length() == 0) || (length < 1)) {
            return str;
        }
        char[] chs = str.toCharArray();
        int len = 0;
        int offset = 0;
        for (int i = 0; i < chs.length; i++, offset++) {
            len += (chs[i] > 0xff) ? 2 : 1;
            if (len > length) {
                break;
            }
        }
        if (offset == chs.length) {
            return str;
        }
        if (suffix == null || suffix.trim().length() == 0) {
            return new String(chs, 0, offset);
        }
        return new String(chs, 0, offset) + suffix.trim();
    }

    /**
     * 系统跟踪号生成 系统跟踪号当日不允许重复，全为数字(前四位已经配置在SysParm.properties的这段MIDID)
     * 此处生成后十位(格式：MMddHHmmss月日时分秒)
     *
     * @return
     */
    public static String genMid() {
        String date = new SimpleDateFormat("MMddHHmmss").format(new Date());
        return date;
    }

    public static String genSeq() {
        String date = new SimpleDateFormat("mmss").format(new Date());
        return date;
    }

    /**
     * 生成订单号
     *
     * @return
     */
    public static String genOrderID() {
        String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return date;
    }

    /**
     * 字符串按照指定长度补全空格
     *
     * @param inStr
     * @param length
     * @return
     */
    public static String fillBlank(String inStr, int length) {
        inStr = trim(inStr);
        if (isBlank(inStr)) {
            inStr = "";
        }
        int len = inStr.length();
        if (len < length) {
            for (int i = 0; i < (length - len); i++) {
                inStr = inStr + " ";
            }
        }
        return inStr;
    }

    /**
     * 字符串按字节指定长度补全空格
     *
     * @param inStr
     * @param length
     * @return
     */
    public static String fillBlankBytes(String inStr, int length) {
        inStr = trim(inStr);
        if (inStr == null || inStr.length() == 0) {
            return inStr;
        } else {
            byte[] strByte = inStr.getBytes();
            int strLen = strByte.length;

            if (strLen < length) {
                for (int i = 0; i < (length - strLen); i++) {
                    inStr = inStr + " ";
                }
            }
        }
        return inStr;
    }

    /**
     * 字符串按照指定长度左边补全0(金额类型的字符串)
     *
     * @param inStr
     * @param length
     * @return
     */
    public static String fillZero(String inStr, int length) {
        inStr = trim(inStr);
        if (isBlank(inStr)) {
            inStr = "0";
        }
        int len = inStr.length();
        if (len < length) {
            for (int i = 0; i < (length - len); i++) {
                inStr = "0" + inStr;
            }
        }
        return inStr;
    }

    /**
     * 字符串按照指定长度右边补全字符串
     *
     * @param inStr
     * @param length
     * @return
     */
    public static String fillString(String inStr, int length, String flag) {
        inStr = trim(inStr);
        if (isBlank(inStr)) {
            inStr = "0";
        }
        int len = inStr.length();
        if (len < length) {
            for (int i = 0; i < (length - len); i++) {
                inStr = inStr + "0";
            }
        }
        return inStr;
    }

    /**
     * 计算传入日期的前N天
     *
     * @param date
     * @author bati
     */
    public static Date getPreDay(Date date, int deltaDay) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.add(Calendar.DATE, deltaDay);
        return c.getTime();
    }

    /**
     * 获得传入日期的前几分钟
     */
    public static Date getPreMinutes(Date date, int minutes) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MINUTE, minutes * (-1));
        return c.getTime();
    }

    /**
     * 组合算法 从n个不同的数中取m个数字的排列公式 n!/(n-m)!
     */
    public static int zHNumber(int n, int m) {
        int p = 1;
        if (m <= 0 || n <= 0 || n < m) {
            return 0;
        }
        for (int i = 0; i < m; i++) {
            p = p * (n - i) / (i + 1);
        }
        return p;
    }

    public static Timestamp getTimestamp(Date date) {
        Timestamp timestamp = new Timestamp(date.getTime());
        return timestamp;
    }

    /**
     * 时间戳
     *
     * @return
     */
    public static String getTimestamptoString(Date date) {
        String d = new SimpleDateFormat("yyyyMMddHHmmss").format(date);
        return d;
    }

    /**
     * 时间戳
     *
     * @return
     */
    public static String getDatetoString(Date date) {
        String d = new SimpleDateFormat("yyyyMMdd").format(date);
        return d;
    }

    /**
     * 心
     *
     * @param date
     * @return
     */
    public static String getTimestamptoStr(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = format.format(date);
        return str;
    }

    /**
     * 时间戳
     *
     * @return
     */
    public static String getTimestamp() {
        String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return date;
    }

    public static String getTimeFormat(Date date1) {
        String date = new SimpleDateFormat("yyyy年MM月dd日").format(date1);
        return date;
    }

    public static String getFormatTimestamp(Timestamp ts, String pattern) {
        String str = "";
        try {
            DateFormat sdf = new SimpleDateFormat(pattern);
            str = sdf.format(ts);
        } catch (Exception e) {
        }
        return str;
    }

    /**
     * 与谢峰MD5保持一致
     */
    public static boolean checkSign(String timestamp, String digest) {
        String sign = MD5.encryptStringWithMD5(timestamp);
        if (Tools.isBlank(timestamp) || Tools.isBlank(digest)) {
            return false;
        }
        if (!digest.equals(sign)) {
            return false;
        }
        return true;
    }

    public static int getFormatNumber(double number) {
        BigDecimal t = new BigDecimal(String.valueOf(number)).setScale(0, BigDecimal.ROUND_HALF_UP);
        return t.intValue();
    }

    public static int getRandomNumber(int min, int max) {
        if (min >= max) {
            return min;
        }
        int num = (int) (Math.random() * max) + 1;
        return num;
    }

    // 校验手机号
    public static boolean isMobile(String mobile) {
        if (Tools.isBlank(mobile)) {
            return true;
        }
        if (mobile.length() != 11) {
            return false;
        }
        Pattern pattern = Pattern.compile(MOBILE_REGEX);
        return pattern.matcher(mobile).matches();
    }

    // 获取当天一个月后的日期
    public static String nextMonthDay(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, 1); // 将当前日期加一个月
        return sdf.format(c.getTime());
    }

    public static Date getMonthLastDate() {
        Calendar c = getCurrentCalendar();
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    public static String getRandomString(int length) { // length表示生成字符串的长度
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 获取半年后的日期
     *
     * @return
     */
    public static Date getSemester() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, 7);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        return cal.getTime();
    }

    public static String getPingYin(String src) {
        char[] t1 = null;
        t1 = src.toCharArray();
        String[] t2 = new String[t1.length];
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();

        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
        String t4 = "";
        int t0 = t1.length;
        try {
            for (int i = 0; i < t0; i++) {
                // 判断是否为汉字字符
                if (java.lang.Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
                    t4 += t2[0];
                } else
                    t4 += java.lang.Character.toString(t1[i]);
            }
            return t4;
        } catch (BadHanyuPinyinOutputFormatCombination e1) {
            e1.printStackTrace();
        }
        return t4;
    }

    // 返回中文的首字母
    public static String getPinYinHeadChar(String str) {

        String convert = "";
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            } else {
                convert += word;
            }
        }
        return convert;
    }

    public static List<String> getIntersection(List<String> list1, List<String> list2) {
        List<String> result = new ArrayList<String>();
        for (String integer : list2) {// 遍历list1
            if (list1.contains(integer)) {// 如果存在这个数
                result.add(integer);// 放进一个list里面，这个list就是交集
            }
        }
        return result;
    }

    public final static String md5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes("utf-8");
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String queryRandomString(int length) { // length表示生成字符串的长度
        String base = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 截取时间戳前3位+ 生成6位字符串+ 截取时间戳后10位
     */
    public static String queryRandomString() {
        // 获取时间戳
        String timeStamp = String.valueOf(System.currentTimeMillis());
        // 截取时间戳前3位
        String begin = timeStamp.substring(0, 3);
        // 截取时间戳后10位
        String end = timeStamp.substring(3, timeStamp.length());
        String sum = queryRandomString(6);
        // 将区域编码和时间戳拼接为shopCode
        String shopCode = begin.concat(sum).concat(end);
        System.out.println(shopCode);
        return shopCode;
    }

    /**
     * 将字符串格式yyyy-MM-dd的字符串转为日期，格式"yyyyMMdd"
     *
     * @param date 日期字符串
     * @return 返回格式化的日期
     * @throws ParseException 分析时意外地出现了错误异常
     */
    public static String strToDateFormat(String time) {
        SimpleDateFormat formatter = new SimpleDateFormat();
        Date newDate = new Date();
        try {
            formatter = new SimpleDateFormat("yyyy-MM-dd");
            formatter.setLenient(false);
            newDate = formatter.parse(time);
            formatter = new SimpleDateFormat("yyyyMMdd");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatter.format(newDate);
    }

    /**
     * 序列化对象
     *
     * @param object
     * @return
     */
    public static byte[] serialize(Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            // 序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
            LOGGER.error("序列化对象出现异常！异常信息为：{},e:{}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * 反序列化对象
     *
     * @param bytes
     * @return
     */
    public static Object unserialize(byte[] bytes) {
        ByteArrayInputStream bais = null;
        try {
            // 反序列化
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            LOGGER.error("反序列化对象出现异常！异常信息为：{},e:{}", e.getMessage(), e);
        }
        return null;
    }

    public static Date getWeekFirstDate() {
        //不是从周末算第一天，改成从周一算第一天
        Calendar c = getCurrentCalendar();
        c.set(Calendar.DAY_OF_WEEK, 2);
        return c.getTime();
    }

    public static Date getNextWeekFirstDate() {
        //不是从周末算第一天，改成从周一算第一天
        Calendar c = getCurrentCalendar();
        c.set(Calendar.DAY_OF_WEEK, 2);
        c.add(Calendar.DAY_OF_WEEK, 7);
        return c.getTime();
    }

    /**
     * 获取今天剩余秒数
     */
    public static Long getDiffSecond() {
        //不是从周末算第一天，改成从周一算第一天
        Date date = getNextDate();
        return (date.getTime() - (new Date()).getTime()) / 1000;
    }

    /**
     * 获取请求方的ip地址信息
     *
     * @param request http请求对象
     * @return 请求方的ip地址
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 创建H5项目订单编号
     * 编号规则: 'SSDL' + 'yyyyMMddHHmmssSSS' + '5位随机字母'
     *
     * @return 订单编号
     */
    public static String createH5OrderNo() {
        StringBuffer orderNo = new StringBuffer();
        orderNo.append("SSDL");
        orderNo.append(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
        orderNo.append(RandomCharUtil.randomString(5));
        return orderNo.toString();
    }

    /**
     * 订单赠送时生成订单编号
     * 编号规则: 'B4' + '随机8位英文字母大写' + '12位数字(MMDDhhmissms)'
     *
     * @return 订单编号
     */
    public static String createGiftOrderNo() {
        StringBuffer orderNo = new StringBuffer();
        orderNo.append("B4");
        orderNo.append(RandomCharUtil.randomString(8));
        String randTime = new SimpleDateFormat("MMddHHmmssSS").format(new Date());
        orderNo.append(randTime.substring(0, randTime.length() - 1));
        return orderNo.toString();
    }
    /**
     * 判断某个时间是否是在当前时间的30天之内
     * @param addtime
     * @param now
     * @return
     */
	public static boolean isLatestMonth(Date addtime, Date now){
		Calendar calendar = Calendar.getInstance();  //得到日历
		calendar.setTime(now);//把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, -30);  //设置为30天前
		Date before30days = calendar.getTime();   //得到30天前的时间
		if(before30days.getTime() < addtime.getTime()){
			return true;
		}else{
			return false;
		}
	}

}
