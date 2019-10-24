<template>
  <div>
    <el-container style="height: 100%;min-height:100vh;" direction="vertical">
      <el-container>
        <!-- 会话面板 -->
        <el-aside width="200px">
          <div class="session-panel">
            <ul>
              <li class="session-item acctive-item">
                <div class="item-avatar">
                  <img src="../../../assets/image/avatar.png">
                </div>
                <div class="item-name">咨询者1</div>
              </li>

              <li class="session-item">
                <div class="item-avatar">
                  <img src="../../../assets/image/avatar.png">
                </div>
                <div class="item-name">咨询者2</div>
              </li>
            </ul>
          </div>
        </el-aside>
        <!-- 聊天面板 -->
        <el-main>
          <div class="chat-panel">
            <div class="chat-header">咨询者1</div>
            <div class="chat-content" ref="chatContent">
              <template v-for="item in msglist">
                <template v-if="item.who == 0">
                  <div class="chat-sender">
                    <div>
                      <img src="../../../assets/image/avatar.png">
                    </div>
                    <!-- <div>{{item.name}}</div> -->
                    <div class="chat-item">
                      <div class="chat-right-triangle"></div>
                      <template v-if="item.type == 0">
                        <span>{{item.msg}}</span>
                      </template>
                      <template v-else-if="item.type == 1">
                        <span>
                          <img v-bind:src="item.File">
                        </span>
                      </template>
                    </div>
                  </div>
                </template>
                <template v-else>
                  <div class="chat-receiver">
                    <div>
                      <img src="../../../assets/image/avatar.png">
                    </div>
                    <!-- <div>{{item.name}}</div> -->
                    <div class="chat-item">
                      <div class="chat-left-triangle"></div>
                      <!-- <span>{{item.msg}}</span> -->
                      <template v-if="item.type == 0">
                        <span>{{item.msg}}</span>
                      </template>
                      <template v-else-if="item.type == 1">
                        <span>
                          <img v-bind:src="item.File">
                        </span>
                      </template>
                    </div>
                  </div>
                </template>
              </template>
            </div>
            <div class="chat-input-panel">
              <div class="chat-tool">
                <ul>
                  <li @click="selectImage()">
                    <input type="file" ref="imgInput" style="display:none" @change="previewImage">
                    <i class="el-icon-picture-outline"></i>
                  </li>
                  <li>
                    <i class="el-icon-video-camera"></i>
                  </li>
                  <li>
                    <i class="el-icon-folder"></i>
                  </li>
                </ul>
              </div>
              <div class="chat-input">
                <div v-if="img != null" class="chat-input-img">
                  <i @click="delImage" class="el-icon-error" style="cursor:pointer"></i>
                  <el-image
                    style="height:100px;width:100px"
                    :src="imgUrl"
                    :preview-src-list="imgUrlList"
                    :fit="fit"
                  ></el-image>
                </div>
                <textarea v-if="img == null" v-model="text"></textarea>
              </div>
              <div class="chat-button">
                <el-button size="mini" @click="send()">发送</el-button>
                <el-button size="mini" @click="history()">历史</el-button>
              </div>
            </div>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>
<script>
export default {
  name: "IM",
  data() {
    return {
      fit: "contain",
      text: "",
      img: null,
      imgUrl: "",
      imgUrlList: [],
      ipt: null,
      data: [
        // 测试数据
        {
          type: 0, // 消息类型
          msg: "Hello 专家!",
          name: "游客",
          portrait: "../../images/qq.png", // 头像
          who: 1,
          File: "" // 文件：图片、音频、视频
        },
        {
          type: 1, // 信息类型
          msg: "",
          name: "专家",
          portrait: "../../images/weixin.png", // 头像
          who: 0,
          File:
            "https://nim-nosdn.netease.im/MTY2OTI3NzI=/bmltYV8xMjk3Mzc3Mjk1Ml8xNTY1ODUzOTI1MzUyX2E4NmE0ZmJjLWJhZTgtNGYwNC05YjMzLTU0ZjJhYTRiNmU0ZQ==?createTime=1565853941890" // 文件：图片、音频、视频
        },
        {
          type: 0, // 信息类型
          msg: "Hello 游客!",
          name: "专家",
          portrait: "../../images/weixin.png", // 头像
          who: 0,
          File: "" // 文件：图片、音频、视频
        }
      ],
      info: {
        scene: "p2p",
        to: "user"
      }
    };
  },
  methods: {
    send() {
      if (this.img == null) {
        this.sendText();
      } else {
        this.sendImage();
      }
    },
    sendText() {
      let val = this.text;
      if (val == "") {
        return;
      }
      var obj = {
        scene: this.info.scene,
        to: this.info.to,
        text: val
      };
      this.$store.dispatch("sendMsg", obj);
      this.text = "";
    },
    selectImage() {
      let ipt = this.$refs.imgInput;
      ipt.click();
    },
    previewImage() {
      let ipt = this.$refs.imgInput;
      let file = ipt.files[0];
      if (file.type.match(/image.*/)) {
        this.ipt = ipt;
        this.img = file;
        this.imgUrl = URL.createObjectURL(file);
        this.imgUrlList = [];
        this.imgUrlList.push(this.imgUrl);
      } else {
        this.$refs.imgInput.value = "";
        this.img = null;
        this.ipt = null;
        this.imgUrlList = [];
      }
    },
    sendImage() {
      this.$store.dispatch("sendFileMsg", {
        scene: this.scene,
        to: this.to,
        fileInput: this.ipt
      });
    },
    delImage() {
      this.img = null;
      this.imgUrl = "";
      this.imgUrlList = [];
    },
    initNim() {
      // 应放置到登陆成功的函数中
      let loginInfo = this.$store.state.user;
      this.$store.dispatch("initNimSDK", loginInfo);
    },
    history() {
      let obj = {
        scene: "p2p",
        to: "user"
      };
      this.$store.dispatch("getHistoryMsgs", obj);
    },
    scrollToBottom() {
      let chatContent = this.$refs.chatContent;
      chatContent.scrollTop = chatContent.scrollHeight;
    }
  },
  mounted() {
    this.initNim();
  },
  computed: {
    msglist() {
      let msgs = this.$store.state.currSessionMsgs;
      return msgs;
    }
  },
  updated() {
    this.scrollToBottom();
  }
};
</script>
<style scoped>
/* 会话列表 */
.session-panel {
  width: 100%;
  height: 100%;
  background: #3ebf7c;
  overflow: auto;
  padding-top: 10px;
}
.session-panel .session-item {
  padding: 4px;
  background: #dcdfe6;
  margin-bottom: 8px;
}
.session-panel .acctive-item {
  background: #ffffff;
}
.session-item .item-avatar {
  display: inline-block;
}
.item-avatar img {
  width: 40px;
  height: 40px;
}
.session-item .item-name {
  padding-left: 10px;
  display: inline-block;
}

/* 内容面板 */
.chat-panel {
  width: 100%;
  display: flex;
  flex-direction: column;
  height: 100vh;
  padding-bottom: 30px;
}
.chat-panel .chat-content {
  flex: 1;
  border: 1px solid #dcdfe6;
  overflow: auto;
  padding: 10px;
}
.chat-item img {
  width: 200px;
}

/* 接收方气泡 */
.chat-content .chat-receiver {
  clear: both;
  font-size: 70%;
  padding-top: 4px;
}
.chat-receiver div:nth-of-type(1) {
  float: left;
}
/* .chat-receiver div:nth-of-type(2) {
  margin: 0 50px 2px 50px;
  padding: 0px;
  color: #848484;
  font-size: 70%;
  text-align: left;
} */
.chat-receiver div:nth-of-type(2) {
  /* background-color: #e4e7ed; */
  background-color: #dcdfe6;
  margin: 0 10px 10px 10px;
  padding: 10px 10px 10px 10px;
  border-radius: 7px;
  text-indent: -12px;
  float: left;
}
/* 发送方气泡 */
.chat-content .chat-sender {
  clear: both;
  font-size: 70%;
  /* 包含浮动子元素 */
  overflow: hidden;
}
/* 发送方头像 */
.chat-sender div:nth-of-type(1) {
  float: right;
}
/* .chat-sender div:nth-of-type(2) {
  margin: 0px 50px 2px 50px;
  padding: 0px;
  color: #848484;
  font-size: 70%;
  text-align: right;
} */
.chat-sender div:nth-of-type(2) {
  float: right;
  background-color: #b2e281;
  margin: 0px 10px 10px 50px;
  padding: 10px 10px 10px 10px;
  border-radius: 7px;
}

/* 气泡三角形 */
.chat-left-triangle {
  height: 0px;
  width: 0px;
  border-width: 6px;
  border-style: solid;
  /* border-color: transparent #e4e7ed transparent transparent; */
  border-color: transparent #dcdfe6 transparent transparent;
  position: relative;
  left: -22px;
  top: 3px;
}
.chat-right-triangle {
  height: 0px;
  width: 0px;
  border-width: 6px;
  border-style: solid;
  border-color: transparent transparent transparent #b2e281;
  position: relative;
  right: -22px;
  top: 3px;
  /* position: absolute;
  right: 10px; */
}
.chat-receiver div:first-child img,
.chat-sender div:first-child img {
  width: 40px;
  height: 40px;
}

/* 输入面板 */
.chat-panel .chat-input-panel {
  height: 150px;
  position: relative;
  border: 1px solid #dcdfe6;
}
.chat-input-panel .chat-tool {
  height: 36px;
  background: #ffffff;
}
.chat-tool ul {
  margin: 0;
}
.chat-tool ul li {
  display: inline-block;
  padding: 6px;
}
.chat-input-panel .chat-input {
  width: 100%;
}
.chat-input textarea {
  width: 100%;
  height: 82px;
  resize: none;
  padding: 5px 15px;
  border: none;
}
.chat-input .chat-input-img {
  display: inline-block;
  height: 100px;
  margin-left: 10px;
  position: relative;
}
.chat-input .chat-input-img i {
  position: absolute;
  right: -6px;
  top: -6px;
  z-index: 1;
}
.chat-input .chat-input-img img {
  height: 100%;
}
.chat-input-panel .chat-button {
  position: absolute;
  right: 10px;
  bottom: 10px;
}
</style>


