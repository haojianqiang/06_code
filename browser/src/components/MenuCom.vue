<template>
  <div class="left-menu">
    <div class="menu-title-group"></div>

    <el-menu
      default-active="2"
      background-color="#F4F4F4"
      text-color="#000"
      active-text-color="#3ebf7c"
      class="el-menu-vertical-demo"
      unique-opened="true"
      router
    >
      <el-menu-item index="/main">
        <i class="el-icon-s-home"></i>
        <span slot="title">首页</span>
      </el-menu-item>
      <el-submenu index="1">
        <template slot="title">
          <i class="el-icon-s-cooperation"></i>
          <span>生产配置</span>
        </template>
        <el-menu-item index="/farm">鸡场管理</el-menu-item>
        <el-menu-item index="/house">鸡棚管理</el-menu-item>
        <el-menu-item index="/chicks">鸡苗登记</el-menu-item>
        <el-menu-item index="/chicksDistribution">鸡苗分配</el-menu-item>
        <el-menu-item index="/feed">饲料登记</el-menu-item>
        <el-menu-item index="/medicine">药物登记</el-menu-item>
        <el-menu-item index="/vaccine">疫苗登记</el-menu-item>
      </el-submenu>
      <el-submenu index="2">
        <template slot="title">
          <i class="el-icon-s-management"></i>
          <span>生产过程管理</span>
        </template>
        <el-menu-item index="/feeding">喂养管理</el-menu-item>
        <el-menu-item index="/prevention">禽病防治</el-menu-item>
        <el-menu-item index="/dailyRecord">日常记录</el-menu-item>
        <el-menu-item index="/deathElimination">淘汰记录</el-menu-item>
      </el-submenu>
      <el-submenu index="3">
        <template slot="title">
          <i class="el-icon-video-camera-solid"></i>
          <span>监控预警</span>
        </template>
        <el-menu-item index="/houseMonitor">鸡棚监控信息</el-menu-item>
        <el-menu-item index="/playBack">鸡棚监控回放</el-menu-item>
        <el-menu-item index="/alertSettings">预警设置</el-menu-item>
        <el-menu-item index="/warningThreshold">预警阈值设置</el-menu-item>
        <el-menu-item index="/warningNotice">预警通知设置</el-menu-item>
        <el-menu-item index="/warningContent">预警内容设置</el-menu-item>
      </el-submenu>
      <el-menu-item index="/saleRecord">
              <i class="el-icon-notebook-1"></i>
              <span slot="title">成交数据管理</span>
      </el-menu-item>
      <el-menu-item index="/origin">
        <i class="el-icon-search"></i>
        <span slot="title">溯源</span>
      </el-menu-item>

      <!-- 农技服务 -->
      <el-submenu index="4">
        <template slot="title">
          <i class="el-icon-s-shop"></i>
          <span>农技服务</span>
        </template>
        <el-menu-item index="/dataBank">资料库</el-menu-item>
        <el-menu-item index="/onlineService">线上服务</el-menu-item>
        <el-menu-item index="/offlineBooking">线下预约</el-menu-item>
        <el-menu-item index="/feedBack">农户反馈</el-menu-item>
      </el-submenu>
     <!-- <el-menu-item index="/main2">
        <i class="el-icon-search"></i>
        <span slot="title">测试首页</span>
      </el-menu-item>-->
    </el-menu>

    <!-- <el-menu class="menu-nav" :default-active="$route.path" router>
      <el-menu-item class="nav-item-parent" index="/main">
        &lt;!&ndash; <i class="el-icon-menu"></i> &ndash;&gt;
        <span slot="title">首页</span>
      </el-menu-item>

      &lt;!&ndash; 渲染菜单:仅适用于父子级别的菜单 &ndash;&gt;
      <template v-for="value1 in navs">
        <template v-if="value1.parentId == 0">
          &lt;!&ndash; 一级菜单 &ndash;&gt;
          <el-submenu class="nav-item-parent" :index="value1.id + ''" :key="value1.id">
            <template slot="title">
              <span>{{value1.name}}</span>
            </template>

            <template v-for="value2 in navs">
              &lt;!&ndash; 二级菜单,无子菜单 &ndash;&gt;
              <template
                v-if="value1.id == value2.parentId && value2.grandparentId == null && value2.greatgrandparentId == null"
              >
                <el-menu-item :index="value2.url" v-if="value2.url!=null" :key="value2.id">
                  <span slot="title">{{value2.name}}</span>
                </el-menu-item>
              </template>

              &lt;!&ndash; 二级菜单,有子菜单 &ndash;&gt;
              <template v-if="value1.id == value2.parentId && value2.url == null">
                <el-submenu :key="value2.id" :index="value2.id+''">
                  <span slot="title">{{value2.name}}</span>
                  &lt;!&ndash; 三级菜单 &ndash;&gt;
                  <template v-for="value3 in navs">
                    <template
                      v-if="value2.id == value3.grandparentId && value3.greatgrandparentId == null"
                    >
                      <el-menu-item
                        v-if="value3.url!=null"
                        :index="value3.url"
                        :key="value3.id"
                      >{{value3.name}}</el-menu-item>
                    </template>

                    <template v-if="value2.id == value3.parentId && value3.url == null">
                      <el-submenu :index="value3.id+''">
                        <span slot="title">{{value3.name}}</span>
                        &lt;!&ndash; 四级菜单 &ndash;&gt;
                        <template v-for="value4 in navs">
                          <el-menu-item
                            v-if="value4.greatgrandparentId == value3.id"
                            :index="value4.url"
                            :key="value4.id"
                          >{{value4.name}}</el-menu-item>
                        </template>
                      </el-submenu>
                    </template>
                  </template>
                </el-submenu>
              </template>
            </template>
          </el-submenu>
        </template>
      </template>
    </el-menu>-->
  </div>
</template>
<script>
export default {
  name: "MenuCom",
  data() {
    return {
      navs: null
    };
  },
  methods: {
    getNav(moduleId) {
      // 发送请求获取当前Module的导航菜单
      var params = new URLSearchParams();
      params.append("moduleId", moduleId);

      this.$http({
        method: "post",
        url: "/web-server/system/nav/nav.json?action=queryByUserAndModule",
        data: params
      })
        .then(res => {
          // console.log(res.data);
          if (res.data.ok) {
            this.navs = res.data.data;
          }
        })
        .catch(err => {
          console.log(err);
        });
    }
  },
  created() {
    // 页面初始化操作
    // let moduleId =
    //   this.$store.state.moduleId == null ? null : this.$store.state.moduleId;
    // this.getNav(moduleId);
  },
  computed: {
    // 获取当前选中的Module
    getCurModule() {
      return this.$store.state.moduleId;
    }
  },
  watch: {
    // 监听Module的变化
    getCurModule(status) {
      console.log(status);
      this.getNav(status);
    }
  }
};
</script>
<style scoped>
.left-menu {
  width: 240px;
  max-width: 240px;
  height: 100%;
  overflow: hidden;
  background-color: #F4F4F4;
}

.el-submenu .el-menu-item {
  height: 38px;
  line-height: 38px;
  border-bottom: 0px solid #e5e5e5;
}

.el-menu-item.is-active {
  background-color: white !important;
}

.menu-nav {
  height: 100%;
  overflow: auto;
}
.menu-title-group {
  /*background: url(../assets/image/nav-top.png);*/
  width: 100%;
  height: 38px;
  position: relative;
  background-size: 242px auto;
  border-bottom: 2px solid #e5e5e5;
}
.menu-title-group .menu-title {
  position: absolute;
  top: 10px;
  left: 44px;
  color: #ff5a00;
  font-weight: 700;
}
.menu-title-group .menu-icon {
  display: inline-block;
  width: 24px;
  height: 24px;
  background: url(../assets/image/more.png);
  background-size: 100% 100%;
  position: absolute;
  right: 10px;
  top: 5px;
}
.menu-nav .nav-item-parent {
  background: url(../assets/image/icon6.png);
}
</style>



