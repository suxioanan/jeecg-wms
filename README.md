# 物流管理系统
### 2021/3/30 更新
此次更新内容为：实现了在添加用户的情况下给用户输入的邮箱发送邮件功能，发送邮件功能通过 qq邮箱的 smtp 服务进行发送，实现思路：https://blog.csdn.net/qq_43647359/article/details/104638599

### 2021/3/8 更新
#### 基础开发环境：由于有小伙伴在运行项目时版本号不一致产生的各种问题，这里可以统一下版本号。
- JDK: 1.8
- Maven: 3.5+
- MySql: 5.7+
- Redis: 3.2 +
- Node Js: 10.0 +
- Npm: 5.6.0+
- Yarn: 1.21.1+
### IDE插件 (lombok 插件必装)

<hr/>

### 2021/3/3 更新
`713091413` 物流项目讨论交流群,有问题的小伙伴可以加此群哦！！！

<hr/>

# 介绍
基于Jeecg-boot开发的物流仓储系统，涵盖模块：用户管理、车辆管理、计划管理、仓库管理、库存管理、财务管理、统计报表、系统管理等模块组成

# 软件架构
- jeecg-boot-master 后台项目
- cable.sql 后台管理系统数据库脚本

# 所用技术
- 此系统基于 Jeecg-boot 为脚手架开发的PRD管理系统
- 后端技术：SpringBoot 2.1.3 + Shiro 1.4.0 + Redis + Mysql 5.7 + MyBatis-Plus 3.1.2 + Jwt 3.7.0 + Swagger-ui
- 前端技术：Vue + Ant-design-vue + Webpack
- 其他技术：Druid(数据库连接池)、Logback(日志工具)、poi(Excel工具)、Quartz(定时任务)、lombok(简化代码)
- 项目构建：Maven3.5+、JDK1.8+

# 项目所需软件下载路径及jeecg文档说明
- JeecgBoot官方文档 [http://jeecg-boot.mydoc.io/](http://jeecg-boot.mydoc.io/)

> # 新手必看启动教学
# 1. 数据库配置
## 首先在本地创建 cable 数据库，选择好字符集编码
![输入图片说明](https://images.gitee.com/uploads/images/2020/1123/134217_192e4886_5459645.jpeg "1606109571(1).jpg")
## 然后在创建好的 cable 数据库下执行 cable.sql 脚本即可
![输入图片说明](https://images.gitee.com/uploads/images/2020/1123/134333_0de565fa_5459645.jpeg "1606109947(1).jpg")

<hr/>

# 2. 后端配置
## 进入 IDEA 工具后设置 Maven 依赖下载设置
更改自己的 Maven 安装路径，用来下载项目所需的 jar 包
![](https://images.gitee.com/uploads/images/2021/0314/191715_99d8e0a2_5459645.png "屏幕截图.png")
## 选择后台项目的启动环境 -> dev[开发环境] 或者 prod[生产环境]
![](https://images.gitee.com/uploads/images/2021/0314/191434_349c2297_5459645.png "屏幕截图.png")
## 然后更改对应开发环境的配置文件，如 application-dev.yml 文件
### 配置项目启动端口号
![](https://images.gitee.com/uploads/images/2021/0314/191506_e32cd887_5459645.png "屏幕截图.png")
### 配置数据库连接信息
![](https://images.gitee.com/uploads/images/2021/0314/191350_24bad6c3_5459645.png "屏幕截图.png")
### 配置 redis 连接信息
![](https://images.gitee.com/uploads/images/2021/0314/191546_fee76fd1_5459645.png "屏幕截图.png")
### 配置 jeecg 专用配置文件上传路径
![](https://images.gitee.com/uploads/images/2021/0314/191624_e804f2fe_5459645.png "屏幕截图.png")
## 找到 JeecgApplication 启动类启动项目即可
## 通过访问 `http://localhost:8080/jeecg-boot/` 可以查看后台 API 接口文档
![](https://images.gitee.com/uploads/images/2020/0908/164142_770af197_5459645.png "屏幕截图.png")
<hr>

# 3. 前端配置
## 前端项目使用 VsCode 工具打开，在控制台执行 `npm install` 命令下载所需依赖
![输入图片说明](https://images.gitee.com/uploads/images/2021/0425/113958_905f5575_5459645.png "屏幕截图.png")
## 配置 index.html 页面的全局配置 -> 指定后台路径
![](https://images.gitee.com/uploads/images/2021/0314/191115_49a9c0d0_5459645.png "屏幕截图.png")
## 配置项目根目录下的 vue.config.js 文件，指定后台路径,建立前后端对接
![](https://images.gitee.com/uploads/images/2021/0314/191229_4445da80_5459645.png "屏幕截图.png")
## 最后配置完成后，需要前端后端同时启动才能访问项目
前端通过 `npm run serve` 命令启动
![输入图片说明](https://images.gitee.com/uploads/images/2021/0425/114230_d4a476cb_5459645.png "屏幕截图.png")

### 项目截图
### 1. 登录界面
![](https://images.gitee.com/uploads/images/2021/0314/190847_08ff48b0_5459645.png "屏幕截图.png")
### 2. 首页
![](https://images.gitee.com/uploads/images/2021/0314/191800_aba16a07_5459645.png "屏幕截图.png")
### 3. 系统设置 - 可以更改系统主体颜色设置等等
![](https://images.gitee.com/uploads/images/2021/0314/191851_8d10569a_5459645.png "屏幕截图.png")
### 4. 员工管理模块 
![](https://images.gitee.com/uploads/images/2021/0314/191915_23526243_5459645.png "屏幕截图.png")
### 5. 角色授权 - 通过分配给用户不同的角色，可访问不同的菜单列表
![](https://images.gitee.com/uploads/images/2021/0314/191934_11a029f4_5459645.png "屏幕截图.png")
### 6. 计划导出 - 通过 excelPoi 技术实现信息导出功能
![](https://images.gitee.com/uploads/images/2021/0314/192000_cee42893_5459645.png "屏幕截图.png")
