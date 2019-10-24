package com.gpdi.schedule.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import pub.spring.web.mvc.ActionResult;
import java.io.IOException;

/**
 * Json工具类(基于Spring Boot Json)
 */
public class JsonUtils {
    /**
     * json转换成对象
     * @param obj 对象
     * @param jsonStr json字符串
     * @return
     * @throws IOException
     */
    public static Object jsonToObject(Object obj,String jsonStr) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        return obj = mapper.readValue(jsonStr,obj.getClass());
    }

    /**
     * 对象转换成Json
     * @param obj 对象
     * @return
     */
    public static String objToJson(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }

    public static void main(String[] args) throws IOException{
        String jsonStr = objToJson(ActionResult.error("Json测试"));
        ActionResult result = (ActionResult) jsonToObject(new ActionResult(),jsonStr);
    }
}
