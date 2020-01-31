package com.itlaoqi.bsbdj.mapper;

import com.itlaoqi.bsbdj.entity.Video;

import java.util.List;

public interface VideoMapper {
    int deleteByPrimaryKey(Long videoId);

    int insert(Video record);

    int insertSelective(Video record);

    Video selectByPrimaryKey(Long videoId);

    int updateByPrimaryKeySelective(Video record);

    int updateByPrimaryKey(Video record);

    public List<Video> findByContentId(Long contentId);

}