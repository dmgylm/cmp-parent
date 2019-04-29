package sd.cmp.facade.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 合同信息表
 * </p>
 *
 * @author lm
 * @since 2019-04-26
 */
public class TContract implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 合同编号
     */
    private String contractNo;

    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 结算类型（1：后结算，2：服务包，3：积分）
     */
    private String settlementType;

    /**
     * 签约总金额
     */
    private BigDecimal totalAmount;

    /**
     * 合同开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 合同结束时间
     */
    private LocalDateTime endTime;

    /**
     * 合同类型(0:非预约类,1:预约类)
     */
    private String contractType;

    /**
     * 合同内容
     */
    private String contractContent;

    /**
     * 是否启用显示（0：否，1：是）
     */
    private String isShow;

    /**
     * 合同状态(0:审批拒绝,1:待审批,2:待生效,3有效:,4:失效)
     */
    private String isStatus;

    /**
     * 连锁商户编码
     */
    private String chainCode;

    /**
     * 收款人姓名
     */
    private String payeeName;

    /**
     * 银行卡号
     */
    private String bankAccount;

    /**
     * 所属银行
     */
    private String bankType;

    /**
     * 类型(0:机构,1:商户,2:连锁商户)(不用)
     */
    private String type;

    /**
     * 类型id(不用)
     */
    private Integer typeId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    private String creater;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 修改人
     */
    private String updater;

    /**
     * 合同生效时间
     */
    private LocalDateTime time;

    /**
     * 合同备注
     */
    private String remark;

    /**
     * 是否是历史记录(0:否,1:是)
     */
    private String isHistory;

    /**
     * 社会信用代码
     */
    private String creditCode;

    /**
     * 身份证号码
     */
    private String idNumber;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getSettlementType() {
        return settlementType;
    }

    public void setSettlementType(String settlementType) {
        this.settlementType = settlementType;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(LocalDateTime beginTime) {
        this.beginTime = beginTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getContractContent() {
        return contractContent;
    }

    public void setContractContent(String contractContent) {
        this.contractContent = contractContent;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    public String getIsStatus() {
        return isStatus;
    }

    public void setIsStatus(String isStatus) {
        this.isStatus = isStatus;
    }

    public String getChainCode() {
        return chainCode;
    }

    public void setChainCode(String chainCode) {
        this.chainCode = chainCode;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIsHistory() {
        return isHistory;
    }

    public void setIsHistory(String isHistory) {
        this.isHistory = isHistory;
    }

    public String getCreditCode() {
        return creditCode;
    }

    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    @Override
    public String toString() {
        return "TContract{" +
        "id=" + id +
        ", contractNo=" + contractNo +
        ", contractName=" + contractName +
        ", settlementType=" + settlementType +
        ", totalAmount=" + totalAmount +
        ", beginTime=" + beginTime +
        ", endTime=" + endTime +
        ", contractType=" + contractType +
        ", contractContent=" + contractContent +
        ", isShow=" + isShow +
        ", isStatus=" + isStatus +
        ", chainCode=" + chainCode +
        ", payeeName=" + payeeName +
        ", bankAccount=" + bankAccount +
        ", bankType=" + bankType +
        ", type=" + type +
        ", typeId=" + typeId +
        ", createTime=" + createTime +
        ", creater=" + creater +
        ", updateTime=" + updateTime +
        ", updater=" + updater +
        ", time=" + time +
        ", remark=" + remark +
        ", isHistory=" + isHistory +
        ", creditCode=" + creditCode +
        ", idNumber=" + idNumber +
        "}";
    }
}
