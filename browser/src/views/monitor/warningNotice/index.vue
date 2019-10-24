<template>
    <div style="height:90%">
        <div style="background: #d3dce6;text-align:center;">可选通知类型：</div>
        <div style=" margin-left:20px; margin-top:20px;text-align:center">
            <el-select v-model.trim="farmId" placeholder="请选择鸡场" @change="farmSelectValueChange" size="small">
                <el-option v-for="item in farms" :key="item.id" :label="item.name" :value="item.id"/>
            </el-select>
            <el-checkbox style="margin-left:30px" v-model="sms.configValue">短信通知</el-checkbox>
            <el-checkbox v-model="wechat.configValue" >微信通知</el-checkbox>
        </div>
        <div style="background:  #e5e9f2;text-align:center;margin-top:20px;">
            <el-button style="background-color: #3ebf7c" type="success" size="small" @click="saveConfig" class="btn-save"
                       clicked="0">保存
            </el-button>
        </div>
    </div>

</template>

<script>
    export default {
        name: "warningNotice",
        data() {
            return {
                farms: '',
                farmId: '',
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
                }
            }
        },
        created() {
            this.fetchFarms();
        },
        methods: {
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
            farmSelectValueChange() {
                let params = new URLSearchParams();
                params.append("farmId", this.farmId);
                console.log(this.farmId);
                this.$http({
                    method: "post",
                    url: "/web-server/monitor/warning/query.json?action=query",
                    data: params
                }).then(res => {
                    var configs = res.data;
                    var isHas = 0;
                    for (var i = 0; i < configs.length; i++) {
                        var value = configs[i];
                        if (value.configCode == "sms") {
                            isHas++;
                            this.sms.configValue = value.configValue == "true" ? true : false;
                            this.sms.id = value.id;
                            this.sms.createTime = new Date(value.createTime);
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