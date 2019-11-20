package com.model;

import lombok.Data;

import java.util.List;

@Data
public class BaseModule {
    protected String path;    // 相对路径名，不包含根路径

    protected String name;    // 模块名,英文
    protected String nameHan;    // 模块名，中文
    protected String tag;    // 模块标签
    protected String description;    // 模块描述



    protected double lowPrice;   // 功能最底价格
    protected double highPrice;   // 功能最高价格
    protected int lowHour;   // 功能正常开发所需最少小时数
    protected int highHour;   // 功能正常开发所需最多小时数

    protected String author;    // 作者
    protected String onlineTime;    // 上线时间
}
