<template>
    <div>
       <el-tabs   style="margin-left:10px ">
           <el-tab-pane label="信息查询" >
              <div>
               <el-form :inline="true" :model="tableQuery" ref ="tableQuery" class="demo-form-inline">
                 <el-form-item label="收购商名称：" prop="assemblers" >
                     <el-input v-model="tableQuery.assemblers"   placeholder="请输入名称"></el-input>
                 </el-form-item>
                 <el-form-item label="交易时间：" prop="startTime">
                    <el-date-picker
                          type="date"
                          v-model="tableQuery.startTime"
                          placeholder="选择开始时间">
                    </el-date-picker>
                 </el-form-item>
                  <el-form-item   prop="endTime">
                    <el-date-picker
                          type="date"
                          v-model="tableQuery.endTime"
                          placeholder="选择结束时间">
                    </el-date-picker>
                  </el-form-item>
                 <el-form-item>
                     <el-button style="background-color: #3ebf7c" @click="fetchData(1)" type="success">查询</el-button>
                     <el-button style="background-color: #3ebf7c" @click="reset('tableQuery')" type="success">重置</el-button>
                 </el-form-item>
              </el-form>
             </div>

             <div>
                 <el-button size="mini" style="background-color: #3ebf7c" type="success" @click="removeBatch(selection)">批量删除</el-button>
             </div>

             <div class="demoTable" style = "margin-top:5px">
                <el-table
                        :data="tableData"
                        v-loading="tableLoading"
                        element-loading-text="加载中"
                        :header-cell-style="{background:'#e6eaf3',height:'50px'}"
                        @selection-change="selectionChange"
                        border>
                    <el-table-column
                            prop="id"
                            type="selection"
                            width="55">
                    </el-table-column>
                    <el-table-column label="操作">
                        <template slot-scope="scope">
                            <a href="javascript:void(0)" @click="seeDetails(scope.row.id)" style="color: #3ebf7c">查看详情</a>
                            <a href="javascript:void(0)" @click="singleDel(scope.row.id)" style="color: #3ebf7c;padding-left: 5px">删除</a>
                        </template>
                    </el-table-column>
                    <el-table-column prop="assemblers" label="收购商"></el-table-column>
                    <el-table-column prop="price" label="收购单价"></el-table-column>
                    <el-table-column prop="count" label="出栏总数"></el-table-column>
                    <el-table-column prop="total" label="总金额"></el-table-column>
                    <el-table-column :formatter="dateFormat" prop="sale_time" label="出售时间"></el-table-column>
                </el-table>
            </div>

            <div class="demoTable" style="float: right;padding-right: 35px;padding-bottom: 10px">
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
               <!--弹窗，显示详情-->
                <el-dialog :visible.sync="dialogShowDetailVisible"  :modal-append-to-body='false'>
                     <div style = " text-align: center;margin-left:20px ; margin-top:20px ;width:94%">
                         <el-table
                              :data="detailData"
                              :header-cell-style="{background:'#e6eaf3',height:'30px'}"
                              border>
                                  <el-table-column prop="name" label="鸡棚名称"/>
                                  <el-table-column prop="saleCount" label="出栏数量"/>
                         </el-table>
                     </div>
                     <div slot="footer" class="dialog-footer" style="text-align: center;margin-top:10px">
                           <el-button  size="mini" @click="dialogShowDetailVisible = false">关闭</el-button>
                     </div>
                </el-dialog>

           </el-tab-pane>

           <el-tab-pane label="信息录入" >
              <div class="grid-content bg-purple" style= "border:1px solid #EEEEEE" >
                <div style="background: #d3dce6;text-align:center;">成交量管理：</div>
                <div style=" margin-left:20px; margin-top:25px;">
                          <el-form :inline="true" :model="tableQuery" >
                             <el-form-item label="日期:" prop="createTime">
                               <el-date-picker v-model="saveData.saleTime" type="date" placeholder="交易日期"/>
                             </el-form-item>
                             <el-form-item label="收购商名称:">
                                  <el-input v-model.trim="saveData.assemblers"  placeholder="收购商名称"/>
                             </el-form-item>
                             <el-form-item label="收购单价:">
                                  <el-input v-model.trim="saveData.price"  placeholder="收购单价"></el-input>
                             </el-form-item>
                             <el-form-item label="出栏总数:">
                                <el-input type="number"  v-model.trim="saveData.count" placeholder="出栏总数"></el-input>
                             </el-form-item>
                             <el-form-item label="总金额:">
                                  <el-input v-model.trim="saveData.total" placeholder="总金额"></el-input>
                             </el-form-item>
                          </el-form>
                          <div>
                            <el-button type="success" @click = "addHouseDetails" size="mini">添加鸡棚出栏详情</el-button>
                          </div>
                          <div style="margin-top:5px;text-align:center;background-color: #e5e9f2">
                            <el-row >
                                 <el-col :span="8">
                                     <div >
                                       鸡棚名称
                                     </div>
                                 </el-col>
                                 <el-col :span="8">
                                     <div >
                                       出栏数
                                     </div>
                                 </el-col>
                                  <el-col :span="8">
                                      <div >
                                        操作
                                      </div>
                                  </el-col>
                            </el-row>
                       </div>
                          <div style="margin-top:1px;text-align:center;border:1px solid #EEEEEE">
                              <el-row v-for="(item ,index) in detailsList" >
                                   <el-col :span="8">
                                       <div >
                                         {{item.houseName}}
                                       </div>
                                   </el-col>
                                   <el-col :span="8">
                                       <div >
                                        {{item.saleCount}}
                                       </div>
                                   </el-col>
                                   <el-col :span="8">
                                      <div >
                                       <i class="el-icon-close" @click="del(index)"></i>
                                      </div>
                                   </el-col>
                              </el-row>
                         </div>
                </div>
                <div style="background: #e5e9f2;text-align:center;margin-top:110px;">
                    <el-button style="background-color: #3ebf7c"  type="success" class="btn-save"
                               @click="saveSaleRecordInfo">保存
                    </el-button>
                </div>
            </div>
             <!--弹出窗口：添加鸡舍出栏详情-->
             <el-dialog :visible.sync="dialogEditFormVisible" width="500px" :modal-append-to-body='false'>
                  <el-form   label-position="right" label-width="120px">
                         <el-form-item label="鸡场名称："  >
                             <el-select v-model="farmId" placeholder="请选择鸡场" @change="farmSelectValueChange">
                                 <el-option v-for="item in farms" :key="item.id" :label="item.name" :value="item.id"/>
                             </el-select>
                         </el-form-item>

                         <el-form-item label="鸡棚名称："  >
                         <el-select v-model="houseId"  placeholder="请选择鸡棚" @change="houseSelectValueChange" >
                             <el-option v-for="item in houses" :key="item.id" :label="item.name" :value="item.id"/>
                         </el-select>
                        </el-form-item>
                      <el-form-item label="鸡苗批号："  >
                          <el-select v-model="chickCcsId"  placeholder="请选择鸡苗批号">
                              <el-option v-for="item in chicksDistributions" :key="item.id" :label="item.batchNumber" :value="item.id"/>
                          </el-select>
                      </el-form-item>
                        <el-form-item label="出栏数："  >
                            <el-input type="number" v-model="saleCount"  placeholder="请输入出栏数"></el-input>
                        </el-form-item>

                    </el-form>
                    <div slot="footer" class="dialog-footer" style="text-align: center">
                        <el-button style="background-color: #3ebf7c"  type="primary"  class="btn-save" @click="saveDialog">确定</el-button>
                        <el-button  @click="dialogEditFormVisible = false">取消</el-button>
                    </div>
                </el-dialog>

           </el-tab-pane>
       </el-tabs>
    </div>
</template>
<script>
import utils from "@/utils"
export default {
    name:'SaleRecord',
    data(){
      return {
        //信息查询部分数据
       tableLoading: false,
       tableData: [],
       selection: [],//选中显示的值
       detil:'',
       dialogShowDetailVisible: false,//是否显示详情
       detailData:'',
       tableQuery: {
           assemblers: '',
           startTime: '',
           endTime: ''
       },
       tablePage: {
           current: 1,
           pages: [10, 20, 30, 40, 50],
           size: 10,
           total: null
       },
       //信息录入的数据
      dialogEditFormVisible: false,//是否显示添加鸡舍出栏信息窗口
      saveData:{
           id:'',
           saleTime:'',
           assemblers:'',//收购商
           price:'',
           count:'',//出栏总数
           total:''//总金额
      },
      //鸡棚出栏集合
      detailsList:[ ],
      //下拉框数据源
      farms: '',
      houses: '',
      chicksDistributions: '',
      //鸡舍信息
      farmId:'',
      houseId:'',
      chickCcsId: '',
      houseName:'',
      saleCount:''
      }
    },
    created() {
       this.fetchData()
       },
    methods:{
       dateFormat(row, column){
           var date = row[column.property];
           return utils.formatTime(new Date(date),"yyyy-MM-dd hh:mm:ss")
       },
       //重置表单的元素
      reset(formName) {
          console.log("重置了");
          this.$refs[formName].resetFields();
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
      //查询交易记录
      fetchData(current){
       if(current){
             this.tablePage.current = current
         }
        this.tableLoading = true;//正在加载数据

        let params = new URLSearchParams();
        params.append("pageNo", this.tablePage.current);
        params.append("pageSize",this.tablePage.size);
        params.append("assemblers",this.tableQuery.assemblers);
        let startTime = '';
        let endTime = '';
        if(this.tableQuery.startTime != ''){
             startTime = utils.formatTime(this.tableQuery.startTime,"yyyy-MM-dd")
        }
        if(this.tableQuery.endTime != ''){
             endTime = utils.formatTime(this.tableQuery.endTime,"yyyy-MM-dd")
        }
        params.append("startTime",startTime);
        params.append("endTime",endTime);
        this.$http({
            method: "post",
            url: "/web-server/saleRecord/query.json?action=query",
            data: params
        }).then(res => {
            this.tableData = res.data.rows;
            this.tableLoading = false
        }).catch(err => {
            console.log(err);
        });

      },
      //删除记录
      singleDel(id) {
         let params = new URLSearchParams();
         params.append("ids", id);
         this.$confirm('确定要删除选中的行吗?', '提示').then(() => {
             this.$http({
                 method: "post",
                 url: "/web-server/saleRecord/query.json?action=delete",
                 data: params
             }).then(res => {
                 this.fetchData();
             }).catch(err => {
                 console.log(err);
             });
         }).catch(() => {
         });
      },
       selectionChange(selection) {
         //被选中的行组成数组
         this.selection = selection;
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
                   url: "/web-server/saleRecord/query.json?action=delete",
                   data: params
               }).then(res => {
                   for(var i = 0;i<ids.length;i++){
                       //循环删除
                       this.del(ids[i]);
                   }
                   this.$message.success("删除成功")
                   this.fetchData();
               }).catch(err => {
                       console.log(err);
                   });
           }).catch(() => {
           });
       },
      //查看详情
      seeDetails(id){
          let params = new URLSearchParams();
          params.append("parentId", id);
          this.$http({
              method: "post",
              url: "/web-server/saleRecord/query.json?action=queryHouseDetails",
              data: params
          }).then(res => {
               this.detailData = res.data.data;
               this.dialogShowDetailVisible = !this.dialogShowDetailVisible;
          }).catch(err => {
              console.log(err);
          });
      },

      //下面是信息录入方面的方法
      //删除每个鸡舍的出栏详情
      del(index){
       this.detailsList.splice(index,1);
      },
      addHouseDetails(){
       this.dialogEditFormVisible = !this.dialogEditFormVisible;
       this.fetchFarms();
      },
      //获取鸡场列表，包含名称及id
      fetchFarms() {
       this.$http({
              method: "get",
              url: "/web-server/monitor/housemonitor/query.json?action=getFarmNameAndId"
          }).then(res => {
              this.farms = res.data.data;
          }).catch(err => {
              console.log(err);
          });
      },
      //根据鸡场id获取鸡舍列表，名称及id
      fetchHouses(farmId) {
          let params = new URLSearchParams();
          params.append("farmId", farmId);
          this.$http({
              method: "post",
              url: "/web-server/monitor/housemonitor/query.json?action=getHouseNameAndId",
              data: params
          }).then(res => {
              this.houses = res.data.data;
          }).catch(err => {
              console.log(err);
          });
      },
        //根据鸡棚ID获取鸡苗批号和分配鸡苗ID
        fetchChicksDistribution(houseId) {
            let params = new URLSearchParams();
            params.append("houseId", farmId);
            this.$http({
                method: "post",
                url: "/web-server/monitor/housemonitor/query.json?action=getBatchNumberAndId",
                data: params
            }).then(res => {
                this.chicksDistributions = res.data.data;
            }).catch(err => {
                console.log(err);
            });
        },
      //鸡场下拉框选择事件
      farmSelectValueChange(farmId) {
        this.houseId = null;
        this.houses = null;
        this.chickCcsId = null;
        this.chicksDistributions = null;
        this.fetchHouses(farmId);
      },
     //鸡棚下拉框变化时
     houseSelectValueChange(val){
           this.chickCcsId = null;
           this.chicksDistributions = null;
         let params = new URLSearchParams();
         params.append("houseId", val);
         this.$http({
             method: "post",
             url: "/web-server/monitor/housemonitor/query.json?action=getBatchNumberAndId",
             data: params
         }).then(res => {
             this.chicksDistributions = res.data.data;
         }).catch(err => {
             console.log(err);
         });
       let obj = {};
       obj = this.houses.find((item)=>{
             return item.id === val;
            });
       this.houseName = obj.name;
     },
     saveDialog(){
      this.dialogEditFormVisible = !this.dialogEditFormVisible;
      if(this.saleCount == ''){
         return;
      }
      //如果已经存在，则累加
      var isHas = false;
      var objIndex = this.detailsList.findIndex(
           (item) =>  { return item.houseId == this.houseId}
       );
      if(objIndex == -1){
         let data = {
             houseId:this.houseId,
             chickCcsId:this.chickCcsId,
             houseName:this.houseName,
             saleCount:this.saleCount
         };
         this.detailsList.push(data);
      }else{
        this.detailsList[objIndex].saleCount = parseInt(this.detailsList[objIndex].saleCount) + parseInt(this.saleCount);
      }
      this.saleCount = null;
     },
     //保存交易信息
     saveSaleRecordInfo(){
        if (this.saveData.saleTime === "" || this.saveData.saleTime == null || this.saveData.saleTime === undefined) {
              alert("请选择日期");
              return false;
         }
         if (this.saveData.assemblers === "" || this.saveData.assemblers == null || this.saveData.assemblers === undefined) {
              alert("请输入收购商");
              return false;
         }
         if (this.saveData.price === "" || this.saveData.price == null || this.saveData.price === undefined) {
            alert("请输入单价");
            return false;
         }
         if (this.saveData.count === "" || this.saveData.count == null || this.saveData.count === undefined) {
             alert("请输入总数");
             return false;
          }
         if (this.saveData.total === "" || this.saveData.total == null || this.saveData.total === undefined) {
            alert("请输入总金额");
            return false;
         }
         //查看总出栏数是否一致
         let count = 0 ;
         this.detailsList.forEach((item) => {count = parseInt(count) + parseInt(item.saleCount)});
         console.log(parseInt(this.saveData.count));
         console.log( parseInt(count));
         if(parseInt(this.saveData.count) != parseInt(count) ){
              alert("各鸡棚的出栏数之和不等于总出栏数！");
              return false;
         }
         let params = new URLSearchParams();
         params.append("assemblers", this.saveData.assemblers);
         params.append("price", this.saveData.price);
         params.append("count", this.saveData.count);
         params.append("saleTime", this.saveData.saleTime);
         params.append("total", this.saveData.total);
         params.append("saleRecordCcs",JSON.stringify(this.detailsList));
         this.$http({
              method: "post",
              url: "/web-server/saleRecord/query.json?action=save",
              data: params
          }).then(res => {
              this.$message.success("添加成功")
              this.fetchData();
          }).catch(err => {
              console.log(err);
          });


     }

    }
}
</script>