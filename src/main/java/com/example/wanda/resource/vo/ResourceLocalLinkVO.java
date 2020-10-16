package com.example.wanda.resource.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

/**
 * @description: 资源本地链接VO
 * @author: yinminxin
 * @create: 2020-09-28
 **/
public class ResourceLocalLinkVO extends BaseRowModel  {

    @ExcelProperty("resource_name")
    private String resourceName;
    @ExcelProperty("local_link")
    private String localLink;

    @Override
    public String toString() {
        return "ResourceLocalLinkVO{" +
                "resourceName='" + resourceName + '\'' +
                ", localLink='" + localLink + '\'' +
                '}';
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getLocalLink() {
        return localLink;
    }

    public void setLocalLink(String localLink) {
        this.localLink = localLink;
    }
}
