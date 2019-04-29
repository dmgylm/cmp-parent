package sd.cmp.facade.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 区域表
 * </p>
 *
 * @author lm
 * @since 2019-04-26
 */
public class TArea implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 省份代码
     */
    private String proNumber;

    /**
     * 省份名称
     */
    private String proName;

    /**
     * 城市代码
     */
    private String cityNumber;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 区域代码
     */
    private String areaNumber;

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 商业中心等详细地址
     */
    private String address;

    /**
     * 商业中心等详细名称
     */
    private String addressName;

    /**
     * 是否启用显示（0：否，1：是）
     */
    private String isShow;

    /**
     * 序列号
     */
    private Integer serialNum;

    /**
     * 城市编号（从100开始，每个地级市都有一个唯一的编号）
     */
    private Integer citySerialno;

    /**
     * 创建者
     */
    private String creater;

    /**
     * 更新者
     */
    private String updater;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProNumber() {
        return proNumber;
    }

    public void setProNumber(String proNumber) {
        this.proNumber = proNumber;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getCityNumber() {
        return cityNumber;
    }

    public void setCityNumber(String cityNumber) {
        this.cityNumber = cityNumber;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaNumber() {
        return areaNumber;
    }

    public void setAreaNumber(String areaNumber) {
        this.areaNumber = areaNumber;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    public Integer getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(Integer serialNum) {
        this.serialNum = serialNum;
    }

    public Integer getCitySerialno() {
        return citySerialno;
    }

    public void setCitySerialno(Integer citySerialno) {
        this.citySerialno = citySerialno;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "TArea{" +
        "id=" + id +
        ", proNumber=" + proNumber +
        ", proName=" + proName +
        ", cityNumber=" + cityNumber +
        ", cityName=" + cityName +
        ", areaNumber=" + areaNumber +
        ", areaName=" + areaName +
        ", address=" + address +
        ", addressName=" + addressName +
        ", isShow=" + isShow +
        ", serialNum=" + serialNum +
        ", citySerialno=" + citySerialno +
        ", creater=" + creater +
        ", updater=" + updater +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
