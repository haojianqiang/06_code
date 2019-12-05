<template>
    <!-- 线上服务 -->
    <div class="container">

        <!-- 输入框和搜索按钮 -->
        <div class="divClass">
            <el-input v-model="inputSearch" placeholder="请输入您要查询的专家名称" style="margin-left: 25%;width:400px;">
                <el-button slot="append" icon="el-icon-search" @click="search()"></el-button>
            </el-input>
        </div>

        <!-- 主体内容 -->
        <div class="divClass">
            <!-- 专家信息 -->
            <div class="divExpert left" v-for="(expert, index) in experts.slice((tablePage.current - 1) * tablePage.size, tablePage.current * tablePage.size)" :key="index">
                <img :src="expert.img">
                <p>{{ expert.name }} - {{ expert.title }}</p>
                <p>{{ expert.college }}</p>
                <p>擅长领域：{{ expert.sphere }}</p>
                <!-- 按钮 -->
                <el-button size="small" type="success" @click="toChat(expert.id)">免费咨询</el-button>
                <el-button size="small" type="info" @click="toIntroduce(expert.id)">个人简介</el-button>
            </div>

            <!-- 分页 -->
            <div class="divClass pagintion">
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
            <el-dialog title="搜索结果" :visible.sync="dialogVisible" :modal-append-to-body="false" :close-on-click-modal="false" width="25%">
                <div class="divExpert dialogBody">
                    <img :src="searchExpert.img">
                    <p>{{ searchExpert.name }} - {{ searchExpert.title }}</p>
                    <p>{{ searchExpert.college }}</p>
                    <p>擅长领域：{{ searchExpert.sphere }}</p>
                    <!-- 按钮 -->
                    <el-button size="small" type="success" @click="toChat(searchExpert.id)">免费咨询</el-button>
                    <el-button size="small" type="info" @click="toIntroduce(searchExpert.id)">个人简介</el-button>
                </div>
                <span slot="footer" class="dialogFooter"></span>
            </el-dialog>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            // 搜索框
            inputSearch: '',
            // 搜索结果显示
            dialogVisible: false,
            // 搜索结果对象
            searchExpert: {
                id: 1,
                name: '袁隆平',
                title: '首席咨询专家',
                college: '华南农业大学',
                sphere: '杂交水稻',
                img: 'http://img2.imgtn.bdimg.com/it/u=2148025029,3379649937&fm=26&gp=0.jpg',
            },
            // 专家组数组
            experts: [
                {
                    id: 1,
                    name: '袁隆平',
                    title: '首席咨询专家',
                    college: '华南农业大学',
                    sphere: '杂交水稻',
                    img: 'http://img2.imgtn.bdimg.com/it/u=2148025029,3379649937&fm=26&gp=0.jpg',
                },
                {
                    id: 2,
                    name: '袁隆平',
                    title: '首席咨询专家',
                    college: '华南农业大学',
                    sphere: '杂交水稻',
                    img: 'http://img4.imgtn.bdimg.com/it/u=878138499,1623582841&fm=26&gp=0.jpg',
                },
                {
                    id: 3,
                    name: '袁隆平',
                    title: '首席咨询专家',
                    college: '华南农业大学',
                    sphere: '杂交水稻',
                    img: 'http://img3.imgtn.bdimg.com/it/u=1150332916,1043869403&fm=26&gp=0.jpg',
                },
                {
                    id: 4,
                    name: '袁隆平',
                    title: '首席咨询专家',
                    college: '华南农业大学',
                    sphere: '杂交水稻',
                    img: 'http://img4.imgtn.bdimg.com/it/u=1046260098,1185832924&fm=26&gp=0.jpg',
                },
                {
                    id: 5,
                    name: '袁隆平',
                    title: '首席咨询专家',
                    college: '华南农业大学',
                    sphere: '杂交水稻',
                    img: 'http://img0.imgtn.bdimg.com/it/u=1607403629,4109042405&fm=26&gp=0.jpg',
                },
                {
                    id: 6,
                    name: '袁隆平',
                    title: '首席咨询专家',
                    college: '华南农业大学',
                    sphere: '杂交水稻',
                    img: 'http://img4.imgtn.bdimg.com/it/u=1046260098,1185832924&fm=26&gp=0.jpg',
                },
                {
                    id: 7,
                    name: '袁隆平',
                    title: '首席咨询专家',
                    college: '华南农业大学',
                    sphere: '杂交水稻',
                    img: 'http://img1.imgtn.bdimg.com/it/u=3874847862,2345107481&fm=26&gp=0.jpg',
                },
                {
                    id: 8,
                    name: '袁隆平',
                    title: '首席咨询专家',
                    college: '华南农业大学',
                    sphere: '杂交水稻',
                    img: 'http://img4.imgtn.bdimg.com/it/u=878138499,1623582841&fm=26&gp=0.jpg',
                },
                {
                    id: 9,
                    name: '袁隆平',
                    title: '首席咨询专家',
                    college: '华南农业大学',
                    sphere: '杂交水稻',
                    img: 'http://img3.imgtn.bdimg.com/it/u=1150332916,1043869403&fm=26&gp=0.jpg',
                },
                {
                    id: 10,
                    name: '袁隆平',
                    title: '首席咨询专家',
                    college: '华南农业大学',
                    sphere: '杂交水稻',
                    img: 'http://img0.imgtn.bdimg.com/it/u=1607403629,4109042405&fm=26&gp=0.jpg',
                },
                {
                    id: 11,
                    name: '袁隆平',
                    title: '首席咨询专家',
                    college: '华南农业大学',
                    sphere: '杂交水稻',
                    img: 'http://img0.imgtn.bdimg.com/it/u=1607403629,4109042405&fm=26&gp=0.jpg',
                },
                {
                    id: 12,
                    name: '袁隆平',
                    title: '首席咨询专家',
                    college: '华南农业大学',
                    sphere: '杂交水稻',
                    img: 'http://img2.imgtn.bdimg.com/it/u=2148025029,3379649937&fm=26&gp=0.jpg',
                },
            ],
            // 分页对象
            tablePage: {
                current: 1,
                pageSizes: [10, 20, 30],
                size: 10,
            },
        }
    },
    methods: {
        // 搜索
        search() {
            // 判断输入框是否有信息
            if(!this.inputSearch) {
                this.$message({
                    type: 'error',
                    message: '请输入专家名称'
                })
                return
            }
            this.dialogVisible = true
            this.$message({
                type: 'success',
                message: '搜索成功！内容如下。'
            })
        },
        // 免费咨询
        toChat(id) {
            this.$router.push({
                name: '免费咨询',
                params: {
                    id: id,
                }
            })
        },
        // 个人简介
        toIntroduce(id) {
            this.$router.push({
                name: '个人简介',
                params: {
                    id: id,
                }
            })
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
    computed: {
        total: function() {
            return this.experts.length
        },
    },
}
</script>

<style scoped>
.container {
    font:Helvetica Neue,Helvetica,PingFang SC,Tahoma,Arial,sans-serif;
    padding: 25px 3%;
}
.divClass {
    margin:5px 0 10px 0;
}
.divExpert {
    width: 200px;
    height: 300px;
    margin: 5px;
    font-size: 14px;
    border: 1px solid #CCCC00;
    padding: 10px;
}
.divExpert img {
    width: 180px;
    height: 140px;
}
.left {
    float: left;
}
.dialogBody {
    margin: 0 auto;
}
.dialogFooter {
    margin-top: 20px;
}
.pagintion {
    clear: both;
    float: right;
    padding-right: 50px;
    margin-bottom: 35px;
}
</style>