<template >
  <div class="bgInfo" :style="bgInfo">
    <el-container>
    <!-- <el-header class="bgLogo" :style="bgLogo"></el-header> -->
     <el-header>
        <div class="left">
            <el-col :span="16" class="headCol">
                <span>{{currentDate}}</span>
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
                   <span class="span-text-2">{{tableQuerytown}}</span>
                   <br/>
                   <span >{{tablemeteorology}}</span>
                 </el-col>
                 <el-col class="span-text-1" :span="10">
                    <span class="span-text-5">{{tableQuerytemperature}}℃</span>
                 </el-col>
              </el-row>
              <el-row class="span-text-color-1" style="margin: 1%">
                <span>息烽县,贵州省贵阳市辖县,位于东经106°27'至106°53',北纬26°57'至27°19'之间</span>
              </el-row>
              <el-row>
                <el-row>
                  <el-col class="span-text-1" :span="10">
                    <img src="../assets/image/humidity-icon.png">
                    <span>相对湿度：{{tablehumidity}}</span>
                  </el-col>
                  <el-col class="span-text-1" :span="14">
                    <img src="../assets/image/wind-icon.png">
                      <span>风力风向：{{tablewind_direction}}</span>
                  </el-col>
                </el-row>
                <el-row>
                <el-col class="span-text-1" :span="10">
                  <img src="../assets/image/air-icon.png">
                    <span>空气质量：{{tableAirQuality}}</span>
                  </el-col>
                 <el-col class="span-text-1" :span="12">
                   <img src="../assets/image/rain-icon.png">
                    <span>总降水量：{{tablePrecipitation}}</span>
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
            <el-col :span="8">
              <div class="span-text-1">区域城市:</div>
                <el-select style="color: red" v-model="city" size="small" placeholder="请选择" @change="DropdownBoxRegion()">
                  <el-option
                    v-for="item in region"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
            </el-col>

              <el-col :span="8">
              <div class="span-text-1">鸡场:</div>
                <el-select v-model="chickenFarm" size="small" clearable  placeholder="请选择" @change="DropdownBoxfarmOptions()">
                <el-option
                  v-for="item in farmOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-col>

              <el-col :span="8">
              <div class="span-text-1">鸡棚:</div>
                <el-select v-model="chickenHouse" size="small" clearable  placeholder="请选择" @change="DropdownBoxChickenHouse()">
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
                :row-style="tableRowStyle1"
                :header-cell-style="warnIngTableHeaderColor"
                :cell-style="tableCellStyle1"
                height="130"
                style="width: 100%;">
                <el-table-column label="预警信息">
                   <el-table-column
                  prop="farmName"
                  label="鸡场"
                  width="180">
                </el-table-column>
                <el-table-column
                  prop="houseName"
                  label="鸡棚"
                  width="180">
                </el-table-column>
                <el-table-column
                  prop="configValue"
                  label="预警项">
                </el-table-column>
                 <el-table-column
                  prop="value"
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
          <div style="margin: 2%;display: flex;" class="myStyle" >
            <el-table
                :data="feedList"
                height="150"
                :row-style="tableRowStyle"
                :header-cell-style="tableHeaderColor"
                :cell-style="cellStyle"
                border
                style="width: 50%">
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
            </el-table>
            <el-table
                :data="medicineList"
                height="150"
                :row-style="tableRowStyle"
                :header-cell-style="tableHeaderColor"
                :cell-style="cellStyle"
                border
                style="width: 50%">
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
            backgroundImage: "url(" + require("../assets/image/bg1.png") + ")",
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
          city:'永靖镇',
          tableQuerytown:'',
          tableQuerytemperature:'',
          tablemeteorology:'',
          tablehumidity:'',
          tablewind_direction:'',
          tableAirQuality:'',
          tablePrecipitation:'',

          HumidityOnCo2:null,
          feedList:null,
          medicineList:null,
          stock:null,
          breedingDeath:[{育成率:100,死亡率:0}],
          chickenFarm:'',
          chickenHouse: [],
          warningInfo:null,
          columnInfo:null,
          productionInfo:null,
          region: [
            {value: '永靖镇',label: '永靖镇'},
            {value: '温泉镇',label: '温泉镇'},
            {value: '九庄镇',label: '九庄镇'},
            {value: '小寨坝镇',label: '小寨坝镇'},
            {value: '青山苗族乡',label: '青山苗族乡'},
            {value: '西山乡',label: '西山乡'},
            {value: '养龙司乡',label: '养龙司乡'},
              {value: '石硐乡',label: '石硐乡'},
              {value: '鹿窝乡',label: '鹿窝乡'},
              {value: '流长乡',label: '流长乡'}
          ],

          farmOptions: [],
        chickenHouse: '',

        //饲料及药物用量表格数据
        feedAndMedicine:[],
        screenHeight: document.body.clientHeight,
          //当前时间
          currentDate: '',
          tableRowStyle1: {
            'opacity': 0,'color': '#fff','font-weight': 500,'border': '1px solid #03559d'
          },
          tableCellStyle1: {
            'opacity': 0,'color': '#fff', 'font-weight': 500, 'font-size':'16px', 'border': '1px solid #03559d'
          },

      }
    },
    created() {
      /*this.fetchData();*/
      this.DropdownBoxRegion();
      this.showTime();
    },
    methods: {
      // 肉鸡出存栏信息
        drawColumnChart() {
          var numberOfBarsList =[];
          var liveValueList =[];
          var saleValueList= [];
          if(this.columnInfo) {
            // this.columnInfo.forEach(function (item, index) {
            this.columnInfo.forEach(function (item) {
                numberOfBarsList.push(item.鸡棚);
                liveValueList.push(item.存栏);
                saleValueList.push(item.出栏);
            });
          }
          this.chartColumn = echarts.init(document.getElementById('chartColumn'));
          this.chartColumn.setOption({
            grid:{
              bottom:'15%',
              left:'15%'
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
              data: numberOfBarsList,
              axisLabel:{
                  interval:'auto'
              },
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
              name: '单位:只',
              /* min: 0,
              max: 1000,
              interval: 200,*/
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
                        data: liveValueList,
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
                        data: saleValueList,
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
            var numberOfBarsList =[];
            var valueList =[];
            if(this.stock){
              this.stock.forEach(function (item) {
              // this.stock.forEach(function (item, index) {
                if(numberOfBarsList.indexOf(item.鸡棚) ==-1){
                    numberOfBarsList.push(item.鸡棚);  
                    valueList.push(item.存栏量);
                }    
              });
            }
            this.drawDepositInfo = echarts.init(document.getElementById('drawDepositInfo'));
            this.drawDepositInfo.setOption({
              grid:{
                bottom:'15%',
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
                  data: numberOfBarsList,
                  axisLabel:{
                      interval:'auto'
                  },
                    axisLine: {
                      lineStyle: {
                        color: '#ffffff',
                    },
                },
              },
              yAxis: {
                  type: 'value',
                  name: '单位:只',
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
                  data: valueList
                }
                ]
            });
           },

           // 温度
            drawTemperatureChart() {
                this.drawTemperature = echarts.init(document.getElementById('drawTemperature'));
                var numberOfBarsList =[];
                var valueList =[];
                var seriesContent =[];
                var dateList =['10','12','14','16','18'];
                if(this.HumidityOnCo2){
                  this.HumidityOnCo2.forEach(function (item) {
                  // this.HumidityOnCo2.forEach(function (item, index) {
                    if(numberOfBarsList.indexOf(item.鸡棚) ==-1){
                       numberOfBarsList.push(item.鸡棚);  
                    }
                  });
                  for(var i=0;i<dateList.length;i++){
                    valueList =[];
                    // this.HumidityOnCo2.forEach(function (item, index) {
                    this.HumidityOnCo2.forEach(function (item) {
                      if(dateList[i]==item.时间){
                        valueList.push(item.温度);
                      }    
                    });
                    if(i==numberOfBarsList.length-1){
                      seriesContent.push({
                      series: [
                          {data:valueList,
                          itemStyle: {
                           normal: {
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
                        }}
                      ]
                     });
                    }else{
                      seriesContent.push({
                      series: [
                          {data:valueList,
                          itemStyle: {
                           normal: {
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
                        }}
                      ]
                      },)
                    }
                }
              }

                this.drawTemperature.setOption({ 
                              baseOption: {
                                  barWidth:12,
                                  barMaxWidth:15,
                  timeline: {
                      axisType: 'category',
                      autoPlay: true,
                      playInterval: 1000,
                      data: dateList,
                      axisLine: {
                            lineStyle: {
                                color: '#ffffff'
                            }
                      }

                  },
                  tooltip: {
                  },
                  calculable : true,
                  grid: {
                       top: '20%',
                       bottom:'45%',
                  },
                  xAxis: [
                      {
                          type:'category',
                          axisLabel:{
                              interval:1
                          },
                          data:numberOfBarsList,
                          axisLine: {
                            lineStyle: {
                                color: '#ffffff'
                            }
                      }    
                      }
                  ],
                  yAxis: [
                      {
                          type: 'value',
                          name: '温度（℃）',
                          min: -10,
                          max: 30,
                          interval: 10,
                          axisLine: {
                            lineStyle: {
                                color: '#ffffff'
                            }
                      }    
                      }
                  ],
                  series: [
                      {name: '温度', type: 'bar'}
                  ]
                  },
              options: seriesContent
             });
  
          },

            // 湿度
            drawHumidityChart() {
              var numberOfBarsList =[];
              var valueList =[];
              var seriesContent =[];
              var colorList =['#4B0082','#0000FF','#778899','#00FFFF','#00FF7F'];
              if(this.HumidityOnCo2){
                  this.HumidityOnCo2.forEach(function (item) {
                  // this.HumidityOnCo2.forEach(function (item, index) {
                    if(numberOfBarsList.indexOf(item.鸡棚) ==-1){
                       numberOfBarsList.push(item.鸡棚);
                    }
                  });
                  for(var i=0;i<numberOfBarsList.length;i++){
                  valueList =[];
                  // this.HumidityOnCo2.forEach(function (item, index) {
                  this.HumidityOnCo2.forEach(function (item) {
                    if(numberOfBarsList[i]==item.鸡棚){
                       valueList.push(item.湿度);
                    }
                  });
                      seriesContent.push(
                          {
                              name:numberOfBarsList[i],
                              type:'line',
                              data: valueList,
                              itemStyle: {
                                  normal: {
                                      label:{
                                          textStyle:{
                                              color:colorList[i],
                                              fontSize:12,
                                          }
                                      },
                                      color: new echarts.graphic.LinearGradient(0,1,0,0,[
                                          {offset:0,color:colorList[i]},
                                          {offset:0.6,color:colorList[i]},
                                          {offset:1,color:colorList[i]},
                                      ])
                                  },
                              },
                          }
                      )
                 }
                }
                this.drawHumidity = echarts.init(document.getElementById('drawHumidity'));
                this.drawHumidity.setOption({
                    legend:{
                        data:numberOfBarsList,
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
                        max: 200,
                        interval: 50,
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
                  series:seriesContent
              },true);
            },

            //二氧化碳
           drawCO2Echart() {
                var numberOfBarsList =[];
                var valueList =[];
                var seriesContent =[];
                var color1List1 =['#F5DEB3','#FF4040','#CAE1FF','#F5DEB3','#FF4040','#CAE1FF'];
                var color1List2 =['#FFDEAD','#FF4500','#C6E2FF','#FFDEAD','#FF4500','#C6E2FF'];
                var color1List3 =['#FFE4C4','#FF3030','#B0E2FF','#FFE4C4','#FF3030','#B0E2FF'];
                if(this.HumidityOnCo2){
                  this.HumidityOnCo2.forEach(function (item) {
                  // this.HumidityOnCo2.forEach(function (item, index) {
                    if(numberOfBarsList.indexOf(item.鸡棚) ==-1){
                       numberOfBarsList.push(item.鸡棚);  
                    }    
                  });
                  for(var i=0;i<numberOfBarsList.length;i++){
                  valueList =[];
                  this.HumidityOnCo2.forEach(function (item) {
                  // this.HumidityOnCo2.forEach(function (item, index) {
                    if(numberOfBarsList[i]==item.鸡棚){
                       valueList.push(item.二氧化碳);
                    }    
                  });
                  if(i==numberOfBarsList.length-1){
                    seriesContent.push({
                     name:numberOfBarsList[i],
                     type:'line',
                     areaStyle: {},
                     data: valueList,
                       itemStyle: {
                           normal: {
                                color: new echarts.graphic.LinearGradient(0,1,0,0,[
                                      {offset:0,color:color1List1[i]},
                                      {offset:0.6,color:color1List2[i]},
                                      {offset:1,color:color1List3[i]},
                                    ])
                              },
                        },
                    });
                  }else{
                    seriesContent.push({
                     name:numberOfBarsList[i],
                     type:'line',
                     areaStyle: {},
                     data: valueList,
                       itemStyle: {
                           normal: {
                                color: new echarts.graphic.LinearGradient(0,1,0,0,[
                                      {offset:0,color:color1List1[i]},
                                      {offset:0.6,color:color1List2[i]},
                                      {offset:1,color:color1List3[i]},
                                    ])
                              },
                        },
                    },)
                  }
                 }
                }

                this.drawCO2 = echarts.init(document.getElementById('drawCO2'));
                this.drawCO2.setOption({
                legend:{
                    data:numberOfBarsList,
                    textStyle:{
                        color: '#ffffff',//字体颜色
                        fontSize:'12px'
                    },
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
                  },series:seriesContent
                },true);
            },
          //氨气
            drawAmmoniaEchart() {
                this.drawAmmonia = echarts.init(document.getElementById('drawAmmonia'));
                var numberOfBarsList =[];
                var valueList =[];
                var seriesContent =[];
                var dateList =['10','12','14','16','18'];
                if(this.HumidityOnCo2){
                  this.HumidityOnCo2.forEach(function (item) {
                  // this.HumidityOnCo2.forEach(function (item, index) {
                    if(numberOfBarsList.indexOf(item.鸡棚) ==-1){
                       numberOfBarsList.push(item.鸡棚);  
                    }    
                  });
                  for(var i=0;i<dateList.length;i++){
                    valueList =[];
                    this.HumidityOnCo2.forEach(function (item) {
                    // this.HumidityOnCo2.forEach(function (item, index) {
                      if(dateList[i]==item.时间){
                        valueList.push(item.氨气);
                      }    
                    });
                    if(i==numberOfBarsList.length-1){
                      seriesContent.push({
                      series: [
                          {data:valueList,
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
                                    ])
                              },
                        }}
                      ]
                     });
                    }else{
                      seriesContent.push({
                      series: [
                          {data:valueList,
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
                                    ])
                              },
                        }}
                      ]
                      },)
                    }
                 }
                }
               

                this.drawAmmonia.setOption({
                              baseOption: {
                                  barWidth:12,
                                  barMaxWidth:15,
                  timeline: {
                      axisType: 'category',
                      autoPlay: true,
                      playInterval: 1000,
                      data: dateList,
                      axisLine: {
                            lineStyle: {
                                color: '#ffffff'
                            }
                      }    
                      
                      
                  },
                  tooltip: {
                  },
                  calculable : true,
                  grid: {
                      top: '20%',
                      bottom:'45%',
                  },
                  xAxis: [
                      {
                          type:'category',
                          axisLabel:{
                              interval:1
                          },
                          data:numberOfBarsList,
                          axisLine: {
                            lineStyle: {
                                color: '#ffffff'
                            }
                      }    
                      }
                  ],
                  yAxis: [
                      {
                          type: 'value',
                          name: '氨气',
                          min: 1,
                          max: 5,
                          interval: 1,
                          axisLine: {
                            lineStyle: {
                                color: '#ffffff'
                            }
                      }    
                      }
                  ],
                  series: [
                      {name: '氨气', type: 'bar'}
                  ]
              },
              options: seriesContent
                });
            },
      //--------------------------------------------成交信息---------------------------------------------------------
            drawProductionChart() {
                var dateList =[];
                var volumeList =[];
                var amountList= [];
                if(this.productionInfo) {
                    this.productionInfo.forEach(function (item) {
                    // this.productionInfo.forEach(function (item, index) {
                        dateList.push(item.时间);
                        volumeList.push(item.成交量);
                        amountList.push(item.成交金额);
                    });
                }
                this.drawProduction = echarts.init(document.getElementById('drawProduction'));

                this.drawProduction.setOption({
                    barWidth:12,
                    barMaxWidth:15,
                  legend:{
                    data:['成交金额'],
                      itemGap: 40,
                    textStyle:{
                        color: '#ffffff',//字体颜色
                        fontSize:'100%'
                    },
                  },
                  grid:{
                    bottom:'20%'
                  },
                  tooltip: {},
                  xAxis: {
                      data: dateList,
                      axisLine: {
                          lineStyle: {
                            color: '#ffffff',
                        },
                    },
                    
                  },
                  yAxis: {
                     type: 'value',
                     name: '万元',
                     min: 0,
                     max: 50,
                     interval: 10,
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
                     name:"成交金额",
                     type:'bar',
                     data: amountList,
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
                      value:100,
                      name:'',
                      itemStyle:{
                        normal:{
                          color: "rgba(80,150,224,0)"
                        }
                      },
                      
                    },
                    //根据值动态填充
                    {
                      value:this.breedingDeath[0].育成率,
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
                      value:100-this.breedingDeath[0].育成率,
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
                      value:100,
                      itemStyle:{
                        normal:{
                          color: "rgba(80,150,224,0)"
                        }
                      }
                    },
                    {
                      value:this.breedingDeath[0].死亡率,
                      itemStyle:{
                        normal:{
                          color: "rgba(	255,140,0,1)"
                        }
                      }
                    },
                    {
                      value:100-this.breedingDeath[0].死亡率,
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
              })
            },
            drawCharts() {                               
                // let echartDivSize = document.getElementsByClassName('middleEchartsSize');

                //肉鸡出存栏信息
                let chartColumn = document.getElementById('chartColumn');
                chartColumn.style.width = window.innerWidth*0.22+'px';
                chartColumn.style.height = window.innerHeight*0.22+'px';
                this.drawColumnChart();
                /*this.getTransationInfo();*/

                
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
                // leftThree
                
                //生产信息
                let drawProduction = document.getElementById('drawProduction');
                drawProduction.style.width = window.innerWidth*0.27+'px';
                drawProduction.style.height = window.innerHeight*0.23+'px';
                this.drawProductionChart();
                /*this.getProductionInfo();*/

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
            //数据更新
            fetchData() {             
                      this.tableLoading = true
                      let params = new URLSearchParams();      
                      params.append("regionName", this.city);
                      params.append("farmId", this.chickenFarm);
                      params.append("houseId", this.chickenHouse);
                      this.$http({
                          method: "post",
                          url: "/web-server/home/query.json?action=earlyWarningQuery",
                          data: params
                      }).then(res => {
                this.tableQuerytown = res.data.meteorological[0].区域;
                this.tableQuerytemperature = res.data.meteorological[0].温度;
                this.tablemeteorology = res.data.meteorological[0].气象情况;
                this.tablehumidity = res.data.meteorological[0].湿度;
                this.tablewind_direction = res.data.meteorological[0].风力风向;
                this.tableAirQuality = res.data.meteorological[0].空气质量比;
                this.tablePrecipitation = res.data.meteorological[0].降水量;
                this.warningInfo = res.data.warning;
                this.HumidityOnCo2 = res.data.HumidityOnCo2;
                this.feedList=res.data.feed;    
                this.medicineList=res.data.medicine;
                this.stock=res.data.stock;     
                this.breedingDeath=res.data.breedingDeath;
                this.columnInfo = res.data.columnInfo;
                this.productionInfo = res.data.productionInfo;
                //绘制图表
                this.drawCharts();
                this.tableLoading = false
                      }).catch(err => {
                              console.log(err);
                      });
            },		
            //镇下拉框触发事件
            DropdownBoxRegion() {             
                      this.tableLoading = true;
                      this.farmOptions = [];
                      this.houseOptions = [];
                      this.chickenFarm = "";
                      this.chickenHouse = "";
                      let params = new URLSearchParams();      
                      params.append("regionName", this.city);
                      this.$http({
                        method: "post",
                        url: "/web-server/home/query.json?action=getDropdownBoxFarmList",
                        data: params
                      }).then(res => {
                         this.farmOptions =res.data.dropdownBoxFarmList;
                         this.fetchData();
                      }).catch(err => {
                        console.log(err);
                      });
            },		
            //鸡场下拉框触发事件
            DropdownBoxfarmOptions() {             
                      this.tableLoading = true;
                      this.houseOptions = [];
                      this.chickenHouse = "";
                      let params = new URLSearchParams();      
                      params.append("farmId", this.chickenFarm);
                      this.$http({
                        method: "post",
                        url: "/web-server/home/query.json?action=getDropdownBoxHouseList",
                        data: params
                      }).then(res => {
                         this.houseOptions =res.data.dropdownBoxHouseList;
                         this.fetchData();
                      }).catch(err => {
                        console.log(err);
                      });
            },
            //鸡舍下拉框触发事件
            DropdownBoxChickenHouse(){
                this.fetchData();
            },	
       showTime(){
          //  var today,hour,second,minute,year,month,date;
           var today,year,month,date;
           var strDate ;
           today=new Date();
           var n_day = today.getDay();
           switch (n_day)
           {
               case 0:{
                   strDate = "星期日"
               }break;
               case 1:{
                   strDate = "星期一"
               }break;
               case 2:{
                   strDate ="星期二"
               }break;
               case 3:{
                   strDate = "星期三"
               }break;
               case 4:{
                   strDate = "星期四"
               }break;
               case 5:{
                   strDate = "星期五"
               }break;
               case 6:{
                   strDate = "星期六"
               }break;
               case 7:{
                   strDate = "星期日"
               }break;
           }
           year = today.getFullYear();
           month = today.getMonth()+1;
           date = today.getDate();
          //  hour = today.getHours();
          //  minute =today.getMinutes();
          //  second = today.getSeconds();
           this.currentDate = year + "年" + month + "月" + date + "日 "+strDate; //显示时间
       },
      //  //修改table tr行的背景色
      //  warningTableRowStyle({row,rowIndex}){
       warningTableRowStyle(){
        return 'background-color: #011120;color: #fff;font-weight: 500;border: 1px solid #03559d'
       },
       //修改table header的背景色
      //  warnIngTableHeaderColor({row,column,rowIndex,columnIndex}){
       warnIngTableHeaderColor({rowIndex}){
         if(rowIndex === 0){
           return 'background-color: #002b71;color: #fff;font-weight: 500;font-size:24px;border: 1px solid #03559d'
         }
          if(rowIndex === 1 ){
           return 'background-color: #002b71;color: #fff;font-weight: 500;font-size:16px;border: 1px solid #03559d'
         }
       },
      //  colColor({row,column,rowIndex,columnIndex}){
       colColor(){
        return 'background-color: #002b71;color: #fff;font-weight: 500;font-size:16px;border: 1px solid #03559d'
       },

        //  //修改table tr行的背景色
      //  tableRowStyle({row,rowIndex}){
       tableRowStyle(){
        return 'background-color: #002b71;color: #fff;font-weight: 300;'
       },
       //修改table header的背景色
      //  tableHeaderColor({row,column,rowIndex,columnIndex}){
       tableHeaderColor({rowIndex,columnIndex}){
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
      //  cellStyle({row,column,rowIndex,columnIndex}){
       cellStyle({columnIndex}){
         let returnStyle = '';
         if(columnIndex !=1){
              returnStyle  += 'border-right: none';
           } 
          return 'border-bottom: none;border-right:1px solid #03559d; color: #fff;font-weight: 300;'+returnStyle;           
         },
        
       },
    
        mounted: function () {
            /*this.drawCharts();*/
            
            // const that = this;
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
       /* updated: function () {
            this.drawCharts()
        }*/
}
</script>

<style>
  .myStyle  .el-input__inner{
      background-color: rgb(1, 17, 32, 0)!important;
      border: 1px solid rgb(7, 127, 232)!important;
      color: aliceblue;
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

  .myStyle .el-table tbody tr:hover>td {
    background-color: #011120!important
  }

  
          
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
            color: #fddeae;
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
              background-color: #5a77cd;
          }
          /* 时间控件样式 */
          /* .el-picker-panel .el-date-picker .el-popper{
           
          } */
          

          .el-table {
            background-color:rgb(1, 1, 1, 0)!important;
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

