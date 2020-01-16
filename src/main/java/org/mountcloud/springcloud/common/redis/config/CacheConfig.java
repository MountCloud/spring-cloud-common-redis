package org.mountcloud.springcloud.common.redis.config;

import org.mountcloud.springproject.common.util.BigDecimalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.math.BigDecimal;
import java.time.Duration;

/**
 * @author zhanghaishan
 * @version V1.0
 * TODO:
 * 2020/1/17.
 */
public class CacheConfig {

    //过期时间
    @Value("${spring.cache.redis.time-to-live}")
    private Long expiration;

    @Autowired
    private RedisConfig redisConfig;

    @Bean
    @Primary
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();

        try {
            BigDecimal defaultExpirationDecimal = BigDecimalUtil.divDecimal(new BigDecimal(expiration),new BigDecimal(1000),BigDecimal.ROUND_UNNECESSARY);
            Long defaultExpiration = defaultExpirationDecimal.longValue();
            redisCacheConfiguration.entryTtl(Duration.ofSeconds(defaultExpiration));

            RedisSerializationContext.SerializationPair<String> keySerializer = RedisSerializationContext.SerializationPair
                    .fromSerializer(redisConfig.keySerializer());
            redisCacheConfiguration.serializeKeysWith(keySerializer);

            RedisSerializationContext.SerializationPair<Object> valueSerializer = RedisSerializationContext.SerializationPair
                    .fromSerializer(redisConfig.valueSerializer());
            redisCacheConfiguration.serializeValuesWith(valueSerializer);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return RedisCacheManager
                .builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(redisCacheConfiguration).build();
    }

}
