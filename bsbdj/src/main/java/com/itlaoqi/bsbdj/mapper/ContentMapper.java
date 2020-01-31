package com.itlaoqi.bsbdj.mapper;

import com.itlaoqi.bsbdj.entity.Content;

import java.util.List;
import java.util.Map;

public interface ContentMapper {
    int deleteByPrimaryKey(Long contentId);

    int insert(Content record);

    int insertSelective(Content record);

    Content selectByPrimaryKey(Long contentId);

    int updateByPrimaryKeySelective(Content record);

    int updateByPrimaryKey(Content record);

    public List<Map<String,Object>> findByParams(Map params);
}