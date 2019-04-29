package cmp.common.util.fastdfs;

import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageClient1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cmp.common.util.string.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sd on 2017/9/7.
 */
public class FastDFSUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FastDFSUtil.class);

    static Map<String, FastDFSClient> map = new HashMap<String, FastDFSClient>();

    /**
     * 获取连接默认组的客户端
     *
     * @return
     */
    public static FastDFSClient getDefaultGroupClient() {
        return getClientByGroupName(FastDFSConfig.DEFAULT_GROUP);
    }

    /**
     * 根据组名获取上传客户端
     *
     * @param groupName 组名
     * @return
     */
    public static FastDFSClient getClientByGroupName(String groupName) {
        if (map.get(groupName) == null || map.get(groupName).getStorageClient1() == null) {
            FastDFSClient client = new FastDFSClient(groupName);
            if (null == client) {
                LOGGER.error("第一次获取到的client为空！！！！！！！！！！");
            }
            StorageClient1 storageClient1 = client.getStorageClient1();
            StorageClient storageClient = client.getStorageClient();
            // 如果第一次获取不到文件服务器连接，则再次尝试获取
            if (null == storageClient1 || null == storageClient) {
                LOGGER.error("开始第二次获取client连接！！！！！！！！！！");
                client = new FastDFSClient(groupName);
            }
            map.put(groupName, client);
        }
        return map.get(groupName);
    }

    /**
     * 根据组名获取该组的storage端口号
     *
     * @param groupName 组名
     * @return 该组的storage端口号
     */
    public static Integer getPortByGroup(String groupName) {
        Integer port = null;
        if (StringUtils.isNotBlank(groupName)) {
            if (FastDFSConfig.NAME_GROUP1.equals(groupName)) {
                port = FastDFSConfig.PORT_GROUP1;
            } else if (FastDFSConfig.NAME_GROUP3.equals(groupName)) {
                port = FastDFSConfig.PORT_GROUP3;
            } else {
                port = FastDFSConfig.PORT_GROUP1;
            }
        } else {
            port = FastDFSConfig.PORT_GROUP1;
        }
        return port;
    }

}
