package impromptune_gui.Dialogs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

/**
 * Created by cdoak_000 on 4/9/2015.
 */
public class NewOrOpenDialog {

    private static Stage stage;

    public static void setStage(Stage st){stage = st;}

    @FXML void onNew(ActionEvent e){
        NewOrOpenLaunch.setResult("new");
        stage.close();
    }

    @FXML void onExisting(ActionEvent e){
        NewOrOpenLaunch.setResult("open");
        stage.close();
    }
}
