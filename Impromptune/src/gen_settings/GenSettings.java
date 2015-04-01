package gen_settings;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import xml_parser.MXMLContentHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Sean on 3/24/2015.
 */
public class GenSettings implements Initializable, EventHandler<ActionEvent> {

    @FXML Slider repetitiveness_scroll;
    @FXML Slider order_scroll;
    @FXML Slider voices_scroll;
    @FXML Button generate_btn;

    int repetitiveness;
    int order;
    int voices;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        repetitiveness = Double.valueOf(repetitiveness_scroll.getValue()).intValue();
        order = Double.valueOf(order_scroll.getValue()).intValue();
        voices = Double.valueOf(voices_scroll.getValue()).intValue();


    }

    @Override
    public void handle(ActionEvent event) {

        //Generate some music!!
        /**
         * 1. parse the xml for the melody *DONE*
         * 2. analyze using markov models to determine base
         * 3. determine possible inner voices
         */

        SAXParser mxp;
        try {
            mxp = SAXParserFactory.newInstance().newSAXParser();
        }catch(Exception e){
            System.out.println("Could not make parser");
            e.printStackTrace();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("started");
                    DefaultHandler handler = new MXMLContentHandler();
                    InputSource inputSource = new InputSource(getClass().getClassLoader().getResourceAsStream("gen_settings/Chant.xml"));
                    mxp.parse(inputSource, handler);
                } catch (SAXException e) {
                    System.out.println("SAX");
                    e.printStackTrace();
                } catch (IOException e) {
                    System.out.println("IO");
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
