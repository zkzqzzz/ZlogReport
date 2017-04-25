package com.zk.upload.http;

import android.content.Context;
import android.util.Log;

import com.zk.upload.BaseUpload;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/20.
 */

public class HttpReporter extends BaseUpload {
    HttpClient httpclient = new DefaultHttpClient();
    private String url;
    private Map<String, String> otherParams;
    private String titleParam;
    private String bodyParam;
    private String fileParam;
    private String to;
    private String toParam;
    private HttpReporter.HttpReportCallback callback;

    public HttpReporter(Context context) {
        super(context);
    }

    protected void sendReport(String title, String body, File file, OnUploadFinishedListener onUploadFinishedListener) {
        SimpleMultipartEntity entity = new SimpleMultipartEntity();
        entity.addPart(this.titleParam, title);
        entity.addPart(this.bodyParam, body);
        entity.addPart(this.toParam, this.to);
        if(this.otherParams != null) {
            Iterator e = this.otherParams.entrySet().iterator();

            while(e.hasNext()) {
                Map.Entry resp = (Map.Entry)e.next();
                entity.addPart((String)resp.getKey(), (String)resp.getValue());
            }
        }

        entity.addPart(this.fileParam, file, true);

        try {
            HttpPost e1 = new HttpPost(this.url);
            e1.setEntity(entity);
            HttpResponse resp1 = this.httpclient.execute(e1);
            int statusCode = resp1.getStatusLine().getStatusCode();
            String responseString = EntityUtils.toString(resp1.getEntity());
            onUploadFinishedListener.onSuceess();
        } catch (Exception var10) {
            onUploadFinishedListener.onError("Http send fail!!!!!!");
            var10.printStackTrace();
        }

    }

    private boolean deleteLog(File file) {
        Log.d("HttpReporter", "delete: " + file.getName());
        return file.delete();
    }

    public String getUrl() {
        return this.url;
    }

    public HttpReporter setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getTitleParam() {
        return this.titleParam;
    }

    public HttpReporter setTitleParam(String titleParam) {
        this.titleParam = titleParam;
        return this;
    }

    public String getBodyParam() {
        return this.bodyParam;
    }

    public HttpReporter setBodyParam(String bodyParam) {
        this.bodyParam = bodyParam;
        return this;
    }

    public String getFileParam() {
        return this.fileParam;
    }

    public HttpReporter setFileParam(String fileParam) {
        this.fileParam = fileParam;
        return this;
    }

    public Map<String, String> getOtherParams() {
        return this.otherParams;
    }

    public void setOtherParams(Map<String, String> otherParams) {
        this.otherParams = otherParams;
    }

    public String getTo() {
        return this.to;
    }

    public HttpReporter setTo(String to) {
        this.to = to;
        return this;
    }

    public HttpReporter.HttpReportCallback getCallback() {
        return this.callback;
    }

    public HttpReporter setCallback(HttpReporter.HttpReportCallback callback) {
        this.callback = callback;
        return this;
    }

    public String getToParam() {
        return this.toParam;
    }

    public HttpReporter setToParam(String toParam) {
        this.toParam = toParam;
        return this;
    }

    public interface HttpReportCallback {
        boolean isSuccess(int var1, String var2);
    }
}

