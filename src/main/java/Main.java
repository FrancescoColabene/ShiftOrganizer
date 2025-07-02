
import Robe.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.*;

/**
 * This class is the "main" class in the sense that its called from the "Test" class to work while being a jar.
 */
public class Main extends Application {

    private static Stage stg;
    public static Calendario calendario = null;

    /**
     * This method is called from the launch method: it setups up the stage and sets the first scene.
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        this.stg = stage;

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxml/InitialPage.fxml")));
        stage.setTitle("UwU Maker");
        stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("/icon.png"))));
        stage.setResizable(false);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        if(screen.getWidth() == 1920) stage.setScene(new Scene(root,1000,800));
        else stage.setScene(new Scene(root,1000,750));
        //stage.setFullScreen(true);
        stage.show();
    }

    /**
     * This method is called from every controller to change the main scene
     * @param fxml to set
     * @throws IOException if there is a problem finding or opening the new fxml
     */
    public void changeScene(String fxml) throws IOException{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml)));
        this.stg.getScene().setRoot(root);
    }

    public static void main(String[] args) {
        // si ferma fino a che non chiudo la finestra aka devo spostare il main dentro allo start
        launch();
    }


}