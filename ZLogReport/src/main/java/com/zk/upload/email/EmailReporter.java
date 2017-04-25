package com.zk.upload.email;

import android.content.Context;

import com.zk.upload.BaseUpload;

import java.io.File;

/**
 * Created by Administrator on 2017/2/20.
 */

public class EmailReporter extends BaseUpload {
    private String mReceiveEmail;
    private String mSendEmail;
    private String mSendPassword;
    private String mHost;
    private String mPort;

    public EmailReporter(Context context) {
        super(context);
    }

    public void setReceiver(String receiveEmail) {
        this.mReceiveEmail = receiveEmail;
    }

    public void setSender(String email) {
        this.mSendEmail = email;
    }

    public void setSendPassword(String password) {
        this.mSendPassword = password;
    }

    public void setSMTPHost(String host) {
        this.mHost = host;
    }

    public void setPort(String port) {
        this.mPort = port;
    }

    protected void sendReport(String title, String body, File file, OnUploadFinishedListener onUploadFinishedListener) {
        MailInfo sender = (new MailInfo()).setUser(this.mSendEmail).setPass(this.mSendPassword).setFrom(this.mSendEmail).setTo(this.mReceiveEmail).setHost(this.mHost).setPort(this.mPort).setSubject(title).setBody(body);
        sender.init();

        try {
            sender.addAttachment(file.getPath(), file.getName());
            sender.send();
            onUploadFinishedListener.onSuceess();
        } catch (Exception var7) {
            onUploadFinishedListener.onError("Send Email fail！Accout or SMTP verification error ！");
            var7.printStackTrace();
        }

    }
}
