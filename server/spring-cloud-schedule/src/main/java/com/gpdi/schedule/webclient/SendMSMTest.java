package com.gpdi.schedule.webclient;

import com.gpdi.schedule.utils.MD5Util;
import sun.misc.BASE64Encoder;

public class SendMSMTest {
    public static void main(String[] args) throws Exception {
       // AuthCommon service = new AuthCommonLocator();
        RegisterServiceLocator registerServiceLocator = new RegisterServiceLocator();

        RegisterSoapBindingStub registerSoapBindingStub =  (RegisterSoapBindingStub) registerServiceLocator.getRegister();
        SendSMSServiceLocator sendSMSServiceLocator = new SendSMSServiceLocator();
        SendSMSSoapBindingStub sendSMSSoapBindingStub = (SendSMSSoapBindingStub) sendSMSServiceLocator.getSendSMS();
        //SendSMSSoapBindingStub sendSMSSoapBindingStub = new SendSMSSoapBindingStub();
        String rand = registerSoapBindingStub.getRandom();
        String pwd = rand+"Abc@654321"+"Abc@654321";
        pwd = MD5Util.MD5(pwd);

        String re = registerSoapBindingStub.setCallBackAddr("075783105458", pwd, rand, "http://61.144.66.11:8808/zd/services/recvSMS?wsdl");
        System.out.println(re);
        String [] calls = {"13025625482"};
        String s = "广东省电信规划设计院有限公司";
        //String s1 = new String(s.getBytes(),"GBK");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        String cont = base64Encoder.encode( s.getBytes("GBK"));
        String callre = sendSMSSoapBindingStub.sendSMS("075783105458",pwd,rand,calls,"1",cont,1234,re);
        System.out.println(callre);

    }
}
