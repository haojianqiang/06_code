<template>
    <div class="container-fluid">
        <div class="main">
            <div class="">
                <div class="three-nav col-xs-12">
                    <div class="row">
                        <ol class="breadcrumb" style="width: 100%">
                            <li><a href="">用户管理</a></li>
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
                            <b><label class="font-color">用户名：</label></b>
                            <input path="name" v-model="tableQuery.name" class="form-control width40"/>
                            <button style="margin-left: 5px" type="button" class="btn btn-danger" @click="fetchData(1)">查询</button>
                        </div>
                    </div>
                </div>
            </form>

            <div class="add-table col-xs-12">
                <p><a href="javascript:void(0)" @click="handleCreate">
                    <i class="el-icon-circle-plus-outline"></i>
                    新增用户</a>
                </p>
                <table class="table table-bordered">
                    <thead>
                    <tr>
                        <td><input type="checkbox"/></td>
                        <td>名称</td>
                        <td>账号</td>
                        <td>配置</td>
                        <td>操作</td>
                    </tr>
                    </thead>
                    <tbody>
                        <tr class="" v-for="row in tableData">
                            <td><input type="checkbox" value=""></td>
                            <td><a href="show.html?id=${row.role_id}">{{row.user_name}}</a></td>
                            <td>{{row.account}}</td>
                            <td class="op" style="width: 25%">
                                <a @click="formRoleTree(row.id)" href="javascript:void(0)">配置角色</a>
                                <a @click="formMenuTree(row.id)" style="margin-left: 5px" href="javascript:void(0)">配置可操作菜单</a>
                                <a @click="formDeptTree(row.id)" style="margin-left: 5px" href="javascript:void(0)">配置查询权限</a>
                            </td>
                            <td class="op" style="width: 25%;">
                                <a @click="handleEdit(row.id)" href="javascript:void(0)">编辑</a>
                                <a style="margin-left: 5px" @click="handleEdit(row.id)" href="javascript:void(0)">修改密码</a>
                                <a @click="delData(row.id)" style="margin-left: 5px" href="javascript:void(0)">删除</a>
                            </td>
                        </tr>
                    </tbody>
                </table>

                <!--弹出窗口：新增-->
                <el-dialog title="配置管理" :visible.sync="dialogCreateFormVisible" width="350px" :modal-append-to-body='false'>
                    <el-form ref="dataForm" :model="temp" label-position="right" label-width="120px">

                        <el-form-item label="名称：" prop="userName">
                            <el-input v-model="temp.userName"></el-input>
                        </el-form-item>

                        <el-form-item label="账号：" prop="account">
                            <el-input v-model="temp.account"></el-input>
                        </el-form-item>

                         <el-form-item label="密码：" prop="pwd">
                             <el-input v-model="temp.pwd"></el-input>
                         </el-form-item>

                    </el-form>
                    <div slot="footer" class="dialog-footer">
                        <el-button @click="dialogCreateFormVisible = false">返回</el-button>
                        <el-button  type="primary" @click="createData">保存</el-button>
                    </div>
                </el-dialog>

                <!--弹出窗口：修改-->
                <el-dialog title="配置管理" :visible.sync="dialogEditFormVisible" width="350px" :modal-append-to-body='false'>
                    <el-form ref="dataForm" :model="temp" label-position="right" label-width="120px">

                        <el-form-item label="名称：" prop="userName">
                            <el-input v-model="temp.userName"></el-input>
                        </el-form-item>

                        <el-form-item label="账号：" prop="account">
                            <el-input v-model="temp.account"></el-input>
                        </el-form-item>

                        <el-form-item label="密码：" prop="pwd">
                            <el-input></el-input>
                        </el-form-item>

                    </el-form>
                    <div slot="footer" class="dialog-footer">
                        <el-button @click="dialogEditFormVisible = false">返回</el-button>
                        <el-button  type="primary" @click="editData">保存</el-button>
                    </div>
                </el-dialog>

                <!--弹出窗口：菜单列表-->
                <el-dialog title="菜单列表" :visible.sync="dialogMenuVisible" width="350px" :modal-append-to-body='false'>
                    <div>
                        <ul id="treeMenu" class="ztree"></ul>
                    </div>
                    <div slot="footer" class="dialog-footer">
                        <el-button @click="dialogMenuVisible = false">返回</el-button>
                        <el-button type="primary" @click="onMenuCheck">保存</el-button>
                    </div>
                </el-dialog>

                <!--弹出窗口：角色列表-->
                <el-dialog title="角色列表" :visible.sync="dialogRoleVisible" width="350px" :modal-append-to-body='false'>
                    <div>
                        <ul id="treeRole" class="ztree"></ul>
                    </div>
                    <div slot="footer" class="dialog-footer">
                        <el-button @click="dialogRoleVisible = false">返回</el-button>
                        <el-button type="primary" @click="onRoleCheck">保存</el-button>
                    </div>
                </el-dialog>

                <!--弹出窗口：部门列表-->
                <el-dialog title="部门列表" :visible.sync="dialogDeptVisible" width="350px" :modal-append-to-body='false'>
                    <div>
                        <ul id="treeDept" class="ztree"></ul>
                    </div>
                    <div slot="footer" class="dialog-footer">
                        <el-button @click="dialogDeptVisible = false">返回</el-button>
                        <el-button type="primary" @click="onDeptCheck">保存</el-button>
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
                dialogMenuVisible: false,
                dialogRoleVisible: false,
                dialogDeptVisible: false,
                tableData: [],
                userId: null,
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
                    id: null,
                    userName: null,
                    account: null,
                    pwd: null
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
                    url: "/web-server/system/user/user.json?action=query",
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
                params.append("id", this.temp.id);
                params.append("userName", this.temp.userName);
                params.append("account", this.temp.account);
                params.append("pwd", this.temp.pwd);
                this.$http({
                    method: "post",
                    url: "/web-server/system/user/user.json?action=save",
                    data: params
                }).then(res => {
                    this.dialogCreateFormVisible = false;
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
                        url: "/web-server/system/user/user.json?action=delete",
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
                    if(item.id==id){
                        this.tableData.splice(i, 1)
                        //在数组的some方法中，如果return true，就会立即终止这个数组的后续循环
                        --this.tablePage.total;
                        return true
                    }
                })
            },
            formMenuTree(userId){
                this.dialogMenuVisible = true;
                this.userId = userId;
                let params = new URLSearchParams();
                params.append("userId", userId);
                this.$http({
                    method: "post",
                    url: "/web-server/system/user/user.json?action=getMenuTree",
                    data: params
                }).then(res => {
                    let tree = res.data.data.tree;
                    this.drawMenuTree(tree);
                })
                    .catch(err => {
                        console.log(err);
                    });
            },
            formRoleTree(userId){
                this.dialogRoleVisible = true;
                this.userId = userId;
                let params = new URLSearchParams();
                params.append("userId", userId);
                this.$http({
                    method: "post",
                    url: "/web-server/system/user/user.json?action=getRoleTree",
                    data: params
                }).then(res => {
                    let tree = res.data.data.tree;
                    this.drawRoleTree(tree);
                })
                    .catch(err => {
                        console.log(err);
                    });
            },
            formDeptTree(userId){
                this.dialogDeptVisible = true;
                this.userId = userId;
                let params = new URLSearchParams();
                params.append("userId", userId);
                this.$http({
                    method: "post",
                    url: "/web-server/system/user/user.json?action=getDeptTree",
                    data: params
                }).then(res => {
                    let tree = res.data.data.tree;
                    this.drawDeptTree(tree);
                })
                    .catch(err => {
                        console.log(err);
                    });
            },
            // 绘制模块
            drawMenuTree(datas){
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
                zTree = $.fn.zTree.init($("#treeMenu"), setting, eval(datas));
            },
            drawDeptTree(datas){
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
                zTree = $.fn.zTree.init($("#treeDept"), setting, eval(datas));
            },
            drawRoleTree(datas){
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
                zTree = $.fn.zTree.init($("#treeRole"), setting, eval(datas));
            },
            onMenuCheck(){
                var treeObj = $.fn.zTree.getZTreeObj("treeMenu");
                var nodes = treeObj.getCheckedNodes(true);
                var ids = [];
                var v = [];
                var checkAllNode = [];
                for (var i = 0; i < nodes.length; i++) {
                    debugger
                    if(checkAllNode.indexOf(nodes[i].pId) != -1){
                        if(nodes[i].check_Child_State==2){
                            checkAllNode.push(nodes[i].id);
                        }
                        continue;
                    }
                    if(nodes[i].check_Child_State==2){
                        checkAllNode.push(nodes[i].id);
                        ids.push(nodes[i].id);
                        v.push(nodes[i].value);
                    }
                    if(nodes[i].check_Child_State==-1){
                        ids.push(nodes[i].id);
                        v.push(nodes[i].value);
                    }
                }
                let params = new URLSearchParams();
                params.append("uid", this.userId);
                params.append("ids", ids);
                this.$http({
                    method: "post",
                    url: "/web-server/system/user/user.json?action=saveMenuTree",
                    data: params
                }).then(res => {
                    this.dialogMenuVisible = false;
                })
                    .catch(err => {
                        console.log(err);
                    });
            },
            onRoleCheck(){
                var treeObj = $.fn.zTree.getZTreeObj("treeRole");
                var nodes = treeObj.getCheckedNodes(true);
                var ids = [];
                for (var i = 0; i < nodes.length; i++) {
                    ids.push(nodes[i].id);
                }
                let params = new URLSearchParams();
                params.append("uid", this.userId);
                params.append("ids", ids);
                this.$http({
                    method: "post",
                    url: "/web-server/system/user/user.json?action=saveRoleTree",
                    data: params
                }).then(res => {
                    this.dialogRoleVisible = false;
                })
                    .catch(err => {
                        console.log(err);
                    });
            },
            onDeptCheck(){
                var treeObj = $.fn.zTree.getZTreeObj("treeDept");
                var nodes = treeObj.getCheckedNodes(true);
                var ids = [];
                var v = [];
                var checkAllNode = [];
                for (var i = 0; i < nodes.length; i++) {
                    if(checkAllNode.indexOf(nodes[i].pId) != -1){
                        if(nodes[i].check_Child_State==2){
                            checkAllNode.push(nodes[i].id);
                        }
                        continue;
                    }
                    if(nodes[i].check_Child_State==2){
                        checkAllNode.push(nodes[i].id);
                        ids.push(nodes[i].id);
                        v.push(nodes[i].value);
                    }
                    if(nodes[i].check_Child_State==-1){
                        ids.push(nodes[i].id);
                        v.push(nodes[i].value);
                    }
                }
                let params = new URLSearchParams();
                params.append("uid", this.userId);
                params.append("ids", ids);
                this.$http({
                    method: "post",
                    url: "/web-server/system/user/user.json?action=saveDeptTree2",
                    data: params
                }).then(res => {
                    this.dialogDeptVisible = false;
                })
                    .catch(err => {
                        console.log(err);
                    });
            },
            handleEdit(userId){
                let params = new URLSearchParams();
                params.append("userId", userId);
                this.$http({
                    method: "post",
                    url: "/web-server/system/user/user.json?action=getUser",
                    data: params
                }).then(res => {
                    this.temp = Object.assign({}, res.data.data)
                    this.dialogEditFormVisible = true;
                })
                    .catch(err => {
                        console.log(err);
                    });
            },
            editData(){
                let params = new URLSearchParams();
                params.append("id", this.temp.id);
                params.append("userName",this.temp.userName);
                params.append("account",this.temp.account);
                this.$http({
                    method: "post",
                    url: "/web-server/system/user/user.json?action=save",
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

