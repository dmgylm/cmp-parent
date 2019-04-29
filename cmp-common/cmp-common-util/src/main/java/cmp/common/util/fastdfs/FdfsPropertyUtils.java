package cmp.common.util.fastdfs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * properties utils
 *
 * @author sd
 * @date 2018/04/13
 */
public class FdfsPropertyUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FdfsPropertyUtils.class);

    private static Properties properties;

    static {
        // initialize the properties
        loadProperties();
    }


    /**
     * initialize the properties
     */
    private static synchronized void loadProperties() {
        try {
            LOGGER.info("开始加载FastDFS配置文件！");
            ClassPathResource resource = new ClassPathResource("fdfs.properties");
            properties = PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("加载FastDFS配置文件异常！异常信息为:{}！e:{}！", e.getMessage(), e);
        }
        LOGGER.info("加载FastDFS配置文件完成！");
    }

    /**
     * Searches for the property with the specified key in this property list.
     * The method returns null if the property is not found.
     *
     * @param key the property key.
     * @return the value in this property list with the specified key value.
     */
    public static String getProperty(String key) {
        if (null == properties) {
            loadProperties();
        }
        return properties.getProperty(key);
    }

    /**
     * Searches for the property with the specified key in this property list.
     * The method returns default value argument if the property is not found.
     *
     * @param key          the hashtable key.
     * @param defaultValue a default value.
     * @return the value in this property list with the specified key value.
     */
    public static String getProperty(String key, String defaultValue) {
        if (null == properties) {
            loadProperties();
        }
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Searches for the Integer property with the specified key in this property list.
     * The method returns null if the property is not found.
     *
     * @param key the property key.
     * @return the value in this property list with the specified key value.
     */
    public static Integer getIntegerProperty(String key) {
        if (null == properties) {
            loadProperties();
        }
        String value = properties.getProperty(key);
        if (value != null) {
            return Integer.parseInt(value);
        }
        return null;
    }
}
