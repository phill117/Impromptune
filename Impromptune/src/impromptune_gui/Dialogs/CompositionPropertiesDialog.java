package impromptune_gui.Dialogs;

import Renderer.Content;
import Renderer.MainWindow;
import com.sun.org.apache.xml.internal.security.Init;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.info.ScoreInfo;
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
import sample.Controller;

import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;

/**
 * Created by doak on 3/31/2015.
 * This is for the dialog box that allows the user to choose:
 * Title
 * Author
 * Tempo
 * etc.
 * of the composition
 */

public class CompositionPropertiesDialog implements Initializable{

    /* Instance Variables and FXML Components */
    @FXML TextField titleField;
    @FXML TextField composerField;

    private static MainWindow mainWindow;
    private static Stage stage;

    private ScoreInfo scoreInfo;

    public static void setStage(Stage st){stage = st;}
    public static void setMainWindow(MainWindow mw){
        mainWindow = mw;
    }

    @FXML void onOkay(ActionEvent event){
        stage.close();
    }

    @FXML void onCancel(ActionEvent event){
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        scoreInfo = mainWindow.getContent().getComposition().getCurrentScore().info;
        titleField.setText(scoreInfo.getTitle());
        composerField.setText(scoreInfo.getComposer());
    }
}
