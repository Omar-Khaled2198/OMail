package sample.Control;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebView;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;


public class DetailsController extends SharedController implements Initializable {


    public DetailsController(Access access) {
        super(access);
    }

    @FXML
    private WebView contentViewer;

    @FXML
    private Label subjectLab;

    @FXML
    private Label senderLab;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        subjectLab.setText("Subject : " + getAccess().getEmail().getSubject());
        senderLab.setText("Sender : " + getAccess().getEmail().getSender());
        contentViewer.getEngine().loadContent(getAccess().getEmail().getContent());
    }
}
