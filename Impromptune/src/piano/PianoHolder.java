package piano;

import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

import Renderer.MainWindow;
import impromptune_gui.ImpromptuneInitializer;
import data_objects.MetaData;
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
import javafx.scene.layout.HBox;
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
    @FXML private HBox whiteKeysCase;
    @FXML private HBox blackKeysCase;
    MetaData metaData;

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        // initialize your logic here: all @FXML variables will have been injected

        metaData = MetaData.getInstance();

        pianoHolder = this;

        //initialize toolbar parameters
        dotted = false;
        tie = false;
        duration = "h";

        //create the player
        player = new Player();

        //assign 'this' as an event listener for mouse presses and releases for white keys
        ObservableList<Node> whiteKeys = whiteKeysCase.getChildren();
        for (Node key : whiteKeys) {
            key.setOnMousePressed(this);
            key.setOnMouseReleased(this);
        }

        //assign 'this' as an event listener for mouse presses and releases for black keys
        blackKeysCase.setPickOnBounds(false);
        ObservableList<Node> blackKeys = blackKeysCase.getChildren();
        for (Node key : blackKeys) {
            if (key.isVisible()) {
                key.setOnMousePressed(this);
                key.setOnMouseReleased(this);
                key.setPickOnBounds(false);
            }
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

            String fifthType = mw.getContent().getComposition().getFifthtype();

            pianoKey = (Node) event.getTarget();
            String id = pianoKey.getId();

            //creates a sepia effect when a key is pressed
            if(id.charAt(1) == 'r')
                pianoKey.setEffect(new SepiaTone(0.8));
            else {
                blackKeyEffect = pianoKey.getEffect();
                pianoKey.setEffect(new SepiaTone(0.8));
            }

            //System.out.println("id: "+id);
            //stores the desired register range
            Integer register = new Double(registerSlider.getValue()).intValue();

            String jNoteToPlay = ""+id.charAt(0); //SEND TO JFUGUE PLAYER
            String zongNote = ""+id.charAt(0);    //SEND TO ZONG RENDERER

            if(fifthType.equals("flat") && id.charAt(1) == 's'){
                zongNote = ""+getNextKey(id.charAt(0));
            }

            //decide if note is flat or sharp
            if(id.charAt(1) == 's') {
                if(fifthType.equals("flat")) zongNote += 'f';
                else zongNote += '#';

                jNoteToPlay += '#';

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
            //System.out.println("Zong played:" + zongNote);
            mw.getContent().addNote(zongNote);
            ImpromptuneInitializer.UNDO.setDisable(false);

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
        char newNote;
        switch (note){
            default:
            case 'A': newNote = 'B'; break;
            case 'B': newNote = 'C'; break;
            case 'C': newNote = 'D'; break;
            case 'D': newNote = 'E'; break;
            case 'E': newNote = 'F'; break;
            case 'F': newNote = 'G'; break;
            case 'G': newNote = 'A'; break;
        }
        return newNote;
    }

}
