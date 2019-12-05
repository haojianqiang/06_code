<template>
    <div class="">
        <div class="box box-primary">
            <div style="float:right">
                <el-date-picker
                        v-model="playBackDate"
                        type="date"
                        @change="dateChange"
                        placeholder="选择日期">
                </el-date-picker>
            </div>
            <br>
            <LivePlayer live muted :hasaudio="hasAudio" :videoUrl="videoUrl" :currentTime="currentTime" @ended="onVideoEnd" @timeupdate="onVideoTimeUpdate"
              v-loading="videoLoading" element-loading-text="加载中" element-loading-background="#000"
              style="margin:0 auto; max-width:700px;">
            </LivePlayer>
            <br>
            <TimeRule :videos="videos" @timeChange="onTimeChange" ref="timeRule" v-loading="loading" :key="timeRuleIsChange"></TimeRule>
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
      touchTimer: 0,
      playBackDate:"",
      timeRuleIsChange:1//用来重新渲染字组件
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
      ];
        this.getRecords(true);
        this.timeRuleIsChange ++;
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

    dateChange(day) {
        this.day = moment(day).format("YYYYMMDD");
    },
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
        this.loading = true;
        this.records = [];
      }
      if(!this.nextTimeRange()){
        this.videos = this.records;
        this.loading = false;
        return
      }
      this.$http.get(
        "/v1/playback/recordlist",
        {params:{
                timeout : 3,
                serial:this.devid,
                code: this.channel,
                starttime: moment(this.timerange[0]).format("YYYY-MM-DDTHH:mm:ss"),
                endtime: moment(this.timerange[1]).format("YYYY-MM-DDTHH:mm:ss")
        }}
    ).then(res => {
        this.records = this.records.concat(res.data["RecordList"]);
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
              "/v1/playback/stop",
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
            "/v1/playback/start",
            {params:{
                    serial: this.devid,
                    code: this.channel,
                    starttime: this.video.StartTime,
                    endtime: this.video.EndTime
                }}
        ).then(res => {
            // console.log(res);
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

