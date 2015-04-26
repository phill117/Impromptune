package impromptune_gui;

import Renderer.MainWindow;
import Renderer.Playback;
import com.xenoage.utils.error.Err;
import com.xenoage.utils.jse.files.RecentFiles;
import com.xenoage.utils.jse.log.DesktopLogProcessing;
import com.xenoage.utils.log.Log;
import com.xenoage.zong.commands.desktop.dialog.AudioSettingsDialogShow;
import com.xenoage.zong.commands.player.convert.DirToMidiConvert;
import com.xenoage.zong.commands.player.convert.FileToMidiConvert;
import com.xenoage.zong.desktop.utils.JseZongPlatformUtils;
import com.xenoage.zong.desktop.utils.error.GuiErrorProcessing;
import com.xenoage.zong.gui.PlayerFrame;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.player.Player;
import data_objects.MetaData;
import gen_settings.GenSettings;
import impromptune_gui.Dialogs.NewCompositionDialog;
import impromptune_gui.Dialogs.CompositionPropertiesLaunch;
import impromptune_gui.Dialogs.NewCompositionLaunch;
import impromptune_gui.Dialogs.NewOrOpenLaunch;
import io_handler.ScoreMXMLBuilder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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
import javafx.stage.WindowEvent;
import org.controlsfx.dialog.Dialogs;
import piano.PianoHolder;
import io_handler.IOHandler;
import xml_parser.MXMLDocUtils;


import static com.xenoage.zong.desktop.App.app;
import static javafx.collections.FXCollections.observableArrayList;

/**
 * Created by Sean on 2/27/2015.
 */
public class ImpromptuneInitializer implements Initializable{

    @FXML Parent root;

    @FXML AnchorPane PianoCase;
    @FXML AnchorPane PlayerCase;
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

    public static MenuItem UNDO;
    public static MenuItem REDO;

    @FXML MenuItem undo;
    @FXML MenuItem redo;
    ArrayList<ToggleButton> durationGroup;

    @FXML Menu recent;
    Player player = null;
    static PlayerFrame frame = null;

    PianoHolder piano;
    GenSettings genSettings;
    FXMLLoader fxmlLoader;
    MainWindow mainWindow; //The MainWindow of the currently selected tab.
    ArrayList<MainWindow> mainWindows = new ArrayList<MainWindow>();

    public static ImpromptuneInitializer self;

    public static final String appName = "Impromptune";

    Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        RendererTabs.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.intValue() < mainWindows.size() && mainWindows.size() > 0){
                    //System.out.println(mainWindows.size());
                    //System.out.println(newValue.intValue());
                    mainWindow = mainWindows.get(newValue.intValue());
                    piano.mw = mainWindow; //Set the current piano window to current renderer window
                    genSettings.setMainWindow(mainWindow);
                    genSettings.setStage(stage);
                    mainWindow.getContent().refresh();
                    stage.setTitle("Impromptune - " + mainWindow.getContent().getSD().getScore().getTitle() +
                            " - " + mainWindow.getContent().getSD().getScore().getCreator());
                }
            }
        });
        try {
            half.setSelected(true);
            durationGroup = new ArrayList<>();
            durationGroup.add(whole);durationGroup.add(half);durationGroup.add(quarter);
            durationGroup.add(eighth);durationGroup.add(sixteenth);durationGroup.add(thirtysecond);

            stage = Impromptune.getStage();

            //Prime recent files
            MenuItem newItem = new MenuItem("Empty");
            recent.getItems().add(newItem);

            //Piano
            fxmlLoader = new FXMLLoader();
            sp = fxmlLoader.load(getClass().getClassLoader().getResource("piano/PianoHolder.fxml").openStream());
            PianoCase.getChildren().add(sp);
            PianoHolder ph = fxmlLoader.getController();  //Get the controller object created
            piano = ph;
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
            genSettings = fxmlLoader.getController();

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
            Log.init(new DesktopLogProcessing(appName + " " + 1));
            Err.init(new GuiErrorProcessing());
            UNDO = undo;
            REDO = redo;

            newTab();
            /*
            fxmlLoader = new FXMLLoader();
            bp = fxmlLoader.load(getClass().getClassLoader().getResource("Renderer/Renderer.fxml").openStream());

           // RendererCase.getChildren().add(FXMLLoader.load(getClass().getClassLoader().getResource("Renderer/Renderer.fxml")));

            MainWindow mw = fxmlLoader.getController();

            ph.mw = mw; //Set the current piano window to current renderer window
            mainWindow = mw;
            UNDO = undo;
            REDO = redo;
            undo.setDisable(true);
            redo.setDisable(true);
            settings.setMainWindow(mainWindow);
            settings.setStage(stage);


            String newOrOpen = new NewOrOpenLaunch().getResult();
            if (newOrOpen.equals("new")){
                new NewCompositionLaunch(mainWindow, stage);
            } else {
                mainWindow.pageIndex = 0;
                String file = IOHandler.load(stage);
                if(file != null)
                {
                    mainWindow.loadedFile = file;
                    mainWindow.getContent().loadScore(file);

                }
            }*/

        self = this;

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

    public Tab getSelectedTab(){
        return RendererTabs.getSelectionModel().getSelectedItem();
    }

    public int getSelectedTabIndex(){
        for (int i = 0; i < RendererTabs.getTabs().size(); i++){
            if(RendererTabs.getTabs().get(i).isSelected())
                return i;
        }
        return -1;
    }

    /*
    * Method for adding a new tab to the RendererTabs
    */
    public void newTab(){
        fxmlLoader = new FXMLLoader();
        try {
            bp = fxmlLoader.load(getClass().getClassLoader().getResource("Renderer/Renderer.fxml").openStream());
        } catch (IOException e){
            e.printStackTrace();
        }

        try {

            String newOrOpen;

            while(true) {
                NewOrOpenLaunch launcher = new NewOrOpenLaunch();
                newOrOpen = launcher.getResult();
                if(launcher.didHitX()) return;
                if (newOrOpen.equals("new")) {

                    MainWindow mw = fxmlLoader.getController();

                    piano.mw = mw; //Set the current piano window to current renderer window
                    mainWindows.add(mw);
                    mainWindow = mw;

                    undo.setDisable(true);
                    redo.setDisable(true);
                    genSettings.setMainWindow(mainWindow);
                    genSettings.setStage(stage);

                    NewCompositionLaunch compLaunch = new NewCompositionLaunch(mainWindow, stage);
                    
                    break;
                } else {
                    MainWindow mw = fxmlLoader.getController();

                    piano.mw = mw; //Set the current piano window to current renderer window
                    mainWindows.add(mw);
                    mainWindow = mw;

                    undo.setDisable(true);
                    redo.setDisable(true);
                    genSettings.setMainWindow(mainWindow);
                    genSettings.setStage(stage);

                    mainWindow.pageIndex = 0;
                   File file;
                    file = IOHandler.load(stage);

                    if (file != null) {
                        mainWindow.loadedFile = file.getAbsolutePath();
                        RecentFiles.addRecentFile(file);
                        mainWindow.getContent().loadScore(file.getAbsolutePath());
                        break;
                    } else
                        continue;
                }
            }

            Tab add = new Tab();
            add.setContent(bp);
            RendererTabs.getTabs().add(add);
            SelectionModel sm = RendererTabs.getSelectionModel();
            sm.select(RendererTabs.getTabs().size() - 1);
            add.setText(mainWindow.getContent().getSD().getScore().getTitle());
            stage.setTitle("Impromptune - " + mainWindow.getContent().getSD().getScore().getTitle() +
                    " - " + mainWindow.getContent().getSD().getScore().getCreator());
            mainWindow.getContent().refresh();
        }catch (Exception e){
            System.out.println("dun messed up");
            e.printStackTrace();
        }
    }

    public void addGenerationToTab(File file){
        fxmlLoader = new FXMLLoader();
        try {
            bp = fxmlLoader.load(getClass().getClassLoader().getResource("Renderer/Renderer.fxml").openStream());
        } catch (IOException e){
            e.printStackTrace();
        }
        
        Tab add = new Tab();
        add.setContent(bp);
        RendererTabs.getTabs().add(add);
        SelectionModel sm = RendererTabs.getSelectionModel();
        sm.select(RendererTabs.getTabs().size() - 1);

        MainWindow mw = fxmlLoader.getController();

        piano.mw = mw; //Set the current piano window to current renderer window
        mainWindows.add(mw);
        mainWindow = mw;

        undo.setDisable(true);
        redo.setDisable(true);
        genSettings.setMainWindow(mainWindow);
        genSettings.setStage(stage);

        mainWindow.pageIndex = 0;
        mainWindow.loadedFile = file.toString();
        mainWindow.getContent().loadScore(mainWindow.loadedFile);

        add.setText(MetaData.getInstance().getTitle() + "*");
        stage.setTitle("Impromptune - " + MetaData.getInstance().getTitle() +
                " - " + MetaData.getInstance().getComposer());
        mainWindow.getContent().refresh();

    }

    public static PlayerFrame getFrame() {
        return frame;
    }

    @FXML void onExit(ActionEvent event) {
        System.exit(1);
    }

    int zoomCounter = 0;
    @FXML void onZIN(ActionEvent event) {
        if(zoomCounter <= 3) {
            mainWindow.setZoom(mainWindow.getZoom() - .25f);
            zoomCounter++;
        }
    }
    @FXML void onZOUT(ActionEvent event) {
        if(zoomCounter >= -3){
            mainWindow.setZoom(mainWindow.getZoom() + .25f);
            zoomCounter--;
        }
    }

    @FXML void onABOUT(ActionEvent event) {
        showMessageDialog("Impromptune Version Sprint 3\n" +
                "This product would have been endorsed by Ludwig von Beethoven, but...\n" +
                "Flash hacks!");
    }

    @FXML void onOpen(ActionEvent event) {
        System.out.println("OnOPEN");
        mainWindow.pageIndex = 0;
        File open = IOHandler.load(stage);
        String file = open.getAbsolutePath();
        if(file != null)
        {
            mainWindow.loadedFile = file;
            mainWindow.getContent().loadScore(file);
            RecentFiles.addRecentFile(open);
            RendererTabs.getSelectionModel().getSelectedItem().setText(new MXMLDocUtils().getPieceTitle(file));
        }
    }

    public void openFile(File file){
        mainWindow.pageIndex = 0;
        String f = file.getAbsolutePath();
        if(f != null)
        {
            mainWindow.loadedFile = f;
            mainWindow.getContent().loadScore(f);
            RendererTabs.getSelectionModel().getSelectedItem().setText(new MXMLDocUtils().getPieceTitle(f));
        }
    }



    @FXML void onNEW(ActionEvent event) {
        new NewCompositionLaunch(mainWindow, stage);
        mainWindow.loadedFile = null;
        mainWindow.pageIndex = 0;
    }

    @FXML void onNext(ActionEvent event) {
        mainWindow.nextPage();
    }

    @FXML void onNewTab(ActionEvent event) {
        newTab();
    }

    @FXML void onCloseTab(ActionEvent event){
        if (!mainWindows.isEmpty() && !RendererTabs.getTabs().isEmpty()) {
            mainWindows.remove(mainWindow);
            if (!mainWindows.isEmpty()) mainWindow = mainWindows.get(0);
            RendererTabs.getTabs().remove(getSelectedTab());
            RendererTabs.getSelectionModel().select(0);
            piano.mw = mainWindow; //Set the current piano window to current renderer window
            genSettings.setMainWindow(mainWindow);
            genSettings.setStage(stage);
            mainWindow.getContent().refresh();
            stage.setTitle("Impromptune - " + mainWindow.getContent().getSD().getScore().getTitle());
        }
    }

    @FXML void onSAVEAS(ActionEvent event) {
        System.out.println("OnSAVEAS");
        FileChooser chooser = new FileChooser();

        File custom = new File(".");
        chooser.setInitialDirectory(custom);


        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML", "*.xml"),
                new FileChooser.ExtensionFilter("MusicXML", "*.mxl")
        );

        chooser.setTitle("Save Composition as MusicXML");

        File file = chooser.showSaveDialog(stage);

        if(file != null) {
            mainWindow.saveAs(file);
            mainWindow.loadedFile = file.toString();

        }
    }

    @FXML void onSAVE(ActionEvent event) {
        System.out.println("OnSAVE");

        if(!mainWindow.getContent().canSave)
            return;

        if(mainWindow.loadedFile != null)
        {
            System.out.println(" NOT NULL SAVE");
            File outFile;
            outFile = new File(mainWindow.loadedFile);
            ScoreMXMLBuilder mxlBuilder = new ScoreMXMLBuilder(mainWindow.getContent().getSD(), outFile);
        }
        else
        {
            System.out.println("NULL SAVE");
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
    }


    @FXML void onRecent(Event event){
        recent.getItems().removeAll(); //Clear it out
        recent.getItems().clear();
        for( File f : RecentFiles.getRecentFiles())
        {
            MenuItem rf = new MenuItem(f.toString());

            rf.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    openFile(f);
                }
            });
            recent.getItems().add(rf);
        }
    }

    @FXML void onUndo(ActionEvent event) {
        mainWindow.undo();
       //if(mainWindow.undo() == 0);
          //  undo.setDisable(true);

    }
    @FXML void onRedo(ActionEvent event) {
        mainWindow.redo();
       // if(mainWindow.redo() == 0)
          //  redo.setDisable(true);
    }

    @FXML void onPRINT(ActionEvent event) {
        Layout layout = mainWindow.getContent().getSD().getLayout();
        if (layout == null)
            System.out.println("PRINT -- Score.getLayout() FAILED");
        IOHandler.print(layout);
    }

    public static void showMessageDialogStat(String message){
        Dialogs.create().title(appName).styleClass(org.controlsfx.dialog.Dialog.STYLE_CLASS_NATIVE).message(message).showInformation();
    }

    public void showMessageDialog(String message) {
        dialog().message(message).showInformation();
    }

    private Dialogs dialog() {
        return Dialogs.create().title(appName).styleClass(org.controlsfx.dialog.Dialog.STYLE_CLASS_NATIVE);
    }

    @FXML void openCompositionSettings(ActionEvent event){
        new CompositionPropertiesLaunch(mainWindow);
        getSelectedTab().setText(mainWindow.getContent().getSD().getScore().getTitle());
        stage.setTitle("Impromptune - " + mainWindow.getContent().getSD().getScore().getTitle() +
                " - " + mainWindow.getContent().getSD().getScore().getCreator());
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
