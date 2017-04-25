package com.zk.upload.email;

import java.util.Date;
import java.util.Properties;
import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeMessage.RecipientType;
/**
 * Created by Administrator on 2017/2/20.
 */

public class MailInfo extends Authenticator {
    private String host;
    private String port;
    private String user;
    private String pass;
    private String from;
    private String to;
    private String subject;
    private String body;
    private Multipart multipart;
    private Properties props;

    public MailInfo() {
    }

    public MailInfo(String user, String pass, String from, String to, String host, String port, String subject, String body) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.pass = pass;
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

    public MailInfo setHost(String host) {
        this.host = host;
        return this;
    }

    public MailInfo setPort(String port) {
        this.port = port;
        return this;
    }

    public MailInfo setUser(String user) {
        this.user = user;
        return this;
    }

    public MailInfo setPass(String pass) {
        this.pass = pass;
        return this;
    }

    public MailInfo setFrom(String from) {
        this.from = from;
        return this;
    }

    public MailInfo setTo(String to) {
        this.to = to;
        return this;
    }

    public MailInfo setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public MailInfo setBody(String body) {
        this.body = body;
        return this;
    }

    public void init() {
        this.multipart = new MimeMultipart();
        MailcapCommandMap mc = (MailcapCommandMap)CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(mc);
        this.props = new Properties();
        this.props.put("mail.smtp.host", this.host);
        this.props.put("mail.smtp.auth", "true");
        this.props.put("mail.smtp.port", this.port);
        this.props.put("mail.smtp.socketFactory.port", this.port);
        this.props.put("mail.transport.protocol", "smtp");
        this.props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        this.props.put("mail.smtp.socketFactory.fallback", "false");
    }

    public boolean send() throws MessagingException {
        if(!this.user.equals("") && !this.pass.equals("") && !this.to.equals("") && !this.from.equals("")) {
            Session session = Session.getDefaultInstance(this.props, this);
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(this.from));
            InternetAddress addressTo = new InternetAddress(this.to);
            msg.setRecipient(RecipientType.TO, addressTo);
            msg.setSubject(this.subject);
            msg.setSentDate(new Date());
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(this.body);
            this.multipart.addBodyPart(messageBodyPart, 0);
            msg.setContent(this.multipart);
            Transport.send(msg);
            return true;
        } else {
            return false;
        }
    }

    public void addAttachment(String filePath, String fileName) throws Exception {
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.attachFile(filePath);
        this.multipart.addBodyPart(messageBodyPart);
    }

    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(this.user, this.pass);
    }
}
