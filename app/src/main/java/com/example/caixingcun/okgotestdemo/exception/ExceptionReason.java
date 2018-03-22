package com.example.caixingcun.okgotestdemo.exception;

/**
 * Created by caixingcun on 2018/3/9.
 *
 * 请求网络失败原因
 */


public enum ExceptionReason {
    /**
     * 解析数据失败
     */
    PARSE_ERROR,
    /**
     * 网络问题
     */
    BAD_NETWORK,
    /**
     * 连接错误
     */
    CONNECT_ERROR,
    /**
     * 连接超时
     */
    CONNECT_TIMEOUT,
    /**
     * 未知错误
     */
    UNKNOWN_ERROR,
}
