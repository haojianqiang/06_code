package com.gpdi.web.entity.config.chicksDistribution;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
@Entity
@Table(name="ce_chicks_ccs")
public class ChicksDistribution implements Serializable {
    private static final long serialVersionUID = 2530137883376969690L;
    private Integer id;
    private Integer chicksId; //鸡苗id
    private Integer houseId; //鸡舍id
    private Integer chickCount;//鸡苗数量
    private Integer produceCycle;//生产周期(天)
    private String originCode;//溯源码
    private String qrCode;//二维码
    private Integer status;//鸡苗状态，1：存栏   0：出栏
    private Date createTime;//创建时间
    private Date modifyTime;//修改时间
    private String modifier;//操作员
    private Integer modifierId;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "chicks_id")
    public Integer getChicksId() {
        return chicksId;
    }

    public void setChicksId(Integer chicksId) {
        this.chicksId = chicksId;
    }
    @Column(name = "house_id")
    public Integer getHouseId() {
        return houseId;
    }

    public void setHouseId(Integer houseId) {
        this.houseId = houseId;
    }
    @Column(name = "chick_count")
    public Integer getChickCount() {
        return chickCount;
    }

    public void setChickCount(Integer chickCount) {
        this.chickCount = chickCount;
    }
    @Column(name = "produce_cycle")
    public Integer getProduceCycle() {
        return produceCycle;
    }
    public void setProduceCycle(Integer produceCycle) {
        this.produceCycle = produceCycle;
    }

    @Column(name = "origin_code")
    public String getOriginCode() {
        return originCode;
    }
    public void setOriginCode(String originCode) {
        this.originCode = originCode;
    }

    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name = "qr_code")
    public String getQrCode() {
        return qrCode;
    }
    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "modify_time")
    public Date getModifyTime() {
        return modifyTime;
    }
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    @Column(name = "modifier")
    public String getModifier() {
        return modifier;
    }
    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    @Column(name = "modifier_id")
    public Integer getModifierId() {
        return modifierId;
    }
    public void setModifierId(Integer modifierId) {
        this.modifierId = modifierId;
    }

}
