package com.example.caixingcun.okgotestdemo.base;

import android.support.v7.app.AppCompatActivity;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by caixingcun on 2018/3/21.
 *  Rx防泄露
 */

public class BaseRxActivity extends AppCompatActivity {
    private CompositeDisposable mCompositeDisposable;

    public void addDisposable(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    public void dispose() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dispose();
    }
}
