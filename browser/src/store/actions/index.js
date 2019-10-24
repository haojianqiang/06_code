// Action提交的是mutations,而不是直接变更状态
// Action可以包含任意异步操作

import { initNimSDK } from "./modules/initNimSDK";
import { sendMsg,getHistoryMsgs,sendFileMsg } from "./modules/msgs";

export default {
  setCurModule({ commit }, moduleId) {
    console.log("actions-->setCurModule", moduleId);
    commit("setCurModule", moduleId);
  },

  // NIM
  initNimSDK,
  // 发送文本消息
  sendMsg,
  // 获取消息记录
  getHistoryMsgs,
  // 发送文件消息
  sendFileMsg
};
