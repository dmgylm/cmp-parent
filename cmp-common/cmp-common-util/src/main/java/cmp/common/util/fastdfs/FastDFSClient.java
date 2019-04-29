package cmp.common.util.fastdfs;

import cmp.common.util.constant.ConfigConsts;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * FastDFS文件上传下载工具类
 *
 * @author sd
 * @date 2017年7月6日
 */
public class FastDFSClient {

    private static final Logger LOG = LoggerFactory.getLogger(FastDFSClient.class);
    private StorageClient1 storageClient1 = null;
    private StorageClient storageClient = null;
    private StorageServer storageServer = null;

    private TrackerClient trackerClient;
    private TrackerServer trackerServer;

    /**
     * 组名
     */
    private String groupName;

    public FastDFSClient() {
        try {
            InputStream inputStream = FastDFSClient.class.getResourceAsStream("fdfs.properties");
            MyClientGlobal.init(inputStream);
            trackerClient = new TrackerClient(MyClientGlobal.g_tracker_group);
            trackerServer = trackerClient.getConnection();
            if (null == trackerServer) {
                LOG.error("trackerServer 为空！");
                throw new IllegalStateException("getConnection return null!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("初始化客户端出现异常！异常信息为：{}，e：{}", e.getMessage(), e);
        }
    }

    /**
     * 指定组名
     *
     * @param groupName
     */
    public FastDFSClient(String groupName) {
        // 初始化FastDFS Client
    	this.groupName = groupName;
        try {
            InputStream inputStream = FastDFSClient.class.getClassLoader().getResourceAsStream("fdfs.properties");
            MyClientGlobal.init(inputStream);
            trackerClient = new TrackerClient(MyClientGlobal.g_tracker_group);
            trackerServer = trackerClient.getConnection();
            if (null == trackerServer) {
                LOG.error("trackerServer 为空！");
                throw new IllegalStateException("getConnection return null");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("初始化客户端出现异常！异常信息为：{}，e：{}", e.getMessage(), e);
        }
    }
    
    private void getNewStorageClient() throws IOException {
    	if(!StringUtils.isEmpty(groupName)) {
    		Integer port = FastDFSUtil.getPortByGroup(groupName);
            StorageServer[] storeStorages = trackerClient.getStoreStorages(trackerServer, "");
            LOG.info("----------storeStorages.length:{}", storeStorages.length);
            if (null != storeStorages && storeStorages.length > 0) {
                for (StorageServer server : storeStorages) {
                    int socketPort = server.getSocket().getPort();
                    LOG.info("----------port:{}", port);
                    LOG.info("----------socketPort:{}", socketPort);
                    if (null == port && FastDFSConfig.PORT_GROUP1.equals(socketPort)) {
                        storageServer = server;
                        LOG.info("==================");
                    } else if (port.equals(socketPort)) {
                        storageServer = server;
                        LOG.info("********************");
                    }
                }
            }
    	}else {
    		StorageServer[] storeStorages = trackerClient.getStoreStorages(trackerServer, "");
            if (null != storeStorages && storeStorages.length > 0) {
                for (StorageServer server : storeStorages) {
                    int socketPort = server.getSocket().getPort();
                    if (socketPort == FastDFSConfig.PORT_GROUP1) {
                        storageServer = server;
                        break;
                    }
                }
            }
    	}
        if (storageServer == null) {
            LOG.error("storageServer 为空！");
            throw new IllegalStateException("getStoreStorage return null");
        }
        storageClient = new StorageClient(trackerServer, storageServer);
    }
    
    private void getNewStorageClient1() throws IOException {
    	this.groupName = "group1";
    	if(!StringUtils.isNumeric(groupName)) {
    		Integer port = FastDFSUtil.getPortByGroup(groupName);
            StorageServer[] storeStorages = trackerClient.getStoreStorages(trackerServer, "");
            LOG.info("----------storeStorages.length:{}", storeStorages.length);
            if (null != storeStorages && storeStorages.length > 0) {
                for (StorageServer server : storeStorages) {
                    int socketPort = server.getSocket().getPort();
                    LOG.info("----------port:{}", port);
                    LOG.info("----------socketPort:{}", socketPort);
                    if (null == port && FastDFSConfig.PORT_GROUP1.equals(socketPort)) {
                        storageServer = server;
                        LOG.info("==================");
                    } else if (port.equals(socketPort)) {
                        storageServer = server;
                        LOG.info("********************");
                    }
                }
            }
    	}else {
    		StorageServer[] storeStorages = trackerClient.getStoreStorages(trackerServer, "");
            if (null != storeStorages && storeStorages.length > 0) {
                for (StorageServer server : storeStorages) {
                    int socketPort = server.getSocket().getPort();
                    if (FastDFSConfig.PORT_GROUP1.equals(socketPort)) {
                        storageServer = server;
                        break;
                    }
                }
            }
    	}
        if (storageServer == null) {
            LOG.error("storageServer 为空！");
            throw new IllegalStateException("getStoreStorage return null");
        }
    	storageClient1 = new StorageClient1(trackerServer, storageServer);
    }

    /**
     * 上传文件
     *
     * @param file     文件对象
     * @param fileName 文件名
     * @return
     * @throws MyException
     * @throws IOException
     */
    public String uploadFile(File file, String fileName) throws IOException, MyException {
        return uploadFile(file, fileName, null);
    }

    /**
     * 上传文件
     *
     * @param file     文件对象
     * @param fileName 文件名
     * @param metaList 文件元数据
     * @return 文件访问路径
     * @throws MyException
     * @throws IOException
     */
    public String uploadFile(File file, String fileName, Map<String, String> metaList) throws IOException, MyException {
        byte[] bytes = IOUtils.toByteArray(new FileInputStream(file));
        NameValuePair[] nameValuePairs = null;
        if (null != metaList) {
            nameValuePairs = new NameValuePair[metaList.size()];
            int index = 0;
            for (Iterator<Map.Entry<String, String>> iterator = metaList.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<String, String> entry = iterator.next();
                String key = entry.getKey();
                String value = entry.getValue();
                nameValuePairs[index++] = new NameValuePair(key, value);
            }
        }
        String extension = fileName.substring(fileName.indexOf(".") + 1);
        getNewStorageClient1();
        String[] results = storageClient1.upload_file(bytes, extension, nameValuePairs);
        for (int i = 0; i < results.length; i++) {
            String result = results[i];
            System.out.println(result);
        }
        String groupName = results[0];
        String remoteFileName = results[1];
        String fileAbsolutePath = FastDFSConfig.PROTOCOL + FastDFSConfig.TRACKER_NGINX_ADDR
                + FastDFSConfig.COLON + FastDFSConfig.TRACKER_NGINX_PORT + FastDFSConfig.SEPARATOR + groupName
                + FastDFSConfig.SEPARATOR + remoteFileName;
        return fileAbsolutePath;
    }

    /**
     * 上传文件
     *
     * @param multipartFile
     *            待上传的文件对象
     * @param nameValuePairs
     *            需要传递的键值对
     * @return 文件服务器上的访问路径
     * @throws IOException
     * @throws MyException
     */
    // public String uploadFile(MultipartFile multipartFile,
    // NameValuePair[] nameValuePairs)
    // throws IOException, MyException {
    // String[] uploadResults = null;
    // String ext = multipartFile.getOriginalFilename()
    // .substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1);
    // uploadResults = storageClient1.upload_file(multipartFile.getBytes(), ext,
    // nameValuePairs);
    // String groupName = uploadResults[0];
    // String remoteFileName = uploadResults[1];
    // String fileAbsolutePath = FastDFSConfig.PROTOCOL +
    // FastDFSConfig.TRACKER_NGNIX_ADDR
    // // + trackerServer.getInetSocketAddress().getHostName()
    // + ":" + FastDFSConfig.TRACKER_NGNIX_PORT +
    // FastDFSConfig.SEPARATOR + groupName
    // + FastDFSConfig.SEPARATOR + remoteFileName;
    // return fileAbsolutePath;
    // }

    /**
     * 上传文件
     *
     * @param multipartFile  待上传的文件对象
     * @param nameValuePairs 需要传递的键值对
     * @return 文件服务器上的访问路径
     * @throws IOException
     * @throws MyException
     */
    public FastDFSUploadResult uploadFile(MultipartFile multipartFile, NameValuePair[] nameValuePairs) {
        FastDFSUploadResult result = null;
        String[] uploadResults = null;
        String groName = "";
        String remoteFileName;
        String fileAbsolutePath;
        try {
            String ext = multipartFile.getOriginalFilename()
                    .substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1);
            getNewStorageClient1();
            uploadResults = storageClient1.upload_file(multipartFile.getBytes(), ext, nameValuePairs);
            groName = uploadResults[0];
            remoteFileName = uploadResults[1];
            fileAbsolutePath = FastDFSConfig.PROTOCOL + FastDFSConfig.TRACKER_NGINX_ADDR
                    + FastDFSConfig.COLON + FastDFSConfig.TRACKER_NGINX_PORT + FastDFSConfig.SEPARATOR + groName
                    + FastDFSConfig.SEPARATOR + remoteFileName;
            result = new FastDFSUploadResult(groName, remoteFileName, fileAbsolutePath,
                    ConfigConsts.FILE_UPLOAD_CODE_SUCCESS, ConfigConsts.FILE_UPLOAD_MSG_SUCCESS);
        } catch (IOException | MyException e) {
            LOG.error("上传文件出现异常!", e);
            result = new FastDFSUploadResult(ConfigConsts.FILE_UPLOAD_CODE_FAILED,
                    ConfigConsts.FILE_UPLOAD_MSG_FAILED + e.getMessage());
        }
        return result;
    }

    /**
     * 上传文件
     *
     * @param bytes    文件对象
     * @param fileName 文件名
     * @param metaList 文件元数据
     * @return 文件上传结果对象
     * @throws MyException
     * @throws IOException
     */
    public FastDFSUploadResult uploadFile(byte[] bytes, String fileName, Map<String, String> metaList)
            throws IOException, MyException {

        FastDFSUploadResult uploadResult = null;
        NameValuePair[] nameValuePairs = null;
        String[] results = new String[]{};
        if (null != metaList) {
            nameValuePairs = new NameValuePair[metaList.size()];
            int index = 0;
            for (Iterator<Map.Entry<String, String>> iterator = metaList.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<String, String> entry = iterator.next();
                String key = entry.getKey();
                String value = entry.getValue();
                nameValuePairs[index++] = new NameValuePair(key, value);
            }
        }
        String extension = fileName.substring(fileName.indexOf(".") + 1);
        getNewStorageClient1();
        results = storageClient1.upload_file(bytes, extension, nameValuePairs);
        String groName = results[0];
        String remoteFileName = results[1];
        String fileAbsolutePath = FastDFSConfig.PROTOCOL + FastDFSConfig.TRACKER_NGINX_ADDR
                + FastDFSConfig.COLON + FastDFSConfig.TRACKER_NGINX_PORT + FastDFSConfig.SEPARATOR + groName
                + FastDFSConfig.SEPARATOR + remoteFileName;

        uploadResult = new FastDFSUploadResult(groupName, remoteFileName, fileAbsolutePath,
                ConfigConsts.FILE_UPLOAD_CODE_SUCCESS, ConfigConsts.FILE_UPLOAD_MSG_SUCCESS);
        return uploadResult;
    }

    /**
     * 上传文件，返回文件id标识
     *
     * @param file     文件对象
     * @param fileName 文件名
     * @param metaList 文件元数据
     * @return 文件id标识
     * @throws MyException
     * @throws IOException
     */
    public String uploadFileForFileId(File file, String fileName, Map<String, String> metaList) throws IOException, MyException {
        String fileId = null;
        byte[] bytes = IOUtils.toByteArray(new FileInputStream(file));
        NameValuePair[] nameValuePairs = null;
        if (null != metaList) {
            nameValuePairs = new NameValuePair[metaList.size()];
            int index = 0;
            for (Iterator<Map.Entry<String, String>> iterator = metaList.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<String, String> entry = iterator.next();
                String key = entry.getKey();
                String value = entry.getValue();
                nameValuePairs[index++] = new NameValuePair(key, value);
            }
        }
        String extension = fileName.substring(fileName.indexOf(".") + 1);
        getNewStorageClient1();
        String[] results = storageClient1.upload_file(bytes, extension, nameValuePairs);
        if (results != null && results.length > 0) {
            fileId = results[0].concat("/").concat(results[1]);
        }
        return fileId;
    }

    /**
     * 获取文件元数据
     *
     * @param fieldId 文件id
     * @return
     * @throws MyException
     * @throws IOException
     */
    public Map<String, String> getFileMetaData(String fieldId) throws IOException, MyException {
    	getNewStorageClient1();
        NameValuePair[] metaList = storageClient1.get_metadata1(fieldId);
        if (null != metaList) {
            HashMap<String, String> map = new HashMap<String, String>();
            for (NameValuePair metaItem : metaList) {
                map.put(metaItem.getName(), metaItem.getValue());
            }
            return map;
        }
        return null;
    }

    /**
     * 下载文件
     *
     * @param fileId  文件ID（上传文件成功后返回的ID）
     * @param outFile 文件下载保存位置
     * @return
     * @throws MyException
     * @throws IOException
     */
    public int downloadFile(String fileId, File outFile) throws IOException, MyException {
        FileOutputStream fos = null;
        byte[] content = null;
        try {
        	getNewStorageClient1();
        	content = storageClient1.download_file1(fileId);
        }catch(Exception e) {
        	getNewStorageClient1();
        	content = storageClient1.download_file1(fileId);
        }
        fos = new FileOutputStream(outFile);
        InputStream inputStream = new ByteArrayInputStream(content);
        IOUtils.copy(inputStream, fos);
        return 0;
    }


    /**
     * 下载文件，获取文件字节数组
     *
     * @param groupName      组名
     * @param remoteFileName 文件名
     * @return 字节数组
     * @throws IOException
     * @throws MyException
     */
    public byte[] downloadFileForBytes(String groupName, String remoteFileName) throws IOException, MyException {
        byte[] bytes = null;
        getNewStorageClient();
        if (null != storageClient) {
            bytes = storageClient.download_file(groupName, remoteFileName);
        } else {
        	LOG.error("storageClient is null ");
        }
        return bytes;
    }

    /**
     * 根据文件元id下载文件，获取文件字节数组
     *
     * @param fileId 文件标识
     * @return 字节数组
     * @throws IOException
     * @throws MyException
     */
    public byte[] downloadFileForBytes(String fileId) throws IOException, MyException {
        byte[] bytes = null;
        getNewStorageClient1();
        if(null == storageClient1) {
        	getNewStorageClient1();
        }
        if (null != storageClient1) {
        	try {
        		bytes = storageClient1.download_file1(fileId);
        	}catch(Exception e) {
        		bytes = storageClient1.download_file1(fileId);
        	}
        } else {
        	LOG.error("storageClient1 is null");
        }
        return bytes;
    }

    /**
     * 下载文件，获取文件字节数组
     *
     * @param groupName      组名
     * @param remoteFileName 文件名
     * @return 字节数组
     * @throws IOException
     * @throws MyException
     */
    public File downloadFileForFile(String groupName, String remoteFileName, File outFile) throws IOException, MyException {
        byte[] bytes = null;
        getNewStorageClient1();
        if (null != storageClient1) {
            bytes = storageClient1.download_file(groupName, remoteFileName);
        }
        if (null != bytes) {
            FileOutputStream fos = new FileOutputStream(outFile);
            InputStream inputStream = new ByteArrayInputStream(bytes);
            IOUtils.copy(inputStream, fos);
        }
        return outFile;
    }

    /**
     * 下载文件
     *
     * @param groupName      文件存放的组名
     * @param remoteFileName 文件名
     * @param destFileName   下载后生成的文件名
     * @return
     * @throws IOException
     * @throws MyException
     */
    public ResponseEntity<byte[]> downloadFile(String groupName, String remoteFileName, String destFileName)
            throws IOException, MyException {
        byte[] content = null;
        HttpHeaders headers = new HttpHeaders();
        getNewStorageClient1();
        content = storageClient1.download_file(groupName, remoteFileName);
        headers.setContentDispositionFormData("attachment", new String(destFileName.getBytes("UTF-8"), "iso-8859-1"));
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(content, headers, HttpStatus.CREATED);
    }

    /**
     * 删除文件
     *
     * @param fileId 文件id
     * @return 删除成功返回0，否则失败返回-1
     * @throws MyException
     * @throws IOException
     */
    public int deleteFile(String fileId) throws IOException, MyException {
        return storageClient1.delete_file1(fileId);
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public StorageClient1 getStorageClient1() {
        return storageClient1;
    }

    public void setStorageClient1(StorageClient1 storageClient1) {
        this.storageClient1 = storageClient1;
    }

    public StorageClient getStorageClient() {
        return storageClient;
    }

    public void setStorageClient(StorageClient storageClient) {
        this.storageClient = storageClient;
    }

}
