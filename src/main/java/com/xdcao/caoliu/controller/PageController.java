package com.xdcao.caoliu.controller;

import com.xdcao.caoliu.model.VideoContent;
import com.xdcao.caoliu.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @RequestMapping(value = "/index")
    public String index(@RequestParam("page") int page, Model model){

        List<VideoContent> dataByPage = redisService.getDataByPage(page);


        model.addAttribute("data", dataByPage.toArray());


        return "index.html";
    }


}
