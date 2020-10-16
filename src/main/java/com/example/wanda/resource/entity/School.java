package com.example.wanda.resource.entity;

import com.example.wanda.resource.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "t_school")
@org.hibernate.annotations.Table(appliesTo = "t_school", comment = "学校")
public class School extends BaseEntity {

    @Column(columnDefinition = "varchar(255) NOT NULL  default '' COMMENT '名称'")
    private String name;

    @Column(columnDefinition = "int(11) unsigned NOT NULL  default 0 COMMENT '区域ID'")
    private long areasId;

    @Column(columnDefinition = "varchar(50) default '' COMMENT '区域名称'")
    private String areaName;

    @Column(columnDefinition = "varchar(11)  NOT NULL default '' COMMENT '学段id,英文逗号分割'")
    private String sectionId;

    @Column(columnDefinition = "varchar(50) NOT NULL default '' COMMENT '学段名称,英文逗号分割'")
    private String sectionName;

    @Column(columnDefinition = "int(11) unsigned NOT NULL default 0 COMMENT '删除状态(0-正常;1-删除)'")
    private int deleted;

    @Column(columnDefinition = "varchar(255) NOT NULL default '' COMMENT '拼音'")
    private String pinyin;

}
