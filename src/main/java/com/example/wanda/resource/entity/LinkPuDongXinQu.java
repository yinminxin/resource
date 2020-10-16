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
@Table(name = "link_pudongxinqu")
@org.hibernate.annotations.Table(appliesTo = "link_pudongxinqu", comment = "本地链接_浦东新区表")
public class LinkPuDongXinQu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "varchar(100) DEFAULT '' COMMENT '文件名'")
    private String fileName;

    @Column(columnDefinition = "longtext DEFAULT '' COMMENT '本地链接'")
    private String localLink;
}
