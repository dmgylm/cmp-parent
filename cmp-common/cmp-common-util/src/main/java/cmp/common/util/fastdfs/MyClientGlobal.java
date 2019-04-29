package cmp.common.util.fastdfs;

import org.csource.common.IniFileReader;
import org.csource.common.MyException;
import org.csource.fastdfs.TrackerGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cmp.common.util.string.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;

/**
 * 工具类
 *
 * @author sd
 * @date 2017/11/02
 */
public class MyClientGlobal {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyClientGlobal.class);

    private static Properties properties = new Properties();
    public static final String CONF_KEY_CONNECT_TIMEOUT = "connect_timeout";
    public static final String CONF_KEY_NETWORK_TIMEOUT = "network_timeout";
    public static final String CONF_KEY_CHARSET = "charset";
    public static final String CONF_KEY_HTTP_ANTI_STEAL_TOKEN = "http.anti_steal_token";
    public static final String CONF_KEY_HTTP_SECRET_KEY = "http.secret_key";
    public static final String CONF_KEY_HTTP_TRACKER_HTTP_PORT = "http.tracker_http_port";
    public static final String CONF_KEY_TRACKER_SERVER = "tracker_server";
    public static final String PROP_KEY_CONNECT_TIMEOUT_IN_SECONDS = "fastdfs.connect_timeout_in_seconds";
    public static final String PROP_KEY_NETWORK_TIMEOUT_IN_SECONDS = "fastdfs.network_timeout_in_seconds";
    public static final String PROP_KEY_CHARSET = "fastdfs.charset";
    public static final String PROP_KEY_HTTP_ANTI_STEAL_TOKEN = "fastdfs.http_anti_steal_token";
    public static final String PROP_KEY_HTTP_SECRET_KEY = "fastdfs.http_secret_key";
    public static final String PROP_KEY_HTTP_TRACKER_HTTP_PORT = "fastdfs.http_tracker_http_port";
    public static final String PROP_KEY_TRACKER_SERVERS = "fastdfs.tracker_servers";
    public static final int DEFAULT_CONNECT_TIMEOUT = 5;
    public static final int DEFAULT_NETWORK_TIMEOUT = 30;
    public static final String DEFAULT_CHARSET = "UTF-8";
    public static final boolean DEFAULT_HTTP_ANTI_STEAL_TOKEN = false;
    public static final String DEFAULT_HTTP_SECRET_KEY = "FastDFS1234567890";
    public static final int DEFAULT_HTTP_TRACKER_HTTP_PORT = 80;
    public static int g_connect_timeout = 5000;
    public static int g_network_timeout = 30000;
    public static String g_charset = "UTF-8";
    public static boolean g_anti_steal_token = false;
    public static String g_secret_key = "FastDFS1234567890";
    public static int g_tracker_http_port = 80;
    public static TrackerGroup g_tracker_group;

    private MyClientGlobal() {
    }

    public static void init(InputStream inputStream) throws IOException, MyException {
        properties.load(inputStream);
        MyIniFileReader myIniFileReader = new MyIniFileReader(inputStream);
        String timeout = properties.getProperty("connect_timeout");
        g_connect_timeout = StringUtils.isNotBlank(timeout) ? Integer.parseInt(timeout) : 5;
        if (g_connect_timeout < 0) {
            g_connect_timeout = 5;
        }

//        g_connect_timeout *= 1000;
        timeout = properties.getProperty("network_timeout");
        g_network_timeout = StringUtils.isNotBlank(timeout) ? Integer.parseInt(timeout) : 30;
        if (g_network_timeout < 0) {
            g_network_timeout = 30;
        }

//        g_network_timeout *= 1000;
        g_charset = properties.getProperty("charset");
        if (g_charset == null || g_charset.length() == 0) {
            g_charset = "ISO8859-1";
        }

        String[] szTrackerServers = myIniFileReader.getValues("tracker_server");
        if (szTrackerServers == null) {
            throw new MyException("item \"tracker_server\" in " + inputStream + " not found");
        } else {
            InetSocketAddress[] tracker_servers = new InetSocketAddress[szTrackerServers.length];

            for (int i = 0; i < szTrackerServers.length; ++i) {
                String[] parts = szTrackerServers[i].split("\\:", 2);
                if (parts.length != 2) {
                    throw new MyException("the value of item \"tracker_server\" is invalid, the correct format is host:port");
                }

                tracker_servers[i] = new InetSocketAddress(parts[0].trim(), Integer.parseInt(parts[1].trim()));
            }

            g_tracker_group = new TrackerGroup(tracker_servers);
            g_tracker_http_port = myIniFileReader.getIntValue("http.tracker_http_port", 80);
            g_anti_steal_token = myIniFileReader.getBoolValue("http.anti_steal_token", false);
            if (g_anti_steal_token) {
                g_secret_key = myIniFileReader.getStrValue("http.secret_key");
            }

        }
    }

    public static void initByProperties(String propsFilePath) throws IOException, MyException {
        Properties props = new Properties();
        InputStream in = IniFileReader.loadFromOsFileSystemOrClasspathAsStream(propsFilePath);
        if (in != null) {
            props.load(in);
        }

        initByProperties(props);
    }

    public static void initByProperties(Properties props) throws IOException, MyException {
        String trackerServersConf = props.getProperty("fastdfs.tracker_servers");
        if (trackerServersConf != null && trackerServersConf.trim().length() != 0) {
            initByTrackers(trackerServersConf.trim());
            String connectTimeoutInSecondsConf = props.getProperty("fastdfs.connect_timeout_in_seconds");
            String networkTimeoutInSecondsConf = props.getProperty("fastdfs.network_timeout_in_seconds");
            String charsetConf = props.getProperty("fastdfs.charset");
            String httpAntiStealTokenConf = props.getProperty("fastdfs.http_anti_steal_token");
            String httpSecretKeyConf = props.getProperty("fastdfs.http_secret_key");
            String httpTrackerHttpPortConf = props.getProperty("fastdfs.http_tracker_http_port");
            if (connectTimeoutInSecondsConf != null && connectTimeoutInSecondsConf.trim().length() != 0) {
                g_connect_timeout = Integer.parseInt(connectTimeoutInSecondsConf.trim()) * 1000;
            }

            if (networkTimeoutInSecondsConf != null && networkTimeoutInSecondsConf.trim().length() != 0) {
                g_network_timeout = Integer.parseInt(networkTimeoutInSecondsConf.trim()) * 1000;
            }

            if (charsetConf != null && charsetConf.trim().length() != 0) {
                g_charset = charsetConf.trim();
            }

            if (httpAntiStealTokenConf != null && httpAntiStealTokenConf.trim().length() != 0) {
                g_anti_steal_token = Boolean.parseBoolean(httpAntiStealTokenConf);
            }

            if (httpSecretKeyConf != null && httpSecretKeyConf.trim().length() != 0) {
                g_secret_key = httpSecretKeyConf.trim();
            }

            if (httpTrackerHttpPortConf != null && httpTrackerHttpPortConf.trim().length() != 0) {
                g_tracker_http_port = Integer.parseInt(httpTrackerHttpPortConf);
            }

        } else {
            throw new MyException(String.format("configure item %s is required", new Object[]{"fastdfs.tracker_servers"}));
        }
    }

    public static void initByTrackers(String trackerServers) throws IOException, MyException {
        ArrayList list = new ArrayList();
        String spr1 = ",";
        String spr2 = ":";
        String[] arr1 = trackerServers.trim().split(spr1);
        String[] trackerAddresses = arr1;
        int len$ = arr1.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            String addrStr = trackerAddresses[i$];
            String[] arr2 = addrStr.trim().split(spr2);
            String host = arr2[0].trim();
            int port = Integer.parseInt(arr2[1].trim());
            list.add(new InetSocketAddress(host, port));
        }

        InetSocketAddress[] var12 = (InetSocketAddress[]) list.toArray(new InetSocketAddress[list.size()]);
        initByTrackers(var12);
    }

    public static void initByTrackers(InetSocketAddress[] trackerAddresses) throws IOException, MyException {
        g_tracker_group = new TrackerGroup(trackerAddresses);
    }

    public static Socket getSocket(String ip_addr, int port) throws IOException {
        Socket sock = new Socket();
        sock.setSoTimeout(g_network_timeout);
        sock.connect(new InetSocketAddress(ip_addr, port), g_connect_timeout);
        return sock;
    }

    public static Socket getSocket(InetSocketAddress addr) throws IOException {
        Socket sock = new Socket();
        sock.setSoTimeout(g_network_timeout);
        sock.connect(addr, g_connect_timeout);
        return sock;
    }

    public static int getG_connect_timeout() {
        return g_connect_timeout;
    }

    public static void setG_connect_timeout(int connect_timeout) {
        g_connect_timeout = connect_timeout;
    }

    public static int getG_network_timeout() {
        return g_network_timeout;
    }

    public static void setG_network_timeout(int network_timeout) {
        g_network_timeout = network_timeout;
    }

    public static String getG_charset() {
        return g_charset;
    }

    public static void setG_charset(String charset) {
        g_charset = charset;
    }

    public static int getG_tracker_http_port() {
        return g_tracker_http_port;
    }

    public static void setG_tracker_http_port(int tracker_http_port) {
        g_tracker_http_port = tracker_http_port;
    }

    public static boolean getG_anti_steal_token() {
        return g_anti_steal_token;
    }

    public static boolean isG_anti_steal_token() {
        return g_anti_steal_token;
    }

    public static void setG_anti_steal_token(boolean anti_steal_token) {
        g_anti_steal_token = anti_steal_token;
    }

    public static String getG_secret_key() {
        return g_secret_key;
    }

    public static void setG_secret_key(String secret_key) {
        g_secret_key = secret_key;
    }

    public static TrackerGroup getG_tracker_group() {
        return g_tracker_group;
    }

    public static void setG_tracker_group(TrackerGroup tracker_group) {
        g_tracker_group = tracker_group;
    }

    public static String configInfo() {
        String trackerServers = "";
        if (g_tracker_group != null) {
            InetSocketAddress[] trackerAddresses = g_tracker_group.tracker_servers;
            InetSocketAddress[] arr$ = trackerAddresses;
            int len$ = trackerAddresses.length;

            for (int i$ = 0; i$ < len$; ++i$) {
                InetSocketAddress inetSocketAddress = arr$[i$];
                if (trackerServers.length() > 0) {
                    trackerServers = trackerServers + ",";
                }

                trackerServers = trackerServers + inetSocketAddress.toString().substring(1);
            }
        }

        return "{\n  g_connect_timeout(ms) = " + g_connect_timeout + "\n  g_network_timeout(ms) = " + g_network_timeout + "\n  g_charset = " + g_charset + "\n  g_anti_steal_token = " + g_anti_steal_token + "\n  g_secret_key = " + g_secret_key + "\n  g_tracker_http_port = " + g_tracker_http_port + "\n  trackerServers = " + trackerServers + "\n}";
    }

    public static String getValueByKey(String key) {
        String value = "";
        try {
            value = properties.getProperty(key);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("get value from key:{} occur error!", key);
        }
        return value;
    }

}
