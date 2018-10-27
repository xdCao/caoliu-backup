package com.xdcao.caoliu.crawl.Parsers;

import com.xdcao.caoliu.model.VideoContent;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.regex.Pattern;

/**
 * @Author: buku.ch
 * @Date: 2018/10/24 1:02 PM
 */

@Component
public interface Parser {

    List<VideoContent> parseBody(String body, Pattern pattern);

    List<VideoContent> parseUrl(VideoContent videoContent);



}
