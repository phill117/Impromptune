package gen_settings;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

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
         * 1. parse the xml for the melody
         * 2. analyze using markov models to determine base
         * 3. determine possible inner voices
         */

    }
}
