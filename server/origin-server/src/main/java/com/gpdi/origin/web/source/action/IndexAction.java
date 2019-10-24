package com.gpdi.origin.web.source.action;

import com.gpdi.origin.service.source.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import pub.functions.StrFuncs;
import pub.spring.web.mvc.ActionResult;

/**
 * @author zzj
 * @date 2019/8/8 0008
 */
@Slf4j
@RestController
public class IndexAction {

    /**
     * 溯源
     *
     * @param originCode
     * @return
     */
    @RequestMapping
    public ActionResult traceOrigin(String originCode) {
        if (StrFuncs.isEmpty(originCode)) {
            return ActionResult.fail("参数异常：溯源码为空.");
        }
        return ActionResult.okWithData(indexService.traceOrigin(originCode));
    }

    /**
     * 溯源码生成相应的小程序二维码
     *
     * @param originCode
     * @return
     */
   /* @RequestMapping
    public ActionResult getWxQrCode(String originCode) {
        if (StrFuncs.isEmpty(originCode)) {
            return ActionResult.fail("参数异常：溯源码为空.");
        }
        // 获取小程序 token
        String accessToken = stringRedisTemplate.opsForValue().get(CommonRedisKeyConstants.WX_ACCESS_TOKEN_REDIS_KEY);
        if (StrFuncs.isEmpty(accessToken)) {
            // http://spring-cloud-zuul/wx/mini_program/token
            String httpToZuulForWxTokenUrl = String.format("http://%s/wx/mini_program.json?action=token", ServiceNameConstants.SPRING_CLOUD_ZUUL);
            try {
                ActionResult retResult = restTemplate.getForObject(httpToZuulForWxTokenUrl, ActionResult.class);
                if (!retResult.isOk()) {
                    return ActionResult.fail("小程序token已过期，且无法生成新的token");
                }
                accessToken = stringRedisTemplate.opsForValue().get(CommonRedisKeyConstants.WX_ACCESS_TOKEN_REDIS_KEY);
            } catch (RestClientException e) {
                e.printStackTrace();
                return ActionResult.fail("小程序token已过期，且生成新的token时出现异常：" + e.toString());
            }
        }
        // 使用 token 发送请求生成二维码
        String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken;
        // 设置参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("scene", originCode);
        String params = JSONUtil.parseFromMap(paramMap).toString();
        // 发送 post 请求并接收返回的二进制流
        byte[] resultBytes = HttpUtil.postForBytesFromWxHttpsInt(url, params);
        // 根据返回的流大小判断是否成功生成二维码
        log.info("receive result bytes length : " + resultBytes.length);
        if (resultBytes.length < 1024) {
            // 小于1024认为生成失败，则返回错误信息
            JSON json = JSONUtil.parse(new String(resultBytes));
            // 返回错误信息
            String errMsg = String.valueOf(json.getByPath("errmsg"));
            log.info(errMsg);
            return ActionResult.fail(errMsg);
        } else {
            String filePath = StrUtil.format("D:/wxQrCode/{}/{}.png", DateUtil.format(DateUtil.date(), "yyyy_mm_dd"), originCode);
            File qrCodeFile = new File(filePath);
            if (!qrCodeFile.exists()) {
                qrCodeFile.getParentFile().mkdirs();
            }
            qrCodeFile = new File(filePath);
            try {
                OutputStream os = new FileOutputStream(qrCodeFile);
                os.write(resultBytes, 0, resultBytes.length);
                os.flush();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 否则成功，保存二维码二进制流
            return ActionResult.okWithData(resultBytes);
        }
    }*/


    @Autowired
    private IndexService indexService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
}
