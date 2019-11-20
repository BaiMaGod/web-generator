package com.service.impl;

import com.form.CreateForm;
import com.mapper.CreateMapper;
import com.mapper.ModuleMapper;
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

//@Service
public class CreateServiceImpl2 implements CreateService {
    @Value("${moduleConfig.rootPath}")
    private String rootPath;    // 模块根路径
    @Autowired
    CreateMapper createMapper;
    @Autowired
    ModuleMapper moduleMapper;

    @Override
    public Result base(CreateForm.BaseForm form) {
        // 首先生成 web 项目基础

        return null;
    }

    @Override
    public Result web(CreateForm.WebForm form) {
    /*    // 首先判断目标文件夹是否存在，能不能创建
        if(StringUtils.isEmpty(form.getCreatePath())){
            return Result.fail(ResultStatus.ERROR_Mkdir);
        }
        File createFolder = new File(form.getCreatePath());
        if(!createFolder.exists()){
            if(!createFolder.mkdir()){
                return Result.fail(ResultStatus.ERROR_Mkdir);
            }
        }
        File projectFolder = new File(form.getCreatePath()+"\\"+form.getProjectName());
        if(!projectFolder.exists()){
            if(!projectFolder.mkdir()){
                return Result.fail(ResultStatus.ERROR_Mkdir);
            }
        }

        // 接着生成 web项目 基础框架
        System.out.println("正在生成web项目基础框架...");
        FolderUtil.copyFolderContent(rootPath + "\\base",projectFolder.getAbsolutePath(), true); // 复制文件夹下的所有文件
        System.out.println("生成成功");

        if(form.getModulePaths() == null){
            return Result.success(1);
        }

        Collections.sort(form.getModulePaths());

        // 逐个生成各模块、各功能、各方法
        for (String modulePath : form.getModulePaths()) {
            System.out.println("正在生成模块["+modulePath+"]...");
            createModule(modulePath,projectFolder);
            System.out.println("生成成功");
        }*/

        return Result.success(1);
    }

    /**
     * 根据模块路径，生成 模块
     * @param modulePath    模块路径，相对
     * @param projectFolder 项目根目录 File对象
     */
    private void createModule(String modulePath, File projectFolder) {
        // 找到 模块 模板代码 位置
        File moduleFile = new File(rootPath+"\\"+modulePath);

        String[] split = modulePath.split("\\\\");
        // 判断是模块、功能、方法
        if(split.length == 1){
            // 生成模块基础代码
            createModuleBase(moduleFile, projectFolder);

            // 添加模块各功能代码
            addModuleAllMold(modulePath,projectFolder);

        }else if(split.length == 2){
            createMold(modulePath, projectFolder);
        }else if(split.length == 3){
            createMethod(modulePath, projectFolder);
        }

    }

    /**
     * 添加模块各功能代码
     * @param modulePath    模块路径，相对
     * @param projectFolder 项目文件夹
     */
    private void addModuleAllMold(String modulePath, File projectFolder) {
        List<Mold> childMold = moduleMapper.getModuleByPath(modulePath).getChildMold();

        for (Mold mold : childMold) {
            addMold(modulePath,mold,projectFolder);
        }
    }

    /**
     * 添加功能
     * @param mold
     * @param projectFolder
     */
    private void addMold(String modulePath,Mold mold, File projectFolder) {
        // 添加java文件
        List<File> javaFolders = FolderUtil.listFolder(rootPath+"\\"+mold.getPath(), "java");
        if(!javaFolders.isEmpty()){
            addMoldJavaFile(javaFolders.get(0),mold,projectFolder,"\\src\\main\\java\\com",modulePath);
        }

        // 添加资源文件
        List<File> resourceFolders = FolderUtil.listFolder(rootPath+"\\"+mold.getPath(), "resource");
        if(!resourceFolders.isEmpty()){
//            addMoldJavaResource(resourceFolders.get(0),mold,projectFolder,"\\src\\main\\resources",modulePath);
        }

        List<Method> childMethod = mold.getChildMethod();
    }

    private void addMoldJavaFile(File javaFolder,Mold mold, File projectFolder, String relationPath, String moduleName) {
        File projectJavaFolder = new File(projectFolder.getAbsolutePath()+relationPath);

        List<File> files = FolderUtil.listFolder(javaFolder.getAbsolutePath()); // 获取javaFolder目录下的所有文件夹
        for (File file : files) {
            File projectModule = new File(projectJavaFolder.getAbsolutePath() + "\\" + file.getName() + moduleName);    // 创建模块包名
            if(!projectModule.exists()){
                projectModule.mkdir();
            }

            // 将file包下的类复制到项目中


            //将file包下的方法复制到项目中

            FolderUtil.copyFolderContent(file.getAbsolutePath(),projectModule.getAbsolutePath(),false);

        }
    }

    private void createModuleBase(File moduleFile, File projectFolder) {
        List<File> baseFolders = FolderUtil.listFolder(moduleFile.getAbsolutePath(), "base");
        if(baseFolders.isEmpty()){
            return;
        }

        List<File> javaFolders = FolderUtil.listFolder(baseFolders.get(0).getAbsolutePath(), "java");
        if(!javaFolders.isEmpty()){
            createProjectFile(javaFolders.get(0),projectFolder,"\\src\\main\\java\\com",moduleFile.getName());
        }

        List<File> resourceFolders = FolderUtil.listFolder(baseFolders.get(0).getAbsolutePath(), "resource");
        if(!resourceFolders.isEmpty()){
            createProjectFile(resourceFolders.get(0),projectFolder,"\\src\\main\\resources",moduleFile.getName());
        }
    }

    /**
     * 生成java文件
     * @param javaFolder java文件夹
     * @param projectFolder 项目文件夹
     * @param relationPath 项目中间相对路径
     */
    private void createProjectFile(File javaFolder,File projectFolder,String relationPath,String moduleName){
        File projectJavaFolder = new File(projectFolder.getAbsolutePath()+relationPath);

        File[] files = javaFolder.listFiles();  // 获取base目录下的所有文件
        for (File file : files) {
//            if(!projectJavaHave(file.getName()+moduleName,projectJavaFolder)){   // 没有该目录
                File projectModule = new File(projectJavaFolder.getAbsolutePath() + "\\" + file.getName() + moduleName);
                projectModule.mkdir();

                FolderUtil.copyFolderContent(file.getAbsolutePath(),projectModule.getAbsolutePath(),false);
//            }else{

//            }

        }

    }

    private boolean projectJavaHave(String fileName, File projectJavaFolder) {
        File projectPackage = new File(projectJavaFolder.getAbsolutePath() + "\\" + fileName);
        if(projectJavaFolder.exists()){
            return true;
        }

        return false;
    }

    /**
     * 根据功能路径，生成功能
     * @param moldPath    功能路径，相对
     * @param projectFolder 项目根目录 File对象
     */
    private void createMold(String moldPath, File projectFolder) {
        // 找到 模块 模板代码 位置
        File moduleFile = new File(rootPath+"\\"+moldPath);


    }

    private void createMethod(String methodPath, File projectFolder) {


    }


    private boolean packageExist(String javaPackage){

        return true;
    }

    private boolean resourceExist(String javaResource){

        return true;
    }
}
