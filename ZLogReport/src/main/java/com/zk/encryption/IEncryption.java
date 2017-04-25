package com.zk.encryption;

/**
 * Created by Administrator on 2017/2/20.
 */

public interface IEncryption {
    String encrypt(String var1) throws Exception;

    String encrypt(String var1, String var2) throws Exception;

    String decrypt(String var1, String var2) throws Exception;

    String decrypt(String var1) throws Exception;
}