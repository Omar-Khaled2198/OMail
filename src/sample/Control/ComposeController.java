package sample.Control;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import org.jsoup.*;
import sample.Model.EmailAccount;

import java.net.URL;
import java.util.ResourceBundle;


public class ComposeController extends SharedController implements Initializable {

    @FXML
    private HTMLEditor textEditor;

    @FXML
    private TextField to;

    @FXML
    private TextField subject;

    @FXML
    private Button send;

    public ComposeController(Access access) {
        super(access);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        send.setOnMouseClicked(e->{
            String content= Jsoup.parse(textEditor.getHtmlText()).text();
            EmailAccount emailAccount=getAccess().getEmailAccount();
            emailAccount.sendEmails(to.getText(),subject.getText(),content);
        });


    }
}
