package com.example.wanda.resource.entity;

import com.example.wanda.resource.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "resource_info")
@org.hibernate.annotations.Table(appliesTo = "resource_info", comment = "资源信息表")
public class ResourceInfo extends BaseEntity {

    @Column(columnDefinition = "varchar(50) DEFAULT '' COMMENT '资源名称'")
    private String name;
    @Column(columnDefinition = "varchar(10) DEFAULT '' COMMENT '资源类型'")
    private String type;
    @Column(columnDefinition = "varchar(200) DEFAULT '' COMMENT '文件名称 （带后缀）'")
    private String filename;
    @Column(columnDefinition = "varchar(50) DEFAULT '' COMMENT '文件大小 单位 M'")
    private String filesize;
    @Column(columnDefinition = "varchar(200) DEFAULT '' COMMENT '封面图路径'")
    private String img;
    @Column(columnDefinition = "varchar(200) DEFAULT '' COMMENT '资源路径'")
    private String url;
    @Column(columnDefinition = "varchar(10) DEFAULT '' COMMENT '学段'")
    private String section;
    @Column(columnDefinition = "varchar(10) DEFAULT '' COMMENT '学科'")
    private String subject;
    @Column(columnDefinition = "varchar(10) DEFAULT '' COMMENT '来源区'")
    private String area;
    @Column(columnDefinition = "varchar(100) DEFAULT '' COMMENT '提供方'")
    private String provider;
    @Column(columnDefinition = "varchar(50) DEFAULT '' COMMENT '学校'")
    private String school;
    @Column(columnDefinition = "varchar(10) DEFAULT '' COMMENT '评审状态  1:未提交 2:待评审 3:已评审'")
    private String review;
    @Column(columnDefinition = "varchar(10) DEFAULT '' COMMENT '是否删除 1是0否'")
    private String removeflag;
    @Column(columnDefinition = "int  COMMENT '创建人'")
    private Long creator;
    @Column(columnDefinition = "int  COMMENT '修改人'")
    private Long updator;

}
