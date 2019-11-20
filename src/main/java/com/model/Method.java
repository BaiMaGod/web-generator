package com.model;

import lombok.Data;

@Data
public class Method extends BaseModule{
    protected String moduleName;    // 所属模块名
    protected String moldName;    // 所属功能名

    private String methodText;  // 方法内容
}
