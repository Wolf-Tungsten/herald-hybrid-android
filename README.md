# 小猴偷米 Hybrid App 安卓版 （herald-hybrid-android）

## 项目宗旨

* WebApp 快速迭代 + 原生 App 丰富API

* 对 herald-web 进行改造/封装

## 设计准则

* 人类的本质是复读机，Hybrid App 的本质是浏览器。

### 原生部分功能

* 接管身份认证和 token 保存，token通过 Java-Javascript 注入到 WebView 中

* 接管 ServiceWorker，利用 X5 内核缓存提高加载速度

* 原生实现 Tab、Router、以及滚动库实现的效果

### Web部分功能

* 主要 UI 由 Web 实现

* 业务数据的获取、展示、持久化

## 项目进度

[*] 原生 - 身份认证
[*] 原生 - Token 注入（通过 JavaScriptInterface ）
[*] 原生 -  tab 切换
[*] 原生 - 路由接管
[*] Web - api.js 改造
[*] Web - 主页面适配
[*] Web - 通知公告部分适配



