<template >
  <div class="bgInfo" :style="bgInfo">
    <el-container>
    <!-- <el-header class="bgLogo" :style="bgLogo"></el-header> -->
     <el-header>
        <div class="left">
            <el-col :span="16" class="headCol">
                <span>2019年08月22号 星期四</span>
            </el-col>
            <el-col :span="4" class="headCol">
                <img src="../assets/image/cloudy-icon.png" alt="多云" class="content-logo">
            </el-col>
        </div>
        <div class="center" :style="center"></div>
        <div class="right">
            <el-col :span="4" class="headCol">
                <img src="../assets/image/tips-icon.png" alt="提示" class="content-logo">
            </el-col>
            <el-col :span="16" class="headCol">
                <span>公告:关于肉鸡养殖......</span>
            </el-col>
            <el-col :span="4">
                <router-link to="/index" style="color:white">返回</router-link>
            </el-col>

        </div>
    </el-header>
    <el-container>
      <!-- <el-aside width="200px">Aside</el-aside> -->
      <el-main>
       <div>
        <el-row >
          <el-col :span="24">
            <div class="myBorder" ref="leftOne" id="leftOne">
              <div class="whole-title" style="margin-bottom:2%;">
                  <span class="title-text">气象信息</span>
              </div>
              <el-row>
                 <!-- <div>
                   <img src="../assets/image/cloudy-icon.png"  class="content-logo">
                 </div>
                 <div class="span-text-1">
                   <span>息烽县</span>
                   <span>25----34℃</span>
                 </div>
                 <div class="span-text-1">
                   <span>34℃</span>
                 </div> -->
                 <el-col :span="4">
                   <img src="../assets/image/cloudy-icon.png"  class="content-logo">
                 </el-col>
                 <el-col class="span-text-1" :span="10">
                   <span class="span-text-2">息烽县</span>
                   <br/>
                   <span >25----34℃</span>
                 </el-col>
                 <el-col class="span-text-1" :span="10">
                    <span class="span-text-5">34℃</span>
                 </el-col>
              </el-row>
              <el-row class="span-text-color-1" style="margin: 1%">
                <span>息烽县,贵州省贵阳市辖县,位于东经106°27'至106°53',北纬26°57'至27°19'之间</span>
              </el-row>
              <el-row>
                <el-row>
                  <el-col class="span-text-1" :span="10">
                    <img src="../assets/image/humidity-icon.png">
                    <span>相对湿度：62%</span>
                  </el-col>
                  <el-col class="span-text-1" :span="14">
                    <img src="../assets/image/wind-icon.png">
                      <span>风力风向：西南风1级</span>
                  </el-col>
                </el-row>
                <el-row>
                <el-col class="span-text-1" :span="10">
                  <img src="../assets/image/air-icon.png">
                    <span>空气质量：良</span>
                  </el-col>
                 <el-col class="span-text-1" :span="12">
                   <img src="../assets/image/rain-icon.png">
                    <span>总降水量：20.6mm</span>
                  </el-col>
                </el-row>
              </el-row>
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
              <div class="myBorder" ref="leftTwo" id="leftTwo">
                <div class="whole-title" style="margin-bottom:4%">
                  <span class="title-text">肉鸡出存栏信息</span>
              </div>
              <div style="margin-bottom:4%" class="myStyle">
                <span class="span-text-1">时间：</span>
                <el-date-picker
                  v-model="startTime"
                  size="small"
                  type="date"
                  placeholder="选择日期">
                </el-date-picker>
                <span>---</span>
                <el-date-picker
                  v-model="endTime"
                  size="small"
                  type="date"
                  placeholder="选择日期">
                </el-date-picker>
              </div>
                 <div id="chartColumn"></div>
              </div>
        
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <div class="myBorder" ref="leftThree" id="leftThree">
              <div class="whole-title">
                  <span class="title-text">存栏量信息</span>
              </div>
              <div id="drawDepositInfo"></div>
            </div> 
          </el-col>
        </el-row>
      </div>
      </el-main>

      <el-main>
        <div class="myBorder myStyle">
          <el-row>
            <el-col :span="6">
              <div class="span-text-1">区域城市:</div>
                <el-input v-model="city" size="small" placeholder="请输入内容"></el-input>
            </el-col>

              <el-col :span="6">
              <div class="span-text-1">鸡场:</div>
                <el-select v-model="chickenFarm" size="small" clearable  placeholder="请选择">
                <el-option
                  v-for="item in farmOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>

              <el-col :span="6">
              <div class="span-text-1">鸡棚:</div>
                <el-select v-model="chickenHouse" size="small" clearable  placeholder="请选择">
                  <el-option
                    v-for="item in houseOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
            </el-col>
          </el-row>
          <el-row>
            <!-- tableBoder myPadding -->
            <div class="myPadding">
                <el-table
                :data="warningInfo"
                :row-style="warningTableRowStyle"
                :header-cell-style="warnIngTableHeaderColor"
                :cell-style="colColor"
                height="130"
                
                style="width: 100%">
                <el-table-column label="预警信息">
                   <el-table-column
                  prop="chickenFarm"
                  label="鸡场"
                  width="180">
                </el-table-column>
                <el-table-column
                  prop="chickenHouse"
                  label="鸡棚"
                  width="180">
                </el-table-column>
                <el-table-column
                  prop="warningItems"
                  label="预警项">
                </el-table-column>
                 <el-table-column
                  prop="warningValue"
                  label="值">
                </el-table-column>
                </el-table-column>      
            </el-table>
            </div>
          </el-row>
          <el-row>
               <el-col :span="12" class="myPadding middleEchartsSize">
               <div class="myBorder">
              <div class="whole-title">
                  <span class="title-text">温度</span>
              </div>
              <div id="drawTemperature"></div>
            </div> 
            </el-col>
            <el-col :span="12" class="myPadding middleEchartsSize">
               <div class="myBorder">
              <div class="whole-title">
                  <span class="title-text">湿度</span>
              </div>
              <div id="drawHumidity" style="width:100%; height:200px;"></div>
            </div> 
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" class="myPadding">
               <div class="myBorder">
              <div class="whole-title">
                  <span class="title-text">二氧化碳</span>
              </div>
              <div id="drawCO2" style=""></div>
            </div> 
            </el-col>
            <el-col :span="12" class="myPadding">
               <div class="myBorder">
              <div class="whole-title">
                  <span class="title-text">氨气</span>
              </div>
              <div id="drawAmmonia" style=""></div>
            </div> 
            </el-col>
          </el-row>
        </div>
      </el-main>
      <el-main>
        <div class="myBorder">
            <div class="whole-title">
                <span class="title-text">生产信息</span>
            </div>
          <el-row>
            <div class="span-text-2"><span class="bullStyle">&bull;</span>成交信息</div>
                 <div id="drawProduction" ></div>
          </el-row>
          <el-row>
            <div class="span-text-2"><span class="bullStyle">&bull;</span>育成率、死亡率百分比</div>
            <el-col :span="12">
              <div id="drawBreedingRate" style=""></div>
            </el-col>
            <el-col :span="12">
              <div id="drawMortality" style=""></div>
            </el-col>
          </el-row>
          <el-row>
            <el-col>
              <div class="span-text-2"><span class="bullStyle">&bull;</span>饲料及药物用量</div>
               <div class="myStyle">
                 <span class="span-text-1">时间：</span>
                <el-date-picker
                  v-model="startTimeTwo"
                  size="small"
                  type="date"
                  placeholder="选择日期">
                </el-date-picker>
                <span>---</span>
                <el-date-picker
                  v-model="endTimeTwo"
                  size="small"
                  type="date"
                  placeholder="选择日期">
                </el-date-picker>
               </div>
            </el-col>
          </el-row>
          <div style="margin: 2%" class="myStyle">
              <el-table
                :data="feedAndMedicine"
                height="150"
                :row-style="tableRowStyle"
                :header-cell-style="tableHeaderColor"
                :cell-style="cellStyle"
                border
                style="width: 100%">
                  <el-table-column
                  prop="feedName"
                  label="饲料名称"
                  >
                </el-table-column>
                <el-table-column
                  prop="feedDosage"
                  label="饲料用量"
                  >
                </el-table-column>
                <el-table-column
                  prop="medicineName"
                  label="药物名称">
                </el-table-column>
                 <el-table-column
                  prop="medicineDosage"
                  label="药物用量">
                </el-table-column>          
            </el-table>
          </div>
         
        </div>
      </el-main>
    </el-container>
  </el-container>
  </div>
</template>
<script>
  import echarts from 'echarts'
export default {
    data() {
            return {
              bgInfo:{
                  backgroundImage: "url(" + require("../assets/image/bg.png") + ")",
                  backgroundRepeat: "no-repeat",
                  backgroundPosition: "25px auto",
              },
              center:{
                  backgroundImage: "url(" + require("../assets/image/bg-logo.png") + ")",
                  backgroundRepeat: "no-repeat",
                  backgroundPosition: "center 1vw",
                  backgroundSize:"40vw 3vw",
            
              },
                chartColumn: null,
                drawDepositInfo:null,
                drawTemperature:null,
                drawHumidity:null,
                drawCO2:null,
                drawAmmonia:null,
                startTime:'',
                endTime:'',
                startTimeTwo:'',
                endTimeTwo:'',
                city:'', 
                

                farmOptions: [{
                value: '1',
                label: '1号鸡场'
                  }, {
                value: '2',
                label: '2号鸡场'
                  }, {
                value: '3',
                label: '3号鸡场'
                  }],
                chickenFarm:'',
                houseOptions: [{
                value: '1',
                label: '1号鸡棚'
                  }, {
                value: '2',
                label: '2号鸡棚'
                  }, {
                value: '3',
                label: '3号鸡棚'
                  }],    
              chickenHouse: '',

              //预警信息表格数据
              warningInfo:[
                {
                  chickenFarm:'息烽县1号鸡场',
                  chickenHouse:'1号鸡棚',
                  warningItems:'XXXXX',
                  warningValue:'XXXXX'
                },
                {
                  chickenFarm:'息烽县1号鸡场',
                  chickenHouse:'1号鸡棚',
                  warningItems:'XXXXX',
                  warningValue:'XXXXX'
                },
                {
                  chickenFarm:'息烽县1号鸡场',
                  chickenHouse:'1号鸡棚',
                  warningItems:'XXXXX',
                  warningValue:'XXXXX'
                },
                {
                  chickenFarm:'息烽县1号鸡场',
                  chickenHouse:'1号鸡棚',
                  warningItems:'XXXXX',
                  warningValue:'XXXXX'
                },
                {
                  chickenFarm:'息烽县1号鸡场',
                  chickenHouse:'1号鸡棚',
                  warningItems:'XXXXX',
                  warningValue:'XXXXX'
                },
                {
                  chickenFarm:'息烽县1号鸡场',
                  chickenHouse:'1号鸡棚',
                  warningItems:'XXXXX',
                  warningValue:'XXXXX'
                },
                {
                  chickenFarm:'息烽县1号鸡场',
                  chickenHouse:'1号鸡棚',
                  warningItems:'XXXXX',
                  warningValue:'XXXXX'
                },
              ],
              //饲料及药物用量表格数据
              feedAndMedicine:[
                {
                  feedName:'国产鱼粉',
                  feedDosage:'10',
                  medicineName:'氟氧沙星',
                  medicineDosage:'20',
                },
                 {
                  feedName:'国产鱼粉',
                  feedDosage:'10',
                  medicineName:'氟氧沙星',
                  medicineDosage:'20',
                },
                 {
                  feedName:'国产鱼粉',
                  feedDosage:'10',
                  medicineName:'氟氧沙星',
                  medicineDosage:'20',
                },
                 {
                  feedName:'国产鱼粉',
                  feedDosage:'10',
                  medicineName:'氟氧沙星',
                  medicineDosage:'20',
                },
                 {
                  feedName:'国产鱼粉',
                  feedDosage:'10',
                  medicineName:'氟氧沙星',
                  medicineDosage:'20',
                },
                
              ],
              screenHeight: document.body.clientHeight,

              // contentStyleObj:{
              //   leftOneHeight:'',
              //   leftTwoHeight:''
              // }
            }
        },

        methods: {
          // 肉鸡出存栏信息
            drawColumnChart() {
                this.chartColumn = echarts.init(document.getElementById('chartColumn'));
                this.chartColumn.setOption({
                  // title: { text: 'Column Chart' },
                  grid:{ 
                    bottom:'15%',
                  },
                  legend:{
                    itemGap: 40,
                    data:['存栏量','成交量'],
                    textStyle:{
                        color: '#ffffff',//字体颜色
                        fontSize:14
                    },
                  },  
                  tooltip: {},
                  xAxis: {
                      data: ["红羽","黄羽","白羽","固始","茶花"],
                      axisLine: {
                          lineStyle: {
                            color: '#ffffff',
                            
                        },
                    },
                    scale: true,
                    splitNumber:10,
      boundaryGap: [0.2, 0.2]
                  },
                  yAxis: {
                     type: 'value',
                     name: '单位:万羽',
                     min: 0,
                     max: 80,
                     interval: 20,
                     axisLabel: {
                       formatter: '{value}'
                     },
                     splitLine:{
                      show:false
                    },
                    axisLine: {
                            lineStyle: {
                                color: '#ffffff'
                            }
                        }    
                  },
                  series: [
                    {
                     name:"存栏量",
                     type:'bar',
                     data: [15, 25, 40, 20, 18],
                     barWidth:12,
                     barMaxWidth:15,
                       itemStyle: {
                           normal: {
                                label:{
                                
                                textStyle:{
                                color:'#fff',
                                fontSize:12,
                                    }
                                  },
                                color: new echarts.graphic.LinearGradient(0,1,0,0,[
                                      {offset:0,color:"#F5DEB3"},
                                      {offset:0.6,color:"#FFDEAD"},
                                      {offset:1,color:"#FFE4C4"},
                                    ])
                              },
                        },
                    },
                     {
                     name:"成交量",
                     type:'bar',
                     barWidth:12,
                     barMaxWidth:15,
                     data: [5, 20, 36, 10, 10],
                      itemStyle: {
                           normal: {
                                label:{
                                
                                textStyle:{
                                color:'#fff',
                                fontSize:12,
                                    }
                                  },
                                color: new echarts.graphic.LinearGradient(0,1,0,0,[
                                      {offset:0,color:"#AFEEEE"},
                                      {offset:0.6,color:"#87CECB"},
                                      {offset:1,color:"#48D1CC"},
                                    ])
                              },
                        },
                    }
                    ]
                });
            },
          //存栏量信息
           drawDepositInfoChart(){
                this.drawDepositInfo = echarts.init(document.getElementById('drawDepositInfo'));
                 this.drawDepositInfo.setOption({
                  grid:{
                    top:'20%',
                    bottom:'12%',
                    left:'15%'
                  },
                  tooltip: {
                    trigger:'axis',
                    axisPointer:{
                      type:"cross",
                      label:{
                        backgroundColor: '#6a7985'
                      }
                    }
                  },
                  xAxis: {
                      type : 'category',
                      boundaryGap : false,
                      data: ["1月份", "2月份", "3月份", "4月份"],
                       axisLine: {
                          lineStyle: {
                            color: '#ffffff',
                        },
                    },
                  },
                  yAxis: {
                     type: 'value',  
                     axisLine: {
                            lineStyle: {
                                color: '#ffffff'
                            }
                        },
                        splitLine:{
                      show:false
                    },
                  },
                  
                  series: [
                    {
                     name:"存栏量",
                       type:'line',
                      smooth: true,
                      symbol: 'circle',
                      symbolSize: 5,
                      sampling: 'average',
                      itemStyle: {
                          normal: {
                              color: '#8ec6ad'
                          }
                            },
                    stack: 'a',
                      areaStyle: {
                        normal: {
                            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                offset: 0,
                                color: '#8ec6ad'
                            }, {
                                offset: 1,
                                color: '#ffe'
                            }])
                        }
                    },
                     data: [4684, 4698, 6832, 10799]
                    }
                    ]
                });
           },

           // 温度
            drawTemperatureChart() {
                this.drawTemperature = echarts.init(document.getElementById('drawTemperature'));
                this.drawTemperature.setOption({
                  // title: { text: 'Column Chart' },
                 
                  grid:{
                   left:'15%',
                    bottom:'15%'
                  },
                  tooltip: {},
                  xAxis: {
                      data: ["10.00", "12.00", "14.00", "16.00", "18.00"],
                      axisLine: {
                          lineStyle: {
                            color: '#ffffff',
                        },
                    },
                    
                  },
                  yAxis: {
                     type: 'value',
                     name: '',
                     min: 0,
                     max: 1800,
                     interval: 600,
                     axisLabel: {
                       formatter: '{value}'
                     },
                     splitLine:{
                      show:false
                    },
                    axisLine: {
                            lineStyle: {
                                color: '#ffffff'
                            }
                        }    
                  },
                  series: [
                    {
                     name:"",
                     type:'bar',
                     barWidth:12,
                     barMaxWidth:15,
                     data: [800, 900, 1300, 1700, 1100],
                       itemStyle: {
                           normal: {
                                barBorderRadius:[8, 8, 0, 0], // 设置柱状图圆角
                                label:{
                                textStyle:{
                                color:'#fff',
                                fontSize:12,
                                    }
                                  },
                                color: new echarts.graphic.LinearGradient(0,1,0,0,[
                                      {offset:0,color:"#1874CD"},
                                      {offset:0.6,color:"#1874CD"},
                                      {offset:1,color:"#1C86EE"},
                                    ])
                              },
                        },
                    },
                       {
                     name:"",
                     type:'line',
                     data: [800, 900, 1300, 1700, 1100],
                       itemStyle: {
                           normal: {
                                label:{
                                textStyle:{
                                color:'#fff',
                                fontSize:12,
                                    }
                                  },
                                color: new echarts.graphic.LinearGradient(0,1,0,0,[
                                      {offset:0,color:"#EEC900"},
                                      {offset:0.6,color:"#EEC900"},
                                      {offset:1,color:"#EEC900"},
                                    ])
                              },
                        },
                    }
                    ]
                });
            },

            // 湿度
            drawHumidityChart() {
                this.drawHumidity = echarts.init(document.getElementById('drawHumidity'));
                this.drawHumidity.setOption({
                  // title: { text: 'Column Chart' },
                  legend:{
     
                    data:['1号鸡棚','2号鸡棚','3号鸡棚'],
                    textStyle:{
                        color: '#ffffff',//字体颜色
                        fontSize:'12px'
                    },
                  },
                  grid:{
                    left:'15%',
                    bottom:'15%'
                  },
                  tooltip: {},
                  xAxis: {
                      data: ["10.00", "12.00", "14.00", "16.00", "18.00"],
                      axisLine: {
                          lineStyle: {
                            color: '#ffffff',
                        },
                    },
                    
                  },
                  yAxis: {
                     type: 'value',
                     name: '',
                     min: 0,
                     max: 1800,
                     interval: 600,
                     axisLabel: {
                       formatter: '{value}'
                     },
                     splitLine:{
                      show:false
                    },
                    axisLine: {
                            lineStyle: {
                                color: '#ffffff'
                            }
                        }    
                  },
                  series: [
                    {
                     name:"1号鸡棚",
                     type:'line',
                     data: [854, 984, 740, 1205, 950],
                       itemStyle: {
                           normal: {
                                label:{
  
                                textStyle:{
                                color:'#fff',
                                fontSize:12,
                                    }
                                  },
                                color: new echarts.graphic.LinearGradient(0,1,0,0,[
                                      {offset:0,color:"#F5DEB3"},
                                      {offset:0.6,color:"#FFDEAD"},
                                      {offset:1,color:"#FFE4C4"},
                                    ])
                              },
                        },
                    },
                     {
                     name:"2号鸡棚",
                     type:'line',
                     data: [1300, 700, 1380, 1654, 1780],
                      itemStyle: {
                           normal: {
                                label:{
                                // show:true,
                                // position:'top',
                                textStyle:{
                                color:'#fff',
                                fontSize:12,
                                    }
                                  },
                                color: new echarts.graphic.LinearGradient(0,1,0,0,[
                                      {offset:0,color:"#AFEEEE"},
                                      {offset:0.6,color:"#87CECB"},
                                      {offset:1,color:"#48D1CC"},
                                    ])
                              },
                        },
                    },
                     {
                     name:"3号鸡棚",
                     type:'line',
                     data: [575, 741, 805, 940, 1054],
                      itemStyle: {
                           normal: {
                                label:{
                                // show:true,
                                // position:'top',
                                textStyle:{
                                color:'#fff',
                                fontSize:12,
                                    }
                                  },
                                color: new echarts.graphic.LinearGradient(0,1,0,0,[
                                      {offset:0,color:"#AFEEEE"},
                                      {offset:0.6,color:"#87CECB"},
                                      {offset:1,color:"#48D1CC"},
                                    ])
                              },
                        },
                    }
                    ]
                });
            },

            //二氧化碳
           drawCO2Echart() {
                this.drawCO2 = echarts.init(document.getElementById('drawCO2'));
                this.drawCO2.setOption({
                  // title: { text: 'Column Chart' },
                    tooltip: {
                    trigger:'axis',
                    axisPointer:{
                      type:"cross",
                      label:{
                        backgroundColor: '#6a7985'
                      }
                    }
                  },
                  grid:{    
                    left:'15%',
                    bottom:'15%'
                  },
                
                  xAxis: {
                     type : 'category',
                     boundaryGap : false,
                      data: ["10.00", "12.00", "14.00", "16.00", "18.00"],
                      axisLine: {
                          lineStyle: {
                            color: '#ffffff',
                        },
                    },
                    
                  },
                  yAxis: {
                     type: 'value',
                    
                     min: 0,
                     max: 1800,
                     interval: 600,
                     axisLabel: {
                       formatter: '{value}'
                     },
                     splitLine:{
                      show:false
                    },
                    axisLine: {
                            lineStyle: {
                                color: '#ffffff'
                            }
                        }    
                  },
                  series: [
                    {
                     name:"",
                     type:'line',
                    
                     areaStyle: {},
                     data: [150, 450, 640, 1120, 718],
                       itemStyle: {
                           normal: {
                                color: new echarts.graphic.LinearGradient(0,1,0,0,[
                                      {offset:0,color:"#F5DEB3"},
                                      {offset:0.6,color:"#FFDEAD"},
                                      {offset:1,color:"#FFE4C4"},
                                    ])
                              },
                        },
                    },
                     {
                     name:"",
                     type:'line',
                    
                     areaStyle: {},
                     data: [756, 546, 987, 1678, 840],
                      itemStyle: {
                           normal: {
                               
                                color: new echarts.graphic.LinearGradient(0,1,0,0,[
                                      {offset:0,color:"#FF4040"},
                                      {offset:0.6,color:"#FF4500"},
                                      {offset:1,color:"#FF3030"},
                                    ])
                              },
                        },
                    },
                     {
                     name:"",
                     type:'line',
                    
                     areaStyle: {},
                     data: [456, 646, 787, 1278, 240],
                      itemStyle: {
                           normal: {
                               
                                color: new echarts.graphic.LinearGradient(0,1,0,0,[
                                      {offset:0,color:"#CAE1FF"},
                                      {offset:0.6,color:"#C6E2FF"},
                                      {offset:1,color:"#B0E2FF"},
                                    ])
                              },
                        },
                    }
                    ]
                });
            },
          //氨气
            drawAmmoniaEchart() {
                this.drawAmmonia = echarts.init(document.getElementById('drawAmmonia'));
                this.drawAmmonia.setOption({
                  // title: { text: 'Column Chart' },
                   barWidth:12,
                    barMaxWidth:15,
                  grid:{
                   left:'15%',
                    bottom:'15%'
                  },
                  tooltip: {},
                  xAxis: {
                      data: ["10.00", "12.00", "14.00", "16.00", "18.00"],
                      axisLine: {
                          lineStyle: {
                            color: '#ffffff',
                        },
                    },
                    
                  },
                  yAxis: {
                     type: 'value',
                     name: '',
                     min: 0,
                     max: 1800,
                     interval: 600,
                     axisLabel: {
                       formatter: '{value}'
                     },
                     splitLine:{
                      show:false
                    },
                    axisLine: {
                            lineStyle: {
                                color: '#ffffff'
                            }
                        }    
                  },
                  series: [
                    {
                     name:"",
                     type:'bar',
                     data: [1300, 1250, 1150, 1350, 1350],
                       itemStyle: {
                           normal: {
                                label:{
                               
                                textStyle:{
                                color:'#fff',
                                fontSize:12,
                                    }
                                  },
                                color: new echarts.graphic.LinearGradient(0,1,0,0,[
                                      {offset:0,color:"#4169E1"},
                                      {offset:0.6,color:"#4169E1"},
                                      {offset:1,color:"#6495ED"},
                                    ],
                                  )

                                // color:function(params){
                                //   var colorList = ['rgb(65,105,225,0.5)','rgb(255,193,37,0.5)','rgb(99,184,255,0.5)','rgb(255,127,80,0.5)'];
                                //    return colorList[params.dataIndex];
                                // }
                               
                              },
                        },
                    },
                    ]
                });
            },
          //成交信息
            drawProductionChart() {
                this.drawProduction = echarts.init(document.getElementById('drawProduction'));
                this.drawProduction.setOption({
                  // title: { text: 'Column Chart' },
                  legend:{
                    data:['成交量','成交金额'],
                      itemGap: 40,
                    textStyle:{
                        color: '#ffffff',//字体颜色
                        fontSize:'100%'
                    },
                  },
                  grid:{
                    top:'10%',
                    bottom:'20%'
                  },
                  tooltip: {},
                  xAxis: {
                      data: ["1月", "2月", "3月", "4月", "5月"],
                      axisLine: {
                          lineStyle: {
                            color: '#ffffff',
                        },
                    },
                    
                  },
                  yAxis: {
                     type: 'value',
                     name: '单位:%',
                     min: 0,
                     max: 80,
                     interval: 20,
                     axisLabel: {
                       formatter: '{value}'
                     },
                     splitLine:{
                      show:false
                    },
                    axisLine: {
                            lineStyle: {
                                color: '#ffffff'
                            }
                        }    
                  },
                  series: [
                    {
                     name:"成交量",
                     type:'bar',
                     data: [15, 25, 40, 20, 18],
                       itemStyle: {
                           normal: {
                                label:{
                                textStyle:{
                                color:'#fff',
                                fontSize:12,
                                    }
                                  },
                                color: new echarts.graphic.LinearGradient(0,1,0,0,[
                                      {offset:0,color:"#F5DEB3"},
                                      {offset:0.6,color:"#FFDEAD"},
                                      {offset:1,color:"#FFE4C4"},
                                    ])
                              },
                        },
                    },
                     {
                     name:"成交金额",
                     type:'bar',
                     data: [5, 20, 36, 10, 10],
                      itemStyle: {
                           normal: {
                                label:{
                                textStyle:{
                                color:'#fff',
                                fontSize:12,
                                    }
                                  },
                                color: new echarts.graphic.LinearGradient(0,1,0,0,[
                                      {offset:0,color:"#AFEEEE"},
                                      {offset:0.6,color:"#87CECB"},
                                      {offset:1,color:"#48D1CC"},
                                    ])
                              },
                        },
                    }
                    ]
                });
            },

            //育成率(扇形仿仪表盘)
            drawBreedingRateChart(){
              this.drawBreedingRate = echarts.init(document.getElementById('drawBreedingRate'));
              this.drawBreedingRate.setOption({
//                 title:[
//                   {
//                     text:'育成率',
//                     textAlign:'center',
//                     x:'center',
//                     top:'25',
//                     textStyle: {//主标题文本样式{"fontSize": 18,"fontWeight": "bolder","color": "#333"}
//                     fontSize: 16,
//                     fontStyle: 'normal',
//                     fontWeight: 'normal',
//                     color:'#97CBFF'
//                },
//                   }
//                 ],
               tooltip: {
                  trigger: 'item',
                  formatter: "{c}",// {b}:数据名； {c}：数据值； {d}：百分比，可以自定义显示内容，
                  textStyle:{
                  fontSize:12
                  }
                }, 
                   
                grid:{
                    top:'0%',
                    left:'10%',
                    bottom:"80%"
                },
                legend:{
                  show:false
                },
                series:[
                 {
                  name:'育成率',
                  type:'pie',
                  radius: ['80%%', '47%'],
                  center: ['40%', '80%'],
                  hoverAnimation: false,
                  avoidLabelOverlap: false,
                  startAngle: 0, 
                  label: {
                    normal: {
                      show:true,
                      position: 'center',
                      color: '#97CBFF',
                      fontSize: 16,
                      fontWeight: 'bold',
                      //formatter: '{{d}%\n{b}}' // {b}:数据名； {c}：数据值； {d}：百分比，可以自定义显示内容，
                      formatter:function(param){
                      if (!param.percent) return ''
                      if(param.dataIndex == 1){
                      return param.value +'\n'+ param.seriesName;
                      }
                    }
            }
        },
        labelLine: {
            normal: {
                show: false
            }
        },
                  // 由三个扇形 根据数值组成
                  data:[
                    //隐藏掉的半圆 等于下面两个数据之和
                    {
                      value:60,
                      name:'',
                      itemStyle:{
                        normal:{
                          color: "rgba(80,150,224,0)"
                        }
                      },
                      
                    },
                    //根据值动态填充
                    {
                      value:42,
                      name:'育成率',
                      itemStyle:{
                        normal:{
                          color: "rgba(80,150,224,1)",
                          show:true,
                          formatter:'{c}',
                          position:'inner'
                        }
                      }
                    },
                    //阴影轮廓
                    {
                      value:18,
                      name:'',
                      itemStyle:{
                        normal:{
                          color: "rgba(80,150,224,0.1)"
                        }
                      }
                    },
                  ],
                  itemStyle:{
                    normal:{
                      borderWidth:'1',
                      shadowBlur:10,
                      shadowColor:'#173b56'
                    }
                  }
                 }
                ],
                // color:['#3e424b',
                //     new echarts.graphic.LinearGradient(0, 0, 0, 1,
                //         [
                //             {offset: 0, color: '#00cdbe'},
                //             {offset: 0.5, color: '#09abc2'},
                //             {offset: 0.8, color: '#1785c6'},
                //             {offset: 1, color: '#1785c6'}
                //         ],false)]
              })
            },
            //死亡率(扇形仿仪表盘)
             drawMortalityChart(){
              this.drawMortality = echarts.init(document.getElementById('drawMortality'));
              this.drawMortality.setOption({
               tooltip: {
                  trigger: 'item',
                  formatter: "{c}",// {b}:数据名； {c}：数据值； {d}：百分比，可以自定义显示内容，
                  textStyle:{
                  fontSize:12
                  }
                }, 
                   
                grid:{
                    top:'30%',
                    left:'30%',
                    bottom:"40%"
                },
                legend:{
                  shiw:false
                },
                series:[
                 {
                  name:'死亡率',
                  type:'pie',
                  radius: ['80%%', '47%'],
                  center: ['40%', '80%'],
                  hoverAnimation: false,
                  avoidLabelOverlap: false,
                  
                  startAngle: 0, 
                  label: {
                    normal: {
                      show:true,
                      position: 'center',
                      color: '#97CBFF',
                      fontSize: 16,
                      fontWeight: 'bold',
                      //formatter: '{{d}%\n{b}}' // {b}:数据名； {c}：数据值； {d}：百分比，可以自定义显示内容，
                      formatter:function(param){
                      if (!param.percent) return ''
                      if(param.dataIndex == 1){
                      return param.value +'\n'+ param.seriesName;
                      }
                    }
            }
        },
                  labelLine:{
                    normal:{
                      show:false
                    }
                  },
                  data:[
                    {
                      value:300,
                      itemStyle:{
                        normal:{
                          color: "rgba(80,150,224,0)"
                        }
                      }
                    },
                    {
                      value:270,
                      itemStyle:{
                        normal:{
                          color: "rgba(	255,140,0,1)"
                        }
                      }
                    },
                    {
                      value:30,
                      itemStyle:{
                        normal:{
                          color: "rgba(80,150,224,0.1)"
                        }
                      }
                    },
                  ],
                  itemStyle:{
                    normal:{
                      borderWidth:'1',
                      shadowBlur:10,
                      shadowColor:'#173b56'
                    }
                  }
                 }
                ],
                // color:['#3e424b',
                //     new echarts.graphic.LinearGradient(0, 0, 0, 1,
                //         [
                //             {offset: 0, color: '#00cdbe'},
                //             {offset: 0.5, color: '#09abc2'},
                //             {offset: 0.8, color: '#1785c6'},
                //             {offset: 1, color: '#1785c6'}
                //         ],false)]
              })
            },
            drawCharts() {                               
                let echartDivSize = document.getElementsByClassName('middleEchartsSize');

                //肉鸡出存栏信息
                let chartColumn = document.getElementById('chartColumn');
                chartColumn.style.width = window.innerWidth*0.2+'px';
                chartColumn.style.height = window.innerHeight*0.2+'px';
                this.drawColumnChart();

                
                //存栏量信息
                let drawDepositInfo = document.getElementById('drawDepositInfo');
                drawDepositInfo.style.width = window.innerWidth*0.22+'px';
                drawDepositInfo.style.height = window.innerHeight*0.22+'px';
                this.drawDepositInfoChart();
                
              //温度
               let drawTemperature = document.getElementById('drawTemperature');
                drawTemperature.style.width = window.innerWidth*0.20+'px';
                drawTemperature.style.height = window.innerHeight*0.22+'px';
                // echartDivSize[0].style.width = window.innerWidth*0.22+'px';
                // echartDivSize[1].style.width = window.innerWidth*0.22+'px';
                this.drawTemperatureChart();
                
                //湿度
                let drawHumidity = document.getElementById('drawHumidity');
                drawHumidity.style.width = window.innerWidth*0.20+'px';
                drawHumidity.style.height = window.innerHeight*0.22+'px';
                this.drawHumidityChart();

                //co2
                let drawCO2 = document.getElementById('drawCO2');
                drawCO2.style.width = window.innerWidth*0.20+'px';
                drawCO2.style.height = window.innerHeight*0.25+'px';
                this.drawCO2Echart();

                //氨气
                let drawAmmonia = document.getElementById('drawAmmonia');
                drawAmmonia.style.width = window.innerWidth*0.20+'px';
                drawAmmonia.style.height = window.innerHeight*0.25+'px';
                this.drawAmmoniaEchart();

                //育成率
                let drawBreedingRate = document.getElementById('drawBreedingRate');
                drawBreedingRate.style.width = window.innerWidth*0.18+'px';
                drawBreedingRate.style.height = window.innerHeight*0.2+'px';
                this.drawBreedingRateChart();

                //死亡率
                let drawMortality = document.getElementById('drawMortality');
                drawMortality.style.width = window.innerWidth*0.18+'px';
                drawMortality.style.height = window.innerHeight*0.2+'px';
                this.drawMortalityChart();
                leftThree
                
                  //生产信息
                let drawProduction = document.getElementById('drawProduction');
                drawProduction.style.width = window.innerWidth*0.27+'px';
                drawProduction.style.height = window.innerHeight*0.23+'px';
                this.drawProductionChart();

                this.drawBreedingRateChart();
                this.drawMortalityChart();
        
              //   let drawDepositInfo = document.getElementById('drawDepositInfo');
              //   drawDepositInfo = echarts.init(drawDepositInfo, 'light');        
              //  let chartColumn = document.getElementById('chartColumn');
              //   chartColumn = echarts.init(chartColumn, 'light');
              //   chartColumn.setOption(this.option1);
              //   drawDepositInfo.setOption(this.option2);

                window.onresize = function(){
                  chartColumn.resize();
                  drawDepositInfo.resize();
                  drawTemperature.resize();
                }
            },
       
      //  //修改table tr行的背景色
       warningTableRowStyle({row,rowIndex}){
        return 'background-color: #011120;color: #fff;font-weight: 500;border: 1px solid #03559d'
       },
       //修改table header的背景色
       warnIngTableHeaderColor({row,column,rowIndex,columnIndex}){
         if(rowIndex === 0){
           return 'background-color: #011120;color: #fff;font-weight: 500;font-size:24px;border: 1px solid #03559d'
         }
          if(rowIndex === 1 ){
           return 'background-color: #011120;color: #fff;font-weight: 500;font-size:16px;border: 1px solid #03559d'
         }
       },
       colColor({row,column,rowIndex,columnIndex}){
        return 'background-color: #011120;color: #fff;font-weight: 500;font-size:16px;border: 1px solid #03559d'
       },

        //  //修改table tr行的背景色
       tableRowStyle({row,rowIndex}){
        return 'background-color: #011120;color: #fff;font-weight: 300;'
       },
       //修改table header的背景色
       tableHeaderColor({row,column,rowIndex,columnIndex}){
         let returnStyle = '';
         if(rowIndex === 0){
           if(columnIndex ==0 || columnIndex == 2){
              returnStyle  += 'border-right: none';
           } 
           if(columnIndex ==1){
              returnStyle  += 'border-right: 1px solid #03559d';
           } 
         }
          return 'background-color: #132F58;color: #02ccce;font-weight: 300;font-size:14px;'+returnStyle;
       },
       cellStyle({row,column,rowIndex,columnIndex}){
         let returnStyle = '';
         if(columnIndex !=1){
              returnStyle  += 'border-right: none';
           } 
          return 'border-bottom: none;border-right:1px solid #03559d; color: #fff;font-weight: 300;'+returnStyle;           
         },
        
       },
    
        mounted: function () {
            this.drawCharts();
            
            const that = this;
            window.onresize = ()=>{
              // window.location.reload();
              // window.screenHeight = document.body.clientHeight;
              // that.screenHeight = window.screenHeight;
                // let chartColumn = document.getElementById('chartColumn');
                // chartColumn.style.width = window.innerWidth*0.22+'px';
                // chartColumn.style.height = window.innerHeight*0.22+'px';

              // let total =  document.body.clientHeight;
              // let leftOneHeight = total * 0.25;
              // let leftTwoHeight = total * 0.3;
              // let leftThreeHeight = total * 0.3;

              // that.$refs.leftOne.style.height= leftOneHeight+"px";
              // that.$refs.leftTwo.style.height= leftTwoHeight+"px";
              // that.$refs.leftThree.style.height= leftThreeHeight+"px";

              let leftOneHeight = document.getElementById('leftOne');
              leftOneHeight.style.height = document.documentElement.clientHeight*0.25+'px';

              let leftTwoHeight = document.getElementById('leftTwo');
              leftTwoHeight.style.height = document.documentElement.clientHeight*0.3+'px';

              let leftThreeHeight = document.getElementById('leftThree');
              leftThreeHeight.style.height = window.innerHeight*0.28+'px';
            };
            
    //         colHeight = total*0.2-document.getElementById("col1").offsetTop;
    // document.getElementById("col1").style.height=colHeight +"px";
            // console.dir(this.$refs.leftOne.$el.clientHeight);
        },
        watch:{
          screenWidth(val){
             if(!this.timer){
                // 一旦监听到的screenWidth值改变，就将其重新赋给data里的screenWidth
                this.screenHeight = val
                this.timer = true
                let that = this
                setTimeout(function(){
                    // 打印screenWidth变化的值
                    console.log(that.screenHeight)
                    that.timer = false
                },400)
            }
          }
        },
        updated: function () {
            this.drawCharts()
        }
}
</script>

<style>
  .myStyle  .el-input__inner{
      background-color: rgb(1, 17, 32)!important;
      border: 1px solid rgb(7, 127, 232)!important;
  }
    .el-picker-panel .el-date-picker .el-popper {
            height: 32px;
            line-height: 32px;
            background-color: #011120;
            border: solid 1px #03559d;
            color: #04A5A4;
          }
    .myStyle .available {
      color: white!important;
    }

    .myStyle .el-table tbody tr:hover>td { background-color: #011120!important }
          
</style>

<style scoped>
   .el-header, .el-footer {
    text-align: center;
    line-height: 2vw;
  }
  
  .el-aside {
  
    color: #333;
    text-align: center;
    line-height: 200px;
  }
  
  .el-main:nth-child(2) {
    /* text-align: center;
    line-height: 150px; */
    width: 60%;
  }
  
  body > .el-container {
    margin-bottom: 40px;
  }   
      .date-class{
        width: 20px;
      }
      .left ,.right{
            position:relative;
            width:300px;
            height: 100%;
                 
        }


       .left{
            float: left;
        }
        .left,.right span{
          color: white;
        }
        .headCol{
          position: relative;
          top:50%;
          transform: translateY(-50%)
        }
        .right{
            float: right;
        }
        .center{
          position: absolute;
          left: 300px;
          right:300px;
          height: 4vw;
        }
        .logo-img {
          width: 100%;
          height: 320px;
            }
            
         .col-item .el-form-item__label:first-child{
              color: white!important;
            }
          .el-date-editor.el-input, .el-date-editor.el-input__inner {
            width: 8.5vw!important;

          }
      
          .myBorder{
            padding-top: 0.56vh;
            margin-bottom: 0.76vh;
            border: 1px solid #03559d;
          }
          .whole-title{
            width: 99%;
            height: 1.8em;
            /* background-color: rgba(3,85,157,0.4); */
            background:linear-gradient(right,rgba(3,85,157,0),rgba(3,85,157,0.6));
            line-height: 1.8em;
            /* 3,85,157 */
          }
          .title-text{
            color: white;
            font-weight: 300;
            float: left;
            /* margin-left: 0.36vw; */
          }
          .mainImg{
           position: relative;
           top:0px;
           left: 0px;
          }
          .span-text-1{
            color: white;
            font-size: 1.7vh;
          }
            .span-text-2{
            color: white;
            font-size: 2vh;
          }
          .span-text-5{
            color: white;
            font-size: 5vh;
          }
          .span-text-color-1{
             color: #02ccce;
             font-size: 2vh
          }
          .tableBoder table{
            border: 1px solid #03559d;
          }
          
          .myPadding{
            padding: 10px;
          }
          /* 滚动条样式 */
          ::-webkit-scrollbar {
              width: 6px;
              background-color: #181c32;
          }

          ::-webkit-scrollbar-thumb {
              background-color: #5a76cd;
          }
          /* 时间控件样式 */
          /* .el-picker-panel .el-date-picker .el-popper{
           
          } */
          

          .el-table{
            background-color: #011120!important;
            color:#03559d;
            border: 0.5px solid #03559d!important;
          }
        
          .bullStyle{
            font-size: 24px;
            color: #46A3FF;
            margin: 8px;
          }
          .myPadding .el-table td, .el-table th.is-leaf {
           border-bottom: 1px solid #03559d!important;
          } 
          
</style>

