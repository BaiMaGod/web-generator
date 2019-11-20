package com.utils;

import com.model.Method;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * java文件 方法操作 工具类
 */
public class JavaMethodUtil {

    /**
     * 获取 java文件中 仅类和属性 的文本
     * @param path
     * @return
     */
    public static String getClassShellText(String path){
        if(path.indexOf(".java")<0){
            System.out.println("不是java文件");
            return null;
        }

        List<String> readStr = FileUtil.fileContent(path);  // 文件中的内容

        int classStartIndex = ListUtil.getIndex(readStr, "class","*");
        int classEndIndex = ListUtil.getLastIndex(readStr, "}","*");

        int firstMethodIndex = ListUtil.getIndex(readStr, "{", "$",classStartIndex+1);
        int firstMethodStartIndex = getMethodStartIndex(readStr, firstMethodIndex);

        String res = ListUtil.excludeStr(readStr, firstMethodStartIndex, classEndIndex-1);

        return res;
    }

    /**
     * 获取 java接口文件中 仅类和属性 的文本
     * @param path
     * @return
     */
    public static String getInterfaceShellText(String path){
        if(path.indexOf(".java")<0){
            System.out.println("不是java文件");
            return null;
        }

        List<String> readStr = FileUtil.fileContent(path);  // 文件中的内容

        int interfaceStartIndex = ListUtil.getIndex(readStr, "{","*");
        int interfaceEndIndex = ListUtil.getLastIndex(readStr, "}","*");

        int firstMethodIndex = ListUtil.getIndex(readStr, ";", "$",interfaceStartIndex+1);
        int firstMethodStartIndex = getMethodStartIndex(readStr, firstMethodIndex);

        String res = ListUtil.excludeStr(readStr, firstMethodStartIndex, interfaceEndIndex-1);

        return res;
    }


    /**
     * 获取 java文件中 所有方法 的文本
     * @param path
     * @return
     */
    public static List<String> getClassAllMethod(String path, List<Method> methods){
        if(path.indexOf(".java")<0){
            System.out.println("不是java文件");
            return null;
        }
        List<String> methodStr = new ArrayList<>(); // 返回的方法字符串集合

        List<String> readStr = FileUtil.fileContent(path);  // 文件中的内容
        for (Method method : methods) {
            int index = ListUtil.getIndex(readStr, method.getName(), ";");   // 定位到指定方法名 的方法行数
            if(index<0){
                continue;
            }
            int endIndex = getMethodEndIndex(readStr, index);
            int startIndex = getMethodStartIndex(readStr, index);

            methodStr.add(ListUtil.concatStr(readStr, startIndex, endIndex));
        }


        return methodStr;
    }

    /**
     * 获取 java接口文件中 所有方法 的文本
     * @param path
     * @return
     */
    public static List<String> getInterfaceAllMethod(String path, List<Method> methods){
        if(path.indexOf(".java")<0){
            System.out.println("不是java文件");
            return null;
        }
        List<String> methodStr = new ArrayList<>(); // 返回的方法字符串集合

        List<String> readStr = FileUtil.fileContent(path);  // 文件中的内容
        for (Method method : methods) {
            int index = ListUtil.getIndex(readStr, method.getName());   // 定位到指定方法名 的方法行数
            if(index<0){
                continue;
            }
            int endIndex = getInterfaceMethodEndIndex(readStr, index);
            int startIndex = getMethodStartIndex(readStr, index);

            methodStr.add(ListUtil.concatStr(readStr, startIndex, endIndex));
        }


        return methodStr;
    }



    /**
     * 获取方法起始行数
     * @param readStr
     * @param fromIndex
     * @return
     */
    public static int getMethodStartIndex(List<String> readStr,int fromIndex){
        for (int i = fromIndex-1; i >= 0 && i<readStr.size(); i--) {
            String str = readStr.get(i).trim();
            if(str.indexOf("@") == 0 || str.indexOf("*") == 0 || str.indexOf("/") == 0 || str.trim().equals("")){
                continue;
            }else{
                return i+1;
            }
        }

        return -1;
    }

    /**
     * 获取方法结束行数
     * @param readStr
     * @param fromIndex
     * @return
     */
    public static int getMethodEndIndex(List<String> readStr,int fromIndex){
        String prefix = "{";
        String suffix = "}";

        Stack strStack = new Stack();   // 存字符串

        for (int i = fromIndex; i < readStr.size() && i>=0; i++) {
            String str = readStr.get(i);
            if(str.indexOf(prefix)>=0){
                if(str.indexOf(suffix)<0){
                    strStack.push(prefix);
                }
            }else if(str.indexOf(suffix)>=0){
                if(strStack.peek().equals(prefix)){    // 如果此时栈顶元素 为前缀，则与要入栈的后缀凑对
                    strStack.pop();
                    if(strStack.isEmpty()){
                        return i;
                    }
                }else{
                    strStack.push(suffix);
                }
            }
        }

        return -1;
    }


    /**
     * 获取接口方法结束行数
     * @param readStr
     * @param fromIndex
     * @return
     */
    private static int getInterfaceMethodEndIndex(List<String> readStr, int fromIndex) {
        for (int i = fromIndex; i < readStr.size() && i>=0; i++) {
            String str = readStr.get(i);
            if(str.indexOf(";")>=0){
                return i;
            }
        }

        return -1;
    }

    /**
     * 获取content中 prefix、Suffix 之间的文本
     * @param prefix
     * @param suffix
     * @return
     */
    public static List<String> getMid(String content,String prefix,String suffix){
        List<String> methods = new ArrayList<>();
        int index1 = content.indexOf(prefix);
        int index2 = content.lastIndexOf(suffix);
        if(index1 >= index2 ){
            return methods;
        }

        Stack strStack = new Stack();   // 存字符串
        Stack indStack = new Stack();   // 存下标
        strStack.push(prefix);
        indStack.push(index1);

        index2 = content.indexOf(suffix,index1 + suffix.length());
        index2 = index2 < 0 ? Integer.MAX_VALUE : index2;
        index1 = content.indexOf(prefix,index1 + prefix.length());
        index1 = index1 < 0 ? Integer.MAX_VALUE : index1;

        while (index1 < content.length() || index2 < content.length()){
            if(index1 < index2){
                strStack.push(prefix);
                indStack.push(index1);

                index1 = content.indexOf(prefix,index1 + prefix.length());
                index1 = index1 < 0 ? Integer.MAX_VALUE : index1;
            }else{
                if(strStack.peek().equals(prefix)){    // 如果此时栈顶元素 为前缀，则与要入栈的后缀凑对
                    strStack.pop();
                    int pop = (int) indStack.pop();
                    if(strStack.isEmpty()){
                        methods.add(content.substring(pop + prefix.length(),index2));
                    }
                }else{
                    strStack.push(suffix);
                    indStack.push(index2);
                }

                index2 = content.indexOf(suffix,index2 + suffix.length());
                index2 = index2 < 0 ? Integer.MAX_VALUE : index2;
            }
        }

        return methods;
    }


    public static void main(String[] args) {
        List<Method> methods = new ArrayList<>();
        Method method = new Method();
        method.setName("direct");
        methods.add(method);
        method = new Method();
        method.setName("Direct");
        methods.add(method);
//        List<String> allMethod = getClassAllMethod("D:\\E\\ideaProject\\base-user\\src\\main\\java\\com\\controller\\LoginController.java",methods);
//
//        for (String s : allMethod) {
//            System.out.println("方法：\n"+s);
//        }
//
//
//        String classShellText = getClassShellText("D:\\E\\ideaProject\\base-user\\src\\main\\java\\com\\controller\\LoginController.java");
//
//        System.out.println("类外壳：\n"+classShellText);


/*
        List<String> interfaceAllMethod = getInterfaceAllMethod("D:\\E\\ideaProject\\base-user\\src\\main\\java\\com\\service\\LoginService.java", methods);

        for (String s : interfaceAllMethod) {
            System.out.println("方法：\n"+s);
        }

        String interfaceShellText = getInterfaceShellText("D:\\E\\ideaProject\\base-user\\src\\main\\java\\com\\service\\LoginService.java");
        System.out.println("接口外壳：\n"+interfaceShellText);
*/


//        String path = "D:\\E\\ideaProject\\base-user\\src\\main\\java\\com\\service\\user\\RegisterService.java";
//        System.out.println(getInterfaceShellText(path));
//
//        String login = StringUtils.capitalize("login");
//        System.out.println(login);

//        String s = "D:\\E\\ideaProject\\module\\user\\login\\java\\service\\user\\LoginService.java";
//
//        List<String> classAllMethod = getInterfaceAllMethod(s, methods);
//        for (String s1 : classAllMethod) {
//            System.out.println(s1);
//        }

        String s = "D:\\E\\ideaProject\\module\\user\\login\\java\\controller\\user\\LoginController.java";
        String classShellText = getClassShellText(s);
        System.out.println(classShellText);

    }
}
