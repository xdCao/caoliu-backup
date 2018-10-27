package com.xdcao.caoliu.model;

/**
 * @Author: buku.ch
 * @Date: 2018/10/24 1:07 PM
 */


public enum VideoType {

    PageURL(0),PostURL(1),VideoURL(2),Video(3);

    private final int urlType;

    VideoType(int urlType) {
        this.urlType = urlType;
    }

    public int getUrlType() {
        return urlType;
    }
}
