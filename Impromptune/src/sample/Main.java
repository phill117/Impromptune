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
        //String s =  getClass().getClassLoader().getResource("/demos/MainWindow.fxml").toString();
        Parent root = FXMLLoader.load(getClass().getResource("../Zong/demos/src/com/xenoage/zong/demos/simplegui/MainWindow.fxml"));
        primaryStage.setTitle("NOOO!!!!!!Hello World - No hablo espanol");
        primaryStage.setScene(new Scene(root, 1000, 1000));
      //  primaryStage.setFullScreen(true); ANNOYING AS HELL!
        primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
