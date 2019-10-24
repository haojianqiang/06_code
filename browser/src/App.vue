<template>
  <div id="app">
    <!-- 路由视图 -->
    <router-view/>
  </div>
</template>

<script>
// import utils from "@/utils";
export default {
  name: "app",
  components: {},
  methods(){
  },
  created() {
    // 在页面刷新前将Vuex里的信息缓存至SessionStorage中
    window.addEventListener("beforeunload", () => {
      this.$store.state.nim = null;
      sessionStorage.setItem("state", JSON.stringify(this.$store.state));
    });
    // 在页面加载前读取SessionStorage中的信息(router.beforeEach执行更快)
    let sessionStore = sessionStorage.getItem("state");
    if (sessionStore) {
      this.$store.replaceState(JSON.parse(sessionStore));
      sessionStorage.removeItem("state");
      let loginInfo = this.$store.state.user;
      this.$store.dispatch("initNimSDK", loginInfo);
    }
  },
  mounted() {}
};
</script>

<style>
#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  /* text-align: center;
  color: #2c3e50; */
}
body {
  margin: 0;
  overflow: hidden;
}
    .el-form-item{
        margin-bottom: 0px;
    }
    .el-form-item__error {
        color: #F56C6C;
        font-size: 12px;
        line-height: 1;
        padding-top: 4px;
        position: inherit!important;
        padding-bottom: 6px!important;
        top: 100%;
        left: 0;
    }
</style>

