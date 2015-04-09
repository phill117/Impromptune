package impromptune_gui.Dialogs;

import Renderer.MainWindow;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Chris Doak on 4/4/2015.
 */

public class CompositionPropertiesLaunch {

    /* Instance variables for this stage */
    private Stage stage;
    private BorderPane root;

    public CompositionPropertiesLaunch(MainWindow mw){
        CompositionPropertiesDialog.setMainWindow(mw);
        this.stage = new Stage();
        root = new BorderPane();
        FXMLLoader loader = new FXMLLoader();
        loader.setRoot(root);
        try {
            root = loader.load(getClass().getResource("CompositionPropertiesDialog.fxml"));
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("Composition Properties");
        CompositionPropertiesDialog.setStage(stage);
        stage.show();
    }
}
