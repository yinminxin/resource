package com.example.wanda.resource.mapper;

import com.example.wanda.resource.base.MyMapper;
import com.example.wanda.resource.entity.Link;
import com.example.wanda.resource.entity.School;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:
 * @author: yinminxin
 * @create: 2020-09-24
 **/
@Repository
public interface SchoolMapper extends MyMapper<School,Long> {

    @Select("SELECT * FROM t_school WHERE name LIKE CONCAT('%',#{school},'%')")
    List<String> selectIdByName(@Param("school") String school);
}
