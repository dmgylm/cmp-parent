package cmp.common.util.excel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cmp.common.util.string.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.text.MessageFormat;
import java.util.Calendar;

/**
 * 导出文件文件的工具类
 */
public class ExportTextUtil {
        /**
         * 声明日志记录器
         */
        private static final Logger LOGGER = LoggerFactory.getLogger(ExportTextUtil.class);

        /**
         * 导出文本文件
         * @param response
         * @param jsonString
         * @param fileName
         */
        public static void writeToTxt(HttpServletResponse response, String jsonString, String fileName) {//设置响应的字符集
            response.setCharacterEncoding("utf-8");
            //设置响应内容的类型
            response.setContentType("text/plain");
            //设置文件的名称和格式
            response.addHeader(
                    "Content-Disposition",
                    "attachment; filename="
                            + genAttachmentFileName(fileName+ "_", "JSON_FOR_UCC_")
                            + MessageFormat.format("{0,date,yyyy-MM-dd HH:mm:ss}", new Object[]{Calendar.getInstance().getTime()})
                            + ".txt");//通过后缀可以下载不同的文件格式
            BufferedOutputStream buff = null;
            ServletOutputStream outStr = null;
            try {
                outStr = response.getOutputStream();
                buff = new BufferedOutputStream(outStr);
                buff.write(delNull(jsonString).getBytes("UTF-8"));
                buff.flush();
                buff.close();
            } catch (Exception e) {
                LOGGER.error("导出文件文件出错，e:{}",e);
            } finally {try {
                    buff.close();
                    outStr.close();
                } catch (Exception e) {
                    LOGGER.error("关闭流对象出错 e:{}",e);
                }
            }
        }

        /**
         * 如果字符串对象为 null，则返回空字符串，否则返回去掉字符串前后空格的字符串
         * @param str
         * @return
         */
        public static String delNull(String str) {
                String returnStr="";
                if (StringUtils.isNotBlank(str)) {
                    returnStr=str.trim();
                }
                return returnStr;
        }
        
        public static String genAttachmentFileName(String cnName, String defaultName) {
            try {
//                fileName = URLEncoder.encode(fileName, "UTF-8");
                cnName = new String(cnName.getBytes("gb2312"), "ISO8859-1");
                /*
                if (fileName.length() > 150) {
                    fileName = new String( fileName.getBytes("gb2312"), "ISO8859-1" );
                }
                */
            } catch (Exception e) {
                cnName = defaultName;
            }
            return cnName;
        }
}
