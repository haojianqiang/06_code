<template>
    <div class="">
        <div class="demoTable">
            <el-form :inline="true" :model="tableQuery" class="demo-form-inline" ref="tableQuery">
                <el-form-item label="鸡场名称：" prop="farmName">
                    <el-input v-model.trim="tableQuery.farmName" placeholder="请输入鸡场名称" class="inputStyle"></el-input>
                </el-form-item>
                <el-form-item label="鸡棚名称：" prop="houseName">
                    <el-input v-model.trim="tableQuery.houseName" placeholder="请输入鸡棚名称" class="inputStyle"></el-input>
                </el-form-item>
                <el-form-item label="鸡棚容量：" prop="capacity">
                    <el-input v-model.trim="tableQuery.capacity" placeholder="请输入容量(只)" class="inputStyle"></el-input>
                </el-form-item>
                <el-form-item label="鸡场地址：" prop="address">
                    <el-input v-model.trim="tableQuery.address" placeholder="请输入鸡场地址" class="inputStyle"></el-input>
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
                        <a href="javascript:void(0)" @click="singleDel(scope.row.id)"
                           class="linkPadding">删除</a>
                    </template>
                </el-table-column>
                <el-table-column prop="farm_name" label="鸡场名称"></el-table-column>
                <el-table-column prop="house_name" label="鸡棚名称"></el-table-column>
                <el-table-column prop="length" label="长度(米)"></el-table-column>
                <el-table-column prop="width" label="宽度(米)"></el-table-column>
                <el-table-column prop="height" label="高度(米)"></el-table-column>
                <el-table-column prop="capacity" label="容量(只)"></el-table-column>
                <el-table-column prop="charger" label="负责人"></el-table-column>
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
            <el-form ref="dataForm" :rules="rules" :inline="true" :model="temp" label-position="right" label-width="100px" style=" margin-top:20px">
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="鸡场名称：" prop="farmId">
                            <el-select :default-active="$route.path" @change="chickvalue"
                                       v-model.trim="searchValue" filterable placeholder="请选择鸡场名称"
                                       style="width: 212px;">
                                <el-option
                                        v-for="item in options"
                                        :key="item.id"
                                        :label="item.name"
                                        v-model="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="鸡棚名称：" prop="name">
                            <el-input v-model.trim="temp.name" placeholder="请输入鸡棚名称"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="长(米)：" prop="length">
                            <el-input v-model.number="temp.length" placeholder="请输入长度"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="宽(米)：" prop="width">
                            <el-input v-model.number="temp.width" placeholder="请输入宽度"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="容量(只)：" prop="capacity">
                           <el-input v-model.number="temp.capacity" placeholder="请输入容量(只)"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="高(米)：" prop="height">
                            <el-input v-model.number="temp.height" placeholder="请输入高度"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="负责人：" prop="charger">
                          <el-input v-model.trim="temp.charger" placeholder="请输入负责人"></el-input>
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
            <el-form ref="dataForm" :rules="rules" :inline="true" :model="temp" label-position="right" label-width="100px" style=" margin-top:20px" >

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="鸡场名称：" prop="farmId">
                            <el-select :default-active="$route.path" @change="chickvalue"
                                       v-model.trim="searchValue" filterable placeholder="请选择鸡场名称"
                                       style="width: 212px;">
                                <el-option
                                        v-for="item in options"
                                        :key="item.id"
                                        :label="item.name"
                                        v-model="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="鸡棚名称：" prop="name">
                            <el-input v-model.trim="temp.name" placeholder="请输入鸡棚名称"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="长(米)：" prop="length">
                            <el-input v-model.number="temp.length" placeholder="请输入长度"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="宽(米)：" prop="width">
                            <el-input v-model.number="temp.width" placeholder="请输入宽度"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="容量(只)：" prop="capacity">
                            <el-input v-model.number="temp.capacity" placeholder="请输入容量(只)"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="高(米)：" prop="height">
                            <el-input v-model.number="temp.height" placeholder="请输入高度"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="负责人：" prop="charger">
                            <el-input v-model.trim="temp.charger" placeholder="请输入负责人"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

            </el-form>
            <div slot="footer" class="dialog-footer footer">
                <el-button class="button" type="primary" @click="editData">保存</el-button>
                <el-button @click="dialogEditFormVisible = false">取消</el-button>
            </div>
        </el-dialog>

    </div>
</template>
<script>
    export default {
        name: 'House',
        data() {
            return {
                rules: {
                    farmId: [{ required: true, message: '必填', trigger: 'blur' }],
                    name: [{ required: true, message: '必填', trigger: 'blur' }],
                    charger: [{ required: true, message: '必填', trigger: 'blur' }],
                    length: [{ required: true, message: '必填', trigger: 'blur' },{ type: 'number', message: '必须为数字', trigger: 'blur' }],
                    width: [{ required: true, message: '必填', trigger: 'blur' },{ type: 'number', message: '必须为数字', trigger: 'blur' }],
                    height: [{ required: true, message: '必填', trigger: 'blur' },{ type: 'number', message: '必须为数字', trigger: 'blur' }],
                    capacity: [{ required: true, message: '必填', trigger: 'blur' },{ type: 'number', message: '必须为数字', trigger: 'blur' }],
                },
                options: '',
                searchValue: '',
                tableLoading: false,
                tableData: [],
                sels: [],//选中显示的值
                tableQuery: {
                    farmName: '',
                    houseName: '',
                    capacity: '',
                    address: ''
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
                    name: '',
                    width: '',
                    length: '',
                    height: '',
                    capacity: '',
                    allocatedCapacity: '',
                    charger: '',
                    createTime: '',
                },
                isIndeterminate: true,
                dialogCreateFormVisible: false,
                dialogEditFormVisible: false
            }

        },
        created() {
            this.fetchData();
        },

        watch: {},

        methods: {
            //获取鸡场名称及id
            fetchSelectData() {
                this.$http({
                    method: "get",
                    url: "/web-server/config/house/query.json?action=getFarmNameAndId",
                }).then(res => {
                    console.dir(res);
                    this.options = res.data.data;
                }).catch(err => {
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
                params.append("farmName", this.tableQuery.farmName.trim());
                params.append("houseName", this.tableQuery.houseName.trim());
                params.append("capacity", this.tableQuery.capacity.trim());
                params.append("address", this.tableQuery.address.trim());

                this.$http({
                    method: "post",
                    url: "/web-server/config/house/query.json?action=query",
                    data: params
                }).then(res => {
                    console.log(res);
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
                    params.append("name", this.temp.name);
                    params.append("width",this.temp.width);
                    params.append("length", this.temp.length);
                    params.append("height", this.temp.height);
                    params.append("capacity",this.temp.capacity);
                    params.append("charger", this.temp.charger);

                    this.$http({
                        method: "post",
                        url: "/web-server/config/house/query.json?action=save",
                        data: params
                    }).then(res => {
                        this.dialogCreateFormVisible = false;
                        this.$message.success("添加成功");
                        this.fetchData()
                    }).catch(err => {
                        console.log(err);
                    });
                })
            },
            handleEdit(houseId) {

                this.searchValue = '';
                this.temp.id = houseId;
                this.fetchSelectData();
                let params = new URLSearchParams();
                params.append("houseId", this.temp.id);
                this.$http({
                    method: "post",
                    url: "/web-server/config/house/query.json?action=getHouse",
                    data: params
                }).then(res => {
                    this.temp = Object.assign({}, res.data.data);
                    this.searchValue = res.data.data.farmId;
                    this.dialogEditFormVisible = true;
                }).catch(err => {
                    console.log(err);
                });
            },
            editData() {

                this.$refs['dataForm'].validate((valid) => {
                    if (!valid) return;

                    let params = new URLSearchParams();
                    params.append("id", this.temp.id);
                    params.append("farmId", this.temp.farmId);
                    params.append("name", this.temp.name);
                    params.append("width",this.temp.width);
                    params.append("length",this.temp.length);
                    params.append("height",this.temp.height);
                    params.append("capacity",this.temp.capacity);
                    params.append("charger", this.temp.charger);
                    let createTime = new Date(this.temp.createTime);
                    params.append("createTime", createTime);
                    this.$http({
                        method: "post",
                        url: "/web-server/config/house/query.json?action=save",
                        data: params
                    }).then(res => {
                        this.dialogEditFormVisible = false;
						this.fetchData();
						this.$message.success("修改成功")
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
                        url: "/web-server/config/house/query.json?action=delete",
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
            chickvalue() {
                this.temp.farmId = this.searchValue;
                console.log(this.searchValue);
            },
            singleDel(id) {
                let params = new URLSearchParams();
                params.append("ids", id);
                this.$confirm('确定要删除选中的行吗?', '提示').then(() => {
                    this.$http({
                        method: "post",
                        url: "/web-server/config/house/query.json?action=delete",
                        data: params
                    }).then(res => {
                        if(res.data.ok){
                            this.$message({
                                message: '删除成功',
                                type: 'success'
                            });
                        }else{
                            this.$message.error('删除失败,此鸡棚已分配鸡苗');
                        }
						this.fetchData();

                    }).catch(err => {
                        console.log(err);
                    });
                }).catch(() => {
                });
            },
            isInteger(obj) {
                return typeof obj === 'number' && obj % 1 === 0;
            }
        }
    }
</script>
<style scoped>
    .inputStyle{
        width: 127px;
        font-size: 13px;
    }
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

