package com.zk.upload.http;

import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

/**
 * Created by Administrator on 2017/2/20.
 */

public class SimpleMultipartEntity implements HttpEntity {
    private static final char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private ByteArrayOutputStream out = new ByteArrayOutputStream();
    boolean isSetFirst = false;
    boolean isSetLast = false;
    private String boundary = this.generateBoundary();

    public SimpleMultipartEntity() {
    }

    protected String generateBoundary() {
        StringBuilder buffer = new StringBuilder();
        Random rand = new Random();
        int count = rand.nextInt(11) + 30;

        for(int i = 0; i < count; ++i) {
            buffer.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
        }

        return buffer.toString();
    }

    public void writeFirstBoundaryIfNeeds() {
        if(!this.isSetFirst) {
            try {
                this.out.write(("--" + this.boundary + "\r\n").getBytes());
            } catch (IOException var2) {
                Log.w(var2.getMessage(), var2);
            }
        }

        this.isSetFirst = true;
    }

    public void writeLastBoundaryIfNeeds() {
        if(!this.isSetLast) {
            try {
                this.out.write(("\r\n--" + this.boundary + "--\r\n").getBytes());
            } catch (IOException var2) {
                Log.w(var2.getMessage(), var2);
            }

            this.isSetLast = true;
        }

    }

    public void addPart(String key, String value) {
        this.writeFirstBoundaryIfNeeds();

        try {
            this.out.write(("Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n").getBytes());
            this.out.write(value.getBytes());
            this.out.write(("\r\n--" + this.boundary + "\r\n").getBytes());
        } catch (IOException var4) {
            var4.printStackTrace();
        }

    }

    public void addPart(String key, String fileName, InputStream fin, boolean isLast) {
        this.addPart(key, fileName, fin, "application/octet-stream", isLast);
    }

    public void addPart(String key, String fileName, InputStream fin, String type, boolean isLast) {
        this.writeFirstBoundaryIfNeeds();

        try {
            type = "Content-Type: " + type + "\r\n";
            this.out.write(("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + fileName + "\"\r\n").getBytes());
            this.out.write(type.getBytes());
            this.out.write("Content-Transfer-Encoding: binary\r\n\r\n".getBytes());
            byte[] e = new byte[4096];

            int l;
            while((l = fin.read(e)) != -1) {
                this.out.write(e, 0, l);
            }

            if(!isLast) {
                this.out.write(("\r\n--" + this.boundary + "\r\n").getBytes());
            }

            this.out.flush();
        } catch (IOException var16) {
            var16.printStackTrace();
        } finally {
            try {
                fin.close();
            } catch (IOException var15) {
                var15.printStackTrace();
            }

        }

    }

    public void addPart(String key, File value, boolean isLast) {
        try {
            this.addPart(key, value.getName(), new FileInputStream(value), isLast);
        } catch (FileNotFoundException var5) {
            var5.printStackTrace();
        }

    }

    public long getContentLength() {
        this.writeLastBoundaryIfNeeds();
        return (long)this.out.toByteArray().length;
    }

    public Header getContentType() {
        return new BasicHeader("Content-Type", "multipart/form-data; boundary=" + this.boundary);
    }

    public boolean isChunked() {
        return false;
    }

    public boolean isRepeatable() {
        return false;
    }

    public boolean isStreaming() {
        return false;
    }

    public void writeTo(OutputStream outstream) throws IOException {
        outstream.write(this.out.toByteArray());
    }

    public Header getContentEncoding() {
        return null;
    }

    public void consumeContent() throws IOException, UnsupportedOperationException {
        if(this.isStreaming()) {
            throw new UnsupportedOperationException("Streaming entity does not implement #consumeContent()");
        }
    }

    public InputStream getContent() throws IOException, UnsupportedOperationException {
        return new ByteArrayInputStream(this.out.toByteArray());
    }
}

