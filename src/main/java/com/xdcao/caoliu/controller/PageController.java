package com.xdcao.caoliu.controller;

import com.alibaba.fastjson.JSON;
import com.xdcao.caoliu.dao.VideoContentRepository;
import com.xdcao.caoliu.model.VideoContent;
import com.xdcao.caoliu.redis.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author: buku.ch
 * @Date: 2018/10/27 5:20 PM
 */

@RequestMapping("/")
@CrossOrigin("*")
@Controller
public class PageController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private VideoContentRepository videoContentRepository;

    @RequestMapping(value = "/index")
    public String index(@RequestParam("page") int page, Model model){

        List<VideoContent> dataByPage = redisService.getDataByPage(page);


        model.addAttribute("data", dataByPage.toArray());


        return "index.html";
    }


    @RequestMapping(value = "/search")
    public String search(@RequestParam(value = "query",required = false) String query,@RequestParam(value = "page",required = false) Integer page, Model model) {

        if (page == null) {
            page = 0;
        }

        PageRequest pageRequest = PageRequest.of(page , 8);

        Page<VideoContent> all = null;

        if (StringUtils.isEmpty(query)) {
            all = videoContentRepository.findAll(pageRequest);
        } else {
            QueryBuilder queryBuilder = new MatchQueryBuilder("title", query);
            all = videoContentRepository.search(queryBuilder, pageRequest);
        }

        model.addAttribute("pageInfo", all);

        return "index2.html";


    }


}
