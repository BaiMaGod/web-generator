package com.utils;

import cn.hutool.core.util.XmlUtil;
import com.model.Method;
import com.model.Module;
import com.model.Mold;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * xml文件解析工具
 */
public class XMLUtil {


    /**
     * 解析xml文件内容,获取模块列表
     * @param rootPath 文件夹路径
     * @return
     */
    public static List<Module> getModules(String rootPath){
        List<Module> modules = new ArrayList<>();

        List<File> files = FolderUtil.listFolder(rootPath); // 获取所有文件夹
        for (File file : files) {
            List<File> files1 = FileUtil.listFile(file.getAbsolutePath(), "module-info.xml");
            if(files1.isEmpty()){
                continue;
            }

            Document document = XmlUtil.readXML(files1.get(0));
            Element rootElement = XmlUtil.getRootElement(document);
            Element info = XmlUtil.getElement(rootElement, "info");

            Module module = new Module();
            module.setPath(file.getName());
            module.setName(XmlUtil.elementText(info, "name"));
            module.setNameHan(XmlUtil.elementText(info, "nameHan"));
            module.setDescription(XmlUtil.elementText(info, "description"));
            module.setTag(XmlUtil.elementText(info, "tag"));
            module.setLowPrice(Double.parseDouble(XmlUtil.elementText(info, "lowPrice")));
            module.setHighPrice(Double.parseDouble(XmlUtil.elementText(info, "highPrice")));
            module.setLowHour(Integer.parseInt(XmlUtil.elementText(info, "lowHour")));
            module.setHighHour(Integer.parseInt(XmlUtil.elementText(info, "highHour")));
            module.setAuthor(XmlUtil.elementText(info, "author"));
            module.setOnlineTime(XmlUtil.elementText(info, "onlineTime"));

            module.setChildMold(getMolds(rootPath,module.getPath(),XmlUtil.getElement(rootElement, "molds")));

            modules.add(module);
        }

        return modules;
    }

    /**
     * 获取xml文件中单个模块 配置的所有功能信息
     * @param moldsElement
     * @return
     */
    public static List<Mold> getMolds(String rootPath,String modulePath,Element moldsElement) {
        List<Mold> molds = new ArrayList<>();

        if(moldsElement == null){
            return molds;
        }

        NodeList elements = moldsElement.getElementsByTagName("mold");
        for (int i = 0; i < elements.getLength(); i++) {
            Node item = elements.item(i);
            String value = item.getTextContent();

            Mold mold = new Mold();
            mold.setPath(modulePath +"\\"+ value);
            mold.setModuleName(modulePath);
            mold.setName(value);
            infoMold(rootPath,mold);

            molds.add(mold);
        }

        return molds;
    }

    /**
     * 补全 单个模块 的所有信息
     * @param mold
     * @return
     */
    private static void infoMold(String rootPath,Mold mold) {
        List<File> files1 = FileUtil.listFile(rootPath + "\\" +mold.getPath(), "mold-info.xml");
        if(files1.isEmpty()){
            return;
        }

        Document document = XmlUtil.readXML(files1.get(0));
        Element rootElement = XmlUtil.getRootElement(document);
        Element info = XmlUtil.getElement(rootElement, "info");

        mold.setName(XmlUtil.elementText(info, "name"));
        mold.setNameHan(XmlUtil.elementText(info, "nameHan"));
        mold.setDescription(XmlUtil.elementText(info, "description"));
        mold.setTag(XmlUtil.elementText(info, "tag"));
        mold.setLowPrice(Double.parseDouble(XmlUtil.elementText(info, "lowPrice")));
        mold.setHighPrice(Double.parseDouble(XmlUtil.elementText(info, "highPrice")));
        mold.setLowHour(Integer.parseInt(XmlUtil.elementText(info, "lowHour")));
        mold.setHighHour(Integer.parseInt(XmlUtil.elementText(info, "highHour")));
        mold.setAuthor(XmlUtil.elementText(info, "author"));
        mold.setOnlineTime(XmlUtil.elementText(info, "onlineTime"));

        mold.setChildMethod(getMethods(mold,XmlUtil.getElement(rootElement, "methods")));

    }

    /**
     * 获取xml文件中配置的所有实现方法信息
     * @param mold
     * @param methodsElement
     * @return
     */
    public static List<Method> getMethods(Mold mold, Element methodsElement) {
        List<Method> methods = new ArrayList<>();

        if(methodsElement == null){
            return methods;
        }

        List<Element> methodsList = XmlUtil.getElements(methodsElement, "method");
        for (Element element : methodsList) {
            Method method = new Method();

            method.setModuleName(mold.getModuleName());
            method.setMoldName(mold.getName());

            method.setName(XmlUtil.elementText(element, "name"));
            method.setPath(mold.getPath() + "\\" + method.getName());
            method.setNameHan(XmlUtil.elementText(element, "nameHan"));
            method.setDescription(XmlUtil.elementText(element, "description"));
            method.setTag(XmlUtil.elementText(element, "tag"));
            method.setLowPrice(Double.parseDouble(XmlUtil.elementText(element, "lowPrice")));
            method.setLowHour(Integer.parseInt(XmlUtil.elementText(element, "lowHour")));
            method.setAuthor(XmlUtil.elementText(element, "author"));
            method.setOnlineTime(XmlUtil.elementText(element, "onlineTime"));

            methods.add(method);
        }

        return methods;
    }


    /**
     * 获取单个功能下的所有实现方式信息
     * @param path
     * @return
     */
    public static List<Method> getMethods(String path) {
        List<Method> methods = new ArrayList<>();

        List<File> files = FolderUtil.listFolder(path); // 获取所有文件夹
        for (File file1 : files) {
            List<File> files1 = FileUtil.listFile(path, "mold-info.xml");
            if(files1.isEmpty()){
                continue;
            }

            Document document = XmlUtil.readXML(files1.get(0));
            Element rootElement = XmlUtil.getRootElement(document);
            Element info = XmlUtil.getElement(rootElement, "info");

            Method method = new Method();
            method.setPath(path);
            method.setName(XmlUtil.elementText(info, "name"));
            method.setNameHan(XmlUtil.elementText(info, "nameHan"));
            method.setDescription(XmlUtil.elementText(info, "description"));
            method.setTag(XmlUtil.elementText(info, "tag"));
            method.setLowPrice(Double.parseDouble(XmlUtil.elementText(info, "lowPrice")));
            method.setHighPrice(Double.parseDouble(XmlUtil.elementText(info, "highPrice")));
            method.setLowHour(Integer.parseInt(XmlUtil.elementText(info, "lowHour")));
            method.setHighHour(Integer.parseInt(XmlUtil.elementText(info, "highHour")));
            method.setAuthor(XmlUtil.elementText(info, "author"));
            method.setOnlineTime(XmlUtil.elementText(info, "onlineTime"));

            methods.add(method);
        }

        return methods;
    }


}
