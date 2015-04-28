package impromptune_gui.Dialogs;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

/**
 * Created by cdoak_000 on 4/9/2015.
 */
public class NewOrOpenLaunch {
    private static String result;
    private static boolean firstTime = true;
    private boolean hitX = false;

    public static void setResult(String res){result = res;}

    public NewOrOpenLaunch(){
        Stage stage = new Stage();
        stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("impromptune_gui/logo/Impromptune Logo Icon.png")));
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if(firstTime)
                    System.exit(0);
                else
                    hitX = true;
            }
        });
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
        if(firstTime)
            stage.setTitle("Welcome to Impromptune!");
        else
            stage.setTitle("Impromptune");
        stage.showAndWait();
        firstTime = false;
    }

    public String getResult() {
        return result;
    }

    public boolean didHitX(){
        return hitX;
    }
}
