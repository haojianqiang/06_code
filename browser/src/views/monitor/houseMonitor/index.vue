<template>
    <div>
        <div style="margin-left: 0;margin-top:0;width: 100%;height: 450px;color: #333333;float: left;">
            <el-tabs type="border-card">
                <el-tab-pane label="实时监控视频">
                    <div style="width: 75%;border: 1px solid #DBDEE7;float: left;">
                        <el-button aria-readonly="true"
                                   style="background-color: #3EBF7C;font-size: 16px;text-align: center;width: 100%;height: 42px;color: #FFFFFF;float: left;">
                            鸡棚视频监控实时信息
                        </el-button>
                        <video width="100%" height="320px" controls="controls">
                            <source src="../../../assets/jntm.mp4" type="video/ogg">
                            <source src="../../../assets/jntm.mp4" type="video/mp4">
                            Your browser does not support the video tag.
                        </video>
                        <el-form :inline="true" :model="pmHouseInfo" align="center" style="margin-top: 15px;">
                            <el-form-item label="当前温度值:" prop="temperature">
                                <el-input v-model.trim="pmHouseInfo.temperature" placeholder="温度值"
                                          size="small" class="inputField"></el-input>
                            </el-form-item>
                            <el-form-item label="当前湿度值:" prop="humidity">
                                <el-input v-model.trim="pmHouseInfo.humidity" placeholder="湿度值"
                                          size="small" class="inputField"></el-input>
                            </el-form-item>
                            <el-form-item label="当前氨气浓度:" prop="nh3">
                                <el-input v-model.trim="pmHouseInfo.nh3" placeholder="NH3浓度"
                                          size="small" class="inputField"></el-input>
                            </el-form-item>
                            <el-form-item label="当前二氧化碳浓度:" prop="co2">
                                <el-input v-model.trim="pmHouseInfo.co2" placeholder="CO2浓度"
                                          size="small" class="inputField"></el-input>
                            </el-form-item>
                        </el-form>

                    </div>
                    <div style="margin-left: 40px;width: 20%;height: 450px;color: #333333;float: left;border: 1px solid #DBDEE7;">
                        <el-button
                                style="background-color: #3EBF7C;text-align: center;width: 100%;font-size: 16px;color: #FFFFFF;"
                                aria-readonly="true">鸡棚列表 <i class="el-icon-arrow-down el-icon&#45;&#45;right"></i>
                        </el-button>
                        <el-select v-model.trim="farmName" size="small" placeholder="请选择鸡场名称" @change="selectChange">
                            <el-option v-for="item in farmNames" :key="item.id" :label="item.name" :value="item.id">
                            </el-option>
                        </el-select>
                        <ul v-if="houseNames">
                            <li v-for="item in houseNames" :key="item.id" style="text-align: center;margin-top: 6px;">
                                <a href="javascript:void(0);" @click="updateInfo(item.id)">{{item.name}}</a>
                            </li>
                        </ul>
                    </div>
                </el-tab-pane>
                <el-tab-pane label="监控设置配置">监控设置配置</el-tab-pane>
            </el-tabs>
        </div>

    </div>
</template>

<script>
    export default {
        name: "houseMonitor",
        data() {
            return {
                pmHouseInfo: {
                    id: '',
                    houseId: '',
                    avgWight: '',
                    co2: '',
                    nh3: '',
                    temperature: '',
                    humidity: '',
                    weather: '',
                    createTime: '',
                    modifyTime: '',
                    modifier: '',
                    modifierId: ''
                },
                farmName: '',
                farmNames: [],
                houseName: '',
                houseNames: []
            }
        },
        created() {
            this.getFarmName();
        },
        watch: {},
        methods: {
            updateInfo(houseId) {
                for (var i in this.pmHouseInfo){
                    if (this.pmHouseInfo[i]){
                        this.pmHouseInfo[i] = ''
                    }
                }

                let params = new URLSearchParams();
                params.append("houseId", houseId);
                this.$http({
                    method: "post",
                    url: "/web-server/monitor/housemonitor/query.json?action=query",
                    data: params
                }).then(res => {
                    this.pmHouseInfo = res.data.rows[0];
                    this.pmHouseInfo.temperature += "℃";
                    this.pmHouseInfo.humidity += "%";
                    this.pmHouseInfo.nh3 += "ppm";
                    this.pmHouseInfo.co2 += "ppm";
                }).catch(err => {
                    console.log(err);
                });
            },
            getFarmName() {
                this.houseName = '';
                this.houseNames = [];

                this.$http({
                    method: "get",
                    url: "/web-server/monitor/housemonitor/query.json?action=getFarmNameAndId"
                }).then(res => {
                    this.farmNames = Object.assign({}, res.data.data);
                }).catch(err => {
                    console.log(err);
                });
            },
            selectChange(farmId) {
                this.houseName = '';
                this.houseNames = [];

                let params = new URLSearchParams();
                params.append("farmId", farmId);

                this.$http({
                    method: "post",
                    url: "/web-server/monitor/housemonitor/query.json?action=getHouseNameAndId",
                    data: params
                }).then(res => {
                    this.houseNames = Object.assign({}, res.data.data);
                }).catch(err => {
                    console.log(err);
                });
            }
        },
        computed: {}

    }
</script>

<style scoped>
    .inputField{
        width:90px;
        font-weight: bolder;
    }
    .el-dropdown-link {
        cursor: pointer;
        color: #409EFF;
    }

    .el-icon-arrow-down {
        font-size: 12px;
    }

    a {
        text-decoration: none;
        color: #909399;
        font-size: 18px;
    }

    a:hover {
        text-decoration: none;
        color: #3EBF7C;
        font-size: 18px
    }
</style>