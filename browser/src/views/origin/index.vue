<template>
  <div class>
    <div style="text-align:center;margin-top:10px;">
      <el-form :inline="true" :model="tableQuery" class="demo-form-inline" ref="tableQuery">
        <!--<el-form-item label="溯源码" prop="originCode">
          <el-input v-model="tableQuery.originCode" placeholder="请输入溯源码"></el-input>
        </el-form-item>-->
        <el-form-item>
          <el-select placeholder="请选择溯源码" v-model.trim="tableQuery.originCode">
            <el-option v-for="item in codes" :key="item.id" :label="item.sourceCode" :value="item.sourceCode"/>
          </el-select>
          <el-button style="background-color: #3ebf7c;margin-left: 5px" @click="fetchData()" type="success">溯源</el-button>
          <el-button
            style="background-color: #3ebf7c"
            @click="reset('tableQuery')"
            type="success"
          >重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 区块链应用 -->
    <div class="info-title">
      <span class="info-title-content">区块链应用</span>
    </div>
    <div class="custom-table">
      <el-table
        :data="tableData.block"
        style="width: 100%"
        :header-cell-style="{background:'#e6eaf3',height:'50px'}"
        border
        >
        <el-table-column prop="source_code" label="溯源码"></el-table-column>
        <el-table-column prop="hash" label="本区块的hash值"></el-table-column>
        <el-table-column prop="county" label="城市"></el-table-column>
        <el-table-column prop="town" label="区域"></el-table-column>
        <el-table-column prop="farm" label="鸡场"></el-table-column>
        <el-table-column prop="house" label="鸡棚"></el-table-column>
      </el-table>
    </div>

    <!-- 养殖户信息 -->
    <div class="info-title">
      <span class="info-title-content">养殖户信息</span>
    </div>
    <div class="custom-table">
      <el-table
        :data="tableData.farm"
        style="width: 100%"
        :header-cell-style="{background:'#e6eaf3',height:'50px'}"
        border
      >
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-form label-position="left" inline class="origin-farm-table-expand">
              <el-form-item label="养殖场名称">
                <span>{{ props.row.name }}</span>
              </el-form-item>
              <el-form-item label="养殖场负责人">
                <span>{{ props.row.charger }}</span>
              </el-form-item>
              <el-form-item label="联系电话">
                <span>{{ props.row.phone_number }}</span>
              </el-form-item>
              <el-form-item label="养殖场地址">
                <span>{{ props.row.address }}</span>
              </el-form-item>
              <el-form-item label="营业执照">
                <img
                  :src="props.row.intelligence"
                  min-width="70"
                  height="500"
                  @click="openImg(props.row.intelligence)"
                />
              </el-form-item>
              <el-form-item label="养殖场简介" style="width:100%;margin-top: 30px;">
                <div v-html="props.row.introduce" class="origin-rich-txt"></div>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="养殖场名称"></el-table-column>
        <el-table-column prop="charger" label="养殖场负责人"></el-table-column>
        <el-table-column prop="phone_number" label="联系电话"></el-table-column>
        <el-table-column prop="address" label="养殖场地址"></el-table-column>
        <el-table-column prop="intelligence" label="营业执照">
          <template slot-scope="scope">
            <img
              :src="scope.row.intelligence"
              min-width="70"
              height="70"
              @click="openImg(scope.row.intelligence)"
            />
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!-- 营业执照弹窗 -->
    <el-dialog :modal="false" width="800px" :visible.sync="imgVisible" class="img-dialog">
      <el-card :body-style="{ padding: '0px' }">
        <img :src="dialogImgUrl" width="100%" height="100%" />
      </el-card>
    </el-dialog>

    <!-- 鸡苗信息 -->
    <div class="info-title">
      <span class="info-title-content">鸡苗信息</span>
    </div>
    <div class="custom-table">
      <el-table
        :data="tableData.chicks"
        :header-cell-style="{background:'#e6eaf3',height:'50px'}"
        border
      >
        <el-table-column prop="batch_number" label="种苗批号"></el-table-column>
        <el-table-column prop="chick_type" label="品种名称"></el-table-column>
        <el-table-column prop="create_time" label="投苗日期" :formatter="dateFormatter"></el-table-column>
        <el-table-column prop="chick_count" label="投苗数量（只）"></el-table-column>
        <el-table-column prop="producer" label="种苗生产企业"></el-table-column>
        <el-table-column prop="produce_cycle" label="生长周期（天）"></el-table-column>
        <el-table-column prop="modifier" label="操作员"></el-table-column>
      </el-table>
    </div>

    <!-- 用料信息 -->
    <div class="info-title">
      <span class="info-title-content">用料信息</span>
    </div>
    <div class="custom-table">
      <el-table
        :data="tableData.feed"
        :header-cell-style="{background:'#e6eaf3',height:'50px'}"
        border
      >
        <el-table-column prop="name" label="饲料名称"></el-table-column>
        <el-table-column prop="producer" label="饲料生产企业"></el-table-column>
        <el-table-column prop="raise_time" label="用料日期" :formatter="dateFormatter"></el-table-column>
        <el-table-column prop="raise_value" label="用料量(KG)"></el-table-column>
        <el-table-column prop="chick_day_age" label="肉鸡日龄"></el-table-column>
        <el-table-column prop="modifier" label="操作员"></el-table-column>
      </el-table>
    </div>

    <!-- 用药信息 -->
    <div class="info-title">
      <span class="info-title-content">用药信息</span>
    </div>
    <div class="custom-table">
      <el-table
        :data="tableData.medicine"
        :header-cell-style="{background:'#e6eaf3',height:'50px'}"
        border
      >
        <el-table-column prop="name" label="药物名称"></el-table-column>
        <el-table-column prop="producer" label="药物生产企业"></el-table-column>
        <el-table-column prop="raise_value" label="剂量"></el-table-column>
        <el-table-column prop="raise_reason" label="用药原因"></el-table-column>
        <el-table-column prop="raise_time" label="用药时间" :formatter="dateFormatter"></el-table-column>
        <el-table-column prop="chick_day_age" label="肉鸡日龄"></el-table-column>
        <el-table-column prop="modifier" label="兽医姓名"></el-table-column>
      </el-table>
    </div>

    <!-- 疫苗信息 -->
    <div class="info-title">
      <span class="info-title-content">疫苗信息</span>
    </div>
    <div class="custom-table">
      <el-table
        :data="tableData.vaccine"
        :header-cell-style="{background:'#e6eaf3',height:'50px'}"
        border
      >
        <el-table-column prop="name" label="疫苗名称"></el-table-column>
        <el-table-column prop="producer" label="疫苗生产企业"></el-table-column>
        <el-table-column prop="raise_value" label="剂量"></el-table-column>
        <el-table-column prop="raise_time" label="接种时间" :formatter="dateFormatter"></el-table-column>
        <el-table-column prop="chick_day_age" label="肉鸡日龄"></el-table-column>
        <el-table-column prop="modifier" label="兽医姓名"></el-table-column>
      </el-table>
    </div>

    <!-- 出栏信息 -->
    <div class="info-title">
      <span class="info-title-content">出栏信息</span>
    </div>
    <div class="custom-table">
      <el-table
        :data="tableData.sale"
        :header-cell-style="{background:'#e6eaf3',height:'50px'}"
        border
      >
        <el-table-column prop="saleDate" label="出栏日期" :formatter="dateFormatter"></el-table-column>
        <el-table-column prop="avgWeight" label="出栏均重(KG)"></el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script>
export default {
  name: "Origin",
  data() {
    return {
      codes: '',
      tableData: {
        block:[
          {
            source_code:'',
            hash:'',
            county:'',
            area:'',
            farm:'',
            house:''
          }
        ],
        farm: [
          {
            name: "上海市普陀区金沙江路养鸡场",
            charger: "王小虎",
            phone_number: " 15182528333",
            address: "上海市普陀区金沙江路 1518 弄",
            size: "大",
            intelligence: "不错",
            introduce: "..."
          }
        ],
        chicks: [
          {
            batch_number: "123456678",
            chick_type: "清远走地鸡",
            create_time: "2019-08-15 11:00:00",
            chick_count: "8000",
            producer: "清远正宗走地鸡种苗批发厂",
            produce_cycle: "90",
            modifier: "李四"
          }
        ],
        feed: [
          {
            name: "01",
            producer: "鸡饲料批发厂",
            raise_time: "2019-08-15 11:00:30",
            raise_value: "10",
            chick_day_age: "30",
            modifier: "王五"
          },
          {
            name: "02",
            producer: "鸡饲料批发厂",
            raise_time: "2019-08-15 11:00:30",
            raise_value: "20",
            chick_day_age: "30",
            modifier: "王五"
          },
          {
            name: "03",
            producer: "鸡饲料批发厂",
            raise_time: "2019-08-15 11:00:30",
            raise_value: "30",
            chick_day_age: "30",
            modifier: "王五"
          },
          {
            name: "04",
            producer: "鸡饲料批发厂",
            raise_time: "2019-08-15 11:00:30",
            raise_value: "40",
            chick_day_age: "30",
            modifier: "王五"
          }
        ],
        medicine: [
          {
            name: "01",
            producer: "鸡饲料批发厂",
            raise_time: "2019-08-15 11:00:30",
            raise_value: "10",
            chick_day_age: "30",
            modifier: "王五"
          },
          {
            name: "02",
            producer: "鸡饲料批发厂",
            raise_time: "2019-08-15 11:00:30",
            raise_value: "20",
            chick_day_age: "30",
            modifier: "王五"
          },
          {
            name: "03",
            producer: "鸡饲料批发厂",
            raise_time: "2019-08-15 11:00:30",
            raise_value: "30",
            chick_day_age: "30",
            modifier: "王五"
          },
          {
            name: "04",
            producer: "鸡饲料批发厂",
            raise_time: "2019-08-15 11:00:30",
            raise_value: "40",
            chick_day_age: "30",
            modifier: "王五"
          }
        ],
        vaccine: [
          {
            name: "01",
            producer: "鸡饲料批发厂",
            raise_time: "2019-08-15 11:00:30",
            raise_value: "10",
            chick_day_age: "30",
            modifier: "王五"
          },
          {
            name: "02",
            producer: "鸡饲料批发厂",
            raise_time: "2019-08-15 11:00:30",
            raise_value: "20",
            chick_day_age: "30",
            modifier: "王五"
          },
          {
            name: "03",
            producer: "鸡饲料批发厂",
            raise_time: "2019-08-15 11:00:30",
            raise_value: "30",
            chick_day_age: "30",
            modifier: "王五"
          },
          {
            name: "04",
            producer: "鸡饲料批发厂",
            raise_time: "2019-08-15 11:00:30",
            raise_value: "40",
            chick_day_age: "30",
            modifier: "王五"
          }
        ],
        sale: [
          {
            saleDate: "2019-08-15 11:00:30",
            avgWeight: "10"
          }
        ]
      },
      tableQuery: {
        originCode: ""
      },
      imgVisible: false,
      dialogImgUrl: null
    };
  },
  created() {
    this.fetchCodes();
  },
  methods: {
    //溯源码下拉框
    fetchCodes(){
      this.$http({
        method: "post",
        url: "/web-server/origin/blockchain/query.json?action=getSourceCodes",
      }).then(res => {
        this.codes = res.data.data;
      }).catch(err => {
        console.log(err);
      });
    },

    // 查询
    fetchData() {
      if (!this.tableQuery.originCode) {
        this.$message.error("请输入溯源码");
        return;
      }
      let loadingInstance = this.$loading({
        fullscreen: false,
        target: ".content-wrap",
        lock: true,
        text: "正在溯源..."
        // spinner: "el-icon-loading",
      });
      let params = new URLSearchParams();
      params.append("sourceCode", this.tableQuery.originCode);

      this.$http({
        method: "post",
        // url: "/origin-server/source/index.json?action=traceOrigin",
        url: "/web-server/origin/blockchain/query.json?action=query",
        data: params
      })
        .then(res => {
          if (res.status==200) {
            this.tableData.block = res.data.rows;
              this.tableLoading = false
          } else {
            this.tableData.block = {};
          }
          loadingInstance.close();
        })
        .catch(err => {
          console.log(err);
          this.tableData.block = {};
          loadingInstance.close();
        });
    },
    // 重置
    reset(formName) {
      //this.$refs[formName].resetFields();
      this.tableQuery.originCode='';
    },
    // 展示图片
    openImg(url) {
      if (url) {
        this.imgVisible = true;
        this.dialogImgUrl = url;
      }
    },
    dateFormatter(row, column, cellValue) {
      if (cellValue) {
        const date = new Date(cellValue);
        var year = date.getFullYear();
        var month =
          date.getMonth() + 1 < 10
            ? "0" + (date.getMonth() + 1)
            : date.getMonth() + 1;
        var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
        var hours =
          date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
        var minutes =
          date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
        var seconds =
          date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
        const retDateStr =
          year +
          "-" +
          month +
          "-" +
          day +
          " " +
          hours +
          ":" +
          minutes +
          ":" +
          seconds;

        return retDateStr;
      }
    }
  }
};
</script>
<style>
.tableHeader {
  height: 50px;
  background: #e6eaf3;
}
.origin-farm-table-expand .el-form-item__label {
  width: 100px;
  color: #99a9bf !important;
}
</style>
<style scoped>
.custom-table {
  text-align: left;
  padding: 0 20px 0 20px;
  margin: 10px 0 30px 0;
}

.table {
  text-align: left;
  padding-left: 40px;
  margin-top: 10px;
}

.layui-inline {
  padding-right: 10px;
}

.info-title {
  margin-bottom: 10px;
  text-align: center;
}
.info-title-content {
  background-color: #3ebf7c;
  color: white;
  padding: 5px 50px 5px 50px;
  text-align: center;
}

.origin-farm-table-expand {
  font-size: 0;
}
.origin-farm-table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}
.origin-rich-txt >>> img{ display:block; max-width: 900px;}

</style>

