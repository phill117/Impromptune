package Renderer;

import com.xenoage.utils.error.Err;
import com.xenoage.utils.exceptions.InvalidFormatException;
import com.xenoage.utils.log.Report;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.format.LayoutFormat;
import com.xenoage.zong.core.format.PageFormat;
import com.xenoage.zong.core.position.MP;
import Renderer.MainWindow;
import Renderer.Playback;
import com.xenoage.zong.desktop.io.ScoreDocIO;
import com.xenoage.zong.desktop.io.musicxml.in.MusicXmlScoreDocFileInput;
import com.xenoage.zong.documents.ScoreDoc;
import com.xenoage.zong.io.midi.out.PlaybackListener;
import com.xenoage.zong.io.musiclayout.LayoutSettingsReader;
import com.xenoage.zong.io.musicxml.in.readers.LayoutFormatReader;
import com.xenoage.zong.layout.Layout;
import com.xenoage.zong.layout.LayoutDefaults;
import com.xenoage.zong.layout.Page;
import com.xenoage.zong.layout.frames.ScoreFrame;
import com.xenoage.zong.layout.frames.ScoreFrameChain;
import com.xenoage.zong.musiclayout.ScoreLayout;
import com.xenoage.zong.musiclayout.layouter.PlaybackLayouter;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouter;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.symbols.SymbolPool;
import com.xenoage.zong.utils.demo.ScoreRevolutionary;
import io_handler.Composition;
import sun.misc.Unsafe;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

import static com.xenoage.utils.jse.JsePlatformUtils.jsePlatformUtils;
import static com.xenoage.zong.util.ZongPlatformUtils.zongPlatformUtils;

/**
 * The loaded document, its layout and playback capabilities.
 * 
 * @author Andreas Wenger
 */
public class Content
	implements PlaybackListener {
	
	private MainWindow mainWindow;

	private int scoreIndex = 0;
	private ScoreDoc scoreDoc = null;
	private Layout layout = null;
	private PlaybackLayouter playbackLayouter = null;

    private Composition comp = null;
    private int undoIndex = 0;
    private int addIndex = 0;
    private int maxIndex = 0;
    private LinkedList<Composition> undoList = new LinkedList<Composition>();

    boolean canUndo = false;
    boolean canRedo = false;
    public boolean canSave = false;
    private Composition blankComp;

	public Content(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
        //listen for playback events (see method playbackAtMP)
        Playback.registerListener(this);
	}


    private void addAction() {
        try {

            if(maxIndex > addIndex  || canUndo == false) {
             //   System.out.println("Set:" + maxIndex  + ":" + addIndex);
                undoList.set(addIndex, comp.deepCopy());
            }
            else {
              //  System.out.println("Add:" + maxIndex);
                maxIndex++;
                undoList.addLast(comp.deepCopy());
            }

            undoIndex = addIndex;
            addIndex++;
           // System.out.println("Finished:" + addIndex + ":" + undoIndex);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public int undoAction(){

        if(!canUndo || addIndex == 1)
        { //System.out.println("No undo:" + addIndex + ":" + undoIndex);
            return 0;}

        canRedo = true;
        if(undoIndex == 1){

            canUndo = false;

            try {
                comp = blankComp.deepCopy();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            addIndex = 1;
          //  System.out.println("Blank:" + addIndex + ":" + undoIndex);
            comp.resync();
            refresh();
            return 1;
        }

        addIndex = undoIndex;
        undoIndex--;
        try {
            comp = undoList.get(undoIndex).deepCopy();
            comp.resync();
            refresh();
            return 1;
         }
         catch (Exception e)
        {
            e.printStackTrace();
            return 0;
        }
       // System.out.println("Undo:" + addIndex + ":" + undoIndex);

    }


    public int redoAction(){
        if(undoIndex > addIndex || !canRedo)
        {  //System.out.println("NO redo" + addIndex + ":" + undoIndex);
            return 0;}

       if(canUndo == false) {

           canUndo = true;
           undoIndex = 1;
           addIndex = 2;
       }
        else
       {
           addIndex++;
           undoIndex++;
       }

        try {
            comp = undoList.get(undoIndex).deepCopy();
            comp.resync();
            refresh();
            return 1;

        }catch (Exception e){
            addIndex--;
            undoIndex--;
            return 0;
        }

       // System.out.println("redo" + addIndex + ":" + undoIndex);
    }




	/**
	 * Loads the next MusicXML demo score from the scores directory.
	 */
	public void loadNextScore() {
		File[] files = new File("scores").listFiles((d, n) -> n.endsWith(".xml"));
		loadScore("scores/" + files[scoreIndex].getName());
		scoreIndex = (scoreIndex + 1) % files.length;
	}

    public void loadBlank() {
        comp = new Composition();
        scoreDoc = comp.getCurrentScoreDoc();
        undoList.clear();
        undoIndex = 0;
        addIndex = 1;
        maxIndex = 1;
        comp.resync();
        refresh();

        try {
            blankComp = comp.deepCopy();
            undoList.add(comp.deepCopy());
        }
        catch   (Exception e)
        {
            System.out.print("deadbeef");
        }
    }



    public void loadNew(String clef, String key, String keyType, String keyMod, String Time, int bpm, String title, String creator) {
        comp = new Composition(clef, key, keyType, keyMod, Time, bpm, title,creator);
        scoreDoc = comp.getCurrentScoreDoc();
        undoList.clear();
        undoIndex = 0;
        addIndex = 1;
        maxIndex = 1;
        comp.resync();
        refresh();
        canSave = false;

        try {
            blankComp = comp.deepCopy();
            undoList.add(comp.deepCopy());
        }
        catch   (Exception e)
        {
            System.out.print("deadbeef");
        }
    }


    public void refresh(){
        layout = comp.getLayout();
        scoreDoc = comp.getCurrentScoreDoc();
       // layout.updateScoreLayouts(comp.getCurrentScore());
        //Sets up the blue playback cursor
        playbackLayouter = new PlaybackLayouter(layout.getScoreFrameChain(comp.getCurrentScore()).getScoreLayout());
        mainWindow.renderLayout(layout);

        //load score into MIDI playback
        Playback.openScore(comp.getCurrentScore());
    }


    //Called by eventhandler
    public void addNote(String note) {
       // comp.
        comp.addNote(note);
        comp.resync();
        refresh();
        canUndo = true;
        canRedo = false;
        addAction();
        canSave = true;


    }

    //Called by eventhandler
    public void addRest(char rest) {
        // comp.
        comp.addRest(rest);
        comp.resync();
        refresh();
        canUndo = true;
        canRedo = false;
        addAction();
        canSave = true;

    }



	/**
	 * Loads the MusicXML score from the given file path.
	 */
	public void loadScore(String filePath) {
		try {
			//stop current playback
			Playback.stop();
            //load the score
            comp = new Composition(filePath);

            undoList.clear();
            undoIndex = 0;
            addIndex = 1;
            maxIndex = 1;
            comp.resync();
           refresh();

            try {
                blankComp = comp.deepCopy();
                undoList.add(comp.deepCopy());
            }
            catch   (Exception e)
            {
                e.getStackTrace();
            }


		}	catch (Exception ex) {
			    Err.handle(Report.error(ex));
		}
	}

    /*///JACOB CUSTOM
    public ScoreDoc test(Score score)
            throws InvalidFormatException, IOException
    {

        //page format
        LayoutFormat layoutFormat = null;
        Object oLayoutFormat = score.getMetaData().get("layoutformat");
        if (oLayoutFormat instanceof LayoutFormat) {
            layoutFormat = (LayoutFormat) oLayoutFormat;
        }

        //use default symbol pool
        SymbolPool symbolPool = zongPlatformUtils().getSymbolPool();

        //load layout settings - TIDY: do not reload each time when a score is loaded
        LayoutSettings layoutSettings = LayoutSettingsReader.read(jsePlatformUtils().openFile(
                "data/layout/default.xml"));

        //create layout defaults
        LayoutDefaults layoutDefaults = new LayoutDefaults(layoutFormat, symbolPool, layoutSettings);

        //create the document
        ScoreDoc ret = new ScoreDoc(score, layoutDefaults);
        Layout layout = ret.getLayout();

        //layout basics
        PageFormat pageFormat = layoutFormat.getPageFormat(0);
        Size2f frameSize = new Size2f(pageFormat.getUseableWidth(), pageFormat.getUseableHeight());
        Point2f framePos = new Point2f(pageFormat.getMargins().getLeft() + frameSize.width / 2,
                pageFormat.getMargins().getTop() + frameSize.height / 2);

        //layout the score to find out the needed space
        ScoreLayouter layouter = new ScoreLayouter(score, symbolPool, layoutSettings, true, frameSize);
        ScoreLayout scoreLayout = layouter.createScoreLayout();

        //create and fill at least one page
        ScoreFrameChain chain = null;
        for (int i = 0; i < scoreLayout.frames.size(); i++) {
            Page page = new Page(pageFormat);
            layout.addPage(page);
            ScoreFrame frame = new ScoreFrame();
            frame.setPosition(framePos);
            frame.setSize(frameSize);
            //TEST frame = frame.withHFill(NoHorizontalSystemFillingStrategy.getInstance());
            page.addFrame(frame);
            if (chain == null) {
                chain = new ScoreFrameChain(score);
                chain.setScoreLayout(scoreLayout);
            }
            chain.add(frame);
        }

        return ret;
    }








    //LOAD FROM REVOLUTIONARY FILE
    public void loadScore() {
        try {
            //stop current playback
            Playback.stop();

            Score test = new ScoreRevolutionary().createScore();

            LayoutFormat layoutFormat = new LayoutFormatReader(
                    null, test.format.getInterlineSpace() / 10).read();

            test.setMetaData("layoutformat", layoutFormat); //TIDY

            ScoreDoc test2 = test(test);

            layout = test2.getLayout();

            Score score = test2.getScore();
            layout.updateScoreLayouts(score);
            //create playback layouter for the playback cursor
            playbackLayouter = new PlaybackLayouter(layout.getScoreFrameChain(score).getScoreLayout());
            //set image to view
            mainWindow.renderLayout(layout);
            //load score into MIDI playback
            Playback.openScore(test2.getScore());

        }
        catch (Exception ex) {
            Err.handle(Report.error(ex));
        }
    }*/


    public ScoreDoc getSD(){
        return scoreDoc;
    }
    public Layout  getLayout(){return layout;}

    public Composition getComposition(){return comp;}

	/**
	 * This method is called by the MIDI playback whenever a new
	 * musical position is reached.
	 */
	@Override public void playbackAtMP(MP mp, long ms) {
		//update cursor position and redraw the layout
		playbackLayouter.setCursorAt(mp);
		mainWindow.renderLayout(layout);
	}

	@Override public void playbackAtMs(long ms) {
	}

	@Override public void playbackStarted() {
	}

	@Override public void playbackPaused() {
	}

	@Override public void playbackStopped() {
	}

	@Override public void playbackAtEnd() {
	}

}
