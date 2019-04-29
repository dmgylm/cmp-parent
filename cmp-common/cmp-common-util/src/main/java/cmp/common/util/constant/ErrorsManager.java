package cmp.common.util.constant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ErrorsManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(ErrorsManager.class);

	private static Properties errors = new Properties();

	static {
		InputStream in = ErrorsManager.class.getClassLoader().getResourceAsStream("ErrorMessage.properties");
		try {
			errors.load(in);
			in.close();
		} catch (IOException e) {
			LOGGER.error(":::读取错误提示文件失败!!! 异常信息为：{}，e:{}", e.getMessage(), e);
		}
	}

	public static String getStrErrorMsg(String key) {
		String desc = null;
		try {
			desc = errors.getProperty(key);
		} catch (Exception e) {
			desc = "";
			LOGGER.error("读取错误信息出现异常! 异常信息为：{}，e:{}", e.getMessage(), e);
		}
		return desc;
	}

}