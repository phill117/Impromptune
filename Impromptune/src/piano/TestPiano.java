package piano;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Sean on 2/20/2015.
 */
public class TestPiano extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("PianoHolder.fxml"));
        primaryStage.setTitle("Piano");
        primaryStage.setScene(new Scene(root, 1200, 500));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
