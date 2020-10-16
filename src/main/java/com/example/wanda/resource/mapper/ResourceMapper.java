package com.example.wanda.resource.mapper;

import com.example.wanda.resource.base.MyMapper;
import com.example.wanda.resource.entity.Link;
import com.example.wanda.resource.entity.Resource;
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
public interface ResourceMapper extends MyMapper<Resource,Long> {

    @Select("SELECT * FROM resource WHERE online_link = '1' AND status = '0'")
    //获取所有未上传资源
    List<Resource> selectByOnLineLinkIsNull();

    @Select("SELECT * FROM resource WHERE online_link = '1' AND NOT ISNULL(local_link) AND status = '0'")
    //获取已有本地链接但未上传的资源
    List<Resource> selectByOnLineLinkIsNullAndLocalLink();

    @Select("SELECT * FROM resource WHERE online_link <> '1'  AND status = '0'")
    //获取所有已上传资源
    List<Resource> selectByOnLineLinkIsNotNull();

    @Select("SELECT * FROM resource WHERE online_link = '1' AND ISNULL(local_link)  AND status = '0'")
    //查找所有未匹配资源
    List<Resource> selectByLocalLinkIsNull();

    @Select("SELECT * FROM resource WHERE online_link = '1' AND ISNULL(local_link) AND status = '0' AND area = #{area}")
    //获取区所有没有本地链接的资源
    List<Resource> selectByArea(@Param("area") String area);

    @Select("SELECT * FROM resource WHERE online_link <> '1' AND NOT ISNULL(local_link) AND status = '1'")
        //获取已有本地链接但未上传的资源
    List<Resource> selectByOnLineLinkIsNotNullAndLocalLink();

    //SELECT * FROM `resource` WHERE local_link LIKE '%1;%'
    @Select("SELECT COUNT(*) FROM resource WHERE local_link LIKE CONCAT('%',#{localLink},'%')")
    //根据本地连接模糊匹配
    int selectByLocalLinkLike(@Param("localLink") String localLink);

    @Select({"<script>",
            "SELECT * FROM resource WHERE ISNULL(local_link) AND status = '0' AND id in" +
                    "<foreach item='id' index='index' collection='ids' open='(' separator=',' close=')'>" +
                    "#{id}" +
                    "</foreach>" +
                    "</script>"})
    //根据ids查找locallink为空的资源
    List<Resource> selectByIdsAndLocalLinkIsNull(@Param("ids") List<String> ids);
}
