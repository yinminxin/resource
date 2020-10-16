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
@Table(name = "link")
@org.hibernate.annotations.Table(appliesTo = "link", comment = "本地链接表")
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "varchar(100) DEFAULT '' COMMENT '文件名'")
    private String fileName;

    @Column(columnDefinition = "longtext DEFAULT '' COMMENT '本地链接'")
    private String localLink;
}
