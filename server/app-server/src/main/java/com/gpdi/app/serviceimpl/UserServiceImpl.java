package com.gpdi.app.serviceimpl;

import com.gpdi.app.data.UserData;
import com.gpdi.app.data.nim.NimConfig;
import com.gpdi.app.data.nim.NimResponse;
import com.gpdi.app.entity.User;
import com.gpdi.app.service.UserService;
import com.gpdi.app.utils.CheckSumBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import pub.dao.GeneralDao;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = false)
public class UserServiceImpl implements UserService{
    @Autowired
    private GeneralDao generalDao;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public UserData login(String account, String password) {
        UserData user = null;
        String sql = "select * from app_user where account = ?";
        List<UserData> result = generalDao.queryList(UserData.class,sql,account);
        if (result.size()>0 && result.get(0).getPassword().equals(password)){
            user = result.get(0);
            NimResponse response = refreshToken(user);
            if (response.getCode().equals("200")){
                user.setToken(response.getInfo().getToken());
            }
        }
        return user;
    }


    private NimResponse refreshToken(User user){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.toString());
        headers.set("AppKey",NimConfig.AppKey.getUrl());
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String nonce = "12345";
        String checkSum = CheckSumBuilder.getCheckSum(NimConfig.AppSecret.getUrl(),nonce,curTime);
        headers.set("Nonce",nonce);
        headers.set("CurTime",curTime);
        headers.set("CheckSum",checkSum);
        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("accid",user.getAccid());
        String url = NimConfig.refreshToken.getUrl();
        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(map,headers);
        ResponseEntity<NimResponse> response = restTemplate.postForEntity(url,request,NimResponse.class);
        return response.getBody();
    }
}
