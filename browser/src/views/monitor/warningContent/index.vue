<template>
    <div style="height:90%">
        <div style="background: #d3dce6;text-align:center;">可选预警内容：</div>
        <div style=" margin-left:20px; margin-top:20px;text-align:center">
            <el-select v-model.trim="farmId" placeholder="请选择鸡场" @change="farmSelectValueChange" size="small">
                <el-option v-for="item in farms" :key="item.id" :label="item.name" :value="item.id"/>
            </el-select>
            <el-checkbox style="margin-left:30px" v-model="environment.configValue">鸡场环境</el-checkbox>
            <el-checkbox v-model="weather.configValue">极端天气</el-checkbox>
        </div>
        <div style="background:  #e5e9f2;text-align:center;margin-top:20px;">
            <el-button style="background-color: #3ebf7c" size="small" type="success" @click="saveConfig" class="btn-save"
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
                environment: {
                    id: '',
                    farmId: '',
                    configName: '鸡场环境',
                    configCode: 'environment',
                    configValue: '',
                    createTime: ''
                },
                weather: {
                    id: '',
                    farmId: '',
                    configName: '极端天气',
                    configCode: 'weather',
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
                this.$http({
                    method: "post",
                    url: "/web-server/monitor/warning/query.json?action=query",
                    data: params
                }).then(res => {
                    var configs = res.data;
                    var isHas = 0;
                    for (var i = 0; i < configs.length; i++) {
                        var value = configs[i];
                        if (value.configCode == "environment") {
                            isHas++;
                            this.environment.configValue = value.configValue == "true" ? true : false;
                            this.environment.id = value.id;
                            this.environment.createTime = new Date(value.createTime);
                        } else if (value.configCode == "weather") {
                            isHas++;
                            this.weather.configValue = value.configValue == "true" ? true : false;
                            this.weather.id = value.id;
                            this.weather.createTime = new Date(value.createTime);
                        }
                    }
                    //没有通知类型这个配置信息
                    if (configs.length == 0 || isHas == 0) {
                        this.environment.configValue = false;
                        this.weather.configValue = false;
                        this.environment.id = null;
                        this.weather.id = null
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
                warnIngList.push(this.environment);
                warnIngList.push(this.weather);
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
                      if(this.environment.id == null || this.environment.id==''){
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