package com.xdcao.caoliu.controller;

import com.alibaba.fastjson.JSON;
import com.xdcao.caoliu.dao.VideoContentRepository;
import com.xdcao.caoliu.model.VideoContent;
import com.xdcao.caoliu.redis.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: buku.ch
 * @Date: 2018/10/26 11:14 PM
 */

@RestController
@RequestMapping("/sync")
public class SyncController {

    private static final Logger logger = LoggerFactory.getLogger(SyncController.class);

    @Autowired
    private RedisService redisService;

    @Autowired
    private VideoContentRepository videoContentRepository;

    @RequestMapping(value = "/es")
    public String syncES() {

        try {
            List<VideoContent> videoContents = redisService.getAllVideoContents("http*");

            for (VideoContent content : videoContents) {
                if (StringUtils.isEmpty(content.getPostUrl())) {

                    logger.error("empty postUrl:  "+ JSON.toJSONString(content));
                    continue;

                }
                videoContentRepository.save(content);
            }
            return "Sync completed";
        }catch (Exception e){
            e.printStackTrace();
            return "sync failed!!";
        }



    }

}
