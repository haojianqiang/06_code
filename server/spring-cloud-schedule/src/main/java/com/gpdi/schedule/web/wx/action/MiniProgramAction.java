package com.gpdi.schedule.web.wx.action;

import com.gpdi.schedule.task.WxMiniProgramTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pub.spring.web.mvc.ActionResult;

/**
 * @author zzj
 * @date 2019/8/15 0015
 */
@RestController
public class MiniProgramAction {

    @Autowired
    private WxMiniProgramTask wxMiniProgramTask;

    /**
     * 重新获取微信小程序token
     * 生成二维码的服务发现token过期时需要远程调用此接口
     *
     * @return
     */
    @RequestMapping
    public ActionResult token() {
        boolean flag = wxMiniProgramTask.tokenRefresh();
        if (flag) {
            return ActionResult.ok();
        } else {
            return ActionResult.fail("get token failed.");
        }
    }
}
