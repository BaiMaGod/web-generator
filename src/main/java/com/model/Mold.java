package com.model;

import lombok.Data;

import java.util.List;

@Data
public class Mold extends BaseModule{

    protected String moduleName;    // 所属模块名

    protected List<Method> childMethod;  // 功能方法

}
