import axios from 'axios'
import router from './router';
import store from './store';

axios.defaults.baseURL = '/api';
axios.defaults.withCredentials = true;

// http请求拦截器
axios.interceptors.request.use(config => {
    // console.log(config);
    return config;
}, error => {
    return Promise.reject(error);
})
// http响应拦截器
axios.interceptors.response.use(response => {
    // console.log(response);
    if (response.data.ok == false && response.data.msg == "用户未登陆") {
        // 清除缓存信息
        store.state.user = null;
        // 路由跳转
        router.replace({
            path: "/login"
        });
    }
    return response;
}, error => {
    return Promise.reject(error);
});
export default axios;