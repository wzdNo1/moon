package com.mipo.core.util;


import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Redis工具类
 *
 */
@Component
public class RedisUtils {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ValueOperations<String, Object> valueOperations;
    @Autowired
    private HashOperations<String, String, Object> hashOperations;
    @Autowired
    private ListOperations<String, Object> listOperations;
    @Autowired
    private SetOperations<String, Object> setOperations;
    @Autowired
    private ZSetOperations<String, Object> zSetOperations;
    /**  默认过期时长，单位：秒 */
    public final static long DEFAULT_EXPIRE = 60 * 60 * 24;
    /**  不设置过期时长 */
    public final static long NOT_EXPIRE = -1;

    public static final int LOCK_EXPIRE = 500; // ms 每次重试之前sleep等待的毫秒数
    public static final int LOCK_MAX_RETRY_TIMES = 130; // ms 一重试次数

    public void set(String key, Object value, long expire){
        if (value instanceof String ){
            valueOperations.set(key,value);
        }
        else {
            valueOperations.set(key, JSONObject.toJSONString(value));
        }
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    /**
     * redis加锁操作
     * @param key
     * @param expire 失效时间 单位毫秒
     * @return
     */
    public boolean lockMs(String key, long expire){
        return  redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                boolean res = connection.setNX(key.getBytes(),key.getBytes());
                if(res){
                    expire(key, expire, TimeUnit.MILLISECONDS);
                }
                return res;
            }
        });
    }
    /**
     * redis加锁操作
     * @param key
     * @param expire 失效时间 单位秒
     * @return
     */
    public boolean lock(String key, long expire){
       return  redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                boolean res = connection.setNX(key.getBytes(),key.getBytes());
                if(res){
                    expire(key, expire);
                }
                return res;
            }
        });
    }

    /**
     * redis释放锁
     * @param key
     */
    public void releaseLock(String key){
        redisTemplate.execute(new RedisCallback<Void>() {
            @Override
            public Void doInRedis(RedisConnection connection) throws DataAccessException {
                connection.del(key.getBytes());
                return null;
            }
        });
    }
    public void set(String key, Object value){
        set(key, value, DEFAULT_EXPIRE);
    }

    public <T> T get(String key, Class<T> clazz, long expire) {
        String value = toJson(valueOperations.get(key));
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value == null ? null : fromJson(value, clazz);
    }

    /**
     * 获取key对应的实体类，使用前提为value必须使用fast序列化。
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T get(String key, Class<T> clazz) {
        return get(key, clazz, NOT_EXPIRE);
    }

    /**
     * 获取key的字符串value，并设置过期时间。
     * @param key
     * @param expire
     * @return
     */
    public String get(String key, long expire) {
        Object value = redisTemplate.opsForValue().get(key);
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        if (value instanceof String){
            return (String) value;
        }
        return toJson(value);
    }

    public String get(String key) {
        return get(key, NOT_EXPIRE);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

//    /**
//     * 获取redis中的对象
//     * @param redisKey
//     * @param key
//     * @param clazz
//     * @param <T>
//     * @return
//     */
    public <T> T getFromMap(String redisKey, String key,Class<T> clazz){
       Object object =  hashOperations.get(redisKey,key);
       String value = null;
       if (object instanceof  String){
           value = (String) object;
       }else if (object instanceof JSONObject){
           value = ((JSONObject) object).toJSONString();
       }
       return value == null ? null : fromJson(value, clazz);
    }

    /**
     * 向redis map数据中存入数据
     * @param redisKey
     * @param hKey
     * @param hValue
     */
    public void putToMap(String redisKey, Object hKey, Object hValue){
        redisTemplate.opsForHash().put(redisKey,hKey,hValue);
    }

    public Map<Object,Object> getMap(String redisKey){
        return redisTemplate.opsForHash().entries(redisKey);
    }

    /**
     * 获取map结果的key值集合
     * @param redisKey
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> Set<T> getKeySet(String redisKey, Class<T> clazz){
        return redisTemplate.opsForHash().keys(redisKey).stream().map((value)->{
            if (value instanceof String){
                return fromJson((String)value,clazz);
            }
            return fromJson(JSONObject.toJSONString(value),clazz);
        }).collect(Collectors.toSet());
    }

    /**
     * 获取redis中Map数据，并进行类型转换
     * @param redisKey
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> Map<String,T> getMap(String redisKey, Class<T> clazz){
        Set<String> hKeys = getKeySet(redisKey,String.class) ;
        Map<String,T> result = new HashMap<>();
        hKeys.stream().forEach((value)->{
            result.put(value,getFromMap(redisKey,value,clazz));
        });
        return result;
    }

    /**
     * 使用前提redisTemplate的value序列化采用fastjson进行序列化
     * @param redisKey
     * @param clszz
     * @param <T>
     * @return
     */
    public <T> List<T> getList(String redisKey,Class<T> clszz){
       Object object =  redisTemplate.opsForList().range(redisKey,0,redisTemplate.opsForList().size(redisKey));
       String value =null;
        if (object instanceof  String){
            value = (String) object;
        }else {
            value = JSONObject.toJSONString(object);
        }
        return value == null ? null : JSONObject.parseArray(value,clszz);
    }


    public <T> Set<T> getSet(String redisKey, Class<T> clazz){
        Set<Object> sets = redisTemplate.opsForSet().members(redisKey);
        return sets.stream().map((value)->{
            if (value instanceof String){
                return (T) (String) value;
            }else {
                return fromJson(JSONObject.toJSONString(value), clazz);
            }
        }).collect(Collectors.toSet());
    }

    /**
     * 歩增
     * @param key
     * @return
     */
    public Long inc(String key){
        return valueOperations.increment(key, 1);
    }

    /**
     * 定义失效时间
     * @param key
     * @param expire
     * @return
     */
    public Boolean expire(String key, long expire){
        return redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }
    /**
     * 定义失效时间
     * @param key
     * @param expire
     * @return
     */
    public Boolean expire(String key, long expire, TimeUnit timeUnit){
        return redisTemplate.expire(key, expire, timeUnit);
    }
    /**
     * 限制次数
     * @param key key
     * @param limitCount 最大次数
     * @param expire 失效时间
     * @return true 表示超出最大值 false 表示未超出最大值
     */
    public boolean overLimitCount(String key,int limitCount, long expire){

        long value = inc(key);
        if(value>limitCount){
            return true;
        }
        if(value == 1){
            expire(key, expire);
        }
        return false;
    }

    public <T> void hputAll(String key, Map<String,T> map){
        Map<String,String> map1 = new HashMap<>();
        Iterator<Map.Entry<String, T>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, T> entry = iterator.next();
            map1.put(entry.getKey(), toJson(entry.getValue()));
        }
        hashOperations.putAll(key, map1);
    }

    public void hput(String key, String field, Object object){
        hashOperations.put(key, field, toJson(object));
    }

    public <T> T hget(String key, String field, Class<T> clazz){
        return fromObject(hashOperations.get(key, field), clazz);
    }
    public <T> List<T> hgetAll(String key, Class<T> clazz){
        List<Object> list = hashOperations.values(key);
        return list.stream().map(item  -> fromObject(item,clazz)).collect(Collectors.toList());
    }

    /**
     * Object转成JSON数据
     */
    private String toJson(Object object){
        if(object == null){
            return null;
        }
        if(object instanceof Integer || object instanceof Long || object instanceof Float ||
                object instanceof Double || object instanceof Boolean || object instanceof String){
            return String.valueOf(object);
        }
        return JSONObject.toJSONString(object);
    }

    /**
     * JSON数据，转成Object,如过是String类型，直接返回
     */
    private <T> T fromJson(String json, Class<T> clazz){
        if(json == null){
            return null;
        }
        if (clazz == String.class){
            return (T) json;
        }else {
            return JSONObject.parseObject(json, clazz);
        }
    }

    /**
     * json对象转换为响应的class
     * @param object
     * @param clazz
     * @param <T>
     * @return
     */
    private <T> T fromObject(Object object, Class<T> clazz){
        if (object instanceof String){
           return fromJson((String)object,clazz);
        }else {
           return fromJson(JSONObject.toJSONString(object),clazz);
        }
    }


}
