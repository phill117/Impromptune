package impromptune_gui.Dialogs;

import Renderer.MainWindow;
import com.xenoage.zong.core.info.ScoreInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;

/**
 * Created by doak on 3/31/2015.
 * This is for the dialog box that allows the user to choose:
 * Title
 * Author
 * Tempo
 * etc.
 * of the composition
 */

public class NewCompositionDialog implements Initializable{

    /* Instance Variables and FXML Components */
    @FXML TextField titleField;
    @FXML TextField composerField;
    @FXML ComboBox timeSig;
    @FXML ComboBox key;
    @FXML ComboBox mode;


    @FXML Button OK;
   private static Stage stage;
    private static Stage parent;
    private static MainWindow mainWindow;

    public static void setMainWindow(MainWindow mw){
        mainWindow = mw;
    }

    public static void setStage(Stage s){
        stage = s;
    }
    public static void setParent(Stage s){
        parent = s;
    }

    @FXML void onOkay(ActionEvent event){
        //                                  clef, key,                      keyMode,                  keyType          Time,            bpm)
        mainWindow.getContent().loadNew("treble",key.getValue().toString(),mode.getValue().toString(),null,timeSig.getValue().toString(),200);
        //mainWindow.getContent().loadNew("treble","A","Major",null,"4/4",200);
        parent.setTitle("Impromptune - " +titleField.getText() + " - " + composerField.getText());
        stage.close();
    }

    @FXML void onCancel(ActionEvent event){

        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titleField.setText("Untitled");
        composerField.setText("Anonymous");

        timeSig.setItems(observableArrayList("4/4","3/4","6/8","4/2","3/2","2/2"));
        key.setItems(observableArrayList("A","B","C","D","E","F","G"));
        mode.setItems(observableArrayList("Major","Minor"));
    }
}
