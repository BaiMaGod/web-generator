package com.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件夹 操作 工具类
 */
public class FolderUtil {

    /**
     * 获取 path 路径下的全部文件夹
     * @param path
     * @return
     */
    public static List<File> listFolder(String path){
        List<File> files = FileUtil.listFile(path);
        List<File> fileList = new ArrayList<>();

        for (File file : files) {
            if(file.isDirectory()){
                fileList.add(file);
            }
        }

        return fileList;
    }
    /**
     * 获取 path 路径下的全部文件夹
     * @param path
     * @return List<String>
     */
    public static List<String> listFolderStr(String path){
        List<File> files = FileUtil.listFile(path);
        List<String> fileList = new ArrayList<>();

        for (File file : files) {
            if(file.isDirectory()){
                fileList.add(file.getName());
            }
        }

        return fileList;
    }



    /**
     * 获取 path 路径下 包含指定字符的 文件夹
     * @param path
     * @return
     */
    public static List<File> listFolder(String path, String word){
        List<File> files = listFolder(path);
        List<File> fileList = new ArrayList<>();

        for (File file : files) {
            if(file.getName().indexOf(word)>=0){
                fileList.add(file);
            }
        }

        return fileList;
    }
    /**
     * 获取 path 路径下 包含指定字符的 文件夹
     * @param path
     * @return List<String>
     */
    public static List<String> listFolderStr(String path,String word){
        List<String> files = listFolderStr(path);
        List<String> fileList = new ArrayList<>();

        for (String file : files) {
            if(file.indexOf(word)>=0){
                fileList.add(file);
            }
        }

        return fileList;
    }
    /**
     * 获取 path 路径下 包含指定字符的 文件（包括文件夹），并去掉前缀
     * @param path
     * @return List<File>
     */
    public static List<String> listFolderStr(String path,String word,String prefix){
        List<String> files = listFolderStr(path,word);
        List<String> fileList = new ArrayList<>();

        for (String file : files) {
            fileList.add(file.replaceFirst(prefix,""));
        }

        return fileList;
    }

    public static void copyFolderContent(String srcPath, String destPath, boolean isOverride){
        File file = new File(srcPath);
        File[] files = file.listFiles();
        File destFolder = new File(destPath);
        if(!destFolder.exists()){
            destFolder.mkdirs();
        }
        for (File file1 : files) {

            cn.hutool.core.io.FileUtil.copy(file1.getAbsolutePath(),destPath,isOverride);
        }
    }

}
