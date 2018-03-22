package com.example.caixingcun.okgotestdemo.observer;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.caixingcun.okgotestdemo.R;
import com.example.caixingcun.okgotestdemo.base.BaseRxActivity;
import com.example.caixingcun.okgotestdemo.base.BaseRxFragment;
import com.example.caixingcun.okgotestdemo.exception.ExceptionReason;
import com.example.caixingcun.okgotestdemo.exception.ErrorCodeException;
import com.example.caixingcun.okgotestdemo.model.LzyResponse;
import com.example.caixingcun.okgotestdemo.util.LogUtils;
import com.example.caixingcun.okgotestdemo.util.ToastUtils;
import com.google.gson.JsonParseException;
import com.lzy.okgo.exception.HttpException;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.ParseException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by caixingcun on 2018/3/22.
 * 封装的 Observer 处理了 各种返回异常
 */

public abstract class MyDefaultObserver<T> implements Observer<T> {
    private BaseRxActivity activity;
    private BaseRxFragment fragment;
    /**
     * fragment or activity use observer
     */
    private Where mWhere;

    enum Where {
        ACTIVITY, FRAGMENT,
    }

    //  Activity 是否在执行onStop()时取消订阅
    private boolean isAddInStop = false;

    public MyDefaultObserver(BaseRxActivity activity) {
        this.activity = activity;
        mWhere = Where.ACTIVITY;
    }

    public MyDefaultObserver(BaseRxFragment fragment) {
        this.fragment = fragment;
        mWhere = Where.FRAGMENT;
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (mWhere == Where.ACTIVITY) {
            activity.addDisposable(d);
        } else {
            fragment.addDisposable(d);
        }
    }

    @Override
    public void onError(Throwable e) {

        LogUtils.error(e.getMessage());
        onComplete(); //手动吊起 onComplete() 方便界面中展示进度条

        if (e instanceof HttpException) {     //   HTTP错误
            onException(ExceptionReason.BAD_NETWORK);
        } else if (e instanceof ConnectException
                || e instanceof UnknownHostException) {   //   连接错误
            onException(ExceptionReason.CONNECT_ERROR);
        } else if (e instanceof InterruptedIOException) {   //  连接超时
            onException(ExceptionReason.CONNECT_TIMEOUT);
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {   //  解析错误
            onException(ExceptionReason.PARSE_ERROR);
        } else if (e instanceof ErrorCodeException) {
            handleErrorCode(e); //后台返回错误信息
        } else {
            onException(ExceptionReason.UNKNOWN_ERROR);
        }
    }

    /**
     * 处理后台 错误code
     *
     * @param e
     */
    protected void handleErrorCode(Throwable e) {
        LogUtils.info(e.getMessage());
        ToastUtils.show(e.getMessage());
    }

    /**
     * 请求 网络/解析异常 处理
     *
     * @param reason
     */
    public void onException(ExceptionReason reason) {
        switch (reason) {
            case CONNECT_ERROR:
                ToastUtils.show(R.string.connect_error, Toast.LENGTH_SHORT);
                break;

            case CONNECT_TIMEOUT:
                ToastUtils.show(R.string.connect_timeout, Toast.LENGTH_SHORT);
                break;

            case BAD_NETWORK:
                ToastUtils.show(R.string.bad_network, Toast.LENGTH_SHORT);
                break;

            case PARSE_ERROR:
                ToastUtils.show(R.string.parse_error, Toast.LENGTH_SHORT);
                break;
            case UNKNOWN_ERROR:
            default:
                ToastUtils.show(R.string.unknown_error, Toast.LENGTH_SHORT);
                break;
        }
    }
}
