package impromptune_gui;
/**
 * Created by doak on 2/25/2015.
 */

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Impromptune extends Application {

    private Stage primaryStage;
    private BorderPane baseLayout;
    private AnchorPane editView;

    @Override
    public void start(Stage primaryStage) throws Exception{
        //String s =  getClass().getClassLoader().getResource("/demos/MainWindow.fxml").toString();
        Parent root = FXMLLoader.load(getClass().getResource("EditView.fxml"));
        primaryStage.setTitle("Impromptune");
        primaryStage.setScene(new Scene(root, 1000, 1000));
        primaryStage.setOnCloseRequest(e -> System.exit(0)); // Exits program when X is closed (Jacob) make this a function to stopplayback
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
