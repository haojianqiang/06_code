<template>
    <div class="container">       
        <!-- 容器 -->
        <div class="header">

            <!-- 输入框和搜索按钮 -->
            <div class="divInput">
                <el-input v-model="inputSearch" placeholder="请输入要查询的资料名称和关键字" style="margin-left: 25%;width:400px;">
                    <el-button slot="append" icon="el-icon-search" @click="search()"></el-button>
                </el-input>
            </div>

            <!-- 文字导航 -->
            <div class="navigation">
                <el-row :gutter="2" align="middle">
                    <el-col :span="2"><span>种植物</span></el-col>
                    <el-col :span="1"><el-link href="/#/classify">水稻</el-link></el-col>
                    <el-col :span="1"><el-link>花生</el-link></el-col>
                    <el-col :span="1"><el-link>番薯</el-link></el-col>
                    <el-col :span="1">|</el-col>

                    <el-col :span="2"><span>蔬菜</span></el-col>
                    <el-col :span="1"><el-link>生菜</el-link></el-col>
                    <el-col :span="1"><el-link>花菜</el-link></el-col>
                    <el-col :span="1"><el-link>芹菜</el-link></el-col>
                    <el-col :span="1">|</el-col>

                    <el-col :span="2"><span>水果</span></el-col>
                    <el-col :span="1"><el-link>香蕉</el-link></el-col>
                    <el-col :span="1"><el-link>苹果</el-link></el-col>
                    <el-col :span="1"><el-link>橙子</el-link></el-col>
                    <el-col :span="1">|</el-col>

                    <el-col :span="1"><span>家禽</span></el-col>
                    <el-col :span="1"><el-link>鸡</el-link></el-col>
                    <el-col :span="1"><el-link>鸭</el-link></el-col>
                    <el-col :span="1"><el-link>鹅</el-link></el-col>

                    <el-col :span="2">
                        <span class="el-dropdown-link" @click="moreNav()" v-if="!navHide">
                            更多<i class="el-icon-arrow-down el-icon--right"></i>
                        </span>
                    </el-col>
                    <el-col :span="2">
                        <span class="el-dropdown-link" @click="moreNav()" v-if="navHide">
                            收起<i class="el-icon-arrow-down el-icon--right"></i>
                        </span>
                    </el-col>     
                </el-row>
                
                <el-row :gutter="2" align="middle" v-if="navHide">
                    <hr>
                    <el-col :span="2"><span>鱼类</span></el-col>
                    <el-col :span="1"><el-link>草鱼</el-link></el-col>
                    <el-col :span="1"><el-link>鲈鱼</el-link></el-col>
                    <el-col :span="1"><el-link>皖鱼</el-link></el-col>
                    <el-col :span="1">|</el-col>

                    <el-col :span="2"><span>瓜类</span></el-col>
                    <el-col :span="1"><el-link>冬瓜</el-link></el-col>
                    <el-col :span="1"><el-link>南瓜</el-link></el-col>
                    <el-col :span="1"><el-link>西瓜</el-link></el-col>
                </el-row>
            </div>
            
            <!-- 主体内容 -->
            <div class="main" v-if="mainHide">
                <div>
                    <!-- 左侧：点击量 -->
                    <div style="float:left;margin-left:20px;">
                        <p>点击量排行榜</p>
                    </div>

                    <!-- 右侧：时间 -->
                    <div style="float:right;margin-right:20px;">
                        <p>{{ today | formatDate }}</p>
                    </div>
                </div>

                <!-- 内容 -->
                <databankslot>
                    <div style="margin: 30px 0;padding: 10px;clear:both; background-color: #ffffff;">
                        <div>
                            <!-- 图片 -->
                            <div style="float:right;">
                                <img src="@/assets/chicken1.jpg" style="width:100px">
                            </div>

                            <!-- 段落 -->
                            <div>
                                <a href="/#/articlecontent">
                                <p>息烽县肉鸡养殖从门到精通</p>
                                <p>2019年11月11日</p>
                                </a>
                            </div>
                        </div>
                    </div>
                </databankslot>
            </div>

            <!-- 搜索内容 -->
            <div class="main" v-if="!mainHide">
                <div class="divSearch">
                    <div class="elPageHeader">
                        <el-page-header @back="goBack" content="资料库"></el-page-header>
                    </div>
                    <hr>
                    <p style="margin:2%;">您搜索的内容空空如也...</p>
                </div>
            </div>
        </div>

        
    </div>
</template>

<script>
import databankslot from './databankslot'


export default {
    data() {
        return {
            mainHide: true,
            navHide: false,
            inputSearch: '',
            today: Date.parse(new Date()),
        }
    },
    filters: {
        formatDate: function(value) {
            let date = new Date(value);
            let y = date.getFullYear();
            let MM = date.getMonth() + 1;
            MM = MM < 10 ? ('0' + MM) : MM;
            let d = date.getDate();
            d = d < 10 ? ('0' + d) : d;
            return y + '年' + MM + '月' + d + '日';
        }
    },
    methods: {
        // 搜索框事件
        search() {
            // 如果搜索内容不为空，就发起get请求
            if(this.inputSearch){
                // this.$http.get('url')
                // .then(res => {
                //     this.mainHide = false;
                //     this.inputSearch = '';
                //     this.$message({
                //         type: 'success',
                //         message: '搜索内容如下',
                //     });
                // }).catch(err => {
                //     console.log(err)
                // })
                this.mainHide = false;
                    this.inputSearch = '';
                    this.$message({
                        type: 'success',
                        message: '搜索内容如下',
                    }); 
            }else{
                this.$message({
                    type: 'warning',
                    message: '搜索内容不能为空！'
                })
            }
            
        },
        // 搜索返回
        goBack() {
            this.mainHide = true;
        },
        // 点击更多展开导航
        moreNav() {
            this.navHide = !this.navHide;
        },
    },
    components: {
        // 引用子组件作为插槽
        databankslot,
    },
    
}
</script>

<style scoped>
    .container {
        background-color: #f4f4f4;
        color:#000000;
        min-height:100%;
    }
    .header {
        padding:30px 80px;
    }
    .divInput {
        margin:5px 0 10px 0;
    }
    .main {
        margin:30px 0;
    }
    .navigation {
        margin: 10px 0;
        padding: 20px;
        max-height:3%;
        background-color:#ffffff;
        box-shadow: 0 1px 2px #CC9999;
    }
    .el-dropdown-link {
        cursor: pointer;
    }
    .el-link:hover {
        background-color:#99CCCC;
    }
    .divSearch {
        background-color:#ffffff;
        min-height: 500px;
    }
    .elPageHeader {
        height:30px;
        padding: 10px 0 0 10px;
    }
</style>