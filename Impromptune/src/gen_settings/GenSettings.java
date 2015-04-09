package gen_settings;

import Renderer.MainWindow;
import data_objects.MetaData;
import data_objects.Note;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import virtuouso.VirtuosoAgent;
import xml_parser.MXMLContentHandler;
import xml_parser.MXMLWriter;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by Sean on 3/24/2015.
 */
public class GenSettings implements Initializable, EventHandler<ActionEvent> {

    @FXML Slider repetitiveness_scroll;
    @FXML Slider order_scroll;
    @FXML Slider voices_scroll;
    @FXML Button generate_btn;

    MainWindow mainWindow;
    Stage stage;

    int repetitiveness;
    int order;
    int voices;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        repetitiveness = Double.valueOf(repetitiveness_scroll.getValue()).intValue();
        order = Double.valueOf(order_scroll.getValue()).intValue();
        voices = Double.valueOf(voices_scroll.getValue()).intValue();


    }

    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void handle(ActionEvent event) {

        //Generate some music!!
        /**
         * 1. parse the xml for the melody *DONE*
         * 2. analyze using markov models to determine base
         * 3. determine possible inner voices
         */

        //disable the generation button
        generate_btn.setDisable(true);

        //make xml parser
        SAXParser mxp;
        MXMLWriter mxmlWriter = new MXMLWriter();
        try {
            mxp = SAXParserFactory.newInstance().newSAXParser();
        }catch(Exception e){
            System.out.println("Could not make parser");
            e.printStackTrace();
            return;
        }

        //save current file to xml
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML", "*.xml"),
                new FileChooser.ExtensionFilter("MusicXML", "*.mxl")
        );
        chooser.setTitle("Save Composition as MusicXML");
        File file = chooser.showSaveDialog(stage);
        mainWindow.saveAs(file);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    //read the file (that is now xml)
                    System.out.println("started");
                    DefaultHandler handler = new MXMLContentHandler();
                    //InputSource inputSource = new InputSource(new FileReader((file)));
                    //      THIS IS A TEMP INPUT SOURCE
//                    InputSource inputSource = new InputSource(getClass().getClassLoader().getResourceAsStream("gen_settings/MozartPianoSonata.xml"));
//                    mxp.parse(inputSource, handler);

                    VirtuosoAgent agent = new VirtuosoAgent(file.getName());
                    System.out.println(agent.buildChordProgression());
                    //create a new xml file from the written data structures
                    File createdFile = mxmlWriter.createMXML( /* create and argument for a file destination*/  );

                    //load file back to screen

//                } catch (SAXException e) {
//                    System.out.println("SAX");
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    System.out.println("IO");
//                    e.printStackTrace();
                }finally {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            generate_btn.setDisable(false);
                        }
                    });

                }
            }
        }).start();
    }
}
