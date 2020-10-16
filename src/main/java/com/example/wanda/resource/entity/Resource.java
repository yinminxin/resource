package com.example.wanda.resource.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * @description:
 * @author: yinminxin
 * @create: 2020-09-24
 **/
@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "resource")
@org.hibernate.annotations.Table(appliesTo = "resource", comment = "资源信息表表")
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "varchar(100) DEFAULT '' COMMENT '本地链接'")
    private String resource_name;
    @Column(columnDefinition = "varchar(100) DEFAULT '' COMMENT '学段'")
    private String section;
    @Column(columnDefinition = "varchar(100) DEFAULT '' COMMENT '学科'")
    private String subject;
    @Column(columnDefinition = "varchar(100) DEFAULT '' COMMENT '提供者'")
    private String source;
    @Column(columnDefinition = "varchar(100) DEFAULT '' COMMENT '区域'")
    private String area;
    @Column(columnDefinition = "varchar(100) DEFAULT '' COMMENT '学校'")
    private String school;
    @Column(columnDefinition = "longtexts DEFAULT '' COMMENT '本地链接'")
    private String local_link;
    @Column(columnDefinition = "longtext DEFAULT '' COMMENT '线上链接'")
    private String online_link;
    @Column(columnDefinition = "varchar(100) DEFAULT '0' COMMENT '同步状态：0-未同步;1-同步'")
    private String status;
}
