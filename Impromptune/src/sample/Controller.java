package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

public class Controller {

    @FXML void zong(ActionEvent event) {
        showMessageDialog("Wolfgang Amadeus Mozart, if he were still alive, would have endorsed this app if Ben did not spam groupme.");
    }

    public void showMessageDialog(String message) {

        dialog().message(message).showInformation();
    }

    private Dialogs dialog() {
        return Dialogs.create().title("Zong!").styleClass(Dialog.STYLE_CLASS_NATIVE);
    }
}
