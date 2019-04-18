安徽全网B2B2BC
---------------------
# 关于Redis

/ioc/redis.xml

深度集成jedis,支持普通模式和集群模式自由切换,集群模式只支持db0(redis特性)

1、Redis普通模式 -- 单机,主从,sentinel,及第三方集群方案(例如codis),均使用原始的redis协议, JedisPool和JedisSentinelPool均继承Pool<Jedis>哦
2、Redis集群模式 -- Redis Cluster, redis 3.x加入的官方集群方案,使用hash槽的概念在不同节点存储数据, 使用MOVE响应指引客户端跳转到目标服务器, Jedis中的实现类是JedisCluster

jedis奇葩的地方是, JedisPool与JedisCluster均为连接池实现,但后者不继承Pool<Jedis>,所以,使用原生jedis的时候,要么注入JedisPool走普通模式,要么注入JedisCluster走集群模式

# 关于缓存

/ioc/cache.xml

改写SpringMVC自带的模板,使用jedis代理,同样支持Redis的集群运行模式

根据业务需要,可使用设置失效期的缓存

# 关于shiro

/ioc/shiro.xml

可以定义自己的权限验证体系,需配置拦截器(/ioc/application.xml) 结合使用

session缓存采用ehcache做一级缓存(速度快)和redis做二级缓存(持久化),二级缓存同样也是支持集群运行模式的

# 关于队列

/ioc/rabbit.xml
 
集群环境务必启用队列,基于队列实现的功能：

1、全局变量：当 rabbit 启用时，会通知每个tomcat实例（所有消费者），更新Globals变量；

2、数据更新：当 rabbit 启用时，会通知一个tomcat实例（某一消费者），执行更新数据操作；

启用rabbit后,系统启动时会初始化两个交换器,用于1的广播模式,用于2的轮播模式

# 关于定时任务

Quartz的数据表在项目启动时会自动初始化,并且支持集群部署,轮循执行

# 其他已启用的功能

@Async 异步方法注解：需异类方法调用才有效

@SLog  系统日志注解：会讲URI请求及结果写入日志表(按月分表)

@Cacheable 缓存注解：默认defaultCache是永久缓存需手动更新请查阅相关文档
           
           若自定义key,请务必添加 #root.targetClass.getName() 前缀,这样就可以在服务类里写公共的清空缓存方法
               @CacheEvict(key = "#root.targetClass.getName()+'*'")
               @Async
               public void clearCache() {    
                      
               }

@Transactional 事务注解：请在Service写事务方法并抛出异常

# 代码生成器

aebiz-codegenerator 代码生成工具类：可通过实体类或数据库生成框架代码；

IDEA插件：依赖aebiz-codegenerator,提供可视化的选择界面,在实体类上右击生成代码；

注：实体类加@Comment 注解的字段，才会在views页面上显示

# 开发说明

1、com.aebiz.app.* 子模块,不允许调用com.aebiz.web.*里面的类方法(为了以后能拆模块进行分布式部署),
commons之类的公共方法应该加到aebiz-commons里

2、@SJson 注解说明,非了好大劲写的这个注解,是为了方便格式化、过滤json数据,以及支持jsonp
@SJson
@SJson("{locked:'password|createAt',ignoreNull:true}")
    不输出locked字段,null值转换,详见 org.nutz.json.JsonFormat
@SJson(value="{locked:'password|createAt',ignoreNull:true}",jsonp=true)
    jsonpParam 默认是callback
@SJson(jsonp=true,jsonpParam="callbackApi")

#  在linux上shiro初始化很久

tomcat, 在setenv.sh添加如下

JAVA_OPTS=-Djava.security.egd=file:/dev/urandom ...其他配置

# 工具类的引用

org.apache.commons.lang3.StringUtils    字符串转数组对象等
org.nutz.lang.Strings                    字符串非空判断等