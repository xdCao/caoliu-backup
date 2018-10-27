package com.xdcao.caoliu.crawl;

import com.alibaba.fastjson.JSON;
import com.xdcao.caoliu.dao.VideoContentRepository;
import com.xdcao.caoliu.model.Constants;
import com.xdcao.caoliu.model.VideoContent;
import com.xdcao.caoliu.model.VideoType;
import com.xdcao.caoliu.redis.RedisService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.Date;

/**
 * @Author: buku.ch
 * @Date: 2018/10/24 9:17 PM
 */

@Component()
public class FetchRunnable implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(FetchRunnable.class);

    private RedisService redisService;

    private VideoContentRepository videoContentRepository;

    private VideoContent videoContent;

    public void setContent(VideoContent content) {
        this.videoContent = content;
    }

    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }

    public void setVideoContentRepository(VideoContentRepository videoContentRepository) {
        this.videoContentRepository = videoContentRepository;
    }

    public void run() {
        videoContent.setCreated(new Date());
        videoContent.setType(VideoType.Video);
        videoContent.setCompleted(true);

        String path = "failed";

        Integer fileNum = redisService.get("fileNum", Integer.class);
        if (fileNum == null) {
            redisService.set("fileNum", 0, -1);
            path = HttpDownload.download(videoContent.getVideoUrl());
            if (!"fail".equals(path)) {
                logger.info("download complete:   "+path+"   url:   "+videoContent.getVideoUrl());
                redisService.incr("fileNum");
            }
        } else if (fileNum < Constants.MAX_FILE_NUM) {
            path = HttpDownload.download(videoContent.getVideoUrl());
            if (!"fail".equals(path)) {
                logger.info("download complete:   "+path+"   url:   "+videoContent.getVideoUrl());
                redisService.incr("fileNum");
            }
        }

        videoContent.setFilePath(path);

        /*存到redis*/

        boolean set = redisService.set(videoContent.getPostUrl(), JSON.toJSONString(videoContent),-1);

        if (!set) {
            logger.error("insert redis failed!!: \n"+JSON.toJSONString(videoContent));
        } else {
            videoContentRepository.save(videoContent);
        }

    }
}
