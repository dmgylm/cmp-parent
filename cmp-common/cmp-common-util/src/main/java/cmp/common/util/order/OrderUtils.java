package cmp.common.util.order;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import cmp.common.util.radom.RandomCharUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 订单相关业务工具类
 *
 * @author sd
 * @date 2017/12/25
 */
public class OrderUtils {


    /**
     * 根据机构编号生成订单编号
     * 13位: 英文7位(机构编号前2位 + 随机英文5位)、随机数字6位
     *
     * @param sourceCode 机构编号
     * @return 订单编号
     */
    public static String createOrderNo(String sourceCode) {
        StringBuffer orderNo = new StringBuffer();
        if (StringUtils.isNotBlank(sourceCode)) {
            // 机构编号前2位
            orderNo.append(sourceCode.substring(0, 2));
            // 随机英文5位
            orderNo.append(RandomCharUtil.randomString(5));
            // 随机数字6位
            orderNo.append(RandomStringUtils.randomNumeric(6));
        } else {
            // 随机英文7位
            orderNo.append(RandomCharUtil.randomString(7));
            // 随机数字6位
            orderNo.append(RandomStringUtils.randomNumeric(6));
        }
        return orderNo.toString();
    }

    public static void main(String[] args) {

        Pattern pattern = Pattern.compile("^[a-zA-Z]{7}\\d{6}$");
        String str = "EDBHEFC598226";
        Matcher matcher = pattern.matcher(str);
        System.out.println(matcher.matches());


        //String sourceCode = "";
        //String orderNo = createOrderNo(sourceCode);
        //System.out.println(orderNo);
        //String random = RandomCharUtil.randomString(5);
        //System.out.println(random);
    }
}
