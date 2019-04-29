package cmp.common.util.radom;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 生成唯一编号工具类
 * 
 * @author user
 *
 */
public class OnlyNumberUtil extends Thread {

    private static long orderNum = 0l;

    private static String date;

    // 生成10位场次编号
    public static String getSerialNumber() {
        String str = new SimpleDateFormat("yyMMdd").format(new Date());
        if (date == null || !date.equals(str)) {
            date = str;
            orderNum = 0l;
        }
        orderNum++;
        long serialNumber = Long.parseLong((date));
        serialNumber += orderNum;
        return serialNumber + "" + OnlyNumberUtil.randomSixNum();
    }

    // 生成订单号
    public static String getOrderNo() {
        String str = new SimpleDateFormat("yyMMddHHmmss").format(new Date());
        if (date == null || !date.equals(str)) {
            date = str;
            orderNum = 0l;
        }
        orderNum++;
        long orderNo = Long.parseLong((date)) * 10000;
        orderNo += orderNum;
        return orderNo + "" + OnlyNumberUtil.getFourRandom();
    }

    /**
     * 生成17位项目编号
     * 
     * @return
     */
    public static String getWorkNo() {
        return System.currentTimeMillis() + OnlyNumberUtil.getFourRandom();
    }

    /**
     * 产生随机的四位数
     * 
     * @return
     */
    public static String getFourRandom() {
        Random rad = new Random();
        String strRan = rad.nextInt(10000) + "";
        switch (strRan.length()) {
        case 1:
            strRan = "000" + strRan;
            break;
        case 2:
            strRan = "00" + strRan;
            break;
        case 3:
            strRan = "0" + strRan;
            break;
        }
        return strRan;
    }

    /*
     * public static String createUUID(int len) {
     * 
     * String uuid = java.util.UUID.randomUUID().toString() .replaceAll("-", ""); return
     * uuid.substring(0, len); }
     */
    /*
     * public static void main(String[] args) { for(int i=0;i<50;i++){ new Thread(new Runnable(){
     * public void run(){ //call method 1 while(true){ System.out.println(buff.toString()); } }
     * }).start();
     * 
     * } }
     */
    public static String addZero(int size, Integer id) {
        String idStr = id.toString();
        for (int i = idStr.length(); i < size; i++) {
            idStr = "0" + idStr;
        }

        return idStr;
    }

    /**
     * @Title: randomFourEng
     * @Description: 随机生成4位英文字母
     * @author wzhiqiang
     * @Date 2016年12月14日
     */
    public static String randomFourEng() {

        char[] A_Z = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

        Random rd = new Random();
        String fourEng = "";

        for (int i = 0; i < 4; i++)// 随机取出4个字母
        {
            fourEng += A_Z[rd.nextInt(26)];
        }

        return fourEng;
    }

    /**
     * @Title: randomSixNum
     * @Description: 随机生成6位数字
     * @author wzhiqiang
     * @Date 2016年12月14日
     */
    public static String randomSixNum() {

        char[] A_Z = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };

        Random rd = new Random();
        String sixNum = "";
        for (int i = 0; i < 6; i++) {
            sixNum += A_Z[rd.nextInt(10)];
        }
        return sixNum;
    }

}
