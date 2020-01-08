package com.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 文件 操作 工具类
 */
public class FileUtil {

    /**
     * 获取 path 路径下的全部文件（包括文件夹）
     * @param path
     * @return List<File>
     */
    public static List<File> listFile(String path){
        File file = new File(path);

        if(file.isDirectory()){
            return Arrays.asList(file.listFiles());
        }

        return new ArrayList<>();
    }
    /**
     * 获取 path 路径下的全部文件（包括文件夹）
     * @param path
     * @return List<String>
     */
    public static List<String> listFileStr(String path){
        File file = new File(path);

        if(file.isDirectory()){
            return Arrays.asList(file.list());
        }

        return new ArrayList<>();
    }
    /**
     * 获取 path 路径下的全部文件名，去掉后缀
     * @param path
     * @return List<String>
     */
    public static List<String> listFileName(String path){
        List<String> strings = listFileStr(path);
        List<String> fileList = new ArrayList<>();

        for (String str : strings) {
            int index = str.indexOf('.');
            if(index<0){
                fileList.add(str);
            }else{
                fileList.add(str.substring(0,index));
            }

        }

        return fileList;
    }


    /**
     * 获取 path 路径下 包含指定字符的 文件（包括文件夹）
     * @param path
     * @return List<File>
     */
    public static List<File> listFile(String path,String word){
        List<File> files = listFile(path);
        List<File> fileList = new ArrayList<>();

        for (File file : files) {
            if(file.getName().indexOf(word)>=0){
                fileList.add(file);
            }
        }

        return fileList;
    }
    /**
     * 获取 path 路径下 包含指定字符的 文件（包括文件夹）
     * @param path
     * @return List<File>
     */
    public static List<String> listFileStr(String path,String word){
        List<String> files = listFileStr(path);
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
    public static List<String> listFileStr(String path,String word,String prefix){
        List<String> files = listFileStr(path,word);
        List<String> fileList = new ArrayList<>();

        for (String file : files) {
            fileList.add(file.replaceFirst(prefix,""));
        }

        return fileList;
    }

    /**
     * 读取文件中的内容
     * @param path
     * @return
     */
    public static List<String> fileContent(String path){
        List<String> readStr = new ArrayList<>();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(path),"UTF-8"));

            String line;
            while ( (line=bufferedReader.readLine()) != null){
                readStr.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return readStr;
    }


}
