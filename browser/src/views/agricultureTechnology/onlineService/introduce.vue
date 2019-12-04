<template>
    <!-- 个人简介 -->
    <div class="container">
        <!-- 头部信息 -->
        <div class="header">
            <el-row :gutter="10">
                <el-col :span="6"><div class="elCol"></div></el-col>
                <el-col :span="12"><h4 class="center">{{ expert.title }}</h4></el-col>
                <el-col :span="6"><div class="elCol"></div></el-col>
            </el-row>
        </div>

        <!-- 主体内容 -->
        <div class="main">
            <el-row :gutter="5">
                <!-- 左边照片 -->
                <el-col :span="12">
                    <img :src="expert.img">
                </el-col>

                <!-- 右边信息 -->
                <el-col :span="12">
                    <div>
                        <h3>{{ expert.name }}</h3>
                        <p>{{ expert.job }}</p>

                        <h3>任职</h3>
                        <p>{{ expert.post }}</p>

                        <h3>成就</h3>
                        <p>{{ expert.achievement }}</p>

                        <h3>擅长</h3>
                        <p>{{ expert.Speciality }}</p>
                    </div>
                </el-col>
            </el-row>
        </div>

        <!-- 底部 -->
        <div>
            <el-row>
                <el-col class="elCol"></el-col>
            </el-row>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            expert: {
                title: '届国家最高科学技术奖得主、杂交水稻之父',
                img: 'http://img2.imgtn.bdimg.com/it/u=2148025029,3379649937&fm=26&gp=0.jpg',
                name: '袁隆平',
                job: '杂交水稻专家',
                post: '中国工程院院士',
                achievement: '发明“三系法”籼型杂交水稻，成功研究出“两系法”杂交水稻，创建了超级杂交稻技术体系。并提出并实施“种三产四丰产工程”，运用超级杂交稻的技术成果，出版中、英文专著6部，发表论文60余篇。',
                Speciality: '作物栽培、植物保护、配方施肥、农业经济效益分析、市场销售管理等。例如，病虫草害防治专家系统是针对作物不同时期出现的各种症状和不同环境条件，诊断可能出现的病虫草灾害，提出有效的防治方法。',
            }
        }
    }
}
</script>

<style scoped>
.container {
    font:Helvetica Neue,Helvetica,PingFang SC,Tahoma,Arial,sans-serif;
    font-size: 14px;
    padding: 25px 3%;
}
.header {
    margin-bottom: 28px;
}
.elCol {
    min-height: 26px;
    background-color: #0066CC;
}
.center {
    text-align: center
}
.left {
    float: left;
}
.main {
    padding: 5px;
    margin-bottom: 28px;
}
.main img {
    width: 400px;
    height: 400px;
    border-radius: 50%;
}
</style>