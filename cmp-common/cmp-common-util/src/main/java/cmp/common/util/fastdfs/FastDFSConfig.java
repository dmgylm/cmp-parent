package cmp.common.util.fastdfs;

import java.io.Serializable;

/**
 * FastDFS相关常量配置类
 *
 * @author sd
 * @date 2017年9月11日
 */
public interface FastDFSConfig extends Serializable {

    public static final String FILE_DEFAULT_AUTHOR = "sd";

    public static final String PROTOCOL = "http://";

    public static final String SEPARATOR = "/";

    public static final String COLON = ":";

    public static final String TRACKER_NGINX_ADDR = FdfsPropertyUtils.getProperty("tracker_nginx_address");

    public static final String TRACKER_NGINX_PORT = FdfsPropertyUtils.getProperty("tracker_nginx_port");

    /**
     * 默认组：group1
     */
    public static final String DEFAULT_GROUP = FdfsPropertyUtils.getProperty("name_group1");

    /**
     * 组1名称
     */
    public static final String NAME_GROUP1 = FdfsPropertyUtils.getProperty("name_group1");

    /**
     * 组3名称
     */
    public static final String NAME_GROUP3 = FdfsPropertyUtils.getProperty("name_group3");

    /**
     * 组1端口号：23000
     */
    public static final Integer PORT_GROUP1 = FdfsPropertyUtils.getIntegerProperty("port_group1");

    /**
     * 组3端口号：23003
     */
    public static final Integer PORT_GROUP3 = FdfsPropertyUtils.getIntegerProperty("port_group3");
}