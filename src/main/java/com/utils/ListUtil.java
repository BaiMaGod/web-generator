package com.utils;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {

    /**
     *
     * @param mainStr
     * @param excludeStr
     * @return
     */
    public static List<String> exclude(List<String> mainStr,List<String> excludeStr){
        List<String> res = new ArrayList<>();

        for (String mains : mainStr) {
            boolean haveFlag = false;
            for (String exclude : excludeStr) {
                if(mains.equals(exclude)){
                    haveFlag = true;
                }
            }
            if(!haveFlag){
                res.add(mains);
            }
        }

        return res;
    }

    /**
     * 拼接list为一个字符串
     * @param readStr
     * @return
     */
    public static String concatStr(List<String> readStr) {

        return concatStr(readStr,0,readStr.size()-1);
    }

    /**
     * 拼接list为一个字符串
     * @param readStr
     * @param startIndex
     * @param endIndex
     * @return
     */
    public static String concatStr(List<String> readStr, int startIndex, int endIndex) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = startIndex; i <= endIndex && i>=0; i++) {
            stringBuilder.append(readStr.get(i)+"\n");
        }

        return stringBuilder.toString();
    }

    /**
     * 去除list中指定行，将剩下的拼接为一个字符串
     * @param readStr
     * @param startIndex
     * @param endIndex
     * @return
     */
    public static String excludeStr(List<String> readStr, int startIndex, int endIndex) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < readStr.size(); i++) {
            if(i < startIndex || i > endIndex)
                stringBuilder.append(readStr.get(i)+"\n");
        }

        return stringBuilder.toString();
    }

    /**
     * 在list集合中搜索包含指定内容的元素的下标
     * @param readStr
     * @param search
     * @return
     */
    public static int getIndex(List<String> readStr,String search){
        return getIndex(readStr,search,0);
    }
    /**
     * 在list集合中搜索包含指定内容的元素的下标,从指定下标开始查找
     * @param readStr
     * @param search
     * @return
     */
    public static int getIndex(List<String> readStr,String search,int fromIndex){
        for (int i = fromIndex; i < readStr.size()-1 && i>=0; i++) {
            String s = readStr.get(i);
            if(s.indexOf(search)>=0){
                return i;
            }
        }
        return -1;
    }

    /**
     * 在list集合中搜索包含指定内容,且不包含指定内容 的元素的下标
     * @param readStr
     * @param search
     * @return
     */
    public static int getIndex(List<String> readStr,String search,String exclude){
        return getIndex(readStr,search,exclude,0);
    }
    /**
     * 在list集合中搜索包含指定内容,且不包含指定内容 的元素的下标,从指定下标开始查找
     * @param readStr
     * @param search
     * @return
     */
    public static int getIndex(List<String> readStr,String search,String exclude,int fromIndex){
        for (int i = fromIndex; i < readStr.size()-1 && i>=0; i++) {
            String s = readStr.get(i);
            if(s.indexOf(search)>=0 && s.indexOf(exclude)<0){
                return i;
            }
        }
        return -1;
    }

    /**
     * 在list集合中搜索包含指定内容的元素的下标,从后往前查
     * @param readStr
     * @param search
     * @return
     */
    public static int getLastIndex(List<String> readStr,String search){
        return getLastIndex(readStr,search,readStr.size()-1);
    }
    /**
     * 在list集合中搜索包含指定内容的元素的下标,从指定下标开始查找,从后往前查
     * @param readStr
     * @param search
     * @return
     */
    public static int getLastIndex(List<String> readStr,String search,int fromIndex){
        for (int i = fromIndex; i >= 0 && i<readStr.size(); i--) {
            String s = readStr.get(i);
            if(s.indexOf(search)>=0){
                return i;
            }
        }
        return -1;
    }

    /**
     * 在list集合中搜索包含指定内容,且不包含指定内容 的元素的下标,从后往前查
     * @param readStr
     * @param search
     * @return
     */
    public static int getLastIndex(List<String> readStr,String search,String exclude){
        return getLastIndex(readStr,search,exclude,readStr.size()-1);
    }
    /**
     * 在list集合中搜索包含指定内容,且不包含指定内容 的元素的下标,从指定下标开始查找,从后往前查
     * @param readStr
     * @param search
     * @return
     */
    public static int getLastIndex(List<String> readStr,String search,String exclude,int fromIndex){
        for (int i = fromIndex; i >= 0 && i<readStr.size(); i--) {
            String s = readStr.get(i);
            if(s.indexOf(search)>=0 && s.indexOf(exclude)<0){
                return i;
            }
        }
        return -1;
    }
}
