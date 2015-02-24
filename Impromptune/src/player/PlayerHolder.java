package player;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.jfugue.MusicXmlParser;
import org.jfugue.Player;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Sean on 2/21/2015.
 */
public class PlayerHolder implements Initializable, EventHandler<MouseEvent> {

    MusicStringCreater msc;
    MusicXmlParser xmlParser;
    File composition;
    Player player;

    @FXML
    Pane btnControlPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // initialize your logic here: all @FXML variables will have been injected
        player = new Player();
        msc = new MusicStringCreater();
        xmlParser = new MusicXmlParser();
        xmlParser.addParserListener(msc);

        composition = new File("res/SampleMusicXML/Chant.xml");

        for(Node node : btnControlPane.getChildren()){
            node.setOnMouseClicked(this);
            //if(node.getId().equals("play_btn"));
        }
    }

    @Override
    public void handle(MouseEvent event) {


        String btnName = ((Button)(event.getTarget())).getId();

        if(btnName.equals("play_btn")){

            xmlParser.parse(composition);
            if(player.isPaused())
                player.resume();

            System.out.println(msc.getCreatedMusicString());

            new Thread(new Runnable() {
                @Override
                public void run() {

                    player.play(msc.getCreatedMusicString());
                }
            }).start();

            return;
        }

        if(btnName.equals("pause_btn")){
            if (player.isPlaying())
                player.pause();
            return;
        }

        if(btnName.equals("stop_btn")){
            if (player.isPlaying())
                player.stop();
            return;
        }
        //player.play();
    }
}
