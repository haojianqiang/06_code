<template>
    <div class="">
        <div style="margin-bottom: 10px;"></div>
        <el-row>
            <el-col :span="3">
                <div class="tree">
                    <el-tree
                            :data="farmTree"
                            show-checkbox
                            ref="tree"
                            :props="defaultProps"
                            @check-change="handleCheckChange"
                            node-key="id"
                    >
                    </el-tree>
                </div>
            </el-col>
            <el-col :span="21">
                <div class="demoTable">
                    <el-form :inline="true" :model="tableQuery" class="demo-form-inline" ref="tableQuery">
                        <el-form-item label="鸡场：" prop="farmId">
                            <el-select @change="farmChange(tableQuery.farmId)" v-model.trim="tableQuery.farmId" placeholder="请选择鸡场" class="inputStyle">
                                <el-option
                                        v-for="item in farmData"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="鸡棚：" prop="houseId">
                            <el-select @change="houseChange(tableQuery.houseId)" v-model.trim="tableQuery.houseId" placeholder="请选择鸡棚" class="inputStyle">
                                <el-option
                                        v-for="item in houseData"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="种鸡批号：" prop="chicksCcsId">
                            <el-select v-model.trim="tableQuery.chicksCcsId" placeholder="种鸡批号" class="inputStyle">
                                <el-option
                                        v-for="item in chicksCcsData"
                                        :label="item.batchNumber"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>

                        <el-form-item>
                            <el-button class="button" @click="fetchData(1)" type="success">查询</el-button>
                            <el-button class="button" type="success"  @click="resetForm('tableQuery')">重置</el-button>
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
                                <a href="javascript:void(0)" @click="handleEdit(scope.row)" class="link">修改</a>
                                <a href="javascript:void(0)" @click="singleDel(scope.row.id)"
                                   class="linkPadding">删除</a>
                            </template>
                        </el-table-column>
                        <el-table-column prop="farmName" label="鸡场名称"></el-table-column>
                        <el-table-column prop="houseName" label="鸡棚编号"></el-table-column>
                        <el-table-column prop="batchNumber" label="种苗批号"></el-table-column>
                        <el-table-column :formatter="dateFormat" prop="createTime" label="登记日期" width="150"></el-table-column>
                        <el-table-column prop="avgWeight" label="平均重量"></el-table-column>
                        <el-table-column prop="hygiene" label="卫生情况"></el-table-column>
                        <el-table-column prop="water" label="用水情况"></el-table-column>
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
            </el-col>
        </el-row>

        <!--弹出窗口：新增-->
        <el-dialog :visible.sync="dialogCreateFormVisible" :modal-append-to-body='false'>
            <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="120px" style = "margin-top:20px">

                <el-row>
                    <el-col :span="11">
                        <el-form-item label="鸡场：" prop="farmId">
                            <el-select @change="farmChange(temp.farmId)" v-model.trim="temp.farmId" placeholder="请选择鸡场" style = "width:220px" >
                                <el-option
                                        v-for="item in farmData"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="11">
                        <el-form-item label="鸡棚：" prop="houseId">
                            <el-select @change="houseChange(temp.houseId)" v-model.trim="temp.houseId" placeholder="请选择鸡棚" style = "width:220px" >
                                <el-option
                                        v-for="item in houseData"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="11">
                        <el-form-item label="鸡苗批号：" prop="chicksCcsId">
                            <el-select v-model.trim="temp.chicksCcsId" placeholder="鸡苗批号" style = "width:220px" >
                                <el-option
                                        v-for="item in chicksCcsData"
                                        :label="item.batchNumber"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="11">
                        <el-form-item label="平均重量：" prop="avgWeight">
                            <el-input v-model.trim="temp.avgWeight" placeholder="请输入平均重量" style="width: 220px"/>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="11">
                        <el-form-item label="用水情况：" prop="water">
                            <el-input v-model.trim="temp.water" placeholder="用水情况" style="width: 220px" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="11">
                        <el-form-item label="卫生情况：" prop="hygiene">
                            <el-input v-model.trim="temp.hygiene" placeholder="卫生情况" style="width: 220px" />
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="11">
                        <el-form-item label="备注：" prop="remark">
                            <el-input type="textarea" v-model.trim="temp.remark" placeholder="备注" style = "width:220px" />
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
            <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="120px" style = "margin-top:20px" >

                <el-row>
                    <el-col :span="11">
                        <el-form-item label="鸡场：" prop="farmId">
                            <el-select @change="farmChange(temp.farmId)" v-model.trim="temp.farmId" placeholder="请选择鸡场" style = "width:220px" >
                                <el-option
                                        v-for="item in farmData"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="11">
                        <el-form-item label="鸡棚：" prop="houseId">
                            <el-select @change="houseChange(temp.houseId)" v-model.trim="temp.houseId" placeholder="请选择鸡棚" style = "width:220px" >
                                <el-option
                                        v-for="item in houseData"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="11">
                        <el-form-item label="鸡苗批号：" prop="chicksCcsId">
                            <el-select v-model.trim="temp.chicksCcsId" placeholder="鸡苗批号" style = "width:220px" >
                                <el-option
                                        v-for="item in chicksCcsData"
                                        :label="item.batchNumber"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="11">
                        <el-form-item label="平均重量：" prop="avgWeight">
                            <el-input v-model.trim="temp.avgWeight" placeholder="请输入平均重量" style="width: 220px" />
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="11">
                        <el-form-item label="用水情况：" prop="water">
                            <el-input v-model.trim="temp.water" placeholder="用水情况" style="width: 220px" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="11">
                        <el-form-item label="卫生情况：" prop="hygiene">
                            <el-input v-model.trim="temp.hygiene" placeholder="卫生情况" style="width: 220px" />
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="11">
                        <el-form-item label="备注：" prop="remark">
                            <el-input type="textarea" v-model.trim="temp.remark" placeholder="备注" />
                        </el-form-item>
                    </el-col>
                </el-row>

            </el-form>
            <div slot="footer" class="dialog-footer footer">
                <el-button class="button"  type="primary" @click="editData">保存</el-button>
                <el-button @click="dialogEditFormVisible = false">取消</el-button>
            </div>
        </el-dialog>

        <!--弹出窗口：点击树节点新增-->
        <el-dialog :visible.sync="dialogTreeFormVisible" :modal-append-to-body='false' :before-close="handleClose">
            <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="120px" style = "margin-top:20px" >

                <el-row>
                    <el-col :span="11">
                        <el-form-item label="用水情况：" prop="water">
                            <el-input v-model.trim="temp.water" placeholder="用水情况" style="width: 220px" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="11">
                        <el-form-item label="卫生情况：" prop="hygiene">
                            <el-input v-model.trim="temp.hygiene" placeholder="卫生情况" style="width: 220px" />
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="11">
                        <el-form-item label="平均重量：" prop="avgWeight">
                            <el-input v-model.trim="temp.avgWeight" placeholder="请输入平均重量" style="width: 220px" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="11">
                        <el-form-item label="备注：" prop="remark">
                            <el-input type="textarea" v-model.trim="temp.remark" placeholder="备注" style = "width:220px" />
                        </el-form-item>
                    </el-col>
                </el-row>

            </el-form>
            <div slot="footer" class="dialog-footer footer">
                <el-button class="button"  type="primary" @click="createData">保存</el-button>
                <el-button @click="cancelAll">取消</el-button>
            </div>
        </el-dialog>

    </div>
</template>
<script>
    import utils from '@/utils';
    export default {
        name: 'DailyRecord',
        data() {
            return {
                defaultProps: {
                    children: 'children',
                    label: 'name'
                },
                rules: {
                    farmId: [{ required: true, message: '请选择鸡场名称', trigger: 'change' }],
                    houseId: [{ required: true, message: '请选择鸡场地址', trigger: 'change' }],
                    chicksCcsId: [{ required: true, message: '请选择种鸡批号', trigger: 'change' }],
                    avgWeight: [{ required: true, message: '平均重量', trigger: 'blur' }],
                    water: [{ required: true, message: '用水情况', trigger: 'blur' }],
                    hygiene: [{ required: true, message: '卫生情况', trigger: 'blur' }]
                },
                tableLoading: false,
                tableData: [],
                farmData: [],
                houseData: [],
                feedData: [],
                chicksCcsData: [],
                typeData: [],
                raiseUnitData: [],
                sels: [],//选中显示的值
                farmTree: [],
                tableQuery: {
                    raiseTimeStart: '',
                    raiseTimeEnd: '',
                    farmId: '',
                    houseId: '',
                    raiseId: '',
                    raiseValueStart: '',
                    raiseValueEnd: '',
                    chicksCcsId: ''
                },
                tablePage: {
                    current: 1,
                    pages: [10, 20, 30, 40, 50],
                    size: 10,
                    total: null
                },
                temp: {
                    id: '',
                    farmId: '',
                    houseId: '',
                    chicksCcsId: '',
                    avgWeight: '',
                    water: '',
                    hygiene: '',
                    remark: '',
                    createTime: ''
                },
                isIndeterminate: true,
                dialogCreateFormVisible: false,
                dialogEditFormVisible: false,
                dialogTreeFormVisible: false
            }

        },
        created() {
            this.init()
            this.fetchData()
        },
        watch: {
        },
        methods: {
            handleCheckChange(data, checked, indeterminate) {
                if(checked){
                    //清空参数
                    this.resetTemp(this.temp)
                    //设置单选限制
                    this.$refs.tree.setCheckedNodes([data])
                    //选中节点
                    let tree = this.$refs.tree.getCheckedNodes(true);
                    this.temp.chicksCcsId  = tree[0].id;
                    this.dialogTreeFormVisible = true;
                }
            },
            cancelAll(){
              this.$refs.tree.setCheckedNodes([])
              this.dialogTreeFormVisible = false
            },
            init(){
                this.$http({
                    method: "post",
                    url: "/web-server/process/dailyRecord/query.json?action=init"
                }).then(res => {
                    this.farmData = res.data.data.listFarm;
                    this.farmTree = res.data.data.listFarmTree;
                })
                    .catch(err => {
                        console.log(err);
                    });
            },
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
                this.tableLoading = true
                let params = new URLSearchParams();
                params.append("pageNo", this.tablePage.current);
                params.append("pageSize",this.tablePage.size);
                params.append("farmId",this.tableQuery.farmId);
                params.append("houseId",this.tableQuery.houseId);
                params.append("chicksCcsId",this.tableQuery.chicksCcsId);
                this.$http({
                    method: "post",
                    url: "/web-server/process/dailyRecord/query.json?action=query",
                    data: params
                }).then(res => {
                    this.tableData = res.data.rows;
                    this.tablePage.total = res.data.rowCount;
                    this.tablePage.current = res.data.pageNo;
                    this.tableLoading = false
                })
                    .catch(err => {
                        console.log(err);
                    });
            },
			//点击关闭图标取消checkbox勾选
			handleClose(done){
				//清空已选节点
                this.$refs.tree.setCheckedNodes([])
                done()
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
                params.append("chicksCcsId", this.temp.chicksCcsId);
                params.append("avgWeight", this.temp.avgWeight);
                params.append("water", this.temp.water);
                params.append("hygiene", this.temp.hygiene);
                params.append("remark", this.temp.remark);
                this.$http({
                    method: "post",
                    url: "/web-server/process/dailyRecord/query.json?action=save",
                    data: params
                }).then(res => {
                    this.dialogCreateFormVisible = false
                    this.dialogTreeFormVisible = false
                    this.$message.success("添加成功")
                    this.fetchData()
                })
                    .catch(err => {
                        console.log(err);
                    });
                })
            },
            handleEdit(row){

                let params = new URLSearchParams();
                params.append("dailyId", row.id);
                params.append("farmId", row.farmId);
                params.append("houseId", row.houseId);
                this.$http({
                    method: "post",
                    url: "/web-server/process/dailyRecord/query.json?action=editHandle",
                    data: params
                }).then(res => {
                    this.houseData = res.data.data.listHouse;
                    this.chicksCcsData = res.data.data.listChicksCcs;
                    this.temp = Object.assign({}, res.data.data.dailyRecord)
                    this.temp.farmId = row.farmId;
                    this.temp.houseId = row.houseId;
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
                    params.append("chicksCcsId", this.temp.chicksCcsId);
                    params.append("avgWeight", this.temp.avgWeight);
                    params.append("water", this.temp.water);
                    params.append("hygiene", this.temp.hygiene);
                    params.append("remark", this.temp.remark);
                    let createTime = new Date(this.temp.createTime);
                    params.append("createTime", createTime);
                    this.$http({
                        method: "post",
                        url: "/web-server/process/dailyRecord/query.json?action=save",
                        data: params
                    }).then(res => {
                        this.dialogEditFormVisible = false;
						this.fetchData()
						this.$message.success("修改成功")
                    })
                        .catch(err => {
                            console.log(err);
                        });
                })
            },
            selsChange(sels) {
                //被选中的行组成数组
                this.sels = sels;
            },
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
                        url: "/web-server/process/dailyRecord/query.json?action=delete",
                        data: params
                    }).then(res => {
                        for(var i = 0;i<ids.length;i++){
                            //循环删除
							this.del(ids[i]);
							this.$message.success("删除成功")
						}						
                    })
                        .catch(err => {
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
                        url: "/web-server/process/dailyRecord/query.json?action=delete",
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
            farmChange(farmId) {
                let params = new URLSearchParams();
                params.append("farmId", farmId);
                this.$http({
                    method: "post",
                    url: "/web-server/process/dailyRecord/query.json?action=queryHouse",
                    data: params
                }).then(res => {
                    this.houseData = res.data.data;
                })
                    .catch(err => {
                        console.log(err);
                    });
            },
            houseChange(houseId) {
                let params = new URLSearchParams();
                params.append("houseId", houseId);
                this.$http({
                    method: "post",
                    url: "/web-server/process/dailyRecord/query.json?action=queryChicksCcs",
                    data: params
                }).then(res => {
                    this.chicksCcsData = res.data.rows;
                })
                    .catch(err => {
                        console.log(err);
                    });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
            dateFormat(row, column){
                var date = row[column.property];
                return utils.formatTime(new Date(date),"yyyy-MM-dd hh:mm:ss")
            }
        }
    }
</script>
<style scoped>
    .inputStyle{
        width: 150px;
        font-size: 13px;
    }
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

