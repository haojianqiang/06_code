<template>
    <!-- 资料库 -->
    <div class="container">       
        <!-- 容器 -->
        <div class="containerMian">

            <!-- 输入框和搜索按钮 -->
            <div class="divInput">
                <el-input v-model="inputSearch" placeholder="请输入要查询的资料名称和关键字" style="margin-left: 25%;width:400px;">
                    <el-button slot="append" icon="el-icon-search" @click="search()"></el-button>
                </el-input>
            </div>

            <!-- 文字导航 --> 
            <el-menu class="elMenu" mode="horizontal" v-for="(navigation, index1) in navigations" :key="index1">
                <el-submenu index="index1">
                    <template slot="title">{{ navigation[0] }}</template>
                    <span v-for="(nav, index2) in navigation.slice(1)" :key="index2">
                        <el-menu-item index="index1-index2" @click="toClassify(nav)">
                            {{ nav }}
                        </el-menu-item>
                    </span>                   
                </el-submenu>
            </el-menu>

            <!-- 主体内容 -->
            <div class="main" v-if="mainHide">
                <div>
                    <!-- 左侧：点击量 -->
                    <div class="mainHits">
                        <p>点击量排行榜</p>
                    </div>

                    <!-- 右侧：时间 -->
                    <div class="mainTime">
                        <p>{{ today | formatDate }}</p>
                    </div>
                </div>

                <!-- 内容 -->
                <div v-for="(maincontent, index) in mainContents" :key="index">
                    <div style="margin: 30px 0;padding: 10px;clear:both; background-color: #ffffff;">
                        <div>
                            <!-- 图片 -->
                            <div style="float:right;">
                                <img :src="maincontent.imgsrc[0]" style="width:100px">
                            </div>

                            <!-- 段落 -->
                            <div>
                                <a href="javascript:void(0);" @click="toArticlecontent(maincontent)">
                                <p>{{ maincontent.title }}</p>      
                                </a>
                                <p>{{ maincontent.pubdate }}</p>
                                <p>点击量:{{ maincontent.hits }}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 搜索内容 -->
            <div class="main" v-if="!mainHide">
                <div class="divSearch">
                    <div class="elPageHeader">
                        <el-page-header @back="goBack" content="资料库"></el-page-header>
                    </div>
                    <hr>
                    <p style="margin:2%;">很抱歉，暂无匹配的资料...</p>
                </div>
            </div>
        </div>

    </div>
</template>

<script>
export default {
    data() {
        return {
            // 主体内容、搜索内容控制按钮
            mainHide: true,
            // 导航栏更多、收起切换按钮
            navHide: false,
            // 搜索框内容
            inputSearch: '',
            // 主体内容右上角的时间
            today: Date.parse(new Date()),          
            // 文章内容对象
            maincontent: {
                title: '',
                pubdate: '',
                update: '',
                hits: null,
                content: '',
                imgsrc: '',
            },
            // 文章主题内容组
            mainContents: [
                {
                    title: '养鸡从门到精通',
                    pubdate: '2019年11月1日',
                    update: '2019年11月1日11点25分',
                    hits: 9999,
                    content: `<div>
                                <p>规模化养鸡场生产的快大型肉鸡（45天出栏）已不能满足消费者的特殊需要，而放养的地方土鸡以围林野养为主，以五谷杂食和田间地头草虫为食，生产的鸡肉野味十足、营养丰富、安全无公害，并且注重生态保护，控制环境污染，倍受消费者青睐。
                                </p>
                                <p>山地养鸡优点：环境自然，食料成本低，建设房屋少。投资成本低。室外放养，鸡食杂草，小虫。营养丰富。4个月出栏的土鸡每只鸡食料成本不足30元。土鸡价格高，虽然长的慢，但成活率高，收益好。另外室外活动大，鸡肉质好。销路宽。本土化本地鸡苗，加上全天然室外放养。自然光照射时间长，鸡食五谷杂粮，青草，小虫。鸡体健康，深得本地老百姓喜爱。是餐桌“地”土文化重要元素。</p>

                                <img src="http://img5.imgtn.bdimg.com/it/u=1165562533,1507359634&fm=26&gp=0.jpg" />
                                <img src="https://fc3tn.baidu.com/it/u=3176056652,1397701118&fm=202&src=bqdata" />

                                <p>这是一种投资少、见效快、收益高的养鸡方法，值得在刚开始从事养鸡业、经济条件较差的养鸡户中推广应用。</p>
                                <p>塑料大棚养鸡的优点取材方便：塑料大棚鸡舍的主要材料为普通农用薄膜，竹子和草帘，这些材料除薄膜外，其它均易获得。可随需随建，不与农争田，且能农牧结合，养鸡肥田，减少环境污染，同时也便于棚舍彻底消毒，根除病菌。</p>
                                <p>发酵床养鸡技术在20世纪50年代首先由日本山岸会进行研究开发。发酵床养鸡就是一种行之有效，更为合理的生态养鸡，既做到了鸡粪的有效处理，实现了零排放、无污染、无臭味，又为鸡的健康生长，提供了最适宜的生态环境。鸡在这种环境下生长，生长快、产蛋多、蛋的品质好、生病少，用工、用水、用料大为节省，养鸡的效益显著提高。</p>
                                <p>发酵助剂与锯末屑混匀后，在圈内均匀铺撒，总厚度不低于40厘米，随着锯末层内鸡粪尿越积越多，3-7天后，功能微生物经过活化定殖，呈几何级数大量繁殖（24小时理论繁殖数量可达2个！），开始利用粪尿等作营养源，将锯末粪尿混和物逐渐升温发酵，中心发酵层温度可达30-50℃或更高，表层温度长期稳定在20几度，基本形成恒温床。下层发酵完成后，锯末等垫料物会因发酵碳化逐渐变深变黑，发酵产物能作肥料或粗饲料，可分批清运出舍。如不需使用，则可长期不清运，时间一长还应补充垫料。</p>
                                <p>对于肉种鸡来说，早期体重达标除了公鸡有些困难外，母鸡比较容易，一般在管理到位的情况下，第一周就可以使体重达标，但鸡只具有较强的择食性，如果一次性加料过多，鸡只一吃饱后就开始挑食，造成严重的撒料现象，导致体重达标往往是付出“更多代价”换来的，所以育雏中期至限饲阶段采用间隔喂料法，将采食时间相对集中，目的就是为了减少撒料浪费，方法是将全天的喂料量分次投给，第一次可以适当的多一些，供鸡只采食1个小时以上，采食完后暂时停料，让鸡只饮水或自由活动，使其有一个短暂饥饿的过程，等待下次加料。这样每次喂料后鸡只都会积极采食，并相对加快采食速度，减少因择食造成的撒料浪费，具体上料时间和停料间隔可根据现场实际情况而定。</p>
                            </div>`,
                    imgsrc: [require("@/assets/chicken1.jpg"),],
                    labels: ['饲养', '管理'],
                },
                {
                    title: '养鸡你需要知道的一些小知识',
                    pubdate: '2019年11月18日',
                    update: '2019年11月18日12点00分',
                    hits: 8888,
                    content: `<div>
                                <p>规模化养鸡场生产的快大型肉鸡（45天出栏）已不能满足消费者的特殊需要，而放养的地方土鸡以围林野养为主，以五谷杂食和田间地头草虫为食，生产的鸡肉野味十足、营养丰富、安全无公害，并且注重生态保护，控制环境污染，倍受消费者青睐。
                                </p>
                                <p>山地养鸡优点：环境自然，食料成本低，建设房屋少。投资成本低。室外放养，鸡食杂草，小虫。营养丰富。4个月出栏的土鸡每只鸡食料成本不足30元。土鸡价格高，虽然长的慢，但成活率高，收益好。另外室外活动大，鸡肉质好。销路宽。本土化本地鸡苗，加上全天然室外放养。自然光照射时间长，鸡食五谷杂粮，青草，小虫。鸡体健康，深得本地老百姓喜爱。是餐桌“地”土文化重要元素。</p>

                                <img src="http://img5.imgtn.bdimg.com/it/u=1165562533,1507359634&fm=26&gp=0.jpg" />
                                <img src="https://fc3tn.baidu.com/it/u=3176056652,1397701118&fm=202&src=bqdata" />

                                <p>这是一种投资少、见效快、收益高的养鸡方法，值得在刚开始从事养鸡业、经济条件较差的养鸡户中推广应用。</p>
                                <p>塑料大棚养鸡的优点取材方便：塑料大棚鸡舍的主要材料为普通农用薄膜，竹子和草帘，这些材料除薄膜外，其它均易获得。可随需随建，不与农争田，且能农牧结合，养鸡肥田，减少环境污染，同时也便于棚舍彻底消毒，根除病菌。</p>
                                <p>发酵床养鸡技术在20世纪50年代首先由日本山岸会进行研究开发。发酵床养鸡就是一种行之有效，更为合理的生态养鸡，既做到了鸡粪的有效处理，实现了零排放、无污染、无臭味，又为鸡的健康生长，提供了最适宜的生态环境。鸡在这种环境下生长，生长快、产蛋多、蛋的品质好、生病少，用工、用水、用料大为节省，养鸡的效益显著提高。</p>
                                <p>发酵助剂与锯末屑混匀后，在圈内均匀铺撒，总厚度不低于40厘米，随着锯末层内鸡粪尿越积越多，3-7天后，功能微生物经过活化定殖，呈几何级数大量繁殖（24小时理论繁殖数量可达2个！），开始利用粪尿等作营养源，将锯末粪尿混和物逐渐升温发酵，中心发酵层温度可达30-50℃或更高，表层温度长期稳定在20几度，基本形成恒温床。下层发酵完成后，锯末等垫料物会因发酵碳化逐渐变深变黑，发酵产物能作肥料或粗饲料，可分批清运出舍。如不需使用，则可长期不清运，时间一长还应补充垫料。</p>
                                <p>对于肉种鸡来说，早期体重达标除了公鸡有些困难外，母鸡比较容易，一般在管理到位的情况下，第一周就可以使体重达标，但鸡只具有较强的择食性，如果一次性加料过多，鸡只一吃饱后就开始挑食，造成严重的撒料现象，导致体重达标往往是付出“更多代价”换来的，所以育雏中期至限饲阶段采用间隔喂料法，将采食时间相对集中，目的就是为了减少撒料浪费，方法是将全天的喂料量分次投给，第一次可以适当的多一些，供鸡只采食1个小时以上，采食完后暂时停料，让鸡只饮水或自由活动，使其有一个短暂饥饿的过程，等待下次加料。这样每次喂料后鸡只都会积极采食，并相对加快采食速度，减少因择食造成的撒料浪费，具体上料时间和停料间隔可根据现场实际情况而定。</p>
                            </div>`,
                    imgsrc: [require("@/assets/chicken2.jpg"),],
                    labels: ['饲养', '科普', '学习'],
                },
            ],
            // 导航栏文字
            navigations: [
                ['种植物', '水稻', '花生', '番薯'],
                ['蔬菜', '生菜', '花菜', '芹菜'],
                ['水果', '香蕉', '苹果', '榴莲'],
                ['家禽', '鸡', '鸭', '鹅'],
                ['鱼类', '草鱼', '鲈鱼', '桂花鱼'],
                ['瓜类', '冬瓜', '南瓜', '西瓜'],
            ]
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
                    // this.inputSearch = '';
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
            this.inputSearch = '';
            this.mainHide = true;
        },
        // 点击更多展开导航
        moreNav() {
            this.navHide = !this.navHide;
        },
        // 点击段落跳转并传递文章内容对象
        toArticlecontent(obj) {
            this.$router.push({
                name: '文章内容',
                params: {
                    obj: obj,
                }
            })
        },
        // 点击导航上的分类跳转，并传递分类对象
        toClassify(obj) {
            this.$router.push({
                name: '分类',
                params: {
                    obj: obj,
                }
            })
        }
    },
}
</script>

<style scoped>
    .container {
        background-color: #f4f4f4;
        color:#000000;
        min-height:100%;
        font:Helvetica Neue,Helvetica,PingFang SC,Tahoma,Arial,sans-serif;
    }
    .containerMian {
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
    .elMenu {
        display:inline-block;
        width: 16.5%;
        background-color: #e6eaf3;
        font-weight: bold;
        color: #9a979a;  
    }
    .el-menu-item:hover {
        font-weight: bold;
    }
    .mainHits {
        float:left;
        margin-bottom:20px;
        background-color:#3ebf7c;
        color: #ffffff;
        padding: 5px 50px;
        text-align: center;
        font-weight: 400;
        line-height: 1.5;
    }
    .mainHits>p {
        margin-bottom: 0;
    }
    .mainTime {
        float:right;
        margin-bottom:20px;
        background-color:#3ebf7c;
        color: #ffffff;
        padding: 5px 50px;
        text-align: center;
        font-weight: 400;
        line-height: 1.5;
    }
    .mainTime>p {
        margin-bottom: 0;
    }
</style>