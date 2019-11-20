package com.result;

/**
 * 返回状态码枚举类
 */
public enum ResultStatus {
    SUCCESS(0,"请求成功"),
    ERROR_SYS(1,"系统错误"),
    ERROR_UPDATE(2,"修改错误"),
    ERROR_Mkdir(11,"目标文件夹不存在，且无法创建指定文件夹"),
    ERROR_Register(11,"注册失败，请稍后再试"),
    ERROR_Register_Direct(12,"注册失败，请输入合法的账号和密码"),
    ERROR_Register_Number_Exist(13,"注册失败，账号已存在"),
    ERROR_Login_Direct(21,"登录失败，请输入合法的账号和密码"),
    ERROR_Login_Number_Password(22,"登录失败，账号或密码错误");


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
