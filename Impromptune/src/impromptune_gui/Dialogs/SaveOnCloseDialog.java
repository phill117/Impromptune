package impromptune_gui.Dialogs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

/**
 * Created by cdoak_000 on 4/9/2015.
 */
public class SaveOnCloseDialog {

    private static Stage stage;

    public static void setStage(Stage st){stage = st;}

    @FXML void onSave(ActionEvent e){
        SaveOnCloseLaunch.setResult("save");
        stage.close();
    }

    @FXML void onClose(ActionEvent e){
        SaveOnCloseLaunch.setResult("close");
        stage.close();
    }

    @FXML void onCancel(ActionEvent e){
        SaveOnCloseLaunch.setResult("cancel");
        stage.close();
    }
}
