package impromptune_gui;
/**
 * Created by doak on 2/25/2015.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Impromptune extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("EditView.fxml"));
        primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("impromptune_gui/logo/Impromptune Logo Same Font (small).png")));
        primaryStage.setTitle("Impromptune");
        primaryStage.setScene(new Scene(root));
        //primaryStage.setOnCloseRequest(e -> System.exit(0)); // Exits program when X is closed (Jacob) make this a function to stopplayback
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getStage(){
        return primaryStage;
    }
}
