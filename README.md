## 简博客后端

使用 SpringBoot + Mybatis 实现的后端项目，没有复杂的功能，没有使用高大上的技术，
作者本人也是新手小白，所以比较适合 Java Web 初学者学习使用。

## 背景

简博客是我 2020 年 3 月 开启的一个项目，整个项目采用前后端分离的模式进行开发，前端使用 webpack 与 node.js 环境进行开发，
后端使用 SpringBoot + MyBatis 进行开发，接口尽量实现 RESTFul 风格，旨在打造一个自己的完整的博客项目，供以后使用。如名字一样，
简博客的功能很简单，第一版要实现的功能有这些：
 - 博客预览
 - 博客归档（按照时间顺序）
 - 博客标签功能
 - 博客状态修改
 - 博客编辑与发布
 - 阅读量图表
 - 日志记录
 
## 安装与部署

在前之前，您需要在部署的的机器上安装好 Maven, MySQL8 和 JDK 11（JDK14 都正式发布了，不要抱着老版本不放手了）
按照下面的步骤进行：
- `git clone ...` 或者直接下载
- 修改配置文件`application-dev.yml`或者`application-prod.yml`中数据库连接参数（用户名，密码等）
- `mvn package`
- 运行（可以根据不同的配置文件运行在不同环境下）
    - `java -jar xxx.jar --spring.profiles.active=dev`（开发环境运行）
    - `java -jar xxx.jar --spring.profiles.active-prod`（生产环境运行）

## 后续工作

 - 增加管理员/博客导航/回收站功能
 - 优化 token 鉴权方式
 - 完成接口文档
 - 解析每个模块功能
 
## 维护者
    
[@xiaobucvg](https://github.com/xiaobucvg)

