package com.mapper;

import com.form.ModuleForm;
import com.model.Method;
import com.model.Module;
import com.model.Mold;
import com.utils.FolderUtil;
import com.utils.XMLUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ModuleMapper{
    @Value("${moduleConfig.rootPath}")
    private String rootPath;    // 模块根路径



    public List<Module> list(ModuleForm.ListForm form) {

        return XMLUtil.getModules(form.getRootPath());
    }

    public List<Module> listAll() {

        return XMLUtil.getModules(rootPath);
    }

    /**
     * 通过模块相对路径获取模块对象
     * @param modulePath
     * @return
     */
    public Module getModuleByPath(String modulePath){
        List<Module> modules = listAll();

        for (Module module : modules) {
            if(module.getPath().equals(modulePath)){
                return module;
            }
        }

        return null;
    }

    /**
     * 通过功能相对路径获取功能对象
     * @param moldPath
     * @return
     */
    public Mold getMoldByPath(String moldPath){
        List<Module> modules = listAll();

        for (Module module : modules) {
            for (Mold mold : module.getChildMold()) {
                if(mold.getPath().equals(moldPath)){
                    return mold;
                }
            }
        }

        return null;
    }

    /**
     * 通过功能相对路径获取功能对象
     * @param methodPath
     * @return
     */
    public Method getMethodByPath(String methodPath){
        List<Module> modules = listAll();

        for (Module module : modules) {
            for (Mold mold : module.getChildMold()) {
                for (Method method : mold.getChildMethod()) {
                    if(method.getPath().equals(methodPath)){
                        return method;
                    }
                }
            }
        }

        return null;
    }


    public static void main(String[] args) {
//        ModuleMapper moduleMapper = new ModuleMapper();
//        List<Module> modules = moduleMapper.listAll();
//        for (Module module : modules) {
//            System.out.println(module);
//        }

        FolderUtil.copyFolderContent("D:\\E\\ideaProject\\module\\user\\base\\java","D:\\H\\WebCreateTest\\myWeb\\src\\main\\java\\com",false);
    }
}
