package com.result;

/**
 * 请求返回状态枚举
 */
public enum ResultStatus {
    SUCCESS(0,"请求成功"),
    ERROR_Mkdir(11,"创建文件夹失败");



    private int code;
    private String msg;

    ResultStatus(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode(){
        return this.code;
    }

    public String getMsg(){
        return this.msg;
    }
}
