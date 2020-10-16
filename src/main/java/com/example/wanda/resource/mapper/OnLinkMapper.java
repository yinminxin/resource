package com.example.wanda.resource.mapper;

import com.example.wanda.resource.base.MyMapper;
import com.example.wanda.resource.entity.OnLink;
import com.example.wanda.resource.entity.Resource;
import org.springframework.stereotype.Repository;

/**
 * @description: 远程文件名对应本地链接mapper
 * @author: yinminxin
 * @create: 2020-10-10
 **/
@Repository
public interface OnLinkMapper extends MyMapper<OnLink,Long> {
}
