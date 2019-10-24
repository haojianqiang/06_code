package com.gpdi.origin.utils;

import cn.hutool.core.util.ArrayUtil;
import org.springframework.data.redis.util.ByteUtils;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author zzj
 * @date 2019/8/14 0014
 */
public class HttpUtil {

    /**
     * post请求微信https接口，返回二进制流
     *
     * @param uri    wx https 请求
     * @param params 字符串类型的json格式参数
     * @return
     */
    public static byte[] postForBytesFromWxHttpsInt(String uri, String params) {
        byte[] resultByteArr = null;
        try {
            URL url = new URL(uri);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
            printWriter.write(params);
            printWriter.flush();

            // 开始读取二进制流
            InputStream is = httpURLConnection.getInputStream();
            int len;
            byte[] arr = new byte[1024];
            while ((len = is.read(arr)) != -1) {
                if (resultByteArr == null) {
                    resultByteArr = new byte[len];
                    ArrayUtil.copy(arr, resultByteArr, len);
                } else {
                    if (len < arr.length) {
                        byte[] arr2 = new byte[len];
                        ArrayUtil.copy(arr, arr2, len);
                        resultByteArr = ByteUtils.concat(resultByteArr, arr2);
                    } else {
                        resultByteArr = ByteUtils.concat(resultByteArr, arr);
                    }
                }
            }
            is.close();
        } catch (Exception e) {
            resultByteArr = e.getCause().toString().getBytes();
            e.printStackTrace();
        }
        return resultByteArr;
    }
}
