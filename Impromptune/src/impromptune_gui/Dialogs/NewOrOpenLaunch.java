package impromptune_gui.Dialogs;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by cdoak_000 on 4/9/2015.
 */
public class NewOrOpenLaunch {
    private static String result;

    public static void setResult(String res){result = res;}

    public NewOrOpenLaunch(){
        Stage stage = new Stage();
        NewOrOpenDialog.setStage(stage);
        BorderPane root = new BorderPane();
        FXMLLoader loader = new FXMLLoader();
        loader.setRoot(root);
        try {
            root = loader.load(getClass().getResource("NewOrOpenDialog.fxml"));
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("Welcome to Impromptune!");
        stage.showAndWait();
    }

    public String getResult() {
        return result;
    }
}
