package com.itlaoqi.bsbdj.entity;

public class Video {
    private Long videoId;

    private String videoUrl;

    private String downloadUrl;

    private Integer width;

    private Integer height;

    private Integer playfcount;

    private Integer duration;

    private Integer playcount;

    private String thumb;

    private String thumbSmall;

    private Long contentId;

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getPlayfcount() {
        return playfcount;
    }

    public void setPlayfcount(Integer playfcount) {
        this.playfcount = playfcount;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getPlaycount() {
        return playcount;
    }

    public void setPlaycount(Integer playcount) {
        this.playcount = playcount;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getThumbSmall() {
        return thumbSmall;
    }

    public void setThumbSmall(String thumbSmall) {
        this.thumbSmall = thumbSmall;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }
}