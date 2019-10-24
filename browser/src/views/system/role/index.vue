<template>
    <div class="container-fluid">
        <div class="main">
            <div class="">
                <div class="three-nav col-xs-12">
                    <div class="row">
                        <ol class="breadcrumb" style="width: 100%">
                            <li><a href="">角色管理</a></li>
                            <li><a href=""></a></li>
                        </ol>
                    </div>
                </div>
            </div>
            <div class="clearfix"></div>
            <form>
                <div style="margin-bottom: 10px">
                    <div class="form-inline clearfix">
                        <div class="form-group col-xs-12">
                            <b><label class="font-color">角色名：</label></b>
                            <input path="name" v-model="tableQuery.name" class="form-control width40"/>
                            <button style="margin-left: 5px" type="" class="btn btn-danger" @click="fetchData(1)">查询</button>
                        </div>
                    </div>
                </div>
            </form>

            <div class="add-table col-xs-12">
                <p><a href="javascript:void(0)" @click="handleCreate">
                    <i class="el-icon-circle-plus-outline"></i>
                    新增角色配置</a>
                </p>
                <table class="table table-bordered">
                    <thead>
                    <tr>
                        <td><input type="checkbox"/></td>
                        <td>角色名称</td>
                        <td>角色代码</td>
                        <td>是否可用</td>
                        <td>操作</td>
                    </tr>
                    </thead>
                    <tbody>
                        <tr class="" v-for="row in tableData">
                            <td><input type="checkbox" value="${row.role_id}"></td>
                            <td><a href="show.html?id=${row.role_id}">{{row.role_name}}</a></td>
                            <td>{{row.role_code}}</td>
                            <td>
                                    <template v-if="row.is_enable == 0">
                                        是
                                    </template>
                                   <template v-if="row.is_enable == 1">
                                       否
                                   </template>
                            </td>
                            <td class="op" style="width: 25%;">
                                <a @click="formTree(row.role_id)" href="javascript:void(0)">配置模块权限</a>
                                <a style="margin-left: 5px" @click="handleEdit(row.role_id)" href="javascript:void(0)">编辑</a>
                                <a style="margin-left: 5px" href="javascript:void(0)" @click="delData(row.role_id)">删除</a>
                            </td>
                        </tr>
                    </tbody>
                </table>

                <!--弹出窗口：新增-->
                <el-dialog title="角色配置管理" :visible.sync="dialogCreateFormVisible" width="350px" :modal-append-to-body='false'>
                    <el-form ref="dataForm" :model="temp" label-position="right" label-width="120px">

                        <el-form-item label="角色名称：" prop="roleName">
                            <el-input v-model="temp.roleName"></el-input>
                        </el-form-item>

                        <el-form-item label="角色编码：" prop="roleCode">
                            <el-input v-model="temp.roleCode"></el-input>
                        </el-form-item>

                         <el-form-item label="是否可用：" prop="isEnable">
                             <el-checkbox label="可用" v-model="temp.isEnable"></el-checkbox>
                         </el-form-item>

                    </el-form>
                    <div slot="footer" class="dialog-footer">
                        <el-button @click="dialogCreateFormVisible = false">返回</el-button>
                        <el-button  type="primary" @click="createData">保存</el-button>
                    </div>
                </el-dialog>

                <!--弹出窗口：修改-->
                <el-dialog title="角色配置管理" :visible.sync="dialogEditFormVisible" width="350px" :modal-append-to-body='false'>
                    <el-form ref="dataForm" :model="temp" label-position="right" label-width="120px">

                        <el-form-item label="角色名称：" prop="roleName">
                            <el-input v-model="temp.roleName"></el-input>
                        </el-form-item>

                        <el-form-item label="角色编码：" prop="roleCode">
                            <el-input v-model="temp.roleCode"></el-input>
                        </el-form-item>

                        <el-form-item label="是否可用：" prop="isEnable">
                            <el-checkbox label="可用" v-model="temp.isEnable"></el-checkbox>
                        </el-form-item>

                    </el-form>
                    <div slot="footer" class="dialog-footer">
                        <el-button @click="dialogEditFormVisible = false">返回</el-button>
                        <el-button  type="primary" @click="editData">保存</el-button>
                    </div>
                </el-dialog>

                <!--弹出窗口：模块列表-->
                <el-dialog title="模块列表" :visible.sync="dialogModuleVisible" width="350px" :modal-append-to-body='false'>
                    <div>
                        <ul id="treeSelect" class="ztree"></ul>
                    </div>
                    <div slot="footer" class="dialog-footer">
                        <el-button @click="dialogModuleVisible = false">返回</el-button>
                        <el-button type="primary" @click="onCheck">保存</el-button>
                    </div>
                </el-dialog>

                <el-pagination
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange"
                        :current-page="tablePage.current"
                        :page-sizes="[10, 20, 30, 40, 50]"
                        :page-size="tablePage.size"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="tablePage.total">
                </el-pagination>

            </div>
        </div>

    </div>
</template>
<script>
    export default {
        name: 'Business',
        data() {
            return {
                tableLoading: false,
                dialogFormVisible: false,
                dialogCreateFormVisible: false,
                dialogEditFormVisible: false,
                dialogModuleVisible: false,
                tableData: [],
                roleId: null,
                tableQuery: {
                    name: ''
                },
                tablePage: {
                    current: 1,
                    pages: [10, 20, 30, 40, 50],
                    size: 10,
                    total: null
                },
                temp: {
                    roleId: '',
                    roleName: null,
                    roleCode: null,
                    isEnable: ''
                },
                isIndeterminate: true
            }

        },

        created() {
            this.initData()
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
            initData(){
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
                params.append("page", this.tablePage.current);
                params.append("limit",this.tablePage.size);
                params.append("name",this.tableQuery.name);

                this.$http({
                    method: "post",
                    url: "/web-server/system/role/role.json?action=query",
                    data: params
                }).then(res => {
                    this.tableData = res.data.rows;
                    this.tablePage.total = res.data.rowCount;
                    this.tablePage.current = res.data.pageNo;
                    this.tablePage.size = res.data.pageSize;
                    })
                    .catch(err => {
                        console.log(err);
                    });
            },
            //新增
            handleCreate() {
                this.dialogCreateFormVisible = true
                this.resetTemp(this.temp)
            },
            createData() {
                let params = new URLSearchParams();
                params.append("roleId", this.temp.roleId);
                params.append("roleName", this.temp.roleName);
                params.append("roleCode", this.temp.roleCode);
                if(this.temp.isEnable){
                    this.temp.isEnable = 0;
                }else {
                    this.temp.isEnable = 1;
                }
                params.append("isEnable", this.temp.isEnable);
                this.$http({
                    method: "post",
                    url: "/web-server/system/role/role.json?action=save",
                    data: params
                }).then(res => {
                    this.dialogCreateFormVisible = false;
                    //重新查询数据
                    this.fetchData();
                })
                    .catch(err => {
                        console.log(err);
                    });
            },
            delData(id) {
                var ids = [];
                ids.push(id)
                let params = new URLSearchParams();
                params.append("ids", ids);
                this.$confirm('确定要删除选中的行吗?', '提示').then(() => {
                    this.$http({
                        method: "post",
                        url: "/web-server/system/role/role.json?action=delete",
                        data: params
                    }).then(res => {
                        for(var i = 0;i<ids.length;i++){
                            //循环删除
                            this.del(ids[i]);
                        }
                    })
                        .catch(err => {
                            console.log(err);
                        });
                }).catch(() => {
                });
            },
            del(id){
                this.tableData.some((item, i)=>{
                    if(item.role_id == id){
                        this.tableData.splice(i, 1)
                        //在数组的some方法中，如果return true，就会立即终止这个数组的后续循环
                        --this.tablePage.total;
                        return true
                    }
                })
            },
            formTree(roleId){
                this.dialogModuleVisible = true;
                this.roleId = roleId;
                let params = new URLSearchParams();
                params.append("roleId", roleId);
                this.$http({
                    method: "post",
                    url: "/web-server/system/role/role.json?action=getModule",
                    data: params
                }).then(res => {
                    var tree = res.data.data.tree;
                    this.drawTree(tree);
                })
                    .catch(err => {
                        console.log(err);
                    });
            },
            // 绘制模块
            drawTree(datas){
                console.log(datas);
                var setting = {
                        check: {
                            enable: true
                        },
                        data: {
                            simpleData: {
                                enable: true
                            }
                        },
                        callback: {
                            onClick: function (e, treeId, treeNode, clickFlag) {
                                zTree.checkNode(treeNode, !treeNode.checked, true);
                            }
                        }
                    },
                    zTree;
                    zTree = $.fn.zTree.init($("#treeSelect"), setting, eval(datas));
            },
            onCheck(){
                let treeObj = $.fn.zTree.getZTreeObj("treeSelect");
                let nodes = treeObj.getCheckedNodes(true);
                let ids = [];
                let v = [];
                for (let i = 0; i < nodes.length; i++) {
                    ids.push(nodes[i].id);
                    v.push(nodes[i].value);
                }
                let params = new URLSearchParams();
                params.append("rid", this.roleId);
                params.append("ids", ids);
                this.$http({
                    method: "post",
                    url: "/web-server/system/role/role.json?action=saveTree",
                    data: params
                }).then(res => {
                    this.dialogModuleVisible = false;
                })
                    .catch(err => {
                        console.log(err);
                    });
            },
            handleEdit(roleId){
                let params = new URLSearchParams();
                params.append("roleId", roleId);
                this.$http({
                    method: "post",
                    url: "/web-server/system/role/role.json?action=getRole",
                    data: params
                }).then(res => {
                    this.temp = Object.assign({}, res.data.data)
                    if(res.data.data.isEnable == 0){
                        this.temp.isEnable = true;
                    }else{
                        this.temp.isEnable = false;
                    }
                    this.dialogEditFormVisible = true;
                })
                    .catch(err => {
                        console.log(err);
                    });
            },
            editData(){
                let params = new URLSearchParams();
                params.append("roleId", this.temp.roleId);
                params.append("roleName",this.temp.roleName);
                params.append("roleCode",this.temp.roleCode);
                if(this.temp.isEnable){
                    this.temp.isEnable = 0;
                }else {
                    this.temp.isEnable = 1;
                }
                params.append("isEnable",this.temp.isEnable);
                this.$http({
                    method: "post",
                    url: "/web-server/system/role/role.json?action=save",
                    data: params
                }).then(res => {
                    this.dialogEditFormVisible = false;
                })
                    .catch(err => {
                        console.log(err);
                    });
            },
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

    .breadcrumb {
        border-radius: 0;
    }
</style>

