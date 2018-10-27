package com.xdcao.caoliu;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Author: buku.ch
 * @Date: 2018/10/25 2:10 PM
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CaoliuApplication.class)
public class RedisTest {

    @Autowired
    private JedisPool jedisPool;

    @Test
    public void get(){
        String key = "name";
        System.out.println(jedisPool.getResource().get(key));//aaa
        Long del = jedisPool.getResource().del(key);
        System.out.println(del);
        jedisPool.close();
    }


    @Test
    public void set(){
        String key = "name";
        String value = "aaa";
        jedisPool.getResource().set(key, value);
        jedisPool.close();
    }//仅做测试，未关闭jedis客户端等资源


}
