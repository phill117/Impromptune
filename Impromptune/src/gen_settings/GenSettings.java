package gen_settings;

import Renderer.MainWindow;
import data_objects.MetaData;
import data_objects.Note;
import impromptune_gui.ImpromptuneInitializer;
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
import jdk.internal.org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
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

    static String partwisePath;
    static String commonmodPath;

    int repetitiveness;
    int order;
    int voices;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        repetitiveness = Double.valueOf(repetitiveness_scroll.getValue()).intValue();
        order = Double.valueOf(order_scroll.getValue()).intValue();
        voices = Double.valueOf(voices_scroll.getValue()).intValue();

        //store the path of the partwise dtd
        partwisePath = getClass().getResource("dtds/partwise.dtd").getPath();
        partwisePath = getClass().getResource("dtds/common.mod").getPath();
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

        //set the number of voices for the new piece
        int parts = new Double(voices_scroll.getValue()).intValue() + 1;
        MetaData.getInstance().setParts(parts);

        //disable the generation button
        generate_btn.setDisable(true);

        //make xml parser
        SAXParser mxp;
        MXMLWriter mxmlWriter = new MXMLWriter();
        XMLReader reader;
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
//            factory.setNamespaceAware(true);
//            factory.setValidating(true);
            mxp = factory.newSAXParser();
            reader = mxp.getXMLReader();
            reader.setEntityResolver(new DTDEntityResolver());
        }catch(Exception e){
            System.out.println("Could not make parser");
            e.printStackTrace();
            return;
        }

        //save current file to xml
        FileChooser chooser = new FileChooser();

        File custom = new File(".");
        chooser.setInitialDirectory(custom);

        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML", "*.xml"),
                new FileChooser.ExtensionFilter("MusicXML", "*.mxl")
        );
        chooser.setTitle("Save Composition as MusicXML");
        File file = chooser.showSaveDialog(stage);

        if(file == null)
        {
            generate_btn.setDisable(false);
            return;
        }
        generate_btn.setText("Generating...");
        //We don't want to save yet do we?? -- Jacob, just use dialog box to get the actual filename to save to
        mainWindow.saveAs(file);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    //read the file (that is now xml)
                    System.out.println("started");
                    DefaultHandler handler = new MXMLContentHandler();
                    InputSource inputSource = new InputSource(new FileReader((file)));
                    //      THIS IS A TEMP INPUT SOURCE
                    //mxp.parse(inputSource, handler);
                    reader.setContentHandler(handler);
                    reader.parse(inputSource);

                    VirtuosoAgent agent = new VirtuosoAgent(file,mainWindow.getContent().getComposition().getFifthtype(), Double.valueOf(order_scroll.getValue()).intValue(), Double.valueOf(voices_scroll.getValue()).intValue());

                    agent.buildChordProgression();
                    agent.addBackToMusic(agent.getGeneratedTones());
                    //create a new xml file from the written data structures
                    File createdFile = mxmlWriter.createMXML( file );
                    Platform.runLater(() -> ImpromptuneInitializer.self.addGenerationToTab(createdFile));
                    //load file back to screen

                } catch (Exception e) {
                    System.out.println("IO");
                    e.printStackTrace();
                }finally {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            generate_btn.setDisable(false);
                            generate_btn.setText("Generate");
                        }
                    });

                }
            }
        }).start();
    }

    public static class DTDEntityResolver implements org.xml.sax.EntityResolver{
        @Override
        public org.xml.sax.InputSource resolveEntity(String publicId, String systemId) throws org.xml.sax.SAXException, IOException {
            System.out.println("SYSTEMID in RESOLVER "+systemId);
            System.out.println("publicID in RESOLVER "+publicId);
            if (systemId.equals("http://www.musicxml.org/dtds/partwise.dtd")) {
                // return a special input source
                FileReader reader = new FileReader(GenSettings.partwisePath);
                return new org.xml.sax.InputSource(reader);
            }else{
                // use the default behaviour
                return new org.xml.sax.InputSource(systemId);
            }
        }
    }
}
