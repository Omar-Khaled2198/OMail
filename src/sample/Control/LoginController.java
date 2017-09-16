package sample.Control;



import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.Model.EmailAccount;
import sample.View.WindowViewer;

import javax.mail.*;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;


public class LoginController extends SharedController implements Initializable {

    public LoginController(Access access) {
        super(access);
    }

    @FXML
    private TextField addressInput;

    @FXML
    private TextField passwordInput;

    @FXML
    private Button login;

    @FXML
    private Label error;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        addressInput.setOnAction(e->{

            error.setText("");
        });

        login.setOnMouseClicked(event->{

            EmailAccount emailAccount=new EmailAccount(addressInput.getText(),passwordInput.getText());

            if(emailAccount.getLoginState()==2)
                error.setText("Email Adress or Password is Wrong");
            else {
                getAccess().setEmailAccount(emailAccount);

                WindowViewer viewer = WindowViewer.sharedViewer;
                Scene scene = viewer.getMainWindow();
                Stage current=(Stage)login.getScene().getWindow();

                Stage stage = new Stage();
                current.close();
                stage.setScene(scene);
                stage.setTitle("O'Mail 1.0" + "(" + addressInput.getText() + ")");
                stage.show();
                error.setText("");
            }
        });
    }

}
