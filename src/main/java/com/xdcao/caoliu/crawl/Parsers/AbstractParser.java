package com.xdcao.caoliu.crawl.Parsers;

import com.xdcao.caoliu.model.VideoContent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @Author: buku.ch
 * @Date: 2018/10/24 2:21 PM
 */

@Component
public class AbstractParser implements Parser {

    private String chrome = "Chrome/66.0.3359.181";
    private String safari = "Safari/537.36";
    private String mozila = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko)";
    private String android = "Mozilla/5.0 (Linux; U; Android 4.3; en-us; SM-N900T Build/JSS15J) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";


    public List<VideoContent> parseBody(String body, Pattern pattern) {
        return null;
    }

    public List<VideoContent> parseUrl(VideoContent videoContent) {
        return null;
    }

    protected Document getDocument(String seedUrl) throws IOException {
        return Jsoup.connect(seedUrl).timeout(1000000)
                .userAgent(android).get();
    }
}
