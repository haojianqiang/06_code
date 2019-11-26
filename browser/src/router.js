import Vue from "vue";
import Router from "vue-router";
import Login from "./views/Login.vue";
import Home from "./views/Home.vue";
import Main from "./views/Main.vue";

Vue.use(Router);
export default new Router({
  routes: [
    {
      path: "/login",
      name: "Login",
      component: Login,
      meta: {
        requireAuth: false
      }
    },
    {
      path: "/main",
      name: "Main",
      component: Main,
      meta: {
      requireAuth: true
          }
    },
    {
      path: "/technology",
      name: "农技服务",
      component: () => import("@/views/service/technology/index"),
      meta: {
        requireAuth: false
      }
    },
    {
        path: "/",
        name: "Home",
        component: Home,
        redirect:"/login",
        meta: {
            requireAuth: true
        },
      children: [
        {
          path: "index",
          name: "首页",
            component: () => import("@/views/Index.vue"),
          meta: {
            requireAuth: true
          }
        },
        //生产配置
        {
          path: "farm",
          name: "鸡场管理",
          component: () => import("@/views/config/farm/index"),
          meta: {
            requireAuth: true
          }
        },
        {
          path: "house",
          name: "鸡棚管理",
          component: () => import("@/views/config/house/index"),
          meta: {
            requireAuth: true
          }
        },
        {
          path: "chicks",
          name: "鸡苗登记",
          component: () => import("@/views/config/chicks/index"),
          meta: {
            requireAuth: true
          }
        },
        {
          path: "chicksDistribution",
          name: "鸡苗分配",
          component: () => import("@/views/config/chicksDistribution/index"),
          meta: {
            requireAuth: true
          }
        },
        {
          path: "feed",
          name: "饲料登记",
          component: () => import("@/views/config/feed/index"),
          meta: {
            requireAuth: true
          }
        },
        {
          path: "medicine",
          name: "药物登记",
          component: () => import("@/views/config/medicine/index"),
          meta: {
            requireAuth: true
          }
        },
        {
          path: "vaccine",
          name: "疫苗登记",
          component: () => import("@/views/config/vaccine/index"),
          meta: {
            requireAuth: true
          }
        },
        //生产过程管理
        {
          path: "feeding",
          name: "喂养管理",
          component: () => import("@/views/process/feeding/index"),
          meta: {
            requireAuth: true
          }
        },
        {
          path: "prevention",
          name: "禽病防治",
          component: () => import("@/views/process/prevention/index"),
          meta: {
            requireAuth: true
          }
        },
          {
              path: "deathElimination",
              name: "淘汰记录",
              component: () => import("@/views/process/deathElimination/index"),
              meta: {
                  requireAuth: true
              }
          },
          {
              path: "dailyRecord",
              name: "日常记录",
              component: () => import("@/views/process/dailyRecord/index"),
              meta: {
                  requireAuth: true
              }
          },
        //监控预警管理
        {
          path: "houseMonitor",
          name: "鸡棚监控信息",
          component: () => import("@/views/monitor/houseMonitor/index"),
          meta: {
              requireAuth: true
          }
        },
          {
              path: "playBack",
              component: () => import("@/views/play/playContenRoot.vue"),
              meta: {
                  requireAuth: true
              },
              children: [
                  {
                      path: '',
                      redirect: '1'
                  },
                  {
                      path: ':page',
                      name:"设备列表",
                      component: () => import("@/views/play/deviceList.vue"),
                      props: true
                  }, {
                      path: 'channels/:devid/:page',
                      name:"通道列表",
                      component: () => import("@/views/play/channelList.vue"),
                      props: true
                  },
                  {
                      path: 'timebox/:devid/:channel/:day?',
                      name:"录像回放",
                      component:() => import("@/views/play/playbackTimebox.vue"),
                      props: true
                  }
              ]
          },
		{
          path: "alertSettings",
          name: "预警设置",
          component: () => import("@/views/monitor/alertSettings/index"),
          meta: {
              requireAuth: true
          }
        },
        {
            path: "warningThreshold",
            name: "预警阈值设置",
            component: () => import("@/views/monitor/warningThreshold/index"),
            meta: {
                requireAuth: true
            }
        },
        {
            path: "warningNotice",
            name: "预警通知设置",
            component: () => import("@/views/monitor/warningNotice/index"),
            meta: {
                requireAuth: true
            }
        },
        {
            path: "warningContent",
            name: "预警内容设置",
            component: () => import("@/views/monitor/warningContent/index"),
            meta: {
                requireAuth: true
            }
        },
        {
          path: "saleRecord",
          name: "成交数据管理",
          component: () => import("@/views/saleRecord/index"),
          meta: {
            requireAuth: true
          }
        },
        // 溯源
        {
          path: "origin",
          name: "溯源",
          component: () => import("@/views/origin/index"),
          meta: {
            requireAuth: true
          }
        },
        // 资料库
        {
          path: "dataBank",
          name: "资料库",
          component: () => import("@/views/agricultureTechnology/dataBank/index"),
          meta: {
            requireAuth: true
          }
        },
        // 文章内容
        {
          path: "articlecontent/:obj",
          name: "文章内容",
          component: () => import("@/views/agricultureTechnology/dataBank/articlecontent"),
          meta: {
            requireAuth: true
          }
        },
        // 分类
        {
          path: "classify/:obj",
          name: "分类",
          component: () => import("@/views/agricultureTechnology/dataBank/classify"),
          meta: {
            requireAuth: true
          }
        },
        {
          path: "dataBankManagement",
          name: "资料库管理",
          component: () => import("@/views/agricultureTechnology/dataBank/dataBankManagement"),
          meta: {
            requireAuth: true
          }
        },
        // 线上服务
        {
          path: "onlineservice",
          name: "线上服务",
          component: () => import("@/views/agricultureTechnology/onlineService/index"),
          meta: {
            requireAuth: true
          }
        },
        // 线下预约
        {
          path: "offlinebooking",
          name: "线下预约",
          component: () => import("@/views/agricultureTechnology/offlineBooking/index"),
          meta: {
            requireAuth: true
          }
        },
        // 农户反馈
        {
          path: "feedback",
          name: "农户反馈",
          component: () => import("@/views/agricultureTechnology/feedBack/index"),
          meta: {
            requireAuth: true
          }
        },
        
          // 测试首页
        /*  {
              path: "main2",
              name: "测试首页",
              component: () => import("@/views/Main2"),
              meta: {
                  requireAuth: true
              }
          }*/
      ]
    }
  ]
});
