package impromptune_gui;

import Renderer.MainWindow;
import com.xenoage.zong.desktop.utils.JseZongPlatformUtils;
import com.xenoage.zong.gui.PlayerFrame;
import com.xenoage.zong.player.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ScrollPane;
import java.net.URL;
import java.util.ResourceBundle;
import com.xenoage.utils.jse.javafx.Dialog;
import javafx.scene.layout.BorderPane;
import piano.PianoHolder;

/**
 * Created by Sean on 2/27/2015.
 */
public class ImpromptuneInitializer implements Initializable{

    @FXML AnchorPane PianoCase;
    @FXML AnchorPane PlayerCase;
    @FXML AnchorPane RendererCase;
    @FXML ToolBar NoteSelection;
    @FXML BorderPane bp;
    @FXML ScrollPane sp;
    @FXML MenuBar Menubar;
    Player player = null;
    static PlayerFrame frame = null;

    FXMLLoader fxmlLoader;

    public static final String appName = "Impromptune";



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {

            fxmlLoader = new FXMLLoader();
            sp = fxmlLoader.load(getClass().getClassLoader().getResource("piano/PianoHolder.fxml").openStream());
            PianoCase.getChildren().add(sp);
            PianoHolder ph = (PianoHolder) fxmlLoader.getController();  //Get the controller object created


           // PianoCase.getChildren().add(FXMLLoader.load(getClass().getClassLoader().getResource("piano/PianoHolder.fxml")));
            player = new Player();
            frame = Dialog.dialog(PlayerFrame.class, null);
            player.setPlayerController(frame);
            PlayerCase.getChildren().add(frame.getRoot());

            //Renderer
            JseZongPlatformUtils.init(appName); // JUST GOTTA DO IT MAN!!!
         //   Log.init(new DesktopLogProcessing(appName + " " + appVersion));
          //  Err.init(new GuiErrorProcessing());
          //  SynthManager.init(false)
            fxmlLoader = new FXMLLoader();
            bp = fxmlLoader.load(getClass().getClassLoader().getResource("Renderer/Renderer.fxml").openStream());
            RendererCase.getChildren().add(bp);
           // RendererCase.getChildren().add(FXMLLoader.load(getClass().getClassLoader().getResource("Renderer/Renderer.fxml")));

            MainWindow mw = (MainWindow) fxmlLoader.getController();

            ph.mw = mw;

            Menubar = (((MenuBar)((BorderPane)FXMLLoader.load(getClass().getResource("MenuBarBaseLayout.fxml"))).getCenter()));

        }catch (Exception e){
            System.out.println("WAHT HAPPENED");
            e.printStackTrace();
           System.exit(1);
        }

    }

    public static PlayerFrame getFrame() {
        return frame;
    }

    @FXML void onExit(ActionEvent event) {
        System.exit(1);
    }
}
