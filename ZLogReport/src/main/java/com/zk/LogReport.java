package com.zk;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;

import com.zk.crash.CrashHandler;
import com.zk.encryption.IEncryption;
import com.zk.save.ISave;
import com.zk.save.imp.LogWriter;
import com.zk.upload.ILogUpload;
import com.zk.upload.UploadService;
import com.zk.utils.NetUtil;

/**
 * Created by Administrator on 2017/2/20.
 */

public class LogReport {
    private static LogReport mLogReport;
    public ILogUpload mUpload;
    private long mCacheSize = 31457280L;
    private String mROOT;
    private IEncryption mEncryption;
    private ISave mLogSaver;
    private boolean mWifiOnly = true;

    private LogReport() {
    }

    public static LogReport getInstance() {
        if(mLogReport == null) {
            Class var0 = LogReport.class;
            synchronized(LogReport.class) {
                if(mLogReport == null) {
                    mLogReport = new LogReport();
                }
            }
        }

        return mLogReport;
    }

    public LogReport setCacheSize(long cacheSize) {
        this.mCacheSize = cacheSize;
        return this;
    }

    public LogReport setEncryption(IEncryption encryption) {
        this.mEncryption = encryption;
        return this;
    }

    public LogReport setUploadType(ILogUpload logUpload) {
        this.mUpload = logUpload;
        return this;
    }

    public LogReport setWifiOnly(boolean wifiOnly) {
        this.mWifiOnly = wifiOnly;
        return this;
    }

    public LogReport setLogDir(Context context, String logDir) {
        if(TextUtils.isEmpty(logDir)) {
            if(Environment.getExternalStorageState().equals("mounted")) {
                this.mROOT = context.getExternalCacheDir().getAbsolutePath();
            } else {
                this.mROOT = context.getCacheDir().getAbsolutePath();
            }
        } else {
            this.mROOT = logDir;
        }

        return this;
    }

    public LogReport setLogSaver(ISave logSaver) {
        this.mLogSaver = logSaver;
        return this;
    }

    public String getROOT() {
        return this.mROOT;
    }

    public void init(Context context) {
        if(TextUtils.isEmpty(this.mROOT)) {
            if(Environment.getExternalStorageState().equals("mounted")) {
                this.mROOT = context.getExternalCacheDir().getAbsolutePath();
            } else {
                this.mROOT = context.getCacheDir().getAbsolutePath();
            }
        }

        if(this.mEncryption != null) {
            this.mLogSaver.setEncodeType(this.mEncryption);
        }

        CrashHandler.getInstance().init(this.mLogSaver);
        LogWriter.getInstance().init(this.mLogSaver);
    }

    public ILogUpload getUpload() {
        return this.mUpload;
    }

    public long getCacheSize() {
        return this.mCacheSize;
    }

    public void upload(Context applicationContext) {
        if(this.mUpload != null) {
         //   if(!NetUtil.isConnected(applicationContext) || NetUtil.isWifi(applicationContext) || !this.mWifiOnly) {
                Intent intent = new Intent(applicationContext, UploadService.class);
                applicationContext.startService(intent);
         //   }
        }
    }
}

