// 内存数据状态
export default {
  // 基础
  openTab: [], // Tabs
  activeIndex: "/index", // 激活状态
  user: { accid: "root", token: "7a86d45eb923e2bef53e7148a50a0829" },
  // user: null,
  modules: null,
  moduleId: null,

  // NIM
  nim: null,
  // 消息列表
  msgs: {
    "p2p-user": []
  },
  // 当前会话
  currSessionId: "p2p-user",
  currSessionMsgs: [],
  // 会话
  sessionList: {}
};
