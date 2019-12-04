<template>
    <!-- 数据库管理 -->
    <div>
        <!-- 头部信息 -->
        <div class="divClass">
            <!-- 查询添加 -->
            <span>分类：</span>
            <el-select v-model="selValue" placeholder="请选择分类" class="select">
                <el-option v-for="(item, index) in options" :key="index" :value="item"></el-option>
            </el-select>
            <span>关键字：</span>
            <el-input v-model="input" placeholder="请输入标题搜索关键字" class="input"></el-input>

            <!-- 按钮 -->
            <el-button class="button" type="success" @click="infoFetch()">查询</el-button>
            <el-button class="button" type="success" @click="infoReset()">重置</el-button>
            <el-button class="button" type="success" @click="infoAdd()">
                <span class="el-icon-circle-plus-outline"></span>添加
            </el-button>
        </div>

        <!-- 主体信息 -->
        <div>
            <!-- 操作按钮 -->
            <div class="divClass">
                <el-button class="button" type="success" @click="infoBatchDel()">批量删除</el-button>
            </div>

            <!-- 表格 -->
            <div class="divClass paddingRight" v-show="!tableShowBtn">
                <el-table :data="tabledata.slice((tablePage.current - 1) * tablePage.size, tablePage.current * tablePage.size)" :header-cell-style="{background:'#e6eaf3',height:'50px'}" border @selection-change="selsChange">
                    <el-table-column type="selection" prop="id"></el-table-column>
                    <el-table-column label="操作">
                        <template v-slot="scope">
                            <a class="link" href="javascript:void(0)" @click="infoEdit(scope.$index)">编辑</a>
                            <a class="link" href="javascript:void(0)"  @click="infoDel(scope.$index)">删除</a>
                            <a class="link" href="javascript:void(0)" v-show="isTopBtn" @click="isTop(scope.$index)">置顶</a>
                            <a class="link"  href="javascript:void(0)" v-show="!isTopBtn" @click="isTop(scope.$index)">取消置顶</a>
                        </template>
                    </el-table-column>
                    <el-table-column label="标题" prop="title"></el-table-column>
                    <el-table-column label="阅读量" prop="hits"></el-table-column>
                    <el-table-column label="所属分类" prop="classfiy"></el-table-column>
                    <el-table-column label="分布时间" prop="pubdate"></el-table-column>
                </el-table>
            </div>

            <!-- 分页 -->
            <div class="divClass pagintion" v-show="!tableShowBtn">
                <el-pagination
                background
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :page-sizes="tablePage.pageSizes"
                :page-size="tablePage.size"
                layout="sizes, prev, pager, next, jumper, total"
                :total="total">
                </el-pagination>
            </div>

            <!-- 搜索结果 -->
            <div class="divClass paddingRight" v-show="tableShowBtn">
                <el-table :data="searchTabledata.slice((tablePage.current - 1) * tablePage.size, tablePage.current * tablePage.size)" :header-cell-style="{background:'#e6eaf3',height:'50px'}" border>
                    <el-table-column type="selection" prop="id"></el-table-column>
                    <el-table-column label="操作">
                        <template v-slot="scope">
                            <a class="link" href="javascript:void(0)" @click="infoEdit(scope.$index)">编辑</a>
                            <a class="link" href="javascript:void(0)"  @click="infoDel(scope.$index)">删除</a>
                            <a class="link" href="javascript:void(0)" v-show="isTopBtn" @click="isTop(scope.$index)">置顶</a>
                            <a class="link"  href="javascript:void(0)" v-show="!isTopBtn" @click="isTop(scope.$index)">取消置顶</a>
                        </template>
                    </el-table-column>
                    <el-table-column label="标题" prop="title"></el-table-column>
                    <el-table-column label="阅读量" prop="hits"></el-table-column>
                    <el-table-column label="所属分类" prop="classfiy"></el-table-column>
                    <el-table-column label="分布时间" prop="pubdate"></el-table-column>
                </el-table>
            </div>

            <!-- 搜索分页 -->
            <div class="divClass pagintion" v-show="tableShowBtn">
                <el-pagination
                background
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :page-sizes="tablePage.pageSizes"
                :page-size="tablePage.size"
                layout="sizes, prev, pager, next, jumper, total"
                :total="0">
                </el-pagination>
            </div>
        </div>      
    </div>
</template>

<script>
export default {
    data() {
        return {
            // 搜索框的输入值
            input: '',
            // 分类框的值
            selValue: '',
            // 选中的表格行
            sels: [], 
            // 置顶按钮
            isTopBtn: true,
            // 搜索结果展示
            tableShowBtn: false,
            // 分类选项
            options: ['种植物', '蔬菜', '水果', '家禽', '鱼类', '瓜类'],
            // 表格数据
            tabledata: [
                {
                    id:1,
                    title:'1养鸡从门到精通',
                    hits: 9999,
                    classfiy: '养殖',
                    pubdate: '2019-11-1',
                },
                {
                    id:2,
                    title:'2养鸡从门到精通',
                    hits: 9999,
                    classfiy: '养殖',
                    pubdate: '2019-11-1',
                },
                {
                    id:3,
                    title:'3养鸡从门到精通',
                    hits: 9999,
                    classfiy: '养殖',
                    pubdate: '2019-11-1',
                },
                {
                    id:4,
                    title:'4养鸡从门到精通',
                    hits: 9999,
                    classfiy: '养殖',
                    pubdate: '2019-11-1',
                },
                {
                    id:5,
                    title:'5养鸡从门到精通',
                    hits: 9999,
                    classfiy: '养殖',
                    pubdate: '2019-11-1',
                },
                {
                    id:6,
                    title:'6养鸡从门到精通',
                    hits: 9999,
                    classfiy: '养殖',
                    pubdate: '2019-11-1',
                },
                {
                    id:7,
                    title:'7养鸡从门到精通',
                    hits: 9999,
                    classfiy: '养殖',
                    pubdate: '2019-11-1',
                },
                {
                    id:8,
                    title:'8养鸡从门到精通',
                    hits: 9999,
                    classfiy: '养殖',
                    pubdate: '2019-11-1',
                },
                {
                    id:9,
                    title:'9养鸡从门到精通',
                    hits: 9999,
                    classfiy: '养殖',
                    pubdate: '2019-11-1',
                },
                {
                    id:10,
                    title:'10养鸡从门到精通',
                    hits: 9999,
                    classfiy: '养殖',
                    pubdate: '2019-11-1',
                },
                {
                    id:11,
                    title:'11养鸡从门到精通',
                    hits: 9999,
                    classfiy: '养殖',
                    pubdate: '2019-11-1',
                },
                {
                    id:12,
                    title:'12养鸡从门到精通',
                    hits: 9999,
                    classfiy: '养殖',
                    pubdate: '2019-11-1',
                },
                {
                    id:13,
                    title:'13养鸡从门到精通',
                    hits: 9999,
                    classfiy: '养殖',
                    pubdate: '2019-11-1',
                },
                {
                    id:14,
                    title:'14养鸡从门到精通',
                    hits: 9999,
                    classfiy: '养殖',
                    pubdate: '2019-11-1',
                },
                {
                    id:15,
                    title:'15养鸡从门到精通',
                    hits: 9999,
                    classfiy: '养殖',
                    pubdate: '2019-11-1',
                },
                {
                    id:16,
                    title:'16养鸡从门到精通',
                    hits: 9999,
                    classfiy: '养殖',
                    pubdate: '2019-11-1',
                },
                {
                    id:17,
                    title:'17养鸡从门到精通',
                    hits: 9999,
                    classfiy: '养殖',
                    pubdate: '2019-11-1',
                },
                {
                    id:18,
                    title:'18养鸡从门到精通',
                    hits: 9999,
                    classfiy: '养殖',
                    pubdate: '2019-11-1',
                },

                {
                    id:19,
                    title:'19养鸡从门到精通',
                    hits: 9999,
                    classfiy: '养殖',
                    pubdate: '2019-11-1',
                },{
                    id:20,
                    title:'20养鸡从门到精通',
                    hits: 9999,
                    classfiy: '养殖',
                    pubdate: '2019-11-1',
                },

                {
                    id:21,
                    title:'21养鸡从门到精通',
                    hits: 9999,
                    classfiy: '养殖',
                    pubdate: '2019-11-1',
                },
                {
                    id:22,
                    title:'22养鸡从门到精通',
                    hits: 9999,
                    classfiy: '养殖',
                    pubdate: '2019-11-1',
                },
                {
                    id:23,
                    title:'23养鸡从门到精通',
                    hits: 9999,
                    classfiy: '养殖',
                    pubdate: '2019-11-1',
                },
                {
                    id:24,
                    title:'24养鸡从门到精通',
                    hits: 9999,
                    classfiy: '养殖',
                    pubdate: '2019-11-1',
                },
                {
                    id:25,
                    title:'25养鸡从门到精通',
                    hits: 9999,
                    classfiy: '养殖',
                    pubdate: '2019-11-1',
                },
                {
                    id:26,
                    title:'26养鸡从门到精通',
                    hits: 9999,
                    classfiy: '养殖',
                    pubdate: '2019-11-1',
                },
            ],
            // 搜索的表格内容
            searchTabledata: [],
            // 表格分页对象
            tablePage: {
                current: 1,
                pageSizes: [10, 20, 30],
                size: 10,
            },
        }
    },
    computed: {
        total:function() {
            return this.tabledata.length;
        }
    },
    methods: {
        // 重置分类框、搜索框，并显示全文
        infoReset() {
            this.input = ''
            this.selValue = ''
            this.$message({
                    type: 'success',
                    message: '重置成功'
                })
            this.tableShowBtn = false
        },
        // 查询数据
        infoFetch() {
            // 判断是否已选择分类
            if(this.selValue === '') {
                this.$message({
                    type: 'error',
                    message: '请选择分类'
                })
                return
            }
            // 判断是否输入标题搜索关键字
            if(this.input === '') {
                this.$message({
                    type: 'error',
                    message: '请输入标题搜索关键字'
                })
                return
            } 
            this.$message({
                type: 'success',
                message: '搜索内容如下'
            })
            this.tableShowBtn = true
        },
        // 添加数据
        infoAdd() {

        },
        // 修改数据
        infoEdit() {

        },
        // 删除数据
        infoDel(rowid) {
            this.tabledata.splice(rowid,1)
        },
        // 批量删除数据
        infoBatchDel() {
            // var ids = [];
            // this.sels.forEach(element => {
            //     console.log("element.id:"+element.id)
            // });
            // let params = new URLSearchParams();
            // params.append("ids", id);

            // 判断是否有勾选
            if(Object.keys(this.sels).length == []) {
                this.$message({
                    type: 'error',
                    message: '请勾选需要删除的资料'
                })
                return
            }
            var max;
            // 将数组从大到小排列
            for(let i=0; i<this.sels.length; i++) {
                for (let j=i; j<this.sels.length; j++) {
                    if(this.sels[i].id < this.sels[j].id) {
                        max = this.sels[j];
                        this.sels[j] = this.sels[i];
                        this.sels[i] = max;
                    }
                }
            }
            this.$confirm('确定要删除选中的行吗？', '提示', {
                confirmButtonText: '确定',
                canceBUttonText: '取消',
                type: 'warning',
            }).then(() => {
                this.sels.forEach(element => {                  
                    this.tabledata.splice(element.id-1,1)
                })    
                this.$message({
                    type: 'success',
                    message: '删除成功！',
                })
            }).catch(() => {
                this.$message({
                    type: 'success',
                    message: '已取消',
                })
            })
        },
        // 将被选中的行组成数组
        selsChange(sels) {
            this.sels = sels;
        },
        // 置顶
        isTop() {
            this.isTopBtn = !this.isTopBtn;
        },
        // 分页数发生改变时
        handleSizeChange(val) {
            this.tablePage.size = val;
        },
        // 当前页
        handleCurrentChange(val) {
            this.tablePage.current = val;
        },
    },

}
</script>

<style scoped>
.divClass {
    text-align: left;
    padding-left: 50px;
    margin-top: 20px;
}
.button {
    background-color: #3ebf7c;
}
.paddingRight {
    padding-right: 50px;
}
.link {
    color: #3ebf7c;
    margin-right: 5px;
}
.select {
    margin-right: 3%;
}
.input {
    margin-right: 3%;
}
.pagintion {
    float: right;
    padding-right: 50px;
    margin-bottom: 35px;
}
</style>