 <template>
    <div class="wrap">
      <div class="loginForm">
      <el-form ref="form" :model="loginForm" label-width="80px">
        <el-form-item label="">
          <el-input v-model="loginForm.account" placeholder="请输入账号" class="login-input"></el-input>
        </el-form-item>
        <el-form-item label="">
          <el-input type="password" v-model="loginForm.password" placeholder="请输入密码" class="login-input"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="login()">登陆</el-button>
        </el-form-item>
      </el-form>
      </div>
    </div>
  </template>


<script>
export default {
  name: "Login",
  data() {
    return {
      loginForm: {
        account: "admin",
        password: "123"
      },
      responseResult: []
    };
  },
  methods: {
    login() {
      var _this = this;
      if (this.loginForm.account == "") {
        return;
      }
      if (this.loginForm.password == "") {
        return;
      }
      // 校验
      var params = new URLSearchParams();
      params.append("account", this.loginForm.account);
      params.append("password", this.loginForm.password);

      // this.$router.replace({path: "/main"});

      // 发送请求
      this.$http({
        method: "post",
        url: "/login.json?action=login",
        data: params
      })
        .then(res => {
          // console.log(res);
          if (res.data.ok) {
            // 缓存数据
            _this.$store.state.user = {id:res.data.data.userId,userName:res.data.data.userName};
            _this.$store.state.modules = res.data.data.modules;
            _this.$store.state.moduleId = res.data.data.modules[0].moduleId;
            // console.log(_this.$store.state.modules)
            // 路由跳转
            this.$router.replace({
              path: "/main"
            });
          }else {
              this.$message.error("密码错误");
          }
        })
        .catch(err => {
          console.log(err);
        });

    },
    // 测试不通过
    error(){
      this.$message({
        message:'请求失败',
        type:'error'
      })
    },
  },
};
</script>
<style>
  .loginForm .el-input__inner {
    border: 1px solid #c5c5c5;
    border-radius: 20px;
    width: 400px;
  }

  .loginForm .el-button {
    border: 1px solid #c5c5c5;
    border-radius: 20px;
    background-color: #33E4C7;
    color: white;
    width: 400px;
  }
</style>
<style scoped>
  .wrap {
    background: url("../assets/image/login2.png") no-repeat;
    height: 100%;
    width: 100%;
    position: fixed;
    text-align: center;
    background-size: cover;
  }

  .loginForm {
    position: absolute;
    bottom: 20%;
    right: 15%;
  }

</style>



