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
                            <el-select @change="farmChange(tableQuery.farmId)" v-model="tableQuery.farmId" placeholder="请选择鸡场">
                                <el-option
                                        v-for="item in farmData"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>

                        <el-form-item label="鸡棚：" prop="houseId">
                            <el-select v-model="tableQuery.houseId" placeholder="请选择鸡棚">
                                <el-option
                                        v-for="item in houseData"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>

                        <el-form-item label="鸡苗批号：" prop="chicksId">
                            <el-select v-model="tableQuery.chicksId" placeholder="请选择鸡苗批号">
                                <el-option
                                        v-for="item in chicksData"
                                        :label="item.batchNumber"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>

                        <el-form-item label="鸡苗品种：" prop="chickType">
                            <el-input v-model="tableQuery.chickType" placeholder="请输入鸡苗品种"></el-input>
                        </el-form-item>

                        <el-form-item label="生产周期：" prop="produceCycle">
                            <el-input v-model="tableQuery.produceCycle" placeholder="请输入生产周期"></el-input>
                        </el-form-item>
                        <el-form-item>
                            <el-button class="button"  @click="fetchData(1)" type="success">查询</el-button>
                            <el-button class="button"  @click="reset('tableQuery')" type="success">重置</el-button>
                        </el-form-item>
                    </el-form>
                </div>

                <div class="demoTable">
                    <el-button class="button"  type="success" @click="removeBatch(sels)">批量删除</el-button>
                    <el-button class="button"  type="success" @click="handleCreate"> <i class="el-icon-plus"></i> 新增</el-button>
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
                            fit
                            border>
                        <el-table-column
                                prop="id"
                                type="selection"
                                width="55">
                        </el-table-column>
                        <el-table-column label="操作" width="100">
                            <template slot-scope="scope">
                                <a href="javascript:void(0)" @click="handleEdit(scope.row)" class="link" >修改</a>
                                <a href="javascript:void(0)" @click="singleDel(scope.row.id)" class="linkPadding" >删除</a>
                            </template>
                        </el-table-column>
                        <el-table-column prop="farmName"  label="鸡场名称" width="150"></el-table-column>
                        <el-table-column prop="houseName" label="鸡棚名称" width="150"></el-table-column>
                        <el-table-column prop="batchNumber" label="鸡苗批号"></el-table-column>
                        <el-table-column prop="chickType" label="鸡苗品种"></el-table-column>
                        <el-table-column prop="producer" label="生产企业"></el-table-column>
                        <el-table-column prop="chickCount" label="鸡苗数量"></el-table-column>
                        <el-table-column prop="produceCycle" label="生产周期（天）" width="150"></el-table-column>
                        <el-table-column prop="qrCode" label="溯源二维码" :show-overflow-tooltip="true" width="150"></el-table-column>
                        <el-table-column prop="originCode" label="溯源码"></el-table-column>
                        <el-table-column prop="createTime" label="进棚时间" :formatter="dateFormat" width="150"></el-table-column>
                        <el-table-column prop="produceTime" label="初始时间" :formatter="dateFormat" width="150"></el-table-column>
                        <el-table-column prop="modifier" label="操作员"></el-table-column>
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
            <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="100px">

                <el-row>
                    <el-col :span="8">
                        <el-form-item label="鸡场：" prop="farmId">
                            <el-select @change="farmDistributionChange(temp.farmId)" v-model="temp.farmId" placeholder="请选择鸡场">
                                <el-option
                                        v-for="item in farmData"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="鸡苗批号：" prop="chicksId">
                            <el-select @change="chicksChange(temp.chicksId)" v-model="temp.chicksId" placeholder="鸡苗批号">
                                <el-option
                                        v-for="item in chicksData"
                                        :label="item.batchNumber"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="未分配数量：" prop="">
                            {{sum}}
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col>
                        <el-table
                                :data="houseData"
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
                            <el-table-column prop="name"  label="鸡棚"></el-table-column>
                            <el-table-column prop="capacity" label="容量（只）"></el-table-column>
                            <el-table-column prop="stock" label="存栏量"></el-table-column>
                            <el-table-column prop="chickCount" label="本次分配数量">
                                <template slot-scope="scope">
                                    <el-input v-model="scope.row.chickCount" placeholder="本次分配数量"></el-input>
                                </template>
                            </el-table-column>
                        </el-table>
                    </el-col>
                </el-row>

            </el-form>
            <div slot="footer" class="dialog-footer footer">
                <el-button class="button"  type="primary" @click="saveBatch(sels)">保存</el-button>
                <el-button @click="dialogCreateFormVisible = false">取消</el-button>
            </div>
        </el-dialog>

        <!--弹出窗口：修改-->
        <el-dialog :visible.sync="dialogEditFormVisible":modal-append-to-body='false'>
            <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="120px">

                <el-row>
                    <el-col :span="8">
                        <el-form-item label="鸡场：" prop="">
                            <el-select disabled  @change="farmDistributionChange(temp.farmId)" v-model="temp.farmId" placeholder="请选择鸡场">
                                <el-option
                                        v-for="item in farmData"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="鸡苗批号：" prop="">
                            <el-select disabled @change="chicksChange(temp.chicksId)" v-model="temp.chicksId" placeholder="鸡苗批号">
                                <el-option
                                        v-for="item in chicksData"
                                        :label="item.batchNumber"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="未分配数量：" prop="">
                            {{sum}}
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col>
                        <el-table
                                :data="houseData"
                                v-loading="tableLoading"
                                element-loading-text="加载中"
                                :header-cell-style="{background:'#e6eaf3',height:'50px'}"
                                @selection-change="selsChange"
                                border>
                            <el-table-column prop="name"  label="鸡棚"></el-table-column>
                            <el-table-column prop="capacity" label="容量（只）"></el-table-column>
                            <el-table-column prop="stock" label="存栏量"></el-table-column>
                            <el-table-column prop="chickCount" label="修改分配数量">
                                <template slot-scope="scope">
                                    <el-input v-model="scope.row.chickCount" placeholder="修改分配数量"></el-input>
                                </template>
                            </el-table-column>
                        </el-table>
                    </el-col>
                </el-row>

            </el-form>
            <div slot="footer" class="dialog-footer footer">
                <el-button class="button"   type="primary" @click="update">保存</el-button>
                <el-button @click="dialogEditFormVisible = false">取消</el-button>
            </div>
        </el-dialog>

        <!--弹出窗口：点击树节点新增-->
        <el-dialog :visible.sync="dialogTreeFormVisible" :modal-append-to-body='false' :before-close="handleClose">
            <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="120px">

                <el-row>
                    <el-col :span="8">
                        <el-form-item label="鸡场：" prop="farmId">
                            <el-select @change="farmDistributionChange(temp.farmId)" disabled="true" v-model="temp.farmId" placeholder="请选择鸡场">
                                <el-option
                                        v-for="item in farmData"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="鸡苗批号：" prop="chicksId">
                            <el-select @change="chicksChange(temp.chicksId)" v-model="temp.chicksId" placeholder="鸡苗批号">
                                <el-option
                                        v-for="item in chicksData"
                                        :label="item.batchNumber"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="未分配数量：" prop="">
                            {{sum}}
                        </el-form-item>>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col>
                        <el-table
                                :data="houseData"
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
                            <el-table-column prop="name"  label="鸡棚"></el-table-column>
                            <el-table-column prop="capacity" label="容量（只）"></el-table-column>
                            <el-table-column prop="stock" label="存栏量"></el-table-column>
                            <el-table-column prop="chickCount" label="本次分配数量">
                                <template slot-scope="scope">
                                    <el-input v-model="scope.row.chickCount" placeholder="本次分配数量"></el-input>
                                </template>
                            </el-table-column>
                        </el-table>
                    </el-col>
                </el-row>

            </el-form>
            <div slot="footer" class="dialog-footer footer" >
                <el-button class="button"   type="primary" @click="saveBatch(sels)">保存</el-button>
                <el-button @click="cancelAll">取消</el-button>
            </div>
        </el-dialog>

    </div>
</template>
<script>
    import Utils from "../../../utils";

    export default {
        name: 'ChicksDistribution',
        data() {
            return {
                defaultProps: {
                    children: 'children',
                    label: 'name'
                },
                rules:{
                    farmId: [{ required: true, message: '必填', trigger: 'change' }],
                    houseId: [{ required: true, message: '必填', trigger: 'change' }],
                    chicksId: [{ required: true, message: '必填', trigger: 'change' }],
                    produceCycle: [{ required: true, message: '必填', trigger: 'blur' }],
                    chickCount: [{ required: true, message: '必填', trigger: 'blur' }],
                    unallocatedCapacity: [{required: true, message: '必填',trigger: 'change'}]
                },
                //下拉框数据绑定
                farmData: '',
                houseData: '',
                chicksData: '',
                tableLoading: false,
                tableData: [],
                sels: [],//选中显示的值
                farmTree: [],
                tableQuery: {
                    farmId: '',
                    houseId: '',
                    chicksId: '',
                    produceCycle: '',
                    chickType: ''
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
                    chicksId: '',
                    chickCount: '',
                    produceCycle: '',
                    createTime: '',
                    modifier: '',
                    modifierId: '',
                    originCode: '',
                    status: 1,
                    unallocatedCapacity: '',//鸡苗未分配数量
                },
                isIndeterminate: true,
                dialogCreateFormVisible: false,
                dialogEditFormVisible: false,
                dialogTreeFormVisible: false
            }

        },
        created() {
            this.fetchData()
            this.init()
        },

        watch: {
        },

        methods: {
            saveBatch(rows) {
                this.$refs['dataForm'].validate((valid) => {
                        if (!valid) return;

                if(rows.length === 0){
                    this.$message.error("请选择选项")
                    return
                }
                let houseVoList = JSON.stringify(rows);
                let params = new URLSearchParams();
                params.append("houseVoList", houseVoList);
                params.append("chicksId", this.temp.chicksId);
                this.$http({
                    method: "post",
                    url: "/web-server/config/chicksDistribution/query.json?action=save",
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
            update() {
                let houseVoList = JSON.stringify(this.houseData);
                let params = new URLSearchParams();
                params.append("houseVoList", houseVoList);
                this.$http({
                    method: "post",
                    url: "/web-server/config/chicksDistribution/query.json?action=update",
                    data: params
                }).then(res => {
                    this.dialogEditFormVisible = false
                    this.$message.success("修改成功")
                    this.fetchData()
                })
                    .catch(err => {
                        console.log(err);
                    });
            },
            handleCheckChange(data, checked, indeterminate) {
                if(checked){
                    //清空参数
                    this.resetTemp(this.temp)
                    this.temp.unallocatedCapacity = '';
                    //设置单选限制
                    this.$refs.tree.setCheckedNodes([data])
                    //选中节点
                    let tree = this.$refs.tree.getCheckedNodes(true);
                    this.temp.farmId  = tree[0].id;
                    let params = new URLSearchParams();
                    params.append("farmId", this.temp.farmId);
                    this.$http({
                        method: "post",
                        url: "/web-server/config/chicksDistribution/query.json?action=getUnDistributionHouse",
                        data: params
                    }).then(res => {
                        this.houseData = res.data.data.houseList;
                        this.chicksData = res.data.data.chicksList;
                        this.dialogTreeFormVisible = true;
                    })
                        .catch(err => {
                            console.log(err);
                        });
                }

            },
            cancelAll(){
                //清空已选择节点
                this.$refs.tree.setCheckedNodes([])
                this.dialogTreeFormVisible = false
            },
            //判断是否为数字类型
            isNumber(val){
                var regPos = /^\d+(\.\d+)?$/; //非负浮点数
                var regNeg = /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/; //负浮点数
                return regPos.test(val) || regNeg.test(val);
            },
            //清空对象所有属性
            resetTemp(temp){
                for(let prop in temp){
                    temp[prop] = '';
                }
                return temp;
            },
            init(){
                this.$http({
                    method: "post",
                    url: "/web-server/config/chicksDistribution/query.json?action=init"
                }).then(res => {
                    this.farmData = res.data.data.listFarm;
                    this.farmTree = res.data.data.listFarmTree;
                })
                    .catch(err => {
                        console.log(err);
                    });
            },
            farmDistributionChange(farmId) {
                //清空参数
                this.temp.chicksId = ''
                this.temp.unallocatedCapacity = ''
                let params = new URLSearchParams();
                params.append("farmId", farmId);
                this.$http({
                    method: "post",
                    url: "/web-server/config/chicksDistribution/query.json?action=getUnDistributionHouse",
                    data: params
                }).then(res => {
                    this.houseData = res.data.data.houseList;
                    this.chicksData = res.data.data.chicksList;
                })
                    .catch(err => {
                        console.log(err);
                    });
            },
            farmChange(farmId) {
                let params = new URLSearchParams();
                params.append("farmId", farmId);
                this.$http({
                    method: "post",
                    url: "/web-server/config/chicksDistribution/query.json?action=getHouseAndChicks",
                    data: params
                }).then(res => {
                    //清空
                    this.tableQuery.houseId = ''
                    this.tableQuery.chicksId = ''
                    this.houseData = res.data.data.houseList;
                    this.chicksData = res.data.data.chicksList;
                })
                    .catch(err => {
                        console.log(err);
                    });
            },
            chicksChange(chicksId) {
                let params = new URLSearchParams();
                params.append("chicksId", chicksId);
                this.$http({
                    method: "post",
                    url: "/web-server/config/chicksDistribution/query.json?action=getChicks",
                    data: params
                }).then(res => {
                    this.temp.unallocatedCapacity = res.data.data.unallocatedCapacity;
                })
                    .catch(err => {
                        console.log(err);
                    });
            },
            //时间格式化
            dateFormat:function(row, column) {
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return Utils.formatTime(new Date(date),'yyyy-MM-dd hh:mm:ss');
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
                params.append("produceCycle",this.tableQuery.produceCycle);
                params.append("batchNumber",this.tableQuery.batchNumber);
                params.append("chickType",this.tableQuery.chickType);
                this.$http({
                    method: "post",
                    url: "/web-server/config/chicksDistribution/query.json?action=query",
                    data: params
                }).then(res => {
                    this.tableData = res.data.rows;
                    console.info(res.data.rows);
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
                this.houseData = [];
                this.chicksData = [];
                this.temp.unallocatedCapacity = '';
                this.dialogCreateFormVisible = true
            },
            createData() {
                this.$refs['dataForm'].validate((valid) => {
                    if (!valid) return;

                    let params = new URLSearchParams();
                    params.append("houseId", this.temp.houseId);
                    params.append("chickCount", this.temp.chickCount);
                    params.append("produceCycle", this.temp.produceCycle);
                    params.append("chicksId", this.temp.chicksId);
                    this.$http({
                        method: "post",
                        url: "/web-server/config/chicksDistribution/query.json?action=save",
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
                params.append("chicksDistributionId", row.id);
                params.append("farmId", row.farmId);
                this.$http({
                    method: "post",
                    url: "/web-server/config/chicksDistribution/query.json?action=editHandle",
                    data: params
                }).then(res => {
                    this.houseData = [];
                    let houseVo = res.data.data.houseVo;
                    this.houseData.push(houseVo);
                    this.temp.farmId = row.farmId;
                    this.chicksData = res.data.data.chicksList;
                    this.temp.chicksId = row.chicksId;
                    this.chicksData.some((item)=>{
                        if(item.id==this.temp.chicksId){
                            this.temp.unallocatedCapacity = item.unallocatedCapacity;
                            return true
                        }
                    })
                    this.dialogEditFormVisible = true;
                })
                    .catch(err => {
                        console.log(err);
                    });
            },
            editData(){
                this.$refs['dataForm'].validate((valid) => {
                    if (!valid) return;

                    let params = new URLSearchParams();
                    params.append("id", this.temp.id);
                    params.append("houseId", this.temp.houseId);
                    params.append("chickCount", this.temp.chickCount);
                    params.append("produceCycle", this.temp.produceCycle);
                    params.append("chicksId", this.temp.chicksId);
                    params.append("modifier", this.temp.modifier);
                    params.append("modifierId", this.temp.modifierId);
                    let createTime = new Date(this.temp.createTime);
                    params.append("createTime", createTime);

                    this.$http({
                        method: "post",
                        url: "/web-server/config/chicksDistribution/query.json?action=save",
                        data: params
                    }).then(res => {
                        this.dialogEditFormVisible = false;
                        this.fetchData()
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
                        url: "/web-server/config/chicksDistribution/query.json?action=delete",
                        data: params
                    }).then(res => {
						this.fetchData();
						this.$message.success("删除成功")
                    })
                        .catch(err => {
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
            singleDel(id) {
                let params = new URLSearchParams();
                params.append("ids", id);
                this.$confirm('确定要删除选中的行吗?', '提示').then(() => {
                    this.$http({
                        method: "post",
                        url: "/web-server/config/chicksDistribution/query.json?action=delete",
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
            reset(tableQuery) {
                this.$refs[tableQuery].resetFields();
            },
        },
        computed: {
            sum: function(){
                let sum;
                if(this.temp.unallocatedCapacity != null && this.temp.unallocatedCapacity != ''){
                    sum = this.temp.unallocatedCapacity;
                }
                let total = 0;
                for(let i = 0;i<this.houseData.length;i++){
                    if(this.houseData[i].chickCount != null && this.houseData[i].chickCount != ''){
                        if(this.houseData[i].chickCount > this.houseData[i].capacity){
                            this.$message.error("分配数量不能超过鸡棚容量")
                        }
                        sum = sum - parseInt(this.houseData[i].chickCount);
                        total += parseInt(this.houseData[i].chickCount);
                    }
                }
                if(total > this.temp.unallocatedCapacity){
                    this.$message.error("分配数量不能超过未分配数量")
                }else {
                    return sum;
                }
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

