package com.zk.upload;

import android.content.Context;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Administrator on 2017/2/20.
 */

public abstract class BaseUpload implements ILogUpload {

    public Context mContext;
    public static final SimpleDateFormat yyyy_MM_dd_HH_mm_ss_SS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS", Locale.getDefault());

    public BaseUpload(Context context) {
        this.mContext = context;
    }

    protected abstract void sendReport(String var1, String var2, File var3, OnUploadFinishedListener var4);

    public void sendFile(File file, String content, OnUploadFinishedListener onUploadFinishedListener) {
        this.sendReport(this.buildTitle(this.mContext), this.buildBody(this.mContext, content), file, onUploadFinishedListener);
    }

    public String buildTitle(Context context) {
        return "【CrashLog】  " + context.getString(context.getApplicationInfo().labelRes) + " " + yyyy_MM_dd_HH_mm_ss_SS.format(Calendar.getInstance().getTime());
    }

    public String buildBody(Context context, String content) {
        return content;
    }
}
