package impromptune_gui.Dialogs;

import Renderer.MainWindow;
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
 * Created by Chris Doak on 4/4/2015.
 */

public class NewCompositionLaunch {

    /* Instance variables for this stage */
    private Stage stage;
    private BorderPane root;
    private static String result;
    public static boolean firstTime = true;


    public static void setResult(String res){result = res;}
    public String getResult(){return result;}

    public NewCompositionLaunch(MainWindow mw, Stage parent){
        CompositionPropertiesDialog.setMainWindow(mw);
        this.stage = new Stage();

        stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("impromptune_gui/logo/Impromptune Logo Icon.png")));
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if(firstTime)
                    System.exit(0);
            }
        });

        root = new BorderPane();
        FXMLLoader loader = new FXMLLoader();
        loader.setRoot(root);
        NewCompositionDialog.setMainWindow(mw);
        NewCompositionDialog.setStage(stage);
        NewCompositionDialog.setParent(parent);
        try {
            root = loader.load(getClass().getResource("NewCompositionDialog.fxml"));
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("New Composition");
        stage.showAndWait();
        firstTime = false;
    }
}
