package com.example.caixingcun.okgotestdemo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.caixingcun.okgotestdemo.base.BaseRxActivity;
import com.example.caixingcun.okgotestdemo.callback.JsonConvert;
import com.example.caixingcun.okgotestdemo.module.home.ArticleListBean;
import com.example.caixingcun.okgotestdemo.model.LzyResponse;
import com.example.caixingcun.okgotestdemo.module.home.PageBean;
import com.example.caixingcun.okgotestdemo.net.RxUtils;
import com.example.caixingcun.okgotestdemo.net.ServerApi;
import com.example.caixingcun.okgotestdemo.net.Urls;
import com.example.caixingcun.okgotestdemo.observer.MyDefaultObserver;
import com.example.caixingcun.okgotestdemo.util.LogUtils;
import com.example.caixingcun.okgotestdemo.util.ToastUtils;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.convert.FileConvert;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.model.HttpMethod;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okrx2.adapter.ObservableBody;
import com.lzy.okrx2.adapter.ObservableResponse;

import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseRxActivity {
    MainActivity mMainActivity;
    private TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainActivity = this;
        mTv = findViewById(R.id.tv);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // requestByUtil();
                requetBySelf();
            }
        });

        /**
         * okRx2 基本请求  请求/获取 string json jsonArray
         */

        //   basicRequest();
        /**
         * 文件下载两种方案  带进度 /不带进度
         */
        //  downLoadFileRequest();
        /**
         * 文件上传两种方案  带进度/不带进度
         */
        //  upLoadFileRequest();

        /**
         * 统一管理请求    RxUtils + ServerApi
         */
        //  formatApi();

    }

    private void requetBySelf() {
        OkGo.<LzyResponse<PageBean<ArticleListBean>>>get("http://www.wanandroid.com/article/list/0/json")
                .converter(new JsonConvert<LzyResponse<PageBean<ArticleListBean>>>(new TypeToken<LzyResponse<PageBean<ArticleListBean>>>(){}.getType()))
                .adapt(new ObservableBody<LzyResponse<PageBean<ArticleListBean>>>())
                .subscribe(new Observer<LzyResponse<PageBean<ArticleListBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LzyResponse<PageBean<ArticleListBean>> pageBeanLzyResponse) {
                        showToast("ok");
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast("error");
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        ;
    }

    private void requestByUtil() {
        Type type = new TypeToken<LzyResponse<PageBean<ArticleListBean>>>() {
        }.getType();
        RxUtils.<LzyResponse<PageBean<ArticleListBean>>>request(HttpMethod.GET, "http://www.wanandroid.com/article/list/0/json", type)
                // RxUtils.<LzyResponse<PageBean<ArticleListBean>>>request(HttpMethod.GET, "http://www.wanandroid.com/lg/collect/list/0/json", type)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        //展示dialog
                        LogUtils.info("展示dialog");
                        LogUtils.debug(Thread.currentThread().getName());
                        showLoading();
                        mTv.setText("展示dialog");
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyDefaultObserver<LzyResponse<PageBean<ArticleListBean>>>(mMainActivity) {
                    @Override
                    public void onNext(LzyResponse<PageBean<ArticleListBean>> pageBeanLzyResponse) {
                        ToastUtils.show(pageBeanLzyResponse.getData().getDatas().get(0).getDesc());
                    }

                    @Override
                    public void onComplete() {
                        //关闭dialog
                        LogUtils.info("关闭dialog");
                        mTv.setText("关闭dialog");
                        dismissLoading();
                    }
                });

    }

    private void formatApi() {
        /**
         * 请求结构封装后获取 获取 json对象
         */
        OkRxRequest();
        /**
         * 获取bitmap
         */
        okRxRequestBitmap();
        /**
         * 缓存
         */
        okRxCache();

    }

    private void upLoadFileRequest() {
        upLoadFile1(); //带进度
        upLoadFile2();//不带进度
    }

    private void upLoadFile2() {
        Observable.create(new ObservableOnSubscribe<Progress>() {
            @Override
            public void subscribe(final ObservableEmitter<Progress> e) throws Exception {
                OkGo.<String>post(Urls.URL_FORM_UPLOAD)//
                        .tag(this)//
                        .headers("header1", "headerValue1")//
                        .headers("header2", "headerValue2")//
                        .params("param1", "paramValue1")//
                        .params("param2", "paramValue2")//
                        .params("file1", new File("文件路径"))
                        .params("file2", new File("文件路径"))
                        .params("file3", new File("文件路径"))
                        //                .addFileParams("file", files)//
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                e.onComplete();
                            }

                            @Override
                            public void onError(Response<String> response) {
                                e.onError(response.getException());
                            }

                            @Override
                            public void uploadProgress(Progress progress) {
                                e.onNext(progress);
                            }
                        });
            }
        })//
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        //正在上传中...
                    }
                })//
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribe(new Observer<Progress>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(Progress progress) {
                        //展示进度
//                        System.out.println("uploadProgress: " + progress);
//                        String downloadLength = Formatter.formatFileSize(getApplicationContext(), progress.currentSize);
//                        String totalLength = Formatter.formatFileSize(getApplicationContext(), progress.totalSize);
//                        tvDownloadSize.setText(downloadLength + "/" + totalLength);
//                        String speed = Formatter.formatFileSize(getApplicationContext(), progress.speed);
//                        tvNetSpeed.setText(String.format("%s/s", speed));
//                        tvProgress.setText(numberFormat.format(progress.fraction));
//                        pbProgress.setMax(10000);
//                        pbProgress.setProgress((int) (progress.fraction * 10000));
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        //上传出错
                    }

                    @Override
                    public void onComplete() {
                        //上传完成
                    }
                });
    }

    private void upLoadFile1() {
        //拼接参数
        OkGo.<String>post(Urls.URL_FORM_UPLOAD)//
                .tag(this)//
                .headers("header1", "headerValue1")//
                .headers("header2", "headerValue2")//
                .params("param1", "paramValue1")//
                .params("param2", "paramValue2")//
                .params("file1", new File("文件路径"))
                .params("file2", new File("文件路径"))
                .params("file3", new File("文件路径"))
                //               .addFileParams("file", files)//
                .converter(new StringConvert())//
                .adapt(new ObservableResponse<String>())//
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        // 加载过程中展示进度
                    }
                })//
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribe(new Observer<Response<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(Response<String> response) {
                        //上传完成
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        //上传出错
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 下载文件
     */
    private void downLoadFileRequest() {
        dowLoadFile1();  //不带进度条下载
        downLoadFile2(); //带进度条下载
    }

    private void downLoadFile2() {
        Observable.create(new ObservableOnSubscribe<Progress>() {
            @Override
            public void subscribe(final ObservableEmitter<Progress> e) throws Exception {
                OkGo.<File>get("")
                        .headers("aaa", "111")
                        .params("bbb", "222")
                        .execute(new FileCallback() {
                            @Override
                            public void onSuccess(Response<File> response) {
                                e.onComplete();
                            }

                            @Override
                            public void onError(Response<File> response) {
                                e.onError(response.getException());
                            }

                            @Override
                            public void downloadProgress(Progress progress) {
                                //更新进度
                                e.onNext(progress);
                            }
                        });

            }
        })
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Progress>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(Progress progress) {
                        //下载 进度更新
                    }

                    @Override
                    public void onError(Throwable e) {
                        //下载出错
                    }

                    @Override
                    public void onComplete() {
                        //下载完成
                    }
                })
        ;
    }

    private void dowLoadFile1() {
        OkGo.<File>get("url")
                .headers("aaa", "111")
                .params("bbb", "222")
                .converter(new FileConvert())
                .adapt(new ObservableResponse<File>())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        //加载进度  这中rx调用方法没有下载进度
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        ;
    }

    private void basicRequest() {
        //提交请求
        commitRequest();
        //请求json数据 string 代表json数据
        jsonRequest();
        // 请求jsonArray 数据
        jsonArrayRequest();
        //上传 String
        upString();
        //上传 json
        upJson();

    }

    private void upJson() {
        HashMap<String, String> params = new HashMap<>();
        params.put("key1", "value1");
        params.put("key2", "这里是需要提交的json格式数据");
        params.put("key3", "也可以使用三方工具将对象转成json字符串");
        params.put("key4", "其实你怎么高兴怎么写都行");
        JSONObject jsonObject = new JSONObject(params);

        OkGo.<String>post(Urls.URL_TEXT_UPLOAD)//
                .headers("aaa", "111")//
                .upJson(jsonObject)//
                .converter(new StringConvert())//
                .adapt(new ObservableResponse<String>())//
                .subscribeOn(Schedulers.io())//
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                    }
                })//
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribe();
    }

    private void upString() {
        OkGo.<String>post(Urls.URL_TEXT_UPLOAD)//
                .headers("aaa", "111")//
                .upString("上传的文本。。。")//
                .converter(new StringConvert())//
                .adapt(new ObservableResponse<String>())//
                .subscribeOn(Schedulers.io())//
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

                    }
                })//
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribe();
    }

    private void jsonArrayRequest() {
        OkGo.<LzyResponse<List<String>>>get(Urls.URL_JSONARRAY)//
                .headers("aaa", "111")//
                .params("bbb", "222")//
                .converter(new JsonConvert<LzyResponse<List<String>>>() {
                })//
                .adapt(new ObservableBody<LzyResponse<List<String>>>())//
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

                    }
                })//
                .map(new Function<LzyResponse<List<String>>, List<String>>() {
                    @Override
                    public List<String> apply(LzyResponse<List<String>> listLzyResponse) throws Exception {
                        return listLzyResponse.getData();
                    }
                })//
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribe();
    }

    private void jsonRequest() {
        OkGo.<LzyResponse<String>>get(Urls.URL_JSONOBJECT)//
                .headers("aaa", "111")//
                .params("bbb", "222")//
                .converter(new JsonConvert<LzyResponse<String>>() {
                })//
                .adapt(new ObservableBody<LzyResponse<String>>())//
                .subscribeOn(Schedulers.io())//
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

                    }
                })//
                .map(new Function<LzyResponse<String>, String>() {
                    @Override
                    public String apply(LzyResponse<String> stringLzyResponse) throws Exception {
                        return stringLzyResponse.getData();
                    }
                })//
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribe();
    }

    private void commitRequest() {
        OkGo.<String>post(Urls.URL_METHOD)//
                .headers("aaa", "111")//
                .params("bbb", "222")//
                .converter(new StringConvert())//
                .adapt(new ObservableResponse<String>())//
                .subscribeOn(Schedulers.io())//
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

                    }
                })//
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribe();
    }

    private void okRxCache() {
        OkGo.<String>post(Urls.URL_METHOD)
                .headers("aaa", "111")
                .params("bbb", "222")
                .cacheKey("rx_cache")
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .converter(new StringConvert())
                .adapt(new ObservableResponse<String>())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        //加载进度
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(Response<String> stringResponse) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        //取消进度条
                    }
                })
        ;
    }

    private void okRxRequestBitmap() {
        ServerApi.getBitmap("aaa", "bbb")
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        //加载进度条
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<Bitmap>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(Response<Bitmap> bitmapResponse) {
                        Bitmap body = bitmapResponse.body();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        //关闭进度条
                    }
                })
        ;

    }


    private void OkRxRequest() {
        Type type = new TypeToken<LzyResponse<String>>() {
        }.getType();

        ServerApi.<LzyResponse<String>>getData(type, Urls.URL_JSONOBJECT, "header", "params")
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        //加载进度
                    }
                })
                .map(new Function<LzyResponse<String>, String>() {
                    @Override
                    public String apply(LzyResponse<String> stringLzyResponse) throws Exception {

                        return stringLzyResponse.getData();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(String s) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        //停止加载进度
                    }
                })
        ;
    }


}
