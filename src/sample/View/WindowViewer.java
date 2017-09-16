package sample.View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import sample.Control.*;

public class WindowViewer {

    public static WindowViewer sharedViewer=new WindowViewer();

    private Access access =new Access();
    private MainController mainController;
    private DetailsController detailsController;
    private LoginController loginController;
    private ComposeController composeController;

    public Scene getMainWindow(){

        mainController=new MainController(access);
        return setScene("MainWindow.fxml",mainController);
    }

    public Scene getDetailsWindows(){

        detailsController=new DetailsController(access);
        return setScene("DetailsWindow.fxml",detailsController);
    }

    public Scene getLoginWindow(){

        loginController=new LoginController(access);
        return setScene("LoginWindow.fxml",loginController);
    }

    public Scene getComposeWindow(){

        composeController=new ComposeController(access);
        return setScene("ComposeWindow.fxml",composeController);
    }

    public Node setIcons(String treeValue){
        String treeIcon=treeValue.toLowerCase();
        ImageView mg=new ImageView();
        try {
            if(treeIcon.contains("at"))
                mg.setImage(new Image(getClass().getResourceAsStream("Icons/email.png")));
            else if(treeIcon.contains("inbox"))
                mg.setImage(new Image(getClass().getResourceAsStream("Icons/inbox.png")));
            else if(treeIcon.contains("sent"))
                mg.setImage(new Image(getClass().getResourceAsStream("Icons/sent.png")));
            else if(treeIcon.contains("spam"))
                mg.setImage(new Image(getClass().getResourceAsStream("Icons/spam.png")));
            else if(treeIcon.contains("trash"))
                mg.setImage(new Image(getClass().getResourceAsStream("Icons/trash.png")));
            else if(treeIcon.contains("folder"))
                mg.setImage(new Image(getClass().getResourceAsStream("Icons/folder.png")));
        }
        catch (NullPointerException e) {

            e.printStackTrace();
            return mg;
        }
        mg.setFitHeight(20);
        mg.setFitWidth(20);

        return mg;
    }

    public Scene setScene(String fxml, SharedController controller){

        FXMLLoader loader;
        Pane pane;
        Scene scene;
        try {
            loader=new FXMLLoader(getClass().getResource(fxml));
            loader.setController(controller);
            pane=loader.load();
        }
        catch (Exception e) {
            return null;
        }
        scene=new Scene(pane);
        scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
        return scene;
    }

}
