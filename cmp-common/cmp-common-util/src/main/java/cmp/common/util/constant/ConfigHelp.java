package cmp.common.util.constant;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName ConfigHelp
 * @Description 配置文件读取工具
 * @author bati
 * @date 2014-1-6
 */
public class ConfigHelp {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigHelp.class);

	private static final String CONFIG_PATH = "configurations.xml";

	private static Configuration config;

	static {
		try {
			config = new XMLConfiguration(CONFIG_PATH);
		} catch (ConfigurationException e) {
			LOGGER.error("ConfigHelp工具类初始化出现异常！异常信息为：{}，e：{}", e.getMessage(), e);
		}
	}

	public static String getString(String infoLabel) {
		return config.getString(infoLabel);
	}

	public static String[] getStringArray(String infoLabel) {
		return config.getStringArray(infoLabel);
	}

	public static boolean getBoolean(String infoLabel) {
		return config.getBoolean(infoLabel);
	}

	public static int getInt(String infoLabel) {
		return config.getInt(infoLabel);
	}

	public static double getDouble(String infoLabel) {
		return config.getDouble(infoLabel);
	}
}
