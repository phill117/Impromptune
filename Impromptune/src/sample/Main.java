package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {




    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World - No hablo espanol");
        primaryStage.setScene(new Scene(root, 500, 500));
      //  primaryStage.setFullScreen(true); ANNOYING AS HELL!
        primaryStage.show();
    }

    @FXML
    void zong(ActionEvent event) {
        System.out.println("HELLO");
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
