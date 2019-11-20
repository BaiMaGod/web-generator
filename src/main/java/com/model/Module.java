package com.model;

import lombok.Data;

import java.util.List;

@Data
public class Module extends BaseModule{

    protected List<Mold> childMold;  // 模块子功能
}
