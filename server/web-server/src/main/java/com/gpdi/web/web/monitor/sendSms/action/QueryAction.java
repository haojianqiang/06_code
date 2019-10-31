package com.gpdi.web.web.monitor.sendSms.action;

import cn.hutool.core.util.RandomUtil;
import com.gpdi.web.utils.MD5Util;
import com.gpdi.web.webclient.RegisterServiceLocator;
import com.gpdi.web.webclient.RegisterSoapBindingStub;
import com.gpdi.web.webclient.SendSMSServiceLocator;
import com.gpdi.web.webclient.SendSMSSoapBindingStub;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pub.dao.GeneralDao;
import sun.misc.BASE64Encoder;

import java.util.List;

@RestController
public class QueryAction {

    @Value("#{'075783105448,075783105449'.split(',')}")
    private List<String> ucnames;
    @Value("#{'Ab123456,Ab123456'.split(',')}")
    private List<String> ucpws;
    @Value("http://172.16.76.15:8083/services/revSMS?wsdl")
    private String ws;

    @RequestMapping
    public void sendSms(String smsCont,String caller){
        try {
            // 调用注册ws接口
            RegisterServiceLocator registerServiceLocator = new RegisterServiceLocator();
            RegisterSoapBindingStub registerSoapBindingStub = (RegisterSoapBindingStub) registerServiceLocator.getRegister();
            // 调用发短信ws接口
            SendSMSServiceLocator sendSMSServiceLocator = new SendSMSServiceLocator();
            SendSMSSoapBindingStub sendSMSSoapBindingStub = (SendSMSSoapBindingStub) sendSMSServiceLocator.getSendSMS();

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
            List<String> configList = (List<String>) JSONArray.toCollection(JSONArray.fromObject(caller), List.class);
            String[] callee = configList.toArray(new String[configList.size()]);
            String smsContParam = new BASE64Encoder().encode(smsCont.getBytes("GBK"));
            sendSMSSoapBindingStub.sendSMS(ucname, pwd, rand,callee, "1", smsContParam, 1000, connId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Autowired
    private GeneralDao generalDao;
}
