package impromptune_gui.Dialogs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * Created by cdoak_000 on 4/9/2015.
 */
public class NewOrOpenDialog {

    private static Stage stage;

    @FXML static ImageView logo;

    public static void setStage(Stage st){stage = st;}

    public void initialize(){
        /*java.io.InputStream logoFile = getClass().getResourceAsStream("Impromptune Logo Same Font (small).png");
        System.out.println(logoFile);
        logo = new ImageView(new Image(logoFile));*/
    }

    @FXML void onNew(ActionEvent e){
        NewOrOpenLaunch.setResult("new");
        stage.close();
    }

    @FXML void onExisting(ActionEvent e){
        NewOrOpenLaunch.setResult("open");
        stage.close();
    }
}
