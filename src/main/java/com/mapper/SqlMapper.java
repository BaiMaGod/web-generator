package com.mapper;

import cn.hutool.core.io.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;

@Component
public class SqlMapper {
    @Value("${moduleConfig.rootPath}")
    private String rootPath;    // 模块根路径
    @Value("${moduleConfig.resourceMidPath}")
    private String projectResourceMidPath;
    private String sqlFilePath = "\\table.sql";    // sql文件路径

    public void addSqlToFile(String projectRootFolder,String sqlText){
        if(StringUtils.isEmpty(sqlText)){
            return;
        }
        File file = new File(projectRootFolder+"\\"+projectResourceMidPath+sqlFilePath);
        if(!file.exists()){
            FileUtil.touch(file);
        }

        FileUtil.appendUtf8String(sqlText,file);

    }
}
