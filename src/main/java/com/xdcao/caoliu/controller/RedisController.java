package com.xdcao.caoliu.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.xdcao.caoliu.model.VideoContent;
import com.xdcao.caoliu.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @Author: buku.ch
 * @Date: 2018/10/25 2:27 PM
 */

@RestController
@CrossOrigin(value = "*")
public class RedisController {

    @Autowired
    private RedisService redisService;

    @RequestMapping("/all/page")
    @ResponseBody
    public String allKeys(@RequestParam("page") int page){
        return JSON.toJSONString(redisService.getDataByPage(page));
    }



}
