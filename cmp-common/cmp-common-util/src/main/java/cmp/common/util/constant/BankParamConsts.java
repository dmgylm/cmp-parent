package cmp.common.util.constant;

/**
 * 中行项目参数状态码 
 * 
 * @author wzhiqiang
 */
public class BankParamConsts {
    
    //图片访问地址，改为服务器的IP
    public final static String IMG_PROJECT_VISIT_URL = "http://192.168.1.152:8848/csp-web-manager/upload/projectImg/";//项目图片访问地址
//    public final static String IMG_APP_VISIT_URL = Utils.getValueByKey("imgAppVisitUrl");//APP上传项目图片访问地址
//    //服务器图片备份文件夹
//    public final static String APP_IMG_SAVE_FOLDER_BACK = Utils.getValueByKey("appImgSaveFolderBack");//项目图片保存文件夹
    
	//redis存储key声明
	public final static String _REDIS_MATERIAL_KEY = "jf_material";//物料代码名称redis key
	public final static String _REDIS_BANK_KEY = "jf_bank";//总行代码名称redis key
	public final static String _REDIS_BRANCH_KEY = "jf_branch";//支行代码名称redis key
	public final static String _REDIS_PROVINCE_KEY = "jf_province";//省份代码名称redis key
	public final static String _REDIS_CITY_KEY = "jf_city";//城市代码名称redis key
	public final static String _REDIS_IVN_BALANCE_RECORD_KEY = "ivn_balance"; //定时任务记录结余数据标识
//	public final static String REDIS_USER_PERM_KEY = "jf_user_perm";//  登录用户名 关联 权限组code
//	public final static String _REDIS_PACKAGE_KEY = "jf_package";//   套餐代码 关联 名称
//        public final static String _REDIS_PERM_KEY = "jf_perm_name";//  权限组code 关联 权限组名称

        /**
         * 用于中行app管理员 查看全部项目组的库存
         */
    public final static String ALL_PERM_GROUP="666"; //全部项目组 用于中行app管理员 查看全部项目组的库存
	
    public final static String _CHARSETNAME = "UTF-8";
    public final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public final static String _KEY = "0123456789ABCDEFG%$@#*&^!()HIJK";//MD5加密串
    public final static String SECRET_KEY = "20150721"; //数据传输秘钥
    public final static String APP_KEY = "57d46e18243b452a515bc59c";//推送key
    public final static String MASTER_SECRET = "d237f86ff712158af235e89d";//推送秘钥
    
    
    //启用状态
    public final static String SYS_STATUS_NORMAL = "1";//启用
    public final static String SYS_STATUS_NORMAL_NAME = "启用";//启用
    public final static String SYS_STATUS_BAN = "0";//停用
    public final static String SYS_STATUS_BAN_NAME = "停用";//停用
    public final static String SYS_STATUS_START = "2";//        初始状态
    public final static String SYS_STATUS_START_NAME = "初始";//初始状态
    
    //返回异常代码
    public final static String EXCEPTION_SUCCESS_CODE = "10040000";//中行项目 操作成功返回代码
    
    //项目上缴状态
    public final static String SYS_PRO_PAID_NO = "未上缴";//未上缴
    public final static String SYS_PRO_PAID_YES = "已上缴";//上缴过物料
    
    //登录类型
    public final static String LOGIN_PHONE = "1";//手机登录
    public final static String LOGIN_PHONE_NAME = "手机登录";//手机登录
    public final static String LOGIN_QQ = "2";//QQ登录
    public final static String LOGIN_QQ_NAME = "QQ登录";//QQ登录
    public final static String LOGIN_WECHAT = "3";//微信
    public final static String LOGIN_WECHAT_NAME = "微信登录";//微信
    public final static String LOGIN_WEIBO = "4";//微博登录
    public final static String LOGIN_WEIBO_NAME = "微博登录";//微博登录
    

    //用户类型
    public final static String USER_TYPE_1 = "1";//手机用户登录
    public final static String USER_TYPE_2 = "2";//第三方用户登录
    //是否
    public final static String SYS_NO = "0";//否
    public final static String SYS_NO_NAME = "否";//否
    public final static String SYS_IS = "1";//是
    public final static String SYS_IS_NAME = "是";//是
    
    //订单来源
    public final static String ORDER_SOURCE_01 = "1";//网站
    public final static String ORDER_SOURCE_02 = "2";//app-android
    public final static String ORDER_SOURCE_03 = "3";//app-ios
    
    
    public final static String IMG_SUFFIX = ".jpg";//图片后缀
    
    //图片保存文件夹
    public final static String PROJECT_IMG_SAVE_FOLDER = "/upload/projectImg/";//项目图片保存文件夹
    public final static String APP_IMG_SAVE_FOLDER = "/upload/appImg/";//项目图片保存文件夹
    
    //默认用户昵称
    public final static String DEFAULT_NAME = "外星人";

    //最高权限
    public final static String SUPPER_ADMIN = "system";
    //最高权限组
    public final static String SUPPER_GROUP = "21";
    //管理员权限组(查询所有数据权限)
    public final static String ADMIN_GROUP = "01";
    //新增 管理员助理权限组
    public final static String ASSISTANT_ADMIN_GROUP = "03";
    
    //用户登录存入cookie的KEY
    public final static String COOKIE_USER_LOGIN = "LOGIN_TOKEN";
    //验证码存入cookie的KEY
    public final static String COOKIE_USER_RAND = "RAND_TOKEN";
    
    //新增兑换 积分类型
    public final static String INTEGRAL_TYPE_1 = "1";
    public final static String INTEGRAL_TYPE_01 = "尊享积分";
    //新增兑换 积分类型
    public final static String INTEGRAL_TYPE_2 = "2";
    public final static String INTEGRAL_TYPE_02 = "交易积分";
    

    public final static String INTEGRAL_TYPE_3 = "3";
    public final static String INTEGRAL_TYPE_03 = "宽途积分";
    
    public final static String SEASON_YES = "1"; //有场次
    public final static String SEASON_NO = "2"; //无场次
    
    public final static String ADMIN = "admin"; //管理员
    
    /**中行相关系统权限组标识*/
    public final static String AUTH_ADMIN_CODE = "boc_jfdh_01"; //管理员权限组
    public final static String AUTH_AREA_CODE = "boc_jfdh_02";  //区域权限组
    
    /**中行权限组标识*/
    public final static String PERM_GROUP_CODE="boc";
    
    /**
     * 重庆权限组
     */
    public final static String CQ_PERM_GROUP_CODE="241";
    
    //结算状态
    public interface SettlementStatus{
        String SETTLEMENT = "1" ; //已结算
        String UNSETTLED = "0"; //未结算
    }
    /** 审批状态*/
    public enum ApprovalStatusEnum{
        UNAPPROVED(0,"未审批"),
        ADOPT(1,"通过"),
        REFUSE(2,"拒绝");
        ApprovalStatusEnum(int code, String value) {
            this.value = value;
            this.code = code;
        }
        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }
    /**订单操作类型*/
    public interface OrderOperatorType{
        String ORDER_UPDATE = "1" ; //订单修改
        String ORDER_DELETE = "2"; //订单删除
    }

}
