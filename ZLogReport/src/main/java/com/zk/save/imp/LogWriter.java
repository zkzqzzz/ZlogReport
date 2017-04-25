package com.zk.save.imp;

import com.zk.LogReport;
import com.zk.save.ISave;
import com.zk.utils.LogUtil;

/**
 * Created by Administrator on 2017/2/20.
 */

public class LogWriter {
    private static LogWriter mLogWriter;
    private static ISave mSave;

    private LogWriter() {
    }

    public static LogWriter getInstance() {
        if(mLogWriter == null) {
            Class var0 = LogReport.class;
            synchronized(LogReport.class) {
                if(mLogWriter == null) {
                    mLogWriter = new LogWriter();
                }
            }
        }

        return mLogWriter;
    }

    public LogWriter init(ISave save) {
        mSave = save;
        return this;
    }

    public static void writeLog(String tag, String content) {
        LogUtil.d(tag, content);
        mSave.writeLog(tag, content);
    }
}

