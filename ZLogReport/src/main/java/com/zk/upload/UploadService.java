package com.zk.upload;

import android.app.IntentService;
import android.content.Intent;

import com.zk.LogReport;
import com.zk.utils.CompressUtil;
import com.zk.utils.FileUtil;
import com.zk.utils.LogUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

/**
 * Created by Administrator on 2017/2/20.
 */

public class UploadService extends IntentService {
    public static final String TAG = "UploadService";
    public static final SimpleDateFormat ZIP_FOLDER_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS", Locale.getDefault());

    public UploadService() {
        super("UploadService");
    }

    public void onCreate() {
        super.onCreate();
    }

    protected void onHandleIntent(Intent intent) {
        final File logfolder = new File(LogReport.getInstance().getROOT() + "Log/");
        if(logfolder.exists() && logfolder.listFiles().length != 0) {
            ArrayList crashFileList = FileUtil.getCrashList(logfolder);
            if(crashFileList.size() == 0) {
                LogUtil.d("UploadService", "只存在log文件，但是不存在崩溃日志，所以不上传");
            } else {
                File zipfolder = new File(LogReport.getInstance().getROOT() + "AlreadyUploadLog/");
                File zipfile = new File(zipfolder, "UploadOn" + ZIP_FOLDER_TIME_FORMAT.format(Long.valueOf(System.currentTimeMillis())) + ".zip");
                final File rootdir = new File(LogReport.getInstance().getROOT());
                StringBuilder content = new StringBuilder();
                zipfile = FileUtil.createFile(zipfolder, zipfile);
                if(CompressUtil.zipFileAtPath(logfolder.getAbsolutePath(), zipfile.getAbsolutePath())) {
                    LogUtil.d("把日志文件压缩到压缩包中 ----> 成功");
                    Iterator var8 = crashFileList.iterator();

                    while(var8.hasNext()) {
                        File crash = (File)var8.next();
                        content.append(FileUtil.getText(crash));
                        content.append("\n");
                    }

                    LogReport.getInstance().getUpload().sendFile(zipfile, content.toString(), new ILogUpload.OnUploadFinishedListener() {
                        public void onSuceess() {
                            LogUtil.d("日志发送成功！！");
                            FileUtil.deleteDir(logfolder);
                            boolean checkresult = UploadService.this.checkCacheSize(rootdir);
                            LogUtil.d("缓存大小检查，是否删除root下的所有文件 = " + checkresult);
                            UploadService.this.stopSelf();
                        }

                        public void onError(String error) {
                            LogUtil.d("日志发送失败：  = " + error);
                            boolean checkresult = UploadService.this.checkCacheSize(rootdir);
                            LogUtil.d("缓存大小检查，是否删除root下的所有文件 " + checkresult);
                            UploadService.this.stopSelf();
                        }
                    });
                } else {
                    LogUtil.d("把日志文件压缩到压缩包中 ----> 失败");
                }

            }
        } else {
            LogUtil.d("Log文件夹都不存在，无需上传");
        }
    }

    public boolean checkCacheSize(File dir) {
        long dirSize = FileUtil.folderSize(dir);
        return dirSize >= LogReport.getInstance().getCacheSize() && FileUtil.deleteDir(dir);
    }
}

