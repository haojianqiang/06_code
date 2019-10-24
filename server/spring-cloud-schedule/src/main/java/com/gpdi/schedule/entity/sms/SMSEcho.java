package com.gpdi.schedule.entity.sms;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "pm_sms_echo")
public class SMSEcho implements Serializable {

    private Integer id;
    private String ucNum;
    private String cee;
    private Integer msgId;
    private Integer res;
    private String recvt;
    private Date createTime;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "ucnum")
    public String getUcNum() {
        return ucNum;
    }

    public void setUcNum(String ucNum) {
        this.ucNum = ucNum;
    }

    @Column(name = "cee")
    public String getCee() {
        return cee;
    }

    public void setCee(String cee) {
        this.cee = cee;
    }

    @Column(name = "msg_id")
    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    @Column(name = "res")
    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    @Column(name = "recvt")
    public String getRecvt() {
        return recvt;
    }

    public void setRecvt(String recvt) {
        this.recvt = recvt;
    }

    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
