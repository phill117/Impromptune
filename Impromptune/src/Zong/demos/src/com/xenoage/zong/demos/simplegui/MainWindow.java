package com.xenoage.zong.demos.simplegui;

//import java.awt.image.BufferedImage;


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
	@FXML private MenuItem mnuPlaybackStop;
	@FXML private MenuItem mnuPlaybackStart;
	@FXML private BorderPane pnlCanvas;
	@FXML private ImageView scoreView;

	//loaded content
	private Content content = new Content(this);
	private WritableImage scoreImage = null;
	
	//rendering engine
	private boolean useJavaFX = true;
	
	
	@FXML public void initialize() {
		//load the first demo score
		content.loadNextScore();
	}

	@FXML void onFileOpen(ActionEvent event) {
		content.loadNextScore();
	}

	@FXML void onFileSaveAsPDF(ActionEvent event) {
		content.saveAs("pdf");
	}
	
	@FXML void onFileSaveAsPNG(ActionEvent event) {
		content.saveAs("png");
	}
	
	@FXML void onFileSaveAsMID(ActionEvent event) {
		content.saveAs("mid");
	}
	
	@FXML void onFileSaveAsOGG(ActionEvent event) {
		content.saveAs("ogg");
	}

	@FXML void onExit(ActionEvent event) {
		SimpleGuiDemo.exit();
	}

	@FXML void onPlaybackStart(ActionEvent event) {
		Playback.start();
	}

	@FXML void onPlaybackStop(ActionEvent event) {
		Playback.stop();
	}
	
	@FXML void onAbout(ActionEvent event) {
		showMessageDialog("This little demo app shows how to use Zong! in your own software.\n" +
			"There are much more complex use cases, but it may be helpful for the beginning.\n\n" +
			"Any questions or ideas? Contact us: info@xenoage.com");
	}
	
	public void renderLayout(Layout layout) {
		//run in JavaFX application thread
		Platform.runLater(() -> {
            System.out.println("RENDERING");
			//if (useJavaFX) {
				//JavaFX renderer
				scoreImage = JfxLayoutRenderer.paintToImage(layout, 0, 2f);
		//	}
		//	else {
				//AWT renderer
			//	BufferedImage awtImage = AwtLayoutRenderer.paintToImage(layout, 0, 2f);
		//		scoreImage = SwingFXUtils.toFXImage(awtImage, scoreImage);
		//	}

			scoreView.setImage(scoreImage);
			scoreView.setFitWidth(scoreImage.getWidth());
			scoreView.setFitHeight(scoreImage.getHeight());
		});
	}
	
	public void showMessageDialog(String message) {
		dialog().message(message).showInformation();
	}
	
	private Dialogs dialog() {
		return Dialogs.create().title(SimpleGuiDemo.appName).styleClass(Dialog.STYLE_CLASS_NATIVE);
	}

}
