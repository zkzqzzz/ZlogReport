package com.zk.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/20.
 */

public class FileUtil {
    private static String TAG = "FileUtil";

    public FileUtil() {
    }

    public static boolean deleteDir(File dir) {
        if(dir.isDirectory()) {
            String[] children = dir.list();
            String[] var2 = children;
            int var3 = children.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                String aChildren = var2[var4];
                boolean success = deleteDir(new File(dir, aChildren));
                if(!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    public static String getText(File file) {
        if(!file.exists()) {
            return null;
        } else {
            StringBuilder text = new StringBuilder();
            BufferedReader br = null;

            try {
                br = new BufferedReader(new FileReader(file));

                String e;
                while((e = br.readLine()) != null) {
                    text.append(e);
                    text.append('\n');
                }
            } catch (IOException var12) {
                Log.e(TAG, var12.toString());
                var12.printStackTrace();
            } finally {
                if(br != null) {
                    try {
                        br.close();
                    } catch (IOException var11) {
                        var11.printStackTrace();
                    }
                }

            }

            return text.toString();
        }
    }

    public static ArrayList<File> getCrashList(File logdir) {
        ArrayList crashFileList = new ArrayList();
        findFiles(logdir.getAbsolutePath(), crashFileList);
        return crashFileList;
    }

    public static void findFiles(String baseDirName, List<File> fileList) {
        File baseDir = new File(baseDirName);
        if(!baseDir.exists() || !baseDir.isDirectory()) {
            LogUtil.e(TAG, "文件查找失败：" + baseDirName + "不是一个目录！");
        }

        File[] files = baseDir.listFiles();
        File[] var6 = files;
        int var7 = files.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            File file = var6[var8];
            if(file.isDirectory()) {
                findFiles(file.getAbsolutePath(), fileList);
            } else if(file.isFile()) {
                String tempName = file.getName();
                if(tempName.contains("Crash")) {
                    fileList.add(file.getAbsoluteFile());
                }
            }
        }

    }

    public static long folderSize(File directory) {
        long length = 0L;
        File[] var3 = directory.listFiles();
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            File file = var3[var5];
            if(file.isFile()) {
                length += file.length();
            } else {
                length += folderSize(file);
            }
        }

        return length;
    }

    public static File createFile(File zipdir, File zipfile) {
        boolean e;
        if(!zipdir.exists()) {
            e = zipdir.mkdirs();
            LogUtil.d("TAG", "zipdir.mkdirs() = " + e);
        }

        if(!zipfile.exists()) {
            try {
                e = zipfile.createNewFile();
                LogUtil.d("TAG", "zipdir.createNewFile() = " + e);
            } catch (IOException var3) {
                var3.printStackTrace();
                Log.e("TAG", var3.getMessage());
            }
        }

        return zipfile;
    }
}
