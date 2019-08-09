package com.mipo.core.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;

/**
 * redis fastJson序列化
 * @param <T>
 */
public class FastJsonRedisSerializer<T> implements RedisSerializer<T> {

    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private Class<T> clz;


    public FastJsonRedisSerializer(Class<T> clz) {
        super();
        this.clz = clz;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t==null){
            return new byte[0];
        }
        if (t instanceof String){
            return ((String) t).getBytes(DEFAULT_CHARSET);
        }else {
            return JSON.toJSONString(t).getBytes(DEFAULT_CHARSET);
        }
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes==null||bytes.length<=0){
            return null;
        }
        String result = new String(bytes,DEFAULT_CHARSET);
        return JSON.parseObject(result,clz) ;
    }
}
