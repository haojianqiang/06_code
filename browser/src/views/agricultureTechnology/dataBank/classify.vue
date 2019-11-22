<template>
    <div style="background-color: #f4f4f4;color:#000000;min-height:100%;">

    <!-- 容器 -->
    <div style="padding:30px 80px;">
        <!-- 头部信息 -->
        <div class="header">
            <img src="@/assets/shuidao.jpg" style="height:100px;">
            <h1 style="display:inline-block;padding:2% 0 0 2%;">水稻</h1>

            <p>
                为您推荐：
                <span><el-link>水稻作物</el-link></span>
                <span><el-link>水稻品种</el-link></span>
                <span><el-link>水稻学家</el-link></span>
            </p>
        </div>

        <!-- 主体内容 -->
        <div style="margin:30px 0;">
            <!-- 内容 -->
            <databankslot>
                <div style="margin: 30px 0;padding: 10px;clear:both; background-color: #ffffff">
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
    </div>
    </div>
</template>

<script>
import databankslot from './databankslot'

export default {
    data() {
        return{

        }
    },
    methods: {

    },
    components: {
        databankslot,
    },
}
</script>

<style scoped>
    .header {
        min-height: 100px;
    }
    .header p {
        margin-top: 2%;
    }
    .header span {
        margin: 0 1%;
        font-size: 14px;
        text-decoration: underline;
        color: #CC0033;
    }
</style>