package com.gpdi.schedule.task;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.gpdi.schedule.dao.SMSInfoDao;
import com.gpdi.schedule.entity.sms.SMSInfo;
import com.gpdi.schedule.utils.MD5Util;
import com.gpdi.schedule.webclient.RegisterServiceLocator;
import com.gpdi.schedule.webclient.RegisterSoapBindingStub;
import com.gpdi.schedule.webclient.SendSMSServiceLocator;
import com.gpdi.schedule.webclient.SendSMSSoapBindingStub;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pub.dao.GeneralDao;
import sun.misc.BASE64Encoder;

import java.util.Date;
import java.util.List;

/**
 * @author zzj
 * @date 2019/8/27 0027
 */
@Slf4j
@Component
public class SendSMSTask extends Task {

    @Value("#{'${sms.send.ucnames}'.split(',')}")
    private List<String> ucnames;
    @Value("#{'${sms.send.ucpws}'.split(',')}")
    private List<String> ucpws;
    @Value("${sms.rev.ws}")
    private String ws;


    /**
     * 定时发送短信
     *
     * @return
     */
    public void execute() {
        List<SMSInfo> smsInfoList = generalDao.queryList(SMSInfo.class, "select * from sys_sms where result is null order by create_time desc");
        if (smsInfoList.size() == 0) {
            return;
        }
        log.info("there are {} sms to be send.", smsInfoList.size());
        try {
            // 调用注册ws接口
            RegisterServiceLocator registerServiceLocator = new RegisterServiceLocator();
            RegisterSoapBindingStub registerSoapBindingStub = (RegisterSoapBindingStub) registerServiceLocator.getRegister();
            // 调用发短信ws接口
            SendSMSServiceLocator sendSMSServiceLocator = new SendSMSServiceLocator();
            SendSMSSoapBindingStub sendSMSSoapBindingStub = (SendSMSSoapBindingStub) sendSMSServiceLocator.getSendSMS();

            for (SMSInfo smsInfo : smsInfoList) {
                int random = RandomUtil.randomInt(0, ucnames.size());
                String ucname = ucnames.get(random);
                String ucpw = ucpws.get(random);
                // 拼接密码
                String rand = registerSoapBindingStub.getRandom();
                String pwd = MD5Util.MD5(rand + ucpw + ucpw);
                // 获取通道ID
                String connId = registerSoapBindingStub.setCallBackAddr(ucname, pwd, rand, ws);
                if (Double.parseDouble(connId) < 0) {
                    return;
                }
                // 发送短信
                int msgId = generalDao.queryValue(Integer.class, "select count(*) + 10000 from sys_sms where result is not null " +
                        "and date_format(create_time,'%Y-%m-%d') = ?", DateUtil.today());
                String smsCont = new BASE64Encoder().encode(smsInfo.getSmsCont().getBytes("GBK"));
                String result = sendSMSSoapBindingStub.sendSMS(ucname, pwd, rand, smsInfo.getCaller().split(","), "1", smsCont, msgId, connId);
                // 存储短信状态
                smsInfo.setMsgId(msgId);
                smsInfo.setUcNum(ucname);
                smsInfo.setSendTime(new Date());
                smsInfo.setResult(result);
                smsInfoDao.save(smsInfo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Autowired
    private SMSInfoDao smsInfoDao;
    @Autowired
    private GeneralDao generalDao;
}
