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

 - ~~增加管理员/博客导航/回收站功能~~
 - ~~优化 token 鉴权方式~~
 - ~~完成接口文档~~
 - 解析每个模块功能
 - 上传文件校验
 
## 重要功能介绍

**跨域问题**
使用 Spring 的 FilterRegistrationBean 注册 CORSFilter，匹配所有路径。

**登录问题**
没有使用 Session，而是实现了一个简单的 Token 机制。虽然在这种简单项目中使用 Token 有点多此一举。

**GET 方式下的复杂查询**
RESTful 风格的 API ，存在资源的获取都使用 GET 方式的规范，对于多种不同的查询，使用同一个接口，于是存在一个复杂查询参数的问题。
Spring 本身支持 GET 方式接收 RequestBody ,可以直接将查询参数放到请求体中，但是一些前端网络请求库可能并不支持使用 GET 方式发送请求体，
于是放弃在 GET 请求中接收 RequestBody，改用 RequestParam 的方式获取路径上的查询参数。
过程：
前端将查询参数对象转换为 json 格式，放到路径中传递；
后端接收到 json 格式的查询参数，反序列化后作为查询标准；
由于许多地方都用这种形式的查询，所以采用切面的形式简化代码编写。
定义注解 ： @RequestJsonParam
定义切面 ： RequestJsonParamAspect
切面作用，拦截所有注解了 @RequestJsonParam 的方法，将 json 参数序列化位对象，并可以对序列化后的字段进行校验。 

**分页查询对象与分页包装对象**
项目中采用手写分页的形式。
定义分页查询对象 ：Pageable
定义分页包装对象 ：Page
分页自动计算注解 ：@PageableAutoCalculate
Pageable 配合 @RequestJsonParam，@PageableAutoCalculate 对象配合使用，可以简单的完成分页参数的接收。

**操作记录**
记录一些重要的后台操作。
定义注解 ：@Log
定义切面 : LogAspect
切面作用，拦截所有注解了 @Log 的方法， 异步的将本次请求的 IP 地址和做的操作存到数据库中。 

## 维护者
    
[@xiaobucvg](https://github.com/xiaobucvg)

