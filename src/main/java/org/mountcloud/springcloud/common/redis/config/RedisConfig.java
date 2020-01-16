package org.mountcloud.springcloud.common.redis.config;

import java.math.BigDecimal;
import java.time.Duration;

import org.mountcloud.springcloud.common.redis.serializer.PrefixKeySerializer;
import org.mountcloud.springproject.common.util.BigDecimalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig {

	@Value("${spring.application.name}")
    private String CACHE_PREFIX;
	
    @Value("${spring.cache.redis.time-to-live}")
    private Long expiration;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    
    /**
     * 所有对象改成json存
     * @return
     */
    @Primary
    @Bean("redisTemplate")
    public StringRedisTemplate stringRedisTemplate() {

        StringRedisTemplate  redisTemplate =  new StringRedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        redisTemplate.setKeySerializer(keySerializer());
        redisTemplate.setValueSerializer(valueSerializer());

        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }
    
    @Bean
    @Primary
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
//                .entryTtl(Duration.ofSeconds());

//        RedisCacheManager redisCacheManager = new RedisCacheManager(stringRedisTemplate);

        try {
            BigDecimal defaultExpirationDecimal = BigDecimalUtil.divDecimal(new BigDecimal(expiration),new BigDecimal(1000),BigDecimal.ROUND_UNNECESSARY);
            Long defaultExpiration = defaultExpirationDecimal.longValue();

            redisCacheConfiguration.entryTtl(Duration.ofSeconds(defaultExpiration));
            //redisCacheConfiguration.prefixKeysWith(CACHE_PREFIX);


            RedisSerializationContext.SerializationPair<String> keySerializer = RedisSerializationContext.SerializationPair
                    .fromSerializer(keySerializer());
            redisCacheConfiguration.serializeKeysWith(keySerializer);

            RedisSerializationContext.SerializationPair<Object> valueSerializer = RedisSerializationContext.SerializationPair
                    .fromSerializer(valueSerializer());
            redisCacheConfiguration.serializeValuesWith(valueSerializer);

//            redisCacheManager.setDefaultExpiration(defaultExpiration);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        return RedisCacheManager
                .builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(redisCacheConfiguration).build();
    }
	
    private RedisSerializer<String> keySerializer() {
        return new PrefixKeySerializer(CACHE_PREFIX);
    }


    private RedisSerializer<Object> valueSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }
    
    /**
     * 返回前缀
     * @return
     */
    public String getPrefix() {
    	return CACHE_PREFIX;
    }
    
    /**
     * 返回redis key的前缀
     * @return
     */
    public String getRedisKeyPrefix() {
    	return CACHE_PREFIX + ":";
    }
}
