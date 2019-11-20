package com.result;

import lombok.Data;

/**
 * 定义统一返回格式
 * @param <T>
 */
@Data
public class Result<T> {
    private int code;   // 状态码
    private long count = 1l; // 内容条数
    private String msg; // 提示信息
    private T data;     // 数据内容


    public Result(){
        setStatus(ResultStatus.SUCCESS);
    }

    public Result(ResultStatus status){
        this.setStatus(status);
    }


    public Result(T t) {
        setStatus(ResultStatus.SUCCESS);
        this.data = t;
    }
    public Result(long count,T t) {
        setStatus(ResultStatus.SUCCESS);
        this.count = count;
        this.data = t;
    }

    public Result<T> setStatus(ResultStatus status) {
        this.code = status.getCode();
        this.msg = status.getMsg();

        return this;
    }

    private Result(T t, Page page) {
        PageData resultData = new PageData(t);
        resultData.setPage(page);
        this.data = (T) resultData;
    }

    public static <T>Result<T> success(T t) {
        Result result = new Result(t);
        return result;
    }

    public static <T>Result<T> fail(T t, ResultStatus status) {
        Result result = new Result(t);
        result.setStatus(status);
        return result;
    }

    public static <T>Result<T> fail(ResultStatus status) {
        Result result = new Result(0);
        result.setStatus(status);
        return result;
    }

    public static <T>Result<T> success(T t, Page page) {
        Result result = new Result(t, page);
        return result;
    }




}
