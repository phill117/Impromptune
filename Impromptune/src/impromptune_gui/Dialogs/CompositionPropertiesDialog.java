package impromptune_gui.Dialogs;

import Renderer.MainWindow;
import com.xenoage.zong.core.Score;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;

/**
 * Created by cdoak_000 on 3/31/2015.
 * This is for the dialog box that allows the user to choose:
 * Title
 * Author
 * Tempo
 * etc.
 * of the composition
 */

public class CompositionPropertiesDialog {

    /* Instance Variables and FXML Components */
    @FXML TextField titleField;
    @FXML TextField composerField;
    @FXML TextField tempoField;

    private MainWindow mainWindow;
    private Stage stage;

    public CompositionPropertiesDialog(MainWindow mw){
        mainWindow = mw;
        this.stage = new Stage();
        FXMLLoader fxmlLoader;
        fxmlLoader = new FXMLLoader();
        try {
            Parent root = (BorderPane) FXMLLoader.load(getClass().getResource("CompositionPropertiesDialog.fxml"));
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("Composition Properties");
        stage.show();
    }

    @FXML void onOkay(ActionEvent event){

    }

    @FXML void onCancel(ActionEvent event){

    }
}
