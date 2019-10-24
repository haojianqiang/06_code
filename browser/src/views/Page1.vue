<template>
  <!-- 表格示例 -->
  <div>
    <!-- 请求参数 -->
    <div class="param-group">
      <el-row type="flex" class justify="space-around">
        <el-col :span="6">
          <div class="input-group">
            <span class="input-title">日期:</span>
            <el-input placeholder="请选择日期" suffix-icon="el-icon-date" v-model="param.date"></el-input>
          </div>
        </el-col>
        <el-col :span="6">
          <div>
            <span class="input-title">姓名:</span>
            <el-input placeholder="请输入姓名" suffix-icon="el-icon-date" v-model="param.name"></el-input>
          </div>
        </el-col>
        <el-col :span="6">
          <div>
            <span class="input-title">地址:</span>
            <el-input placeholder="请选择地址" suffix-icon="el-icon-date" v-model="param.address"></el-input>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 表格 -->
    <div class="table-group">
      <div class="table-content">
        <el-table :data="tableData" style="width:100%">
          <el-table-column prop="date" label="日期" width="180"></el-table-column>
          <el-table-column prop="name" label="姓名" width="180"></el-table-column>
          <el-table-column prop="address" label="地址"></el-table-column>
        </el-table>
      </div>
      <div class="table-page">
        <page-com></page-com>
      </div>
    </div>
  </div>
</template>
<script>
import PageCom from "@/components/PageCom.vue";

export default {
  name: "Page1",
  components: {
    PageCom
  },
  data() {
    return {
      param: {
        date: "",
        name: "",
        address: ""
      },
      tableData: [
        {
          date: "2016-05-02",
          name: "王小虎",
          address: "上海市普陀区金沙江路 1518 弄"
        },
        {
          date: "2016-05-04",
          name: "王小虎",
          address: "上海市普陀区金沙江路 1517 弄"
        },
        {
          date: "2016-05-01",
          name: "王小虎",
          address: "上海市普陀区金沙江路 1519 弄"
        },
        {
          date: "2016-05-03",
          name: "王小虎",
          address: "上海市普陀区金沙江路 1516 弄"
        }
      ]
    };
  },
  methods: {
    init() {
      // 请求数据
    }
  }
};
</script>
<style scoped>
.param-group {
  padding: 10px;
}
.input-group {
}
.input-title {
  margin-right: 10px;
}
.el-input {
  width: 80%;
}
.table-group{

}
.table-group .table-page{
  text-align: center;
}
</style>


