# USE 使用
##  USE spring cloud common ，使用spring cloud common方式
```
<parent>
  <groupId>org.mountcloud</groupId>
  <artifactId>spring-cloud-common-parent</artifactId>
  <version>2.2.1.RELEASE-Hoxton.RELEASE-1.1</version>
</parent>
<dependency>
  <groupId>org.mountcloud</groupId>
  <artifactId>spring-cloud-common-redis</artifactId>
</dependency>
```
## OR Use alone，或者单独引用。
```
<dependency>
  <groupId>org.mountcloud</groupId>
  <artifactId>spring-cloud-common-redis</artifactId>
  <version>1.1</version>
</dependency>
```

## Enable cache，启用缓存（spring cache）
After using spring-cloud-common-redis, redis has been enabled, but the cache is not enabled. That means @Cacheable will not work. If you need to enable the cache, you must use this annotation to enable it.

使用spring-cloud-common-redis后已经启用了redis，但是并未开启缓存（cache）也就是说@Cacheable并不会起作用，如果需要开启cache，则必须使用这个注解来开启.

```
@EnableRedisCache
```

# Note
This project is spring cloud redis common,Support redis key prefix and cache key prefix.When using redis, it is often necessary to configure the redis key prefix, so this project will extract this type of business from non-public components.

Prefix is spring.application.name

# 描述
这个项目是spring cloud redis的公共库，支持redis key前缀，与缓存 key前缀。使用redis时往往需要配置redis的key前缀，所以这个项目将此类业务提取未公共组件。

前缀就是：spring.application.name

# config 配置

```
spring:
	redis:                                    #redis 配置
	  host: 127.0.0.1                         #redis 服务的IP
	  port: 6379                              #redis 服务的端口号
	  password:                               #redis 的连接密码,nopassword代表无需设置密码
	  database: 4                             #redis database编号
	  pool:                                   #redis 连接池配置
		max-active: 200                      #redis 连接池 最大活跃连结数
		min-idle: 50                          #redis 连接池  最小空闲连接数
		max-idle: 100                         #redis 连接池  最大空闲连接数
	cache:
	  type: REDIS
	  redis:
		time-to-live: 6000000		             #单位是毫秒,设置缓存的的有效期
```

# Files 文件列表
```
.
├── .gitignore
├── pom.xml
└── src
    ├── main
    │   └── java
    │       └── org
    │           └── mountcloud
    │               └── springcloud
    │                   └── common
    │                       └── redis
    │                           ├── config
    │                           │   ├── CacheConfig.java
    │                           │   └── RedisConfig.java
    │                           ├── EnableRedisCache.java
    │                           └── serializer
    │                               └── PrefixKeySerializer.java
    └── test
        └── java
            └── org
                └── mountcloud
                    └── AppTest.java
```
