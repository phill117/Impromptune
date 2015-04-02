package impromptune_gui.Dialogs;

import Renderer.MainWindow;
import com.xenoage.zong.core.Score;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
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

    public CompositionPropertiesDialog(Window owner){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("CompositionPropertiesDialog.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML void onOkay(ActionEvent event){

    }

    @FXML void onCancel(ActionEvent event){

    }
}
