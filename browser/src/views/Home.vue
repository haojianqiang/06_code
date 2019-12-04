<template>
<body>
  <!-- 头部 -->
  <header-com></header-com>

  <div class="menu-wrap">
    <!-- 左侧导航栏 -->
    <menu-com></menu-com>
  </div>

  <!-- 右侧内容展示区 -->
  <div class="app-wrap">
    <div class="app-wrap-group">
      <div class="tab-wrap">
        <!-- Tabs -->
        <el-tabs
          v-model="activeIndex"
          type="border-card"
          closable
          v-if="openTab.length"
          @tab-click="tabClick"
          @tab-remove="tabRemove"
        >
          <el-tab-pane
            v-for="item in openTab"
            :key="item.name"
            :label="item.name"
            :name="item.route"
          ></el-tab-pane>
        </el-tabs>
      </div>
      <div class="content-wrap">
        <!-- 内容展示 -->
        <!--<keep-alive>-->
        <router-view/>
        <!--</keep-alive>-->
      </div>
    </div>
  </div>

  <!-- 底部 -->
  <footer-com></footer-com>
</body>
</template>

<script>
import HeaderCom from "@/components/HeaderCom.vue";
import FooterCom from "@/components/FooterCom.vue";
import MenuCom from "@/components/MenuCom.vue";
// import ModuleCom from "@/components/ModuleCom.vue";
export default {
  name: "Home",
  components: {
    HeaderCom,
    FooterCom,
    MenuCom,
    // ModuleCom
  },
  data() {
    return {};
  },
  methods: {
    // 点击Tabs,切换相应路由
    tabClick() {
      this.$router.push({ path: this.activeIndex });
    },
    // 移除Tabs
      tabRemove(tabIndex) {
          // console.log(tabIndex);
          // 首页不删
          if (tabIndex == "/" || tabIndex == "/index") {
              return;
          }
          this.$store.commit("deleteTabs", tabIndex);
          if (this.activeIndex === tabIndex) {
              // 设置当前激活的路由
              this.$store.commit(
                  "setActiveIndex",
                  this.openTab[this.openTab.length - 1].route
              );
              this.$router.push({ path: this.activeIndex });
          } else {
              // this.$router.push({ path: "/" });
          }
      },
  },
  mounted() {
    //默认展示首页
    let openTab = this.$store.state.openTab;
    if (openTab.length == 0) {
      this.$store.commit("addTabs", { route: "/index", name: "首页" });
    }
  },
  computed: {
    openTab() {
      return this.$store.state.openTab;
    },
    activeIndex: {
      get() {
        return this.$store.state.activeIndex;
      },
      set(val) {
        this.$store.commit("setActiveIndex", val);
      }
    }
  },
  watch: {
    $route(to) {
      let flag = false;
      for (let item of this.openTab) {
        if (item.name === to.name) {
          // Tabs页面已存在
          this.$store.commit("setActiveIndex", to.path);
          flag = true;
          break;
        }
      }
      // 不存在Tabs页面,则新增
      if (!flag) {
        this.$store.commit("addTabs", { route: to.path, name: to.name });
        this.$store.commit("setActiveIndex", to.path);
      }
    }
  }
};
</script>

<style scoped>
body {
  background-color: #3ebf7c;
  height: 100vh;
  width: 100%;
}
.module-wrap {
  border-top: 8px solid #3ebf7c;
}
.menu-wrap {
  background-color: #F4F4F4;
  position: absolute;
  left: 0px;
  z-index: 1;
  top: 85px;
  bottom: 20px;
  border-top-right-radius: 0px;
  border-right: 0px solid #e5e5e5;
}
.app-wrap {
  position: absolute;
  left: 240px;
  top: 85px;
  right: 0px;
  bottom: 20px;
  overflow: hidden;
  z-index: 1;
  border-left: 0px solid #3ebf7c;
  border-bottom: 0px solid #3ebf7c;
  background: #ffffff;
}
.app-wrap-group {
  width: 100%;
  height: 100%;
  position: relative;
}
.tab-wrap{
  height: 35px;
}
.content-wrap {
  border: 1px solid #dcdff7;
  border-top: none;
  background: #ffffff;
  overflow: auto;
  width: 100%;
  min-width: 100%;
  position: absolute;
  bottom: 0px;
  top: 35px;
  border-top: 2px solid #3ebf7c;
}
</style>
<style>
.el-tabs.el-tabs--border-card {
  box-shadow: none;
  border-bottom: none;
}
</style>
