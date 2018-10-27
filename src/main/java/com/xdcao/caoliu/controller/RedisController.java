package com.xdcao.caoliu.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.xdcao.caoliu.model.VideoContent;
import com.xdcao.caoliu.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @Author: buku.ch
 * @Date: 2018/10/25 2:27 PM
 */

@RestController
public class RedisController {

    @Autowired
    private RedisService redisService;

    @RequestMapping("/all/page")
    @ResponseBody
    public String allKeys(@RequestParam("page") int page){
        List<VideoContent> allKVs = redisService.getAllVideoContents("http*");
        Collections.sort(allKVs, new Comparator<VideoContent>() {
            @Override
            public int compare(VideoContent o1, VideoContent o2) {
                long time = o1.getCreated().getTime() - o2.getCreated().getTime();
                return (int)time;
            }
        });
        List<List<VideoContent>> partition = Lists.partition(allKVs, 10);
        if (page > partition.size()) {
            return "";
        }

        return JSON.toJSONString(partition.get(page-1));
    }

}
