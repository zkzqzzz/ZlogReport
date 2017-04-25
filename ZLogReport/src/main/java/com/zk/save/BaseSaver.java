package com.zk.save;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.zk.LogReport;
import com.zk.encryption.IEncryption;
import com.zk.utils.FileUtil;
import com.zk.utils.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017/2/20.
 */

public abstract class BaseSaver implements ISave {
    private static final String TAG = "BaseSaver";
    public ExecutorService mThreadPool = Executors.newFixedThreadPool(2);
    public static final SimpleDateFormat yyyy_mm_dd = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    public static final SimpleDateFormat yyyy_MM_dd_HH_mm_ss_SS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS", Locale.getDefault());
    public static final String SAVE_FILE_TYPE = ".txt";
    public static final String LOG_CREATE_TIME;
    public static String TimeLogFolder;
    public static final String LOG_FILE_NAME_MONITOR;
    public Context mContext;
    public static IEncryption mEncryption;

    public BaseSaver(Context context) {
        this.mContext = context;
    }

    public static String formatLogMsg(String tag, String tips) {
        String timeStr = yyyy_MM_dd_HH_mm_ss_SS.format(Calendar.getInstance().getTime());
        Thread currThread = Thread.currentThread();
        StringBuilder sb = new StringBuilder();
        sb.append("Thread ID: ").append(currThread.getId()).append(" Thread Name:　").append(currThread.getName()).append(" Time: ").append(timeStr).append(" FromClass: ").append(tag).append(" > ").append(tips);
        LogUtil.d("添加的内容是:\n" + sb.toString());
        return sb.toString();
    }

    public File createFile(File file, Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append("Application Information").append('\n');
        PackageManager pm = context.getPackageManager();
        ApplicationInfo ai = context.getApplicationInfo();
        sb.append("App Name : ").append(pm.getApplicationLabel(ai)).append('\n');

        try {
            PackageInfo e = pm.getPackageInfo(ai.packageName, 0);
            sb.append("Version Code: ").append(e.versionCode).append('\n');
            sb.append("Version Name: ").append(e.versionName).append('\n');
        } catch (PackageManager.NameNotFoundException var8) {
            var8.printStackTrace();
        }

        sb.append('\n');
        sb.append("DEVICE INFORMATION").append('\n');
        sb.append("BOOTLOADER: ").append(Build.BOOTLOADER).append('\n');
        sb.append("BRAND: ").append(Build.BRAND).append('\n');
        sb.append("DEVICE: ").append(Build.DEVICE).append('\n');
        sb.append("HARDWARE: ").append(Build.HARDWARE).append('\n').append('\n');
        LogUtil.d("创建的设备信息（加密前） = \n" + sb.toString());
        sb = new StringBuilder(this.encodeString(sb.toString()));
        LogUtil.d("创建的设备信息（加密后） = \n" + sb.toString());

        try {
            if(!file.exists()) {
                boolean e1 = file.createNewFile();
                if(!e1) {
                    return null;
                }
            }

            FileOutputStream e2 = new FileOutputStream(file);
            e2.write(sb.toString().getBytes());
            e2.close();
        } catch (IOException var7) {
            var7.printStackTrace();
        }

        return file;
    }

    public void setEncodeType(IEncryption encodeType) {
        mEncryption = encodeType;
    }

    public String encodeString(String content) {
        if(mEncryption != null) {
            try {
                return mEncryption.encrypt(content);
            } catch (Exception var3) {
                Log.e("BaseSaver", var3.toString());
                var3.printStackTrace();
                return content;
            }
        } else {
            return content;
        }
    }

    public String decodeString(String content) {
        if(mEncryption != null) {
            try {
                return mEncryption.decrypt(content);
            } catch (Exception var3) {
                Log.e("BaseSaver", var3.toString());
                var3.printStackTrace();
                return content;
            }
        } else {
            return content;
        }
    }

    public void writeLog(final String tag, final String content) {
        this.mThreadPool.execute(new Runnable() {
            public void run() {
                Class var1 = BaseSaver.class;
                synchronized(BaseSaver.class) {
                    BaseSaver.TimeLogFolder = LogReport.getInstance().getROOT() + "/Log/" + BaseSaver.yyyy_mm_dd.format(new Date(System.currentTimeMillis())) + "/";
                    File logsDir = new File(BaseSaver.TimeLogFolder);
                    File logFile = new File(logsDir, BaseSaver.LOG_FILE_NAME_MONITOR);
                    if(!Environment.getExternalStorageState().equals("mounted")) {
                        LogUtil.d("SDcard 不可用");
                    } else {
                        if(!logsDir.exists()) {
                            LogUtil.d("logsDir.mkdirs() =  +　" + logsDir.mkdirs());
                        }

                        if(!logFile.exists()) {
                            BaseSaver.this.createFile(logFile, BaseSaver.this.mContext);
                        }

                        BaseSaver.this.writeText(logFile, BaseSaver.this.decodeString(FileUtil.getText(logFile)) + BaseSaver.formatLogMsg(tag, content) + "\n");
                    }
                }
            }
        });
    }

    public void writeText(File logFile, String content) {
        FileOutputStream outputStream = null;

        try {
            String e = this.encodeString(content);
            LogUtil.d("最终写到文本的Log：\n" + content);
            outputStream = new FileOutputStream(logFile);
            outputStream.write(e.getBytes("UTF-8"));
        } catch (Exception var13) {
            Log.e("BaseSaver", var13.toString());
            var13.printStackTrace();
        } finally {
            if(outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (Exception var12) {
                    var12.printStackTrace();
                }
            }

        }

    }

    static {
        LOG_CREATE_TIME = yyyy_mm_dd.format(new Date(System.currentTimeMillis()));
        LOG_FILE_NAME_MONITOR = "MonitorLog" + LOG_CREATE_TIME + ".txt";
    }
}

