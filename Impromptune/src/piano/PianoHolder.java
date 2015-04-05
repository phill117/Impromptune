package piano;

import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

import Renderer.MainWindow;
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
    public MainWindow mw;
    static PianoHolder pianoHolder;

    static String duration;
    static boolean dotted;
    static boolean tie;

    @FXML private Slider registerSlider;
    @FXML private Pane pianoPane;

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        // initialize your logic here: all @FXML variables will have been injected

        pianoHolder = this;

        //initialize toolbar parameters
        dotted = false;
        tie = false;
        duration = "h";

        //create the player
        player = new Player();

        //assign 'this' as an event listener for mouse presses and releases for all keys
        ObservableList<Node> keys = pianoPane.getChildren();
        for(Node key : keys){
            key.setOnMousePressed(this);
            key.setOnMouseReleased(this);
        }
    }

    public static PianoHolder getPianoHolder() { return pianoHolder; }

    public static void setDuration(String d){ duration = d;}
    public static String getDuration(){return duration;}

    public static void setDotted(boolean d){ dotted = d;}
    public static boolean getDotted(){return dotted;}
    public static void setTie(boolean t){ tie = t;}
    public static boolean getTie(){return tie;}

    public void handleRest(){
        mw.getContent().addRest(duration.charAt(0));
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

            String jNoteToPlay = ""+id.charAt(0); //SEND TO JFUGUE PLAYER
            String zongNote = ""+id.charAt(0);    //SEND TO ZONG RENDERER

            //todo : change this for when we get key retrieval
            if(false /* when the key is flat*/){
                jNoteToPlay = zongNote = ""+getNextKey(id.charAt(0));
            }

            //decide if note is flat or sharp
            if(id.charAt(1) == 's') {
                //todo : this one too
                if(false /* when the key is flat*/){
                    jNoteToPlay += 'b';
                    zongNote += 'b';
                }else {
                    jNoteToPlay += '#';
                    zongNote += '#';
                }
            } else {
                jNoteToPlay += 'n';
                zongNote += 'n';
            }

            //decide on note register
            char keyReg = id.charAt(2);

            switch (keyReg){
                case '1': jNoteToPlay += register; zongNote += register;break;
                case '2': jNoteToPlay += (register+1);zongNote += (register+1); break;
                case '3': jNoteToPlay += (register+2);zongNote += (register+2); break;
                default: break;
            }

            //make the note a 'half note'
            jNoteToPlay += "h";
            zongNote += duration;

           // System.out.println("jNote played:" + jNoteToPlay);

            mw.getContent().addNote(zongNote);

            //put the string into a final variable so that it can be used in a thread
            final String musicString = jNoteToPlay;

            //start a new thread to play the note
            Runnable play = () -> player.play(musicString);
            new Thread(play).start();
        }

        if(et == MouseEvent.MOUSE_RELEASED){
            //end the sepia effect
            if(pianoKey.getId().charAt(1) == 'r') pianoKey.setEffect(null);
            else pianoKey.setEffect(blackKeyEffect);
        }
    }

    //method used for getting flats rather than sharps
    char getNextKey(char note){
        switch (note){
            case 'A': note = 'B'; break;
            case 'B': note = 'C'; break;
            case 'C': note = 'D'; break;
            case 'D': note = 'E'; break;
            case 'E': note = 'F'; break;
            case 'F': note = 'G'; break;
            case 'G': note = 'A'; break;
        }
        return note;
    }

}
