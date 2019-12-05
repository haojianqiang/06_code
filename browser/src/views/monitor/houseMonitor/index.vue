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
<!--                        <video width="100%" height="320px" controls="controls">-->
<!--                            <source src="../../../assets/jntm.mp4" type="video/ogg">-->
<!--                            <source src="../../../assets/jntm.mp4" type="video/mp4">-->
<!--                            Your browser does not support the video tag.-->
<!--                        </video>-->
                        <div style="clear: both">
                            <div  @mousemove="resetCloseTimer()"  @touchstart="resetCloseTimer()" >
                                <LivePlayer :videoUrl="player.url" live muted :hasaudio="false" stretch v-loading="player.bLoading" element-loading-text="加载中..." element-loading-background="#000" :loading.sync="player.bLoading" @message="$message"></LivePlayer>
                                <div class="video-close" v-show="player.url && player.bCloseShow" v-on:click="closeVideo()">关闭</div>
                                <div class="video-close" v-show="!player.url && player.bCloseShow" v-on:click.stop="selectChannel()">选择通道</div>
                            </div>
                        </div>
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
            </el-tabs>
        </div>
        <!-- 通道选择弹出框-->
        <el-dialog :visible.sync="channelListDlgVisible" :modal-append-to-body='false'>
            <el-table :data="channels" stripe  :max-height="500" @row-click="play" :row-style="rowStyle"
                      ref="channelTable" v-loading="loading" element-loading-text="加载中...">
                <el-table-column prop="DeviceID" label="设备国标编号" min-width="200" show-overflow-tooltip sortable="custom" :fixed="false"></el-table-column>
                <el-table-column prop="ID" label="通道国标编号" min-width="200" show-overflow-tooltip sortable="custom" :fixed="false"></el-table-column>
                <el-table-column prop="Name" label="通道名称" min-width="120" :formatter="formatName" show-overflow-tooltip></el-table-column>
                <el-table-column prop="Status" label="在线状态" min-width="100">
                    <template slot-scope="props">
                        <span v-if="props.row.SubCount > 0">-</span>
                        <span v-else-if="props.row.Status == 'ON'" class="text-success">在线</span>
                        <span v-else>离线</span>
                    </template>
                </el-table-column>
                <el-table-column prop="SubCount" label="子节点数" min-width="100" sortable="custom"></el-table-column>
                <el-table-column prop="Manufacturer" label="厂家" min-width="120" :formatter="formatName" show-overflow-tooltip></el-table-column>
            </el-table>
            <el-pagination v-if="total > 0" layout="total,prev,pager,next" :pager-count="5" class="pull-right" :total="total" :page-size.sync="pageSize" :current-page.sync="currentPage"></el-pagination>
            <div slot="footer" class="dialog-footer footer" >
                <el-button @click="cancel">取消</el-button>
            </div>
        </el-dialog>


    </div>
</template>

<script>
    import LivePlayer  from '@liveqing/liveplayer'
    export default {
        name: "houseMonitor",
        components:{LivePlayer},
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
                farmName: '养龙鸡场',
                farmNames: [],
                houseName: '养龙鸡棚2号',
                houseNames: [],
                //视频直播的data
                // videoUrl : "http://192.168.1.103:10000/sms/34020000002020000001/flv/hls/34020000001320000001_34020000001320000102.flv",
                // bLoading:false,
                // live:true,
                protocol: "",
                player:{
                    url: "",
                    bLoading: false,
                    timer: 0,
                    bCloseShow: false,
                    closeTimer: 0
                },
                channelListDlgTitle: "",
                channelListDlgVisible:false,
                q: "",
                total: 0,
                pageSize: 10,
                currentPage: 1,
                sort: "",
                order: "",
                loading: false,
                channels: [],
                index: 0,
            }
        },
        created() {
            this.getFarmName();
        },
        mounted(){
            this.protocol = this.getQueryString("protocol", "");
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
            },
            //实时视频部分
            resetCloseTimer:function() {
                this.player.bCloseShow = true;
                if (this.player.closeTimer) {
                    clearTimeout(this.player.closeTimer)
                }
                this.player.closeTimer = setTimeout(() => {
                    this.player.bCloseShow = false;
                }, 2000);
            },
            closeVideo: function () {
               if (!this.player) {
                    return;
                }
                if (this.player.closeTimer) {
                    clearTimeout(this.player.closeTimer);
                    this.player.closeTimer = 0;
                }
                this.player.bCloseShow = false;
                this.player.bloading = false;
                this.player.url = "";
            },
            getQueryString(name, defVal = "") {
                var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
                var r = window.location.search.substr(1).match(reg);
                if (r != null) {
                    return unescape(r[2]);
                }
                return defVal;
            },
            //选择通道
            selectChannel() {
                this.channelListDlgVisible = !this.channelListDlgVisible;
                this.loading = true;
                this.$http.get(
                    "/v1/channel/list",
                    {params:{
                            q: this.q,
                            start: (this.currentPage -1) * this.pageSize,
                            limit: this.pageSize,
                            channel_type: "device",
                            online: "true",
                            sort: this.sort,
                            order: this.order
                        }}
                ).then(res => {
                    this.total = res.data.ChannelCount;
                    this.channels = res.data.ChannelList || [];
                }).catch(err => {
                    console.log(err);
                }).finally(()=>{
                    this.loading = false;
                });
            },
            formatName(row, col, cell) {
                if (cell) return cell;
                return "-";
            },
            cancel(){
                this.channelListDlgVisible = !this.channelListDlgVisible;
            },
            //播放
            play(row, event, column) {
                if(row.Status !== "ON") return;
                 if (!this.player) {
                    return;
                }
                this.channelListDlgVisible = !this.channelListDlgVisible;
                this.player.bLoading = true;
                this.$http.get(
                    "/v1/stream/start",
                    {params:{
                            serial: row.DeviceID,
                            code: row.ID
                        }}
                ).then(stream => {
                    console.log(stream);
                    var videoUrl = this.isMobile() ? stream.data.HLS : stream.data.RTMP;
                    if(stream.data.WS_FLV) {
                        videoUrl = stream.data.WS_FLV;
                    } else if(stream.data.FLV) {
                        videoUrl = stream.data.FLV;
                    }
                    var _protocol = String(this.protocol).toUpperCase();
                    switch (_protocol) {
                        case "RTMP":
                            videoUrl = stream.data.RTMP || "";
                            break;
                        case "HLS":
                            videoUrl = stream.data.HLS || "";
                            break;
                        case "FLV":
                            videoUrl = stream.data.FLV || "";
                            break;
                        case "WS_FLV":
                            videoUrl = stream.data.WS_FLV || "";
                            break;
                    }
                    this.player.url = videoUrl;
                    this.resetCloseTimer(this.player);
                }).catch(err => {
                    console.log(err);
                }).finally( ()=>{
                    this.player.bLoading = !this.player.bLoading;
                });
            },
            isMobile(){
                let flag = navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i)
                return flag;
            },
            rowStyle({row, rowIndex}) {
                if(row.Status === "ON") {
                    return "cursor:pointer";
                }
                return "";
            },
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
    .video-close {
        position: absolute ;
        top: 8%;
        right: 26%;
        color: white;
        font-size: 12px;
        background-color: #ADADAD;
        padding: 2px 5px;
        cursor: pointer;
        border-radius: 2px;
        max-width: 120px;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
    }
</style>
