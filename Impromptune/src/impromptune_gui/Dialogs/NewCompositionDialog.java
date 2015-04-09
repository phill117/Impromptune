package impromptune_gui.Dialogs;

import Renderer.MainWindow;
import com.xenoage.zong.core.info.ScoreInfo;
import data_objects.MetaData;
import impromptune_gui.ImpromptuneInitializer;
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
    @FXML ComboBox symbol;
    @FXML ComboBox tempo;

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

        if(!isValidKey()){
            ImpromptuneInitializer.showMessageDialogStat("Oh No! There are too many sharps or flats in the key you've chosen. Please choose a new one.");
            return;
        }

        MetaData metaData = MetaData.getInstance();
        if(symbol.getValue().toString().equals("♭")) metaData.setFifthType("flat");

        int bpm = Integer.parseInt(tempo.getValue().toString().substring((tempo.getValue().toString().indexOf('–')) + 1).trim());

        mainWindow.getContent().loadNew("treble",key.getValue().toString(),mode.getValue().toString(),symbol.getValue().toString(),timeSig.getValue().toString(),bpm);

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
        symbol.setValue("♮");
        tempo.setValue("Moderato – 110");

        timeSig.setItems(observableArrayList("4/4","3/4","6/8","4/2","3/2","2/2"));
        key.setItems(observableArrayList("A","B","C","D","E","F","G"));
        mode.setItems(observableArrayList("Major","Minor"));
        symbol.setItems(observableArrayList("♮", "♭", "♯"));


        tempo.setItems(observableArrayList("Larghissimo - 24",
                "Grave – 30",
                "Largo – 45",
                "Larghetto – 60",
                "Adagio – 70",
                "Andante – 80",
                "Andante moderato - 95",
                "Moderato – 110",
                "Allegretto – 120",
                "Allegro – 135",
                "Vivace – 170",
                "Presto – 185",
                "Prestissimo – 200"));
    }

    private boolean isValidKey(){
        String letter = key.getValue().toString();
        String majmin = mode.getValue().toString().toLowerCase();
        String mod    = symbol.getValue().toString();

        if(mod.equals("♮")) return true;

        if(majmin.equals("major")){
            if(mod.equals("♯")){
                if(letter.equals("G") || letter.equals("D") || letter.equals("A") ||letter.equals("E") ||letter.equals("B")) return false;
            }else{
                if(letter.equals("F")) return false;
            }
        }else{
            if(mod.equals("♯")){
                if(letter.equals("E") || letter.equals("B")) return false;
            }else{
                if(letter.equals("F") || letter.equals("C") || letter.equals("G") || letter.equals("D")) return false;
            }
        }
        return true;


    }
}
