package com.itlaoqi.bsbdj.entity;

public class Image {
    private Long imageId;

    private String bigUrl;

    private String watermarkerUrl;

    private Integer rawHeight;

    private Integer rawWidth;

    private String thumbUrl;

    private Long contentId;

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public String getBigUrl() {
        return bigUrl;
    }

    public void setBigUrl(String bigUrl) {
        this.bigUrl = bigUrl;
    }

    public String getWatermarkerUrl() {
        return watermarkerUrl;
    }

    public void setWatermarkerUrl(String watermarkerUrl) {
        this.watermarkerUrl = watermarkerUrl;
    }

    public Integer getRawHeight() {
        return rawHeight;
    }

    public void setRawHeight(Integer rawHeight) {
        this.rawHeight = rawHeight;
    }

    public Integer getRawWidth() {
        return rawWidth;
    }

    public void setRawWidth(Integer rawWidth) {
        this.rawWidth = rawWidth;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }
}