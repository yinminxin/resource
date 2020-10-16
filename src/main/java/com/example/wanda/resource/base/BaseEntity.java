/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.example.wanda.resource.base;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 基础信息
 */
@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper=false)
public class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false, name = "created_time")
    //@JSONField(serialize = false)
    private LocalDateTime createdTime;

    @Column(nullable = false, name = "updated_time")
//    @JSONField(serialize = false)
    private LocalDateTime updatedTime;


    /**
     * 创建人ID
     *//*
    @Transient
    @JSONField(serialize = false)
    private Long createdId;

    *//**
     * 创建时间
     *//*
    @Transient
    @JSONField(serialize = false)
    private Date createdTime = new Date();

    *//**
     * 修改人ID
     *//*
    @Transient
    @JSONField(serialize = false)
    private Long modifiedId;

    *//**
     * 修改时间
     *//*
    @Transient
    @JSONField(serialize = false)
    private Date modifiedTime;

    @Transient
    @JSONField(serialize = false)
    private Integer pageNum = 1;

    @Transient
    @JSONField(serialize = false)
    private Integer pageSize = 20;*/
}