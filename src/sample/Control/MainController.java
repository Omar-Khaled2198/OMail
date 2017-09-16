package sample.Control;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Callback;
import sample.Model.EmailAccount;
import sample.Model.EmailMessage;
import sample.Model.TreeFolder;
import sample.View.WindowViewer;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.ResourceBundle;

public class MainController extends SharedController implements Initializable
{
    public MainController(Access access) {
        super(access);
    }


    private MenuItem details=new MenuItem("Details");



    @FXML
    private TableColumn<EmailMessage, String> subjectCol;

    @FXML
    private TableColumn<EmailMessage, String> senderCol;

    @FXML
    private TableColumn<EmailMessage, String> sizeCol;

    @FXML
    private TableColumn<EmailAccount, String> dateCol;

    @FXML
    private TableView<EmailMessage> emailTable;

    @FXML
    private TreeView<String> emailTree;




    @FXML
    private Button button1;

    @FXML
    private Button button2;

    @FXML
    private Button button3;

    @FXML
    private WebView messagePlace;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        WindowViewer viewer = WindowViewer.sharedViewer;
        EmailAccount emailAccount=getAccess().getEmailAccount();

        subjectCol.setCellValueFactory(new PropertyValueFactory<EmailMessage, String>("subject"));
        senderCol.setCellValueFactory(new PropertyValueFactory<EmailMessage, String>("sender"));
        sizeCol.setCellValueFactory(new PropertyValueFactory<EmailMessage, String>("size"));
        dateCol.setCellValueFactory(new PropertyValueFactory<EmailAccount,String>("date"));

        sizeCol.setComparator(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                Integer n1 = EmailMessage.sizeMP.get(o1);
                Integer n2 = EmailMessage.sizeMP.get(o2);

                return n1.compareTo(n2);
            }
        });

        TreeFolder<String> root=new TreeFolder<String>(emailAccount.getEmailAddress());
        TreeFolder<String> inbox=new TreeFolder<String>("Inbox","CompleteInbox");
        TreeFolder<String> sent=new TreeFolder<String>("Sent","CompleteSent");
        TreeFolder<String> spam=new TreeFolder<String>("Spam","CompleteSpam");
        TreeFolder<String> trash=new TreeFolder<String>("Trash","CompleteTrash");
        emailTree.setRoot(root);
        root.getChildren().addAll(inbox, sent, spam, trash);
        root.setExpanded(true);

        emailTree.setOnMouseClicked(e -> {

            TreeFolder<String> clicked = (TreeFolder<String>) emailTree.getSelectionModel().getSelectedItem();
            if (clicked != null) {

                emailTable.setItems(clicked.getData());
                getAccess().setTreeFolder(clicked);
            }
        });

        emailTable.setOnMouseClicked(e -> {

            EmailMessage clicked = emailTable.getSelectionModel().getSelectedItem();
            if (clicked != null) {
                getAccess().setEmail(clicked);
                messagePlace.getEngine().loadContent(clicked.getContent());

            }
        });

        emailTable.setContextMenu(new ContextMenu(details));

        details.setOnAction(e -> {

            Scene scene = viewer.getDetailsWindows();
            Stage detailsWindow = new Stage();
            detailsWindow.setScene(scene);
            detailsWindow.setTitle("Details");
            detailsWindow.show();
        });

        emailTable.setRowFactory(new Callback<TableView<EmailMessage>, TableRow<EmailMessage>>() {
            @Override
            public TableRow<EmailMessage> call(TableView<EmailMessage> param) {
                return new TableRow<EmailMessage>() {
                    @Override
                    protected void updateItem(EmailMessage item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null && !item.isRead()) {
                            setStyle("-fx-font-weight: bold");
                        } else {
                            setStyle("");
                        }
                    }
                };
            }
        });

        button1.setOnMouseClicked((MouseEvent e) -> {
            Service<Void> service = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            emailAccount.receiveEmails(getAccess().getTreeFolder().getData(), (String) getAccess().getTreeFolder().getName());
                            emailTable.setItems(getAccess().getTreeFolder().getData());
                            emailTable.setId("my-custom");
                            getAccess().getTreeFolder().incrementUnreaded(emailAccount.unReadMessages);
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    try{
                                        //FX Stuff done here
                                    }finally{

                                    }
                                }
                            });
                            //Keep with the background work
                            return null;
                        }
                    };
                }
            };
            service.start();

        });

        button2.setOnMouseClicked(e->{

            Scene scene = viewer.getComposeWindow();
            Stage composeWindow = new Stage();
            composeWindow.setScene(scene);
            composeWindow.setTitle("Compose");
            composeWindow.show();
        });

        button3.setOnMouseClicked((MouseEvent e) ->{
            EmailMessage message=getAccess().getEmail();
            if(message!=null){
                if(message.isRead()){
                    getAccess().getEmail().setRead(false);
                    getAccess().getTreeFolder().incrementUnreaded(1);
                    button3.setText("Read");
                    emailAccount.messageReadState(getAccess().getEmail().getNumber(),false, (String) getAccess().getTreeFolder().getName());
                }

                    else {
                        getAccess().getEmail().setRead(true);
                        getAccess().getTreeFolder().decrementUnreaded(1);
                        button3.setText("Unread");
                    emailAccount.messageReadState(getAccess().getEmail().getNumber(),true, (String) getAccess().getTreeFolder().getName());
                    }
                }

            emailTable.refresh();
        });

    }

}
