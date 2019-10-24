<template>
    <el-row :gutter="20" style="margin-top:30px;margin-left:30px;margin-right:30px">
        <el-col :span="14">
            <div class="grid-content bg-purple">
                <div style="background: #d3dce6;text-align:center;">预警阈值设置：</div>
                <div style=" margin-left:20px; margin-top:60px;">
                    <el-row style=" margin-bottom:30px">
                        <el-col :span="6" style="text-align:right;padding-top:5px">
                            <div>温度值：</div>
                        </el-col>
                        <el-col :span="6">
                            <div>
                                <el-input v-model.trim="temperature_lowerLimit.configValue" size="small"
                                          placeholder="请输入下限值"/>
                            </div>
                        </el-col>
                        <el-col :span="6" style=" margin-left:15px">
                            <div>
                                <el-input v-model.trim="temperature_upperLimit.configValue" size="small"
                                          placeholder="请输入上限值"/>
                            </div>
                        </el-col>
                    </el-row>

                    <el-row style=" margin-bottom:30px">
                        <el-col :span="6" style="text-align:right;padding-top:5px">
                            <div>湿度值：</div>
                        </el-col>
                        <el-col :span="6">
                            <div>
                                <el-input v-model.trim="humidity_lowerLimit.configValue" size="small"
                                          placeholder="请输入下限值"/>
                            </div>
                        </el-col>
                        <el-col :span="6" style=" margin-left:15px">
                            <div>
                                <el-input v-model.trim="humidity_upperLimit.configValue" size="small"
                                          placeholder="请输入上限值"/>
                            </div>
                        </el-col>
                    </el-row>

                    <el-row style=" margin-bottom:30px">
                        <el-col :span="6" style="text-align:right;padding-top:5px">
                            <div>氨气浓度：</div>
                        </el-col>
                        <el-col :span="6">
                            <div>
                                <el-input v-model.trim="nh3_lowerLimit.configValue" size="small" placeholder="请输入下限值"/>
                            </div>
                        </el-col>
                        <el-col :span="6" style=" margin-left:15px">
                            <div>
                                <el-input v-model.trim="nh3_upperLimit.configValue" size="small" placeholder="请输入上限值"/>
                            </div>
                        </el-col>
                    </el-row>

                    <el-row style=" margin-bottom:30px">
                        <el-col :span="6" style="text-align:right;padding-top:5px">
                            <div>二氧化碳浓度：</div>
                        </el-col>
                        <el-col :span="6">
                            <div>
                                <el-input v-model.trim="co2_lowerLimit.configValue" size="small" placeholder="请输入下限值"/>
                            </div>
                        </el-col>
                        <el-col :span="6" style=" margin-left:15px">
                            <div>
                                <el-input v-model.trim="co2_upperLimit.configValue" size="small" placeholder="请输入上限值"/>
                            </div>
                        </el-col>
                    </el-row>
                </div>
                <div style="background: #e5e9f2;text-align:center;margin-left:350px;margin-top:50px;">
                    <el-button style="background-color: #3ebf7c;width:300px;height:40px;" @click="saveConfig" size="small" type="success"
                               class="btn-save"
                               clicked="0">保存
                    </el-button>
                </div>
            </div>
        </el-col>
        <el-col :span="5">
            <div class="grid-content bg-purple">
                <div class="label_div">鸡场列表：</div>
                <el-select v-model.trim="farmId" placeholder="请选择鸡场" size="small" @change="farmSelectValueChange">
                    <el-option v-for="item in farms" :key="item.id" :label="item.name" :value="item.id"/>
                </el-select>
            </div>
        </el-col>
        <el-col :span="5">
            <div class="grid-content bg-purple">
                <div class="label_div">鸡棚列表：</div>
                <el-select v-model="houseIds" multiple placeholder="请选择鸡棚" size="small"
                           @change="houseSelectValueChange">
                    <el-option v-for="item in houses" :key="item.id" :label="item.name" :value="item.id"/>
                </el-select>
                <div style="background: #d3dce6;width: 415px;height:30px;text-align:center;margin:40px  -220px ;">可选预警内容：</div>
                <div>
                    <el-checkbox style="margin-left:-100px" v-model="environment.configValue">鸡场环境</el-checkbox>
                    <el-checkbox style="margin-top:5px;" v-model="weather.configValue">极端天气</el-checkbox>
                </div>
                <div style="background: #d3dce6;text-align:center;width: 415px;height:30px;margin:20px  -220px ;">可选通知类型：</div>
                <div>
                    <el-checkbox style="margin-left:-100px;" v-model="sms.configValue">短信通知</el-checkbox>
                    <el-checkbox style="margin-top:5px;" v-model="wechat.configValue">微信通知</el-checkbox>
                </div>
            </div>
        </el-col>
    </el-row>
</template>

<script>
    export default {
        name: "alertSettings",
        data() {
            return {
                //4个阈值下限值
                temperature_lowerLimit: {
                    id: '',
                    farmId: '',
                    houseId: '',
                    configName: '温度下限值',
                    configCode: 'temperature_lowerLimit',
                    configValue: '',
                    createTime: ''
                },
                humidity_lowerLimit: {
                    id: '',
                    farmId: '',
                    houseId: '',
                    configName: '湿度下限值',
                    configCode: 'humidity_lowerLimit',
                    configValue: '',
                    createTime: ''
                },
                nh3_lowerLimit: {
                    id: '',
                    farmId: '',
                    houseId: '',
                    configName: '氨气浓度下限值',
                    configCode: 'nh3_lowerLimit',
                    configValue: '',
                    createTime: ''
                },
                co2_lowerLimit: {
                    id: '',
                    farmId: '',
                    houseId: '',
                    configName: '二氧化碳浓度下限值',
                    configCode: 'co2_lowerLimit',
                    configValue: '',
                    createTime: ''
                },
                //四个上限值
                temperature_upperLimit: {
                    id: '',
                    farmId: '',
                    houseId: '',
                    configName: '温度上限值',
                    configCode: 'temperature_upperLimit',
                    configValue: '',
                    createTime: ''
                },
                humidity_upperLimit: {
                    id: '',
                    farmId: '',
                    houseId: '',
                    configName: '湿度上限值',
                    configCode: 'humidity_upperLimit',
                    configValue: '',
                    createTime: ''
                },
                nh3_upperLimit: {
                    id: '',
                    farmId: '',
                    houseId: '',
                    configName: '氨气浓度上限值',
                    configCode: 'nh3_upperLimit',
                    configValue: '',
                    createTime: ''
                },
                co2_upperLimit: {
                    id: '',
                    farmId: '',
                    houseId: '',
                    configName: '二氧化碳浓度上限值',
                    configCode: 'co2_upperLimit',
                    configValue: '',
                    createTime: ''
                },
                //下拉框数据源
                farms: '',
                houses: '',
                //两个下拉数据
                farmId: '',
                houseIds: '',
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
                },sms: {
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
        }, created() {
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

            //根据鸡场id获取鸡舍列表，名称及id
            fetchHouses(farmId) {
                let params = new URLSearchParams();
                params.append("farmId", farmId);
                this.$http({
                    method: "post",
                    url: "/web-server/monitor/housemonitor/query.json?action=getHouseNameAndId",
                    data: params
                }).then(res => {
                    this.houses = res.data.data;
                }).catch(err => {
                    console.log(err);
                });
            },



            //鸡舍下拉框选择事件
            houseSelectValueChange() {
                //只选一个鸡棚时，才给显示配置信息
                if (this.houseIds.length == 1) {
                    this.getWarnConfig();
                } else {
                    this.clearValue();
                }
            },
            //获取阈值配置
            getWarnConfig() {
                let params = new URLSearchParams();
                params.append("farmId", this.farmId);
                params.append("houseId", this.houseIds[0]);
                this.$http({
                    method: "post",
                    url: "/web-server/monitor/warning/query.json?action=query",
                    data: params
                }).then(res => {
                    var configs = res.data;
                    console.log(configs);
                    var isHas = 0;
                    for (var i = 0; i < configs.length; i++) {
                        var value = configs[i];
                        if (value.configCode == "temperature_lowerLimit") {
                            this.temperature_lowerLimit.configValue = value.configValue;
                            this.temperature_lowerLimit.id = value.id;
                            this.temperature_lowerLimit.createTime = new Date(value.createTime);
                            isHas++;
                        } else if (value.configCode == "humidity_lowerLimit") {
                            this.humidity_lowerLimit.configValue = value.configValue;
                            this.humidity_lowerLimit.id = value.id;
                            this.humidity_lowerLimit.createTime = new Date(value.createTime);
                            isHas++;
                        } else if (value.configCode == "nh3_lowerLimit") {
                            this.nh3_lowerLimit.configValue = value.configValue;
                            this.nh3_lowerLimit.id = value.id;
                            this.nh3_lowerLimit.createTime = new Date(value.createTime);
                            isHas++;
                        } else if (value.configCode == "co2_lowerLimit") {
                            this.co2_lowerLimit.configValue = value.configValue;
                            this.co2_lowerLimit.id = value.id;
                            this.co2_lowerLimit.createTime = new Date(value.createTime);
                            isHas++;
                        } else if (value.configCode == "temperature_upperLimit") {
                            this.temperature_upperLimit.configValue = value.configValue;
                            this.temperature_upperLimit.id = value.id;
                            this.temperature_upperLimit.createTime = new Date(value.createTime);
                            isHas++;
                        } else if (value.configCode == "humidity_upperLimit") {
                            this.humidity_upperLimit.configValue = value.configValue;
                            this.humidity_upperLimit.id = value.id;
                            this.humidity_upperLimit.createTime = new Date(value.createTime);
                            isHas++;
                        } else if (value.configCode == "nh3_upperLimit") {
                            this.nh3_upperLimit.configValue = value.configValue;
                            this.nh3_upperLimit.id = value.id;
                            this.nh3_upperLimit.createTime = new Date(value.createTime);
                            isHas++;
                        } else if (value.configCode == "co2_upperLimit") {
                            this.co2_upperLimit.configValue = value.configValue;
                            this.co2_upperLimit.id = value.id;
                            this.co2_upperLimit.createTime = new Date(value.createTime);
                            isHas++;
                        }
                    }
                    if (configs.length == 0 || isHas == 0) {
                        this.clearValue();
                    }
                }).catch(err => {
                    console.log(err);
                });
            },
            //鸡场下拉框变化事件
            farmSelectValueChange(farmId) {
                let params = new URLSearchParams();
                params.append("farmId", this.farmId);
                console.log(this.farmId);
                this.clearValue();
                this.houses = null;
                this.houseIds = null;
                this.fetchHouses(farmId);
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
                        } else if (value.configCode == "environment") {
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
                        this.sms.configValue = false;
                        this.wechat.configValue = false;
                        this.sms.id = null;
                        this.wechat.id = null;
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

                if (this.houseIds.length < 1) {
                    return;
                }
                if (this.temperature_lowerLimit.configValue === "" || this.temperature_lowerLimit.configValue == null || this.temperature_lowerLimit.configValue === undefined) {
                    alert("请输入温度值下限");
                    return false;
                }
                if (this.temperature_upperLimit.configValue === "" || this.temperature_upperLimit.configValue == null || this.temperature_upperLimit.configValue === undefined) {
                    alert("请输入温度值上限");
                    return false;
                }
                if (this.humidity_lowerLimit.configValue === "" || this.humidity_lowerLimit.configValue == null || this.humidity_lowerLimit.configValue === undefined) {
                    alert("请输入湿度值下限");
                    return false;
                }
                if (this.humidity_upperLimit.configValue === "" || this.humidity_upperLimit.configValue == null || this.humidity_upperLimit.configValue === undefined) {
                    alert("请输入湿度值上限");
                    return false;
                }
                if (this.nh3_lowerLimit.configValue === "" || this.nh3_lowerLimit.configValue == null || this.nh3_lowerLimit.configValue === undefined) {
                    alert("请输入氨气浓度值下限");
                    return false;
                }
                if (this.nh3_upperLimit.configValue === "" || this.nh3_upperLimit.configValue == null || this.nh3_upperLimit.configValue === undefined) {
                    alert("请输入氨气浓度值上限");
                    return false;
                }

                if (this.co2_lowerLimit.configValue === "" || this.co2_lowerLimit.configValue == null || this.co2_lowerLimit.configValue === undefined) {
                    alert("请输入二氧化碳浓度值下限");
                    return false;
                }
                if (this.co2_upperLimit.configValue === "" || this.co2_upperLimit.configValue == null || this.co2_upperLimit.configValue === undefined) {
                    alert("请输入二氧化碳浓度值上限");
                    return false;
                }

                var warnIngList = new Array();
                warnIngList.push(this.temperature_lowerLimit);
                warnIngList.push(this.humidity_lowerLimit);
                warnIngList.push(this.nh3_lowerLimit);
                warnIngList.push(this.co2_lowerLimit);
                warnIngList.push(this.temperature_upperLimit);
                warnIngList.push(this.humidity_upperLimit);
                warnIngList.push(this.nh3_upperLimit);
                warnIngList.push(this.co2_upperLimit);
                warnIngList.push(this.environment);
                warnIngList.push(this.weather);
                warnIngList.push(this.sms);
                warnIngList.push(this.wechat);


                var JsonData = JSON.stringify(warnIngList);
                let params = new URLSearchParams();
                params.append("pmWarningConfigs", JsonData);
                params.append("farmId", this.farmId);
                params.append("houseIds", JSON.stringify(this.houseIds));
                this.$http({
                    method: "post",
                    url: "/web-server/monitor/warning/query.json?action=saveThresholdValue",
                    data: params
                }).then(res => {
                    this.$message.success("保存成功")
                    //如果是第一次保存，需要请求一下数据库，获取id
                    if (this.temperature_lowerLimit.id == null || this.temperature_upperLimit.id == '') {
                        console.log("重新获取");
                        this.houseSelectValueChange();
                    }else if(this.environment.id == null || this.environment.id==''){
                        this.farmSelectValueChange();
                    }else if(this.sms.id == null || this.sms.id==''){
                        this.farmSelectValueChange();
                    }
                }).catch(err => {
                    console.log(err);
                });

                //延迟执行
                setTimeout(function () {
                    btnSave.attr("clicked", "0");
                }, 2000);
            },
            clearValue() {
                this.temperature_lowerLimit.configValue = '';
                this.humidity_lowerLimit.configValue = '';
                this.nh3_lowerLimit.configValue = '';
                this.co2_lowerLimit.configValue = '';
                this.temperature_upperLimit.configValue = '';
                this.humidity_upperLimit.configValue = '';
                this.nh3_upperLimit.configValue = '';
                this.co2_upperLimit.configValue = '';

                this.temperature_lowerLimit.id = null;
                this.humidity_lowerLimit.id = null
                this.nh3_lowerLimit.id = null;
                this.co2_lowerLimit.id = null;
                this.temperature_upperLimit.id = null;
                this.humidity_upperLimit.id = null
                this.nh3_upperLimit.id = null;
                this.co2_upperLimit.id = null;
            },
            //阈值校验
            checkValue() {
                this.temperature_lowerLimit.configValue = '';
                this.humidity_lowerLimit.configValue = '';
                this.nh3_lowerLimit.configValue = '';
                this.co2_lowerLimit.configValue = '';
                this.temperature_upperLimit.configValue = '';
                this.humidity_upperLimit.configValue = '';
                this.nh3_upperLimit.configValue = '';
                this.co2_upperLimit.configValue = '';
            }
        }
    }
</script>

<style scoped>
    .grid-content {
        min-height: 450px;
        min-width: 200px;
    }

    .value_div {
        text-align: center;
        margin-top: 35px
    }

    .label_div {
        text-align: center;
        background: #d3dce6;
        width: 196px
    }
</style>