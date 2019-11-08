<template>
  <div class="wrap">
    <div class="left">
      <h2>息烽县数字化肉鸡养殖云服务平台管理系统</h2>
    </div>
    <ul class="layui-nav right nav-R" lay-filter style="background-color: #3ebf7c;">
      <li class="layui-nav-item">
        <span style="font-size:10px;">{{user.username}},欢迎您!</span>
      </li>
      <li class="layui-nav-item">
        <a href="javascript:void(0)" style="font-size: 12px;">
          <i style="margin-right: 4px;">
            <img src="../assets/image/admin.png">
          </i>
          用户信息
        </a>
      </li>
      <li v-on:click="logout()" class="layui-nav-item to-index">
        <a href="javascript:void(0)" style="font-size: 12px;">
          <i style="margin-right: 4px;">
            <img src="../assets/image/exit.png">
          </i>退出系统
        </a>
      </li>
    </ul>
  </div>
</template>
<script>
export default {
  name: "Header",
  data() {
    return {
      user: {
        username:
          this.$store.state.user == null ? "" : this.$store.state.user.userName
      }
    };
  },
  methods: {
    logout() {
      this.$http({
        method: "post",
        url: "/login/login.json?action=logout",
      })
        .then(res => {
          // console.log(res);
          if (res.data.ok) {
            // 清空缓存
            this.$store.state.user = null;
            // 路由跳转
            this.$router.replace({
              path: "/login"
            });
          }
        })
        .catch(err => {});
    }
  }
};
</script>
<style scoped>
.wrap {
 /* background: url(../assets/image/icon-top.png) no-repeat;*/
  background-color: #3ebf7c;
  height: 86px;
  width: 100%;
}
.wrap .right {
  width: 400px;
  float: right;
}

.wrap .left {
    width: 800px;
   float: left;
   color: white;
   padding-top: 20px;
   padding-left: 30px;
 }

.nav-R {
  height: 27px;
  top: 28px;
  margin-right: 30px;
  border-radius: 4px;
}
.wrap .layui-nav-item {
  line-height: 27px;
  font-size: 10px !important;
}
</style>

