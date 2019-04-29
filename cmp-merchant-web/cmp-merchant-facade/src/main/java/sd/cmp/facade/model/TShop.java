package sd.cmp.facade.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 商户信息表
 * </p>
 *
 * @author lm
 * @since 2019-04-26
 */
@Data
@ToString
public class TShop implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商户编号
     */
    private String shopCode;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 门店名称
     */
    private String shopName;

    /**
     * 营业开始时间
     */
    private LocalDateTime openTime;

    /**
     * 营业结束时间
     */
    private LocalDateTime restTime;

    /**
     * 联系人
     */
    private String contactName;

    /**
     * 第二联系人
     */
    private String contactNameSecond;

    /**
     * 联系人电话
     */
    private String telephone;

    /**
     * 第二联系人电话
     */
    private String telephoneSecond;

    /**
     * 商户描述
     */
    private String shopDesc;

    /**
     * 商户地址
     */
    private String address;

    /**
     * 火星坐标(高德腾讯坐标)
     */
    private String gcjCoordinate;

    /**
     * 坐标
     */
    private String coordinate;

    /**
     * 用户名 (不用)
     */
    private String userName;

    /**
     * 密码(不用)
     */
    private String password;

    /**
     * 状态(2:停用（解约）,3:下线（下线）,4:有效（新增，上线）)
     */
    private String isStatus;

    /**
     * 审批状态(4:审批通过，5:待审批，6:待生效，7:审批未通过)
     */
    private String approveStatus;

    /**
     * 是否连锁,0:否,1:是
     */
    private String isChain;

    /**
     * 连锁商户编号
     */
    private String chainShopCode;

    /**
     * 是否关联机构(0：否，1：是)
     */
    private String isSource;

    /**
     * 网点类型(0:汽车美容店,1:汽车美容店+快修,2:汽车美容店+维修)
     */
    private String shopType;

    /**
     * 操作工位(0:1个及以上,1:2个及以上,2:3个及以上,3:4个及以上)
     */
    private String operateStation;

    /**
     * 营业面积(0:100平米以下,1:100-300平米,2:300平米以上)
     */
    private String areaSize;

    /**
     * 合作年限(0:<1年,1:1-3年,2:>3年)
     */
    private String year;

    /**
     * 网点装修(0:一般,1:舒适,2:豪华)
     */
    private String shopDecoration;

    /**
     * 客户休息室(0:一般,1:舒适,2:豪华)
     */
    private String restRoom;

    /**
     * 员工着装(0:不统一,1:统一)
     */
    private String employeeDressing;

    /**
     * 区域表ID
     */
    private Integer areaId;

    /**
     * 拓展账号(用户表账号)
     */
    private String expandUserName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 商户评级
     */
    private String rating;

    /**
     * 商户评分
     */
    private String ratingScore;

    /**
     * 生效开始时间(下线时间)
     */
    private LocalDateTime time;

    /**
     * 生效结束时间(下线的上线日期)
     */
    private LocalDateTime offTime;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 性别（1：男，2：女）
     */
    private String gender;

    /**
     * 性别2（1：男，2：女）
     */
    private String genderSecond;

    /**
     * 收款人姓名（暂时不用）
     */
    private String payeeName;

    /**
     * 银行卡号（暂时不用）
     */
    private String bankAccount;

    /**
     * 是否是历史记录(0:否,1:是)
     */
    private String isHistory;

    /**
     * 是否启用显示（0：否，1：是）
     */
    private String isShow;

    /**
     * 创建人
     */
    private String creater;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    private String updater;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 第三方门店编码
     */
    private String thirdShopCode;

    /**
     * 总订单数量
     */
    private Integer orderNum;

    /**
     * 订单数量(30天内)
     */
    private Integer orderNumMonthly;

    /**
     * 总登录次数
     */
    private Integer loginNum;

    /**
     * 登录次数(30天内)
     */
    private Integer loginNumMonthly;

    /**
     * 网点坐标地址
     */
    @TableField("coordinateAddress")
    private String coordinateAddress;

    /**
     * 运营状态(0:休息,1:打烊,2:繁忙,3:空闲,4:排队中)
     */
    private String runStatus;
}
