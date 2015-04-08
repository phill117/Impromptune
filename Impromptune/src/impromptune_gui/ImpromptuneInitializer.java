package impromptune_gui;

import Renderer.MainWindow;
import com.xenoage.zong.commands.desktop.dialog.AudioSettingsDialogShow;
import com.xenoage.zong.commands.player.convert.DirToMidiConvert;
import com.xenoage.zong.commands.player.convert.FileToMidiConvert;
import com.xenoage.zong.desktop.utils.JseZongPlatformUtils;
import com.xenoage.zong.gui.PlayerFrame;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.player.Player;
import gen_settings.GenSettings;
import impromptune_gui.Dialogs.NewCompositionDialog;
import impromptune_gui.Dialogs.CompositionPropertiesLaunch;
import impromptune_gui.Dialogs.NewCompositionLaunch;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import com.xenoage.utils.jse.javafx.Dialog;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.dialog.Dialogs;
import piano.PianoHolder;
import io_handler.IOHandler;


import static com.xenoage.zong.desktop.App.app;

/**
 * Created by Sean on 2/27/2015.
 */
public class ImpromptuneInitializer implements Initializable{

    @FXML Parent root;

    @FXML AnchorPane PianoCase;
    @FXML AnchorPane PlayerCase;
    @FXML AnchorPane RendererCase;
    @FXML TabPane RendererTabs;
    @FXML AnchorPane GenSettingsCase;

    @FXML ToolBar NoteSelection;
    @FXML BorderPane bp;
    @FXML ScrollPane sp;
    @FXML MenuBar Menubar;

    @FXML ToggleButton whole;
    @FXML ToggleButton half;
    @FXML ToggleButton quarter;
    @FXML ToggleButton eighth;
    @FXML ToggleButton sixteenth;
    @FXML ToggleButton thirtysecond;
    @FXML ToggleButton tie;
    @FXML ToggleButton dot;
    @FXML Button rest;

    ArrayList<ToggleButton> durationGroup;

    Player player = null;
    static PlayerFrame frame = null;

    FXMLLoader fxmlLoader;
    MainWindow mainWindow;
    public static final String appName = "Impromptune";

    Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            half.setSelected(true);
            durationGroup = new ArrayList<>();
            durationGroup.add(whole);durationGroup.add(half);durationGroup.add(quarter);
            durationGroup.add(eighth);durationGroup.add(sixteenth);durationGroup.add(thirtysecond);

            stage = Impromptune.getStage();

            //Piano
            fxmlLoader = new FXMLLoader();
            sp = fxmlLoader.load(getClass().getClassLoader().getResource("piano/PianoHolder.fxml").openStream());
            PianoCase.getChildren().add(sp);
            PianoHolder ph = fxmlLoader.getController();  //Get the controller object created
            AnchorPane.setTopAnchor(sp, 0.0);
            AnchorPane.setBottomAnchor(sp, 0.0);
            AnchorPane.setLeftAnchor(sp, 0.0);
            AnchorPane.setRightAnchor(sp, 0.0);

            //Player
            player = new Player();
            frame = Dialog.dialog(PlayerFrame.class, null);
            player.setPlayerController(frame);
            PlayerCase.getChildren().add(frame.getRoot());
            AnchorPane.setTopAnchor(frame.getRoot(), 0.0);
            AnchorPane.setBottomAnchor(frame.getRoot(), 0.0);
            AnchorPane.setLeftAnchor(frame.getRoot(), 0.0);
            AnchorPane.setRightAnchor(frame.getRoot(), 0.0);

            //GenSettings
            fxmlLoader = new FXMLLoader();
            Node settingsDisplay = fxmlLoader.load(getClass().getClassLoader().getResource("gen_settings/GenSettings.fxml").openStream());
            GenSettingsCase.getChildren().add(settingsDisplay);
            GenSettings settings = fxmlLoader.getController();

            //Set pictures to toggle buttons
            whole.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("PlayerPictures/whole.png"), 25,25,false,false)));
            half.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("PlayerPictures/half.png"), 25,25,false,false)));
            quarter.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("PlayerPictures/quarter.png"), 25,25,false,false)));
            eighth.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("PlayerPictures/eighth.png"), 25,25,false,false)));
            sixteenth.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("PlayerPictures/sixteenth.png"), 25,25,false,false)));
            thirtysecond.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("PlayerPictures/thirtysecond.png"), 25,25,false,false)));

            rest.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("PlayerPictures/rest.png"), 25,25,false,false)));
            dot.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("PlayerPictures/dot.png"), 25,25,false,false)));
            tie.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("PlayerPictures/tie.png"), 25,25,false,false)));

            //Renderer
            JseZongPlatformUtils.init(appName); // JUST GOTTA DO IT MAN!!!
            fxmlLoader = new FXMLLoader();
            bp = fxmlLoader.load(getClass().getClassLoader().getResource("Renderer/Renderer.fxml").openStream());
            RendererCase.getChildren().add(bp);

            AnchorPane.setTopAnchor(bp, 0.0);
            AnchorPane.setBottomAnchor(bp, 0.0);
            AnchorPane.setLeftAnchor(bp, 0.0);
            AnchorPane.setRightAnchor(bp, 0.0);
           // RendererCase.getChildren().add(FXMLLoader.load(getClass().getClassLoader().getResource("Renderer/Renderer.fxml")));

            MainWindow mw = fxmlLoader.getController();

            ph.mw = mw; //Set the current piano window to current renderer window
            mainWindow = mw;

            settings.setMainWindow(mainWindow);
            settings.setStage(stage);

        }catch (IOException e){
            System.out.println("WAHT HAPPENED");
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
            System.out.println(e.getMessage());
            System.exit(1);
        }catch (Exception e){
            System.out.println("could not create player");
            e.printStackTrace();
        }

    }

    public static PlayerFrame getFrame() {
        return frame;
    }

    @FXML void onExit(ActionEvent event) {
        System.exit(1);
    }

    @FXML void onZIN(ActionEvent event) {
        mainWindow.setZoom(mainWindow.getZoom() - .25f);
    }

    @FXML void onZOUT(ActionEvent event) {
        mainWindow.setZoom(mainWindow.getZoom() + .25f);
    }

    @FXML void onABOUT(ActionEvent event) {
        showMessageDialog("Impromptune Version Sprint 2\n" +
                "This product would have been endorsed by Ludwig von Beethoven, but...\n" +
                "someone keeps pushing workspace.xml and .iml files and messing up the build, so he didn't.");
    }

    @FXML void onOpen(ActionEvent event) {
        mainWindow.pageIndex = 0;
        String file = IOHandler.load(stage);
        if(file != null)
        {
            mainWindow.loadedFile = file;
             mainWindow.getContent().loadScore(file);

        }

    }

    @FXML void onNEW(ActionEvent event) {
        new NewCompositionLaunch(mainWindow);
        mainWindow.loadedFile = null;
        mainWindow.pageIndex = 0;
            mainWindow.getContent().loadBlank();
    }

    @FXML void onNext(ActionEvent event) {
        mainWindow.nextPage();
    }

    @FXML void onNewTab(ActionEvent event) {
        new NewCompositionLaunch(mainWindow);
    }

    @FXML void onSAVEAS(ActionEvent event) {

        FileChooser chooser = new FileChooser();

        File custom = new File(".");
        chooser.setInitialDirectory(custom);


        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML", "*.xml"),
                new FileChooser.ExtensionFilter("MusicXML", "*.mxl")
        );

        chooser.setTitle("Save Composition as MusicXML");

        File file = chooser.showSaveDialog(stage);

        if(file != null)
            mainWindow.saveAs(file);
    }



    @FXML void onSAVE(ActionEvent event) {
        mainWindow.save(stage);
    }




    @FXML void onUndo(ActionEvent event) {
        mainWindow.undo();
    }
    @FXML void onRedo(ActionEvent event) {
        mainWindow.redo();
    }




    @FXML void onPRINT(ActionEvent event) {
        Layout layout = mainWindow.getContent().getSD().getLayout();
        if (layout == null)
            System.out.println("PRINT -- Score.getLayout() FAILED");
        IOHandler.print(layout);
    }

   // @FXML void onREM(ActionEvent event) {
    //    mainWindow.getContent().undo();
   // }

    public void showMessageDialog(String message) {
        dialog().message(message).showInformation();
    }

    private Dialogs dialog() {
        return Dialogs.create().title(appName).styleClass(org.controlsfx.dialog.Dialog.STYLE_CLASS_NATIVE);
    }

    @FXML void openCompositionSettings(ActionEvent event){
        new CompositionPropertiesLaunch(mainWindow);
    }

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


    /**
     * Methods to handle keyclicks and mouse press for the duration buttons
     */

    @FXML void onNumDurationPressed(KeyEvent event){
        String key = event.getText();
        //handle rest, tie, or dotted
        if(key.equals(".")){PianoHolder.setDotted(!PianoHolder.getDotted()); dot.setSelected(!dot.isSelected()); return;}
        if(key.equals("t")){PianoHolder.setTie(!PianoHolder.getTie()); tie.setSelected(!tie.isSelected()); return;}
        if(key.equals("r")){PianoHolder.getPianoHolder().handleRest(); return;}

        if(key.equals("1")){
            flipDurations("whole");
            PianoHolder.setDuration("w");
        }
        if(key.equals("2")){
            flipDurations("half");
            PianoHolder.setDuration("h");
        }
        if(key.equals("3")){
            flipDurations("quarter");
            PianoHolder.setDuration("q");
        }
        if(key.equals("4")){
            flipDurations("eighth");
            PianoHolder.setDuration("i");
        }
        if(key.equals("5")){
            flipDurations("sixteenth");
            PianoHolder.setDuration("s");
        }
        if(key.equals("6")){
            flipDurations("thirtysecond");
            PianoHolder.setDuration("t");
        }
    }

    @FXML void onRestPressed(ActionEvent event){
        PianoHolder.getPianoHolder().handleRest();
    }

    @FXML void onDurationPressed(ActionEvent event){
        ToggleButton button = (ToggleButton) event.getTarget();
        String id = button.getId();

        //handle rest, tie, or dotted
        if(id.equals("tie")){PianoHolder.setTie(!PianoHolder.getTie()); return;}
        if(id.equals("dot")){PianoHolder.setDotted(!PianoHolder.getDotted()); return;}

        //turn off all other durations
        flipDurations(id);

        if(id.equals("whole")){
            PianoHolder.setDuration("w");
        }
        if(id.equals("half")){
            PianoHolder.setDuration("h");
        }
        if(id.equals("quarter")){
            PianoHolder.setDuration("q");
        }
        if(id.equals("eighth")){
            PianoHolder.setDuration("i");
        }
        if(id.equals("sixteenth")){
            PianoHolder.setDuration("s");
        }
        if(id.equals("thirtysecond")){
            PianoHolder.setDuration("t");
        }

    }

    private void flipDurations(String id){
        for(ToggleButton toggleButton : durationGroup){
            if(toggleButton.getId().equals(id)) toggleButton.setSelected(true);
            else toggleButton.setSelected(false);
        }
    }

}
