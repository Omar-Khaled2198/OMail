package sample.Model;


import javafx.collections.ObservableList;
import javafx.scene.control.DatePicker;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class EmailAccount {

    private String emailAdress;
    private String password;
    Properties properties;
    public int unReadMessages=0;

    private Store store;
    private Session session;
    private int loginState = State.LOGIN_STATE_NOT_READY;

    private Map<String,Integer>lastMessage;

    public EmailAccount(String EmailAddress, String Password) {
        this.emailAdress = EmailAddress;
        this.password = Password;

        properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.transport.protocol", "smtps");
        properties.put("mail.smtps.host", "smtp.gmail.com");
        properties.put("mail.smtps.auth", "true");
        properties.put("incomingHost", "imap.gmail.com");
        properties.put("outgoingHost", "smtp.gmail.com");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        lastMessage=new HashMap<String,Integer>();
        lastMessage.put("Inbox",0);
        lastMessage.put("Sent",0);
        lastMessage.put("Spam",0);
        lastMessage.put("Trash",0);

        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EmailAddress, password);
            }
        };
        session = Session.getInstance(properties, auth);
        try {
            this.store = session.getStore();
            store.connect(properties.getProperty("incomingHost"), emailAdress, password);
            System.out.println("EmailAccount constructed succesufully!!!");
            loginState = State.LOGIN_STATE_SUCCEDED;
        } catch (Exception e) {
            loginState = State.LOGIN_STATE_FAILED_BY_CREDENTIALS;
            e.printStackTrace();

        }
    }

    private Map<String,String >folderName=new HashMap<String,String>();

    public void messageReadState(int messageNumber,boolean state,String location){
        folderName.put("Inbox","Inbox");
        folderName.put("Sent","[Gmail]/Sent Mail");
        folderName.put("Trash","[Gmail]/Trash");
        folderName.put("Spam","[Gmail]/Spam");
        try {
            Folder folder = store.getFolder(folderName.get(location));
            folder.open(Folder.READ_WRITE);
            folder.getMessage(messageNumber).setFlag(Flags.Flag.SEEN,state);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    public  void sendEmails(String to,String subject,String content){

        try {
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            message.setSubject(subject);
            message.setText(content);
            Transport.send(message);
            System.out.println("message sent successfully");
        } catch (MessagingException e) {throw new RuntimeException(e);}

    }

    public void receiveEmails(ObservableList<EmailMessage> data, String location){
        try {
            folderName.put("Inbox","Inbox");
            folderName.put("Sent","[Gmail]/Sent Mail");
            folderName.put("Trash","[Gmail]/Trash");
            folderName.put("Spam","[Gmail]/Spam");
            Folder folder = store.getFolder(folderName.get(location));
            folder.open(Folder.READ_ONLY);

            for(int i = folder.getMessageCount(); i > lastMessage.get(location); i--){

                Message message = folder.getMessage(i);
                if(message.getFlags().contains(Flags.Flag.SEEN)==false)
                    unReadMessages++;
                String content=getText(message);
                Date date=message.getReceivedDate();
                Format formatter = new SimpleDateFormat("yyyy/MM/dd || h.mma");
                String dateFormated=formatter.format(date);
                EmailMessage messageBean = new EmailMessage(message.getSubject(),
                        message.getFrom()[0].toString(),
                        message.getSize(),
                        content,
                        message.getFlags().contains(Flags.Flag.SEEN),
                        message.getMessageNumber(),
                        dateFormated);
                System.out.println("Got: " + messageBean + "Number : " + i);
                data.add(messageBean);

            }
            lastMessage.put(location,lastMessage.get(location)+folder.getMessageCount());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getText(Part p) throws
            MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            String s = (String)p.getContent();
            boolean textIsHtml = p.isMimeType("text/html");
            return s;
        }

        if (p.isMimeType("multipart/alternative")) {
            Multipart mp = (Multipart)p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null)
                        text = getText(bp);
                    continue;
                } else if (bp.isMimeType("text/html")) {
                    String s = getText(bp);
                    if (s != null)
                        return s;
                } else {
                    return getText(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart)p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null)
                    return s;
            }
        }

        return null;
    }

    public String getEmailAddress() {
        return emailAdress;
    }

    public Properties getProperties() {
        return properties;
    }

    public Store getStore() {
        return store;
    }

    public Session getSession() {
        return session;
    }

    public int getLoginState() {
        return loginState;
    }

    public void setEmailAdress(String emailAdress) {
        this.emailAdress = emailAdress;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
