package com.service.impl;


import com.form.CreateForm;
import com.mapper.CreateMapper;
import com.mapper.MethodMapper;
import com.mapper.ModuleMapper;
import com.model.Method;
import com.model.Module;
import com.model.Mold;
import com.result.Result;
import com.result.ResultStatus;
import com.service.CreateService;
import com.utils.FileUtil;
import com.utils.FolderUtil;
import com.utils.JavaMethodUtil;
import com.utils.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CreateServiceImpl implements CreateService {
    @Value("${moduleConfig.rootPath}")
    private String rootPath;    // 模块根路径
    @Value("${moduleConfig.javaMidPath}")
    private String projectJavaMidPath;
    @Value("${moduleConfig.resourceMidPath}")
    private String projectResourceMidPath;
    @Value("${moduleConfig.projectBasePath}")
    private String projectBasePath;

    @Autowired
    CreateMapper createMapper;
    @Autowired
    ModuleMapper moduleMapper;
    @Autowired
    MethodMapper methodMapper;


    @Override
    public Result base(CreateForm.BaseForm form) {

        return null;
    }

    @Override
    public Result web(CreateForm.WebForm form) {
        // 首先判断目标文件夹是否存在，能不能创建
        if(StringUtils.isEmpty(form.getCreatePath())){
            return Result.fail(ResultStatus.ERROR_Mkdir);
        }
        File createFolder = new File(form.getCreatePath());
        if(!createFolder.exists()){
            if(!createFolder.mkdirs()){
                return Result.fail(ResultStatus.ERROR_Mkdir);
            }
        }
        File projectRootFolder = new File(form.getCreatePath()+"\\"+form.getProjectName());
        int i = 1;
        while (projectRootFolder.exists()){
            projectRootFolder = new File(form.getCreatePath()+"\\"+form.getProjectName()+"("+(i++)+")");
        }
        if(!projectRootFolder.mkdirs()){
            return Result.fail(ResultStatus.ERROR_Mkdir);
        }

        // 接着生成 web项目 基础框架
        System.out.println("正在生成web项目基础框架...");
        FolderUtil.copyFolderContent(projectBasePath,projectRootFolder.getAbsolutePath(), true); // 复制文件夹下的所有文件
        System.out.println("生成成功");

        if(form.getModulePaths() == null){
            return Result.success(1);
        }

        Collections.sort(form.getModulePaths());

        // 逐个生成各模块、各功能、各方法
        for (String modulePath : form.getModulePaths()) {
//            modulePath = modulePath.replaceAll("\\\\","\\\\");
            String[] split = modulePath.split("\\\\");
            switch (split.length){
                case 1 :

                    addModule(moduleMapper.getModuleByPath(modulePath),projectRootFolder);

                    break;
                case 2 :

                    addMold(moduleMapper.getMoldByPath(modulePath),projectRootFolder);

                    break;
                case 3 :

                    addMethod(moduleMapper.getMethodByPath(modulePath),projectRootFolder);

                    break;
            }
        }

        return Result.success(1);
    }


    /**
     * 添加单个模块到指定项目（模块包含的所有功能方法）
     * @param module
     * @param projectRootFolder
     */
    private void addModule(Module module, File projectRootFolder) {
        if(module==null) return;
        System.out.println("正在生成模块["+module.getName()+"]...");

        // 添加 模块基础 代码
        addModuleBase(module,projectRootFolder);

        // 添加 模块各个功能 代码
        for (Mold mold : module.getChildMold()) {
            addMold(mold,projectRootFolder);
        }

        System.out.println("模块["+module.getName()+"]生成成功");
    }

    private void addModuleBase(Module module, File projectRootFolder) {
        if(module==null) return;
        System.out.println("正在生成模块["+module.getName()+"]基础代码...");

        // 添加模块基础 java 代码
        addModuleBaseJava(module,projectRootFolder);

        // 添加模块基础 resource 代码
        addModuleBaseResource(module,projectRootFolder);

        System.out.println("模块["+module.getName()+"]基础代码 生成成功");
    }

    /**
     * 添加模块基础 java 代码
     * @param module
     * @param projectRootFolder
     */
    private void addModuleBaseJava(Module module, File projectRootFolder) {
        if(module==null) return;
        System.out.println("正在生成模块["+module.getName()+"]基础 java 代码...");

        // 首先获取 模块基础代码模板 java目录对象
        File moduleBaseJavaFolder = new File(rootPath+"\\"+module.getPath()+"\\base\\java");
        // 判断是否存在模板代码,如存在则复制
        if(moduleBaseJavaFolder.exists()){
            // 列出 java目录下的所有文件夹
            List<File> javaPackage = FolderUtil.listFolder(moduleBaseJavaFolder.getAbsolutePath());
            for (File file : javaPackage) {
                // 将java目录下的所有文件 复制到 项目对应的目录 下的 模块文件夹中
                FolderUtil.copyFolderContent(file.getAbsolutePath(),projectRootFolder+"\\"+projectJavaMidPath+"\\"+file.getName(),false);
            }
        }

        System.out.println("模块["+module.getName()+"]基础 java 代码 生成成功");
    }

    /**
     * 添加模块基础 resource 代码
     * @param module
     * @param projectRootFolder
     */
    private void addModuleBaseResource(Module module, File projectRootFolder) {
        if(module==null) return;
        System.out.println("正在生成模块["+module.getName()+"]基础 resource 代码...");

        // 首先获取 模块基础代码模板 resource 目录对象
        File moduleBaseJavaFolder = new File(rootPath+"\\"+module.getPath()+"\\base\\resources");
        // 判断是否存在模板代码,如存在则复制
        if(moduleBaseJavaFolder.exists()){
            // 将 resource 目录下的所有文件 复制到 项目对应的目录 下的 模块文件夹中
            FolderUtil.copyFolderContent(moduleBaseJavaFolder.getAbsolutePath(),projectRootFolder+"\\"+projectResourceMidPath,false);
        }

        System.out.println("模块["+module.getName()+"]基础 resource 代码 生成成功");
    }


    /**
     * 添加单个功能到指定项目（功能包含的所有方法）
     * @param mold
     * @param projectRootFolder
     */
    private void addMold(Mold mold, File projectRootFolder) {
        if(mold==null) return;
        System.out.println("正在生成功能["+mold.getPath()+"]...");

        // 添加 功能基础代码（仅类）
        addMoldBase(mold,projectRootFolder);

        // 添加 功能各个方法 代码
        for (Method method : mold.getChildMethod()) {
            addMethod(method,projectRootFolder);
        }

        System.out.println("功能["+mold.getPath()+"] 生成成功");
    }

    /**
     * 添加 功能基础代码
     * @param mold
     * @param projectRootFolder
     */
    private void addMoldBase(Mold mold, File projectRootFolder) {
        if(mold==null) return;
        System.out.println("正在生成功能["+mold.getPath()+"]基础代码...");

        // 添加 功能基础代码
        addModuleBase(moduleMapper.getModuleByPath(mold.getModuleName()),projectRootFolder);

        // 添加功能 java基础 代码
        addMoldBaseJava(mold,projectRootFolder);

        // 添加功能 resource基础 代码
        addMoldBaseResource(mold,projectRootFolder);

        System.out.println("功能["+mold.getPath()+"]基础代码 生成成功");
    }

    /**
     * 添加功能 java基础 代码 (仅类)
     * @param mold
     * @param projectRootFolder
     */
    private void addMoldBaseJava(Mold mold, File projectRootFolder) {
        System.out.println("正在生成功能["+mold.getPath()+"]基础 java 代码...");

        // 首先获取 模块基础代码模板 java目录对象
        File moldBaseJavaFolder = new File(rootPath+"\\"+mold.getPath()+"\\java");
        // 判断是否存在模板代码,如存在则复制
        if(moldBaseJavaFolder.exists()){
            // 列出 java目录下的所有文件夹
            List<File> javaPackage = FolderUtil.listFolder(moldBaseJavaFolder.getAbsolutePath());
            for (File package1 : javaPackage) {
                // 将java目录下的所有文件 复制到 项目对应的目录 下的 模块文件夹中
                String destPath = projectRootFolder + "\\" + projectJavaMidPath + "\\" + package1.getName() + "\\"+ mold.getModuleName();
                String destJavaComPath = projectRootFolder + "\\" + projectJavaMidPath + "\\" + package1.getName();
                // config 包下的类直接复制
                if(package1.getName().equals("config")){
                    FolderUtil.copyFolderContent(package1.getAbsolutePath(),destJavaComPath,false);
                }
                // model 包下的类直接复制
                else if(package1.getName().equals("model")){
                    FolderUtil.copyFolderContent(package1.getAbsolutePath(),destPath,false);
                }
                // utils 包下的类直接复制
                else if(package1.getName().equals("utils")){
                    FolderUtil.copyFolderContent(package1.getAbsolutePath(),destJavaComPath,false);
                }
                // result 包下的类特殊处理
                else if(package1.getName().equals("result")){
                    File file = new File(package1.getAbsolutePath()+"\\ResultStatus.java");
                    if(file.exists()){
                        String targetPath = projectRootFolder + "\\" + projectJavaMidPath + "\\" + package1.getName() + "\\ResultStatus.java";
                        methodMapper.addEnum(file.getAbsolutePath(),targetPath);
                    }
                }else{
                    // 包下面的所有 java 类
                    List<File> javaFiles = FileUtil.listFile(package1.getAbsolutePath(), ".java");
                    for (File javaFile : javaFiles) {
                        copyJavaShell(mold,"\\"+package1.getName()+"\\",javaFile,projectRootFolder);
                    }

                    // 子包下的所有Java类
                    List<File> childPackage = FolderUtil.listFolder(package1.getAbsolutePath());
                    for (File package2 : childPackage) {
                        javaFiles = FileUtil.listFile(package2.getAbsolutePath(), ".java");
                        for (File javaFile : javaFiles) {
                            copyJavaShell(mold,"\\"+package1.getName()+"\\"+package2.getName()+"\\",javaFile,projectRootFolder);
                        }
                    }
                }
            }
        }

        System.out.println("功能["+mold.getPath()+"]基础 java 代码 生成成功");
    }

    private void copyJavaShell(Mold mold,String packagePath,File javaFile,File projectRootFolder){
        System.out.println("正在生成功能["+mold.getPath()+"]基础 java 代码 ["+packagePath+javaFile.getName()+"]类外壳...");

        // 如果 该类 在项目中不存在，则生成类外壳
        if(!projectJavaHave(mold,packagePath,javaFile.getName(),projectRootFolder)){
            String classShellText = "";
            if(javaFile.getName().indexOf("Service.java")>0 || javaFile.getName().indexOf("Mapper.java")>0){
                classShellText = JavaMethodUtil.getInterfaceShellText(javaFile.getAbsolutePath());
            }else{
                classShellText = JavaMethodUtil.getClassShellText(javaFile.getAbsolutePath());
            }

            String targetFileFolder = projectRootFolder.getAbsolutePath() + "\\" + projectJavaMidPath+packagePath;
            String targetFilePath = targetFileFolder + javaFile.getName();

            File targetFile = new File(targetFilePath);

            cn.hutool.core.io.FileUtil.touch(targetFilePath);
//                targetFile.createNewFile();
            cn.hutool.core.io.FileUtil.writeString(classShellText,targetFile,"UTF-8");
        }

        System.out.println("功能["+mold.getPath()+"]基础 java 代码 ["+packagePath+javaFile.getName()+"]类外壳 生成成功");
    }


    /**
     * 判断项目中是否含有 mold 功能 下面的java类
     * @param javaFileName
     * @param projectJavaFolder
     * @return
     */
    private boolean projectJavaHave(Mold mold,String packageName,String javaFileName, File projectJavaFolder) {
        String targetFilePath = projectJavaFolder.getAbsolutePath() + "\\" + projectJavaMidPath+packageName+javaFileName;
        File targetFile = new File(targetFilePath);
        if(targetFile.exists()){
            return true;
        }

        return false;
    }



    /**
     * 添加 功能 resource 代码 （复制）
     * @param mold
     * @param projectRootFolder
     */
    private void addMoldBaseResource(Mold mold, File projectRootFolder) {
        if(mold==null) return;
        System.out.println("正在生成 功能["+mold.getName()+"] resource 代码...");

        // 首先获取 功能 基础代码模板 resource 目录对象
        File moldBaseJavaFolder = new File(rootPath+"\\"+mold.getModuleName()+"\\"+mold.getPath()+"\\resources");
        // 判断是否存在模板代码,如存在则复制
        if(moldBaseJavaFolder.exists()){
            // 将java目录下的所有文件 复制到 项目对应的目录 下的 模块文件夹中
            FolderUtil.copyFolderContent(moldBaseJavaFolder.getAbsolutePath(),projectRootFolder+"\\"+projectResourceMidPath,false);
        }

        System.out.println("功能["+mold.getName()+"] resource 代码 生成成功");

    }

    /**
     * 添加单个方法到指定项目
     * @param method
     * @param projectRootFolder
     */
    private void addMethod(Method method, File projectRootFolder) {
        if(method==null) return;
        System.out.println("正在生成方法["+method.getPath()+"]...");

        // 添加 功能基础代码
        addMoldBase(moduleMapper.getMoldByPath(method.getModuleName()+"\\"+method.getMoldName()),projectRootFolder);

        // 添加 方法 java代码
        addMethodJava(method,projectRootFolder);

        // 添加 方法 resource 代码
        addMethodResource(method,projectRootFolder);

        System.out.println("方法["+method.getPath()+"] 生成成功");
    }

    /**
     * 添加 方法 java代码
     * @param method
     * @param projectRootFolder
     */
    private void addMethodJava(Method method, File projectRootFolder) {
        System.out.println("正在生成方法["+method.getPath()+"] java代码...");

        String javaPath = rootPath+"\\"+method.getModuleName()+"\\"+method.getMoldName()+"\\java";

        // 添加 controller 方法
        addControllerMethodJava(javaPath,method,projectRootFolder);

        // 添加 form 方法
        addFormMethodJava(javaPath,method,projectRootFolder);

        // 添加 service 方法
        addServiceMethodJava(javaPath,method,projectRootFolder);

        // 添加 mapper 方法
        addMapperMethodJava(javaPath,method,projectRootFolder);

        System.out.println("方法["+method.getPath()+"] java代码 生成成功");
    }

    private void addControllerMethodJava(String javaPath,Method method, File projectRootFolder) {
        System.out.println("正在生成方法["+method.getPath()+"] controller 包下的 java代码...");

        List<File> controllerFolders = FolderUtil.listFolder(javaPath,"controller");
        if(!controllerFolders.isEmpty()){
            File controllerFolder = controllerFolders.get(0);
            List<File> controllerFiles = FileUtil.listFile(controllerFolder.getAbsolutePath(), StringUtils.capitalize(method.getMoldName()));
            if(!controllerFiles.isEmpty()){     // controller java文件 存在
                copyJavaClassMethod(controllerFiles.get(0),method,projectRootFolder,"controller");
            }

            List<File> controllerChildFolders = FolderUtil.listFolder(controllerFolder.getAbsolutePath());
            for (File controllerChildFolder : controllerChildFolders) {
                controllerFiles = FileUtil.listFile(controllerChildFolder.getAbsolutePath(), StringUtils.capitalize(method.getMoldName()));
                if(!controllerFiles.isEmpty()){     // controller java文件 存在
                    copyJavaClassMethod(controllerFiles.get(0),method,projectRootFolder,"controller\\"+controllerChildFolder.getName());
                }
            }

        }

        System.out.println("方法["+method.getPath()+"] controller 包下的 java代码 生成成功");
    }

    private void addFormMethodJava(String javaPath, Method method, File projectRootFolder) {
        System.out.println("正在生成方法["+method.getPath()+"] form 包下的 java代码...");

        List<File> controllerFolders = FolderUtil.listFolder(javaPath,"form");
        if(!controllerFolders.isEmpty()){
            File controllerFolder = controllerFolders.get(0);
            List<File> controllerFiles = FileUtil.listFile(controllerFolder.getAbsolutePath(), StringUtils.capitalize(method.getMoldName()));
            if(!controllerFiles.isEmpty()){     //
                copyJavaClassMethod(controllerFiles.get(0),method,projectRootFolder,"form");
            }

            List<File> controllerChildFolders = FolderUtil.listFolder(controllerFolder.getAbsolutePath());
            for (File controllerChildFolder : controllerChildFolders) {
                controllerFiles = FileUtil.listFile(controllerChildFolder.getAbsolutePath(), StringUtils.capitalize(method.getMoldName()));
                if(!controllerFiles.isEmpty()){     //
                    copyJavaClassMethod(controllerFiles.get(0),method,projectRootFolder,"form\\"+controllerChildFolder.getName());
                }
            }

        }

        System.out.println("方法["+method.getPath()+"] form 包下的 java代码 生成成功");
    }

    private void addServiceMethodJava(String javaPath, Method method, File projectRootFolder) {
        System.out.println("正在生成方法["+method.getPath()+"] service 包下的 java代码...");

        List<File> controllerFolders = FolderUtil.listFolder(javaPath,"service");
        if(!controllerFolders.isEmpty()){
            File controllerFolder = controllerFolders.get(0);
            List<File> controllerFiles = FileUtil.listFile(controllerFolder.getAbsolutePath(), StringUtils.capitalize(method.getMoldName()));
            for (File javaServiceFile : controllerFiles) {
                if(javaServiceFile.getName().indexOf("Service.java")>0){    // 是接口
                    copyJavaInterfaceMethod(javaServiceFile,method,projectRootFolder,"service");
                }else{
                    copyJavaClassMethod(javaServiceFile,method,projectRootFolder,"service");
                }
            }


            List<File> controllerChildFolders = FolderUtil.listFolder(controllerFolder.getAbsolutePath());
            for (File controllerChildFolder : controllerChildFolders) {
                controllerChildFolders = FileUtil.listFile(controllerChildFolder.getAbsolutePath(), StringUtils.capitalize(method.getMoldName()));
                for (File javaServiceFile : controllerChildFolders) {
                    if(javaServiceFile.getName().indexOf("Service.java")>0){    // 是接口
                        copyJavaInterfaceMethod(javaServiceFile,method,projectRootFolder,"service\\"+controllerChildFolder.getName());
                    }else{
                        copyJavaClassMethod(javaServiceFile,method,projectRootFolder,"service\\"+controllerChildFolder.getName());
                    }
                }
            }

        }

        System.out.println("方法["+method.getPath()+"] service 包下的 java代码 生成成功");
    }

    private void addMapperMethodJava(String javaPath, Method method, File projectRootFolder) {
        System.out.println("正在生成方法["+method.getPath()+"] mapper 包下的 java代码...");

        List<File> controllerFolders = FolderUtil.listFolder(javaPath,"mapper");
        if(!controllerFolders.isEmpty()){
            File controllerFolder = controllerFolders.get(0);
            List<File> controllerFiles = FileUtil.listFile(controllerFolder.getAbsolutePath(), StringUtils.capitalize(method.getMoldName()));
            if(!controllerFiles.isEmpty()){     //
                copyJavaInterfaceMethod(controllerFiles.get(0),method,projectRootFolder,"mapper");
            }

            List<File> controllerChildFolders = FolderUtil.listFolder(controllerFolder.getAbsolutePath());
            for (File controllerChildFolder : controllerChildFolders) {
                controllerFiles = FileUtil.listFile(controllerChildFolder.getAbsolutePath(), StringUtils.capitalize(method.getMoldName()));
                if(!controllerFiles.isEmpty()){     //
                    copyJavaInterfaceMethod(controllerFiles.get(0),method,projectRootFolder,"mapper\\"+controllerChildFolder.getName());
                }
            }

        }

        System.out.println("方法["+method.getPath()+"] mapper 包下的 java代码 生成成功");
    }

    /**
     * 拷贝java类文件中的方法到指定
     * @param javaFile
     * @param method
     * @param projectRootFolder
     * @param packageName
     */
    private void copyJavaClassMethod(File javaFile, Method method, File projectRootFolder,String packageName) {
        List<Method> methods = getClassAllMethodText(method);
        String targetJavaFilePath = projectRootFolder+"\\"+projectJavaMidPath+"\\"+packageName+"\\"+javaFile.getName();

        // 先判断目标文件里是否已经存在
        File targetJavaFile = new File(targetJavaFilePath);
        if(targetJavaFile.exists()){
            // 判断目标文件里是否已经存在该方法,如果存在，返回
            List<String> targetClassAllMethod = JavaMethodUtil.getClassAllMethod(targetJavaFilePath, methods);
            if(targetClassAllMethod!=null && !targetClassAllMethod.isEmpty()){
                return;
            }

            List<String> classAllMethod = JavaMethodUtil.getClassAllMethod(javaFile.getAbsolutePath(), methods);

            addMethodToJavaFile(classAllMethod,targetJavaFile);
        }

    }

    /**
     * 拷贝java接口文件中的方法到指定
     * @param javaFile
     * @param method
     * @param projectRootFolder
     * @param packageName
     */
    private void copyJavaInterfaceMethod(File javaFile, Method method, File projectRootFolder, String packageName) {
        List<Method> methods = getClassAllMethodText(method);

        String targetJavaFilePath = projectRootFolder+"\\"+projectJavaMidPath+"\\"+packageName+"\\"+javaFile.getName();

        // 先判断目标文件里是否已经存在
        File targetJavaFile = new File(targetJavaFilePath);
        if(targetJavaFile.exists()){
            // 判断目标文件里是否已经存在该方法,如果存在，返回
            List<String> targetClassAllMethod = JavaMethodUtil.getInterfaceAllMethod(targetJavaFilePath, methods);
            if(targetClassAllMethod!=null && !targetClassAllMethod.isEmpty()){
                return;
            }

            List<String> classAllMethod = JavaMethodUtil.getInterfaceAllMethod(javaFile.getAbsolutePath(), methods);

            addMethodToJavaFile(classAllMethod,targetJavaFile);
        }
    }

    /**
     * 封装单个方法对象为list，并添加首字母大写的方法
     * @param method
     * @return
     */
    List<Method> getClassAllMethodText(Method method){
        List<Method> methods = new ArrayList<>();
        methods.add(method);
        Method method1 = new Method();
        method1.setName(StringUtils.capitalize(method.getName()));
        methods.add(method1);

        return methods;
    }

    /**
     * 添加方法文本到java类文件中
     * @param methodTexts
     * @param targetJavaFile
     */
    private void addMethodToJavaFile(List<String> methodTexts,File targetJavaFile){
        for (String methodText : methodTexts) {
            if(targetJavaFile.exists()){
                List<String> targetJavaFileContent = FileUtil.fileContent(targetJavaFile.getAbsolutePath());
                int lastIndex = ListUtil.getLastIndex(targetJavaFileContent, "}");
                if(lastIndex>0){
                    targetJavaFileContent.add(lastIndex,methodText);

                    String newTargetJavaFileContent = ListUtil.concatStr(targetJavaFileContent);
                    cn.hutool.core.io.FileUtil.writeString(newTargetJavaFileContent,targetJavaFile,"UTF-8");
                }
            }
        }
    }




    /**
     * 添加 方法 resource 代码    【暂未实现】
     * @param method
     * @param projectRootFolder
     */
    private void addMethodResource(Method method, File projectRootFolder) {


    }



}
