package com.zk.encryption.imp;

import android.util.Base64;

import com.zk.encryption.IEncryption;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Administrator on 2017/2/20.
 */

public class DESEncode implements IEncryption {
    private static final String transformation = "DES/CBC/PKCS5Padding";
    private static byte[] iv = new byte[]{(byte)1, (byte)2, (byte)3, (byte)4, (byte)5, (byte)6, (byte)7, (byte)8};
    private static final String DEFAULT_KEY = "zk";

    public DESEncode() {
    }

    public String encrypt(String src) throws Exception {
        return this.encrypt("zk", src);
    }

    public String decrypt(String src) throws Exception {
        return this.decrypt("zk", src);
    }

    public String encrypt(String key, String src) throws Exception {
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(1, secretKeySpec, zeroIv);
        byte[] encryptedData = cipher.doFinal(src.getBytes());
        return Base64.encodeToString(encryptedData, 0);
    }

    public String decrypt(String key, String encodeString) throws Exception {
        byte[] byteMi = Base64.decode(encodeString, 0);
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(2, secretKeySpec, zeroIv);
        byte[] decryptedData = cipher.doFinal(byteMi);
        return new String(decryptedData);
    }
}
