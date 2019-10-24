package com.gpdi.schedule.entity.sms;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "sys_sms")
public class SMSInfo {
    private Integer id;
    private String smsCont;
    private Integer msgId;
    private String caller;
    private String ucNum;
    private Date createTime;
    private Date sendTime;
    private String result;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "sms_cont")
    public String getSmsCont() {
        return smsCont;
    }

    public void setSmsCont(String smsCont) {
        this.smsCont = smsCont;
    }


    @Column(name = "msg_id")
    public Integer getMsgId() {
        return msgId;
    }

    public void setMsgId(Integer msgId) {
        this.msgId = msgId;
    }

    @Column(name = "caller")
    public String getCaller() {
        return caller;
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }

    @Column(name = "ucnum")
    public String getUcNum() {
        return ucNum;
    }

    public void setUcNum(String ucNum) {
        this.ucNum = ucNum;
    }

    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "send_time")
    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    @Column(name = "result")
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
