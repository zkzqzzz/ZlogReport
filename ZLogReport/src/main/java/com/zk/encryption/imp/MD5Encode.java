package com.zk.encryption.imp;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2017/2/20.
 */

public class MD5Encode {
    public MD5Encode() {
    }

    public static String encrypt(String data) {
        try {
            MessageDigest e = MessageDigest.getInstance("MD5");
            e.reset();
            e.update(data.getBytes());
            return toHexString(e.digest());
        } catch (NoSuchAlgorithmException var2) {
            var2.printStackTrace();
            return "";
        }
    }

    private static String toHexString(byte[] bytes) {
        StringBuilder hexstring = new StringBuilder();
        byte[] var2 = bytes;
        int var3 = bytes.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            byte b = var2[var4];
            String hex = Integer.toHexString(255 & b);
            if(hex.length() == 1) {
                hexstring.append('0');
            }

            hexstring.append(hex);
        }

        return hexstring.toString();
    }
}

