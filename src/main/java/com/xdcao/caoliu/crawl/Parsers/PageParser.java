package com.xdcao.caoliu.crawl.Parsers;

import com.alibaba.fastjson.JSON;
import com.xdcao.caoliu.model.Constants;
import com.xdcao.caoliu.model.VideoContent;
import com.xdcao.caoliu.model.VideoType;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: buku.ch
 * @Date: 2018/10/24 1:12 PM
 */

@Component
public class PageParser extends AbstractParser {

    private static int pageNum = 2;

    @Override
    public List<VideoContent> parseBody(String body, Pattern pattern) {
        return null;
    }

    @Override
    public List<VideoContent> parseUrl(VideoContent inContent) {

//        pageUrl不加redis去重

        String pageRegexp = "href=\"(thread[0-9]+.php.fid=[0-9]+&amp;search=&amp;page="+pageNum+")\">[^<]+</a>";

        List<VideoContent> contents = new ArrayList<VideoContent>();

        if (StringUtils.isBlank(inContent.getPageUrl()) || inContent.getType()!=VideoType.PageURL) {
            return contents;
        }

        try {
            Document document = getDocument(inContent.getPageUrl());
            Pattern pagePattern = Pattern.compile(pageRegexp);
            System.out.println("page pattern: "+pagePattern.toString());
            Matcher matcher = pagePattern.matcher(document.body().toString());
            if (matcher.find()) {
                if (matcher.group(1)!=null) {
                    String pageUrl = matcher.group(1).replace("&amp;", "&");
                    VideoContent videoContent = new VideoContent();
                    videoContent.setType(VideoType.PageURL);
                    videoContent.setPageUrl(Constants.prefix+pageUrl);
                    videoContent.setCompleted(true);
                    videoContent.setCreated(new Date());
                    contents.add(videoContent);
                    pageNum++;
                    return contents;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return contents;
        }

        return contents;

    }



    public static void main(String[] args) {
        VideoContent videoContent =new VideoContent();
        videoContent.setPageUrl("http://cl.39u.xyz/thread0806.php?fid=22&search=&page=1");
        videoContent.setType(VideoType.PageURL);
        PageParser pageParser = new PageParser();
        List<VideoContent> contents = pageParser.parseUrl(videoContent);
        System.out.println(JSON.toJSONString(contents.get(0)));


    }




}
