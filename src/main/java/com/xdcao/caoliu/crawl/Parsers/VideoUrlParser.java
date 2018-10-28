package com.xdcao.caoliu.crawl.Parsers;


import com.xdcao.caoliu.model.VideoContent;
import com.xdcao.caoliu.model.VideoType;

import com.xdcao.caoliu.redis.RedisService;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: buku.ch
 * @Date: 2018/10/24 3:25 PM
 */

@Component
public class VideoUrlParser extends AbstractParser {

    private static final Logger logger = LoggerFactory.getLogger(VideoUrlParser.class);

    @Autowired
    private RedisService redisService;

    public static final String VIDEO_PAGE_REGEXP = "(http*://.*/embed/[0-9]+)";

    public static final Pattern VIDEO_PAGE_PATTERN = Pattern.compile(VIDEO_PAGE_REGEXP);

    public static final String VIDEO_REGEXP = "(http.*://[0-9a-zA-Z]+.[0-9a-zA-Z]+.com/get_file/[0-9a-zA-Z]+/[0-9a-zA-Z]+/[0-9]+/[0-9]+/[0-9]+.mp4)";

    public static final Pattern VIDEO_PATTERN = Pattern.compile(VIDEO_REGEXP);

    public static final String PREVIEW_REGEXP = "pre.*(http.*://[0-9a-zA-Z]+.[0-9a-zA-Z]+.com/contents/.*/preview.mp4.jpg)";

    public static final Pattern PREVIEW_PATTERN = Pattern.compile(PREVIEW_REGEXP);

    @Override
    public List<VideoContent> parseUrl(VideoContent inContent) {

        List<VideoContent> contents = new ArrayList<VideoContent>();


        if (StringUtils.isBlank(inContent.getPostUrl()) || inContent.getType() != VideoType.PostURL) {
            return contents;
        }

        try {

            Boolean exists = redisService.exists(inContent.getPostUrl());
            if (exists) {
                return contents;
            }


            Document document = getDocument(inContent.getPostUrl());
            Matcher videoPageMatcher = VIDEO_PAGE_PATTERN.matcher(document.body().toString());

            if (videoPageMatcher.find()) {
//                System.out.println("find video page    "+videoPageMatcher.group(0)+" title: "+inContent.getTitle());

                String videoPage = videoPageMatcher.group(0).replace("______", ".");
                videoPage = videoPage.replace("http://www.viidii.info/?", "");

//                System.out.println("Changed page:   "+videoPage);

                Document videoDoc = getDocument(videoPage);

                Matcher videoMatcher = VIDEO_PATTERN.matcher(videoDoc.body().toString());
                Matcher previewMatcher = PREVIEW_PATTERN.matcher(videoDoc.body().toString());
                if (videoMatcher.find()) {
                    logger.info("find video !!!" + videoMatcher.group(0));
                    VideoContent videoContent = new VideoContent();
                    videoContent.setPostUrl(inContent.getPostUrl());
                    videoContent.setPageUrl(inContent.getPageUrl());
                    videoContent.setTitle(inContent.getTitle());
                    videoContent.setType(VideoType.VideoURL);
                    videoContent.setVideoUrl(videoMatcher.group(0));
                    videoContent.setVideoPageUrl(videoPage);
                    System.out.println(videoPage);

                    if (previewMatcher.find()) {
                        videoContent.setPreviewUrl(previewMatcher.group(1));
                        System.out.println(previewMatcher.group(1));
                    }

                    contents.add(videoContent);
                    return contents;
                }
            }

            return contents;

        } catch (IOException e) {
            e.printStackTrace();
            return contents;
        }


    }

    public static void main(String[] args) {
        VideoContent videoContent = new VideoContent();
        videoContent.setType(VideoType.PostURL);
        videoContent.setPostUrl("http://cl.39u.xyz/htm_data/22/1810/3318013.html");
        VideoUrlParser videoUrlParser = new VideoUrlParser();
        videoUrlParser.parseUrl(videoContent);
    }

}
