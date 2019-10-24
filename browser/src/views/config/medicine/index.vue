<template>
    <div class="">
        <div class="demoTable">
            <el-form :inline="true" :model="tableQuery" class="demo-form-inline">
                <el-form-item label="药物名称：">
                    <el-input v-model.trim="tableQuery.name" placeholder="请输入名称"></el-input>
                </el-form-item>
                <el-form-item label="药物生产企业：">
                    <el-input v-model.trim="tableQuery.producer" placeholder="请输入生产企业"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button class="button" @click="fetchData(1)" type="success">查询</el-button>
                    <el-button class="button" type="success">重置</el-button>
                </el-form-item>
            </el-form>
        </div>

        <div class="demoTable">
            <el-button class="button" type="success" @click="removeBatch(sels)">批量删除</el-button>
            <el-button class="button" type="success" @click="handleCreate"> <i class="el-icon-plus"></i> 新增</el-button>
        </div>

        <!-- 表格 -->
        <div style="margin-bottom: 10px;"></div>
        <div class="demoTable">
            <el-table
                    :data="tableData"
                    v-loading="tableLoading"
                    element-loading-text="加载中"
                    :header-cell-style="{background:'#e6eaf3',height:'50px'}"
                    @selection-change="selsChange"
                    border>
                <el-table-column
                        prop="id"
                        type="selection"
                        width="55">
                </el-table-column>
                <el-table-column label="操作">
                    <template slot-scope="scope">
                        <a href="javascript:void(0)" @click="handleEdit(scope.row.id)" class="link">修改</a>
                        <a href="javascript:void(0)" @click="singleDel(scope.row.id)" class="linkPadding">删除</a>
                    </template>
                </el-table-column>
                <el-table-column prop="name" label="药物名称"></el-table-column>
                <el-table-column prop="producer" label="药物生产企业"></el-table-column>
                <el-table-column prop="batch_number" label="入库批号"></el-table-column>
                <el-table-column :formatter="dateFormat" prop="create_time" label="入库时间"></el-table-column>
                <el-table-column prop="remark" label="备注"></el-table-column>
            </el-table>
        </div>

        <div class="demoTable page">
            <el-pagination
                    background
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="tablePage.current"
                    :page-sizes="[10, 20, 30, 40, 50]"
                    :page-size="tablePage.size"
                    layout="sizes ,prev, pager, next, jumper, total"
                    :total="tablePage.total">
            </el-pagination>
        </div>

        <!--弹出窗口：新增-->
        <el-dialog :visible.sync="dialogCreateFormVisible" :modal-append-to-body='false'>
            <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="120px" style = "margin-top:20px" >

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="药物名称：" prop="name">
                            <el-input v-model.trim="temp.name" placeholder="请输入药物名称"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="药物生产企业：" prop="producer">
                            <el-input v-model.trim="temp.producer" placeholder="请输入药物生产企业"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="入库批号：" prop="batchNumber">
                            <el-input v-model.trim="temp.batchNumber" placeholder="请输入入库批号"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="入库时间：" prop="createTime">
                            <el-date-picker
                                    v-model.trim="temp.createTime"
                                    type="datetime"
                                    placeholder="选择日期时间"
                                    style= "width:214px">
                            </el-date-picker>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="规格：" prop="specs">
                            <el-input v-model.trim="temp.specs" placeholder="请输入规格"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="单价(元)：" prop="price">
                            <el-input oninput="value=value.replace(/[^\d.]/g,'')" v-model="temp.price" placeholder="请输入单价(元)"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="单位：" prop="unit">
                            <el-input v-model.trim="temp.unit" placeholder="请输入单位"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="备注：" prop="remark">
                            <el-input v-model.trim="temp.remark" placeholder="请输入备注"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

            </el-form>
            <div slot="footer" class="dialog-footer footer">
                <el-button class="button" type="primary" @click="createData">保存</el-button>
                <el-button @click="dialogCreateFormVisible = false">取消</el-button>
            </div>
        </el-dialog>

        <!--弹出窗口：修改-->
        <el-dialog :visible.sync="dialogEditFormVisible" :modal-append-to-body='false'>
            <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="120px" style = "margin-top:20px">

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="药物名称：" prop="name">
                            <el-input v-model.trim="temp.name" placeholder="请输入药物名称"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="药物生产企业：" prop="producer">
                            <el-input v-model.trim="temp.producer" placeholder="请输入药物生产企业"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="入库批号：" prop="batchNumber">
                            <el-input v-model.trim="temp.batchNumber" placeholder="请输入入库批号"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="入库时间：" prop="createTime">
                            <el-date-picker
                                    v-model.trim="temp.createTime"
                                    type="date"
                                    placeholder="选择日期时间"
                                    style= "width:214px">
                            </el-date-picker>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="规格：" prop="specs">
                            <el-input v-model.trim="temp.specs" placeholder="请输入规格"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="单价(元)：" prop="price">
                            <el-input oninput="value=value.replace(/[^\d.]/g,'')" v-model="temp.price" placeholder="请输入单价(元)"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="单位：" prop="unit">
                            <el-input v-model.trim="temp.unit" placeholder="请输入单位"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="备注：" prop="remark">
                            <el-input v-model.trim="temp.remark" placeholder="请输入备注"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

            </el-form>
            <div slot="footer" class="dialog-footer footer">
                <el-button class="button"  type="primary" @click="editData">保存</el-button>
                <el-button @click="dialogEditFormVisible = false">取消</el-button>
            </div>
        </el-dialog>

    </div>
</template>


<script>
import utils from "@/utils"
   export default {
       name: 'Medicine',
       data() {
           return {
               rules:{
                   name: [{ required: true, message: '请输入药物名称', trigger: 'blur' }],
                   producer: [{ required: true, message: '请输入药物生产企业', trigger: 'blur' }],
                   batchNumber: [{ required: true, message: '请输入入库批号', trigger: 'blur' }],
                   price: [{required: true, message: '必填', trigger: 'blur'}]
               },
               tableLoading: false,//数据是否在加载
               tableData: [],
               sels: [],//选中行的集合，用来批量删除
               tableQuery: {
                   name: '',
                   producer: ''
               },
               tablePage: {
                   current: 1,
                   pages: [10, 20, 30, 40, 50],
                   size: 10,
                   total: null
               },
               temp: {
                   id: '',
                   name: '',
                   producer: '',
                   batchNumber: '',
                   createTime: '',
                   specs: '',
                   price: '',
                   unit:'',
                   remark: ''
                 },
               isIndeterminate: true,
               dialogCreateFormVisible: false,
               dialogEditFormVisible: false
           }

       },
       created() {
         this.fetchData()
       },

       watch: {
       },

       methods: {
           //清空对象所有属性
           resetTemp(temp){
               for(let prop in temp){
                   temp[prop] = '';
               }
               return temp;
           },
           //分页
           handleSizeChange(val) {
               this.tablePage.size = val;
               this.fetchData();
           },
           handleCurrentChange(val) {
               this.tablePage.current = val;
               this.fetchData();
           },
           //查询
           fetchData(current) {
               if(current){
                   this.tablePage.current = current
               }
               this.tableLoading = true;//正在加载数据
               let params = new URLSearchParams();
               params.append("pageNo", this.tablePage.current);
               params.append("pageSize",this.tablePage.size);
               params.append("name",this.tableQuery.name);
               params.append("charger",this.tableQuery.producer);

               this.$http({
                   method: "post",
                   url: "/web-server/config/medicine/query.json?action=query",
                   data: params
               }).then(res => {
                   this.tableData = res.data.rows;
                   this.tablePage.total = res.data.rowCount;
                   this.tablePage.current = res.data.pageNo;
                   this.tableLoading = false
               }).catch(err => {
                       console.log(err);
                   });
           },
           //新增
           handleCreate() {
               this.resetTemp(this.temp)
               this.dialogCreateFormVisible = true
           },
           createData() {
               this.$refs['dataForm'].validate((valid) => {
                   if (!valid) return;

                   let params = new URLSearchParams();
                   params.append("name", this.temp.name);
                   params.append("producer", this.temp.producer);
                   params.append("batchNumber", this.temp.batchNumber);
                   params.append("specs", this.temp.specs);
                   params.append("createTime", this.temp.createTime);
                   params.append("price", this.temp.price);
                   params.append("unit", this.temp.unit);
                   params.append("remark", this.temp.remark);
                   this.$http({
                       method: "post",
                       url: "/web-server/config/medicine/query.json?action=save",
                       data: params
                   }).then(res => {
                       this.dialogCreateFormVisible = false
                       this.$message.success("添加成功")
                       this.fetchData()
                   }).catch(err => {
                       console.log(err);
                   });
               })
           },
           //修改
           handleEdit(medicineId){

               let params = new URLSearchParams();
               params.append("medicineId", medicineId);
               this.$http({
                   method: "post",
                   url: "/web-server/config/medicine/query.json?action=getMedicine",
                   data: params
               }).then(res => {
                   this.temp = Object.assign({}, res.data.data)
                   this.dialogEditFormVisible = true;
               }).catch(err => {
                   console.log(err);
               });
           },
           editData(){
               this.$refs['dataForm'].validate((valid) => {
                   if (!valid) return;

                   let params = new URLSearchParams();
                   params.append("id", this.temp.id);
                   params.append("name", this.temp.name);
                   params.append("producer", this.temp.producer);
                   params.append("batchNumber", this.temp.batchNumber);
                   let createTime = new Date(this.temp.createTime);
                   params.append("createTime", createTime);
                   params.append("specs", this.temp.specs);
                   params.append("price", this.temp.price);
                   params.append("unit", this.temp.unit);
                   params.append("remark", this.temp.remark);

                   this.$http({
                       method: "post",
                       url: "/web-server/config/medicine/query.json?action=save",
                       data: params
                   }).then(res => {
                       this.dialogEditFormVisible = false;
                       this.$message.success("修改成功")
                       this.fetchData()
                   }).catch(err => {
                       console.log(err);
                   });
               })
           },
           selsChange(sels) {
               //被选中的行组成数组
               this.sels = sels;
           },
            //批量删除
           removeBatch(rows) {
               var ids = [];
               rows.forEach(element => {
                   ids.push(element.id)
               })
               let params = new URLSearchParams();
               params.append("ids", ids);
               this.$confirm('确定要删除选中的行吗?', '提示').then(() => {
                   this.$http({
                       method: "post",
                       url: "/web-server/config/medicine/query.json?action=delete",
                       data: params
                   }).then(res => {
                       this.fetchData();
                       this.$message.success("删除成功")
                   }).catch(err => {
                           console.log(err);
                       });
               }).catch(() => {
               });
           },
           singleDel(id) {
               let params = new URLSearchParams();
               params.append("ids", id);
               this.$confirm('确定要删除选中的行吗?', '提示').then(() => {
                   this.$http({
                       method: "post",
                       url: "/web-server/config/medicine/query.json?action=delete",
                       data: params
                   }).then(res => {
					   this.fetchData();
					   this.$message.success("删除成功")
                   }).catch(err => {
                       console.log(err);
                   });
               }).catch(() => {
               });
           },
           del(id){
               this.tableData.some((item, i)=>{
                   if(item.id==id){
                       this.tableData.splice(i, 1)
                       //在数组的some方法中，如果return true，就会立即终止这个数组的后续循环
                       --this.tablePage.total;
                       return true
                   }
               })
           },
            dateFormat(row, column){
              var date = row[column.property];
              return utils.formatTime(new Date(date),"yyyy-MM-dd hh:mm:ss")
             }
       }
   }
</script>
<style scoped>
    .demoTable{
        text-align: left;
        padding-left: 50px;
        margin-top: 10px;
    }
    .table{
        text-align: left;
        padding-left: 40px;
        margin-top: 10px;
    }

    .layui-inline{
        padding-right: 10px;
    }

    .tree {
        height: 600px;
        border-right: 1px #e6eaf3 solid;
    }
    .button {
        background-color: #3ebf7c;
    }
    .link {
        color: #3ebf7c
    }
    .linkPadding {
        color: #3ebf7c;
        padding-left: 5px
    }
    .page {
        float: right;
        padding-right: 35px;
        padding-bottom: 10px
    }
    .input {
        width: 200px;
    }
    .footer {
        text-align: center;
    }
</style>

