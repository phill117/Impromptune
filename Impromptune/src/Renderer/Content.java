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

import java.io.File;
import java.io.IOException;

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


	public Content(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
        comp = new Composition(); // create blank
		//listen for playback events (see method playbackAtMP)
		Playback.registerListener(this);	
	}
	
	/**
	 * Loads the next MusicXML demo score from the scores directory.
	 */
	public void loadNextScore() {
		File[] files = new File("scores").listFiles((d, n) -> n.endsWith(".xml"));
		loadScore("scores/" + files[scoreIndex].getName());
		scoreIndex = (scoreIndex + 1) % files.length;
	}




    public void loadBlank()
    {
        layout = comp.getCurrentScoreDoc().getLayout();

        layout.updateScoreLayouts(comp.getCurrentScore());

       playbackLayouter = new PlaybackLayouter(layout.getScoreFrameChain(comp.getCurrentScore()).getScoreLayout());

        mainWindow.renderLayout(layout);

        //load score into MIDI playback
      //  Playback.openScore(comp.getCurrentScore());



    }


	/**
	 * Loads the MusicXML score from the given file path.
	 */
	private void loadScore(String filePath) {
		try {
			//stop current playback
			Playback.stop();

            //CUSTOM JACOB
            //get manual score

            //read information about the score
         //   ScoreInfo scoreInfo = new ScoreInfoReader(mxlScore.getScoreHeader()).read();
          //  test.setInfo(scoreInfo);

            //read score format
         //  MxlScoreHeader mxlScoreHeader = mxlScore.getScoreHeader();
        //    MxlDefaults mxlDefaults = mxlScoreHeader.getDefaults();
         //   ScoreFormat scoreFormat = new ScoreFormatReader(mxlDefaults).read();
            //test.setFormat(scoreFormat);

            //read layout format
         //   MxlLayout mxlLayout = (mxlDefaults != null ? mxlDefaults.getLayout() : null);


            Score test = new ScoreRevolutionary().createScore();

            ScoreDoc scoreDoc2 = ScoreDocIO.read(new File(filePath), new MusicXmlScoreDocFileInput());

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
	}

    ///JACOB CUSTOM
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

//End Jacob




	
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
