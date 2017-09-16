package sample;

import javafx.application.Application;
import javafx.beans.WeakInvalidationListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.View.WindowViewer;

import static com.sun.javafx.scene.control.skin.Utils.getResource;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        WindowViewer viewer = WindowViewer.sharedViewer;
        Scene scene=viewer.getLoginWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("O'Mail 1.0");
        primaryStage.show();


    }


    public static void main(String[] args) {
        launch(args);
    }
}
