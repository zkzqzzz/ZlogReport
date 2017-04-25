package com.zk.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by Administrator on 2017/2/20.
 */

public class CompressUtil {
    public CompressUtil() {
    }

    public static boolean zipFileAtPath(String sourcePath, String toLocation) {
        boolean BUFFER = true;
        File sourceFile = new File(sourcePath);

        try {
            FileOutputStream dest = new FileOutputStream(toLocation);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            if(sourceFile.isDirectory()) {
                zipSubFolder(out, sourceFile, sourceFile.getParent().length());
            } else {
                byte[] data = new byte[2048];
                FileInputStream fi = new FileInputStream(sourcePath);
                BufferedInputStream e = new BufferedInputStream(fi, 2048);
                ZipEntry entry = new ZipEntry(getLastPathComponent(sourcePath));
                out.putNextEntry(entry);

                int count;
                while((count = e.read(data, 0, 2048)) != -1) {
                    out.write(data, 0, count);
                }
            }

            out.close();
            return true;
        } catch (Exception var11) {
            var11.printStackTrace();
            return false;
        }
    }

    private static void zipSubFolder(ZipOutputStream out, File folder, int basePathLength) throws IOException {
        boolean BUFFER = true;
        File[] fileList = folder.listFiles();
        File[] var6 = fileList;
        int var7 = fileList.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            File file = var6[var8];
            if(file.isDirectory()) {
                zipSubFolder(out, file, basePathLength);
            } else {
                byte[] data = new byte[2048];
                String unmodifiedFilePath = file.getPath();
                String relativePath = unmodifiedFilePath.substring(basePathLength);
                FileInputStream fi = new FileInputStream(unmodifiedFilePath);
                BufferedInputStream origin = new BufferedInputStream(fi, 2048);
                ZipEntry entry = new ZipEntry(relativePath);
                out.putNextEntry(entry);

                int count;
                while((count = origin.read(data, 0, 2048)) != -1) {
                    out.write(data, 0, count);
                }

                origin.close();
            }
        }

    }

    public static String getLastPathComponent(String filePath) {
        String[] segments = filePath.split("/");
        if(segments.length == 0) {
            return "";
        } else {
            String lastPathComponent = segments[segments.length - 1];
            return lastPathComponent;
        }
    }
}

