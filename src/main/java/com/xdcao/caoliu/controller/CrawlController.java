package com.xdcao.caoliu.controller;

import com.xdcao.caoliu.crawl.Fetcher;
import com.xdcao.caoliu.crawl.Parsers.PageParser;
import com.xdcao.caoliu.crawl.Parsers.PostParser;
import com.xdcao.caoliu.crawl.Parsers.VideoUrlParser;
import com.xdcao.caoliu.model.Constants;
import com.xdcao.caoliu.model.VideoContent;
import com.xdcao.caoliu.model.VideoType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @Author: buku.ch
 * @Date: 2018/10/26 1:51 PM
 */

@RestController
@RequestMapping(value = "/crawl")
public class CrawlController {

    private static final Logger logger = LoggerFactory.getLogger(CrawlController.class);

    @Autowired
    private PageParser pageParser;

    @Autowired
    private PostParser postParser;

    @Autowired
    private VideoUrlParser videoUrlParser;

    @Autowired
    private Fetcher fetcher;

    @RequestMapping(value = "/start")
    public String startCrawl(){

        final ArrayBlockingQueue<VideoContent> queue = new ArrayBlockingQueue<VideoContent>(100000);

        try {
            VideoContent seed = new VideoContent();
            seed.setPageUrl(Constants.seedUrl);
            seed.setType(VideoType.PageURL);
            seed.setCompleted(false);
            seed.setCreated(new Date());

            List<VideoContent> pageContents = pageParser.parseUrl(seed);
            List<VideoContent> postContents = postParser.parseUrl(seed);
            List<VideoContent> videoUrlContents = videoUrlParser.parseUrl(seed);
            List<VideoContent> all =new ArrayList<VideoContent>();
            all.addAll(pageContents);
            all.addAll(postContents);
            all.addAll(videoUrlContents);


            for (VideoContent videoContent:all) {
                try {
                    queue.put(videoContent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            Thread productor = new Thread(new Runnable() {
                public void run() {
                    while (!queue.isEmpty()) {
                        VideoContent take = null;
                        try {
                            take = queue.take();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (take!=null) {
                            List<VideoContent> pageContents = pageParser.parseUrl(take);
                            List<VideoContent> postContents = postParser.parseUrl(take);
                            List<VideoContent> videoUrlContents = videoUrlParser.parseUrl(take);
                            List<VideoContent> all =new ArrayList<VideoContent>();

                            all.addAll(pageContents);
                            all.addAll(postContents);
                            all.addAll(videoUrlContents);

                            for (VideoContent content:all) {
                                try {

                                    if (content.getType()==VideoType.VideoURL) {
//                                        System.out.println("传入的content: "+ JSON.toJSONString(content));
                                        fetcher.fetch(content);
                                        System.out.println("                            fetch: "+content.getVideoUrl());
                                    } else {
                                        System.out.println("                new in queue: "+content.getPageUrl());
//                                        System.out.println("                new in queue: "+content.getPostUrl());
                                        queue.put(content);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            });

            productor.start();
            return "ok";
        }catch (Exception e) {
            logger.error("" ,e);
            return "failed";
        }


    }


}
