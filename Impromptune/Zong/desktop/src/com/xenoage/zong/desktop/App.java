package com.xenoage.zong.desktop;

import static com.xenoage.utils.error.Err.handle;
import static com.xenoage.utils.jse.JsePlatformUtils.io;
import static com.xenoage.utils.jse.async.Sync.sync;
import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.error;
import static com.xenoage.utils.log.Report.remark;
import static com.xenoage.utils.log.Report.warning;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.sound.midi.MidiUnavailableException;
import javax.swing.JOptionPane;

import lombok.Getter;

import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

import com.xenoage.utils.document.Document;
import com.xenoage.utils.document.command.Command;
import com.xenoage.utils.document.command.CommandPerformer;
import com.xenoage.utils.document.io.FileFormat;
import com.xenoage.utils.document.io.FileOutput;
import com.xenoage.utils.document.io.SupportedFormats;
import com.xenoage.utils.error.BasicErrorProcessing;
import com.xenoage.utils.error.Err;
import com.xenoage.utils.exceptions.InvalidFormatException;
import com.xenoage.utils.filter.Filter;
import com.xenoage.utils.io.InputStream;
import com.xenoage.utils.jse.JsePlatformUtils;
import com.xenoage.utils.jse.io.JseInputStream;
import com.xenoage.utils.jse.io.JseOutputStream;
import com.xenoage.utils.jse.javafx.JavaFXApp;
import com.xenoage.utils.jse.lang.LangManager;
import com.xenoage.utils.jse.lang.LanguageInfo;
import com.xenoage.utils.lang.Lang;
import com.xenoage.utils.lang.VocID;
import com.xenoage.utils.log.Log;
import com.xenoage.zong.Voc;
import com.xenoage.zong.Zong;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.desktop.io.midi.out.MidiScorePlayer;
import com.xenoage.zong.desktop.io.midi.out.SynthManager;
import com.xenoage.zong.desktop.utils.FilenameDialogFilter;
import com.xenoage.zong.io.musicxml.in.MusicXmlFileReader;

/**
 * Base class for all Zong! applications based on JavaFX.
 * 
 * It has a single instance and manages the main {@link Window}
 * and app-wide {@link CommandPerformer}.
 * 
 * @author Andreas Wenger
 */
public class App<DocType extends Document>
	extends JavaFXApp {

	/**
	 * Type of JavaFX app.
	 */
	public enum AppType {
		/** A desktop application that can be closed by <code>Platform.exit()</code>. */
		DesktopApp,
		/** An applet that can be displayed on a webpage. */
		Applet;
	}

	/**
	 * Number of documents that can be open at the same time.
	 */
	public enum DocumentInterface {
		/** The application can open only a single document at the same time. */
		SDI,
		/** The application can open multiple documents at the same time. */
		MDI;
	}


	//the only instance of the this class (singleton pattern)
	protected static App<?> app = null;

	/** The "first" name of the app, like "Player" for the Zong! Player. */
	@Getter protected String appFirstName = "JavaFXApp";
	/** The JavaFX GUI type of the application. */
	@Getter protected AppType appType = AppType.DesktopApp;
	/** The document interface of the application. */
	@Getter protected DocumentInterface documentInterface = DocumentInterface.SDI;
	/** The main window, used as parent for showing dialogs. */
	@Getter protected Window mainWindow = null;
	/** The invoker for app-wide commands.
	 * Do not use it for commands on documents in a multiple-document-app). */
	@Getter protected CommandPerformer commandPerformer = new CommandPerformer(null);
	/** The file formats supported by this app. */
	@Getter public SupportedFormats<DocType> supportedFormats = null;

	//the open documents
	private int activeDocumentIndex = -1;
	private ArrayList<DocType> documents = new ArrayList<>();
	
	//the program icon in different resolutions
	private List<Image> icons = new ArrayList<>();


	/**
	 * Gets the only instance of the {@link App} class.
	 * One of {@link App}'s subclasses should be instatiated before. Then
	 * that one is returned. Otherwise, a new instance of this class is created,
	 * which is useful for testing purposes.
	 */
	public static App<?> app() {
		if (app == null) {
			//create minimal app for testing purposes
			app = new App<Document>();
		}
		return app;
	}

	/**
	 * Creates a new {@link App} instance.
	 * May be useful for testing purposes, but normally subclasses of this class
	 * should be instantiated.
	 */
	protected App(String appFirstName, AppType appType, DocumentInterface documentInterface) {
		app = this;
		this.appFirstName = appFirstName;
		this.appType = appType;
		this.documentInterface = documentInterface;
		init();
	}
	
	private App() {
		init();
	}
	
	private void init() {
		//initialize JavaFXApp, platform utils, logging, error handling, language, audio and GUI
		//using the template method pattern
		JavaFXApp.init(this);
		initPlatformUtils();
		initLog();
		initErr();
		initLang();
		initAudio();
		initGUI();
	}

	/**
	 * Initializes the {@link JsePlatformUtils}.
	 */
	protected void initPlatformUtils() {
		JsePlatformUtils.init(getAppPath());
	}

	/**
	 * Initializes the logging interface.
	 * No logging is used by default.
	 */
	protected void initLog() {
		Log.initNoLog();
	}

	/**
	 * Initializes the error handling interface.
	 * Minimal error handling is used by default.
	 */
	protected void initErr() {
		Err.init(new BasicErrorProcessing());
	}

	/**
	 * Initializes the default language pack.
	 * This is the language of the system, or English if not available.
	 */
	protected void initLang() {
		//load language
		String langID = null;
		try {
			//get available languages
			List<LanguageInfo> languages = LanguageInfo.getAvailableLanguages(LangManager.defaultLangPath);
			//use system's default (TODO: config)
			langID = LanguageInfo.getDefaultID(languages);
			LangManager.loadLanguage(langID);
		} catch (Exception ex) {
			warning("Could not load language " + langID, ex);
			LangManager.loadLanguage("en");
		}
		//register tokens
		Lang.registerToken("{app.name}", getName());
	}
	
	/**
	 * Initializes the audio engine, using the {@link SynthManager}
	 * and {@link MidiScorePlayer}.
	 */
	protected void initAudio() {
		try {
			SynthManager.init(true);
			MidiScorePlayer.init();
		} catch (MidiUnavailableException ex) {
			handle(warning(Voc.MidiNotAvailable, ex));
		}
	}

	/**
	 * This method can be overriden to initialize the GUI.
	 * In the default implementation, the program icons are loaded.
	 */
	protected void initGUI() {
		for (String s : new String[]{"16", "32", "64", "128", "256", "512"}) {
			icons.add(readImage("logo" + s + ".png"));
		}
	}
	
	private Image readImage(String filename) {
		return new Image(Zong.class.getResourceAsStream("gui/img/" + filename));
	}

	/**
	 * Gets the relative path of the app. This is "{@value Zong#filename}/" +
	 * {@link #getAppFirstName()}.
	 */
	public String getAppPath() {
		return Zong.filename + "/" + appFirstName + "/";

	}

	/**
	 * Gets the name of the program, like "Zong! Player".
	 */
	@Override public String getName() {
		return Zong.getName(appFirstName);
	}

	/**
	 * Gets the name and version of the program as a String.
	 */
	public String getNameAndVersion() {
		return Zong.getNameAndVersion(appFirstName);
	}

	/**
	 * Gets the active document, or null, if there is none.
	 */
	public DocType getActiveDocument() {
		if (activeDocumentIndex == -1)
			return null;
		else
			return documents.get(activeDocumentIndex);
	}

	/**
	 * Gets the document with the given index.
	 */
	public DocType getDocument(int index) {
		return documents.get(index);
	}

	/**
	 * Activates the document with the given index.
	 * Applications should extend this method to display the
	 * selected document.
	 */
	public void setActiveDocument(int index) {
		activeDocumentChanged(index);
	}

	/**
	 * This method should be called when the selected document
	 * was changed by the user, e.g. by selecting a tab.
	 */
	public void activeDocumentChanged(int index) {
		activeDocumentIndex = index;
	}

	/**
	 * Requests to close the document with the given index.
	 * If successfull, true is returned, otherwise false.
	 */
	public boolean requestCloseDocument(int documentIndex) {
		//TODO: when last document is closed, add a "Welcome"-screen
		//or something like that
		//last document can't be closed
		if (documents.size() > 1) {
			documents.remove(documentIndex);
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Closes the app.
	 * Does only work for desktop applications.
	 */
	public void close() {
		if (appType == AppType.DesktopApp) {
			Log.close();
			Platform.exit();
			System.exit(0); //close all open threads
		}
	}
	
	/**
	 * Gets a factor for creating controlsfx dialogs.
	 * It is preconfigured with the program's name as the title and
	 * and with for native style dialogs.
	 */
	public Dialogs dialog() {
		return Dialogs.create().title(getName()).styleClass(Dialog.STYLE_CLASS_NATIVE);
	}

	/**
	 * Shows a message dialog with the given information.
	 */
	public void showMessageDialog(final String message, final String title) {
		log(remark("Message: " + message));
		dialog().message(message).showInformation();
	}

	/**
	 * Shows a message dialog with information.
	 */
	public void showMessageDialog(String message) {
		showMessageDialog(message, getNameAndVersion());
	}

	/**
	 * Opens the file at the given path and sets it as the active document.
	 */
	public void openDocument(String filePath) {
		DocType doc = openDocumentData(filePath);
		if (doc == null)
			return;
		addDocument(doc);
	}

	/**
	 * Loads the data of the file at the given path.
	 * Returns null if the file could not be loaded.
	 * Must be overridden by real applications.  
	 */
	public DocType openDocumentData(String filePath) {
		return null;
	}

	/**
	 * Adds the given document and sets it as the active document.
	 * Override this method to add custom behaviour after calling the
	 * super implementation, e.g. for starting playback after adding.
	 */
	public void addDocument(DocType doc) {
		if (documentInterface == DocumentInterface.SDI)
			documents.clear();
		documents.add(doc);
		setActiveDocument(documents.size() - 1);
	}

	/**
	 * Saves the current document at the given path in the given format.
	 */
	public void saveDocument(String filePath, FileFormat<?> format) {
		DocType doc = getActiveDocument();
		if (doc == null)
			return;
		@SuppressWarnings("unchecked") FileOutput<DocType> output =
			(FileOutput<DocType>) format.getOutput();
		try {
			if (output != null)
				output.write(doc, new JseOutputStream(new File(filePath)), filePath);
			else
				throw new IOException("No writer for format " + format.getName());
		} catch (IOException ex) {
			error(Voc.CouldNotSaveDocument, ex, filePath);
		}
	}

	/**
	 * Loads a score from the given file. XML scores,
	 * XML opera and compressed MusicXML files are supported.
	 * When a compressed MusicXML file contains an opus,
	 * the user can select a file in a small dialog. If the action is cancelled,
	 * null is returned.
	 * If an error occurs, an error dialog is shown.
	 */
	public Score loadMxlScore(String path) {
		List<Score> scores = loadMxlScores(path, new FilenameDialogFilter());
		if (scores.size() > 0)
			return scores.get(0);
		else
			return null;
	}

	/**
	 * Loads a list of scores from the given file. XML scores,
	 * XML opera and compressed MusicXML files are supported.
	 * The given filter is used to select scores, if there are
	 * more than one (like in an opus). If the action is cancelled,
	 * null is returned.
	 * If an error occurs, an error dialog is shown.
	 */
	public List<Score> loadMxlScores(String path, Filter<String> filter) {
		try {
			
			//GOON: integrate absolute path in DesktopIO
			InputStream is;
			if (new File(path).isAbsolute())
				is = new JseInputStream(new File(path));
			else
				is = io().openFile(path);
				
			return sync(new MusicXmlFileReader(is, path, filter));
		} catch (Exception ex) {
			reportOpenFileError(ex, path);
			return new LinkedList<Score>();
		}
	}

	/**
	 * Reports an error when opening a file.
	 */
	public void reportOpenFileError(Exception fileOpenError, String filePath) {
		try {
			throw fileOpenError;
		} catch (FileNotFoundException ex) {
			handle(error(Voc.OpenFileNotFound, ex, filePath));
		} catch (InvalidFormatException ex) {
			handle(error(Voc.OpenFileInvalidFormat, ex, filePath));
		} catch (IOException ex) {
			handle(error(Voc.OpenFileIOError, ex, filePath));
		} catch (SecurityException ex) {
			handle(error(Voc.SecurityError, ex, filePath));
		} catch (Exception ex) {
			handle(error(Voc.UnknownError, ex, filePath));
		}
	}
	
	/**
	 * Convenience shortcut for {@link #getCommandPerformer()}.execute(...).
	 */
	public void execute(Command command) {
		commandPerformer.execute(command);
	}
	
	/**
	 * Applies the icons of the app to the given {@link Stage}.
	 */
	@Override public void applyIcons(Stage stage) {
		stage.getIcons().clear();
		stage.getIcons().addAll(icons);
	}
	
	/**
	 * Shows a warning message using Swing, showing that this version is not usable but
	 * "work in progress".
	 */
	public static void showWorkInProgressWarning() {
		JOptionPane.showMessageDialog(null, "Warning: This version of " + Zong.projectFamilyName +
			" is \"work in progress\" any may not work " + "as expected.\n" +
			"If you need a working program, use version " + Zong.projectVersion + "" +
			Zong.projectIterationLastWorking, Zong.projectFamilyName + " " + Zong.projectVersionLong,
			JOptionPane.WARNING_MESSAGE);
	}
	
	@Override public VocID[] getVoc() {
		return Voc.values();
	}

}
