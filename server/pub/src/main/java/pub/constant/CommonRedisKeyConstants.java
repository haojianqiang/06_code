package pub.constant;

/**
 * @author zzj
 * @date 2019/8/15 0015
 */

/**
 * redis 默认键名
 */
public interface CommonRedisKeyConstants {
    /**
     * 微信小程序 token
     */
    String WX_ACCESS_TOKEN_REDIS_KEY = "wx_mini_program_access_token";

    /**
     * 鸡舍日常信息最新记录
     */
    String PM_HOUSE_INFO_RECORD_NEWEST_REDIS_KEY = "pm_house_info_record:newest";

    /**
     * 鸡场环境最新记录
     */
    String CM_WEATHER_NEWEST_REDIS_KEY = "cm_weather:newest";

}
