import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css';
import '@/assets/css/element.css';
import axios from './axios';
import Echarts from 'echarts';
import $ from 'jquery';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.js'
import moment from 'moment'

import VueQuillEditor from 'vue-quill-editor'
// require styles 引入样式
import 'quill/dist/quill.core.css' //通过语句下载好了的
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'

Vue.use(ElementUI);
Vue.use(VueQuillEditor)
Vue.config.productionTip = false;
Vue.prototype.$http=axios;
Vue.prototype.echarts = Echarts;
Vue.use(Echarts);
Vue.prototype.$ = $;

Vue.prototype.$moment = moment;//赋值使用
moment.locale('zh-cn');//需要汉化

// 路由拦截
router.beforeEach((to,from,next)=>{
  // console.log("router.beforeEach");
  if (to.meta.requireAuth){ // 判断该路由是否需要登陆权限
    let state = JSON.parse(sessionStorage.getItem("state"));
    let user = null;
    if (state!=null){
      user = state.user
    }
    if (store.state.user == null && user == null){
      next({
        path:'/login'
      });
    }else{
      next();
    }
  }else{
    next();
  }
});

new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app')
