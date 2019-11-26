<template>
    <div>
        <!-- 头部信息 -->
        <div class="header">
            <h2>{{ this.$route.params.obj.title }}</h2>
            <p>
                <span>{{ this.$route.params.obj.pubdate }}</span>
                <span>阅读数：{{ this.$route.params.obj.hits }}</span>
                <span>关键字：<span v-for="(label, index) in this.$route.params.obj.labels" :key="index">{{ label }}</span></span>
            </p>
        </div>

        <hr style="margin: 0;">

        <!-- 内容 -->
        <div class="contentP" id="contentP" ref="contentP" :class="{ contentPHeight:contentStatus }">
            <div v-html="this.$route.params.obj.content"></div>
        </div>

        <!-- 阅读全文按钮 -->
        <div class="contentPToggle" v-if="contentStatus" @click="contentStatus = !contentStatus">
            <a>展开阅读全文<i class="el-icon-arrow-down"></i></a>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            contentStatus: false,
            contentPHeight: 10,
            curHeight: 0,
        }
    },
    mounted() {
        setTimeout( ()=> {
            this.contentToggle();
        }, 100)
    },
    methods: {
        contentToggle() {
            this.curHeight = this.$refs.contentP.offsetHeight;
            if(this.curHeight > this.contentPHeight) {
                this.contentStatus = true;
            }else{
                this.contentStatus = false;
            }
        },
    },
}
</script>

<style scoped>
    .header {
        padding:2% 0 1% 2%;
    }
    span {
        font-size:14px;
        color:#999999;
        margin-right: 20px;
    }
    .contentP {
        margin: 2%;
        overflow: hidden;
    }
    .contentPHeight {
        height: 300px;
    }
    .contentPToggle {
        height: 30px;
        width: 30%;
        margin: 10px auto;
        line-height: 30px;
        text-align: center;
        color: red;
        border: 1px solid red;
        border-radius: 5px;     
    }
    .contentPToggle:hover {
        cursor: pointer;
    }
    .contentP /deep/ p {
        text-indent: 2em;
        line-height: 160%;
    }
    .contentP /deep/ img {
        width: 35%;
        margin: 2% 5%;
    }
    .contentP /deep/ a {
        display: block;
        text-align: center;
    }
</style>