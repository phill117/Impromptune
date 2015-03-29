package Renderer;


import Renderer.Content;
import Renderer.Playback;
import com.xenoage.utils.jse.io.JseFile;
import com.xenoage.utils.jse.io.JseOutputStream;
import com.xenoage.utils.jse.xml.JseXmlWriter;
import com.xenoage.utils.jse.xml.XMLWriter;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.musicxml.MusicXMLDocument;
import com.xenoage.zong.musicxml.types.MxlScorePartwise;
import com.xenoage.zong.renderer.javafx.JfxLayoutRenderer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import java.io.File;



/**
 * Controller for the JavaFX main window (MainWindow.fxml).
 * 
 * @author Andreas Wenger
 */
public class MainWindow {

	//GUI elements
	@FXML private BorderPane pnlCanvas;
	@FXML private ImageView scoreView;

	//loaded content
	private Content content = new Content(this);
	private WritableImage scoreImage = null;

    public Content getContent() { return content;}

    private float zoomFactor = 1.25f;

    public void setZoom(float z)
    {
        zoomFactor = z;
        content.refresh();
    }

    public float getZoom()
    {
        return zoomFactor;
    }

	@FXML public void initialize() {

		//content.loadNextScore();
       // content.loadScore();  // TO LOAD REVOLUTIONARY

        //Load blank composition
        content.loadBlank();
	}


    @FXML void onAddNote(ActionEvent event) {
        content.addNote("Af3h");
    }

    //Handles saving file as musicXML
    public void save(File outFile) {

        System.out.println("hi");
        //Get current ScoreDoc->Score->MetaData(hashmap)->mxldoc object
        MxlScorePartwise scoreOut = (MxlScorePartwise) content.getSD().getScore().getMetaData().get("mxldoc");

        //YOU SUCK LOMBOK HOW DARE YOU TEMPT ME TO USE YOU
        MusicXMLDocument newXML = new MusicXMLDocument(scoreOut);

        try {
            JseOutputStream oStream = new JseOutputStream(outFile);
            JseXmlWriter xmlWrite = new JseXmlWriter(oStream);
            newXML.write(xmlWrite);
        }
        catch (Exception e)
        {
            //blah
        }
    }











	public void renderLayout(Layout layout) {
		//run in JavaFX application thread
		Platform.runLater(() -> {
			scoreImage = JfxLayoutRenderer.paintToImage(layout, 0, zoomFactor);
			scoreView.setImage(scoreImage);
			scoreView.setFitWidth(scoreImage.getWidth());
			scoreView.setFitHeight(scoreImage.getHeight());
		});
	}

}
