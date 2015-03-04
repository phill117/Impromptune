package Renderer;

//import java.awt.image.BufferedImage;


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
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

//import com.xenoage.zong.renderer.awt.AwtLayoutRenderer;


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

	@FXML public void initialize() {

		//content.loadNextScore();
       // content.loadScore();  // TO LOAD REVOLUTIONARY

        //Load blank composition
        content.loadBlank();
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
