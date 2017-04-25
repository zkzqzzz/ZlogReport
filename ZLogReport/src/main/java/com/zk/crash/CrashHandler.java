package com.zk.crash;

import com.zk.save.ISave;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by Administrator on 2017/2/20.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    private static CrashHandler INSTANCE = new CrashHandler();
    private ISave mSave;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    public void init(ISave logSaver) {
        this.mSave = logSaver;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void uncaughtException(Thread thread, Throwable ex) {
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);

        for(Throwable cause = ex.getCause(); cause != null; cause = cause.getCause()) {
            cause.printStackTrace(printWriter);
        }

        printWriter.close();
        String stringBuilder = "↓↓↓↓exception↓↓↓↓\n" + writer.toString();
        this.mSave.writeCrash(thread, ex, "CrashHandler", stringBuilder);

        try {
            Thread.sleep(3000L);
        } catch (InterruptedException var8) {
            var8.printStackTrace();
        }

    }
}

