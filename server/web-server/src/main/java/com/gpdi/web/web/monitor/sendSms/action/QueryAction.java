package com.gpdi.web.web.monitor.sendSms.action;


import com.gpdi.web.utils.SmsUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QueryAction {

    @RequestMapping
    public void sendSms(String smsCont,String caller){
        SmsUtil.sendSms(smsCont,caller);
    }

}
