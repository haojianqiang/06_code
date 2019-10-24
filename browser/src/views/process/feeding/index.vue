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
                        <el-form-item label="起始时间：" prop="raiseTimeStart">
                            <el-date-picker
                                    :clearable="false"
                                    v-model.trim="tableQuery.raiseTimeStart"
                                    type="date"
                                    placeholder="选择日期时间" class="inputStyle">
                            </el-date-picker>
                        </el-form-item>

                        <el-form-item label="终止时间：" prop="raiseTimeEnd">
                            <el-date-picker
                                    :clearable="false"
                                    v-model.trim="tableQuery.raiseTimeEnd"
                                    type="date"
                                    placeholder="选择日期时间" class="inputStyle">
                            </el-date-picker>
                        </el-form-item>

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
                        <el-form-item label="饲料品牌：" prop="raiseId">
                            <el-select v-model.trim="tableQuery.raiseId" placeholder="请选择饲料品牌" class="inputStyle">
                                <el-option
                                        v-for="item in feedData"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="饲料用量（起）：" prop="raiseValueStart">
                            <el-input v-model.trim="tableQuery.raiseValueStart" placeholder="请输入饲料用量" class="inputStyle"></el-input>
                        </el-form-item>
                        <el-form-item label="饲料用量（止）：" prop="raiseValueEnd">
                            <el-input v-model.trim="tableQuery.raiseValueEnd" placeholder="请输入饲料用量" class="inputStyle"></el-input>
                        </el-form-item>
                        <el-form-item label="种鸡批次：" prop="chicksCcsId">
                            <el-select v-model.trim="tableQuery.chicksCcsId" placeholder="种鸡批次" class="inputStyle">
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

                    <el-button class="button" type="success" @click="removeBatch(sels)">批量删除</el-button>
                    <el-button class="button" type="success" @click="handleCreate"><i
                            class="el-icon-plus"></i> 新增
                    </el-button>
                </div>

                    <div class="demoTable">
                <div style="margin-bottom: 10px;"></div>
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
                        <el-table-column prop="raiseName" label="饲料品牌"></el-table-column>
                        <el-table-column prop="raiseValue" label="饲料用量"></el-table-column>
                        <el-table-column :formatter="dateFormat" prop="raiseTime" label="投喂时间"></el-table-column>
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
            <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="120px">
                <el-row>
                    <el-col :span="11">
                        <el-form-item label="鸡场：" prop="farmId">
                            <el-select @change="farmChange(temp.farmId)" v-model.trim="temp.farmId" placeholder="请选择鸡场">
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
                            <el-select @change="houseChange(temp.houseId)" v-model.trim="temp.houseId" placeholder="请选择鸡棚">
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
                        <el-form-item label="类型：" prop="raiseType">
                            <el-select v-model.trim="temp.raiseType" placeholder="请选择类型">
                                <el-option
                                        v-for="item in raiseTypeData"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="11">
                        <el-form-item label="饲料品牌：" prop="raiseId">
                            <el-select @change="feedChange(temp.raiseId)" v-model.trim="temp.raiseId" placeholder="请选择饲料品牌">
                                <el-option
                                        v-for="item in feedData"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="11">
                        <el-form-item label="饲料使用量：" prop="raiseValue">
                            <el-input v-model.trim="temp.raiseValue" placeholder="公斤" ></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="11">
                        <el-form-item label="用量单位：" prop="raiseUnit">
                            <el-select v-model.trim="temp.raiseUnit" placeholder="请选择单位">
                                <el-option label="公斤" value="公斤"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="11">
                        <el-form-item label="投喂时间：" prop="raiseTime">
                            <el-date-picker
                                    v-model.trim="temp.raiseTime"
                                    type="datetime"
                                    placeholder="选择日期时间"
                                    style="width: 194px">
                            </el-date-picker>
                        </el-form-item>
                    </el-col>
                    <el-col :span="11">
                        <el-form-item label="鸡苗批号：" prop="chicksCcsId">
                            <el-select v-model.trim="temp.chicksCcsId" placeholder="鸡苗批号">
                                <el-option
                                        v-for="item in chicksCcsData"
                                        :label="item.batchNumber"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <div slot="footer" class="dialog-footer footer">
                <el-button class="button"  type="primary" @click="createData">保存</el-button>
                <el-button @click="dialogCreateFormVisible = false">取消</el-button>
            </div>
        </el-dialog>

        <!--弹出窗口：修改-->
        <el-dialog :visible.sync="dialogEditFormVisible" :modal-append-to-body='false'>
            <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="120px">
                <el-row>
                    <el-col :span="11">
                        <el-form-item label="鸡场：" prop="farmId">
                            <el-select @change="farmChange(temp.farmId)" v-model.trim="temp.farmId" placeholder="请选择鸡场">
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
                            <el-select @change="houseChange(temp.houseId)" v-model.trim="temp.houseId" placeholder="请选择鸡棚">
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
                        <el-form-item label="类型：" prop="raiseType">
                            <el-select v-model.trim="temp.raiseType" placeholder="请选择类型">
                                <el-option
                                        v-for="item in raiseTypeData"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="11">
                        <el-form-item label="饲料品牌：" prop="raiseId">
                            <el-select @change="feedChange(temp.raiseId)" v-model.trim="temp.raiseId" placeholder="请选择饲料品牌">
                                <el-option
                                        v-for="item in feedData"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="11">
                        <el-form-item label="饲料使用量：" prop="raiseValue">
                            <el-input v-model.trim="temp.raiseValue" placeholder="公斤" ></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="11">
                        <el-form-item label="用量单位：" prop="raiseUnit">
                            <el-select v-model.trim="temp.raiseUnit" placeholder="请选择单位">
                                <el-option label="公斤" value="公斤"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="11">
                        <el-form-item label="投喂时间：" prop="raiseTime">
                            <el-date-picker
                                    v-model.trim="temp.raiseTime"
                                    type="datetime"
                                    placeholder="选择日期时间"
                                    style="width: 194px">
                            </el-date-picker>
                        </el-form-item>
                    </el-col>
                    <el-col :span="11">
                        <el-form-item label="鸡苗批号：" prop="chicksCcsId">
                            <el-select v-model.trim="temp.chicksCcsId" placeholder="鸡苗批号">
                                <el-option
                                        v-for="item in chicksCcsData"
                                        :label="item.batchNumber"
                                        :value="item.id">
                                </el-option>
                            </el-select>
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
            <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="120px">

                <el-row>
                    <el-col :span="11">
                        <el-form-item label="类型：" prop="raiseType">
                            <el-select v-model.trim="temp.raiseType" placeholder="请选择类型">
                                <el-option
                                        v-for="item in raiseTypeData"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="11">
                        <el-form-item label="饲料品牌：" prop="raiseId">
                            <el-select @change="feedChange(temp.raiseId)" v-model.trim="temp.raiseId" placeholder="请选择饲料品牌">
                                <el-option
                                        v-for="item in feedData"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="11">
                        <el-form-item label="饲料使用量：" prop="raiseValue">
                            <el-input v-model.trim="temp.raiseValue" placeholder="公斤"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="11">
                        <el-form-item label="用量单位：" prop="raiseUnit">
                            <el-select v-model.trim="temp.raiseUnit" placeholder="请选择单位">
                                <el-option label="公斤" value="公斤"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="11">
                        <el-form-item label="投喂时间：" prop="raiseTime">
                            <el-date-picker
                                    v-model.trim="temp.raiseTime"
                                    type="datetime"
                                    placeholder="选择日期时间"
                                    style="width: 194px">
                            </el-date-picker>
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
        name: 'Feeding',
        data() {
            return {
                defaultProps: {
                    children: 'children',
                    label: 'name'
                },
                rules: {
                    farmId: [{ required: true, message: '请选择鸡场', trigger: 'change' }],
                    houseId: [{ required: true, message: '请选择鸡棚', trigger: 'change' }],
                    raiseType: [{ required: true, message: '请选择类型', trigger: 'change' }],
                    raiseId: [{ required: true, message: '请选择饲料品牌', trigger: 'change' }],
                    chicksCcsId: [{ required: true, message: '请选择种鸡批次', trigger: 'change' }],
                    raiseValue: [{ required: true, message: '请输入饲料使用量', trigger: 'blur' }],
                    raiseUnit: [{ required: true, message: '请选择用量单位', trigger: 'blur' }],
                    raiseTime: [{ required: true, message: '请选择投喂时间', trigger: 'blur' }],
                },
                tableLoading: false,
                tableData: [],
                farmData: [],
                houseData: [],
                feedData: [],
                chicksCcsData: [],
                raiseTypeData: [],
                raiseUnitData: [],
                sels: [],//选中显示的值,
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
                    raiseId: '',
                    raiseName: '',
                    raiseValue: '',
                    raiseTime: '',
                    raiseType: '',
                    raiseUnit: '',
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
                    this.resetTemp(this.temp)
                    this.$refs.tree.setCheckedNodes([data])
                    let tree = this.$refs.tree.getCheckedNodes(true);
                    this.temp.chicksCcsId  = tree[0].id;
                    this.dialogTreeFormVisible = true;
                }
            },
            cancelAll(){
                //清空已选择节点
                this.$refs.tree.setCheckedNodes([])
                this.dialogTreeFormVisible = false
            },
            init(){
                this.$http({
                    method: "post",
                    url: "/web-server/process/feeding/query.json?action=init"
                }).then(res => {
                    this.farmData = res.data.data.listFarm;
                    this.feedData = res.data.data.listFeed;
                    this.raiseTypeData = res.data.data.listRaiseType;
                    this.raiseUnitData = res.data.data.listRaiseUnit;
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
                params.append("raiseId",this.tableQuery.raiseId);
                params.append("raiseValueStart",this.tableQuery.raiseValueStart);
                params.append("raiseValueEnd",this.tableQuery.raiseValueEnd);
                params.append("chicksCcsId",this.tableQuery.chicksCcsId);
                let raiseTimeStart = '';
                let raiseTimeEnd = '';
                if(this.tableQuery.raiseTimeStart != ''){
                     raiseTimeStart = utils.formatTime(new Date(this.tableQuery.raiseTimeStart),"yyyy-MM-dd hh:mm:ss")
                }
                if(this.tableQuery.raiseTimeEnd != ''){
                     raiseTimeEnd = utils.formatTime(new Date(this.tableQuery.raiseTimeEnd),"yyyy-MM-dd hh:mm:ss")
                }
                params.append("raiseTimeStart",raiseTimeStart);
                params.append("raiseTimeEnd",raiseTimeEnd);
                console.log(raiseTimeStart)
                console.log(raiseTimeEnd)
                this.$http({
                    method: "post",
                    url: "/web-server/process/feeding/query.json?action=query",
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
                    params.append("raiseId", this.temp.raiseId);
                    params.append("raiseName", this.temp.raiseName);
                    params.append("raiseValue", this.temp.raiseValue);
                    params.append("raiseTime", this.temp.raiseTime);
                    params.append("raiseType", this.temp.raiseType);
                    params.append("raiseUnit", this.temp.raiseUnit);
                    this.$http({
                        method: "post",
						url: "/web-server/process/feeding/query.json?action=save",						
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
                params.append("raiseRecordId", row.id);
                params.append("farmId", row.farmId);
                params.append("houseId", row.houseId);
                this.$http({
                    method: "post",
                    url: "/web-server/process/feeding/query.json?action=editHandle",
                    data: params
                }).then(res => {
                    this.houseData = res.data.data.listHouse;
                    this.chicksCcsData = res.data.data.listChicksCcs;
                    this.temp = Object.assign({}, res.data.data.raiseRecord)
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
                    params.append("raiseId", this.temp.raiseId);
                    params.append("raiseName", this.temp.raiseName);
                    params.append("raiseValue", this.temp.raiseValue);
                    let raiseTime = new Date(this.temp.raiseTime);
                    params.append("raiseTime", raiseTime);
                    let createTime = new Date(this.temp.createTime);
                    params.append("createTime", createTime);
                    params.append("raiseType", this.temp.raiseType);
                    params.append("raiseUnit", this.temp.raiseUnit);
                    this.$http({
                        method: "post",
                        url: "/web-server/process/feeding/query.json?action=save",
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
                        url: "/web-server/process/feeding/query.json?action=delete",
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
                        url: "/web-server/process/feeding/query.json?action=delete",
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
                    url: "/web-server/process/feeding/query.json?action=queryHouse",
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
                    url: "/web-server/process/feeding/query.json?action=queryChicksCcs",
                    data: params
                }).then(res => {
                    this.chicksCcsData = res.data.rows;
                })
                    .catch(err => {
                        console.log(err);
                    });
            },
            feedChange(feedId) {
                let obj = {};
                obj = this.feedData.find((item)=>{
                    return item.id === feedId;//筛选出匹配数据
                });
                this.temp.raiseName = obj.name;
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
<style>
    .tableHeader {
        height: 50px;
        background: #e6eaf3;
    }
</style>
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

