package org.mountcloud.springcloud.common.redis.config;


import org.mountcloud.springcloud.common.redis.serializer.PrefixKeySerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
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
    

    public RedisSerializer<String> keySerializer() {
        return new PrefixKeySerializer(CACHE_PREFIX);
    }


    public RedisSerializer<Object> valueSerializer() {
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
