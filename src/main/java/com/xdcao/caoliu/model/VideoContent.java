package com.xdcao.caoliu.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: buku.ch
 * @Date: 2018/10/24 1:03 PM
 */

@Document(indexName = "caoliu",type = "video")
public class VideoContent implements Serializable {

    private String pageUrl = "";

    @Id
    private String postUrl = "";

    private String videoUrl = "";

    private String videoPageUrl = "";

    private String filePath = "";

    private boolean completed = false;

    private String title = "";

    private String previewUrl = "";

    private Date created = new Date();

    private VideoType type;


    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean getCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public VideoType getType() {
        return type;
    }

    public void setType(VideoType type) {
        this.type = type;
    }


    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getVideoPageUrl() {
        return videoPageUrl;
    }

    public void setVideoPageUrl(String videoPageUrl) {
        this.videoPageUrl = videoPageUrl;
    }
}
