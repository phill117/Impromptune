package piano;

import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.effect.Effect;
import javafx.scene.effect.SepiaTone;
import javafx.scene.input.MouseEvent;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.jfugue.Player;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Created by Sean on 2/19/2015.
 */
public class PianoHolder implements Initializable, EventHandler<MouseEvent>{

    private Player player;
    Node pianoKey;
    Effect blackKeyEffect;

    @FXML
    private Slider registerSlider;
    @FXML
    private Pane pianoPane;

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        // initialize your logic here: all @FXML variables will have been injected
        player = new Player();

        //assign 'this' as an event listener for mouse presses and releases for all keys
        ObservableList<Node> keys = pianoPane.getChildren();
        for(Node key : keys){
            key.setOnMousePressed(this);
            key.setOnMouseReleased(this);
        }
    }

    @Override
    public void handle(MouseEvent event) {

        //cache the event type
        EventType<MouseEvent> et = (EventType<MouseEvent>) event.getEventType();

        if(et == MouseEvent.MOUSE_PRESSED){
            pianoKey = (Node) event.getTarget();
            String id = pianoKey.getId();

            //creates a sepia effect when a key is pressed
            if(id.charAt(1) == 'r')
                pianoKey.setEffect(new SepiaTone(0.8));
            else {
                blackKeyEffect = pianoKey.getEffect();
                pianoKey.setEffect(new SepiaTone(0.8));
            }

            //stores the desired register range
            Integer register = new Double(registerSlider.getValue()).intValue();

            String noteToPlay = ""+id.charAt(0);

            //decide if note is flat or sharp
            if(id.charAt(1) == 's') noteToPlay += '#';
            //decide on note register
            char keyReg = id.charAt(2);

            switch (keyReg){
                case '1': noteToPlay += register; break;
                case '2': noteToPlay += (register+1); break;
                case '3': noteToPlay += (register+2); break;
                default: break;
            }

            //make the note a 'half note'
            noteToPlay += "h";

            //put the string into a final variable so that it can be used in a thread
            final String musicString = noteToPlay;

            //start a new thread to play the note
            new Thread(new Runnable() {
                @Override
                public void run() {
                    player.play(musicString);
                }
            }).start();
        }

        if(et == MouseEvent.MOUSE_RELEASED){
            //end the sepia effect
            if(pianoKey.getId().charAt(1) == 'r') pianoKey.setEffect(null);
            else pianoKey.setEffect(blackKeyEffect);
        }
    }
}
