package com.example.wanda.resource.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * @description: 浦东新区的资源名称ID对应表
 * @author: yinminxin
 * @create: 2020-09-29
 **/
@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "resource_name_pudongxinqu")
@org.hibernate.annotations.Table(appliesTo = "resource_name_pudongxinqu", comment = "浦东新区的资源名称ID对应表")
public class ResourceNamePuDongXinQu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "varchar(100) DEFAULT '' COMMENT '资源名称'")
    private String reourceName;
}
