package Renderer;


import impromptune_gui.ImpromptuneInitializer;
import io_handler.ScoreMXMLBuilder;

import com.xenoage.zong.layout.Layout;
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

    public int noteCounter = 0;

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

    public void prevPage() {
        if(pageIndex > 0 )
            pageIndex--;
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
       // content.loadBlank();
	}


    //Handles saving file as musicXML
    public void saveAs(File outFile) {
        if(outFile == null)
            return;
        ScoreMXMLBuilder mxlBuilder = new ScoreMXMLBuilder(content.getSD(), outFile);
    }

    public void undo()
    {
        if(content.undoAction() == 1) {
            ImpromptuneInitializer.UNDO.setDisable(false);
            ImpromptuneInitializer.REDO.setDisable(false);
            noteCounter--;
        }
        else
            ImpromptuneInitializer.UNDO.setDisable(true);
    }


    public void redo() {
        if(content.redoAction() == 1){
            ImpromptuneInitializer.REDO.setDisable(false);
            ImpromptuneInitializer.UNDO.setDisable(false);
            noteCounter++;
        }
        else
            ImpromptuneInitializer.REDO.setDisable(true);
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

//ORIGINAL SAVING OCODE
//        //Get current ScoreDoc->Score->MetaData(hashmap)->mxldoc object
//        MxlScorePartwise scoreOut = (MxlScorePartwise) content.getSD().getScore().getMetaData().get("mxldoc");
//
//        //YOU SUCK LOMBOK HOW DARE YOU TEMPT ME TO USE YOU
//        MusicXMLDocument newXML = new MusicXMLDocument(scoreOut);
//
//        try {
////            JseOutputStream oStream = new JseOutputStream(outFile);
////            JseXmlWriter xmlWrite = new JseXmlWriter(oStream);
////            newXML.write(xmlWrite);
//            ScoreMXLBuilder mxlBuilder = new ScoreMXLBuilder(content.getSD());
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }