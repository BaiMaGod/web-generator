package com.service.impl;

import com.form.CreateForm;
import com.mapper.CreateMapper;
import com.mapper.MethodMapper;
import com.mapper.ModuleMapper;
import com.mapper.SqlMapper;
import com.model.Method;
import com.model.Mold;
import com.result.Result;
import com.result.ResultStatus;
import com.service.CreateService;
import com.utils.FileUtil;
import com.utils.FolderUtil;
import com.utils.XMLUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.Collections;
import java.util.List;

@Service
public class AddServiceImpl implements AddService {
    @Value("${moduleConfig.rootPath}")
    private String rootPath;    // 模块根路径
    @Value("${moduleConfig.javaMidPath}")
    private String projectJavaMidPath;
    @Value("${moduleConfig.resourceMidPath}")
    private String projectResourceMidPath;
    @Value("${moduleConfig.projectBasePath}")
    private String projectBasePath;


    @Autowired
    CreateServiceImpl createService;
    @Autowired
    CreateMapper createMapper;
    @Autowired
    ModuleMapper moduleMapper;
    @Autowired
    MethodMapper methodMapper;
    @Autowired
    SqlMapper sqlMapper;


    /**
     * 添加功能到已有 web 项目
     * @param form
     * @return
     */
    @Override
    public Result add(CreateForm.AddForm form) {
        // 首先判断目标文件夹是否存在，能不能创建
        if(StringUtils.isEmpty(form.getProjectPath())){
            return Result.fail(ResultStatus.ERROR_Mkdir);
        }

        File projectRootFolder = new File(form.getProjectPath());
        if(form.getModulePaths() == null){
            return Result.success(1);
        }

        Collections.sort(form.getModulePaths());

        // 逐个生成各模块、各功能、各方法
        for (String modulePath : form.getModulePaths()) {
            String[] split = modulePath.split("\\\\");
            switch (split.length){
                case 1 :

                    createService.addModule(moduleMapper.getModuleByPath(modulePath),projectRootFolder);

                    break;
                case 2 :

                    createService.addMold(moduleMapper.getMoldByPath(modulePath),projectRootFolder);

                    break;
                case 3 :

                    createService.addMethod(moduleMapper.getMethodByPath(modulePath),projectRootFolder);

                    break;
            }
        }

        return Result.success(1);
    }

}
