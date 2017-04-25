package com.zk.save;

import com.zk.encryption.IEncryption;

/**
 * Created by Administrator on 2017/2/20.
 */

public interface ISave {
    void writeLog(String var1, String var2);

    void writeCrash(Thread var1, Throwable var2, String var3, String var4);

    void setEncodeType(IEncryption var1);
}