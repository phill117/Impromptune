package impromptune_gui;
/**
 * Created by doak on 2/25/2015.
 */

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.IOException;
import piano.PianoHolder;

public class Impromptune extends Application {

    private Stage primaryStage;
    private BorderPane baseLayout;
    private BorderPane editView;
    private HBox noteControls;
    private PianoHolder piano;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Impromptune");

        initBaseLayout();
        showEditView();
        initPiano();
    }

    /**
     * Initializes the base layout
     */
    public void initBaseLayout() {
        try {
            // Load base layout from fxml file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Impromptune.class.getResource("MenuBarBaseLayout.fxml"));
            baseLayout = (BorderPane) loader.load();

            // Show the scene containing the base layout
            Scene scene = new Scene(baseLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Shows the edit view in the base layout.
     */
    public void showEditView() {
        try {
            // Load edit view
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Impromptune.class.getResource("EditView.fxml"));
            editView = (BorderPane) loader.load();

            // Set edit view to center of base layout
            baseLayout.setCenter(editView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initPiano() {
        noteControls = (HBox) editView.getBottom();
        AnchorPane pianoContainer = (AnchorPane) noteControls.getChildren().get(1);
        // Eventually add piano to children of pianoContainer
    }

    public static void main(String[] args) {
        launch(args);
    }
}
