package impromptune_gui;

import Renderer.MainWindow;
import com.xenoage.zong.commands.desktop.dialog.AudioSettingsDialogShow;
import com.xenoage.zong.commands.player.convert.DirToMidiConvert;
import com.xenoage.zong.commands.player.convert.FileToMidiConvert;
import com.xenoage.zong.desktop.utils.JseZongPlatformUtils;
import com.xenoage.zong.gui.PlayerFrame;
import com.xenoage.zong.player.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ScrollPane;
import java.net.URL;
import java.util.ResourceBundle;
import com.xenoage.utils.jse.javafx.Dialog;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.controlsfx.dialog.Dialogs;
import piano.PianoHolder;

import static com.xenoage.zong.desktop.App.app;

/**
 * Created by Sean on 2/27/2015.
 */
public class ImpromptuneInitializer implements Initializable{

    @FXML Parent root;
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
    MainWindow mainWindow;
    public static final String appName = "Impromptune";

    Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {

            stage = Impromptune.getStage();

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

            ph.mw = mw; //Set the current piano window to current renderer window
            mainWindow = mw;

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

    @FXML void onZIN(ActionEvent event) {
        mainWindow.setZoom(mainWindow.getZoom()-.25f);
    }

    @FXML void onZOUT(ActionEvent event) {
        mainWindow.setZoom(mainWindow.getZoom() + .25f);
    }

    @FXML void onABOUT(ActionEvent event) {
        showMessageDialog("Impromptune Version Sprint 1\n" +
                "This product would have been endorsed by Ludwig von Beethoven, but...\n" +
                "someone keeps pushing workspace.xml and .iml files and messing up the build, so he didn't.");
    }

    @FXML void onOpen(ActionEvent event) {
           // content.loadNextScore();
        }


    @FXML void onREM(ActionEvent event) {
        mainWindow.getContent().undo();
    }

    public void showMessageDialog(String message) {
        dialog().message(message).showInformation();
    }

    private Dialogs dialog() {
        return Dialogs.create().title(appName).styleClass(org.controlsfx.dialog.Dialog.STYLE_CLASS_NATIVE);
    }

    @FXML void openCompositionSettings(ActionEvent event){}

    /**
     * Necessary Ported Methods from the Player MenuBar
     */
    @FXML void onSettingsAudio(ActionEvent event) {app().execute(new AudioSettingsDialogShow(stage)); }

    @FXML void onConvertFileToMidi(ActionEvent event) {
        app().execute(new FileToMidiConvert(stage));
    }

    @FXML void onConvertDirToMidi(ActionEvent event) {
        app().execute(new DirToMidiConvert(stage));
    }
}
