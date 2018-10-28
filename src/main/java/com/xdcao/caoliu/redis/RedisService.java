package com.xdcao.caoliu.redis;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.xdcao.caoliu.model.VideoContent;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;

/**
 * @Author: buku.ch
 * @Date: 2018/10/25 2:18 PM
 */

@Service
public class RedisService {

    @Autowired
    private JedisPool jedisPool;

    //将目标对象序列化为String
    private <T> String beanToString(T value) {
        if(value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();
        //基础类型不需要json序列化
        if(clazz == int.class || clazz == Integer.class) {
            return ""+value;
        }else if(clazz == String.class) {
            return (String)value;
        }else if(clazz == long.class || clazz == Long.class) {
            return ""+value;
        }else {
            return JSON.toJSONString(value);
        }
    }


    public <T> boolean set(String key, T value,int seconds){
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            String str = beanToString(value);
            if(StringUtils.isEmpty(str)) {
                return false;
            }
            //生成真正的key
            String realKey  = key;
            if(seconds <= 0) {
                jedis.set(realKey, str);
            }else {
                //设置过期时间
                jedis.setex(realKey, seconds, str);
            }
            return true;
        }finally {
            //返回给jedisPoll连接池
            returnToPool(jedis);
        }
    }


    private <T> T stringToBean(String str, Class<T> clazz) {
        if(StringUtils.isEmpty(str) || clazz == null) {
            return null;
        }
        if(clazz == int.class || clazz == Integer.class) {
            return (T)Integer.valueOf(str);
        }else if(clazz == String.class) {
            return (T)str;
        }else if(clazz == long.class || clazz == Long.class) {
            return  (T)Long.valueOf(str);
        }else {
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }


    public <T> T get(String key,  Class<T> clazz){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String realKey = key;
            String str = jedis.get(realKey);
            T t = stringToBean(str,clazz);
            return t;
        }finally {
            returnToPool(jedis);
        }
    }


    public  boolean exists(String key) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  =  key;
            return  jedis.exists(realKey);
        }finally {
            returnToPool(jedis);
        }
    }
    public Long incr(String key) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  = key;
            return  jedis.incr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }
    public Long decr(String key) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  = key;
            return  jedis.decr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }


    public List<VideoContent> getAllVideoContents(String pattern) {
        Jedis jedis = null;
        try {
            List<VideoContent> datas = new ArrayList<>();
            jedis = jedisPool.getResource();
            Set<String> keys = jedis.keys(pattern);
            for (String key : keys) {
                String value = jedis.get(key);
                if (!StringUtils.isEmpty(value)) {
                    VideoContent videoContent = stringToBean(value, VideoContent.class);
                    datas.add(videoContent);
                }
            }
            return datas;
        }finally {
            returnToPool(jedis);
        }

    }


    private void returnToPool(Jedis jedis) {
        if (jedis!=null) {
            jedis.close();
        }
    }

    public List<VideoContent> getDataByPage(int page) {
        List<VideoContent> allKVs = getAllVideoContents("http*");
        allKVs.sort(new Comparator<VideoContent>() {
            @Override
            public int compare(VideoContent o1, VideoContent o2) {
                long time = o2.getCreated().getTime() - o1.getCreated().getTime();
                return (int) time;
            }
        });
        List<List<VideoContent>> partition = Lists.partition(allKVs, 8);
        if (page >= partition.size()) {
            return new ArrayList<VideoContent>();
        }

        return partition.get(page);
    }



}
