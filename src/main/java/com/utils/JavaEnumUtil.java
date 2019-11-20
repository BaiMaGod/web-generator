package com.utils;


import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * java 枚举类 解析工具
 */
public class JavaEnumUtil {


    /**
     * 添加多个枚举项到 枚举类 文件中
     * @param filePath
     * @param enums
     */
    public static void addEnumToFile(String filePath,List<String> enums){
        if(enums==null || enums.isEmpty()){
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (String anEnum : enums) {
            stringBuilder.append(anEnum+",\n");
        }

        String enumText = stringBuilder.toString();
        int i = enumText.lastIndexOf(",");
        enumText = enumText.substring(0,i)+";\n";

        addEnumToFile(filePath,enumText);

    }

    /**
     * 添加 单个 枚举项 到 枚举类 中
     * @param filePath
     * @param enumText
     */
    public static void addEnumToFile(String filePath,String enumText){
        if(StringUtils.isEmpty(enumText)){
            return;
        }

        // 首先 读取 目标文件内容
        List<String> readStr = FileUtil.fileContent(filePath);
        // 定位到最后一个枚举项位置
        int anEnum = ListUtil.getIndex(readStr, "enum");
        int enumEndIndex = getEnumEndIndex(readStr,anEnum);

        // 将最后一个 ； 变为 ，
        readStr.set(enumEndIndex,readStr.get(enumEndIndex).replace(";",","));

        readStr.add(enumEndIndex+1,enumText);

        String targetText = ListUtil.concatStr(readStr);

        cn.hutool.core.io.FileUtil.writeString(targetText,filePath,"UTF-8");

    }


    /**
     * 获取枚举类中所有的枚举项
     * @param filePath
     * @return
     */
    public static List<String> getClassAllEnumName(String filePath){
        List<String> classAllEnum = getClassAllEnum(filePath);

        List<String> enumNames = new ArrayList<>();
        if(classAllEnum!=null){
            for (String enumLine : classAllEnum) {
                int index = enumLine.indexOf("(");
                enumNames.add(enumLine.substring(0,index));
            }
        }

        return enumNames;
    }

    /**
     * 获取枚举类中所有的枚举项
     * @param filePath
     * @return
     */
    public static List<String> getClassAllEnum(String filePath){
        if(filePath.indexOf(".java")<0){
            System.out.println("不是java文件");
            return null;
        }
        List<String> readStr = FileUtil.fileContent(filePath);  // 文件中的内容

        int anEnum = ListUtil.getIndex(readStr, "enum");
        if(anEnum<0){
            System.out.println("不是enum类");
            return null;
        }

        List<String> enums = new ArrayList<>();

        int nextEnumIndex = getNextEnumIndex(readStr, anEnum + 1);
        int enumEndIndex = getEnumEndIndex(readStr,anEnum);
        while (nextEnumIndex>0 && nextEnumIndex<=enumEndIndex){
            String str = readStr.get(nextEnumIndex);
            int i = str.lastIndexOf(",");
            int i1 = str.lastIndexOf(";");

            enums.add(str.substring(0, Math.max(i,i1)));
            nextEnumIndex = getNextEnumIndex(readStr,nextEnumIndex+1);
        }

        return enums;
    }

    /**
     * 获取枚举类中所有 符合名字 的枚举项,
     * @param filePath
     * @return
     */
    public static List<String> getClassAllEnum(String filePath,List<String> enumNames){
        List<String> classAllEnum = getClassAllEnum(filePath);

        List<String> enums = new ArrayList<>();
        for (String enumStr : classAllEnum) {
            for (String enumName : enumNames) {
                if(enumStr.indexOf(enumName)>=0){
                    enums.add(enumStr);
                    break;
                }
            }
        }


        return enums;
    }

    private static int getEnumEndIndex(List<String> readStr, int fromIndex) {
        for (int i = fromIndex; i < readStr.size() && fromIndex >=0; i++) {
            if(readStr.get(i).indexOf(";")>=0){
                return i;
            }
        }

        return -1;
    }

    private static int getNextEnumIndex(List<String> readStr, int fromIndex) {
        for (int i = fromIndex; i < readStr.size() && fromIndex >=0; i++) {
            String line = readStr.get(i).trim();
            if(!"".equals(line) && isLetter(line.charAt(0))){
                return i;
            }
        }

        return -1;
    }

    /**
     * 判断字符是不是字母
     * @param charAt
     * @return
     */
    private static boolean isLetter(char charAt) {
        if((charAt>=65 && charAt<=90) || (charAt>=97 && charAt<=122)){
            return true;
        }

        return false;
    }


    public static void main(String[] args) {
        String path1 = "D:\\E\\ideaProject\\web-generator\\src\\main\\java\\com\\result\\ResultStatus.java";
        List<String> classAllEnum = getClassAllEnum(path1);
        for (String s : classAllEnum) {
            System.out.println(s);
        }

        List<String> classAllEnumName = getClassAllEnumName(path1);
        for (String s : classAllEnumName) {
            System.out.println(s);
        }
    }
}
