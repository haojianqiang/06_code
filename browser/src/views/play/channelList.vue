<template>
    <div>
        <div class="box box-primary">
          <div class="box-header">
            <h4 class="text-primary text-center">通道列表({{name||devid}}<span v-if="dir_serial">:{{dir_serial}}</span>)</h4>
          </div>
          <div class="box-body">
            <form class="form-inline" autocomplete="off" spellcheck="false">
              <div class="form-group form-group-sm">
                <button type="button" class="btn btn-sm btn-primary" @click.prevent="$router.go(-1)">
                  <i class="fa fa-chevron-left"></i> 返回
                </button>
              </div>
              <span class="hidden-xs">&nbsp;&nbsp;</span>
              <div class="form-group form-group-sm">
                <label>搜索</label>
                <input type="text" class="form-control" placeholder="关键字" v-model.trim="q" @keydown.enter.prevent ref="q">
              </div>
              <span class="hidden-xs">&nbsp;&nbsp;</span>
              <div class="form-group form-group-sm">
                <label>通道类型</label>
                <select class="form-control" v-model.trim="channel_type">
                  <option value="">全部</option>
                  <option value="device">设备</option>
                  <option value="dir">子目录</option>
                </select>
              </div>
              <span class="hidden-xs">&nbsp;&nbsp;</span>
              <div class="form-group form-group-sm">
                <label>在线状态</label>
                <select class="form-control" v-model.trim="online">
                  <option value="">全部</option>
                  <option value="true">在线</option>
                  <option value="false">离线</option>
                </select>
              </div>
            </form>
            <br>
            <el-table :data="channels" stripe :default-sort="{prop: 'Channel', order: 'ascending'}" @sort-change="sortChange" v-loading="loading" element-loading-text="加载中...">
              <el-table-column prop="Channel" label="通道号" min-width="100" show-overflow-tooltip sortable="custom"></el-table-column>
              <el-table-column label="操作" min-width="260" v-if="isMobile()" class-name="opt-group">
                <template slot-scope = "props">
                    <div class="btn-group btn-group-xs" v-if="props.row.SubCount == 0">
                        <button type="button" class="btn btn-primary" @click.prevent="playStream(props.row)" :disabled="props.row.Locked" v-if="props.row.Status == 'ON'">
                          <i class="fa fa-play-circle"></i> 播放
                        </button>
                        <button type="button" class="btn btn-danger" @click.prevent="stopStream(props.row)" v-if="props.row.Status == 'ON' && props.row.StreamID ">
                          <i class="fa fa-stop"></i> 停止
                        </button>
                        <a :href="`/play.html?serial=${props.row.DeviceID}&code=${props.row.ID}`" class="btn btn-warning" target="_blank" v-if="false">
                          <i class="fa fa-share"></i> 分享页
                        </a>
                        <router-link class="btn btn-info" :to="`/devices/playback/timebox/${devid}/${props.row.ID}`">
                          <i class="fa fa-info"></i> 设备录像
                        </router-link>
                    </div>
                    <div class="btn-group btn-group-xs" v-else>
                        <router-link class="btn btn-info" :to="`/devices/channels/${devid}/1?dir_serial=${props.row.ID}`">
                          <i class="fa fa-info"></i> 查看子目录
                        </router-link>
                    </div>
                </template>
              </el-table-column>
              <el-table-column prop="ID" label="通道国标编号" min-width="200" show-overflow-tooltip sortable="custom"></el-table-column>
              <el-table-column prop="Name" label="通道名称" min-width="120" :formatter="formatName" show-overflow-tooltip></el-table-column>
              <el-table-column prop="Status" label="在线状态" min-width="100">
                <template slot-scope="props">
                  <span v-if="props.row.SubCount > 0">-</span>
                  <span v-else-if="props.row.Status == 'ON'" class="text-success">在线</span>
                  <span v-else>离线</span>
                </template>
              </el-table-column>
<!--              <el-table-column min-width="100" label="快照">-->
<!--                  <template slot-scope="props">-->
<!--                      <el-popover placement="left" :title="`通道${props.row.Channel}-${props.row.Name}`" width="400" trigger="hover">-->
<!--                          <img onerror='this.src="../assets/image/snap.png"' style="width:100%;height:100%;" :src="props.row.SnapURL">-->
<!--                          <img onerror='this.src="../assets/image/snap.png"' style="height:30px;width:50px;" slot="reference" :src="props.row.SnapURL">-->
<!--                      </el-popover>-->
<!--                  </template>-->
<!--              </el-table-column>-->
              <el-table-column prop="AudioEnable" label="开启音频" min-width="100" v-if="false">
                  <template slot-scope="props">
                      <el-switch :value="props.row.AudioEnable" @change="toggleAudio(props.row)" :disabled="props.row.SubCount > 0"></el-switch>
                  </template>
              </el-table-column>
              <el-table-column prop="Ondemand" label="按需直播" min-width="100" v-if="false">
                  <template slot-scope="props">
                      <el-switch :value="props.row.Ondemand" @change="toggleOndemand(props.row)" :disabled="props.row.SubCount > 0"></el-switch>
                  </template>
              </el-table-column>
               <el-table-column prop="CloudRecord" label="云端录像" min-width="100" v-if="false">
                  <template slot-scope="props">
                      <el-switch :value="props.row.CloudRecord" @change="toggleCloudRecord(props.row)" :disabled="props.row.SubCount > 0"></el-switch>
                  </template>
              </el-table-column>
              <el-table-column prop="Shared" label="开启分享" min-width="100" v-if="false">
                  <template slot-scope="props">
                      <el-switch :value="props.row.Shared" @change="toggleShared(props.row)" :disabled="props.row.SubCount > 0"></el-switch>
                  </template>
              </el-table-column>
              <el-table-column prop="NumOutputs" label="在线人数" min-width="100"></el-table-column>
              <el-table-column prop="SubCount" label="子节点数" min-width="100"></el-table-column>
              <el-table-column prop="Manufacturer" label="厂家" min-width="120" :formatter="formatName" show-overflow-tooltip></el-table-column>
              <el-table-column label="操作" min-width="260" fixed="right" v-if="!isMobile()" class-name="opt-group">
                <template slot-scope="props">
                    <div class="btn-group btn-group-xs" v-if="props.row.SubCount == 0">
                        <!--
                        <button type="button" class="btn btn-info" @mousedown.prevent="talkStart(props.row)" :disabled="props.row.Locked" v-if="props.row.Status == 'ON'">
                          <i class="fa fa-microphone"></i> 对讲
                        </button>
                        -->
<!--                        <button type="button" class="btn btn-primary" @click.prevent="playStream(props.row)" :disabled="props.row.Locked" v-if="props.row.Status == 'ON'">-->
<!--                          <i class="fa fa-play-circle"></i> 播放-->
<!--                        </button>-->
<!--                        <button type="button" class="btn btn-danger" @click.prevent="stopStream(props.row)" v-if="props.row.Status == 'ON' && props.row.StreamID ">-->
<!--                          <i class="fa fa-stop"></i> 停止-->
<!--                        </button>-->
<!--                        <a :href="`/play.html?serial=${props.row.DeviceID}&code=${props.row.ID}`" class="btn btn-warning" target="_blank" v-if="false">-->
<!--                          <i class="fa fa-share"></i> 分享页-->
<!--                        </a>-->
                        <router-link class="btn btn-info" :to="`/playback/timebox/${devid}/${props.row.ID}`" v-if="props.row.Status == 'ON'" >
                          <i class="fa fa-info"></i> 录像回放
                        </router-link>
                    </div>
<!--                    <div class="btn-group btn-group-xs" v-else>-->
<!--                        <router-link class="btn btn-info" :to="`/devices/channels/${devid}/1?dir_serial=${props.row.ID}`">-->
<!--                          <i class="fa fa-info"></i> 查看子目录-->
<!--                        </router-link>-->
<!--                    </div>-->
                </template>
              </el-table-column>
            </el-table>
          </div>
          <div class="box-footer" v-if="total > 0">
            <el-pagination layout="total,prev,pager,next" :pager-count="5" class="pull-right" :total="total" :page-size.sync="pageSize" :current-page.sync="currentPage"></el-pagination>
          </div>
        </div>
<!--        <VideoDlg ref="videoDlg" live :ptz="!isMobile()"></VideoDlg>-->
    </div>
</template>

<script>
// import _ from "lodash";
// import VideoDlg from "./videoDlg.vue";
// import { mapState } from "vuex";
export default {
  props: {
    devid: {
      type: String,
      default: ""
    },
    name: {
      type: String,
      default: ""
    }
  },
  data() {
    return {
      q: "",
      online: "",
      channel_type: "",
      dir_serial: "",
      total: 0,
      pageSize: 10,
      currentPage: 1,
      sort: "Channel",
      order: "asc",
      loading: false,
      timer: 0,
      channels: [],
      recorder: null,
      bAudioSending: false,
      bAudioSendError: false,
    };
  },
  computed: {
    // ...mapState(['userInfo', 'serverInfo']),
  },
  watch: {
    q: function(newVal, oldVal) {
      // this.doDelaySearch();
    },
    online: function(newVal, oldVal) {
      // this.doSearch();
    },
    channel_type: function(newVal, oldVal) {
      // this.doSearch();
    },
    dir_serial: function(newVal, oldVal) {
      // this.doSearch();
    },
    currentPage: function(newVal, oldVal) {
      // this.doSearch(newVal);
    }
  },
  // components: {
  //   VideoDlg
  // },
  mounted() {
    // this.$refs["q"].focus();
    this.getChannels();
    this.timer = setInterval(() => {
        this.getChannels();
    }, 10000);//每10秒钟获取一下通道
    $(document).on("mouseup", this.talkStop);
  },
  beforeDestroy() {
    if (this.timer) {
      clearInterval(this.timer);
      this.timer = 0;
    }
    this.talkStop();
    $(document).off("mouseup", this.talkStop);
  },
  methods: {
    isMobile() {
      // return videojs.browser.IS_IOS || videojs.browser.IS_ANDROID;
        return false;
    },
    doSearch(page = 1) {
      var query = {};
      if (this.q) query["q"] = this.q;
      if (this.online) query["online"] = this.online;
      if (this.channel_type) query["channel_type"] = this.channel_type;
      if (this.dir_serial) query["dir_serial"] = this.dir_serial;
      this.$router.replace({
        path: `/devices/channels/${this.devid}/${page}`,
        query: query
      });
    },
    doDelaySearch: _.debounce(function() {
      this.doSearch();
    }, 500),

    getChannels() {
        // let params = new URLSearchParams();
        // params.append("serial", this.devid);
        // params.append("q",this.q);
        // params.append("start", (this.currentPage -1) * this.pageSize);
        // params.append("limit", this.pageSize);
        // params.append("online",this.online);
        // params.append("channel_type", this.channel_type);
        // params.append("dir_serial",this.dir_serial)
        // params.append("sort", this.sort);
        // params.append("order", this.order);
        // console.log(params.toString());
        // this.$http({
        //     method: "get",
        //     url: "/play/api/v1/device/channellist",
        //     data:params
        // }).then(res => {
        //     console.log(res);
        //     this.total = res.data["DeviceCount"];
        //     this.devices = res.data["DeviceList"];
        // }).catch(err => {
        //     console.log(err);
        // });
        this.$http.get(
            "/v1/device/channellist",
            {params:{serial:this.devid}}
        ).then(res => {
            // console.log(res);
            this.total = res.data["ChannelCount"];
            this.channels = res.data["ChannelList"];
        }).catch(err => {
            console.log(err);
        });


    },
    sortChange(data) {
      this.sort = data.prop;
      this.order = data.order == "ascending" ? "asc" : "desc";
      this.getChannels();
    },
    formatName(row, col, cell) {
      if (cell) return cell;
      return "-";
    },
    toggleAudio(row) {
      // $.get("/api/v1/device/setchannelaudio", {
      //   serial: row.DeviceID,
      //   code: row.ID,
      //   audio: !row.AudioEnable,
      // }).then(() => {
      //   row.AudioEnable = !row.AudioEnable;
      // })
    },
    toggleOndemand(row) {
      // $.get("/api/v1/device/setchannelondemand", {
      //   serial: row.DeviceID,
      //   code: row.ID,
      //   ondemand: !row.Ondemand,
      // }).then(() => {
      //   row.Ondemand = !row.Ondemand;
      // })
    },
    toggleCloudRecord(row) {
      // var tip = row.CloudRecord ? "关闭云端录像, 是否同时设置 开启 按需直播?" : "开启云端录像, 是否同时设置 关闭 按需直播?";
      // this.$confirm(tip, "提示", {
      //   distinguishCancelAndClose: true,
      //   cancelButtonText: "不设置",
      // }).then(() => {
      //    $.get("/api/v1/device/setchannelcloudrecord", {
      //     serial: row.DeviceID,
      //     code: row.ID,
      //     cloudrecord: !row.CloudRecord,
      //     ondemand: row.CloudRecord,
      //   }).always(() => {
      //     row.Ondemand = row.CloudRecord;
      //     row.CloudRecord = !row.CloudRecord;
      //   });
      // }).catch(action => {
      //   if(action === 'cancel') {
      //     $.get("/api/v1/device/setchannelcloudrecord", {
      //       serial: row.DeviceID,
      //       code: row.ID,
      //       cloudrecord: !row.CloudRecord,
      //     }).then(() => {
      //       row.CloudRecord = !row.CloudRecord;
      //     })
      //   }
      // });
    },
    toggleShared(row) {
      // $.get("/api/v1/device/setchannelshared", {
      //   serial: row.DeviceID,
      //   code: row.ID,
      //   shared: !row.Shared,
      // }).then(() => {
      //   row.Shared = !row.Shared;
      // })
    },
    talkStart(row) {
      // if(this.recorder) {
      //   return;
      // }
      // LiveRecorder.get((rec, err) => {
      //   if(err) {
      //     alert(err);
      //     return
      //   }
      //
      //   this.recorder = rec;
      //   this.recorder.start();
      // }, {
      //   sampleBits: 16,
      //   sampleRate: 8000,
      //   pcmCallback: pcm => {
      //     if(this.bAudioSendError) return;
      //     var reader = new window.FileReader();
      //     reader.onloadend = () => {
      //       var base64 = reader.result;
      //       var base64 = base64.split(',')[1];
      //       this.bAudioSending = true;
      //       $.get("/api/v1/control/talk", {
      //         serial: row.DeviceID,
      //         code: row.ID,
      //         audio: base64,
      //       }).error(() => {
      //         if(!this.bAudioSendError) {
      //           this.bAudioSendError = true;
      //           setTimeout(() => {
      //             this.bAudioSendError = false;
      //           }, 10000);
      //         }
      //       }).always(() => {
      //         this.bAudioSending = false;
      //       })
      //     }
      //     reader.readAsDataURL(pcm);
      //   }
      // })
    },
    talkStop() {
      // if(this.recorder) {
      //   this.recorder.stop();
      //   this.recorder = null;
      //   return;
      // }
    },
    playStream(row) {
      // this.loading = true;
      // this.$set(row, "Locked", true);
      // $.get("/api/v1/stream/start", {
      //   serial: row.DeviceID,
      //   code: row.ID
      // }).then(streamInfo => {
      //   this.loading = false;
      //   this.$nextTick(() => {
      //     this.$refs["videoDlg"].play(row.Name||row.ID, row.SnapURL||"", row.DeviceID, row.ID, streamInfo);
      //   });
      // }).always(() => {
      //   this.$delete(row, "Locked");
      //   this.loading = false;
      // })
    },
    stopStream(row) {
    //   this.$confirm(`确认停止直播 ${row.Name || row.ID} ?`, "提示").then(() => {
    //     $.get("/api/v1/stream/stop", {
    //       serial: this.devid,
    //       code: row.ID
    //     }).always(() => {
    //       this.getChannels();
    //     });
    //   }).catch(() => {});
    // }
  },
   beforeRouteEnter(to, from, next) {
    next(vm => {
      vm.q = to.query.q || "";
      vm.online = to.query.online || "";
      vm.channel_type = to.query.channel_type || "";
      vm.dir_serial = to.query.dir_serial || "";
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
      this.channel_type = to.query.channel_type || "";
      this.dir_serial = to.query.dir_serial || "";
      this.currentPage = parseInt(to.params.page) || 1;
      this.channels = [];
      this.getChannels();
    });
  }
}
}
</script>

<style >
.opt-group .cell {
  overflow: visible;
}
</style>



