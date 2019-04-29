package cmp.common.util.sms;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import cmp.common.util.exception.BaseException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 发送短信配置文件读取工具类
 * 
 * @date 2017年9月4日
 */
public class SMSPropertiesHelp {

	/**
	 * 通过key得到value
	 * 
	 * @param key
	 * @return
	 */
	public static String getValueByKey(String key) {
		String desc = null;
		try {
			// String path = ResourceUtils.getFile("classpath:sms.properties").getAbsolutePath();
			// Resource resource = new ClassPathResource(path);
			Resource resource = new ClassPathResource("/sms.properties");
			Properties props = PropertiesLoaderUtils.loadProperties(resource);
			desc = props.getProperty(key);
		} catch (IOException e) {
			desc = "获取短信配置文件中的值出现异常！异常信息为：" + e.getMessage();
			throw new BaseException("获取短信配置文件中的值出现异常！异常信息为：{}", e.getMessage());
		}
		return desc;
	}

	public static void main(String[] args) {
		try {
//			String path = ResourceUtils.getFile("classpath:sms.properties").getAbsolutePath();
			Resource resource = new ClassPathResource("/sms.properties");
			Properties properties = new Properties();
			InputStream inputStream = resource.getInputStream();
			String filename = resource.getFilename();
			properties.load(inputStream);
			System.out.println(filename);
			// Properties props =
			// PropertiesLoaderUtils.loadProperties(resource);
			String key = "sign";
			String value = properties.getProperty(key);
			System.out.println(value);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}