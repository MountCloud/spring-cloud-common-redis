package org.mountcloud.springcloud.common.redis.serializer;

import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author zhanghaishan
 * @version V1.0
 * TODO: 使用前缀的rediskey序列化
 * 2020年1月7日.
 */
public class PrefixKeySerializer extends StringRedisSerializer{
	
	//前缀
	private String prefix;

	/**
	 * 普通的构造函数
	 * @param prefixParam 前缀
	 */
	public PrefixKeySerializer(String prefixParam) {
		if(!prefixParam.endsWith(":")) {
			prefixParam = prefixParam + ":";
		}
		prefix = prefixParam;
	}

	/**
	 * 序列化
	 * @param string key
	 * @return 结果
	 */
    @Override
    public byte[] serialize(String string) {
        String tempString = string;
        if(!tempString.startsWith(prefix)){
            tempString = prefix+tempString;
        }
        return super.serialize(tempString);
    }

}
