package sample.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import sample.View.WindowViewer;


public class TreeFolder<String> extends TreeItem<String>{

    private boolean top=false;
    private String name;
    private String completeName;
    private int unReaded=0;
    private ObservableList<EmailMessage>data= FXCollections.observableArrayList();

    public TreeFolder(String val){

        super(val, WindowViewer.sharedViewer.setIcons("at"));
        name=val;
        completeName=val;
        data=null;
        top=true;
        this.setExpanded(true);
    }

    public TreeFolder(String val, String completeName){

        super(val, WindowViewer.sharedViewer.setIcons((java.lang.String) val));
        name=val;
        this.completeName=completeName;
    }

    public boolean isTop() {
        return top;
    }

    public void setTop(boolean top) {
        this.top = top;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void update(){

        if(unReaded>0){

            this.setValue((String) (name + "(" + unReaded + ")"));
        }
        else
            this.setValue(name);
    }

    public void incrementUnreaded(int Messages){
        unReaded+=Messages;
        update();
    }

    public void decrementUnreaded(int Messages){
        unReaded-=Messages;
        update();
    }

    public void setData(ObservableList<EmailMessage> data) {
        this.data = data;
    }

    public ObservableList<EmailMessage> getData() {
        return data;
    }

    void addEmail(EmailMessage message){

        data.add(message);
        if(!message.isRead()){
            incrementUnreaded(1);
        }
    }
}

