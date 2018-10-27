package com.xdcao.caoliu.crawl;

import com.alibaba.fastjson.JSON;
import com.xdcao.caoliu.dao.VideoContentRepository;
import com.xdcao.caoliu.model.VideoContent;
import com.xdcao.caoliu.model.VideoType;
import com.xdcao.caoliu.redis.RedisService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: buku.ch
 * @Date: 2018/10/24 4:56 PM
 */

@Component
public class Fetcher{

    @Autowired
    private RedisService redisService;

    @Autowired
    private VideoContentRepository videoContentRepository;

    private static ExecutorService executorService = Executors.newFixedThreadPool(6);

    public void fetch(final VideoContent videoContent) {

        if (videoContent.getType() != VideoType.VideoURL || videoContent.getVideoUrl() == null) {
            return;
        }

        FetchRunnable fetchRunnable = new FetchRunnable();
        fetchRunnable.setContent(videoContent);
        fetchRunnable.setRedisService(redisService);
        fetchRunnable.setVideoContentRepository(videoContentRepository);

        executorService.submit(fetchRunnable);

    }




}
