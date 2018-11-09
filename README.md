# Reading

## 简介
 Reading: Reading是一款基于WanAndroid OpenApi开发的阅读类工具，如果你是一个热衷于Android开发者，那么这款软件能帮助你阅读精品Android文章。同时Reading中还包含"英文单词"、"账号本子"、"天气"、"查单词"等小工具。项目基于"Kotlin+MVP"架构开发，风格大概也许属于Material Desgin原质化风格，包含主题颜色切换、百变Logo、等功能。在此感谢WanAndroid的OpenApi,以及其它开源项目的贡献。
 
## ScreenShot

<img width="220" height=“400” src="https://github.com/Hankkin/Reading/blob/develop-1.0.0/sceenshot/reading.gif"></img>



<img width="173" height=“274” src="https://github.com/Hankkin/Reading/blob/develop-1.0.0/sceenshot/1.jpeg"></img>
<img width="173" height=“274” src="https://github.com/Hankkin/Reading/blob/develop-1.0.0/sceenshot/2.jpeg"></img>
<img width="173" height=“274” src="https://github.com/Hankkin/Reading/blob/develop-1.0.0/sceenshot/3.jpeg"></img>
<img width="173" height=“274” src="https://github.com/Hankkin/Reading/blob/develop-1.0.0/sceenshot/4.jpeg"></img>
<img width="173" height=“274” src="https://github.com/Hankkin/Reading/blob/develop-1.0.0/sceenshot/5.jpeg"></img>
<img width="173" height=“274” src="https://github.com/Hankkin/Reading/blob/develop-1.0.0/sceenshot/6.jpeg"></img>
<img width="173" height=“274” src="https://github.com/Hankkin/Reading/blob/develop-1.0.0/sceenshot/7.jpeg"></img>
<img width="173" height=“274” src="http://lc-2hxprqvs.cn-n1.lcfile.com/4816d3bcff1402f46181.jpeg"></img>
<img width="173" height=“274” src="https://github.com/Hankkin/Reading/blob/develop-1.0.0/sceenshot/9.jpeg"></img>
<img width="173" height=“274” src="https://github.com/Hankkin/Reading/blob/develop-1.0.0/sceenshot/11.jpeg"></img>
<img width="173" height=“274” src="https://github.com/Hankkin/Reading/blob/develop-1.0.0/sceenshot/12.jpeg"></img>
<img width="173" height=“274” src="https://github.com/Hankkin/Reading/blob/develop-1.0.0/sceenshot/13.jpeg"></img>
<img width="173" height=“274” src="https://github.com/Hankkin/Reading/blob/develop-1.0.0/sceenshot/14.jpeg"></img>
<img width="173" height=“274” src="https://github.com/Hankkin/Reading/blob/develop-1.0.0/sceenshot/15.jpeg"></img>
<img width="173" height=“274” src="https://github.com/Hankkin/Reading/blob/develop-1.0.0/sceenshot/16.jpeg"></img>
<img width="173" height=“274” src="http://lc-2hxprqvs.cn-n1.lcfile.com/144d71ae6eeb0bd3d873.jpeg"></img>

## DownLoad

[https://fir.im/Reading](https://fir.im/Reading)


<img width="400" height=“400” src="https://github.com/Hankkin/Reading/blob/develop-1.0.0/sceenshot/fir_download.png"></img>


## Function Tips

#### 1.首页
- WanAndroid API实现**Android文章列表**、**热门搜索文章**、**项目列表**
- **刷新、置顶、分享文章**、**搜索文章**、**搜索历史**
- **收藏列表**

#### 2.工作台
- **扫一扫** 快速扫一扫、从相册选择扫一扫
- **查询单词** 使用有道SDK查询单词 加入单词本
- **单词本** 单词本记录功能 标记重点
- **账号本子** 新建账号 保存本地 记录你的常用账号密码
- **每日推荐** 标记重点的单词推荐在首页

#### 3.我的
- **登录注册** 使用WanAndroid Api进行账号注册登录
- **TODO List** WanAndroid API 待办事项 （待开发）
- **设置** 个性换肤、账号锁功能、本地数据备份、百变Logo、清除缓存

## Skill Tips

- **项目架构** 使用MVP架构模式搭建，封装MvpActivity、MvpFragment、BasePresenter、BaseView。P层绑定V层生命周期 。具体可见[**MVP包**](https://github.com/Hankkin/Reading/tree/develop-1.0.0/app/src/main/java/com/hankkin/reading/mvp)

- **网络层** 使用Retrofit+RxJava进行网络请求，封装公共Rerofit的HttpClientUtils(创建RetrofitBuilder、OkHttpClient、addHeader、拦截器、Cookie、https等)、工厂模式创建不同BaseUrl的网络对象。具体可见[**http包**](https://github.com/Hankkin/Reading/tree/develop-1.0.0/app/src/main/java/com/hankkin/reading/http)

- **缓存** 使用GreenDao数据库缓存本地数据，并提供数据备份、还原功能。同时结合MVP架构，将数据库操作作为Dao层，Dao层封装Protocol，利用工厂模式包装GreenDao产生的xxxBeanDao

- **RecycleAdapter** 封装RecycleView Adapter 配合SwipeRefreshLayout支持下拉刷新、上拉加载功能，不满足一屏数据

- **RxBusTools** 封装RxBusTools事件分发 封装BaseActivity、BaseFragment统一处理注册监听事件

- **StatuBarUtils沉浸式** 使用**写代码的猴子**的[StatusBarUtil 状态栏工具类（实现沉浸式状态栏/变色状态栏）](https://jaeger.itscoder.com/android/2016/03/27/statusbar-util.html)适配沉浸式通知栏

- **主题切换** B站开源框架[MagicaSakura](https://github.com/Bilibili/MagicaSakura)

- **百变Logo** *Android群英传*中利用<activity-alias>根据主题颜色动态切换Icon，部分机型可能会出现crash，目前还没找到更好的解决方案，所以提供了开启关闭的开关。如果有大神可以在Issues提供。

- **本地数据备份还原** 通过读取数据库数据写入txt文件，读取txt文件数据还原实现，记录备份还原的时间戳作为版本号

- **单词翻译** 使用有道SDK翻译单词

- **FloatActionButton** 实现首页悬浮按钮 点击展开

- **HorizontalScrollView+ViewPager** 首页仿网易云音乐滑动

- **BottomSheet** 主题切换底部弹出框

- **手势解锁** 参考[Github-ihsg/PatternLocker](https://github.com/ihsg/PatternLocker)

## Version

### V3.1.0

- [新增] 干货Tab

- [优化] 优化UI排版，功能删减合并

- [修复] 部分bug修复

### V3.0.0

- [新增] 微信公众号

- [优化] 优化UI排版，功能删减合并

- [修复] 部分bug修复

### V2.4.0
  
- [优化] 优化业务需求

- [修复] 部分bug修复

### V2.3.0
  
- [新增] 新增已完成事项查看更多

- [修复] 部分bug修复

### V2.2.0
  
- [优化] 切换PageLayout

- [修复] 部分bug修复

### V2.1.1
 
- [新增] 应用更新功能
 
- [优化] webview

- [修复] 部分bug修复

### V2.0.0
 
- [新增] TODO模块
 
- [优化] 启动引导页

- [修复] 部分bug修复


### V1.1.0
 
- [新增] 仿网易云音乐切换主题
 
- [优化] 文字网页加载进度

- [修复] 添加youdaoSDK so文件

- [兼容] 兼容部分机型 

### V1.0.2
 
- [优化] mvp、部分utils提到library

- [修复] 修复bug

### V1.0.1
 
- [优化] 完善数据备份还原清空数据功能

- [优化] 完善账号锁功能

- [完善] 新增版本号

### V1.0.0 
- 首次提交 部分功能不完善


## License
> Copyright (C) 2018 Hankkin

> Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

> [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

> Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
