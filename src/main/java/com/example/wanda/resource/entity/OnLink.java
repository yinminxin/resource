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
@Table(name = "on_link")
@org.hibernate.annotations.Table(appliesTo = "on_link", comment = "远程链接对应表")
public class OnLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "varchar(100) DEFAULT '' COMMENT '远程文件名'")
    private String fileName;
    @Column(columnDefinition = "longtext DEFAULT '' COMMENT '本地链接'")
    private String localLink;
    @Column(columnDefinition = "varchar(100) DEFAULT '' COMMENT '学校'")
    private String school;
    @Column(columnDefinition = "varchar(100) DEFAULT '' COMMENT '区域'")
    private String area;
    @Column(columnDefinition = "varchar(100) DEFAULT '' COMMENT '学段'")
    private String section;
    @Column(columnDefinition = "varchar(100) DEFAULT '' COMMENT '学科'")
    private String subject;
}
