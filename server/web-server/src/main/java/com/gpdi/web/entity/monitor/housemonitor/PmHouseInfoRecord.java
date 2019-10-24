package com.gpdi.web.entity.monitor.housemonitor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "pm_house_info_record")
public class PmHouseInfoRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer houseId;
    private Float   avgWight;
    private Float   co2;
    private Float   nh3;
    private Float   temperature;
    private Float   humidity;
    private String  weather;
    private Date    createTime;
    private Date    modifyTime;
    private String  modifier;
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

    @Column(name = "house_id")
    public Integer getHouseId() {
        return houseId;
    }

    public void setHouseId(Integer houseId) {
        this.houseId = houseId;
    }

    @Column(name = "avg_wight")
    public Float getAvgWight() {
        return avgWight;
    }

    public void setAvgWight(Float avgWight) {
        this.avgWight = avgWight;
    }

    @Column(name = "co2")
    public Float getCo2() {
        return co2;
    }

    public void setCo2(Float co2) {
        this.co2 = co2;
    }

    @Column(name = "nh3")
    public Float getNh3() {
        return nh3;
    }

    public void setNh3(Float nh3) {
        this.nh3 = nh3;
    }

    @Column(name = "temperature")
    public Float getTemperature() {
        return temperature;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    @Column(name = "humidity")
    public Float getHumidity() {
        return humidity;
    }

    public void setHumidity(Float humidity) {
        this.humidity = humidity;
    }

    @Column(name = "weather")
    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
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
