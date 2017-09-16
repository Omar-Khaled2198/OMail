package sample.Model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class EmailMessage {

    private SimpleStringProperty subject;
    private SimpleStringProperty sender;
    private SimpleStringProperty size;
    private String content;
    private SimpleBooleanProperty read=new SimpleBooleanProperty(true);
    private SimpleIntegerProperty number;
    private SimpleStringProperty date;

    public boolean isRead() {
        return read.get();
    }

    public void setRead(boolean read) {
        this.read.set(read);
    }

    public boolean getRead() {
        return read.get();
    }

    public SimpleBooleanProperty readProperty() {
        return read;
    }

    public static Map<String,Integer>sizeMP=new HashMap<String,Integer>();

    public EmailMessage(String subject, String sender, int size, String content, boolean read,int number,String date) {
        this.read=new SimpleBooleanProperty(read);
        this.subject = new SimpleStringProperty(subject);
        this.sender = new SimpleStringProperty(sender);
        this.size = new SimpleStringProperty(sizeFormat(size));
        this.content=content;
        this.number=new SimpleIntegerProperty(number);
        this.date =new SimpleStringProperty(date);
    }

    public String getSubject() {
        return subject.get();
    }

    public String getSender() {
        return sender.get();
    }

    public String getSize() {
        return size.get();
    }

    public String getContent() {
        return content;
    }

    public int getNumber() {
        return number.get();
    }

    public SimpleIntegerProperty numberProperty() {
        return number;
    }

    public void setNumber(int number) {
        this.number.set(number);
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    private String sizeFormat(int size) {

        String result;

        if(size<=0)
            result="0";
        else if(size<=1024)
            result=size+"B";
        else if(size<=1048576)
            result =size/1024+"KB";
        else
            result=size/1048576+"MB";


        sizeMP.put(result,size);
        return result;

    }

}
