package Renderer;


import Renderer.Content;
import Renderer.Playback;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.renderer.javafx.JfxLayoutRenderer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;




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


	@FXML public void initialize() {

		//content.loadNextScore();
       // content.loadScore();  // TO LOAD REVOLUTIONARY

        //Load blank composition
        content.loadBlank();
	}


    @FXML void onAddNote(ActionEvent event) {
        content.addNote("Af3h");
    }

	public void renderLayout(Layout layout) {
		//run in JavaFX application thread
		Platform.runLater(() -> {
			scoreImage = JfxLayoutRenderer.paintToImage(layout, 0, 2f);
			scoreView.setImage(scoreImage);
			scoreView.setFitWidth(scoreImage.getWidth());
			scoreView.setFitHeight(scoreImage.getHeight());
		});
	}

}
