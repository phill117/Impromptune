package impromptune_gui.Dialogs;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

/**
 * Created by cdoak_000 on 4/9/2015.
 */
public class SaveOnCloseLaunch {
    private static String result;

    public static void setResult(String res){result = res;}

    public SaveOnCloseLaunch(){
        Stage stage = new Stage();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                result = "close";
            }
        });
        SaveOnCloseDialog.setStage(stage);
        BorderPane root = new BorderPane();
        FXMLLoader loader = new FXMLLoader();
        loader.setRoot(root);
        try {
            root = loader.load(getClass().getResource("SaveOnCloseDialog.fxml"));
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("Would you like to save?");
        stage.showAndWait();
    }

    public String getResult() {
        return result;
    }

}
