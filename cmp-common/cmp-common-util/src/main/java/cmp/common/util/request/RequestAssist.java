package cmp.common.util.request;

import cmp.common.util.string.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author ：caochuanqi
 * @Date ：Created in 上午10:28 2018/11/21
 * @Description： 请求参数辅助类
 * @Modified By：
 * @Version ：
 */
public class RequestAssist<T> {

    public RequestAssist(){}

    /**
     *@Description:  * 传入AreaTreeVO对象list和type值，来获取省/市/区的字符串list
     * @param areaTreeVOList
     * @param type 0省，1市，2区
     *@Date: 上午11:00 2018/11/21
     *@Return:  * @return : java.util.List<java.lang.String>
     **/
    public List<String> changeAreaTreeVOList(List<T> areaTreeVOList, String type) throws Exception {
        if(StringUtils.isBlank(type)){
            throw new RuntimeException();
        }
        List<String> rtnList = new ArrayList<>();
        if(areaTreeVOList == null){
            return rtnList;
        }
        for(T t : areaTreeVOList){
            Field[] fields = t.getClass().getDeclaredFields();
            if(fields != null){
                Object idValue = null;
                //寻找id值
                for(Field eachField : fields) {
                    String fieldName = eachField.getName();
                    if ("id".equals(fieldName)) {
                        String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                        Object value = (String) t.getClass().getMethod(getMethodName, new Class[]{}).invoke(t, new Object[]{});
                        idValue = value;
                        break;
                    }
                }
                //寻找type值
                if(idValue != null){
                    for(Field eachField : fields){
                        String fieldName = eachField.getName();
                        if("type".equals(fieldName)){
                            String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                            Object value = (String)t.getClass().getMethod(getMethodName, new Class[]{}).invoke(t, new Object[]{});
                            if(value != null){
                                String typeValue = (String) value;
                                if(type.equals(typeValue)){
                                    //找到了需要的级别地域
                                    rtnList.add((String) idValue);
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
        return rtnList;
    }
}
