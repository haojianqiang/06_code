// 更改Vuex的store中的状态的唯一方法是提交mutations
// this.$store.commit(method,params)

export default {
  // 新增Tab页面,state不能省略
  addTabs(state, data) {
    state.openTab.push(data);
  },
  // 移除Tab页面
  deleteTabs(state, route) {
    let index = 0;
    for (let tab of state.openTab) {
      if (tab.route === route) {
        break;
      }
      index++;
    }
    state.openTab.splice(index, 1);
  },
  // 设置当前激活的Tab
  setActiveIndex(state, index) {
    state.activeIndex = index;
  },
  // 设置当前选中的ModuleId
  setCurModule(state, moduleId) {
    console.log("mutations-->setCurModule");
    state.moduleId = moduleId;
  },

  // NIM
  // 更新会话
  updateSessions(state, sessions) {
    const nim = state.nim;
    state.sessionList = nim.mergeSessions(state.sessionList, sessions);
    state.sessionlist.sort((a, b) => {
      return b.updateTime - a.updateTime;
    });
  },
  // 初始化、接收离线、漫游消息
  updateMsgs(state, msgs) {
    const nim = state.nim;
    let tempSessionMap = {};
    msgs.forEach(msg => {
      let sessionId = msg.sessionId;
      tempSessionMap[sessionId] = true;
      if (state.msgs[sessionId]) {
        state.msgs[sessionId] = [];
      }
      state.msgs[sessionId] = nim.mergeMsgs(state.msgs[sessionId], [msg]);
    });
  },
  // 接收消息
  putMsg(state, msg) {
    console.log(msg);
    let sessionId = msg.sessionId;
    console.log(state.msgs);
  
    if (state.msgs[sessionId]) {
      state.msgs[sessionId] = [];
    }
    // BUG:若属性不存在,则抛push is not undefined
    state.msgs[sessionId].push(msg);
    // state.msgs[sessionId].push(msg);
    // let tempMsgs = state.msgs[sessionId];
    // let lastMsgIndex = tempMsgs.length - 1;
    // if (tempMsgs.length === 0 || msg.time >= tempMsgs[lastMsgIndex].time) {
    //   tempMsgs.push(msg);
    // }
  },
  // 更新当前会话列表的聊天记录
  updateCurrSessionMsgs(state, obj) {
     state.currSessionMsgs.push(obj);
  }
};
