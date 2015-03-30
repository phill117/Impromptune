package Renderer;

import com.xenoage.utils.jse.io.JseOutputStream;
import com.xenoage.utils.jse.xml.JseXmlWriter;

import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.musicxml.MusicXMLDocument;
import com.xenoage.zong.musicxml.types.MxlScorePartwise;
import com.xenoage.zong.renderer.javafx.JfxLayoutRenderer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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

    public int pageIndex = 0;
    public Content getContent() { return content;}

    public String loadedFile;
    private float zoomFactor = 1.25f;

    public void setZoom(float z)
    {
        zoomFactor = z;
        content.refresh();
    }

    public void nextPage() {
        if(content.getLayout().getPages().size()-1 > pageIndex)
            pageIndex++;
        else
            pageIndex = 0;

        renderLayout(content.getLayout());
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
    public void save(Stage s) {

        File outFile;
        if(loadedFile != null)
            outFile = new File(loadedFile);
        else
        {
            FileChooser chooser = new FileChooser();

            chooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("XML", "*.xml"),
                    new FileChooser.ExtensionFilter("MusicXML", "*.mxl")
            );

            chooser.setTitle("Save Composition as MusicXML");

            File file = chooser.showSaveDialog(s);

            saveAs(file);
            return;
        }


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


    //Change above to save-as, save will just get the current filename loaded?
    //Handles saving file as musicXML
    public void saveAs(File outFile) {

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


			scoreImage = JfxLayoutRenderer.paintToImage(layout, pageIndex, zoomFactor);
			scoreView.setImage(scoreImage);
			scoreView.setFitWidth(scoreImage.getWidth());
			scoreView.setFitHeight(scoreImage.getHeight());
		});
	}

}
