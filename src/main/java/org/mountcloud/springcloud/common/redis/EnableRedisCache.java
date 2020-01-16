package org.mountcloud.springcloud.common.redis;

import org.mountcloud.springcloud.common.redis.config.CacheConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author zhanghaishan
 * @version V1.0
 * TODO:
 * 2020/1/17.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(CacheConfig.class)
@EnableCaching
public @interface EnableRedisCache {
}
