<template>
    <div class="box box-primary">
        <div class="box-header">
            <h4 class="text-primary text-center">设备列表</h4>
        </div>
        <div class="box-body">
<!--            <form class="form-inline" autocomplete="off" spellcheck="false">-->
<!--                <div class="form-group form-group-sm">-->
<!--                    <label>搜索</label>-->
<!--                    <input type="text" class="form-control" placeholder="关键字" v-model.trim="q" @keydown.enter.prevent ref="q">-->
<!--                </div>-->
<!--                <span class="hidden-xs">&nbsp;&nbsp;</span>-->
<!--                <div class="form-group form-group-sm">-->
<!--                    <label>在线状态</label>-->
<!--                    <select class="form-control" v-model.trim="online">-->
<!--                        <option value="">全部</option>-->
<!--                        <option value="true">在线</option>-->
<!--                        <option value="false">离线</option>-->
<!--                    </select>-->
<!--                </div>-->
<!--                <span class="hidden-xs">&nbsp;&nbsp;</span>-->
<!--                <div class="form-group form-group-sm">-->
<!--                    <label>准入状态</label>-->
<!--                    <select class="form-control" v-model.trim="forbidden">-->
<!--                        <option value="">全部</option>-->
<!--                        <option value="false">准入</option>-->
<!--                        <option value="true">禁入</option>-->
<!--                    </select>-->
<!--                </div>-->
<!--                <div class="form-group pull-right">-->
<!--                    <router-link :to="`/devices/tree`" class="btn btn-default btn-sm">-->
<!--                        <i class="fa fa-sitemap"></i> 树视图-->
<!--                    </router-link>-->
<!--                </div>-->
<!--            </form>-->
            <br>
            <div class="clearfix"></div>
            <el-table :data="devices" stripe :default-sort="{prop: 'ID', order: 'ascending'}" @sort-change="sortChange">
                <el-table-column prop="ID" label="设备国标编号" min-width="200" sortable="custom">
                    <template slot-scope="props">
                        <span :class="{'text-red': props.row.Forbidden}" :title="props.row.Forbidden ? '禁止接入':''">{{props.row.ID}}</span>
                    </template>
                </el-table-column>
                <el-table-column label="操作" min-width="220" v-if="isMobile()">
                    <template slot-scope="props">
                        <div class="btn-group btn-group-xs">
                            <router-link class="btn btn-info" :to="`/devices/channels/${props.row.ID}/1`" v-if="props.row.Online && props.row.ChannelCount > 0">
                                查看通道
                            </router-link>
                            <button type="button" class="btn btn-primary" @click.prevent="fetchCatalog(props.row)" v-if="props.row.Online">
                                <i class="fa fa-refresh"></i> 更新通道
                            </button>
<!--                            <button type="button" class="btn btn-warning" @click.prevent="editDevice(props.row)" v-if="userInfo">-->
<!--                                <i class="fa fa-edit"></i> 编辑-->
<!--                            </button>-->
                            <button type="button" class="btn btn-danger" @click.prevent="removeDevice(props.row)" v-if="!props.row.Online">
                                <i class="fa fa-remove"></i> 删除
                            </button>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column prop="Name" label="名称" min-width="120" :formatter="formatName" show-overflow-tooltip></el-table-column>
                <el-table-column prop="CommandTransport" label="信令传输模式" min-width="140" :formatter="formatName" show-overflow-tooltip></el-table-column>
                <el-table-column prop="MediaTransport" label="流传输模式" min-width="140">
                    <template slot-scope="props">
                        <el-dropdown size="small" trigger="click" v-if="false" @command="setMediaTransport">
                <span class="el-dropdown-link">
                  {{formatTransport(props.row)}} <i class="el-icon-arrow-down el-icon--right"></i>
                </span>
                            <el-dropdown-menu slot="dropdown">
                                <el-dropdown-item :command="{row: props.row, MediaTransport: 'UDP'}">UDP</el-dropdown-item>
                                <el-dropdown-item :command="{row: props.row, MediaTransport: 'TCP', MediaTransportMode: 'passive'}">TCP 被动</el-dropdown-item>
                                <el-dropdown-item :command="{row: props.row, MediaTransport: 'TCP', MediaTransportMode: 'active'}">TCP 主动</el-dropdown-item>
                            </el-dropdown-menu>
                        </el-dropdown>
                        <span v-else>{{formatTransport(props.row)}}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="ChannelCount" label="通道数" min-width="100"></el-table-column>
                <el-table-column prop="Online" label="在线状态" min-width="100">
                    <template slot-scope="props">
                        <span class="text-success" v-if="props.row.Online">在线</span>
                        <span class="text-gray" v-else>离线</span>
                    </template>
                </el-table-column>
                <el-table-column prop="RemoteIP" label="出口IP" min-width="140"></el-table-column>
                <el-table-column prop="RemotePort" label="端口" min-width="100"></el-table-column>
                <el-table-column prop="Manufacturer" label="厂家" min-width="120" :formatter="formatName" show-overflow-tooltip></el-table-column>
                <el-table-column prop="CreatedAt" label="创建时间" min-width="160" sortable="custom"></el-table-column>
                <el-table-column prop="UpdatedAt" label="更新时间" min-width="160" sortable="custom"></el-table-column>
                <el-table-column label="操作" min-width="220" fixed="right" v-if="!isMobile()">
                    <template slot-scope="props">
                        <div class="btn-group btn-group-xs" >
                            <router-link class="btn btn-info" :to="`/playBack/channels/${props.row.ID}/1`" v-if="props.row.Online && props.row.ChannelCount > 0">
                                <i class="fa fa-info"></i> 查看通道
                            </router-link>
<!--                            <button type="button" class="btn btn-primary" @click.prevent="fetchCatalog(props.row)" v-if="props.row.Online && userInfo">-->
<!--                                <i class="fa fa-refresh"></i> 更新通道-->
<!--                            </button>-->
<!--                            <button type="button" class="btn btn-warning" @click.prevent="editDevice(props.row)" v-if="userInfo">-->
<!--                                <i class="fa fa-edit"></i> 编辑-->
<!--                            </button>-->
<!--                            <button type="button" class="btn btn-danger" @click.prevent="removeDevice(props.row)" v-if="!props.row.Online && userInfo">-->
<!--                                <i class="fa fa-remove"></i> 删除-->
<!--                            </button>-->
                        </div>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <div class="box-footer" v-if="total > 0">
            <el-pagination layout="total,prev,pager,next" :pager-count="5" class="pull-right" :total="total" :page-size.sync="pageSize" :current-page.sync="currentPage"></el-pagination>
        </div>
<!--        <DeviceEditDlg ref="deviceEditDlg" @submit="getDeviceList"></DeviceEditDlg>-->
    </div>
</template>

<script>
    import _ from "lodash";
    // import DeviceEditDlg from 'components/DeviceEditDlg.vue'
    // import { mapState } from "vuex"
    export default {
        props: {},
        data() {
            return {
                q: "",
                online: "",
                forbidden: "",
                total: 0,
                pageSize: 10,
                currentPage: 1,
                sort: "ID",
                order: "asc",
                devices: [],
                loading: false,
                timer: 0
            };
        },
        // components: {
        //     DeviceEditDlg
        // },
        // computed: {
        //     ...mapState(["userInfo"])
        // },
        mounted() {
            this.getDeviceList();
            //之后每20秒钟取一次设备列表
            this.timer = setInterval(() => {
                // console.log("获取设备");
                this.getDeviceList();
            }, 20000);
        },
        beforeDestroy() {
            if (this.timer) {
                clearInterval(this.timer);
                this.timer = 0;
            }
        },
       methods: {
            isMobile() {
                let flag = navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i)
                return flag;
            },
            doSearch(page = 1) {
                // var query = {};
                // if (this.q) query["q"] = this.q;
                // if (this.online) query["online"] = this.online;
                // if (this.forbidden) query["forbidden"] = this.forbidden;
                // this.$router.replace({
                //     path: `/devices/${page}`,
                //     query: query
                // });
            },
            // doDelaySearch: _.debounce(function() {
            //     this.doSearch();
            // }, 500),
            getDeviceList() {
                this.loading = true;
                var timenow = new Date().getTime();
                let params = new URLSearchParams();
                params.append("q", "");
                params.append("start", 0);
                params.append("limit", 10);
                params.append("online","");
                params.append("forbidden", "");
                params.append("sort", "ID");
                params.append("order", "asc");
                params.append("_", timenow.timestamp);
                this.$http({
                    method: "get",
                    url: "/v1/device/list",
                    data:params
                }).then(res => {
                    // console.log(res);
                    this.total = res.data["DeviceCount"];
                    this.devices = res.data["DeviceList"];
                }).catch(err => {
                    console.log(err);
                }).finally( ()=>{
                    this.loading = false;
                });
            },
            sortChange(data) {
                // this.sort = data.prop;
                // this.order = data.order == "ascending" ? "asc" : "desc";
                // this.getDeviceList();
            },
            fetchCatalog(row) {
                // $.ajax("/api/v1/device/fetchcatalog", {
                //     global: false,
                //     data: {
                //         serial: row.ID
                //     }
                // }).then(() => {
                //     this.$message({
                //         type: "success",
                //         message: "更新通道信息成功"
                //     })
                // })
            },
            // removeDevice(row) {
            //     this.$confirm(`确认删除 ${row.Name || row.ID}`, "提示").then(() => {
            //         $.get("/api/v1/device/remove", {
            //             serial: row.ID
            //         }).always(() => {
            //             this.getDeviceList();
            //         });
            //     }).catch(() => {});
            // },
            // editDevice(row) {
            //     this.$refs["deviceEditDlg"].show({
            //         serial: row.ID,
            //         name: row.Name,
            //         media_transport: row.MediaTransport,
            //         media_transport_mode: row.MediaTransportMode,
            //         recv_stream_ip: row.RecvStreamIP,
            //         contact_ip: row.ContactIP,
            //         catalog_interval: row.CatalogInterval,
            //         subscribe_interval: row.SubscribeInterval,
            //         password: row.Password,
            //         forbidden: row.Forbidden,
            //         freeticket: row.Freeticket,
            //     });
            // },
            formatName(row, col, cell) {
                if (cell) return cell;
                return "-";
            },
            formatTransport(row, col, cell) {
                var ret = String(row.MediaTransport).toUpperCase();
                if(ret == "TCP") {
                    ret += row.MediaTransportMode == 'active' ? " 主动" : " 被动";
                }
                return ret;
            },
            setMediaTransport(cmd) {
                // cmd.row.MediaTransport = cmd.MediaTransport || cmd.row.MediaTransport;
                // cmd.row.MediaTransportMode = cmd.MediaTransportMode || cmd.row.MediaTransportMode;
                // this.$http().get("/play/api/v1/device/setmediatransport", {
                //     serial: cmd.row.ID,
                //     media_transport: cmd.row.MediaTransport,
                //     media_transport_mode: cmd.row.MediaTransportMode
                // })
            }
        },
        beforeRouteEnter(to, from, next) {
            next(vm => {
                vm.q = to.query.q || "";
                vm.online = to.query.online || "";
                vm.forbidden = to.query.forbidden || "";
                vm.currentPage = parseInt(to.params.page) || 1;
            });
        },
        beforeRouteLeave(to, from, next) {
            if (this.timer) {
                clearInterval(this.timer);
                this.timer = 0;
            }
            next();
        },
        beforeRouteUpdate(to, from, next) {
            next();
            this.$nextTick(() => {
                this.q = to.query.q || "";
                this.online = to.query.online || "";
                this.forbidden = to.query.forbidden || "";
                this.currentPage = parseInt(to.params.page) || 1;
                this.devices = [];
                this.getDeviceList();
            });
        }
    };
</script>

