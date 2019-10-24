// 初始化、重新连接SDK

import config from "@/config";
import utils from "@/utils";

import {onSessions,onUpdateSession} from './session';
import {onRoamingMsgs,onOfflineMsgs,onMsg} from './msgs';

// const SDK = require("@/sdk/NIM_Web_SDK_v6.7.0");
export function initNimSDK({ state, commit, dispatch }, loginInfo) {
  if (state.nim) {
    state.nim.disconnect();
  }
  // 初始化SDK
  window.nim = state.nim = SDK.NIM.getInstance({
    appKey: config.appKey,
    account: loginInfo.accid,
    token: loginInfo.token,
    db: config.db, // 是否支持数据库
    onconnect: function onConnect() {
      console.log("SDK连接成功!");
    },
    onerror: function onError(event) {
      console.log("网络连接状态异常!");
    },
    ondisconnect: function onDisconnect(error) {
      switch (error.code) {
        case 302:
          console.log("账号或密码错误!");
          break;
        case "kicked":
          let map = {
            PC: "电脑版",
            Web: "网页版",
            Android: "手机版",
            iOS: "手机版",
            WindowsPhone: "手机版"
          };
          let str = error.from;
          let errorMsg = "你的账号于"+utils.formatDate(new Date())+"被"+(map[str] || '其他端')+"提出下线,请确定账号信息安全";
          console.log(errorMsg);
          break;
        default:
          break;
      }
    },
    // 会话
    onsessions:function(sessions){
      console.log("收到会话列表")
      console.log(sessions);
    },
    // onsessions:onSessions,
    onupdatesession:onUpdateSession,
    // 消息
    onroamingmsgs:onRoamingMsgs,
    onofflinemsgs:onOfflineMsgs,
    onmsg:onMsg
  });
}
