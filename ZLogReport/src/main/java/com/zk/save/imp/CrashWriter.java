package com.zk.save.imp;

import android.content.Context;
import android.os.Environment;

import com.zk.LogReport;
import com.zk.save.BaseSaver;
import com.zk.utils.FileUtil;
import com.zk.utils.LogUtil;

import java.io.File;
import java.util.Date;

/**
 * Created by Administrator on 2017/2/20.
 */

public class CrashWriter extends BaseSaver {
    private static final String TAG = "CrashWriter";
    public static final String LOG_FILE_NAME_EXCEPTION;
    private static final Thread.UncaughtExceptionHandler sDefaultHandler;

    public CrashWriter(Context context) {
        super(context);
    }

    public synchronized void writeCrash(final Thread thread, final Throwable ex, final String tag, final String content) {
        this.mThreadPool.execute(new Runnable() {
            public void run() {
                Class var1 = BaseSaver.class;
                synchronized(BaseSaver.class) {
                    BaseSaver.TimeLogFolder = LogReport.getInstance().getROOT() + "Log/" + BaseSaver.yyyy_mm_dd.format(new Date(System.currentTimeMillis())) + "/";
                    File logsDir = new File(BaseSaver.TimeLogFolder);
                    File crashFile = new File(logsDir, CrashWriter.LOG_FILE_NAME_EXCEPTION);
                    if(!Environment.getExternalStorageState().equals("mounted")) {
                        LogUtil.d("CrashWriter", "SDcard 不可用");
                    } else {
                        if(!logsDir.exists()) {
                            LogUtil.d("CrashWriter", "logsDir.mkdirs() =  +　" + logsDir.mkdirs());
                        }

                        if(!crashFile.exists()) {
                            CrashWriter.this.createFile(crashFile, CrashWriter.this.mContext);
                        }

                        StringBuilder preContent = new StringBuilder(CrashWriter.this.decodeString(FileUtil.getText(crashFile)));
                        LogUtil.d("CrashWriter", "读取本地的Crash文件，并且解密 = \n" + preContent.toString());
                        preContent.append(BaseSaver.formatLogMsg(tag, content)).append("\n");
                        LogUtil.d("CrashWriter", "即将保存的Crash文件内容 = \n" + preContent.toString());
                        CrashWriter.this.writeText(crashFile, preContent.toString());
                        CrashWriter.sDefaultHandler.uncaughtException(thread, ex);
                    }
                }
            }
        });
    }

    static {
        LOG_FILE_NAME_EXCEPTION = "CrashLog" + LOG_CREATE_TIME + ".txt";
        sDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }
}

