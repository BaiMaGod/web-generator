package com.mapper;

import com.utils.JavaEnumUtil;
import com.utils.ListUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MethodMapper {
    @Value("${moduleConfig.rootPath}")
    private String rootPath;    // 模块根路径


    /**
     * 从原 枚举类中，添加 目标枚举类中没有的 枚举项
     * @param srcFilePath
     * @param destFilePath
     */
    public void addEnum(String srcFilePath,String destFilePath){
        List<String> srcEnumNames = JavaEnumUtil.getClassAllEnumName(srcFilePath);
        List<String> destEnumNames = JavaEnumUtil.getClassAllEnumName(destFilePath);

        List<String> surplusEnumNames = ListUtil.exclude(srcEnumNames, destEnumNames);

        List<String> classAllEnum = JavaEnumUtil.getClassAllEnum(srcFilePath, surplusEnumNames);

        JavaEnumUtil.addEnumToFile(destFilePath,classAllEnum);

    }

}
