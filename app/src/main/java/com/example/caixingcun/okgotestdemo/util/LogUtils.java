package com.example.caixingcun.okgotestdemo.util;

import android.nfc.Tag;
import android.util.Log;

/**
 * Created by caixingcun on 2018/3/22.
 */

public class LogUtils {
    public static final boolean isDebug = true;

    public static final String TAG = "tag";

    public static void verbose(String msg) {
        verbose(TAG, msg);
    }

    public static void debug(String message) {
        debug(TAG, message);
    }

    public static void info(String msg) {
        info(TAG, msg);
    }

    public static void warn(String message) {
        warn(TAG, message);
    }

    public static void error(String message) {
        error(TAG, message);
    }

    /////////////////////////////////////////////////////////////////////
    public static void verbose(String tag, String message) {
        if (isDebug) {
            Log.v(tag, message);
        }
    }

    public static void info(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }
    }

    public static void debug(String tag, String msg) {
        if (isDebug) {
            Log.d(tag, msg);
        }
    }

    public static void warn(String tag, String msg) {
        if (isDebug) {
            Log.w(tag, msg);
        }
    }

    public static void error(String tag, String message) {
        if (isDebug) {
            Log.e(tag, message);
        }
    }


}
