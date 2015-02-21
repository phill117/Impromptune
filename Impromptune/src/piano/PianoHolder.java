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
import javafx.scene.control.Slider;
import javafx.scene.effect.Effect;
import javafx.scene.effect.SepiaTone;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.jfugue.Player;

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

        ObservableList<Node> keys = pianoPane.getChildren();
        for(Node key : keys){
            key.setOnMousePressed(this);
            key.setOnMouseReleased(this);
        }
    }

    @Override
    public void handle(MouseEvent event) {

        System.out.println(event.getEventType());

        EventType<MouseEvent> et = (EventType<MouseEvent>) event.getEventType();

        if(et == MouseEvent.MOUSE_PRESSED){
            pianoKey = (Node) event.getTarget();
            String id = pianoKey.getId();

            if(id.charAt(1) == 'r')
                pianoKey.setEffect(new SepiaTone(0.8));
            else {
                blackKeyEffect = pianoKey.getEffect();
                pianoKey.setEffect(new SepiaTone(0.8));
            }

            Integer register = new Double(registerSlider.getValue()).intValue();

            String noteToPlay = ""+id.charAt(0);
            if(id.charAt(1) == 's') noteToPlay += '#';
            char keyReg = id.charAt(2);

            switch (keyReg){
                case '1': noteToPlay += register; break;
                case '2': noteToPlay += (register+1); break;
                case '3': noteToPlay += (register+2); break;
                default: break;
            }

            noteToPlay += "h";

            final String musicString = noteToPlay;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    player.play(musicString);
                }
            }).start();
        }

        if(et == MouseEvent.MOUSE_RELEASED){
            if(pianoKey.getId().charAt(1) == 'r') pianoKey.setEffect(null);
            else pianoKey.setEffect(blackKeyEffect);
        }
    }
}
