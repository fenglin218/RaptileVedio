package com.itlaoqi.bsbdj.entity;

public class Forum {
    private Long forumId;

    private Integer postCount;

    private String logo;

    private Integer forumSort;

    private Integer forumStatus;

    private String name;

    private Integer userCount;

    private String info;

    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    public Integer getPostCount() {
        return postCount;
    }

    public void setPostCount(Integer postCount) {
        this.postCount = postCount;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Integer getForumSort() {
        return forumSort;
    }

    public void setForumSort(Integer forumSort) {
        this.forumSort = forumSort;
    }

    public Integer getForumStatus() {
        return forumStatus;
    }

    public void setForumStatus(Integer forumStatus) {
        this.forumStatus = forumStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}