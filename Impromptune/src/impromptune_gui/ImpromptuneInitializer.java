package impromptune_gui;

import com.xenoage.zong.gui.PlayerFrame;
import com.xenoage.zong.player.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.Enumeration;
import java.util.ResourceBundle;
import com.xenoage.utils.jse.javafx.Dialog;
/**
 * Created by Sean on 2/27/2015.
 */
public class ImpromptuneInitializer implements Initializable{

    @FXML AnchorPane PianoCase;
    @FXML AnchorPane PlayerCase;
    @FXML ToolBar NoteSelection;
    Player player = null;
    static PlayerFrame frame = null;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            PianoCase.getChildren().add(FXMLLoader.load(getClass().getClassLoader().getResource("piano/PianoHolder.fxml")));

            player = new Player();
            frame = Dialog.dialog(PlayerFrame.class, null);

            player.setPlayerController(frame);

            PlayerCase.getChildren().add(frame.getRoot());
        }catch (Exception e){
            System.out.println("WAHT HAPPENED");
            e.printStackTrace();
        }

    }

    public static PlayerFrame getFrame() {
        return frame;
    }
}
