package com.zk.upload;

import java.io.File;

/**
 * Created by Administrator on 2017/2/20.
 */
public interface ILogUpload {

    void sendFile(File var1, String var2, ILogUpload.OnUploadFinishedListener var3);

    interface OnUploadFinishedListener {
        void onSuceess();

        void onError(String var1);
    }
}