<template>
    <div class="">
        <div class="demoTable">
            <el-form :inline="true" :model="tableQuery" class="demo-form-inline" ref="tableQuery">
                <el-form-item label="鸡场名称：" prop="name">
                    <el-input v-model.trim="tableQuery.name" placeholder="请输入名称" class="inputStyle"></el-input>
                </el-form-item>
                <el-form-item label="鸡场负责人：" prop="charger">
                    <el-input v-model.trim="tableQuery.charger" placeholder="请选择负责人" class="inputStyle"></el-input>
                </el-form-item>
                <el-form-item label="联系电话：" prop="phoneNumber">
                    <el-input v-model.trim="tableQuery.phoneNumber" placeholder="请输入联系电话" class="inputStyle"></el-input>
                </el-form-item>
                <el-form-item label="鸡场地址：" prop="address">
                    <el-input v-model.trim="tableQuery.address" placeholder="请输入地址" class="inputStyle"></el-input>
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
                        <a href="javascript:void(0)" @click="details(scope.row.id)"
                           class="linkPadding">详情</a>
                    </template>
                </el-table-column>
                <el-table-column prop="company_name" label="公司名称"></el-table-column>
                <el-table-column prop="legal_person" label="公司法人"></el-table-column>
                <el-table-column prop="name" label="鸡场名称"></el-table-column>
                <el-table-column prop="size" label="鸡场规模"></el-table-column>
                <el-table-column prop="charger" label="负责人"></el-table-column>
                <el-table-column prop="phone_number" label="联系电话"></el-table-column>
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
        <el-dialog title="新增" :visible.sync="dialogCreateFormVisible" :modal-append-to-body='false'>
            <el-form :rules="rules" ref="dataForm" :model="temp" label-position="right" label-width="100px">

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="县：" prop="county">
                            <el-select  v-model.trim="temp.county" placeholder="请选择县" style="width: 220px">
                                <el-option label="息烽县" value="息烽县"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="镇：" prop="town">
                            <el-select v-model.trim="temp.town" placeholder="请选择镇" style="width: 220px">
                                <el-option label="永靖镇" value="永靖镇"></el-option>
                                <el-option label="温泉镇" value="温泉镇"></el-option>
                                <el-option label="九庄镇" value="九庄镇"></el-option>
                                <el-option label="小寨坝镇" value="小寨坝镇"></el-option>
                                <el-option label="青山苗族乡" value="青山苗族乡"></el-option>
                                <el-option label="西山乡" value="西山乡"></el-option>
                                <el-option label="养龙司乡" value="养龙司乡"></el-option>
                                <el-option label="石硐乡" value="石硐乡"></el-option>
                                <el-option label="鹿窝乡" value="鹿窝乡"></el-option>
                                <el-option label="流长乡" value="流长乡"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="鸡场地址：" prop="address">
                            <el-input v-model.trim="temp.address" placeholder="请输入鸡场地址" style="width: 220px"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="鸡场名称：" prop="name">
                            <el-input v-model.trim="temp.name" placeholder="请输入鸡场名称" style="width: 220px"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="鸡场规模：" prop="size">
                            <el-input v-model.trim="temp.size" placeholder="请输入鸡场规模" style="width: 220px"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="营业执照：" prop="intelligence">
                            <el-upload
                                    class="avatar-uploader"
                                    action="null"
                                    :show-file-list="false"
                                    :before-upload="beforeImageUpload"
                                    :http-request="uploadIntelligenceImage"
                            >
                                <img v-if="temp.intelligence" :src="temp.intelligence" class="avatar">
                                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                            </el-upload>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="公司名称：" prop="companyName">
                            <el-input v-model.trim="temp.companyName" placeholder="请输入公司名称" style="width: 220px"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="公司法人：" prop="legalPerson">
                            <el-input v-model.trim="temp.legalPerson" placeholder="请输入公司法人" style="width: 220px"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="负责人 ：" prop="charger">
                            <el-input v-model.trim="temp.charger" placeholder="请输入负责人" style="width: 220px"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="联系电话：" prop="phoneNumber">
                            <el-input v-model.trim="temp.phoneNumber" placeholder="请输入联系电话" style="width: 220px"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="23">
                        <el-form-item style="min-height: 200px" label="鸡场简介：" prop="introduce">
                            <el-upload
                                    hidden="hidden"
                                    class="introduce"
                                    action="null"
                                    :show-file-list="false"
                                    :before-upload="beforeImageUpload"
                                    :http-request="uploadImage">
                            </el-upload>
                            <!--富文本编辑器组件-->
                            <quill-editor
                                    style="min-height: 400px;"
                                    v-model="temp.introduce"
                                    ref="myQuillEditor"
                                    :options="editorOption"
                            >
                            </quill-editor>
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
        <el-dialog title="修改" :visible.sync="dialogEditFormVisible" :modal-append-to-body='false'>
            <el-form :rules="rules" ref="dataForm" :model="temp" label-position="right" label-width="100px">

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="县：" prop="county">
                            <el-select v-model.trim="temp.county" placeholder="请选择县" style="width: 220px">
                                <el-option label="息烽县" value="息烽县"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="镇：" prop="town">
                            <el-select v-model.trim="temp.town" placeholder="请选择镇" style="width: 220px" >
                                <el-option label="永靖镇" value="永靖镇"></el-option>
                                <el-option label="温泉镇" value="温泉镇"></el-option>
                                <el-option label="九庄镇" value="九庄镇"></el-option>
                                <el-option label="小寨坝镇" value="小寨坝镇"></el-option>
                                <el-option label="青山苗族乡" value="青山苗族乡"></el-option>
                                <el-option label="西山乡" value="西山乡"></el-option>
                                <el-option label="养龙司乡" value="养龙司乡"></el-option>
                                <el-option label="石硐乡" value="石硐乡"></el-option>
                                <el-option label="鹿窝乡" value="鹿窝乡"></el-option>
                                <el-option label="流长乡" value="流长乡"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="鸡场地址：" prop="address">
                            <el-input v-model.trim="temp.address" placeholder="请输入鸡场地址" style="width: 220px"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="鸡场名称：" prop="name">
                            <el-input v-model.trim="temp.name" placeholder="请输入鸡场名称" style="width: 220px"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="鸡场规模：" prop="size">
                            <el-input v-model.trim="temp.size" placeholder="请输入鸡场规模" style="width: 220px"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="营业执照：" prop="intelligence">
                            <el-upload
                                    class="avatar-uploader"
                                    action="null"
                                    :show-file-list="false"
                                    :before-upload="beforeImageUpload"
                                    :http-request="uploadIntelligenceImage"
                            >
                                <img v-if="temp.intelligence" :src="temp.intelligence" class="avatar">
                                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                            </el-upload>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="公司名称：" prop="companyName">
                            <el-input v-model.trim="temp.companyName" placeholder="请输入公司名称" style="width: 220px"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="公司法人：" prop="legalPerson">
                            <el-input v-model.trim="temp.legalPerson" placeholder="请输入公司法人" style="width: 220px"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="负责人 ：" prop="charger">
                            <el-input v-model.trim="temp.charger" placeholder="请输入负责人" style="width: 220px"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="联系电话：" prop="phoneNumber">
                            <el-input v-model.trim="temp.phoneNumber" placeholder="请输入联系电话" style="width: 220px"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="23">
                        <el-form-item style="min-height: 200px" label="鸡场简介：" prop="introduce">
                            <el-upload
                                    hidden="hidden"
                                    class="introduce"
                                    action="null"
                                    :show-file-list="false"
                                    :before-upload="beforeImageUpload"
                                    :http-request="uploadImage">
                            </el-upload>
                            <!--富文本编辑器组件-->
                            <quill-editor
                                    style="min-height: 400px;"
                                    v-model="temp.introduce"
                                    ref="myQuillEditor"
                                    :options="editorOption"
                            >
                            </quill-editor>
                        </el-form-item>
                    </el-col>
                </el-row>

            </el-form>
            <div slot="footer" class="dialog-footer footer" >
                <el-button class="button" type="primary" @click="editData">保存</el-button>
                <el-button @click="dialogEditFormVisible = false">取消</el-button>
            </div>
        </el-dialog>

        <!--弹出窗口：详情-->
        <el-dialog title="详情" :visible.sync="dialogDetailsVisible" :modal-append-to-body='false'>

            <el-form ref="dataForm" :model="temp" label-position="right" label-width="100px">

                <el-row class="row">
                    <el-col :span="12">
                        <el-row>
                            <el-col :span="6" class="textRight">县：</el-col>
                            <el-col :span="6">{{temp.county}}</el-col>
                        </el-row>
                    </el-col>
                    <el-col :span="12">
                        <el-row>
                            <el-col :span="6" class="textRight">镇：</el-col>
                            <el-col :span="6">{{temp.town}}</el-col>
                        </el-row>
                    </el-col>
                </el-row>

                <el-row class="row">
                    <el-col :span="12">
                        <el-row>
                            <el-col :span="6" class="textRight">鸡场地址：</el-col>
                            <el-col :span="6">{{temp.address}}</el-col>
                        </el-row>
                    </el-col>
                    <el-col :span="12">
                        <el-row>
                            <el-col :span="6" class="textRight">鸡场名称：</el-col>
                            <el-col :span="6">{{temp.name}}</el-col>
                        </el-row>
                    </el-col>
                </el-row>

                <el-row class="row">
                    <el-col :span="12">
                        <el-row>
                            <el-col :span="6" class="textRight">鸡场规模：</el-col>
                            <el-col :span="6">{{temp.size}}</el-col>
                        </el-row>
                    </el-col>
                    <el-col :span="12">
                        <el-row>
                            <el-col :span="6" class="textRight">公司名称：</el-col>
                            <el-col :span="6">{{temp.companyName}}</el-col>
                        </el-row>
                    </el-col>
                </el-row>

                <el-row class="row">
                    <el-col :span="12">
                        <el-row>
                            <el-col :span="6" class="textRight">公司法人：</el-col>
                            <el-col :span="6">{{temp.legalPerson}}</el-col>
                        </el-row>
                    </el-col>
                    <el-col :span="12">
                        <el-row>
                            <el-col :span="6" class="textRight">负责人：</el-col>
                            <el-col :span="6">{{temp.charger}}</el-col>
                        </el-row>
                    </el-col>
                </el-row>

                <el-row class="row">
                    <el-col :span="12">
                        <el-row>
                            <el-col :span="6" class="textRight">联系电话：</el-col>
                            <el-col :span="6">{{temp.phoneNumber}}</el-col>
                        </el-row>
                    </el-col>
                    <el-col :span="12">
                        <el-row>
                            <el-col :span="6" class="textRight">营业执照：</el-col>
                            <el-col :span="6"><img v-if="temp.intelligence" :src="temp.intelligence" class="avatar"></el-col>
                        </el-row>
                    </el-col>
                </el-row>

                <el-row class="row">
                    <el-col :span="12">
                        <el-row>
                            <el-col :span="6" class="textRight">鸡场简介：</el-col>
                            <el-col :span="18"><div v-html="temp.introduce"> </div></el-col>
                        </el-row>
                    </el-col>
                </el-row>

            </el-form>

            <div slot="footer" class="dialog-footer footer">
                <el-button @click="dialogDetailsVisible = false">取消</el-button>
            </div>
        </el-dialog>

    </div>
</template>
<script>
    import {quillEditor} from 'vue-quill-editor'

    // 工具栏配置
    const toolbarOptions = [
        ['bold', 'italic', 'underline', 'strike'],        // 字体切换按钮
        ['blockquote', 'code-block'],                     //引用，显示代码区域块
        //标题 - header
        [{'header': 1}, {'header': 2}],                   // 值字体大小
        //列表 - list
        [{'list': 'ordered'}, {'list': 'bullet'}],        // 值ordered，bullet
        //上标/下标 - script
        [{'script': 'sub'}, {'script': 'super'}],         // 值sub，super
        //缩进 - indent
        [{'indent': '-1'}, {'indent': '+1'}],             // 值-1，+1
        //文本方向 - direction
        [{'direction': 'rtl'}],                            // 值rtl
        //字体大小
        [{'size': ['small', false, 'large', 'huge']}],  // custom dropdown
        //标题大小
        [{'header': [1, 2, 3, 4, 5, 6, false]}],
        //字体颜色、背景颜色、字体样式、排列、超链接、图片、视频、清空
        [{'color': []}, {'background': []}],          // dropdown with defaults from theme
        [{'font': []}],
        [{'align': []}],
        ['link', 'image', 'video'],
        ['clean']                                         // remove formatting button
    ]

    export default {
        name: 'Farm',
        data() {
            return {
                quillUpdateImg: false, // 根据图片上传状态来确定是否显示loading动画，刚开始是false,不显示
                isInputChange:false,//input是否有被编辑
                tableLoading: false,
                tableData: [],
                sels: [],//选中显示的值
                tableQuery: {
                    name: '',
                    charger: '',
                    phoneNumber: '',
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
                    name: '',
                    address: '',
                    size: '',
                    charger: '',
                    phoneNumber: '',
                    intelligence: '',
                    introduce: '',
                    county: '',
                    town: '',
                    companyName: '',
                    legalPerson: '',
                    createTime: ''
                },
                rules: {
                    county: [{ required: true, message: '必填', trigger: 'blur' }],
                    town: [{ required: true, message: '必填', trigger: 'blur' }],
                    name: [{ required: true, message: '必填', trigger: 'blur' }],
                    address: [{ required: true, message: '必填', trigger: 'blur' }],
                    size: [{ required: true, message: '必填', trigger: 'blur' }],
                    charger: [{ required: true, message: '必填', trigger: 'blur' }],
                    intelligence: [{ required: true, message: '必填', trigger: 'blur' }],
                    phoneNumber: [{
                        required: true,
                        pattern: /^1[34578]\d{9}$/,//正则表达式
                        message: '目前只支持中国大陆的手机号码',
                        trigger: 'blur'
                    }],
                },
                editorOption: {
                    placeholder: '请输入内容',
                    theme: 'snow',  // or 'bubble'
                    modules: {
                        toolbar: {
                            container: toolbarOptions,  // 工具栏
                            handlers: {
                                'image': function (value) {
                                    if (value) {
                                        // 触发input框选择图片文件
                                        document.querySelector('.introduce input').click()
                                    } else {
                                        this.quill.format('image', false);
                                    }
                                }
                            }
                        }
                    }
                },
                isIndeterminate: true,
                dialogCreateFormVisible: false,
                dialogEditFormVisible: false,
                dialogDetailsVisible: false
            }

        },
        created() {
            this.fetchData()
        },

        watch: {},

        methods: {
            uploadImage (param) {
                const target = param.file
                let params = new FormData()
                params.append("file",target)

                this.$http({
                    method: "post",
                    url: "/web-server/config/farm/query.json?action=uploadImage",
                    data: params
                }).then(res => {
                    // 获取富文本组件实例
                    let quill = this.$refs.myQuillEditor.quill
                    // 获取光标所在位置
                    let length = quill.getSelection().index;
                    // 插入图片  res.info为服务器返回的图片地址
                    quill.insertEmbed(length, 'image', res.data.data)
                    // 调整光标到最后
                    quill.setSelection(length + 1)
                }).catch(err => {
                    console.log(err);
                });
            },
            uploadIntelligenceImage (param) {
                const target = param.file
                let params = new FormData()
                params.append("file",target)

                this.$http({
                    method: "post",
                    url: "/web-server/config/farm/query.json?action=uploadImage",
                    data: params
                }).then(res => {
                    this.temp.intelligence = res.data.data;
                }).catch(err => {
                    console.log(err);
                });
            },
            beforeImageUpload(file) {
                const isJPG = file.type === 'image/jpeg';
                const isLt2M = file.size / 1024 / 1024 < 2;
                if (!isJPG) {
                    this.$message.error('图片只能是 JPG 格式');
                }
                if (!isLt2M) {
                    this.$message.error('图片大小不能超过 2MB');
                }
                return isJPG && isLt2M;
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
                this.tableLoading = true
                let params = new URLSearchParams();
                params.append("pageNo", this.tablePage.current);
                params.append("pageSize", this.tablePage.size);
                params.append("name", this.tableQuery.name);
                params.append("charger", this.tableQuery.charger);
                params.append("phoneNumber", this.tableQuery.phoneNumber);
                params.append("address", this.tableQuery.address);

                this.$http({
                    method: "post",
                    url: "/web-server/config/farm/query.json?action=query",
                    data: params
                }).then(res => {
                    this.tableData = res.data.rows
                    console.log(this.tableData)
                    this.tablePage.total = res.data.rowCount;
                    this.tablePage.current = res.data.pageNo;
                    this.tableLoading = false
                }).catch(err => {
                        console.log(err);
                    this.tableLoading = false
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
                    params.append("county", this.temp.county);
                    params.append("town", this.temp.town);
                    params.append("address", this.temp.address);
                    params.append("size", this.temp.size);
                    params.append("charger", this.temp.charger);
                    params.append("phoneNumber", this.temp.phoneNumber);
                    params.append("intelligence", this.temp.intelligence);
                    params.append("introduce", this.temp.introduce);
                    params.append("companyName", this.temp.companyName);
                    params.append("legalPerson", this.temp.legalPerson);

                    this.$http({
                        method: "post",
                        url: "/web-server/config/farm/query.json?action=save",
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
            handleEdit(farmId) {
                //每次点击新增按钮都将属性值赋值为0
                $(".btn-save").attr("clicked", "0")

                let params = new URLSearchParams();
                params.append("farmId", farmId);
                this.$http({
                    method: "post",
                    url: "/web-server/config/farm/query.json?action=getFarm",
                    data: params
                }).then(res => {
                    this.temp = Object.assign({}, res.data.data)
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
                    params.append("name", this.temp.name);
                    params.append("county", this.temp.county);
                    params.append("town", this.temp.town);
                    params.append("address", this.temp.address);
                    params.append("size", this.temp.size);
                    params.append("charger", this.temp.charger);
                    params.append("phoneNumber", this.temp.phoneNumber);
                    params.append("intelligence", this.temp.intelligence);
                    params.append("introduce", this.temp.introduce);
                    params.append("companyName", this.temp.companyName);
                    params.append("legalPerson", this.temp.legalPerson);
                    let createTime = new Date(this.temp.createTime);
                    params.append("createTime", createTime);
                    this.$http({
                        method: "post",
                        url: "/web-server/config/farm/query.json?action=save",
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
                        url: "/web-server/config/farm/query.json?action=delete",
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
            singleDel(id) {
                let params = new URLSearchParams();
                params.append("ids", id);
                this.$confirm('确定要删除选中的行吗?', '提示').then(() => {
                    this.$http({
                        method: "post",
                        url: "/web-server/config/farm/query.json?action=delete",
                        data: params
                    }).then(res => {
                        if(res.data.ok){
                            this.$message({
                                message: '删除成功',
                                type: 'success'
                            });
                        }else{
                            this.$message.error('删除失败,此鸡场已分配鸡棚');
                        }
						this.fetchData();

                    }).catch(err => {
                        console.log(err);
                    });
                }).catch(() => {
                });
            },
            del(id) {
                this.tableData.some((item, i) => {
                    if (item.id == id) {
                        this.tableData.splice(i, 1)
                        //在数组的some方法中，如果return true，就会立即终止这个数组的后续循环
                        --this.tablePage.total;
                        return true
                    }
                })
            },
            details(farmId) {
                let params = new URLSearchParams();
                params.append("farmId", farmId);
                this.$http({
                    method: "post",
                    url: "/web-server/config/farm/query.json?action=getFarm",
                    data: params
                }).then(res => {
                    this.temp = Object.assign({}, res.data.data)
                    this.dialogDetailsVisible = true;
                }).catch(err => {
                    console.log(err);
                });
            },
            reset(formName) {
                this.$refs[formName].resetFields();
            }
        }
    }
</script>
<style>
    .avatar-uploader .el-upload {
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
    }
    .avatar-uploader .el-upload:hover {
        border-color: #409EFF;
    }
    .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 178px;
        height: 178px;
        line-height: 178px;
        text-align: center;
    }
    .avatar {
        width: 178px;
        height: 178px;
        display: block;
    }
</style>
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

    .textRight {
        text-align: right;
    }
    .row {
        padding-bottom: 10px;
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

