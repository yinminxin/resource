package com.example.wanda.resource.base;

import tk.mybatis.mapper.common.*;
import tk.mybatis.mapper.common.special.InsertListMapper;

import java.io.Serializable;

/**
 * BaseMapper接口：使mapper包含完整的CRUD方法<br>
 * MySqlMapper接口：使mapper支持MySQL特有的批量插入和返回自增字段<br>
 * IdsMapper接口：使mapper支持批量ID操作<br>
 * ConditionMapper接口：使mapper支持Condition类型参数<br>
 * ExampleMapper
 * InsertListMapper 批量插入，支持批量插入的数据库可以使用，例如MySQL,H2等，另外该接口限制实体包含id属性并且必须为自增列
 * @param <T> 实体类.class
 */
public interface MyMapper<T, PK extends Serializable> extends BaseMapper<T>,
        MySqlMapper<T>,
        IdsMapper<T>,
        ConditionMapper<T>,
        ExampleMapper<T>,
        InsertListMapper<T> {
}
