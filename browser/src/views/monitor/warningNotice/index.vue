<template>
    <div style="height:90%">
        <div style="background: #d3dce6;text-align:center;">可选通知类型：</div>
        <div style=" margin-left:20px; margin-top:20px;text-align:center">
            <el-row type="flex" justify="center">
                <el-col :span="3">
                    <el-select v-model.trim="farmId" placeholder="请选择鸡场" @change="farmSelectValueChange" size="small">
                        <el-option v-for="item in farms" :key="item.id" :label="item.name" :value="item.id"/>
                    </el-select>
                </el-col>

                <el-col :span="3">
                    <el-row>
                        <el-checkbox v-model="sms.configValue">短信通知</el-checkbox>
                    </el-row>
                    <el-row>
                        <el-checkbox v-model="wechat.configValue" >微信通知</el-checkbox>
                    </el-row>
                </el-col>

                <el-col :span="12">
                    <el-row>
                        <el-select multiple v-model='selectedArray' @change='changeSelect' placeholder="短信通知人" size="small" style="width: 550px">
                            <el-checkbox v-model="checked" @change='selectAll' style="text-align: right; width: 100%; padding-right: 10px;">全选</el-checkbox>
                            <el-option v-for='(item, index) in phones' :key='index' :label='item.phone' :value='item.phone'></el-option>
                        </el-select>
                    </el-row>
                    <el-row>
                        <el-col :span="24" style="margin-top: 5px">
                            <el-input type="textarea" v-model.trim="temp.introduce" placeholder="短信内容编辑" ></el-input>
                        </el-col>
                    </el-row>
                </el-col>

                <el-col :span="2">
                    <el-row>
                        <el-button type="success" size="small" @click="handleCreate">新增号码</el-button>
                    </el-row>
                    <el-row style="margin-top: 15px;">
                        <el-button type="success" size="small" @click="sendSMS">发送短信</el-button>
                    </el-row>

                </el-col>
            </el-row>
        </div>
        <div style="background:  #e5e9f2;text-align:center;margin-top:20px;">
            <el-button style="background-color: #3ebf7c" type="success" size="small" @click="saveConfig" class="btn-save"
                       clicked="0">保存
            </el-button>
        </div>

        <!--弹出窗口：新增-->
        <el-dialog title="新增" :visible.sync="dialogCreateFormVisible" :modal-append-to-body='false'>
            <el-form :rules="rules" ref="dataForm" :model="temp" label-position="right" label-width="100px">

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="号码：" prop="phone">
                            <el-input v-model.trim="temp.phone" placeholder="请输入号码" style="width: 220px"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="联系人：" prop="name">
                            <el-input v-model.trim="temp.name" placeholder="请输入联系人" style="width: 220px"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

            </el-form>
            <div slot="footer" class="dialog-footer footer">
                <el-button class="button" type="primary" @click="createData">保存</el-button>
                <el-button @click="dialogCreateFormVisible = false">取消</el-button>
            </div>
        </el-dialog>
    </div>

</template>

<script>
    export default {
        name: "warningNotice",
        data() {
            return {
                checked: false,
                selectedArray: [],
                phones: '',
                farms: '',
                farmId: '',
                temp: {
                    name:'',
                    phone:'',
                    introduce:''
                },
                rules: {
                    name: [{ required: true, message: '必填', trigger: 'blur' }],
                    phone: [{
                        required: true,
                        pattern: /^1[034578]\d{9}$/,//正则表达式
                        message: '目前只支持中国大陆的手机号码',
                        trigger: 'blur'
                    }],
                },
                sms: {
                    id: '',
                    farmId: '',
                    configName: '短信通知',
                    configCode: 'sms',
                    configValue: '',
                    createTime: ''
                },
                wechat: {
                    id: '',
                    farmId: '',
                    configName: '微信通知',
                    configCode: 'wechat',
                    configValue: '',
                    createTime: ''
                },
                dialogCreateFormVisible: false,
            }
        },
        created() {
            this.fetchFarms();
            this.fetchPhone('');
        },
        methods: {
            //发送短信
            sendSMS(){
                let params = new URLSearchParams();
                var JsonData = JSON.stringify(this.selectedArray);
                params.append("smsCont", this.temp.introduce);
                params.append("caller",JsonData);
                this.$http({
                    method: "post",
                    url: "/web-server/monitor/sendSms/query.json?action=sendSms",
                    data: params
                }).then(res => {
                    this.$message.success("发送成功")
                }).catch(err => {
                    console.log(err);
                });

            },
            //清空对象所有属性
            resetTemp(temp) {
                for (let prop in temp) {
                    temp[prop] = '';
                }
                return temp;
            },
            //新增号码
            handleCreate() {
                this.resetTemp(this.temp)
                this.dialogCreateFormVisible = true
            },
            createData() {
                this.$refs['dataForm'].validate((valid) => {
                    if (!valid) return;

                    let params = new URLSearchParams();
                    params.append("name", this.temp.name);
                    params.append("phone", this.temp.phone);
                    this.$http({
                        method: "post",
                        url: "/web-server/monitor/housemonitor/query.json?action=savePhone",
                        data: params
                    }).then(res => {
                        this.dialogCreateFormVisible = false
                        this.$message.success("添加成功")
                        this.fetchPhone()
                    }).catch(err => {
                        console.log(err);
                    });
                })
            },
            //获取号码
            fetchPhone(farmId) {
                let params = new URLSearchParams();
                params.append("farmId", farmId);
                this.$http({
                    method: "post",
                    url: "/web-server/monitor/housemonitor/query.json?action=getPhoneAndId",
                    data: params
                }).then(res => {
                    this.phones = res.data.data;
                }).catch(err => {
                    console.log(err);
                });
            },
            //多选框全选事件
            selectAll() {
                this.selectedArray = []
                if (this.checked) {
                    this.phones.map((item) => {
                        this.selectedArray.push(item.phone)
                    })
                } else {
                    this.selectedArray = []
                }
            },
            //多选框改变触发事件
            changeSelect(val) {
                if (val.length === this.phones.length) {
                    this.checked = true
                } else {
                    this.checked = false
                }
            },
            //获取鸡场列表，包含名称及id
            fetchFarms() {
                this.$http({
                    method: "post",
                    url: "/web-server/monitor/housemonitor/query.json?action=getFarmNameAndId",
                }).then(res => {
                    this.farms = res.data.data;
                }).catch(err => {
                    console.log(err);
                });
            },
            //鸡场下拉框变化事件
            async farmSelectValueChange() {
                let params = new URLSearchParams();
                params.append("farmId", this.farmId);

                let param = new URLSearchParams();
                param.append("farmId", this.farmId);
                //同步执行
                await this.$http({
                        method: "post",
                        url: "/web-server/monitor/housemonitor/query.json?action=getPhoneAndId",
                        data: param
                    }).then(res => {
                        this.phones = res.data.data;
                    }).catch(err => {
                        console.log(err);
                    });

                this.$http({
                    method: "post",
                    url: "/web-server/monitor/warning/query.json?action=query",
                    data: params
                }).then(res => {
                    var configs = res.data;
                    var isHas = 0;
                    this.selectedArray = [];
                    this.temp.introduce = '';
                    for (var i = 0; i < configs.length; i++) {
                        var value = configs[i];
                        if (value.configCode == "sms") {
                            isHas++;
                            this.sms.configValue = value.configValue == "true" ? true : false;
                            this.sms.id = value.id;
                            this.sms.createTime = new Date(value.createTime);
                            //根据后端传过来的所有单一phones对象，如果它们的farm_id与当前farmId相同
                            //就显示号码和短信内容
                            for (let j = 0; j < this.phones.length; j++) {
                                if (this.phones[j].farmId==this.farmId){
                                    this.temp.introduce=this.phones[j].introduce;
                                    this.selectedArray.push(this.phones[j].phone);
                                }
                            }
                        } else if (value.configCode == "wechat") {
                            isHas++;
                            this.wechat.configValue = value.configValue == "true" ? true : false;
                            this.wechat.id = value.id;
                            this.wechat.createTime = new Date(value.createTime);
                        }
                    }
                    //没有通知类型这个配置信息
                    if (configs.length == 0 || isHas == 0) {
                        this.sms.configValue = false;
                        this.wechat.configValue = false;
                        this.sms.id = null;
                        this.wechat.id = null
                    }
                }).catch(err => {
                    console.log(err);
                });
            },
            //保存
            saveConfig() {
                //如果新增-保存按钮的属性值为0,则放行
                var btnSave = $(".btn-save");
                if (btnSave.attr("clicked") !== "0") {
                    return;
                }
                //将按钮的属性值赋值为1,防止重复提交
                btnSave.attr("clicked", "1");

                var warnIngList = new Array();
                warnIngList.push(this.sms);
                warnIngList.push(this.wechat);
                var JsonData = JSON.stringify(warnIngList);
                let params = new URLSearchParams();
                var phones = JSON.stringify(this.selectedArray);
                params.append("smsCont", this.temp.introduce);//短信内容
                params.append("caller",phones);//号码
                params.append("pmWarningConfigs", JsonData);
                params.append("farmId", this.farmId);
                this.$http({
                    method: "post",
                    url: "/web-server/monitor/warning/query.json?action=save",
                    data: params
                }).then(res => {
                    this.$message.success("保存成功")
                    if(this.sms.id == null || this.sms.id==''){
                      this.farmSelectValueChange();
                    }
                }).catch(err => {
                    console.log(err);
                });

                //延迟执行
                setTimeout(function () {
                    btnSave.attr("clicked", "0");
                }, 2000);
            }
        }
    }
</script>

<style scoped>

</style>