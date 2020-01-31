package com.itlaoqi.bsbdj.entity;

public class User {
    private Long uid;

    private String header;

    private Integer isVip;

    private Integer isV;

    private String roomUrl;

    private String roomName;

    private String roomRole;

    private String roomIcon;

    private String nickname;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Integer getIsVip() {
        return isVip;
    }

    public void setIsVip(Integer isVip) {
        this.isVip = isVip;
    }

    public Integer getIsV() {
        return isV;
    }

    public void setIsV(Integer isV) {
        this.isV = isV;
    }

    public String getRoomUrl() {
        return roomUrl;
    }

    public void setRoomUrl(String roomUrl) {
        this.roomUrl = roomUrl;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomRole() {
        return roomRole;
    }

    public void setRoomRole(String roomRole) {
        this.roomRole = roomRole;
    }

    public String getRoomIcon() {
        return roomIcon;
    }

    public void setRoomIcon(String roomIcon) {
        this.roomIcon = roomIcon;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}