package com.gpdi.schedule.task;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.gpdi.schedule.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pub.constant.CommonRedisKeyConstants;
import pub.dao.GeneralDao;
import pub.functions.StrFuncs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * 微信小程序
 *
 * @author zzj
 * @date 2019/8/14 0014
 */
@Component
@Slf4j
public class WxMiniProgramTask extends Task {
    public final static String WX_ACCESS_TOKEN_API_URI = "https://api.weixin.qq.com/cgi-bin/token?";
    public final static String WX_QR_CODE_UNLIMIT_URI = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?";
    public final static String WX_ACCESS_TOKEN_API_GRANT_TYPE = "client_credential";
    public final static String WX_QR_CODE_PARENT_FOLDER = "/home/wisdom_farm/data/wx/qr_code";
    @Value("${wx.mini.program.appid}")
    private String appId;
    @Value("${wx.mini.program.secret}")
    private String secret;

    /**
     * 溯源码生成相应的小程序二维码
     *
     * @return
     */
    public void execute() {
        // 获取小程序 token
        String accessToken = stringRedisTemplate.opsForValue().get(CommonRedisKeyConstants.WX_ACCESS_TOKEN_REDIS_KEY);
        if (StrFuncs.isEmpty(accessToken)) {
            log.info("wx miniProgram token is expired,try to refresh..");
            if (tokenRefresh()) {
                accessToken = stringRedisTemplate.opsForValue().get(CommonRedisKeyConstants.WX_ACCESS_TOKEN_REDIS_KEY);
                log.info("wx miniProgram token refreshed successfully.");
            }
            if (StrFuncs.isEmpty(accessToken)) {
                return;
            }
        }
        // 查询需要生成二维码的溯源码
        List<String> newOriginCodeList = generalDao.queryList(String.class, "select origin_code from ce_chicks_ccs where qr_code is null order by create_time limit 1000");
        log.info("there are {} originCodes to be generated...", newOriginCodeList.size());
        if (newOriginCodeList.size() == 0) {
            return;
        }
        // 批量更新语句
        String batchUpdateSql = "update ce_chicks_ccs set qr_code = :qr_code where origin_code = :origin_code";
        // 批量更新参数
        List<Map<String, Object>> batchParamsList = new ArrayList<>();
        // 遍历生成二维码，并为溯源码保存图片路径
        for (String newOriginCode : newOriginCodeList) {
            // token
            String tokenParam = StrUtil.format("access_token={}", accessToken);
            String url = WX_QR_CODE_UNLIMIT_URI + tokenParam;
            // 传参溯源码
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("scene", newOriginCode);
            String params = JSONUtil.parseFromMap(paramMap).toString();

            // 发送 post 请求并接收返回的二进制流,根据流大小判断是否成功生成二维码
            byte[] resultBytes = HttpUtil.postForBytesFromWxHttpsInt(url, params);
            log.info("receive result bytes length : {}", resultBytes.length);
            if (resultBytes.length < 1024) {
                // 小于1024认为生成失败，则返回错误信息
                JSON json = JSONUtil.parse(new String(resultBytes));
                String errMsg = String.valueOf(json.getByPath("errmsg"));
                log.info(errMsg);
            } else {
                // 保存二维码图片
                String filePath = StrUtil.format("{}/{}/{}.png", WX_QR_CODE_PARENT_FOLDER, DateUtil.format(DateUtil.date(), "YYYY_MM_dd"), newOriginCode);
                if (saveQrCodePng(filePath, resultBytes)) {
                    batchParamsList.add(new HashMap() {{
                        put("qr_code", filePath);
                        put("origin_code", newOriginCode);
                    }});
                }
            }
        }

        if (batchParamsList.size() > 0) {
            // 更新二维码路径
            generalDao.executeBatch(batchUpdateSql, batchParamsList);
        }
    }

    private boolean saveQrCodePng(String filePath, byte[] resultBytes) {
        boolean retFlag = true;
        try {
            File qrCodeFile = new File(filePath);
            if (!qrCodeFile.exists()) {
                qrCodeFile.getParentFile().mkdirs();
            }
            qrCodeFile = new File(filePath);
            OutputStream os = new FileOutputStream(qrCodeFile);
            os.write(resultBytes, 0, resultBytes.length);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
            retFlag = false;
        }
        return retFlag;
    }


    /**
     * 刷新小程序token
     *
     * @return
     */
    @Scheduled(cron = "0 */30 * * * ?")
    public boolean tokenRefresh() {
        boolean retFlag = true;
        try {
            // 获取token过期时间
            long expireTime = stringRedisTemplate.getExpire(CommonRedisKeyConstants.WX_ACCESS_TOKEN_REDIS_KEY, TimeUnit.SECONDS);
            // 小于五分钟则重新获取token
            if (expireTime < 60 * 5) {
                // 拼接url
                String params = StrUtil.format("grant_type={}&appid={}&secret={}", WX_ACCESS_TOKEN_API_GRANT_TYPE, appId, secret);
                String url = WX_ACCESS_TOKEN_API_URI + params;
                // get方式请求
                String httpGetResult = cn.hutool.http.HttpUtil.get(url);
                log.info(httpGetResult);
                JSON resultToJson = JSONUtil.parse(httpGetResult);
                // 获取token与过期时间
                String token = (String) resultToJson.getByPath("access_token");
                int expires = (int) resultToJson.getByPath("expires_in");
                log.info(token);
                // 存储redis共享token，同时设置过期时间
                stringRedisTemplate.opsForValue().set(CommonRedisKeyConstants.WX_ACCESS_TOKEN_REDIS_KEY, token, expires, TimeUnit.SECONDS);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            retFlag = false;
        }

        return retFlag;
    }

    @Autowired
    private GeneralDao generalDao;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
}
