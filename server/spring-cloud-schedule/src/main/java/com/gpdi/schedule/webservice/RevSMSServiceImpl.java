package com.gpdi.schedule.webservice;

import com.xxxx.schedule.dao.SMSEchoDao;
import com.xxxx.schedule.entity.sms.SMSEcho;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jws.WebService;
import java.util.Date;


/**
 * 与接口中指定的服务名称一致
 */
@WebService(serviceName = "revSMS",
        targetNamespace = "http://sms.xxxx.com",
        endpointInterface = "com.gpdi.schedule.webservice.RevSMSService"
)
@Component
public class RevSMSServiceImpl implements RevSMSService {

    @Override
    /**
     *
     * @param ucNum 发送方号码，用于第三方服务器定位发送客户端
     * @param cee 接收方号码
     * @param msgid 短消息编号，用于客户端匹配请求消息
     * @param res 回执的结果
     * @param recvt 短消时间
     */
    public void echoOfSendSMS(String ucNum, String cee, int msgid, int res, String recvt) {
        SMSEcho smsEcho = new SMSEcho();
        smsEcho.setUcNum(ucNum);
        smsEcho.setCee(cee);
        smsEcho.setMsgId(msgid);
        smsEcho.setRes(res);
        smsEcho.setRecvt(recvt);
        smsEcho.setCreateTime(new Date());
        sMSEchoDao.save(smsEcho);
    }

    @Autowired
    private SMSEchoDao sMSEchoDao;
}
