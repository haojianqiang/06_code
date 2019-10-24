const path = require("path");
function resolve(dir) {
  return path.join(__dirname, dir);
}

module.exports = {
  publicPath: "./",
  outputDir: "dist", // 打包目录
  lintOnSave: true, // 保存时校验格式
  productionSourceMap: false, // 生产环境是否生成SourceMap
  devServer: {
    open: true,
    host: "0.0.0.0",
    port: 8088, // 服务端口
    https: false,
    hotOnly: false, // 热更新,webpack已实现,此处false即可
    proxy: {
      "/api": {
        target: "http://127.0.0.1:8082",
        changeOrigin: true, // 是否跨域
        pathRewrite: {
          "^/api": "/" // 规定请求地址以什么作为开头
        }
      }
    }, // 设置代理
    before: app => {}
  },
  configureWebpack: {
    resolve: {
      alias: {
        // 引用路径别名
        "@": resolve("src")
      }
    },
    // module: {
    //   rules: [
    //     {
    //       test: /\.js$/,
    //       loader: "babel-loader",
    //       include: [resolve("src"), resolve("test")],
    //       exclude: resolve("src/sdk"), // 将SDK文件排除
    //       query: {
    //         presets: [
    //           ["stage-0"],
    //           ["es2015", { loose: true, modules: "commonjs" }]
    //         ],
    //         cacheDirectory: true,
    //         plugins: [
    //           "add-module-exports",
    //           ["transform-runtime", { polyfill: true }]
    //         ]
    //       }
    //     }
    //   ]
    // }
  }
};
