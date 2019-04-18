# Aebiz-B2B2C 开发指南

本框架支持分布式部署，但为了提高开发效率，目前是“分模块、单应用”，根据项目业务需要可拆分模块出来用Dubbox做分布式部署。

框架集成了Quartz、Shiro、RabbitMQ、Redis、Cache 等，其中 Redis 是作为 Shiro二级缓存（多Tomcat部署时不再需要手动配置 session 同步）及Spring-cache 使用的，RabbitMQ 主要用于更新多Tomcat 的系统参数及其他业务需求，Quartz 也已做好了集群。

---

- [01. **快速开始**](01.QuickStart)
  - [01.01 **开发环境**](01.QuickStart/01.01.Environment.md)
  - [01.02 **数据源配置**](01.QuickStart/01.02.DBConfig.md)
  - [01.03 **项目启动**](01.QuickStart/01.03.Start.md)
- [02. **权限体系**](02.Shiro)
  - [02.01 **权限表设计**](02.Shiro/02.01.Desgin.md)
  - [02.02 **Shiro配置**](02.Shiro/02.02.Settings.md)
- [03. **框架运行**](03.Run)
  - [03.01 **入口类及数据初始化**](03.Run/03.01.Init.md)
  - [03.02 **过滤器**](03.Run/03.02.Interceptor.md)
    - [03.02.01 **全局变量**](03.Run/03.02.01.GlobalsInterceptor.md)
    - [03.02.02 **打印响应耗时**](03.Run/03.02.02.LogTimeInterceptor.md)
    - [03.02.03 **防SQL注入&跨站攻击**](03.Run/03.02.03.XssSqlInterceptor.md)
  - [03.03 **拦截器**](03.Run/03.03.Filter.md)
    - [03.03.01 **自定义路由**](03.Run/03.03.02.RouteFilter.md)
  - [03.04 **@Async 异步方法**](03.Run/03.04.Async.md)
  - [03.05 **@SLog 操作日志**](03.Run/03.05.Slog.md)
  - [03.06 **@SJson Json对象输出**](03.Run/03.06.Sjson.md)
  - [03.07 **@Cacheable 缓存数据**](03.Run/03.07.Cacheable.md)
- [04. **后台开发**](04.Code)
  - [04.01 **Pojo类**](04.Code/04.01.Pojo.md)
  - [04.02 **代码生成**](04.Code/04.02.Generator.md)
  - [04.03 **服务类**](04.Code/04.03.Service.md)
  - [04.04 **控制类**](04.Code/04.04.Controller.md)
- [05 **前台开发**](05.Web)
  - [05.01 **分页及查询**](05.Web/05.01.Datatable.md)
  - [05.02 **Loading**](05.Web/05.02.Loading.md)
  - [05.03 **提示框**](05.Web/05.03.Toast.md)
  - [05.04 **表单验证**](05.Web/05.04.Parsley.md)
  - [05.05 **JS组件国际化**](05.Web/05.05.Jslocales.md)
  - [05.06 **JS网络请求**](05.Web/05.06.Jsurl.md)
- [06 **Nutz Dao官方文档**](http://nutzam.com/core/dao/hello.html)
