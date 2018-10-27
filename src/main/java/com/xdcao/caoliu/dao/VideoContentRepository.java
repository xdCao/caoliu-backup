package com.xdcao.caoliu.dao;

import com.xdcao.caoliu.model.VideoContent;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: buku.ch
 * @Date: 2018/10/26 1:09 PM
 */

@Repository
public interface VideoContentRepository extends ElasticsearchRepository<VideoContent,String> {
}
