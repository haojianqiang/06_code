<template>
    <!-- 免费咨询 -->
    <div class="container">
        <!-- 头部信息 -->
        <div class="divClass header">
            <img :src="expert.img">
            <span>{{ expert.name }}</span>
            <span class="right" @click="dialogVisible=true" style="cursor: pointer;">结束对话</span>
        </div>

        <!-- 聊天显示框 -->
        <div class="divClass chatBox">
            <!-- 左边信息 -->
            <div class="left clear">
                <div v-for="(msg1, index1) in msgLeft" :key="'msg1'+index1">
                    <div class="clear">
                        <img class="left" :src="msg1.img">
                        <p class="msgP left">{{ msg1.msg }}</p>
                    </div>
                </div>
            </div>

            <!-- 右边信息 -->
            <div class="right clear">
                <div v-for="(msg2, index2) in msgRight" :key="'msg2'+index2">
                    <div class="clear">
                        <img class="right" :src="msg2.img">
                        <p class="msgP right">{{ msg2.msg }}</p>
                    </div>
                </div>
            </div>
        </div>

        <!-- 聊天工具栏 -->
        <div class="tools">
            <a><i class="el-icon-picture-outline"></i></a>
            <a><i class="el-icon-folder-add"></i></a>
            <a><i class="el-icon-chat-dot-round"></i></a>
            <a><i class="el-icon-scissors"></i></a>
            <a><i class="el-icon-video-camera-solid"></i></a>
        </div>

        <!-- 聊天输入框 -->
        <div>
            <el-input class="elTextarea1" type="textarea" :rows="3" v-model="textarea" maxlength="100" show-word-limit placeholder="请在此输入您要咨询的问题..." ref="textareaVal"></el-input>
            <el-button class="sendBtn right" type="success" size="mini" @click="sendMsg">发送</el-button>
        </div>

        <!-- 评价模态框 -->
        <el-dialog :visible.sync="dialogVisible" :modal-append-to-body="false" :close-on-click-modal="false" width="500px">
            <div class="divDialog">
                <!-- 头部信息 -->
                <div>
                    <p  style="margin-top: 10px;"><span class="el-icon-s-data iconStyle"></span>您已结束对话，请对此次咨询作出评价吧！</p>
                </div>

                <hr>

                <!-- 总体评价 -->
                <div>
                    <p style="margin-left:100px;"><span>总体评价: </span><el-rate v-model="rateValue" show-text :texts="rateTexts"></el-rate></p>
                    <p><el-input class="elTextarea2" type="textarea" :rows="3" maxlength="70" v-model="dialogTextarea" show-word-limit placeholder="请输入评价内容..."></el-input></p>
                </div>

                <!-- 其他评价 -->
                <div>
                    <p>其他评价</p>
                    <div v-for="(item, index) in appraiseLabels" :key="index+'item.test'">
                        <el-button class="appraiseBtn left" size="mini" :type="item.type" :id="item.id" @click="appraiseChange(index)" round>{{ item.test }}  {{ item.count }}</el-button>
                    </div>
                </div>

                <hr>

                <!-- 关闭、返回、提交 -->
                <div>
                    <p class="center">
                        <el-button type="info" size="mini" round @click="colse">关闭页面</el-button>
                        <el-button type="success" size="mini" round @click="submit">提交评价</el-button>
                    </p>
                </div>
            </div>
            
            <div slot="footer"></div>
        </el-dialog>
    </div>
</template>

<script>
export default {
    data() {
        return {
            // 文本输入框
            textarea: '',
            // 评价展示boolean
            dialogVisible: false,
            // 评价内容
            dialogTextarea: '',
            // 评价星级
            rateValue: 0,
            // 评价辅助文字数组
            rateTexts: [ '不满意', '一般', '比较满意','满意', '非常满意' ],
            // 评价标签数组
            appraiseLabels: [
                {
                    id: 0,
                    test: '服务专业',
                    type: '',
                    boolean: false,
                    count: 10,
                },
                {
                    id: 1,
                    test: '态度和蔼',
                    type: '',
                    boolean: false,
                    count: 18,
                },
                {
                    id: 2,
                    test: '有帮助',
                    type: '',
                    boolean: false,
                    count: 33,
                },
                {
                    id: 3,
                    test: '讲解清晰',
                    type: '',
                    boolean: false,
                    count: 26,
                },
                {
                    id: 4,
                    test: '很棒',
                    type: '',
                    boolean: false,
                    count: 15,
                },

                {
                    id: 5,
                    test: '赞',
                    type: '',
                    boolean: false,
                    count: 68,
                },
                {
                    id: 6,
                    test: '厉害',
                    type: '',
                    boolean: false,
                    count: 2,
                },
                {
                    id: 7,
                    test: '很友好',
                    type: '',
                    boolean: false,
                    count: 8,
                },
            ],
            // 专家对象
            expert: {
                id: 1,
                name: '袁隆平',
                title: '首席咨询专家',
                college: '华南农业大学',
                sphere: '杂交水稻',
                img: 'http://img2.imgtn.bdimg.com/it/u=2148025029,3379649937&fm=26&gp=0.jpg',
            },
            // 左边信息
            msgLeft: [
                {
                    img: 'http://img2.imgtn.bdimg.com/it/u=2148025029,3379649937&fm=26&gp=0.jpg',
                    msg:'你好',
                },
            ],
            // 右边信息
            msgRight: [
                {
                    img: 'https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1198495970,3782994083&fm=26&gp=0.jpg',
                    msg:'袁专家，你好。请问...',
                },
            ],
        }
    },
    methods: {
        // 发送消息
        sendMsg() {
            // 判断文本输入框是否有内容
            if(!this.textarea) {
                this.$message({
                    type: 'error',
                    message: '请输入您要咨询的问题'
                })
                this.$refs.textareaVal.focus()
                return
            }
            this.msgRight.push({
                img: 'https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1198495970,3782994083&fm=26&gp=0.jpg',
                msg: this.textarea
            })
            this.textarea = ''
            this.$refs.textareaVal.focus()
        },
        // 评价标签
        appraiseChange(index) {
            // 判断是否已经点击评价标签
            if(this.appraiseLabels[index].boolean == false){
                this.appraiseLabels[index].boolean = true
                this.appraiseLabels[index].type = 'warning'
                this.appraiseLabels[index].count += 1
                return
            }
            this.appraiseLabels[index].boolean = false
            this.appraiseLabels[index].type = ''
            this.appraiseLabels[index].count -= 1
            return
        },
        // 提交评价
        submit() {
            if(!this.dialogTextarea){
                this.$message({
                    type: 'warning',
                    message:'请输入评价内容'
                });
                return
            }
            this.$message({
                type: 'success',
                message: '评价成功！',
            });
            setTimeout(() => {
                this.dialogVisible = false
                this.$store.commit("deleteTabs", this.activeIndex);
                this.$store.commit("setActiveIndex", this.openTab[this.openTab.length - 1].route);
                this.$router.push({ path: this.activeIndex });
            }, 1500)
        },
        // 关闭页面
        colse() {
            this.dialogVisible = false
            this.$store.commit("deleteTabs", this.activeIndex);
            this.$store.commit("setActiveIndex", this.openTab[this.openTab.length - 1].route);
            this.$router.push({ path: this.activeIndex });
        },
    },
    computed: {
        openTab() {
            return this.$store.state.openTab;
        },
        activeIndex: {
            get() {
                return this.$store.state.activeIndex;
            },
            set(val) {
                this.$store.commit("setActiveIndex", val);
            }
        },
    },
}
</script>

<style scoped>
.container {
    font:Helvetica Neue,Helvetica,PingFang SC,Tahoma,Arial,sans-serif;
    font-size: 14px;
    color: black;
    padding: 1% 12%;
    min-width: 800px;
}
.container img {
    width: 30px;
    height: 30px;
    margin: 0 10px;
    border-radius: 20px;
}
.divClass {
    padding:10px;
}
.header {
    background-color: #3ebf7c;
}
.right {
    float: right;
}
.left {
    float: left;
}
.center {
    text-align: center;
}
.clear {
    clear: both;
    margin: 5px 0;
}
.chatBox {
    padding: 20px 0;
    background-color: hsl(180, 29%, 91%);
    width: 100%;
    height: 400px;
    overflow: auto;
}
.msgP {
    padding: 5px 10px;
    background-color: #fff;
    border-radius: 5px;
    max-width: 350px;
    /* 单词换行 */
    word-break: break-all;
}
.tools {
    height: 25px;
}
.tools a {
    color: #999999;
    font-size: 16px;
    padding: 1px;
    margin: 5px 0 5px 15px;
}
.tools a:hover {
    font-weight: bold;
    cursor: pointer;
}
.elTextarea1 {
    width: 90%;
    display: inline-block;
}
.elTextarea2 {
    display: inline-block;
}
.sendBtn {
    width: 9.9%;
    height: 75px;
    color: #fff;
}
.el-scrollbar__wrap {
    overflow-x: hidden;
}
.divDialog {
    width: 450px;
    height: 365px;
    margin: 10px auto;
    padding: 5px 20px;
    border: 1px solid #003366;
}
.iconStyle {
    color: #ffffff;
    background-color: #e6a23c;
    margin-right: 10px;
    padding: 3px;
    border-radius: 2px;
}
.divDialog .el-button {
    /* 清除元素被选择时的蓝色外框 */ 
    outline:none;
}
.el-rate {
    display: inline-block;
    margin-left: 5px;
}
.appraiseBtn {
    width: 22%;
    margin: 5px;
}
</style>