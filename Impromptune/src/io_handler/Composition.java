package io_handler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Size2f;
import com.xenoage.zong.commands.core.music.ColumnElementWrite;
import com.xenoage.zong.commands.core.music.PartAdd;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.format.LayoutFormat;
import com.xenoage.zong.core.format.PageFormat;
import com.xenoage.zong.core.format.StaffLayout;
import com.xenoage.zong.core.instrument.Instrument;
import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.MeasureSide;
import com.xenoage.zong.core.music.Part;
import com.xenoage.zong.core.music.Staff;
import com.xenoage.zong.core.music.barline.Barline;
import com.xenoage.zong.core.music.barline.BarlineStyle;
import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.music.clef.ClefType;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.music.time.Time;
import com.xenoage.zong.core.music.time.TimeType;

import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.desktop.io.ScoreDocIO;

import com.xenoage.zong.desktop.io.musicxml.in.MusicXmlScoreDocFileInput;
import com.xenoage.zong.documents.ScoreDoc;
import com.xenoage.zong.io.musiclayout.LayoutSettingsReader;
import com.xenoage.zong.io.musicxml.in.readers.LayoutFormatReader;
import com.xenoage.zong.io.selection.Cursor;
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

import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.zong.core.position.MP.mp;
import static com.xenoage.zong.util.ZongPlatformUtils.zongPlatformUtils;
import static com.xenoage.utils.jse.JsePlatformUtils.jsePlatformUtils;
/**
 * Created by ben on 2/25/15.
 */
public class Composition {

    //internal zong score
    private Score currentComp = null;
    private ArrayList<Quill> quills = null;

    //for viewage
    private LayoutSettings layoutSettings = null;
    private ScoreDoc currentScoreDoc = null;
    private Layout layout = null;
    private PlaybackLayouter playbackLayouter = null;
    private LayoutDefaults layoutDefaults = null;
    private SymbolPool symbolPool = null;
    private int scoreIndex = 0;
    private int currentIndex = 0;
    private int parts = 0;

    //const
    private final float SPACING = 9;

    public Composition() {
        quills = new ArrayList<Quill>();
        currentComp = initializeEmptyScore();
        setLayoutFormat(currentComp);
        currentScoreDoc = initializeScoreDoc(currentComp);
        layout = currentScoreDoc.getLayout();
        currentComp.getCommandPerformer().addCommandListener(quills.get(0)); //first one for now
    }

    public Composition(String fileName) {
        quills = new ArrayList<Quill>();
        currentScoreDoc = initializeScoreDocFromFile(fileName);
        currentComp = currentScoreDoc.getScore();
        layout = currentScoreDoc.getLayout();
        layout.updateScoreLayouts(currentComp);
//        Part pianoPart = new Part("Piano", null, 1, alist(Instrument.defaultInstrument));
//        new PartAdd(currentComp, pianoPart, 0, null).execute();
        MP mp = getLastMeasure();
        if (mp == null) {
            System.out.println("FAILED, bad MP");
            return;
        }

        quills.add(parts++, new Quill(new Cursor(currentComp, mp, true), "Piano"));
        currentComp.getCommandPerformer().addCommandListener(quills.get(0)); //first one for now

    }

    public MP getLastMeasure() {
//
//        MP mp = MP.atMeasure();
        MP mp = MP.atVoice(0, currentComp.getMeasuresCount() - 1, 0);
        mp = mp.getWithBeat(currentComp);
//        Staff staff = currentComp.getStaff(0);
        if (currentComp.isMPExisting(mp))
            return currentComp.clipToMeasure(currentComp.getMeasuresCount(), mp);
        else {
            System.out.println("invalid MP @ measure: " + currentComp.getMeasuresCount());
            return null;
        }

    }

    public void addNote(String str) {

        quills.get(currentIndex).writeNote(str);
//        Barline barlineEnd = Barline.barline(BarlineStyle.LightHeavy);
//        new ColumnElementWrite(barlineEnd, currentComp.getColumnHeader(0), null, MeasureSide.Right).execute();
    }

    public void addRest(char r) {
        quills.get(currentIndex).writeRest(r);
    }

    public Quill getCurrentPart() {
        return quills.get(currentIndex);
    }

    public ScoreDoc getCurrentScoreDoc() {
        return currentScoreDoc;
    }

    public Score getCurrentScore() {
        return currentComp;
    }

    public void setCurrentScoreDoc(ScoreDoc scoreDoc) {
        currentScoreDoc = scoreDoc;
    }

    public void setCurrentScore(Score score) {
        currentComp = score;
    }

    public void setCurrentLayout(Layout l) {
        layout = l;
    }

    public void setLayouter(PlaybackLayouter pl) {
        playbackLayouter = pl;
    }

    public void setScoreIndex(int i) {
        scoreIndex = i;
    }

    public Layout getLayout() {
        return layout;
    }

    //Not sure what this does but we need it -- Jacob
    void setLayoutFormat(Score score) {
        LayoutFormat layoutFormat = new LayoutFormatReader(
                null, score.format.getInterlineSpace() / SPACING).read();

       score.setMetaData("layoutformat", layoutFormat); //TIDY
    }

    void loadLayout(LayoutFormat layoutFormat) {
        try {
            layoutSettings = LayoutSettingsReader.read(jsePlatformUtils().openFile(
                    "data/layout/default.xml"));

            //use default symbol pool
            symbolPool = zongPlatformUtils().getSymbolPool();

            //create layout defaults
            layoutDefaults = new LayoutDefaults(layoutFormat, symbolPool, layoutSettings);

        } catch( IOException e) { e.printStackTrace(); }
    }

    void addNewPart(String instrName, int staffSpan, Score comp) {
        Instrument instr = Instrument.defaultInstrument;
        Part part = new Part(instrName, null, staffSpan, alist(instr));
        new PartAdd(comp, part, 0, null).execute();
        quills.add(parts++, new Quill(new Cursor(currentComp, mp(1, 0, 0, _0, 0), true), instrName));
    }

    public void removeLast() {
        if(currentComp.getCommandPerformer().isUndoPossible()) {
            currentComp.getCommandPerformer().undo();
            System.out.print(("Composition: COMMAND [IS UNDOABLE]"));
        }
    }

    public void addLast() {
        if (currentComp.getCommandPerformer().isRedoPossible()) {
            currentComp.getCommandPerformer().redo();
            System.out.print(("Composition: COMMAND [IS REDOABLE]"));
        }
    }

    Score initializeEmptyScore() {
        Score currentComp = new Score();

        Instrument instr = Instrument.defaultInstrument;

        float is = currentComp.getFormat().getInterlineSpace();
        StaffLayout staffLayout = new StaffLayout(is * SPACING); // Was 9, changes distance between staves
        currentComp.getFormat().setStaffLayoutOther(staffLayout);

        Part pianoPart = new Part("Piano", null, 1, alist(instr));
        new PartAdd(currentComp, pianoPart, 0, null).execute();

        quills.add(parts++, new Quill( new Cursor(currentComp, MP.mp0, true), "Piano"));

        Quill quil1 = quills.get(0);

        quil1.writeClef("treble");
//        //C major default, C (4/4) time
        quil1.writeStaffKeySig("C", "major");
        quil1.writeTime("4/4");
        //second staff: bass clef
     //   cursorStaff2.write(new Clef(ClefType.clefBass));

//        //C major default, C (4/4) time
       // cursorStaff2.write((ColumnElement) new TraditionalKey(3, TraditionalKey.Mode.Major));
      //  cursorStaff2.write(new Time(TimeType.timeCommon));
        //end line

        return currentComp;
    }

    ScoreDoc initializeScoreDocFromFile(String filePath) {
        try {
            return ScoreDocIO.read(new File(filePath), new MusicXmlScoreDocFileInput());
        } catch(IOException e) { e.printStackTrace(); }

        return null;
    }

    ScoreDoc initializeScoreDoc(Score score) {
        //page format
        LayoutFormat layoutFormat = null;
        Object oLayoutFormat = score.getMetaData().get("layoutformat");

        if (oLayoutFormat instanceof LayoutFormat) {
            layoutFormat = (LayoutFormat) oLayoutFormat;
        }

        //load layout settings
        if (layoutSettings == null)
            loadLayout(layoutFormat);

        //create the document
        ScoreDoc scoreDoc = new ScoreDoc(score, layoutDefaults);
        Layout layout = scoreDoc.getLayout();

        //layout basics
        PageFormat pageFormat = layoutFormat.getPageFormat(0);  //Change page format default values for width of bar??
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

        return scoreDoc;
    }
}

