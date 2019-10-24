package com.gpdi.schedule.webservice;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

public class RevSmsClientTest {

    public static void main(String[] args) {
        try {
            // 接口地址
            String address = "http://172.16.76.15:8083/services/revSMS?wsdl";
            // 代理工厂
            JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
            // 设置代理地址
            jaxWsProxyFactoryBean.setAddress(address);
            // 设置接口类型
            jaxWsProxyFactoryBean.setServiceClass(RevSMSService.class);
            // 创建一个代理接口实现
            RevSMSService us = (RevSMSService) jaxWsProxyFactoryBean.create();
            // 数据准备
            // 调用代理接口的方法调用并返回结果
            us.echoOfSendSMS("1", "2", 3, 2, "1");
//            System.out.println("返回结果:" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
