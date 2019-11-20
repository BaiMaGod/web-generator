package com.form;

import lombok.Data;

import java.util.List;

public class CreateForm {

    @Data
    public static class BaseForm{
        private List<String> modulePaths;  //

        private String createPath;  //
    }

    @Data
    public static class WebForm{
        private List<String> modulePaths;  //  要生成的模块路径，相对

        private String projectName = "project";  // 生成的项目名

        private String createPath = "D:\\H\\WebCreateTest";  // 生成到那个文件夹
    }
}
