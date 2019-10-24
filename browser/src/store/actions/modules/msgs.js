import store from "@/store";
import utils from "@/utils";

// 漫游消息
export function onRoamingMsgs(obj) {
  let msgs = obj.msgs;
  store.commit("updateMsgs", msgs);
}

// 离线消息
export function onOfflineMsgs(obj) {
  let msgs = obj.msgs;
  store.commit("updateMsgs", msgs);
  //
  for (let msg of msgs) {
    if (msg.sessionId == store.state.currSessionId) {
      store.commit("updateCurrSessionMsgs", formatMsg(msg));
    }
  }
}

// 接收消息
export function onMsg(msg) {
  store.commit("putMsg", msg);
  if (msg.sessionId == store.state.currSessionId) {
    store.commit("updateCurrSessionMsgs", formatMsg(msg));
  }
}

// 获取历史消息(云端获取历史记录)
export function getHistoryMsgs({ state, commit }, obj) {
  const nim = state.nim;
  if (nim) {
    let { scene, to } = obj;
    let options = {
      scene,
      to,
      asc: true,
      limit: 10,
      done: function getHistoryMsgsDone(error, obj) {
        // 存储到当前会话消息列表
        if (!error) {
          // 清空当前会话消息
          state.currSessionMsgs = [];
          // console.log(obj);
          let msgs = obj.msgs;
          for (let msg of msgs) {
            store.commit("updateCurrSessionMsgs", formatMsg(msg));
          }
        }
      }
    };
    nim.getHistoryMsgs(options);
  }
}

// 发送普通消息
export function sendMsg({ state, commit }, obj) {
  const nim = state.nim;
  nim.sendText({
    scene: obj.scene,
    to: obj.to,
    text: obj.text,
    done: onSendMsgDone
  });
}

// 格式化消息
export function formatMsg(msg) {
  let accid = store.state.user.accid;
  let who;
  var item;
  if (msg.from == accid) {
    who = utils.whoType.SENDER;
  } else {
    who = utils.whoType.RECEIVER;
  }
  if (msg.type == "text") {
    item = {
      type: utils.msgType.MSG_TEXT,
      msg: msg.text,
      name: msg.from,
      portrait: "", // 头像url,暂未实现
      who: who,
      File: ""
    };
  } else if (msg.type == "image") {
    item = {
      type: utils.msgType.MSG_IMAGE,
      msg: "",
      name: msg.from,
      portrait: "", // 头像url,暂未实现
      who: who,
      File: msg.file.url
    };
  }
  return item;
}

// 发送消息结果回调函数
function onSendMsgDone(error, msg) {
  if (!error) {
    console.log("发送消息成功!");
    // 存储消息
    store.commit("putMsg", msg);
    if (msg.sessionId === store.state.currSessionId) {
      // 更新当前消息列表
      store.commit("updateCurrSessionMsgs", formatMsg(msg));
      console.log(formatMsg(msg));
    }
  }
}

// 发送文件消息 图片、视频
export function sendFileMsg({ state, commit }, obj) {
  const nim = state.nim;
  let { type, fileInput } = obj;
  if (!type && fileInput) {
    type = "file";
    if (/\.(png|jpg|bmp|jpeg|gif)$/i.test(fileInput.value)) {
      type = "image";
    } else if (/\.(mov|mp4|ogg|webm)$/i.test(fileInput.value)) {
      type = "video";
    }
  }
  const data = Object.assign(
    {
      type,
      uploadprogress: function() {},
      uploaderror: function() {
        fileInput.value = "";
        console.log("上传失败");
      },
      uploaddone: function(error, file) {
        console.log('上传' + (!error ? '成功' : '失败'), error, file);
        console.log(file.url);
        fileInput.value = "";
        // 显示消息
        

      },
      beforesend: function(msg) {},
      done: function(error, msg) {
        console.log("发送消息成功")
        onSendMsgDone(error, msg);
      }
    },
    obj
  );
  nim.sendFile(data);
}
