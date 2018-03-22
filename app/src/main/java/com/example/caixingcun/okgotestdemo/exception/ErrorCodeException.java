package com.example.caixingcun.okgotestdemo.exception;

/**
 * Created by caixingcun on 2018/3/22.
 *  自定义异常  方便区分 是否是 服务器返回错误码
 */

public class ErrorCodeException extends Exception{
    public ErrorCodeException(String message) {
        super(message);
    }
}
