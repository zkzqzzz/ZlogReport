package com.zk.utils;

import android.util.Log;

/**
 * Created by Administrator on 2017/2/20.
 */

public class LogUtil {
    public static boolean isDebug = true;
    private static final String TAG = "zk";

    private LogUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void i(String msg) {
        if(isDebug) {
            Log.i("zk", msg);
        }

    }

    public static void d(String msg) {
        if(isDebug) {
            Log.d("zk", msg);
        }

    }

    public static void e(String msg) {
        if(isDebug) {
            Log.e("zk", msg);
        }

    }

    public static void v(String msg) {
        if(isDebug) {
            Log.v("zk", msg);
        }

    }

    public static void i(String tag, String msg) {
        if(isDebug) {
            Log.i(tag, msg);
        }

    }

    public static void d(String tag, String msg) {
        if(isDebug) {
            Log.i(tag, msg);
        }

    }

    public static void e(String tag, String msg) {
        if(isDebug) {
            Log.i(tag, msg);
        }

    }

    public static void v(String tag, String msg) {
        if(isDebug) {
            Log.i(tag, msg);
        }

    }
}
