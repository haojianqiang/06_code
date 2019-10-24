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
                        <el-form-item label="种苗批号：" prop="batchNumber">
                            <el-input v-model.trim="tableQuery.batchNumber" placeholder="请输入种苗批号"></el-input>
                        </el-form-item>
                        <el-form-item label="种苗品种：" prop="chickType">
                            <el-input v-model.trim="tableQuery.chickType" placeholder="请输入种苗品种"></el-input>
                        </el-form-item>
                        <el-form-item>
                            <el-button class="button" @click="fetchData(1)" type="success">查询</el-button>
                            <el-button class="button" @click="reset('tableQuery')" type="success">重置
                            </el-button>
                        </el-form-item>
                    </el-form>
                </div>

                <div class="demoTable">
                    <el-button class="button" type="success" @click="removeBatch(sels)">批量删除</el-button>
                    <el-button class="button" type="success" @click="handleCreate"><i
                            class="el-icon-plus"></i> 新增
                    </el-button>
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
                        <el-table-column prop="farmName" label="鸡场名称"></el-table-column>
                        <el-table-column prop="batch_number" label="种苗批号"></el-table-column>
                        <el-table-column prop="chick_type" label="种苗品种"></el-table-column>
                        <el-table-column prop="producer" label="种苗生产企业"></el-table-column>
                        <el-table-column prop="chick_count" label="进苗数量"></el-table-column>
                        <el-table-column prop="unit" label="单位"></el-table-column>
                        <el-table-column prop="price" label="单价(元)"></el-table-column>
                        <el-table-column prop="produce_time" :formatter="dateFormat" label="投苗时间" width="150"></el-table-column>
                        <el-table-column prop="create_time" :formatter="dateFormat" label="初始日期" width="150"></el-table-column>
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
            <el-form ref="dataForm" :rules="rules" :inline="true" :model="temp" label-position="right" label-width="100px" style="margin-top: 20px;">

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="鸡场名称：" prop="farmId">
                            <el-select v-model.trim="temp.farmId" placeholder="请选择鸡场名称" style="width: 200px;">
                                <el-option
                                        v-for="item in options"
                                        :key="item.id"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="鸡苗批号：" prop="batchNumber" >
                            <el-input v-model="temp.batchNumber" placeholder="请输入鸡苗批号" style="width: 200px;"/>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="生产企业：" prop="producer">
                            <el-input v-model.trim="temp.producer" placeholder="请输入种苗生产企业" style="width: 200px;" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="鸡苗品种：" prop="chickType">
                            <el-input v-model.trim="temp.chickType" placeholder="请输入鸡苗品种" style="width: 200px;" />
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="进苗数量：" prop="chickCount">
                            <el-input v-model.number="temp.chickCount" placeholder="请输入进苗数量" style="width: 200px;" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="单位：" prop="unit">
                            <el-input v-model.trim="temp.unit" placeholder="请输入单位" style="width: 200px;" />
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="初始时间：" prop="createTime">
                            <el-date-picker v-model="temp.createTime" type="datetime" placeholder="请选择初始日期" style="width: 200px;" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="投苗时间：" prop="produceTime">
                            <el-date-picker v-model="temp.produceTime" type="datetime" placeholder="请输入投苗时间" style="width: 200px;"ss />
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="单价：" prop="price">
                            <el-input oninput="value=value.replace(/[^\d.]/g,'')" v-model="temp.price" placeholder="请输入单价(元)" style="width: 200px;" />
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
        <el-dialog :visible.sync="dialogEditFormVisible" :modal-append-to-body='false' >
            <el-form ref="dataForm" :rules="rules" :inline="true" :model="temp" label-position="right" label-width="100px" style = "margin-top :20px">

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="鸡场名称：" prop="farmId">
                            <el-select v-model.trim="temp.farmId" placeholder="请选择鸡场名称" style="width: 200px;">
                                <el-option
                                        v-for="item in options"
                                        :key="item.id"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="鸡苗批号：" prop="batchNumber">
                            <el-input v-model="temp.batchNumber" placeholder="请输入鸡苗批号" style="width: 200px;" ></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="生产企业：" prop="producer">
                            <el-input v-model.trim="temp.producer" placeholder="请输入种苗生产企业" style="width: 200px"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="鸡苗品种：" prop="chickType">
                            <el-input v-model.trim="temp.chickType" placeholder="请输入鸡苗品种" style="width: 200px;" ></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="进苗数量：" prop="chickCount">
                            <el-input v-model.number="temp.chickCount" placeholder="请输入进苗数量" style="width: 200px;" ></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="单位：" prop="unit">
                            <el-input v-model.trim="temp.unit" placeholder="请输入单位(元)" style="width: 200px;" ></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="初始时间：" prop="createTime">
                            <el-date-picker v-model="temp.createTime" type="datetime" placeholder="请选择初始日期" style="width: 200px;" ></el-date-picker>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="投苗时间：" prop="produceTime">
                            <el-date-picker v-model="temp.produceTime" type="datetime" placeholder="请输入投苗时间" style="width: 200px;" ></el-date-picker>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="单价：" prop="price">
                            <el-input oninput="value=value.replace(/[^\d.]/g,'')" v-model="temp.price" placeholder="请输入单价(元)" style="width: 200px;" ></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

            </el-form>
            <div slot="footer" class="dialog-footer" style="text-align: center">
                <el-button class="button" type="primary" @click="editData">保存</el-button>
                <el-button @click="dialogEditFormVisible = false">取消</el-button>
            </div>
        </el-dialog>

        <!--弹出窗口：点击树节点新增-->
        <el-dialog :visible.sync="dialogTreeFormVisible" :modal-append-to-body='false'  :before-close="handleClose">
            <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="120px" style = "margin-top:20px">

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="鸡苗批号：" prop="batchNumber">
                            <el-input v-model="temp.batchNumber" placeholder="请输入鸡苗批号" style="width: 200px;" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="生产企业：" prop="producer">
                            <el-input v-model.trim="temp.producer" placeholder="请输入种苗生产企业" style="width: 200px"/>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="鸡苗品种：" prop="chickType">
                            <el-input v-model.trim="temp.chickType" placeholder="请输入鸡苗品种" style="width: 200px;" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="进苗数量：" prop="chickCount">
                            <el-input v-model.number="temp.chickCount" placeholder="请输入进苗数量" style="width: 200px;"/>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="单价：" prop="price">
                            <el-input oninput="value=value.replace(/[^\d.]/g,'')" v-model="temp.price" placeholder="请输入单价(元)" style="width: 200px;" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="单位：" prop="unit">
                            <el-input v-model.trim="temp.unit" placeholder="请输入单位(元)" style="width: 200px;" />
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="初始时间：" prop="createTime">
                            <el-date-picker v-model.trim="temp.createTime" type="datetime" placeholder="请选择初始日期" style="width: 200px;" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="投苗时间：" prop="produceTime">
                            <el-date-picker v-model.trim="temp.produceTime" type="datetime" placeholder="请输入投苗时间" style="width: 200px;" />
                        </el-form-item>
                    </el-col>
                </el-row>

            </el-form>
            <div slot="footer" class="dialog-footer footer" >
                <el-button class="button"  type="primary" @click="createData">保存</el-button>
                <el-button @click="cancelAll">取消</el-button>
            </div>
        </el-dialog>

    </div>
</template>
<script>
    import Utils from "../../../utils";
    export default {
        name: 'Chicks',
        data() {
            return {
                defaultProps: {
                    children: 'children',
                    label: 'name'
                },
                rules: {
                    batchNumber: [{
                        required: true,
                        pattern: /^[0-9a-zA-Z]+$/,//正则表达式
                        message: '批号只能是字母和数字',
                        trigger: 'blur'
                    }],
                    farmId: [{required: true, message: '必填', trigger: 'blur'}],
                    chickType: [{required: true, message: '必填', trigger: 'blur'}],
                    chickCount: [{required: true, message: '必填', trigger: 'blur'},{ type: 'number', message: '进苗数量必须为数字值'}],
                    producer: [{required: true, message: '必填', trigger: 'blur'}],
                    price: [{required: true, message: '必填', trigger: 'blur'}],
                    createTime: [{required: true, message: '必填', trigger: 'blur'}],
                    produceTime: [{required: true, message: '必填', trigger: 'blur'}]
                },
                options: '',
                searchValue: '',
                tableLoading: false,
                tableData: [],
                sels: [],//选中显示的值
                farmTree: [],
                farmData: [],
                tableQuery: {
                    batchNumber: '',
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
                    farmId:'',
                    batchNumber: '',
                    chickType: '',
                    chickCount: '',
                    producer: '',
                    specs:'',
                    unit:'',
                    price:'',
                    unallocatedCapacity:'',
                    produceTime: '',
                    createTime: ''
                },
                isIndeterminate: true,
                dialogCreateFormVisible: false,
                dialogEditFormVisible: false,
                dialogTreeFormVisible: false
            }

        },
        created() {
            this.fetchData();
            this.init();
        },

        watch: {},

        methods: {
            handleCheckChange(data, checked, indeterminate) {
                if(checked){
                    //清空参数
                    this.resetTemp(this.temp)
                    //设置单选限制
                    this.$refs.tree.setCheckedNodes([data])
                    //选中节点
                    let tree = this.$refs.tree.getCheckedNodes(true);
                    this.temp.farmId  = tree[0].id;
                    this.dialogTreeFormVisible = true;
                }
            },
            cancelAll(){
                //清空已选择节点
                this.$refs.tree.setCheckedNodes([])
                this.dialogTreeFormVisible = false
            },
            fetchSelectData() {
                this.$http({
                    method: "get",
                    url: "/web-server/config/chicks/query.json?action=getFarmNameAndId",
                }).then(res => {
                    this.options = res.data.data;
                }).catch(err => {
                    console.log(err);
                });
            },
            init(){
                this.$http({
                    method: "post",
                    url: "/web-server/config/chicks/query.json?action=init"
                }).then(res => {
                    this.farmData = res.data.data.listFarm;
                    this.farmTree = res.data.data.listFarmTree;
                })
                    .catch(err => {
                        console.log(err);
                    });
            },
            //清空对象所有属性
            resetTemp(temp) {
                for (let prop in temp) {
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
                if (current) {
                    this.tablePage.current = current
                }
                this.tableLoading = true;
                let params = new URLSearchParams();
                params.append("pageNo", this.tablePage.current);
                params.append("pageSize", this.tablePage.size);
                params.append("batchNumber", this.tableQuery.batchNumber.trim());
                params.append("chickType", this.tableQuery.chickType.trim());


                this.$http({
                    method: "post",
                    url: "/web-server/config/chicks/query.json?action=query",
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
            //点击关闭图标取消checkbox勾选
            handleClose(done){
				//清空已选节点
                this.$refs.tree.setCheckedNodes([])
                done()
            },
            //新增
            handleCreate() {
                this.searchValue = '';
                this.resetTemp(this.temp);
                this.dialogCreateFormVisible = true;
                this.fetchSelectData();
            },
            createData() {
                this.$refs['dataForm'].validate((valid) => {
                    if (!valid) return;

                    let params = new URLSearchParams();
                    params.append("farmId", this.temp.farmId);
                    params.append("batchNumber", this.temp.batchNumber);
                    params.append("chickType", this.temp.chickType);
                    params.append("producer", this.temp.producer);
                    params.append("produceTime",this.temp.produceTime);
                    params.append("createTime", this.temp.createTime);
                    params.append("chickCount",this.temp.chickCount);
                    params.append("unallocatedCapacity", this.temp.chickCount);
                    params.append("price", this.temp.price);
                    params.append("unit", this.temp.unit);

                    this.$http({
                        method: "post",
                        url: "/web-server/config/chicks/query.json?action=save",
                        data: params
                    }).then(res => {
                        this.dialogCreateFormVisible = false;
                        this.dialogTreeFormVisible = false
                        this.$message.success("添加成功");
                        this.fetchData()
                    }).catch(err => {
                        console.log(err);
                    });
                })
            },
            handleEdit(id) {

                this.searchValue = '';
                this.temp.id = id;
                this.fetchSelectData();
                let params = new URLSearchParams();
                params.append("chicksId", this.temp.id);

                this.$http({
                    method: "post",
                    url: "/web-server/config/chicks/query.json?action=getChicks",
                    data: params
                }).then(res => {
                    this.temp = Object.assign({}, res.data.data);
                    this.dialogEditFormVisible = true;
                }).catch(err => {
                    console.log(err);
                });
            },
            isNumber(val){
                var regPos = /^\d+(\.\d+)?$/; //非负浮点数
                var regNeg = /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/; //负浮点数
                return regPos.test(val) || regNeg.test(val);
            },
            editData() {
                this.$refs['dataForm'].validate((valid) => {
                    if (!valid) return;

                    let params = new URLSearchParams();
                    params.append("id", this.temp.id);
                    params.append("farmId", this.temp.farmId);
                    params.append("batchNumber", this.temp.batchNumber);
                    params.append("chickType", this.temp.chickType);
                    params.append("producer", this.temp.producer);
                    let produceTime = new Date(this.temp.produceTime);
                    params.append("produceTime", produceTime);
                    let createTime = new Date(this.temp.createTime);
                    params.append("createTime", createTime);
                    params.append("chickCount", this.temp.chickCount);
                    params.append("unallocatedCapacity", this.temp.chickCount);
                    params.append("price", this.temp.price);
                    params.append("unit", this.temp.unit);

                    this.$http({
                        method: "post",
                        url: "/web-server/config/chicks/query.json?action=save",
                        data: params
                    }).then(res => {
                        this.dialogEditFormVisible = false;
                        this.$message.success("修改成功");
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
            removeBatch(rows) {
                var ids = [];
                rows.forEach(element => {
                    ids.push(element.id)
                });
                let params = new URLSearchParams();
                params.append("ids", ids);
                this.$confirm('确定要删除选中的行吗?', '提示').then(() => {
                    this.$http({
                        method: "post",
                        url: "/web-server/config/chicks/query.json?action=delete",
                        data: params
                    }).then(res => {
						this.fetchData();
						this.$message.success("删除成功");
                    }).catch(err => {
                        console.log(err);
                    });
                }).catch(() => {
                });
            },
            del(id) {
                this.tableData.some((item, i) => {
                    if (item.id === id) {
                        this.tableData.splice(i, 1);
                        //在数组的some方法中，如果return true，就会立即终止这个数组的后续循环
                        --this.tablePage.total;
                        return true
                    }
                })
            },
            reset(formName) {
                this.$refs[formName].resetFields();
            },
            singleDel(id) {
                let params = new URLSearchParams();
                params.append("ids", id);
                this.$confirm('确定要删除选中的行吗?', '提示').then(() => {
                    this.$http({
                        method: "post",
                        url: "/web-server/config/chicks/query.json?action=delete",
                        data: params
                    }).then(res => {
						this.fetchData();
						this.$message.success("删除成功");
                    }).catch(err => {
                        console.log(err);
                    });
                }).catch(() => {
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
            isInteger(obj) {
                return typeof obj === 'number' && obj % 1 === 0;
            },

        }
    }
</script>
<style scoped>
    .demoTable {
        text-align: left;
        padding-left: 50px;
        margin-top: 10px;
    }

    .table {
        text-align: left;
        padding-left: 40px;
        margin-top: 10px;
    }

    .layui-inline {
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

