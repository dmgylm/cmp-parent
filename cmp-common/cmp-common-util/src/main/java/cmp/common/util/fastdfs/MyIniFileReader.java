package cmp.common.util.fastdfs;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * 读取FastDFS配置文件工具类
 *
 * @author sd
 * @date 2017/11/02
 */
public class MyIniFileReader {

    private Hashtable paramTable;
    private String conf_filename;
    private InputStream inputStream;

    public MyIniFileReader(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;
        this.loadFromFile(inputStream);
    }

    public static ClassLoader classLoader() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = ClassLoader.getSystemClassLoader();
        }

        return loader;
    }

    public static InputStream loadFromOsFileSystemOrClasspathAsStream(String filePath) {
        Object in = null;

        try {
            if ((new File(filePath)).exists()) {
                in = new FileInputStream(filePath);
            } else {
                in = classLoader().getResourceAsStream(filePath);
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return (InputStream) in;
    }

    public String getConfFilename() {
        return this.conf_filename;
    }

    public String getStrValue(String name) {
        Object obj = this.paramTable.get(name);
        return obj == null ? null : (obj instanceof String ? (String) obj : (String) ((ArrayList) obj).get(0));
    }

    public int getIntValue(String name, int default_value) {
        String szValue = this.getStrValue(name);
        return szValue == null ? default_value : Integer.parseInt(szValue);
    }

    public boolean getBoolValue(String name, boolean default_value) {
        String szValue = this.getStrValue(name);
        return szValue == null ? default_value : szValue.equalsIgnoreCase("yes") || szValue.equalsIgnoreCase("on") || szValue.equalsIgnoreCase("true") || szValue.equals("1");
    }

    public String[] getValues(String name) {
        Object obj = this.paramTable.get(name);
        if (obj == null) {
            return null;
        } else {
            String[] values;
            if (obj instanceof String) {
                values = new String[]{(String) obj};
                return values;
            } else {
                Object[] objs = ((ArrayList) obj).toArray();
                values = new String[objs.length];
                System.arraycopy(objs, 0, values, 0, objs.length);
                return values;
            }
        }
    }

    private void loadFromFile(InputStream inputStream) throws IOException {
        InputStream in = loadFromOsFileSystemOrClasspathAsStream("fdfs.properties");

        try {
            this.readToParamTable(in);
        } catch (Exception var12) {
            var12.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception var11) {
                var11.printStackTrace();
            }

        }

    }

    private void readToParamTable(InputStream in) throws IOException {
        this.paramTable = new Hashtable();
        if (in != null) {
            InputStreamReader inReader = null;
            BufferedReader bufferedReader = null;

            try {
                inReader = new InputStreamReader(in);
                bufferedReader = new BufferedReader(inReader);

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    line = line.trim();
                    if (line.length() != 0 && line.charAt(0) != 35) {
                        String[] parts = line.split("=", 2);
                        if (parts.length == 2) {
                            String name = parts[0].trim();
                            String value = parts[1].trim();
                            Object obj = this.paramTable.get(name);
                            if (obj == null) {
                                this.paramTable.put(name, value);
                            } else {
                                ArrayList valueList;
                                if (obj instanceof String) {
                                    valueList = new ArrayList();
                                    valueList.add(obj);
                                    valueList.add(value);
                                    this.paramTable.put(name, valueList);
                                } else {
                                    valueList = (ArrayList) obj;
                                    valueList.add(value);
                                }
                            }
                        }
                    }
                }
            } catch (Exception var19) {
                var19.printStackTrace();
            } finally {
                try {
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }

                    if (inReader != null) {
                        inReader.close();
                    }
                } catch (Exception var18) {
                    var18.printStackTrace();
                }

            }

        }
    }
}
