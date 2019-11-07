<template>
    <div class="">
        <div class="box box-primary">
          <div class="box-header" style="height: 10px">
<!--            <h4 class="text-primary text-center">设备录像时间轴({{devid}}-{{channel}})</h4>-->
          </div>
          <div class="box-body">
            <form class="form-inline">
              <div class="form-group form-group-sm">
                <button type="button" class="btn btn-sm btn-primary" @click.prevent="$router.go(-1)">
                  <i class="fa fa-chevron-left"></i> 返回
                </button>
              </div>
<!--              <div class="form-group pull-right" >-->
<!--                  <div class="input-group input-group-sm" >-->
<!--                      <DatePicker class="form-control" @update:day="updateDay" :day="day" ref="datePicker"></DatePicker>-->
<!--                      <div class="input-group-btn">-->
<!--                          <button type="button" class="btn btn-sm btn-default" @click.prevent="showDatePicker">-->
<!--                            <i class="fa fa-calendar"></i>-->
<!--                          </button>-->
<!--                          <router-link :to="`/devices/playback/list/${this.devid}/${this.channel}/${this.day}`" replace class="btn btn-default btn-sm">-->
<!--                              <i class="fa fa-hand-o-right"></i> 列表视图-->
<!--                          </router-link>-->
<!--                      </div>-->
<!--                  </div>-->
<!--              </div>-->
            </form>
            <br>
            <div class="clearfix"></div>
            <LivePlayer live muted :hasaudio="hasAudio" :videoUrl="videoUrl" :currentTime="currentTime" @ended="onVideoEnd" @timeupdate="onVideoTimeUpdate"
              v-loading="videoLoading" element-loading-text="加载中" element-loading-background="#000"
              style="margin:0 auto; max-width:700px;">
            </LivePlayer>
            <br>
            <br>
            <TimeRule :videos="videos" @timeChange="onTimeChange" ref="timeRule" v-loading="loading"></TimeRule>
            <div class="clearfix"></div>
            <br>
          </div>
        </div>
    </div>
</template>

<script>
// import _ from "lodash";
import moment from "moment";
// import DatePicker from "./datePicker.vue";
import TimeRule from './timeRule.vue'
import LivePlayer from '@liveqing/liveplayer'
// import { mapState } from "vuex";

export default {
  props: {
    devid: {
      type: String,
      default: ""
    },
    channel: {
      type: String,
      default: ""
    },
    mode: {
      type: String,
      default: "timebox"
    },
    day: {
      type: String,
      default: () => moment().format("YYYYMMDD")
    }
  },
  data() {
    return {
      timerange: [
        moment(this.day, "YYYYMMDD").startOf('hour').toDate(),
        moment(this.day, "YYYYMMDD").startOf('hour').toDate()
      ],
      videoLoading: false,
      loading: false,
      records: [],
      currentTime: null,
      videos: [],
      video: null,
      videoUrl: "",
      hasAudio: false,
      streamID: "",
      touchTimer: 0
    };
  },
  // computed: {
  //   ...mapState(['userInfo', 'serverInfo']),
  // },
  watch: {
    day: function(newVal, oldVal) {
      this.timerange = [
        moment(this.day, "YYYYMMDD").startOf('hour').toDate(),
        moment(this.day, "YYYYMMDD").startOf('hour').toDate()
      ]
    },
    video: function(newVal, oldVal) {
      if(newVal && newVal != oldVal) {
        this.startPlayback();
      } else {
        this.stopPlayback();
      }
    }
  },
  components: {
     LivePlayer, TimeRule
  },
  methods: {
    isMobile() {
        let flag = navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i)
        return flag;
    },
    showDatePicker() {
      $(this.$refs.datePicker.$el).focus();
    },
    // updateDay(day) {
    //   this.$router.replace(`/devices/playback/${this.mode}/${this.devid}/${this.channel}/${day}`);
    // },
    nextTimeRange() {
      var end = moment(this.day, "YYYYMMDD").add(24, 'hours');
      var now = moment().startOf("second");
      if(end.isAfter(now, "second")) {
        end = now;
      }
      var r1 = moment(this.timerange[1]);
      if(r1.isSameOrAfter(end, "second")){
        return false;
      }
      var r2 = moment(this.timerange[1]).add(6, 'hours');
      if(r2.isAfter(end)) {
        r2 = end;
      }
      if(r2.startOf("minute").isSameOrBefore(r1.startOf("minute"), "second")) {
        return false;
      }
      this.timerange = [r1.toDate(), r2.toDate()];
      return true;
    },
    getRecords(refresh) {
      if(refresh) {
          console.log(1);
        this.loading = true;
        this.records = [];
      }
      if(!this.nextTimeRange()){
          console.log(2);
        this.videos = this.records;
        this.loading = false;
        return
      }
      this.$http.get(
        "/play/api/v1/playback/recordlist",
        {params:{
                timeout : 3,
                serial:this.devid,
                code: this.channel,
                starttime: moment(this.timerange[0]).format("YYYY-MM-DDTHH:mm:ss"),
                endtime: moment(this.timerange[1]).format("YYYY-MM-DDTHH:mm:ss")
        }}
    ).then(res => {
        this.records = this.records.concat(res.data["RecordList"]);
        console.log("记录："+this.records);
    }).catch(err => {
        console.log(err);
    }).finally(()=>{
          this.$nextTick(() => {
            this.getRecords(false);
          })
    });
    },
    formatName(row, col, cell) {
      if (cell) return cell;
      return "-";
    },
    stopPlayback() {
      return new Promise((resolve, reject) => {
          if(this.touchTimer) {
            clearInterval(this.touchTimer);
            this.touchTimer = 0;
          }
          if(!this.streamID) {
            resolve();
            return
          }
           this.$http.get(
              "/play/api/v1/playback/stop",
              {params:{streamid: this.streamID}}
          ).catch(err => {
              console.log(err);
          }).finally(()=>{
              this.streamID = "";
              this.videoUrl = "";
              resolve();
          });
      })
    },
    async startPlayback() {
      await this.stopPlayback();
      if(!this.video) return;
      this.videoLoading = true;
      this.$http.get(
            "/play/api/v1/playback/start",
            {params:{
                    serial: this.devid,
                    code: this.channel,
                    starttime: this.video.StartTime,
                    endtime: this.video.EndTime
                }}
        ).then(res => {
            console.log(res);
            var videoUrl = this.isMobile() ? res.data.HLS : res.data.RTMP;
            if( res.data.FLV) {
                videoUrl = res.data.FLV;
            }
            this.streamID = res.data.StreamID;
            this.hasAudio = res.data.AudioEnable && res.data.SourceAudioCodecName != "";
            this.videoUrl = videoUrl;
        }).catch(err => {
            console.log(err);
        }).finally(()=>{
            this.videoLoading = false;
        });

    },
    onTimeChange(video) {
        this.video = video;
    },
    onVideoEnd() {

    },
    onVideoTimeUpdate() {

    }
  },
  mounted() {
    let mmt = moment();
    let n = mmt.diff(mmt.clone().startOf('day'), 'minutes');
    n -= 10;
    if(n < 0) n = 0;
    this.$refs.timeRule.clickMinute(n);
    this.getRecords(true);
  },
  beforeDestroy() {
    this.stopPlayback();
  },
  beforeRouteLeave(to, from, next) {
    this.stopPlayback();
    next();
  },
  beforeRouteUpdate(to, from, next) {
    this.stopPlayback();
    next();
    this.$nextTick(() => {
      this.getRecords(true);
    })
  }
};
</script>

